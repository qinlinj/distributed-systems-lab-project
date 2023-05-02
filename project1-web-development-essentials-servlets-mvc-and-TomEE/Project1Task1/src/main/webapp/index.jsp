<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- Specifies the attributes of the JSP page. It sets the language used in the page to Java, the content type to text/html and the encoding to UTF-8.--%>
<!DOCTYPE html>
<%-- Declares the type of document used, in this case it's an HTML5 document. --%>
<html>
<head>
    <meta charset="UTF-8">
    <%-- Specifies the character encoding for the document to be UTF-8. --%>
    <title>Compute Hashes</title>
    <%--  --%>
    <%-- Specifies the title of the document, which will be displayed in the browser's title bar. --%>
</head>
<body>
<h1>Compute Hashes</h1>
<form action="computeHash" method="GET">
    <label for="letter">String you want to hash:</label><br>
    <input type="text" name="hash_value_name"><br>
    <p>Choose the way you want to hash:</p>
    <input type="radio" name="hash_way" value="MD5" checked>
    <label for="MD5">MD5</label><br>
    <input type="radio" name="hash_way" value="SHA-256">
    <label for="SHA-256">SHA-256</label><br>
    <input type="submit" value="HASH"></a>
</form>
<br/>
</body>
</html>