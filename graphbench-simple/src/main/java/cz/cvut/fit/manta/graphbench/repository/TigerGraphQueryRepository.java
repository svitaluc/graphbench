package cz.cvut.fit.manta.graphbench.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TigerGraph Import Repository
 *
 * @author dbucek
 */
public class TigerGraphQueryRepository implements QueryRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(TigerGraphQueryRepository.class);

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TigerGraphQueryRepository() {
    }

    @Override
    public void close() {
    }

// How to create in sequence:
// CREATE VERTEX person(PRIMARY_ID personId STRING, name STRING, year INT) WITH primary_id_as_attribute="true"
// CREATE DIRECTED EDGE friendship(FROM person, TO person, connect_day DATETIME)
// CREATE GRAPH social (person, friendship)

    @Override
    public Set<String> queryDirect(String[][] vertices) {
        return queryBatch("http://localhost:9000/query/simple_select/", vertices);
    }

    @Override
    public Set<String> queryTraverse(String[][] vertices) {
        return queryBatch("http://localhost:9000/query/select_with_finding/", vertices);
    }

    private Set<String> queryBatch(String url, String[][] vertices) {
        Set<String> output = new HashSet<>();

        for (int i = 0; i < vertices.length; i += 1000) {
            output.addAll(query(url, Arrays.copyOfRange(vertices, i, Math.min((i + 1000), vertices.length))));
        }

        return output;
    }

    private Set<String> query(String url, String[][] vertices) {
        // Create http post
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(createInputStreamEntity(vertices));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
            String result = bufferedReader.lines().collect(Collectors.joining("\n"));

            if (response.getStatusLine().getStatusCode() != 200) {
                LOGGER.error("Status code: " + response.getStatusLine().getStatusCode());
                LOGGER.error(result);

                throw new RuntimeException("Response is not 200");
            }
            LOGGER.trace(result);

            @SuppressWarnings("unchecked")
            Map<String, Object> resultsMap = objectMapper.readValue(result, Map.class);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> resultsList = (List<Map<String, Object>>) resultsMap.get("results");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> resultMap = (List<Map<String, Object>>) resultsList.get(0).get("Result");

            return resultMap.stream()
                    .map(a -> String.valueOf(a.get("v_id")))
                    .collect(Collectors.toSet());

        } catch (IOException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }

    private HttpEntity createInputStreamEntity(String[][] vertices) {
        StringBuilder builder = new StringBuilder();

        String separator = "";
        for (String[] line : vertices) {
            builder.append(separator);
            builder.append("personIds=");
            builder.append(line[0]);
            separator = "&";
        }

        return new InputStreamEntity(new ByteArrayInputStream(builder.toString().getBytes()));
    }
}
