package pp.s1542664.q1_2;

import org.antlr.v4.runtime.*;
import org.junit.Test;
import pp.block1.cc.antlr.Example;
import pp.block3.cc.tabular.MyErrorListener;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogier on 18-05-16.
 */
public class DecimalTest {

    @Test
    public void decimalCorrect(){
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

//    This method only prints a message to the console if the lexer succeeds but should not have.
    @Test
    public void decimalFalse(){
        try {
            scanSingle("0..9");
            System.out.println("Should fail but didn't");
        } catch (LexerNoViableAltException e) {
//            succes
        }
        try {
            scanSingle("9e1.f");
            System.out.println("Should fail but didn't");
        } catch (LexerNoViableAltException e) {
//            succes
        }
        try {
            scanSingle("1.");
            System.out.println("Should fail but didn't");
        } catch (LexerNoViableAltException e) {
//            succes
        }
        try {
            scanSingle("0x1234");
            System.out.println("Should fail but didn't");
        } catch (LexerNoViableAltException e) {
//            succes
        }




    }


    public static List<Token> scan(String text) {
        CharStream stream = new ANTLRInputStream(text);
        Lexer lexer = new Decimal(stream);

        return new ArrayList<>(lexer.getAllTokens());
    }


    public static Token scanSingle(String text) throws LexerNoViableAltException {
        CharStream stream = new ANTLRInputStream(text);
        Lexer lexer = new Decimal(stream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new MyErrorListerner());
        return lexer.getAllTokens().get(0);
    }
}
