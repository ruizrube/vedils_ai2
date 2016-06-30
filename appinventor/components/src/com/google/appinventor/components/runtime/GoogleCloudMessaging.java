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
	
	public GoogleCloudMessaging(ComponentContainer componentContainer) {
		super(componentContainer.$form());	
		connectionServer = new GoogleCloudMessagingConnectionServer(componentContainer.$context());
		GoogleCloudMessaging.component = (GoogleCloudMessaging) this;
		activity = componentContainer.$context();
				//new GsonBuilder().setExclusionStrategies(/*new MyExclusionStrategy(String.class)*/).serializeNulls().create();
	}
	
	
	/**
	 * Function to send a text message to all devices with the same app.
	 * @param message
	 */
	@SimpleFunction(description="Function to send a text message to all devices registered in the same app.")
	public void SendMessage(String message, String action) {
		connectionServer.sendMessage(message, action);
	}
	
	/**
	 * Function to send a list of objects as a message to all devices with the same app.
	 * @param objects
	 */
	@SimpleFunction(description="Function to send a list of object as a message to all devices registered in the same app.")
	public void SendDataList(List<Object> objects) {
		connectionServer.sendObjectsMessage(objects);
	}
	
	/**
	 * Function to get the registration token.
	 */
	@SimpleFunction(description="Function to get the registration token.")
	public void Register() {
		connectionServer.register();
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
		activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	MessageReceived(message, action);
            }
        });
	}
	
	
	public static void handledDataListReceived(final List<Object> objects) {
		//Dispatch the event
		activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	DataListReceived(objects);
            }
        });
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
