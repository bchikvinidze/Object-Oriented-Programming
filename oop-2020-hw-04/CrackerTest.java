import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class CrackerTest {

    /* testing molly case: just SHA value */
    @Test
    public void test1() throws NoSuchAlgorithmException {
        String cr = Cracker.shaVal("molly");
        assertTrue(cr.equals("4181eecbd7a755d19fdf73887c54837cbecf63fd"));
    }
    /* testing a! case: just SHA value */
    @Test
    public void test2() throws NoSuchAlgorithmException {
        String cr = Cracker.shaVal("a!");
        assertTrue(cr.equals("34800e15707fae815d7c90d49de44aca97e2d759"));
    }
    /* testing xyz case: just SHA value */
    @Test
    public void test3() throws NoSuchAlgorithmException {
        String cr = Cracker.shaVal("xyz");
        assertTrue(cr.equals("66b27417d37e024c46526c2f6d358a754fc552f3"));
    }
    /* testing cracking of "a" and also test that built-in hexToArray works*/
    @Test
    public void test4() throws InterruptedException {
        Cracker cr = new Cracker("86f7e437faa5a7fce15d1ddcb9eaeaea377667b8", 1, 8);
        assertTrue(cr.psw().equals("a"));
        assertTrue(Cracker.hexToString(Cracker.hexToArray("86f7e437faa5a7fce15d1ddcb9eaeaea377667b8")).equals("86f7e437faa5a7fce15d1ddcb9eaeaea377667b8"));
    }
    /* testing cracking of "xyz" */
    @Test
    public void test5() throws InterruptedException {
        Cracker cr = new Cracker("66b27417d37e024c46526c2f6d358a754fc552f3", 3, 40);
        assertTrue(cr.psw().equals("xyz"));
    }
}