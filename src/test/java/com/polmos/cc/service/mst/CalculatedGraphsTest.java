package com.polmos.cc.service.mst;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;

import org.infinispan.Cache;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class CalculatedGraphsTest {

	@Mock
	private Cache<Integer, JsonObject> cache;

	@InjectMocks
	private CalculatedGraphs graphs;

	@Before
	public void setup() {
		final Map<Integer, JsonObject> tmpCache = new HashMap<>();
		Mockito.when(cache.put(Matchers.anyInt(), Matchers.any(JsonObject.class))).then(new Answer<JsonObject>() {

			@Override
			public JsonObject answer(InvocationOnMock invocation)
					throws Throwable {
				tmpCache.put((Integer)invocation.getArguments()[0], (JsonObject)invocation.getArguments()[1]);
				return null;
			}
		});
		Mockito.when(cache.get(Matchers.anyInt())).then(new Answer<JsonObject>() {

			@Override
			public JsonObject answer(InvocationOnMock invocation)
					throws Throwable {
				return tmpCache.get(invocation.getArguments()[0]);
			}
		});
	}
	
	@Test
	public void addTwoGraphsOneAfterAnother() {
		int firstId = graphs.putGraph(emptyJson());
		int secondId = graphs.putGraph(emptyJson());
		assertEquals(0, firstId);
		assertEquals(1, secondId);
	}

	@Test
	public void checkIfCacheIsCalledProperly() {
		ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<JsonObject> jsonCaptor = ArgumentCaptor.forClass(JsonObject.class);
		JsonObject input = randomJson();
		
		int graphId = graphs.putGraph(input);
		graphs.getGraph(graphId);
		
		Mockito.verify(cache).put(idCaptor.capture(), jsonCaptor.capture());
		assertEquals(new Integer(graphId), idCaptor.getValue());
		assertEquals(input, jsonCaptor.getValue());
	}
	
	@Test
	public void addAndGetGraph() {
		JsonObject input = randomJson();
		int id = graphs.putGraph(input);
		assertEquals(input, graphs.getGraph(id));
	}
	
	@Test
	public void checkIfNullMSTCanReplaceExistingDailyMSTInstance() {
		assertFalse(graphs.putDailyMST(null));
	}
	
	@Test
	public void addNewDailyGraph() {
		JsonObject input = randomJson();
		assertTrue(graphs.putDailyMST(input));
	}
	
	@Test
	public void addAndGetNewDailyGraph() {
		JsonObject input = randomJson();
		assertTrue(graphs.putDailyMST(input));
		assertEquals(input, graphs.getDailyMST());
	}
	
	@Test
	public void verifyIfNewMSTUpdatesExistingDailyMST() {
		JsonObject firstMST = randomJson();
		JsonObject secondMST = randomJson();
		graphs.putDailyMST(firstMST);
		graphs.putDailyMST(secondMST);
		assertEquals(secondMST, graphs.getDailyMST());
	}

	private JsonObject emptyJson() {
		return Json.createObjectBuilder().build();
	}
	
	private JsonObject randomJson() {
		return Json.createObjectBuilder().add("id", System.currentTimeMillis()).build();
	}
}
