package eu.profinit.manta.graphbench.tinkerpop3;

import org.apache.tinkerpop.gremlin.structure.Property;
import eu.profinit.manta.graphbench.core.access.IProperty;

import java.util.NoSuchElementException;

public class TP3Property<P> implements IProperty<P> {

    private Property<P> property;

    public TP3Property(Property<P> property) {
        this.property = property;
    }

    @Override
    public P value() throws NoSuchElementException {
        return property.value();
    }
}
