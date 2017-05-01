import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RectangleFinderEndtoEndTests {

    @Test
    public void VerticalRectangleInMiddleOfSpaceTest() throws Exception {
        int[][] testInput = new int[][] {
                {1,1,1,1,1,1},
                {1,1,1,1,1,1},
                {1,1,0,0,1,1},
                {1,1,0,0,1,1},
                {1,1,0,0,1,1},
                {1,1,1,1,1,1}
        };

        Result expectedResult = new Result(4, 3, 2, 2);

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangle(testInput);

        assertEquals(expectedResult, largestRectangle);
        assertEquals(6, largestRectangle.getArea());
    }

    @Test
    public void multiRectangleTest() throws Exception {
        int[][] testInput = new int[][] {
                {1,1,1,1,1,1,1,1},
                {0,0,0,1,1,1,1,1},
                {0,0,0,1,1,1,1,1},
                {0,0,0,1,0,0,0,1},
                {1,1,1,1,0,0,0,1},
                {1,1,0,0,1,1,1,1},
                {1,1,0,0,1,1,1,1},
                {1,1,1,1,1,1,1,1}
        };

        Result expectedResult = new Result(3, 2, 1, 0);

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangle(testInput);

        assertEquals(expectedResult, largestRectangle);
        assertEquals(9, largestRectangle.getArea());
    }

    @Test
    public void multiLevelTest() throws Exception {
        int [][] testInput = new int[][]{
                {1,1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,0,0,0,1,1,1,1},
                {1,1,1,1,0,0,0,1,1,1,1},
                {1,1,1,1,0,0,0,1,1,1,1},
                {1,1,1,1,0,0,0,1,1,1,1},
                {1,1,1,1,0,0,0,1,1,1,1},
                {1,1,1,1,0,0,0,1,1,1,1},
                {1,1,1,0,0,0,0,1,1,1,1},
                {1,1,0,0,0,0,0,0,1,1,1},
                {1,0,0,0,0,0,0,0,1,1,1},
                {1,0,0,0,0,0,0,0,0,0,0}
        };

        Result expectedResult = new Result(10, 6, 1, 4);

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangle(testInput);

        assertEquals(expectedResult, largestRectangle);
        assertEquals(30, largestRectangle.getArea());
    }

    @Test
    public void oneBigRectangleTest() throws Exception {
        int[][] testInput =  new int[][]{
                {0,0,0,0,0,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,0}
        };

        Result expectedResult = new Result(5, 5, 0, 0);

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangle(testInput);

        assertEquals(expectedResult, largestRectangle);
        assertEquals(36, largestRectangle.getArea());
    }

    @Test
    public void RectangleAlongTopRowTest() throws Exception {
        int[][] testInput = new int[][] {
                {1,0,0,0,0,1},
                {1,1,1,1,1,1},
                {1,1,1,1,1,1},
                {1,1,1,1,1,1},
        };

        Result expectedResult = new Result(0, 4, 0, 1);

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangle(testInput);

        assertTrue(largestRectangle.containsRectangle());
        assertEquals(expectedResult, largestRectangle);
        assertEquals(4, largestRectangle.getArea());
    }

    @Test
    public void noRectanglesTest1() throws Exception {
        int[][] testInput = new int[][]{
                {1,1,1,1,1},
                {1,1,1,1,1},
                {1,1,1,1,1},
                {1,1,1,1,1},
                {1,1,1,1,1}

        };
        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangle(testInput);
        assertFalse(largestRectangle.containsRectangle());
    }

    @Test
    public void noRectanglesTest2() throws Exception {
        int[][] testInput = new int[][]{
                {1,0,1,0,1},
                {0,1,0,1,0},
                {1,0,1,0,1},
                {0,1,0,1,0},
                {1,0,1,0,1}

        };
        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangle(testInput);
        assertFalse(largestRectangle.containsRectangle());
    }

    @Test
    public void towersTest() throws Exception {
        int[][] testInput = new int[][]{
                {1,1,1,1,0,1,1,1},
                {1,1,1,1,0,1,1,1},
                {1,1,1,1,0,1,1,1},
                {1,1,1,1,0,1,1,1},
                {1,1,0,1,0,1,1,1},
                {1,1,0,1,0,1,0,1},
                {1,1,0,1,0,1,0,1},
                {1,0,0,0,0,0,0,0},

        };

        Result expectedResult = new Result(7, 4, 0, 4);

        Result largestRectangle = RectangleFinder.INSTANCE.findLargestRectangle(testInput);

        assertEquals(expectedResult, largestRectangle);
        assertEquals(8, largestRectangle.getArea());
    }
}
