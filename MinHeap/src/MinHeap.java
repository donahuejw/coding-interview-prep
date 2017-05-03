import com.google.common.base.Preconditions;

import java.util.*;

public class MinHeap<T extends Comparable<T>> {

    private List<T> nodes = new ArrayList<>();


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
            //move new value to proper location in Heap
            heapify(nodes.size() - 1);
        }
    }

    public T removeMin() {
        if (isEmpty()) {
            throw new IllegalStateException("removeMin() called on Heap while it is currently empty");
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
        if (isIndexValid(parentIndex) && outOfPlaceNode.compareTo(nodes.get(parentIndex)) <= 0) {
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
            throw new IllegalArgumentException("nodeToBubbleDownIndex must point to a valid, non-null location in the MinHeap");
        }

        int leftChildIndex = getLeftChildIndex(nodeToBubbleDownIndex);
        int rightChildIndex = getRightChildIndex(nodeToBubbleDownIndex);

        if ((isIndexValid(leftChildIndex) && nodes.get(leftChildIndex).compareTo(nodeToBubbleDown) <= 0)
                || (isIndexValid(rightChildIndex) && nodes.get(rightChildIndex).compareTo(nodeToBubbleDown) <= 0)) {
            // node is greater than one of its children, so bubble it down
            // now, drop down to right location in Heap

            int finalIndexForNodeToBubbleDown = nodeToBubbleDownIndex;

            // Shift nodes up as long as they are greater than the node to bubble down, then once we find where
            // the node to bubble down goes we copy it in.  This saves some copies by avoiding repeated swaps of the
            // node to bubble down
            while ((isIndexValid(leftChildIndex) && nodeToBubbleDown.compareTo(nodes.get(leftChildIndex)) > 0)
                    || (isIndexValid(rightChildIndex) && nodeToBubbleDown.compareTo(nodes.get(rightChildIndex)) > 0)) {
                if (isIndexValid(leftChildIndex)
                        && (!isIndexValid(rightChildIndex)
                            || nodes.get(leftChildIndex).compareTo(nodes.get(rightChildIndex))<=0)) {
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
            throw new IllegalArgumentException("nodeToBubbleUpIndex must point to a non-null location in the MinHeap");
        }

        int finalIndexForNodeToBubbleUp = nodeToBubbleUpIndex;

        while (isIndexValid(getParentIndex(finalIndexForNodeToBubbleUp))
                && nodeToBubbleUp.compareTo(nodes.get(getParentIndex(finalIndexForNodeToBubbleUp))) <= 0) {
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
}
