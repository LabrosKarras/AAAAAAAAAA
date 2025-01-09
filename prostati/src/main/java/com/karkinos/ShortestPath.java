package com.karkinos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class ShortestPath {

    public static List<Node> findShortestPath(Node start, Node end, List<Edge> edges) {
        // Validate edges
        validateEdges(edges);

        // Build adjacency list for efficient neighbor lookup
        Map<Node, List<Edge>> adjacencyList = buildAdjacencyList(edges);

        // Map to store the shortest distance to each node
        Map<Node, Double> distances = new HashMap<>();
        // Map to track predecessors for path reconstruction
        Map<Node, Node> predecessors = new HashMap<>();
        // Priority queue to process nodes by shortest distance
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(Comparator.comparingDouble(nd -> nd.distance));

        start = NodeHandling.findClosestNode(edges, start.lat, start.lon);
        end = NodeHandling.findClosestNode(edges, end.lat, end.lon);

        // Initialize distances to infinity and add start node
        distances.put(start, 0.0);
        pq.add(new NodeDistance(start, 0.0));

        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            Node currentNode = current.node;

            // Stop if we've reached the target node
            if (currentNode.equals(end)) {
                break;
            }

            // Explore neighbors using adjacency list
            List<Edge> neighbors = adjacencyList.getOrDefault(currentNode, Collections.emptyList());
            for (Edge edge : neighbors) {
                Node neighbor = edge.getTo();
                double newDist = distances.getOrDefault(currentNode, Double.MAX_VALUE) + edge.getDistance();

                // Update distance if new path is shorter
                if (newDist < distances.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, currentNode);
                    pq.add(new NodeDistance(neighbor, newDist));
                }
            }
        }

        // Reconstruct path
        return reconstructPath(predecessors, start, end);
    }

    private static List<Node> reconstructPath(Map<Node, Node> predecessors, Node start, Node end) {
        List<Node> path = new LinkedList<>();
        for (Node at = end; at != null; at = predecessors.get(at)) {
            path.add(0, at);
        }

        // If start node isn't in the path, no path was found
        if (!path.isEmpty() && path.get(0).equals(start)) {
            return path;
        } else {
            return Collections.emptyList(); // No path found
        }
    }

    private static Map<Node, List<Edge>> buildAdjacencyList(List<Edge> edges) {
        Map<Node, List<Edge>> adjacencyList = new HashMap<>();
        for (Edge edge : edges) {
            adjacencyList.computeIfAbsent(edge.getFrom(), k -> new ArrayList<>()).add(edge);
        }
        return adjacencyList;
    }

    private static void validateEdges(List<Edge> edges) {
        Set<Node> uniqueNodes = new HashSet<>();
        for (Edge edge : edges) {
            if (edge.getFrom() == null || edge.getTo() == null) {
                throw new IllegalArgumentException("Edge contains null nodes: " + edge);
            }
            uniqueNodes.add(edge.getFrom());
            uniqueNodes.add(edge.getTo());

            if (edge.getDistance() <= 0) {
                throw new IllegalArgumentException("Edge has invalid distance: " + edge);
            }
        }
        System.out.println("Validation complete. Total unique nodes: " + uniqueNodes.size());
    }

    public static void checkGraphConnectivity(List<Edge> edges) {
        Map<Node, List<Edge>> adjacencyList = buildAdjacencyList(edges);
        Set<Node> visited = new HashSet<>();

        // Start traversal from any node
        Node startNode = edges.get(0).getFrom();
        dfs(adjacencyList, startNode, visited);

        // Check if all nodes are visited
        Set<Node> allNodes = new HashSet<>();
        for (Edge edge : edges) {
            allNodes.add(edge.getFrom());
            allNodes.add(edge.getTo());
        }

        if (visited.containsAll(allNodes)) {
            System.out.println("The graph is fully connected.");
        } else {
            allNodes.removeAll(visited);
            System.out.println("The following nodes are disconnected: " + allNodes);
        }
    }

    private static void dfs(Map<Node, List<Edge>> adjacencyList, Node current, Set<Node> visited) {
        if (visited.contains(current)) return;
        visited.add(current);

        for (Edge edge : adjacencyList.getOrDefault(current, Collections.emptyList())) {
            dfs(adjacencyList, edge.getTo(), visited);
        }
    }

    // Helper class to store node-distance pairs for the priority queue
    private static class NodeDistance {
        Node node;
        double distance;

        NodeDistance(Node node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }

    // Updated distance calculation using Haversine formula
    public static class NodeHandling {
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
    }
}
