import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		int len = str.length();
		if(len < 2) return len;
		int max = 1;
		int cur = 1;
		for(int i=1; i<len; i++){
			if(str.charAt(i) == str.charAt(i-1)) {
				cur += 1;
				if(cur > max) max = cur;
			} else {
				cur = 1;
			}
		}
		return max;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		int pos = 0;
		String res = "";
		while(pos < str.length()){
			char cur = str.charAt(pos);
			if(!Character.isDigit(cur)) {
				res += cur;
			} else {
				if(pos != (str.length()-1)){
					int add = (int) (cur - '0');
					for(int i=0;i<add;i++){
						res += str.charAt(pos+1);
					}
				}
			}
			pos += 1;
		}
		return res;
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		HashSet<String> hset = new HashSet<String>();
		int alen = a.length();
		int blen = b.length();
		if(len > alen || len > blen) return false;
		for(int i=0; i<=(alen-len); i++){
			String sub = a.substring(i,i+len);
			hset.add(sub);
		}
		for(int i=0; i<=(blen-len); i++){
			String sub = b.substring(i,i+len);
			if(hset.contains(sub)) return true;
		}
		return false;
	}
}
