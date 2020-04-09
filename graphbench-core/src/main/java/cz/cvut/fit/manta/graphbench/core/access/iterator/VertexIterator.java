package cz.cvut.fit.manta.graphbench.core.access.iterator;

import cz.cvut.fit.manta.graphbench.core.access.Vertex;

import java.util.Iterator;

/**
 * Abstract iterator for a vertex.
 *
 * @param <V> A local class extending the {@link Vertex}
 * @param <I> Vertex class of a concrete language used
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public abstract class VertexIterator<V extends Vertex<?,?>, I> extends ElementIterator<V, I> {

    /**
     * Constructor of the {@link VertexIterator}.
     * @param iterator Iterator of an inner vertex representation.
     */
    public VertexIterator(Iterator<I> iterator) {
        super(iterator);
    }
}