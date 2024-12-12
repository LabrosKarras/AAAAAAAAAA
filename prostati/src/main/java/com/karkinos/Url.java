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

            // Get the HTTP response code
            int responseCode = conn.getResponseCode();

            // If the server returns 500 (Internal Server Error), return null
            if (responseCode == 500) {
                System.out.println("Error 500: Internal Server Error.");
                return null;  // Return null when encountering a 500 error
            }

            // If the response code is not 200 (OK), handle it as an error
            if (responseCode != 200) {
                System.out.println("HTTP GET Request Failed with Error code: " + responseCode);
                return null;  // Return null for other non-200 status codes
            }

            // Read the response if successful (200 OK)
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line);
            }
            conn.disconnect();

            // Return the raw response as a String
            return output.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Return null in case of any exceptions
        }
    }
}

