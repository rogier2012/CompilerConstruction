package pp.block3.cc.test;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import pp.block3.cc.symbol.DeclUse;
import pp.block3.cc.symbol.DeclUseLexer;
import pp.block3.cc.symbol.DeclUseParser;
import pp.block3.cc.tabular.MyErrorListener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static org.junit.Assert.assertEquals;

/**
 * Created by Rogier on 09-05-16 in Enschede
 */
public class DeclUseTest {
    private DeclUse declUse = new DeclUse();


    @Test
    public void test(){

        try {

            test(0, new FileReader("src/pp/block3/cc/symbol/program1.txt"));
            test(1, new FileReader("src/pp/block3/cc/symbol/program2.txt"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void test(int errors, Reader file){
        ParseTree tree = parseDeclUse(file);
        this.declUse.init();
        new ParseTreeWalker().walk(declUse,tree);
        assertEquals(errors, this.declUse.getErrors().size());
    }


    private ParseTree parseDeclUse(Reader file) {
        CharStream chars = null;
        try {
            chars = new ANTLRInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Lexer lexer = new DeclUseLexer(chars);
        TokenStream tokens = new CommonTokenStream(lexer);
        DeclUseParser parser = new DeclUseParser(tokens);
        parser.removeErrorListeners();
        MyErrorListener error = new MyErrorListener();
        parser.addErrorListener(error);
        System.out.println(error.getErrors());
        return parser.program();
    }





}
