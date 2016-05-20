package pp.s1542664.q1_5;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static org.junit.Assert.assertEquals;

/**
 * Created by Rogier on 20-05-16 in Enschede.
 * The EmoteCheckerTest for exercise 5 of the first take home exam.
 * This JUnit test will test for correct and incorrect words with emotional intensity.
 * It uses the EmoteChecker which is a listener for the parse tree of the Emote.g4 grammar.
 */
public class EmoteCheckerTest {
    private EmoteChecker emoteChecker = new EmoteChecker();

    /*
     This method tests the two given files for correctness using the EmoteChecker.
     */
    @Test
    public void test() {
        try {
            test(0, new FileReader("src/pp/s1542664/q1_5/ok1.txt"));
            test(1, new FileReader("src/pp/s1542664/q1_5/err1.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void test(int errors, Reader file) {
        ParseTree tree = parseEmote(file);
        this.emoteChecker.init();
        new ParseTreeWalker().walk(emoteChecker, tree);
        System.out.println(emoteChecker.getErrors());
        assertEquals(errors, emoteChecker.getErrors().size());
    }

    private ParseTree parseEmote(Reader file) {
        CharStream chars = null;
        try {
            chars = new ANTLRInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Lexer lexer = new EmoteLexer(chars);
        TokenStream tokens = new CommonTokenStream(lexer);
        EmoteParser parser = new EmoteParser(tokens);
        return parser.text();
    }

}
