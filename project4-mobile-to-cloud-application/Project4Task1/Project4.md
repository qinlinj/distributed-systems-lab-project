Title: REST Countries API Usage Documentation

1. API Name: REST Countries API
2. API Documentation URL: [https://restcountries.com](https://restcountries.com/)
3. Description: My mobile application will utilize the REST Countries API to display relevant information about any country the user searches for. The user will input the country's name, and the application will retrieve the corresponding data from the API. The retrieved information will include the country's population, area, currency, official language, and flag. This will provide users with a quick and easy way to access essential information about different countries.
4. Example:

Suppose a user enters "France" as the search query. The mobile app will send an API request to the following URL: `https://restcountries.com/v3.1/name/france`

The REST Countries API will return the JSON data containing information about France. The Java code provided





1. API Name: Hearthstone API
2. API Documentation URL: https://hearthstonejson.com/
3. Description: The mobile application will prompt the user for a Hearthstone card name and use the Hearthstone API to fetch card information. The app will display the card's details, including an image, to provide users with quick access to card information while playing or discussing Hearthstone. Additionally, users can save card details to a MongoDB database for future reference.





ConnectionString connectionString = new ConnectionString("mongodb+srv://justinqinlin:qinlinj@cluster0.otvez1g.mongodb.net/?retryWrites=true&w=majority");
MongoClientSettings settings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
        .serverApi(ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build())
        .build();
MongoClient mongoClient = MongoClients.create(settings);
MongoDatabase database = mongoClient.getDatabase("test");