package com.polmos.cc.service.scheduler;

import static com.polmos.cc.constants.OperationType.ASK;
import static com.polmos.cc.service.ResourceManager.allCurrencies;
import static com.polmos.cc.service.ResourceManager.toList;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.json.JsonArray;

import org.jboss.logging.Logger;

import com.polmos.cc.rest.RESTClient;
import com.polmos.cc.service.JsonUtils;
import com.polmos.cc.service.mst.CalculatedGraphs;
import com.polmos.cc.service.mst.MSTService;

public class DailyMSTUpdater implements Runnable {

	private static final Logger LOG = Logger.getLogger(DailyMSTUpdater.class);
	private static final String ALL_CURRENCIES_COMMA_SEPARATED = allCurrencies();
	private static final List<String> ALL_CURRENCIES = toList(ALL_CURRENCIES_COMMA_SEPARATED);
	private static final String URL_TO_TIME_SERIES = "http://cc-comparator.rhcloud.com/rest/mst?type=ask&range=1&currencies=" + ALL_CURRENCIES_COMMA_SEPARATED;
	
	@Inject
	private RESTClient client;
	
	@Inject
	private JsonUtils jsonUtils;
	
	@Inject
	private MSTService mstService;
	
	@Inject
	private CalculatedGraphs calculatedGraphs;
	
	@Override
	public void run() {
		LOG.info("Scheduled daily MST update has been launched");
		boolean updated = false;
		
		JsonArray rawTimeSeries = client.sendGetRequest(URL_TO_TIME_SERIES);
		try {
			Map<String, Set<String>> mst = mstService.generateMST(ALL_CURRENCIES, jsonUtils.convertJsonArray(rawTimeSeries), ASK);
			updated = calculatedGraphs.putDailyMST(jsonUtils.convertMap(mst));
		} catch (Exception e) {
			LOG.error("Failed to update another daily MST", e);
		}
		
		LOG.info("Scheduled daily MST update is finished with success:" + updated);
	}
	
}
