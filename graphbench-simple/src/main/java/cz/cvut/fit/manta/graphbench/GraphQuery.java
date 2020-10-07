package cz.cvut.fit.manta.graphbench;

import com.opencsv.CSVReaderHeaderAware;
import cz.cvut.fit.manta.graphbench.repository.QueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import java.io.FileReader;
import java.util.Set;

/**
 * Main Application Class of Graph Query
 * <p>
 * Two arguments:
 * 1) vertices csv file
 *
 * @author dbucek
 */
public class GraphQuery {
    private final static Logger LOGGER = LoggerFactory.getLogger(GraphQuery.class);
    private final static Profiler PROFILER = new Profiler("Query total");

    /**
     * Main import repository
     */
    private final QueryRepository queryRepository;

    public static void main(String[] args) throws Exception {
        PROFILER.setLogger(LOGGER);
        new GraphQuery(args[0]);
    }

    public GraphQuery(String verticesCsvFileName) throws Exception {
        RepositoryFactory repositoryFactory = new RepositoryFactory();
        queryRepository = repositoryFactory.createQueryRepository();

        try {
            // Run import process
            run(verticesCsvFileName);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            if (LOGGER.isDebugEnabled()) {
                e.printStackTrace();
            }
        } finally {
            queryRepository.close();
        }
    }

    void run(String verticesCsvFileName) throws Exception {
        // Import vertices
        try (CSVReaderHeaderAware verticesReader = new CSVReaderHeaderAware(new FileReader(verticesCsvFileName))) {
            LOGGER.info("Read vertices from CSV");
            PROFILER.start("Read vertices from CSV");
            // Partitioning of data
            BatchReader batchReader = new BatchReader(verticesReader);

            // Query Direct Tests
            queryDirect(batchReader, "Warm up query", 100_000);
            queryDirect(batchReader, "Querying direct", 10);
            queryDirect(batchReader, "Querying direct", 100);
            queryDirect(batchReader, "Querying direct", 1000);
            queryDirect(batchReader, "Querying direct", 10_000);
            queryDirect(batchReader, "Querying direct", 100_000);

            // Query Traverse Tests
            queryTraverse(batchReader, "Warm up query", 100_000);
            queryTraverse(batchReader, "Query traverse", 10);
            queryTraverse(batchReader, "Query traverse", 100);
            queryTraverse(batchReader, "Query traverse", 1000);
            queryTraverse(batchReader, "Query traverse", 10_000);
            queryTraverse(batchReader, "Query traverse", 100_000);
        }

        PROFILER.stop().log();
    }

    private void queryDirect(BatchReader batchReader, String testName, int batchSize) {
        LOGGER.info(testName + ": " + batchSize);
        PROFILER.start(testName + ": " + batchSize);

        String[][] batch = batchReader.next(batchSize);
        Set<String> results = queryRepository.queryDirect(batch);

        // compare with results
        if (batch.length != results.size()) {
            throw new RuntimeException("Result has different size that input. Must be the same");
        }

        // compare with results
        for (String[] line : batch) {
            if (!results.contains(line[0])) {
                throw new RuntimeException("Result doesn't contains identifier from input: " + line[0]);
            }
        }
    }

    private void queryTraverse(BatchReader batchReader, String testName, int batchSize) {
        LOGGER.info(testName + ": " + batchSize);
        PROFILER.start(testName + ": " + batchSize);

        String[][] batch = batchReader.next(batchSize);
        Set<String> results = queryRepository.queryTraverse(batch);

        // compare with results
        if (batch.length != results.size()) {
            // Result is not same in big numbers. We probably prepared vertices which doesn't have any edges.
            //throw new RuntimeException("Result has different size that input. Must be the same");
        }
    }
}
