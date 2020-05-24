<%@ page import="ru.javaops.masterjava.xml.schema.User" %>
<%@ page import="ru.javaops.masterjava.xml.schema.FlagType" %>
<%@ page import="java.util.Set" %>
<%--
  Created by IntelliJ IDEA.
  User: Marina Sokurenko
  Date: 24.05.2020
  Time: 15:59
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>

<%
    Set<User> list = (Set<User>) request.getAttribute("list");
%>

<head>
    <link rel="stylesheet" href="css/control_style.css" type="text/css"/>
    <title>Users</title>
</head>

<body>
    <h3><a href="upload">Upload</a></h3>
    <hr>
    <table class="reestr-table">
        <caption><h2>Пользователи</h2></caption>
        <tr>
            <th>Пользователь</th>
            <th>EMail</th>
            <th>Флаг</th>
        </tr>
        <c:forEach items="${list}" var="user">
            <jsp:useBean id="user1" class="ru.javaops.masterjava.xml.schema.User"/>
            <tr class="<c:out value="${user.flag == FlagType.ACTIVE ? 'highlight_green' : 'highlight_red'}"/>">
                <td>${user.value}</td>
                <td>${user.email}</td>
                <td>${user.flag}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>