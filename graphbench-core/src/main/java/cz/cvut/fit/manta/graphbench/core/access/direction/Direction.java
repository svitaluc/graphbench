package cz.cvut.fit.manta.graphbench.core.access.direction;

/**
 * Enumeration of all direction options.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum Direction {
    IN, OUT, BOTH;

    /**
     * Method for finding an opposite direction to the one on which the method was called.
     * @return Opposite direction to the one on which the method was called
     */
    public Direction opposite() {
        if (this.equals(OUT))
            return IN;
        else if (this.equals(IN))
            return OUT;
        else
            return BOTH;
    }
}
