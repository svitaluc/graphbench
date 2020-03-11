package cz.cvut.fit.manta.graphbench.core.access.iterator;

import cz.cvut.fit.manta.graphbench.core.access.IVertex;

import java.util.Iterator;

/**
 * Abstract iterator for a vertex.
 */
public abstract class IVertexIterator extends IElementIterator<IVertex> {

    public IVertexIterator(Iterator iterator) {
        super(iterator);
    }
}