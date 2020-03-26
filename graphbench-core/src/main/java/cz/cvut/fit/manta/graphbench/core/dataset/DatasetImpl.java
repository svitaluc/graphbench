package cz.cvut.fit.manta.graphbench.core.dataset;

import cz.cvut.fit.manta.graphbench.core.db.Translator;

import java.util.Collection;
import java.util.Collections;

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
    public Collection<String> getVerticesIds(Translator trans) {
        return Collections.unmodifiableCollection(trans.getAllNodeIds());
    }
}
