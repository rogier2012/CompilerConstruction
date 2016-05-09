package pp.block2.cc.antlr;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import pp.block2.cc.SymbolFactory;

import java.math.BigInteger;

/**
 * Created by Rogier on 06-05-16.
 */
public class MyCalculator extends ArithmeticBaseListener{
    private ParseTreeProperty<BigInteger> newTree;
    private final SymbolFactory factory;
    private BigInteger expressionResult;

    public MyCalculator() {
        factory = new SymbolFactory(ArithmeticLexer.class);
    }

    public BigInteger calculate(Lexer lexer) {
        ArithmeticParser parser = new ArithmeticParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.goal();
        newTree = new ParseTreeProperty<>();
        new ParseTreeWalker().walk(this,tree);
        return expressionResult;
    }


    @Override
    public void exitGoal(ArithmeticParser.GoalContext ctx) {
        expressionResult = newTree.get(ctx.getChild(0));
    }


    @Override
    public void exitMinusrule(ArithmeticParser.MinusruleContext ctx) {
        BigInteger result = newTree.get(ctx.getChild(0)).subtract(newTree.get(ctx.getChild(2)));
        setBigInteger(ctx, result);
    }

    @Override
    public void exitPlusrule(ArithmeticParser.PlusruleContext ctx) {
        BigInteger result = newTree.get(ctx.getChild(0)).add(newTree.get(ctx.getChild(2)));
        setBigInteger(ctx, result);
    }

    @Override
    public void exitSingleruleexpr(ArithmeticParser.SingleruleexprContext ctx) {
        setBigInteger(ctx,newTree.get(ctx.getChild(0)));
    }

    @Override
    public void exitDividerule(ArithmeticParser.DivideruleContext ctx) {
        BigInteger result = newTree.get(ctx.getChild(0)).divide(newTree.get(ctx.getChild(2)));
        setBigInteger(ctx, result);
    }

    @Override
    public void exitSingleruleterm(ArithmeticParser.SingleruletermContext ctx) {
        setBigInteger(ctx,newTree.get(ctx.getChild(0)));
    }

    @Override
    public void exitMultiplyrule(ArithmeticParser.MultiplyruleContext ctx) {
        BigInteger result = newTree.get(ctx.getChild(0)).multiply(newTree.get(ctx.getChild(2)));
        setBigInteger(ctx, result);
    }

    @Override
    public void exitPowerrule(ArithmeticParser.PowerruleContext ctx) {
        BigInteger result = newTree.get(ctx.getChild(0)).pow(newTree.get(ctx.getChild(2)).intValue());
        setBigInteger(ctx, result);
    }

    @Override
    public void exitSingleruleexponent(ArithmeticParser.SingleruleexponentContext ctx) {
        setBigInteger(ctx,newTree.get(ctx.getChild(0)));
    }

    @Override
    public void exitFactor(ArithmeticParser.FactorContext ctx) {
        if (ctx.getChildCount() == 1){
            setBigInteger(ctx,newTree.get(ctx.getChild(0)));
        } else {
            setBigInteger(ctx,newTree.get(ctx.getChild(1)));
        }

    }

    @Override
    public void visitTerminal(TerminalNode node) {
        Token token = node.getSymbol();
        if (token.getType() == ArithmeticLexer.NUM){
            setBigInteger(node, new BigInteger(token.getText()));
        }

    }

    private void setBigInteger(ParseTree node,BigInteger integer){
        newTree.put(node, integer);
    }
}
