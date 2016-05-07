package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import android.content.Context;
import android.telephony.TelephonyManager;

@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "IMEITracker Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSLEARNINGANALYTICS, iconName = "images/arColorTracker.png")
@UsesPermissions(permissionNames = "android.permission.READ_PHONE_STATE")
public class DeviceInfo extends AndroidNonvisibleComponent implements Component {
	
	String imei;
	
	public DeviceInfo(ComponentContainer componentContainer) {
		super(componentContainer.$form());
		
		//Get the IMEI code of the current device:
		TelephonyManager manager = (TelephonyManager) componentContainer.$form().$context().getSystemService(Context.TELEPHONY_SERVICE);
		this.imei = manager.getDeviceId();
	}
	
	/**
	 * Specifies the IMEI code of the current device
	 * 
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "")
	@SimpleProperty
	public String Imei() {
		return this.imei;
	}
	
	public String getImei() {
		return this.imei;
	}
	
}
