// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Bank {
	public static final int ACCOUNTS = 20;	 // number of accounts
	public static final int INIT_BALANCE = 1000;	 // number of accounts
	public static final int CAPACITY = 2000;
	private BlockingQueue<Transaction> queue;
	private List<Account> accounts;
	private List<Worker> workers;
	private CountDownLatch latch;
	private final Transaction nullTrans = new Transaction(-1,0,0);
	private int numThreads;

	public Bank(int numThreads){
		this.numThreads = numThreads;
		queue = new ArrayBlockingQueue<Transaction>(CAPACITY);
		initAccounts();
	}

	/*
	 Reads transaction data (from/to/amt) from a file for processing.
	 (provided code)
	 */
	public void readFile(String file) throws InterruptedException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			// Use stream tokenizer to get successive words from file
			StreamTokenizer tokenizer = new StreamTokenizer(reader);
			while (true) {
				int read = tokenizer.nextToken();
				if (read == StreamTokenizer.TT_EOF) break;  // detect EOF
				int from = (int)tokenizer.nval;
				tokenizer.nextToken();
				int to = (int)tokenizer.nval;
				tokenizer.nextToken();
				int amount = (int)tokenizer.nval;
				Transaction t = new Transaction(from, to, amount);
				queue.put(t);
			}
			reader.close();
		}
		catch (Exception e) {e.printStackTrace();}
	}

	/*
	 Processes one file of transaction data
	 -fork off workers
	 -read file into the buffer
	 -wait for the workers to finish
	*/
	public void processFile(String file, int numWorkers) throws InterruptedException, FileNotFoundException {
		initWorkers();
		readFile(file);
		latch = new CountDownLatch(numThreads);
		addFinalTransactions();
		latch.await();
		for(int i=0; i<numThreads; i++){
			workers.get(i).join();
		}
	}

	/* adds placeholder transactions to indicate the end of worker's job */
	private void addFinalTransactions() throws InterruptedException {
		for (int i = 0; i < numThreads; i++)
			queue.put(new Transaction(-1, 0, 0));
	}

	/* initializes accounts for Bank */
	private void initAccounts(){
		accounts = new ArrayList<Account>(ACCOUNTS);
		for(int i=0; i<ACCOUNTS; i++){
			accounts.add(new Account(this, i, INIT_BALANCE));
		}
	}

	/* iniializes and starts worker threads */
	private void initWorkers(){
		workers = new ArrayList<Worker>(numThreads);
		for(int i=0; i<numThreads; i++){
			workers.add(i, new Worker());
			workers.get(i).start();
		}
	}

	/* prints final states to the console */
	public void printStates(){
		System.out.println(accounts.toString());
	}

	/* Returns Account's value*/
	public int AccountVal(int i){
		return accounts.get(i).balance();
	}

	/* Returns Account's transaction count*/
	public int AccountTransCnt(int i){
		return accounts.get(i).transCount();
	}
	/*
	 Looks at commandline args and calls Bank processing.
	*/
	public static void main(String[] args) throws InterruptedException, FileNotFoundException {
		// deal with command-lines args
		if (args.length == 0) {
			System.out.println("Args: transaction-file [num-workers [limit]]");
			System.exit(1);
		}
		
		String file = args[0];
		
		int numWorkers = 1;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}
		
		Bank bank = new Bank(numWorkers);
		bank.processFile(file, numWorkers);
		bank.printStates();
	}

	/* Inner class doing all the work (thus called worker) of
	 * actually performing the transactions. */
	private class Worker extends Thread{
		@Override
		public void run(){
			while(true){
				try {
					Transaction t = queue.take();
					if(t.equals(nullTrans)) break;
					Account from = accounts.get(t.from);
					from.modifyBalance(-t.amount);
					Account to = accounts.get(t.to);
					to.modifyBalance(t.amount);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			latch.countDown();
		}
	}
}

