package com.google.appinventor.components.runtime.util;

import java.util.Map;
import java.util.HashMap;
import com.google.appinventor.components.runtime.ActivityTracker;

public class ActivityTrackerInstances {
	static Map<String, ActivityTracker> activityTrackerInstances = new HashMap<String, ActivityTracker>();
	
	public static void insertActivityTracker(String screenName, ActivityTracker activityTracker) {
		activityTrackerInstances.put(screenName, activityTracker);
	}
	
	public static ActivityTracker getActivityTracker(String screeName) {
		return activityTrackerInstances.get(screeName);
	}
	
}
