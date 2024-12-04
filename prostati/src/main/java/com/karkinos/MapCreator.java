package com.karkinos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapCreator {
    
    public static Map<Long, Node> parseNodesFromGeoJson() throws IOException {

        File jsonFile = new File("prostati/src/main/resources/athens_transport_data.geojson");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonFile);

        Map<Long, Node> nodes = new HashMap<>();

        // Parse nodes (Point type) and ways (LineString type)
        for (JsonNode element : rootNode.get("features")) {
            if (element.has("geometry")) {
                JsonNode geometry = element.get("geometry");

                if (geometry.has("type") && geometry.get("type").asText().equals("Point")) {
                    // Node type (bus stop, subway, etc.)
                    JsonNode coordinates = geometry.get("coordinates");
                    if (coordinates != null && coordinates.size() >= 2) {
                        long id = -1;
                        if (element.has("properties") && element.get("properties").has("id")) {
                            id = element.get("properties").get("id").asLong();
                        }
                        double lon = coordinates.get(0).asDouble();
                        double lat = coordinates.get(1).asDouble();
                        nodes.put(id, new Node(id, lat, lon));
                    }
                }
            }
        }
        return nodes;
    }

    // Method to parse the GeoJSON file and build the graph (edges between nodes)
    public static Map<Long, List<Edge>> parseGraphFromGeoJson(File jsonFile, Map<Long, Node> nodes) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonFile);
        var calc = new NodeHandling();
        Map<Long, List<Edge>> graph = new HashMap<>();

        // Parse ways (LineString type) and create edges between nodes
        for (JsonNode element : rootNode.get("features")) {
            if (element.has("geometry")) {
                JsonNode geometry = element.get("geometry");

                if (geometry.has("type") && geometry.get("type").asText().equals("LineString")) {
                    // Way type (path between nodes)
                    JsonNode coordinates = geometry.get("coordinates");
                    long wayId = -1;
                    if (element.has("properties") && element.get("properties").has("id")) {
                        wayId = element.get("properties").get("id").asLong();
                    }

                    List<Node> wayNodes = new ArrayList<>();
                    for (JsonNode coord : coordinates) {
                        double lon = coord.get(0).asDouble();
                        double lat = coord.get(1).asDouble();
                        Node currentNode = new Node(wayId, lat, lon);
                        wayNodes.add(currentNode);
                        nodes.putIfAbsent(wayId, currentNode);
                    }

                    // Create edges between consecutive nodes in the way
                    for (int i = 0; i < wayNodes.size() - 1; i++) {
                        Node from = wayNodes.get(i);
                        Node to = wayNodes.get(i + 1);
                        double distance = calc.calculateDistance(from, to);
                        Edge edge = new Edge(from, to, distance);
                        graph.putIfAbsent(from.id, new ArrayList<>());
                        graph.get(from.id).add(edge);

                        // If the graph is undirected, add the reverse edge as well
                        Edge reverseEdge = new Edge(to, from, distance);
                        graph.putIfAbsent(to.id, new ArrayList<>());
                        graph.get(to.id).add(reverseEdge);
                    }
                }
            }
        }
        return graph;
    }
}
