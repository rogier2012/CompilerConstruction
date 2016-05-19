package pp.block2.cc.ll;

import pp.block2.cc.NonTerm;
import pp.block2.cc.Symbol;
import pp.block2.cc.Term;

import java.util.*;

/**
 * Created by Rogier on 25-04-16.
 */
public class MyLLCalc implements LLCalc {
    private Grammar grammar;
    private Map<Symbol, Set<Term>> first;
    private Map<NonTerm, Set<Term>> follow;
    private Map<Rule,Set<Term>> firstp;

    public MyLLCalc(Grammar grammar) {
        this.grammar = grammar;
    }

    @Override
    public Map<Symbol, Set<Term>> getFirst() {
        first = new HashMap<Symbol, Set<Term>>();
        for (Term term : grammar.getTerminals()){
            Set<Term> firstSet = new HashSet<>();
            firstSet.add(term);
            first.put(term, firstSet);
        }
        Set<Term> set1 = new HashSet<>();
        Set<Term> set2 = new HashSet<>();
        set1.add(Symbol.EMPTY);
        set2.add(Symbol.EOF);
        first.put(Symbol.EMPTY, set1);
        first.put(Symbol.EOF, set2);

        for (NonTerm nonTerm : grammar.getNonterminals()){
            first.put(nonTerm, new HashSet<Term>());
        }

        boolean changed = true;
        while(changed){
            changed = false;
            for (Rule p : grammar.getRules()){
                int k = p.getRHS().size();
                Set<Term> rhs = new HashSet<>(getFirstBetaI(1,p));
                rhs.remove(Symbol.EMPTY);
                int i = 1;
                while ( i <= k-1 &&  getFirstBetaI(i,p).contains(Symbol.EMPTY)){
                    Set<Term> firstbeta = first.get(p.getRHS().get(i +1));
                    firstbeta.remove(Symbol.EMPTY);
                    rhs.addAll(firstbeta);
                }


                if (i == k && getFirstBetaI(k,p).contains(Symbol.EMPTY)){
                    rhs.add(Symbol.EMPTY);
                }
                int sizep = first.get(p.getLHS()).size();
                first.get(p.getLHS()).addAll(rhs);
                if (first.get(p.getLHS()).size() > sizep){
                    changed = true;
                }
            }

        }

        return first;
    }

    private Set<Term> getFirstBetaI(int i ,Rule p){
        return first.get(p.getRHS().get(i-1));
    }


    @Override
    public Map<NonTerm, Set<Term>> getFollow() {
       follow = new HashMap<>();
        for (NonTerm nonTerm : grammar.getNonterminals()){
            follow.put(nonTerm, new HashSet<Term>());
        }
        follow.get(grammar.getStart()).add(Symbol.EOF);

        boolean changed = true;
        while(changed){
            changed = false;
            for (Rule p : grammar.getRules()){
                int k = p.getRHS().size();
                Set<Term> trailer = new HashSet<>(follow.get(p.getLHS()));

                for (int i = k-1; i >= 0 ; i--) {
                    Symbol betaI = p.getRHS().get(i);

                    if (betaI instanceof NonTerm){
                        int sizep = follow.get(betaI).size();

                        follow.get(betaI).addAll(trailer);

                        if (follow.get(betaI).size() > sizep){
                            changed = true;
                        }

                        if (first.get(betaI).contains(Symbol.EMPTY)){
                            trailer.addAll(first.get(betaI));
                            trailer.remove(Symbol.EMPTY);
                        } else {
                            trailer = new HashSet<>(first.get(betaI));
                        }


                    } else {
                        trailer = new HashSet<>(first.get(betaI));
                    }


                }
            }

        }

        return follow;
    }

    @Override
    public Map<Rule, Set<Term>> getFirstp() {
        firstp = new HashMap<Rule,Set<Term>>();
        for (Rule p : grammar.getRules()){
            Set<Term> firstbeta = new HashSet<>(getFirstBetaI(1,p));
            if (firstbeta.contains(Symbol.EMPTY)){
                Set<Term> followterms = follow.get(p.getLHS());
                firstbeta.addAll(followterms);
                firstp.put(p,firstbeta);
            } else{
                firstp.put(p,firstbeta);
            }


        }

        return firstp;
    }

    @Override
    public boolean isLL1() {
        this.getFirst();

        this.getFollow();

        this.getFirstp();

        boolean isLL = true;
        for (NonTerm nonTerm : grammar.getNonterminals()){
            Set<Term> terms = new HashSet<>();
            for (Rule p : grammar.getRules(nonTerm)){
                for (Term term : firstp.get(p)){
                    if (terms.contains(term)){
                        isLL = false;
                    }
                    terms.add(term);
                }
            }
        }



        return isLL;
    }
}
