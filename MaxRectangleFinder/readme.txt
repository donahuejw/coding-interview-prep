Problem - 

Write an algorithm to find the largest rectangle consisting of all zeroes within an arbitrarily sized grid of ones.  For example:

int[][] input = new int[][] {{1,1,1,1,1,1,1,1},
  							 {0,0,0,1,1,1,1,1},
							 {0,0,0,1,1,1,1,1},
							 {0,0,0,1,0,0,0,1},
							 {1,1,1,1,0,0,0,1},
							 {1,1,0,0,1,1,1,1},
							 {1,1,0,0,1,1,1,1},
							 {1,1,1,1,1,1,1,1}};
							 
should find the largest rectangle having an area of 9, with its upper-left corner at row=1, col=0.

Solution:

Compute a histogram for each row of the input grid, where the values for each column represent the height of contiguous zeroes at that location.  Within each such histogram you can then calculate the largest rectangle in O(n) time using a stack.