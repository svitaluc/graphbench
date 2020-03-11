package cz.cvut.fit.manta.graphbench.core.access.direction;

/**
 * Interface representing direction operations.
 */
public interface IDirection {
    /**
     * Returns an opposite direction to the one provided as a parameter.
     * @param direction the original direction
     * @return the opposite direction
     */
    Direction opposite(Direction direction);
}
