package cz.cvut.fit.manta.graphbench.data.generator;

import au.com.bytecode.opencsv.CSVWriter;
import cz.cvut.fit.manta.graphbench.data.generator.model.Edge;
import cz.cvut.fit.manta.graphbench.data.generator.model.EdgeAttribute;
import cz.cvut.fit.manta.graphbench.data.generator.model.Element;
import cz.cvut.fit.manta.graphbench.data.generator.model.Node;
import org.apache.log4j.Logger;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Class generating the required data.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class DataGenerator {
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(DataGenerator.class);
    /** After how many generated items the progress is logged. */
    private final int LOG_EACH = 1000;
    /** Path of a directory into which the data will be generated. */
    private String outputPath;

    /** Map of generated nodes. The key is the node id and the value is the node itself. */
    private Map<Long, Node> nodes = new HashMap<>();
    /** Map of generated edges. The key is the edge id and the value is the edge itself. */
    private Map<Long, Edge> edges = new HashMap<>();
    /** Map of generated edge attributes. The key is the edge-attribute id and the value is the edge attribute itself. */
    private Map<Long, EdgeAttribute> edgeAttributes = new HashMap<>();
    /** List of node ids. Although this information is duplicate since it's present in the {@code nodes} attribute,
     * it's present for the performance purposes. */
    private List<Long> nodeKeys = new ArrayList<>();

    /**
      * Constructor of the {@link DataGenerator}.
      * @param path Path of a directory into which the data will be generated
     */
    DataGenerator(String path) {
        outputPath = path;
        if (!outputPath.endsWith("/")) {
            outputPath += "/";
        }
    }

    /**
     * Generates all the data - nodes, edges and edge attributes - and writes them into corresponding files.
     * @param nodesAmount amount of nodes to be generated
     * @param edgesAmount amount of edges to be generated
     * @throws IOException when the data cannot be written into files
     */
    void generate(Long nodesAmount, Long edgesAmount) throws IOException {
        generateNodesAndEdges(nodesAmount, edgesAmount);
        generateEdgeAttributes();

        writeFiles();
    }

    /**
     * Writes all generated data (nodes, edges, edge attributes) into individual files.
     * @throws IOException when there is a problem with writing files in the {@code outputPath}
     * set via constructor.
     */
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

    /**
     * Writes data to an individual file
     * @param writer {@link CSVWriter} of a file
     * @param map {@link Map} which values contain individual elements
     * @param <E> {@link Element} subtype
     */
    private <E extends Element> void writeToFile(CSVWriter writer, Map<Long, E> map) {
        for (Element e : map.values()) {
            writer.writeNext(e.getStringSet());
        }
    }

    /**
     * Generates nodes and edges which amount is determined by parameters.
     * @param nodesAmount Number of nodes to be generated
     * @param edgesAmount Number of edges to be generated
     */
    private void generateNodesAndEdges(Long nodesAmount, Long edgesAmount) {
        Long nodeIdCounter = 0L;
        Long edgeIdCounter = 0L;

        storeParentNode(nodes, nodeKeys, nodeIdCounter, UUID.randomUUID().toString());

        // generate nodes and edges connected these nodes with their parents
        while (nodeIdCounter < nodesAmount) {
            nodeIdCounter++;
            edgeIdCounter++;
            Long parentId = getRandomItem(nodeKeys);

            storeNode(nodes, nodeKeys, nodeIdCounter, UUID.randomUUID().toString(), parentId);
            storeEdge(edges, edgeIdCounter, UUID.randomUUID().toString(), parentId, nodeIdCounter);
            if (nodeIdCounter % LOG_EACH == 0) {
                LOGGER.info("Generated " + nodeIdCounter + " nodes.");
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

        try {
            generateEdgesParallel(edgesAmount);
        } catch (InterruptedException e) {
            LOGGER.error("Error when generating additional edges in parallel.", e);
        }
    }

    /**
     * Generates edges in parallel threads.
     * @param edgesAmount Number of edges to generate
     * @throws InterruptedException If the thread was interrupted while waiting
     */
    private void generateEdgesParallel(Long edgesAmount) throws InterruptedException {
        int numberOfThreads = 25;
        int numberOfSets = 5;
        long edgeIdFrom =  edges.size();
        long edgesToGenerate = edgesAmount - edgeIdFrom;
        long bulk = Math.floorDiv(edgesToGenerate, numberOfThreads);

        List<List<Long>> nodeIdLists = createNodeIdLists(numberOfSets);
        List<int[]> combinations = new ArrayList<>();
        Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(numberOfSets, 2);
        while (iterator.hasNext()) {
            combinations.add(iterator.next());
        }

        List<Callable<Map<Long, Edge>>> tasks = new ArrayList<>();
        // generate callables within a set
        for (int i = 0; i < numberOfSets; i++) {
            tasks.add(generateEdgesCallable(nodeIdLists.get(i), (i * bulk) + edgeIdFrom, bulk));
        }
        edgeIdFrom += numberOfSets * bulk;
        edgesToGenerate = edgesAmount - edgeIdFrom;

        // generate callables between sets
        for (int i = 0; i < combinations.size(); i++) {
            int[] combination = combinations.get(i);
            long numberOfEdges = (i == combinations.size()-1) ? bulk + edgesToGenerate%numberOfThreads : bulk;
            tasks.add(generateEdgesCallable(nodeIdLists.get(combination[0]), nodeIdLists.get(combination[1]), (i * bulk) + edgeIdFrom, numberOfEdges));
        }

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        try {
            long start = System.currentTimeMillis();
            List<Future<Map<Long, Edge>>> results = executor.invokeAll(tasks);
            for (Future<Map<Long, Edge>> bulkEdges : results) {
                bulkEdges.get().forEach(edges::putIfAbsent);
            }
            long elapsed = System.currentTimeMillis() - start;
            LOGGER.info("Number of resulting nodes: " + nodes.size());
            LOGGER.info("Number of resulting edges: " + edges.size());
            LOGGER.info("Time elapsed: " + elapsed);
        } catch (ExecutionException e) {
            LOGGER.error("Can't get number of edges.", e);
        }
        finally {
            executor.shutdown();
        }
    }

    /**
     * Creates a list of lists with node ids, where each id is contained exactly in one list.
     * @param numberOfCollections Number of inner collections to be created.
     * @return List of lists with node ids, where each id is contained exactly in one list.
     */
    private List<List<Long>> createNodeIdLists(int numberOfCollections) {
        int bulk = Math.floorDiv(nodeKeys.size(), numberOfCollections);
        int lastBulk = nodeKeys.size() % numberOfCollections;
        List<List<Long>> setList = new ArrayList<>();
        for (int i = 0; i < numberOfCollections; i++) {
            int startIndex = i * bulk;
            int endIndex = (i == numberOfCollections-1) ? startIndex + bulk + lastBulk : startIndex + bulk;
            setList.add(nodeKeys.subList(startIndex, endIndex));
        }
        return setList;
    }

    /**
     * Creates a {@link Callable} generating edges. It creates given number of edges ({@code numberOfEdges}) which
     * ids start from {@code edgeIdFrom}. Set of possible ids for starting and ending nodes
     * is determined by {@code nodeIds}.
     * @param nodeIds Node ids to be chosen from when creating sources and targets of the new edges
     * @param edgeIdFrom First edge id that will be set to a new edge and incremented further on
     * @param numberOfEdges Number of edges to be generated
     * @return {@link Callable} generating edges
     */
    private Callable<Map<Long, Edge>> generateEdgesCallable(List<Long> nodeIds, Long edgeIdFrom, long numberOfEdges) {
        return new Callable<Map<Long, Edge>>() {
            @Override
            public Map<Long, Edge> call() {
                return generateEdges(nodeIds, null, edgeIdFrom, numberOfEdges);
            }
        };
    }

    /**
     * Creates a {@link Callable} generating edges. It creates given number of edges ({@code numberOfEdges}) which
     * ids start from {@code edgeIdFrom}. Set of possible ids for starting nodes is given in {@code startNodeIds}
     * and possible ids for ending nodes are determined by {@code endNodeIds}.
     * @param startNodeIds Possible values of ids for starting nodes
     * @param endNodeIds Possible values of ids for ending nodes
     * @param edgeIdFrom First edge id that will be set to a new edge and incremented further on
     * @param numberOfEdges Number of edges to be generated
     * @return {@link Callable} generating edges
     */
    private Callable<Map<Long, Edge>> generateEdgesCallable(List<Long> startNodeIds, List<Long> endNodeIds, Long edgeIdFrom, long numberOfEdges) {
        return new Callable<Map<Long, Edge>>() {
            @Override
            public Map<Long, Edge> call() {
                return generateEdges(startNodeIds, endNodeIds, edgeIdFrom, numberOfEdges);
            }
        };
    }

    /**
     * It generates given number of edges ({@code numberOfEdges}) which
     * ids start from {@code edgeIdFrom}. Set of possible ids for starting nodes is given in {@code startNodeIds}
     * and possible ids for ending nodes are determined by {@code endNodeIds}. If the {@code endNodeIds} is null,
     * the {@code startNodeIds} is taken as a pool for an end id.
     * @param startNodeIds Possible values of ids for starting nodes
     * @param endNodeIds Possible values of ids for ending nodes
     * @param edgeIdFrom First edge id that will be set to a new edge and incremented further on
     * @param numberOfEdges Number of edges to be generated
     * @return {@link Map} of generated edges, key representing an edge id, value representing the edge itself
     */
    private Map<Long, Edge> generateEdges(List<Long> startNodeIds, List<Long> endNodeIds, Long edgeIdFrom, Long numberOfEdges) {
        Map<Long, Edge> bulkEdges = new HashMap<>();
        edges.forEach(bulkEdges::putIfAbsent);

        for (long i = edgeIdFrom; i < edgeIdFrom + numberOfEdges; i++) {
            Long startId = getRandomItem(startNodeIds);
            Long endId;
            if (endNodeIds == null) {
                endId = getNewRandomEdgeEnd(startNodeIds, startId, bulkEdges);
            } else {
                endId = getNewRandomEdgeEnd(endNodeIds, startId, bulkEdges);
            }
            storeEdge(bulkEdges, i, UUID.randomUUID().toString(), startId, endId);
            if ((i - edgeIdFrom + 1) % LOG_EACH == 0) {
                LOGGER.info("Thread " + Thread.currentThread().getName() +
                        " generated edges with edgeIdFrom: " + edgeIdFrom + " and numberOfEdges: " + numberOfEdges +
                        ". " + (i - edgeIdFrom));
            }
        }
        return bulkEdges;
    }

    /**
     * Gets a random item from a given {@link List} of values.
     * @param list {@link List} of values to be chosen from.
     * @return Random item from a given {@link List} of values.
     */
    private Long getRandomItem(List<Long> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    /**
     * Finds an id of a vertex which is not yet a an ending vertex of an edge starting in a vertex with a @startId.
     * Warning: the more existing edges the higher the probability that the process of finding a not connected vertex
     * will take a long time. Therefore, there is a maximum of 10 tries for finding a not yet existing edge.
     * If such is not found after 10 attempts, an existing one is returned.
     * @param startId id of a vertex
     * @return an id of a vertex not yet connected with the vertex of a @startId in the direction v(startId) -> v(endId).
     */
    private Long getNewRandomEdgeEnd(List<Long> nodeIds, Long startId, Map<Long, Edge> edges) {
        Long endId = getRandomItem(nodeIds);
        int maxTries = 10;
        while ((endId.equals(startId) || edgeExists(startId, endId, edges)) && maxTries > 0) {
            endId = getRandomItem(nodeIds);
            maxTries--;
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
    private boolean edgeExists(Long startId, Long endId, Map<Long, Edge> edges) {
        for (Edge e : edges.values()) {
            if (e.getStartNode().equals(startId) && e.getEndNode().equals(endId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates edge attributes for each edge.
     */
    private void generateEdgeAttributes() {
        for (Long key : edges.keySet()) {
            storeEdgeAttribute(edgeAttributes, key, EdgeAttribute.ATTRIBUTE_NAME, UUID.randomUUID().toString());
        }
    }

    /**
     * Stores information about an edge attribute to a provided {@link Map}.
     * @param map {@link Map} containing edge attributes as values, keyed with their id
     * @param edgeKey key of the edge to which the attribute belongs
     * @param attributeKey key of the edge attribute
     * @param attributeValue value of the edge attribute
     */
    private void storeEdgeAttribute(Map<Long, EdgeAttribute> map, Long edgeKey, String attributeKey, String attributeValue) {
        EdgeAttribute edgeAttribute = new EdgeAttribute();

        edgeAttribute.setId(edgeKey);
        edgeAttribute.setKey(attributeKey);
        edgeAttribute.setValue(attributeValue);

        map.put(edgeKey, edgeAttribute);
    }

    /**
     * Stores information about a parent node to a provided {@link Map}.
     * @param map {@link Map} containing edge attributes as values, keyed with their id
     * @param keys {@link List} storing ids of all nodes
     * @param id Id of the new parent
     * @param name Name of the new parent
     */
    private void storeParentNode(Map<Long, Node> map, List<Long> keys, Long id, String name) {
        storeNode(map, keys, id, name, null);
    }

    /**
     * Stores information about a node to a provided {@link Map}.
     * @param map {@link Map} containing nodes as values, keyed with their id
     * @param keys {@link List} storing ids of all nodes
     * @param id Id of the new node
     * @param name Name of the new node
     * @param parentId Id of a new parent
     */
    private void storeNode(Map<Long, Node> map, List<Long> keys, Long id, String name, Long parentId) {
        Node node = new Node();

        node.setId(id);
        node.setName(name);
        node.setParent(parentId);

        map.put(id, node);
        keys.add(id);
    }

    /**
     * Stores information about an edge to a provided {@link Map}.
     * @param map {@link Map} containing edges as values, keyed with their id
     * @param id Id of the new edge
     * @param name Name of the new node
     * @param startId Id of a starting (source) node of the edge
     * @param endId Id of an ending node (target) of the edge
     */
    private void storeEdge(Map<Long, Edge> map, Long id, String name, Long startId, Long endId) {
        Edge edge = new Edge();

        edge.setId(id);
        edge.setName(name);
        edge.setStartNode(startId);
        edge.setEndNode(endId);

        map.put(id, edge);
    }

    /**
     * Creates a {@link CSVWriter} of a file specified by the {@code path} parameter.
     * @param path Path to a file for which the {@link CSVWriter} will be created
     * @return {@link CSVWriter} of a file specified by the {@code path} parameter.
     * @throws IOException When there's an issue with reading/creating/writing a file.
     */
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
