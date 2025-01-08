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
    } */

    public static List<Edge> parseEdgesFromGeoJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("prostati/src/main/resources/athens_transport_data.geojson"));
        JsonNode elements = root.get("features");
        List<Edge> edges = new ArrayList<>();
        Map<String, Node> nodeMap = new HashMap<>();
        AtomicInteger idCounter = new AtomicInteger(1); // Use AtomicInteger for mutable counter
    
        for (JsonNode element : elements) {
            String geometryType = element.get("geometry").get("type").asText();
    
            if ("LineString".equals(geometryType)) {
                // Process LineString as before
                JsonNode coordinates = element.get("geometry").get("coordinates");
                edges.addAll(processCoordinates(coordinates, nodeMap, idCounter));
            } else if ("Polygon".equals(geometryType)) {
                // Process Polygon by using the exterior ring
                JsonNode coordinates = element.get("geometry").get("coordinates");
                if (coordinates.isArray() && coordinates.size() > 0) {
                    // Use the first ring (exterior boundary) for edges
                    JsonNode exteriorRing = coordinates.get(0);
                    edges.addAll(processCoordinates(exteriorRing, nodeMap, idCounter));
                }
            }
        }
        return edges;
    }
    
    /**
     * Processes a set of coordinates to create edges.
     * @param coordinates The coordinates array.
     * @param nodeMap A map for tracking unique nodes.
     * @param idCounter A counter for generating unique IDs.
     * @return A list of edges created from the coordinates.
     */
    private static List<Edge> processCoordinates(JsonNode coordinates, Map<String, Node> nodeMap, AtomicInteger idCounter) {
        List<Edge> edges = new ArrayList<>();
        Node prevNode = null;
    
        for (JsonNode coordinate : coordinates) {
            double lon = coordinate.get(0).asDouble();
            double lat = coordinate.get(1).asDouble();
    
            // Generate a unique key for the coordinate
            String key = String.format("%.6f,%.6f", lat, lon);
    
            // Retrieve or create a node with a unique ID
            Node currentNode = nodeMap.computeIfAbsent(key, k -> new Node("L" + idCounter.getAndIncrement(), lat, lon));
    
            if (prevNode != null && !prevNode.id.equals(currentNode.id)) {
                double distance = NodeHandling.calculateDistance(prevNode, currentNode);
                edges.add(new Edge(prevNode, currentNode, distance));
            }
    
            prevNode = currentNode;
        }
        return edges;
    }
    
}
