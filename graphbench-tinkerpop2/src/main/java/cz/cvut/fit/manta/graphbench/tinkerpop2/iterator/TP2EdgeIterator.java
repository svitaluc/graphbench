package cz.cvut.fit.manta.graphbench.tinkerpop2.iterator;

import com.tinkerpop.blueprints.Edge;
import cz.cvut.fit.manta.graphbench.core.access.iterator.IEdgeIterator;
import cz.cvut.fit.manta.graphbench.tinkerpop2.TP2Edge;

public class TP2EdgeIterator extends IEdgeIterator {

    public TP2EdgeIterator(Iterable<Edge> iterable) {
        super(iterable.iterator());
    }

    @Override
    public TP2Edge next() {
        return new TP2Edge((Edge)iterator.next());
    }
}
