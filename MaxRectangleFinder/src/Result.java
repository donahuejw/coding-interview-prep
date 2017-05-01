public class Result {
    private static final int ROW_INDEX = 0;
    private static final int COL_INDEX = 1;
    private int[] lowerRight;
    private int[] upperLeft;

    public Result(int lowerRightRow, int lowerRightCol, int upperLeftRow, int upperLeftCol) {
        this.lowerRight = new int[]{lowerRightRow, lowerRightCol};
        this.upperLeft = new int[]{upperLeftRow, upperLeftCol};
    }

    public int[] getLowerRight() {
        return this.lowerRight;
    }

    public int[] getUpperLeft() {
        return this.upperLeft;
    }

    public boolean containsRectangle() {
        return (this.lowerRight[ROW_INDEX] >= 0
                && this.lowerRight[COL_INDEX] >= 0
                && this.upperLeft[ROW_INDEX] >= 0
                && this.upperLeft[COL_INDEX] >= 0);
    }

    public int getArea() {
        if (this.lowerRight[ROW_INDEX] == -1) {
            return 0;
        }

        return ((lowerRight[ROW_INDEX] - upperLeft[ROW_INDEX])+1) * ((lowerRight[COL_INDEX] - upperLeft[COL_INDEX])+1);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Result)) {
            return false;
        }

        Result that = (Result) obj;

        return (this.lowerRight[ROW_INDEX] == that.lowerRight[ROW_INDEX]
            && this.lowerRight[COL_INDEX] == that.lowerRight[COL_INDEX]
            && this.upperLeft[ROW_INDEX] == that.upperLeft[ROW_INDEX]
            && this.upperLeft[COL_INDEX] == that.upperLeft[COL_INDEX]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Rectangle coordinates -\nLower right corner: {")
                .append(this.lowerRight[ROW_INDEX])
                .append(", ")
                .append(this.lowerRight[COL_INDEX])
                .append("}");
        sb.append("\nUpper left corner: {")
                .append(this.upperLeft[ROW_INDEX])
                .append(", ")
                .append(this.upperLeft[COL_INDEX])
                .append("}");
        sb.append("\nArea: ")
                .append(this.getArea());

        return sb.toString();
    }
}
