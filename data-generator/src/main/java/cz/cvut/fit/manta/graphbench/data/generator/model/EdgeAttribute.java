package cz.cvut.fit.manta.graphbench.data.generator.model;

/**
 * Represents an edge attribute.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class EdgeAttribute extends Element {
    public static final String FILE_NAME = "edge_attribute.csv";

    private final int EDGE_ATTRIBUTE_I_EDGE = 0;
    private final int EDGE_ATTRIBUTE_I_KEY = 1;
    private final int EDGE_ATTRIBUTE_I_VALUE = 2;

    public static String ATTRIBUTE_NAME = "edge_attribute";

    private String key;
    private String value;

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String[] getStringSet() {
        return new String[]{id.toString(), key, value};
    }
}
