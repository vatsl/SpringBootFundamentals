<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Insert title here</title>
</head>
<body>
<form:form commandName="preferences">
    <table border="1">
        <tr>
            <td>Professional Musician?:</td>
            <td><form:checkbox path="professional"/></td>
        </tr>

        <tr>
            <td>Interests:</td>
            <td>Singing: <form:checkbox path="interests" value="singing"/>
                Lilting: <form:checkbox path="interests" value="lilting"/>
                Humming<form:checkbox path="interests" value="humming"/>
            </td>
        </tr>
        <tr>
            <td>Favorite Notes:</td>
            <td><form:checkboxes path="favoriteNotes"
                                 items="${preferences.notesList}"/></td>
        </tr>
        <tr>
            <td>Preferred Direction:</td>
            <td>Ascending: <form:radiobutton path="preferredDirection"
                                             value="Ascending"/> <br/> Descending: <form:radiobutton
                    path="preferredDirection" value="Descending"/>
            </td>
        </tr>
        <tr>
            <td>String Instrument:</td>
            <td><form:select path="stringInstrument"
                             items="${preferences.stringInstruments}"/></td>
        </tr>
        <tr>
            <td>Wind Instrument:</td>
            <td><form:select path="windInstrument">
                <form:option value="-" label="- Select One -"/>
                <form:options items="${preferences.windInstruments}"/>
            </form:select></td>
        </tr>
        <tr>
            <td>Your Deepest Thoughts:</td>
            <td><form:textarea path="deepestThoughts"></form:textarea>
        </tr>
    </table>
    <input type="submit" value="DoIt" name="doit"/>
</form:form>
</body>
</html>