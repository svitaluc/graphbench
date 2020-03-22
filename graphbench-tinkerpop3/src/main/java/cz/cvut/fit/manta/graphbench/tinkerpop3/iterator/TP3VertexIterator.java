package cz.cvut.fit.manta.graphbench.tinkerpop3.iterator;

import cz.cvut.fit.manta.graphbench.core.access.iterator.VertexIterator;
import cz.cvut.fit.manta.graphbench.tinkerpop3.TP3Vertex;

import java.util.Iterator;

/**
 * Vertex iterator for the TinkerPop 3 framework.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP3VertexIterator extends VertexIterator<TP3Vertex, org.apache.tinkerpop.gremlin.structure.Vertex> {

    /**
     * Constructor of the {@link TP3VertexIterator}.
     * @param iterator Iterator of the TinkerPop 3 vertex.
     */
    public TP3VertexIterator(Iterator<org.apache.tinkerpop.gremlin.structure.Vertex> iterator) {
        super(iterator);
    }

    @Override
    public TP3Vertex next() {
        return new TP3Vertex(iterator.next());
    }
}
