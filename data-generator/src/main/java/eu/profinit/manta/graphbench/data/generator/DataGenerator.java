package eu.profinit.manta.graphbench.data.generator;

import au.com.bytecode.opencsv.CSVWriter;
import eu.profinit.manta.graphbench.data.generator.model.Edge;
import eu.profinit.manta.graphbench.data.generator.model.EdgeAttribute;
import eu.profinit.manta.graphbench.data.generator.model.Entity;
import eu.profinit.manta.graphbench.data.generator.model.Node;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

public class DataGenerator {
    private Logger LOGGER = Logger.getLogger(DataGenerator.class);
    private final int LOG_EACH = 1000;
    private Long items = 0L;
    private String outputPath;

    private Map<Long, Node> nodes = new HashMap<>();
    private Map<Long, Edge> edges = new HashMap<>();
    private Map<Long, EdgeAttribute> edgeAttributes = new HashMap<>();
    private List<Long> nodeKeys = new ArrayList<>();
    private List<Long> edgeKeys = new ArrayList<>();
    private Random random = new Random();

    public DataGenerator(String path) {
        outputPath = path;
        if (!outputPath.endsWith("/")) {
            outputPath += "/";
        }
    }

    public void generate(Long nodesAmount, Long edgesAmount) throws IOException {
        generateNodesAndEdges(nodesAmount, edgesAmount);
        generateEdgeAttributes();

        writeFiles();
    }

    private void writeFiles()  throws IOException {
        CSVWriter nodeWriter;
        CSVWriter edgeWriter;
        CSVWriter edgeAttributeWriter;
        try {
            nodeWriter = createWriter(outputPath + Node.FILE_NAME);
            edgeWriter = createWriter(outputPath + Edge.FILE_NAME);
            edgeAttributeWriter = createWriter(outputPath + EdgeAttribute.FILE_NAME);
        } catch (IOException e) {
            LOGGER.error("Couldn't create a writer for a file.");
            throw new IOException(e);
        }

        writeToFile(nodeWriter, nodes);
        writeToFile(edgeWriter, edges);
        writeToFile(edgeAttributeWriter, edgeAttributes);

        nodeWriter.close();
        edgeWriter.close();
        edgeAttributeWriter.close();
    }

    private <E extends Entity> void writeToFile(CSVWriter writer, Map<Long, E> map) {
        for (Entity e : map.values()) {
            writer.writeNext(e.getStringSet());
        }
    }

    private void generateNodesAndEdges(Long nodesAmount, Long edgesAmount) {

        Long nodeIdCounter = 0L;
        Long edgeIdCounter = 0L;
        Node parent = new Node();

        storeParentNode(nodes, nodeKeys, parent, nodeIdCounter, UUID.randomUUID().toString());

        // generate nodes and edges connected these nodes with their parents
        while (nodeIdCounter < nodesAmount) {
            nodeIdCounter++;
            Long parentId = getRandomItem(nodeKeys);

            storeNode(nodes, nodeKeys, new Node(), nodeIdCounter, UUID.randomUUID().toString(), parentId);
            storeEdge(edges, edgeKeys, new Edge(), edgeIdCounter, UUID.randomUUID().toString(), parentId, nodeIdCounter);

            edgeIdCounter++;
            if (items++ % LOG_EACH == 0) {
                LOGGER.info("Generated " + items + " nodes.");
            }
        }
        // generate additional edges to reach the desired amount

        // clique check - division by 2 is not present because we consider directed edges. Check < 0 for the case of
        // a multiplication result overflow.
        Long cliqueEdges = Long.valueOf(nodes.size()) * (nodes.size() - 1);
        if (edgesAmount > cliqueEdges || cliqueEdges < 0) {
            throw new IllegalArgumentException("Cannot generate more unique edges than the number of edges in a clique. " +
                    "In order to enable this feature, refactor the behavior to generate any new edge end" +
                    " (instead of a not-yet connected one).");
        }
        while (edgeIdCounter < edgesAmount) {
            Long startId = getRandomItem(nodeKeys);
            Long endId = getNewRandomEdgeEnd(startId);
            storeEdge(edges, edgeKeys, new Edge(), edgeIdCounter, UUID.randomUUID().toString(), startId, endId);
            edgeIdCounter++;
            if (items++ % LOG_EACH == 0) {
                LOGGER.info("Generated " + items + " edges.");
            }
        }
    }

    private Long getRandomItem(List<Long> list) {
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Finds an id of a vertex which is not yet a an ending vertex of an edge starting in a vertex with a @startId.
     * Warning: the more existing edges the higher the probability that the process of finding a not connected vertex
     * will take a long time.
     * @param startId id of a vertex
     * @return an id of a vertex not yet connected with the vertex of a @startId in the direction v(startId) -> v(endId).
     */
    private Long getNewRandomEdgeEnd(Long startId) {
        Long endId = getRandomItem(nodeKeys);
        while (endId.equals(startId) || edgeExists(startId, endId)) {
            endId = getRandomItem(nodeKeys);
        }
        return endId;
    }

    /**
     * Checks existence of an edge with a given start vertex id and end vertex id. It expects directed edges
     * (if there is an edge v1 -> v2 and we ask for an edge with a start vertex v2 and an end vertex v1,
     * the return value is false).
     * @param startId id of a start vertex
     * @param endId id of an end vertex
     * @return true if an edge with a starting vertex of a @startId and an ending vertex of an @endId exists.
     */
    private boolean edgeExists(Long startId, Long endId) {
        for (Edge e : edges.values()) {
            if (e.getStartNode().equals(startId) && e.getEndNode().equals(endId)) {
                return true;
            }
        }
        return false;
    }

    private void generateEdgeAttributes() {
        int amount = edgeKeys.size();

        for (Long key : edgeKeys) {
            storeEdgeAttribute(edgeAttributes, new EdgeAttribute(), key, EdgeAttribute.ATTRIBUTE_NAME, UUID.randomUUID().toString());
        }
    }

    private void storeEdgeAttribute(Map<Long, EdgeAttribute> map, EdgeAttribute edgeAttribute, Long edgeKey, String attributeKey, String attributeValue) {
        edgeAttribute.setId(edgeKey);
        edgeAttribute.setKey(attributeKey);
        edgeAttribute.setValue(attributeValue);

        map.put(edgeKey, edgeAttribute);
    }

    private void storeParentNode(Map<Long, Node> map, List<Long> keys, Node node, Long id, String name) {
        storeNode(map, keys, node, id, name, null);
    }

    private void storeNode(Map<Long, Node> map, List<Long> keys, Node node, Long id, String name, Long parent) {
        node.setId(id);
        node.setName(name);
        node.setParent(parent);

        map.put(id, node);
        keys.add(id);
    }

    private void storeEdge(Map<Long, Edge> map, List<Long> keys, Edge edge, Long id, String name, Long start, Long end) {
        edge.setId(id);
        edge.setName(name);
        edge.setStartNode(start);
        edge.setEndNode(end);

        map.put(id, edge);
        keys.add(id);
    }

    private CSVWriter createWriter(String path) throws IOException {
        File outputFile = new File(path);

        if (outputFile.exists()) {
            outputFile.delete();
        }
        outputFile.createNewFile();

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
        return new CSVWriter(new BufferedWriter(streamWriter));
    }
}
