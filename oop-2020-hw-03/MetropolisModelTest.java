import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class MetropolisModelTest {

    /* test basic functionality of empty model*/
    @Test
    public void test1(){
        MetropolisModel model = new MetropolisModel();
        assertEquals(model.getRowCount(), 0);
        assertEquals(model.getColumnCount(), 3);
        assertTrue(model.getColumnName(0).equals("Metropolis"));
    }

    /* test basic functionality of model: add a standard row*/
    @Test
    public void test2() throws SQLException {
        MetropolisModel model = new MetropolisModel();
        model.addRow("a","b","100");
        assertTrue(model.getValueAt(0,2).equals("100"));
        assertNull(model.getValueAt(0,3));
    }

    /* test adding multiple rows*/
    @Test
    public void test3() throws SQLException {
        MetropolisDB db = new MetropolisDB();
        MetropolisModel model = new MetropolisModel();
        ResultSet rs = db.query("", "", "20000000","Population Larger Than", "");
        model.addRows(rs);
        db.closeConnection();
        assertTrue(model.getValueAt(0,0).equals("Mumbai"));
        assertTrue(model.getValueAt(1,2).equals("21295000"));
    }
}