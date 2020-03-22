package cz.cvut.fit.manta.graphbench.core.db;

import cz.cvut.fit.manta.graphbench.core.access.Edge;
import cz.cvut.fit.manta.graphbench.core.access.Element;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Translator keeps maps from vertex id defined by the loaded data to db's internal vertex id.
 * This is a recommended approach for JanusGraph/Titan db as described at
 * https://docs.janusgraph.org/advanced-topics/bulk-loading/. With more dbs it should be reconsidered
 * when to use it.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class Translator {

    /** Logger. */
	private final static Logger LOG = Logger.getLogger(Translator.class);

    /** Map containing mappings from a vertex id defined in dataset to a vertex id in a db. */
	private Map<String, String> nodeMap;
	/** Map containing mappings from an element id defined in a dataset to an element representation in the code.
     * It's cleared after each commit. */
    private Map<String, Element> tempNodeMap;
    /** Map containing mappings from an edge id defined in dataset to an edge id in a db. */
    private Map<String, String> edgeMap;
    /** Map containing mappings from an edge id defined in a dataset to an edge representation in the code.
     * It's cleared after each commit. */
    private Map<String, Edge> tempEdgeMap;

    /** Id of a super root. */
    private String superRootId;

    /**
     * @return Id of a super root
     */
    public String getSuperRootId() {
        return superRootId;
    }

    /**
     * @param superRootId Id of a super root
     */
    public void setSuperRootId(String superRootId) {
        this.superRootId = superRootId;
    }

    /**
     * Constructor of the {@link Translator}.
     */
    public Translator() {
        nodeMap = new HashMap<>();
        tempNodeMap = new HashMap<>();
        edgeMap = new HashMap<>();
        tempEdgeMap = new HashMap<>();
    }

    /**
     * Puts records into the main map.
     * @param idOrig Original id of an element.
     * @param idNew New element id in the database.
     */
    private void putNode(String idOrig, String idNew) {
        nodeMap.put(idOrig, idNew);
    }

    /**
     * Get a database id of an element specified with its original id from the data ({@code idOrig}).
     * @param idOrig Original id of the element in a dataset.
     * @return Id of the element assigned in the database.
     */
    public String getNode(String idOrig) {
        if (nodeMap.get(idOrig) == null) {
            LOG.info("not in map");
        }
        return nodeMap.get(idOrig);
    }

    /**
     * Puts mapping of an id to its {@link Element} representation to a temporary map.
     * @param idOrig Original id of an element in the dataset.
     * @param element {@link Element} representation of the element.
     */
    public void putTempNode(String idOrig, Element element) {
        tempNodeMap.put(idOrig, element);
        putNode(idOrig, element.getId().toString());
    }

    /**
     * @param idOrig Original id of an element in the dataset.
     * @return {@link Element} representation of the element from the temporary map of id - element pairs.
     */
    public Element getTempNode(String idOrig) {
        return tempNodeMap.get(idOrig);
    }

    /**
     * Puts information about an edge - its original id from the dataset and its assigned id
     * in the database - to an edge map.
     * @param idOrig Original edge id from the dataset
     * @param idNew Assigned edge id in the database
     */
    private void putEdge(String idOrig, String idNew) {
        edgeMap.put(idOrig, idNew);
    }

    /**
     * @param idOrig Original id of the edge in the dataset
     * @return Assigned id of the edge in the database
     */
    public String getEdge(String idOrig) {
        return edgeMap.get(idOrig);
    }

    /**
     * Puts information about an edge - its original id in a dataset and its {@link Element} representation
     * of the edge.
     * @param idOrig Original id of the edge in the dataset
     * @param edge {@link Edge} representation of the edge.
     */
    public void putTempEdge(String idOrig, Edge edge) {
        tempEdgeMap.put(idOrig, edge);
        putEdge(idOrig, edge.getId().toString());
    }

    /**
     * @param idOrig Original id of the edge in the dataset
     * @return {@link Edge} representation of the edge from the temporary map of id - edge pairs.
     */
    public Edge getTempEdge(String idOrig) {
        return tempEdgeMap.get(idOrig);
    }

    /**
     * Puts all the mapped pairs from temporary maps (where key is a {@link String} of an original id
     * in the dataset, and a value implements the {@link Element}) to id maps (where both key and
     * value are a {@link String}, original id in the dataset - id assigned in the database).
     */
    public void remapTempAndClear() {
        tempNodeMap.forEach((k, v) -> nodeMap.put(k, v.getId().toString()));
        tempEdgeMap.forEach((k, v) -> edgeMap.put(k, v.getId().toString()));
        
        tempNodeMap.clear();
        tempEdgeMap.clear();
    }

    /**
     * @return All original node ids in the dataset.
     */
    public Collection<String> getAllNodeIds(){
        return nodeMap.keySet();
    }
}
