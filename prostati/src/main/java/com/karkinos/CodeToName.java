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
        String[] xy = {"-1", "-1"};
        if (!"null".equals(Url.urlCreator(endpoint))) {
            String x = getExactInfoForOne(Url.urlCreator(endpoint), "stop_lat");
            String y = getExactInfoForOne(Url.urlCreator(endpoint), "stop_lng");
            xy[0] = x;
            xy[1] = y;
        }
        return xy;
    }

    public String getLineName(String lineCode) {
        String endpoint = "?act=getLineName&p1=" + lineCode;
        String info = null;
        if (!"null".equals(Url.urlCreator(endpoint))) {
            info = getExactInfoForOne(Url.urlCreator(endpoint), "line_descr");
        }
        return info;
    }

    public String getMLName(String MLCode) {
        String endpoint = "?act=getMLName&p1" + MLCode;
        String info = null;
        if (!"null".equals(Url.urlCreator(endpoint))) {
            info = getExactInfoForOne(Url.urlCreator(endpoint), "ml_descr");
        }
        return info;
    }

    public String getRouteName(String routeCode) {
        String endpoint = "?act=getRouteName&p1=" + routeCode;
        String info = null;
        if (!"null".equals(Url.urlCreator(endpoint))) {
            info = getExactInfoForOne(Url.urlCreator(endpoint), "route_descr");
        }
        return info;
    }

    public String getExactInfoForOne(String urlItem, String object) {
        var jsonArray = new JSONArray(urlItem);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String info = jsonObject.getString(object);
        return info;
    }
}
