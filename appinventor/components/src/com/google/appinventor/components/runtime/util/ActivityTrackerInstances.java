package com.google.appinventor.components.runtime.util;

import java.util.Map;
import java.util.HashMap;
import com.google.appinventor.components.runtime.ActivityTracker;
import android.app.Activity;

public class ActivityTrackerInstances {
	static Map<Activity, ActivityTracker> activityTrackerInstances = new HashMap<Activity, ActivityTracker>();
	
	public static void insertActivityTracker(Activity activity, ActivityTracker activityTracker) {
		activityTrackerInstances.put(activity, activityTracker);
	}
	
	public static ActivityTracker getActivityTracker(Activity activity) {
		return activityTrackerInstances.get(activity);
	}
	
}
