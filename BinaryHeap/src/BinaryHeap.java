import com.google.common.base.Preconditions;

import java.util.*;

public class BinaryHeap<T extends Comparable<T>> {

    private static final int DEFAULT_INITIAL_CAPACITY = 100;
    private List<T> nodes;
    private HeapOrder ordering;

    public enum HeapOrder {
        ASC(1),
        DESC(-1);

        private int orderModifier;

        HeapOrder(int orderModifier) {
            this.orderModifier = orderModifier;
        }
    }


    /**
     * Constructs a heap with a default ordering of ascending and a default size of DEFAULT_INITIAL_CAPACITY
     */
    public BinaryHeap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Constructs a heap with the DEFAULT_INITIAL_CAPACITY and provided ordering
     * @param ordering desired ordering of the returned elements (ascending or descending)
     */
    public BinaryHeap(HeapOrder ordering) {
        this(DEFAULT_INITIAL_CAPACITY, ordering);
    }

    /**
     * Constructs a heap with the indicated initial capacity, ordered in ascending order
     * @param initialCapacity desired initial capacity for the heap
     */
    public BinaryHeap(int initialCapacity) {
        this(initialCapacity, HeapOrder.ASC);
    }

    /**
     * Constructs a heap with the indicated initial capacity and desired element ordering
     * @param initialCapacity desired initial capacity for the heap
     * @param ordering desired ordering of the returned elements (ascending or descending)
     */
    public BinaryHeap(int initialCapacity, HeapOrder ordering) {
        nodes = new ArrayList<>(initialCapacity);
        this.ordering = ordering;
    }


    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public void insertAll(List<T> newValues) {
        newValues.forEach(this::insert);
    }

    public void insert(T newValue) {
        Preconditions.checkArgument(newValue != null, "newValue to insert into the heap cannot be NULL");
        nodes.add(newValue);

        if (nodes.size() > 1) {
            //move new value to proper location in BinaryHeap
            heapify(nodes.size() - 1);
        }
    }

    /**
     * Removes and returns the root element from the BinaryHeap.  Depending on the ordering specified during
     * the BinaryHeap's construction this will either be the minimum or maximum element remaining in the heap
     * according to the elements' natural ordering
     *
     * @return the minimum or maximum element remaining in the BinaryHeap, depending on the ordering specified
     * at construction (HeapOrder.ASC or HeapOrder.DESC)
     */
    public T remove() {
        if (isEmpty()) {
            throw new IllegalStateException("remove() called on BinaryHeap while it is currently empty");
        }

        T result = nodes.get(0); // this is what we'll return at the end of this method
        T last = nodes.remove(nodes.size() - 1);

        if (result == last) {
            return result; //only one item in heap, so just return it
        }

        // otherwise, move what is now the last node to the top of the heap and then bubble down to it's proper location

        nodes.set(0, last);

        if (nodes.size() > 1) {
            heapify(0);
        }

        return result;
    }

    private void heapify(int outOfPlaceNodeIndex) {
        T outOfPlaceNode = nodes.get(outOfPlaceNodeIndex);

        int parentIndex = getParentIndex(outOfPlaceNodeIndex);
        if (isIndexValid(parentIndex) && compare(outOfPlaceNode, nodes.get(parentIndex)) <= 0) {
            // node is <= its parent, so need to bubble it up
            bubbleUp(outOfPlaceNodeIndex);
        } else {
            // node is potentially > one of its children, so may need to bubble it down
            bubbleDown(outOfPlaceNodeIndex);
        }
    }

    void bubbleDown(int nodeToBubbleDownIndex) {
        T nodeToBubbleDown = getNodeSafely(nodeToBubbleDownIndex);

        if (nodeToBubbleDown == null) {
            throw new IllegalArgumentException("nodeToBubbleDownIndex must point to a valid, non-null location in the BinaryHeap");
        }

        int leftChildIndex = getLeftChildIndex(nodeToBubbleDownIndex);
        int rightChildIndex = getRightChildIndex(nodeToBubbleDownIndex);

        if ((isIndexValid(leftChildIndex) && compare(nodes.get(leftChildIndex), nodeToBubbleDown) <= 0)
                || (isIndexValid(rightChildIndex) && compare(nodes.get(rightChildIndex), nodeToBubbleDown) <= 0)) {
            // node is greater than one of its children, so bubble it down
            // now, drop down to right location in BinaryHeap

            int finalIndexForNodeToBubbleDown = nodeToBubbleDownIndex;

            // Shift nodes up as long as they are greater than the node to bubble down, then once we find where
            // the node to bubble down goes we copy it in.  This saves some copies by avoiding repeated swaps of the
            // node to bubble down
            while ((isIndexValid(leftChildIndex) && compare(nodeToBubbleDown, nodes.get(leftChildIndex)) > 0)
                    || (isIndexValid(rightChildIndex) && compare(nodeToBubbleDown, nodes.get(rightChildIndex)) > 0)) {
                if (isIndexValid(leftChildIndex)
                        && (!isIndexValid(rightChildIndex)
                            || compare(nodes.get(leftChildIndex), nodes.get(rightChildIndex))<=0)) {
                    nodes.set(finalIndexForNodeToBubbleDown, nodes.get(leftChildIndex));
                    finalIndexForNodeToBubbleDown = leftChildIndex;
                } else {
                    nodes.set(finalIndexForNodeToBubbleDown, nodes.get(rightChildIndex));
                    finalIndexForNodeToBubbleDown = rightChildIndex;
                }

                leftChildIndex = getLeftChildIndex(finalIndexForNodeToBubbleDown);
                rightChildIndex = getRightChildIndex(finalIndexForNodeToBubbleDown);
            }
            nodes.set(finalIndexForNodeToBubbleDown, nodeToBubbleDown);
        }
    }

    void bubbleUp(int nodeToBubbleUpIndex) {
        // Shift nodes down as long as they are greater than the node to bubble up, then once we find where
        // the node to bubble up goes we copy it in.  This saves some copies by avoiding repeated swaps of the
        // node to bubble up
        T nodeToBubbleUp = getNodeSafely(nodeToBubbleUpIndex);
        if (nodeToBubbleUp == null) {
            throw new IllegalArgumentException("nodeToBubbleUpIndex must point to a non-null location in the BinaryHeap");
        }

        int finalIndexForNodeToBubbleUp = nodeToBubbleUpIndex;

        while (isIndexValid(getParentIndex(finalIndexForNodeToBubbleUp))
                && compare(nodeToBubbleUp, nodes.get(getParentIndex(finalIndexForNodeToBubbleUp))) <= 0) {
            nodes.set(finalIndexForNodeToBubbleUp, nodes.get(getParentIndex(finalIndexForNodeToBubbleUp)));
            finalIndexForNodeToBubbleUp = getParentIndex(finalIndexForNodeToBubbleUp);
        }

        nodes.set(finalIndexForNodeToBubbleUp, nodeToBubbleUp);
    }

    private T getNodeSafely(int nodeIndex) {
        return (isIndexValid(nodeIndex)) ? nodes.get(nodeIndex) : null;
    }

    private boolean isIndexValid(int index) {
        return index >= 0 && index < nodes.size();
    }

    private int getLeftChildIndex(int parent) {
        return (2 * parent) + 1;
    }

    private int getRightChildIndex(int parent) {
        return (2 * parent) + 2;
    }

    private int getParentIndex(int child) {
        int intermediateResult = (child - 1);
        return (intermediateResult < 0 ? intermediateResult : intermediateResult / 2);
    }

    private int compare(T e1, T e2) {
        return ordering.orderModifier * e1.compareTo(e2);
    }
}
