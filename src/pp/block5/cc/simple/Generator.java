package pp.block5.cc.simple;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;
import pp.block5.cc.pascal.SimplePascalBaseVisitor;
import pp.block5.cc.pascal.SimplePascalParser;
import pp.iloc.Simulator;
import pp.iloc.model.*;
/** Class to generate ILOC code for Simple Pascal. */
public class Generator extends SimplePascalBaseVisitor<Op> {
	/** The representation of the boolean value <code>false</code>. */
	public final static Num FALSE_VALUE = new Num(Simulator.FALSE);
	/** The representation of the boolean value <code>true</code>. */
	public final static Num TRUE_VALUE = new Num(Simulator.TRUE);

	/** The base register. */
	private Reg arp = new Reg("r_arp");
	/** The outcome of the checker phase. */
	private Result checkResult;
	/** Association of statement nodes to labels. */
	private ParseTreeProperty<Label> labels;
	/** The program being built. */
	private Program prog;
	/** Register count, used to generate fresh registers. */
	private int regCount;
	/** Association of expression and target nodes to registers. */
	private ParseTreeProperty<Reg> regs;

    private int ifcount;

    private int whilecount;


	/** Generates ILOC code for a given parse tree,
	 * given a pre-computed checker result.
	 */
	public Program generate(ParseTree tree, Result checkResult) {
		this.prog = new Program();
		this.checkResult = checkResult;
		this.regs = new ParseTreeProperty<>();
		this.labels = new ParseTreeProperty<>();
		this.regCount = 0;
        this.ifcount = 0;
        this.whilecount = 0;
		tree.accept(this);
		return this.prog;
	}



    @Override
    public Op visitMultExpr(SimplePascalParser.MultExprContext ctx) {
        Label label = null;
        if (hasLabel(ctx)){
            label = labels.get(ctx);
        }
        labels.put(ctx.expr(0),label);
        visit(ctx.expr(0));
        visit(ctx.expr(1));
        int ruleindex = ((TerminalNode) ctx.multOp().getChild(0)).getSymbol().getType();
        OpCode opCode;
        if (ruleindex == SimplePascalParser.STAR){
            opCode = OpCode.mult;
        } else {
            opCode = OpCode.div;
        }
        return emit(opCode,reg(ctx.expr(0)), reg(ctx.expr(1)), reg(ctx));
    }

    @Override
    public Op visitPlusExpr(SimplePascalParser.PlusExprContext ctx) {
        Label label = null;
        if (hasLabel(ctx)){
            label = labels.get(ctx);
        }
        labels.put(ctx.expr(0),label);
        visit(ctx.expr(0));
        visit(ctx.expr(1));
        int ruleIndex = ((TerminalNode) ctx.plusOp().getChild(0)).getSymbol().getType();
        OpCode opCode;
        if (ruleIndex == SimplePascalParser.PLUS){
            opCode = OpCode.add;
        } else {
            opCode = OpCode.sub;
        }

        return emit(opCode,reg(ctx.expr(0)), reg(ctx.expr(1)), reg(ctx));
    }

    @Override
    public Op visitPrfExpr(SimplePascalParser.PrfExprContext ctx) {
        Label label = null;
        if (hasLabel(ctx)){
            label = labels.get(ctx);
        }
        labels.put(ctx.expr(),label);
        visit(ctx.expr());
        Op operation;
        if (((TerminalNode) ctx.prfOp().getChild(0)).getSymbol().getType() == SimplePascalParser.MINUS){
            operation = emit(OpCode.rsubI,reg(ctx.expr()),new Num(0),reg(ctx));
        } else {
            operation = emit(OpCode.rsubI,reg(ctx.expr()),new Num(1),reg(ctx));
        }
        return operation;
    }

