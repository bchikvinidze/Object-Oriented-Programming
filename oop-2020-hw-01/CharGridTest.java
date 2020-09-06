
// Test cases for CharGrid -- a few basic tests are provided.

import junit.framework.TestCase;

public class CharGridTest extends TestCase {
	
	public void testCharArea1() {
		char[][] grid = new char[][] {
				{'a', 'y', ' '},
				{'x', 'a', 'z'},
			};
		
		
		CharGrid cg = new CharGrid(grid);
				
		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('z'));
	}
	
	
	public void testCharArea2() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
			};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(6, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
	}

	/* case:
	* in the opposite corners;
	* involving every row;
	* never appearing;
	* appearing once;
	* space appearing;
	* */
	public void testCharArea3() {
		char[][] grid = new char[][] {
				{'a', 'b', 'c', 'd'},
				{'a', ' ', 'c', 'b'},
				{'x', 'b', 'c', 'a'}
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(12, cg.charArea('a'));
		assertEquals(9, cg.charArea('b'));
		assertEquals(0, cg.charArea('z'));
		assertEquals(3, cg.charArea('c'));
		assertEquals(1, cg.charArea('x'));
		assertEquals(1, cg.charArea(' '));
	}

	/* case:
	* empty grid;
	* one row grid;
	* grid with two empty columns;
	* */
	public void testCharArea4() {
		char[][] grid = new char[][] {
		};
		char[][] grid2 = new char[][] {
				{}
		};
		char[][] grid3 = new char[][] {
				{},
				{}
		};

		CharGrid cg = new CharGrid(grid);
		CharGrid cg2 = new CharGrid(grid2);
		CharGrid cg3 = new CharGrid(grid3);

		assertEquals(0, cg.charArea('a'));
		assertEquals(0, cg2.charArea('a'));
		assertEquals(0, cg3.charArea('a'));

	}

	/* countPlus tests begin.
	* case:
	* empty grid;
	* one row grid;
	* empty, two columns
	* */
	public void testCountPlus1(){
		char[][] grid = new char[][] {
		};
		char[][] grid2 = new char[][] {
				{}
		};
		char[][] grid3 = new char[][] {
				{},
				{}
		};

		CharGrid cg = new CharGrid(grid);
		CharGrid cg2 = new CharGrid(grid2);
		CharGrid cg3 = new CharGrid(grid3);

		assertEquals(0, cg.countPlus());
		assertEquals(0, cg2.countPlus());
		assertEquals(0, cg3.countPlus());
	}

	/* case:
	* case used in handout.
	* */
	public void testCountPlus2(){
		char[][] grid = new char[][] {
				{' ', ' ', 'p', ' ', ' ',' ', ' ', ' ', ' '},
				{' ', ' ', 'p', ' ', ' ',' ', ' ', 'x', ' '},
				{'p', 'p', 'p', 'p', 'p',' ', 'x', 'x', 'x'},
				{' ', ' ', 'p', ' ', ' ','y', ' ', 'x', ' '},
				{' ', ' ', 'p', ' ', 'y','y', 'y', ' ', ' '},
				{'z', 'z', 'z', 'z', 'z','y', 'z', 'z', 'z'},
				{' ', ' ', 'x', 'x', ' ','y', ' ', ' ', ' '}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(2, cg.countPlus());
	}

	/* case:
	* same case as in handout, but has plus made from nullspace characters.
	* */
	public void testCountPlus3(){
		//checking empty
		char[][] grid = new char[][] {
				{' ', ' ', 'p', ' ', ' ','\u0000', ' ', ' ', ' '},
				{' ', ' ', 'p', 'z', '\u0000','\u0000', '\u0000', 'x', ' '},
				{'p', 'p', 'p', 'p', 'p','\u0000', 'x', 'x', 'x'},
				{' ', ' ', 'p', ' ', ' ','y', ' ', 'x', ' '},
				{' ', ' ', 'p', ' ', 'y','y', 'y', ' ', ' '},
				{'z', 'z', 'z', 'z', 'z','y', 'z', 'z', 'z'},
				{' ', ' ', 'x', 'x', ' ','y', ' ', ' ', ' '}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(3, cg.countPlus());
	}

	/* case:
	*  3x3 grid with same character everywhere
	* */
	public void testCountPlus4(){
		//same characters
		char[][] grid = new char[][] {
				{'p', 'p', 'p'},
				{'p', 'p', 'p'},
				{'p', 'p', 'p'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(1, cg.countPlus());
	}
}
