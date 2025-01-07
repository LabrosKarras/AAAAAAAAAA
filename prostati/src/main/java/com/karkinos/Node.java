package com.karkinos;

class Node {
    public String id;
    public double lat;
    public double lon;

    public Node(String id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return "Node{id=" + id + ", lat=" + lat + ", lon=" + lon + "}";
    }
}
