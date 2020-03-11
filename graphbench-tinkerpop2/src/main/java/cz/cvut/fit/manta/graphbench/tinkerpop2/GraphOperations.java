package cz.cvut.fit.manta.graphbench.tinkerpop2;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import cz.cvut.fit.manta.graphbench.core.config.Configuration;
import cz.cvut.fit.manta.graphbench.core.config.ConfigProperties;
import cz.cvut.fit.manta.graphbench.core.config.model.ConfigProperty;
import cz.cvut.fit.manta.graphbench.core.db.IGraphDBConnector;
import cz.cvut.fit.manta.graphbench.tinkerpop2.direction.TP2Direction;
import org.apache.log4j.Logger;
import cz.cvut.fit.manta.graphbench.core.access.IGraphOperations;
import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;

import java.util.*;

public class GraphOperations extends IGraphOperations<TP2Vertex> {
    final static Logger LOG = Logger.getLogger(GraphOperations.class);
    private Configuration config = ConfigProperties.getInstance();
    public GraphOperations(IGraphDBConnector db) {
        super(db);
    }

    @Override
    public List<TP2Vertex> getChildren(TP2Vertex node) {
        List<TP2Vertex> childList = new ArrayList<>();
        Iterable<Vertex> children = node.getVertex().query()
                .labels(config.getStringProperty(ConfigProperty.EDGE_PARENT_LABEL))
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
                .has(config.getStringProperty(ConfigProperty.EDGE_CHILD_NAME), name)
                .labels(config.getStringProperty(ConfigProperty.EDGE_PARENT_LABEL))
                .direction(com.tinkerpop.blueprints.Direction.IN)
                .vertices();

        //System.out.println("getChildrenByName:");
        for(Vertex v: children) {
            childList.add(new TP2Vertex(v));
            //System.out.println(v.getProperty(MainConfig.NODE_NAME).toString());
        }
        //System.out.println("--------------------------------------\n");
        return childList;
    }

    @Override
    public TP2Vertex getParent(TP2Vertex node) {
        Iterable<Vertex> parentIt = node.getVertex()
                .getVertices(com.tinkerpop.blueprints.Direction.OUT, config.getStringProperty(ConfigProperty.EDGE_PARENT_LABEL));
        Vertex parent = parentIt.iterator().next();

        if(parentIt.iterator().hasNext()) {
            LOG.error("Node:" + node.id() + " has more than one Parent node.");

        }

       /* System.out.println("getParent:");
        System.out.println(parent.getProperty(MainConfig.NODE_NAME).toString());
        System.out.println("--------------------------------------\n");*/
        return new TP2Vertex(parent);
    }

    @Override
    public List<TP2Vertex> getVerticesByEdgeType(TP2Vertex node, String edgeType, Direction dir) {
        List<TP2Vertex> vertices = new ArrayList<>();
        com.tinkerpop.blueprints.Direction originalDirection = TP2Direction.mapToOriginal(dir);
        Iterable<Vertex> it = node.getVertex().getVertices(originalDirection, edgeType);

        for(Vertex v: it) {
            vertices.add(new TP2Vertex(v));
            //System.out.println(v.getProperty(MainConfig.NODE_NAME).toString());
        }

        return vertices;
    }

    @Override
    public List<TP2Vertex> simpleFlow(TP2Vertex node, String edgeType, Direction dir) {
        List<TP2Vertex> reachable = new ArrayList<>();
        Deque<TP2Vertex> stack = new ArrayDeque<>();
        Map<String, Boolean> visited = new HashMap<>();
        com.tinkerpop.blueprints.Direction originalDirection = TP2Direction.mapToOriginal(dir);

        stack.add(node);

        while(!stack.isEmpty()) {
            TP2Vertex currNode = stack.pop();

            if(visited.containsKey(currNode.id())) {
                continue; //this node was already visited
            }

            Iterator<Edge> edges = currNode.getVertex().query().labels(edgeType).direction(originalDirection).edges().iterator();

            while (edges.hasNext()) {
                Edge outgoingEdge = edges.next();
                Vertex neighbour = outgoingEdge.getVertex(originalDirection.opposite());

                stack.push(new TP2Vertex(neighbour));
            }

            visited.put(currNode.id().toString(), true);
            reachable.add(currNode);
        }

        return reachable;
    }
}
