package com.polmos.cc.service.scheduler;

import static java.util.concurrent.TimeUnit.HOURS;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.inject.Instance;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DailyMSTSchedulerTest {

	@Mock
	private ManagedScheduledExecutorService executorService;

	@Mock
	private DailyMSTUpdater updaterTask;

	@Mock
	private Instance<Date> dateProducer;

	@InjectMocks
	private DailyMSTScheduler scheduler;

	@Before
	public void setup() {
		Mockito.when(dateProducer.get()).thenReturn(new Date());
	}

	@Test
	public void initializeServiceOnSundayNoon() {
		when(dateProducer.get()).thenReturn(toDate(0, 12, 7));

		scheduler.initialize();

		verify(executorService).scheduleAtFixedRate(any(DailyMSTUpdater.class),
				eq(30l), eq(24l), eq(HOURS));
	}

	@Test
	public void initializeServiceOnSaturday0Am() {
		when(dateProducer.get()).thenReturn(toDate(0, 0, 6));

		scheduler.initialize();

		verify(executorService).scheduleAtFixedRate(any(DailyMSTUpdater.class),
				eq(66l), eq(24l), eq(HOURS));
	}

	@Test
	public void initializeServiceOnSunday0AM() {
		when(dateProducer.get()).thenReturn(toDate(0, 0, 7));

		scheduler.initialize();

		Mockito.verify(executorService).scheduleAtFixedRate(
				any(DailyMSTUpdater.class), eq(42l), eq(24l), eq(HOURS));
	}

	@Test
	public void initializeServiceOnMonday1AM() {
		when(dateProducer.get()).thenReturn(toDate(0, 1, 1));

		scheduler.initialize();

		Mockito.verify(executorService).scheduleAtFixedRate(
				any(DailyMSTUpdater.class), eq(17l), eq(24l), eq(HOURS));
	}

	@Test
	public void initializeServiceOnTuesday1845() {
		when(dateProducer.get()).thenReturn(toDate(45, 18, 2));

		scheduler.initialize();

		Mockito.verify(executorService).scheduleAtFixedRate(
				any(DailyMSTUpdater.class), eq(0l), eq(24l), eq(HOURS));
	}
	
	@Test
	public void initializeServiceOnFriday20() {
		when(dateProducer.get()).thenReturn(toDate(0, 20, 5));

		scheduler.initialize();

		Mockito.verify(executorService).scheduleAtFixedRate(
				any(DailyMSTUpdater.class), eq(0l), eq(24l), eq(HOURS));
	}

	private Date toDate(int minute, int hour, int dayOfWeek) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.clear();
		calendar.set(2014, 11, dayOfWeek, hour, minute);
		return calendar.getTime();
	}
}
