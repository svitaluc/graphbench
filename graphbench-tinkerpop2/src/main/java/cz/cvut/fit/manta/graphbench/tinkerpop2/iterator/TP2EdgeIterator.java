package cz.cvut.fit.manta.graphbench.tinkerpop2.iterator;

import cz.cvut.fit.manta.graphbench.core.access.iterator.EdgeIterator;
import cz.cvut.fit.manta.graphbench.tinkerpop2.TP2Edge;

/**
 *
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP2EdgeIterator extends EdgeIterator<TP2Edge, com.tinkerpop.blueprints.Edge> {

    public TP2EdgeIterator(Iterable<com.tinkerpop.blueprints.Edge> iterable) {
        super(iterable.iterator());
    }

    @Override
    public TP2Edge next() {
        return new TP2Edge(iterator.next());
    }
}
