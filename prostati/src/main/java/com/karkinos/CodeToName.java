package com.karkinos;

import org.json.JSONArray;
import org.json.JSONObject;

public class CodeToName extends Url {

    public String getStopName(String stopCode) {
        String endpoint = "?act=getStopNameAndXY&p1=" + stopCode;
        String info = null;
        if (!"null".equals(Url.urlCreator(endpoint))) {
            info = getExactInfoForOne(Url.urlCreator(endpoint), "stop_descr");
        }
        return info;
    }

    public String[] getStopXY(String stopCode) {
        String endpoint = "?act=getStopNameAndXY&p1=" + stopCode;
        String x = getExactInfoForOne(Url.urlCreator(endpoint), "stop_lat");
        String y = getExactInfoForOne(Url.urlCreator(endpoint), "stop_lng");
        String[] xy = {x, y};
        return xy;
    }

    public String getLineName(String lineCode) {
        String endpoint = "?act=getLineName&p1=" + lineCode;
        return getExactInfoForOne(Url.urlCreator(endpoint), "line_descr");
    }

    public String getMLName(String MLCode) {
        String endpoint = "?act=getMLName&p1" + MLCode;
        return getExactInfoForOne(Url.urlCreator(endpoint), "ml_descr");
    }

    public String getRouteName(String routeCode) {
        String endpoint = "?act=getRouteName&p1=" + routeCode;
        return getExactInfoForOne(Url.urlCreator(endpoint), "route_descr");
    }

    public String getExactInfoForOne(String urlItem, String object) {
        var jsonArray = new JSONArray(urlItem);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String info = jsonObject.getString(object);
        return info;
    }
}
