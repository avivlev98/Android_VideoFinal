package com.example.videogamecatalog.services;

import android.os.StrictMode;


import com.example.videogamecatalog.Classes.Game;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DataService {
    private static final String BASE_URL = "https://api.igdb.com/v4/";
    private static final String CLIENT_ID = "3ygom4z4bwsucofnx61gfhraymfagx"; // Replace with your IGDB client ID
    private static final String ACCESS_TOKEN = "fgig0uvxejqswlgqsmbd2hgbyo61nc"; // Replace with your IGDB access token

    public static void main() throws Exception {
        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Get games by name (using search query)
        String gamesEndpoint = BASE_URL + "games";
        String desiredFields = "name,cover.url,genres.name,summary,first_release_date,rating,platforms.name";
        String url = gamesEndpoint + "?fields=" + desiredFields + "&limit=300";


        sendGetRequest(url);
        //sendGetRequest(coversUrl);
    }

    private static void sendGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Client-ID", CLIENT_ID);
        connection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder responseContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();

            System.out.println(responseContent.toString());
            JsonParser parser = new JsonParser();
            JsonElement root = parser.parse(responseContent.toString());
            JsonArray rootArray = root.getAsJsonArray();
            for (JsonElement je : rootArray){

                JsonObject obj = je.getAsJsonObject();
                JsonElement gameID = obj.get("id");
                JsonElement gameName = obj.get("name");
                //get game image
                JsonElement coverElement = obj.get("cover");
                if (coverElement == null || coverElement.isJsonNull()) {
                    // Handle case when cover is not present
                    continue; // Skip this iteration if coverElement is null
                }
                JsonObject coverObject = coverElement.getAsJsonObject();
                JsonElement imageUrlElement = coverObject.get("url");
                if (imageUrlElement == null || imageUrlElement.isJsonNull()) {
                    // Handle case when imageUrl is not present
                    continue; // Skip this iteration if imageUrlElement is null
                }
                String imageUrl = imageUrlElement.getAsString();
                // get game image
                JsonElement first_release_date = obj.get("first_release_date");
                if (first_release_date == null || first_release_date.isJsonNull()) {
                    // Handle case when cover is not present
                    continue; // Skip this iteration if coverElement is null
                }
                // fetch genre list
                JsonArray genresArray = obj.getAsJsonArray("genres");
                if (genresArray == null || genresArray.isJsonNull()) {
                    // Handle case when cover is not present
                    continue; // Skip this iteration if coverElement is null
                }
                ArrayList<String> genreNames = new ArrayList<>();
                for (JsonElement element : genresArray) {
                    JsonObject genreObject = element.getAsJsonObject();
                    String genreName = genreObject.get("name").getAsString();
                    genreNames.add(genreName);
                }
                // fetch genre list
                //fetch platform list
                JsonArray platformsArray = obj.getAsJsonArray("platforms");
                if (platformsArray == null || platformsArray.isJsonNull()) {
                    // Handle case when cover is not present
                    continue; // Skip this iteration if coverElement is null
                }
                ArrayList<String> platformNames = new ArrayList<>();
                for (JsonElement element : platformsArray) {
                    JsonObject platformObject = element.getAsJsonObject();
                    String platformName = platformObject.get("name").getAsString();
                    platformNames.add(platformName);
                }

                //fetch platform list
                JsonElement gameSummery = obj.get("summary");
                if (gameSummery == null || gameSummery.isJsonNull()) {
                    // Handle case when cover is not present
                    continue; // Skip this iteration if coverElement is null
                }


                String game_ID = gameID.toString();
                String game_Name = gameName.toString();
                String image_URL = imageUrl.toString();
                String formattedDateTime = null; // Declare formattedDateTime outside of if statement block
                String yearString = null;
                if (first_release_date != null && !first_release_date.isJsonNull()) {
                    int release_Date;
                    release_Date = first_release_date.getAsInt();
                    Instant instant = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        instant = Instant.ofEpochSecond(release_Date);
                    }

                    // Convert Instant to LocalDateTime
                    LocalDateTime dateTime = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    }

                    // Format LocalDateTime as desired
                    DateTimeFormatter formatter = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        formattedDateTime = dateTime.format(formatter); // Assign value to formattedDateTime
                        int year = dateTime.getYear();
                        yearString = String.valueOf(year);
                    }
                }
                String game_Summery = gameSummery.toString();
                Game game = new Game(game_ID,image_URL,yearString,genreNames,platformNames,game_Summery,game_Name);
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference gamesRef = database.getReference("games");
                String gameId = game.getId();

                gamesRef.child(gameId).setValue(game, (error, ref) -> {
                    if (error != null) {
                        System.out.println("Data could not be saved: " + error.getMessage());
                    } else {
                        System.out.println("Data saved successfully.");
                    }
                });
                gamesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long numberOfGames = dataSnapshot.getChildrenCount();
                        System.out.println("Number of games in the database: " + numberOfGames);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("Error getting number of games: " + databaseError.getMessage());
                    }
                });

            }

            // Get the number of results
            //int numberOfResults = rootArray.size();

            //System.out.println("Number of results: " + numberOfResults);

        } else {
            System.out.println("Error: HTTP response code " + responseCode);
        }

        connection.disconnect();
    }
}
