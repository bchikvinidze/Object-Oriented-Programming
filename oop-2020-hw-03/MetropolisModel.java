import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  MetropolisModel class
 */
public class MetropolisModel extends AbstractTableModel{
    private List<String> colNames;
    private List<List<String>> data;

    public MetropolisModel(){
        colNames = new ArrayList<String>();
        colNames.add("Metropolis");
        colNames.add("Continent");
        colNames.add("Population");
        data = new ArrayList<>();
    }

    /**
     * getter of row count
     * @return row count
     */
    @Override
    public int getRowCount() {
        return data.size();
    }

    /**
     * getter of column count
     * @return column count
     */
    @Override
    public int getColumnCount() {
        return colNames.size();
    }

    /**
     * getter for column names
     * @param i row index
     * @return Name of curret column
     */
    @Override
    public String getColumnName(int i) {
        return colNames.get(i);
    }

    /**
     * method for getting value at index (i,j) of data
     * @param i row index
     * @param j column index
     * @return either null of String containing content of one of query's columns
     */
    @Override
    public Object getValueAt(int i, int j) {
        List<String> row = data.get(i);
        if(j < row.size())
            return row.get(j);
        return null;
    }

    /**
     * Converts ResultSet object to the type of ArrayList of Strings.
     * @param r ResultSet
     * @return ArrayLit<String>
     * @throws SQLException
     */
    public ArrayList<String> resultSetToArrayList(ResultSet r) throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        result.add(r.getString(1));
        result.add(r.getString(2));
        result.add(r.getString(3));
        return result;
    }

    /**
     * given a ResultSet, adds all of them to the table as strings
     * @param r ResultSet which was returned by querying database
     * @throws SQLException
     */
    public void addRows(ResultSet r) throws SQLException {
        removeRows();
        while (r.next()) {
            List<String> row = resultSetToArrayList(r);
            data.add(row);
            int index = data.size() - 1;
            fireTableDataChanged();
        }
    }

    /**
     * Clears the data
     */
    public void removeRows(){ data = new ArrayList<>(); }

    /**
     * Given three strings adds them onto one row of the table
     * @param s1 metropolis string
     * @param s2 continent string
     * @param s3 population string
     * @return index of insertion, which will always be zero
     * @throws SQLException
     */
    public int addRow(String s1, String s2, String s3) throws SQLException {
        removeRows();
        List<String> row = new ArrayList<>();
        row.add(s1);
        row.add(s2);
        row.add(s3);
        data.add(row);
        this.fireTableDataChanged();
        return 0;
    }
}
