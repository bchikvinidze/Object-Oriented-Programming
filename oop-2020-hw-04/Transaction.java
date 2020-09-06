// Transaction.java
/*
 (provided code)
 Transaction is just a dumb struct to hold <<< dumb? how dare you
 one transaction. Supports toString.
*/
public class Transaction {
	public int from;
	public int to;
	public int amount;

	/* constructor for transaction */
   	public Transaction(int from, int to, int amount) {
		this.from = from;
		this.to = to;
		this.amount = amount;
	}

	/* Equals method for transaction */
	public boolean equals(Transaction b) {
   		if(b.from == this.from && b.to == this.to && b.amount == this.amount)
   			return true;
   		return false;
	}
}
