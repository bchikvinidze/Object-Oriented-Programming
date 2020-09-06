package hw2;
import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4, pyr5;
	private Piece s, sRotated;

	protected void setUp() throws Exception {
		super.setUp();
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		pyr5 = pyr4.computeNextRotation(); //full turn
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
	}
	
	// Here are some sample tests to get you started
	
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}

	//can actually test getWidth and getHeight without implementing rotation functions
	public void testSampleSize2() {
		// Check size of pyr piece
		Piece pieceTest = new Piece(Piece.PYRAMID_STR);
		assertEquals(3, pieceTest.getWidth());
		assertEquals(2, pieceTest.getHeight());

		Piece pieceTest2 = new Piece(Piece.L1_STR);
		assertEquals(2, pieceTest2.getWidth());
		assertEquals(3, pieceTest2.getHeight());

		Piece pieceTest3 = new Piece(Piece.L2_STR);
		assertEquals(2, pieceTest3.getWidth());
		assertEquals(3, pieceTest3.getHeight());

		Piece pieceTest4 = new Piece(Piece.STICK_STR);
		assertEquals(1, pieceTest4.getWidth());
		assertEquals(4, pieceTest4.getHeight());

		Piece pieceTest5 = new Piece(Piece.SQUARE_STR);
		assertEquals(2, pieceTest5.getWidth());
		assertEquals(2, pieceTest5.getHeight());
	}

	//checks width and height of irregular shape, 1x1 shape
	public void testSampleSize3() {
		Piece pieceTest = new Piece("0 0  1 0  2 0  3 0  4 0  2 1  2 2  4 1");
		assertEquals(5, pieceTest.getWidth());
		assertEquals(3, pieceTest.getHeight());

		Piece pieceTest2 = new Piece("0 0");
		assertEquals(1, pieceTest2.getWidth());
		assertEquals(1, pieceTest2.getHeight());
	}
	
	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}

	// Tests skirt without having to implement rotations methods first
	public void testSampleSkirt2() {
		Piece pieceTest1 = new Piece(Piece.PYRAMID_STR);
		Piece pieceTest2 = new Piece(Piece.L1_STR);
		Piece pieceTest3 = new Piece(Piece.SQUARE_STR);
		Piece pieceTest4 = new Piece(Piece.S2_STR);
		Piece pieceTest5 = new Piece(Piece.STICK_STR);

		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pieceTest1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0}, pieceTest2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0}, pieceTest3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 0}, pieceTest4.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0}, pieceTest5.getSkirt()));
	}

	//Test skirt of irregular shape and 1x1 shape
	public void testSampleSkirt3(){
		Piece pieceTest1 = new Piece("0 0  1 0  2 0  3 0  4 0  2 1  2 2  4 1");
		Piece pieceTest2 = new Piece("0 0");

		assertTrue(Arrays.equals(new int[] {0, 0, 0, 0, 0}, pieceTest1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0}, pieceTest2.getSkirt()));
	}

	//check standard cases
	public void testEquals(){
		Piece pieceTest1 = new Piece("0 0  1 0  1 1  1 2"); //L2
		Piece pieceTest2 = new Piece("1 0  0 0  1 2  1 1"); //L2
		assertTrue(pieceTest1.equals(pieceTest2));

		Piece pieceTest3 = new Piece("0 0  1 0  1 1  0 1"); //Square
		Piece pieceTest4 = new Piece("1 0  0 0  0 1  1 1"); //Square
		assertTrue(pieceTest3.equals(pieceTest4));

		Piece pieceTest5 = new Piece("0 0  1 0  1 1  2 1"); //S1
		Piece pieceTest6 = new Piece("1 0  2 0  0 1  1 1"); //S2
		assertTrue(!pieceTest5.equals(pieceTest6));

		Piece pieceTest7 = new Piece("0 0  0 1  0 2  0 3"); //Stick
		Piece pieceTest8 = new Piece("0 0  0 2  0 3  0 1"); //Stick
		assertTrue(pieceTest7.equals(pieceTest8));
	}

	//check irregular case
	public void testEquals2(){
		Piece pieceTest1 = new Piece("0 0  1 0  2 1  2 0");
		Piece pieceTest2 = new Piece("2 1  1 0  2 0  0 0");
		Piece pieceTest3 = new Piece("2 1  1 0  3 0  0 0");
		assertTrue(pieceTest1.equals(pieceTest2));
		assertTrue(!pieceTest1.equals(pieceTest3));
	}

	//check full rotations
	public void testEquals3(){
		assertTrue(pyr1.equals(pyr5));
	}

	//check standard cases (makes no sense to check irregular cases)
	public void testFastRotations(){
		Piece[] pc = Piece.getPieces();
		assertTrue(pc[Piece.STICK].fastRotation().equals(new Piece("0 0  1 0  2 0  3 0")));
		assertTrue(pc[Piece.L1].fastRotation().equals(new Piece("0 0  1 0  2 0  2 1")));
		assertTrue(pc[Piece.L2].fastRotation().equals(new Piece("0 1  1 1  2 1  2 0")));
		assertTrue(pc[Piece.S1].fastRotation().equals(sRotated));
		assertTrue(pc[Piece.SQUARE].fastRotation().equals(new Piece("0 0  1 0  0 1  1 1")));
	}

	//test toBoard on pyramid
	public void testToBoard(){
		boolean[][] smallBoard =
				{
						{true, false},
						{true, true},
						{true, false}
				};
		assertTrue(Arrays.deepEquals(smallBoard, pyr1.toBoard()));

		boolean[][] smallBoard2 =
				{
						{false, true, false},
						{true, true, true}
				};
		assertTrue(Arrays.deepEquals(smallBoard2, pyr2.toBoard()));

		boolean[][] smallBoard3 =
				{
						{false, true},
						{true, true},
						{false, true}
				};
		assertTrue(Arrays.deepEquals(smallBoard3, pyr3.toBoard()));
	}

	//test toBoard on stick
	public void testToBoard2(){
		boolean[][] smallBoard =
				{
						{true, true, true, true}
				};
		Piece stick = new Piece(Piece.STICK_STR);
		assertTrue(Arrays.deepEquals(smallBoard, stick.toBoard()));

	}

	//testing irregular string passing to parseString
	public void testParseString(){
		//Notice I would use assertThows but I use JUnit 4 (assertThrows starts at JUnit 5
		try {
			Piece error = new Piece("This is not acceptable");
		} catch (RuntimeException e) {
			assertTrue(true);
		}
		//this part won't be reached, because test is designed to throw an error and 'catch' will be entered.
	}

	//tests the body of the Piece that is stick
	public void testGetBody(){
		Piece stick = new Piece(Piece.STICK_STR);
		TPoint[] comp = new TPoint[stick.getHeight()];
		comp[0] = new TPoint(0,0);
		comp[1] = new TPoint(0,1);
		comp[2] = new TPoint(0,2);
		comp[3] = new TPoint(0,3);
		TPoint[] body = stick.getBody();
		assertTrue(Arrays.deepEquals(comp, body));
	}

	public void testRotations(){
		Piece stick = Piece.getPieces()[0]; //stick
		Piece stick90 = stick.fastRotation();
		Piece stick180 = stick90.fastRotation();
		Piece stick270 = stick180.fastRotation();
		assertEquals(1,stick270.getHeight());
	}
}
