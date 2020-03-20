package cz.cvut.fit.manta.graphbench.core.csv;

import au.com.bytecode.opencsv.CSVWriter;
import cz.cvut.fit.manta.graphbench.core.config.ConfigProperties;
import cz.cvut.fit.manta.graphbench.core.config.GraphDBConfiguration;
import cz.cvut.fit.manta.graphbench.core.config.model.ConfigProperty;
import cz.cvut.fit.manta.graphbench.core.dataset.Dataset;
import cz.cvut.fit.manta.graphbench.core.test.Test;
import cz.cvut.fit.manta.graphbench.core.test.TestResult;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class for writing results of a benchmark into a csv file.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class CSVOutput {
    /** Path to a directory into which the output csv file should be written. **/
    private String outputDirectoryPath;
    /** Maximum allowed size for a csv file. If a loaded file exceeds this limit,
     * a new file with a following postfix is created. **/
    private final static int SIZE_LIMIT = 1000000;
    /** Logger. **/
    private static final Logger LOGGER = Logger.getLogger(CSVOutput.class);
    /** Properties of the main configuration file. **/
    private final ConfigProperties CONFIG = ConfigProperties.getInstance();

    /**
     * Constructor for {@link CSVOutput}.
     * @param outputDirectoryPath Path to a directory into which the output csv file should be written
     */
    public CSVOutput(String outputDirectoryPath) {
        this.outputDirectoryPath = outputDirectoryPath;
    }

    /**
     * Writes all to results from a test to a CSV file.
     * @param test Test that was run
     * @param graphDBConfiguration Configuration of a graph database
     * @throws IOException When the results cannot be written to the file
     */
    public void writeTestToCSV(Test test, GraphDBConfiguration graphDBConfiguration) throws IOException {
        List<TestResult> results = test.getResults();
        for (TestResult result : results) {
            writeLineToCSV(test.getDataset(), result.getTestName(), result.getTestTime(), graphDBConfiguration);
        }
    }

    /**
     * Writes one line with a result of one test to a CSV file
     * @param dataset Dataset on which the test was run on
     * @param testName Name of the test
     * @param time Execution time of the test
     * @param conf Configuration of a database used for the test
     * @throws IOException When a writer can't be created
     */
    private void writeLineToCSV(Dataset dataset, String testName, long time, GraphDBConfiguration conf) throws IOException {
        ArrayList<String> line = new ArrayList<String>();
        CSVWriter writer;

        try {
            writer = createWriter(outputDirectoryPath);
        } catch (IOException e) {
            LOGGER.error("Couldn't create a writer.");
            throw new IOException(e);
        }

        line.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        line.add(CONFIG.getStringProperty(ConfigProperty.DATABASE_TYPE));
        line.add(conf.getDatabaseVersion());

        line.add(conf.getIndexBackend());
        line.add(conf.getIndexVersion());
        line.add(conf.getStorageBackend());

        if (dataset == null) {
            line.add("no dataset");
        } else {
            line.add(dataset.getDatasetDir());
        }

        line.add(CONFIG.getStringProperty(ConfigProperty.TEST_TYPE));
        line.add(testName);
        line.add(Long.toString(time));

        writer.writeNext(line.toArray(new String[line.size()]));

        writer.close();
    }

    /**
     * Creates a new CSVWriter with the smallest postfix possible without exceeding size limit
     * @param directoryPath path to the output directory
     * @return {@link CSVWriter} to write to an output csv file
     * @throws IOException When the file cannot be found
     */
    private CSVWriter createWriter(String directoryPath) throws IOException {
        File outputFile;

        int postfix = 1;
        do {
            String pathWithPostfix = directoryPath + File.separator + "graphbench-output" + "_" + postfix + ".csv";
            LOGGER.info("Output file path: " + pathWithPostfix);
            outputFile = new File(pathWithPostfix);
            outputFile.createNewFile();
            postfix++;
        } while (outputFile.length() > SIZE_LIMIT);

        FileOutputStream outputStream = new FileOutputStream(outputFile, true);
        OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
        return new CSVWriter(new BufferedWriter(streamWriter));
    }
}
