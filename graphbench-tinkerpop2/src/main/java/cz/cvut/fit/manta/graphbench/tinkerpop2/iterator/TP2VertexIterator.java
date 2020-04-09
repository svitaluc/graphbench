package cz.cvut.fit.manta.graphbench.tinkerpop2.iterator;

import com.tinkerpop.blueprints.Vertex;
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
    protected TP2Vertex convertElement(Vertex innerElement) {
        return new TP2Vertex(innerElement);
    }
}
