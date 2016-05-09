package pp.block3.cc.symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogier on 09-05-16 in Enschede.
 */
public class MySymbolTable implements SymbolTable {
    List<List<String>> scopes;

    public MySymbolTable() {
        this.scopes = new ArrayList<>();
        this.scopes.add(new ArrayList<>());
    }

    @Override
    public void openScope() {
        this.scopes.add(new ArrayList<>());
    }

    @Override
    public void closeScope() {
        if (this.scopes.size() == 1){
            throw new RuntimeException();
        }
        this.scopes.remove(this.scopes.size()-1);
    }

    @Override
    public boolean add(String id) {
        if (this.scopes.get(this.scopes.size()-1).contains(id)){
            return false;
        } else {
            this.scopes.get(this.scopes.size()-1).add(id);
            return true;
        }
    }

    @Override
    public boolean contains(String id) {
        for (List<String> list : this.scopes){
            if (list.contains(id)){
                return true;
            }
        }

        return false;
    }
}
