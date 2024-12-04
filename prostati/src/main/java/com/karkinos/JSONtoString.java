package com.karkinos;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONtoString extends CodeToName {
    public List<String> getExactInfo(String urlItem, String object1, String object2) {
        var jsonArray = new JSONArray(urlItem);
        List<String> infoList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.has(object1)) { 
                infoList.add(jsonObject.getString(object1));
                infoList.add(jsonObject.getString(object2));
            }
        }
        return infoList;
    }

    public List<String> getStopInfo(String stopCode) {
        List<String> stopInfoList = new ArrayList<>();
        String endpoint = "?act=getStopArrivals&p1=" + stopCode;
        String urlItem = Url.urlCreator(endpoint);
        stopInfoList.add(getStopName(stopCode));
        if (!"null".equals(urlItem)) {
            for (int i = 0; i < getExactInfo(urlItem, "route_code", "btime2").size(); i++) {
                String routeOrTime = getExactInfo(urlItem, "route_code", "btime2").get(i);
                if (i%2 == 0) {
                    stopInfoList.add(getRouteName(routeOrTime));
                } else {
                    stopInfoList.add(routeOrTime);
                } 
            }
        } else {
            stopInfoList.add("null");
        }
        return stopInfoList;
    }

    public String test(String stopCode) {
        String endpoint = "?act=getStopArrivals&p1=" + stopCode;
        String urlItem = Url.urlCreator(endpoint);
        return urlItem;
    }
}