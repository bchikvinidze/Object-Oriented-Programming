<%@ page import="main.java.Product" %>
<%@ page import="main.java.DBConnection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    String curProductid = (String) request.getParameter("id");
    Product curProduct = ((DBConnection)application.getAttribute("database")).getProduct(curProductid);
    String curProductName = curProduct.getName();
%>
<head>
    <meta charset=\"UTF-8\" />
    <title> <%= curProductName %> </title>
</head>
<body>

<h1> <%= curProductName %> </h1>
</br>
</br>

<img src= <%= "pics/" + curProduct.getImageFile() %> >
</br>

<form action="AddToCartServlet">
    <label> <%= curProduct.getPrice() %> </label>
    <input name="productID" type="hidden" value=<%= curProductid %> >
    <input type="submit" value="Add to Cart"/>
</form>

</body>
</html>