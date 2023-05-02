package ds;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

//        The following code is a Java servlet for a simple answer clicking application. The servlet receives HTTP requests and
//        performs the appropriate actions based on the request's URL path. The servlet has two URL patterns: "/getP1T3Servlet" and "/getResults".
//        The servlet uses a business model class, "AnswerClickerModel", to store the number of times each answer has been
//        clicked. The servlet instantiates this model when it is first started. When the servlet receives a request with
//        the answer parameter, it updates the count for that answer in the model. When the servlet receives a request
//        with the path "/getResults", it retrieves the total number of clicks for each answer and forwards the data to
//        a JSP page for display.

@WebServlet(name = "AnswerClickerServlet", urlPatterns = {"/getP1T3Servlet", "/getResults"})
public class AnswerClickerServlet extends HttpServlet {
    // Declare a model to store the answers
    private AnswerClickerModel answerClickerModel = new AnswerClickerModel();

    // Handles GET requests
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Handles POST requests
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    private void setDoctypeAttribute(HttpServletRequest request) {
        boolean isMobile = isMobileDevice(request);
        request.setAttribute("doctype", isMobile ?
                "<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.2//EN\" \"http://www.openmobilealliance.org/tech/DTD/xhtml-mobile12.dtd\">" :
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
    }
    // Common logic for processing requests, either GET or POST
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the request is from a mobile device
        setDoctypeAttribute(request);
        // Get the servlet path
        String path = request.getServletPath();
        String nextView;

        // Check the path and process the request accordingly
        if (path.equals("/getResults")) {
            nextView = processResults(request);
        } else {
            nextView = processAnswer(request);
        }
        // Get the RequestDispatcher for the next view
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        // Forward the request to the next view
        view.forward(request, response);
    }

    // Processes the answer submitted by the user
    private String processAnswer(HttpServletRequest request) {
        // Get the answer from the request
        String answer = request.getParameter("answer");
        if (answer != null) {
            // Add the answer to the model
            answerClickerModel.addAnswer(answer);
            // Set the answer as a request attribute
            request.setAttribute("answer", answer);
            // Return the result view
            return "result.jsp";
        }
        // Return the prompt view if no answer is submitted
        return "input.jsp";
    }

    // Processes the results of the answers submitted so far
    private String processResults(HttpServletRequest request) {
        // Get the number of answers for each option
        int numAnswersA = answerClickerModel.getAnswerCount("A");
        int numAnswersB = answerClickerModel.getAnswerCount("B");
        int numAnswersC = answerClickerModel.getAnswerCount("C");
        int numAnswersD = answerClickerModel.getAnswerCount("D");
        int totalAnswers = numAnswersA + numAnswersB + numAnswersC + numAnswersD;


        request.setAttribute("numAnswersA", numAnswersA);
        request.setAttribute("numAnswersB", numAnswersB);
        request.setAttribute("numAnswersC", numAnswersC);
        request.setAttribute("numAnswersD", numAnswersD);
        request.setAttribute("totalAnswers", totalAnswers);
        return "output.jsp";
    }

    private boolean isMobileDevice(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null && (userAgent.indexOf("Android") != -1 || userAgent.indexOf("iPhone") != -1);
    }
}

