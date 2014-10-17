package com.polmos.cc.rest;

import com.polmos.cc.constants.OperationType;
import com.polmos.cc.service.JsonUtils;
import com.polmos.cc.service.mst.CalculatedGraphs;
import com.polmos.cc.service.mst.MSTService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        logger.info("requested graphId:" + id);
        return requestedDailyMST(id) ? calculatedGraphs.getDailyMST() : calculatedGraphs.getGraph(id);
    }

	private boolean requestedDailyMST(int id) {
		return id == -1;
	}
    
    @Override
    public int processRequest(JsonObject reqBody, String type) throws IOException {
        int graphId = -1;
        ParsedInput input = validateInput(reqBody, type);
        try {
            Map<String, Set<String>> mst = mstService.generateMST(input.getCurrencies(), jsonUtils.convertJsonArray(input.getTimeSeries()), input.getType());
            graphId = calculatedGraphs.putGraph(jsonUtils.convertMap(mst));
            logger.info("produced graphId:" + graphId);
        } catch (IOException e) {
            logger.error("Exception occurred during processing mst", e);
        }
        return graphId;
    }

    private ParsedInput validateInput(JsonObject reqBody, String type) throws IOException {
        throwIfBodyIsNull(reqBody);
        
        List<String> currencies = getCurrencies(reqBody);
        throwIfNoCurrencies(currencies);
        
        JsonArray timeSeries = reqBody.getJsonArray("data");
        throwIfNoTimeSeries(timeSeries);
        
        OperationType opType = OperationType.toOperationType(type);
        throwIfOperationTypeIsNull(opType);
        
        return new ParsedInput(opType, currencies, timeSeries);
    }

	private void throwIfBodyIsNull(JsonObject reqBody) throws IOException {
		if (reqBody == null) {
            throw new IOException("Null input body.");
        }
	}

	private void throwIfOperationTypeIsNull(OperationType opType)
			throws IOException {
		if (opType == null) {
            throw new IOException("Invalid input. Operation type should be bid or ask");
        }
	}

	private void throwIfNoTimeSeries(JsonArray timeSeries) throws IOException {
		if (timeSeries == null || timeSeries.isEmpty()) {
            throw new IOException("Invalid input. Expected at least one time series");
        }
	}

	private void throwIfNoCurrencies(List<String> currencies)
			throws IOException {
		if (currencies == null || currencies.isEmpty()) {
            throw new IOException("Invalid input. Expected at least one currency");
        }
	}
    
    private List<String> getCurrencies(JsonObject reqBody) {
        List<String> currencies = null;
        String rawCurrencies = reqBody.getString("currencies");
        if (rawCurrencies != null) {
            currencies = new ArrayList<>();
            currencies.addAll(Arrays.asList(rawCurrencies.split(",")));
        }
        return currencies;
    }
    
    private static class ParsedInput {
        private final OperationType type;
        private final List<String> currencies;
        private final JsonArray timeSeries;
        
        private ParsedInput(OperationType type, List<String> currencies, JsonArray timeSeries) {
            this.type = type;
            this.currencies = currencies;
            this.timeSeries = timeSeries;
        }
        
        private List<String> getCurrencies() {
            return new ArrayList<>(currencies);
        }
        
        private OperationType getType() {
            return type;
        }
        
        private JsonArray getTimeSeries() {
            return this.timeSeries;
        }
    }
}