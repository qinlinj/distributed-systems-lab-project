<%-- Set the content type and encoding for the page --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- Print the doctype from the request attribute --%>
<%= request.getAttribute("doctype") %>

<html>
<head>
    <!-- Set the content type and encoding for the page -->
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Set the title of the page -->
    <title>Distributed Systems Class Clicker</title>
</head>
<body>
<!-- Display the header for the page -->
<h1>Distributed Systems Class Clicker</h1>
<!-- Add a blank line -->
<p> </p>
<!-- Create a form for submitting answers -->
<form action="getP1T3Servlet" method="POST">
    <!-- Display the previously submitted answer -->
    <p>Your "<%= request.getAttribute("answer")%>" has been registered</p>
    <!-- Prompt the user to submit their answer to the current question -->
    <p>Submit your answer to the current question:</p>
    <!-- Radio buttons for each answer option -->
    <input type="radio" id="A" name="answer" value="A">
    <label for="A">A</label><br>
    <input type="radio" id="B" name="answer" value="B">
    <label for="B">B</label><br>
    <input type="radio" id="C" name="answer" value="C">
    <label for="C">C</label><br>
    <input type="radio" id="D" name="answer" value="D">
    <label for="D">D</label><br>
    <!-- Add a blank line -->
    <p> </p>
    <!-- Submit button -->
    <input type="submit" value="Submit" />
</form>
</body>
</html>
