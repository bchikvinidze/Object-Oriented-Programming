import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class BankTest {
    public static final int ACCOUNTS = 20;
    /* test easiest case */
    @Test
    public void test1() throws FileNotFoundException, InterruptedException {
            Bank b = new Bank(1000);
            b.processFile("small.txt", 4);
            b.printStates();
            for(int i=0; i<ACCOUNTS; i+=2)
                assertEquals(b.AccountVal(i), 999);
            for(int i=1; i<ACCOUNTS; i+=2)
                assertEquals(b.AccountVal(i),  1001);
            for(int i=0; i<ACCOUNTS; i++)
                assertEquals(b.AccountTransCnt(i),1);
    }

    /* Test another file case (5k) */
    @Test
    public void test2() throws FileNotFoundException, InterruptedException {
            Bank b = new Bank(2000);
            b.processFile("5k.txt", 21);
            b.printStates();
            for(int i=0; i<ACCOUNTS; i++) {
                assertEquals(b.AccountVal(i), 1000);
                assertEquals(b.AccountVal(i), 1000);
            }
            assertTrue(b.AccountTransCnt(0) == 518);
            assertTrue(b.AccountTransCnt(10) == 498);
            assertTrue(b.AccountTransCnt(19) == 504);
    }

    /* Test another file case (100k) */
    @Test
    public void test3() throws FileNotFoundException, InterruptedException {
            Bank b = new Bank(2000);
            b.processFile("100k.txt", 21);
            b.printStates();
        for(int i=0; i<ACCOUNTS; i++) {
            assertEquals(b.AccountVal(i), 1000);
            assertEquals(b.AccountVal(i), 1000);
        }
        assertTrue(b.AccountTransCnt(0) == 10360);
        assertTrue(b.AccountTransCnt(10) == 9960);
        assertTrue(b.AccountTransCnt(19) == 10080);
    }

}