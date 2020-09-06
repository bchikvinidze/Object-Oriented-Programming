import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.

	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
			"1 6 4 0 0 0 0 0 2",
			"2 0 0 4 0 3 9 1 0",
			"0 0 5 0 8 0 4 0 7",
			"0 9 0 0 0 6 5 0 0",
			"5 0 0 1 0 2 0 0 8",
			"0 0 8 9 0 0 0 3 0",
			"8 0 9 0 4 0 2 0 0",
			"0 7 3 5 0 9 0 0 1",
			"4 0 0 0 0 0 6 7 9");


	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
			"530070000",
			"600195000",
			"098000060",
			"800060003",
			"400803001",
			"700020006",
			"060000280",
			"000419005",
			"000080079");

	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
			"3 7 0 0 0 0 0 8 0",
			"0 0 1 0 9 3 0 0 0",
			"0 4 0 7 8 0 0 0 3",
			"0 9 3 8 0 0 0 1 2",
			"0 0 0 0 4 0 0 0 0",
			"5 2 0 0 0 6 7 9 0",
			"6 0 0 0 2 1 0 4 0",
			"0 0 0 5 3 0 9 0 0",
			"0 3 0 0 0 0 0 5 1");


	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	private long solveTime;
	private int solutionCnt;
	private List<Spot> spots;
	private int[][] grid;
	private int[][] solution;

	// Provided various static utility methods to
	// convert data formats to int[][] grid.

	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}


	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}

		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}


	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);

		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		spots = new ArrayList<Spot>();
		grid = ints;
		solution = new int[SIZE][SIZE];
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				if(ints[i][j] == 0) spots.add(new Spot(ints[i][j], i, j));
				else solution[i][j] = ints[i][j];
			}
		}
		Collections.sort(spots);
		solutionCnt = 0;
	}

	/**
	 * Sets up based on given string
	 */
	public Sudoku(String ints){
		this(textToGrid(ints));
	}

	private String makeString(boolean original){
		StringBuilder strBuilder = new StringBuilder();
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				if(original)
					strBuilder.append(" "+grid[i][j]);
				else
					strBuilder.append(" "+solution[i][j]);
			}
			strBuilder.append("\n");
		}
		return strBuilder.toString();
	}

	@Override
	public String toString() {
		return makeString(true);
	}

	/*
	 * fills solution with appropriate values
	 */
	private void fillSolution(int n){
		for(int i=0; i<n; i++){
			Spot curSpot = spots.get(i);
			solution[curSpot.getRow()][curSpot.getCol()] = curSpot.getCurVal();;
		}
	}

	/*
	 * Helper function for solve that uses recursive backtracking
	 * to solve sudoku. Assigns solution to the first discovered one.
	 */
	private void solveRecursive(int n){
		if(n == spots.size()) {
			if(solutionCnt == 0) fillSolution(n);
			solutionCnt++;
			return;
		}
		if(solutionCnt >= 100) return;
		Spot curSpot = spots.get(n);
		Iterator<Integer> it = curSpot.getValSet().iterator();
		while(it.hasNext()){
			int val = it.next();
			if(curSpot.addable(val)) {
				curSpot.set(val);
				grid[curSpot.getRow()][curSpot.getCol()] = val;
				solveRecursive(n+1);
				grid[curSpot.getRow()][curSpot.getCol()] = 0;
			}
		}
	}

	/*
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		long startTime = System.currentTimeMillis();
		solveRecursive(0);
		solveTime = System.currentTimeMillis() - startTime;
		return solutionCnt;
	}

	public String getSolutionText() {
		if(solutionCnt==0)	return "";
		return makeString(false);
	}

	public long getElapsed() {
		return solveTime;
	}

	/*
	 * Inner class representing each cell of Sudoku board
	 */
	public class Spot implements Comparable<Spot>{
		private int valuesCount; //how many different values could this spot have taken
		private int curVal; //current value assigned to spot
		private Set<Integer> possibleVals; // set of possible values
		private int row; //row index on board
		private int col; //column index on board

		public Spot(int num, int row, int col){
			curVal = num;
			this.row = row;
			this.col = col;
			possibleVals = new HashSet<Integer>();
			if(num == 0) valuesCount = calcPossibleVals();
		}

		/*
		 * These three methods: rowContains, colContains and blockContains
		 * are self-explanatory. They iterate through grid to find if number
		 * appears in row, column and 3x3 block, respectively
		 */
		private boolean rowContains(int num){
			for(int i=0; i<SIZE; i++)
				if(grid[row][i] == num) return true;
			return false;
		}

		private boolean colContains(int num){
			for(int i=0; i<SIZE; i++)
				if(grid[i][col] == num) return true;
			return false;
		}

		private boolean blockContains(int num){
			int blockRow = row/3;
			int blockCol = col/3;
			for(int i=blockRow*3; i<blockRow*3+3; i++){
				for(int j=blockCol*3; j<blockCol*3+3; j++)
					if(grid[i][j] == num) return true;
			}
			return false;
		}

		/* Checks if num can be added to the Spot possible value*/
		public boolean addable(int num){
			if(rowContains(num) || colContains(num) || blockContains(num)) return false;
			return true;
		}

		/* Checks if number is one of the possible values of spot
		 * and updates possible value Hashset accordingly
		 * */
		private boolean canAdd(int num){
			if(!addable(num)) return false;
			possibleVals.add(num);
			return true;
		}

		/* Calculates possible number of spot values, as well as marks them on
		 *  possibleVars array.
		 * */
		private int calcPossibleVals(){
			int result = 0;
			for(int i=1; i<=9; i++)
				if(canAdd(i)) result++;
			return result;
		}

		/* Sets value of the spot */
		public void set(int num){
			curVal = num;
		}

		/* getter for values set*/
		public Set<Integer> getValSet(){ return possibleVals; }

		/* getter for curVal*/
		public int getCurVal(){	return curVal; }

		/* getter for row*/
		public int getRow(){ return row; }

		/* getter for col*/
		public int getCol(){ return col; }

		/*
		sort by values count - how many different values this spot could take
		 */
		@Override
		public int compareTo(Spot spot) {
			if(spot.valuesCount == this.valuesCount) return 0;
			if(this.valuesCount > spot.valuesCount) return 1;
			return -1;
		}
	}

}
