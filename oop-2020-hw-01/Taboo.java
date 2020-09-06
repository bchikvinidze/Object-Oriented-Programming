
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {
	private List<T> rules;
	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		this.rules = rules;
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		Set<T> res = new HashSet<T>();
		if(rules.size()<2) return Collections.emptySet();
		for(int i=0; i<(rules.size()-1); i++){
			if(rules.get(i) != null && rules.get(i).equals(elem)) {
				res.add(rules.get(i+1));
			}
		}
		return res;
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		if(rules.size()<2 || list.size()<2) return;
		for(int i=0; i<(list.size()-1); i++){
			if(noFollow(list.get(i)).contains(list.get(i+1))){
				list.remove(i+1);
				i--;
			}
		}
	}
}
