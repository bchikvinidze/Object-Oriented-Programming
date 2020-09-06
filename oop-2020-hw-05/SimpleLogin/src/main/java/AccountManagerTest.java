package main.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountManagerTest {
    /* checks basic functionality of an accountManager*/
    @Test
    public void test1(){
        AccountManager am = new AccountManager();
        assertTrue(am.exists("Patrick"));
        assertTrue(!am.exists("Patrick2"));
        assertTrue(am.passwordMatches("Molly", "FloPup"));
        assertTrue(!am.passwordMatches("Molly", "FloPup2"));
    }

    /* checks accountManager after adding new entries*/
    @Test
    public void test2(){
        AccountManager am = new AccountManager();
        am.newAccount("Bubu", "");
        assertTrue(am.exists("Bubu"));
        assertTrue(!am.exists(""));
        assertTrue(am.passwordMatches("Bubu", ""));
    }

    /* checks accountManager after adding 2  same-named accounts with */
    @Test
    public void test3(){
        AccountManager am = new AccountManager();
        am.newAccount("Bubu", "");
        am.newAccount("Bubu", "1");
        assertTrue(am.exists("Bubu"));
        assertTrue(am.passwordMatches("Bubu", ""));
        assertTrue(!am.passwordMatches("Bubu", "1"));
    }
}