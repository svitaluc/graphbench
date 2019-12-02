package eu.profinit.manta.graphbench.core.access.iterator;

import eu.profinit.manta.graphbench.core.access.IEdge;

import java.util.Iterator;

public abstract class IEdgeIterator extends IElementIterator<IEdge> {

    public IEdgeIterator(Iterator iterator) {
        super(iterator);
    }
}
