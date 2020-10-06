package cz.cvut.fit.manta.graphbench;

import java.util.Iterator;

public class BatchReader implements Iterator<String[][]> {
    private final int batchSize;
    private final Iterator<String[]> iterator;
    private int readDataSize = 0;

    public BatchReader(Iterable<String[]> reader) {
        this.batchSize = 100; // default
        this.iterator = reader.iterator();
    }

    public BatchReader(int batchSize, Iterable<String[]> reader) {
        this.batchSize = batchSize;
        this.iterator = reader.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public String[][] next() {
        return getBatch(batchSize);
    }

    /**
     * Next with custom defined batch size
     *
     * @param batchSize Size in each next could be different
     * @return batch
     */
    public String[][] next(int batchSize) {
        return getBatch(batchSize);
    }

    public int getReadDataSize() {
        return readDataSize;
    }

    /*
     * Private methods
     */
    private String[][] getBatch(int batchSize) {
        // Partitioning of data
        int counter = 0;
        String[][] partition = new String[batchSize][];

        // Fill partition until it's full
        while (iterator.hasNext()) {
            if (counter != batchSize) {
                partition[counter] = iterator.next();
                counter++;
            } else {
                break;
            }
        }

        // Shrink in case partition it's bigger than data
        if (counter < partition.length) {
            String[][] smallerPartition = new String[counter][];
            System.arraycopy(partition, 0, smallerPartition, 0, counter);

            // Add read data
            readDataSize += smallerPartition.length;

            return smallerPartition;
        } else {
            // Add read data
            readDataSize += partition.length;

            return partition;
        }
    }
}
