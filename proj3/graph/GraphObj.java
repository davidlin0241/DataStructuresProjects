package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;
import java.util.List;

import static graph.Iteration.iteration;
import static java.util.Collections.emptyIterator;

/** A partial implementation of Graph containing elements common to
 *  directed and undirected graphs.
 *
 *  @author David Lin
 */
abstract class GraphObj extends Graph {

    /** Representation of the graph. */
    private ArrayList<List<Integer>> graph;
    /** Stores the edgeIds. */
    private ArrayList<int[]> edgeIdList;
    /** max number of vertices ever. */
    private int maxVertex;

    /** gets the graph.
     * @returns graph */
    ArrayList<List<Integer>> getGraph() {
        return graph;
    }

    /** gets the edgeIdList.
     * @returns edgeIdList */
    ArrayList<int[]> getEdgeIdList() {
        return edgeIdList;
    }

    /** gets num of maxVertex.
     * @returns maxVertex */
    int getMaxVertex() {
        return maxVertex;
    }

    /** A new, empty Graph. */
    GraphObj() {
        graph = new ArrayList<>();
        edgeIdList = new ArrayList<>();
        graph.add(null);
        edgeIdList.add(null);
        maxVertex = 0;
    }

    @Override
    public int vertexSize() {
        int size = 0;
        for (List L: graph) {
            if (L != null) {
                size += 1;
            }
        }
        return size;
    }

    @Override
    public int maxVertex() {
        for (int i = graph.size() - 1; i > 0; i--) {
            if (graph.get(i) != null) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public int edgeSize() {
        int edges = 0;
        for (List L: graph) {
            if (L != null) {
                edges += L.size();
            }
        }
        return edges;
    }

    @Override
    public abstract boolean isDirected();

    @Override
    public int outDegree(int v) {
        List L = graph.get(v);
        int degree = 0;
        if (L != null) {
            degree = graph.get(v).size();
        }

        if (!contains(v)) {
            return 0;
        } else {
            if (!isDirected()) {
                for (int i = 1; i < graph.size(); i++) {
                    L = graph.get(i);
                    if (L != null) {
                        if (i == v) {
                            continue;
                        } else {
                            for (Object o : L) {
                                if (o.equals(v)) {
                                    degree += 1;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return degree;
    }

    @Override
    public abstract int inDegree(int v);

    @Override
    public boolean contains(int u) {
        if (u >= 1 && u < graph.size()) {
            return graph.get(u) != null;
        } else {
            return false;
        }
    }

    @Override
    public boolean contains(int u, int v) {
        if (contains(u) && graph.get(u).contains(v)) {
            return true;
        }

        if (!isDirected()) {
            return contains(v) && graph.get(v).contains(u);
        } else {
            return false;
        }
    }

    /** @returns next available vertexNum. */
    public int nextVertexNum() {
        for (int index = 1; index < graph.size(); index++) {
            if (graph.get(index) == null) {
                return index;
            }
        }
        return graph.size();
    }

    @Override
    public int add() {
        List<Integer> newVertex = new ArrayList<>();
        int vertexNum = nextVertexNum();

        if (vertexNum == graph.size()) {
            graph.add(newVertex);
        } else {
            graph.set(vertexNum, newVertex);
        }

        if (vertexSize() > maxVertex) {
            maxVertex = vertexSize();
        }

        return vertexNum;
    }

    @Override
    public int add(int u, int v) {
        if (contains(u) && contains(v)) {
            if (!graph.get(u).contains(v)) {
                if (!isDirected()) {
                    if (!graph.get(v).contains(u)) {
                        graph.get(u).add(v);
                    }
                } else {
                    graph.get(u).add(v);
                }
                edgeIdList.add(new int[2]);
                edgeIdList.get(edgeIdList.size() - 1)[0] = u;
                edgeIdList.get(edgeIdList.size() - 1)[1] = v;
                return edgeId(u, v);
            }
        }

        return 0;

    }

    @Override
    public void remove(int v) {
        List L;
        if (contains(v)) {
            graph.set(v, null);
        }

        for (int i = 1; i < graph.size(); i++) {
            L = graph.get(i);
            if (L != null) {
                for (int j = 0; j < L.size(); j++) {
                    if (L.get(j).equals(v)) {
                        L.remove(j);
                    }
                }
            }
        }
    }

    @Override
    public void remove(int u, int v) {
        List L;
        if (contains(u, v)) {
            L = graph.get(u);
            if (isDirected()) {
                if (L.contains(v)) {
                    L.remove(L.indexOf(v));
                }
            } else {
                if (L.contains(v)) {
                    L.remove(L.indexOf(v));
                } else {
                    L = graph.get(v);
                    if (L.contains(u)) {
                        L.remove(L.indexOf(u));
                    }
                }
            }
        }
    }

    @Override
    public Iteration<Integer> vertices() {
        ArrayList<Integer> outerList = new ArrayList<>();
        for (int i = 1; i < graph.size(); i++) {
            if (graph.get(i) != null) {
                outerList.add(i);
            }
        }

        return iteration(outerList);

    }

    @Override
    public Iteration<Integer> successors(int v) {
        ArrayList<Integer> vertices = new ArrayList<>();
        if (contains(v)) {
            if (!isDirected()) {
                for (int i = 1; i < graph.size(); i++) {
                    if (contains(i, v)) {
                        vertices.add(i);
                    }
                }
            } else {
                vertices.addAll(graph.get(v));
            }

            return iteration(vertices);
        } else {
            return iteration(emptyIterator());
        }
    }

    @Override
    public abstract Iteration<Integer> predecessors(int v);

    @Override
    public Iteration<int[]> edges() {
        List L;
        ArrayList<int[]> edges = new ArrayList<>();
        for (int i = 0; i < graph.size() - 1; i++) {
            L = graph.get(i + 1);
            if (L != null) {
                for (int j = 0; j < L.size(); j++) {
                    edges.add(new int[2]);
                    edges.get(edges.size() - 1)[0] = i + 1;
                    edges.get(edges.size() - 1)[1] = (Integer) L.get(j);
                }
            }
        }
        return iteration(edges);
    }

    @Override
    protected int edgeId(int u, int v) {

        if (contains(u, v)) {
            for (int i = 1; i < edgeIdList.size(); i++) {
                if (edgeIdList.get(i)[0] == u
                        && edgeIdList.get(i)[1] == v) {
                    return i;
                } else {
                    if (!isDirected()
                            && edgeIdList.get(i)[0] == v
                            && edgeIdList.get(i)[1] == u) {
                        return i;
                    }
                }
            }
        }

        return 0;
    }
}
