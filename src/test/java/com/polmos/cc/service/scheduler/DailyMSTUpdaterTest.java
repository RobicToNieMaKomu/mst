package com.polmos.cc.service.scheduler;

import static com.polmos.cc.service.ResourceManager.allCurrencies;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.polmos.cc.constants.OperationType;
import com.polmos.cc.rest.RESTClient;
import com.polmos.cc.service.JsonUtils;
import com.polmos.cc.service.ResourceManager;
import com.polmos.cc.service.mst.CalculatedGraphs;
import com.polmos.cc.service.mst.MSTService;

@RunWith(MockitoJUnitRunner.class)
public class DailyMSTUpdaterTest {

	private static final String ALL_CURRENCIES = allCurrencies();
	private static final List<String> FULL_LIST_OF_CURRENCIES = ResourceManager.toList(ALL_CURRENCIES);
	
	@Mock
	private RESTClient client;
	
	@Mock
	private MSTService mstService;
	
	@Mock
	private JsonUtils utils;
	
	@InjectMocks
	private DailyMSTUpdater mstUpdater;
	
	@Mock
	private CalculatedGraphs calculatedGraphs;
	
	@Test
	public void checkIfRESTClientIsProperlyCalled() {
		mstUpdater.run();
		
		verify(client).sendGetRequest(eq("http://cc-comparator.rhcloud.com/rest/mst?type=ask&range=1&currencies=" + ALL_CURRENCIES));
	}
	
	@Test
	public void checkIfMSTisCreatedBasedOnReceivedData() throws IOException {
		JsonArray input = timeSeries();
		when(utils.convertJsonArray(Mockito.eq(input))).thenReturn(Collections.<JsonObject>emptyList());
		when(client.sendGetRequest(Mockito.anyString())).thenReturn(input);
		mstUpdater.run();
		
		verify(mstService).generateMST(eq(FULL_LIST_OF_CURRENCIES), eq(Collections.<JsonObject>emptyList()), any(OperationType.class));
	}
	
	@Test
	public void checkIfCalculatedGraphIsPutToCache() {
		mstUpdater.run();
		
		verify(calculatedGraphs).putDailyMST(Matchers.any(JsonObject.class));
		verifyNoMoreInteractions(calculatedGraphs);
	}

	private JsonArray timeSeries() {
		return Json.createArrayBuilder().build();
	}
}