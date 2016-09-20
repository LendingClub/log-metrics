package io.macgyver.metrics.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Context;
import io.macgyver.metrics.log.LogMetricsManager;

public class MetricsAppender extends AppenderBase<ILoggingEvent> {

	LogMetricsManager logMetricsManager = LogMetricsManager.getInstance();

	

	@Override
	protected void append(ILoggingEvent event) {

		
		String message = event.getFormattedMessage();
		
		logMetricsManager.receive( event.getFormattedMessage(),event.getLevel().toString(),event.getLoggerName(),null);

	}

}
