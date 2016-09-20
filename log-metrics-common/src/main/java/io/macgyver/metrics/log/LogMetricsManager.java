package io.macgyver.metrics.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.LoggerFactory;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

public class LogMetricsManager {

	org.slf4j.Logger logger = LoggerFactory.getLogger(LogMetricsManager.class);
	static LogMetricsManager instance = new LogMetricsManager();

	AtomicReference<List<ConfigTab>> listRef = new AtomicReference<>(new CopyOnWriteArrayList<ConfigTab>());

	MetricRegistry registry;

	Map<String,Meter> meterMap = new ConcurrentHashMap<>();
	public static LogMetricsManager getInstance() {
		return instance;
	}

	public void setMetricRegistry(MetricRegistry registry) {
		this.registry = registry;
	}

	public void registerHit(ConfigTab c) {

		if (c.meter == null) {
			if (registry != null) {
				
				c.meter = meterMap.get(c.metric);
				if (c.meter==null) {
					c.meter = registry.meter(c.metric);
					if (c.meter!=null) {
						meterMap.put(c.metric, c.meter);
					}
				}
				
				
				
			}
		}

		if (c.meter != null) {
			c.meter.mark();
		}
	}

	

	
	public void resetMeters() {
		meterMap.clear();
	}
	public void parseConfigTab(File f) throws IOException {
		logger.info("parsing config tab: {}",f);
		try (FileReader fr = new FileReader(f)) {
			parseConfigTab(f);
		}
	}
	public void parseConfigTab(String s) throws IOException {
		try (StringReader sr = new StringReader(s)) {
			parseConfigTab(sr);
		}
	}
	public void parseConfigTab(Reader r) throws IOException {
		if (r==null) {
			return;
		}
	
		BufferedReader br = new BufferedReader(r);
		String line = null;
		
		List<ConfigTab> tmp = new ArrayList<>();
		while ((line=br.readLine()) !=null) {
			Optional<ConfigTab> cfg = ConfigTab.parseConfigLine(line);
			if (cfg.isPresent()) {
				tmp.add(cfg.get());
			}
		}
	
		CopyOnWriteArrayList<ConfigTab> list = new CopyOnWriteArrayList<>();
		list.addAll(tmp);
		listRef.set(list);
	}
	public void addConfigTabEntry(String input) {
		ConfigTab.parseConfigLine(input).ifPresent(it -> {
			addConfig(it);
		});
		
	}
	public void addConfig(String metric, String messageRegex, String levelRegex, String loggerRegex) {
		ConfigTab c = new ConfigTab().metric(metric).messagePattern(messageRegex).levelPattern(levelRegex)
				.loggerPattern(loggerRegex);

		addConfig(c);
	}

	public void setMetricConfig(List<ConfigTab> cfg) {
	
		CopyOnWriteArrayList<ConfigTab> x = new CopyOnWriteArrayList<>();
		x.addAll(cfg);
		
		this.listRef.set(x);
	}
	protected void addConfig(ConfigTab c) {
		this.listRef.get().add(c);
	}

	public void receive(String message, String level, String logger, Throwable throwable) {
		
		for (ConfigTab c : listRef.get()) {

			if (c.match(message, level, logger, throwable)) {
				registerHit(c);
			}
			
		}
	}
}
