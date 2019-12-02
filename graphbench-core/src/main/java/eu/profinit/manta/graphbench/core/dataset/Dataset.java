package eu.profinit.manta.graphbench.core.dataset;

import eu.profinit.manta.graphbench.core.db.Translator;

import java.util.*;
import java.util.stream.IntStream;

public class Dataset {
    private String datasetDir;

    public Dataset(String datasetDir) {
        this.datasetDir = datasetDir;
    }

    public String getDatasetDir() {
        return datasetDir;
    }

    public Collection<String> getVerticesIds(Translator trans, Integer seed) {
        Collection<String> allIds = trans.getAllFileIds();
        List<String> sortedAllIds = createSortedList(allIds);

        final int[] positions = getRandomPositions(allIds.size(), allIds.size(), seed);

        Collection<String> verticesIds = new HashSet<>();
        for (int i = 0; i < positions.length; i++) {
            verticesIds.add(sortedAllIds.get(positions[i]));
        }
        return verticesIds;
    }


    private int[] getRandomPositions(Integer size, Integer generatedAmount, Integer seed) {
        IntStream intStream = new Random(seed).ints(0, size);
        if (size >= generatedAmount) {
            intStream = intStream.distinct();
        }
        return intStream
                .limit(generatedAmount)
                .toArray();
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
