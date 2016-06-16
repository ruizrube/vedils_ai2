package com.google.appinventor.components.runtime.util;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.appinventor.components.runtime.GoogleCloudMessaging;

import android.os.Bundle;

public class GoogleCloudMessagingListenerService extends GcmListenerService {
	
	
	/**
     * Function call when the message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        System.out.println("From: " + from);
        System.out.println("Message: " + message);
        
        //Send the message to the GoogleCloudMessaging component.
        GoogleCloudMessaging.handledReceivedMessage(message);
    }
}
