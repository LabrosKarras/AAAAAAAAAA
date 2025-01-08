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

    // Iterate over all edges and compare both "from" and "to" nodes
    for (Edge edge : edges) {
        // Check the "from" node
        Node fromNode = edge.getFrom();
        double distanceFrom = calculateDistance(fromNode, targetLat, targetLon);
        if (distanceFrom < minDistance) {
            minDistance = distanceFrom;
            closestNode = fromNode;
        }

        // Check the "to" node
        Node toNode = edge.getTo();
        double distanceTo = calculateDistance(toNode, targetLat, targetLon);
        if (distanceTo < minDistance) {
            minDistance = distanceTo;
            closestNode = toNode;
        }
    }

    return closestNode;
}

    public static double calculateDistance(Node from, Node to) {
        return Math.sqrt(Math.pow(from.getLat() - to.getLat(), 2) + Math.pow(from.getLon() - to.getLon(), 2));
    }

    private static double calculateDistance(Node node, double lat, double lon) {
        return Math.sqrt(Math.pow(node.getLat() - lat, 2) + Math.pow(node.getLon() - lon, 2));
    }
}
