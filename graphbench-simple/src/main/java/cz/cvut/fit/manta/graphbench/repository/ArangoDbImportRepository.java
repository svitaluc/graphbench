package cz.cvut.fit.manta.graphbench.repository;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoGraph;
import com.arangodb.entity.BaseEdgeDocument;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.entity.EdgeEntity;
import com.arangodb.entity.GraphEntity;
import com.arangodb.entity.StreamTransactionEntity;
import com.arangodb.entity.VertexEntity;
import com.arangodb.model.EdgeCreateOptions;
import com.arangodb.model.GraphCreateOptions;
import com.arangodb.model.StreamTransactionOptions;
import com.arangodb.model.VertexCreateOptions;
import cz.cvut.fit.manta.graphbench.model.IsFriend;
import cz.cvut.fit.manta.graphbench.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * ArangoDB Import Repository
 *
 * @author dbucek
 */
public class ArangoDbImportRepository implements ImportRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(ArangoDbImportRepository.class);

    private final static String ARANGODB_DATABASE = "import";
    private final static String ARANGODB_USERNAME = "root";
    private final static String ARANGODB_PASSWORD = "admin";

    private static final String GRAPH_NAME = "traversalGraph";
    private static final String EDGE_COLLECTION_NAME = "edges";
    private static final String VERTEX_COLLECTION_NAME = "vertices";

    private final ArangoDB arangoDB;
    private final ArangoGraph graph;

    public ArangoDbImportRepository() {
        // OPEN/CREATE DATABASE
        arangoDB = new ArangoDB.Builder()
                .user(ARANGODB_USERNAME)
                .password(ARANGODB_PASSWORD)
                .build();
        try {
            arangoDB.createDatabase(ARANGODB_DATABASE);
            System.out.println("Database created: " + ARANGODB_DATABASE);
        } catch (ArangoDBException e) {
            System.err.println("Failed to create database: " + ARANGODB_DATABASE + "; " + e.getMessage());
        }
        ArangoDatabase db = arangoDB.db(ARANGODB_DATABASE);
        graph = db.graph(GRAPH_NAME);
    }

    @Override
    public void init() {
//        ArangoVertexCollection vertexCollection = graph.vertexCollection(VERTEX_COLLECTION_NAME);
//        ArangoEdgeCollection edgeCollection = graph.edgeCollection(EDGE_COLLECTION_NAME);
//
//        if (db.collection(VERTEX_COLLECTION_NAME) == null) {
//            db.createCollection(VERTEX_COLLECTION_NAME);
//        }
//
//        if (db.collection(EDGE_COLLECTION_NAME) == null) {
//            db.createCollection(EDGE_COLLECTION_NAME);
//        }


        final Collection<EdgeDefinition> edgeDefinitions = new ArrayList<>();
        final EdgeDefinition edgeDefinition = new EdgeDefinition()
                .collection(EDGE_COLLECTION_NAME)
                .from(VERTEX_COLLECTION_NAME)
                .to(VERTEX_COLLECTION_NAME);

        edgeDefinitions.add(edgeDefinition);
        if (!graph.db().graph(GRAPH_NAME).exists()) {
            GraphEntity graphEntity = graph.db().createGraph(GRAPH_NAME, edgeDefinitions, new GraphCreateOptions());
        }
    }

    @Override
    public void storeVertices(String[][] vertices) {
        try {
            StreamTransactionEntity tx = graph.db().beginStreamTransaction(new StreamTransactionOptions().readCollections(VERTEX_COLLECTION_NAME).writeCollections(VERTEX_COLLECTION_NAME));
            for (String[] vertexArray : vertices) {
                createVertex(new Person(null, vertexArray[0], vertexArray[1], Integer.parseInt(vertexArray[2])), tx.getId());
            }

            graph.db().commitStreamTransaction(tx.getId());

        } catch (ArangoDBException e) {
            LOGGER.error("Arango Error: " + e.getErrorMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void storeEdges(String[][] edges) {
        StreamTransactionEntity tx = graph.db().beginStreamTransaction(new StreamTransactionOptions().readCollections(EDGE_COLLECTION_NAME).writeCollections(EDGE_COLLECTION_NAME));
        for (String[] vertexArray : edges) {
            try {
                createEdge(vertexArray, tx.getId());

//                LOGGER.info("Edge inserted: " + vertexArray[0] + "-->" + vertexArray[1]);
            } catch (ArangoDBException e) {
                LOGGER.error(e.getErrorMessage());

//                e.printStackTrace();
            }
        }

        graph.db().commitStreamTransaction(tx.getId());
    }

    @Override
    public void close() {
        arangoDB.shutdown();
    }

    @Override
    public int getDefaultVertexBatchSize() {
        return 500_000;
    }

    @Override
    public int getDefaultEdgeBatchSize() {
        return 10_000;
    }

    private void saveEdge(final IsFriend edge) throws ArangoDBException {
        graph.db().graph(GRAPH_NAME).edgeCollection(EDGE_COLLECTION_NAME).insertEdge(edge);
    }

    private VertexEntity createVertex(final Person vertex, final String streamTransactionId) throws ArangoDBException {
        return graph.db().graph(GRAPH_NAME).vertexCollection(VERTEX_COLLECTION_NAME).insertVertex(vertex, new VertexCreateOptions().streamTransactionId(streamTransactionId));
    }

    private EdgeEntity createEdge(final String[] edge, final String streamTransactionId) throws ArangoDBException {
        Person fromPerson = graph.db().query(String.format("FOR doc IN vertices\n" +
                "    FILTER doc.personId == \"%s\"\n" +
                "    RETURN doc", edge[0]), Person.class).iterator().next();
        Person toPerson = graph.db().query(String.format("FOR doc IN vertices\n" +
                "    FILTER doc.personId == \"%s\"\n" +
                "    RETURN doc", edge[1]), Person.class).iterator().next();
        BaseEdgeDocument document = new BaseEdgeDocument("vertices/" + fromPerson.getKey(), "vertices/" + toPerson.getKey());
        return graph.db().graph(GRAPH_NAME).edgeCollection(EDGE_COLLECTION_NAME).insertEdge(document, new EdgeCreateOptions().streamTransactionId(streamTransactionId));
    }
}
