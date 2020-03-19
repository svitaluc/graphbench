package cz.cvut.fit.manta.graphbench.core.access.iterator;

import java.util.Iterator;

/**
 * Abstract class for all element (both a vertex and an edge) iterators.
 * @param <E> element type to be iterated over
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public abstract class ElementIterator<E, I> {
    protected Iterator<I> iterator;

    public ElementIterator(){}

    public ElementIterator(Iterator iterator) {
        this.iterator = iterator;
    }

    /**
     * Finds and returns the next element of this iterator.
     * @return the next element
     */
    public abstract E next();

    /**
     * Returns true if this iterator has another element in its input.
     * @return true if and only if this iterator has another element
     */
    public boolean hasNext() {
        return iterator.hasNext();
    }
}