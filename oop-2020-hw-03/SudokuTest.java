import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SudokuTest {
    /* Testing hard case: count and solution */
    @Test
    public void test1(){
        Sudoku sud = new Sudoku(Sudoku.hardGrid);
        System.out.println(sud);
        int count = sud.solve();
        String sol = sud.getSolutionText();
        System.out.println(sol);
        assertEquals(1,count);
        assertTrue(sol.equals(" 3 7 5 1 6 2 4 8 9\n" +
                " 8 6 1 4 9 3 5 2 7\n" +
                " 2 4 9 7 8 5 1 6 3\n" +
                " 4 9 3 8 5 7 6 1 2\n" +
                " 7 1 6 2 4 9 8 3 5\n" +
                " 5 2 8 3 1 6 7 9 4\n" +
                " 6 5 7 9 2 1 3 4 8\n" +
                " 1 8 2 5 3 4 9 7 6\n" +
                " 9 3 4 6 7 8 2 5 1\n"));
    }

    /* Testing hard case 3 7 with 7 changed to 0: count and solution, as well as textToGrid method. */
    @Test
    public void test2(){
        String hardGrid2 = "3 0 0 0 0 0 0 8 0\n"+
                "0 0 1 0 9 3 0 0 0\n"+
                "0 4 0 7 8 0 0 0 3\n"+
                "0 9 3 8 0 0 0 1 2\n"+
                "0 0 0 0 4 0 0 0 0\n"+
                "5 2 0 0 0 6 7 9 0\n"+
                "6 0 0 0 2 1 0 4 0\n"+
                "0 0 0 5 3 0 9 0 0\n"+
                "0 3 0 0 0 0 0 5 1\n";
        int[][] grid = new int[][] {{3,0,0,0,0,0,0,8,0},
                                    {0,0,1,0,9,3,0,0,0},
                                    {0,4,0,7,8,0,0,0,3},
                                    {0,9,3,8,0,0,0,1,2},
                                    {0,0,0,0,4,0,0,0,0},
                                    {5,2,0,0,0,6,7,9,0},
                                    {6,0,0,0,2,1,0,4,0},
                                    {0,0,0,5,3,0,9,0,0},
                                    {0,3,0,0,0,0,0,5,1}};
        assertTrue(Arrays.deepToString(Sudoku.textToGrid(hardGrid2)).equals(Arrays.deepToString(grid)));
        Sudoku sud = new Sudoku(hardGrid2);
        int count = sud.solve();
        String sol = sud.getSolutionText();
        assertEquals(6,count);
        assertTrue(sud.getElapsed() >= 0);

        assertTrue(sol.equals(" 3 5 7 1 6 2 4 8 9\n" +
                " 8 6 1 4 9 3 2 7 5\n" +
                " 2 4 9 7 8 5 1 6 3\n" +
                " 4 9 3 8 5 7 6 1 2\n" +
                " 1 7 6 2 4 9 5 3 8\n" +
                " 5 2 8 3 1 6 7 9 4\n" +
                " 6 8 5 9 2 1 3 4 7\n" +
                " 7 1 4 5 3 8 9 2 6\n" +
                " 9 3 2 6 7 4 8 5 1\n"));
    }

    /* Testing medium case: count and solution */
    @Test
    public void test3(){
        Sudoku sud = new Sudoku(Sudoku.mediumGrid);
        System.out.println(sud);
        int count = sud.solve();
        String sol = sud.getSolutionText();
        System.out.println(sol);
        assertEquals(1,count);
        assertTrue(sol.equals(" 5 3 4 6 7 8 9 1 2\n" +
                " 6 7 2 1 9 5 3 4 8\n" +
                " 1 9 8 3 4 2 5 6 7\n" +
                " 8 5 9 7 6 1 4 2 3\n" +
                " 4 2 6 8 5 3 7 9 1\n" +
                " 7 1 3 9 2 4 8 5 6\n" +
                " 9 6 1 5 3 7 2 8 4\n" +
                " 2 8 7 4 1 9 6 3 5\n" +
                " 3 4 5 2 8 6 1 7 9\n"));
    }

    /* Testing easy case: count and solution */
    @Test
    public void test4(){
        Sudoku sud = new Sudoku(Sudoku.easyGrid);
        System.out.println(sud);
        int count = sud.solve();
        String sol = sud.getSolutionText();
        System.out.println(sol);
        assertEquals(1,count);
        assertTrue(sol.equals(" 1 6 4 7 9 5 3 8 2\n" +
                " 2 8 7 4 6 3 9 1 5\n" +
                " 9 3 5 2 8 1 4 6 7\n" +
                " 3 9 1 8 7 6 5 2 4\n" +
                " 5 4 6 1 3 2 7 9 8\n" +
                " 7 2 8 9 5 4 1 3 6\n" +
                " 8 1 9 6 4 7 2 5 3\n" +
                " 6 7 3 5 2 9 8 4 1\n" +
                " 4 5 2 3 1 8 6 7 9\n"));
    }

    /* Testing illegal argument */
    @Test
    public void test5(){
        String hardGridIllegal = "3 a 0 0 0 0 0 8 0\n"+
                "0 0 1 0 9 3 0 0 0\n"+
                "0 4 0 7 8 0 0 0 3\n"+
                "0 9 3 8 0 0 0 1 2\n"+
                "0 0 0 0 4 0 0 0 0\n"+
                "5 2 0 0 0 6 7 9 0\n"+
                "6 0 0 0 2 1 0 4 0\n"+
                "0 0 0 5 3 0 9 0 0\n"+
                "0 3 0 0 0 0 0 5 1\n";
        try {
            Sudoku sud = new Sudoku(hardGridIllegal);
            assertTrue(false); // this should not be reached
        } catch (RuntimeException e){
            assertTrue(true);
        }
    }
}