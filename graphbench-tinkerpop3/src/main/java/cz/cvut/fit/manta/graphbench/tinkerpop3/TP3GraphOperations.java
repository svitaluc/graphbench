package cz.cvut.fit.manta.graphbench.tinkerpop3;

import cz.cvut.fit.manta.graphbench.core.access.GraphOperations;
import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.core.config.ConfigProperties;
import cz.cvut.fit.manta.graphbench.core.config.Configuration;
import cz.cvut.fit.manta.graphbench.core.config.model.ConfigProperty;
import cz.cvut.fit.manta.graphbench.core.db.GraphDBConnector;
import cz.cvut.fit.manta.graphbench.core.db.structure.NodeProperty;
import cz.cvut.fit.manta.graphbench.tinkerpop3.direction.TP3Direction;
import org.apache.log4j.Logger;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.*;

/**
 * Class for graph operations in the TinkerPop 3 framework.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP3GraphOperations extends GraphOperations<TP3Vertex> {
    /** Logger. */
    private final static Logger LOG = Logger.getLogger(TP3GraphOperations.class);
    /** Traversal source of the TinkerPop 3 framework. */
    private GraphTraversalSource traversal;
    /** Configuration of the config.properties. */
    private final Configuration config = ConfigProperties.getInstance();
    /** Utility for translation of local and TinkerPop 2 direction. */
    private TP3Direction tp3Direction = new TP3Direction();

    /**
     * Constructor of the {@link TP3GraphOperations}.
     * @param db Connector to the graph database.
     */
    public TP3GraphOperations(GraphDBConnector<TP3Vertex, TP3Edge> db) {
        super(db);
        traversal = (GraphTraversalSource) db.getTraversal();
    }

    @Override
    public List<TP3Vertex> getChildren(TP3Vertex node) {
        List<TP3Vertex> childList = new ArrayList<>();
        long startTime = System.nanoTime();
        Iterator<Vertex> children = traversal.V(node.getId()).in(config.getStringProperty(ConfigProperty.EDGE_PARENT_LABEL));
        while (children.hasNext()) {
            Vertex v = children.next();
            childList.add(new TP3Vertex(v));
        }
        long endTime = System.nanoTime();
        LOG.info("getChildren Total time = " + (endTime - startTime));
        LOG.info("--------------------------------------\n");
        return childList;
    }

    @Override
    public List<TP3Vertex> getChildrenByName(TP3Vertex node, String name) {

        List<TP3Vertex> childList = new ArrayList<>();
        long startTime = System.nanoTime();
        Iterator<Vertex> children = traversal.V(node.getId())
                .inE(config.getStringProperty(ConfigProperty.EDGE_PARENT_LABEL))
                .has(config.getStringProperty(ConfigProperty.EDGE_CHILD_NAME), name)
                .outV();

        long endTime = System.nanoTime();
        while (children.hasNext()) {
            Vertex v = children.next();
            childList.add(new TP3Vertex(v));
        }

        LOG.info("getChildrenByName Total time = " + (endTime - startTime));
        LOG.info("--------------------------------------\n");
        return childList;
    }


    @Override
    public TP3Vertex getParent(TP3Vertex node) {
        long startTime = System.nanoTime();
        Iterator<Vertex> parentIt = traversal.V(node.getId()).out(config.getStringProperty(ConfigProperty.EDGE_PARENT_LABEL));

        Vertex parent = parentIt.next();

        if (parentIt.hasNext()) {
            LOG.error("Node:" + node.getId() + " has more than one Parent node.");
        }
        long endTime = System.nanoTime();
        LOG.info("getParent Total time = " + (endTime - startTime));
        LOG.info(parent.property(NodeProperty.NODE_NAME.t()).toString());
        LOG.info("--------------------------------------\n");

        return new TP3Vertex(parent);
    }

    @Override
    public List<TP3Vertex> getVerticesByEdgeType(TP3Vertex node, String edgeType, Direction dir) {
        List<TP3Vertex> vertices = new ArrayList<>();
        org.apache.tinkerpop.gremlin.structure.Direction originalDirection = tp3Direction.mapToOriginal(dir);
        long startTime = System.nanoTime();
        Iterator<Vertex> it = traversal.V(node.getId()).toE(originalDirection, edgeType).toV(originalDirection);

        while (it.hasNext()) {
            Vertex v = it.next();
            vertices.add(new TP3Vertex(v));
        }
        long endTime = System.nanoTime();
        LOG.info("getVerticesByEdgeType Total time = " + (endTime - startTime));
        return vertices;
    }

    @Override
    public List<TP3Vertex> simpleFlow(TP3Vertex node,
                                      String edgeType, Direction dir) {
        List<TP3Vertex> reachable = new ArrayList<>();
        Deque<TP3Vertex> stack = new ArrayDeque<>();
        Map<String, Boolean> visited = new HashMap<>();
        org.apache.tinkerpop.gremlin.structure.Direction originalDirection = tp3Direction.mapToOriginal(dir);

        stack.add(node);
        long startTime = System.nanoTime();
        while (!stack.isEmpty()) {
            TP3Vertex currNode = stack.pop();

            if (visited.containsKey(currNode.getId())) {
                continue; //this node was already visited
            }


            Iterator<Edge> edges = traversal.V(currNode.getId()).toE(originalDirection, edgeType);

            while (edges.hasNext()) {
                Edge outgoingEdge = edges.next();
                Vertex neighbour = outgoingEdge.inVertex();

                stack.push(new TP3Vertex(neighbour));
            }

            visited.put(currNode.getId().toString(), true);
            reachable.add(currNode);
        }
        long endTime = System.nanoTime();
        LOG.info("simpleFlow Total time = " + (endTime - startTime));
        return reachable;
    }
}
