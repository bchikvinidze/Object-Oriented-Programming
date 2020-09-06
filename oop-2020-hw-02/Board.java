// Board.java
package hw2;

import org.jetbrains.annotations.NotNull;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	private boolean committed;
	private int maxHeight;
	private int[] heights;
	private int[] widths;
	//Backup data structures
	private boolean[][] gridB;
	private int maxHeightB;
	private int[] heightsB;
	private int[] widthsB;
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		if(width<0 || height<0) throw new UnsupportedOperationException("Board dimensions can't be negative!");
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		heights = new int[width];
		widths = new int[height];
		maxHeight = 0;
		committed = true;
	}

	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}

	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		return maxHeight;
	}

	/** Helper function for sanityCheck() recalculates
	 * widths, heights and maxheight
	 **/
	private int recalcParams(int[] tmpWidths, int[] tmpHeights){
		int tmpMaxHeight = 0;
		for(int row=0; row<height; row++){
			for(int col=0; col<width; col++){
				if(grid[col][row]) {
					tmpWidths[row]++;
					tmpHeights[col] = row+1;
					if(tmpMaxHeight < tmpHeights[col])
						tmpMaxHeight = Integer.valueOf(tmpHeights[col]);
				}
			}
		}
		return tmpMaxHeight;
	}

	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			int[] tmpWidths = new int[height];
			int[] tmpHeights = new int[width];
			int tmpMaxHeight = recalcParams(tmpWidths, tmpHeights);
			if(tmpMaxHeight != maxHeight) throw new RuntimeException("MaxHeight mismatch!"+ tmpMaxHeight + " " + maxHeight);
			for(int row=0; row<height; row++)
				if(tmpWidths[row] != widths[row]) throw new RuntimeException("widths mismatch!");
			for(int col=0; col<width; col++)
				if(tmpHeights[col] != heights[col]) throw new RuntimeException("heights mismatch!");
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int offset = 0;
		if(piece.getWidth()+x > width) throw new UnsupportedOperationException("Piece is out of bounds!");
		for(int i=0; i<piece.getWidth(); i++){
			int tmpOffset = heights[x+i] - piece.getSkirt()[i];
			if(tmpOffset > offset)
				offset = tmpOffset;
		}
		return offset;
	}

	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}

	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y];
	}

	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if(x>=width || y>=height) return true;
		return grid[x][y];
	}

	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	/** Baks up main data structures before making changes*
	 */
	private void backup(){
		gridB = new boolean[width][height];
		heightsB = new int[width];
		widthsB = new int[height];
		for(int i=0; i<grid.length; i++)
			System.arraycopy(grid[i], 0, gridB[i], 0, height);
		maxHeightB = maxHeight;
		System.arraycopy(heights, 0, heightsB, 0, width);
		System.arraycopy(widths, 0, widthsB, 0, height);
	}

	/**
	  Helper method which takes certain coordinates of a Piece and Board, and depending on
	  whether it is filled or not, updates heights, widths and maxHeight of the
	  Board. if a row if filled, returns true, false otherwise.
	  */
	private boolean updateParams(int x, int col, int y, int row, int result, boolean[][] pieceGrid){
		if(pieceGrid[col][row]) {
			grid[x+col][y+row] = pieceGrid[col][row];
			widths[y+row]++;
			if(y+row >= heights[x+col]){
				heights[x+col] = y+row+1;
				if(heights[x+col] > maxHeight)
					maxHeight = heights[x+col];
			}
			if(widths[y+row] == width)
				return true;
		}
		return false;
	}

	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		if (!committed) throw new RuntimeException("place commit problem");
		backup();
		committed = false;
		int result = PLACE_OK;
		boolean[][] pieceGrid = piece.toBoard();
		for(int row=0; row<piece.getHeight(); row++){
			for(int col=0; col<piece.getWidth(); col++){
				if(x+col>=width || y+row>=height || y<0 || x<0) return PLACE_OUT_BOUNDS;
				if(grid[x+col][y+row] && pieceGrid[col][row]) return PLACE_BAD;
				if(updateParams(x, col, y, row, result, pieceGrid))
					result = PLACE_ROW_FILLED;
			}
		}
		sanityCheck();
		return result;
	}

	/** Copies a row to a new boolean 2d array
	 */
	private void copyRow(boolean[][] updatedBoard, int row, int rowsCleared){
		for(int i=0; i<width; i++){
			updatedBoard[i][row-rowsCleared] = grid[i][row];
		}
	}

	/** calculates heights of each column. starts
	 	by checking from top to bottom. On first encounter of
	 	a Piece, sets that position as a hew height.
	 	Notice how I don't use this approach in sanityCheck()
	 	method, because using another (although less efficient) approach
	 	could help me find potential problems with the code.
	  */
	private void recalculateHeights(){
		maxHeight=0;
		for(int col=0; col<width; col++){
			heights[col] = 0;
			for(int row=height-1; row>=0; row--){
				if(grid[col][row]) {
					heights[col] = row+1;
					if(maxHeight < heights[col]) maxHeight = heights[col];
					break;
				}
			}
		}
	}

	/** Helper function for clearRows. iterates through rows
	  and copies non-full rows to a temporary data structure.
	  Returns number of removed rows
	 */
	private int iterRows(boolean[][] updatedBoard, int[] updatedWidths){
		int rowsCleared=0;
		for(int row=0; row<(maxHeight); row++){
			if(widths[row] != width){
				copyRow(updatedBoard, row, rowsCleared);
				updatedWidths[row-rowsCleared] = widths[row];
			}  else {
				rowsCleared++;
			}
		}
		return rowsCleared;
	}
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	 This is a wrapper function.
	*/
	public int clearRows() {
		boolean[][] updatedBoard = new boolean[width][height];
		int[] updatedWidths = new int[height];
		int rowsCleared = iterRows(updatedBoard, updatedWidths);
		grid = updatedBoard;
		recalculateHeights();
		widths = updatedWidths;
		if(rowsCleared > 0) committed = false;
		sanityCheck();
		return rowsCleared;
	}

	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(committed) return;
		int[] tempWidths = widths;
		widths = widthsB;
		widthsB = tempWidths;

		int[] tempHeights = heights;
		heights = heightsB;
		heightsB = tempHeights;

		boolean[][] tempGrid = grid;
		grid = gridB;
		gridB = tempGrid;

		int tempMaxHeight = maxHeight;
		maxHeight = maxHeightB;
		maxHeightB = tempMaxHeight;

		committed=true;
		sanityCheck();
	}

	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
		backup(); // basically overwrite previous state. New state will be the only state.
	}

	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}