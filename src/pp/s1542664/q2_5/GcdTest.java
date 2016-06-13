package pp.s1542664.q2_5;

import org.junit.Test;
import pp.iloc.Assembler;
import pp.iloc.Simulator;
import pp.iloc.model.Program;
import pp.iloc.parse.FormatException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by Rogier on 13-06-16 in Enschede.
 */
public class GcdTest {
    public static final String GCD_FILE = "src/pp/s1542664/q2_5/gcd.iloc";

    @Test
    public void testGcd(){
        assertArrayEquals(GCDJava(99,97), GCDIloc(GCD_FILE,"99\n97"));
        assertArrayEquals(GCDJava(9872,3242), GCDIloc(GCD_FILE,"9872\n3242"));
        assertArrayEquals(GCDJava(4,2), GCDIloc(GCD_FILE,"4\n2"));
        assertArrayEquals(GCDJava(1337,42), GCDIloc(GCD_FILE,"1337\n42"));
        assertArrayEquals(GCDJava(42,56), GCDIloc(GCD_FILE,"42\n56"));
        assertArrayEquals(GCDJava(345,135), GCDIloc(GCD_FILE,"345\n135"));
    }

    public static int[] GCDIloc(String filename, String numbers){
        int[] result = new int[2];
        try {
            Program prog = Assembler.instance().assemble(new File(filename));
            Simulator simulator = new Simulator(prog);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            simulator.setIn(new ByteArrayInputStream(numbers.getBytes()));
            simulator.setOut(outputStream);
            simulator.run();
            System.out.println(outputStream.toString());
            String string = outputStream.toString();
            string = string.replaceAll("[^0-9\n]","");
            Scanner scanner = new Scanner(string);
            result[0] = scanner.nextInt();
            result[1] = scanner.nextInt();

        } catch (FormatException | IOException exc) {
            exc.printStackTrace();
        }
        return result;
    }


    public static int[] GCDJava(int a1, int a2){
        GCD.count = 0;
        int[] result = new int[2];
        result[0]=  GCD.gcd(a1,a2);
        result[1] = GCD.count;
        return result;


    }
}
