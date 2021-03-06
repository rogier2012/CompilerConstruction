package pp.s1542664.q1_2;

import org.antlr.v4.runtime.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Rogier on 18-05-16.
 * The DecimalTest for exercise 2 of the first take home exam.
 * The DecimalTest is to test the lexer grammar Decimal.g4 and its correctness.
 * It performs two tests: In the first one all tokens are correct and should all give the correct type.
 * In the second one all tokens are wrong and should give exceptions which all will be catch by the test.
 */
public class DecimalTest {

    /*
     All of the tokens should produce the correct type.
     */
    @Test
    public void decimalCorrect() {
        List<Token> test = scan("0.2f .3436D 234234_34234.3453453d 0.1E4_4f 350f 0.1 0_____4");
        assertEquals(test.get(0).getType(), Decimal.FLOAT);
        assertEquals(test.get(1).getType(), Decimal.DOUBLE);
        assertEquals(test.get(2).getType(), Decimal.DOUBLE);
        assertEquals(test.get(3).getType(), Decimal.FLOAT);
        assertEquals(test.get(4).getType(), Decimal.FLOAT);
        assertEquals(test.get(5).getType(), Decimal.DOUBLE);
        assertEquals(test.get(6).getType(), Decimal.DOUBLE);
        System.out.println(test);

    }

    /*
     This method only prints a message to the console if the lexer succeeds but should not have.
     Moreover, the JUnit tests fails if the token is correctly tokenized.
     */
    @Test
    public void decimalFalse() {
        try {
            scanSingle("0..9");
            System.out.println("Should fail but didn't");
            fail();
        } catch (LexerNoViableAltException e) {
//            succes
        }
        try {
            scanSingle("9e1.f");
            System.out.println("Should fail but didn't");
            fail();
        } catch (LexerNoViableAltException e) {
//            succes
        }
        try {
            scanSingle("1.");
            System.out.println("Should fail but didn't");
            fail();
        } catch (LexerNoViableAltException e) {
//            succes
        }
        try {
            scanSingle("0x1234");
            System.out.println("Should fail but didn't");
            fail();
        } catch (LexerNoViableAltException e) {
//            succes
        }
    }

    /*
     This method scans the given string and returns all tokens produces by the Decimal lexer generated by Decimal.g4.
     */
    public static List<Token> scan(String text) {
        CharStream stream = new ANTLRInputStream(text);
        Lexer lexer = new Decimal(stream);

        return new ArrayList<>(lexer.getAllTokens());
    }

    /*
     This methods scans the given string and returns the first token produces by the Decimal lexer generated by Decimal.g4.
     */
    private static Token scanSingle(String text) throws LexerNoViableAltException {
        CharStream stream = new ANTLRInputStream(text);
        Lexer lexer = new Decimal(stream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new MyErrorListener());
        return lexer.getAllTokens().get(0);
    }
}
