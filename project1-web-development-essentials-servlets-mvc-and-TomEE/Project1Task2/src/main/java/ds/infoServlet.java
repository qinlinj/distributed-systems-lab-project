package ds;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "StateInformationServlet",
        urlPatterns = {"/getAnStateInformation"})
public class infoServlet extends HttpServlet {

    // Declare a variable to store the StateInformationModel object
    infoModel sim = null;

    @Override
    public void init() {
        // Initialize the StateInformationModel object
        sim = new infoModel();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Get the state name from the request
        String search = request.getParameter("state");

        // Get the User-Agent header from the request
        String ua = request.getHeader("User");

        // A flag to keep track of whether the request comes from a mobile device
        boolean mobile;

        // Check if the request is from an Android or iPhone device
        if (ua != null && ((ua.indexOf("Android") != -1) || (ua.indexOf("iPhone") != -1))) {
            mobile = true;

            // Set the doctype for mobile devices
            request.setAttribute("doctype", "<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.2//EN\" \"http://www.openmobilealliance.org/tech/DTD/xhtml-mobile12.dtd\">");
        } else {
            mobile = false;

            // Set the doctype for non-mobile devices
            request.setAttribute("doctype", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        }

        // A variable to store the next view
        String nextView;

        // If the state name is not null
        if (search != null) {
            // Get the picture size based on the type of device
            String picSize = (mobile) ? "mobile" : "desktop";

            // Get the information for the state from the StateInformationModel object
            String country = search;
            String capital = sim.getCapital(search);
            String nickName = sim.getNickname(search);
            String topScorer = sim.getTopScorer(search);
            String flag = sim.flagSearch(search);
            String flagURL = sim.flagEmojiSearch(search);

            // Set the information as request attributes
            request.setAttribute("country",country);
            request.setAttribute("capital", capital);
            request.setAttribute("nickName", nickName);
            request.setAttribute("topScorer", topScorer);
            request.setAttribute("flag", flag);
            request.setAttribute("flagURL", flagURL);

            // Set the next view to be the result.jsp
            nextView = "result.jsp";
        } else {
            // If the state name is null, set the next view to be the prompt.jsp
            nextView = "prompt.jsp";
        }

        // Forward the request to the next view
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        view.forward(request, response);
    }
}
