package com.google.appinventor.components.runtime;

import com.leapmotion.leap.Controller;
import java.io.IOException;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesNativeLibraries;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.LeapMotionSensor;

/**
 * @author ivanruizrube
 * 
 */
@UsesLibraries(libraries = "LeapJava.jar")
@UsesNativeLibraries(v7aLibraries = "libLeapJava.so")
@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "HandGestureSensor Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSINTERACTIONS, iconName = "images/handGestureSensor.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET, " + "android.permission.ACCESS_NETWORK_STATE,"
		+ "android.permission.WRITE_EXTERNAL_STORAGE, " + "android.permission.READ_EXTERNAL_STORAGE,"
		+ "android.permission.ACCESS_FINE_LOCATION," + "android.permission.ACCESS_COARSE_LOCATION,"
		+ "android.permission.ACCESS_MOCK_LOCATION," + "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS")
public class HandGestureSensor extends AndroidNonvisibleComponent implements Component {

	Controller controller;
	LeapMotionSensor listener;

	public HandGestureSensor(ComponentContainer componentContainer) {
		super(componentContainer.$form());
	}

	////////////
	// EVENTS //
	////////////

	/**
	 * Event to be raised when a hand is recognized
	 * hand: left 0, right 1
	 */
	@SimpleEvent(description = "Event to be raised when a hand is recognized", userVisible = true)
	public void HandAppears(int hand) {
		EventDispatcher.dispatchEvent(this, "HandAppears", hand);
	}

	/**
	 * Event to be raised when a hand is recognized
	 * hand: left 0, right 1
	 */
	@SimpleEvent(description = "Event to be raised when a hand is not longer recognized", userVisible = true)
	public void HandDisappears(int hand) {
		EventDispatcher.dispatchEvent(this, "HandDisappears", hand);
	}

	
	/**
	 * Event to be raised when the tip of a finger draws a circle within the field of view of the motion 
	 * sensor hand: left 0, right 1 
	 * direction: clockwise, 0 counterclockwise 1
	 * progress: for example, a value of 3 indicates that the finger has gone around the the circle three times
	 */
	@SimpleEvent(description = "Event to be raised when the tip of a finger draws a circle within the field of view of the motion sensor", userVisible = true)
	public void CircleGesture(int hand, int finger, int direction, float progress) {
		EventDispatcher.dispatchEvent(this, "CircleGesture", hand, finger, direction, progress);
	}

	/**
	 * Event to be raised when a swiping motion of finger or tool has been done
	 * hand: left 0, right 1 
	 * finger: 1, 2, 3, 4, 5 
	 * direction: left 0, right 1, up 2, down 3
	 */
	@SimpleEvent(description = "Event to be raised when a swiping motion of finger or tool has been done", userVisible = true)
	public void SwipeGesture(int hand, int finger, int direction) {
		EventDispatcher.dispatchEvent(this, "SwipeGesture", hand, finger, direction);
	}

	/**
	 * Event to be raised when the tip of a finger rotates down toward the palm
	 * and then springs back to approximately the original position hand: left
	 * 0, right 1 finger: 1, 2, 3, 4, 5
	 */
	@SimpleEvent(description = "Event to be raised when the tip of a finger rotates down toward the palm and then springs back to approximately the original position", userVisible = true)
	public void KeyTapGesture(int hand, int finger) {
		EventDispatcher.dispatchEvent(this, "KeyTapGesture", hand, finger);
	}

	/**
	 * Event to be raised when the tip of a finger pokes forward and then
	 * springs back to approximately the original position, as if tapping a
	 * vertical screen. hand: left 0, right 1 finger: 1, 2, 3, 4, 5
	 */
	@SimpleEvent(description = "Event to be raised when the tip of a finger pokes forward and then springs back to approximately the original position, as if tapping a vertical screen", userVisible = true)
	public void ScreenTapGesture(int hand, int finger) {
		EventDispatcher.dispatchEvent(this, "ScreenTapGesture", hand, finger);
	}

	/**
	 * Event to be raised when a change in the orientation of a single hand is
	 * recognized hand: left 0, right 1 angle: radians
	 */
	@SimpleEvent(description = "Event to be raised when a change in the orientation of a single hand is recognized", userVisible = true)
	public void RotateHand(int hand, int angle) {
		EventDispatcher.dispatchEvent(this, "RotateHand", hand, angle);
	}

	/**
	 * Event to be raised when a change in position of that hand is recognized
	 * hand: left 0, right 1 angle: radians
	 */
	@SimpleEvent(description = "Event to be raised when a change in position of that hand is recognized", userVisible = true)
	public void TranslateHand(int hand, int angle) {
		EventDispatcher.dispatchEvent(this, "TranslateHand", hand, angle);
	}

	/**
	 * Event to be raised when a change in finger spread is recognized hand:
	 * left 0, right 1 angle: radians
	 */
	@SimpleEvent(description = "Event to be raised when a change in finger spread is recognized", userVisible = true)
	public void ScaleHand(int hand, int angle) {
		EventDispatcher.dispatchEvent(this, "ScaleHand", hand, angle);
	}

	/**
	 * Start the recognizer of hand gestures
	 */
	@SimpleFunction(description = "Start the recognizer of hand gestures", userVisible = true)
	public void Start() {
		// Create a listener and controller
		if (listener == null) {
			listener = new LeapMotionSensor();
			listener.setComponent(this);
			
		}
		if (controller == null) {
			controller = new Controller();
		}

		// Have the sample listener receive events from the controller
		controller.addListener(listener);

	}

	/**
	 * Stop the recognizer of hand gestures
	 */
	@SimpleFunction(description = "the recognizer of hand gestures", userVisible = true)
	public void Stop() {

		if (listener != null && controller != null) {

			// Remove the sample listener when done
			controller.removeListener(listener);
		}

	}

}
