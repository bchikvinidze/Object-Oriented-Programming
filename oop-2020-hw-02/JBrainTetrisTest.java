package hw2;

import junit.framework.TestCase;

import javax.swing.*;

public class JBrainTetrisTest extends TestCase {

    // tests if JBrainTetris constructor works
    public void testJBrainConstructor(){
        JBrainTetris normalJBrain = new JBrainTetris(16);
        assertEquals(0, normalJBrain.getPieceCnt());
    }

    //test constructor with bad argument
    public void testJBrainConstructor2() throws RuntimeException{
        //I would use assertThrows, but it is not available in JUnit 3.8.
        try {
            JBrainTetris normalJBrain = new JBrainTetris(-1);
            assertTrue(false);
        } catch (RuntimeException e){
            assertTrue(true);
        }
    }
}