package eu.profinit.manta.graphbench.run;

import eu.profinit.manta.graphbench.core.config.Config;
import eu.profinit.manta.graphbench.core.db.product.GraphDBType;
import eu.profinit.manta.graphbench.core.config.Property;
import eu.profinit.manta.graphbench.core.csv.CSVType;
import eu.profinit.manta.graphbench.core.csv.ProcessCSV;
import eu.profinit.manta.graphbench.core.dataset.Dataset;
import eu.profinit.manta.graphbench.core.db.GraphDBCommonImpl;
import eu.profinit.manta.graphbench.core.db.IGraphDBConnector;
import org.apache.log4j.Logger;
import eu.profinit.manta.graphbench.core.test.ITest;
import eu.profinit.manta.graphbench.core.test.TestFactory;

public class App {

	private final static Logger LOG = Logger.getLogger(App.class);
	private Config config = Config.getInstance();
	private IGraphDBConnector db;

	/**
	 * The main method run when the jar filed is executed.
	 * In order to run your own database implementation (implementation of the interface {@link IGraphDBConnector})
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
	public void start() {
		connectDB();
		startBody();
	}

	/**
	 * Start method that can be called with a custom {@link IGraphDBConnector} implementation.
	 * @param graphDB an implementation of the {@link IGraphDBConnector} interface.
	 */
	public void start(IGraphDBConnector graphDB) {
		connectDB(graphDB);
		startBody();
	}

	/**
	 * Loads the main configuration properties and runs a test.
	 */
	private void startBody() {
		try{
			Dataset dataset = new Dataset(config.getStringProperty(Property.DATASET_DIR));
			ITest test = TestFactory.getTest(config.getTestTypeProperty(Property.TEST_TYPE), dataset);

			runBenchmarkTest(dataset, test, db);

			finishDB();

		} catch (Exception e) {
			LOG.error("Error when starting the test!", e);
			if (db.isConnected()) {
				db.rollback();
			}
		}
	}

	/**
	 * Connects a database set in the config file.
	 */
	private void connectDB() {
		String databasePath = config.getStringProperty(Property.DATABASE_DIR);

		GraphDBType databaseType = config.getGraphDBTypeProperty(Property.DATABASE_TYPE);
		db = new GraphDBCommonImpl(GraphDBFactory.getGraphDB(databaseType));

		db.connect(databasePath, null, null);
	}

	/**
	 * Connects a database set by the connector parameter.
	 * @param connector a custom database connector
	 */
	private void connectDB(IGraphDBConnector connector) {
		String databasePath = config.getStringProperty(Property.DATABASE_DIR);
		db = connector;
		db.connect(databasePath, null, null);
	}

	/**
	 * Finalizes database operations with final commit.
	 */
	private void finishDB() {
		LOG.info("... final commit ...");
		db.commit();
//		LOG.info("Number of vertices in db: " + db.getNodesCount());

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
	private void runBenchmarkTest(Dataset dataset, ITest test, IGraphDBConnector db) {

		if(Config.getInstance().getBooleanProperty(Property.WITH_IMPORT)) {
			ProcessCSV csv = loadCSV(dataset.getDatasetDir(), db);
			test.test(csv, db);
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
	private static ProcessCSV loadCSV(String csvDir, IGraphDBConnector db) {
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

