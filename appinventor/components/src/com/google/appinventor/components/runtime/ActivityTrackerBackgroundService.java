package com.google.appinventor.components.runtime;

import com.google.appinventor.components.runtime.util.ActivityTrackerManager;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;

public class ActivityTrackerBackgroundService extends AccessibilityService {
	
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	private ActivityTrackerManager activityTrackerManager;

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		System.out.println("AccessibilityService: eventType: " + AccessibilityEvent.eventTypeToString(event.getEventType()));
		System.out.println("AccessibilityService: packageName: " + event.getPackageName());
		//System.out.println("AccessibilityService: contentDescription" + event.getContentDescription());
		//System.out.println("AccessibilityService: viewResourceName" + event.getSource().getViewIdResourceName());
	}

	@Override
	public void onInterrupt() {
		System.out.println("AccessibilityService: onInterrupt()");
	}
	
	@Override
    protected void onServiceConnected() {
		System.out.println("AccessibilityService: onServiceConnected()");
		
		AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.notificationTimeout = 100;
        info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
        setServiceInfo(info);
        
        //Si el diseñador elige registrar la localización
        addLocationData();
    }
	
	
	public int onStartCommand (Intent intent, int flags, int startId) {
		
    	System.out.println("AccessibilityService: Recibiendo intent.");
    	
    	/*Gson gson = new Gson();
	    ActivityTracker activityTracker =  gson.fromJson(intent.getStringExtra("ActivityTracker"), ActivityTracker.class);
	    
	    if(activityTracker != null) {
	    	System.out.println("AccessibilityService: ActivityTrackerTest: no es nulo.");
	    }
	    
	    activityTrackerManager = new ActivityTrackerManagerFusionTables(activityTracker, null);
	    
	    if(activityTrackerManager != null) {
        	System.out.println("AccessibilityService: ActivityTrackerManagerTest: no es nulo.");
        }*/
	    
	    return super.onStartCommand(intent, flags, startId);
	}
	
	private void addLocationData() {
		mLocationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               System.out.println("AccessibilityService: locationChanged = " + location.getLatitude() + ", " + location.getLongitude());
            }

			@Override
			public void onProviderDisabled(String arg0) {}

			@Override
			public void onProviderEnabled(String arg0) {}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
        };
        
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 500, 5, mLocationListener);
	}
}
