package com.google.appinventor.components.runtime;

import java.util.UUID;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.vr4ai.VRActivity;
import com.google.appinventor.components.runtime.vr4ai.util.Video360Parcelable;

//Edson
import com.google.appinventor.components.runtime.gvr.VideoActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "Video360 source for VR (by SPI-FM at UCA)", category = ComponentCategory.VEDILSVIRTUALREALITY, iconName = "images/virtualRealityVideo.png")
public class VRVideo360 extends AndroidNonvisibleComponent {

	private ComponentContainer container;
	public String video360Path = null;
	public boolean video360IsURL = false;
	public boolean video360isloop = false;
	public int video360Volume = 50;
	public static VRVideo360 instance;
	public String video360Quality = "240";
	public UUID id = UUID.randomUUID();
	public Video360Parcelable video360par = new Video360Parcelable();
	
	//Edson Unificando inicio
	public boolean ScrollScreen;
	//Edson Unificando fin
	
	public BroadcastReceiver videoEndEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("video360", "BETA3");

			EndVideo();
		}

	};
	public BroadcastReceiver videoStartEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			int videoDuration = intent.getIntExtra("VideoDuration", 0);
			StartVideo(videoDuration);
		}

	};

	public VRVideo360(ComponentContainer container) {

		super(container.$form());
		this.container = container;
		instance = this;

		// valores por defecto
		video360par.setId(id + "");
		video360par.setVideo360Volumen(video360Volume);
		video360par.setVideo360Quality(video360Quality);

		if (video360isloop) {
			video360par.setIsLoop(1);
		} else {
			video360par.setIsLoop(0);
		}
		if (video360IsURL) {
			video360par.setIsURL(1);
		} else {
			video360par.setIsURL(0);
		}

	}

	public static VRVideo360 getInstance() {
		return instance;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ONLY_VRSCENE, defaultValue = "")
	@SimpleProperty(description = "Stick the virtual object to a given Camera", userVisible = true)
	public void VRScene(VRScene scene) {
		if (scene != null) {
			scene.isVideo360 = true;
			scene.video360Par = video360par;
			scene.setAssetToExtract(this);
			//Edson unificando inicio
			Log.d("Unificando", "Probando donde pregunta.");
			if (scene.cardboard)
				ScrollScreen = true;
			else
				ScrollScreen = false;
			//	System.out.println("REdson prueba VR_ACTIVITY_CLASS: "+scene.VR_ACTIVITY_CLASS);
			//Edosn unificando fin
		}
	}
	//Edosn unificando inicio
	//@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "true")
	//@SimpleProperty(userVisible = true)
	public void CardBoard(boolean board)
	{
		ScrollScreen = board;
	}

	//@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public boolean getCardboard()
	{
		return ScrollScreen;
	}	
	//Edosn unificando fin
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String getVideo360Asset() {

		return video360Path;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void Video360Asset(String path) {
		//if (video360Path == null) {
			this.video360Path = path;
		//}

		Log.d("video360", path);

	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String getVideo360Url() {

		return video360Path;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void Video360Url(String path) {

		//if (video360Path == null) {
			video360IsURL = true;
			this.video360Path = path;

			video360par.setIsURL(1);
			video360par.setVideo360Path(path);
			//}

		Log.d("video360", path);

	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public boolean getLoop() {

		return video360isloop;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
	@SimpleProperty(userVisible = true)
	public void Loop(boolean loop) {

		video360isloop = loop;

		if (video360isloop) {
			video360par.setIsLoop(1);
		} else {
			video360par.setIsLoop(0);
		}

		Log.d("video360", video360isloop + "");
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT, defaultValue = "50")
	@SimpleProperty(description = "Sets the volume to a number between 0 and 100")
	public void Volume(int vol) {
		video360Volume = vol;

		video360par.setVideo360Volumen(video360Volume);
	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public int getVolume() {

		return video360Volume;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_QUALITY_YOUTUBE, defaultValue = "720 HD")
	@SimpleProperty(description = "Set video quality")
	public void VideoUrlQuality(String quality) {
		video360Quality = quality;

		video360par.setVideo360Quality(video360Quality);
	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String getVideoUrlQuality() {

		return video360Quality;
	}
	// al existir ahora mas de un video en la escena, tengo que pensar bien estos
	// eventos
//Edson Unificando inicio
	@SimpleFunction(description = "Stop video360", userVisible = true)
	public void Stop() 
	{
		if (ScrollScreen)
		{
			Intent stopIntent = new Intent(VRActivity.VR_VIDEO_STOP);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(stopIntent);
		}
		else
		{
			Intent stopIntent = new Intent(VideoActivity.VR_VIDEO_STOP);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(stopIntent);
		}

	}
	
	@SimpleFunction(description = "Play a custom start end video both in milliseconds.", userVisible = true)
	public void PlaySection(int start, int end) 
	{
			if (ScrollScreen)
			{
				Intent PlaySectionIntent = new Intent(VRActivity.VR_VIDEO_PLAYSECTION);
				PlaySectionIntent.putExtra("StartSection", start);
				PlaySectionIntent.putExtra("EndSection", end);
				LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(PlaySectionIntent);
				Log.d("GVR", "Lanza el VRActivity PlaySection: "+ScrollScreen);	
			}
			else
			{	
				/*Intent PlaySectionIntent = new Intent(VideoActivity.VR_VIDEO_PLAYSECTION);
				PlaySectionIntent.putExtra("StartSection", start);
				PlaySectionIntent.putExtra("EndSection", end);
				LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(PlaySectionIntent);*/
				Log.d("GVR", "Lanza el VideoActivity PlaySection: "+ScrollScreen);			
			}

		}	
	

	@SimpleFunction(description = "Stop video360", userVisible = true)
	public void Play() 
	{
		if (ScrollScreen)
		{
			Intent playIntent = new Intent(VRActivity.VR_VIDEO_PLAY);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(playIntent);
		}
		else
		{
			Intent playIntent = new Intent(VideoActivity.VR_VIDEO_PLAY);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(playIntent);
		}

	}

	@SimpleFunction(description = "Stop video360", userVisible = true)
	public void Pause() 
	{
		if (ScrollScreen)
		{
			Intent pauseIntent = new Intent(VRActivity.VR_VIDEO_PAUSE);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(pauseIntent);
		}
		else
		{
			Intent pauseIntent = new Intent(VideoActivity.VR_VIDEO_PAUSE);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(pauseIntent);			
		}

	}

	@SimpleFunction(description = "Stop video360", userVisible = true)
	public void Reset()
	{
		if (ScrollScreen)
		{
			Intent resetIntent = new Intent(VRActivity.VR_VIDEO_RESET);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(resetIntent);
		}
		else
		{		
			Intent resetIntent = new Intent(VideoActivity.VR_VIDEO_RESET);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(resetIntent);
		}
	}

	@SimpleFunction(description = "Seek to video360", userVisible = true)
	public void SeekTo(int position) 
	{
		if (ScrollScreen)
		{
			Intent seektoIntent = new Intent(VRActivity.VR_VIDEO_SEEKTO);
			seektoIntent.putExtra("SeektoPosition", position);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(seektoIntent);
			Log.d("GVR", "Lanza el VRActivity SeekTo: "+ScrollScreen);	
		}
		else
		{	
			Intent seektoIntent = new Intent(VideoActivity.VR_VIDEO_SEEKTO);
			seektoIntent.putExtra("SeektoPosition", position);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(seektoIntent);
			Log.d("GVR", "Lanza el VideoActivity SeekTo: "+ScrollScreen);			
		}

	}
//Edson Unificando fin 
	@SimpleEvent
	public void EndVideo() {
		Log.d("video360", "--BETA4");

		EventDispatcher.dispatchEvent(this, "EndVideo");
	}

	@SimpleEvent
	public void StartVideo(int videoDuration) {
		EventDispatcher.dispatchEvent(this, "StartVideo", videoDuration);
	}
}
