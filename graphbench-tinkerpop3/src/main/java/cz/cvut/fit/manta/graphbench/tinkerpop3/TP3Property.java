package cz.cvut.fit.manta.graphbench.tinkerpop3;

import cz.cvut.fit.manta.graphbench.core.access.Property;

import java.util.NoSuchElementException;

/**
 *
 * @param <P>
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP3Property<P> implements Property<P> {

    private org.apache.tinkerpop.gremlin.structure.Property<P> property;

    public TP3Property(org.apache.tinkerpop.gremlin.structure.Property<P> property) {
        this.property = property;
    }

    @Override
    public P value() throws NoSuchElementException {
        return property.value();
    }
}
