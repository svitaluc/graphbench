package cz.cvut.fit.manta.graphbench.core.access;

import java.util.NoSuchElementException;

/**
 * Interface representing a property of either a vertex or an edge.
 * @param <P> type of the property value
 */
public interface IProperty<P> {
    /**
     * Returns the value of the property
     * @return value of the property
     * @throws NoSuchElementException If the property is empty
     */
    P value() throws NoSuchElementException;
}
