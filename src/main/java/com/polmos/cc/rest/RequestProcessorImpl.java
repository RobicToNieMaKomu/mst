package com.polmos.cc.rest;

import com.polmos.cc.constants.OperationType;
import com.polmos.cc.service.JsonUtils;
import com.polmos.cc.service.mst.CalculatedGraphs;
import com.polmos.cc.service.mst.MSTService;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.jboss.logging.Logger;

/**
 *
 * @author RobicToNieMaKomu
 */
public class RequestProcessorImpl implements RequestProcessor {

    private static final Logger logger = Logger.getLogger(RequestProcessorImpl.class);
    @Inject
    private MSTService mstService;
    @Inject
    private JsonUtils jsonUtils;
    @Inject
    private CalculatedGraphs calculatedGraphs;

    @Override
    public JsonObject processRequest(int id) throws IOException {
        return calculatedGraphs.getGraph(id);
    }
    
    @Override
    public int processRequest(JsonArray rawTimeSeries, String type) throws IOException {
        int graphId = -1;
        OperationType operationType = validateInput(rawTimeSeries, type);
        try {
            Map<String, Set<String>> mst = mstService.generateMST(jsonUtils.convertJsonArray(rawTimeSeries), operationType);
            graphId = calculatedGraphs.putGraph(jsonUtils.convertMap(mst));
        } catch (IOException e) {
            logger.error("Exception occurred during processing mst", e);
        }
        return graphId;
    }

    private OperationType validateInput(JsonArray timeSeries, String type) throws IOException {
        if (timeSeries == null) {
            throw new IOException("Invalid input. Range should be equal to or greater than 0");
        }
        OperationType opType = OperationType.toOperationType(type);
        if (opType == null) {
            throw new IOException("Invalid input. Operation type should be bid or ask");
        }
        return opType;
    }
}