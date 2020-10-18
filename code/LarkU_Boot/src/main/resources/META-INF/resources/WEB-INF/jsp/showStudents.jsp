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
<h2>All Students</h2>
<table border="1" width="200">
    <tr>
        <td>#</td>
        <td>Id</td>
        <td>Name</td>
        <td>Status</td>
        <td>Courses</td>
    </tr>
    <c:forEach items="${students}" var="student" varStatus="loopStatus">
        <tr>
            <td>${loopStatus.index}</td>
            <td>${student.id}</td>
            <td>${student.name}</td>
            <td>${student.status}</td>
            <td>
                <c:forEach items="${student.classes}" var="clazz">
                    ${clazz.course.code}<br/>
                </c:forEach>
            </td>
        </tr>
    </c:forEach>
</table>
<br/> <a href="/">Home</a></td>
</body>
</html>
