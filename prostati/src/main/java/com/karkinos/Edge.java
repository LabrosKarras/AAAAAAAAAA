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

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Edge{from=" + from + ", to=" + to + ", distance=" + distance + "}";
    }
}
