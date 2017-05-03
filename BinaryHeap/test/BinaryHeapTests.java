import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class BinaryHeapTests {
    @Test
    public void InsertWithNullArgThrowsIAE() throws Exception {
        BinaryHeap<Integer> heap = new BinaryHeap<>();

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
        BinaryHeap<Integer> heap = new BinaryHeap<>();
        heap.insert(10);

        assertThat(10, equalTo(heap.remove()));
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
        BinaryHeap<Integer> heap = new BinaryHeap<>();

        testData.forEach(heap::insert);

        int lastRemoved = Integer.MIN_VALUE;
        while (!heap.isEmpty()) {
            Integer currentValue = heap.remove();
            assertTrue("remove() incorrectly returned " + lastRemoved + " before " + currentValue,
                    lastRemoved < currentValue);
            lastRemoved = currentValue;
        }
        assertTrue(heap.isEmpty());
    }

    @Test
    public void HeapHandlesDupeValuesTest() throws Exception {
        BinaryHeap<Integer> heap = new BinaryHeap<>();
        heap.insert(10);
        heap.insert(10);

        assertThat(10, equalTo(heap.remove()));
        assertThat(10, equalTo(heap.remove()));
        assertTrue(heap.isEmpty());
    }

    @Test
    public void RemoveFromEmptyHeapThrowsExceptionTest() throws Exception {
        BinaryHeap<Integer> heap = new BinaryHeap<>();

        boolean failedAsExpected = false;
        try {
            heap.remove();
        } catch (IllegalStateException e) {
            failedAsExpected = true;
        }

        assertTrue("remove() method call did not fail as expected", failedAsExpected);
    }

    @Test
    public void LargeInsertAndRemoveWithAscendingOrderTest() throws Exception {
        Random r = new Random();
        BinaryHeap<Integer> heap = new BinaryHeap<>();

        IntStream.range(0,50).forEach(i -> heap.insert(r.nextInt(100)+1));

        int lastNumberReturned = heap.remove();
        while (!heap.isEmpty()) {
            int nextNumber = heap.remove();
            assertTrue("BinaryHeap incorrectly returned " + lastNumberReturned + " before " + nextNumber, lastNumberReturned <= nextNumber);
            lastNumberReturned = nextNumber;
        }
    }

    @Test
    public void LargeInsertAndRemoveWithDescendingOrderTest() throws Exception {
        Random r = new Random();
        BinaryHeap<Integer> heap = new BinaryHeap<>(BinaryHeap.HeapOrder.DESC);

        IntStream.range(0,50).forEach(i -> heap.insert(r.nextInt(100)+1));

        int lastNumberReturned = heap.remove();
        while (!heap.isEmpty()) {
            int nextNumber = heap.remove();
            assertTrue("BinaryHeap incorrectly returned " + lastNumberReturned + " before " + nextNumber, lastNumberReturned >= nextNumber);
            lastNumberReturned = nextNumber;
        }
    }

    @Test
    public void InsertAllTest() throws Exception {
        List<Integer> testData = Lists.newArrayList(10,20,30,40,50);
        BinaryHeap<Integer> heap = new BinaryHeap<>();

        heap.insertAll(testData);
        assertThat(10, equalTo(heap.remove()));
        assertThat(20, equalTo(heap.remove()));
        assertThat(30, equalTo(heap.remove()));
        assertThat(40, equalTo(heap.remove()));
        assertThat(50, equalTo(heap.remove()));
        assertTrue(heap.isEmpty());
    }
}
