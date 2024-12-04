package com.karkinos;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        var x = new CodeToName();
        var y = new JSONtoString();
        System.out.println(x.getStopName("400075"));
        System.out.println(x.getStopXY("400075")[0]);
        System.out.println(x.getStopXY("400075")[1]);
        System.out.println(x.getLineName("871"));
        System.out.println(x.getRouteName("1881"));
        for (String z : y.getStopInfo("240033")) {
            System.out.println(z);
        }
        System.out.println("37.951596, 23.695745");
        System.err.println(NodeHandling.findClosestNode(MapCreator.parseNodesFromGeoJson(), 37.951596, 23.695745).lon);
    }
}
