package pp.s1542664.q1_4;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Rogier on 20-05-16.
 */
public class NumberCalcTest {

    private final ParseTreeWalker walker = new ParseTreeWalker();
    private final NumberCalc number = new NumberCalc();

    @Test
    public void decimalTest() {
        test(101,"101");
        test(74567,"74567");
        test(16423317, "16423317");
    }

    @Test
    public void binaryTest(){
        test(5,"b101");
        test(74567, "b10010001101000111");
        test(16423317, "b111110101001100110010101");
    }

    @Test
    public void hexTest(){
        test(257,"x101");
        test(74567,"x12347");
        test(16423317, "xFA9995");
    }

    private void test(int expected, String num) {
        assertEquals(expected, parseNumberAttr(num).val);
        ParseTree tree = parseCalc(num);
        this.number.init();
        this.walker.walk(this.number, tree);
        assertEquals(expected, this.number.getValue(tree));
    }

    private ParseTree parseCalc(String text) {
        CharStream chars = new ANTLRInputStream(text);
        Lexer lexer = new NumberLexer(chars);
        TokenStream tokens = new CommonTokenStream(lexer);
        NumberParser parser = new NumberParser(tokens);
        return parser.num();
    }

    private NumberAttrParser.NumContext parseNumberAttr(String text) {
        CharStream chars = new ANTLRInputStream(text);
        Lexer lexer = new NumberAttrLexer(chars);
        TokenStream tokens = new CommonTokenStream(lexer);
        NumberAttrParser parser = new NumberAttrParser(tokens);
        NumberAttrParser.NumContext tree = parser.num();
        return tree;
    }
}
