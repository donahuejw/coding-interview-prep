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

        while (current != null && bucketsThusFar + current.size <= index) {
            bucketsThusFar += current.size;
            current = current.next;
        }

        // current should now hold reference to Node that contains our desired element
        int indexInNode = (bucketsThusFar == 0 ? index : index % bucketsThusFar);
        return current.values.get(indexInNode);
    }


/*
    public void set(int index, E value) {
        if (index >= this.size) {
            throw new IllegalArgumentException("Specified index: " + index + " is larger than current size of the list: " + size);
        }

        Node<E> current = head;
        int bucketsThusFar = 0;

        while (current != null && bucketsThusFar + current.size <= index) {
            bucketsThusFar += DEFAULT_MAX_NODE_SIZE;
            current = current.next;
        }

        if (current == null) {
            throw new ArrayIndexOutOfBoundsException("Cannot insert a new element beyond the end of the current linked list");
        }

        int indexInNode = (bucketsThusFar == 0) ? index : index % bucketsThusFar;

        if (indexInNode < current.size) {
            // move elements that would have to be shifted to a new node
            Node<E> newNode = new Node<>();

            int indexInNewNode = 0;
            for (int idx = indexInNode; idx < current.size; idx++) {
                newNode.values.set(indexInNewNode++, current.values.get(idx));
                newNode.size += 1;
            }
            current.size -= (current.size - indexInNode);
            newNode.next = current.next;
            current.next = newNode;
        }

        current.values.set(indexInNode, value);
        current.size += 1;

        this.size += 1;
    }
*/

    public boolean add(E value) {
        Node<E> current = head;
        int elemsThusFar = current.size;

        while (current.next != null) {
            current = current.next;
            elemsThusFar += current.size;
        }

        // at this point current should be the final node in the UnrolledLinkedList
        if (current.size == (DEFAULT_MAX_NODE_SIZE/2)) {
            Node<E> newNode = new Node<>(this.maxNodeSize);
            current.next = newNode;
            current = newNode;
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

        if (current.size >= maxNodeSize/2) {
            Node<E> newNode = new Node<>(maxNodeSize);
            Node<E> currentNext = current.next;

            current.next = newNode;
            newNode.next = currentNext;
            newNode.size = 1;
            newNode.values.add(current.values.get(current.size-1));
            --current.size;
        }

        int nodeLocalIndex = safeMod(index, totalElems);

        ++current.size;
        ++this.size;
        current.values.add(nodeLocalIndex, value);
    }

    public int getSize() {
        return size;
    }

    private int safeMod(int numerator, int denominator) {
        return (denominator == 0 ? numerator : numerator % denominator);
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