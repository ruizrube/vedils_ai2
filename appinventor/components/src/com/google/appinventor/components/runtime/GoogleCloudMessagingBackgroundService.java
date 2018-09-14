package com.google.appinventor.components.runtime;

/*import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;*/

import com.google.android.gms.gcm.GcmListenerService;
//import com.google.appinventor.components.runtime.GoogleCloudMessaging;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class GoogleCloudMessagingBackgroundService extends GcmListenerService {
	
	
	@Override
    public void onMessageReceived(String from, Bundle data) {
		String message = data.getString("message");
        String action = data.getString("action");
        System.out.println("From: " + from);
        System.out.println("Message: " + message);
        
        String packageName = getApplicationContext().getPackageName();
		Activity activity = (Activity) getApplicationContext();
        
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
        		//.setSmallIcon(activity.getApplicationInfo().icon)
                .setContentTitle("Tienes nuevas notificaciones")
                .setContentText("Pulsa la notificación para visualizarlas")
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notificationBuilder.build());
	}
	
	
	/*public static String packageName = "";
	public static Activity activity;
	public static GoogleCloudMessaging gcmComponent;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		packageName = getApplicationContext().getPackageName();
		activity = (Activity) getApplicationContext();
	    return Service.START_NOT_STICKY;
	}*/
}
