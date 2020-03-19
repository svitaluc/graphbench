package cz.cvut.fit.manta.graphbench.core.access;

import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.core.db.GraphDBConnector;

import java.util.List;


/**
 * Class for all the graph operations in the database.
 * @param <V> vertex extending the {@link Vertex} interface
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public abstract class GraphOperations<V extends Vertex> {

    /** Database representation. */
    protected GraphDBConnector db;

    /**
     * Constructor for the {@link GraphOperations}.
     * @param db Database connector
     */
    public GraphOperations(GraphDBConnector db) {
        this.db = db;
    }

    /**
     * Returns a list of children of the provided vertex.
     * @param node the starting vertex
     * @return a list of children of the provided vertex
     */
    public abstract List<V> getChildren(V node);

    /**
     * Returns a list of children with a specified name.
     * @param node the vertex which children will be acquired
     * @param name value of the property specifying a child name
     * @return list of children with specified name
     */
    public abstract List<V> getChildrenByName(V node, String name);

    /**
     * Returns a parent of the provided vertex.
     * @param node the starting vertex
     * @return a parent of the provided vertex
     */
    public abstract V getParent(V node);

    /**
     * Returns a list of vertices connected with the provided vertex by edges of the provided type and direction.
     * @param node the starting vertex
     * @param edgeType only edges with this type will be traversed
     * @param dir only edges of this direction will be traversed
     * @return a list of vertices connected with the provided vertex by edges of the provided type and direction
     */
    public abstract List<V> getVerticesByEdgeType(V node, String edgeType, Direction dir);

    /**
     * Starting from the provided node, it pursues the DFS search of the graph in the specified direction
     * and on the specified edge types.
     * No depth limits are set, so in the case of a connected graph, the whole graph can be traversed.
     * The visited nodes are remembered not to be traversed more times.
     * @param node the starting node
     * @param edgeType only edges with this type will be traversed
     * @param dir only edges of this direction will be traversed
     * @return a list of traversed vertices
     */
    public abstract List<V> simpleFlow(V node, String edgeType, Direction dir);
}
