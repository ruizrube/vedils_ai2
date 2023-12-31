package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesAssets;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.ActivityTrackerInstances;
import com.google.appinventor.components.runtime.util.ActivityTrackerManager;
import com.google.appinventor.components.runtime.util.ActivityTrackerManagerFusionTables;
import com.google.appinventor.components.runtime.util.ActivityTrackerManagerLearningRecordStore;
import com.google.appinventor.components.runtime.util.ActivityTrackerManagerMongoDB;
//import com.google.appinventor.components.runtime.util.ActivityTrackerManagerStream;
//import com.google.appinventor.components.runtime.util.GlobalComponentsInstances;

import android.app.Activity;

/**
 * ActivityTracker Component
 * @author SPI-FM at UCA
 *
 */
@UsesAssets(fileNames = "ActivityTrackerVEDILS-e804e05b5eb3.p12")
@UsesLibraries(libraries =
"fusiontables.jar," +
"google-api-client-beta.jar," +
"google-api-client-android2-beta.jar," +
"google-http-client-beta.jar," +
"google-http-client-android2-beta.jar," +
"google-http-client-android3-beta.jar," +
"google-oauth-client-beta.jar," +
"guava-14.0.1.jar," +
"gson-2.1.jar," +
"jxapi.jar," +
"la4ai.jar")
@SimpleObject
@DesignerComponent(version = YaVersion.ACTIVITYTRACKER_COMPONENT_VERSION, 
description = "A component for logging activity in an application. " +
		"Actions to register can be <b>specific</b> or <b>automatic</b> type. " +
		"Finally, sending data can be performed in real time, user demand or in batches.", 
category = ComponentCategory.VEDILSLEARNINGANALYTICS, iconName = "images/activityTracker_icon.png", nonVisible= true)
@UsesPermissions(permissionNames = 
"android.permission.INTERNET, " +
"android.permission.ACCESS_NETWORK_STATE," +
"android.permission.WRITE_EXTERNAL_STORAGE, " +
"android.permission.READ_EXTERNAL_STORAGE," +
"android.permission.ACCESS_FINE_LOCATION," +
"android.permission.ACCESS_COARSE_LOCATION," +
"android.permission.ACCESS_MOCK_LOCATION," +
"android.permission.SYSTEM_ALERT_WINDOW," +
"android.permission.BIND_ACCESSIBILITY_SERVICE," +
"android.permission.ACCESS_LOCATION_EXTRA_COMMANDS," +
"android.permission.READ_PHONE_STATE")
public class ActivityTracker extends AndroidNonvisibleComponent implements Component {
	
	private String userTrackerId;
	private final ComponentContainer componentContainer;
	private String tableId;
	private int synchronizationMode;
	private int batchTime;
	private int communicationMode;
	private boolean startTracking;
	private ActivityTrackerManager activityTrackerManager;
	private int storageMode;
	private boolean streamMode;
	private User user;
	
	public ActivityTracker(ComponentContainer componentContainer) {
		super(componentContainer.$form());

		this.componentContainer = componentContainer; 
		
		//Default mode
		this.synchronizationMode = Component.REALTIME;
		this.batchTime = 0;
		this.communicationMode = Component.INDIFFERENT;
		this.startTracking = false;
		this.storageMode = Component.FUSIONTABLES;
		activityTrackerManager = new ActivityTrackerManagerFusionTables(this, componentContainer);
		this.streamMode = false;
		
		//Record current ActivityTracker
		ActivityTrackerInstances.insertActivityTracker((Activity)componentContainer.$context(), this);
		
		System.out.println("ActivityTracker created - AspectJ.");
		
		//Intent intent = new Intent(componentContainer.$form().getApplication(), ActivityTrackerBackgroundService.class);
		
		/*Gson gson = new Gson();
		intent.putExtra("ActivityTracker", gson.toJson(this));*/
		
		//componentContainer.$form().startService(intent);
		
		//System.out.println("AccessibilityService: ActivityTracker lanzando service..");
	}
	
	/**
	 * Returns the current ActivityTrackerManager.
	 * 
	 * @return ActivityTrackerManager
	 */
	public ActivityTrackerManager getActivityTrackerManager() {
		return activityTrackerManager;
	}
	
	/**
	 * Specifies the user data associated.
	 * 
	 * @param user
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ONLY_USER,
		      defaultValue = "")
		  @SimpleProperty(userVisible = false)
	public void User(User user) {
		this.user = user;		
	} 
	
	public User getUser() {
		
		/*if(this.user == null) {
			this.user = (User) GlobalComponentsInstances.getGlobalComponentByType(User.class);
		}*/
		
