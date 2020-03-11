package cz.cvut.fit.manta.graphbench.data.generator.model;

/**
 * Represents a node.
 */
public class Node extends Element {
    public static final String FILE_NAME = "node.csv";

    private Long parent;
    private String name;

    public void setParent(Long id) {
        this.parent = id;
    }

    public void setName(String name) {
        this.name = name;
    };

    @Override
    public String[] getStringSet() {
        if (parent != null) {
            return new String[]{id.toString(), parent.toString(), name};
        }
        return new String[]{id.toString(), "", name};
    }
}
