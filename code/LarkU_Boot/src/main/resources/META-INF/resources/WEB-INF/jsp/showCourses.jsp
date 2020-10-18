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
<h2>Course Info</h2>
<table border="1">
    <tr>
        <td width="50">#</td>
        <td width="50">Id</td>
        <td>Code</td>
        <td>Title</td>
        <td>Credit</td>
    </tr>
    <c:forEach items="${courses}" var="course" varStatus="loopStatus">
        <tr>
            <td>${loopStatus.index}</td>
            <td>${course.id}</td>
            <td>${course.code}</td>
            <td>${course.title}</td>
            <td>${course.credits}</td>
        </tr>
    </c:forEach>
</table>
<br/> <a href="/">Home</a></td>
</body>
</html>
