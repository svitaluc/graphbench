package eu.profinit.manta.graphbench.data.generator.model;

public abstract class Entity {
    protected Long id;

    public void setId(Long id) {
        this.id = id;
    };

    public abstract String[] getStringSet();
}
