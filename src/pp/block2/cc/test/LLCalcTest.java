package pp.block2.cc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import pp.block2.cc.NonTerm;
import pp.block2.cc.Symbol;
import pp.block2.cc.SymbolFactory;
import pp.block2.cc.Term;
import pp.block2.cc.ll.*;
import pp.block2.cc.ll.If;

public class LLCalcTest {
	/** Tests the LL-calculator for the Sentence grammar. */
	@Test
	public void testSentenceOrig() {
		Grammar g = Grammars.makeSentence();
		// Without the last (recursive) rule, the grammar is LL-1
		assertTrue(createCalc(g).isLL1());
	}

	@Test
	public void testSentenceExtended() {
		Grammar g = Grammars.makeSentence();
		// Without the last (recursive) rule, the grammar is LL-1
		assertTrue(createCalc(g).isLL1());
		// Now add the last rule, causing the grammar to fail
		// Define the non-terminals
		NonTerm subj = g.getNonterminal("Subject");
		NonTerm obj = g.getNonterminal("Object");
		NonTerm sent = g.getNonterminal("Sentence");
		NonTerm mod = g.getNonterminal("Modifier");
		g.addRule(mod, mod, mod);
		// Define the terminals
		Term adj = g.getTerminal(Sentence.ADJECTIVE);
		Term noun = g.getTerminal(Sentence.NOUN);
		Term verb = g.getTerminal(Sentence.VERB);
		Term end = g.getTerminal(Sentence.ENDMARK);
		LLCalc calc = createCalc(g);
		// FIRST sets
		Map<Symbol, Set<Term>> first = calc.getFirst();
		assertEquals(set(adj, noun), first.get(sent));
		assertEquals(set(adj, noun), first.get(subj));
		assertEquals(set(adj, noun), first.get(obj));
		assertEquals(set(adj), first.get(mod));
		// FOLLOW sets
		Map<NonTerm, Set<Term>> follow = calc.getFollow();
		assertEquals(set(Symbol.EOF), follow.get(sent));
		assertEquals(set(verb), follow.get(subj));
		assertEquals(set(end), follow.get(obj));
		assertEquals(set(noun, adj), follow.get(mod));
		// FIRST+ sets: test per rule
		Map<Rule, Set<Term>> firstp = calc.getFirstp();
		List<Rule> subjRules = g.getRules(subj);
		assertEquals(set(noun), firstp.get(subjRules.get(0)));
		assertEquals(set(adj), firstp.get(subjRules.get(1)));
		// is-LL1-test
		assertFalse(calc.isLL1());
	}

//    @Test
//    public void testIfOrig() {
//        Grammar grammar = Grammars.makeIf();
//
//        assertTrue(createCalc(grammar).isLL1());
//    }


    @Test
    public void testIf () {
        Grammar g = Grammars.makeIf(); // to be defined (Ex. 2-CC.6)
        NonTerm stat = g.getNonterminal("Stat");
        NonTerm elsePart = g.getNonterminal("ElsePart");
        Term assign = g.getTerminal(If.ASSIGN);
        Term tif = g.getTerminal(If.IF);
        Term expr =  g.getTerminal(If.COND);
        Term then = g.getTerminal(If.THEN);
        Term telse = g.getTerminal(If.ELSE);
        Term eof = Symbol.EOF;
        Term empty = Symbol.EMPTY;
        LLCalc  calc = createCalc(g);
        Map<Symbol, Set<Term>> first = calc.getFirst();
        assertEquals(set(assign, tif), first.get(stat));
        assertEquals(set(telse, empty), first.get(elsePart));

        Map<NonTerm, Set<Term>> follow = calc.getFollow();
        assertEquals(set(eof, telse), follow.get(stat));
        assertEquals(set(eof, telse), follow.get(elsePart));

        Map<Rule, Set<Term>> firstp = calc.getFirstp();
        List<Rule> statRules = g.getRules(stat);
        List<Rule> elsePartRules = g.getRules(elsePart);
		assertEquals(set(telse), firstp.get(elsePartRules.get(0)));
		assertEquals(set(assign), firstp.get(statRules.get(0)));

        assertEquals(set(telse), firstp.get(elsePartRules.get(0)));
        assertEquals(set(telse, empty, eof), firstp.get(elsePartRules.get(1)));

        assertEquals(set(assign), firstp.get(statRules.get(0)));
        assertEquals(set(tif), firstp.get(statRules.get(1)));

        assertFalse(calc.isLL1());
    }

	@Test
    public void testLRQ(){
        Grammar grammar = Grammars.makeLRQ();
        NonTerm l = new NonTerm("L");
        NonTerm r = new NonTerm("R");
        NonTerm q = new NonTerm("Q");

        Term a = grammar.getTerminal(LRQ.A);
        Term b = grammar.getTerminal(LRQ.B);
        Term c = grammar.getTerminal(LRQ.C);

        LLCalc calc = createCalc(grammar);
        Map<Symbol, Set<Term>> first = calc.getFirst();
        Map<NonTerm, Set<Term>> follow = calc.getFollow();
        Map<Rule, Set<Term>> firstp = calc.getFirstp();

        assertFalse(createCalc(grammar).isLL1());

    }

    @Test
    public void testLRQnew(){
        Grammar grammar = Grammars.makeLRQnew();

        assertTrue(createCalc(grammar).isLL1());

    }





	/** Creates an LL1-calculator for a given grammar. */
	private LLCalc createCalc(Grammar g) {
		return new MyLLCalc(g); // your implementation of LLCalc (Ex. 2-CC.5)
	}

	@SuppressWarnings("unchecked")
	private <T> Set<T> set(T... elements) {
		return new HashSet<>(Arrays.asList(elements));
	}
}
