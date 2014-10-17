package com.polmos.cc.service.mst;

import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;

import org.infinispan.Cache;

/**
 *
 * @author RobicToNieMaKomu
 */
@ApplicationScoped
public class CalculatedGraphs {

	@Inject
	private Cache<Integer, JsonObject> calculatedGraphs;
	
	private final AtomicInteger id;
	private JsonObject dailyMST;
	
	public CalculatedGraphs() {
		this.id = new AtomicInteger(0);
	}
	
	public int putGraph(JsonObject mstGraph) {
		int graphId = id.getAndIncrement();
		calculatedGraphs.put(graphId, mstGraph);
		return graphId;
	}

	public JsonObject getGraph(int graphId) {
		return calculatedGraphs.get(graphId);
	}

	public boolean putDailyMST(JsonObject mstGraph) {
		boolean graphUpdated = false;
		if (mstGraph != null) {
			this.dailyMST = mstGraph;
			graphUpdated = true;
		}
		return graphUpdated;
	}

	public JsonObject getDailyMST() {
		return dailyMST;
	}
}