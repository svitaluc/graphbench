package cz.cvut.fit.manta.graphbench;

import com.opencsv.CSVReaderHeaderAware;
import cz.cvut.fit.manta.graphbench.repository.ImportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import java.io.FileReader;

/**
 * Main Application Class of Graph Importer
 * <p>
 * Two arguments:
 * 1) vertices csv file
 * 2) edges csv file
 *
 * @author dbucek
 */
public class GraphImporter {
    private final static Logger LOGGER = LoggerFactory.getLogger(GraphImporter.class);
    private final static Profiler PROFILER = new Profiler("Import total");

    /**
     * Main import repository
     */
    private final ImportRepository importRepository;

    public static void main(String[] args) throws Exception {
        PROFILER.setLogger(LOGGER);
        new GraphImporter(args[0], args[1]);
    }

    public GraphImporter(String verticesCsvFileName, String edgesCsvFileName) throws Exception {
        RepositoryFactory repositoryFactory = new RepositoryFactory();
        importRepository = repositoryFactory.createImportRepository();

        try {
            // Init repo
            importRepository.init();

            // Run import process
            run(verticesCsvFileName, edgesCsvFileName);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            if (LOGGER.isDebugEnabled()) {
                e.printStackTrace();
            }
        } finally {
            importRepository.close();
        }
    }

    void run(String verticesCsvFileName, String edgesCsvFileName) throws Exception {
        // Import vertices
        try (CSVReaderHeaderAware verticesReader = new CSVReaderHeaderAware(new FileReader(verticesCsvFileName))) {
            int batchSize = importRepository.getDefaultVertexBatchSize();

            LOGGER.info("Storing vertices");
            PROFILER.start("Storing vertices");
            // Partitioning of data
            BatchReader batchReader = new BatchReader(batchSize, verticesReader);

            while (batchReader.hasNext()) {
                // Data block import start
                long start = System.nanoTime();

                String[][] partition = batchReader.next();
                importRepository.storeVertices(partition);

                // Data block import end
                long timeElapsed = System.nanoTime() - start;
                LOGGER.info("Total of " + batchReader.getReadDataSize() + " processed in time: " + ((double) timeElapsed / 1_000_000_000) + " seconds");

                //break;
            }

            LOGGER.info("Vertices stored");
        }

        // Import edges
        try (CSVReaderHeaderAware edgesReader = new CSVReaderHeaderAware(new FileReader(edgesCsvFileName))) {
            int batchSize = importRepository.getDefaultEdgeBatchSize();

            LOGGER.info("Storing edges");
            PROFILER.start("Storing edges");
            // Partitioning of data
            BatchReader batchReader = new BatchReader(batchSize, edgesReader);

            while (batchReader.hasNext()) {
                // Data block import start
                long start = System.nanoTime();

                String[][] partition = batchReader.next();
                importRepository.storeEdges(partition);

                // Data block import end
                long timeElapsed = System.nanoTime() - start;
                LOGGER.info("Total of " + batchReader.getReadDataSize() + " processed in time: " + ((double) timeElapsed / 1_000_000_000) + " seconds");
            }

            LOGGER.info("Edges stored");
        }

        PROFILER.stop().log();
    }
}
