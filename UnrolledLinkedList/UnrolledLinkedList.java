import java.util.stream.*;

public class UnrolledLinkedList {
	private int size = 0;
	private Node head;
	static private final int MAX_NODE_SIZE = 5;
	
	private static class Node {
		int size;
		int[] values;
		Node next;
		
		public Node() {
			values = new int[UnrolledLinkedList.MAX_NODE_SIZE];
			next = null;
			size = 0;
		}
	}
	
	public UnrolledLinkedList() {
		head = new Node();
	}
	
	public int get(int index) {
		if (index >= size) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		Node current = head;
		int bucketsThusFar = 0;
		
		while (current != null && bucketsThusFar + current.size <= index) {
			bucketsThusFar += current.size;
			current = current.next;
		}
		
		// current should now hold reference to Node that contains our desired element
		int indexInNode = (bucketsThusFar == 0 ? index : index % bucketsThusFar);
		return current.values[indexInNode];
	}
	
	
	public void insert(int index, int value) {
		Node current = head;
		int bucketsThusFar = 0;
		
		while (current != null && bucketsThusFar + MAX_NODE_SIZE <= index) {
			bucketsThusFar += MAX_NODE_SIZE;
			current = current.next;
		}
		
		if (current == null) {
			throw new ArrayIndexOutOfBoundsException("Cannot insert a new element beyond the end of the current linked list");
		}
		
		int indexInNode = (bucketsThusFar == 0) ? index : index % bucketsThusFar;
		
		if (indexInNode < current.size) {
			// move elements that would have to be shifted to a new node
			Node newNode = new Node();
			
			int indexInNewNode = 0;
			for (int idx = indexInNode; idx < current.size; idx++) {
				newNode.values[indexInNewNode++] = current.values[idx];
				newNode.size += 1;
			}
			current.size -= (current.size-indexInNode);
			newNode.next = current.next;
			current.next = newNode;
		}
			
		current.values[indexInNode] = value;
		current.size += 1;
		
		this.size += 1;
	}
	
	public int add(int value) {
		Node current = head;
		int elemsThusFar = current.size;
		
		while (current.next != null) {
			current = current.next;
			elemsThusFar += current.size;
		}
		
		// at this point current should be the final node in the ULL
		if (current.size == MAX_NODE_SIZE) {
			Node newNode = new Node();
			current.next = newNode;
			current = newNode;
		}
		
		current.values[current.size++] = value;
		
		return elemsThusFar;
	}
	
	public String toString() {
		Node current = head;
		
		StringBuilder sb = new StringBuilder();
		while (current != null) {
			sb.append("size: ").append(current.size).append("; ");
			sb.append("{");
			for (int idx=0; idx<current.size-1; idx++) {
				sb.append(current.values[idx]).append(",");
			}
			sb.append(current.values[current.size-1]).append("}");
			
			current = current.next;
			if (current != null) {
				sb.append(" --> ");
			}
		}
		
		return sb.toString();
	}
	
	public static void main(String args[]) {
		UnrolledLinkedList ull = new UnrolledLinkedList();
		ull.insert(0, 1);
		ull.insert(1, 3);
		ull.insert(2, 5);		
		ull.insert(3, 7);
		ull.insert(4, 9);
		System.out.println(ull);
		
		
		IntStream.range(0,ull.size).forEach(i -> {
			System.out.println("get(" + i + "): " + ull.get(i));			
		});
		
		ull.insert(3, 11);
		ull.insert(4, 13);
		System.out.println(ull);
		IntStream.range(0,ull.size).forEach(i -> {
			System.out.println("get(" + i + "): " + ull.get(i));			
		});
		
		ull.insert(1, 15);
		System.out.println(ull);
		ull.insert(2, 17);
		System.out.println(ull);
		IntStream.range(0,ull.size).forEach(i -> {
			System.out.println("get(" + i + "): " + ull.get(i));			
		});
		
		ull.insert(9, 19);
		System.out.println(ull);
		
		final UnrolledLinkedList ull2 = new UnrolledLinkedList();
		IntStream.range(0,11).map(i -> 2*i+1).forEach(mi -> System.out.println(ull2.add(mi)));
		System.out.println(ull2);
		ull2.insert(11, 23);
		System.out.println(ull2);
	}
	
}