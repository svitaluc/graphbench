package cz.cvut.fit.manta.graphbench.tinkerpop3;

import cz.cvut.fit.manta.graphbench.core.access.Property;

import java.util.NoSuchElementException;

/**
 * Wrapper for a property of the TinkerPop 3 framework.
 * @param <P> Type of the property value
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP3Property<P> implements Property<P> {

    /** Property of the TinkerPop 3 framework which is wrapped by the {@link TP3Property}. */
    private org.apache.tinkerpop.gremlin.structure.Property<P> property;

    /**
     * Constructor of the {@link TP3Property}.
     * @param property Property of the TinkerPop 3 framework which will be wrapped by the {@link TP3Property}
     */
    public TP3Property(org.apache.tinkerpop.gremlin.structure.Property<P> property) {
        this.property = property;
    }

    @Override
    public P value() throws NoSuchElementException {
        return property.value();
    }
}
