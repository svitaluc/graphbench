package eu.profinit.manta.graphbench.core.test;

import eu.profinit.manta.graphbench.core.config.GraphDBConfiguration;
import eu.profinit.manta.graphbench.core.csv.ProcessCSV;
import eu.profinit.manta.graphbench.core.dataset.Dataset;
import eu.profinit.manta.graphbench.core.db.IGraphDBConnector;
import org.apache.commons.configuration.Configuration;

import java.util.Map;

public interface ITest<C extends IGraphDBConnector> {
    void test(ProcessCSV csv, C db);

    Map<String, Long> getRestults();

    Dataset getDataset();
    GraphDBConfiguration getGraphDBConfiguration();
}
