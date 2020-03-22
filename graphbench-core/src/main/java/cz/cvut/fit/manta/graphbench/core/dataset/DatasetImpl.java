package cz.cvut.fit.manta.graphbench.core.dataset;

import cz.cvut.fit.manta.graphbench.core.db.Translator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Basic implementation of the {@link Dataset} interface.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class DatasetImpl implements Dataset {
    /** Directory path of the dataset. */
    private String datasetDir;

    /**
     * Constructor of the {@link DatasetImpl}.
     *
     * @param datasetDir Directory path of the dataset
     */
    public DatasetImpl(String datasetDir) {
        this.datasetDir = datasetDir;
    }

    @Override
    public String getDatasetDir() {
        return datasetDir;
    }

    @Override
    public Collection<String> getVerticesIds(Translator trans, Integer seed) {
        return Collections.unmodifiableCollection(trans.getAllNodeIds());
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
