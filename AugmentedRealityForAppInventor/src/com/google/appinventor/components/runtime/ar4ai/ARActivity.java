package com.google.appinventor.components.runtime.ar4ai;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Window;

public abstract class ARActivity extends Activity {

	// CONSTANTS

	public static final String AR_ACTIVITY_CLASS = "com.google.appinventor.components.runtime.ar4ai.vuforia.VuforiaARActivity";

	public static final String AR_ACTIVITY_ARG_CAMERAOBJECT = AR_ACTIVITY_CLASS + ".cameraObject";
	public static final String AR_ACTIVITY_ARG_VIRTUALOBJECTS = AR_ACTIVITY_CLASS + ".arrayOfVirtualObjects";
	public static final String AR_ACTIVITY_ARG_PHYSICALOBJECTS = AR_ACTIVITY_CLASS + ".arrayOfPhysicalObject";

	public static final String AR_ACTIVITY_SIGNAL_STOP = AR_ACTIVITY_CLASS + ".signalStop";
	public static final String AR_ACTIVITY_SIGNAL_REFRESH = AR_ACTIVITY_CLASS + ".signalRefresh";
	public static final String AR_ACTIVITY_SIGNAL_UPDATE_ACTIONBAR = AR_ACTIVITY_CLASS + ".signalUpdateActionBar";

	public static final String AR_ACTIVITY_EVENT_PO = AR_ACTIVITY_CLASS + ".eventPO";

	public static final String AR_ACTIVITY_EVENT_CAMERA = AR_ACTIVITY_CLASS + ".eventCamera";
	public static final String AR_ACTIVITY_EVENT_CAMERA_LONGPRESS = AR_ACTIVITY_EVENT_CAMERA + ".longpress";

	public static final String AR_ACTIVITY_EVENT_VO = AR_ACTIVITY_CLASS + ".eventVO";

	/////////////////////
	// LOCAL ATTRIBUTES //
	/////////////////////

	public static final String LOGTAG = "ar4ai-VuforiaARActivity";

	public static final int MAX_TRACKERS = 25;

	public Camera camera;
	public ArrayList<VirtualObject> arrayOfVirtualObjects;
	public ArrayList<PhysicalObject> arrayOfPhysicalObject;

	///////////////
	// RECEIVER //
	//////////////
	
	private BroadcastReceiver updateActionBarEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(LOGTAG, "AR4AI RECIBE: AR_ACTIVITY_SIGNAL_REFRESH");
			extractDataFromIntent(intent);
			updateActionBar();
		}

	};

	private BroadcastReceiver refreshEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(LOGTAG, "AR4AI RECIBE: AR_ACTIVITY_SIGNAL_UPDATE_ACTIONBAR");
			extractDataFromIntent(intent);
			refreshARScene();
		}

	};

	private BroadcastReceiver stopEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(LOGTAG, "AR4AI RECIBE: AR_ACTIVITY_SIGNAL_STOP");
			finish();
		}

	};

	abstract protected void refreshARScene();

	/////////////////////////
	// ACTIVITY LIFECYCLE //
	/////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		if (getActionBar() != null) {
			getActionBar().setDisplayShowTitleEnabled(true);
			getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
		}

		super.onCreate(savedInstanceState);

		// Recuperados datos sobre los objetos fisicos y virtuales
		extractDataFromIntent(getIntent());
		updateActionBar();

	}

	private void updateActionBar() {
		boolean show = false;
		if (getActionBar() != null) {
			if (this.camera.getTitle() != null && !this.camera.getTitle().equals("")) {
				show = true;
				getActionBar().setTitle(this.camera.getTitle());
			}

			if (this.camera.getSubtitle() != null && !this.camera.getSubtitle().equals("")) {
				show = true;
				getActionBar().setSubtitle(this.camera.getSubtitle());
			}

			if (show) {
				getActionBar().show();
			} else {
				getActionBar().hide();
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		// Registrar receptor de broadcast
		LocalBroadcastManager.getInstance(this).registerReceiver(stopEventBroadCastReceiver, new IntentFilter(AR_ACTIVITY_SIGNAL_STOP));
		LocalBroadcastManager.getInstance(this).registerReceiver(refreshEventBroadCastReceiver, new IntentFilter(AR_ACTIVITY_SIGNAL_REFRESH));
		LocalBroadcastManager.getInstance(this).registerReceiver(updateActionBarEventBroadCastReceiver, new IntentFilter(AR_ACTIVITY_SIGNAL_UPDATE_ACTIONBAR));

	}

	@Override
	protected void onPause() {
		super.onPause();

		// Unregister since the activity is not visible
		LocalBroadcastManager.getInstance(this).unregisterReceiver(this.stopEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(this.refreshEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(this.updateActionBarEventBroadCastReceiver);

	}

	@Override
	protected void onDestroy() {
		// The final call you receive before your activity is destroyed.
		super.onDestroy();
	}

	//////////////////////
	// ACCESSOR METHODS //
	//////////////////////

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public ArrayList<VirtualObject> getArrayOfVirtualObjects() {
		return arrayOfVirtualObjects;
	}

	public void setArrayOfVirtualObjects(ArrayList<VirtualObject> arrayOfVirtualObjects) {
		this.arrayOfVirtualObjects = arrayOfVirtualObjects;
	}

	public ArrayList<PhysicalObject> getArrayOfPhysicalObject() {
		return arrayOfPhysicalObject;
	}

	public void setArrayOfPhysicalObject(ArrayList<PhysicalObject> arrayOfPhysicalObject) {
		this.arrayOfPhysicalObject = arrayOfPhysicalObject;
	}

	//////////////////////
	// INTERNAL METHODS //
	//////////////////////

	private synchronized void extractDataFromIntent(Intent myIntent) {

		// Obtenemos los datos definidoss para la camara y los objetos fisicos y
		// virtuales de AppInventor

		if (myIntent.hasExtra(AR_ACTIVITY_ARG_CAMERAOBJECT)) {

			this.setCamera((Camera) myIntent.getExtras().getParcelable(AR_ACTIVITY_ARG_CAMERAOBJECT));
			Log.d(LOGTAG, "RECIBIMOS" + this.getCamera() + "LA CAMARA");

		}

		if (myIntent.hasExtra(AR_ACTIVITY_ARG_PHYSICALOBJECTS)) {

			ArrayList<PhysicalObject> pos = myIntent.getParcelableArrayListExtra(AR_ACTIVITY_ARG_PHYSICALOBJECTS);
			this.setArrayOfPhysicalObject(pos);
			Log.d(LOGTAG, "RECIBIMOS" + this.getArrayOfPhysicalObject().size() + "objetos fisicos");
				

		}

		if (myIntent.hasExtra(AR_ACTIVITY_ARG_VIRTUALOBJECTS)) {

			ArrayList<VirtualObject> vos = myIntent.getParcelableArrayListExtra(AR_ACTIVITY_ARG_VIRTUALOBJECTS);
			this.setArrayOfVirtualObjects(vos);

			Log.d(LOGTAG, "RECIBIMOS" + this.getArrayOfVirtualObjects().size() + "objetos virtuales");

		}
	}

}