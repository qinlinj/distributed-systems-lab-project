package ds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class infoModel {
    /**
     * This method retrieves the capital of a country given its name
     *
     * @param searchTag name of the country to search for
     * @return the capital of the country, or an empty string if the capital cannot be found
     */
    public String getCapital(String searchTag){
        switch (searchTag) {
            case "England":
                searchTag = "United%20Kingdom";
                break;
            default:
                break;
        }
        String capitalResponse = fetch("https://restcountries.com/v3.1/name/" + searchTag);
        int cutLeft = capitalResponse.indexOf("\"capital\":[\"");
        int cutRight = capitalResponse.indexOf("\"],\"altSpellings\"", cutLeft);
        if (cutLeft == -1 || cutRight == -1) {
            return "";
        }
        String capital = capitalResponse.substring(cutLeft + 12, cutRight);
        System.out.println(capital);
        return capital;
    }

    /**
     * This method retrieves the nickname of a soccer team given its name
     *
     * @param searchTag name of the soccer team to search for
     * @return the nickname of the soccer team, or an empty string if the nickname cannot be found
     */
    public String getNickname(String searchTag){
        String nicknameResponse = fetch("https://www.topendsports.com/sport/soccer/team-nicknames-women.htm");
        int cutLeft = nicknameResponse.indexOf(searchTag + "</td><td>") + searchTag.length() + 9;
        int cutRight = nicknameResponse.indexOf("</td>", cutLeft);
        if (cutLeft == -1 || cutRight == -1) {
            return "Not found";
        }
        String nickname = nicknameResponse.substring(cutLeft, cutRight);
        return nickname;
    }

    public String getTopScorer(String searchTag){
        String player = "N/A";
        try {
            // Fetch the top scorer response from the URL
            String topScorerResponse = fetch("https://www.espn.com/soccer/stats/_/league/FIFA.WWC/season/2019/view/scoring");

            // Find the index of the search tag in the response
            int pointIndex = topScorerResponse.indexOf(searchTag);

            // Find the end index of the player's name
            int endIndex = topScorerResponse.lastIndexOf("</a></span></td>",pointIndex);

            // Find the start index of the player's name
            int startIndex = topScorerResponse.lastIndexOf(">",endIndex) + 1;

            // Extract the player's name
            String playerName = topScorerResponse.substring(startIndex, endIndex);

            // Find the start index of the player's score
            int startPointIndex = topScorerResponse.indexOf("\"tar\">",pointIndex+97) + 1;

            // Find the end index of the player's score
            int endPointIndex = topScorerResponse.indexOf("</span>",startPointIndex);

            // Extract the player's score
            String playerScore = topScorerResponse.substring(startPointIndex+5, endPointIndex);

            // Combine the player's name and score into a single string
            player = playerName + "," +playerScore;

            // Return `null` if the player string is less than or equal to 3 characters in length
            if (player.length() <= 3 || player.length() >= 50) {
                return "N/A";
            }
            if (startIndex < 0 || endIndex >= topScorerResponse.length() || endIndex <= startIndex) {
                return "N/A";
            }
            if (startPointIndex < 0 || endPointIndex >= topScorerResponse.length() || endPointIndex <= startPointIndex) {
                return "N/A";
            }
        } catch (Exception e) {
            // Handle the exception and log it
            e.printStackTrace();
        }
        // Return the combined player string
        return player;
    }



    public String flagSearch(String searchTag) throws UnsupportedEncodingException {
        switch (searchTag) {
            case "England":
                searchTag = "United Kingdom";
                break;
            default:
                break;
        }
        searchTag = searchTag.replace(" ", "-");
        // Replace spaces in the search tag with hyphens to match the URL format
        String searchTagLower = searchTag.toLowerCase();

        // Fetch the flag response from the URL
        String flagResponse = fetch("https://www.cia.gov/the-world-factbook/countries/" + searchTagLower + "/flag");

        // Find the index of ".jpg" in the response
        int cut1 = flagResponse.indexOf(".jpg");

        // Find the start index of the flag URL
        int cutLeft = flagResponse.lastIndexOf("<a href=", cut1);

        // Find the end index of the flag URL
        int cutRight = flagResponse.indexOf("\" class=\"mb0\"", cutLeft);

        // Extract the flag URL from the response
        String flagURL = "https://www.cia.gov" + flagResponse.substring(cutLeft + 9, cutRight);

        // Print the extracted URL
        System.out.println(flagURL);

        // Return the extracted URL
        return flagURL;
    }

    public String flagEmojiSearch(String searchTag) throws UnsupportedEncodingException {
        String flagEmojiURL = flagEmojiSearch.searchFlagEmoji(searchTag);
        return flagEmojiURL;
    }


    /**
     * This method fetches data from a given URL and returns it as a string
     *
     * @param urlString URL from where to fetch the data
     * @return response data fetched from the URL as a string
     */
    private String fetch(String urlString) {
        String response = "";
        try {
            // Create a URL object from the urlString
            URL url = new URL(urlString);

            // Open an HttpURLConnection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Read the response from the connection
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
                String str;
                // Read each line of the response
                while ((str = in.readLine()) != null) {
                    response += str;
                }
            }
        } catch (IOException e) {
            System.out.println("Exception");
        }
        return response;
    }

}
