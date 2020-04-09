package cz.cvut.fit.manta.graphbench.data.generator.model;

/**
 * Represents an edge.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class Edge extends Element {
    /** Name of the resulting file containing edge data. */
    public final static String FILE_NAME = "edge.csv";

    /** Start node of the edge. */
    private Long startNode;
    /** End node of the edge. */
    private Long endNode;
    /** Name of the edge (in some databases called a label or a tag). */
    private String name;

    /**
     * @param startNode Start node of the edge
     */
    public void setStartNode(Long startNode) {
        this.startNode = startNode;
    }

    /**
     * @param endNode End node of the edge
     */
    public void setEndNode(Long endNode) {
        this.endNode = endNode;
    }

    /**
     * @param name Name of the edge (in some databases called a label or a tag)
     */
    public void setName(String name) {
        this.name = name;
    };

    /**
     * @return Start node of the edge
     */
    public Long getStartNode() {
        return startNode;
    }

    /**
     * @return End node of the edge
     */
    public Long getEndNode() {
        return endNode;
    }

    @Override
    public String[] getStringSet() {
        return new String[]{id.toString(), startNode.toString(), endNode.toString(), name};
    }

    @Override
    public String toString() {
        return startNode + ", " + endNode + ", " + name;
    }
}
