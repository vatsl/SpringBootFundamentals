<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link type="text/css" rel="stylesheet" media="all"
          href="${pageContext.request.contextPath}/css/larkU.css">
    <title>LarkU</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/larkUHeader.jsp"/>
<h2> Register a student for a class </h2>
<form action="${pageContext.request.contextPath}/registration/registerStudentForClass" method="post">
    <table border="1" width="300">
        <tr>
            <td>Student</td>
            <td>Scheduled Class</td>
        </tr>
        <tr>
            <td><select name="studentId">
                <c:forEach items="${students}" var="student">
                    <option value="${student.id}">${student.name}</option>
                </c:forEach>
            </select></td>

            <td><select name="classId">
                <c:forEach items="${classes}" var="sClass">
                    <option value="${sClass.id}">${sClass.course.code} - ${sClass.startDate}
                        - ${sClass.endDate}</option>
                </c:forEach>
            </select></td>
        </tr>
        <tr>
            <td><input type="submit" name="registerStudent" value="Register Student"/></td>
        </tr>
    </table>
</form>
<br/>
<a href="/">Home</a>
</body>
</html>
