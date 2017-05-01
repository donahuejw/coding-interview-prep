import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BuildHistogramsTests {
    @Test
    public void NoZeroesTest() throws Exception {
        int[][] testInput = new int[][] {
                {1,1,1,1},
                {1,1,1,1},
                {1,1,1,1}
        };

        int[][] expected = new int[][] {
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0}
        };

        assertHistogramsEqual(expected, RectangleFinder.INSTANCE.buildHistograms(testInput));
    }

    @Test
    public void AllZeroesTest() throws Exception {
        int[][] testInput = new int[][] {
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0}
        };

        int[][] expected = new int[][] {
                {1,1,1,1},
                {2,2,2,2},
                {3,3,3,3}
        };

        assertHistogramsEqual(expected, RectangleFinder.INSTANCE.buildHistograms(testInput));
    }

    @Test
    public void RectangleInMiddleOfLargerAreaTest() throws Exception {
        int[][] testInput = new int[][] {
                {1,1,1,1,1,1},
                {1,1,0,0,0,1},
                {1,1,0,0,0,1},
                {1,1,0,0,0,1},
                {1,1,1,1,1,1}
        };

        int[][] expected = new int[][] {
                {0,0,0,0,0,0},
                {0,0,1,1,1,0},
                {0,0,2,2,2,0},
                {0,0,3,3,3,0},
                {0,0,0,0,0,0}
        };

        assertHistogramsEqual(expected, RectangleFinder.INSTANCE.buildHistograms(testInput));
    }

    @Test
    public void RectangleAlongTopRowTest() throws Exception {
        int[][] testInput = new int[][] {
                {1,0,0,0,0,1},
                {1,1,1,1,1,1},
                {1,1,1,1,1,1},
                {1,1,1,1,1,1},
        };

        int[][] expected = new int[][] {
                {0,1,1,1,1,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,0}
        };

        assertHistogramsEqual(expected, RectangleFinder.INSTANCE.buildHistograms(testInput));
    }

    private void assertHistogramsEqual(int[][] h1, int[][] h2) {
        assertEquals(h1.length, h2.length);
        assertEquals(h1[0].length, h2[0].length);

        for (int r=0; r<h1.length; r++) {
            int[] row1 = h1[r];
            int[] row2 = h2[r];
            for (int c=0; c<row1.length; c++) {
                assertEquals(row1[c], row2[c]);
            }
        }
    }
}
