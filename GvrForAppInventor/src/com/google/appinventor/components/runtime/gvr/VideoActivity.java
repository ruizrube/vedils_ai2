/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.appinventor.components.runtime.gvr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Patterns;
import android.view.Gravity;
import android.view.ViewGroup;
import com.google.appinventor.components.runtime.gvr.rendering.Mesh;
import com.google.appinventor.components.runtime.vr4ai.util.Video360Parcelable;
import android.net.Uri;  
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.media.MediaPlayer;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.RelativeLayout;
import java.util.List;
import android.view.Window;
import android.view.WindowManager;
import com.google.appinventor.components.runtime.gvr.YouTubeExtractor;
import com.google.appinventor.components.runtime.gvr.YouTubeExtractor.YouTubeExtractorResult;

import android.widget.Toast;

/**
* Basic Activity to hold {@link MonoscopicView} and render a 360 video in 2D.
*
* Most of this Activity's code is related to Android & VR permission handling. The real work is in
* MonoscopicView.
*
* The default intent for this Activity will load a 360 placeholder panorama. For more options on
* how to load other media using a custom Intent, see {@link MediaLoader}.
*/

public class VideoActivity extends Activity implements YouTubeExtractor.YouTubeExtractorListener{
	
	private static final String TAG = "VideoActivity";
	private MonoscopicView videoView;	
	private Intent starterIntent;
	private YouTubeExtractor youTubeExtractor;
	private YouTubeExtractorResult result;
	public boolean conversionOk = false;
	public String urlToPlay = "";
	public String videoId;
	private List<Integer> preferredVideoQualities;  
	AlertDialog alert;
	public int video360Volume;
	public boolean stereoMode = false;
    public boolean isURL;
    public boolean isCompanion=false;
    public boolean isLoop;
    public String video360Quality;
    public String video360Path = "";
	private MediaLoader medialoader;
	private MediaPlayer mediaPlayer;
	private VideoUiView videoUi;
	private boolean primeraVez=true;
	Bundle extras;

	public static final String GVR_ACTIVITY_CLASS = "com.google.appinventor.components.runtime.gvr.VideoActivity";
	
	public static final String VR_STOP = GVR_ACTIVITY_CLASS + ".stop";
	public static final String VR_COMPANION = GVR_ACTIVITY_CLASS + ".isCompanion";
	
	//RECIBIDOS
	public static final String VR_VIDEO_SEEKTO = GVR_ACTIVITY_CLASS + ".seekToVideo";
	public static final String VR_VIDEO_STOP = GVR_ACTIVITY_CLASS + ".stopVideo";
	public static final String VR_VIDEO_PLAY = GVR_ACTIVITY_CLASS + ".playVideo";
	public static final String VR_VIDEO_PAUSE = GVR_ACTIVITY_CLASS + ".pauseVideo";
	public static final String VR_VIDEO_RESET = GVR_ACTIVITY_CLASS + ".resetVideo";
		
	//ENVIADOS
	public static final String VR_EVENT_VIDEO_END = GVR_ACTIVITY_CLASS + ".endVideo";
	public static final String VR_EVENT_VIDEO_START = GVR_ACTIVITY_CLASS + ".startVideo";	
		
	public static final String VR_EVENT_TOUCH_DOBLETAP = GVR_ACTIVITY_CLASS + ".VideoActivity.touchDobleTapScreen";
	public static final String VR_EVENT_TOUCH_SINGLETAP = GVR_ACTIVITY_CLASS + ".VideoActivity.touchSingleTapScreen";
	public static final String VR_EVENT_TOUCH_LONGPRESS = GVR_ACTIVITY_CLASS + ".VideoActivity.touchLongPressScreen";
	
	///////////////
	// RECEIVER //
	//////////////

