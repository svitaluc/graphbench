package cz.cvut.fit.manta.graphbench.core.access.direction;

/**
 * Enumeration of all direction options.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum Direction {
    IN, OUT, BOTH;

    public Direction opposite() {
        if (this.equals(OUT))
            return IN;
        else if (this.equals(IN))
            return OUT;
        else
            return BOTH;
    }
}
