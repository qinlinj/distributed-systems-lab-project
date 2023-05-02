/**
 * andrewId: qinlinj
 * author: Justin Jia
 */

import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.Scanner;

public class HearthstoneMongoDB {
    // MongoDB database name and collection name
    private static final String DATABASE_NAME = "hearthstone";
    private static final String COLLECTION_NAME = "cards";

    public static void main(String[] args) throws Exception {
        // Get user input for Hearthstone card name
        System.out.print("Enter a Hearthstone card name: ");
        Scanner scanner = new Scanner(System.in);
        String cardName = scanner.nextLine();

        // Connect to MongoDB Atlas cluster
        ConnectionString CONNECTION_STRING = new ConnectionString("mongodb+srv://justinqinlin:qinlinj@cluster0.otvez1g.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(CONNECTION_STRING)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        MongoClient mongoClient = MongoClients.create(settings);

        // Get MongoDB database and collection
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        // collection.drop();

        // Get Hearthstone card information from Hearthstone API
        Card card = HearthstoneAPI.getCardInfo(HearthstoneAPI.fetchCardData(), cardName);
        if (card != null) {
            // Create a MongoDB Document object from the Hearthstone Card object
            Document cardDocument = new Document("name", card.getName())
                    .append("cardId", card.getCardId())
                    .append("cardClass", card.getCardClass())
                    .append("cardCost", card.getCardCost())
                    .append("cardFlavor", card.getCardFlavor())
                    .append("cardRarity", card.getCardRarity())
                    .append("cardImageUrl", card.getCardImageUrl());

            // Add the appropriate fields based on the type of card
            if (card instanceof MinionCard) {
                MinionCard minionCard = (MinionCard) card;
                cardDocument.append("cardType", CardType.MINION.getName())
                        .append("race", minionCard.getRace())
                        .append("attack", minionCard.getAttack())
                        .append("health", minionCard.getHealth());
            } else if (card instanceof SpellCard) {
                cardDocument.append("cardType", CardType.SPELL.getName());
            } else if (card instanceof WeaponCard) {
                WeaponCard weaponCard = (WeaponCard) card;
                cardDocument.append("cardType", CardType.WEAPON.getName())
                        .append("attack", weaponCard.getAttack())
                        .append("durability", weaponCard.getDurability());
            } else if (card instanceof HeroCard) {
                cardDocument.append("cardType", CardType.HERO.getName())
                        .append("armor", ((HeroCard) card).getArmor());
            } else {
                cardDocument.append("cardType", "Unknown");
            }

            // Insert the card information into the MongoDB collection
            collection.insertOne(cardDocument);
            System.out.println("Stored card information in the database.");

            System.out.println("Reading all documents in the collection:");
        } else {
            System.out.println("Card not found.");
        }

        // Print all documents in the MongoDB collection
        JsonWriterSettings jsonSettings = JsonWriterSettings.builder()
                .outputMode(JsonMode.RELAXED)
                .indent(true)
                .build();
        for (Document doc : collection.find()) {
            // Convert document to JSON and remove the _id field
            String json = doc.toJson(jsonSettings);
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            jsonObject.remove("_id");
            System.out.println(jsonObject.toString());
        }

        // Close the MongoDB client connection
        mongoClient.close();
    }
}