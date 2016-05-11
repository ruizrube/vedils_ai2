package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import java.io.IOException;
import android.os.AsyncTask;

import android.content.Context;
import com.google.android.gms.iid.InstanceID;

//BackPack = https://github.com/mit-cml/appinventor-sources/commit/5a38a3553f09951ab3b68f5ef08f239c381d0e46

@UsesLibraries(libraries = "google-play-services.jar," +
		"android-support-v4.jar")
@SimpleObject
//@DesignerComponent(nonVisible= true, version = 1, description = "Google Cloud Messaging Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSLEARNINGANALYTICS, iconName = "images/arColorTracker.png")
@UsesPermissions(permissionNames = 
"com.google.android.c2dm.permission.RECEIVE," +
"android.permission.INTERNET," +
"android.permission.GET_ACCOUNTS," +
"android.permission.WAKE_LOCK," +
"android.permission.VIBRATE")
public class GoogleCloudMessaging extends AndroidNonvisibleComponent implements Component {
	
	private String token;
	private String senderId;
	private DeviceInfo deviceInfo;
	private Context context;
	
	public GoogleCloudMessaging(ComponentContainer componentContainer) {
		super(componentContainer.$form());	
		
		this.token = "";
		this.context = componentContainer.$form().$context();
		this.senderId = "488209129656";
		//Get the imei of the device to get the registration token.
		this.deviceInfo = new DeviceInfo(componentContainer.$form());
	}
	
	/**
	 * Specifies the SenderId to establish the connection.
	 * 
	 * @param tableId
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "488209129656")
	@SimpleProperty
	public void SenderId(String senderId) {
		this.senderId = senderId;
	}
	
	
	
	/**
	 * Function to get the registration token.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SimpleFunction(description="Function to get the registration token.")
	public void Register() {
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... arg0) {
				InstanceID instanceID = InstanceID.getInstance(context);
				System.out.println("Sender ID = "+ senderId + " - GCM");
				try {
			        token = instanceID.getToken(senderId,
			        		com.google.android.gms.gcm.GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
				}catch(IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Object result) {
				System.out.println("Register Token = " + token + " - GCM");
			}
		}.execute();
	}

}
