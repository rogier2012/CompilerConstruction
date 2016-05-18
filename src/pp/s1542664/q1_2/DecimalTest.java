package pp.s1542664.q1_2;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.junit.Test;
import pp.block1.cc.antlr.Example;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogier on 18-05-16.
 */
public class DecimalTest {

    @Test
    public void decimal(){
        List<Token> test = scan("0.2f .3436D 234234_34234.3453453d 0.1E4_4f 0f 0.1");
        assertEquals(test.get(0).getType(), Decimal.FLOAT);
        assertEquals(test.get(1).getType(), Decimal.DOUBLE);
        assertEquals(test.get(2).getType(), Decimal.DOUBLE);
        assertEquals(test.get(3).getType(), Decimal.FLOAT);
        assertEquals(test.get(4).getType(), Decimal.FLOAT);
        assertEquals(test.get(5).getType(), Decimal.DOUBLE);
        System.out.println(test);

    }


    public static List<Token> scan(String text) {
        CharStream stream = new ANTLRInputStream(text);
        Lexer lexer = new Decimal(stream);
        return new ArrayList<>(lexer.getAllTokens());
    }
}
