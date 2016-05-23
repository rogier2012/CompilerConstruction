package pp.block5.cc.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import pp.block5.cc.ErrorListener;
import pp.block5.cc.ParseException;

/** Prettyprints a (number, word)-sentence and sums up the numbers. */
public class NumWordProcessor extends NumWordBaseVisitor<Integer> {
	public static void main(String[] args) {
		NumWordProcessor grouper = new NumWordProcessor();
		if (args.length == 0) {
			process(grouper, "1sock2shoes 3 holes");
			process(grouper, "3 strands 10 blocks 11 weeks 15 credits");
			process(grouper, "1 2 3");
		} else {
			for (String text : args) {
				process(grouper, text);
			}
		}
	}

	private static void process(NumWordProcessor grouper, String text) {
		try {
			System.out.printf("Processing '%s':%n", text);
			int result = grouper.group(text);
			System.out.println("Total: " + result);
		} catch (ParseException exc) {
			exc.print();
		}
	}

	/** Groups a given sentence and prints it to stdout.
	 * Returns the sum of the numbers in the sentence.
	 */
	public int group(String text) throws ParseException {
		CharStream chars = new ANTLRInputStream(text);
		ErrorListener listener = new ErrorListener();
		Lexer lexer = new NumWordLexer(chars);
		lexer.removeErrorListeners();
		lexer.addErrorListener(listener);
		TokenStream tokens = new CommonTokenStream(lexer);
		NumWordParser parser = new NumWordParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(listener);
		ParseTree tree = parser.sentence();
		listener.throwException();
		return visit(tree);
	}

	@Override
	public Integer visitSentence(NumWordParser.SentenceContext ctx) {
		int childcount = ctx.number().size();
        StringBuilder stringBuilder = new StringBuilder();
        int sum = 0;
		for (int i = 0; i < childcount; i++) {
            int result = visitNumber(ctx.number(i));
			sum = sum + result;
            if (i == childcount - 1){
                stringBuilder.append(" and ");
                stringBuilder.append(result);
                stringBuilder.append(" ");
                stringBuilder.append(ctx.word(i).getText());
            } else if ( i == 0){
                stringBuilder.append(result);
                stringBuilder.append(" ");
                stringBuilder.append(ctx.word(i).getText());
            } else {
                stringBuilder.append(", ");
                stringBuilder.append(result);
                stringBuilder.append(" ");
                stringBuilder.append(ctx.word(i).getText());
            }
		}
        System.out.println(stringBuilder.toString());
        return sum;
	}

	@Override
	public Integer visitNumber(NumWordParser.NumberContext ctx) {

        return Integer.parseInt(ctx.NUMBER().getText());
	}


}
