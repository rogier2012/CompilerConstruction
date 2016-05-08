package pp.block2.cc.test;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.junit.Test;
import pp.block2.cc.antlr.ArithmeticLexer;
import pp.block2.cc.antlr.MyCalculator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * Created by Christiaan on 06-05-16.
 */
public class CalculatorTest {
    private MyCalculator calculator;

    private Class<? extends Lexer> lexerType;

    @Test
    public void testExpression() {
        this.lexerType = ArithmeticLexer.class;
        calculator = new MyCalculator();
        correct("(3+5)-4", new BigInteger("4"));
        correct("3^2", new BigInteger("9"));
        correct("0", new BigInteger("0"));
        correct("3^1^5^0", new BigInteger("3"));
        correct("(5/5)", new BigInteger("1"));
        correct("3*2^2", new BigInteger("12"));
        correct("(3+5)*5*(3-2)^2*20/4", new BigInteger("200"));
    }


    public void correct(String text, BigInteger answer) {
        assertEquals(answer, calculator.calculate(scan(text)));
    }


    /**
     * Converts a text into a token stream, using the preset lexer type.
     */
    private Lexer scan(String text) {
        Lexer result = null;
        CharStream stream = new ANTLRInputStream(text);
        try {
            Constructor<? extends Lexer> lexerConstr = this.lexerType
                    .getConstructor(CharStream.class);
            result = lexerConstr.newInstance(stream);
        } catch (NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException
                | InvocationTargetException e) {
            // should never occur, as all Antlr-generated lexers are
            // well-behaved
            e.printStackTrace();
        }
        return result;

    }
}