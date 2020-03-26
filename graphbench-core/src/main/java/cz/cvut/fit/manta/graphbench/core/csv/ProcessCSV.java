package cz.cvut.fit.manta.graphbench.core.csv;

import au.com.bytecode.opencsv.CSVReader;
import cz.cvut.fit.manta.graphbench.core.access.Edge;
import cz.cvut.fit.manta.graphbench.core.access.Vertex;
import cz.cvut.fit.manta.graphbench.core.config.ConfigProperties;
import cz.cvut.fit.manta.graphbench.core.config.Configuration;
import cz.cvut.fit.manta.graphbench.core.config.model.ConfigProperty;
import cz.cvut.fit.manta.graphbench.core.db.GraphDBConnector;
import cz.cvut.fit.manta.graphbench.core.db.Translator;
import cz.cvut.fit.manta.graphbench.core.db.structure.EdgeLabel;
import cz.cvut.fit.manta.graphbench.core.db.structure.NodeProperty;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.MessageFormat;

/**
 * Serves for processing the input csv files.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class ProcessCSV {

    /**  Logger. **/
    private final static Logger LOG = Logger.getLogger(ProcessCSV.class);
    /** Translator from vertex id defined by the loaded data to db's internal vertex id. **/
    private Translator translator = new Translator();
    /** Database representation. **/
    private GraphDBConnector<Vertex<?, ?>, Edge<?, ?>> db;
    /** Properties of the main configuration file. **/
    private final Configuration config = ConfigProperties.getInstance();
    /** How long the process of loading the csv file took. **/
    private long importTime;

    /** After how many items the data should be committed. **/
    private int commitEveryCount = config.getIntegerProperty(ConfigProperty.COMMIT_EVERY_COUNT);
    /** After how many items the information about the loading process should be logged. **/
    private int loadProgressInfoCount = config.getIntegerProperty(ConfigProperty.LOAD_PROGRESS_INFO_COUNT);

    /**
     * Creates a {@link BufferedReader} for a given csv file.
     * @param csvDir Directory in which the input csv files are stored
     * @param fileName Name of the csv file for which we want to create the reader
     * @return {@link BufferedReader} of a given csv file
     * @throws UnsupportedEncodingException When the file encoding is not supported
     * @throws FileNotFoundException When the given file in a given directory cannot be found
     */
    private BufferedReader getInput(String csvDir, String fileName) throws UnsupportedEncodingException, FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(new File(csvDir, fileName)),
                config.getStringProperty(ConfigProperty.CSV_ENCODING)));
    }

    /**
     * Constructor of the {@link ProcessCSV}.
     * @param database Underlying database representation
     */
    public ProcessCSV(GraphDBConnector<Vertex<?, ?>, Edge<?, ?>> database) {
        db = database;
    }

    /**
     * @return Translator from vertex id defined by the loaded data to db's internal vertex id
     */
    public Translator getTranslator() {
        return translator;
    }

    /**
     * Makes a commit and operations related to this action.
     */
    private void processCommit() {
        db.commit();

        translator.remapTempAndClear();
    }

    /**
     * Gets a vertex of a given id.
     * @param id Id of a vertex
     * @return {@link Vertex} representation of the vertex with the provided {@code id}
     */
    public Vertex<?,?> getNode(String id) {
        return db.getVertex(translator.getNode(id));
    }

    /**
     * Creates a vertex that becomes a super root.
     */
    public void addSuperRoot() {
       Vertex<?,?> sr = db.addEmptyVertex();
        
       sr.property(NodeProperty.NODE_NAME.t(), config.getStringProperty(ConfigProperty.SUPER_ROOT_NAME));
       sr.property(NodeProperty.NODE_TYPE.t(), config.getStringProperty(ConfigProperty.SUPER_ROOT_TYPE));

       translator.setSuperRootId(sr.getId().toString());
       LOG.info("Added super-root");
    }

    /**
     * Creates a node out of loaded information, split into individual items.
     * @param parts individual items of one record (one line of a csv file, already split)
     */
    private void addNode(String[] parts) {
        db.addVertex(parts, translator);
    }

    /**
     * Processes one line of a csv file containing information about individual nodes.
     * @param csvReader Reader of the csv file containing information about individual nodes
     * @param fileName Name of the file containing information about individual nodes (only for logging purposes)
     */
    private void processCSVLineNode(CSVReader csvReader, String fileName) {
        String[] parts;
        int progress = 0;

        try {
            while ((parts = csvReader.readNext()) != null) {
                addNode(parts);

                progress++;
                linePostprocessing(progress);
            }
        } catch (IOException e) {
            LOG.error(MessageFormat.format("Error when reading the file \"{0}\".", fileName), e);
        }
    }

    /**
     * Runs operations necessary after each line processing.
     * @param progress Number of processed items
     */
    private void linePostprocessing(int progress) {
        // To log or not to log
        if ((progress % loadProgressInfoCount == 0)) {
            LOG.info(MessageFormat.format("... processed records: {0}", progress));
        }

        // To commit or not to commit
        if ((commitEveryCount > 0) && ((progress % commitEveryCount) == 0)) {
            processCommit();
        }
    }

    /**
     * Gets a vertex according to its id stored in the {@code row} parameter on a position determined by the
     * {@code vertexPosition} parameter.
     * @param vertexPosition position of a vertex id to acquire in the {@code row} array
     * @param row {@link String} array containing various information including id of the vertex to acquire
     * @return Vertex of an id stored in a {@code row} on a position {@code vertexPosition}
     */
    private Vertex<?,?> getVertex(Integer vertexPosition, String[] row) {
        Vertex<?,?> node = null;
        if (vertexPosition < row.length && row[vertexPosition].length() > 0) {
            // tries to find a record about the vertex in a translator
            String nodeID = translator.getNode(row[vertexPosition]);
            if (nodeID != null) {
                // tries to get the vertex via translator
                node = (Vertex<?,?>) translator.getTempNode(row[vertexPosition]);
                if (node == null) {
                    // tries to get the vertex directly from a database
                    node = db.getVertex(nodeID);
                }
            } else {
                LOG.warn(MessageFormat.format("Didn't find a start node to set an edge. " +
                        "Original node ID: \"{0}\".", row[vertexPosition]));
            }
        }
        return node;
    }

    /**
     * Processes one line of a csv file containing information about individual edges.
     * @param csvReader Reader of the csv file containing information about individual edges
     * @param fileName Name of the file containing information about individual edges (only for logging purposes)
     */
    private void processCSVLineEdge(CSVReader csvReader, String fileName) {
        String[] parts;
        int progress = 0;
        try {
            while ((parts = csvReader.readNext()) != null) {
                Integer edgeStartPosition = config.getIntegerProperty(ConfigProperty.EDGE_I_START);
                Integer edgeEndPosition = config.getIntegerProperty(ConfigProperty.EDGE_I_END);

                Vertex<?, ?> startNode = getVertex(edgeStartPosition, parts);
                Vertex<?, ?> endNode = getVertex(edgeEndPosition, parts);

                String type;
                Integer edgeTypePosition = config.getIntegerProperty(ConfigProperty.EDGE_I_END);

                if (parts[edgeTypePosition].equalsIgnoreCase(EdgeLabel.FILTER.t())) {
                    type = EdgeLabel.FILTER.t();
                } else {
                    type = EdgeLabel.DIRECT.t();
                }

                if ((startNode == null) || (endNode == null))
                    LOG.warn(MessageFormat.format("Unsuccessful in finding vertices of an edge. " +
                                    "Start node id: \"{0}\", end node id: \"{1}\".",
                            parts[edgeStartPosition], parts[edgeEndPosition]));
                else {
                    //Zapsat hranu
                    Edge<?,?> edge;
                    edge = db.addEdge(startNode, endNode, type);
                    translator.putTempEdge(parts[config.getIntegerProperty(ConfigProperty.EDGE_I_ID)], edge);
                }

                progress++;
                linePostprocessing(progress);
            }
        }  catch (IOException e) {
            LOG.error(MessageFormat.format("Error when reading the file \"{0}\".", fileName), e);
            throw new IllegalStateException(MessageFormat.format("Error when reading the file \"{0}\".", fileName), e);
        }
    }

    /**
     * Processes one line of a csv file containing information about individual edge attributes.
     * @param csvReader Reader of the csv file containing information about individual edge attributes
     * @param fileName Name of the file containing information about individual edge attributes (only for logging purposes)
     */
    private void processCSVLineEdgeAttr(CSVReader csvReader, String fileName) {
        String[] parts;
        int progress = 0;

        try {
            while ((parts = csvReader.readNext()) != null) {
                Integer edgePosition = config.getIntegerProperty(ConfigProperty.EDGE_ATTRIBUTE_I_EDGE);

                if (parts[edgePosition].length() > 0) {
                    String edgeAttrID = translator.getEdge(parts[edgePosition]);

                    if (edgeAttrID != null) {

                        Edge<?,?> edgeAttr = db.getEdge(edgeAttrID);

                        if (edgeAttr != null) {
                            db.setEdgeProperty(edgeAttr,
                                    parts[config.getIntegerProperty(ConfigProperty.EDGE_ATTRIBUTE_I_KEY)],
                                    parts[config.getIntegerProperty(ConfigProperty.EDGE_ATTRIBUTE_I_VALUE)]);
                        } else LOG.warn(
                                MessageFormat.format("The database cannot find an edge of the edge attribute. " +
                                                "Original edge id: \"{0}\", new edge id: \"{1}\"",
                                        parts[edgePosition], edgeAttrID));
                    } else LOG.warn(MessageFormat.format("Edge id for setting the edge attribute wasn't found. " +
                            "Original edge id: \"{0}\".", parts[edgePosition]));
                }

                progress++;
                linePostprocessing(progress);
            }
        } catch (IOException e) {
            LOG.error(MessageFormat.format("Error when reading the file \"{0}\".", fileName), e);
            throw new IllegalStateException(MessageFormat.format("Error when reading the file \"{0}\".", fileName), e);
        }
    }

    /**
     * @return Value of how long the process of loading the csv file took
     */
    public long getImportTime() {
        return importTime;
    }

    /**
     * Loads the whole csv file.
     * @param csvDir Directory of the csv file
     * @param type Type ({@link CSVType}) of the file to load
     */
    public void loadCSV(String csvDir, CSVType type) {
        String fileName = type.getFileName();

        Integer progress = 0;
        CSVReader csvReader = null;
        long startTime = System.currentTimeMillis();
        try {
            csvReader = new CSVReader(getInput(csvDir, fileName));
            Validate.notNull(type);
            switch (type) {
                case NODE: processCSVLineNode(csvReader, fileName);
                    break;
                case EDGE: processCSVLineEdge(csvReader, fileName);
                    break;
                case EDGE_ATTR: processCSVLineEdgeAttr(csvReader, fileName);
                    break;
                default: throw new IllegalArgumentException (MessageFormat.format(
                        "Wrong csv file type that cannot be processed: \"{0}\".", fileName, type.toString()));
            }
        } catch (FileNotFoundException e) {
            LOG.error(MessageFormat.format("The file \"{0}\" does not exist in the directory \"{1}\".",
                            fileName, csvDir), e);
        } catch (IOException e) {
            String message = "Error when reading the file \"{0}\".";
            LOG.error(MessageFormat.format(message, fileName), e);
            throw new IllegalArgumentException (MessageFormat.format(message, fileName), e);
        } catch (Throwable e) {
            String message = "Error when processing a row.";
            LOG.error(message, e);
            throw new IllegalArgumentException (message, e);
        } finally {
            IOUtils.closeQuietly(csvReader);
            importTime = System.currentTimeMillis() - startTime;
        }

        LOG.info(MessageFormat.format("...total number of processed records {0}: {1}", type.toString(), progress));

        processCommit();
    }
}
