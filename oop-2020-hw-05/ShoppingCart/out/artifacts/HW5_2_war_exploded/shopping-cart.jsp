<%@ page import="main.java.ShoppingCart" %>
<%@ page import="java.util.Set" %>
<%@ page import="main.java.DBConnection" %>
<%@ page import="main.java.Product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    double total = 0;
    ShoppingCart curCart = (ShoppingCart) session.getAttribute("cart");
    Set<String> keys = curCart.keys();
    DBConnection db = (DBConnection) application.getAttribute("database");
%>
<html>
<head>
    <meta charset=\"UTF-8\" />
    <title>Shopping Cart</title>
</head>
<body>

<h1>Shopping Cart</h1>
</br>
</br>

<ul>
    <form action="UpdateCartServlet">
       <%
           for(String elem : keys){
               Product curProd = db.getProduct(elem);
               total += curProd.getPrice() * curCart.getCnt(curProd.getId());
               out.print("<li> <input type=\"number\" id=" + curProd.getId() + " name=\"item\" value=" + curCart.getCnt(elem) + "> " +
                       curProd.getName() + ", " + curProd.getPrice() + "</li>");
           }
       %>
        </br>
        <label>Total: $ <%=String.format("%.2f", total)%> </label>
        <input type="submit" value="Update Cart"/>
        </br></br>
    </form>
</ul>

<a href="index.jsp">Continue Shopping</a>
</body>
</html>