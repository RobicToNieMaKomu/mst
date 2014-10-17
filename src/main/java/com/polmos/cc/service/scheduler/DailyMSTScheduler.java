package com.polmos.cc.service.scheduler;

import static java.util.concurrent.TimeUnit.HOURS;
import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.logging.Logger;

@Singleton
@Startup
@TransactionAttribute(NOT_SUPPORTED)
public class DailyMSTScheduler {

	private static final Logger LOG = Logger.getLogger(DailyMSTScheduler.class);
	
	private static final int SAT = 7;
	private static final int SUN = 1;
	private static final int START_HOUR = 18;
	
	@Resource
	private ManagedScheduledExecutorService executorService;
	
	@Inject
	private DailyMSTUpdater updaterTask;
	
	@Inject
	@Now
	private Instance<Date> dateProducer;
	
	@PostConstruct
	public void initialize() {
		LOG.info("Initializing Daily MST Scheduler...");
		
		executorService.scheduleAtFixedRate(updaterTask, initialDelayInHours(), 24, HOURS);
		
		LOG.info("Daily MST Scheduler initialized");
	}
	
	private int initialDelayInHours() {
		Calendar calendar = prepareCalendar();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		
		return calculateDelay(dayOfWeek, hour);
	}

	private int calculateDelay(int dayOfWeek, int hour) {
		int result = 0;
		if (dayOfWeek == SAT) {
			result += 24 + (24 - hour) + START_HOUR;
		} else if (dayOfWeek == SUN) {
			result += (24 - hour) + START_HOUR; 
		} else {
			result = beforeScheduledHour(hour) ? START_HOUR - hour : 0;
		}
		return result;
	}

	private boolean beforeScheduledHour(int hour) {
		return hour <= START_HOUR;
	}

	private Calendar prepareCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(dateProducer.get());
		return calendar;
	}
}