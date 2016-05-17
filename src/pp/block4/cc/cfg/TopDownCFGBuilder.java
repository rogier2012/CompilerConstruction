package pp.block4.cc.cfg;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
import pp.block4.cc.ErrorListener;
import pp.block4.cc.cfg.FragmentParser.BreakStatContext;
import pp.block4.cc.cfg.FragmentParser.ContStatContext;
import pp.block4.cc.cfg.FragmentParser.ProgramContext;

/** Template top-down CFG builder. */
public class TopDownCFGBuilder extends FragmentBaseListener {
	/** The CFG being built. */
	private Graph graph;
    private ParseTreeProperty<Pair<ParseTree,ParseTree>> pairParseTreeProperty;
    private ParseTreeProperty<Node> nodeTree;

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
        super.enterProgram(ctx);
    }


    @Override
    public void enterDecl(FragmentParser.DeclContext ctx) {
        Node node = addNode(ctx,"Declare");
        nodeTree.put(ctx,node);
        Node incoming = nodeTree.get(pairParseTreeProperty.get(ctx).a);

        incoming.addEdge(node);
    }

    @Override
    public void enterAssignStat(FragmentParser.AssignStatContext ctx) {
        Node node = addNode(ctx,"Assign");
    }

    @Override
    public void enterIfStat(FragmentParser.IfStatContext ctx) {
        Node cond = addNode(ctx,"Cond");
        Node after = addNode(ctx, "After");

    }

    @Override
    public void enterWhileStat(FragmentParser.WhileStatContext ctx) {
        Node cond = addNode(ctx,"Cond");
    }

    @Override
    public void enterBlockStat(FragmentParser.BlockStatContext ctx) {
        int childCount = ctx.stat().size();
        for (int i = 0; i < (childCount -1); i++) {

        }
    }

    @Override
    public void enterPrintStat(FragmentParser.PrintStatContext ctx) {
        Node node = addNode(ctx,"Print");

    }

    /** Adds a node to he CGF, based on a given parse tree node.
	 * Gives the CFG node a meaningful ID, consisting of line number and 
	 * a further indicator.
	 */
	private Node addNode(ParserRuleContext node, String text) {
		return this.graph.addNode(node.getStart().getLine() + ": " + text);
	}


    private void setPair(ParseTree toBeconnected, ParseTree connection){
        Pair<ParseTree,ParseTree> pair = new Pair<>(toBeconnected,connection);
        pairParseTreeProperty.put(connection,pair);
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
