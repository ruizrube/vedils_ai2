package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.util.OnInitializeListener;
import com.google.appinventor.components.runtime.gvr.VideoActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

@UsesLibraries(libraries = "GoogleVR4ai.jar, jpct_ae.jar, libprotobuf-java-2.3-nano.jar, android-support-v4.jar")
//sdk-controller-1.130.0.jar, sdk-base-1.130.0.jar, sdk-common-1.130.0.jar,
//@UsesNativeLibraries(v7aLibraries = "libpano_video_renderer.so, libpanorenderer.so")
//@SimpleObject
//@DesignerComponent(nonVisible = true, version = 1, description = "Scene container for GVR (by SPI-FM at UCA)", category = ComponentCategory.VEDILSVIRTUALREALITY, iconName = "images/virtualRealityVideo.png")
@UsesPermissions(permissionNames = "android.permission.NFC, android.permission.VIBRATE ,android.permission.WRITE_EXTERNAL_STORAGE, android.permission.READ_EXTERNAL_STORAGE,android.permission.INTERNET,android.permission.RECORD_AUDIO")

public class GVRVideo360 extends AndroidNonvisibleComponent implements OnInitializeListener, OnResumeListener, OnPauseListener, OnStopListener, OnDestroyListener, ActivityResultListener {
	
