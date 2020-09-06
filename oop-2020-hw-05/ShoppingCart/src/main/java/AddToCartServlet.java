package main.java;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* This servlet is responsible for handling adding of a
* new item to the cart.
* */
@WebServlet(value = "/AddToCartServlet", name = "AddToCartServlet")
public class AddToCartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ShoppingCart cart = (ShoppingCart) req.getSession().getAttribute("cart");
        cart.addItem(req.getParameter("productID"));
        req.getRequestDispatcher("shopping-cart.jsp").forward(req, resp);
    }
}
