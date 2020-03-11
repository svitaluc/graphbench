package cz.cvut.fit.manta.graphbench.core.test;

import cz.cvut.fit.manta.graphbench.core.db.IGraphDBConnector;
import cz.cvut.fit.manta.graphbench.core.csv.ProcessCSV;
import cz.cvut.fit.manta.graphbench.core.dataset.IDataset;

import java.util.List;

/**
 * Interface for individual tests.
 * @param <C> graph database
 */
public interface ITest<C extends IGraphDBConnector> {
    void test(ProcessCSV csv, C db);

    /**
     * @return Gets results of the test. It's a list of individual {@link TestResult}s.
     */
    List<TestResult> getResults();

    /**
     * @return Gets a dataset on which the test was run. It can be null if no dataset was used.
     */
    IDataset getDataset();
}
