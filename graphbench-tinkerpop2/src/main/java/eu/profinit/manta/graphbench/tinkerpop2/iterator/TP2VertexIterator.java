package eu.profinit.manta.graphbench.tinkerpop2.iterator;

import com.tinkerpop.blueprints.Vertex;
import eu.profinit.manta.graphbench.core.access.IVertex;
import eu.profinit.manta.graphbench.core.access.iterator.IVertexIterator;
import eu.profinit.manta.graphbench.tinkerpop2.TP2Vertex;

public class TP2VertexIterator extends IVertexIterator {
    public TP2VertexIterator(Iterable<Vertex> iterable) {
        super(iterable.iterator());
    }

    @Override
    public IVertex next() {
        return new TP2Vertex((Vertex)iterator.next());
    }
}
