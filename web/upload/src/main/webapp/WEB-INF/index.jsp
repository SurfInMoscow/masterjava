<%--
  Created by IntelliJ IDEA.
  User: vorobyev
  Date: 08/12/2019
  Time: 00:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Upload File</title>
</head>
<body>
<h2>File upload</h2>
<br>
<form method="POST" enctype="multipart/form-data" action="">
    File to upload: <input type="file" name="file"><br/>
    <br/>
    <input type="submit" value="Press">
</form>
</body>
</html>