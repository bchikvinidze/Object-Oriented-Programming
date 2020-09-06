import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		HashMap<T, Integer> map = new HashMap<T, Integer>();
		int cnt = 0;
		checkSame(map, a, 1);
		checkSame(map, b, -1);
		for(Integer elem : map.values()){
			if(elem==0) cnt++;
		}
		return cnt;
	}

	/**
	 * increases or decreases count of elements in a hashmap
	 * @param i signifies whether to increase or decrease count.
	 * */
	private static <T> void checkSame(HashMap<T, Integer> map, Collection<T> col, int i){
		for(T elem : col){
			if(map.containsKey(elem)){
				map.put(elem, map.get(elem)+i*1);
			} else {
				map.put(elem, i*1);
			}
		}
	}
}
