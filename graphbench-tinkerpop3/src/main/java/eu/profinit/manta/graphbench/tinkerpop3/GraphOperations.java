package eu.profinit.manta.graphbench.tinkerpop3;

import eu.profinit.manta.graphbench.core.config.Configuration;
import eu.profinit.manta.graphbench.core.config.ConfigProperties;
import eu.profinit.manta.graphbench.core.config.model.ConfigProperty;
import eu.profinit.manta.graphbench.core.db.IGraphDBConnector;
import eu.profinit.manta.graphbench.core.db.structure.NodeProperty;
import org.apache.log4j.Logger;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import eu.profinit.manta.graphbench.core.access.IGraphOperations;
import eu.profinit.manta.graphbench.core.access.direction.Direction;
import eu.profinit.manta.graphbench.tinkerpop3.direction.TP3Direction;

import java.util.*;


public class GraphOperations extends IGraphOperations<TP3Vertex> {
    final static Logger LOG = Logger.getLogger(GraphOperations.class);
    private GraphTraversalSource traversal;
    private Configuration config = ConfigProperties.getInstance();

    public GraphOperations(IGraphDBConnector<TP3Vertex, TP3Edge> db) {
        super(db);
        traversal = db.getTraversal();
    }

    @Override
    public List<TP3Vertex> getChildren(TP3Vertex node) {
        List<TP3Vertex> childList = new ArrayList<>();
        long startTime = System.nanoTime();
        Iterator<Vertex> children = traversal.V(node.id()).in(config.getStringProperty(ConfigProperty.EDGE_PARENT_LABEL));
        while(children.hasNext()) {
            Vertex v = children.next();
            childList.add(new TP3Vertex(v));
        }
        long endTime = System.nanoTime();
        System.out.println("getChildren Total time = " + (endTime - startTime));
        System.out.println("--------------------------------------\n");
        return childList;
    }

    @Override
    public List<TP3Vertex> getChildrenByName(TP3Vertex node, String name) {

        List<TP3Vertex> childList = new ArrayList<>();
        long startTime = System.nanoTime();
        Iterator<Vertex> children = traversal.V(node.id())
                .inE(config.getStringProperty(ConfigProperty.EDGE_PARENT_LABEL))
                .has(config.getStringProperty(ConfigProperty.EDGE_CHILD_NAME), name)
                .outV();

        long endTime = System.nanoTime();
        while(children.hasNext()) {
            Vertex v = children.next();
            childList.add(new TP3Vertex(v));
        }

        System.out.println("getChildrenByName Total time = " + (endTime - startTime));
        System.out.println("--------------------------------------\n");
        return childList;
    }


    @Override
    public TP3Vertex getParent(TP3Vertex node) {
        long startTime = System.nanoTime();
        Iterator<Vertex> parentIt = traversal.V(node.id()).out(config.getStringProperty(ConfigProperty.EDGE_PARENT_LABEL));

        Vertex parent = parentIt.next();

        if(parentIt.hasNext()) {
            LOG.error("Node:" + node.id() + " has more than one Parent node.");
        }
        long endTime = System.nanoTime();
        System.out.println("getParent Total time = " + (endTime - startTime));
        System.out.println(parent.property(NodeProperty.NODE_NAME.t()).toString());
        System.out.println("--------------------------------------\n");

        return new TP3Vertex(parent);
    }

    @Override
    public List<TP3Vertex> getVerticesByEdgeType(TP3Vertex node, String edgeType, Direction dir) {
        List<TP3Vertex> vertices = new ArrayList<>();
        org.apache.tinkerpop.gremlin.structure.Direction originalDirection = TP3Direction.mapToOriginal(dir);
        long startTime = System.nanoTime();
        Iterator<Vertex> it = traversal.V(node.id()).toE(originalDirection, edgeType).toV(originalDirection);

        while(it.hasNext()) {
            Vertex v = it.next();
            vertices.add(new TP3Vertex(v));
        }
        long endTime = System.nanoTime();
        System.out.println("getVerticesByEdgeType Total time = " + (endTime - startTime));
        return vertices;
    }

    @Override
    public List<TP3Vertex> simpleFlow(TP3Vertex node,
                                      String edgeType, Direction dir) {
        List<TP3Vertex> reachable = new ArrayList<>();
        Deque<TP3Vertex> stack = new ArrayDeque<>();
        Map<String, Boolean> visited = new HashMap<>();
        org.apache.tinkerpop.gremlin.structure.Direction originalDirection = TP3Direction.mapToOriginal(dir);

        stack.add(node);
        long startTime = System.nanoTime();
        while(!stack.isEmpty()) {
            TP3Vertex currNode = stack.pop();

            if(visited.containsKey(currNode.id())) {
                continue; //this node was already visited
            }


            Iterator<Edge> edges = traversal.V(currNode.id()).toE(originalDirection, edgeType);

            while (edges.hasNext()) {
                Edge outgoingEdge = edges.next();
                Vertex neighbour = outgoingEdge.inVertex();

                stack.push(new TP3Vertex(neighbour));
            }

            visited.put(currNode.id().toString(), true);
            reachable.add(currNode);
        }
        long endTime = System.nanoTime();
        System.out.println("simpleFlow Total time = " + (endTime - startTime));
        return reachable;
    }
}
