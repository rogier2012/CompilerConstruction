package pp.block1.cc.dfa;

/**
 * Created by Rogier on 20-04-16 in Enschede.
 */
public class MyChecker implements Checker {

    @Override
    public boolean accepts(State start, String word) {
        char[] chars = word.toCharArray();
        State current = start;
        for (int i = 0; i < chars.length; i++) {
            if (!current.hasNext(chars[i])){
                return false;
            }
            current = current.getNext(chars[i]);
        }
        return current.isAccepting();
    }
}
