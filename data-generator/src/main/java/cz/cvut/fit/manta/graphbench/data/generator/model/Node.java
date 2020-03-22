package cz.cvut.fit.manta.graphbench.data.generator.model;

/**
 * Represents a node.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class Node extends Element {
    /** Name of the resulting file containing node data. */
    public static final String FILE_NAME = "node.csv";

    /** Id of a parent node. */
    private Long parent;
    /** Name of the node (in some databases called a label or a tag). */
    private String name;

    /**
     * @param id Id of a parent node
     */
    public void setParent(Long id) {
        this.parent = id;
    }

    /** Name of the node (in some databases called a label or a tag) */
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
