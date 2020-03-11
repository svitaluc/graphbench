package cz.cvut.fit.manta.graphbench.tinkerpop3.iterator;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import cz.cvut.fit.manta.graphbench.core.access.iterator.IVertexIterator;
import cz.cvut.fit.manta.graphbench.tinkerpop3.TP3Vertex;

import java.util.Iterator;

public class TP3VertexIterator extends IVertexIterator {
    public TP3VertexIterator(Iterator<Vertex> iterator) {
        super(iterator);
    }

    @Override
    public TP3Vertex next() {
        return new TP3Vertex((Vertex)iterator.next());
    }
}
