package com.google.appinventor.components.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

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
import com.google.appinventor.components.runtime.util.FusionTablesConnection;
import com.google.appinventor.components.runtime.util.TextViewUtil;
import com.google.appinventor.components.runtime.util.TimerSendData;
import com.google.appinventor.components.runtime.util.ActivityTrackerInstances;
import com.google.appinventor.components.runtime.util.ActivityTrackerManager;

import com.google.appinventor.components.runtime.la4ai.util.ConnectionInfo;
import com.google.appinventor.components.runtime.la4ai.util.TinyDB;

import android.app.Activity;
import android.content.Context;

@UsesAssets(fileNames = "ruizrube-cd84632c4ea8.p12, ruizrube-4718dd8c5168.json")
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
"la4ai.jar," +
"aspectjrt-1.8.8.jar")
@SimpleObject
@DesignerComponent(nonVisible= true, version = 1, description = "ActivityTracker Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSLEARNINGANALYTICS, iconName = "images/arColorTracker.png")
@UsesPermissions(permissionNames = 
"android.permission.INTERNET, " +
"android.permission.ACCESS_NETWORK_STATE," +
"android.permission.WRITE_EXTERNAL_STORAGE, " +
"android.permission.READ_EXTERNAL_STORAGE," +
"android.permission.ACCESS_FINE_LOCATION," +
"android.permission.ACCESS_COARSE_LOCATION," +
"android.permission.ACCESS_MOCK_LOCATION," +
"android.permission.ACCESS_LOCATION_EXTRA_COMMANDS")
public class ActivityTracker extends AndroidNonvisibleComponent implements Component {
	
	private String userTrackerId;
	//private final Activity activity;
	@SuppressWarnings("unused")
	private final ComponentContainer componentContainer;
	private String tableId;
	//private String columns;
	//private String values;
	//private String email;
	//private String apiKey;
	//private FusionTablesConnection fusionTablesConnection;
	//private String path;
	//private LocationSensor locationSensor;
	//private Context context;
	private int synchronizationMode;
	private int batchTime;
	private int communicationMode;
	private boolean startTracking;
	private boolean publishEvents;
	private boolean publishMethods;
	private boolean publishGetters;
	private boolean publishSetters;
	//private TinyDB tinyDB;
	//private int tagDB;
	//private TimerSendData timerSendData;
	//private String currentIP;
	private ActivityTrackerManager activityTrackerManager;
	
	public ActivityTracker(ComponentContainer componentContainer) {
		super(componentContainer.$form());
		
		activityTrackerManager = new ActivityTrackerManager(this, componentContainer);
		this.componentContainer = componentContainer; 
		//this.activity = componentContainer.$context();
		//this.locationSensor = new LocationSensor(componentContainer);
		//this.context = componentContainer.$context();
		//this.tinyDB = new TinyDB(componentContainer.$context());
		//this.tagDB = 0;
		//this.currentIP = "0.0.0.0";
		
		//Default mode
		this.synchronizationMode = Component.REALTIME;
		this.batchTime = 0;
		this.communicationMode = Component.INDIFFERENT;
		this.startTracking = false;
		this.publishEvents = true;
		this.publishMethods = true;
		this.publishGetters = true;
		this.publishSetters = true;
		//Define data for FusionTableControl connection.
        
		//columns = "UserID, IP, MAC, Latitude, Longitude, Date, AppID, ScreenID, ComponentID, ComponentType, ActionID, ActionType, Param1, Param2, Param3";
		tableId = "1xZCj24xYWpj6jHWN2IK2xiErYPY7XbeHAqXVR4Bw";
		//email = "1075849932338-n26pqlvqfea3dspaebf52vnacch77nhf@developer.gserviceaccount.com";
		//apiKey = "AIzaSyDL9s7r6ZIr9DN47_kNIIzRcm2JhWxy7ZU";
		//path = ASSET_DIRECTORY + '/' + "ruizrube-cd84632c4ea8.p12";
		
		//Connection with FusionTables
		
		//this.fusionTablesConnection = new FusionTablesConnection(columns, apiKey, path, email, componentContainer, true);
		//this.timerSendData = new TimerSendData(this);
		
		//And finally, save the current ActivityTracker
		ActivityTrackerInstances.insertActivityTracker(componentContainer.$context().getTitle().toString(), this);
		
		System.out.println("ActivityTracker created - AspectJ.");
		
	}
	
	//Record Data
	
	/*@SuppressWarnings({"unchecked"})
	private void recordData(String actionId, String param1, String param2, String param3) {
		
		String ip = ConnectionInfo.getCurrentIP(communicationMode, context);
		String mac = ConnectionInfo.getMAC(context);

	    String appName = context.getApplicationInfo().packageName;
	    String screenName = activity.getTitle().toString(); 
	    String actionType = "SPECIFIC";
	    //componentContainer.$form().getLocalClassName() (Devuelve el ScreenX también)
	    
	    //Do the query
		values = "'" + this.userTrackerId + "','" + 
		ip + "','" +
	    mac + "'," +
		locationSensor.Latitude() + "," +
		locationSensor.Longitude() + ",'" +
		Clock.FormatDate(Clock.Now(), "MM/dd/yyyy HH:mm:ss") + "','" +
		appName + "','" +
		screenName + "','" +
		"" + "','" +
		"" + "','" +
		actionId + "','" +
		actionType + "','" +
		param1 + "','" +
		param2 + "','" +
		param3 + "'";
		
		currentIP = ip;
		
		if(fusionTablesConnection.internetAccess(this.communicationMode) && synchronizationMode == Component.REALTIME) {
			//If internet access then send the values to FusionTables
				
			fusionTablesConnection.insertRow(values, this.tableId);
				
			//And if db is not empty send the content too
			List<String> listTags = (List<String>) tinyDB.GetTags();
			if(!listTags.isEmpty()) {
				recordDataBatch();
			}
			
		} else {
				
			if(synchronizationMode == Component.BATCH && tagDB == 0) { //if is the first call the function to wait batch sec.
				Timer timer = new Timer();
				timer.schedule(timerSendData, 0, this.batchTime * 1000);
			}
				
			tinyDB.StoreValue(Integer.toString(tagDB), values);
			System.out.println("Store value in TinyDB: " +values);
			tagDB++;
				
		}
	}*/
	
	/*@SuppressWarnings("unchecked")
	public void recordDataBatch() {
		List<String> listTags = (List<String>) tinyDB.GetTags();
		List<String> listValues = new ArrayList<String>();
		
		for(String tagAux: listTags) {
			listValues.add(tinyDB.GetValue(tagAux, "").toString().replaceFirst("'0.0.0.0'", "'" + this.currentIP + "'"));
		}
		
		if(fusionTablesConnection.internetAccess(this.communicationMode)) {
			fusionTablesConnection.insertRows(listValues, this.tableId);
			tinyDB.ClearAll();
		}
	}*/
	
	/**
	 * Returns the current ActivityTrackerManager.
	 * 
	 * @return ActivityTrackerManager
	 */
	public ActivityTrackerManager getActivityTrackerManager() {
		return activityTrackerManager;
	}
	
	/**
	 * Specifies the userId of the application.
	 * 
	 * @param userId
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "userIdEmpty")
	@SimpleProperty
	public void UserTrackerId(String userTrackerId) {
		this.userTrackerId = userTrackerId;
		
	}
	
	/**
	 * Return the value of userId of the application.
	 * 
	 * Return userId
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the value of userId of the application.", userVisible = true)
	public String UserTrackerId() {
		return this.userTrackerId;
	}
	
	public String getUserTrackerId() {
		return this.userTrackerId;
	}
	
	/**
	 * Specifies the tableId of Fusion Table (Google) to establish the connection.
	 * 
	 * @param tableId
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "1xZCj24xYWpj6jHWN2IK2xiErYPY7XbeHAqXVR4Bw")
	@SimpleProperty
	public void TableId(String newtableId) {
		tableId = newtableId;
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
	@SimpleFunction(description="Function to notify a specific action (version with one argument).")
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
	@SimpleFunction(description="Function to notify a specific action (version with two arguments).")
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
	@SimpleFunction(description="Function to notify a specific action (version with three arguments).")
	public void NotifyWithThreeArguments(String actionId, String valueArgument, String valueArgument2, String valueArgument3) {
		activityTrackerManager.prepareQueryManual(actionId, valueArgument, valueArgument2, valueArgument3);
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
	
	/**
	 * Specifies when the events are tracked.
	 * @param publishEvents
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
	      defaultValue = "True")
	@SimpleProperty
	public void PublishEvents(boolean publishEvents) {
	  this.publishEvents = publishEvents;
	}
	
	public boolean getPublishEventsStatus() {
		return this.publishEvents;
	}
	
	/**
	 * Specifies when the methods (functions) are tracked.
	 * @param publishMethods
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
	      defaultValue = "True")
	@SimpleProperty
	public void PublishMethods(boolean publishMethods) {
	  this.publishMethods = publishMethods;
	}
	
	public boolean getPublishMethodsStatus() {
		return this.publishMethods;
	}
	
	/**
	 * Specifies when the Getters are tracked.
	 * @param publishGetters
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
	      defaultValue = "True")
	@SimpleProperty
	public void PublishGetters(boolean publishGetters) {
	  this.publishGetters = publishGetters;
	}
	
	public boolean getPublishGettersStatus() {
		return this.publishGetters;
	}
	
	/**
	 * Specifies when the Setters are tracked.
	 * @param publishSetters
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
	      defaultValue = "True")
	@SimpleProperty
	public void PublishSetters(boolean publishSetters) {
	  this.publishSetters = publishSetters;
	}
	
	public boolean getPublishSettersStatus() {
		return this.publishSetters;
	}
}
