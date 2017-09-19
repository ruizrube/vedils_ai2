package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.la4ai.util.DeviceInfoFunctions;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * DeviceInfo Component
 * @author SPI-FM at UCA
 *
 */
@SimpleObject
@DesignerComponent(version = YaVersion.DEVICEINFO_COMPONENT_VERSION, 
description = "A component to obtain information of the used device. The information provided is the <code>IMEI</code>, <code>MAC</code>, and <code>IP</code>.", 
category = ComponentCategory.VEDILSCOMMUNICATION, iconName = "images/device_info_icon.png", nonVisible = true)
@UsesPermissions(permissionNames = "android.permission.READ_PHONE_STATE,"
	+ "android.permission.INTERNET," 
	+ "android.permission.ACCESS_NETWORK_STATE")
@UsesLibraries(libraries =
"la4ai.jar")
public class DeviceInfo extends AndroidNonvisibleComponent implements Component {
	
	private String IMEI;
	private String MAC;
	private int width=0;
	private int height=0;
	private Context context;
	
	public DeviceInfo(ComponentContainer componentContainer) {
		super(componentContainer.$form());
		
		//Get the IMEI code of the current device:
		this.context = componentContainer.$form().$context();
		this.IMEI = DeviceInfoFunctions.getIMEI(this.context);
		this.MAC = DeviceInfoFunctions.getMAC(this.context);
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		this.width = size.x;
		this.height= size.y;
	}
	
	/**
	 * Specifies the IMEI code of the current device
	 * 
	 */
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR,
			description = "Returns the value of width of the current device.", userVisible = true)
	public int ScreenWidth() {
		
		return this.width;
	}
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR,
			description = "Returns the value of height of the current device.", userVisible = true)
	public int ScreenHeight() {
		
		return this.height;
	}
	@SimpleProperty(category = PropertyCategory.BEHAVIOR,
			description = "Returns the value of IMEI code of the current device.", userVisible = true)
	public String IMEI() {
		return this.IMEI;
	}
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR,
			description = "Returns the value of MAC address of the current device.", userVisible = true)
	public String MAC() {
		return this.MAC;
	}
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR,
			description = "Returns the value of current IP address of the device.", userVisible = true)
	public String IP() {
		return DeviceInfoFunctions.getCurrentIP(Component.INDIFFERENT, this.context);
	}
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR,
			description = "Returns the internet access state", userVisible = true)
	public boolean InternetConnection() {
		return DeviceInfoFunctions.checkInternetConnection(context);
	}
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR,
			description = "Returns the version of current Android API", userVisible = true)
	public int AndroidAPILevel() {
		return DeviceInfoFunctions.getAndroidAPIVersion();
	}
}
