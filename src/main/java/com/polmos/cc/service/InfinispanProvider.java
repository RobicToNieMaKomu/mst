package com.polmos.cc.service;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.json.JsonObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jboss.logging.Logger;

@Singleton
@Startup
public class InfinispanProvider {

	private static final Logger logger = Logger.getLogger(InfinispanProvider.class);
	
	private EmbeddedCacheManager cacheManager;
	
	@PostConstruct
	public void startup() throws NamingException {
		this.cacheManager = (EmbeddedCacheManager) new InitialContext().lookup("java:jboss/infinispan/container/mst-cache-container");
	}
	
	@Produces 
	@MSTCache
	public Cache<String, JsonObject> produceCache() {
		logger.info("producing cache...");
		return cacheManager.getCache("mst-cache");
	}
}
