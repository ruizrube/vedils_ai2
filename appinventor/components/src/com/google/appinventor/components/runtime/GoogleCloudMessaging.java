package com.google.appinventor.components.runtime;
import java.util.List;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.GoogleCloudMessagingConnectionServer;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Google Cloud Messaging Component
 * @author SPI-FM at UCA
 *
 */
@UsesLibraries(libraries = "google-play-services.jar," +
		"android-support-v4.jar," +
		"la4ai.jar," +
		"gson-2.1.jar")
@SimpleObject
@DesignerComponent(version = YaVersion.GOOGLECLOUDMESSAGING_COMPONENT_VERSION, 
description = "A component to send messages between all devices that are using the same application. " +
		"First, we need to call the <code>Register</code> function to subscribe in the server. " +
		"Then we can call the <code>sendMessage</code> and <code>send DataList</code> functions to send a text message or a data list.", 
category = ComponentCategory.VEDILSCOMMUNICATION, iconName = "images/gcm_icon.png", nonVisible = true)
@UsesPermissions(permissionNames = 
"com.google.android.c2dm.permission.RECEIVE," +
"android.permission.INTERNET," +
"android.permission.INTERNET," +
"android.permission.READ_PHONE_STATE," +
"android.permission.GET_ACCOUNTS," +
"android.permission.WAKE_LOCK," +
"android.permission.VIBRATE," +
"com.google.android.c2dm.permission.RECEIVE," +
"com.miappgcm.appfactory.permission.C2D_MESSAGE")
public class GoogleCloudMessaging extends AndroidNonvisibleComponent implements Component {
	
	private static GoogleCloudMessagingConnectionServer connectionServer;
	private static Component component;
	private static Activity activity;
	private final ComponentContainer componentContainer;
	
	public GoogleCloudMessaging(ComponentContainer componentContainer) {
		super(componentContainer.$form());
		
		this.componentContainer = componentContainer;
		connectionServer = new GoogleCloudMessagingConnectionServer(componentContainer.$context());
		GoogleCloudMessaging.component = (GoogleCloudMessaging) this;
		activity = componentContainer.$context();
	}
	
//	/******************************************************************************** EDSON ***/
	/**
	 * Function to send a text message to device, specifying the IMEI of the receiver, or typing 'ALL' for all devices registered in the same app.
	 *  
	 * @param receiver IMEI the receptor of the message, can be ALL.
	 * @param action The action does not specify anything, lets use appinventor programmer. 
	 * @param message Text to send.
	 */
	@SimpleFunction(description="Function to send a text message to device registered in the same app, specifying the IMEI of the receiver.")
	public void SendMessage(String receiver, String action, String message) {
		connectionServer.sendMessage(message, action, receiver);
	}	
	
	/**
	 * Function to send a list of objects as a message to a device, specifying the IMEI of the receiver, or typing 'ALL' for all devices registered in the same app.
	 * 
	 * @param imei_receiver IMEI the receptor of the message, can be ALL.
	 * @param objects List of object.
	 */
	@SimpleFunction(description="Function to send a list of object as a message to device registered in the same app, specifying the IMEI of the receiver.")
	public void SendDataList(String receiver, List<Object> objects) {
		connectionServer.sendObjectsMessage(objects, receiver);
	}	

	@SimpleFunction(description="Function to send a text message for all devices registered in the same app.")
	public void SendGlobalMessage(String action, String message) {
		connectionServer.sendMessage(message, action, "ALL");
	}	
	
	/**
	 * Function to send a list of objects as a message to a device, specifying the IMEI of the receiver, or typing 'ALL' for all devices registered in the same app.
	 * 
	 * @param imei_receiver IMEI the receptor of the message, can be ALL.
	 * @param objects List of object.
	 */
	@SimpleFunction(description="Function to send a list of object as a message for all devices registered in the same app.")
	public void SendGlobalDataList(List<Object> objects) {
		connectionServer.sendObjectsMessage(objects, "ALL");
	}
	
	// COMENTO ESTE TROZO PARA MODIFICARLO	
//	/**
//	 * Function to send a text message to all devices with the same app.
//	 * @param message
//	 */
//	@SimpleFunction(description="Function to send a text message to all devices registered in the same app.")
//	public void SendMessage(String message, String action) {
//		connectionServer.sendMessage(message, action);
//	}
//	
//	/**
//	 * Function to send a list of objects as a message to all devices with the same app.
//	 * @param objects
//	 */
//	@SimpleFunction(description="Function to send a list of object as a message to all devices registered in the same app.")
//	public void SendDataList(List<Object> objects) {
//		connectionServer.sendObjectsMessage(objects);
//	}
//	/******************************************************************************** EDSON ***/		
	
	
	/**
	 * Function to get the registration token.
	 */
	@SimpleFunction(description="Function to get the registration token.")
	public void Register() {
		//System.out.println("HolaEdfunciona/n");
		connectionServer.register();
	}
	
