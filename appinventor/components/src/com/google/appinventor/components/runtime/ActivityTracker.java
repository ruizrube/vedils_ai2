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
import com.google.appinventor.components.runtime.util.TimerSendData;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

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
"gson-2.1.jar")
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
	private final Activity activity;
	@SuppressWarnings("unused")
	private final ComponentContainer componentContainer;
	private String tableId;
	private String columns;
	private String values;
	private String email;
	private String apiKey;
	private FusionTablesConnection fusionTablesConnection;
	private String path;
	private LocationSensor locationSensor;
	private Context context;
	private int synchronizationMode;
	private int batchTime;
	private int communicationMode;
	private TinyDB tinyDB;
	private int tagDB;
	private TimerSendData timerSendData;
	private String currentIP;
	
	public ActivityTracker(ComponentContainer componentContainer) {
		super(componentContainer.$form());
		
		this.componentContainer = componentContainer; 
		this.activity = componentContainer.$context();
		this.locationSensor = new LocationSensor(componentContainer);
		this.context = componentContainer.$context();
		this.tinyDB = new TinyDB(componentContainer);
		this.tagDB = 0;
		this.currentIP = "0.0.0.0";
		
		//Default mode
		this.synchronizationMode = Component.REALTIME;
		this.batchTime = 0;
		this.communicationMode = Component.INDIFFERENT;
		
		//Define data for FusionTableControl connection.
        
		columns = "UserID, IP, MAC, Latitude, Longitude, Date, AppID, ScreenID, ComponentID, ComponentType, ActionID, ActionType, Param1, Param2, Param3";
		tableId = "1xZCj24xYWpj6jHWN2IK2xiErYPY7XbeHAqXVR4Bw";
		email = "1075849932338-n26pqlvqfea3dspaebf52vnacch77nhf@developer.gserviceaccount.com";
		apiKey = "AIzaSyDL9s7r6ZIr9DN47_kNIIzRcm2JhWxy7ZU";
		path = ASSET_DIRECTORY + '/' + "ruizrube-cd84632c4ea8.p12";
		
		//Connection with FusionTables
		
		this.fusionTablesConnection = new FusionTablesConnection(columns, apiKey, path, email, componentContainer, true);
		this.timerSendData = new TimerSendData(this);
		System.out.println("ActivityTracker created.");
		
	}
	
	//Record Data
	
	@SuppressWarnings({ "deprecation", "unchecked"})
	private void recordData(String actionId, String param1, String param2, String param3) {
		
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
		String mac = wm.getConnectionInfo().getMacAddress();

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
				sendDataBatch();
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
	}
	
	@SuppressWarnings("unchecked")
	public void sendDataBatch() {
		List<String> listTags = (List<String>) tinyDB.GetTags();
		List<String> listValues = new ArrayList<String>();
		
		for(String tagAux: listTags) {
			listValues.add(tinyDB.GetValue(tagAux, "").toString().replaceFirst("'0.0.0.0'", "'" + this.currentIP + "'"));
		}
		
		if(fusionTablesConnection.internetAccess(this.communicationMode)) {
			fusionTablesConnection.insertRows(listValues, this.tableId);
			tinyDB.ClearAll();
		}
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
	
	/**
	 * Specifies the tableId of Fusion Table (Google) to establish the connection.
	 * 
	 * @param tableId
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "1xZCj24xYWpj6jHWN2IK2xiErYPY7XbeHAqXVR4Bw")
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
		return this.tableId;
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
	
	
	/**
	 *Function to notify a specific action (version without parameters).
	 * 
	 * @param actionId
	 */
	@SimpleFunction(description="Function to notify a specific action (version without arguments).")
	public void NotifyWithoutArguments(String actionId) {
		recordData(actionId, "", "", "");
	}
	
	
	/**
	 * Function to notify a specific action (version with one argument).
	 * 
	 * @param actionId
	 * @param valueArgument
	 */
	@SimpleFunction(description="Function to notify a specific action (version with one argument).")
	public void NotifyWithOneArgument(String actionId, String valueArgument) {
		recordData(actionId, valueArgument, "", "");
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
		recordData(actionId, valueArgument, valueArgument2, "");
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
		recordData(actionId, valueArgument, valueArgument2, valueArgument3);
	}
	
	/**
	 * Function to send data on user demand.
	 */
	@SimpleFunction(description="Function to send data on user demand.")
	public void PublishActivities() {
		sendDataBatch();
	}
}
