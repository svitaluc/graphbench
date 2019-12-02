package eu.profinit.manta.graphbench.tinkerpop3.iterator;

import org.apache.tinkerpop.gremlin.structure.Edge;
import eu.profinit.manta.graphbench.core.access.iterator.IEdgeIterator;
import eu.profinit.manta.graphbench.tinkerpop3.TP3Edge;

import java.util.Iterator;

public class TP3EdgeIterator extends IEdgeIterator {
    public TP3EdgeIterator(Iterator<Edge> iterator) {
        super(iterator);
    }

    @Override
    public TP3Edge next() {
        return new TP3Edge((Edge)iterator.next());
    }
}
