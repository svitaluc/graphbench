package cz.cvut.fit.manta.graphbench.core.access.iterator;

import cz.cvut.fit.manta.graphbench.core.access.Vertex;

import java.util.Iterator;

/**
 * Abstract iterator for a vertex.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public abstract class VertexIterator<V extends Vertex, I> extends ElementIterator<V, I> {

    public VertexIterator(Iterator iterator) {
        super(iterator);
    }
}