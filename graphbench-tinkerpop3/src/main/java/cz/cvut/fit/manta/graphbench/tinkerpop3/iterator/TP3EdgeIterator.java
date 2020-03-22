package cz.cvut.fit.manta.graphbench.tinkerpop3.iterator;

import cz.cvut.fit.manta.graphbench.core.access.iterator.EdgeIterator;
import cz.cvut.fit.manta.graphbench.tinkerpop3.TP3Edge;
import org.apache.tinkerpop.gremlin.structure.Edge;

import java.util.Iterator;

/**
 * Edge iterator for the TinkerPop 3 framework.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP3EdgeIterator extends EdgeIterator<TP3Edge, org.apache.tinkerpop.gremlin.structure.Edge> {

    /**
     * Constructor for the {@link TP3EdgeIterator}.
     * @param iterator Iterator of the TinkerPop 3 edge.
     */
    public TP3EdgeIterator(Iterator<Edge> iterator) {
        super(iterator);
    }

    @Override
    public TP3Edge next() {
        return new TP3Edge(iterator.next());
    }
}
