package pp.s1542664.q1_2;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * Created by Rogier on 19-05-16 in Enschede.
 * The MyErrorListener for exercise 2 of the first take home exam.
 * This extension on BaseErrorListener throws a RecognitionException instead of catching it.
 * The result of this class is let the user of this class to handle the exception.
 */
public class MyErrorListener extends BaseErrorListener {

    /*
     The method syntaxError will receive incoming RecognitionExceptions along with other information.
     This method will only throw the RecognitionException.
     */
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        throw e;
    }
}
