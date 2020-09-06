import java.math.BigInteger;
import java.sql.*;

/* Database class for Metropolis*/
public class MetropolisDB {
    private Connection connection;

    /*
    * Query for preparing database and creating table, inserting data
    */
    private String setupQuery(){
        return "USE mysql;\n" +
                "DROP TABLE IF EXISTS metropolises;\n" +
                " -- remove table if it already exists and start from scratch\n" +
                "CREATE TABLE metropolises (\n" +
                "    metropolis CHAR(64),\n" +
                "    continent CHAR(64),\n" +
                "    population BIGINT\n" +
                ");\n" +
                "INSERT INTO metropolises VALUES\n" +
                "\t(\"Mumbai\",\"Asia\",20400000),\n" +
                "        (\"New York\",\"North America\",21295000),\n" +
                "\t(\"San Francisco\",\"North America\",5780000),\n" +
                "\t(\"London\",\"Europe\",8580000),\n" +
                "\t(\"Rome\",\"Europe\",2715000),\n" +
                "\t(\"Melbourne\",\"Australia\",3900000),\n" +
                "\t(\"San Jose\",\"North America\",7354555),\n" +
                "\t(\"Rostov-on-Don\",\"Europe\",1052000)\n;";
    }

    /*
    * creates and connects to a database
    */
    public MetropolisDB(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost?allowMultiQueries=true",
                    "root",
                    "");
            Statement useDbStm = connection.createStatement();
            useDbStm.execute(setupQuery());
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

    /*
    * Inserts data into the database
    */
    public void insert(String metropolis, String continent, String population) throws SQLException {
        Statement insertStatement = connection.createStatement();
        insertStatement.executeUpdate("INSERT INTO metropolises VALUES\n" +
                "(\""+metropolis+"\",\""+continent+"\",\""+population+"\");");
    }

    /*
     * Adds "AND" keyword after parts of Query for multiple conditions in WHERE clause
     */
    private void queryAndClause(queryContents r){
        if(!r.getMetropolis().equals("") & !r.getContinent().equals("") & !r.getPopulation().equals("")){
            r.setMetropolis(r.getMetropolis() + "AND ");
            r.setContinent(r.getContinent() + "AND ");
        } else if(!r.getMetropolis().equals("") & (!r.getContinent().equals("") | !r.getPopulation().equals("")))
            r.setMetropolis(r.getMetropolis() + "AND ");
        else if(!r.getContinent().equals("") & !r.getPopulation().equals(""))
            r.setContinent(r.getContinent() + "AND ");
    }

    /*
    * Make queryContents contain contents of a WHERE clause
    */
    public queryContents queryWhereClause(String metropolis, String continent, String population,
                                  String populFilter, String matchFilter){
        queryContents r = new queryContents("", "","");
        if(matchFilter.equals("Exact Match")){
            if(!metropolis.equals("")) r.setMetropolis("metropolis LIKE '" + metropolis + "' ");
            if(!continent.equals(""))  r.setContinent("continent LIKE '" + continent + "' ");
        } else if(matchFilter.equals("Partial Match")){
            if(!metropolis.equals("")) r.setMetropolis("metropolis LIKE '%" + metropolis + "%' ");
            if(!continent.equals("")) r.setContinent("continent LIKE '%" + continent + "%' ");
        }
        if(!population.equals("")) {
            if (populFilter.equals("Population Larger Than")) r.setPopulation(" population > " + population);
            else r.setPopulation(" population < " + population);
        }
        queryAndClause(r);
        return r;
    }

    /*
    * makes a query depending on inputs and returns the result.
    */
    public ResultSet query(String metropolis, String continent, String population,
                           String populFilter, String matchFilter) throws SQLException {
        queryContents r = queryWhereClause(metropolis, continent, population, populFilter, matchFilter);
        String queryText = "SELECT * FROM metropolises";
        if(!metropolis.equals("") || !continent.equals("") || !population.equals(""))
            queryText += " WHERE ";
        queryText = queryText + r.getMetropolis() + r.getContinent() + r.getPopulation() + ";";
        System.out.println(queryText);
        Statement queryStm = connection.createStatement();
        ResultSet result = queryStm.executeQuery(queryText);
        return result;
    }

    /*
    * Inner class representing one query. Consists
    * of three components: filters for metropolis, continent and population
    */
    public class queryContents{
        private String metropolis, continent, population;

        public queryContents(String metropolis, String continent, String population){
            this.metropolis = metropolis;
            this.continent = continent;
            this.population = population;
        }

        /* Getter methods */
        public String getMetropolis(){ return metropolis;}

        public String getContinent(){ return continent;}

        public String getPopulation(){ return population;}

        /* setter methods */
        public void setPopulation(String newPopulation){ this.population = newPopulation;}

        public void setContinent(String newContinent){ this.continent = newContinent;}

        public void setMetropolis(String newtMetropolis){ this.metropolis = newtMetropolis;}
    }
}
