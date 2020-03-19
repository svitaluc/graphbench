package cz.cvut.fit.manta.graphbench.core.test;

import cz.cvut.fit.manta.graphbench.core.csv.ProcessCSV;
import cz.cvut.fit.manta.graphbench.core.dataset.Dataset;
import cz.cvut.fit.manta.graphbench.core.db.GraphDBConnector;

import java.util.List;

/**
 * Interface for individual tests.
 * @param <C> graph database
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public interface Test<C extends GraphDBConnector> {
    void test(ProcessCSV csv, C db);

    /**
     * @return Gets results of the test. It's a list of individual {@link TestResult}s.
     */
    List<TestResult> getResults();

    /**
     * @return Gets a dataset on which the test was run. It can be null if no dataset was used.
     */
    Dataset getDataset();
}
