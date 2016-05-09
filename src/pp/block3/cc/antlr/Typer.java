package pp.block3.cc.antlr;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Created by Rogier on 09-05-16 in Enschede.
 */
public class Typer extends TypesBaseListener {
    ParseTreeProperty<Type> types;


    public void init() {
        this.types = new ParseTreeProperty<>();
    }

    @Override
    public void exitPar(TypesParser.ParContext ctx) {
        val(ctx.type());
    }

    @Override
    public void exitBool(TypesParser.BoolContext ctx) {
        set(ctx,Type.BOOL);
    }

    @Override
    public void exitString(TypesParser.StringContext ctx) {
        set(ctx,Type.STR);
    }

    @Override
    public void exitNum(TypesParser.NumContext ctx) {
        set(ctx,Type.NUM);
    }

    @Override
    public void exitEquals(TypesParser.EqualsContext ctx) {
        if (val(ctx.type(0)) == val(ctx.type(1))){
            set(ctx, Type.BOOL);
        } else {
            set(ctx, Type.ERR);
        }
    }

    @Override
    public void exitHat(TypesParser.HatContext ctx) {
        if (val(ctx.type(0)) == Type.NUM && val(ctx.type(1)) == Type.NUM){
            set(ctx, Type.NUM);
        } else if (val(ctx.type(0)) == Type.STR && val(ctx.type(1)) == Type.NUM){
            set(ctx, Type.STR);
        } else {
            set(ctx, Type.ERR);
        }

    }

    @Override
    public void exitPlus(TypesParser.PlusContext ctx) {
        if (val(ctx.type(0)) == Type.NUM && val(ctx.type(1)) == Type.NUM){
            set(ctx, Type.NUM);
        } else if (val(ctx.type(0)) == Type.BOOL && val(ctx.type(1)) == Type.BOOL){
            set(ctx, Type.BOOL);
        } else if (val(ctx.type(0)) == Type.STR){
            set(ctx, Type.STR);
        } else if (val(ctx.type(1)) == Type.STR){
            set(ctx, Type.STR);
        } else {
            set(ctx, Type.ERR);
        }
    }

    private void set(ParseTree node, Type val) {
        this.types.put(node, val);
    }

    public Type val(ParseTree node) {
        return this.types.get(node);
    }
}
