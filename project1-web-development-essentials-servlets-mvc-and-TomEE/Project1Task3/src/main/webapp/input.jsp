<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- Adding a JSP directive to set the content type and page encoding for the page --%>
<%= request.getAttribute("doctype") %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%-- Setting the content type and encoding of the page --%>
    <title>JSP Page</title>
</head>
<body>
<h1>Distributed Systems Class Clicker</h1>
<p> </p>
<form action="getP1T3Servlet" method="POST">
    <%-- Defining a form that posts data to the "getP1T3Servlet" servlet --%>
    <p>Submit your answer to the current question:</p>
    <input type="radio" id="A" name="answer" value="A">
    <label for="A">A</label><br>
    <input type="radio" id="B" name="answer" value="B">
    <label for="B">B</label><br>
    <input type="radio" id="C" name="answer" value="C">
    <label for="C">C</label><br>
    <input type="radio" id="D" name="answer" value="D">
    <label for="D">D</label><br>
    <p> </p>
    <input type="submit" value="Submit" />
</form>
</body>
</html>
