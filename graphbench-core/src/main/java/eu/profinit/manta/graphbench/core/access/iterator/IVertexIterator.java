package eu.profinit.manta.graphbench.core.access.iterator;

import eu.profinit.manta.graphbench.core.access.IVertex;

import java.util.Iterator;

/**
 *
 */
public abstract class IVertexIterator extends IElementIterator<IVertex> {

    public IVertexIterator(Iterator iterator) {
        super(iterator);
    }
}