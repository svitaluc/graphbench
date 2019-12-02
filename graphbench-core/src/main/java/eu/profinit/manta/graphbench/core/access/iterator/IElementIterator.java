package eu.profinit.manta.graphbench.core.access.iterator;

import java.util.Iterator;

/**
 * Abstract class for all element (both a vertex and an edge) iterators.
 * @param <E> element type to be iterated over
 */
public abstract class IElementIterator<E> {
    protected Iterator iterator;

    public IElementIterator(){}

    public IElementIterator(Iterator iterator) {
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