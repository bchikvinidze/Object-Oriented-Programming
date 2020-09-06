import junit.framework.TestCase;
import java.util.*;

public class TetrisGridTest extends TestCase {
	
	// Provided simple clearRows() test
	// width 2, height 3 grid
	public void testClear1() {
		boolean[][] before =
		{	
			{true, true, false, },
			{false, true, true, }
		};
		boolean[][] after =
		{	
			{true, false, false},
			{false, true, false}
		};
		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();
		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
	}

	/* one row and column*/
	public void testClear2() {
		boolean[][] before =
				{{true}};
		boolean[][] after =
				{{false}};
		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();
		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
	}

	/* one row, many columns - all true*/
	public void testClear3() {
		boolean[][] before =
				{{true}, {true}, {true}};
		boolean[][] after =
				{{false}, {false}, {false}};
		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();
		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
	}

	/* one row, many columns*/
	public void testClear4() {
		boolean[][] before =
				{{true}, {false}, {true}};
		boolean[][] after =
				{{true}, {false}, {true}};
		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();
		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
	}

	/* one column, many rows - all true*/
	public void testClear5() {
		boolean[][] before =
				{{true, true, true}};
		boolean[][] after =
				{{false, false, false}};
		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();
		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
	}

	/* one column, many rows*/
	public void testClear6() {
		boolean[][] before =
				{{true, false, true}};
		boolean[][] after =
				{{false, false, false}};
		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();
		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
	}
}
