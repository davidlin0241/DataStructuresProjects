package graph;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class TraversalTest {
    DirectedGraph directed = new DirectedGraph();
    UndirectedGraph undirected = new UndirectedGraph();
    BreadthFirstTraversal bft;
    DepthFirstTraversal dft;
    ArrayList<Integer> visited, fringe = new ArrayList<>(),
            correct = new ArrayList<>();

    @Test
    public void testBFTDirected() {
        setUp();

        correct.add(1);
        correct.add(2);
        correct.add(5);
        correct.add(6);
        correct.add(4);
        correct.add(3);

        bft = new BreadthFirstTraversal(directed);
        visited = bft.traverseTester(fringe, false);

        for (int i = 0; i < 6; i++) {
            assertTrue(visited.get(i) == correct.get(i));
        }
    }

    @Test
    public void testDFTPre() {
        setUp();

        correct.add(1);
        correct.add(6);
        correct.add(5);
        correct.add(2);
        correct.add(4);
        correct.add(3);

        dft = new DepthFirstTraversal(directed);
        visited = dft.traverseTester(fringe, false);

        for (int i = 0; i < 6; i++) {
            assertTrue(visited.get(i) == correct.get(i));
        }
    }

    @Test
    public void testDFTPost() {
        setUp();

        correct.add(5);
        correct.add(6);
        correct.add(3);
        correct.add(4);
        correct.add(2);
        correct.add(1);

        dft = new DepthFirstTraversal(directed);
        visited = dft.traverseTester(fringe, true);

        for (int i = 0; i < 6; i++) {
            assertTrue(visited.get(i) == correct.get(i));
        }
    }

    public void setUp() {
        fringe.add(1);

        for (int i = 1; i < 7; i++) {
            directed.add();
        }

        directed.add(1, 2);
        directed.add(1, 5);
        directed.add(1, 6);
        directed.add(2, 4);
        directed.add(4, 3);
        directed.add(4, 5);
        directed.add(6, 5);
    }

    @Test
    public void aGDFT() {

        for (int i = 1; i < 11; i++) {
            directed.add();
        }

        directed.add(1, 2);
        directed.add(1, 3);
        directed.add(1, 4);
        directed.add(2, 3);
        directed.add(2, 5);
        directed.add(2, 6);
        directed.add(3, 7);
        directed.add(3, 8);
        directed.add(8, 1);
        directed.add(8, 9);
        directed.add(8, 10);
        directed.add(10, 7);

        fringe.add(1);
        dft = new DepthFirstTraversal(directed);
        dft.traverseTester(fringe, true);
        assertEquals(" 1 4 <4> 3 8 10 7 <7> <10> 9 <9> "
                + "<8> <3> 2 6 <6> 5 <5> <2> <1>", dft.getVisited());
    }

    @Test
    public void aGDFTSelf() {
        for (int i = 1; i < 11; i++) {
            directed.add();
        }

        directed.add(1, 1);
        directed.add(1, 2);
        directed.add(1, 3);
        directed.add(1, 4);
        directed.add(2, 3);
        directed.add(2, 5);
        directed.add(2, 6);
        directed.add(3, 7);
        directed.add(3, 8);
        directed.add(8, 1);
        directed.add(8, 8);
        directed.add(8, 9);
        directed.add(8, 10);
        directed.add(10, 7);

        fringe.add(1);
        dft = new DepthFirstTraversal(directed);
        dft.traverseTester(fringe, true);
        assertEquals(" 1 4 <4> 3 8 10 7 <7> <10> 9 <9> "
                + "<8> <3> 2 6 <6> 5 <5> <2> <1>", dft.getVisited());
    }

    @Test
    public void aGDFTU() {

        for (int i = 1; i < 11; i++) {
            directed.add();
        }

        directed.add(1, 2);
        directed.add(1, 3);
        directed.add(1, 4);
        directed.add(1, 8);
        directed.add(2, 1);
        directed.add(2, 3);
        directed.add(2, 5);
        directed.add(2, 6);
        directed.add(3, 1);
        directed.add(3, 2);
        directed.add(3, 7);
        directed.add(3, 8);
        directed.add(4, 1);
        directed.add(5, 2);
        directed.add(6, 2);
        directed.add(7, 3);
        directed.add(7, 10);
        directed.add(8, 1);
        directed.add(8, 3);
        directed.add(8, 9);
        directed.add(8, 10);
        directed.add(9, 8);
        directed.add(10, 7);
        directed.add(10, 8);

        fringe.add(1);
        dft = new DepthFirstTraversal(directed);
        dft.traverseTester(fringe, true);
        assertEquals(" 1 8 10 7 3 2 6 <6> 5 <5> "
                + "<2> <3> <7> <10> 9 <9> <8> 4 <4> <1>", dft.getVisited());
    }
}
