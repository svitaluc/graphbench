package cz.cvut.fit.manta.graphbench.core.db.structure;

/**
 * All available edge labels.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum EdgeLabel {
    /** Denoting parenting relationship. */
    HAS_PARENT("hasParent"),
    /** Direct relationship between the two nodes. */
    DIRECT("direct"),
    /** Indirect relationship between the two nodes. */
    FILTER("filter");

    private String text;

    /**
     * @param text text value for the database
     */
    EdgeLabel(String text) {
        this.text = text;
    }

    /**
     * @return text value for the database
     */
    public String t() {
        return text;
    }


    /**
     * Factory creating the enum instance from its string representation.
     * @param input string name of the label
     * @return enum instance. Null if the string doesn't match any of the enum instances.
     */
    public static EdgeLabel parseFromDbType(String input) {
        for (EdgeLabel item : values()) {
            if (item.text.equals(input)) {
                return item;
            }
        }
        return null;
    }
}
