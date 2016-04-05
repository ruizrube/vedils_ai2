package com.google.appinventor.components.runtime;

import org.aspectj.lang.annotation.*;

import android.app.Activity;

import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.util.ActivityTrackerInstances;

import org.aspectj.lang.JoinPoint;

@Aspect
public class NotifyAJ {
	
	@Pointcut("within(@com.google.appinventor.components.annotations.DesignerComponent *)")
	public void onlyComponents() {}

	//@Pointcut("within(@com.google.appinventor.components.annotations.Notificable *)")
	//public void onlyNotificableComponents() {}
	
	@Pointcut ("execution(@com.google.appinventor.components.annotations.SimpleFunction * com.google.appinventor.components.runtime.*.*(..))")
	private void onlyFunctions() {}
	
	@Pointcut ("execution(@com.google.appinventor.components.annotations.SimpleProperty * com.google.appinventor.components.runtime.*.*(..))")
	private void onlyGetOrSet() {}
	
	@Pointcut ("execution(@com.google.appinventor.components.annotations.SimpleEvent * com.google.appinventor.components.runtime.*.*(..))")
	private void onlyEvents() {}
	
	@Before ("onlyComponents() && onlyFunctions()")
	public void recordDataOnlyFunctions(JoinPoint pointcut) {
		if(pointcut.getThis() != null) {
			System.out.println("Before recordDataOnlyFunctions pointcut - AspectJ.");
			System.out.println("Class: " + pointcut.getThis().getClass() + " Method: " + pointcut.getSignature().getName() + " - AspectJ");
			
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
			//Send data to FusionTables in function of the number of arguments
			if(notificable && screenName != "" && ActivityTrackerInstances.getActivityTracker(screenName).getTrackingStatus() &&  ActivityTrackerInstances.getActivityTracker(screenName).getPublishMethodsStatus()) {
				switch(pointcut.getArgs().length) {
					case 0: //0 arguments
						ActivityTrackerInstances.getActivityTracker(screenName).getActivityTrackerManager().prepareQueryAutomatic(
						"Function", pointcut.getSignature().getName(), pointcut.getThis().getClass().getSimpleName(), "", "", "", "");
					break;
					case 1: //1 argument
						ActivityTrackerInstances.getActivityTracker(screenName).getActivityTrackerManager().prepareQueryAutomatic(
						"Function", pointcut.getSignature().getName(), pointcut.getThis().getClass().getSimpleName(), "", pointcut.getArgs()[0].toString(), "", "");
					break;
					case 2: //2 arguments
						ActivityTrackerInstances.getActivityTracker(screenName).getActivityTrackerManager().prepareQueryAutomatic(
						"Function", pointcut.getSignature().getName(), pointcut.getThis().getClass().getSimpleName(), "", pointcut.getArgs()[0].toString(), pointcut.getArgs()[1].toString(), "");
					break;
					default: //3 arguments or more
						ActivityTrackerInstances.getActivityTracker(screenName).getActivityTrackerManager().prepareQueryAutomatic(
						"Function", pointcut.getSignature().getName(), pointcut.getThis().getClass().getSimpleName(), "", pointcut.getArgs()[0].toString(), pointcut.getArgs()[1].toString(), pointcut.getArgs()[2].toString());
					break;
				}
			}
		}
	}
	
	@Before("onlyComponents() && onlyGetOrSet()")
	public void recordDataOnlyGetOrSet(JoinPoint pointcut) {
		if(pointcut.getThis() != null) {
			System.out.println("Before recordDataOnlyGetOrSet pointcut - AspectJ.");
			System.out.println("Class: " + pointcut.getThis().getClass() + " Method: " + pointcut.getSignature().getName() + " - AspectJ");
			
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
			//0 arguments -> Get, 1 arguments -> Set (simpleFunction is modifier of only one attribute).
			if(ActivityTrackerInstances.getActivityTracker(screenName) != null) {
				if(notificable && screenName != "" && ActivityTrackerInstances.getActivityTracker(screenName).getTrackingStatus()) {
					if(pointcut.getArgs().length == 0 && ActivityTrackerInstances.getActivityTracker(screenName).getPublishGettersStatus()) {
						ActivityTrackerInstances.getActivityTracker(screenName).getActivityTrackerManager().prepareQueryAutomatic(
								"Get", pointcut.getSignature().getName(), pointcut.getThis().getClass().getSimpleName(), "", "", "", "");
					} else if(pointcut.getArgs().length > 0 && ActivityTrackerInstances.getActivityTracker(screenName).getPublishSettersStatus()) {
						ActivityTrackerInstances.getActivityTracker(screenName).getActivityTrackerManager().prepareQueryAutomatic(
								"Set", pointcut.getSignature().getName(), pointcut.getThis().getClass().getSimpleName(), "", pointcut.getArgs()[0].toString(), "", "");
					}
				}
			}
		}
	}
	
	@Before("onlyComponents() && onlyEvents()")
	public void recordDataOnlyEvents(JoinPoint pointcut) {
		if(pointcut.getThis() != null) {
			System.out.println("Before recordDataOnlyEvents pointcut - AspectJ.");
			System.out.println("Class: " + pointcut.getThis().getClass() + " Method: " + pointcut.getSignature().getName() + " - AspectJ");
			
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
			//Send data to FusionTables in function of the number of arguments
			if(ActivityTrackerInstances.getActivityTracker(screenName) != null) {
				if(notificable && screenName != "" && ActivityTrackerInstances.getActivityTracker(screenName).getTrackingStatus() &&  ActivityTrackerInstances.getActivityTracker(screenName).getPublishEventsStatus()) {
					switch(pointcut.getArgs().length) {
						case 0: //0 arguments
							ActivityTrackerInstances.getActivityTracker(screenName).getActivityTrackerManager().prepareQueryAutomatic(
									"Event", pointcut.getSignature().getName(), pointcut.getThis().getClass().getSimpleName(), "", "", "", "");
						break;
						case 1: //1 argument
							ActivityTrackerInstances.getActivityTracker(screenName).getActivityTrackerManager().prepareQueryAutomatic(
									"Event", pointcut.getSignature().getName(), pointcut.getThis().getClass().getSimpleName(), "", pointcut.getArgs()[0].toString(), "", "");
						break;
						case 2: //2 arguments
							ActivityTrackerInstances.getActivityTracker(screenName).getActivityTrackerManager().prepareQueryAutomatic(
									"Event", pointcut.getSignature().getName(), pointcut.getThis().getClass().getSimpleName(), "", pointcut.getArgs()[0].toString(), pointcut.getArgs()[1].toString(), "");
						break;
						default: //3 arguments or more
							ActivityTrackerInstances.getActivityTracker(screenName).getActivityTrackerManager().prepareQueryAutomatic(
									"Event", pointcut.getSignature().getName(), pointcut.getThis().getClass().getSimpleName(), "", pointcut.getArgs()[0].toString(), pointcut.getArgs()[1].toString(), pointcut.getArgs()[2].toString());
						break;
					}
				}
			}
		}	
	}
}
