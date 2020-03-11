package cz.cvut.fit.manta.graphbench.core.dataset;

import cz.cvut.fit.manta.graphbench.core.db.Translator;

import java.util.*;

/**
 * Basic implementation of the {@link IDataset} interface.
 */
public class Dataset implements IDataset {
    private String datasetDir;

    public Dataset(String datasetDir) {
        this.datasetDir = datasetDir;
    }

    @Override
    public String getDatasetDir() {
        return datasetDir;
    }

    @Override
    public Collection<String> getVerticesIds(Translator trans, Integer seed) {
        Collection<String> allIds = trans.getAllNodeIds();
        return allIds;
    }

    /**
     * Creates a sorted list out of a collection of comparable objects, in ascending order.
     * @param c collection of comparable objects
     * @param <T> comparable type
     * @return sorted list of provided objects, in ascending order
     */
    private <T extends Comparable<? super T>> List<T> createSortedList(Collection<T> c) {
        List<T> list = new ArrayList<>(c);
        Collections.sort(list);
        return list;
    }
}
