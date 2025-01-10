package com.karkinos;

import java.util.List;
import java.util.Map;

public class NodeHandling {
    
    public static Node findClosestNode(Map<String, Node> nodes, double targetLat, double targetLon) {
        Node closestNode = null;
        double minDistance = Double.MAX_VALUE;

        for (Node node : nodes.values()) {
            // Calculate the distance between the node and the target coordinates
            double distance = calculateDistance(node, targetLat, targetLon);
            if (distance < minDistance) {
                minDistance = distance;
                closestNode = node;
            }
        }

        return closestNode;
    }

    public static Node findClosestNode(List<Edge> edges, double targetLat, double targetLon) {
        Node closestNode = null;
        double minDistance = Double.MAX_VALUE;

        for (Edge edge : edges) {
            Node[] nodes = {edge.getFrom(), edge.getTo()};
            for (Node node : nodes) {
                double distance = calculateDistance(node, targetLat, targetLon);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestNode = node;
                }
            }
        }

        return closestNode;
    }

    public static double calculateDistance(Node from, Node to) {
        return Math.sqrt(Math.pow(from.getLat() - to.getLat(), 2) + Math.pow(from.getLon() - to.getLon(), 2));
    }

    public static double calculateDistance(Node from, double lat, double lon) {
        final int EARTH_RADIUS = 6371; // Radius in kilometers
        double latDiff = Math.toRadians(lat - from.getLat());
        double lonDiff = Math.toRadians(lon - from.getLon());
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                   Math.cos(Math.toRadians(from.getLat())) * Math.cos(Math.toRadians(lat)) *
                   Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
