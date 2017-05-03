import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class MinHeapTests {
    @Test
    public void InsertWithNullArgThrowsIAE() throws Exception {
        MinHeap<Integer> heap = new MinHeap<>();

        boolean threwAsExpected = false;
        try {
            heap.insert(null);
        } catch (IllegalArgumentException e) {
            threwAsExpected = true;
        }

        assertTrue("call to insert() did not throw the expected exception", threwAsExpected);
    }

    @Test
    public void AddRemoveOneItemTest() throws Exception {
        MinHeap<Integer> heap = new MinHeap<>();
        heap.insert(10);

        assertThat(10, equalTo(heap.removeMin()));
        assertTrue(heap.isEmpty());
    }

    @Test
    public void AddMultipleItemsInSortedOrderThenRemoveTest() throws Exception {
        List<Integer> testData = Lists.newArrayList(10, 20, 30, 40, 50);
        testWithOrder(testData, (i,j) -> (i.compareTo(j)));
        testWithOrder(testData, (i,j) -> (-1 * i.compareTo(j)));
    }

    private void testWithOrder(List<Integer> testData, Comparator<Integer> comparator) {
        testData.sort(comparator);
        MinHeap<Integer> heap = new MinHeap<>();

        testData.forEach(heap::insert);

        int lastRemoved = Integer.MIN_VALUE;
        while (!heap.isEmpty()) {
            Integer currentValue = heap.removeMin();
            assertTrue("removeMin() incorrectly returned " + lastRemoved + " before " + currentValue,
                    lastRemoved < currentValue);
            lastRemoved = currentValue;
        }
        assertTrue(heap.isEmpty());
    }

    @Test
    public void HeapHandlesDupeValuesTest() throws Exception {
        MinHeap<Integer> heap = new MinHeap<>();
        heap.insert(10);
        heap.insert(10);

        assertThat(10, equalTo(heap.removeMin()));
        assertThat(10, equalTo(heap.removeMin()));
        assertTrue(heap.isEmpty());
    }

    @Test
    public void RemoveFromEmptyHeapThrowsExceptionTest() throws Exception {
        MinHeap<Integer> heap = new MinHeap<>();

        boolean failedAsExpected = false;
        try {
            heap.removeMin();
        } catch (IllegalStateException e) {
            failedAsExpected = true;
        }

        assertTrue("removeMin() method call did not fail as expected", failedAsExpected);
    }

    @Test
    public void LargeInsertAndRemoveTest() throws Exception {
        Random r = new Random();
        MinHeap<Integer> heap = new MinHeap<>();

        IntStream.range(0,50).forEach(i -> heap.insert(r.nextInt(100)+1));

        int lastNumberReturned = heap.removeMin();
        while (!heap.isEmpty()) {
            int nextNumber = heap.removeMin();
            assertTrue("MinHeap incorrectly returned " + lastNumberReturned + " before " + nextNumber, lastNumberReturned <= nextNumber);
            lastNumberReturned = nextNumber;
        }
    }

    @Test
    public void InsertAllTest() throws Exception {
        List<Integer> testData = Lists.newArrayList(10,20,30,40,50);
        MinHeap<Integer> heap = new MinHeap<>();

        heap.insertAll(testData);
        assertThat(10, equalTo(heap.removeMin()));
        assertThat(20, equalTo(heap.removeMin()));
        assertThat(30, equalTo(heap.removeMin()));
        assertThat(40, equalTo(heap.removeMin()));
        assertThat(50, equalTo(heap.removeMin()));
        assertTrue(heap.isEmpty());
    }
}