    @Override
    public Op visitBoolExpr(SimplePascalParser.BoolExprContext ctx) {
        Label label = null;
        if (hasLabel(ctx)){
            label = labels.get(ctx);
        }
        labels.put(ctx.expr(0),label);
        visit(ctx.expr(0));
        visit(ctx.expr(1));

        int ruleIndex = ((TerminalNode) ctx.boolOp().getChild(0)).getSymbol().getType();
        OpCode opCode;
        if (ruleIndex == SimplePascalParser.AND){
            opCode = OpCode.and;
        } else {
            opCode = OpCode.or;
        }

        return emit(opCode,reg(ctx.expr(0)), reg(ctx.expr(1)), reg(ctx));


    }

    @Override
    public Op visitCompExpr(SimplePascalParser.CompExprContext ctx) {
        Label label = null;
        if (hasLabel(ctx)){
            label = labels.get(ctx);
        }
        labels.put(ctx.expr(0),label);
        visit(ctx.expr(0));
        visit(ctx.expr(1));

        int ruleIndex = ((TerminalNode) ctx.compOp().getChild(0)).getSymbol().getType();
        OpCode opCode = null;
        switch (ruleIndex){
            case SimplePascalParser.LE:
                opCode = OpCode.cmp_LE;
                break;
            case SimplePascalParser.LT:
                opCode = OpCode.cmp_LT;
                break;
            case SimplePascalParser.GE:
                opCode = OpCode.cmp_GE;
                break;
            case SimplePascalParser.GT:
                opCode = OpCode.cmp_GT;
                break;
            case SimplePascalParser.EQ:
                opCode = OpCode.cmp_EQ;
                break;
            case SimplePascalParser.NE:
                opCode = OpCode.cmp_NE;
                break;

        }

        return emit(opCode, reg(ctx.expr(0)),reg(ctx.expr(1)),reg(ctx));
    }

    @Override
    public Op visitParExpr(SimplePascalParser.ParExprContext ctx) {
        if (hasLabel(ctx)){
            labels.put(ctx.expr(),labels.get(ctx));
        }

        return super.visitParExpr(ctx);
    }

    @Override
	public Op visitIfStat(SimplePascalParser.IfStatContext ctx) {
        Label label = null;
        if (hasLabel(ctx)){
            label = labels.get(ctx);
        }
        labels.put(ctx.expr(), label);
        ifcount++;
        int statCount = ctx.stat().size();
        Label label1 = new Label("then"+ifcount);
        labels.put(ctx.stat(0),label1);
        Label label3 = new Label("endif"+ifcount);
        Label label2 = null;
        if (statCount == 2){
            label2 = new Label("else"+ifcount);
            labels.put(ctx.stat(1),label2);
        } else {
            label2 = label3;
        }
        visit(ctx.expr());
        emit(OpCode.cbr,reg(ctx.expr()),label1, label2);
        for (int i = 0; i < statCount; i++) {
            visit(ctx.stat(i));
        }
        return emit(label3, OpCode.nop);
	}

    @Override
    public Op visitAssStat(SimplePascalParser.AssStatContext ctx) {

        Label label = null;
        if (hasLabel(ctx)){
            label = labels.get(ctx);
        }
        labels.put(ctx.expr(),label);
        visit(ctx.expr());
        return emit( OpCode.storeAI, reg(ctx.expr()), arp, new Num(checkResult.getOffset(ctx)));
    }

    @Override
    public Op visitWhileStat(SimplePascalParser.WhileStatContext ctx) {
        whilecount++;
        Label label1 = new Label("while" + whilecount);
        labels.put(ctx.expr(),label1);
        Label label2 = new Label("body" + whilecount);
        labels.put(ctx.stat(),label2);
        Label label3 = new Label("endwhile" + whilecount);
        visit(ctx.expr());
        emit(OpCode.cbr,reg(ctx.expr()),label2,label3);
        visit(ctx.stat());
        emit(OpCode.jumpI,label1);
        return emit(label3,OpCode.nop);
    }

    @Override
    public Op visitInStat(SimplePascalParser.InStatContext ctx) {
        Label label = null;
        if (hasLabel(ctx)){
            label = labels.get(ctx);
        }
        emit(label,OpCode.in, new Str(ctx.STR().getText()),reg(ctx));
        return emit(OpCode.storeAI,reg(ctx), arp, new Num(checkResult.getOffset(ctx.target())));

    }

