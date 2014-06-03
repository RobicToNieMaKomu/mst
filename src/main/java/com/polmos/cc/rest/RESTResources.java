package com.polmos.cc.rest;

import com.polmos.cc.constants.Constants;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.jboss.logging.Logger;

/**
 *
 * @author RobicToNieMaKomu
 */
@Path("/")
@Produces("application/json")
@Consumes("application/json")
public class RESTResources {

    private static final Logger logger = Logger.getLogger(RESTResources.class);
    private static final String HTTP_HEADER = "http://";
    private static final String SERVICE_NAME = "rest/mst/";
    private static final String URL_TO_RESOURCES = HTTP_HEADER + System.getProperty(Constants.OPENSHIFT_APP_DNS_PROPERTY) + SERVICE_NAME;

    @Inject
    private RequestProcessor processor;

    @GET
    @Path("/mst/{id}")
    public Response getMST(@QueryParam("id") int id) {
        Response response = null;
        try {
            JsonObject graph = processor.processRequest(id);
            if (graph == null) {
                response = Response.status(Status.NOT_FOUND).build();
            } else {
                response = Response.ok(graph).build();
            }
        } catch (IOException ex) {
            logger.error("Exception while processing REST call", ex);
            response = Response.status(Status.BAD_REQUEST).build();
        }
        return response;
    }

    @POST
    @Path("/mst/{type}")
    public Response createMst(final JsonArray body, @PathParam("type") String type) {
        Response response = null;
        try {
            int id = processor.processRequest(body, type);
            logger.info("APP DNS:" + System.getProperty(Constants.OPENSHIFT_APP_DNS_PROPERTY));
            logger.info("Full Url:" + URL_TO_RESOURCES);
            response = Response.created(new URI(URL_TO_RESOURCES + id)).build();
        } catch (IOException | URISyntaxException ex) {
            logger.error("Exception while processing REST call", ex);
            response = Response.status(Status.BAD_REQUEST).build();
        }
        return response;
    }
}
