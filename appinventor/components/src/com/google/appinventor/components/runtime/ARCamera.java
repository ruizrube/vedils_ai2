/**
 * 
 */
package com.google.appinventor.components.runtime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesNativeLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.ar4ai.ARActivity;
import com.google.appinventor.components.runtime.ar4ai.Camera;
import com.google.appinventor.components.runtime.ar4ai.PhysicalObject;
import com.google.appinventor.components.runtime.ar4ai.VirtualObject;
import com.google.appinventor.components.runtime.util.ARCameraBroadCastReceiver;
import com.google.appinventor.components.runtime.util.ARPhysicalObjectBroadCastReceiver;
import com.google.appinventor.components.runtime.util.ARVirtualObjectBroadCastReceiver;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.OnInitializeListener;
import com.google.appinventor.components.runtime.util.SdkLevel;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.support.v4.content.LocalBroadcastManager;

/**
 * @author ivanruizrube
 *
 */
@UsesLibraries(libraries = "android-support-v4.jar, ar4ai.jar, Vuforia.jar, jpct_ae.jar")
@UsesNativeLibraries(v7aLibraries = "libVuforia.so")
@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "Augmented Reality Camera Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSAUGMENTEDREALITY, iconName = "images/arCamera.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.CAMERA, android.permission.ACCESS_NETWORK_STATE,android.permission.WRITE_EXTERNAL_STORAGE, android.permission.READ_EXTERNAL_STORAGE")

