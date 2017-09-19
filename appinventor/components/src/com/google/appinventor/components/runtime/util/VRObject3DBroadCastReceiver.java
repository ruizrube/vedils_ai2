package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.runtime.VRScene;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class VRObject3DBroadCastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent intent) {
		
		String msg= intent.getStringExtra("msg");
		VRScene vrScene= VRScene.getInstance();
		//vrScene.binocularMode=true;
		
	}

}
