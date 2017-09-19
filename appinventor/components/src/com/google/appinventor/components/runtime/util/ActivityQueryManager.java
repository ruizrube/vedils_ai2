package com.google.appinventor.components.runtime.util;

import java.util.List;

import com.google.appinventor.components.runtime.ActivityProcessor;

/**
 * Interface to allow queries to different types of DBs (now Google Fusion Tables or MongoDB).
 * @author SPI-FM at UCA
 */
public interface ActivityQueryManager {
	public ActivityProcessor getComponent();
	public void sendQuery();
	public String generateQueryStatement();
	public String makeSelect(List<String> fields, List<String> aggregations);
	public String makeFrom();
	public String makeWhere();
	public String makeGroupBy(List<String> groupByColumns);
}
