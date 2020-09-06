<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="main.java.Product" %>
<%@ page import="main.java.DBConnection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset=\"UTF-8\" />
  <title>Student Store</title>
</head>
<body>

<h1>Student Store</h1>
</br>
Items available:
</br>
<ul>
  <c:forEach var="product" items="${productList}">
    <li>
      <a href="show-product.jsp?id=${product.getId()}">${product.getName()}</a>
    </li>
  </c:forEach>
</ul>

</body>
</html>