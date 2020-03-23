package cz.cvut.fit.manta.graphbench.test;

import cz.cvut.fit.manta.graphbench.core.dataset.Dataset;
import cz.cvut.fit.manta.graphbench.core.test.Test;
import cz.cvut.fit.manta.graphbench.test.benchmark.BasicOperationsTest;

/**
 * Creates an instance of a class implementing the {@link Test} interface.
 *
 * If you want to extend the benchmark with a new test, add another "case" in the switch command of the
 * {@link TestFactory#createTest(String, Dataset)} method, returning a new instance of the new test (that implements
 * the {@link Test} interface.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TestFactory {
    /**
     * Gets an instance of a class implementing the {@link Test} interface. It requires definition of the test
     * type and a reference to a dataset.
     * @param testName Name of the test type
     * @param dataset {@link Dataset} representing a dataset for the given test
     * @return test of the given test type referencing given dataset
     */
    public static Test createTest(String testName, Dataset dataset) {
        TestType type = TestType.getTestType(testName);

        switch (type) {
            case BASIC_OPERATIONS:
                return new BasicOperationsTest(dataset);
            default:
                throw new IllegalArgumentException ("Test type " + type.toString() + " is not supported.");
        }
    }
}
