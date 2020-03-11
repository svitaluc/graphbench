package cz.cvut.fit.manta.graphbench.core.db.structure;

/**
 * All available edge properties.
 */
public enum EdgeProperty {
    /** Call identification of an edge */ //TODO pryc
    EDGE_ATTRIBUTE("edge_attribute");

    private final String text;

    /**
     * @param text text value for the database
     */
    EdgeProperty(String text) {
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
    public static EdgeProperty parseFromDbType(String input) {
        for (EdgeProperty item : values()) {
            if (item.text.equals(input)) {
                return item;
            }
        }
        return null;
    }
}