	// GLOBAL
	private BroadcastReceiver stopBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
			Log.d("GVR", "Lanza el stopBroadCastReceiver");	
		}
	};
	

	// VIDEO
	private BroadcastReceiver seekToVideoEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int position = intent.getIntExtra("SeektoPosition", 0);
			videoView.mediaLoader.mediaPlayer.seekTo(position);
			Log.d("GVR", "Lanza el seekToVideoEventBroadCastReceiver");	
		}

	};
	
	private BroadcastReceiver stopVideoEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("GVR", "Lanza el stopVideoEventBroadCastReceiver");
			videoState("stop");
			
		}
	};
	
	private BroadcastReceiver playVideoEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			videoState("play");
		}
	};
	
	private BroadcastReceiver pauseVideoEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			videoState("pause");
		}
	};
	
	private BroadcastReceiver resetVideoEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			videoState("reset");
		}
	};  

	
	private void videoState(String state) {
		final String stateFinal = state;
				switch (stateFinal) {
				case "stop":
					videoView.mediaLoader.mediaPlayer.stop();
					Log.d("GVR", "En sotp de videoState");
					break;
				case "play":
					videoView.mediaLoader.mediaPlayer.start();
					Log.d("GVR", "En start de videoState");
					break;
				case "pause":
					videoView.mediaLoader.mediaPlayer.pause();
					Log.d("GVR", "En pause de videoState");
					break;
				case "reset":
					videoView.mediaLoader.mediaPlayer.reset();
					Log.d("GVR", "En reset de videoState");
					break;
				case "release":
					videoView.mediaLoader.mediaPlayer.release();					
					Log.d("GVR", "En release de videoState");
					;
					break;
				}
	}

 
  @Override
  public void onCreate(Bundle savedInstanceState) {	    
	  registerReceivers();	  
	  extras = getIntent().getExtras();
	  final Video360Parcelable video360Par = (Video360Parcelable)this.extras.getParcelable("Video360Object");
      this.video360Path = video360Par.getVideo360Path();
      this.isCompanion = this.extras.getBoolean("com.google.appinventor.components.runtime.gvr.VideoActivity.isCompanion");      
      if (video360Par.getIsURL() == 0)
          this.isURL = false;
      else 
          this.isURL = true;
      
      if (video360Par.getIsLoop() == 0)
          this.isLoop = false;
      else
          this.isLoop = true;
      this.video360Volume = video360Par.getVideo360Volumen();
      this.video360Quality = video360Par.getVideo360Quality();
      //this.stereoMode = this.extras.getBoolean("StereoMode");
      //this.isCompanion = this.extras.getBoolean("com.google.appinventor.components.runtime.gvr.VideoActivity.isCompanion");
      Log.d("GVR", "Path de video 360: "+this.video360Path);
    
    if (this.video360Path.contains("http")) 
    {
		if (!isValid(this.video360Path)) 
		{
			noValidURLWindow();
			alert.show();
		}		
	    if (this.video360Path.contains("youtube"))
	    {
	      if (this.video360Path != null && !this.video360Path.contentEquals("")) 
	      {
	        int startIndex = this.video360Path.indexOf("=");
	        videoId = this.video360Path.substring(startIndex + 1, this.video360Path.length());
	        Log.d("GVR", "video id: "+videoId);
	        youTubeExtractor = new YouTubeExtractor(videoId);
	        preferredVideoQualities = new ArrayList<Integer>();
	        preferredVideoQualities.add(setYoutubeQuality(video360Quality));
	        youTubeExtractor.setPreferredVideoQualities(preferredVideoQualities);
	        youTubeExtractor.startExtracting(this);
	      }
	    }
	    Log.d("GVR", "is a url valid: "+isValid(this.video360Path));
	} 
    else
    {
		//Sigue ya que sera video local.
        String uri = this.video360Path;
        Intent intent =
    	       new Intent (getIntent()).setClass(VideoActivity.this, VideoActivity.class);//VrVideoActivity.class);
    	intent.setAction(Intent.ACTION_VIEW);
    	intent.setData(Uri.parse(uri));
    	intent.putExtra(
    	       MediaLoader.MEDIA_FORMAT_KEY,
    	       getIntent().getIntExtra(MediaLoader.MEDIA_FORMAT_KEY, Mesh.MEDIA_MONOSCOPIC));
        videoView.loadMedia(intent,uri);	     
        Log.d("GVR", "URL sin yotube"+uri);    
    	starterIntent = getIntent();    	
	}
    
    
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
    super.onCreate(savedInstanceState);
    RelativeLayout viewLayout = new RelativeLayout(this);
    ViewGroup.LayoutParams uiParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    videoUi = new VideoUiView(this, null);
    videoView = new MonoscopicView(this, null);
    videoView.setLayoutParams(uiParams);
    viewLayout.setGravity(Gravity.TOP);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    setContentView(viewLayout);
    viewLayout.addView(videoView);
    videoView.initialize(videoUi, this);

  }

	private boolean isValid(String urlString) {
		try {
			URL url = new URL(urlString);
			return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches();
		} catch (MalformedURLException e) {}
		return false;
	}
	

	public void noValidURLWindow() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setMessage("No ha introducido una URL valida");
		builder1.setCancelable(true);
		builder1.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				onBackPressed();
			}
		});
		alert = builder1.create();
	}  
  
    
  @Override
  protected void onResume() {
    super.onResume();
    videoView.onResume();
    System.out.println("lentra resume");
	if(primeraVez)	
		primeraVez=false;
	else	
		registerReceivers();
	
  }

  long lastPress;
  Toast backpressToast;
   
  @Override
  public void onBackPressed() {
	this.unregisterReceivers();
	Log.d("GVR", "Entra en onBackRessed");
    long currentTime = System.currentTimeMillis();
    if(currentTime - lastPress > 5000){
        backpressToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_LONG);
        backpressToast.show();
        lastPress = currentTime;
    } else {
        if (backpressToast != null) backpressToast.cancel();
        super.onBackPressed();
    }	
  }
  
  
    @Override
  protected void onPause() {
    this.unregisterReceivers();
    videoView.onPause();
    super.onPause();    
	Log.d("GVR", "Entra en onPause");
  }

    
  @Override
  protected void onDestroy() {
  	this.unregisterReceivers();
  	videoView.destroy();  
    super.onDestroy();
    Log.d("GVR", "Entra en onDestroy");
  }    
    
  
  private Integer setYoutubeQuality(String video360Quality) {
	  Log.d("GVR", "Video Quyality: "+ video360Quality);
    switch(video360Quality)
    {
      case "240":
        return YouTubeExtractor.YOUTUBE_VIDEO_QUALITY_SMALL_240;
      case "360":
        return YouTubeExtractor.YOUTUBE_VIDEO_QUALITY_MEDIUM_360;
      case "720":
        return YouTubeExtractor.YOUTUBE_VIDEO_QUALITY_HD_720;
      case "1080":
        return YouTubeExtractor.YOUTUBE_VIDEO_QUALITY_HD_1080;
    }
    return null;
    
  }


  @Override
  public void onSuccess(YouTubeExtractorResult result) {
    String uri = "file:///sdcard/video.mp4";
    Intent intent =
	       new Intent (getIntent()).setClass(VideoActivity.this, VideoActivity.class);
	intent.setAction(Intent.ACTION_VIEW);
	intent.setData(Uri.parse(uri));
	intent.putExtra(
	       MediaLoader.MEDIA_FORMAT_KEY,
	       getIntent().getIntExtra(MediaLoader.MEDIA_FORMAT_KEY, Mesh.MEDIA_MONOSCOPIC));
    videoView.loadMedia(intent,result.getVideoUri().toString());//getIntent());	     
    Log.d("GVR", "URL convertida"+result.getVideoUri().toString());    
	starterIntent = getIntent();     

  }

  
  @Override
  public void onFailure(Error error) {
    Log.e("OnFailure", error.getMessage());
    onBackPressed();
    // TODO Auto-generated method stub
  }

  
	public void registerReceivers()
	{
	    LocalBroadcastManager.getInstance(this).registerReceiver(stopBroadCastReceiver,
				new IntentFilter(VideoActivity.VR_STOP));
		
		LocalBroadcastManager.getInstance(this).registerReceiver(stopVideoEventBroadCastReceiver,
				new IntentFilter(VR_VIDEO_STOP));
		LocalBroadcastManager.getInstance(this).registerReceiver(playVideoEventBroadCastReceiver,
				new IntentFilter(VR_VIDEO_PLAY));
		LocalBroadcastManager.getInstance(this).registerReceiver(pauseVideoEventBroadCastReceiver,
				new IntentFilter(VR_VIDEO_PAUSE));
		LocalBroadcastManager.getInstance(this).registerReceiver(resetVideoEventBroadCastReceiver,
				new IntentFilter(VR_VIDEO_RESET));
		LocalBroadcastManager.getInstance(this).registerReceiver(seekToVideoEventBroadCastReceiver,
				new IntentFilter(VR_VIDEO_SEEKTO));
	}


	public void unregisterReceivers()
	{		
		LocalBroadcastManager.getInstance(this).unregisterReceiver(this.stopBroadCastReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(this.stopVideoEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(this.playVideoEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(this.pauseVideoEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(this.resetVideoEventBroadCastReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(this.seekToVideoEventBroadCastReceiver);
	}

	
    public InputStream openAsset(final String name) throws IOException {
        Log.v("COMPANION", new StringBuilder(String.valueOf(this.isCompanion)).toString());
        if (this.isCompanion) {
            final InputStream is = new FileInputStream("/sdcard/AppInventor/assets/" + name);
            return is;
        }
        return this.getAssets().open(name);
    }

  
}
