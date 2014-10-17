package com.polmos.cc.rest;

import javax.json.JsonArray;

/**
 *
 * @author RobicToNieMaKomu
 */
public interface RESTClient {

	JsonArray sendGetRequest(String url);
}
