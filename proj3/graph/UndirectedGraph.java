package graph;

import java.util.ArrayList;
import java.util.List;

import static graph.Iteration.iteration;
import static java.util.Collections.emptyIterator;

/** Represents an undirected graph.  Out edges and in edges are not
 *  distinguished.  Likewise for successors and predecessors.
 *
 *  @author David Lin
 */
public class UndirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public int inDegree(int v) {
        int degree = 0;
        List L;
        for (int i = 1; i < getGraph().size(); i++) {
            L = getGraph().get(i);
            if (L != null) {
                if (i == v) {
                    degree += L.size();
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

        return degree;
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        ArrayList<Integer> vertices = new ArrayList<>();
        List L;
        if (contains(v)) {
            for (int i = 1; i < getGraph().size(); i++) {
                L = getGraph().get(i);
                if (L != null) {
                    if (i != v) {
                        for (int j = 0; j < L.size(); j++) {
                            if (L.get(j).equals(v)) {
                                vertices.add(i);
                                break;
                            }
                        }
                    } else {
                        for (int k = 0; k < L.size(); k++) {
                            vertices.add((Integer) L.get(k));
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
