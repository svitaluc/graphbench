package cz.cvut.fit.manta.graphbench.core.test.benchmark;

import cz.cvut.fit.manta.graphbench.core.db.IGraphDBConnector;
import cz.cvut.fit.manta.graphbench.core.db.Translator;
import cz.cvut.fit.manta.graphbench.core.csv.ProcessCSV;
import cz.cvut.fit.manta.graphbench.core.dataset.IDataset;
import cz.cvut.fit.manta.graphbench.core.access.IEdge;
import cz.cvut.fit.manta.graphbench.core.access.IVertex;
import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.core.access.iterator.IEdgeIterator;
import cz.cvut.fit.manta.graphbench.core.access.iterator.IVertexIterator;
import cz.cvut.fit.manta.graphbench.core.test.ITest;
import cz.cvut.fit.manta.graphbench.core.test.TestResult;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Test for basic operations. It contains testing of
 *  - get all vertices
 *  - get all vertices with their edges
 *  - get all vertices with their neighbors
 */
public class BasicOperationsTest implements ITest {
    private final static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ITest.class);

    /** Dataset containing information about all elements. */
    private IDataset dataset;
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
    public BasicOperationsTest(IDataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public void test(ProcessCSV csv, final IGraphDBConnector db) {
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
                () -> getVerticesWithNeighbors(translator, db, 789123));

        //-------------------------------------------------

        // GET VERTICES WITH EDGES TEST
        runSubTest("getVerticesWithEdges", "vertices with some edges",
                () -> getVerticesWithEdges(translator, db));

        // GET VERTICES WITH NEIGHBORS TEST
        runSubTest("getVerticesWithNeighbors", "vertices with some neighbors",
                () -> getVerticesWithNeighbors(translator, db, 789123));
    }

    /**
     * Runs a particular test with measuring and logging.
     * @param testName Name of the test
     * @param elementsFoundName Name of elements that are found with the test
     * @param subtest {@link Callable} of a test method to be run
     */
    private void runSubTest(String testName, String elementsFoundName, Callable subtest) {
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
            elementsFound = ((Map) result).keySet().size();
        } else {
            // result instanceof Set is always true
            elementsFound = ((Set) result).size();
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
    public IDataset getDataset()
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
    private Set<IVertex> getVertices(Translator translator, IGraphDBConnector db) {
        Collection<String> idSet = dataset.getVerticesIds(translator, 123456);
        LOGGER.info("idSet size: " + idSet.size());
        Set<IVertex> result = new HashSet<>();

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
    private Map<IVertex, Set<IEdge>> getVerticesWithEdges(Translator translator, IGraphDBConnector db) {
        Collection<String> idSet = dataset.getVerticesIds(translator, 456789);
        Map<IVertex, Set<IEdge>> result = new HashMap<>();

        List<String> idList = new ArrayList<>(idSet);

        int c = 0;
        for(int i = 0; i < idSet.size(); i++) {
            IVertex vertex = db.getVertex(translator.getNode(idList.get(i)));
            IEdgeIterator edgeIterator = vertex.edges(Direction.BOTH);
            Set<IEdge> edgeSet = new HashSet<>();
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
    private Map<IVertex, Set<IVertex>> getVerticesWithNeighbors(Translator translator, IGraphDBConnector db, Integer seed) {
        Collection<String> idSet = dataset.getVerticesIds(translator, seed);
        Map<IVertex, Set<IVertex>> result = new HashMap<>();
        List<String> idList = new ArrayList<>(idSet);

        int c = 0;
        for (int i = 0; i < idSet.size(); i++) {
            IVertex vertex = db.getVertex(translator.getNode(idList.get(i)));
            IVertexIterator vertexIterator = vertex.vertices(Direction.BOTH);
            Set<IVertex> vertexSet = new HashSet<>();
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
