<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${not empty chosenStudent}">
    {"name" : "${chosenStudent.name}", "id" : ${chosenStudent.id}, "phoneNumber" : "${chosenStudent.phoneNumber}" }

</c:if>