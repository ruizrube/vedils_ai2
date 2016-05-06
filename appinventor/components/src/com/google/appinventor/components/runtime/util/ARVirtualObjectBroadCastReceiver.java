/**
 * 
 */
package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.runtime.ARCamera;
import com.google.appinventor.components.runtime.ARVirtualObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author ivanruizrube
 *
 */
public class ARVirtualObjectBroadCastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		String action = intent.getStringExtra("action");
		if (action.equals("collision")) {
			String giverId = intent.getStringExtra("giver");
			ARVirtualObject giver = ARCamera.getInstance().mapOfARVirtualObjects.get(giverId);
			String receiverId = intent.getStringExtra("receiver");
			ARVirtualObject receiver = ARCamera.getInstance().mapOfARVirtualObjects.get(receiverId);
			giver.CollidedWith(receiver);
		}
	}
}
