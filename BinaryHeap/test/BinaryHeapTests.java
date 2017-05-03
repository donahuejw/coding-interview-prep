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

    @Test
    public void CustomComparatorWithAscSortTest() throws Exception {
        List<Integer> expectedOrderOfRemoval = Lists.newArrayList(2,4,6,8,10,12,1,3,5,7,9,11);
        testCustomComparator(BinaryHeap.HeapOrder.ASC, expectedOrderOfRemoval);
    }

    @Test
    public void CustomComparatorWithDescSortTest() throws Exception {
        List<Integer> expectedOrderOfRemoval = Lists.newArrayList(11,9,7,5,3,1,12,10,8,6,4,2);
        testCustomComparator(BinaryHeap.HeapOrder.DESC, expectedOrderOfRemoval);
    }

    private void testCustomComparator(BinaryHeap.HeapOrder sortOrder, List<Integer> expected) {
        List<Integer> testData = Lists.newArrayList(1,2,3,4,5,6,7,8,9,10,11,12);

        Comparator<Integer> evenFirstComparator = (o1, o2) -> {
            boolean isO1Even = (o1 % 2 == 0);
            boolean isO2Even = (o2 % 2 == 0);

            if (isO1Even && !isO2Even) {
                return -1;
            } else if (isO2Even && !isO1Even) {
                return 1;
            } else {
                return Integer.compare(o1, o2);
            }
        };

        BinaryHeap<Integer> heap = new BinaryHeap<>(evenFirstComparator, sortOrder);
        heap.insertAll(testData);

        int idx = 0;
        while(!heap.isEmpty()) {
            assertEquals(expected.get(idx++), heap.remove());
        }
    }
}
