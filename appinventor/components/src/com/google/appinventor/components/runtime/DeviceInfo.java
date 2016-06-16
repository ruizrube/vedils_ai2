package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.la4ai.util.DeviceInfoFunctions;

import android.content.Context;

@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "DeviceInfo Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSLEARNINGANALYTICS, iconName = "images/arColorTracker.png")
@UsesPermissions(permissionNames = "android.permission.READ_PHONE_STATE,"
	+ "android.permission.INTERNET," 
	+ "android.permission.ACCESS_NETWORK_STATE")
@UsesLibraries(libraries =
"la4ai.jar")
public class DeviceInfo extends AndroidNonvisibleComponent implements Component {
	
	private String IMEI;
	private String MAC;
	private Context context;
	
	public DeviceInfo(ComponentContainer componentContainer) {
		super(componentContainer.$form());
		
		//Get the IMEI code of the current device:
		this.context = componentContainer.$form().$context();
		this.IMEI = DeviceInfoFunctions.getIMEI(this.context);
		this.MAC = DeviceInfoFunctions.getMAC(this.context);
	}
	
	/**
	 * Specifies the IMEI code of the current device
	 * 
	 */
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
}
