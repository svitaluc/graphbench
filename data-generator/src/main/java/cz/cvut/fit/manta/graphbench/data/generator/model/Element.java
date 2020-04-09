package cz.cvut.fit.manta.graphbench.data.generator.model;

/**
 * Represents an element (an edge, a node or an edge attribute).
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public abstract class Element {
    /** Id of the element. */
    protected Long id;

    /**
     * @param id Id of the element
     */
    public void setId(Long id) {
        this.id = id;
    };

    /**
     * @return String array containing a String representation of each element attribute in each field
     */
    public abstract String[] getStringSet();
}
