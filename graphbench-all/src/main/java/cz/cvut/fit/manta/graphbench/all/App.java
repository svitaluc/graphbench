package cz.cvut.fit.manta.graphbench.all;

import cz.cvut.fit.manta.graphbench.core.config.ConfigProperties;
import cz.cvut.fit.manta.graphbench.core.csv.CSVOutput;
import cz.cvut.fit.manta.graphbench.core.db.product.GraphDBType;
import cz.cvut.fit.manta.graphbench.core.config.model.ConfigProperty;
import cz.cvut.fit.manta.graphbench.core.csv.CSVType;
import cz.cvut.fit.manta.graphbench.core.csv.ProcessCSV;
import cz.cvut.fit.manta.graphbench.core.dataset.Dataset;
import cz.cvut.fit.manta.graphbench.core.dataset.DatasetImpl;
import cz.cvut.fit.manta.graphbench.core.db.GraphDBCommonImpl;
import cz.cvut.fit.manta.graphbench.core.db.GraphDBConnector;
import org.apache.log4j.Logger;
import cz.cvut.fit.manta.graphbench.core.test.Test;
import cz.cvut.fit.manta.graphbench.core.test.TestFactory;

import java.io.IOException;

/**
 * The main class running the application.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class App {
	/** A String flag to denote the end of a benchmark. In some cases (f.e. JanusGraph + Cassandra), the
	 * process running a cassandra instance does not stop. Therefore the end of a test is further labeled. */
	public final static String BENCHMARK_FINISHED = "BENCHMARK TEST FINISHED";
	/** Logger. */
	private final static Logger LOG = Logger.getLogger(App.class);
	/** User configuration set in the config.properties file. */
	private final ConfigProperties CONFIG = ConfigProperties.getInstance();
	/** Database connector. */
	private GraphDBConnector db;

	/**
	 * The main method run when the jar filed is executed.
	 * In order to run your own database implementation (implementation of the interface {@link GraphDBConnector})
	 * without adjusting this project, call the below start method.
	 * @param args - main arguments (not used)
	 */
	public static void main(String[] args) {
		App app = new App();
		app.start();
	}

	/**
	 * Start method with all configurations set in config file.
	 */
	private void start() {
		connectDB();
		startBody();
	}

	/**
	 * Start method that can be called with a custom {@link GraphDBConnector} implementation.
	 * @param graphDB an implementation of the {@link GraphDBConnector} interface.
	 */
	public void start(GraphDBConnector graphDB) {
		connectDB(graphDB);
		startBody();
	}

	/**
	 * Loads the main configuration properties and runs a test.
	 */
	private void startBody() {
		try{
			Dataset dataset = new DatasetImpl(CONFIG.getStringProperty(ConfigProperty.DATASET_DIR));
			Test test = TestFactory.createTest(CONFIG.getTestTypeProperty(ConfigProperty.TEST_TYPE), dataset);

			runBenchmarkTest(dataset, test, db);

			writeTestResults(test);

			finishDB();
			LOG.info(BENCHMARK_FINISHED);

		} catch (Exception e) {
			LOG.error("Error when starting the test!", e);
			if (db.isConnected()) {
				db.rollback();
			}
		}
	}

	/**
	 * Writes results of a test to a csv file.
	 * @param test {@link Test} implementation providing results of its run.
	 */
	private void writeTestResults(Test test) {
		CSVOutput csvOut = new CSVOutput(CONFIG.getStringProperty(ConfigProperty.CSV_OUTPUT_DIRECTORY));
		try {
			csvOut.writeTestToCSV(test, db.getGraphDBConfiguration());
		}
		catch (IOException e) {
			LOG.error("Problem with writing the test results.", e);
		}
	}

	/**
	 * Connects a database set in the config file.
	 */
	private void connectDB() {
		String databasePath;
		try {
			databasePath = CONFIG.getPathProperty(ConfigProperty.DATABASE_DIR);
		} catch (IOException e) {
			LOG.error("Database directory " + CONFIG.getStringProperty(ConfigProperty.DATABASE_DIR)
					+ "can't be converted to absolute path", e);
			return;
		}

		GraphDBType databaseType = CONFIG.getGraphDBTypeProperty(ConfigProperty.DATABASE_TYPE);
		db = new GraphDBCommonImpl(GraphDBFactory.getGraphDB(databaseType));

		db.connect(databasePath, null, null);
	}

	/**
	 * Connects a database set by the connector parameter.
	 * @param connector a custom database connector
	 */
	private void connectDB(GraphDBConnector connector) {
		String databasePath;
		try {
			databasePath = CONFIG.getPathProperty(ConfigProperty.DATABASE_DIR);
		} catch (IOException e) {
			LOG.error("Database directory " + CONFIG.getStringProperty(ConfigProperty.DATABASE_DIR)
					+ "can't be converted to absolute path", e);
			return;
		}

		db = connector;
		db.connect(databasePath, null, null);
	}

	/**
	 * Finalizes database operations with final commit.
	 */
	private void finishDB() {
		LOG.info("... final commit ...");
		db.commit();

		if (db.isConnected()) {
			db.disconnect();
		}
	}

	/**
	 * Loads the data and runs a test over them.
	 * @param dataset dataset to be tested
	 * @param test particular test
	 * @param db database connector
	 */
	private void runBenchmarkTest(Dataset dataset, Test test, GraphDBConnector db) {

		if (ConfigProperties.getInstance().getBooleanProperty(ConfigProperty.WITH_IMPORT)) {
			ProcessCSV csv = loadCSV(dataset.getDatasetDir(), db);
			try {
				test.test(csv, db);
			} catch (Throwable e) {
				LOG.error("Unexpected exception!", e);
				writeTestResults(test);
			}
		} else {
			throw new UnsupportedOperationException("'WITH_IMPORT = false' not yet implemented.");
		}
	}

	/**
	 * Loads all the data into the database.
	 * @param csvDir directory of the csv files
	 * @param db database connector
	 * @return
	 */
	private static ProcessCSV loadCSV(String csvDir, GraphDBConnector db) {
		ProcessCSV csv = new ProcessCSV(db);
		csv.addSuperRoot();

		LOG.info("Loading NODE...");
		csv.loadCSV(csvDir, CSVType.NODE);
		LOG.info("Loading EDGE...");
		csv.loadCSV(csvDir, CSVType.EDGE);
		LOG.info("Loading EDGE_ATTR...");
		csv.loadCSV(csvDir, CSVType.EDGE_ATTR);

		return csv;
	}
}

