package com.google.appinventor.components.runtime;

import java.util.Timer;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.util.TimerThingSpeakData;
import com.google.appinventor.components.runtime.la4ai.util.GPSTracker;
import java.net.URL;
import java.net.HttpURLConnection;
import java.lang.Exception;
import java.io.BufferedReader;
import java.lang.StringBuilder;
import java.io.InputStreamReader;

@SimpleObject
//@DesignerComponent(nonVisible = true, version = 1, description = "ThingSpeakLocationSensor Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSLEARNINGANALYTICS, iconName = "images/arColorTracker.png")
@UsesPermissions(permissionNames = 
"android.permission.INTERNET, " +
"android.permission.ACCESS_NETWORK_STATE," +
"android.permission.ACCESS_FINE_LOCATION," +
"android.permission.ACCESS_COARSE_LOCATION," +
"android.permission.ACCESS_MOCK_LOCATION," +
"android.permission.ACCESS_LOCATION_EXTRA_COMMANDS")
public class ThingSpeakLocationSensor extends AndroidNonvisibleComponent implements Component {
	
	private String API_KEY;
	private String CHANNEL_ID;
	private GPSTracker gpsTracker;
	private boolean start;
	
	private static final String THINGSPEAK_FIELD1 = "field1";
    private static final String THINGSPEAK_FIELD2 = "field2";
    private static final String THINGSPEAK_UPDATE_URL = "https://api.thingspeak.com/update?";
    
    TimerThingSpeakData timerThingSpeakData;

	public ThingSpeakLocationSensor(ComponentContainer componentContainer) {
		super(componentContainer.$form());
		this.timerThingSpeakData = new TimerThingSpeakData(this);
		//To init the timerThingSpeakData.
		this.start = false;
		this.gpsTracker = new GPSTracker(componentContainer.$context());
	}
	
	/**
	 * Specifies the API_KEY of the ThingSpeak public channel.
	 * 
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "")
	@SimpleProperty
	public void ChannelApiKey(String API_KEY) {
		this.API_KEY = API_KEY;
	}
	
	/**
	 * Specifies the API_KEY of the ThingSpeak public channel.
	 * 
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR,
			description = "Returns the value of API_KEY of the ThingSpeak public channel.", userVisible = true)
	public String ChannelApiKey() {
		return this.API_KEY;
	}
	
	/**
	 * Specifies the CHANNEL_ID of the ThingSpeak public channel.
	 * 
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "")
	@SimpleProperty
	public void ChannelId(String CHANNEL_ID) {
		this.CHANNEL_ID = CHANNEL_ID;
	}
	
	/**
	 * Specifies the CHANNEL_ID of the ThingSpeak public channel.
	 * 
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR,
			description = "Returns the value of CHANNEL_ID of the ThingSpeak public channel.", userVisible = true)
	public String ChannelId() {
		return this.CHANNEL_ID;
	}
	
	/**
	 * Function to send location data to the ThingSpeak channel.
	 * 
	 */
	@SimpleFunction(description="Function to send location data to ThingSpeak.")
	public void SendLocationDataToThingSpeak() {
		if(!this.start) {
			this.start = true;
			Timer timer = new Timer();
			timer.schedule(this.timerThingSpeakData, 0, 62 * 1000);
		} else {
			try {
			URL url = new URL(THINGSPEAK_UPDATE_URL + "key" + "=" +
                    this.API_KEY + "&" + THINGSPEAK_FIELD1 + "=" +  + this.gpsTracker.getLatitude() +
                    "&" + THINGSPEAK_FIELD2 + "=" + this.gpsTracker.getLongitude());
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                System.out.println("ThingSpeak request = " +stringBuilder.toString());
            }
            finally{
                urlConnection.disconnect();
            }
			} catch(Exception e) {}
		}
	}

}