public class ARCamera extends AndroidNonvisibleComponent
		implements Serializable, ActivityResultListener, OnInitializeListener, OnResumeListener, OnStopListener, OnPauseListener, OnDestroyListener {

	/////////////////////
	// CLASS ATTRIBUTES //
	/////////////////////

	private static final long serialVersionUID = 1L;
	private static ARCamera instance;

	/////////////////
	// CONSTANTS //
	/////////////////

	/**
	 * @return the instance
	 */
	public static ARCamera getInstance() {
		return instance;
	}

	private static final String AR_ACTIVITY_CLASS = "com.google.appinventor.components.runtime.ar4ai.vuforia.VuforiaARActivity";

	/////////////////////
	// LOCAL ATTRIBUTES //
	/////////////////////

	private final ComponentContainer container;

	ARPhysicalObjectBroadCastReceiver poEventBroadCastReceiver = new ARPhysicalObjectBroadCastReceiver();
	ARVirtualObjectBroadCastReceiver voEventBroadCastReceiver = new ARVirtualObjectBroadCastReceiver();
	ARCameraBroadCastReceiver cameraEventBroadCastReceiver = new ARCameraBroadCastReceiver();

	public Camera data;
	public /*static*/ HashMap<String, ARVirtualObject> mapOfARVirtualObjects = new HashMap<String, ARVirtualObject>();
	public /*static*/ HashMap<String, ARPhysicalObject> mapOfARPhysicalObjects = new HashMap<String, ARPhysicalObject>();

	/////////////////
	// CONSTRUCTOR //
	/////////////////

	public ARCamera(ComponentContainer container) {
		super(container.$form());
		this.container = container;

		container.$form().registerForOnInitialize(this);
		container.$form().registerForOnResume(this);
		container.$form().registerForOnPause(this);
		container.$form().registerForOnStop(this);
		container.$form().registerForOnDestroy(this);

		data = new Camera();
		instance = this;
	}

	///////////////////////
	// OVERRIDED METHODS //
	///////////////////////

	@Override
	public void onInitialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resultReturned(int requestCode, int resultCode, Intent data) {
		if (requestCode == this.data.getRequestCode() && resultCode == Activity.RESULT_OK) {
			if (data != null && data.getData() != null) {
				AfterARCameraClosed();// ("AR camera closed with data: " +
										// data.getDataString());
			} else {
				AfterARCameraClosed();// ("AR camera closed with no data");
			}
		} else {
			AfterARCameraClosed();// ("AR camera aborted!");
		}

		// Unregister since the activity is not visible
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(poEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(voEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(cameraEventBroadCastReceiver);

	}

	//////////////////////
	// INTERNAL METHODS //
	//////////////////////

	private Intent getIntent() {
		Intent intent = new Intent();
		intent.setClassName(container.$context(), AR_ACTIVITY_CLASS);

		intent.putExtra(ARActivity.AR_ACTIVITY_ARG_CAMERAOBJECT, this.data);
		intent.putExtra(ARActivity.AR_ACTIVITY_ARG_VIRTUALOBJECTS, extractPureVO(mapOfARVirtualObjects));
		intent.putExtra(ARActivity.AR_ACTIVITY_ARG_PHYSICALOBJECTS, extractPurePO(mapOfARPhysicalObjects));

		
		
		
		return intent;
	}

	private ArrayList<VirtualObject> extractPureVO(HashMap<String, ARVirtualObject> myMap) {
		ArrayList<VirtualObject> result = new ArrayList<VirtualObject>();
		for (String key : myMap.keySet()) {
			result.add(myMap.get(key).getData());
		}
		return result;
	}

	private ArrayList<PhysicalObject> extractPurePO(HashMap<String, ARPhysicalObject> myMap) {
		ArrayList<PhysicalObject> result = new ArrayList<PhysicalObject>();
		for (String key : myMap.keySet()) {
			result.add(myMap.get(key).getData());
			
		}
		return result;
	}

	////////////////
	// PROPERTIES //
	////////////////

	/**
	 * @return the stereo
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public boolean Stereo() {
		return data.isStereo();
	}

	/**
	 * @param stereo
	 *            the stereo to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
	@SimpleProperty(description = "Specifies if the reality reproduction is stereophonic", userVisible = true)
	public void Stereo(boolean stereo) {
		this.data.setStereo(stereo);
	}

	/**
	 * @return the frontCamera
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public boolean UseFront() {
		return data.isFrontCamera();
	}

	/**
	 * @param frontCamera
	 *            the frontCamera to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
	@SimpleProperty(description = "Specifies if the front camera is used for capturing video", userVisible = true)
	public void UseFront(boolean frontCamera) {
		this.data.setFrontCamera(frontCamera);
	}

	/**
	 * @return the screenOrientation
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String ScreenOrientation() {
		switch (data.getScreenOrientation()) {
		case ActivityInfo.SCREEN_ORIENTATION_BEHIND:
			return "behind";
		case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
			return "landscape";
		case ActivityInfo.SCREEN_ORIENTATION_NOSENSOR:
			return "nosensor";
		case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
			return "portrait";
		case ActivityInfo.SCREEN_ORIENTATION_SENSOR:
			return "sensor";
		case ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED:
			return "unspecified";
		case ActivityInfo.SCREEN_ORIENTATION_USER:
			return "user";
		case 10: // ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
			return "fullSensor";
		case 8: // ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
			return "reverseLandscape";
		case 9: // ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
			return "reversePortrait";
		case 6: // ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
			return "sensorLandscape";
		case 7: // ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
			return "sensorPortrait";
		}

		return "unspecified";
	}

	/**
	 * @param screenOrientation
	 *            the screenOrientation to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_SCREEN_ORIENTATION, defaultValue = "portrait")
	@SimpleProperty(description = "Specifies the requested screen orientation, specified as a text value.", userVisible = true)
	public void ScreenOrientation(String screenOrientation) {
		if (screenOrientation.equalsIgnoreCase("behind")) {
			this.data.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
		} else if (screenOrientation.equalsIgnoreCase("landscape")) {
			this.data.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else if (screenOrientation.equalsIgnoreCase("nosensor")) {
			this.data.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		} else if (screenOrientation.equalsIgnoreCase("portrait")) {
			this.data.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else if (screenOrientation.equalsIgnoreCase("sensor")) {
			this.data.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		} else if (screenOrientation.equalsIgnoreCase("unspecified")) {
			this.data.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		} else if (screenOrientation.equalsIgnoreCase("user")) {
			this.data.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
		} else if (SdkLevel.getLevel() >= SdkLevel.LEVEL_GINGERBREAD) {
			if (screenOrientation.equalsIgnoreCase("fullSensor")) {
				this.data.setScreenOrientation(10); // ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
			} else if (screenOrientation.equalsIgnoreCase("reverseLandscape")) {
				this.data.setScreenOrientation(8); // ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
			} else if (screenOrientation.equalsIgnoreCase("reversePortrait")) {
				this.data.setScreenOrientation(9); // ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
			} else if (screenOrientation.equalsIgnoreCase("sensorLandscape")) {
				this.data.setScreenOrientation(6); // ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
			} else if (screenOrientation.equalsIgnoreCase("sensorPortrait")) {
				this.data.setScreenOrientation(7); // ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
			} else {
				this.form.dispatchErrorOccurredEvent(this, "ScreenOrientation", ErrorMessages.ERROR_INVALID_SCREEN_ORIENTATION, screenOrientation);
			}
		} else {
			this.form.dispatchErrorOccurredEvent(this, "ScreenOrientation", ErrorMessages.ERROR_INVALID_SCREEN_ORIENTATION, screenOrientation);
		}
	}

	/**
	 * Returns the path of the XML file of the object database.
	 *
	 * @return the path of the XML file of the object database
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String ObjectDatabaseXML() {
		return data.getPathTargetDBXML();
	}

	/**
	 * Returns the path of the DAT file of the object database.
	 *
	 * @return the path of the DAT file of the object database
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String ObjectDatabaseDAT() {
		return data.getPathTargetDBDAT();
	}

	/**
	 * Specifies the path XML file of the object database.
	 *
	 * <p/>
	 * See {@link MediaUtil#determineMediaSource} for information about what a
	 * path can be.
	 *
	 * @param path
	 *            the path of the XML file of the object database
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET_DATABASE_XML, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void ObjectDatabaseXML(String path) {
		data.setPathTargetDBXML((path == null) ? "" : path);
	}

	/**
	 * Specifies the path DAT file of the object database.
	 *
	 * <p/>
	 * See {@link MediaUtil#determineMediaSource} for information about what a
	 * path can be.
	 *
	 * @param path
	 *            the path of the DAT file of the object database
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET_DATABASE_DAT, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void ObjectDatabaseDAT(String path) {
		data.setPathTargetDBDAT((path == null) ? "" : path);
	}

	/**
	 * @return the title
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public String Title() {
		return data.getTitle();
	}

	/**
	 * @param title
	 *            the title to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(description = "Specifies the title of the AR scene", userVisible = true)
	public void Title(String text) {
		this.data.setTitle(text);
	}

	/**
	 * @return the subtitle
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public String Subtitle() {
		return data.getSubtitle();
	}

	/**
	 * @param Subtitle
	 *            the Subtitle to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(description = "Specifies the Subtitle of the AR scene", userVisible = true)
	public void Subtitle(String text) {
		this.data.setSubtitle(text);
	}
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public boolean LeftButtonEnabled() {
		return data.isLeftBtEnabled();
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
	@SimpleProperty(description = "Enables the left button on the user interface over the camera", userVisible = true)
	public void LeftButtonEnabled(boolean enable) {
		this.data.setLeftBtEnabled(enable);
	}
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public boolean RightButtonEnabled() {
		return data.isRightBtEnabled();
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
	@SimpleProperty(description = "Enables the right button on the user interface over the camera", userVisible = true)
	public void RightButtonEnabled(boolean enable) {
		this.data.setRightBtEnabled(enable);
	}
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String LeftButtonText() {
		return data.getLeftBtText();
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(description = "Sets the text for the left button on the user interface over the camera", userVisible = true)
	public void LeftButtonText(String text) {
		this.data.setLeftBtText(text);
	}
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String RightButtonText() {
		return data.getRightBtText();
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(description = "Sets the text for the left button on the user interface over the camera", userVisible = true)
	public void RightButtonText(String text) {
		this.data.setRightBtText(text);
	}
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String FloatingText() {
		return data.getFloatingText();
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(description = "Sets the text in the middle of the buttons on the user interface over the camera", userVisible = true)
	public void FloatingText(String text) {
		this.data.setFloatingText(text);
	}
	////////////////
	// FUNCTIONS //
	////////////////

	/**
	 * Start the augmented reality camera
	 */
	@SimpleFunction(description = "Start the augmented reality camera", userVisible = true)
	public void Start() {
		if (data.getRequestCode() == 0) { // only need to register once
			data.setRequestCode(container.$form().registerForActivityResult(this));
		}
		container.$context().startActivityForResult(getIntent(), data.getRequestCode());

		LocalBroadcastManager.getInstance(container.$context()).registerReceiver(poEventBroadCastReceiver, new IntentFilter(ARActivity.AR_ACTIVITY_EVENT_PO));
		LocalBroadcastManager.getInstance(container.$context()).registerReceiver(voEventBroadCastReceiver, new IntentFilter(ARActivity.AR_ACTIVITY_EVENT_VO));
		LocalBroadcastManager.getInstance(container.$context()).registerReceiver(cameraEventBroadCastReceiver, new IntentFilter(ARActivity.AR_ACTIVITY_EVENT_CAMERA));
	}

	/**
	 * Stop the augmented reality camera
	 */
	@SimpleFunction(description = "Stop the augmented reality camera", userVisible = true)
	public void Stop() {
		Intent intent = new Intent(ARActivity.AR_ACTIVITY_SIGNAL_STOP);
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(intent);

	}

	/**
	 * Reinitialize all the physical and virtual objects on the augmented
	 * reality camera
	 */
	@SimpleFunction(description = "Reinitialize all the physical and virtual objects on the augmented reality camera", userVisible = true)
	public void Refresh() {
		Intent intent = new Intent(ARActivity.AR_ACTIVITY_SIGNAL_REFRESH);

		ArrayList<VirtualObject> aux = extractPureVO(mapOfARVirtualObjects);

		intent.putExtra(ARActivity.AR_ACTIVITY_ARG_CAMERAOBJECT, this.data);
		intent.putExtra(ARActivity.AR_ACTIVITY_ARG_VIRTUALOBJECTS, aux);
		intent.putExtra(ARActivity.AR_ACTIVITY_ARG_PHYSICALOBJECTS, extractPurePO(mapOfARPhysicalObjects));
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(intent);

	}


	/**
	 * Updates the header of the AR camera
	 */
	@SimpleFunction(description = "Updates the header of the AR camera", userVisible = true)
	public void UpdateHeader() {
		Intent intent = new Intent(ARActivity.AR_ACTIVITY_SIGNAL_UPDATE_ACTIONBAR);

		intent.putExtra(ARActivity.AR_ACTIVITY_ARG_CAMERAOBJECT, this.data);
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(intent);

	}

	
	////////////////
	// EVENTS //
	////////////////

	/**
	 * When the user touches the AR camera and then immediately lifts finger:
	 * provides the (x,y) position of the touch, relative to the upper left of
	 * the canvas. touchedAnyVirtualObject is true if the same touch also
	 * touched a sprite, and false otherwise.
	 *
	 * @param x
	 *            x-coordinate of the point that was touched
	 * @param y
	 *            y-coordinate of the point that was touched
	 * @param touchedAnySprite
	 *            {@code true} if a sprite was touched, {@code false} otherwise
	 */
	@SimpleEvent(description = "When the user touches the camera and then immediately lifts finger", userVisible = true)
	public void Touched(float x, float y, boolean touchedAnyVirtualObject) {
		EventDispatcher.dispatchEvent(this, "Touched", x, y, touchedAnyVirtualObject);
	}

	/**
	 * Event to be raised after AR camera has been closed
	 */
	@SimpleEvent(description = "When the AR camera has been closed", userVisible = true)
	public void AfterARCameraClosed() {
		EventDispatcher.dispatchEvent(this, "AfterARCameraClosed");
	}
	
	@SimpleEvent(description = "When the left button is clicked", userVisible = true)
	public void LeftButtonClick() {
		EventDispatcher.dispatchEvent(this, "LeftButtonClick");
	}
	
	@SimpleEvent(description = "When the right button is clicked", userVisible = true)
	public void RightButtonClick() {
		EventDispatcher.dispatchEvent(this, "RightButtonClick");
	}

}
