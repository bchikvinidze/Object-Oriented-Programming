import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class MetropolisDBTest {
    private MetropolisDB db;

    @Before
    public void setup(){
        db = new MetropolisDB();
    }

    @After
    public void close() throws SQLException {
        db.closeConnection();
    }

    /* Testing query on original table without filters*/
    @Test
    public void test1() throws SQLException {
        ResultSet rs = db.query("", "", "","", "");
        String res = "";
        while(rs.next()) {
            res += rs.getString(1) +","+ rs.getString(2) +","+ rs.getString(3) +"\n";
        }
        assertTrue(res.equals("Mumbai,Asia,20400000\n" +
                "New York,North America,21295000\n" +
                "San Francisco,North America,5780000\n" +
                "London,Europe,8580000\n" +
                "Rome,Europe,2715000\n" +
                "Melbourne,Australia,3900000\n" +
                "San Jose,North America,7354555\n" +
                "Rostov-on-Don,Europe,1052000\n"));
    }

    /* Testing query on table with regular record added */
    @Test
    public void test2() throws SQLException {
        db.insert("a", "b", "1");
        ResultSet rs = db.query("", "", "","", "");
        String res = "";
        while(rs.next()) {
            res += rs.getString(1) +","+ rs.getString(2) +","+ rs.getString(3) +"\n";
        }
        assertTrue(res.equals("Mumbai,Asia,20400000\n" +
                "New York,North America,21295000\n" +
                "San Francisco,North America,5780000\n" +
                "London,Europe,8580000\n" +
                "Rome,Europe,2715000\n" +
                "Melbourne,Australia,3900000\n" +
                "San Jose,North America,7354555\n" +
                "Rostov-on-Don,Europe,1052000\n" +
                "a,b,1\n"));
    }

    /* Testing query on table with partially empty record added*/
    @Test
    public void test3() throws SQLException {
        db.insert("", "", "10000000000000000");
        ResultSet rs = db.query("", "", "","", "");
        String res = "";
        while(rs.next()) {
            res += rs.getString(1) +","+ rs.getString(2) +","+ rs.getString(3) +"\n";
        }
        assertTrue(res.equals("Mumbai,Asia,20400000\n" +
                "New York,North America,21295000\n" +
                "San Francisco,North America,5780000\n" +
                "London,Europe,8580000\n" +
                "Rome,Europe,2715000\n" +
                "Melbourne,Australia,3900000\n" +
                "San Jose,North America,7354555\n" +
                "Rostov-on-Don,Europe,1052000\n" +
                ",,10000000000000000\n"));
    }

    /* Testing query on table with filters: metropolis and continent exact match*/
    @Test
    public void test4() throws SQLException {
        ResultSet rs = db.query("Mumbai", "Asia", "","", "Exact Match");
        String res = "";
        while(rs.next()) {
            res += rs.getString(1) +","+ rs.getString(2) +","+ rs.getString(3) +"\n";
        }
        assertTrue(res.equals("Mumbai,Asia,20400000\n"));
    }

    /* Testing query on table with filters: metropolis and continent partial match*/
    @Test
    public void test5() throws SQLException {
        ResultSet rs = db.query("o", "e", "","", "Partial Match");
        String res = "";
        while(rs.next()) {
            res += rs.getString(1) +","+ rs.getString(2) +","+ rs.getString(3) +"\n";
        }
        assertTrue(res.equals("New York,North America,21295000\n" +
                "San Francisco,North America,5780000\n" +
                "London,Europe,8580000\n" +
                "Rome,Europe,2715000\n" +
                "San Jose,North America,7354555\n" +
                "Rostov-on-Don,Europe,1052000\n"));
    }

    /* Testing query on table with population filter - Larger than */
    @Test
    public void test6() throws SQLException {
        ResultSet rs = db.query("", "", "20000000","Population Larger Than", "");
        String res = "";
        while(rs.next()) {
            res += rs.getString(1) +","+ rs.getString(2) +","+ rs.getString(3) +"\n";
        }
        assertTrue(res.equals("Mumbai,Asia,20400000\n" +
                "New York,North America,21295000\n"));
    }

    /* Testing query on table with special characters on both exact match and partial match*/
    @Test
    public void test7() throws SQLException {
        ResultSet rs1 = db.query("San%", "", "","", "Exact Match");
        ResultSet rs2 = db.query("an%", "", "","", "Partial Match");
        String res1 = "";
        while(rs1.next()) {
            res1 += rs1.getString(1) +","+ rs1.getString(2) +","+ rs1.getString(3) +"\n";
        }
        String res2 = "";
        while(rs2.next()) {
            res2 += rs2.getString(1) +","+ rs2.getString(2) +","+ rs2.getString(3) +"\n";
        }
        assertTrue(res2.equals("San Francisco,North America,5780000\n" +
                "San Jose,North America,7354555\n"));
        assertTrue(res1.equals("San Francisco,North America,5780000\n" +
                "San Jose,North America,7354555\n"));
    }
}