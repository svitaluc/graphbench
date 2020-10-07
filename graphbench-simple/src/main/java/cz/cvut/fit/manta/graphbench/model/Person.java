package cz.cvut.fit.manta.graphbench.model;

import com.arangodb.entity.DocumentField;

public class Person {
    @DocumentField(DocumentField.Type.KEY)
    private String key;
    private String personId;
    private String name;
    private int year;

    public Person() {
    }

    public Person(String key, String personId, String name, int year) {
        this.key = key;
        this.personId = personId;
        this.name = name;
        this.year = year;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