	/**
	 * Function to configure the background service.
	 */
	@SimpleFunction(description="Function to start the GCM background service.")
	public void StartBackgroundService() {
		Intent intent = new Intent(componentContainer.$form().getApplication(), GoogleCloudMessagingBackgroundService.class);
		//Gson gson = new Gson();
		//intent.putExtra("GCM", gson.toJson(this));
		componentContainer.$form().startService(intent);
		System.out.println("Background service GCM: starting service..");
	}
	
	/**
	 * Function to configure the background service.
	 */
	@SimpleFunction(description="Function to stop the GCM background service.")
	public void StopBackgroundService() {
		Intent intent = new Intent(componentContainer.$form().getApplication(), GoogleCloudMessagingBackgroundService.class);
		//Gson gson = new Gson();
		//intent.putExtra("GCM", gson.toJson(this));
		componentContainer.$form().stopService(intent);
		System.out.println("Background service GCM: stopping service..");
	}

	
	/**
	 * Function to unregister client on server.
	 */
	@SimpleFunction(description="Function to delete this client of server.")
	public void UnRegister() {
		connectionServer.unRegister();
	}
	
	/**
	 * Function to unregister all clients on server.
	 */
	@SimpleFunction(description="Function to delete all clients of server.")
	public void UnRegisterAll() {
		connectionServer.unRegisterAll();
	}
	
	public static void handledReceivedMessage(final String message, final String action) {
		//Dispatch the event
		if(activity != null) {
			activity.runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	            	MessageReceived(message, action);
	            }
	        });
		} else {
			/*Intent intent = GoogleCloudMessagingBackgroundService.activity.getPackageManager()
					.getLaunchIntentForPackage(GoogleCloudMessagingBackgroundService.packageName);
			GoogleCloudMessagingBackgroundService.activity.startActivity(intent);
			MessageReceived(message, action);*/
			MessageReceived(message, action);
		}
	}
	
	
	public static void handledDataListReceived(final List<Object> objects) {
		//Dispatch the event
		if(activity != null) {
			activity.runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	            	DataListReceived(objects);
	            }
	        });
		} else {
			/*Intent intent = GoogleCloudMessagingBackgroundService.activity.getPackageManager()
					.getLaunchIntentForPackage(GoogleCloudMessagingBackgroundService.packageName);
			GoogleCloudMessagingBackgroundService.activity.startActivity(intent);
			DataListReceived(objects);*/
			DataListReceived(objects);
		}
	}
	
	/**
	 * Indicates that a message has been received.
	 */
	 @SimpleEvent(description = "A message has been received.")
	 public static void MessageReceived(String message, String action) { 
	    EventDispatcher.dispatchEvent(component, "MessageReceived", message, action);
	 }
	 
	 /**
	 * Indicates that a message has been received.
	 */
	 @SimpleEvent(description = "A message with a object has been received.")
	 public static void DataListReceived(List<Object> objects) {
	    EventDispatcher.dispatchEvent(component, "DataListReceived", objects);
	 }
	 
	 @SimpleFunction(description="Function to show message in notification bar.")
	 public void ShowNotificationBar(String message, String title) {
		 String classname = activity.getPackageName() + ".Screen1";
	        Intent intent = null;
			try {
				intent = new Intent(activity, Class.forName(classname));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0 /* Request code */, intent,
	                PendingIntent.FLAG_ONE_SHOT);

	        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(activity)
	        		.setSmallIcon(activity.getApplicationInfo().icon)
	                .setContentTitle(title)
	                .setContentText(message)
	                .setAutoCancel(false)
	                .setSound(defaultSoundUri)
	                .setContentIntent(pendingIntent);

	        NotificationManager notificationManager =
	                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

	        notificationManager.notify(1, notificationBuilder.build());
	 }
	 
	 /*public class MyExclusionStrategy implements ExclusionStrategy {
			private final Class<?> typeToSkip;

			public MyExclusionStrategy(Class<?> typeToSkip) {
				this.typeToSkip = typeToSkip;
			}

			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}

			public boolean shouldSkipField(FieldAttributes f) {
				return (!f.getDeclaredType().toString().equals("int") && 
						!f.getDeclaredType().toString().equals("double") && 
						!f.getDeclaredType().toString().equals("boolean") && 
						!f.getDeclaredType().toString().contains("String"));
			}
	}*/
}
