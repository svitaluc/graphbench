package cz.cvut.fit.manta.graphbench.test.benchmark;

import cz.cvut.fit.manta.graphbench.core.access.Edge;
import cz.cvut.fit.manta.graphbench.core.access.Vertex;
import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.core.access.iterator.EdgeIterator;
import cz.cvut.fit.manta.graphbench.core.access.iterator.VertexIterator;
import cz.cvut.fit.manta.graphbench.core.csv.ProcessCSV;
import cz.cvut.fit.manta.graphbench.core.dataset.Dataset;
import cz.cvut.fit.manta.graphbench.core.db.GraphDBConnector;
import cz.cvut.fit.manta.graphbench.core.db.Translator;
import cz.cvut.fit.manta.graphbench.core.test.Test;
import cz.cvut.fit.manta.graphbench.core.test.TestResult;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Test for basic operations. It contains testing of
 *  <ul>
 *    <li> get all vertices </li>
 *    <li> get all vertices with their edges </li>
 *    <li> get all vertices with their neighbors </li>
 *  </ul>
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class BasicOperationsTest implements Test {
    /** Logger. */
    private final static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(Test.class);

    /** DatasetImpl containing information about all elements. */
    private Dataset dataset;
    /** Test results */
    private List<TestResult> results;

    /**
     * Constructor of the {@link BasicOperationsTest}.
     */
    public BasicOperationsTest() {
    }

    /**
     * Constructor of the {@link BasicOperationsTest}.
     * @param dataset Dataset containing information about all elements
     */
    public BasicOperationsTest(Dataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public void test(ProcessCSV csv, GraphDBConnector<?,?> db) {
        results = new ArrayList<>();
        results.add(new TestResult(System.currentTimeMillis(), "import", csv.getImportTime()));
        final Translator translator = csv.getTranslator();

        // GET VERTICES TEST
        runSubTest("getVertices", "vertices",
                () -> getVertices(translator, db));

        // GET VERTICES WITH EDGES TEST
        runSubTest("getVerticesWithEdges", "vertices with some edges",
                () -> getVerticesWithEdges(translator, db));

        // GET VERTICES WITH NEIGHBORS TEST
        runSubTest("getVerticesWithNeighbors", "vertices with some neighbors",
                () -> getVerticesWithNeighbors(translator, db));

        //-------------------------------------------------

        // GET VERTICES WITH EDGES TEST
        runSubTest("getVerticesWithEdges", "vertices with some edges",
                () -> getVerticesWithEdges(translator, db));

        // GET VERTICES WITH NEIGHBORS TEST
        runSubTest("getVerticesWithNeighbors", "vertices with some neighbors",
                () -> getVerticesWithNeighbors(translator, db));
    }

    /**
     * Runs a particular test with measuring and logging.
     * @param testName Name of the test
     * @param elementsFoundName Name of elements that are found with the test
     * @param subtest {@link Callable} of a test method to be run
     */
    private void runSubTest(String testName, String elementsFoundName, Callable<Object> subtest) {
        LOGGER.info("BasicOperations: " + testName + ": The test begins.");
        long testStart = System.currentTimeMillis();
        Object result;

        try {
            result = subtest.call();
        } catch (Exception e) {
            LOGGER.error("Can't call the subtest method " + testName, e);
            return;
        }

        LOGGER.info("BasicOperations: " + testName + ": Test end.");
        long testEnd = System.currentTimeMillis();


        int elementsFound;
        if (result instanceof Map) {
            elementsFound = ((Map<?,?>) result).keySet().size();
        } else {
            // result instanceof Set is always true
            elementsFound = ((Set<?>) result).size();
        }

        LOGGER.info("BasicOperations: " + testName + ": Found " + elementsFound + " " + elementsFoundName + ".");
        LOGGER.info("BasicOperations: " + testName + ": Test time: " + (testEnd - testStart));
        results.add(new TestResult(System.currentTimeMillis(), testName, testEnd - testStart));
    }

    @Override
    public List<TestResult> getResults() {
        return results;
    }

    @Override
    public Dataset getDataset()
    {
        return dataset;
    }

    /**
     * Test that gets all vertices of the graph. This test can be useful when checking behavior
     * of different configuration and index backends.
     * @param translator Translator containing mappings of element ids in the dataset to ids in the database
     * @param db Connector to a graph database
     * @return {@link Set} of vertices
     */
    private Set<Vertex<?,?>> getVertices(Translator translator, GraphDBConnector<?,?> db) {
        Collection<String> idSet = dataset.getVerticesIds(translator);
        LOGGER.info("idSet size: " + idSet.size());
        Set<Vertex<?,?>> result = new HashSet<>();

        idSet.forEach(id ->
            result.add(db.getVertex(translator.getNode(id)))
        );

        return result;
    }

    /**
     * Test that gets all vertices of the graph together with their edges. This test can be useful when
     * checking behavior of different configuration and index backends.
     * @param translator Translator containing mappings of element ids in the dataset to ids in the database
     * @param db Connector to a graph database
     * @return {@link Map} containing vertex as a key and a {@link Set} of its edges as its value
     */
    private Map<Vertex<?,?>, Set<Edge<?,?>>> getVerticesWithEdges(Translator translator, GraphDBConnector<?,?> db) {
        Collection<String> idSet = dataset.getVerticesIds(translator);
        Map<Vertex<?,?>, Set<Edge<?,?>>> result = new HashMap<>();

        List<String> idList = new ArrayList<>(idSet);

        int c = 0;
        for(int i = 0; i < idSet.size(); i++) {
            Vertex<?,?> vertex = db.getVertex(translator.getNode(idList.get(i)));
            EdgeIterator<?,?> edgeIterator = vertex.edges(Direction.BOTH);
            Set<Edge<?,?>> edgeSet = new HashSet<>();
            if (edgeIterator != null) {
                while (edgeIterator.hasNext()) {
                    edgeSet.add(edgeIterator.next());
                }
            } else {
                c++;
            }
            result.put(vertex, edgeSet);
        }
        LOGGER.info("Vertex null in " + c + " cases.");

        return result;
    }

    /**
     * Test that gets all vertices of the graph together with their neighbors. This test can be useful when
     * checking behavior of different configuration and index backends.
     * @param translator Translator containing mappings of element ids in the dataset to ids in the database
     * @param db Connector to a graph database
     * @return {@link Map} containing vertex as a key and a {@link Set} of its neighbors (vertices) as its value
     */
    private Map<Vertex<?,?>, Set<Vertex<?,?>>> getVerticesWithNeighbors(Translator translator, GraphDBConnector<?,?> db) {
        Collection<String> idSet = dataset.getVerticesIds(translator);
        Map<Vertex<?,?>, Set<Vertex<?,?>>> result = new HashMap<>();
        List<String> idList = new ArrayList<>(idSet);

        int c = 0;
        for (int i = 0; i < idSet.size(); i++) {
            Vertex<?,?> vertex = db.getVertex(translator.getNode(idList.get(i)));
            VertexIterator<?,?> vertexIterator = vertex.vertices(Direction.BOTH);
            Set<Vertex<?,?>> vertexSet = new HashSet<>();
            if (vertexIterator != null) {
                while (vertexIterator.hasNext()) {
                    vertexSet.add(vertexIterator.next());
                }
            } else {
                c++;
            }
            result.put(vertex, vertexSet);
        }
        LOGGER.info("Vertex null in " + c + " cases.");

        return result;
    }
}
