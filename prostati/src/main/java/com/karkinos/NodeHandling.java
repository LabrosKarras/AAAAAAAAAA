package com.karkinos;

import java.util.Map;

public class NodeHandling {
    
    public static Node findClosestNode(Map<Long, Node> nodes, double targetLat, double targetLon) {
        Node closestNode = null;
        double minDistance = Double.MAX_VALUE;

        for (Node node : nodes.values()) {
            // Calculate the distance between the node and the target coordinates
            double distance = calculateDistance(node, new Node("-1", targetLat, targetLon));
            if (distance < minDistance) {
                minDistance = distance;
                closestNode = node;
            }
        }

        return closestNode;
    }

    public static double calculateDistance(Node from, Node to) {
        final double R = 6371; // Radius of the Earth in kilometers
        double lat1 = Math.toRadians(from.lat);
        double lon1 = Math.toRadians(from.lon);
        double lat2 = Math.toRadians(to.lat);
        double lon2 = Math.toRadians(to.lon);

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c * 1000; // Convert to meters
    }
}
