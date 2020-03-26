package cz.cvut.fit.manta.graphbench.tinkerpop2;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import cz.cvut.fit.manta.graphbench.core.config.Configuration;
import cz.cvut.fit.manta.graphbench.core.config.ConfigProperties;
import cz.cvut.fit.manta.graphbench.core.config.model.ConfigProperty;
import cz.cvut.fit.manta.graphbench.core.db.GraphDBConnector;
import cz.cvut.fit.manta.graphbench.tinkerpop2.direction.TP2Direction;
import org.apache.log4j.Logger;
import cz.cvut.fit.manta.graphbench.core.access.GraphOperations;
import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;

import java.util.*;

/**
 * Class for graph operations in the TinkerPop 2 framework.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP2GraphOperations extends GraphOperations<TP2Vertex> {
    /** Logger. */
    private final static Logger LOG = Logger.getLogger(TP2GraphOperations.class);
    /** Utility for translation of local and TinkerPop 2 direction. */
    private TP2Direction tp2Direction = new TP2Direction();
    /** Configuration of the config.properties. */
    private final Configuration CONFIG = ConfigProperties.getInstance();

    /**
     * Constructor of the {@link TP2GraphOperations}.
     * @param db Connector to a graph database.
     */
    public TP2GraphOperations(GraphDBConnector<cz.cvut.fit.manta.graphbench.core.access.Vertex<?, ?>,
            cz.cvut.fit.manta.graphbench.core.access.Edge<?, ?>> db) {
        super(db);
    }

    @Override
    public List<TP2Vertex> getChildren(TP2Vertex node) {
        List<TP2Vertex> childList = new ArrayList<>();
        Iterable<Vertex> children = node.getVertex().query()
                .labels(CONFIG.getStringProperty(ConfigProperty.EDGE_PARENT_LABEL))
                .direction(com.tinkerpop.blueprints.Direction.IN).vertices();
        for(Vertex v: children) {
            childList.add(new TP2Vertex(v));
        }
        return childList;
    }

    @Override
    public List<TP2Vertex> getChildrenByName(TP2Vertex node, String name) {
        List<TP2Vertex> childList = new ArrayList<>();
        Iterable<Vertex> children = node.getVertex().query()
                .has(CONFIG.getStringProperty(ConfigProperty.EDGE_CHILD_NAME), name)
                .labels(CONFIG.getStringProperty(ConfigProperty.EDGE_PARENT_LABEL))
                .direction(com.tinkerpop.blueprints.Direction.IN)
                .vertices();

        for(Vertex v: children) {
            childList.add(new TP2Vertex(v));
        }
        return childList;
    }

    @Override
    public TP2Vertex getParent(TP2Vertex node) {
        Iterable<Vertex> parentIt = node.getVertex()
                .getVertices(com.tinkerpop.blueprints.Direction.OUT, CONFIG.getStringProperty(ConfigProperty.EDGE_PARENT_LABEL));
        Vertex parent = parentIt.iterator().next();

        if (parentIt.iterator().hasNext()) {
            LOG.error("Node:" + node.getId() + " has more than one Parent node.");

        }

        return new TP2Vertex(parent);
    }

    @Override
    public List<TP2Vertex> getVerticesByEdgeType(TP2Vertex node, String edgeType, Direction dir) {
        List<TP2Vertex> vertices = new ArrayList<>();
        com.tinkerpop.blueprints.Direction originalDirection = tp2Direction.mapToOriginal(dir);
        Iterable<Vertex> it = node.getVertex().getVertices(originalDirection, edgeType);

        for(Vertex v: it) {
            vertices.add(new TP2Vertex(v));
        }

        return vertices;
    }

    @Override
    public List<TP2Vertex> simpleFlow(TP2Vertex node, String edgeType, Direction dir) {
        List<TP2Vertex> reachable = new ArrayList<>();
        Deque<TP2Vertex> stack = new ArrayDeque<>();
        Map<String, Boolean> visited = new HashMap<>();
        com.tinkerpop.blueprints.Direction originalDirection = tp2Direction.mapToOriginal(dir);

        stack.add(node);

        while (!stack.isEmpty()) {
            TP2Vertex currNode = stack.pop();

            if (visited.containsKey(currNode.getId())) {
                continue; //this node was already visited
            }

            Iterator<Edge> edges = currNode.getVertex().query().labels(edgeType).direction(originalDirection).edges().iterator();

            while (edges.hasNext()) {
                Edge outgoingEdge = edges.next();
                Vertex neighbour = outgoingEdge.getVertex(originalDirection.opposite());

                stack.push(new TP2Vertex(neighbour));
            }

            visited.put(currNode.getId().toString(), true);
            reachable.add(currNode);
        }

        return reachable;
    }
}
