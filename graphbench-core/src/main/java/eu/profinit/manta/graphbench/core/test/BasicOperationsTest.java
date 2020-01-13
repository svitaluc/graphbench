package eu.profinit.manta.graphbench.core.test;

import eu.profinit.manta.graphbench.core.config.GraphDBConfiguration;
import eu.profinit.manta.graphbench.core.csv.ProcessCSV;
import eu.profinit.manta.graphbench.core.dataset.Dataset;
import eu.profinit.manta.graphbench.core.db.IGraphDBConnector;
import eu.profinit.manta.graphbench.core.db.Translator;
import eu.profinit.manta.graphbench.core.access.IEdge;
import eu.profinit.manta.graphbench.core.access.IVertex;
import eu.profinit.manta.graphbench.core.access.direction.Direction;
import eu.profinit.manta.graphbench.core.access.iterator.IEdgeIterator;
import eu.profinit.manta.graphbench.core.access.iterator.IVertexIterator;

import java.util.*;

public class BasicOperationsTest implements ITest {
    private final static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ITest.class);

    private Dataset dataset;
    private Map<String, Long> results;
    private GraphDBConfiguration configuration;

    public BasicOperationsTest() {
    }

    public BasicOperationsTest(Dataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public void test(ProcessCSV csv, IGraphDBConnector db) {
        configuration = db.getGraphDBConfiguration();

        results = new HashMap<String, Long>();

        // GET VERTICES TEST
        LOGGER.info("BasicOperations: getVertices: The test begins.");
        long testStart = System.currentTimeMillis();
        Set getVerticesResult = getVertices(csv.getTranslator(), db, 123456);
        LOGGER.info("BasicOperations: getVertices: Test end.");
        long testEnd = System.currentTimeMillis();
        LOGGER.info("BasicOperations: getVertices: Found " + getVerticesResult.size() + " vertices.");
        LOGGER.info("BasicOperations: getVertices: Test time: " + (testEnd - testStart));
        results.put("getVertices", testEnd-testStart);


        // GET VERTICES WITH NEIGHBORS TEST
        LOGGER.info("BasicOperations: getVerticesWithNeighbors: The test begins.");
        testStart = System.currentTimeMillis();
        Map getVerticesWithNeighborsResult = getVerticesWithNeighbors(csv.getTranslator(), db, 789123);
        LOGGER.info("BasicOperations: getVerticesWithNeighbors: Test end.");
        testEnd = System.currentTimeMillis();
        LOGGER.info("BasicOperations: getVerticesWithNeighbors: Found " + getVerticesWithNeighborsResult.keySet().size() + " vertices with some neighbors.");
        LOGGER.info("BasicOperations: getVerticesWithNeighbors: Test time: " + (testEnd - testStart));
        results.put("getVerticesWithNeighbours", testEnd-testStart);


        // GET VERTICES WITH EDGES TEST
        LOGGER.info("BasicOperations: getVerticesWithEdges: The test begins.");
        testStart = System.currentTimeMillis();
        Map getVerticesWithEdgesResult = getVerticesWithEdges(csv.getTranslator(), db, 456789);
        LOGGER.info("BasicOperations: getVerticesWithEdges: Test end.");
        testEnd = System.currentTimeMillis();
        LOGGER.info("BasicOperations: getVerticesWithEdges: Found " + getVerticesWithEdgesResult.keySet().size() + " vertices with some edges.");
        LOGGER.info("BasicOperations: getVerticesWithEdges: Test time: " + (testEnd - testStart));
        results.put("getVerticesWithEdges", testEnd-testStart);

        //-------------------------------------------------


        // GET VERTICES WITH EDGES TEST
        LOGGER.info("BasicOperations: getVerticesWithEdges: The test begins.");
        testStart = System.currentTimeMillis();
        Map getVerticesWithEdgesResult2 = getVerticesWithEdges(csv.getTranslator(), db, 456789);
        LOGGER.info("BasicOperations: getVerticesWithEdges: Test end.");
        testEnd = System.currentTimeMillis();
        LOGGER.info("BasicOperations: getVerticesWithEdges: Found " + getVerticesWithEdgesResult2.keySet().size() + " vertices with some edges.");
        LOGGER.info("BasicOperations: getVerticesWithEdges: Test time: " + (testEnd - testStart));
        results.put("getVerticesWithEdges2", testEnd-testStart);

        // GET VERTICES WITH NEIGHBORS TEST
        LOGGER.info("BasicOperations: getVerticesWithNeighbors: The test begins.");
        testStart = System.currentTimeMillis();
        Map getVerticesWithNeighborsResult2 = getVerticesWithNeighbors(csv.getTranslator(), db, 789123);
        LOGGER.info("BasicOperations: getVerticesWithNeighbors: Test end.");
        testEnd = System.currentTimeMillis();
        LOGGER.info("BasicOperations: getVerticesWithNeighbors: Found " + getVerticesWithNeighborsResult2.keySet().size() + " vertices with some neighbors.");
        LOGGER.info("BasicOperations: getVerticesWithNeighbors: Test time: " + (testEnd - testStart));
        results.put("getVerticesWithNeighbours2", testEnd-testStart);
    }

    @Override
    public Map<String, Long> getRestults() {
        return results;
    }

    @Override
    public Dataset getDataset()
    {
        return dataset;
    }

    @Override
    public GraphDBConfiguration getGraphDBConfiguration() {
        return configuration;
    }

    private Set<IVertex> getVertices(Translator translator, IGraphDBConnector db, Integer seed) {
        Collection<String> idSet = dataset.getVerticesIds(translator, seed);
        LOGGER.info("idSet size: " + idSet.size());
        Set<IVertex> result = new HashSet<>();

        idSet.forEach(id ->
            result.add(db.getVertex(translator.get(id)))
        );

        return result;
    }

    private Map<IVertex, Set<IEdge>> getVerticesWithEdges(Translator translator, IGraphDBConnector db, Integer seed) {
        Collection<String> idSet = dataset.getVerticesIds(translator, seed);
        Map<IVertex, Set<IEdge>> result = new HashMap<>();

        List<String> idList = new ArrayList<>(idSet);

        int c = 0;
        for(int i = 0; i < idSet.size(); i++) {
            IVertex vertex = db.getVertex(translator.get(idList.get(i)));
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

    private Map<IVertex, Set<IVertex>> getVerticesWithNeighbors(Translator translator, IGraphDBConnector db, Integer seed) {
        Collection<String> idSet = dataset.getVerticesIds(translator, seed);
        Map<IVertex, Set<IVertex>> result = new HashMap<>();
        List<String> idList = new ArrayList<>(idSet);

        int c = 0;
        for (int i = 0; i < idSet.size(); i++) {
            IVertex vertex = db.getVertex(translator.get(idList.get(i)));
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
