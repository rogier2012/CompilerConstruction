package pp.s1542664.q2_3;

import org.junit.Test;
import pp.iloc.Assembler;
import pp.iloc.model.Program;
import pp.iloc.parse.FormatException;
import pp.s1542664.q2.Graph;
import pp.s1542664.q2.Node;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Rogier on 12-06-16 in Enschede.
 */
public class ILOC2DDGTest {
    private ILOC2DDG iloc2DDG;
    private static String FILE_PATH = "src/pp/s1542664/";

    public ILOC2DDGTest() {
        iloc2DDG = ILOC2DDG.instance();
    }

    public static Program createProgram(String filename){
        Program prog = null;
        try {
            prog = Assembler.instance().assemble(new File(FILE_PATH + filename));
        } catch (FormatException | IOException exc) {
            exc.printStackTrace();
        }
        return prog;
    }

    @Test
    public void test1(){
        Program program = createProgram("q2_2/test1.iloc");
        Graph graph = iloc2DDG.convert(program);
        assertEquals(6, graph.size());
        List<Node> nodes = graph.getNodes();
        assertEquals(0,nodes.get(0).getEdges().size());
        assertEquals(0,nodes.get(1).getEdges().size());
        assertEquals(2,nodes.get(2).getEdges().size());
        assertEquals(1,nodes.get(3).getEdges().size());
        assertEquals(1,nodes.get(4).getEdges().size());
        assertEquals(2,nodes.get(5).getEdges().size());
    }

    @Test
    public void test2(){
        Program program = createProgram("q2_2/test2.iloc");
        Graph graph = iloc2DDG.convert(program);
        assertEquals(5, graph.size());
        List<Node> nodes = graph.getNodes();
        assertEquals(0,nodes.get(0).getEdges().size());
        assertEquals(1,nodes.get(1).getEdges().size());
        assertEquals(0,nodes.get(2).getEdges().size());
        assertEquals(1,nodes.get(3).getEdges().size());
        assertEquals(1,nodes.get(4).getEdges().size());
    }

    @Test
    public void test3(){
        Program program = createProgram("q2_2/test3.iloc");
        Graph graph = iloc2DDG.convert(program);
        assertEquals(7, graph.size());
        List<Node> nodes = graph.getNodes();
        assertEquals(0,nodes.get(0).getEdges().size());
        assertEquals(0,nodes.get(1).getEdges().size());
        assertEquals(2,nodes.get(2).getEdges().size());
        assertEquals(2,nodes.get(3).getEdges().size());
        assertEquals(1,nodes.get(4).getEdges().size());
        assertEquals(2,nodes.get(5).getEdges().size());
        assertEquals(1,nodes.get(6).getEdges().size());
    }

}
