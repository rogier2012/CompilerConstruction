package pp.s1542664.q2_3;

import pp.iloc.Assembler;
import pp.iloc.model.*;
import pp.iloc.parse.FormatException;
import pp.s1542664.q2.Graph;
import pp.s1542664.q2.Node;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ILOC2DDG {
	/** The singleton instance of this class. */
	private static final ILOC2DDG instance = new ILOC2DDG();

    private Map<Integer,Node> instrNodeMap;

	/** Returns the singleton instance of this class. */
	public static ILOC2DDG instance() {
		return instance;
	}

	/** Converts an ILOC file given as parameter and prints out the
	 * resulting CFG. 
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: filename");
		}
		try {
			Program prog = Assembler.instance().assemble(new File(args[0]));
			System.out.println(instance().convert(prog));
		} catch (FormatException | IOException exc) {
			exc.printStackTrace();
		}
	}

	/** Private constructor for the singleton instance. */
	private ILOC2DDG() {
		// empty by design
	}


    /** Converts the given program into Graph.
     *  It loop twice through the program, once to add all dependencies of the instructions and
     *  once to connect the end node to all store operations. In the first loop is a special case if its a load operation.
     */
	public Graph convert(Program prog) {
        Graph graph = new Graph();
        instrNodeMap = new HashMap<>();

        List<Instr> instrList = prog.getInstr();
        for (int i = 0; i < instrList.size(); i++) {
            for (Op operation : instrList.get(i)){
                Node node = graph.addNode(operation.getOpCode().name());
                instrNodeMap.put(operation.getLine(), node);

                if (this.loadOperation(operation) || this.popOperation(operation)){
                    graph = this.connectStorePush(graph, instrList.subList(0,i), node);
                }

                List<Operand> operands = operation.getArgs();
                int source = operation.getOpCode().getSourceCount();
                for(int j = 0; j < source; j++){
                    if (operands.get(j).getType() == Operand.Type.REG){
                        graph = this.connectDependency(graph,instrList.subList(0,i),node,operands.get(j));
                    }
                }
            }
        }
        Node endNode = graph.addNode("END");
        for (int i = instrList.size()-1; i >= 0; i--){
            for (Op operation : instrList.get(i)){
                if (storeOperation(operation) || pushOperation(operation)){
                    endNode.addEdge(getInstrNode(operation));
                }
            }
        }

        this.writeDot(graph);
        return graph;
	}



    /**
     */
    /**
     * Connects the given node to the first register that matches its name at the target side of an operation.
     * Except for a store node, which has target node who do not alter the register.
     * @param graph current graph
     * @param previousInstr previous Instructions
     * @param currentNode currentNode
     * @param oper register of the current operation.
     * @return the given graph plus a possible dependency to this currentNode.
     */
    public Graph connectDependency(Graph graph, List<Instr> previousInstr, Node currentNode, Operand oper){
        connect:
        for (int i = previousInstr.size() - 1; i >= 0; i--){
            for (Op operation : previousInstr.get(i)){
                int target = operation.getOpCode().getTargetCount();
                if (target == 1){
                    Operand operand = operation.getArgs().get(operation.getArgs().size()-1);
                    if (operand.equals(oper) && !(storeOperation(operation) || pushOperation(operation))){
                        currentNode.addEdge(this.getInstrNode(operation));
                        break connect;
                    }
                }
            }
        }
        return graph;
    }

    /**
     * Connects this node to all store and push nodes previously in the program.
     * @param graph current graph
     * @param previousInstr previous Instructions
     * @param currentNode currentNode
     * @return the given graph plus all the store and push dependencies of this node.
     */
    public Graph connectStorePush(Graph graph,List<Instr> previousInstr, Node currentNode){
        for (Instr instr: previousInstr) {
            for (Op operation : instr) {
                if (this.storeOperation(operation) || this.pushOperation(operation)){
                    currentNode.addEdge(getInstrNode(operation));
                }
            }
        }
        return graph;
    }

    /**
     * @param operation operation
     * @return if this operation is a push operation
     */
    public boolean pushOperation(Op operation){
        return operation.getOpCode() == OpCode.push || operation.getOpCode() == OpCode.cpush;
    }

    /**
     *
     * @param operation operation
     * @return if this operation is a store operation
     */
    public boolean storeOperation(Op operation){
        return operation.getOpCode() == OpCode.store
                || operation.getOpCode() == OpCode.storeAI
                || operation.getOpCode() == OpCode.storeAO
                || operation.getOpCode() == OpCode.cstore
                || operation.getOpCode() == OpCode.cstoreAI
                || operation.getOpCode() == OpCode.cstoreAO;
    }

    /**
     *
     * @param operation operation
     * @return if this operation is a load operation (loadI excluded).
     */
    public boolean loadOperation(Op operation){
        return operation.getOpCode() == OpCode.load
                || operation.getOpCode() == OpCode.loadAI
                || operation.getOpCode() == OpCode.loadAO
                || operation.getOpCode() == OpCode.cload
                || operation.getOpCode() == OpCode.cloadAO
                || operation.getOpCode() == OpCode.cloadAI;
    }

    /**
     *
     * @param operation operation
     * @return if this operation is a pop operation.
     */
    public boolean popOperation(Op operation){
        return operation.getOpCode() == OpCode.pop || operation.getOpCode() == OpCode.cpop;
    }

    /**
     *
     * @param operation operation
     * @return node corresponding to this operation's linenumber (which is unique).
     */
    public Node getInstrNode(Op operation){
        return instrNodeMap.get(operation.getLine());
    }

    /**
     * writes a Graph to a dot file to get a graphical view of the dependency graph.
     * @param graph graph
     */
    public void writeDot(Graph graph){
        try {
            graph.writeDOT("src/pp/s1542664/q2_3/datagraph.dot", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
