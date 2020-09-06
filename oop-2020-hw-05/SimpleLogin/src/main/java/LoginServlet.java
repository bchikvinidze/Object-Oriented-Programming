package main.java;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/Login", name = "Login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountManager manager = (AccountManager) getServletContext().getAttribute("manager");
        String name = req.getParameter("name");
        String psw = req.getParameter("password");
        if(manager.exists(name) && manager.passwordMatches(name, psw))
            req.getRequestDispatcher("welcome.jsp").forward(req, resp);
        else
            req.getRequestDispatcher("try_again.jsp").forward(req, resp);
    }
}
