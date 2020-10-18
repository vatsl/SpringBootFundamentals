<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>LarkU</title>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link rel="stylesheet" type="text/css" href="schoolStyles.css"/>
    <script type="text/javascript" src="js/jquery-1.8.2.js"></script>
    <script language="javascript">
        var visitedLinkColor;

        function doShowStudent(event) {
            event.preventDefault();

            event = event || window.event;
            var href = $(this).attr('href');
            //console.log("href = " + href);

            var that = this;
            /*
            $.ajax({
                url : href,
                dataType : "json",
                success : function(data) {
                    //console.log("data = " + data);

                    $('#nameSpan').text(data.name);
                    $('#idSpan').text(data.id);
                    $('#phoneSpan').text(data.phoneNumber);

                    $('#allStudentsTable a').not(that).removeClass("visited").addClass("link")
                    $(that).removeClass("link").addClass("visited");

                    //$(that).css("color", visitedLinkColor);
                },

                error : function(jqXHR, textStatus, errorThrown) {
                    console.log("textStatus is " + textStatus + ", error is "
                            + errorThrown);
                }
            });
             */

            $.get(
                href,
                function (data) {
                    //console.log("data = " + data);
                    $('#nameSpan').text(data.name);
                    $('#idSpan').text(data.id);
                    $('#phoneSpan').text(data.phoneNumber);

                    $('#allStudentsTable a').not(that).removeClass("visited")
                        .addClass("link")
                    $(that).removeClass("link").addClass("visited");

                }, "json")
                .error(function (jqXHR, textStatus, errorThrown) {
                    console.log("textStatus is " + textStatus + ", error is "
                        + errorThrown);
                });


            return true;
        }

        $(document).ready(function () {
            $('#allStudentsTable a').click(doShowStudent);
            /*
            var fakeVisitedLink = $('<a id="fakeLink" href=' + $(location).attr("href") + '>fake link</a>');
            fakeVisitedLink.appendTo("#studentInfoDiv");
            var visitedLinkColor = fakeVisitedLink.css("color");
            fakeVisitedLink.remove();
             */
        });
    </script>
</head>
<body>
<table id="allStudentsTable" border="1">
    <h2>All Students</h2>
    <tr>
        <td>#</td>
        <td>Id</td>
        <td>Name</td>
    </tr>
    <c:forEach items="${students}" var="student" varStatus="loopStatus">
        <tr>
            <td>${loopStatus.index}</td>
            <td><a class="link"
                   href="/Solutions/ajaxRequestJPA?id=${student.id}">${student.id}</a></td>
            <td>${student.name}</td>
        </tr>
    </c:forEach>
</table>

<div id="studentInfoDiv" class="table">
    <h2>Selected Student Info</h2>
    <div class="tableRow">
        <span class="tableCell">Name</span> <span id="nameSpan"
                                                  class="tableCell">${chosenStudent.name}</span>
    </div>
    <div class="tableRow">
        <span class="tableCell">Id</span> <span id="idSpan" class="tableCell">${chosenStudent.id}</span>
    </div>
    <div class="tableRow">
        <span class="tableCell">Phone Number</span> <span id="phoneSpan"
                                                          class="tableCell">${chosenStudent.phoneNumber}</span>
    </div>
</div>
</body>
</html>