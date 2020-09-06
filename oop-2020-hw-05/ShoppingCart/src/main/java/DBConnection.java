package main.java;

import java.sql.*;
import java.util.ArrayList;

public class DBConnection {
    private Connection connection;

    /*
     * Query for preparing database and creating table, inserting data.
     */
    private PreparedStatement statementSetup() throws SQLException {
        return connection.prepareStatement("USE mysql;\n" +
                "DROP TABLE IF EXISTS products;\n" +
                " -- remove table if it already exists and start from scratch\n" +
                "\n" +
                "CREATE TABLE products (\n" +
                "\tproductid CHAR(6),\n" +
                "    name CHAR(64),\n" +
                "    imagefile CHAR(64),\n" +
                "    price DECIMAL(6,2)\n" +
                ");\n" +
                "\n" +
                "INSERT INTO products VALUES\n" +
                "\t(\"HC\",\"Classic Hoodie\",\"Hoodie.jpg\",40),\n" +
                "    (\"HLE\", \"Limited Edition Hood\",\"LimitedEdHood.jpg\",54.95),\n" +
                "\t(\"TC\", \"Classic Tee\",\"TShirt.jpg\",13.95),\n" +
                "\t(\"TS\",\"Seal Tee\",\"SealTShirt.jpg\",19.95),\n" +
                "\t(\"TLJa\",\"Japanese Tee\",\"JapaneseTShirt.jpg\",17.95),\n" +
                "\t(\"TLKo\",\"Korean Tee\",\"KoreanTShirt.jpg\",17.95),\n" +
                "\t(\"TLCh\",\"Chinese Tee\",\"ChineseTShirt.jpg\",17.95),\n" +
                "\t(\"TLHi\",\"Hindi Tee\",\"HindiTShirt.jpg\",17.95),\n" +
                "\t(\"TLAr\",\"Arabic Tee\",\"ArabicTShirt.jpg\",17.95),\n" +
                "\t(\"TLHe\",\"Hebrew Tee\",\"HebrewTShirt.jpg\",17.95),\n" +
                "\t(\"AKy\",\"Keychain\",\"Keychain.jpg\",2.95),\n" +
                "\t(\"ALn\",\"Lanyard\",\"Lanyard.jpg\",5.95),\n" +
                "\t(\"ATherm\",\"Thermos\",\"Thermos.jpg\",19.95),\n" +
                "\t(\"AMinHm\",\"Mini Football Helmet\",\"MiniHelmet.jpg\",29.95)");
    }

    /*
     * creates and connects to a database
     */
    public DBConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306?allowMultiQueries=true",
                    "root",
                    "");
            Statement useDbStm = connection.createStatement();
            PreparedStatement ps = statementSetup();
            ps.execute();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Closes connection to the database
     */
    public void closeConnection() throws SQLException {
        connection.close();
    }

    /* converts database records to a list */
    public ArrayList<Product> toList() throws SQLException {
        ArrayList<Product> result = new ArrayList<Product>();
        Statement queryStm = connection.createStatement();
        ResultSet rs = queryStm.executeQuery("SELECT * FROM products;");
        while (rs.next()) {
            Product newItem = new Product(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4));
            result.add(newItem);
        }
        return result;
    }

    /* returns product class from knowing it's name */
    public Product getProduct(String productName) throws SQLException {
        Statement queryStm = connection.createStatement();
        ResultSet rs = queryStm.executeQuery("SELECT * FROM products WHERE productid = \"" + productName + "\"");
        if(rs.next())
            return new Product(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4));
        return null;
    }

}
