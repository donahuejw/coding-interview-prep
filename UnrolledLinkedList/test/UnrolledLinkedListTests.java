import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UnrolledLinkedListTests {
    @Test
    public void addAndGetItemsThatFitInFirstHalfOfSingleNodeTest() throws Exception {
        UnrolledLinkedList<Integer> ull = new UnrolledLinkedList<>(8);
        ull.add(10);
        ull.add(20);
        ull.add(30);
        ull.add(40);

        checkExpectedContentsInOrder(Lists.newArrayList(10,20,30,40), ull);
        assertNull(ull.head.next);
    }

    @Test
    public void addAndGetItemsThatRollIntoSecondNodeTest() throws Exception {
        UnrolledLinkedList<Integer> ull = new UnrolledLinkedList<>(8);
        ull.add(10);
        ull.add(20);
        ull.add(30);
        ull.add(40);
        ull.add(50);

        checkExpectedContentsInOrder(Lists.newArrayList(10,20,30,40,50), ull);

        // also assert that we have two nodes now
        assertNotNull("Expecting there to be a second node " +
                "since we've added more elements than half maxNodeSize", ull.head.next);
        assertNull("Should only be 2 nodes, not a 3rd", ull.head.next.next);
    }

    @Test
    public void addAndGetItemsThatRollIntoThirdNodeTest() throws Exception {
        UnrolledLinkedList<Integer> ull = new UnrolledLinkedList<>(8);
        ull.add(10);
        ull.add(20);
        ull.add(30);
        ull.add(40);
        ull.add(50);
        ull.add(60);
        ull.add(70);
        ull.add(80);
        ull.add(90);

        checkExpectedContentsInOrder(Lists.newArrayList(10,20,30,40,50,60,70,80,90), ull);

        // also assert that we have two nodes now
        assertNotNull("Expecting there to be a second node", ull.head.next);
        assertNotNull("Expecting there to be a third node", ull.head.next.next);
        assertNull("Expecting there to be a third node", ull.head.next.next.next);
    }

    @Test
    public void addAtSpecificIndexWithoutExceedingHalfwayMarkJustShiftsWithinNodeTest() throws Exception {
        UnrolledLinkedList<Integer> ull = new UnrolledLinkedList<>(8);
        ull.add(10);
        ull.add(20);
        ull.add(30);
        checkExpectedContentsInOrder(Lists.newArrayList(10,20,30), ull);
        assertNull(ull.head.next);

        ull.add(2, 25);
        checkExpectedContentsInOrder(Lists.newArrayList(10,20,25,30), ull);
        assertNull(ull.head.next);
    }

    @Test
    public void addAtSpecificIndexWhileExceedingHalfwayMarkShiftsToNewNodeTest() throws Exception {
        UnrolledLinkedList<Integer> ull = new UnrolledLinkedList<>(8);
        ull.add(10);
        ull.add(20);
        ull.add(30);
        ull.add(40);
        checkExpectedContentsInOrder(Lists.newArrayList(10,20,30,40), ull);
        assertNull(ull.head.next);

        ull.add(2, 25);
        checkExpectedContentsInOrder(Lists.newArrayList(10,20,25,30,40), ull);

        // should now have two nodes in list
        assertNotNull("expecting two nodes in list since we have > 1/2 maxNodeSize", ull.head.next);
        assertNull("only expecting two nodes in list", ull.head.next.next);

        // first node should have 4 elements, 2nd should have 1
        assertEquals(4, ull.head.size);
        assertEquals(1, ull.head.next.size);

        assertEquals(5, ull.getSize());
    }

    @Test
    public void combinedAddAndAddAtSpecificIndexTest() throws Exception {
        UnrolledLinkedList<Integer> ull = new UnrolledLinkedList<>(8);
        ull.add(10);
        ull.add(20);
        ull.add(30);
        ull.add(40);
        ull.add(50);
        ull.add(1, 15);
        ull.add(3, 25);
        ull.add(5, 35);
        ull.add(7, 45);

        // nodes' contents should now be
        // 10,15,20,25 --> 30 --> 35,40 --> 45,50

        checkExpectedContentsInOrder(Lists.newArrayList(10,15,20,25,30,35,40,45,50), ull);

        //head node
        assertEquals(4, ull.head.size);

        // 2nd node
        assertNotNull(ull.head.next);
        assertEquals(1, ull.head.next.size);

        // 3rd node
        assertNotNull(ull.head.next.next);
        assertEquals(2, ull.head.next.next.size);

        // 4th node
        assertNotNull(ull.head.next.next.next);
        assertEquals(2, ull.head.next.next.next.size);

        assertNull(ull.head.next.next.next.next);
    }

    private void checkExpectedContentsInOrder(List<Integer> expected, UnrolledLinkedList<Integer> ull) {
        int i = 0;
        for (Integer integer : expected) {
            assertEquals(integer, ull.get(i++));
        }
    }
}
