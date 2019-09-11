package graph;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class ShortestPathsTest {

    DirectedGraph d = new DirectedGraph();
    LabeledGraph<Double, Double> l =  new LabeledGraph<>(d);

    private class DijkstraTester extends SimpleShortestPaths {

        DijkstraTester(LabeledGraph G, int source, int dest) {
            super(G, source);
        }

        @Override
        public double getWeight(int u, int v) {
            return (double) ((LabeledGraph) _G).getLabel(u, v);
        }
    }

    private class AStarTester extends DijkstraTester {

        AStarTester(LabeledGraph G, int source, int dest) {
            super(G, source, dest);
        }

        @Override
        protected double estimatedDistance(int v) {
            return v;
        }
    }

    @Test
    public void testDijkstra() {
        setUp();

        DijkstraTester s = new DijkstraTester(l, 1, 0);
        s.setPaths();

        assertTrue(s.getWeight(1) == 0.0);
        assertTrue(s.getWeight(2) == 4.0);
        assertTrue(s.getWeight(3) == 6.0);
        assertTrue(s.getWeight(4) == 2.0);
        assertTrue(s.getWeight(5) == 7.0);
    }

    @Test
    public void testAStar() {
        setUp();

        AStarTester s = new AStarTester(l, 1, 5);
        s.setPaths();
    }

    public void setUp() {
        l.add(0.0);
        l.add(Double.POSITIVE_INFINITY);
        l.add(Double.POSITIVE_INFINITY);
        l.add(Double.POSITIVE_INFINITY);
        l.add(Double.POSITIVE_INFINITY);

        l.add(1, 2, 4.0);
        l.add(1, 4, 2.0);
        l.add(2, 4, 1.0);
        l.add(4, 2, 3.0);
        l.add(2, 5, 4.0);
        l.add(4, 3, 4.0);
        l.add(2, 3, 2.0);
        l.add(3, 5, 1.0);
        l.add(5, 4, 7.0);
    }

}
