package eu.profinit.manta.graphbench.tinkerpop2.iterator;

import com.tinkerpop.blueprints.Edge;
import eu.profinit.manta.graphbench.core.access.iterator.IEdgeIterator;
import eu.profinit.manta.graphbench.tinkerpop2.TP2Edge;

public class TP2EdgeIterator extends IEdgeIterator {

    public TP2EdgeIterator(Iterable<Edge> iterable) {
        super(iterable.iterator());
    }

    @Override
    public TP2Edge next() {
        return new TP2Edge((Edge)iterator.next());
    }
}
