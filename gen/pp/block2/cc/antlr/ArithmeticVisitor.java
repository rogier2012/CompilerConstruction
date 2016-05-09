// Generated from /Users/Rogier/Library/Mobile Documents/com~apple~CloudDocs/Universiteit Twente/Programming Paradigms/Compiler Construction/CC_1/src/pp/block2/cc/antlr/Arithmetic.g4 by ANTLR 4.5.1
package pp.block2.cc.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ArithmeticParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ArithmeticVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ArithmeticParser#goal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGoal(ArithmeticParser.GoalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code minusrule}
	 * labeled alternative in {@link ArithmeticParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinusrule(ArithmeticParser.MinusruleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code plusrule}
	 * labeled alternative in {@link ArithmeticParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlusrule(ArithmeticParser.PlusruleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code singleruleexpr}
	 * labeled alternative in {@link ArithmeticParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleruleexpr(ArithmeticParser.SingleruleexprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dividerule}
	 * labeled alternative in {@link ArithmeticParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDividerule(ArithmeticParser.DivideruleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code singleruleterm}
	 * labeled alternative in {@link ArithmeticParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleruleterm(ArithmeticParser.SingleruletermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code multiplyrule}
	 * labeled alternative in {@link ArithmeticParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplyrule(ArithmeticParser.MultiplyruleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code powerrule}
	 * labeled alternative in {@link ArithmeticParser#exponent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPowerrule(ArithmeticParser.PowerruleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code singleruleexponent}
	 * labeled alternative in {@link ArithmeticParser#exponent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleruleexponent(ArithmeticParser.SingleruleexponentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ArithmeticParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(ArithmeticParser.FactorContext ctx);
}