		return this.user;
	}
	
	/**
	 * Specifies the storage mode used.
	 * 
	 * @param storage
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STORAGEMODE,
		      defaultValue = Component.FUSIONTABLES + "")
		  @SimpleProperty(
		      userVisible = false)
	public void StorageMode(int storageMode) {
		this.storageMode = storageMode;
		
		//Configure ActivityTrackerManager to send data (Fusion Tables, MongoDB or LRS storage mode).
		if(this.storageMode == Component.FUSIONTABLES) {
			activityTrackerManager = new ActivityTrackerManagerFusionTables(this, componentContainer);
		} else if(this.storageMode == Component.MONGODB) {
			activityTrackerManager = new ActivityTrackerManagerMongoDB(this, componentContainer);
		} else if(this.storageMode == Component.LEARNINGRECORDSTORE) {
			activityTrackerManager = new ActivityTrackerManagerLearningRecordStore(this, componentContainer);
		}
	}
	
	/**
	 * Returns the current storage mode.
	 * @return  one of {@link Component#FUSIONTABLES} or
	 *          {@link Component#MONGODB}
	 */
	@SimpleProperty(
		      category = PropertyCategory.BEHAVIOR,
		      description = "Storage mode for ActivityTracker component.",
		      userVisible = false)
    public int StorageMode() {
		return this.storageMode;
    }
	
	
	/**
	 * Allows to enable / disable the stream mode.
	 * 
	 * @param streamMode
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
		      defaultValue = false + "")
		  @SimpleProperty(
		      userVisible = false)
	public void StreamMode(boolean streamMode) {
		this.streamMode = streamMode;
	}
	
	public boolean getStreamMode() {
		return this.streamMode;
	}
	
	/**
	 * Specifies the userId of the application.
	 * 
	 * @param userId
	 */
	@Deprecated
	/*@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "")*/
	@SimpleProperty
	public void UserTrackerId(String userTrackerId) {
		this.userTrackerId = userTrackerId;
		
	}
	
	/**
	 * Return the value of userId of the application.
	 * 
	 * Return userId
	 */
	@Deprecated
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the value of userId of the application.", userVisible = true)
	public String UserTrackerId() {
		return this.userTrackerId;
	}
	
	@Deprecated
	public String getUserTrackerId() {
		return this.userTrackerId;
	}
	
	/**
	 * Specifies the tableId of Fusion Table (Google) to establish the connection.
	 * 
	 * @param tableId
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_TEXTBOX_AND_HYPERLINK_FORACTIVITYTRACKER,
		      defaultValue = "")
	@SimpleProperty
	public void TableId(String tableId) {
		this.tableId = tableId;
	}
	
	/**
	 * Returns the id of the current Fusion Table.
	 * 
	 * @return tableId
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the id of the current Fusion Table", userVisible = true)
	public String TableId() {
		return tableId;
	}
	
	public String getTableId() {
		return tableId;
	}
	
	/**
	 * Specifies the communication mode.
	 * @param communicationMode
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMMUNICATIONMODE,
		      defaultValue = Component.INDIFFERENT + "")
		  @SimpleProperty(
		      userVisible = false)
	public void CommunicationMode(int communicationMode) {
		this.communicationMode = communicationMode;
    }
	
	/**
	 * Returns the current communication mode.
	 * @return  one of {@link Component#COMMUNICATIONMODE_ONLY_WIFI} or
	 *          {@link Component#COMMUNICATIONMODE_INDIFFERENT}
	 */
	@SimpleProperty(
		      category = PropertyCategory.BEHAVIOR,
		      description = "Communication mode for ActivityTracker component.",
		      userVisible = false)
    public int CommunicationMode() {
		return this.communicationMode;
    }
	
	public int getCommunicationMode() {
		return this.communicationMode;
	}
	
	/**
	 * Specifies the synchronization mode.
	 * @param synchronizationMode
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_SYNCHRONIZATIONMODE,
		      defaultValue = Component.REALTIME + "")
		  @SimpleProperty(
		      userVisible = false)
	public void SynchronizationMode(int synchronizationMode) {
		this.synchronizationMode = synchronizationMode;
    }
	
	/**
	 * Returns the current synchronization mode.
	 * @return  one of {@link Component#SYNCHRONIZATIONMODE_REALTIME},
	 *          {@link Component#SYNCHRONIZATIONMODE_BATCH} or
	 *          {@link Component#SYNCHRONIZATIONMODE_ONDEMAND}
	 */
	@SimpleProperty(
		      category = PropertyCategory.BEHAVIOR,
		      description = "Synchronization mode for ActivityTracker component.",
		      userVisible = false)
    public int SynchronizationMode() {
		return this.synchronizationMode;
    }
	
	public int getSynchronizationMode() {
		return this.synchronizationMode;
	}
	
	/**
	 * Specifies the bachTime for the batch type connection.
	 * 
	 * @param bachTime
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER,
		      defaultValue = "0")
	@SimpleProperty
	public void BatchTime(int batchTime) {
		this.batchTime = batchTime;
	}
	
	/**
	 * Returns the id of the current Fusion Table.
	 * 
	 * @return batchTime
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the current bachTime in seconds.", userVisible = true)
	public int BatchTime() {
		return this.batchTime;
	}
	
	public int getBatchTime() {
		return this.batchTime;
	}
	
	
	/**
	 *Function to notify a specific action (version without parameters).
	 * 
	 * @param actionId
	 */
	@SimpleFunction(description="Function to notify a specific action (version without arguments).")
	public void NotifyWithoutArguments(String actionId) {
		activityTrackerManager.prepareQueryManual(actionId, "", "", "");
	}
	
	
	/**
	 * Function to notify a specific action (version with one argument).
	 * 
	 * @param actionId
	 * @param valueArgument
	 */
	@Deprecated 
	@SimpleFunction(description="DEPRECATED. Function to notify a specific action (version with one argument).")
	public void NotifyWithOneArgument(String actionId, String valueArgument) {
		activityTrackerManager.prepareQueryManual(actionId, valueArgument, "", "");
	}
	
	
	/**
	 * Function to notify a specific action (version with two arguments).
	 * 
	 * @param actionId
	 * @param valueArgument
	 * @param valueArgument2
	 */
	@Deprecated
	@SimpleFunction(description="DEPRECATED. Function to notify a specific action (version with two arguments).")
	public void NotifyWithTwoArguments(String actionId, String valueArgument, String valueArgument2) {
		activityTrackerManager.prepareQueryManual(actionId, valueArgument, valueArgument2, "");
	}
	
	/**
	 * Function to notify a specific action (version with three arguments).
	 * 
	 * @param actionId
	 * @param valueArgument
	 * @param valueArgument2
	 * @param valueArgument3
	 */
	@Deprecated
	@SimpleFunction(description="DEPRECATED. Function to notify a specific action (version with three arguments).")
	public void NotifyWithThreeArguments(String actionId, String valueArgument, String valueArgument2, String valueArgument3) {
		activityTrackerManager.prepareQueryManual(actionId, valueArgument, valueArgument2, valueArgument3);
	}
	
	/**
	 * Function to notify a specific action (version to send multiple data in List(key,value) format).
	 * 
	 */
	@SimpleFunction(description="Function to send an specific xAPI statment.")
	public void SendStatement(String actionId, Object data) {
		activityTrackerManager.prepareQueryManual(actionId, data);
	}
	
	
	/**
	 * Function to notify a specific action (version to send multiple data in List(key,value) format).
	 * 
	 */
	@SimpleFunction(description="Function to notify a specific action (version to send multiple data in List(key,value) format).")
	public void NotifyWithData(String actionId, Object data) {
		activityTrackerManager.prepareQueryManual(actionId, data);
	}
	
	
	/**
	 * Function to notify a specific action (version to send multiple data in List(key,value) format).
	 * 
	 */
	@SimpleFunction(description="Function to return the statement of a notify specific action (version to send multiple data in List(key,value) format).")
	public Object NotifyAndReturnStatement(String actionId, Object data) {
		return activityTrackerManager.prepareQueryManualWithReturn(actionId, data);
	}
	
	/**
	 * Function to send data on user demand.
	 */
	@SimpleFunction(description="Function to send data on user demand.")
	public void PublishActivities() {
		activityTrackerManager.recordDataBatch();
	}
	
	/**
	 * Function to start automatic tracking.
	 */
	@SimpleFunction(description="Function to start automatic tracking.")
	public void StartTracking() {
		this.startTracking = true;
	}
	
	/**
	 * Function to stop automatic tracking.
	 */
	@SimpleFunction(description="Function to stop automatic tracking.")
	public void StopTracking() {
		this.startTracking = false;
	}
	
	public boolean getTrackingStatus() {
		return this.startTracking;
	}
}
