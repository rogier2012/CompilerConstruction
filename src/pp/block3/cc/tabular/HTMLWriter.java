package pp.block3.cc.tabular;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import pp.block2.cc.antlr.SentenceParser;
import pp.block3.cc.symbol.DeclUseLexer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Rogier on 09-05-16 in Enschede.
 */
public class HTMLWriter extends LatexBaseListener {
    private ParseTreeProperty<String> htmlTree;

    public String parseHTML(Lexer lexer){
        LatexParser parser = new LatexParser(new CommonTokenStream(lexer));
        MyErrorListener errorListener = new MyErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        ParseTree tree = parser.table();
        if (errorListener.getErrors().size() > 0){
            return errorListener.getErrors().toString();
        }
        htmlTree = new ParseTreeProperty<>();
        new ParseTreeWalker().walk(this,tree);

        return val(tree);
    }

    @Override
    public void enterTable(LatexParser.TableContext ctx) {
        String result = "<html>\n" +
                "<body>\n" +
                "<table border=\"1\">\n";
        set(ctx,result);
    }

    @Override
    public void exitTable(LatexParser.TableContext ctx) {
        String result = val(ctx);
        for (LatexParser.RowContext row : ctx.row()){
            result = result + val(row);
        }
        result = result + "</table>\n" +
                " </body>\n </html>";
        set(ctx,result);
    }

    @Override
    public void enterRow(LatexParser.RowContext ctx) {
        String result = "<tr>\n";
        set(ctx,result);
    }

    @Override
    public void exitRow(LatexParser.RowContext ctx) {
        String result = val(ctx);
        for (int i = 0; i < ctx.getChildCount(); i++) {
            result = result + val(ctx.rowentry(i));
        }
        result = result + "</tr>\n";
        set(ctx,result);
    }

    @Override
    public void exitRowentry(LatexParser.RowentryContext ctx) {
        String result = "<td>"+ctx.ENTRY().getText() +"</td>\n";
        set(ctx,result);
    }

    private void set(ParseTree node, String text){
        htmlTree.put(node,text);
    }

    public String val(ParseTree node){
        return htmlTree.get(node);
    }

    public static void main(String[] args) {
        FileReader file = null;
        try {
            file = new FileReader("src/pp/block3/cc/tabular/tabular-1.tex");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        CharStream chars = null;
        FileWriter writer = null;
        try {
            chars = new ANTLRInputStream(file);
            writer = new FileWriter("index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Lexer lexer = new LatexLexer(chars);
        HTMLWriter htmlwriter = new HTMLWriter();
        String html = htmlwriter.parseHTML(lexer);
        try {
            writer.write(html);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
