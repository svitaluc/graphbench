package cz.cvut.fit.manta.graphbench.core.access.direction;

/**
 * Interface representing mappings from local direction to direction
 * of given implementation and the other way round.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public interface DirectionTranslation<T> {

    T mapToOriginal(Direction direction);
    Direction mapFromOriginal(T originalDirection);
}
