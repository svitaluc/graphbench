package eu.profinit.manta.graphbench.core.test;

import eu.profinit.manta.graphbench.core.csv.ProcessCSV;
import eu.profinit.manta.graphbench.core.db.IGraphDBConnector;

public interface ITest<C extends IGraphDBConnector> {
    void test(ProcessCSV csv, C db);
}
