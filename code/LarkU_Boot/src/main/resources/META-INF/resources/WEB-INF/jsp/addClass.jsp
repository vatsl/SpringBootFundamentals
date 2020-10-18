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
<h2>Add a new Scheduled Class</h2>
<form action="${pageContext.request.contextPath}/registration/addClass" method="post">
    <table border="1" width="300">
        <tr>
            <td>Course Code</td>
            <td><select name="courseCode">
                <c:forEach items="${courses}" var="course">
                    <option value="${course.code}">${course.code}</option>
                </c:forEach>
            </select></td>
        </tr>
        <tr>
            <td>Start Date</td>
            <td><input type="text" name="startDate"/></td>
        </tr>
        <tr>
            <td>End Date</td>
            <td><input type="text" name="endDate"/></td>
        </tr>
        <tr>
            <td><input type="submit" name="addClass" value="Add Class"/></td>
        </tr>
    </table>
</form>
</body>
</html>