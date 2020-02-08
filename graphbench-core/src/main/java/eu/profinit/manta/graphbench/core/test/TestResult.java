package eu.profinit.manta.graphbench.core.test;

/**
 * Class representing the result of a test, that is further logged.
 */
public class TestResult {
    /** time when the result was created */
    private Long recordTimestamp;
    /** name of the test */
    private String testName;
    /** amount of time the test was running for */
    private Long testTime;

    public TestResult(Long recordTimestamp, String testName, Long testTime) {
        this.recordTimestamp = recordTimestamp;
        this.testName = testName;
        this.testTime = testTime;
    }

    public String getTestName() {
        return testName;
    }

    public Long getTestTime() {
        return testTime;
    }

    @Override
    public String toString() {
        return "Test record created at: " + recordTimestamp + "\n" +
                "\t test name: " + testName + "\n" +
                "\t test time: " + testTime;
    }
}
