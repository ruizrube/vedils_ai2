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

public class ActivityTrackerManagerFusionTables implements ActivityTrackerManager {

	private ActivityTracker currentActivityTracker;
	
	private ComponentContainer componentContainer;
	
	//FusionTablesConnection data
	//A new field is added last, to make it compatible with tables with older versions of ActivityTracker (FusionTables API inserts the new columns at the end of the list).
	private String columns = "UserID,IP,MAC,IMEI,Latitude,Longitude,Date,AppID,ScreenID,ComponentID,ComponentType,ActionID,ActionType,"
			+ "NumberInputParam1,NumberInputParam2,NumberInputParam3,NumberOutputParam,TextInputParam1,TextInputParam2,TextInputParam3,TextOutputParam,APILevel";
	private String values;
	private String email = "activitytracker-vedils@activitytracker-vedils.iam.gserviceaccount.com";
	private String apiKey = "e804e05b5eb3d9ac77eff415bd43fb674302968f";
	private String path = Component.ASSET_DIRECTORY + '/' + "ActivityTrackerVEDILS-e804e05b5eb3.p12";
	private FusionTablesConnection fusionTablesConnection;
	
	//Record data when not internet access
	private TinyDB tinyDB;
	private int tagDB;
	
	private TimerSendData timerSendData;
	private String currentIP;
	private GPSTracker gpsTracker;
	
	
	public ActivityTrackerManagerFusionTables(ActivityTracker currentActivityTracker, ComponentContainer componentContainer) {
		this.currentActivityTracker = currentActivityTracker;
		this.componentContainer = componentContainer;
		this.fusionTablesConnection = new FusionTablesConnection(columns, apiKey, path, email, componentContainer, true);
		this.tinyDB = new TinyDB(componentContainer.$context());
		this.tagDB = 0;
		this.timerSendData = new TimerSendData(this);
		gpsTracker = new GPSTracker(componentContainer.$context());
	}
	
