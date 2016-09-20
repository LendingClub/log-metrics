package io.macgyver.metrics.log;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;

import com.codahale.metrics.Meter;

public class ConfigTab {

	static org.slf4j.Logger logger = LoggerFactory.getLogger(ConfigTab.class);
	
	String metric;
	Meter meter;
	Pattern messagePattern = Pattern.compile(".*");
	Pattern levelPattern = Pattern.compile(".*");
	Pattern loggerPattern = Pattern.compile(".*");
	public ConfigTab() {

	}

	
	public ConfigTab metric(String name) {
		this.metric = name;
		return this;
	}

	public ConfigTab messagePattern(String p) {
		if (p == null) {
			messagePattern = Pattern.compile(".*");
		} else {
			messagePattern = Pattern.compile(p);
		}
		return this;
	}
	
	public ConfigTab levelPattern(String p) {
		if (p == null) {
			levelPattern = Pattern.compile(".*");
		} else {
			levelPattern = Pattern.compile(p);
		}
		return this;
	}
	public ConfigTab loggerPattern(String p) {
		if (p == null) {
			loggerPattern = Pattern.compile(".*");
		} else {
			loggerPattern = Pattern.compile(p);
		}
		return this;
	}
	public static Optional<ConfigTab> parseConfigLine(String line) {

		if (line == null) {
			return Optional.empty();
		}
		line = line.trim();
		if (line.startsWith("#")) {
			return Optional.empty();
		}
		StringTokenizer st = new StringTokenizer(line);
		List<String> tmp = new ArrayList<>();
		while (st.hasMoreTokens()) {
			tmp.add(st.nextToken().trim());
		}
		if (tmp.size()<2) {
			logger.warn("invalid config tab entry: {}",line);
			return Optional.empty();
		}
		while (tmp.size() < 5) {
			tmp.add(null);
		}
		String metric = tmp.get(0);
		String messageRegex = tmp.get(1);
		String levelRegex = tmp.get(2);
		String loggerRegex = tmp.get(3);
		
		ConfigTab cfg = new ConfigTab().metric(metric).messagePattern(messageRegex).levelPattern(levelRegex).loggerPattern(loggerRegex);
		return Optional.of(cfg);
	}
	public boolean match(String message, String level, String logger, Throwable throwable) {
	
		
		if (message !=null && !messagePattern.matcher(message).matches()) {
			return false;
		}
		if (level !=null && !levelPattern.matcher(level).matches()) {
			return false;
		}
		if (logger !=null && !loggerPattern.matcher(logger).matches()) {
			return false;
		}
		return true;
	}
	public ConfigTab meter(Meter m) {
		this.meter = m;
		return this;
	}
}
