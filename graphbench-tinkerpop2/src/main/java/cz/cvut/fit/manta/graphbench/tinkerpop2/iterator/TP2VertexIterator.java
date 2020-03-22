package cz.cvut.fit.manta.graphbench.tinkerpop2.iterator;

import cz.cvut.fit.manta.graphbench.core.access.iterator.VertexIterator;
import cz.cvut.fit.manta.graphbench.tinkerpop2.TP2Vertex;

/**
 * Vertex iterator for the TinkerPop 2 framework.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP2VertexIterator extends VertexIterator<TP2Vertex, com.tinkerpop.blueprints.Vertex> {

    /**
     * Constructor of the {@link TP2VertexIterator}.
     * @param iterable Iterable of the TinkerPop 2 vertex.
     */
    public TP2VertexIterator(Iterable<com.tinkerpop.blueprints.Vertex> iterable) {
        super(iterable.iterator());
    }

    @Override
    public TP2Vertex next() {
        return new TP2Vertex(iterator.next());
    }
}
