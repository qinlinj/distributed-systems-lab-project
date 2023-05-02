/**
 * Author: Justin Jia
 * Last Modified: Feb 10, 2023
 *
 */

package hash.compute;

import java.io.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.security.MessageDigest;
import jakarta.servlet.ServletException;
import java.security.NoSuchAlgorithmException;

// This annotation maps the servlet class to a specific URL pattern
@WebServlet(name = "computeHashes", urlPatterns = {"/computeHash"})
public class ComputeHashes extends HttpServlet {

    // Overrides the doGet method to handle HTTP GET requests
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Get the values of the hash_value_name and hash_way parameters from the request
        String original_value = request.getParameter("hash_value_name");
        String hash_way = request.getParameter("hash_way");

        try {
            // Declare variables to store the hash values
            String hash_hexadecimal;
            String hash_64notation;
            MessageDigest md;

            // Check which type of hash to compute
            if(hash_way.equals("SHA-256")) {
                md = MessageDigest.getInstance("SHA-256");
            } else {
                md = MessageDigest.getInstance("MD5");
            }

            // Update the message digest with the original value
            md.update(original_value.getBytes());
            byte[] digest = md.digest();

            // Compute the hexadecimal and 64 notation representations of the digest
            hash_64notation = jakarta.xml.bind.DatatypeConverter.printBase64Binary(digest);
            hash_hexadecimal = jakarta.xml.bind.DatatypeConverter.printHexBinary(digest);

            // Get the writer for the response
            PrintWriter out = response.getWriter();
            // Write the HTML response
            out.println("<html><body>");
            out.println("<h1>The " + hash_way + " Hash of " + original_value + "</h1>");
            out.println("<h1>Hexadecimal: " + hash_hexadecimal + "</h1>");
            out.println("<h1>64 notation: " + hash_64notation + "</h1>");
            out.println("</body></html>");
        } catch (NoSuchAlgorithmException e) {
            // If the specified hash algorithm is not available, print an error message
            System.out.println("No Hash available" + e);
        }
    }
}


