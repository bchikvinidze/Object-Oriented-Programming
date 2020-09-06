package main.java;

import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    Map<String,String> hashMap;

    /* constructor for AccountManager class
    *  starts with placeholder accounts for testing.
    * */
    public AccountManager(){
        hashMap = new HashMap<String,String>();
        hashMap.put("Patrick", "1234");
        hashMap.put("Molly", "FloPup");
    }

    /* returns true if hashmap contains such account */
    public boolean exists(String account){
        return hashMap.containsKey(account);
    }

    /* returns true of password is correct for given account */
    public boolean passwordMatches(String acct, String psw){
        return hashMap.get(acct).equals(psw);
    }

    /* Adds new account to hashMap */
    public void newAccount(String acct, String psw){
        if(!hashMap.containsKey(acct))
            hashMap.put(acct, psw);
    }

}
