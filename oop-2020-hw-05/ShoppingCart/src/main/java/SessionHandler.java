package main.java;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/* This class handles creation and destruction of new sessions. */
@WebListener
public class SessionHandler implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        ShoppingCart cart = new ShoppingCart();
        httpSessionEvent.getSession().setAttribute("cart", cart);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {}
}
