package com.google.appinventor.components.runtime.util;

import java.util.ArrayList;
import java.util.List;

import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;

public class GlobalComponentsInstances {
	
	private static List<AndroidNonvisibleComponent> globalComponents = new ArrayList<AndroidNonvisibleComponent>();
	
	public static void addGlobalComponent(AndroidNonvisibleComponent component) {
		globalComponents.add(component);
	}
	
	public static List<AndroidNonvisibleComponent> getGlobalComponents() {
		return globalComponents;
	}
	
	public static <T> AndroidNonvisibleComponent getGlobalComponentByType(Class<T> type) {
		for(AndroidNonvisibleComponent component: globalComponents) {
			System.out.println("GLOBALES el component class es: " + component.getClass() + " y el type buscado es = " + type);
			if(component.getClass().equals(type)) {
				return component;
			}
		}
		return null;
	}
}
