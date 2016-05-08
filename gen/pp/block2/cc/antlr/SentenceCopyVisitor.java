// Generated from /Users/Rogier/Library/Mobile Documents/com~apple~CloudDocs/Universiteit Twente/Programming Paradigms/Compiler Construction/CC_1/src/pp/block2/cc/antlr/SentenceCopy.g4 by ANTLR 4.5.1
package pp.block2.cc.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SentenceCopyParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SentenceCopyVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SentenceCopyParser#sentence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSentence(SentenceCopyParser.SentenceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code modSubject}
	 * labeled alternative in {@link SentenceCopyParser#subject}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModSubject(SentenceCopyParser.ModSubjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleSubject}
	 * labeled alternative in {@link SentenceCopyParser#subject}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleSubject(SentenceCopyParser.SimpleSubjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link SentenceCopyParser#object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject(SentenceCopyParser.ObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link SentenceCopyParser#modifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifier(SentenceCopyParser.ModifierContext ctx);
}