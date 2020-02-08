package eu.profinit.manta.graphbench.data.generator.model;

public class Edge extends Entity {
    public static final String FILE_NAME = "edge.csv";

    private Long startNode;
    private Long endNode;
    private String name;

    public void setStartNode(Long startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(Long endNode) {
        this.endNode = endNode;
    }

    public void setName(String name) {
        this.name = name;
    };

    public Long getStartNode() {
        return startNode;
    }

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
