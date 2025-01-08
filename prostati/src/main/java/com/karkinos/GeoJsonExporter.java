package com.karkinos;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GeoJsonExporter {

    /**
     * Exports a shortest path to GeoJSON format.
     *
     * @param pathNodes      List of nodes representing the shortest path.
     * @param outputFilePath The file path where the GeoJSON will be saved.
     * @throws IOException If there's an error writing the file.
     */
    public static void exportShortestPathToGeoJson(List<Node> pathNodes, String outputFilePath) throws IOException {
        if (pathNodes == null || pathNodes.isEmpty()) {
            throw new IllegalArgumentException("Path nodes cannot be null or empty.");
        }

        StringBuilder geoJson = new StringBuilder("{ \"type\": \"FeatureCollection\", \"features\": [");

        // Add Nodes
        for (Node node : pathNodes) {
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

        // Add Edges (connect consecutive nodes)
        for (int i = 0; i < pathNodes.size() - 1; i++) {
            Node from = pathNodes.get(i);
            Node to = pathNodes.get(i + 1);

            geoJson.append("{")
                .append("\"type\": \"Feature\",")
                .append("\"geometry\": {")
                .append("\"type\": \"LineString\",")
                .append("\"coordinates\": [")
                .append("[").append(from.lon).append(",").append(from.lat).append("],")
                .append("[").append(to.lon).append(",").append(to.lat).append("]")
                .append("]},")
                .append("\"properties\": {}") // No extra properties for edges in the path
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
