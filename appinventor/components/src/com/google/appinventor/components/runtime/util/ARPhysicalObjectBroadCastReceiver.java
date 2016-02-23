/**
 * 
 */
package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.runtime.ARCamera;
import com.google.appinventor.components.runtime.ARPhysicalObject;
import com.google.appinventor.components.runtime.ar4ai.ARActivity;
import com.google.appinventor.components.runtime.ar4ai.PhysicalObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author ivanruizrube
 *
 */
public class ARPhysicalObjectBroadCastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// Extract data included in the Intent
		String uuid = intent.getStringExtra("uuid");
		int status = intent.getIntExtra("status", PhysicalObject.STATUS_INVISIBLE);

		float x = intent.getFloatExtra("x", -1f);
		float y = intent.getFloatExtra("y", -1f);
		float z = intent.getFloatExtra("z", -1f);

		// Locate the ARPhysicalObject
		ARPhysicalObject arPO = ARCamera.mapOfARPhysicalObjects.get(uuid);

		
		if (arPO != null) {
			if (status == PhysicalObject.STATUS_DISAPPEARS) {
				arPO.Disappears();
			} else if (status == PhysicalObject.STATUS_APPEARS) {
				arPO.Appears(x, y, z);
			} else if (status == PhysicalObject.STATUS_CHANGEDPOSITION) {
				arPO.ChangedPosition(x, y, z);
			}
		}

	}
}
