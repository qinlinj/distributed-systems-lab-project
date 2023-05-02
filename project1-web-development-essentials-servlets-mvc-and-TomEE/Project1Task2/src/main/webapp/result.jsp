<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%-- Declaring the content type and encoding for the page --%>
<%= request.getAttribute("doctype") %>
<%-- Outputting the doctype attribute value from the request object --%>

<html>
<head>
    <title>Search Results</title>
</head>
<body>
<font size="7">Country: <%= request.getAttribute("country")%> </font>
<%-- Outputting the country name attribute value from the request object --%>

<h1>Nickname: <%= request.getAttribute("nickName")%></h1>
<%-- Outputting the nickname attribute value from the request object --%>
<p>https://www.topendsports.com/sport/soccer/team-nicknames-women.htm</p>
<%-- Source of information for the nickname --%>

<h1>Capital City: <%= request.getAttribute("capital")%></h1>
<%-- Outputting the capital city attribute value from the request object --%>
<p>https://restcountries.com/v3.1/name/</p>
<%-- Source of information for the capital city --%>

<h1>Top Scorer in 2019: <%= request.getAttribute("topScorer")%></h1>
<%-- Outputting the top scorer attribute value from the request object --%>
<p>https://www.espn.com/soccer/stats/_/league/FIFA.WWC/season/2019/view/scoring</p>
<%-- Source of information for the top scorer --%>

<h1>Flag: </h1>
<img src="<%= request.getAttribute("flag")%>", width=300>
<%-- Outputting the flag URL attribute value from the request object as the image source --%>
<p>https://www.cia.gov/the-world-factbook/countries/</p>
<%-- Source of information for the flag --%>

<h1>Flag Emoji: </h1>
<img src="<%= request.getAttribute("flagURL")%>", width=300>
<%-- Outputting the flag emoji URL attribute value from the request object as the image source --%>
<p>https://cdn.jsdelivr.net/npm/country-flag-emoji-json@2.0.0/dist/index.json</p>
<%-- Source of information for the flag emoji --%>

<form action="getAnCountryInformation" method="GET">
    <p> </p>
    <input type="submit" value="Continue" />
</form>
<%-- A form to submit the GET request to the "getAnCountryInformation" action --%>
</body>
</html>



