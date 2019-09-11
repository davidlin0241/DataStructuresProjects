package graph;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests for the Graph class.
 *  @author David Lin
 */
public class GraphTest {

    DirectedGraph g = new DirectedGraph();
    UndirectedGraph f = new UndirectedGraph();

    @Test
    public void emptyGraph() {
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
        assertEquals("Initial graph has a non-zero maxVertex",
                0, g.maxVertex());
    }

    @Test
    public void testAddVertex() {
        assertEquals(1, g.add());
        assertEquals(1, g.vertexSize());
        assertEquals(0, g.edgeSize());
        assertEquals(1, g.maxVertex());
        assertEquals(2, g.add());
        assertEquals(2, g.vertexSize());
        assertEquals(0, g.edgeSize());
        assertEquals(2, g.maxVertex());
    }

    @Test
    public void testEdgeId() {
        g.add();
        g.add();
        g.add(1, 2);
        assertEquals(1, g.edgeId(1, 2));
        g.add();
        g.add(2, 3);
        assertEquals(2, g.edgeId(2, 3));
    }

    @Test
    public void testContains() {
        g.add();
        g.add();
        g.add();
        assertTrue(g.contains(1));
        assertTrue(g.contains(2));
        assertTrue(g.contains(3));
        assertFalse(g.contains(0));
        assertFalse(g.contains(4));
    }

    @Test
    public void testAddEdge() {
        f.add();
        f.add();
        f.add(1, 2);
        assertEquals(1, f.edgeId(1, 2));
        assertEquals(1, f.edgeSize());
        assertTrue(f.contains(1, 2));
        assertTrue(f.contains(2, 1));
        f.add(1, 2);
        f.add(2, 1);
        assertEquals(1, f.edgeSize());
        f.add();
        f.add(1, 3);
        f.add(2, 3);
        assertEquals(3, f.edgeSize());
        assertTrue(f.contains(1, 3));
        assertTrue(f.contains(2, 3));
    }

    @Test
    public void testRemoveEdge() {
        f.add();
        f.add();
        f.add();
        f.add(3, 2);
        f.remove(2, 3);
        assertEquals(0, f.edgeSize());
        f.add(1, 2);
        f.remove(1, 2);
        assertEquals(0, f.edgeSize());
    }

    @Test
    public void testRemoveVertex() {
        f.add();
        f.add();
        f.add();
        f.add(3, 2);
        f.remove(2);
        assertEquals(2, f.vertexSize());
        assertEquals(3, f.maxVertex());
        assertEquals(0, f.edgeSize());
        f.add();
        f.add();
        f.add(1, 2);
        f.add(4, 3);
        f.remove(1);
        assertEquals(3, f.vertexSize());
        assertEquals(4, f.maxVertex());
        assertEquals(1, f.edgeSize());
    }

    @Test
    public void testDegrees() {
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(2, 3);
        g.add(1, 3);
        assertEquals(2, g.inDegree(3));
        assertEquals(2, g.outDegree(1));

        f.add();
        f.add();
        f.add();
        f.add(3, 2);
        f.add(2, 1);
        assertEquals(2, f.inDegree(2));
        assertEquals(2, f.outDegree(2));
    }

    @Test
    public void testVertices() {
        g.add();
        g.add();
        g.add();
        Iteration i = g.vertices();
        for (int j = 1; j < g.maxVertex(); j++) {
            assertEquals(j, i.next());
        }
    }

    @Test
    public void testSuccessors() {
        g.add();
        g.add();
        g.add();
        Iteration i = g.successors(2);
        assertTrue(!i.hasNext());
        g.add(1, 2);
        i = g.successors(2);
        assertTrue(!i.hasNext());

        f.add();
        f.add();
        f.add();
        f.add(1, 2);
        i = f.successors(2);
        assertTrue(i.hasNext());
    }

    @Test
    public void testEdges() {
        int[] temp;
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(2, 1);
        Iteration i = g.edges();
        temp = (int[]) i.next();
        assertTrue(temp[0] == 1);
        assertTrue(temp[1] == 2);
    }

    @Test
    public void testPredecessors() {
        g.add();
        g.add();
        g.add();
        g.add(1, 2);
        g.add(3, 2);
        Iteration i = g.predecessors(2);
        assertTrue(i.next().equals(1));
        assertTrue(i.next().equals(3));

        f.add();
        f.add();
        f.add();
        f.add(1, 2);
        f.add(1, 3);
        i = f.predecessors(1);
        assertTrue(i.next().equals(2));
        assertTrue(i.next().equals(3));
    }
}
