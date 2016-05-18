package pp.block4.cc.cfg;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import pp.block2.cc.NonTerm;
import pp.block4.cc.ErrorListener;
import pp.block4.cc.cfg.FragmentParser.BreakStatContext;
import pp.block4.cc.cfg.FragmentParser.ContStatContext;
import pp.block4.cc.cfg.FragmentParser.ProgramContext;

/** Template top-down CFG builder. */
public class TopDownCFGBuilder extends FragmentBaseListener {
	/** The CFG being built. */
	private Graph graph;
    private ParseTreeProperty<ParseTree> pairParseTreeProperty;
    private ParseTreeProperty<List<Node>> nodeTree;

	/** Builds the CFG for a program contained in a given file. */
	public Graph build(File file) {
		Graph result = null;
		ErrorListener listener = new ErrorListener();
		try {
			CharStream chars = new ANTLRInputStream(new FileReader(file));
			Lexer lexer = new FragmentLexer(chars);
			lexer.removeErrorListeners();
			lexer.addErrorListener(listener);
			TokenStream tokens = new CommonTokenStream(lexer);
			FragmentParser parser = new FragmentParser(tokens);
			parser.removeErrorListeners();
			parser.addErrorListener(listener);
			ProgramContext tree = parser.program();
			if (listener.hasErrors()) {
				System.out.printf("Parse errors in %s:%n", file.getPath());
				for (String error : listener.getErrors()) {
					System.err.println(error);
				}
			} else {
				result = build(tree);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/** Builds the CFG for a program given as an ANTLR parse tree. */
	public Graph build(ProgramContext tree) {
		this.graph = new Graph();
        pairParseTreeProperty = new ParseTreeProperty<>();
        nodeTree = new ParseTreeProperty<>();
        new ParseTreeWalker().walk(this,tree);
		return graph;
	}

	@Override
	public void enterBreakStat(BreakStatContext ctx) {
		throw new IllegalArgumentException("Break not supported");
	}

	@Override
	public void enterContStat(ContStatContext ctx) {
		throw new IllegalArgumentException("Continue not supported");
	}

    @Override
    public void enterProgram(ProgramContext ctx) {
        int childCount = ctx.stat().size();
        Node program = addNode(ctx,"Program");
        if (childCount >= 1){
            setPair(ctx.stat(0),ctx);
        }

        for (int i = 1; i < (childCount); i++) {
            setPair(ctx.stat(i),ctx.stat(i-1));
        }
        setNodeTree(ctx,program);
    }


    @Override
    public void enterDecl(FragmentParser.DeclContext ctx) {
        Node node = addNode(ctx,"Assign");

        Node previousNode = previous(ctx);
        if (ctx.getParent().getParent() instanceof FragmentParser.WhileStatContext){
            node.addEdge(previousNode);
        } else if (ctx.getParent().getParent() instanceof FragmentParser.IfStatContext){
            previousNode.addEdge(node);
            node.addEdge(next(ctx));
        } else {
            previousNode.addEdge(node);
        }
        setNodeTree(ctx,node);

    }

    @Override
    public void enterAssignStat(FragmentParser.AssignStatContext ctx) {
        Node node = addNode(ctx,"Assign");
        Node previousNode = previous(ctx);
        if (ctx.getParent().getParent() instanceof FragmentParser.WhileStatContext){
            if (nodeTree.get(pairParseTreeProperty.get(ctx)).size() == 2){
                node.addEdge(nodeTree.get(ctx.getParent().getParent()).get(0));
                previousNode = next(ctx);
                previousNode.addEdge(node);
            } else {
                node.addEdge(previousNode);
            }
            setNodeTree(ctx,node);
        } else if (ctx.getParent().getParent() instanceof FragmentParser.IfStatContext){
            previousNode.addEdge(node);
            node.addEdge(next(ctx));
            setNodeTree(ctx,next(ctx));
        } else {
            previousNode.addEdge(node);
            setNodeTree(ctx,node);
        }

    }

    @Override
    public void enterIfStat(FragmentParser.IfStatContext ctx) {
        Node previous = previous(ctx);

        Node cond = addNode(ctx,"Cond");
        Node after = addNode(ctx, "After");

        previous.addEdge(cond);
        cond.addEdge(after);

        setNodeTree(ctx,cond,after);
        setPair(ctx.stat(0),ctx);

    }

    @Override
    public void enterWhileStat(FragmentParser.WhileStatContext ctx) {
        Node previous = previous(ctx);
        Node cond = addNode(ctx,"Cond");
        previous.addEdge(cond);

        setNodeTree(ctx,cond);
        setPair(ctx.stat(),ctx);
    }

    @Override
    public void enterBlockStat(FragmentParser.BlockStatContext ctx) {
        int childCount = ctx.stat().size();

        if (childCount >= 1){
            setPair(ctx.stat(0),ctx.getParent());

        }

        for (int i = 1; i < (childCount); i++) {
            setPair(ctx.stat(i),ctx.stat(i-1));
        }


    }

    @Override
    public void enterPrintStat(FragmentParser.PrintStatContext ctx) {
        Node node = addNode(ctx,"Print");
        Node previous = previous(ctx);
        if (ctx.getParent().getParent() instanceof FragmentParser.WhileStatContext){
            previous.addEdge(node);
            node.addEdge(previous);
        } else if (ctx.getParent().getParent() instanceof FragmentParser.IfStatContext){
            previous.addEdge(node);
            node.addEdge(next(ctx));
        } else {
            previous.addEdge(node);
        }

        setNodeTree(ctx,node);
    }

    /** Adds a node to he CGF, based on a given parse tree node.
	 * Gives the CFG node a meaningful ID, consisting of line number and 
	 * a further indicator.
	 */
	private Node addNode(ParserRuleContext node, String text) {
		return this.graph.addNode(node.getStart().getLine() + ": " + text);
	}


    private void setPair(ParseTree child, ParseTree toBeconnected){
        pairParseTreeProperty.put(child,toBeconnected);
    }

    private void setNodeTree(ParseTree tree,Node... node){
        nodeTree.put(tree,Arrays.asList(node));
    }

    private Node previous(ParseTree tree){
        return nodeTree.get(pairParseTreeProperty.get(tree)).get(0);
    }

    private Node next(ParseTree tree){
        return nodeTree.get(pairParseTreeProperty.get(tree)).get(1);
    }


	/** Main method to build and print the CFG of a simple Java program. */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Usage: [filename]+");
			return;
		}
		TopDownCFGBuilder builder = new TopDownCFGBuilder();
		for (String filename : args) {
			File file = new File(filename);
			System.out.println(filename);
			System.out.println(builder.build(file));
		}
	}
}
