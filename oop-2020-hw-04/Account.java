// Account.java

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/*
 Simple, thread-safe Account class encapsulates
 a balance and a transaction count.
*/
public class Account {
	private int id;
	private int balance;
	private int transactions;

	// It may work out to be handy for the account to
	// have a pointer to its Bank.
	// (a suggestion, not a requirement)
	private Bank bank;  

	/* constructor */
	public Account(Bank bank, int id, int balance) {
		this.bank = bank;
		this.id = id;
		this.balance = balance;
		this.transactions = 0;
	}

	/* synchronized method for changing balance */
	public synchronized void modifyBalance(int amount){
		transactions++;
		balance += amount;
	}

	/* getter method for balance*/
	public int balance(){
		return balance;
	}

	/* getter method for transactions count*/
	public int transCount(){ return transactions; }

	/* converting to String */
	public String toString(){ return "acct:" + id + " bal:" + balance + " trans:" + transactions;}
}
