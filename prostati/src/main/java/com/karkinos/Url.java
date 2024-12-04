package com.karkinos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Url {
    
    private static final String BASE_URL = "http://telematics.oasa.gr/api/";

    protected static String urlCreator(String endpoint) {
        try {
            // Construct the complete URL
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP GET Request Failed with Error code : " + conn.getResponseCode());
            }

            // Read the response
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line);
            }
            conn.disconnect();

            // Return the raw response as a String
            return output.toString();  // This is the raw response

        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Return null in case of error
        }
    }
}

/* 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Url {
    
    private static final String BASE_URL = "http://telematics.oasa.gr/api/";

    protected static String urlCreator(String endpoint) {
        try {
            // Construct the complete URL
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP GET Request Failed with Error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line);
            }
            conn.disconnect();

            // Parse the JSON response
            JsonObject jsonResponse = JsonParser.parseString(output.toString()).getAsJsonObject();
            return jsonResponse.toString();  // Return the raw JSON response

        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Return null in case of error
        }
    }

} */