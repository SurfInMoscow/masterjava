<%--
  Created by IntelliJ IDEA.
  User: vorobyev
  Date: 08/12/2019
  Time: 19:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<head>
    <title>Uploaded</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
            background-color: #ebebbc;
        }
        th, td {
            padding: 15px;
            text-align: left;
        }
    </style>
</head>
<body>
<h2>Submitted File</h2>
<table>
    <tr>
        <th>Name</th>
        <th>email</th>
        <th>flag</th>
    </tr>
    <c:forEach var="users" items="${users}">
        <jsp:useBean id="users" type="ru.javaops.masterjava.xml.schema.User"/>
        <tr>
            <td>${users.value}</td>
            <td>${users.email}</td>
            <td>${users.flag.value()}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
