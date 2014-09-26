package com.polmos.cc.service.mst;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
	
    private final Map<Integer, JsonObject> graphs;
    private final AtomicInteger currId;
    
    public CalculatedGraphs() {
        this.graphs = new ConcurrentHashMap<>();
        this.currId = new AtomicInteger(0);
    }
    
    public int putGraph(JsonObject mst) {
        int id = currId.getAndIncrement();
        graphs.put(id, mst);
        return id;
    }
    
    public JsonObject getGraph(int id) {
        return graphs.get(id);
    }
}