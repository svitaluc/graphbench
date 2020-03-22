package cz.cvut.fit.manta.graphbench.core.access.direction;

/**
 * Interface representing mappings from local direction to direction
 * of given implementation and the other way round.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public interface DirectionTranslation<T> {

    /**
     * Maps a local {@link Direction} instance to a corresponding instance of a Direction representation
     * in a language used by the given graph database.
     * @param direction Local {@link Direction} instance
     * @return Corresponding instance of a Direction representation in a language used by the given graph database.
     */
    T mapToOriginal(Direction direction);

    /**
     * Maps an instance of a Direction representation in a language used by the given graph database
     * to a corresponding local {@link Direction} instance
     * @param originalDirection Instance of a Direction representation in a language used by the given graph database
     * @return Corresponding local {@link Direction} instance
     */
    Direction mapFromOriginal(T originalDirection);
}
