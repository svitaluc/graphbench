package eu.profinit.manta.graphbench.core.test;

public enum TestType {
    BASIC_OPERATIONS("BASIC_OPERATIONS");

    String name;

    TestType(String name) {
        this.name = name;
    }

    public static TestType getTestType(String type) {
        for (TestType item : values()) {
            if (item.name.equals(type)) {
                return item;
            }
        }
        throw new UnsupportedOperationException("The required test type " + type + " is not supported.");
    }
}
