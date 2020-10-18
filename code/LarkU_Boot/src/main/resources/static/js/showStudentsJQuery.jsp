<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>5.5_LarkU</title>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/schoolStyles.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.2.js"></script>
    <script language="javascript">
        var visitedLinkColor;

        function doShowStudent(event) {
            event.preventDefault();

            event = event || window.event;
            var href = $(this).attr('href');
            //console.log("href = " + href);

            var that = this;
            $.ajax({
                url: href,
                dataType: "json",
                success: function (data) {
                    //console.log("data = " + data);
                    var i = 10;
                    var elems = $(data);
                    $('#studentName').text(data.name);
                    $('#studentId').text(data.id);
                    $('#studentStatus').text(data.status);
                    $('#studentPhoneNumber').text(data.phoneNumber);
                    var classes = "";
                    for (var i = 0; i < data.classes.length; i++) {
                        classes += data.classes[i].course.code;
                        if (i + 1 < data.classes.length) {
                            classes += ",";
                        }
                    }
                    $('#studentClasses').text(classes);
                    var newContent = "<p>" + JSON.stringify(elems) + "</p>";
                    var divToChange = $('#studentInfoDiv');
                    if ($('#studentInfo').length) {
                        $('#studentInfo').remove();
                    }
                    //$(newContent).appendTo("#studentInfoDiv");

                    $(that).removeClass("link");
                    $(that).addClass("visited");

                    //$(that).css("color", visitedLinkColor);
                },

                error: function (jqXHR, textStatus, errorThrown) {
                    console.log("textStatus is " + textStatus + ", error is "
                        + errorThrown);
                }
            });

            return true;
        }

        $(document).ready(function () {
            $('#allStudentsTable a').click(doShowStudent);
        });
    </script>
</head>
<body>
<h2>All Students</h2>
<table id="allStudentsTable" border="1" width="200">
    <tr>
        <td>#</td>
        <td>Id</td>
        <td>Name</td>
    </tr>
    <c:forEach items="${students}" var="student" varStatus="loopStatus">
        <tr>
            <td>${loopStatus.index}</td>
            <td><a class="link"
                   href="${pageContext.request.contextPath}/adminrest/students/${student.id}">${student.id}</a></td>
            <td>${student.name}</td>
        </tr>
    </c:forEach>
</table>

<div id="studentInfoDiv">
    <h2>Selected Student Info</h2>
    <table id="studentInfoTable" border="1" width="400">
        <tr>
            <td>Name</td>
            <td id="studentName"></td>
        </tr>
        <tr>
            <td>Id</td>
            <td id="studentId"></td>
        </tr>
        <tr>
            <td>Phone Number</td>
            <td id="studentPhoneNumber"></td>
        </tr>
        <tr>
            <td>Status</td>
            <td id="studentStatus"></td>
        </tr>
        <tr>
            <td>Classes</td>
            <td id="studentClasses"></td>
        </tr>
    </table>
</div>
<br/>
<a href="${pageContext.request.contextPath}/index.jsp">Home</a>
</body>
</html>