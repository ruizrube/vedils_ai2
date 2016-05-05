package com.google.appinventor.components.runtime;

import org.aspectj.lang.annotation.*;
import com.google.appinventor.components.runtime.NotifyAJManager;

import org.aspectj.lang.JoinPoint;

@Aspect
public class NotifyAJ {
	
	@Pointcut("within(@com.google.appinventor.components.annotations.DesignerComponent *)")
	public void onlyComponents() {}
	
	@Pointcut ("execution(@com.google.appinventor.components.annotations.SimpleFunction * com.google.appinventor.components.runtime.*.*(..))")
	private void onlyFunctions() {}
	
	@Pointcut ("execution(@com.google.appinventor.components.annotations.SimpleProperty * com.google.appinventor.components.runtime.*.*(..))")
	private void onlyGetOrSet() {}
	
	@Pointcut ("execution(@com.google.appinventor.components.annotations.SimpleEvent * com.google.appinventor.components.runtime.*.*(..))")
	private void onlyEvents() {}
	
	@AfterReturning (pointcut = "onlyComponents() && onlyFunctions()", returning="returnValue")
	public void recordDataOnlyFunctions(JoinPoint pointcut, Object returnValue) {
		if(pointcut.getThis() != null) {
			System.out.println("Before recordDataOnlyFunctions pointcut - AspectJ.");
			System.out.println("Class: " + pointcut.getThis().getClass() + " Method: " + pointcut.getSignature().getName() + " - AspectJ");
			
			NotifyAJManager.sendNotification(pointcut, "Function", returnValue);
		}
	}
	
	@AfterReturning(pointcut = "onlyComponents() && onlyGetOrSet()", returning="returnValue")
	public void recordDataOnlyGetOrSet(JoinPoint pointcut, Object returnValue) {
		if(pointcut.getThis() != null) {
			System.out.println("Before recordDataOnlyGetOrSet pointcut - AspectJ.");
			System.out.println("Class: " + pointcut.getThis().getClass() + " Method: " + pointcut.getSignature().getName() + " - AspectJ");
			
			if(pointcut.getArgs().length == 0) {
				NotifyAJManager.sendNotification(pointcut, "Get", returnValue);
			} else {
				NotifyAJManager.sendNotification(pointcut, "Set", returnValue);
			}	
		}
	}
	
	@AfterReturning(pointcut = "onlyComponents() && onlyEvents()", returning="returnValue")
	public void recordDataOnlyEvents(JoinPoint pointcut, Object returnValue) {
		if(pointcut.getThis() != null) {
			System.out.println("Before recordDataOnlyEvents pointcut - AspectJ.");
			System.out.println("Class: " + pointcut.getThis().getClass() + " Method: " + pointcut.getSignature().getName() + " - AspectJ");
			
			NotifyAJManager.sendNotification(pointcut, "Event", returnValue);
			
		}	
	}
}