	@Override
	public void prepareQueryAutomatic(String actionType, String actionId, String componentType, String componentId,
			String[] paramsName, Object[] paramsValue, String returnParamValue) {
		String param1 = "";
		String param2 = "";
		String param3 = "";
		int count = 0; //Only get the first 3 values.
		
		for(Object paramValue: paramsValue) {
			if(count == 0) {
				param1 = paramValue.toString();
			} else if(count == 1) {
				param2 = paramValue.toString();
			} else if(count == 2) {
				param3 = paramValue.toString();
			}
			count += 1;
		}
		
		float valorParam1=-1;
		float valorParam2=-1;
		float valorParam3=-1;
		float valorOutputParam=-1;
		try {
			valorParam1=Float.valueOf(param1);
		} catch (NumberFormatException e) {
			;
		}
		
		try {
			valorParam2=Float.valueOf(param2);
		} catch (NumberFormatException e) {
			;
		}
		try {
			valorParam3=Float.valueOf(param3);
		} catch (NumberFormatException e) {
			;
		}
		
		try {
			valorOutputParam=Float.valueOf(returnParamValue);
		} catch (NumberFormatException e) {
			;
		}
		
		
	
		String ip = DeviceInfoFunctions.getCurrentIP(currentActivityTracker.getCommunicationMode(), this.componentContainer.$context());
		String mac = DeviceInfoFunctions.getMAC(componentContainer.$context());
	    String appName = componentContainer.$context().getApplicationInfo().packageName;
	    String screenName = componentContainer.$form().getLocalClassName(); 
	    currentIP = ip;
	    
	    //Do the query
		//values = "'" + currentActivityTracker.getUserTrackerId() + "','" +
	    values = "'" + currentActivityTracker.getUser().getName() + " " 
		+ currentActivityTracker.getUser().getSurname()  + "','" +
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
		returnParamValue + "','" +
		DeviceInfoFunctions.getAndroidAPIVersion() + "'";
		
		//And try to send data to FusionTables
		recordData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void prepareQueryManual(String actionId, Object data) {
		if(data instanceof List) {
			String param1 = "";
			String param2 = "";
			String param3 = "";
			int count = 1; //Only get the first 3 values.
			
			List<Object> dataList = (List<Object>) data;
			
			for(Object value: dataList) {
				System.out.println("Type of element: " + value.getClass().getName());
				if(value instanceof YailList) { //main YailList
					YailList list = (YailList) value;
					if(count == 1) {
						System.out.println("element of list: " + list.getString(1));
						param1 = list.getString(1);
					} else if(count == 2) {
						System.out.println("element of list: " + list.getString(1));
						param2 = list.getString(1);
					} else if(count == 3) {
						System.out.println("element of list: " + list.getString(1));
						param3 = list.getString(1);
					}
					count += 1;
				}
			}
			
			prepareQueryManual(actionId, param1, param2, param3);
		}
	}
	
	@Override
	public void prepareQueryManual(String actionId, String param1, String param2, String param3) {
		
		float valorParam1=-1;
		float valorParam2=-1;
		float valorParam3=-1;
		
		try {
			valorParam1=Float.valueOf(param1);
		} catch (NumberFormatException e) {
			;
		}
	
		try {
			valorParam2=Float.valueOf(param2);
		} catch (NumberFormatException e) {
			;
		}
		try {
			valorParam3=Float.valueOf(param3);
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
		//values = "'" + currentActivityTracker.getUserTrackerId() + "','" + 
	    values = "'" + currentActivityTracker.getUser().getName() + " " 
	    		+ currentActivityTracker.getUser().getSurname()  + "','" +
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
		"" + "','" +
		DeviceInfoFunctions.getAndroidAPIVersion() + "'";
		
		//And try to send data to FusionTables
		recordData();
	}
	
	@SuppressWarnings({"unchecked"})
	@Override
	public void recordData() {
		
		if(DeviceInfoFunctions.checkInternetConnection(componentContainer.$context()) && currentActivityTracker.getSynchronizationMode() == Component.REALTIME) {
			//If internet access then send the values to FusionTables
				
			//fusionTablesConnection.insertRow(values, currentActivityTracker.getTableId());
			fusionTablesConnection.processColumnsToInsert(values, currentActivityTracker.getTableId());
				
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
	@Override
	public void recordDataBatch() {
		List<String> listTags = (List<String>) tinyDB.GetTags();
		List<String> listValues = new ArrayList<String>();
		
		for(String tagAux: listTags) {
			listValues.add(tinyDB.GetValue(tagAux, "").toString().replaceFirst("'0.0.0.0'", "'" + this.currentIP + "'"));
		}
		
		if(DeviceInfoFunctions.checkInternetConnection(componentContainer.$context())) {
			//fusionTablesConnection.insertRows(listValues, currentActivityTracker.getTableId());
			fusionTablesConnection.processColumnsToInsert(listValues, currentActivityTracker.getTableId());
			tinyDB.ClearAll();
		}
	}

	@Override
	public Object prepareQueryManualWithReturn(String actionId, Object data) {
		if(data instanceof List) {
			
			String query = "";
			
			String param1 = "";
			String param2 = "";
			String param3 = "";
			int count = 1; //Only get the first 3 values.
			
			List<Object> dataList = (List<Object>) data;
			
			for(Object value: dataList) {
				System.out.println("Type of element: " + value.getClass().getName());
				if(value instanceof YailList) { //main YailList
					YailList list = (YailList) value;
					if(count == 1) {
						System.out.println("element of list: " + list.getString(1));
						param1 = list.getString(1);
					} else if(count == 2) {
						System.out.println("element of list: " + list.getString(1));
						param2 = list.getString(1);
					} else if(count == 3) {
						System.out.println("element of list: " + list.getString(1));
						param3 = list.getString(1);
					}
					count += 1;
				}
			}
			
			float valorParam1=-1;
			float valorParam2=-1;
			float valorParam3=-1;
			
			try {
				valorParam1=Float.valueOf(param1);
			} catch (NumberFormatException e) {
				;
			}
		
			try {
				valorParam2=Float.valueOf(param2);
			} catch (NumberFormatException e) {
				;
			}
			try {
				valorParam3=Float.valueOf(param3);
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
			//values = "'" + currentActivityTracker.getUserTrackerId() + "','" + 
		    values = "'" + currentActivityTracker.getUser().getName() + " " 
		    		+ currentActivityTracker.getUser().getSurname()  + "','" +
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
			"" + "','" +
			DeviceInfoFunctions.getAndroidAPIVersion() + "'";
			
			query = "INSERT INTO " + currentActivityTracker.TableId() + " (" + columns + ")" + " VALUES " + "(" + values + ");";
			return query;
			
		}
		
		return "";
	}
}
