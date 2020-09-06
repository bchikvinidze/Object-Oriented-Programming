<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset=\"UTF-8\" />
    <title>Welcome <%= request.getParameter("name") %></title>
</head>
<body>
<h1>Welcome <%= request.getParameter("name") %></h1>
</body>
</html>
