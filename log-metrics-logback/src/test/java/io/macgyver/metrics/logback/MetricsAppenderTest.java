package io.macgyver.metrics.logback;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import ch.qos.logback.classic.Logger;
import io.macgyver.metrics.log.LogMetricsManager;

public class MetricsAppenderTest {

	org.slf4j.Logger logger = LoggerFactory.getLogger(MetricsAppenderTest.class);

	public MetricsAppenderTest() {
		// TODO Auto-generated constructor stub
	}

	static MetricRegistry mr = new MetricRegistry();

	@BeforeClass
	public static void setup() throws IOException {
		MetricRegistry mr = new MetricRegistry();
		final ConsoleReporter reporter = ConsoleReporter.forRegistry(mr).convertRatesTo(TimeUnit.MINUTES)
				.convertDurationsTo(TimeUnit.MILLISECONDS).build();
		reporter.start(5, TimeUnit.SECONDS);

		LogMetricsManager.getInstance().parseConfigTab("");
		LogMetricsManager.getInstance().setMetricRegistry(mr);
	}

	@Test
	public void testIt() throws IOException {

		LogMetricsManager.getInstance().parseConfigTab("bar .*bad.* INFO\nfoo .*badx.*");

	
		Random r = new Random();
		long t0 = System.currentTimeMillis();
		while (System.currentTimeMillis() - t0 < 11000) {
			try {
				LogMetricsManager.getInstance().parseConfigTab("bar .*bad.* INFO\nfoo .*badx.*");
				Thread.sleep(500);
			} catch (Exception e) {
				//ignore
				e.printStackTrace();
			}
		
			logger.info("something {}",(r.nextInt() % 3==0) ? "bad" : "good");

		}
		
	
		
	}

}
