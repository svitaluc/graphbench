package cz.cvut.fit.manta.graphbench.data.generator.model;

/**
 * Represents an edge attribute.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class EdgeAttribute extends Element {
    /** Name of the resulting file containing edge-attribute data. */
    public final static String FILE_NAME = "edge_attribute.csv";

    /** Name of the edge attribute (in some databases called a label or a tag). */
    public static String ATTRIBUTE_NAME = "edge_attribute";

    /** Key of the edge attribute. */
    private String key;
    /** Value of the edge attribute. */
    private String value;

    /**
     * @param key Key of the edge attribute
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @param value Value of the edge attribute
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String[] getStringSet() {
        return new String[]{id.toString(), key, value};
    }
}
