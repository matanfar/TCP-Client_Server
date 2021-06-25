package clientserver;

import java.util.List;

/**
 * The root interface in the Matrix hierarchy.
 * A Matrix represents a collection of cells in a 2D array, known as its indices.
 * Represents the common functionality required for all matrices
 */
public interface IMatrix {
    /**
     *
     * @param index to be queried
     * @return the actual value it encapsulates
     */
    public int getValue(Index index);

    /**
     *
     * @param index
     * @return a list of its neighboring indices.
     * Adjacency logic is delegated to implementing classes
     */
    public List<Index> getAdjacentIndices(Index index);
}