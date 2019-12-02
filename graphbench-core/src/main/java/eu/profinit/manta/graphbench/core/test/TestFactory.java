package eu.profinit.manta.graphbench.core.test;

import eu.profinit.manta.graphbench.core.dataset.Dataset;

public class TestFactory {
    public static ITest getTest(TestType type, Dataset dataset) {
        switch (type) {
            case BASIC_OPERATIONS:
                return new BasicOperationsTest(dataset);
            default:
                throw new UnsupportedOperationException("Test type " + type.toString() + " is not supported.");
        }
    }
}
