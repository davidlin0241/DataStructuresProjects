package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;
import java.util.List;

import static graph.Iteration.iteration;
import static java.util.Collections.emptyIterator;

/** Represents a general unlabeled directed graph whose vertices are denoted by
 *  positive integers. Graphs may have self edges.
 *
 *  @author David Lin
 */
public class DirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public int inDegree(int v) {
        int degree = 0;
        for (List L: getGraph()) {
            if (L != null) {
                for (Object i: L) {
                    if (i.equals(v)) {
                        degree += 1;
                        break;
                    }
                }
            }
        }
        return degree;
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        List L;
        ArrayList<Integer> vertices = new ArrayList<>();
        if (contains(v)) {
            for (int i = 1; i < getGraph().size(); i++) {
                L = getGraph().get(i);
                if (L != null) {
                    for (int j = 0; j < L.size(); j++) {
                        if (L.get(j).equals(v)) {
                            vertices.add(i);
                            break;
                        }
                    }
                }
            }
            return iteration(vertices);
        } else {
            return iteration(emptyIterator());
        }
    }
}
