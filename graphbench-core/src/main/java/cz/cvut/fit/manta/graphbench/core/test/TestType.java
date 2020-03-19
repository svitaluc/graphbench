package cz.cvut.fit.manta.graphbench.core.test;

/**
 * Enumeration of all possible tests.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum TestType {
    BASIC_OPERATIONS("BASIC_OPERATIONS");

    /** Name of the test that must eventually match with the property
     * {@link cz.cvut.fit.manta.graphbench.core.config.model.ConfigProperty#TEST_TYPE} in the config.properties file. */
    private String name;

    TestType(String name) {
        this.name = name;
    }

    /**
     * Gets type of the test or throws an {@link IllegalArgumentException} if the parameter string does not match
     * any test type.
     * @param type {@link String} representing name of a test type
     * @return {@link TestType} test type
     */
    public static TestType getTestType(String type) {
        for (TestType item : values()) {
            if (item.name.equals(type)) {
                return item;
            }
        }
        throw new IllegalArgumentException("The required test type " + type + " is not supported.");
    }
}
