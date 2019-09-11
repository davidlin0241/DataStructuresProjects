package graph;

/* See restrictions in Graph.java. */


import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/** The shortest paths through an edge-weighted graph.
 *  By overrriding methods getWeight, setWeight, getPredecessor, and
 *  setPredecessor, the client can determine how to represent the weighting
 *  and the search results.  By overriding estimatedDistance, clients
 *  can search for paths to specific destinations using A* search.
 *  @author David Lin
 */
public abstract class ShortestPaths {

    /** The graph being searched. */
    protected final Graph _G;
    /** The starting vertex. */
    private final int _source;
    /** The target vertex. */
    private final int _dest;
    /** Newly defined comparator for graph context. */
    private Comparator<Integer> _comparator;

    /** The shortest paths in G from SOURCE. */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public ShortestPaths(Graph G, int source, int dest) {
        _G = G;
        _source = source;
        _dest = dest;
        _comparator = new Comp();
    }

    /** Initialize the shortest paths.  Must be called before using
     *  getWeight, getPredecessor, and pathTo. */
    public void setPaths() {
        int vertex;
        PriorityQueue<Integer> fringe =
                new PriorityQueue<>(_comparator);
        for (int v: _G.vertices()) {
            if (v != _source) {
                setWeight(v, Double.POSITIVE_INFINITY);
                setPredecessor(v, 0);
            }
            fringe.add(v);
        }

        while (!fringe.isEmpty()) {
            vertex = fringe.poll();
            if (vertex == _dest) {
                return;
            }

            for (Integer successor : _G.successors(vertex)) {
                if (getWeight(vertex)
                        + getWeight(vertex, successor) < getWeight(successor)) {
                    setWeight(successor, getWeight(vertex)
                            + getWeight(vertex, successor));
                    setPredecessor(successor, vertex);

                    fringe.remove(successor);
                    fringe.add(successor);
                }
            }
        }

    }

    /** New comparator class to override compare method for vertices. */
    class Comp implements Comparator<Integer> {
        @Override
        public int compare(Integer x, Integer y) {
            if (estimatedDistance(x) + getWeight(x)
                    >= estimatedDistance(y) + getWeight(y)) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /** Returns the starting vertex. */
    public int getSource() {
        return _source;
    }

    /** Returns the target vertex, or 0 if there is none. */
    public int getDest() {
        return _dest;
    }

    /** Returns the current weight of vertex V in the graph.  If V is
     *  not in the graph, returns positive infinity. */
    public abstract double getWeight(int v);

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    protected abstract void setWeight(int v, double w);

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    public abstract int getPredecessor(int v);

    /** Set getPredecessor(V) to U. */
    protected abstract void setPredecessor(int v, int u);

    /** Returns an estimated heuristic weight of the shortest path from vertex
     *  V to the destination vertex (if any).  This is assumed to be less
     *  than the actual weight, and is 0 by default. */
    protected double estimatedDistance(int v) {
        return 0.0;
    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    protected abstract double getWeight(int u, int v);

    /** Returns a list of vertices starting at _source and ending
     *  at V that represents a shortest path to V.  Invalid if there is a
     *  destination vertex other than V. */
    public List<Integer> pathTo(int v) {
        ArrayList<Integer> path = new ArrayList<>();
        int currentVertex = v;
        path.add(currentVertex);
        while (currentVertex != getSource()) {
            currentVertex = getPredecessor(currentVertex);
            path.add(currentVertex);
        }

        Collections.reverse(path);
        return path;
    }

    /** Returns a list of vertices starting at the source and ending at the
     *  destination vertex. Invalid if the destination is not specified. */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }

}
