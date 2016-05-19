package pp.block4.cc.iloc;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import pp.block4.cc.ErrorListener;
import pp.block4.cc.iloc.CalcParser.CompleteContext;
import pp.iloc.Simulator;
import pp.iloc.model.*;

/** Compiler from Calc.g4 to ILOC. */
public class CalcCompiler extends CalcBaseListener {
	/** Program under construction. */
	private Program prog;
	private ParseTreeProperty<Reg> registers;
	private int registerCount;
	// Attribute maps and other fields

	/** Compiles a given expression string into an ILOC program. */
	public Program compile(String text) {
		Program result = null;
		ErrorListener listener = new ErrorListener();
		CharStream chars = new ANTLRInputStream(text);
		Lexer lexer = new CalcLexer(chars);
		lexer.removeErrorListeners();
		lexer.addErrorListener(listener);
		TokenStream tokens = new CommonTokenStream(lexer);
		CalcParser parser = new CalcParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(listener);
		ParseTree tree = parser.complete();
		if (listener.hasErrors()) {
			System.out.printf("Parse errors in %s:%n", text);
			for (String error : listener.getErrors()) {
				System.err.println(error);
			}
		} else {
			result = compile(tree);
		}
		return result;
	}

	/** Compiles a given Calc-parse tree into an ILOC program. */
	public Program compile(ParseTree tree) {
		registers = new ParseTreeProperty<>();
		registerCount = 0;
        new ParseTreeWalker().walk(this,tree);
		return prog;
	}

    @Override
    public void exitComplete(CompleteContext ctx) {
        Reg expr = getRegister(ctx.expr());
        emit(OpCode.out,expr);
    }

    @Override
    public void exitPar(CalcParser.ParContext ctx) {
        Reg expr = getRegister(ctx.expr());
        setRegister(ctx,expr);

    }

    @Override
    public void exitPlus(CalcParser.PlusContext ctx) {
        Reg register = new Reg("r" +registerCount);
        setRegister(ctx,register);
        Reg expr1 = getRegister(ctx.expr(0));
        Reg expr2 = getRegister(ctx.expr(1));
        emit(OpCode.add, expr1,expr2,register);
    }

    @Override
    public void exitMinus(CalcParser.MinusContext ctx) {
        Reg register = new Reg("r" +registerCount);
        setRegister(ctx,register);
        Reg expr1 = getRegister(ctx.expr());
        emit(OpCode.rsubI,new Num(0),expr1,register);
    }

    @Override
    public void exitTimes(CalcParser.TimesContext ctx) {
        Reg register = new Reg("r" +registerCount);
        setRegister(ctx,register);
        Reg expr1 = getRegister(ctx.expr(0));
        Reg expr2 = getRegister(ctx.expr(1));
        emit(OpCode.mult, expr1,expr2,register);
    }

    @Override
	public void exitNumber(CalcParser.NumberContext ctx) {
        Reg register = new Reg("r"+registerCount);
//        System.out.println(ctx.NUMBER().);
        setRegister(ctx, register);
        emit(OpCode.loadI, new Num(Integer.parseInt(ctx.NUMBER().toString())),register);
	}

	/** Constructs an operation from the parameters
	 * and adds it to the program under construction. */
	private void emit(OpCode opCode, Operand... args) {
		this.prog.addInstr(new Op(opCode, args));
	}


	public void setRegister(ParseTree node, Reg register){
		registers.put(node,register);
		registerCount++;
	}

    public Reg getRegister(ParseTree node){
        return registers.get(node);
    }


	/** Calls the compiler, and simulates and prints the compiled program. */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Usage: [expr]+");
			return;
		}
		CalcCompiler compiler = new CalcCompiler();
		for (String expr : args) {
			System.out.println("Processing " + expr);
			Program prog = compiler.compile(expr);
			new Simulator(prog).run();
			System.out.println(prog.prettyPrint());
		}
	}
}
