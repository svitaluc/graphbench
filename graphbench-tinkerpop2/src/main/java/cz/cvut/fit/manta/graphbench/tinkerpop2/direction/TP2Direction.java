package cz.cvut.fit.manta.graphbench.tinkerpop2.direction;

import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.core.access.direction.DirectionTranslation;
import org.apache.commons.lang.Validate;

/**
 * Class providing translation from local {@link Direction} instances to the
 * {@link com.tinkerpop.blueprints.Direction} instances and the other way round.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP2Direction implements DirectionTranslation<com.tinkerpop.blueprints.Direction> {

    @Override
    public com.tinkerpop.blueprints.Direction mapToOriginal(Direction direction) {
        Validate.notNull(direction);
        switch (direction) {
            case OUT:
                return com.tinkerpop.blueprints.Direction.OUT;
            case IN:
                return com.tinkerpop.blueprints.Direction.IN;
            case BOTH:
                return com.tinkerpop.blueprints.Direction.BOTH;
            default:
                throw new IllegalArgumentException ("Direction type " + direction + "is not supported.");
        }
    }

    @Override
    public Direction mapFromOriginal(com.tinkerpop.blueprints.Direction direction) {
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
