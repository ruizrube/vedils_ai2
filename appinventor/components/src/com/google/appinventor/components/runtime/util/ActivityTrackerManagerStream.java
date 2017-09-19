package com.google.appinventor.components.runtime.util;

import java.util.List;
import java.util.Timer;

import org.json.JSONObject;

import com.google.appinventor.components.runtime.ActivityTracker;
import com.google.appinventor.components.runtime.Clock;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.la4ai.util.DeviceInfoFunctions;
import com.google.appinventor.components.runtime.la4ai.util.GPSTracker;
import com.google.appinventor.components.runtime.la4ai.util.TinyDB;

public class ActivityTrackerManagerStream implements ActivityTrackerManager  {
	
	private ActivityTracker currentActivityTracker;
	private ComponentContainer componentContainer;
	
	private String URL_SERVER_ADD_QUEUE = "http://vedilsanalytics.uca.es:8080/VedilsAnalyticsWS/flinkClient/AddToKafkaQueue";
	
	private JSONObject dataJSON;
	
	//Record data when not internet access
	private TinyDB tinyDB;
	private int tagDB;
		
	private TimerSendData timerSendData;
	private String currentIP;
	private GPSTracker gpsTracker;
	
	public ActivityTrackerManagerStream(ActivityTracker currentActivityTracker, ComponentContainer componentContainer) {
		this.currentActivityTracker = currentActivityTracker;
		this.componentContainer = componentContainer;
		this.tinyDB = new TinyDB(componentContainer.$context());
		this.tagDB = 0;
		this.timerSendData = new TimerSendData(this);
		gpsTracker = new GPSTracker(componentContainer.$context());
	}
	
	@Override
	public void prepareQueryAutomatic(String actionType, String actionId, String componentType, String componentId,
			String[] paramsName, Object[] paramsValue, String returnParamValue) {
		try {
			addBasicNotificationData();
			dataJSON.put("ComponentType", componentType);
			dataJSON.put("ComponentID", componentId);
			dataJSON.put("ActionType", actionType);
			dataJSON.put("ActionID", actionId);
		
			//Add Data
			int count = 0;
			for(String paramName: paramsName) {
				dataJSON.put(paramName, paramsValue[count]);
				count += 1;
			}
			
			dataJSON.put(actionId.toLowerCase(), returnParamValue);
		} catch (Exception e) {
			System.out.println("ActivityTrackerManagerMongoDB error" + e.getMessage());
			e.printStackTrace();
		}
		
		//And try to send data to MongoDB
		recordData();
	}

	@Override
	public void prepareQueryManual(String actionId, List<Object> data) {
		try {
			addBasicNotificationData();
			dataJSON.put("ActionType", "SPECIFIC");
			dataJSON.put("ActionID", actionId);
			
			//Add Data
			for(Object value: data) {
				System.out.println("Type of element: " + value.getClass().getName());
				if(value instanceof YailList) { //main YailList
					YailList list = (YailList) value;
					System.out.println("element of list: " + list.getString(1));
					dataJSON.put(list.getString(0), list.getString(1));
				}
			}		
		} catch (Exception e) {
			System.out.println("ActivityTrackerManagerMongoDB error" + e.getMessage());
			e.printStackTrace();
		}
		
		//And try to send data to MongoDB
		recordData();
	}

	@Override
	public void prepareQueryManual(String actionId, String param1, String param2, String param3) {
		try {
			addBasicNotificationData();
			dataJSON.put("ActionType", "SPECIFIC");
			dataJSON.put("ActionID", actionId);
			
			//Add data
			dataJSON.put("param1", param1);
			dataJSON.put("param2", param2);
			dataJSON.put("param3", param3);
		
		} catch (Exception e) {
			System.out.println("ActivityTrackerManagerMongoDB error" + e.getMessage());
			e.printStackTrace();
		}
		
		//And try to send data to MongoDB
	    recordData();
	}

	@Override
	public void recordData() {
		if(DeviceInfoFunctions.checkInternetConnection(componentContainer.$context()) && currentActivityTracker.getSynchronizationMode() == Component.REALTIME) {	
			try {
				dataJSON.put("database", componentContainer.$context().getApplicationInfo().packageName);
				dataJSON.put("collection", currentActivityTracker.getTableId());
			} catch(Exception e) {
				System.out.println("ActivityTrackerManagerMongoDB error" + e.getMessage());
				e.printStackTrace();
			}
			
			new AsyncHttpRequestManager(URL_SERVER_ADD_QUEUE, dataJSON, null, false).execute();
			
			//And if db is not empty send the content too
			List<String> listTags = (List<String>) tinyDB.GetTags();
			if(!listTags.isEmpty()) {
				recordDataBatch();
			}
		} else {
			if(currentActivityTracker.getSynchronizationMode() == Component.BATCH && tagDB == 0) { 
				Timer timer = new Timer();
				timer.schedule(timerSendData, 0, currentActivityTracker.getBatchTime() * 1000);
			}
				
			tinyDB.StoreValue(Integer.toString(tagDB), dataJSON.toString());
			System.out.println("Store value in TinyDB: " +dataJSON.toString());
			tagDB++;
		}
	}

	@Override
	public void recordDataBatch() {
		List<String> listTags = (List<String>) tinyDB.GetTags();
		String listDataJson = "";
		
		for(String tagAux: listTags) {
			listDataJson = listDataJson + tinyDB.GetValue(tagAux, "").toString().replaceFirst("'0.0.0.0'", "'" + this.currentIP + "'") + ";";
		}
		
		if(DeviceInfoFunctions.checkInternetConnection(componentContainer.$context()) && !listDataJson.isEmpty()) {
			JSONObject dataJsonAux = new JSONObject();
			try {
				dataJsonAux.put("list", listDataJson);
				dataJsonAux.put("database", componentContainer.$context().getApplicationInfo().packageName);
				dataJsonAux.put("collection", currentActivityTracker.getTableId());
			} catch(Exception e) {
				System.out.println("ActivityTrackerManagerMongoDB error" + e.getMessage());
				e.printStackTrace();
			}
			new AsyncHttpRequestManager(URL_SERVER_ADD_QUEUE, dataJsonAux, null, false).execute();
			tinyDB.ClearAll();
		}
	}
	
	private void addBasicNotificationData() throws Exception {
		dataJSON = new JSONObject();
		dataJSON.put("UserID", currentActivityTracker.getUserTrackerId());
		dataJSON.put("IP", DeviceInfoFunctions.getCurrentIP(currentActivityTracker.getCommunicationMode(), this.componentContainer.$context()));
		dataJSON.put("MAC", DeviceInfoFunctions.getMAC(componentContainer.$context()));
		dataJSON.put("IMEI", DeviceInfoFunctions.getIMEI(componentContainer.$context()));
		dataJSON.put("APILevel", DeviceInfoFunctions.getAndroidAPIVersion());
		dataJSON.put("Latitude", gpsTracker.getLatitude());
		dataJSON.put("Longitude", gpsTracker.getLongitude());
		dataJSON.put("Date", Clock.FormatDate(Clock.Now(), "MM/dd/yyyy HH:mm:ss"));
		dataJSON.put("AppID", componentContainer.$context().getApplicationInfo().packageName);
		dataJSON.put("ScreenID", componentContainer.$form().getLocalClassName());
	}
}
