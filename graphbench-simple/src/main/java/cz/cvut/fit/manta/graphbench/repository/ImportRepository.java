package cz.cvut.fit.manta.graphbench.repository;

/**
 * ImportRepository Interface
 * <p>
 * You should implement this interface when you are implementing a new technology
 *
 * @author dbucek
 */
public interface ImportRepository extends AutoCloseable {
    /**
     * Any init code should be here
     */
    void init();

    /**
     * Method which stores vertices
     *
     * @param vertices Array of personId, name, year, LABEL (always Person)
     */
    void storeVertices(String[][] vertices);

    /**
     * Method which stores edges
     *
     * @param edges Array of startNode(personId), endNode(personId), type of relationship (always FRIEND)
     */
    void storeEdges(String[][] edges);

    /**
     * Get default size of batch of vertices for implemented technology
     *
     * @return Size of batch
     */
    int getDefaultVertexBatchSize();

    /**
     * Get default size of batch of vertices for implemented technology
     *
     * @return Size of batch
     */
    int getDefaultEdgeBatchSize();
}
