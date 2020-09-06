// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}

	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int rows = grid.length;
		if(rows == 0) return 0;
		int cols = grid[0].length;
		if(cols == 0) return 0;

		int area = 0;
		int minx = rows;
		int miny = cols;
		int maxx = -1;
		int maxy = -1;
		for(int i=0; i<rows; i++){
			for(int j=0; j<cols; j++) {
				if(grid[i][j] == ch){
					if(i<minx) minx=i;
					if(j<miny) miny=j;
					if(i>maxx) maxx=i;
					if(j>maxy) maxy=j;
					area = (1+max(i-minx, maxx-i))*(1+max(j-miny, maxy-j));
				}
			}
		}
		return area;
	}

	private int max(int a, int b){
		if(a>b) return a;
		return b;
	}

	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int result = 0;
		int rows = grid.length;
		if(rows <= 2) return 0;
		int cols = grid[0].length;
		if(cols <= 2) return 0;

		for(int i=1; i<rows-1; i++){
			for(int j=1; j<cols-1; j++){
				int up = verticalArm(i,j,-1);
				int down = verticalArm(i,j,1);
				int left = horizontalArm(i,j,-1);
				int right = horizontalArm(i,j,1);
				if(up == down && up == left && up == right && up >= 1) result += 1;
			}
		}
		return result;
	}

	/* counts vertical direction arm length */
	private int verticalArm(int i, int j, int dir){
		char mid = grid[i][j];
		int res = 0;
		while(true) {
			j = j + 1*dir;
			if(j < 0 || j >= grid[i].length || grid[i][j] != mid) break;
			res += 1;
		}
		return res;
	}
	/* countr horizontal direction arm length */
	private int horizontalArm(int i, int j, int dir){
		char mid = grid[i][j];
		int res = 0;
		while(true) {
			i = i + 1*dir;
			if(i < 0 || i >= grid.length || grid[i][j] != mid) break;
			res += 1;
		}
		return res;
	}


}
