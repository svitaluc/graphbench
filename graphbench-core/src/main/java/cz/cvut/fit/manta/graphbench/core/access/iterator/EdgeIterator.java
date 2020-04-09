package cz.cvut.fit.manta.graphbench.core.access.iterator;

import cz.cvut.fit.manta.graphbench.core.access.Edge;

import java.util.Iterator;

/**
 * Abstract iterator for an edge.
 *
 * @param <E> A local class extending the {@link Edge}
 * @param <I> Edge class of a concrete language used
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public abstract class EdgeIterator<E extends Edge<?,?>, I> extends ElementIterator<E, I> {

    /**
     * Consturctor of the {@link EdgeIterator}.
     * @param iterator Iterator of an inner edge representation.
     */
    public EdgeIterator(Iterator<I> iterator) {
        super(iterator);
    }
}
