//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

public class TetrisGrid {
	private boolean[][] grid;
	private int rowCnt;
	private int colCnt;

	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
		this.rowCnt = grid[0].length;
		this.colCnt = grid.length;
	}
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		boolean[] rowToRemove = new boolean[rowCnt];
		int rmCnt = 0;
		for(int i=0; i<rowCnt; i++){
			int filledCnt = 0;
			for(int j=0; j<colCnt; j++){
				if(grid[j][i]) filledCnt++;
			}
			if(filledCnt==colCnt) {
				rowToRemove[i] = true;
				rmCnt++;
			}
		}
		deleteRows(rowToRemove, rmCnt);
	}

	/* Deletes all the filled rows from the grid by creating
	* possibly smaller grid and then copying small grid's values to
	* original grid.
	* @param rmCnt number of rows to remove
	* @param rowToRemove boolean array containing true if i-th row should be removed
	* */
	private void deleteRows(boolean[] rowToRemove, int rmCnt){
		boolean[][] smallerGrid = new boolean[colCnt][rowCnt-rmCnt];
		int copied = 0;
		for(int i=0; i<rowCnt; i++){
			if(!rowToRemove[i]){
				for(int j=0; j<colCnt; j++){ //copy that row
					smallerGrid[j][copied] = grid[j][i];
				}
				copied++;
			}
		}
		grid = new boolean[colCnt][rowCnt];
		for(int i=0; i<(rowCnt-rmCnt); i++){
			for(int j=0; j<colCnt; j++){
				grid[j][i] = smallerGrid[j][i];
			}
		}
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return this.grid;
	}
}
