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