package com.google.appinventor.components.runtime;

import java.util.ArrayList;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.util.ARVirtualObjectBroadCastReceiver;
import com.google.appinventor.components.runtime.util.OnInitializeListener;
import com.google.appinventor.components.runtime.util.VRObject3DBroadCastReceiver;
import com.google.appinventor.components.runtime.vr4ai.VRActivity;
import com.google.appinventor.components.runtime.vr4ai.util.Object3DParcelable;
import com.threed.jpct.Texture;

import android.util.Log;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

@UsesLibraries(libraries = "vr4ai.jar, jpct_ae.jar, cardboard.jar, libprotobuf-java-2.3-nano.jar, android-support-v4.jar")
@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "Scene container for VR (by SPI-FM at UCA)", category = ComponentCategory.VEDILSVIRTUALREALITY, iconName = "images/virtualRealityScene.png")
@UsesPermissions(permissionNames = "android.permission.NFC, android.permission.VIBRATE ,android.permission.WRITE_EXTERNAL_STORAGE, android.permission.READ_EXTERNAL_STORAGE,android.permission.INTERNET")

public class VRScene  extends AndroidNonvisibleComponent implements OnInitializeListener, OnResumeListener, OnPauseListener, OnStopListener, OnDestroyListener, ActivityResultListener {

	
	private static final String VR_ACTIVITY_CLASS = "com.google.appinventor.components.runtime.vr4ai.VRActivity";
	private ComponentContainer container;
	private final int requestCode;
	Intent intent = new Intent();
	public boolean is3DObject=false;
	public boolean isImage360=false;
	public boolean isVideo360=false;
	public boolean hasController=false;
	public String  model3DPath="";
	public String  material3DPath="";
	public Object objtoExtract;
	private static VRScene instance;
	VRObject3DBroadCastReceiver vrBroadCast= new VRObject3DBroadCastReceiver();
	private VR3DObject vr3d;
	private VRImage360 vrI360;
	private VRVideo360 vrV360;
	private VRController controller;
	private boolean stereoMode;
	private String skyboxPath;
	public ArrayList<Object3DParcelable> object3DList= new ArrayList<Object3DParcelable>();
	private int ambientLight;
	
	public VRScene(ComponentContainer container) {
		super(container.$form());
		Log.v("VRSCENE", "entrando en constructor");
		this.container = container;

		Log.v("VRSCENE", "registrando en el form");
		container.$form().registerForOnInitialize(this);
		container.$form().registerForOnResume(this);
		container.$form().registerForOnPause(this);
		container.$form().registerForOnStop(this);
		container.$form().registerForOnDestroy(this);
		Log.v("VRSCENE", "pidiendo request code");
		requestCode = form.registerForActivityResult(this);
		Log.v("VRSCENE", "seteando clase del intent");
		intent.setClassName(container.$context(), VR_ACTIVITY_CLASS);
		instance = this;
	
	}

