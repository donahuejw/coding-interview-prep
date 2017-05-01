import java.util.Stack;

/**
 * Created by John on 1/24/2017.
 */
public class RectangleFinder {

    public static RectangleFinder INSTANCE = new RectangleFinder();


    private RectangleFinder() {
    }

    public Result findLargestRectangle(int[][] input) {

        int[][] histograms = buildHistograms(input);
        Result result = new Result(-1, -1, -1, -1);

        for (int row = 0; row<histograms.length; row++) {
            Result rectangleForHistogram = findLargestRectangleInHistogram(histograms[row], row);
            if (rectangleForHistogram.getArea() > result.getArea()) {
                result = rectangleForHistogram;
            }
        }

        return result;
    }

    Result findLargestRectangleInHistogram(int[] histogram, int currentRowIndex) {
        int largestRectangleArea = 0;
        int[] lowerRightCoordinate = new int[]{-1,-1};
        int[] upperLeftCoordinate = new int[]{-1,-1};

        Stack<Integer> indicesOfHeightChanges = new Stack<>();
        int column;

        for (column=0; column<histogram.length;) {
            if (indicesOfHeightChanges.empty() || histogram[indicesOfHeightChanges.peek()] < histogram[column]) {
                indicesOfHeightChanges.push(column++);
            } else {
                int height = histogram[indicesOfHeightChanges.pop()];
                int width = indicesOfHeightChanges.empty() ? column : (column - indicesOfHeightChanges.peek()-1);
                int areaOfFoundRectangle = height * width;
                if (largestRectangleArea < areaOfFoundRectangle && (areaOfFoundRectangle != 1)) {
                    largestRectangleArea = areaOfFoundRectangle;
                    lowerRightCoordinate = new int[]{currentRowIndex, column-1};
                    upperLeftCoordinate = new int[]{(currentRowIndex-height)+1, column-width};
                }
            }
        }

        while (!indicesOfHeightChanges.empty()) {
            int height = histogram[indicesOfHeightChanges.pop()];
            int width = indicesOfHeightChanges.empty() ? column : (column-indicesOfHeightChanges.peek()-1);
            int areaOfFoundRectangle = height * width;
            if (largestRectangleArea < areaOfFoundRectangle && (areaOfFoundRectangle != 1)) {
                largestRectangleArea = areaOfFoundRectangle;
                lowerRightCoordinate = new int[]{currentRowIndex, column-1};
                upperLeftCoordinate = new int[]{(currentRowIndex-height)+1, column-width};
            }
        }

        return new Result(lowerRightCoordinate[0], lowerRightCoordinate[1], upperLeftCoordinate[0], upperLeftCoordinate[1]);
    }

    int[][] buildHistograms(int[][] input) {
        int[][] histograms = new int[input.length][input[0].length];

        //copy in first row reversed
        for (int col=0; col<input[0].length; col++) {
            histograms[0][col] = (input[0][col]^1);
        }

        // now build up overall histogram one row at a time
        for (int row=1;row<input.length;row++) {
            for (int col=0; col<input[row].length; col++) {
                if (input[row][col] == 0) {
                    histograms[row][col] = histograms[row-1][col] + 1;
                } else {
                    histograms[row][col] = 0;
                }
            }
        }

        return histograms;
    }
}