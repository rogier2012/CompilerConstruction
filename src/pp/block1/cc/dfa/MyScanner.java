package pp.block1.cc.dfa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rogier on 20-04-16 in Enschede.
 */
public class MyScanner implements Scanner {

    @Override
    public List<String> scan(State dfa, String text) {
        State start = dfa;
        ArrayList<String> words = new ArrayList<>();
        while (!text.equals("")){

            String result = "";
            ArrayList<State> stack = new ArrayList<>();
            char[] chars = text.toCharArray();
            State current = start;
            stack.add(current);
            int i = 0;
            while (i < chars.length && current.hasNext(chars[i])){
                result = result + chars[i];
                current = current.getNext(chars[i]);
                if (current.isAccepting()){
                    stack = new ArrayList<>();
                }
                stack.add(current);
                i++;
            }
            while (stack.size() > 1){
                result = result.substring(0,result.length()-1);
                stack.remove(stack.size() -1);
            }

            text = text.substring(result.length());
            if (result.length() == 0){
                return null;
            } else {
                words.add(result);
            }
        }

        return words;
    }
}
