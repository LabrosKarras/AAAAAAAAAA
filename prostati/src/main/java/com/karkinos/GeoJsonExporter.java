package com.karkinos;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GeoJsonExporter {

    public static void exportToGeoJson(Map<String, Node> nodes, List<Edge> edges, String outputFilePath) throws IOException {
        StringBuilder geoJson = new StringBuilder("{ \"type\": \"FeatureCollection\", \"features\": [");

        // Add Nodes
        for (Node node : nodes.values()) {
            geoJson.append("{")
                .append("\"type\": \"Feature\",")
                .append("\"geometry\": {")
                .append("\"type\": \"Point\",")
                .append("\"coordinates\": [")
                .append(node.lon).append(",").append(node.lat)
                .append("]},")
                .append("\"properties\": { \"id\": \"").append(node.id).append("\" }")
                .append("},");
        }

        // Add Edges
        for (Edge edge : edges) {
            geoJson.append("{")
                .append("\"type\": \"Feature\",")
                .append("\"geometry\": {")
                .append("\"type\": \"LineString\",")
                .append("\"coordinates\": [")
                .append("[").append(edge.from.lon).append(",").append(edge.from.lat).append("],")
                .append("[").append(edge.to.lon).append(",").append(edge.to.lat).append("]")
                .append("]},")
                .append("\"properties\": { \"distance\": ").append(edge.distance).append(" }")
                .append("},");
        }

        // Remove trailing comma and close the JSON
        if (geoJson.charAt(geoJson.length() - 1) == ',') {
            geoJson.setLength(geoJson.length() - 1);
        }
        geoJson.append("]}");

        // Write to file
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write(geoJson.toString());
        }
    }
}
