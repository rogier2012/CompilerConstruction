/**
 * 
 */
package pp.block2.cc.ll;

import pp.block2.cc.NonTerm;
import pp.block2.cc.Symbol;
import pp.block2.cc.SymbolFactory;
import pp.block2.cc.Term;

/**
 * Class containing some example grammars.
 * @author Arend Rensink
 *
 */
public class Grammars {
	/** Returns a grammar for simple English sentences. */
	public static Grammar makeSentence() {
		// Define the non-terminals
		NonTerm sent = new NonTerm("Sentence");
		NonTerm subj = new NonTerm("Subject");
		NonTerm obj = new NonTerm("Object");
		NonTerm mod = new NonTerm("Modifier");
		// Define the terminals, using the Sentence.g4 lexer grammar
		SymbolFactory fact = new SymbolFactory(Sentence.class);
		Term noun = fact.getTerminal(Sentence.NOUN);
		Term verb = fact.getTerminal(Sentence.VERB);
		Term adj = fact.getTerminal(Sentence.ADJECTIVE);
		Term end = fact.getTerminal(Sentence.ENDMARK);
		// Build the context free grammar
		Grammar g = new Grammar(sent);
		g.addRule(sent, subj, verb, obj, end);
		g.addRule(subj, noun);
		g.addRule(subj, mod, subj);
		g.addRule(obj, noun);
		g.addRule(obj, mod, obj);
		g.addRule(mod, adj);
		return g;
	}

	public static Grammar makeIf() {
        NonTerm stat = new NonTerm("Stat");
        NonTerm elsepart = new NonTerm("ElsePart");

        SymbolFactory factory = new SymbolFactory(If.class);
        Term assign = factory.getTerminal(If.ASSIGN);
        Term tif = factory.getTerminal(If.IF);
        Term expr = factory.getTerminal(If.COND);
        Term then = factory.getTerminal(If.THEN);
        Term telse = factory.getTerminal(If.ELSE);
        Grammar g = new Grammar(stat);
        g.addRule(stat, assign);
        g.addRule(stat, tif,expr,then,stat,elsepart);
        g.addRule(elsepart,telse,stat);
        g.addRule(elsepart, Symbol.EMPTY);
        return g;
    }


    public static Grammar makeLRQ(){
        NonTerm l = new NonTerm("L");
        NonTerm r = new NonTerm("R");
        NonTerm q = new NonTerm("Q");

        SymbolFactory factory = new SymbolFactory(LRQ.class);
        Term a = factory.getTerminal(LRQ.A);
        Term b = factory.getTerminal(LRQ.B);
        Term c = factory.getTerminal(LRQ.C);

        Grammar g = new Grammar(l);
        g.addRule(l,r,a);
        g.addRule(l,q,b,a);
        g.addRule(r,a,b,a);
        g.addRule(r,c,a,b,a);
        g.addRule(r,r,b,c);
        g.addRule(q,b,b,c);
        g.addRule(q,b,c);
        return g;


    }

    public static Grammar makeLRQnew(){
        NonTerm l = new NonTerm("L");
        NonTerm r = new NonTerm("R");
        NonTerm rp = new NonTerm("RPrime");
        NonTerm q = new NonTerm("Q");
        NonTerm n = new NonTerm("N");

        SymbolFactory factory = new SymbolFactory(LRQ.class);
        Term a = factory.getTerminal(LRQ.A);
        Term b = factory.getTerminal(LRQ.B);
        Term c = factory.getTerminal(LRQ.C);

        Grammar g = new Grammar(l);
        g.addRule(l,r,a);
        g.addRule(l,q,b,a);
        g.addRule(r,a,b,a,rp);
        g.addRule(r,c,a,b,a,rp);
        g.addRule(rp,b,c,rp);
        g.addRule(rp,Symbol.EMPTY);
        g.addRule(q,b,n);
        g.addRule(n,b,c);
        g.addRule(n,c);
        return g;


    }
}