	public void setAssetToExtract(Object obj)
	{
		objtoExtract=obj;
	}
	public void setControllerComponent(VRController controller)
	{
		this.controller=controller;
	}
	public static VRScene getInstance() {
		return instance;
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET_IMAGE, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void SkyboxImage(String path) {

		this.skyboxPath = path;

		Log.d("sender", path);

	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "false")
	@SimpleProperty(userVisible = true)
	public void Stereo(boolean bin) {
		
		this.stereoMode=bin;
		
		
	}
	@SimpleFunction(description = "Stop vrScene", userVisible = true)
	public void Stop() {
	
		
		    Intent stopIntent = new Intent(VRActivity.VR_STOP);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(stopIntent);

	}
	@SimpleFunction(description = "Move Object3D focus", userVisible = true)
	public void MoveFocus() {
	
		
		    Intent moveFocusIntent = new Intent(VRActivity.VR_MOVEFOCUS);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(moveFocusIntent);

	}
	@SimpleFunction(description = "Start 3d viewer", userVisible = true)
	public void Start() {
	
		//LocalBroadcastManager.getInstance(container.$context()).registerReceiver(vrBroadCast, new IntentFilter(VR_ACTIVITY_CLASS+ ".mensaje"));
		
		prepareIntent();
		container.$context().startActivityForResult(intent, requestCode);

	}
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public int getAmbientLightIntensity() {

		return ambientLight;
	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = "50")
	@SimpleProperty(userVisible = true)
	public void AmbientLightIntensity(int val) {

		if(val>250){
		this.ambientLight = 250;
		}
		else
		{
			this.ambientLight = val;
		}

		Log.d("sender", val+"");

	}
	
	
	private void prepareIntent() {
		
		intent.putExtra("StereoMode", stereoMode);
		intent.putExtra("Controller", hasController);
		//OBJECT3D//
		if(is3DObject)
		{
			/*vr3d=(VR3DObject)objtoExtract;
			intent.putExtra("3DmodelPath",vr3d.model3DPath);
			intent.putExtra("3DmaterialPath",vr3d.material3DPath);
			intent.putExtra("PositionX", vr3d.positionX);
			intent.putExtra("PositionY", vr3d.positionY);
			intent.putExtra("PositionZ", vr3d.positionZ);
			intent.putExtra("Scale", vr3d.scale);*/
			intent.putExtra("Object3D", true);
			intent.putExtra("SkyboxPath", skyboxPath);
			intent.putExtra("MoveSpeed", controller.moveSpeed);
			intent.putExtra("RotateSpeed", controller.rotationSpeed);
			
			intent.putParcelableArrayListExtra("Object3DList", object3DList);
			
		}
	
		//IMAGE360//
		if(isImage360)
		{
			vrI360=(VRImage360)objtoExtract;
			intent.putExtra("Image360Path",vrI360.image360Path);
			intent.putExtra("Image360", true);
		}
			
		//VIDEO360//
		
		if(isVideo360)
		{
			vrV360=(VRVideo360)objtoExtract;
			intent.putExtra("Video360Path",vrV360.video360Path);
			intent.putExtra("IsURL", vrV360.isURL);
			intent.putExtra("IsLoop", vrV360.video360isloop);
			intent.putExtra("Video360Volume", vrV360.video360Volume);
			intent.putExtra("Video360Quality", vrV360.video360Quality);
			intent.putExtra("Video360", true);
			
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(vrV360.videoEndEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_VIDEO_END));
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(vrV360.videoStartEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_VIDEO_START));
		}
			
		if(hasController)
		{
			//Buttons
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(controller.pressAEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_PRESS_A_BUTTON));
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(controller.pressBEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_PRESS_B_BUTTON));
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(controller.pressCEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_PRESS_C_BUTTON));
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(controller.pressDEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_PRESS_D_BUTTON));
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(controller.pressXEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_PRESS_X_BUTTON));
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(controller.pressZEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_PRESS_Z_BUTTON));
			
			//Joystick
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(controller.pressUpJoystickEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_PRESS_UP_JOYSTICK));
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(controller.pressDownJoystickEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_PRESS_DOWN_JOYSTICK));
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(controller.pressLeftJoystickEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_PRESS_LEFT_JOYSTICK));
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(controller.pressRightJoystickEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_PRESS_RIGHT_JOYSTICK));
			
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(controller.pressUpLeftJoystickEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_PRESS_UP_LEFT_JOYSTICK));
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(controller.pressUpRightJoystickEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_PRESS_UP_RIGHT_JOYSTICK));
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(controller.pressDownLeftJoystickEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_PRESS_DOWN_LEFT_JOYSTICK));
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(controller.pressDownRightJoystickEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_EVENT_PRESS_DOWN_RIGHT_JOYSTICK));
		}
		
	}

	@Override
	public void resultReturned(int requestCode, int resultCode, Intent data) {
		//cuando vuelvo al scrren1 dejo de recibir de vractivity
		if(hasController){
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(controller.pressAEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(controller.pressBEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(controller.pressCEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(controller.pressDEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(controller.pressXEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(controller.pressZEventBroadCastReceiver);
		
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(controller.pressUpJoystickEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(controller.pressDownJoystickEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(controller.pressLeftJoystickEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(controller.pressRightJoystickEventBroadCastReceiver);
		
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(controller.pressUpLeftJoystickEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(controller.pressUpRightJoystickEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(controller.pressDownLeftJoystickEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(controller.pressDownRightJoystickEventBroadCastReceiver);

		}
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitialize() {
		// TODO Auto-generated method stub
		
	}

}
