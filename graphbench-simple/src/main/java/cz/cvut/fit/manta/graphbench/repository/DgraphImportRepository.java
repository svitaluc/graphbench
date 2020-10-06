package cz.cvut.fit.manta.graphbench.repository;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import cz.cvut.fit.manta.graphbench.model.Person;
import io.dgraph.DgraphClient;
import io.dgraph.DgraphGrpc;
import io.dgraph.DgraphProto;
import io.dgraph.Transaction;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dgraph Import Repository
 *
 * @author dbucek
 */
public class DgraphImportRepository implements ImportRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(DgraphImportRepository.class);

    private final DgraphClient dgraphClient;

    public DgraphImportRepository() {
        ManagedChannel channel1 = ManagedChannelBuilder
                .forAddress("localhost", 9080)
                .usePlaintext().build();
        DgraphGrpc.DgraphStub stub1 = DgraphGrpc.newStub(channel1);

        dgraphClient = new DgraphClient(stub1);
    }

    @Override
    public void init() {

    }

    @Override
    public void storeVertices(String[][] vertices) {
        // Create schema with indexes
        String schema = "name: string @index(exact) .";
        DgraphProto.Operation operation = DgraphProto.Operation.newBuilder()
                .setSchema(schema)
                .setRunInBackground(true)
                .build();
        dgraphClient.alter(operation);

        // Import vertices
        Person person;

        Transaction txn = dgraphClient.newTransaction();

        for (String[] vertexArray : vertices) {
            person = new Person(null, vertexArray[0], vertexArray[1], Integer.parseInt(vertexArray[2]));

            // Serialize it
            Gson gson = new Gson();
            String json = gson.toJson(person);

            // Run mutation
            DgraphProto.Mutation mu = DgraphProto.Mutation.newBuilder()
                    .setSetJson(ByteString.copyFromUtf8(json))
                    .build();
            txn.mutate(mu);
        }

        // Commit previous bunch
        txn.commit();
    }

    @Override
    public void storeEdges(String[][] edges) {
//        String query = "query {\n" +
//                "user as var(func: eq(email, \"wrong_email@dgraph.io\"))\n" +
//                "}\n";
//        DgraphProto.Mutation mu = DgraphProto.Mutation.newBuilder()
//                .setSetNquads(ByteString.copyFromUtf8("uid(user) <email> \"correct_email@dgraph.io\" ."))
//                .setCond("@if(eq(len(user), 1))")
//                .build();
//        DgraphProto.Request request = DgraphProto.Request.newBuilder()
//                .setQuery(query)
//                .addMutations(mu)
//                .setCommitNow(true)
//                .build();
//        txn.doRequest(request);
    }

    @Override
    public int getDefaultVertexBatchSize() {
        return 500_000;
    }

    @Override
    public int getDefaultEdgeBatchSize() {
        return 500_000;
    }

    @Override
    public void close() {
    }
}
