package ds.web;

import jakarta.annotation.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.jms.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;

/*
 * MyQueueWriter is a Servlet that takes the value of the simpleTextMessage parameter in a GET request
 * and sends it as a message to a Queue.
 */
@WebServlet(name = "MyQueueWriter", urlPatterns = {"/MyQueueWriter"})
public class MyQueueWriter extends HttpServlet {

    // Lookup the ConnectionFactory using resource injection and assign to cf
    @Resource(mappedName = "jms/myConnectionFactory")
    private ConnectionFactory cf;
    // lookup the Queue using resource injection and assign to q
    @Resource(mappedName = "jms/myQueue")
    private Queue q;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // We are going to do a simple response to the browser.  We are not using MVC in this example
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Get the value of the simpleTextMessage parameter in the GET request
        String val = request.getParameter("simpleTextMessage");

        try {
            // With the ConnectionFactory, establish a Connection, and then a Session on that Connection
            Connection con = cf.createConnection();
            Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            con.start(); // Always remember to start the connection

            /*
             * You send and receive messages to/from the queue via a session. We
             * want to send, making us a MessageProducer. Therefore, create a
             * MessageProducer for the session
             */
            MessageProducer writer = session.createProducer(q);

            /*
             * The message can be text, a byte stream, a Java object, or a
             * attribute/value Map We want to send a text message. BTW, a text
             * message can be a string, or it can be a JSON, XML, or SOAP object.
             */
            TextMessage msg = session.createTextMessage();
            msg.setText(val);

            // Send the message to the destination Queue
            writer.send(msg);

            // Close the connection
            con.close();

            // Respond to the browser that the message has been sent to the Queue
            out.println("<HTML><BODY><H1>Wrote " + val + " to queue</H1>");
            out.println("</BODY></HTML>");
            System.out.println("Servlet sent " + val + " to jms/myQueue");
        } catch (Exception e) {
            System.out.println("Servlet through exception " + e);
        } finally {
            out.close();
        }
    }
}
