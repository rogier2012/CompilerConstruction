package pp.block2.cc.antlr;

import org.antlr.v4.runtime.*;

import org.antlr.v4.runtime.tree.*;
import pp.block2.cc.*;
import pp.block2.cc.Parser;
import pp.block2.cc.ll.Sentence;

import java.util.ArrayList;
import java.util.List;

public class SentenceConverter //
		extends SentenceBaseListener implements Parser {
	/** Factory needed to create terminals of the {@link Sentence}
	 * grammar. See {@link pp.block2.cc.ll.SentenceParser} for
	 * example usage. */
	private final SymbolFactory fact;
    ParseTreeProperty<AST> newtree;
    private AST astmain;
	private int errorcount;
    private static final NonTerm SENT = new NonTerm("Sentence");
    private static final NonTerm SUBJ = new NonTerm("Subject");
    private static final NonTerm OBJ = new NonTerm("Object");
    private static final NonTerm MOD = new NonTerm("Modifier");


	public SentenceConverter() {
		this.fact = new SymbolFactory(Sentence.class);
	}

	@Override
	public AST parse(Lexer lexer) throws ParseException {
        SentenceParser parser = new SentenceParser(new CommonTokenStream(lexer));
		errorcount =0;
        ParseTree tree = parser.sentence();
        newtree = new ParseTreeProperty<>();
        new ParseTreeWalker().walk(this, tree);
		if (errorcount > 0){
			throw new ParseException("");
		}
        return astmain;
	}

	@Override
	public void exitSentence(SentenceParser.SentenceContext ctx) {
        AST ast = new AST(SENT);
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ast.addChild(newtree.get(ctx.getChild(i)));
        }
        astmain = ast;
	}

	@Override
	public void exitSubject(SentenceParser.SubjectContext ctx) {
        AST ast = new AST(SUBJ);
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ast.addChild(newtree.get(ctx.getChild(i)));
        }
       setAST(ctx,ast);
	}


	@Override
	public void exitObject(SentenceParser.ObjectContext ctx) {
        AST ast = new AST(OBJ);
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ast.addChild(newtree.get(ctx.getChild(i)));
        }
        setAST(ctx,ast);
	}

	@Override
	public void exitModifier(SentenceParser.ModifierContext ctx) {
        AST ast = new AST(MOD);
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ast.addChild(newtree.get(ctx.getChild(i)));
        }
        setAST(ctx,ast);
	}

	@Override
	public void visitTerminal(TerminalNode node) {
        Token token = node.getSymbol();
        Term term =fact.getTerminal(token.getType());
        setAST(node,new AST(term, token));

    }

	@Override
	public void visitErrorNode(ErrorNode node) {
		this.errorcount++;
	}

    private void setAST(ParseTree node, AST ast) {
        this.newtree.put(node, ast);
    }


	// From here on overwrite the listener methods
	// Use an appropriate ParseTreeProperty to
	// store the correspondence from nodes to ASTs
}
