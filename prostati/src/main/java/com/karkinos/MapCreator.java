package com.karkinos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapCreator {
    
    public static Map<String, Node> parseNodesFromGeoJson() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("prostati/src/main/resources/athens_transport_data.geojson"));
        var cords = new CodeToName();
        JsonNode elements = root.get("features");
        Map<String, Node> nodes = new HashMap<>();
        
        for (JsonNode element : elements) {
            if (element.has("properties") && element.get("properties").has("ref")) {
                String id = "-1";
                String ref = element.get("properties").get("ref").toString();
                double lat = -1;
                double lon = -1;
                ref = ref.substring(1, ref.length() - 1);
                if (ref.matches("\\d{6}")) {
                    id = ref;
                    //System.out.println(cords.getStopXY(ref)[0]);
                    //lat = Double.parseDouble(cords.getStopXY(ref)[0]);
                    //lon = Double.parseDouble(cords.getStopXY(ref)[1]);
                    lat = element.get("geometry").get("coordinates").get(1).asDouble();
                    lon = element.get("geometry").get("coordinates").get(0).asDouble();
                    //System.out.println(lat + " " + lon);
                    nodes.put(id, new Node(id, lat, lon));
                }
            }
        }
        return nodes;
    }
/* 
    public static List<Edge> parseEdgesFromGeoJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("prostati/src/main/resources/athens_transport_data.geojson"));
        JsonNode elements = root.get("features");
        List<Edge> edges = new ArrayList<>();
        int i = 1;

        for (JsonNode element : elements) {
            if ("LineString".equals(element.get("geometry").get("type").asText())) {
                JsonNode coordinates = element.get("geometry").get("coordinates");
                Node prevNode = null;

                for (JsonNode coordinate : coordinates) {
                    double lon = coordinate.get(0).asDouble();
                    double lat = coordinate.get(1).asDouble();
                    String id = "L" + i;
                    // Find the nearest node for the coordinate
                    Node currentNode = new Node(id , lat, lon);
                    if (prevNode != null && currentNode != null && !prevNode.id.equals(currentNode.id)) {
                        double distance = NodeHandling.calculateDistance(prevNode, currentNode);
                        boolean doesItExist = false;
                        for (Edge edge : edges) {
                            if (currentNode.lat == edge.from.lat && currentNode.lon == edge.from.lon) {
                                currentNode.id = edge.from.id;
                                doesItExist = true;
                                break;
                            } else if (currentNode.lat == edge.to.lat && currentNode.lon == edge.to.lon) {
                                currentNode.id = edge.to.id;
                                doesItExist = true;
                                break;
                            }
                        }
                        if (doesItExist == false) {
                            i++;
                        }
                        edges.add(new Edge(prevNode, currentNode, distance));
                    } else {
                        i++;
                    }
                    prevNode = currentNode;
                }
            }
        }
        return edges;
    } */
   
    public static List<Edge> parseEdgesFromGeoJson() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(new File("prostati/src/main/resources/athens_transport_data.geojson"));
    JsonNode elements = root.get("features");
    List<Edge> edges = new ArrayList<>();
    Map<String, Node> nodeMap = new HashMap<>();
    AtomicInteger idCounter = new AtomicInteger(1); // Use AtomicInteger for mutable counter

    for (JsonNode element : elements) {
        if ("LineString".equals(element.get("geometry").get("type").asText())) {
            JsonNode coordinates = element.get("geometry").get("coordinates");
            Node prevNode = null;

            for (JsonNode coordinate : coordinates) {
                double lon = coordinate.get(0).asDouble();
                double lat = coordinate.get(1).asDouble();

                // Generate a unique key for the coordinate
                String key = String.format("%.6f,%.6f", lat, lon); // Round coordinates for precision

                // Retrieve or create a node with a unique ID
                Node currentNode = nodeMap.computeIfAbsent(key, k -> new Node("L" + idCounter.getAndIncrement(), lat, lon));

                if (prevNode != null && !prevNode.id.equals(currentNode.id)) {
                    double distance = NodeHandling.calculateDistance(prevNode, currentNode);
                    edges.add(new Edge(prevNode, currentNode, distance));
                }

                prevNode = currentNode;
            }
        }
    }
    return edges;
} 
}
