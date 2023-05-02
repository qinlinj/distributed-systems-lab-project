# Lab 1

### Exercise 1

```java
/*  Output
    Hello World
    A591A6D40BF420404A011733CFB7B190D62C65BF0BCDA32B57B277D9AD9F146E
*/
```

### Exercise 2

Yes. a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e

### Exercise 3

```jsp
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Compute Hash</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="hello-servlet">Compute Hash</a>
</body>
</html>
```

### Exercise 4

```java
package ds.helloworld;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "The SHA256 Hash of Hello World is ";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String str = "Hello World";
        try {
            MessageDigest messageDigest;
            String encodeStr;
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>" + message + encodeStr + "</h1>");
            out.println("</body></html>");
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("No SHA-256 available" + e);
        }
    }

    public void destroy() {
    }

    /**
     * Convert byte to hexadecimal
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;

        // Code from GitHub
        // https://gist.github.com/arloor/15e98d7d76f93560b337924d6f6c5b56
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);

            if (temp.length() == 1) {
                // 1 to get a bit of the complementary 0 operation
                stringBuffer.append("0");
            }

            stringBuffer.append(temp);
        }

        return stringBuffer.toString().toUpperCase();
    }
}

/*  Output
    Hello World
    A591A6D40BF420404A011733CFB7B190D62C65BF0BCDA32B57B277D9AD9F146E
*/

```



![image-20230128161452795](/Users/macbook/Library/Application Support/typora-user-images/image-20230128161452795.png)



### Exercise 5

**![101941674674589_.pic](/Users/macbook/Library/Containers/com.tencent.xinWeChat/Data/Library/Application Support/com.tencent.xinWeChat/2.0b4.0.9/3ce9a1349ddd029a07bbe197ce634872/Message/MessageTemp/b1882dddbd2925e37f55783e504c6bc6/Image/101941674674589_.pic.jpg)



Q: Explain why the request is not committed on the peers (with dark edges surrounding the term) until after the leader visits twice - once with the request and then a follow up confirmation.

A: None of the nodes will submit a request until the majority of them vote to submit the request.
So, the peers must wait until the server counts the votes and confirms that the majority says to submit.

### Exercise 6 

![image-20230128162236809](/Users/macbook/Library/Application Support/typora-user-images/image-20230128162236809.png)![image-20230128162259149](/Users/macbook/Library/Application Support/typora-user-images/image-20230128162259149.png)

Q: Explain what happened and why.

A: The four requests were not replicated to the stopped followers.
When the follower comes back online, the algorithm will replicate the requests to the follower in turn.

### Exercise 7

![image-20230128162456313](/Users/macbook/Library/Application Support/typora-user-images/image-20230128162456313.png)



Q: Explain what happened and why. Also, explain what happens if the three followers are started back up.

A: These three requests were held by a leader and a follower as a tentative rather than a commitment. The three followers were in a stopped state, and these requests were not supported by the majority (shown as a dashed line). When the three followers were reinstated, they were informed of the three requests and the whole cluster reached a consensus.

### Exercise 8 

Q: Quiz: Which of the blocks on the right can be the next block in the chain of length 2? Format: Nonce,Difficulty,id,Tx1,Tx2,HashPointer

FindThisNonce,4,19,Pink,Orange,002fdb16086d97e03613fa0caa87b280eca956216e61a35400408bdd3a449e45

A: 109346,5,19,Pink,Orange,002fdb16086d97e03613fa0caa87b280eca956216e61a35400408bdd3a449e45

![image-20230128180213186](/Users/macbook/Library/Application Support/typora-user-images/image-20230128180213186.png)

# Lab 2

### 1. HelloWorld servlet working (Step 1).

![image-20230128164309720](/Users/macbook/Library/Application Support/typora-user-images/image-20230128164309720.png)

### 2. Response to "Here is an interesting picture of a zzzz8888" and a picture of a peach (step 3g).

![image-20230128165830678](/Users/macbook/Library/Application Support/typora-user-images/image-20230128165830678.png)

### 3. Why is the loop used and what format is the str data? (steps 4a and 4b).

Read each line of "in" until done, adding each to "response"

html

### 4. The string in the response file that is used to create pictureURL (step5b).

	https://media.istockphoto.com/id/1323133343/photo/small-snack-bananas-on-white-background.jpg?b=1&s=612x612&w=0&k=20&c=62SyiEcAL5L7BvWOEG17YPaIvC_r20P-va3pg_e0nz4=

### 5. The working Easter Egg (step 6a).

```jsp
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%= request.getAttribute("doctype") %>

<html>
    <head>
        <title>Interesting Picture</title>
    </head>
    <body>
        <% if (request.getAttribute("pictureURL") != null) { %>
        <h1>Here is an interesting picture of a <%= request.getParameter("searchWord")%></h1><br>
        <% if (!request.getParameter("searchWord").equalsIgnoreCase("Andy")) {%>
        <img src="<%= request.getAttribute("pictureURL")%>"><br><br>
        <% } else { %>
        <% for (int i = 0; i < 10; i++) { %>
        <img src="<%= "https://upload.wikimedia.org/wikipedia/commons/0/09/Andrew_Carnegie%2C_by_Theodore_Marceau.jpg"%>", width=100><br><br>
        <% }} %>
        <% } else { %>
        <h1>An interesting picture of a <%= request.getParameter("searchWord")%> could not be found</h1><br>
        <% } %>
         <form action="getAnInterestingPicture" method="GET">
            <label for="letter">Type another word.</label>
            <input type="text" name="searchWord" value="" /><br>
            <input type="submit" value="Submit" />
        </form>
    </body>
</html>
```

![image-20230128170526847](/Users/macbook/Library/Application Support/typora-user-images/image-20230128170526847.png)