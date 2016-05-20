package pp.s1542664.q1_5;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogier on 20-05-16 in Enschede.
 * The EmoteChecker for exercise 5 of the first take home exam.
 */
public class EmoteChecker extends EmoteBaseListener {
    private EmoteSymbolTable<Integer> integerSymbolTable;
    private List<String> errors;


    public void init(){
        integerSymbolTable = new EmoteSymbolTable<>();
        errors = new ArrayList<>();
    }
    public List<String> getErrors(){
        return errors;
    }

    @Override
    public void exitRaiseItem(EmoteParser.RaiseItemContext ctx) {
        String id = ctx.WORD().getText();
        int emotion;
        if (integerSymbolTable.contains(id)){
            emotion = getEmotion(id);
        } else {
            emotion = 0;
        }

        setEmotion(id,emotion+1);
    }

    @Override
    public void enterTextItem(EmoteParser.TextItemContext ctx) {
        integerSymbolTable.openScope();
    }

    @Override
    public void exitTextItem(EmoteParser.TextItemContext ctx) {
        integerSymbolTable.closeScope();
    }

    @Override
    public void exitWordItem(EmoteParser.WordItemContext ctx) {
        String id = ctx.WORD().getText();

        if (!integerSymbolTable.contains(id)){
            setEmotion(id,0);
        }

        String excl = "";
        if (ctx.EXCL() != null){
            excl = ctx.EXCL().getText();
        }

        int emotion = getEmotion(id);
        if (excl.length() != emotion){
            errors.add("line " + ctx.WORD().getSymbol().getLine() + ":" + ctx.WORD().getSymbol().getCharPositionInLine()
                    +" wrong emotional intensity");
        }

    }

    private int getEmotion(String id){
        return integerSymbolTable.get(id);
    }

    private void setEmotion(String id, int emotion){
        integerSymbolTable.setEmotion(id,emotion);
    }
}
