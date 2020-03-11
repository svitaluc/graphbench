package cz.cvut.fit.manta.graphbench.tinkerpop3.direction;

import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.core.access.direction.IDirection;

public class TP3Direction implements IDirection {

    @Override
    public Direction opposite(Direction direction) {
        org.apache.tinkerpop.gremlin.structure.Direction apacheDirection = mapToOriginal(direction);
        org.apache.tinkerpop.gremlin.structure.Direction opposite = apacheDirection.opposite(); // we do it for this call
        return mapToTP3(opposite);
    }

    public static org.apache.tinkerpop.gremlin.structure.Direction mapToOriginal(Direction tp3Direction) {
        switch (tp3Direction) {
            case OUT: return org.apache.tinkerpop.gremlin.structure.Direction.OUT;
            case IN: return org.apache.tinkerpop.gremlin.structure.Direction.IN;
            case BOTH: return org.apache.tinkerpop.gremlin.structure.Direction.BOTH;
            default: throw new UnsupportedOperationException("Direction type " + tp3Direction + " is not supported.");
        }
    }

    private Direction mapToTP3(org.apache.tinkerpop.gremlin.structure.Direction direction) {
        switch (direction) {
            case OUT: return Direction.OUT;
            case IN: return Direction.IN;
            case BOTH: return Direction.BOTH;
            default: throw new UnsupportedOperationException("Direction type " + direction + " is not supported.");
        }
    }
}
