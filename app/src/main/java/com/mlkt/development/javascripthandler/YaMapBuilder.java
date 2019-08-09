package com.mlkt.development.javascripthandler;

import java.util.ArrayList;
import java.util.List;

public class YaMapBuilder {

    private String header = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
            "<script src=\"https://api-maps.yandex.ru/2.1/?lang=ru_RU\" type=\"text/javascript\">\n" +
            "</script>\n" +
            "<script type=\"text/javascript\">\n" +
            "ymaps.ready(init);\n";

    private String footer = " </script>\n" +
            "</head>\n" +
            "<body>\n" +
            "<div id=\"map\" style=\"width: %1dpx; height: %2dpx\"></div>\n" +
            "</body>\n" +
            "</html>";

    private String newMap = "";
    private String myMapStart = "myMap = new ymaps.Map(\"map\", {%1s zoom: 12, controls: [\"geolocationControl\"] }); ";
    private String mapCenter = "center: [%1s, %2s],";

    private String placeMarkTemplate = "myPlacemark%1d = new ymaps.Placemark([%1s, %2s], {\n" +
            "hintContent: '%3s',\n" +
            "}, " +
            "{\n" +
            "preset: 'islands#dotIcon',\n" +
            "iconColor: '%3s'\n" +
            "}" +
            ");";

    private String placeMarkClickListener = "myPlacemark%1d.events.add('click', function () {\n" +
            "Android.returnResult('%1s');\n" +
            "});"+
            "myMap.geoObjects.add(myPlacemark%1d);";

    private String function = "function init(){ Android.returnResult('init'); %1s }";

    private List<String> placeMarkList = new ArrayList<>();
    private List<String> placeMarkClickListenersList = new ArrayList<>();

    public YaMapBuilder(int width, int height){
        footer = String.format(footer, width,height);
    }

    public YaMapBuilder addMap(String latitude, String longitude) {
        mapCenter = String.format(mapCenter,latitude,longitude);
        newMap = String.format(myMapStart,mapCenter);
        return this;
    }

    public YaMapBuilder addPlaceMark(int id, String latitude, String longitude, String hintContent, String color){
        placeMarkList.add(String.format(placeMarkTemplate,id,latitude,longitude,hintContent, color));
        return this;
    }

    public YaMapBuilder addPlaceMarkListener(int id, String callback) {
        placeMarkClickListenersList.add(String.format(placeMarkClickListener,id,callback,id));
        return this;
    }

    public String getMap() {
        StringBuilder bodyFunction = new StringBuilder();
        bodyFunction.append(newMap);
        for (String placeMark : placeMarkList) {
            bodyFunction.append(placeMark);
        }
        for (String placeMarkListeners : placeMarkClickListenersList) {
            bodyFunction.append(placeMarkListeners);
        }
        return header+String.format(function, bodyFunction.toString())+footer;
    }
}
