package cz.cvut.fit.manta.graphbench.repository;

import java.util.Set;

/**
 * ImportRepository Interface
 * <p>
 * You should implement this interface when you are implementing a new technology
 *
 * @author dbucek
 */
public interface QueryRepository extends AutoCloseable {
    /**
     * Method which stores vertices
     *
     * @param vertices Array of personId, name, year, LABEL (always Person)
     * @return
     */
    Set<String> queryDirect(String[][] vertices);

    /**
     * Method which stores edges
     *
     * @param edges Array of startNode(personId), endNode(personId), type of relationship (always FRIEND)
     * @return
     */
    Set<String> queryTraverse(String[][] edges);
}
