package com.polmos.cc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author RobicToNieMaKomu
 */
public class JsonUtilsImpl implements JsonUtils {

    @Override
    public JsonObject convertMap(Map<String, Set<String>> map) {
        JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        if (map != null) {
            for (String key : map.keySet()) {
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                for (String item : map.get(key)) {
                    arrayBuilder.add(item);
                }
                objBuilder.add(key, arrayBuilder.build());
            }
        }
        return objBuilder.build();
    }

    @Override
    public List<JsonObject> convertJsonArray(JsonArray array) {
        List<JsonObject> output = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                output.add(array.getJsonObject(i));
            }
        }
        return output;
    }
}