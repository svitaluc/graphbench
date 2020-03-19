package cz.cvut.fit.manta.graphbench.all;

import cz.cvut.fit.manta.graphbench.core.db.GraphDBConnector;
import cz.cvut.fit.manta.graphbench.core.db.product.GraphDBType;
import cz.cvut.fit.manta.graphbench.janusgraph.JanusGraphDB;
import cz.cvut.fit.manta.graphbench.titan.TitanDB;

/**
 * Factory for creation of a graph database representation.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class GraphDBFactory {
    /**
     * Creates a graph database connector based on a provided graph database type.
     * @param type Type of a graph database
     * @return Graph database connector based on a provided graph database type.
     */
    public static GraphDBConnector getGraphDB(GraphDBType type) {
        switch (type) {
            case JANUSGRAPH:
                return new JanusGraphDB();
            case TITAN:
                return new TitanDB();
            default:
                throw new UnsupportedOperationException("Graph type " + type.toString() + " is not supported.");
        }
    }
}
