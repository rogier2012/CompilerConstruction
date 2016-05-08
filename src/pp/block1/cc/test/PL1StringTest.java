package pp.block1.cc.test;

import org.junit.Test;
import pp.block1.cc.antlr.Example;
import pp.block1.cc.antlr.PL1String;

/**
 * Created by Rogier on 21-04-16 in Enschede.
 */
public class PL1StringTest {

    private static LexerTester tester = new LexerTester(PL1String.class);

    @Test
    public void succeedingTest() {
        tester.correct("\" Hallo \"");
        tester.correct("\" Hallo \"\" Jan\"");
        tester.yields("\" Hallo \"\" Jan Smit \"", PL1String.STRING);
        tester.wrong("\"");
    }


}
