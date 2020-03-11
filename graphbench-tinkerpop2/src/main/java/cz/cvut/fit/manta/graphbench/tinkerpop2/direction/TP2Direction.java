package cz.cvut.fit.manta.graphbench.tinkerpop2.direction;

import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.core.access.direction.IDirection;

public class TP2Direction implements IDirection {

    @Override
    public Direction opposite(Direction direction) {
        com.tinkerpop.blueprints.Direction blueprintsDirection = mapToOriginal(direction);
        com.tinkerpop.blueprints.Direction opposite = blueprintsDirection.opposite();
        return mapToTP2(opposite);
    }

    public static com.tinkerpop.blueprints.Direction mapToOriginal(Direction tp2Direction) {
        switch (tp2Direction) {
            case OUT: return com.tinkerpop.blueprints.Direction.OUT;
            case IN: return com.tinkerpop.blueprints.Direction.IN;
            case BOTH: return com.tinkerpop.blueprints.Direction.BOTH;
            default: throw new UnsupportedOperationException("Direction type " + tp2Direction + "is not supported.");
        }
    }

    private Direction mapToTP2(com.tinkerpop.blueprints.Direction direction) {
        switch (direction) {
            case OUT: return Direction.OUT;
            case IN: return Direction.IN;
            case BOTH: return Direction.BOTH;
            default: throw new UnsupportedOperationException("Direction type " + direction + " is not supported.");
        }
    }
}
