import org.junit.Test;

import static org.junit.Assert.*;

public class JCountTest {

    /* tests basic functionality of the start button*/
    @Test
    public void test1() throws InterruptedException {
        JCount jcount = new JCount();
        jcount.startClick();
        Thread.sleep(1000);
        int labelValue = Integer.parseInt(jcount.labelVal());
        assertTrue( labelValue > 0);
    }

    /* tests basic functionality of the stop button*/
    @Test
    public void test2() throws InterruptedException {
        JCount jcount = new JCount();
        jcount.startClick();
        Thread.sleep(100);
        jcount.stopClick();
        Thread.sleep(2000);
        assertTrue(!(jcount.workerAlive()));
    }
}