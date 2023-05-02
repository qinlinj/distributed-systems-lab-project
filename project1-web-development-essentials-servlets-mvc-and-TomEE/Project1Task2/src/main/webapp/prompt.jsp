<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%= request.getAttribute("doctype") %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Women's World Cup 2023</title>
</head>
<body>
<h1>Women's World Cup 2023</h1>
<p>Created by Justin Jia</p>
<h2>Participating Countries</h2>
<p>Choose a country:</p>
<form action="getAnStateInformation" method="GET">
    <select name="state">
        <option value="Argentina">Argentina</option>
        <option value="Australia">Australia</option>
        <option value="Brazil">Brazil</option>
        <option value="Canada">Canada</option>
        <option value="China">China</option>
        <option value="Colombia">Colombia</option>
        <option value="Costa Rica">Costa Rica</option>
        <option value="Denmark">Denmark</option>
        <option value="England">England</option>
        <option value="France">France</option>
        <option value="Germany">Germany</option>
        <option value="Ireland">Ireland</option>
        <option value="Italy">Italy</option>
        <option value="Jamaica">Jamaica</option>
        <option value="Japan">Japan</option>
        <option value="Morocco">Morocco</option>
        <option value="Netherlands">Netherlands</option>
        <option value="New Zealand">New Zealand</option>
        <option value="United States">United States</option>
        <option value="South Africa">South Africa</option>
    </select>
    <input type="submit" value="Submit">
</form>
</body>
</html>