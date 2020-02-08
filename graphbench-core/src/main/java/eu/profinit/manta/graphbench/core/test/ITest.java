package eu.profinit.manta.graphbench.core.test;

import eu.profinit.manta.graphbench.core.config.GraphDBConfiguration;
import eu.profinit.manta.graphbench.core.csv.ProcessCSV;
import eu.profinit.manta.graphbench.core.dataset.Dataset;
import eu.profinit.manta.graphbench.core.db.IGraphDBConnector;
import org.apache.commons.configuration.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Interface for tests.
 * @param <C> graph database
 */
public interface ITest<C extends IGraphDBConnector> {
    void test(ProcessCSV csv, C db);

    List<TestResult> getResults();
    Dataset getDataset();
}
