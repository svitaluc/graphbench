package cz.cvut.fit.manta.graphbench.core.csv;

import cz.cvut.fit.manta.graphbench.core.config.ConfigProperties;
import cz.cvut.fit.manta.graphbench.core.config.model.ConfigProperty;

/**
 * Type of the csv file. It determines whether the file contains information about nodes, edges, etc.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum CSVType {
    /** CSV file containing information about nodes. */
    NODE(ConfigProperties.getInstance().getStringProperty(ConfigProperty.CSV_NODE_NAME)),
    /** CSV file containing information about edges. */
    EDGE(ConfigProperties.getInstance().getStringProperty(ConfigProperty.CSV_EDGE_NAME)),
    /** CSV file containing information about edge attributes. */
    EDGE_ATTR(ConfigProperties.getInstance().getStringProperty(ConfigProperty.CSV_EDGE_ATTR_NAME));

    /** Name of the file which stores given type of data. **/
    private String filename;

    /**
     * Constructor of the {@link CSVType}.
     * @param filename Name of the file which stores given type of data
     */
    private CSVType(String filename) {
        this.filename = filename;
    }

    /**
     * @return Name of the file which stores given type of data
     */
    public String getFileName() {
        return filename;
    }
}
