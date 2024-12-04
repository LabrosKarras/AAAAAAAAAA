package com.karkinos;

class Edge {
    public Node from;
    public Node to;
    public double distance;

    public Edge(Node from, Node to, double distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }
}
