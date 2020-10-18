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
<table>
    <tr>
        <td><a
                href="${pageContext.request.contextPath}/admin/getStudents">Get
            all students</a></td>
    </tr>
    <tr>
        <td>
            <form method="get" action="${pageContext.request.contextPath}/admin/getStudent">
                <input type="text" name="id"/> <input type="submit" value="Get Student With Id"/>
            </form>
        </td>
    </tr>
    <tr>
        <td><a
                href="${pageContext.request.contextPath}/admin/addStudent">Add
            a student</a></td>
    </tr>
    <tr>
        <td><a
                href="${pageContext.request.contextPath}/registration/getAllClasses">Get All Classes</a></td>
    </tr>
    <tr>
        <td><a
                href="${pageContext.request.contextPath}/registration/addClass">Add Class</a></td>
    </tr>
    <tr>
        <td><a
                href="${pageContext.request.contextPath}/registration/registerStudentForClass">
            Register Student For Class</a></td>
    </tr>
    <tr>
        <td><a
                href="${pageContext.request.contextPath}/adminrest/student/showStudents">
            Show Students Restfully</a></td>
    </tr>
    <tr>
        <td><a
                href="${pageContext.request.contextPath}/adminrest/student/addStudents">
            Add Students Restfully</a></td>
    </tr>
</body>
</html>