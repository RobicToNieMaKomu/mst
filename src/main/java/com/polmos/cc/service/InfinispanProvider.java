package com.polmos.cc.service;

import static java.util.concurrent.TimeUnit.MINUTES;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;

import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.jboss.logging.Logger;

@Singleton
@Startup
public class InfinispanProvider {

	private static final Logger logger = Logger.getLogger(InfinispanProvider.class);
	
	@Produces
	public Configuration produceCache() {
		logger.info("producing cache configuration...");
		return new ConfigurationBuilder().eviction().strategy(EvictionStrategy.LRU).maxEntries(100).expiration().maxIdle(1, MINUTES).build();
	}
}
