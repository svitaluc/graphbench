package cz.cvut.fit.manta.graphbench.core.access.iterator;

import java.util.Iterator;

/**
 * Abstract class for all element (a vertex, an edge, and an edge attribute) iterators.
 *
 * @param <E> Element type to be returned
 * @param <I> Inner element type (of a concrete language in a given database) to be iterated over
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public abstract class ElementIterator<E, I> {
    /** Iterator of an inner element - element of a concrete language in a given graph database. */
    private final Iterator<I> iterator;

    /**
     * Constructor of the {@link ElementIterator}.
     */
    public ElementIterator(){
        iterator = null;
    }

    /**
     * Constructor of the {@link ElementIterator}.
     * @param iterator Iterator of an inner element - element of
     *                a concrete language in a given graph database
     */
    public ElementIterator(Iterator<I> iterator) {
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

    /**
     * @return Iterator of an inner element - element of a concrete language in a given graph database.
     */
    protected Iterator<I> getIterator() {
        return iterator;
    }
}