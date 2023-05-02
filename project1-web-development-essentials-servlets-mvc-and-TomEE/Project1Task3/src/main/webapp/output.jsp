<%-- Set the content type and encoding for the page --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- Print the doctype from the request attribute --%>
<%= request.getAttribute("doctype") %>

<html>
<body>
<!-- Display the header for the page -->
<h1>Distributed Systems Class Clicker</h1>

<!-- Print the total number of answers -->
<% System.out.println(request.getAttribute("totalAnswers")); %>

<!-- Check if the total number of answers is 0 -->
<% if(request.getAttribute("totalAnswers").equals(0)) { %>
<!-- If there are no answers, display a message -->
<h1> There are currently no results </h1> <br>
<% } else {%>
<!-- If there are answers, display the number of answers for each option -->
<h1>A: <%= request.getAttribute("numAnswersA")%></h1>
<h1>B: <%= request.getAttribute("numAnswersB")%></h1>
<h1>C: <%= request.getAttribute("numAnswersC")%></h1>
<h1>D: <%= request.getAttribute("numAnswersD")%></h1>
<!-- Add a blank line -->
<p> </p>
<% } %>
</body>
</html>

