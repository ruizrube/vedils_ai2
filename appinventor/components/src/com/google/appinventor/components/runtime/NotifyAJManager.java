package com.google.appinventor.components.runtime;

import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.util.ActivityTrackerInstances;
import org.aspectj.lang.JoinPoint;

import android.app.Activity;

public class NotifyAJManager {
	
	protected static void sendNotification(JoinPoint pointcut, String type) {
		
		String screenName = "";
		boolean notificable = true;
		
		if(pointcut.getThis() instanceof AndroidNonvisibleComponent) {
			System.out.println("AndroidNonvisibleComponent - AspectJ.");
			
			AndroidNonvisibleComponent nonvisibleComponent = (AndroidNonvisibleComponent) pointcut.getThis();
			screenName = nonvisibleComponent.form.$context().getTitle().toString();
			notificable = nonvisibleComponent.getNotificable();
			
		} else if(pointcut.getThis() instanceof AndroidViewComponent) {
			System.out.println("AndroidViewComponent - AspectJ.");
			AndroidViewComponent viewComponent = (AndroidViewComponent) pointcut.getThis();
			Activity activity = (Activity) viewComponent.getView().getContext();
			screenName = activity.getTitle().toString();
			notificable = viewComponent.getNotificable();
		} else {
			System.out.println("Other type - AspectJ.");
		}
		
		System.out.println("ScreenName = " +screenName + " - AspectJ.");
		//If ActivityTracker has been instantiated.
		if(ActivityTrackerInstances.getActivityTracker(screenName) != null) {
			if(notificable && ActivityTrackerInstances.getActivityTracker(screenName).getTrackingStatus() &&
					ActivityTrackerInstances.getActivityTracker(screenName).getPublishMethodsStatus()) {
				//Send data to FusionTables in function of the number of arguments
				switch(pointcut.getArgs().length) {
					case 0: //0 arguments
						ActivityTrackerInstances.getActivityTracker(screenName).getActivityTrackerManager().prepareQueryAutomatic(
						type, pointcut.getSignature().getName(), pointcut.getThis().getClass().getSimpleName(), "", "", "", "");
					break;
					case 1: //1 argument
						ActivityTrackerInstances.getActivityTracker(screenName).getActivityTrackerManager().prepareQueryAutomatic(
						type, pointcut.getSignature().getName(), pointcut.getThis().getClass().getSimpleName(), "", pointcut.getArgs()[0].toString(), "", "");
					break;
					case 2: //2 arguments
						ActivityTrackerInstances.getActivityTracker(screenName).getActivityTrackerManager().prepareQueryAutomatic(
						type, pointcut.getSignature().getName(), pointcut.getThis().getClass().getSimpleName(), "", pointcut.getArgs()[0].toString(), pointcut.getArgs()[1].toString(), "");
					break;
					default: //3 arguments or more
						ActivityTrackerInstances.getActivityTracker(screenName).getActivityTrackerManager().prepareQueryAutomatic(
						type, pointcut.getSignature().getName(), pointcut.getThis().getClass().getSimpleName(), "", pointcut.getArgs()[0].toString(), pointcut.getArgs()[1].toString(), pointcut.getArgs()[2].toString());
					break;
				}
			}
		}
	}
}
