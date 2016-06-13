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



	public Graph convert(Program prog) {
        Graph graph = new Graph();
        instrNodeMap = new HashMap<>();

        List<Instr> instrList = prog.getInstr();
        for (int i = 0; i < instrList.size(); i++) {
            for (Op operation : instrList.get(i)){
                Node node = graph.addNode(operation.getLine() + "_" +operation.getOpCode().name());
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


    public Graph connectDependency(Graph graph, List<Instr> previousInstr, Node currentNode, Operand oper){
        connect:
        for (int i = previousInstr.size() - 1; i >= 0; i--){
            for (Op operation : previousInstr.get(i)){
                int target = operation.getOpCode().getTargetCount();
                if (target == 1){
                    Operand operand = operation.getArgs().get(operation.getArgs().size()-1);
                    if (operand.equals(oper)){
                        currentNode.addEdge(this.getInstrNode(operation));
                        break connect;
                    }
                }
            }
        }
        return graph;
    }


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


    public boolean pushOperation(Op operation){
        return operation.getOpCode() == OpCode.push || operation.getOpCode() == OpCode.cpush;
    }


    public boolean storeOperation(Op operation){
        return operation.getOpCode() == OpCode.store
                || operation.getOpCode() == OpCode.storeAI
                || operation.getOpCode() == OpCode.storeAO
                || operation.getOpCode() == OpCode.cstore
                || operation.getOpCode() == OpCode.cstoreAI
                || operation.getOpCode() == OpCode.cstoreAO;
    }

    public boolean loadOperation(Op operation){
        return operation.getOpCode() == OpCode.load
                || operation.getOpCode() == OpCode.loadAI
                || operation.getOpCode() == OpCode.loadAO
                || operation.getOpCode() == OpCode.cload
                || operation.getOpCode() == OpCode.cloadAO
                || operation.getOpCode() == OpCode.cloadAI;
    }


    public boolean popOperation(Op operation){
        return operation.getOpCode() == OpCode.pop || operation.getOpCode() == OpCode.cpop;
    }

    public Node getInstrNode(Op operation){
        return instrNodeMap.get(operation.getLine());
    }


    public void writeDot(Graph graph){
        try {
            graph.writeDOT("src/pp/s1542664/q2_3/datagraph.dot", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
