package com.polmos.cc.rest;

import java.io.IOException;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 *
 * @author RobicToNieMaKomu
 */
public interface RequestProcessor {
    
    JsonObject processRequest(int id) throws IOException;
    
    int processRequest(JsonArray rawTimeSeries, String type) throws IOException;
}
