package pp.block4.cc.cfg;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import pp.block4.cc.ErrorListener;
import pp.block4.cc.cfg.FragmentParser.BlockStatContext;
import pp.block4.cc.cfg.FragmentParser.BreakStatContext;

/** Template bottom-up CFG builder. */
public class BottomUpCFGBuilder extends FragmentBaseListener {
	/** The CFG being built. */
	private Graph graph;
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
			ParseTree tree = parser.program();
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
	public Graph build(ParseTree tree) {
		this.graph = new Graph();
        nodeTree = new ParseTreeProperty<>();
		new ParseTreeWalker().walk(this,tree);
        return graph;
	}

	@Override
	public void enterBreakStat(BreakStatContext ctx) {
		throw new IllegalArgumentException("Break not supported");
	}

	@Override
	public void enterContStat(FragmentParser.ContStatContext ctx) {
		throw new IllegalArgumentException("Continue not supported");
	}



    @Override
    public void exitProgram(FragmentParser.ProgramContext ctx) {
        int childCount = ctx.stat().size();
        for (int i = 0; i < (childCount-1); i++) {
            outgoingVal(ctx.stat(i)).addEdge(incomingVal(ctx.stat(i+1)));
        }

        List<Node> enterExit = new ArrayList<>();
        if (childCount >= 1){
            enterExit.add(0,incomingVal(ctx.stat(0)));
            enterExit.add(1,outgoingVal(ctx.stat(childCount-1)));
        }

        nodeTree.put(ctx,enterExit);
    }

    public void assign(ParserRuleContext ctx){
        Node node = this.addNode(ctx, "Assign");
        List<Node> enterExit = new ArrayList<>();
        enterExit.add(node);
        enterExit.add(node);
        nodeTree.put(ctx,enterExit);
    }

    @Override
    public void exitDecl(FragmentParser.DeclContext ctx) {
        assign(ctx);
    }

    @Override
    public void exitAssignStat(FragmentParser.AssignStatContext ctx) {
        assign(ctx);
    }

    @Override
    public void exitIfStat(FragmentParser.IfStatContext ctx) {
        Node cond = this.addNode(ctx, "Cond");
        Node after = this.addNode(ctx, "After");

        cond.addEdge(incomingVal(ctx.stat(0)));
        outgoingVal(ctx.stat(0)).addEdge(after);
        cond.addEdge(after);

        List<Node> enterExit = new ArrayList<>();
        enterExit.add(0,cond);
        enterExit.add(1,after);
        nodeTree.put(ctx,enterExit);
    }

    @Override
    public void exitWhileStat(FragmentParser.WhileStatContext ctx) {
        Node cond = this.addNode(ctx, "Cond");
        cond.addEdge(incomingVal(ctx.stat()));
        outgoingVal(ctx.stat()).addEdge(cond);


        List<Node> enterExit = new ArrayList<>();
        enterExit.add(0,cond);
        enterExit.add(1,cond);
        nodeTree.put(ctx,enterExit);
    }

    @Override
    public void exitBlockStat(BlockStatContext ctx) {
        int childCount = ctx.stat().size();
        for (int i = 0; i < (childCount-1); i++) {
            outgoingVal(ctx.stat(i)).addEdge(incomingVal(ctx.stat(i+1)));
        }

        List<Node> enterExit = new ArrayList<>();
        if (childCount >= 1){
            enterExit.add(0,incomingVal(ctx.stat(0)));
            enterExit.add(1,outgoingVal(ctx.stat(childCount-1)));
        }


        nodeTree.put(ctx,enterExit);
    }

    @Override
    public void exitPrintStat(FragmentParser.PrintStatContext ctx) {
        Node node= this.addNode(ctx, "Print");

        List<Node> enterExit = new ArrayList<>();
        enterExit.add(0,node);
        enterExit.add(1,node);
        nodeTree.put(ctx,enterExit);
    }


    /** Adds a node to he CGF, based on a given parse tree node.
	 * Gives the CFG node a meaningful ID, consisting of line number and 
	 * a further indicator.
	 */
	private Node addNode(ParserRuleContext node, String text) {
		return this.graph.addNode(node.getStart().getLine() + ": " + text);
	}

    private Node incomingVal(ParseTree node){
        return nodeTree.get(node).get(0);
    }

    private Node outgoingVal(ParseTree node){
        return nodeTree.get(node).get(1);
    }

    private List<Node> val(ParseTree node){
        return nodeTree.get(node);
    }


	/** Main method to build and print the CFG of a simple Java program. */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Usage: [filename]+");
			return;
		}
		BottomUpCFGBuilder builder = new BottomUpCFGBuilder();
		for (String filename : args) {
			File file = new File(filename);
			System.out.println(filename);
			System.out.println(builder.build(file));
		}
	}
}
