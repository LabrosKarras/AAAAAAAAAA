package com.karkinos;

//import java.lang.classfile.components.ClassPrinter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class ShortestPath {

    public static List<Node> findShortestPath(Node start, Node end, List<Edge> edges) {
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

            // Explore neighbors
            for (Edge edge : edges) {
                if (edge.getFrom().equals(currentNode)) {
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
        }

        // Reconstruct path
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

    // Helper class to store node-distance pairs for the priority queue
    private static class NodeDistance {
        Node node;
        double distance;

        NodeDistance(Node node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }
}