	private static final long serialVersionUID = 1L;
	private static GVRVideo360 instance;	
	public int video360Volume = 50;	
	private static final String GVR_ACTIVITY_CLASS = "com.google.appinventor.components.runtime.gvr.VideoActivity";
	private ComponentContainer container;
	Intent intent = new Intent();
	private boolean stereoMode;
	private final int requestCode;
	private boolean receiverV360EventsRegistered;
	private String urlToPlay;	
	//private static GVRVideo360 instance;	
	//public String video360Path = null;
	//public boolean video360IsURL = false;
	//public boolean isVideo360=false;	
	//private VRVideo360 vrV360;
	//private final int requestCode;	
	//public Video360Parcelable video360Par;
	//private int ambientLight;	
	//public Object objtoExtract;	

	
	public GVRVideo360(ComponentContainer container){
		super(container.$form());
		Log.v("VRSCENE", "entrando en constructor");
		this.container = container;
		
		Log.v("VRSCENE", "registrando en el form");		
		container.$form().registerForOnInitialize(this);
		container.$form().registerForOnResume(this);
		container.$form().registerForOnPause(this);
		container.$form().registerForOnStop(this);
		container.$form().registerForOnDestroy(this);
		Log.v("GVRVideo360", "pidiendo request code");
		
		requestCode = form.registerForActivityResult(this);
		Log.v("GVRVideo360", "seteando clase del intent");
		intent.setClassName(container.$context(), GVR_ACTIVITY_CLASS);
		instance = this;	
		this.receiverV360EventsRegistered = false;

	}	

	
	public static GVRVideo360 getInstance() 
	{
		return instance;
	}	
	
	
	public BroadcastReceiver videoEndEventBroadCastReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("video360", "BETA3");			
			EndVideo();
		}

	};
	
	
	public BroadcastReceiver videoStartEventBroadCastReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) {

			int videoDuration = intent.getIntExtra("VideoDuration", 0);
			StartVideo(videoDuration);
		}

	};	
	
	
	public BroadcastReceiver doubleTapTouchEventBroadCastReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			DobleTapTouch();
		}

	};
	
	
	public BroadcastReceiver singleTapTouchEventBroadCastReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			SingleTapTouch();
		}

	};
	
	
	public BroadcastReceiver longPressTouchEventBroadCastReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			LongPressTouch();
		}

	};

	
	//@SimpleEvent
	public void EndVideo() {
		Log.d("video360", "--BETA4");

		EventDispatcher.dispatchEvent(this, "EndVideo");		
	}

	//@SimpleEvent
	public void StartVideo(int videoDuration) {
		EventDispatcher.dispatchEvent(this, "StartVideo", videoDuration);
	}	
	
	//@SimpleEvent
	public void DobleTapTouch() {
		EventDispatcher.dispatchEvent(this, "DobleTapTouch");
		
	}
	
	//@SimpleEvent
	public void SingleTapTouch() {
		EventDispatcher.dispatchEvent(this, "SingleTapTouch");
	}
	
	//@SimpleEvent
	public void LongPressTouch() {
		EventDispatcher.dispatchEvent(this, "LongPressTouch");
	}


	//@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT, defaultValue = "50")
	//@SimpleProperty(description = "Sets the volume to a number between 0 and 100")
	public void Volume(int vol) {
		
		video360Volume = vol;
		
	}

	//@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public int getVolume() 
	{
		return video360Volume;	
	}	
	
	//@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	//@SimpleProperty(userVisible = true)
	public void Video360Url(String path) 
	{
		urlToPlay=path;
		Log.d("video360", path);
	}	

	
	//@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "false")
	//@SimpleProperty(userVisible = true)
	public void Stereo(boolean bin) 
	{
		this.stereoMode=bin;
	}

	//@SimpleFunction(description = "Start GvrScene", userVisible = true)
	public void Start() 
	{
		prepareIntent();
		container.$context().startActivityForResult(intent, requestCode);
	}		
	
	//@SimpleFunction(description = "Stop GvrScene", userVisible = true)
	public void Stop() 
	{
		Intent stopIntent = new Intent(VideoActivity.VR_STOP);
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(stopIntent);			
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(doubleTapTouchEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(singleTapTouchEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(longPressTouchEventBroadCastReceiver);
	
	}
	
	
	//@SimpleFunction(description = "Stop video360", userVisible = true)
	public void StopPlay() 
	{

		Intent stopIntent2 = new Intent(VideoActivity.VR_VIDEO_STOP);
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(stopIntent2);
		System.out.println("pentra en StopPlay()");
	}

	//@SimpleFunction(description = "Play video360", userVisible = true)
	public void Play() 
	{
		Intent playIntent = new Intent(VideoActivity.VR_VIDEO_PLAY);
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(playIntent);

	}

	//@SimpleFunction(description = "Pause video360", userVisible = true)
	public void Pause()
	{

		Intent pauseIntent = new Intent(VideoActivity.VR_VIDEO_PAUSE);
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(pauseIntent);

	}

	//@SimpleFunction(description = "Reset video360", userVisible = true)
	public void Reset()
	{
		Intent resetIntent = new Intent(VideoActivity.VR_VIDEO_RESET);
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(resetIntent);

	}

	//@SimpleFunction(description = "Seek to video360", userVisible = true)
	public void SeekTo(int position) {

		Intent seektoIntent = new Intent(VideoActivity.VR_VIDEO_SEEKTO);
		seektoIntent.putExtra("SeektoPosition", position);
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(seektoIntent);

	}	
	
	
	private void prepareIntent() 
	{
		intent.putExtra("Video360Volume",video360Volume);
		intent.putExtra("StereoMode", stereoMode);
		intent.putExtra("urlToPlay", urlToPlay);
		intent.putExtra(VideoActivity.VR_COMPANION, container.$form() instanceof ReplForm);
		
		LocalBroadcastManager.getInstance(container.$form()).registerReceiver(doubleTapTouchEventBroadCastReceiver,
				new IntentFilter(VideoActivity.VR_EVENT_TOUCH_DOBLETAP));
		
		LocalBroadcastManager.getInstance(container.$form()).registerReceiver(singleTapTouchEventBroadCastReceiver,
				new IntentFilter(VideoActivity.VR_EVENT_TOUCH_SINGLETAP));
		
		LocalBroadcastManager.getInstance(container.$form()).registerReceiver(longPressTouchEventBroadCastReceiver,
				new IntentFilter(VideoActivity.VR_EVENT_TOUCH_LONGPRESS));
		
		
		if(!this.receiverV360EventsRegistered) {
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver( this.videoEndEventBroadCastReceiver,
					new IntentFilter(VideoActivity.VR_EVENT_VIDEO_END));
			LocalBroadcastManager.getInstance(container.$form()).registerReceiver(this.videoStartEventBroadCastReceiver,
					new IntentFilter(VideoActivity.VR_EVENT_VIDEO_START));
			this.receiverV360EventsRegistered = true;
			} 
	}//fin preareIntent
	
	@Override
	public void resultReturned(int requestCode, int resultCode, Intent data) 
	{
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(doubleTapTouchEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(singleTapTouchEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(container.$context()).unregisterReceiver(longPressTouchEventBroadCastReceiver);
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

	
	
	}//fin GVRVideo360
	