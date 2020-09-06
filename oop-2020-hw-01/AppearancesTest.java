import junit.framework.TestCase;

import java.util.*;

public class AppearancesTest extends TestCase {
	// utility -- converts a string to a list with one
	// elem for each char.
	private List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<s.length(); i++) {
			list.add(String.valueOf(s.charAt(i)));
			// note: String.valueOf() converts lots of things to string form
		}
		return list;
	}
	
	public void testSameCount1() {
		List<String> a = stringToList("abbccc");
		List<String> b = stringToList("cccbba");
		assertEquals(3, Appearances.sameCount(a, b));
	}
	
	public void testSameCount2() {
		// basic List<Integer> cases
		List<Integer> a = Arrays.asList(1, 2, 3, 1, 2, 3, 5);
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 9, 9, 1)));
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1, 1)));
	}

	public void testSameCount3() {
		// check zero elements
		List<Integer> a = Arrays.asList();
		assertEquals(0, Appearances.sameCount(a, Arrays.asList()));
		assertEquals(0, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1)));
	}

	public void testSameCount4() {
		// check more complex types with one element
		HashSet<Integer> c1 = new HashSet<Integer>();
		//Set<Integer>> c2 = c1;
		List<HashSet<Integer>> a = Arrays.asList(c1);
		List<HashSet<Integer>> b = Arrays.asList(c1);
		assertEquals(1, Appearances.sameCount(a, b));
	}

	public void testSameCount5() {
		// check more complex types with multiple same elements
		HashSet<Integer> c1 = new HashSet<Integer>();
		HashSet<Integer> c2 = c1;
		List<HashSet<Integer>> a = Arrays.asList(c1, c2);
		List<HashSet<Integer>> b = Arrays.asList(c1, c2);
		assertEquals(1, Appearances.sameCount(a, b));
	}

	public void testSameCount6() {
		// check more complex types with different elements
		HashSet<Integer> c1 = new HashSet<Integer>();
		HashSet<Integer> c2 = new HashSet<Integer>();
		c2.add(1);
		List<HashSet<Integer>> a = Arrays.asList(c1);
		List<HashSet<Integer>> b = Arrays.asList(c2);
		assertEquals(0, Appearances.sameCount(a, b));
	}
}
