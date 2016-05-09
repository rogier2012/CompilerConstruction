package pp.block3.cc.symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogier on 09-05-16 in Enschede.
 */
public class DeclUse extends DeclUseBaseListener {
    private SymbolTable symbolTable;
    private List<String> errors;

    public void init(){
        symbolTable = new MySymbolTable();
        errors = new ArrayList<>();
    }


    @Override
    public void enterSeries(DeclUseParser.SeriesContext ctx) {
        symbolTable.openScope();
    }

    @Override
    public void exitSeries(DeclUseParser.SeriesContext ctx) {
        symbolTable.closeScope();
    }


    @Override
    public void exitDecl(DeclUseParser.DeclContext ctx) {
        if (!symbolTable.add(ctx.ID().getText())){
            errors.add("Double declaration at line " + ctx.ID().getSymbol().getLine()
                    + " at " + ctx.ID().getSymbol().getCharPositionInLine());
        }


    }

    @Override
    public void exitUse(DeclUseParser.UseContext ctx) {
        if (!symbolTable.contains(ctx.ID().getText())){
            errors.add("No declaration for " + ctx.ID().getText() + " at line " + ctx.ID().getSymbol().getLine()
                    + " at " + ctx.ID().getSymbol().getCharPositionInLine());
        }
    }

    public List<String> getErrors() {
        return errors;
    }
}
