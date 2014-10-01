package com.polmos.cc.service;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.json.JsonObject;

import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;
import org.jboss.logging.Logger;

@Singleton
@Startup
public class InfinispanProvider {

	private static final Logger logger = Logger.getLogger(InfinispanProvider.class);
	
	@Resource(lookup = "java:jboss/infinispan/container/myCache")
	private CacheContainer cacheManager;
	
	@Produces 
	@MSTCache
	public Cache<String, JsonObject> produceCache() {
		logger.info("producing cache...");
		return cacheManager.getCache("mst-cache");
	}
}
