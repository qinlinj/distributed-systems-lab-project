/**
 * andrewId: qinlinj
 * author: Justin Jia
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HearthstoneAPI {

    // URL to Hearthstone API that returns collectible cards in JSON format
    private static final String BASE_URL = "https://api.hearthstonejson.com/v1/latest/enUS/cards.collectible.json";

    public static void main(String[] args) {
        // Create scanner object to get user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a Hearthstone card name: ");
        String cardName = scanner.nextLine();

        try {
            // Fetch card data from Hearthstone API and get card info for the specified card name
            String jsonString = fetchCardData();
            getCardInfo(jsonString, cardName);
        } catch (Exception e) {
            // Print error message if there was an error fetching card data
            System.err.println("Error fetching card data: " + e.getMessage());
        }
    }

    // Method to fetch card data from the Hearthstone API
    static String fetchCardData() throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        return response.toString();
    }

    // Method to get card info for the specified card name from the fetched card data
    static Card getCardInfo(String jsonString, String cardName) {
        // Parse JSON string into a JsonArray object
        JsonArray cards = JsonParser.parseString(jsonString).getAsJsonArray();
        // Iterate over each card in the JsonArray to find the card with the specified name
        for (int i = 0; i < cards.size(); i++) {
            JsonObject card = cards.get(i).getAsJsonObject();
            if (card.get("name").getAsString().equalsIgnoreCase(cardName)) {
                // Get card information from the card JsonObject
                String cardId = card.get("id").getAsString();
                String cardClass = card.get("cardClass").getAsString();
                int cardCost = card.get("cost").getAsInt();
                String cardFlavor = card.get("flavor").getAsString();
                String cardRarity = card.get("rarity").getAsString();
                int attack = 0;
                if (card.has("attack")) {
                    attack = card.get("attack").getAsInt();
                }
                int health = 0;
                if (card.has("health")) {
                    health = card.get("health").getAsInt();
                }
                int durability = 0;
                if (card.has("durability")) {
                    durability = card.get("durability").getAsInt();
                }
                int armor = 0;
                if (card.has("armor")) {
                    armor = card.get("armor").getAsInt();
                }
                String race = null;
                if (card.has("race")) {
                    race = card.get("race").getAsString();
                }
                String text = null;
                if (card.has("text")) {
                    text = card.get("text").getAsString();
                }

                String cardType = card.get("type").getAsString();
                // Generate the card image URL using the card ID
                String cardImageUrl = generateCardImageUrl(cardId);
                // Create a Card object with the card information and return it
                Card cardInfo;
                switch (cardType) {
                    case "HERO":
                        cardInfo = new HeroCard(cardName, cardId, cardClass, cardCost, cardFlavor, cardRarity, cardImageUrl, armor);
                        break;
                    case "MINION":
                        cardInfo = new MinionCard(cardName, cardId, cardClass, cardCost, cardFlavor, cardRarity, cardImageUrl, race, attack, health);
                        break;
                    case "SPELL":
                        cardInfo = new SpellCard(cardName, cardId, cardClass, cardCost, cardFlavor, cardRarity, cardImageUrl, text);
                        break;
                    case "WEAPON":
                        cardInfo = new WeaponCard(cardName, cardId, cardClass, cardCost, cardFlavor, cardRarity, cardImageUrl, attack, durability);
                        break;
                    default:
                        cardInfo = new Card(cardName, cardId, cardClass, cardCost, cardFlavor, cardRarity, cardImageUrl);
                        break;
                }

                if (cardInfo != null) {
                    System.out.println(cardInfo.toString());
                }
                return cardInfo;
            }
        }
        // Return null if the specified card name was not found in the fetched card data
        return null;
    }

    // Method to generate the URL for the card image given the card ID
    private static String generateCardImageUrl(String cardId) {
        String locale = "enUS";
        String resolution = "512x";
        String extension = "png";
        return String.format("https://art.hearthstonejson.com/v1/render/latest/%s/%s/%s.%s",
                locale, resolution, cardId, extension);
    }

    public static Card fetchCardByName(String cardName) throws Exception {
        String jsonString = fetchCardData();
        return getCardInfo(jsonString, cardName);
    }
}
