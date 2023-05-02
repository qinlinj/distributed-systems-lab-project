<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>JMS Example</title>
</head>
<body>
<h3>Enter a message that will be sent to the servlet.</h3>
<form action="MyQueueWriter">
  <table>
    <tbody>
    <tr> <td>Enter a message</td> <td>
      <input type="text" name="simpleTextMessage" value="Enter text here" /></td>
    </tr>
    </tbody>
  </table>
  <input type="submit" value="Submit text to servlet" />
</form>
</body>
</html>
