package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesNativeLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.MyoSensor;
import com.thalmic.myo.Hub;

import android.app.Activity;

/**
 * 
 * @author SPI-FM
 *
 */

@UsesLibraries(libraries = "Myo-connector.jar")
@UsesNativeLibraries(v7aLibraries = "libgesture-classifier.so")
@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "ArmbandGestureSensor Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSINTERACTIONS, iconName = "images/armbandSensor.png")
@UsesPermissions(permissionNames = "android.permission.BLUETOOTH, "
		+ "android.permission.BLUETOOTH_ADMIN,"
		+ "android.permission.INTERNET")
public class ArmbandGestureSensor extends AndroidNonvisibleComponent implements Component {
	
	MyoSensor listener;
	Hub hub;
	Activity activity;
	
	public ArmbandGestureSensor(ComponentContainer componentContainer) {
		super(componentContainer.$form());
		activity = componentContainer.$context();
	}
	
	//Events:
	@SimpleEvent(description = "Event to be raised when Myo device is connected", userVisible = true)
	public void DeviceConnected() {
		EventDispatcher.dispatchEvent(this, "DeviceConnected");
	}
	
	@SimpleEvent(description = "Event to be raised when Myo device is connected", userVisible = true)
	public void DeviceDisconnected() {
		EventDispatcher.dispatchEvent(this, "DeviceDisconnected");
	}
	
	/**
	 * left = 0, right = 1
	 */
	@SimpleEvent(description = "Event to be raised when KeyTap gesture occurs.", userVisible = true)
	public void KeyTapGesture(int hand) {
		EventDispatcher.dispatchEvent(this, "KeyTapGesture", hand);
	}
	
	/**
	 * left = 0, right = 1
	 */
	@SimpleEvent(description = "Event to be raised when Wave in gesture occurs.", userVisible = true)
	public void WaveInGesture(int hand) {
		EventDispatcher.dispatchEvent(this, "WaveInGesture", hand);
	}
	
	/**
	 * left = 0, right = 1
	 */
	@SimpleEvent(description = "Event to be raised when Wave out gesture occurs.", userVisible = true)
	public void WaveOutGesture(int hand) {
		EventDispatcher.dispatchEvent(this, "WaveOutGesture", hand);
	}
	
	/**
	 * left = 0, right = 1
	 */
	@SimpleEvent(description = "Event to be raised when Fist gesture occurs.", userVisible = true)
	public void FistGesture(int hand) {
		EventDispatcher.dispatchEvent(this, "FistGesture", hand);
	}
	
	/**
	 * left = 0, right = 1
	 */
	@SimpleEvent(description = "Event to be raised when Fingers Spread gesture occurs.", userVisible = true)
	public void FingersSpreadGesture(int hand) {
		EventDispatcher.dispatchEvent(this, "FingersSpreadGesture", hand);
	}
	
	@SimpleEvent(description = "Event to be raised when Myo provides his orientation data.", userVisible = true)
	public void OrientationData(float x, float y, float z) {
		EventDispatcher.dispatchEvent(this, "OrientationData", x, y, z);
	}
	
	@SimpleEvent(description = "Event to be raised when Myo is synchronized.", userVisible = true)
	public void DeviceSynchronized() {
		EventDispatcher.dispatchEvent(this, "DeviceSynchronized");
	}
	
	@SimpleEvent(description = "Event to be raised when Myo is unsynchronized.", userVisible = true)
	public void DeviceUnsynchronized() {
		EventDispatcher.dispatchEvent(this, "DeviceUnsynchronized");
	}
	
	//Functions:
	/**
	 * Start the recognizer of hand gestures
	 */
	@SimpleFunction(description = "Start the recognizer of armband gestures", userVisible = true)
	public void Start() {
		// Create a listener and controller
		if (listener == null) {
			listener = new MyoSensor();
			listener.setComponent(this);
		}
		
		if (hub == null) {
			hub = Hub.getInstance();
			if (!hub.init(activity)) {
			    System.out.println("Myo: Could not initialize the Hub.");
			}
		}
		
		//Connect to a nearby Myo
		hub.attachToAdjacentMyo();
		//Intent intent = new Intent(activity, ScanActivity.class);
		//activity.startActivity(intent);

		// Have the sample listener receive events from the controller
		hub.addListener(listener);
	}
	
	/**
	 * Stop the recognizer of hand gestures
	 */
	@SimpleFunction(description = "the recognizer of armband gestures", userVisible = true)
	public void Stop() {

		if (listener != null && hub != null) {

			// Remove the sample listener when done
			hub.removeListener(listener);
			hub.shutdown();
		}

	}
}
