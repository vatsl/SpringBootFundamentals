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
<h2>Add a Course</h2>
<form:form commandName="course">
    <table border="1" width="200">
        <tr>
            <td>Code</td>
            <td><form:input path="code"/></td>
        </tr>
        <tr>
            <td>Title</td>
            <td><form:input path="title"/></td>
        </tr>
        <tr>
            <td>Credits</td>
            <td><form:select path="credits" items="${course.creditList}"/></td>
        </tr>
        <tr>
            <td><input type="submit" name="addCourse" value="Add Course"/></td>
        </tr>
    </table>
</form:form>
</body>
</html>