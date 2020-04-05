package cz.cvut.fit.manta.graphbench.all;

import cz.cvut.fit.manta.graphbench.core.access.Edge;
import cz.cvut.fit.manta.graphbench.core.access.Vertex;
import cz.cvut.fit.manta.graphbench.core.config.ConfigProperties;
import cz.cvut.fit.manta.graphbench.core.csv.CSVOutput;
import cz.cvut.fit.manta.graphbench.core.config.model.ConfigProperty;
import cz.cvut.fit.manta.graphbench.core.csv.CSVType;
import cz.cvut.fit.manta.graphbench.core.csv.ProcessCSV;
import cz.cvut.fit.manta.graphbench.core.dataset.Dataset;
import cz.cvut.fit.manta.graphbench.core.dataset.DatasetImpl;
import cz.cvut.fit.manta.graphbench.core.db.GraphDBCommonImpl;
import cz.cvut.fit.manta.graphbench.core.db.GraphDBConnector;
import cz.cvut.fit.manta.graphbench.core.test.Test;
import cz.cvut.fit.manta.graphbench.db.GraphDBFactory;
import cz.cvut.fit.manta.graphbench.db.GraphDBType;
import org.apache.log4j.Logger;
import cz.cvut.fit.manta.graphbench.test.TestFactory;

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
	private final ConfigProperties config = ConfigProperties.getInstance();
	/** Database connector. */
	private GraphDBConnector<Vertex<?,?>, Edge<?,?>> db;

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
	public void start(GraphDBConnector<Vertex<?,?>, Edge<?,?>> graphDB) {
		connectDB(graphDB);
		startBody();
	}

	/**
	 * Loads the main configuration properties and runs a test.
	 */
	private void startBody() {
		try{
			Dataset dataset = new DatasetImpl(config.getStringProperty(ConfigProperty.DATASET_DIR));

			Test test = TestFactory.createTest(config.getStringProperty(ConfigProperty.TEST_TYPE), dataset);

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
		CSVOutput csvOut = new CSVOutput(config.getStringProperty(ConfigProperty.CSV_OUTPUT_DIRECTORY));
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
	@SuppressWarnings("unchecked") /* The cast in the db initialization via GraphDBConnector
		/* is necessary because the GraphDBFactory needs to return GraphDBConnector<?,?>. That's
		/* because of the implementation of the specific database connectors
		/* that implement GraphDBConnector<specific-vertex, specific-edge> and the compiler does not look further that
		/* e.g. the specific-vertex actually implements Vertex<..., ...>. */
	private void connectDB() {
		String databasePath;
		try {
			databasePath = config.getPathProperty(ConfigProperty.DATABASE_DIR);
		} catch (IOException e) {
			LOG.error("Database directory " + config.getStringProperty(ConfigProperty.DATABASE_DIR)
					+ "can't be converted to absolute path", e);
			return;
		}

		GraphDBType databaseType = GraphDBType.getGraphDBType(config.getStringProperty(ConfigProperty.DATABASE_TYPE));
		db = new GraphDBCommonImpl((GraphDBConnector<Vertex<?,?>, Edge<?,?>>)GraphDBFactory.getGraphDB(databaseType));

		db.connect(databasePath, null, null);
	}

	/**
	 * Connects a database set by the connector parameter.
	 * @param connector a custom database connector
	 */
	private void connectDB(GraphDBConnector<Vertex<?,?>, Edge<?,?>> connector) {
		String databasePath;
		try {
			databasePath = config.getPathProperty(ConfigProperty.DATABASE_DIR);
		} catch (IOException e) {
			LOG.error("Database directory " + config.getStringProperty(ConfigProperty.DATABASE_DIR)
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
	private void runBenchmarkTest(Dataset dataset, Test test, GraphDBConnector<Vertex<?,?>, Edge<?,?>> db) {

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
	 * @return Csv processor
	 */
	private static ProcessCSV loadCSV(String csvDir, GraphDBConnector<Vertex<?,?>, Edge<?,?>> db) {
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

