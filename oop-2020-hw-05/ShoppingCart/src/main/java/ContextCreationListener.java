package main.java;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;
import java.util.ArrayList;

/* This class is responsible for handling setting up of server context,
* which includes creating database, and attaching it and product list
* to server context.
* */
@WebListener
public class ContextCreationListener implements ServletContextListener {
    DBConnection db;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
        db = new DBConnection();
        ArrayList<Product> ls = db.toList();
        servletContextEvent.getServletContext().setAttribute("productList", ls);
        servletContextEvent.getServletContext().setAttribute("database", db);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            db.closeConnection();
        } catch (SQLException e) {e.printStackTrace();}
    }
}
