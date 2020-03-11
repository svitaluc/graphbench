package cz.cvut.fit.manta.graphbench.core.access.iterator;

import cz.cvut.fit.manta.graphbench.core.access.IEdge;

import java.util.Iterator;

/**
 * Abstract iterator for an edge.
 */
public abstract class IEdgeIterator extends IElementIterator<IEdge> {

    public IEdgeIterator(Iterator iterator) {
        super(iterator);
    }
}
