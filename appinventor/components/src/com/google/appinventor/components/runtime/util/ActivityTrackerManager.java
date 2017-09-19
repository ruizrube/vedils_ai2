package com.google.appinventor.components.runtime.util;

import java.util.List;

/**
 * Interface to allow record data to different types of DBs (now Google Fusion Tables or MongoDB).
 * @author SPI-FM at UCA
 */
public interface ActivityTrackerManager {
	public void prepareQueryAutomatic(String actionType, String actionId, String componentType, String componentId, String[] paramsName, Object[] paramsValue, String returnParamValue);
	public void prepareQueryManual(String actionId, List<Object> data);
	public void prepareQueryManual(String actionId, String param1, String param2, String param3);
	public void recordData();
	public void recordDataBatch();
}
