package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.runtime.util.FusionTablesConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.google.appinventor.components.runtime.ActivityTracker;
import com.google.appinventor.components.runtime.Clock;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.la4ai.util.TinyDB;
import com.google.appinventor.components.runtime.la4ai.util.DeviceInfoFunctions;
import com.google.appinventor.components.runtime.la4ai.util.GPSTracker;

public class ActivityTrackerManager {
	
	private ActivityTracker currentActivityTracker;
	
	private ComponentContainer componentContainer;
	
	//FusionTablesConnection data
	private String columns = "UserID, IP, MAC, IMEI, Latitude, Longitude, Date, AppID, ScreenID, ComponentID, ComponentType, ActionID, ActionType, NumberInputParam1, NumberInputParam2, NumberInputParam3, NumberOutputParam, TextInputParam1, TextInputParam2, TextInputParam3, TextOutputParam";
	private String values;
	private String email = "1075849932338-n26pqlvqfea3dspaebf52vnacch77nhf@developer.gserviceaccount.com";
	private String apiKey = "AIzaSyDL9s7r6ZIr9DN47_kNIIzRcm2JhWxy7ZU";
	private String path = Component.ASSET_DIRECTORY + '/' + "ruizrube-cd84632c4ea8.p12";
	private FusionTablesConnection fusionTablesConnection;
	
	//Record data when not internet access
	private TinyDB tinyDB;
	private int tagDB;
	
	private TimerSendData timerSendData;
	private String currentIP;
	private GPSTracker gpsTracker;
	
	
	public ActivityTrackerManager(ActivityTracker currentActivityTracker, ComponentContainer componentContainer) {
		this.currentActivityTracker = currentActivityTracker;
		this.componentContainer = componentContainer;
		this.fusionTablesConnection = new FusionTablesConnection(columns, apiKey, path, email, componentContainer, true);
		this.tinyDB = new TinyDB(componentContainer.$context());
		this.tagDB = 0;
		this.timerSendData = new TimerSendData(this);
		gpsTracker = new GPSTracker(componentContainer.$context());
	}
	
	public void prepareQueryAutomatic(String actionType, String actionId, String componentType, String componentId, String param1, String param2, String param3, String returnValue) {
		int valorParam1=-1;
		int valorParam2=-1;
		int valorParam3=-1;
		int valorOutputParam=-1;
		
		try {
			valorParam1=Integer.valueOf(param1);
		} catch (NumberFormatException e) {
			;
		}
	
		try {
			valorParam2=Integer.valueOf(param2);
		} catch (NumberFormatException e) {
			;
		}
		try {
			valorParam3=Integer.valueOf(param3);
		} catch (NumberFormatException e) {
			;
		}
		
		try {
			valorOutputParam=Integer.valueOf(returnValue);
		} catch (NumberFormatException e) {
			;
		}
		
		
	
		String ip = DeviceInfoFunctions.getCurrentIP(currentActivityTracker.getCommunicationMode(), this.componentContainer.$context());
		String mac = DeviceInfoFunctions.getMAC(componentContainer.$context());
	    String appName = componentContainer.$context().getApplicationInfo().packageName;
	    String screenName = componentContainer.$form().getLocalClassName(); 
	    currentIP = ip;
	    
	    //Do the query
		values = "'" + currentActivityTracker.getUserTrackerId() + "','" + 
		ip + "','" +
	    mac + "','" +
	    DeviceInfoFunctions.getIMEI(componentContainer.$context()) + "'," +
		gpsTracker.getLatitude() + "," +
		gpsTracker.getLongitude() + ",'" +
		Clock.FormatDate(Clock.Now(), "MM/dd/yyyy HH:mm:ss") + "','" +
		appName + "','" +
		screenName + "','" +
		componentId + "','" +
		componentType + "','" +
		actionId + "','" +
		actionType + "'," +
		valorParam1 + "," +
		valorParam2 + "," +
		valorParam3 + "," +
		valorOutputParam + ",'" +
		param1 + "','" +
		param2 + "','" +
		param3 + "','" +
		returnValue + "'";
		
		//And try to send data to FusionTables
		recordData();
	}
	
	
	public void prepareQueryManual(String actionId, String param1, String param2, String param3) {
		
		int valorParam1=-1;
		int valorParam2=-1;
		int valorParam3=-1;
		
		try {
			valorParam1=Integer.valueOf(param1);
		} catch (NumberFormatException e) {
			;
		}
	
		try {
			valorParam2=Integer.valueOf(param2);
		} catch (NumberFormatException e) {
			;
		}
		try {
			valorParam3=Integer.valueOf(param3);
		} catch (NumberFormatException e) {
			;
		}
	
		
		String ip = DeviceInfoFunctions.getCurrentIP(currentActivityTracker.getCommunicationMode(), this.componentContainer.$context());
		String mac = DeviceInfoFunctions.getMAC(componentContainer.$context());
	    String appName = componentContainer.$context().getApplicationInfo().packageName;
	    String actionType = "SPECIFIC";
	    String screenName = componentContainer.$form().getLocalClassName(); 
	    currentIP = ip;
	    
	    //Do the query
		values = "'" + currentActivityTracker.getUserTrackerId() + "','" + 
		ip + "','" +
	    mac + "','" +
	    DeviceInfoFunctions.getIMEI(componentContainer.$context()) + "'," +
		gpsTracker.getLatitude() + "," +
		gpsTracker.getLongitude() + ",'" +
		Clock.FormatDate(Clock.Now(), "MM/dd/yyyy HH:mm:ss") + "','" +
		appName + "','" +
		screenName + "','" +
		"" + "','" +
		"" + "','" +
		actionId + "','" +
		actionType + "'," +
		valorParam1 + "," +
		valorParam2 + "," +
		valorParam3 + "," +
		"-1" + ",'"  +
		param1 + "','" +
		param2 + "','" +
		param3 + "','" +
		"" + "'";
		
		//And try to send data to FusionTables
		recordData();
	}
	
	@SuppressWarnings({"unchecked"})
	private void recordData() {
		
		if(fusionTablesConnection.internetAccess(currentActivityTracker.getCommunicationMode()) && currentActivityTracker.getSynchronizationMode() == Component.REALTIME) {
			//If internet access then send the values to FusionTables
				
			fusionTablesConnection.insertRow(values, currentActivityTracker.getTableId());
				
			//And if db is not empty send the content too
			List<String> listTags = (List<String>) tinyDB.GetTags();
			if(!listTags.isEmpty()) {
				recordDataBatch();
			}
			
		} else {
				
			if(currentActivityTracker.getSynchronizationMode() == Component.BATCH && tagDB == 0) { //if is the first call the function to wait batch sec.
				Timer timer = new Timer();
				timer.schedule(timerSendData, 0, currentActivityTracker.getBatchTime() * 1000);
			}
				
			tinyDB.StoreValue(Integer.toString(tagDB), values);
			System.out.println("Store value in TinyDB: " +values);
			tagDB++;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void recordDataBatch() {
		List<String> listTags = (List<String>) tinyDB.GetTags();
		List<String> listValues = new ArrayList<String>();
		
		for(String tagAux: listTags) {
			listValues.add(tinyDB.GetValue(tagAux, "").toString().replaceFirst("'0.0.0.0'", "'" + this.currentIP + "'"));
		}
		
		if(fusionTablesConnection.internetAccess(currentActivityTracker.getCommunicationMode())) {
			fusionTablesConnection.insertRows(listValues, currentActivityTracker.getTableId());
			tinyDB.ClearAll();
		}
	}
	
}
