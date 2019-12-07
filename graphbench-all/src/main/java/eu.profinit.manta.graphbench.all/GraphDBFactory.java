package eu.profinit.manta.graphbench.all;

import eu.profinit.manta.graphbench.core.db.IGraphDBConnector;
import eu.profinit.manta.graphbench.core.db.product.GraphDBType;
import eu.profinit.manta.graphbench.janusgraph.JanusGraphDB;
import eu.profinit.manta.graphbench.titan.TitanDB;

public class GraphDBFactory {
    public static IGraphDBConnector getGraphDB(GraphDBType type) {
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
