// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	private CountDownLatch latch;
	private final int numWorkers;
	private final int passwordLen;
	private final String passwordSha;
	private List<String> crackedList;
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}

	/* Constructor for Cracker class */
	public Cracker(String targ, int len, int num) throws InterruptedException {
		numWorkers = num;
		passwordLen = len;
		passwordSha = targ;
		latch = new CountDownLatch(num);
		crackedList = new ArrayList<String>();
		for(int i=0; i<num; i++){
			new Worker(i).start();
		}
		latch.await();
		//aq unda davajoino mgoni xom
		System.out.println("All done!");
	}

	/* returns first found cracked password */
	public String psw(){
		return crackedList.get(0);
	}

	/* Returns SHA representation of the string */
	public static String shaVal(String psw) throws NoSuchAlgorithmException {
		String result = "";
		MessageDigest md = MessageDigest.getInstance("SHA");
		byte[] res = md.digest(psw.getBytes());
		result = hexToString(res);
		return result;
	}

	/* Main takes arguments and creates Cracker based on them. */
	/*public static void main(String[] args) throws InterruptedException {
		if (args.length < 2) {
			System.out.println(Cracker.shaVal(args[0]));
		} else {
			String targ = args[0];
			int len = Integer.parseInt(args[1]);
			int num = 1;
			if (args.length > 2) {
				num = Integer.parseInt(args[2]);
			}
			// a! 34800e15707fae815d7c90d49de44aca97e2d759
			// xyz 66b27417d37e024c46526c2f6d358a754fc552f3
			Cracker cracker =  new Cracker(targ,len,num);
		}
	}*/

	/* Inner helper class for running multiple threads */
	private class Worker extends Thread{
		private int id;
		public Worker(int id){
			this.id=id;
		}

		/* Does the brute force work of finding password by permutations*/
		@Override
		public void run(){
			for(int i=CHARS.length/numWorkers*id; i<CHARS.length/numWorkers*(id+1); i++){
				char[] word = new char[passwordLen];
				word[0] = CHARS[i];
				try {
					permutation(word, 1);
				} catch (NoSuchAlgorithmException e) {e.printStackTrace();}
			}
			latch.countDown();
		}

		/* Permutation algorithm: for each index generate all possible chars of password */
		private void permutation(char[] perm, int index) throws NoSuchAlgorithmException {
			if(index == passwordLen){
				MessageDigest md = MessageDigest.getInstance("SHA");
				byte[] res = md.digest(new String(perm).getBytes());
				if(Cracker.hexToString(res).equals(passwordSha)) {
					String psw = new String(perm);
					System.out.println(new String(psw));
					crackedList.add(psw);
				}
			} else {
				for(int i=0; i<CHARS.length; i++){
					perm[index] = CHARS[i];
					permutation(perm, index+1);
				}
			}
		}

	}

}
