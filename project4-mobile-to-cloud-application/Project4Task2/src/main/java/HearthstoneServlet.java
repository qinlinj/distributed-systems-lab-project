/**
 * andrewId: qinlinj
 * author: Justin Jia
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "HearthstoneServlet", urlPatterns = {"/cardInfo"})
public class HearthstoneServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cardName = request.getParameter("cardName");
        if (cardName != null) {
            try {
                Card card = HearthstoneAPI.fetchCardByName(cardName);
                if (card != null) {
                    JsonObject jsonObject = card.toJson();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer = response.getWriter();
                    writer.print(jsonObject.toString());
                    writer.flush();
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Card not found.");
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching card data.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing card name parameter.");
        }
    }
}







