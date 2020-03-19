package cz.cvut.fit.manta.graphbench.tinkerpop3.direction;

import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.core.access.direction.DirectionTranslation;
import org.apache.commons.lang.Validate;

/**
 *
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP3Direction implements DirectionTranslation<org.apache.tinkerpop.gremlin.structure.Direction> {

    @Override
    public org.apache.tinkerpop.gremlin.structure.Direction mapToOriginal(Direction direction) {
        Validate.notNull(direction);
        switch (direction) {
            case OUT:
                return org.apache.tinkerpop.gremlin.structure.Direction.OUT;
            case IN:
                return org.apache.tinkerpop.gremlin.structure.Direction.IN;
            case BOTH:
                return org.apache.tinkerpop.gremlin.structure.Direction.BOTH;
            default:
                throw new IllegalArgumentException ("Direction type " + direction + " is not supported.");
        }
    }

    @Override
    public Direction mapFromOriginal(org.apache.tinkerpop.gremlin.structure.Direction direction) {
        Validate.notNull(direction);
        switch (direction) {
            case OUT:
                return Direction.OUT;
            case IN:
                return Direction.IN;
            case BOTH:
                return Direction.BOTH;
            default:
                throw new IllegalArgumentException ("Direction type " + direction + " is not supported.");
        }
    }
}
