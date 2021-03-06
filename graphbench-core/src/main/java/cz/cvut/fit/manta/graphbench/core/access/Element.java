package cz.cvut.fit.manta.graphbench.core.access;

/**
 * Interface representing an element of a graph (both a vertex and an edge).
 *
 * @param <T> Class type of the element id
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public interface Element<T> {
    /**
     * Returns id of the element.
     * @return id of the element
     */
    T getId();
}
