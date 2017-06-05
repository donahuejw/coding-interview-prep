import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class UnrolledLinkedList<E> implements List<E> {
    private int size = 0;
    Node<E> head;
    private int maxNodeSize;
    static private final int DEFAULT_MAX_NODE_SIZE = 8;

    static class Node<E> {
        int size;
        E[] values;
        Node<E> next;

        Node(int maxNodeSize) {
            values = (E[]) new Object[maxNodeSize];
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
        return current.values[indexInNode];
    }

    public boolean add(E value) {
        Node<E> current = head;

        while (current.next != null) {
            current = current.next;
        }

        // at this point current should be the final node in the UnrolledLinkedList
        if (current.size == maxNodeSize) {
            splitNode(current);
            current = current.next;
        }

        current.values[current.size] = value;
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

        // Check whether new element is being inserted in middle of node's array of
        // values, in which case we need to shift some values to make room
        if (nodeLocalIndex < current.size-1) {
            int indexToShift = current.size-2;
            do {
                current.values[indexToShift+1] = current.values[indexToShift];
                indexToShift--;
            } while (indexToShift >= nodeLocalIndex);
        }
        current.values[nodeLocalIndex] = value;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    // misc methods

    void splitNode(Node<E> current) {
        // create new node and fix up linked list pointers
        Node<E> newNode = new Node<>(this.maxNodeSize);
        newNode.next = current.next;
        current.next = newNode;

        // move 1/2 values in current node to new node
        int numItemsToMove = current.size - current.size/2;
        int indexToStartRemove = current.size/2;
        for (int indexToRemove = indexToStartRemove; indexToRemove<indexToStartRemove + numItemsToMove; indexToRemove++) {
            newNode.values[newNode.size] = current.values[indexToRemove];
            current.values[indexToRemove] = null;
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
                sb.append(current.values[idx]).append(",");
            }
            sb.append(current.values[current.size - 1]).append("}");

            current = current.next;
            if (current != null) {
                sb.append(" --> ");
            }
        }

        return sb.toString();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;

        for (E element : c) {
            result = this.add(element) || result;
        }
        return result;
    }

    @Override
    public void clear() {
        Node<E> current = this.head;
        while (current != null) {
            Node<E> next = current.next;
            current.next = null;
            current.values = null;
            current = next;
        }
        this.head = new Node<>(maxNodeSize);
        this.size = 0;
    }

    // not yet implemented methods

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {

    }

    @Override
    public void sort(Comparator<? super E> c) {

    }

    @Override
    public Spliterator<E> spliterator() {
        return null;
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return false;
    }

    @Override
    public Stream<E> stream() {
        return null;
    }

    @Override
    public Stream<E> parallelStream() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super E> action) {

    }
}