package com.google.appinventor.components.runtime;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.services.GoogleKeyInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.Fusiontables.Query.Sql;
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
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.OAuth2Helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;

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
	private final ComponentContainer componentContainer;
	private String tableId;
	private String columns;
	private String values;
	private String email;
	private String apiKey;
	private FusiontablesControlWithoutDialog fusiontablesControl;
	private String path;
	private LocationSensor locationSensor;
	private Context context;
	
	public ActivityTracker(ComponentContainer componentContainer) {
		super(componentContainer.$form());
		
		this.componentContainer = componentContainer; 
		this.activity = componentContainer.$context();
		fusiontablesControl = new FusiontablesControlWithoutDialog(componentContainer);
		this.locationSensor = new LocationSensor(componentContainer);
		context = componentContainer.$context();
		
		//Define data for FusionTableControl connection.
		
		columns = "UserID, IP, MAC, Latitude, Longitude, Date, AppID, ScreenID, ComponentID, ComponentType, ActionID, ActionType, Params";
		tableId = "1xZCj24xYWpj6jHWN2IK2xiErYPY7XbeHAqXVR4Bw";
		email = "1075849932338-n26pqlvqfea3dspaebf52vnacch77nhf@developer.gserviceaccount.com";
		apiKey = "AIzaSyDL9s7r6ZIr9DN47_kNIIzRcm2JhWxy7ZU";
		path = ASSET_DIRECTORY + '/' + "ruizrube-cd84632c4ea8.p12";
		
		//Configuration of FusionTableControl.
		
		fusiontablesControl.ServiceAccountEmail(email);
		fusiontablesControl.ApiKey(apiKey);
		fusiontablesControl.UseServiceAuthentication(true);
		fusiontablesControl.KeyFile(path);
		
		this.userTrackerId = new String();
		System.out.println("ActivityTracker created.");
		
	}
	
	//Record Data
	
	private String recordParamsValues(List<String> values) {
		String paramsValues = new String();
		
		for(String value: values) {
			System.out.println("Value: "+value);
			paramsValues = paramsValues + value + ";";
		}
		return paramsValues;
	}
	
	@SuppressWarnings("deprecation")
	private void recordData(String actionId, String paramsValues) {
		
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
		String mac = wm.getConnectionInfo().getMacAddress();

	    String appName = context.getApplicationInfo().packageName;
	    String screenName = activity.getTitle().toString(); 
	    String actionType = "SPECIFIC";
	    //componentContainer.$form().getLocalClassName() (Devuelve el ScreenX también)
	    
	    
		System.out.println("Date: "+ Clock.FormatDate(Clock.Now(), "MM/dd/yyyy HH:mm:ss"));
		System.out.println("Latitude: " +locationSensor.Latitude());
		System.out.println("Longitude: " +locationSensor.Longitude());
		System.out.println("IP: " +ip);
		System.out.println("MAC: " +mac);
		
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
		paramsValues + "'";
		String query = "INSERT INTO " + tableId + " (" + columns + ")" + " VALUES " + "(" + values + ")";
		new QueryProcessorV1WithoutDialog(fusiontablesControl, activity).execute(query);
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
	 *Function to notify a specific action (version without parameters).
	 * 
	 * @param actionId
	 */
	@SimpleFunction(description="Function to notify a specific action (version without arguments).")
	public void NotifyWithoutArguments(String actionId) {
		System.out.println("Notify1");
		List<String> values = new ArrayList<String>();
		recordData(actionId, recordParamsValues(values));
	}
	
	
	/**
	 * Function to notify a specific action (version with one argument).
	 * 
	 * @param actionId
	 * @param valueArgument
	 */
	@SimpleFunction(description="Function to notify a specific action (version with one argument).")
	public void NotifyWithOneArgument(String actionId, String valueArgument) {
		System.out.println("Notify2");
		List<String> values = new ArrayList<String>();
		values.add(valueArgument);
		recordData(actionId, recordParamsValues(values));
		
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
		System.out.println("Notify3");
		List<String> values = new ArrayList<String>();
		values.add(valueArgument);
		values.add(valueArgument2);
		recordData(actionId, recordParamsValues(values));
		
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
		System.out.println("Notify4");
		List<String> values = new ArrayList<String>();
		values.add(valueArgument);
		values.add(valueArgument2);
		values.add(valueArgument3);
		recordData(actionId, recordParamsValues(values));
		
	}
	
	public class FusiontablesControlWithoutDialog extends FusiontablesControl {

		public FusiontablesControlWithoutDialog(ComponentContainer componentContainer) {
			super(componentContainer);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		void signalJsonResponseError(String query, String parsedException) {}
		
	}
	
	public class QueryProcessorV1WithoutDialog extends FusiontablesControl.QueryProcessorV1 {

		QueryProcessorV1WithoutDialog(FusiontablesControl fusiontablesControl, Activity activity) {
			fusiontablesControl.super(activity);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		protected void onPreExecute() {}
		
		@Override
		protected void onPostExecute(String result) {
		      fusiontablesControl.GotResult(result);
		}
	}
}
