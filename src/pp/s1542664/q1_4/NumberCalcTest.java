package pp.s1542664.q1_4;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Rogier on 20-05-16.
 * The NumberCalcTest for exercise 4 of the first take home exam.
 * This JUnit test will test three different numbers calculated by the NumberCalc which implemented the Number.g4 grammar.
 * It also tests the NumberAttr.g4 attribute rule grammar and its generated NumberAttrParser.
 * The first test is for decimals, the second is for binary numbers and the third one is for hexadecimal numbers.
 */
public class NumberCalcTest {

    private final ParseTreeWalker walker = new ParseTreeWalker();
    private final NumberCalc number = new NumberCalc();

    /*
     This JUnit test tests three different decimal numbers and compares it to the given integer value.
     */
    @Test
    public void decimalTest() {
        test(101, "101");
        test(74567, "74567");
        test(16423317, "16423317");
    }

    /*
     This JUnit test tests three different binary numbers and compares it to the given integer value.
     */
    @Test
    public void binaryTest() {
        test(5, "b101");
        test(74567, "b10010001101000111");
        test(16423317, "b111110101001100110010101");
    }

    /*
     This JUnit test tests three different hexadecimal numbers and compares it to the given integer value.
     */
    @Test
    public void hexTest() {
        test(257, "x101");
        test(74567, "x12347");
        test(16423317, "xFA9995");
    }

    /*
     This method will test the attribute grammar and the listener to the expected integer value.
     */
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
        return parser.num();
    }
}