    @Override
    public Op visitOutStat(SimplePascalParser.OutStatContext ctx) {
        Label label = null;
        if (hasLabel(ctx)){
            label = labels.get(ctx);
        }
        visit(ctx.expr());
        return emit(label,OpCode.out,new Str(ctx.STR().getText()),reg(ctx.expr()));

    }

    @Override
    public Op visitBlockStat(SimplePascalParser.BlockStatContext ctx) {
        if (hasLabel(ctx)){
            labels.put(ctx.block().stat(0),labels.get(ctx));
        }
        return super.visitBlockStat(ctx);
    }

    @Override
    public Op visitNumExpr(SimplePascalParser.NumExprContext ctx) {
        Label label = null;
        if (hasLabel(ctx)){
            label = labels.get(ctx);
        }
        return emit(label,OpCode.loadI,new Num(Integer.parseInt(ctx.NUM().getText())),reg(ctx));
    }

    @Override
    public Op visitIdExpr(SimplePascalParser.IdExprContext ctx) {
        Label label = null;
        if (hasLabel(ctx)){
            label = labels.get(ctx);
        }
        return emit(label,OpCode.loadAI,arp,new Num(checkResult.getOffset(ctx)),reg(ctx));
    }

    @Override
    public Op visitFalseExpr(SimplePascalParser.FalseExprContext ctx) {
        Label label = null;
        if (hasLabel(ctx)){
            label = labels.get(ctx);
        }
        return emit(label,OpCode.loadI,FALSE_VALUE,reg(ctx));
    }

    @Override
    public Op visitTrueExpr(SimplePascalParser.TrueExprContext ctx) {
        Label label = null;
        if (hasLabel(ctx)){
            label = labels.get(ctx);
        }
        return emit(label,OpCode.loadI,TRUE_VALUE,reg(ctx));
    }

    // Override the visitor methods
	/** Constructs an operation from the parameters 
	 * and adds it to the program under construction. */
	private Op emit(Label label, OpCode opCode, Operand... args) {
		Op result = new Op(label, opCode, args);
		this.prog.addInstr(result);
		return result;
	}

	/** Constructs an operation from the parameters 
	 * and adds it to the program under construction. */
	private Op emit(OpCode opCode, Operand... args) {
		return emit((Label) null, opCode, args);
	}

	/** 
	 * Looks up the label for a given parse tree node,
	 * creating it if none has been created before.
	 * The label is actually constructed from the entry node
	 * in the flow graph, as stored in the checker result.
	 */
	private Label label(ParserRuleContext node) {
		Label result = this.labels.get(node);
		if (result == null) {
			ParserRuleContext entry = this.checkResult.getEntry(node);
			result = createLabel(entry, "n");
			this.labels.put(node, result);
		}
		return result;
	}

	/** Creates a label for a given parse tree node and prefix. */
	private Label createLabel(ParserRuleContext node, String prefix) {
		Token token = node.getStart();
		int line = token.getLine();
		int column = token.getCharPositionInLine();
		String result = prefix + "_" + line + "_" + column;
		return new Label(result);
	}

	/** Retrieves the offset of a variable node from the checker result,
	 * wrapped in a {@link Num} operand. */
	private Num offset(ParseTree node) {
		return new Num(this.checkResult.getOffset(node));
	}

	/** Returns a register for a given parse tree node,
	 * creating a fresh register if there is none for that node. */
	private Reg reg(ParseTree node) {
		Reg result = this.regs.get(node);
		if (result == null) {
			result = new Reg("r_" + this.regCount);
			this.regs.put(node, result);
			this.regCount++;
		}
		return result;
	}

    public boolean hasLabel(ParseTree node){
        return labels.get(node) != null;
    }

	/** Assigns a register to a given parse tree node. */
	private void setReg(ParseTree node, Reg reg) {
		this.regs.put(node, reg);
	}
}
