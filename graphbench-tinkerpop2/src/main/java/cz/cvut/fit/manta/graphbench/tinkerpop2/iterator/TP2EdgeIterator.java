package cz.cvut.fit.manta.graphbench.tinkerpop2.iterator;

import cz.cvut.fit.manta.graphbench.core.access.iterator.EdgeIterator;
import cz.cvut.fit.manta.graphbench.tinkerpop2.TP2Edge;

/**
 * Edge iterator for the TinkerPop 2 framework.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP2EdgeIterator extends EdgeIterator<TP2Edge, com.tinkerpop.blueprints.Edge> {

    /**
     * Constructor of the {@link TP2EdgeIterator}.
     * @param iterable Iterable of the TinkerPop 2 edge.
     */
    public TP2EdgeIterator(Iterable<com.tinkerpop.blueprints.Edge> iterable) {
        super(iterable.iterator());
    }

    @Override
    public TP2Edge next() {
        return new TP2Edge(getIterator().next());
    }
}
