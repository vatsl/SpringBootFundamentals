<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link type="text/css" rel="stylesheet" media="all"
          href="${pageContext.request.contextPath}/css/larkU.css">
    <title>LarkU</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/larkUHeader.jsp"/>
<h2>Student Info</h2>
<table border="1">
    <tr>
        <td>Name</td>
        <td>${student.name}</td>
    </tr>
    <tr>
        <td>Id</td>
        <td>${student.id}</td>
    </tr>
    <tr>
        <td>Phone Number</td>
        <td>${student.phoneNumber}</td>
    </tr>
    <tr>
        <td>Status</td>
        <td>${student.status}</td>
    </tr>
    <tr>
        <td>Courses</td>
        <td>
            <c:forEach items="${student.classes}" var="sClass">
                ${sClass.course.code}<br/>
            </c:forEach>
        </td>
    </tr>
</table>
<br/>
<a href="/">Home</a>
</td>
</body>
</html>
