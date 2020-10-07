package cz.cvut.fit.manta.graphbench.repository;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * TigerGraph Import Repository
 *
 * @author dbucek
 */
public class TigerGraphImportRepository implements ImportRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(TigerGraphImportRepository.class);

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public TigerGraphImportRepository() {
    }

    @Override
    public void close() {
    }

    @Override
    public void init() {

    }

    @Override
    public void storeVertices(String[][] partition) {
        HttpEntity http;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JsonFactory jFactory = new JsonFactory();
            JsonGenerator jGenerator = jFactory.createGenerator(outputStream, JsonEncoding.UTF8);

            generateVertices(jGenerator, partition);
            outputStream.flush();

            http = new InputStreamEntity(new ByteArrayInputStream(outputStream.toByteArray()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Create http post
        HttpPost httpPost = new HttpPost("http://localhost:9000/graph/social");
        httpPost.setEntity(http);

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
            String result = bufferedReader.lines().collect(Collectors.joining("\n"));

            LOGGER.debug(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void storeEdges(String[][] partition) {
        HttpEntity http;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JsonFactory jFactory = new JsonFactory();
            JsonGenerator jGenerator = jFactory.createGenerator(outputStream, JsonEncoding.UTF8);

            generateEdges(jGenerator, partition);
            outputStream.flush();
            http = new InputStreamEntity(new ByteArrayInputStream(outputStream.toByteArray()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Create http post
        HttpPost httpPost = new HttpPost("http://localhost:9000/graph/social");
        httpPost.setEntity(http);

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
            String result = bufferedReader.lines().collect(Collectors.joining("\n"));

            LOGGER.debug(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getDefaultVertexBatchSize() {
        return 500_000;
    }

    @Override
    public int getDefaultEdgeBatchSize() {
        return 500_000;
    }

    //            "vertices": {
//                "<vertex_type>": {
//                    "<vertex_id>": {
//                        "<attribute>": {
//                            "value": <value>,
//                                    "op": <opcode>
//                        }
//                    }
//                }
//            },
    private void generateVertices(JsonGenerator jGenerator, String[][] vertices) throws IOException {
        // Generate JSON
        jGenerator.writeStartObject();
        // vertices
        jGenerator.writeFieldName("vertices");
        jGenerator.writeStartObject();
        // person
        jGenerator.writeFieldName("person");
        jGenerator.writeStartObject();

        for (String[] vertex : vertices) {
            // personId
            jGenerator.writeFieldName(vertex[0]);
            jGenerator.writeStartObject();

            // Attribute - name
            jGenerator.writeFieldName("name");
            jGenerator.writeStartObject();
            jGenerator.writeFieldName("value");
            jGenerator.writeString(vertex[1]);
            jGenerator.writeEndObject();

            // Attribute - year
            jGenerator.writeFieldName("year");
            jGenerator.writeStartObject();
            jGenerator.writeFieldName("value");
            jGenerator.writeNumber(vertex[2]);
            jGenerator.writeEndObject();

            // End personId
            jGenerator.writeEndObject();
        }

        // End person
        jGenerator.writeEndObject();
        // End vertices
        jGenerator.writeEndObject();
        // End JSON
        jGenerator.writeEndObject();

        // Close
        jGenerator.close();
    }

//            "edges": {
//                "<source_vertex_type>": {
//                    "<source_vertex_id>": {
//                        "<edge_type>": {
//                            "<target_vertex_type>": {
//                                "<target_vertex_id>": {
//                                    "<attribute>": {
//                                        "value": <value>,
//                                                "op": <opcode>
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }

    private void generateEdges(JsonGenerator jGenerator, String[][] edges) throws IOException {
        // Generate JSON
        jGenerator.writeStartObject();
        // vertices
        jGenerator.writeFieldName("edges");
        jGenerator.writeStartObject();
        // person
        jGenerator.writeFieldName("person");
        jGenerator.writeStartObject();

        for (String[] edge : edges) {
            // source vertex
            jGenerator.writeFieldName(edge[0]);
            jGenerator.writeStartObject();

            // edge type
            jGenerator.writeFieldName("friendship");
            jGenerator.writeStartObject();

            // target vertex type
            jGenerator.writeFieldName("person");
            jGenerator.writeStartObject();

            // target vertex
            jGenerator.writeFieldName(edge[1]);
            jGenerator.writeStartObject();

            // end target
            jGenerator.writeEndObject();
            // end target type
            jGenerator.writeEndObject();
            // end edge type
            jGenerator.writeEndObject();
            // end source vertex
            jGenerator.writeEndObject();
        }

        // End person
        jGenerator.writeEndObject();
        // End vertices
        jGenerator.writeEndObject();
        // End JSON
        jGenerator.writeEndObject();

        // Close
        jGenerator.close();
    }
}
