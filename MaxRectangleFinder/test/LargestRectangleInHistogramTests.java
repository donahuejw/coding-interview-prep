import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LargestRectangleInHistogramTests {
    @Test
    public void simpleHorizontalRectangleTest() throws Exception {
        int[] testInput = new int[]{2,2,2,2,2,2};

        Result expectedResult = new Result(1, 5, 0, 0);

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangleInHistogram(testInput, 1);

        assertTrue(largestRectangle.containsRectangle());
        assertEquals(expectedResult, largestRectangle);
        assertEquals(12, largestRectangle.getArea());
    }

    @Test
    public void simpleVerticalRectangleTest() throws Exception {
        int[] testInput = new int[]{0,0,4,4,0,0};

        Result expectedResult = new Result(3, 3, 0, 2);

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangleInHistogram(testInput, 3);

        assertTrue(largestRectangle.containsRectangle());
        assertEquals(expectedResult, largestRectangle);
        assertEquals(8, largestRectangle.getArea());
    }

    @Test
    public void PerpendicularRectanglesWithLargerHorizontalRectangleTest() throws Exception {
        int[] testInput = new int[] {2,2,4,4,2,2};

        Result expectedResult = new Result(3, 5, 2, 0);

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangleInHistogram(testInput,3);

        assertTrue(largestRectangle.containsRectangle());
        assertEquals(expectedResult, largestRectangle);
        assertEquals(12, largestRectangle.getArea());
    }

    @Test
    public void PerpendicularRectanglesWithLargerVerticalRectangleTest() throws Exception {
        int[] testInput = new int[]{0,2,5,5,2,0};

        Result expectedResult = new Result(4, 3, 0, 2);

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangleInHistogram(testInput, 4);

        assertTrue(largestRectangle.containsRectangle());
        assertEquals(expectedResult, largestRectangle);
        assertEquals(10, largestRectangle.getArea());
    }

    @Test
    public void NoRectangleFoundWithAllZeroes() throws Exception {
        int[] testInput = new int[]{0,0,0,0,0};

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangleInHistogram(testInput, 0);
        assertFalse(largestRectangle.containsRectangle());
    }

    @Test
    public void NoRectangleFoundWithNonContiguousOnes() throws Exception {
        int[] testInput = new int[]{0,1,0,1,0,1};

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangleInHistogram(testInput, 0);
        assertFalse(largestRectangle.containsRectangle());
    }

    @Test
    public void SingleRowRectangleTest() throws Exception {
        int[] testInput = new int[]{0,1,1,1,1,0};

        Result expectedResult = new Result(0, 4, 0, 1);

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangleInHistogram(testInput, 0);
        assertTrue(largestRectangle.containsRectangle());
        assertEquals(expectedResult, largestRectangle);
        assertEquals(4, largestRectangle.getArea());
    }

    @Test
    public void SingleColumnRectangleTest() throws Exception {
        int[] testInput = new int[]{0,0,4,0,0,0};

        Result expectedResult = new Result(3, 2, 0, 2);

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangleInHistogram(testInput, 3);
        assertTrue(largestRectangle.containsRectangle());
        assertEquals(expectedResult, largestRectangle);
        assertEquals(4, largestRectangle.getArea());
    }

    @Test
    public void stepUpTest() throws Exception {
        int[] testInput = new int[] {1,2,3,4,5,6};

        Result expectedResult = new Result(5, 5, 2, 3);

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangleInHistogram(testInput, 5);

        assertEquals(expectedResult, largestRectangle);
        assertEquals(12, largestRectangle.getArea());
    }

    @Test
    public void stepDownTest() throws Exception {
        int[] testInput = new int[]{6,5,4,3,2,1};

        Result expectedResult = new Result(5, 2, 2, 0);

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangleInHistogram(testInput,5);

        assertEquals(expectedResult, largestRectangle);
        assertEquals(12, largestRectangle.getArea());
    }

    @Test
    public void divotTest() throws Exception {
        int[] testInput = new int[]{7,7,2,2,7,7};

        Result expectedResult = new Result(6, 1, 0, 0);

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangleInHistogram(testInput, 6);

        assertEquals(expectedResult, largestRectangle);
        assertEquals(14, largestRectangle.getArea());
    }
}
