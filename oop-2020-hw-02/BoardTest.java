package hw2;
import junit.framework.TestCase;

public class BoardTest extends TestCase {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;
	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	protected void setUp() throws Exception {
		b = new Board(3, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		b.place(pyr1, 0, 0);
	}
	
	// Check the basic width/height/max after the one placement
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}

	//test illegal dropHeight
	public void testDropHeight(){
		b.commit();
		int result = b.place(sRotated, 1, 1);
		//Notice I would use assertThows but I use JUnit 4 (assertThrows starts at JUnit 5
		try {
			b.dropHeight(sRotated,2);
			assertTrue(false);
		} catch (RuntimeException e){
			assertTrue(true);
		}
	}
	
	// Place sRotated into the board, then check some measures
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(3,b.dropHeight(sRotated,1));
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}

	//Place pyramid, then sRotated, then Stick to go over boundary
	public void testSample3(){
		Board b2 = new Board(3, 6);
		assertEquals(0,b2.dropHeight(new Piece(Piece.STICK_STR),0));
		b2.place(pyr1, 0, 0);
		b2.commit();
		b2.place(sRotated, 1, 1);
		Piece stk = new Piece(Piece.STICK_STR);
		b2.commit();
		int result = b2.place(stk, 1, 4);
		assertEquals(3,b2.dropHeight(stk,2));
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		assertEquals(1, b2.getColumnHeight(0));
		assertEquals(6, b2.getColumnHeight(1));
		assertEquals(3, b2.getColumnHeight(2));
		assertEquals(6, b2.getMaxHeight());
	}

	//Test case when overlap happens with
	public void testSample4(){
		Board b3 = new Board(3, 6);
		b3.place(pyr1, 0, 0);
		b3.commit();
		b3.place(sRotated, 1, 1);
		b3.commit();
		Piece stk = new Piece(Piece.STICK_STR);
		int result = b3.place(stk, 2, 1);
		assertEquals(Board.PLACE_BAD, result);
		assertEquals(3, b3.getColumnHeight(2));
		assertEquals(3,b3.getRowWidth(0));
		assertEquals(2,b3.getRowWidth(2));
		assertEquals(4, b3.getMaxHeight());
	}

	//test case when a row gets filled (by placing pyramid)
	public void testSample5(){
		Board b4 = new Board(3, 6);
		int result = b4.place(pyr1, 0, 0);
		assertEquals(Board.PLACE_ROW_FILLED, result);
	}

	//checking if placing on uncommitted board
	public void testSample6(){
		Board b5 = new Board(3,6);
		b5.place(pyr3,1,1);
		//Notice I would use assertThows but I use JUnit 4 (assertThrows starts at JUnit 5
		try {
			b5.place(pyr1,0,0);
			assertTrue(false);
		} catch (RuntimeException e) {
			assertTrue(true);
		}
	}

	//Test clearRows
	public void testSample7(){
		Board b3 = new Board(3, 6);
		b3.place(pyr1, 0, 0);
		b3.commit();
		b3.place(sRotated, 1, 1);
		b3.commit();
		Piece stk = new Piece(Piece.STICK_STR);
		b3.place(stk, 0, 1);
		b3.clearRows();
		b3.commit();
		assertEquals(2, b3.getColumnHeight(0));
		assertEquals(1, b3.getColumnHeight(1));
		assertEquals(0, b3.getColumnHeight(2));
		assertEquals(2, b3.getRowWidth(0));
		assertEquals(1, b3.getRowWidth(1));
		assertEquals(0, b3.getRowWidth(2));
		assertEquals(0, b3.getRowWidth(3));
		assertEquals(0, b3.getRowWidth(4));
		assertEquals(0, b3.getRowWidth(5));
		assertEquals(2, b3.getMaxHeight());
		System.out.print(b3.toString());
	}

	//testing undo after place
	public void testUndo(){
		Board b4 = new Board(3, 6);
		b4.place(pyr1, 0, 0);
		b4.undo();
		assertEquals(0, b4.getColumnHeight(0));
		assertEquals(0, b4.getColumnHeight(1));
		assertEquals(0, b4.getColumnHeight(2));
		assertEquals(0, b4.getRowWidth(1));
		assertEquals(0, b4.getMaxHeight());
	}

	//testing undo after place and clearRows
	public void testUndo2(){
		Board b4 = new Board(3, 6);
		b4.place(pyr1, 0, 0);
		b4.clearRows();
		b4.undo();
		assertEquals(0, b4.getColumnHeight(0));
		assertEquals(0, b4.getColumnHeight(1));
		assertEquals(0, b4.getColumnHeight(2));
		assertEquals(0, b4.getRowWidth(1));
		assertEquals(0, b4.getMaxHeight());
	}

	//testing undo after place, commit and clearRows
	public void testUndo3(){
		Board b4 = new Board(3, 6);
		b4.place(pyr1, 0, 0);
		b4.commit();
		b4.clearRows();
		b4.undo();
		assertEquals(1, b4.getColumnHeight(0));
		assertEquals(2, b4.getColumnHeight(1));
		assertEquals(1, b4.getColumnHeight(2));
		assertEquals(3, b4.getRowWidth(0));
		assertEquals(1, b4.getRowWidth(1));
		assertEquals(2, b4.getMaxHeight());
	}

	//using undo twice
	public void testUndo4(){
		Board b4 = new Board(3, 6);
		b4.place(pyr1, 0, 0);
		b4.commit();
		b4.clearRows();
		b4.undo();
		b4.undo();
		assertEquals(3, b4.getWidth());
		assertEquals(6, b4.getHeight());
		assertEquals(1, b4.getColumnHeight(0));
		assertEquals(2, b4.getColumnHeight(1));
		assertEquals(1, b4.getColumnHeight(2));
		assertEquals(3, b4.getRowWidth(0));
		assertEquals(1, b4.getRowWidth(1));
		assertEquals(2, b4.getMaxHeight());
	}

	//testing broken board: 0x0
	public void testBrokenBoard(){
		Board b4 = new Board(0, 0);
		int res = b4.place(pyr1, 0, 0);
		assertEquals(res, Board.PLACE_OUT_BOUNDS);
		assertEquals(0, b4.getWidth());
		assertEquals(0, b4.getHeight());
	}

	//Broken board: negative dimensions:
	public void testBrokenBoard2(){
		//Notice I would use assertThows but I use JUnit 4 (assertThrows starts at JUnit 5
		try {
			Board b5 = new Board(-1,3);
			assertTrue(false);
		} catch (RuntimeException e) {
			assertTrue(true);
		}
	}

	//Test inspired by JTetris
	public void testBoardJTetris(){
		Board board = new Board(3, 3 + 2);
		board.commit();
		Piece piece = new Piece("0 0  0 1  0 2  1 0");
		int x = (board.getWidth() - piece.getWidth())/2;
		int y = board.getHeight() - piece.getHeight();
		int result = board.place(piece, x, y);
		assertEquals(result, Board.PLACE_OK);
	}

}
