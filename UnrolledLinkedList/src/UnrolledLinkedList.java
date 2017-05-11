import java.util.ArrayList;
import java.util.List;

public class UnrolledLinkedList<E> {
    private int size = 0;
    final Node<E> head;
    private int maxNodeSize;
    static private final int DEFAULT_MAX_NODE_SIZE = 8;

    static class Node<E> {
        int size;
        List<E> values;
        Node<E> next;

        Node(int maxNodeSize) {
            values = new ArrayList<>(maxNodeSize);
            next = null;
            size = 0;
        }
    }

    public UnrolledLinkedList(int maxNodeSize) {
        this.maxNodeSize = maxNodeSize;
        head = new Node<>(maxNodeSize);
    }

    public UnrolledLinkedList() {
        this(DEFAULT_MAX_NODE_SIZE);
    }

    public E get(int index) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }

        Node<E> current = head;
        int bucketsThusFar = 0;

        while (bucketsThusFar + current.size <= index) {
            bucketsThusFar += current.size;
            current = current.next;
        }

        // current should now hold reference to Node that contains our desired element
        int indexInNode = index - bucketsThusFar;
        return current.values.get(indexInNode);
    }

    public boolean add(E value) {
        Node<E> current = head;
        int elemsThusFar = current.size;

        while (current.next != null) {
            current = current.next;
            elemsThusFar += current.size;
        }

        // at this point current should be the final node in the UnrolledLinkedList
        if (current.size == maxNodeSize) {
            splitNode(current);
            current = current.next;
        }

        current.values.add(value);
        ++current.size;
        ++this.size;

        return true;
    }


    public void add(int index, E value) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException();
        }

        Node<E> current = head;
        int totalElems = 0;

        while (current != null && index>=(totalElems + current.size)) {
            totalElems += current.size;
            current = current.next;
        }

        if (current == null) {
            throw new IndexOutOfBoundsException("add(index,value) cannot be used to add an element beyond " +
                    "the current end of the list. Use the add(value) method instead to add it at the end of the list");
        }

        if (current.size == maxNodeSize) {
            splitNode(current);
            if ((index-totalElems)>current.size) {
                totalElems += current.size;
                current = current.next;
            }
        }

        int nodeLocalIndex = index - totalElems;

        ++current.size;
        ++this.size;
        current.values.add(nodeLocalIndex, value);
    }

    public int getSize() {
        return size;
    }

    void splitNode(Node<E> current) {
        // create new node and fix up linked list pointers
        Node<E> newNode = new Node<>(this.maxNodeSize);
        newNode.next = current.next;
        current.next = newNode;

        // move 1/2 values in current node to new node
        int numItemsToMove = current.size - current.size/2;
        int indexToRemoveFrom = current.size/2;
        for (int i=1; i<=numItemsToMove; i++) {
            newNode.values.add(current.values.remove(indexToRemoveFrom));
            ++newNode.size;
            --current.size;
        }
    }

    @Override
    public String toString() {
        Node current = head;

        StringBuilder sb = new StringBuilder();
        while (current != null) {
            sb.append("size: ").append(current.size).append("; ");
            sb.append("{");
            for (int idx = 0; idx < current.size - 1; idx++) {
                sb.append(current.values.get(idx)).append(",");
            }
            sb.append(current.values.get(current.size - 1)).append("}");

            current = current.next;
            if (current != null) {
                sb.append(" --> ");
            }
        }

        return sb.toString();
    }
}