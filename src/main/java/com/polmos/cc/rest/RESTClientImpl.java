package com.polmos.cc.rest;

import javax.json.JsonArray;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;

import org.jboss.logging.Logger;

/**
 *
 * @author RobicToNieMaKomu
 */
public class RESTClientImpl implements RESTClient {

    private static final Logger logger = Logger.getLogger(RESTClientImpl.class);
    private static final String CONTENT_TYPE = "application/json";

    public RESTClientImpl() {
    }

    @Override
    public JsonArray sendGetRequest(String url) {
    	JsonArray result = null;
        if (url != null && !url.isEmpty()) {
            logger.info("Sending get request to url:" + url);
            try {
                Client client = ClientBuilder.newClient();
                Invocation request = client.target(url).request(CONTENT_TYPE).accept(CONTENT_TYPE).buildGet();
                result = request.invoke(JsonArray.class);
            } catch (Exception e) {
                logger.error("Couldnt get resource", e);
            }
        }
        return result;
    }
}
