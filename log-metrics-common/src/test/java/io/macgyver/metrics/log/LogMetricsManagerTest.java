package io.macgyver.metrics.log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import ch.qos.logback.classic.Logger;

public class LogMetricsManagerTest {

	org.slf4j.Logger logger = LoggerFactory.getLogger(LogMetricsManagerTest.class);

	public LogMetricsManagerTest() {
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testIt() throws IOException {
		MetricRegistry mr = new MetricRegistry();
		final ConsoleReporter reporter = ConsoleReporter.forRegistry(mr)
                .convertRatesTo(TimeUnit.MINUTES)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
			reporter.start(5, TimeUnit.SECONDS	);
		
		
		LogMetricsManager mm = LogMetricsManager.getInstance();

		mm.setMetricRegistry(mr);
		
	
		mm.parseConfigTab("foobar     .*bad.*");


	
	}

}
