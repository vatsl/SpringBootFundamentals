<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${not empty chosenStudent}">
    <div id="studentInfo" class="table">
        <div class="tableRow">
            <span class="tableCell">Name</span> <span class="tableCell">${chosenStudent.name}</span>
        </div>
        <div class="tableRow">
            <span class="tableCell">Id</span> <span class="tableCell">${chosenStudent.id}</span>
        </div>
        <div class="tableRow">
            <span class="tableCell">Phone Number</span> <span class="tableCell">${chosenStudent.phoneNumber}</span>
        </div>
    </div>
</c:if>