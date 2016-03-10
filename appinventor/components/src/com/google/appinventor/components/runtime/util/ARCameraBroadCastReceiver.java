/**
 * 
 */
package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.runtime.ARCamera;
import com.google.appinventor.components.runtime.ar4ai.ARActivity;
import com.google.appinventor.components.runtime.ar4ai.PhysicalObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author ivanruizrube
 *
 */
public class ARCameraBroadCastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		// Extract data included in the Intent
		String status = intent.getStringExtra("status");
		float x = intent.getFloatExtra("x", -1f);
		float y = intent.getFloatExtra("y", -1f);

		if (status.equals(ARActivity.AR_ACTIVITY_EVENT_CAMERA_LONGPRESS)) {
			ARCamera.getInstance().Touched(x, y, false);
		}
		
		if (status.equals(ARActivity.AR_ACTIVITY_EVENT_CAMERA_RIGHTBUTTON)) {
			ARCamera.getInstance().RightButtonClick();
		}
		
		if (status.equals(ARActivity.AR_ACTIVITY_EVENT_CAMERA_LEFTBUTTON)) {
			ARCamera.getInstance().LeftButtonClick();
		}
	}
}
