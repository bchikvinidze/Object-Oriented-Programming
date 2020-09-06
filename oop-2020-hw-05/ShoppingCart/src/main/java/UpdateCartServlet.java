package main.java;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/* This class is responsible for updating the cart by values entered from
* form and not by "add to cart" button.
* */
@WebServlet(value = "/UpdateCartServlet", name = "UpdateCartServlet")
public class UpdateCartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ShoppingCart cart = (ShoppingCart) req.getSession().getAttribute("cart");
        Set<String> itemsSet = cart.keys();
        String[] newCounts = req.getParameterValues("item");
        int i=0;
        for(String item : itemsSet){
            cart.setCount(item, Integer.parseInt(newCounts[i++]));
        }
        req.getRequestDispatcher("shopping-cart.jsp").forward(req, resp);
    }
}
