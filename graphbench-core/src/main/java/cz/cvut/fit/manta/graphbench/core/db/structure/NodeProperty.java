package cz.cvut.fit.manta.graphbench.core.db.structure;

/**
 * All available node properties.
 */
public enum NodeProperty {

    /** Node type. */
    NODE_TYPE("nodeType"),
    /** Node name. */
    NODE_NAME("nodeName"),

    /** Flag labeling a super root node of the whole graph. */
    SUPER_ROOT("superRoot");

    private final String text;

    /**
     * @param text text value for the database
     */
    NodeProperty(String text) {
        this.text = text;
    }

    /**
     * @return text value for the database
     */
    public String t() {
        return text;
    }
}
