package eu.profinit.manta.graphbench.core.csv;

import au.com.bytecode.opencsv.CSVReader;
import eu.profinit.manta.graphbench.core.config.Config;
import eu.profinit.manta.graphbench.core.config.Property;
import eu.profinit.manta.graphbench.core.db.IGraphDBConnector;
import eu.profinit.manta.graphbench.core.db.Translator;
import eu.profinit.manta.graphbench.core.db.structure.EdgeLabel;
import eu.profinit.manta.graphbench.core.db.structure.NodeProperty;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import eu.profinit.manta.graphbench.core.access.IEdge;
import eu.profinit.manta.graphbench.core.access.IVertex;

import java.io.*;
import java.text.MessageFormat;

public class ProcessCSV {

    private final Logger LOG = Logger.getLogger(ProcessCSV.class);
    private Translator trans = new Translator();
    private IGraphDBConnector db;
    private Config config = Config.getInstance();

    private int commitEveryCount = config.getIntegerProperty(Property.COMMIT_EVERY_COUNT);

    private BufferedReader getInput(String csvDir, String fileName) throws UnsupportedEncodingException, FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(new File(csvDir, fileName)),
                config.getStringProperty(Property.CSV_ENCODING)));
    }

    public ProcessCSV(IGraphDBConnector database) {
        db = database;
    }

    public Translator getTranslator() {
        return trans;
    }

    private void processCommit() {
        db.commit();

        trans.remapTempAndClear();
    }

    public IVertex getNode(String id) {
        return db.getVertex(trans.get(id));
    }
    
    public void addSuperRoot() {
       IVertex sr = db.addVertex();
        
       sr.property(NodeProperty.NODE_NAME.t(), config.getStringProperty(Property.SUPER_ROOT_NAME));
       sr.property(NodeProperty.NODE_TYPE.t(), config.getStringProperty(Property.SUPER_ROOT_TYPE));

       trans.setSuperRootId(sr.id().toString());
       LOG.info("Added super-root");
    }

    private void addVertexNode(String[] parts) {
        db.addVertexNode(parts, trans);
    }
    
    private void processCSVLineNode(CSVReader csvReader, String fileName) {
        String[] parts;
        int progress = 0;

        try {
            while ((parts = csvReader.readNext()) != null) {
                addVertexNode(parts);

                progress++;
                linePostprocessing(progress);
            }
        } catch (IOException e) {
            LOG.error(MessageFormat.format("Error when reading the file \"{0}\".", fileName), e);
            throw new IllegalStateException(MessageFormat.format("Error when reading the file \"{0}\".", fileName), e);
        }
    }

    private void linePostprocessing(int progress) {
        // To log or not to log
        if ((progress % config.getIntegerProperty(Property.LOAD_PROGRESS_INFO_COUNT) == 0)) {
            LOG.info(MessageFormat.format("... processed records: {0}", progress));
        }

        // To commit or not to commit
        if ((commitEveryCount > 0) && ((progress % commitEveryCount) == 0)) {
            processCommit();
        }
    }

    private IVertex getVertex(Integer vertexPosition, String[] row) {
        IVertex node = null;
        if (row[vertexPosition].length() > 0) {
            String nodeID = trans.get(row[vertexPosition]);
            if (nodeID != null) {

                node = (IVertex)trans.getTemp(row[vertexPosition]);
                if(node == null) {
                    node = db.getVertex(nodeID);
                }
            } else LOG.warn(MessageFormat.format("Didn't find a start node to set an edge. Original node ID: \"{0}\".", row[vertexPosition]));
        }
        return node;
    }

    private void processCSVLineEdge(CSVReader csvReader, String fileName) {
        String[] parts;
        int progress = 0;
        try {
            while ((parts = csvReader.readNext()) != null) {
                Integer edgeStartPosition = config.getIntegerProperty(Property.EDGE_I_START);
                Integer edgeEndPosition = config.getIntegerProperty(Property.EDGE_I_END);

                IVertex startNode = getVertex(edgeStartPosition, parts);
                IVertex endNode = getVertex(edgeEndPosition, parts);

                String type;
                Integer edgeTypePosition = config.getIntegerProperty(Property.EDGE_I_END);

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
                    IEdge edge;
                    edge = db.addEdge(startNode, endNode, type);
                    trans.putTempEdge(parts[config.getIntegerProperty(Property.EDGE_I_ID)], edge);
                }

                progress++;
                linePostprocessing(progress);
            }
        }  catch (IOException e) {
            LOG.error(MessageFormat.format("Error when reading the file \"{0}\".", fileName), e);
            throw new IllegalStateException(MessageFormat.format("Error when reading the file \"{0}\".", fileName), e);
        }
    }

    private void processCSVLineEdgeAttr(CSVReader csvReader, String fileName) {
        String[] parts;
        int progress = 0;

        try {
            while ((parts = csvReader.readNext()) != null) {
                Integer edgePosition = config.getIntegerProperty(Property.EDGE_ATTRIBUTE_I_EDGE);

                if (parts[edgePosition].length() > 0) {
                    String edgeAttrID = trans.getEdge(parts[edgePosition]);

                    if (edgeAttrID != null) {

                        IEdge edgeAttr = db.getEdge(edgeAttrID);

                        if (edgeAttr != null) {
                            db.setEdgeProperty(edgeAttr,
                                    parts[config.getIntegerProperty(Property.EDGE_ATTRIBUTE_I_KEY)],
                                    parts[config.getIntegerProperty(Property.EDGE_ATTRIBUTE_I_VALUE)]);
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

    

    public void loadCSV(String csvDir, CSVType typ) {
        String fileName = typ.getFileName();

        Integer progress = 0;
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(getInput(csvDir, fileName));
            switch (typ) {
                case NODE: processCSVLineNode(csvReader, fileName);
                    break;
                case EDGE: processCSVLineEdge(csvReader, fileName);
                    break;
                case EDGE_ATTR: processCSVLineEdgeAttr(csvReader, fileName);
                    break;
                default: throw new IllegalStateException(MessageFormat.format(
                        "Wrong csv file type that cannot be processed: \"{0}\".", fileName, typ.toString()));
            }
        } catch (FileNotFoundException e) {
            LOG.error(MessageFormat.format("The file \"{0}\" does not exist in the directory \"{1}\".",
                            fileName, csvDir), e);
        } catch (IOException e) {
            String message = "Error when reading the file \"{0}\".";
            LOG.error(MessageFormat.format(message, fileName), e);
            throw new IllegalStateException(MessageFormat.format(message, fileName), e);
        } catch (Throwable e) {
            String message = "Error when processing a row.";
            LOG.error(message, e);
            throw new IllegalStateException(message, e);
        } finally {
            IOUtils.closeQuietly(csvReader);
        }

        LOG.info(MessageFormat.format("...total number of processed records {0}: {1}", typ.toString(), progress));

        processCommit();
    }
}
