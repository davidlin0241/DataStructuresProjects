package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

/** Implements a depth-first traversal of a graph.  Generally, the
 *  client will extend this class, overriding the visit and
 *  postVisit methods, as desired (by default, they do nothing).
 *  @author David Lin
 */
public class DepthFirstTraversal extends Traversal {

    /** DS of the edges. */
    private ArrayList<int[]> _edges;
    /** The graph. */
    private Graph _G;

    /** A depth-first Traversal of G. */
    protected DepthFirstTraversal(Graph G) {
        super(G, Collections.asLifoQueue(new ArrayDeque<>()));
        _edges = new ArrayList<>();
        _G = G;
    }

    @Override
    protected boolean visit(int v) {
        return super.visit(v);
    }

    @Override
    protected boolean postVisit(int v) {
        return super.postVisit(v);
    }

    @Override
    protected boolean shouldPostVisit(int v) {
        return true;
    }
}
