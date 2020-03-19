package cz.cvut.fit.manta.graphbench.core.access.iterator;

import cz.cvut.fit.manta.graphbench.core.access.Edge;

import java.util.Iterator;

/**
 * Abstract iterator for an edge.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public abstract class EdgeIterator<E extends Edge, I> extends ElementIterator<E, I> {

    public EdgeIterator(Iterator iterator) {
        super(iterator);
    }
}
