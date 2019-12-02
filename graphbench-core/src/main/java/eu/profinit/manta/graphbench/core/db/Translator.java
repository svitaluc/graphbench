package eu.profinit.manta.graphbench.core.db;

import org.apache.log4j.Logger;
import eu.profinit.manta.graphbench.core.access.IElement;

import java.util.*;

public class Translator {

	final static Logger LOG = Logger.getLogger(Translator.class);

	private Map<String, String> mainMap;
    private Map<String, IElement> tempMap;
    private Map<String, String> edgeMap;
    private Map<String, IElement> tempEdgeMap;

    private String superRootId;

    public String getSuperRootId() {
        return superRootId;
    }

    public void setSuperRootId(String superRootId) {
        this.superRootId = superRootId;
    }
    
    public Translator() {
        mainMap = new HashMap<>();
        tempMap = new HashMap<>();
        edgeMap = new HashMap<>();
        tempEdgeMap = new HashMap<>();
    }

    public void put(String idOrig, String idNew) {
        mainMap.put(idOrig, idNew);
    }

    public String get(String idOrig) {
        if (mainMap.get(idOrig) == null) {
            LOG.info("not in map");
        }
        return mainMap.get(idOrig);
    }

    public void putTemp(String idOrig, IElement element) {
        tempMap.put(idOrig, element);
        put(idOrig, element.id().toString());
    }

    public IElement getTemp(String idOrig) {
        return tempMap.get(idOrig);
    }

    public void putEdge(String idOrig, String idNew) {
        edgeMap.put(idOrig, idNew);
    }

    public String getEdge(String idOrig) {
        return edgeMap.get(idOrig);
    }

    public void putTempEdge(String idOrig, IElement element) {
        tempEdgeMap.put(idOrig, element);
        putEdge(idOrig, element.id().toString());
    }

    public IElement getTempEdge(String idOrig) {
        return tempEdgeMap.get(idOrig);
    }

    public void remapTempAndClear() {
        tempMap.forEach((k, v) -> mainMap.put(k, v.id().toString()));
        tempEdgeMap.forEach((k, v) -> edgeMap.put(k, v.id().toString()));
        
        tempMap.clear();
        tempEdgeMap.clear();
        ;
    }

    public Collection<String> getAllFileIds(){
//        return mainMap.values();
        return mainMap.keySet();
    }

    public void logMainMap() {
        List<String> keyList = new ArrayList<>(mainMap.keySet());
        java.util.Collections.sort(keyList);
        for(String key : keyList) {
            LOG.info("Main map KEY: " + key + " - " + mainMap.get(key));
        }
    }
}
