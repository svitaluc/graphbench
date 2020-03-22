package cz.cvut.fit.manta.graphbench.core.db.structure;

/**
 * All available edge properties.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum EdgeProperty {
    /** General edge attribute. */
    EDGE_ATTRIBUTE("edge_attribute");

    /** Text of the property. */
    private String text;

    /**
     * @param text Text value for the database
     */
    EdgeProperty(String text) {
        this.text = text;
    }

    /**
     * @return Text value for the database
     */
    public String t() {
        return text;
    }

    /**
     * Factory creating the enum instance from its string representation.
     * @param input String name of the label
     * @return Enum instance. Null if the string doesn't match any of the enum instances.
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
