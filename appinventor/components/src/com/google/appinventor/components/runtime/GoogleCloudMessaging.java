package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.GoogleCloudMessagingConnectionServer;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

@UsesLibraries(libraries = "google-play-services.jar," +
		"android-support-v4.jar," +
		"la4ai.jar")
@SimpleObject
@DesignerComponent(nonVisible= true, version = 1, description = "Google Cloud Messaging Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSLEARNINGANALYTICS, iconName = "images/arColorTracker.png")
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
	
	private GoogleCloudMessagingConnectionServer connectionServer;
	private static Component component;
	private static Activity activity;
	
	public GoogleCloudMessaging(ComponentContainer componentContainer) {
		super(componentContainer.$form());	
		connectionServer = new GoogleCloudMessagingConnectionServer(componentContainer.$context());
		GoogleCloudMessaging.component = (GoogleCloudMessaging) this;
		activity = componentContainer.$context();
	}
	
	
	/**
	 * Function to send a text message to all devices with the same app.
	 * @param message
	 */
	@SimpleFunction(description="Function to send a text message to all devices registered in the same app.")
	public void SendMessage(String message) {
		this.connectionServer.sendMessage(message);
	}
	
	
	/**
	 * Function to get the registration token.
	 */
	@SimpleFunction(description="Function to get the registration token.")
	public void Register() {
		this.connectionServer.register();
	}
	
	/**
	 * Function to unregister client on server.
	 */
	@SimpleFunction(description="Function to delete this client of server.")
	public void UnRegister() {
		this.connectionServer.unRegister();
	}
	
	/**
	 * Function to unregister all clients on server.
	 */
	@SimpleFunction(description="Function to delete all clients of server.")
	public void UnRegisterAll() {
		this.connectionServer.unRegisterAll();
	}
	
	public static void handledReceivedMessage(final String message) {
		//Dispatch the event
		activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	MessageReceived(message);
            }
        });
	}
	
	/**
	 * Indicates that a message has been received.
	 */
	 @SimpleEvent(description = "A message has been received.")
	 public static void MessageReceived(String message) { 
	    EventDispatcher.dispatchEvent(component, "MessageReceived", message);
	 }
	 
	 @SimpleFunction(description="Function to show message in notification bar.")
	 public void ShowNotificationBar(String message) {
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
	                .setContentTitle(activity.getApplicationInfo().packageName + " Notification.")
	                .setContentText(message)
	                .setAutoCancel(true)
	                .setSound(defaultSoundUri)
	                .setContentIntent(pendingIntent);

	        NotificationManager notificationManager =
	                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

	        notificationManager.notify(1, notificationBuilder.build());
	 }
}
