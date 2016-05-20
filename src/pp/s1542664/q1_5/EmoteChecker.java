package pp.s1542664.q1_5;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogier on 20-05-16 in Enschede.
 * The EmoteChecker for exercise 5 of the first take home exam.
 * This class will look for incorrect sentences and will flag each one of them.
 * At the end the EmoteChecker produced a list of all errors found and can give that list back the user if desired.
 */
public class EmoteChecker extends EmoteBaseListener {
    /*
     Extended SymbolTable with a additional method for setting the emotion value within a scope.
     */
    private EmoteSymbolTable<Integer> integerSymbolTable;
    /*
     List of errors found walking the parse tree.
     The errors are in the format "line .:. : #word wrong emotional intensity"
     */
    private List<String> errors;

    public void init() {
        integerSymbolTable = new EmoteSymbolTable<>();
        errors = new ArrayList<>();
    }

    public List<String> getErrors() {
        return errors;
    }

    /*
     This method will increase the emotional value of a word.
     If the word didn't exist in the current scope, it will be declared and set to 1.
     */
    @Override
    public void exitRaiseItem(EmoteParser.RaiseItemContext ctx) {
        String id = ctx.WORD().getText();
        int emotion;
        if (integerSymbolTable.contains(id)) {
            emotion = getEmotion(id);
        } else {
            emotion = 0;
        }

        setEmotion(id, emotion + 1);
    }

    /*
     New parentheses open a new scope in the symboltable.
     */
    @Override
    public void enterTextItem(EmoteParser.TextItemContext ctx) {
        integerSymbolTable.openScope();
    }

    /*
     Exiting the parentheses closes the scope in the symboltable.
     */
    @Override
    public void exitTextItem(EmoteParser.TextItemContext ctx) {
        integerSymbolTable.closeScope();
    }

    /*
     This method will check if the word has the correct emotional value.
     If the word never declared, it will declare this word with emotinal value 0.
     */
    @Override
    public void exitWordItem(EmoteParser.WordItemContext ctx) {
        String id = ctx.WORD().getText();

        if (!integerSymbolTable.contains(id)) {
            setEmotion(id, 0);
        }

        String excl = "";
        if (ctx.EXCL() != null) {
            excl = ctx.EXCL().getText();
        }

        int emotion = getEmotion(id);
        if (excl.length() != emotion) {
            errors.add("line " + ctx.WORD().getSymbol().getLine() + ":" + ctx.WORD().getSymbol().getCharPositionInLine()
                    + " wrong emotional intensity");
        }

    }

    private int getEmotion(String id) {
        return integerSymbolTable.get(id);
    }

    private void setEmotion(String id, int emotion) {
        integerSymbolTable.setValue(id, emotion);
    }
}
