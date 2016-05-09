package pp.block3.cc.test;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import pp.block3.cc.antlr.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by Rogier on 09-05-16 in Enschede.
 */
public class TypeTest {

    private final ParseTreeWalker walker = new ParseTreeWalker();
    private final Typer typer = new Typer();

    @Test
    public void test() {
        test(Type.STR, "\"hallo\"^2");

    }

    private void test(Type expected, String expr) {
        assertEquals(expected, parseTypesAttr(expr).val);
        ParseTree tree = parseTypes(expr);
        this.typer.init();
        this.walker.walk(this.typer, tree);
        assertEquals(expected, this.typer.val(tree));
    }

    private ParseTree parseTypes(String text) {
        CharStream chars = new ANTLRInputStream(text);
        Lexer lexer = new TypesLexer(chars);
        TokenStream tokens = new CommonTokenStream(lexer);
        TypesParser parser = new TypesParser(tokens);
        return parser.type();
    }

    private TypesAttrParser.TypeContext parseTypesAttr(String text) {
        CharStream chars = new ANTLRInputStream(text);
        Lexer lexer = new TypesAttrLexer(chars);
        TokenStream tokens = new CommonTokenStream(lexer);
        TypesAttrParser parser = new TypesAttrParser(tokens);
        return parser.type();
    }
}
