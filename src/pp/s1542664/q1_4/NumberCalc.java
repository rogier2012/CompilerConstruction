package pp.s1542664.q1_4;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Created by Rogier on 20-05-16.
 * The NumberCalc for exercise 4 of the first take home exam.
 * This extension to the generated NumberBaseListener from Number.g4 will calculate the integer value of the given ParseTree.
 * It used both the enter and exit method provided by the listener to calculate the correct value.
 */
public class NumberCalc extends NumberBaseListener {
    /*
     Integer base to save the numbers current base.
     */
    private int base;
    /*
     ParseTreeProperty with integers to store at each node of
     the tree the current position in the number(starting with 0 in right).
     */
    private ParseTreeProperty<Integer> position;
    /*
     ParseTreeProperty with integers to store at each node of
     the tree the value calculated.
     */
    private ParseTreeProperty<Integer> value;



    public void init() {
        this.base = 0;
        this.position = new ParseTreeProperty<>();
        this.value = new ParseTreeProperty<>();
    }

    @Override
    public void exitPrefixNum(NumberParser.PrefixNumContext ctx) {
        setValue(ctx, getValue(ctx.seq()));
    }

    @Override
    public void enterNormalNum(NumberParser.NormalNumContext ctx) {
        base = 10;
    }

    @Override
    public void exitNormalNum(NumberParser.NormalNumContext ctx) {
        setValue(ctx, getValue(ctx.seq()));
    }

    @Override
    public void exitBin(NumberParser.BinContext ctx) {
        base = 2;
    }

    @Override
    public void exitHex(NumberParser.HexContext ctx) {
        base = 16;
    }


    @Override
    public void exitSeqDig(NumberParser.SeqDigContext ctx) {
        setPosition(ctx, getPosition(ctx.seq()) + 1);
        int val = (getValue(ctx.dig()) * (int) Math.pow(base, getPosition(ctx)) + getValue(ctx.seq()));
        setValue(ctx, val);

    }

    @Override
    public void exitSingleDig(NumberParser.SingleDigContext ctx) {
        setPosition(ctx, 0);

        setValue(ctx, getValue(ctx.dig()) * (int) Math.pow(base, 0));
    }

    @Override
    public void exitDig(NumberParser.DigContext ctx) {
        int val = Integer.parseInt(ctx.DIGIT().getText(), 16);
        setValue(ctx, val);
    }

    private void setPosition(ParseTree node, int position) {
        this.position.put(node, position);
    }

    private void setValue(ParseTree node, int value) {
        this.value.put(node, value);
    }

    private int getPosition(ParseTree node) {
        return this.position.get(node);
    }

    public int getValue(ParseTree node) {
        return this.value.get(node);
    }


}
