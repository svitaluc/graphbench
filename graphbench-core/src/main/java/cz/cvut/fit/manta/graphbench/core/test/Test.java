package cz.cvut.fit.manta.graphbench.core.test;

import cz.cvut.fit.manta.graphbench.core.csv.ProcessCSV;
import cz.cvut.fit.manta.graphbench.core.dataset.Dataset;
import cz.cvut.fit.manta.graphbench.core.db.GraphDBConnector;

import java.util.List;

/**
 * Interface for individual tests.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public interface Test {
    /**
     * Runs the test method.
     * @param csv Processor of a csv file
     * @param db Connector of a graph database
     */
    void test(ProcessCSV csv, GraphDBConnector<?,?> db);

    /**
     * @return Gets results of the test. It's a list of individual {@link TestResult}s.
     */
    List<TestResult> getResults();

    /**
     * @return Gets a dataset on which the test was run. It can be null if no dataset was used.
     */
    Dataset getDataset();
}
