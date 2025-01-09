package com.karkinos;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        var x = new CodeToName();
        var y = new JSONtoString();
        /* 
        System.out.println(x.getStopName("450028"));
        System.out.println(x.getStopXY("110071")[0]);
        System.out.println(x.getStopXY("110071")[1]);
        System.out.println(x.getLineName("871"));
        System.out.println(x.getRouteName("1881"));
        for (String z : y.getStopInfo("240033")) {
            System.out.println(z);
        }*/
        System.out.println("37.951596, 23.695745");
        //System.out.println(NodeHandling.findClosestNode(MapCreator.parseNodesFromGeoJson(), 37.951596, 23.695745).lon); 
        Map<String, Node> nodes = MapCreator.parseNodesFromGeoJson();/* 
        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
            String key = entry.getKey();
            Node node = entry.getValue();
            
            System.out.println("ID: " + key + ", Node: " + node);
        } */ 
        List<Edge> edges = MapCreator.parseEdgesFromGeoJson(); /* 
        for (Edge edge : edges) {  
            System.out.println(edge);
        } */
        System.out.println(edges.size()); 
        System.out.println(nodes.size());
        System.out.println(NodeHandling.findClosestNode(edges, 38.064525, 23.826156));
        String outputFilePath = System.getProperty("user.home") + "/all_edges.geojson";
        var asoee = new Node("asoee", 37.994124, 23.731996);
        var t1 = new Node("t1", 37.994050, 23.731982);
        var t2 = new Node("t2", 38.057299, 23.708586);
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write(edges.toString());
        }
        System.out.println(ShortestPath.findShortestPath(asoee, t2, edges));
        outputFilePath = System.getProperty("user.home") + "/exported_data.geojson";
        GeoJsonExporter.exportShortestPathToGeoJson(ShortestPath.findShortestPath(t1, t2, edges), outputFilePath);
        ShortestPath.checkGraphConnectivity(edges);
        //outputFilePath = System.getProperty("user.home") + "/test_data.geojson";
        //GeoJsonExporter.exportToGeoJson(nodes, edges, outputFilePath);
    }
}
