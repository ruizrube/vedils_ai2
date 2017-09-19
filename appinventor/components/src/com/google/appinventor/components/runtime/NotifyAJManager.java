package com.google.appinventor.components.runtime;

import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.util.ActivityTrackerInstances;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import android.app.Activity;

public class NotifyAJManager {
	
	private static boolean notificableMethod(String selectedActivities, String type, String method) {
		System.out.println("Type operation: " +type);
		System.out.println("Method: "+method);
		System.out.println("SelectedActivities: " + selectedActivities);
		if(type.equals("Get")) {
			return selectedActivities.contains("ActionType:Get:ActionID:"+method);
		} else if(type.equals("Set")) {
			return selectedActivities.contains("ActionType:Set:ActionID:"+method);
		} else {
			return selectedActivities.contains(method);
		}
	}
	
	protected static void sendNotification(JoinPoint pointcut, String type, Object returnValue) {
		
		Activity currentActivity = null;
		String selectedActivities = "";
		String returnValueString = "";
		String componentName = "";
		
		if(pointcut.getThis() instanceof AndroidNonvisibleComponent) {
			System.out.println("AndroidNonvisibleComponent - AspectJ.");
			
			AndroidNonvisibleComponent nonvisibleComponent = (AndroidNonvisibleComponent) pointcut.getThis();
			currentActivity = nonvisibleComponent.form.$context();
			selectedActivities = nonvisibleComponent.getActivitiesToTrack();
			componentName = nonvisibleComponent.getName();
			System.out.println("Activities to Notify: "+nonvisibleComponent.getActivitiesToTrack() + "- AspectJ.");
			
		} else if(pointcut.getThis() instanceof AndroidViewComponent) {
			System.out.println("AndroidViewComponent - AspectJ.");
			AndroidViewComponent viewComponent = (AndroidViewComponent) pointcut.getThis();
			if(!(pointcut.getThis() instanceof ListView)) {
				currentActivity = (Activity) viewComponent.getView().getContext();
			}
			selectedActivities = viewComponent.getActivitiesToTrack();
			componentName = viewComponent.getName();
			System.out.println("Activities to Notify: "+viewComponent.getActivitiesToTrack() + "- AspectJ.");
		} else {
			System.out.println("Other type - AspectJ.");
		}
		
		System.out.println("Selected Activities for component: " + selectedActivities + " - ActivitiesToNotify.");
		
		if(returnValue instanceof String) {
			returnValueString = (String) returnValue;
		} else if(returnValue instanceof Boolean) {
			Boolean returnValueBoolean = (Boolean) returnValue;
			returnValueString = returnValueBoolean.toString();
		} else if(returnValue instanceof Integer) {
			Integer returnValueInteger = (Integer) returnValue;
			returnValueString = returnValueInteger.toString();
		} else if(returnValue instanceof Double) {
			Double returnValueDouble = (Double) returnValue;
			returnValueString = returnValueDouble.toString();
		} else if(returnValue instanceof Object) {
			//For other types
			returnValueString = returnValue.toString();
		}
		
		//System.out.println("ScreenName = " +screenName + " - AspectJ.");
		//If ActivityTracker has been instantiated.
		if(ActivityTrackerInstances.getActivityTracker(currentActivity) != null) {
			//If the activity name is recorded in selectedActivities, then the activity is notify.
			if(notificableMethod(selectedActivities, type, pointcut.getSignature().getName()) && ActivityTrackerInstances.getActivityTracker(currentActivity).getTrackingStatus()) {
				//Send data to FusionTables in function of the number of arguments
				System.out.println("Operation: " + selectedActivities + " - ActivitiesToNotify.");
				ActivityTrackerInstances.getActivityTracker(currentActivity).getActivityTrackerManager().prepareQueryAutomatic(
						type, pointcut.getSignature().getName(), pointcut.getThis().getClass().getSimpleName(), componentName, ((MethodSignature)pointcut.getSignature()).getParameterNames(), pointcut.getArgs(), returnValueString);
			}
		}
	}
}
