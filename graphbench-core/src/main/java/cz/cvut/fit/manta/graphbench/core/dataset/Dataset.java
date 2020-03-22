package cz.cvut.fit.manta.graphbench.core.dataset;

import cz.cvut.fit.manta.graphbench.core.db.Translator;

import java.util.Collection;

/**
 * Interface representing a dataset containing records of particular vertices/edges/edge attributes
 * (always in a separate file).
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public interface Dataset {

    /** Get directory path of the dataset. */
    String getDatasetDir();

    /** Get collection of all vertex ids in the dataset. */
    Collection<String> getVerticesIds(Translator trans, Integer seed);
}
