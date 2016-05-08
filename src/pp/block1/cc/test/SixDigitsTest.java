package pp.block1.cc.test;

import org.junit.Test;

import pp.block1.cc.antlr.Example;
import pp.block1.cc.antlr.SixDigits;

public class SixDigitsTest {
    private static LexerTester tester = new LexerTester(SixDigits.class);

    @Test
    public void succeedingTest() {
        tester.correct("N11111");
        tester.wrong("1111111");
        tester.wrong("A123456");
        tester.correct("ROGIER");
    }



    @Test
    public void noSpacesBetweenKeywordsTest() {
        // the following is perfectly fine, so claiming it's wrong will fail
        tester.yields("ROGIERROGIER",SixDigits.WORD, SixDigits.WORD );
    }
}