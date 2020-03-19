package cz.cvut.fit.manta.graphbench.data.generator.model;

/**
 * Represents an element (an edge or a node).
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public abstract class Element {
    protected Long id;

    public void setId(Long id) {
        this.id = id;
    };

    public abstract String[] getStringSet();
}
