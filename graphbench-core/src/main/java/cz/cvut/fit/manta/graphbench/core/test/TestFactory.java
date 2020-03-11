package cz.cvut.fit.manta.graphbench.core.test;

import cz.cvut.fit.manta.graphbench.core.dataset.IDataset;
import cz.cvut.fit.manta.graphbench.core.test.benchmark.BasicOperationsTest;

/**
 * Creates an instance of a class implementing the {@link ITest} interface.
 *
 * If you want to extend the benchmark with a new test, add another "case" in the switch command of the
 * {@link TestFactory#getTest(TestType, IDataset)} method, returning a new instance of the new test (that implements
 * the {@link ITest} interface.
 */
public class TestFactory {
    /**
     * Gets an instance of a class implementing the {@link ITest} interface. It requires definition of the test
     * type and a reference to a dataset.
     * @param type {@link TestType} test type
     * @param dataset {@link IDataset} representing a dataset for the given test
     * @return test of the given test type referencing given dataset
     */
    public static ITest getTest(TestType type, IDataset dataset) {
        switch (type) {
            case BASIC_OPERATIONS:
                return new BasicOperationsTest(dataset);
            default:
                throw new UnsupportedOperationException("Test type " + type.toString() + " is not supported.");
        }
    }
}
