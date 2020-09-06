// TabooTest.java
// Taboo class tests -- nothing provided.

import java.util.*;

import junit.framework.TestCase;

public class TabooTest extends TestCase {

    public void testNoFollow1(){
        /* case from handout*/
        List<String> rules = new ArrayList<String>();
        rules.add("a");
        rules.add("c");
        rules.add("a");
        rules.add("b");
        Taboo<String> tb = new Taboo<String>(rules);

        Set<String> res = new HashSet<String>();
        res.add("c");
        res.add("b");
        assertEquals(res, tb.noFollow("a"));
    }

    public void testNoFollow2(){
        /* empty rules */
        List<String> rules = new ArrayList<String>();
        Taboo<String> tb = new Taboo<String>(rules);

        Set<String> res = new HashSet<String>();
        assertEquals(res, tb.noFollow("a"));
    }

    public void testNoFollow3(){
        /* not empty rules but not concerning input*/
        List<String> rules = new ArrayList<String>();
        rules.add("k");
        rules.add("c");
        rules.add("q");
        rules.add("a");
        Taboo<String> tb = new Taboo<String>(rules);

        Set<String> res = new HashSet<String>();
        assertEquals(res, tb.noFollow("a"));
    }

    public void testNoFollow4(){
        /* same input and output*/
        List<String> rules = new ArrayList<String>();
        rules.add("a");
        rules.add("a");
        Taboo<String> tb = new Taboo<String>(rules);

        Set<String> res = new HashSet<String>();
        res.add("a");
        assertEquals(res, tb.noFollow("a"));
    }

    public void testReduce1(){
        // one more case from handout
        List<String> rules = new ArrayList<String>();
        rules.add("a");
        rules.add("c");
        rules.add("a");
        rules.add("b");
        Taboo<String> tb = new Taboo<String>(rules);

        List<String> toReduce = new ArrayList<String>();
        toReduce.add("a");
        toReduce.add("c");
        toReduce.add("b");
        toReduce.add("x");
        toReduce.add("c");
        toReduce.add("a");

        List<String> res = new ArrayList<String>();
        res.add("a");
        res.add("x");
        res.add("c");

        tb.reduce(toReduce);
        assertEquals(res, toReduce);
    }

    public void testReduce2(){
        //edge case: same element over and over
        List<String> rules = new ArrayList<String>();
        rules.add("a");
        rules.add("a");
        Taboo<String> tb = new Taboo<String>(rules);

        List<String> toReduce = new ArrayList<String>();
        toReduce.add("a");
        toReduce.add("a");
        toReduce.add("a");
        toReduce.add("a");
        toReduce.add("a");
        toReduce.add("a");

        List<String> res = new ArrayList<String>();
        res.add("a");

        tb.reduce(toReduce);
        assertEquals(res, toReduce);
    }

    public void testReduce3(){
        //edge case: only one element in rules
        List<String> rules = new ArrayList<String>();
        rules.add("a");
        Taboo<String> tb = new Taboo<String>(rules);

        List<String> toReduce = new ArrayList<String>();
        toReduce.add("a");
        toReduce.add("b");

        List<String> res = new ArrayList<String>();
        res.add("a");
        res.add("b");

        tb.reduce(toReduce);
        assertEquals(res, toReduce);
    }

    public void testReduce4(){
        //empty list to reduce
        List<String> rules = new ArrayList<String>();
        rules.add("a");
        Taboo<String> tb = new Taboo<String>(rules);

        List<String> toReduce = new ArrayList<String>();

        List<String> res = new ArrayList<String>();

        tb.reduce(toReduce);
        assertEquals(res, toReduce);
    }

    public void testReduce5(){
        //using null in rules
        List<String> rules = new ArrayList<String>();
        rules.add("a");
        rules.add("b");
        rules.add(null);
        rules.add("c");
        rules.add("d");
        Taboo<String> tb = new Taboo<String>(rules);

        List<String> toReduce = new ArrayList<String>();
        toReduce.add("b");
        toReduce.add("c");
        toReduce.add("b");
        toReduce.add(null);
        toReduce.add("a");
        toReduce.add("b");

        List<String> res = new ArrayList<String>();
        res.add("b");
        res.add("c");
        res.add("b");
        res.add("a");

        tb.reduce(toReduce);
        assertEquals(res, toReduce);
    }
}
