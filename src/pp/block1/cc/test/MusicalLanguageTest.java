package pp.block1.cc.test;

import org.junit.Test;
import pp.block1.cc.antlr.Example;
import pp.block1.cc.antlr.MusicalLanguage;

/**
 * Created by Rogier on 21-04-16 in Enschede.
 */
public class MusicalLanguageTest {
    private static LexerTester tester = new LexerTester(MusicalLanguage.class);

    @Test
    public void succeedingTest() {
        tester.wrong("LaLi");
        tester.yields("Laaaa LaLaa", MusicalLanguage.LA_LA, MusicalLanguage.LA);
        tester.wrong("La La La La Li La");
    }


}
