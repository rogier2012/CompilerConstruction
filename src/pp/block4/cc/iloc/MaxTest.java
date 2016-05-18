package pp.block4.cc.iloc;

import pp.iloc.Assembler;
import pp.iloc.Simulator;
import pp.iloc.eval.Machine;
import pp.iloc.model.Program;
import pp.iloc.parse.FormatException;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.fail;

/**
 * Created by Rogier on 18-05-16 in Enschede.
 */
public class MaxTest {
    private final static String BASE_DIR = "./src/pp/block4/cc/iloc/";


    public static void main(String[] args) {
        MaxTest maxTest = new MaxTest();
        maxTest.fibMMTest();
    }

    public void maxTest(){
        Program program = parse("max");
        System.out.println(program.prettyPrint());
        Machine c = new Machine();
        c.init("a", 2,3,4,3,34,4);
        c.init("alength", 6);

        new Simulator(program,c).run();
        System.out.println(c);

    }

    public void fibRRTest(){
        Program program = parse("fibrr");
        System.out.println(program.prettyPrint());
        Machine c = new Machine();
        c.setNum("n",106);


        new Simulator(program,c).run();
        System.out.println(c);

    }

    public void fibMMTest(){
        Program program = parse("fibmm");
        System.out.println(program.prettyPrint());
        Machine c = new Machine();
        c.init("n",48);
        c.init("x",1);
        c.init("y",1);
        c.init("z",1);

        new Simulator(program,c).run();
        System.out.println(c);

    }


    Program parse(String filename) {
        File file = new File(filename + ".iloc");
        if (!file.exists()) {
            file = new File(BASE_DIR + filename + ".iloc");
        }
        try {
            return Assembler.instance().assemble(file);
        } catch (FormatException | IOException e) {
            fail(e.getMessage());
            return null;
        }
    }
}
