// 
// Decompiled by Procyon v0.5.30
// 

package com.google.appinventor.components.runtime.vr4ai;

import java.io.FileInputStream;
import java.io.InputStream;
import android.content.IntentFilter;
import com.google.appinventor.components.runtime.vr4ai.util.Video360Parcelable;
import com.threed.jpct.TextureManager;
import android.view.KeyEvent;
import android.view.InputDevice;
import com.threed.jpct.Config;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;

import java.io.IOException;
import android.util.Log;
import android.view.MotionEvent;
import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import com.google.appinventor.components.runtime.vr4ai.util.ShakeListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.os.Bundle;
import android.os.Handler;

import com.threed.jpct.Texture;
import android.media.MediaPlayer;
import com.google.appinventor.components.runtime.vr4ai.util.Object3DParcelable;
import java.util.ArrayList;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.appinventor.components.runtime.vr4ai.view.CardboardOverlayView;
import com.google.vrtoolkit.cardboard.CardboardActivity;

public class VRActivity extends CardboardActivity
{
    private CardboardOverlayView mOverlayView;
    private CardboardView cardboardView;
    String contentToRender;
    public float rotateSpeed;
    public float moveSpeed;
    public ArrayList<Object3DParcelable> object3DListAi2;
    public String model3DPath;
    public String material3DPath;
    public String image360Path;
    public String video360Path;
    public String skyboxPath;
    public MediaPlayer mediaPlayer;
    public Texture externalTexture;
    public VRVideo360RenderScene rendererVideo360;
    VR3DObjectRenderScene renderer3DObject;
    private int media_length;
    public boolean object3D;
    public boolean image360;
    public boolean video360;
    public boolean hasController;
    Bundle extras;
    public boolean isURL;
    public boolean isLoop;
    public int video360Volume;
    public boolean stereoMode;
    public String video360Quality;
    public int positionX;
    public int positionY;
    public int positionZ;
    public int scale;
    private float lastX;
    private float lastY;
    public boolean joystickRotateAction;
    private boolean primeraVez;
    public boolean isCompanion;
    GestureDetector gestureDetector;
    VRActivity mActivity;
    public static final String VR_ACTIVITY_CLASS = "com.google.appinventor.components.runtime.vr4ai.VRActivity";
    public static final String VR_COMPANION = "com.google.appinventor.components.runtime.vr4ai.VRActivity.isCompanion";
    public static final String VR_VIDEO_SEEKTO = "com.google.appinventor.components.runtime.vr4ai.VRActivity.seekToVideo";
    //Edson inicio
    public static final String VR_VIDEO_PLAYSECTION = "com.google.appinventor.components.runtime.vr4ai.VRActivity.playSection";
    //Edson fin 
    public static final String VR_VIDEO_STOP = "com.google.appinventor.components.runtime.vr4ai.VRActivity.stopVideo";
    public static final String VR_VIDEO_PLAY = "com.google.appinventor.components.runtime.vr4ai.VRActivity.playVideo";
    public static final String VR_VIDEO_PAUSE = "com.google.appinventor.components.runtime.vr4ai.VRActivity.pauseVideo";
    public static final String VR_VIDEO_RESET = "com.google.appinventor.components.runtime.vr4ai.VRActivity.resetVideo";
    public static final String VR_STOP = "com.google.appinventor.components.runtime.vr4ai.VRActivity.stop";
    public static final String VR_MOVEFOCUS_NEXT = "com.google.appinventor.components.runtime.vr4ai.VRActivity.moveFocusNext";
    public static final String VR_MOVEFOCUS_PREVIOUS = "com.google.appinventor.components.runtime.vr4ai.VRActivity.moveFocusPrevious";
    public static final String VR_3DOBJECT_RESET = "com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectReset";
    public static final String VR_3DOBJECT_ROTATE_LEFT = "com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectRotateLeft";
    public static final String VR_3DOBJECT_ROTATE_RIGHT = "com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectRotateRight";
    public static final String VR_3DOBJECT_ROTATE_UP = "com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectRotateUp";
    public static final String VR_3DOBJECT_ROTATE_DOWN = "com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectRotateDown";
    public static final String VR_3DOBJECT_ZOOM_IN = "com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectRotateIn";
    public static final String VR_3DOBJECT_ZOOM_OUT = "com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectRotateOut";
    public static final String VR_3DOBJECT_MOVE_DOWN = "com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectMoveDown";
    public static final String VR_3DOBJECT_MOVE_UP = "com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectMoveUp";
    public static final String VR_3DOBJECT_MOVE_LEFT = "com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectMoveLeft";
    public static final String VR_3DOBJECT_MOVE_RIGHT = "com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectMoveRight";
    public static final String VR_3DOBJECT_SCALE_IN = "com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectScaleIn";
    public static final String VR_3DOBJECT_SCALE_DE = "com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectScaleDe";
    public static final String VR_3DOBJECT_SETFOCUS = "com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectSetFocus";
    public static final String VR_EVENT_VIDEO_END = "com.google.appinventor.components.runtime.vr4ai.VRActivity.endVideo";
    public static final String VR_EVENT_VIDEO_START = "com.google.appinventor.components.runtime.vr4ai.VRActivity.startVideo";
    public static final String VR_EVENT_SHAKE = "com.google.appinventor.components.runtime.vr4ai.VRActivity.shake";
    public static final String VR_EVENT_PRESS_A_BUTTON = "com.google.appinventor.components.runtime.vr4ai.VRActivity.pressA";
    public static final String VR_EVENT_PRESS_B_BUTTON = "com.google.appinventor.components.runtime.vr4ai.VRActivity.pressB";
    public static final String VR_EVENT_PRESS_C_BUTTON = "com.google.appinventor.components.runtime.vr4ai.VRActivity.pressC";
    public static final String VR_EVENT_PRESS_D_BUTTON = "com.google.appinventor.components.runtime.vr4ai.VRActivity.pressD";
    public static final String VR_EVENT_PRESS_X_BUTTON = "com.google.appinventor.components.runtime.vr4ai.VRActivity.pressX";
    public static final String VR_EVENT_PRESS_Z_BUTTON = "com.google.appinventor.components.runtime.vr4ai.VRActivity.pressZ";
    public static final String VR_EVENT_PRESS_UP_JOYSTICK = "com.google.appinventor.components.runtime.vr4ai.VRActivity.pressUpJoystick";
    public static final String VR_EVENT_PRESS_DOWN_JOYSTICK = "com.google.appinventor.components.runtime.vr4ai.VRActivity.pressDownJoystick";
    public static final String VR_EVENT_PRESS_LEFT_JOYSTICK = "com.google.appinventor.components.runtime.vr4ai.VRActivity.pressLeftJoystick";
    public static final String VR_EVENT_PRESS_RIGHT_JOYSTICK = "com.google.appinventor.components.runtime.vr4ai.VRActivity.pressRightJoystick";
    public static final String VR_EVENT_PRESS_UP_LEFT_JOYSTICK = "com.google.appinventor.components.runtime.vr4ai.VRActivity.pressUpLeftJoystick";
    public static final String VR_EVENT_PRESS_UP_RIGHT_JOYSTICK = "com.google.appinventor.components.runtime.vr4ai.VRActivity.pressUpRightJoystick";
    public static final String VR_EVENT_PRESS_DOWN_LEFT_JOYSTICK = "com.google.appinventor.components.runtime.vr4ai.VRActivity.pressDownLeftJoystick";
    public static final String VR_EVENT_PRESS_DOWN_RIGHT_JOYSTICK = "com.google.appinventor.components.runtime.vr4ai.VRActivity.pressDownRightJoystick";
    public static final String VR_EVENT_TOUCH_SINGLETAP = "com.google.appinventor.components.runtime.vr4ai.VRActivity.touchSingleTapScreen";
    public static final String VR_EVENT_TOUCH_DOBLETAP = "com.google.appinventor.components.runtime.vr4ai.VRActivity.touchDobleTapScreen";
    public static final String VR_EVENT_TOUCH_LONGPRESS = "com.google.appinventor.components.runtime.vr4ai.VRActivity.touchLongPressScreen";
    private ShakeListener mShaker;
    private BroadcastReceiver stopBroadCastReceiver;
    private BroadcastReceiver seekToVideoEventBroadCastReceiver;
    //Edson inicio
    private BroadcastReceiver PlaySectionBroadCastReceiver;
    //Edson fin 
    private BroadcastReceiver stopVideoEventBroadCastReceiver;
    private BroadcastReceiver playVideoEventBroadCastReceiver;
    private BroadcastReceiver pauseVideoEventBroadCastReceiver;
    private BroadcastReceiver resetVideoEventBroadCastReceiver;
    private BroadcastReceiver reset3DObjectEventBroadCastReceiver;
    private BroadcastReceiver rotateLeft3DObjectEventBroadCastReceiver;
    private BroadcastReceiver rotateRight3DObjectEventBroadCastReceiver;
    private BroadcastReceiver rotateUp3DObjectEventBroadCastReceiver;
    private BroadcastReceiver rotateDown3DObjectEventBroadCastReceiver;
    private BroadcastReceiver scaleIncre3DObjectEventBroadCastReceiver;
    private BroadcastReceiver scaleDecre3DObjectEventBroadCastReceiver;
    private BroadcastReceiver zoomIn3DObjectEventBroadCastReceiver;
    private BroadcastReceiver zoomOut3DObjectEventBroadCastReceiver;
    private BroadcastReceiver moveLeft3DObjectEventBroadCastReceiver;
    private BroadcastReceiver moveRight3DObjectEventBroadCastReceiver;
    private BroadcastReceiver moveUp3DObjectEventBroadCastReceiver;
    private BroadcastReceiver moveDown3DObjectEventBroadCastReceiver;
    private BroadcastReceiver moveFocusNextEventBroadCastReceiver;
    private BroadcastReceiver moveFocusPreviousEventBroadCastReceiver;
    private BroadcastReceiver setFocusObjectEventBroadCastReceiver;
    
    public VRActivity() {
        this.rendererVideo360 = null;
        this.renderer3DObject = null;
        this.object3D = false;
        this.image360 = false;
        this.video360 = false;
        this.hasController = false;
        this.stereoMode = false;
        this.joystickRotateAction = false;
        this.primeraVez = true;
        this.isCompanion = false;
        this.mActivity = this;
        this.stopBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                VRActivity.this.finish();
            }
        };
        this.seekToVideoEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final int position = intent.getIntExtra("SeektoPosition", 0);
                VRActivity.this.rendererVideo360.mediaPlayer.seekTo(position);                
            }
        };
        
        //Edson inicio
        this.PlaySectionBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
            	final int startposition = intent.getIntExtra("StartSection", 0);
            	final int endposition = intent.getIntExtra("EndSection", 0);
            	VRActivity.this.rendererVideo360.mediaPlayer.seekTo(startposition);
            	VRActivity.this.rendererVideo360.mediaPlayer.start();
            	
            	Log.d("Handler", "antes del Running Handler");
            	
            	final Handler handler = new Handler();
            	
            	 
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Write whatever to want to do after delay specified (1 sec)
                        //Log.d("Handler", "Running Handler getCurrentPosition: "+VRActivity.this.rendererVideo360.mediaPlayer.getCurrentPosition());
            	
            	        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            	boolean interrupted = true;
                            	while(interrupted){
                            	Log.d("Handler", "Running Handler getCurrentPosition: "+VRActivity.this.rendererVideo360.mediaPlayer.getCurrentPosition());
                    	        	if (endposition < VRActivity.this.rendererVideo360.mediaPlayer.getCurrentPosition())
	                    	        {
	                    	        	VRActivity.this.rendererVideo360.mediaPlayer.pause();                    	        	
	                    	        	handler.removeCallbacksAndMessages(this);
	       	                            final Intent intentVideoEnd = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.endVideo");
	    	                            LocalBroadcastManager.getInstance(VRActivity.this).sendBroadcast(intentVideoEnd);
	    	                            Log.v("MEDIACOMPLETADO", "Running Handler video acabado");	
	    	                            interrupted=false;
	                    	        }
                            	}//while
                    	        	
                            }
                        });        
            	        /*if (endposition > VRActivity.this.rendererVideo360.mediaPlayer.getCurrentPosition())
            	        	handler.postDelayed(this, 500);  	      
            	        else
            	        	handler.removeCallbacksAndMessages(null);*/
                    }                    
                }, 500);            	            	
            }
        };           
        //Edson fin
        
        /*
         *                                 boolean interrupted= true;
                            	while (interrupted)
                            	{
                            		Log.d("Handler", "Running Handler getCurrentPosition: "+VRActivity.this.rendererVideo360.mediaPlayer.getCurrentPosition());
        	                    	
        	            	        if (endposition < VRActivity.this.rendererVideo360.mediaPlayer.getCurrentPosition())
        	            	        {
        	            	        	VRActivity.this.rendererVideo360.mediaPlayer.pause();                    	        	
        	            	        	handler.removeCallbacksAndMessages(this);
        	                            final Intent intentVideoEnd = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.endVideo");
        	                            LocalBroadcastManager.getInstance(VRActivity.this).sendBroadcast(intentVideoEnd);
        	                            Log.v("MEDIACOMPLETADO", "Running Handlervideo acabado");	            	        	
        	            	        	interrupted= false;	            	        	
        	            	        }                      
        	            	        else	            	        	
        	            	        	handler.postDelayed(this, 500); 
         */
        
        
        
        
        this.stopVideoEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                VRActivity.this.videoState("stop");
            }
        };
        this.playVideoEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                VRActivity.this.videoState("play");
            }
        };
        this.pauseVideoEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                VRActivity.this.videoState("pause");
            }
        };
        this.resetVideoEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                VRActivity.this.videoState("reset");
            }
        };
        this.reset3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String UUID = intent.getStringExtra("id");
                VRActivity.this.Object3DState("reset", UUID);
            }
        };
        this.rotateLeft3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String UUID = intent.getStringExtra("id");
                VRActivity.this.Object3DState("rotateLeft", UUID);
            }
        };
        this.rotateRight3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String UUID = intent.getStringExtra("id");
                VRActivity.this.Object3DState("rotateRight", UUID);
            }
        };
        this.rotateUp3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String UUID = intent.getStringExtra("id");
                VRActivity.this.Object3DState("rotateUp", UUID);
            }
        };
        this.rotateDown3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String UUID = intent.getStringExtra("id");
                VRActivity.this.Object3DState("rotateDown", UUID);
            }
        };
        this.scaleIncre3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String UUID = intent.getStringExtra("id");
                VRActivity.this.Object3DState("scaleIncre", UUID);
            }
        };
        this.scaleDecre3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String UUID = intent.getStringExtra("id");
                VRActivity.this.Object3DState("scaleDecre", UUID);
            }
        };
        this.zoomIn3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String UUID = intent.getStringExtra("id");
                VRActivity.this.Object3DState("zoomIn", UUID);
            }
        };
        this.zoomOut3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String UUID = intent.getStringExtra("id");
                VRActivity.this.Object3DState("zoomOut", UUID);
            }
        };
        this.moveLeft3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String UUID = intent.getStringExtra("id");
                VRActivity.this.Object3DState("moveLeft", UUID);
            }
        };
        this.moveRight3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String UUID = intent.getStringExtra("id");
                VRActivity.this.Object3DState("moveRight", UUID);
            }
        };
        this.moveUp3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String UUID = intent.getStringExtra("id");
                VRActivity.this.Object3DState("moveUp", UUID);
            }
        };
        this.moveDown3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String UUID = intent.getStringExtra("id");
                VRActivity.this.Object3DState("moveDown", UUID);
            }
        };
        this.moveFocusNextEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                VRActivity.this.renderer3DObject.moveFocusNextObject();
            }
        };
        this.moveFocusPreviousEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                VRActivity.this.renderer3DObject.moveFocusPreviousObject();
            }
        };
        this.setFocusObjectEventBroadCastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String UUID = intent.getStringExtra("id");
                VRActivity.this.renderer3DObject.setFocusObject(UUID);
            }
        };
    }
    
    private void videoState(final String state) {
        this.cardboardView.queueEvent((Runnable)new Runnable() {
            @Override
            public void run() {
                final String val$stateFinal;
                switch (val$stateFinal = state) {
                    case "play": {
                        VRActivity.this.rendererVideo360.mediaPlayer.start();
                        break;
                    }
                    case "stop": {
                        VRActivity.this.rendererVideo360.mediaPlayer.stop();
                        break;
                    }
                    case "pause": {
                        VRActivity.this.rendererVideo360.mediaPlayer.pause();
                        break;
                    }
                    case "reset": {
                        VRActivity.this.rendererVideo360.mediaPlayer.reset();
                        break;
                    }
                    case "release": {
                        VRActivity.this.rendererVideo360.mediaPlayer.release();
                        break;
                    }
                    default:
                        break;
                }
            }
        });
    }
    
    private void Object3DState(final String state, final String uuid) {
        this.cardboardView.queueEvent((Runnable)new Runnable() {
            @Override
            public void run() {
                final String val$stateFinal;
                switch (val$stateFinal = state) {
                    case "moveUp": {
                        VRActivity.this.renderer3DObject.move3DObject("up", uuid);
                        break;
                    }
                    case "zoomIn": {
                        VRActivity.this.renderer3DObject.move3DObject("zoomIn", uuid);
                        break;
                    }
                    case "zoomOut": {
                        VRActivity.this.renderer3DObject.move3DObject("zoomOut", uuid);
                        break;
                    }
                    case "moveDown": {
                        VRActivity.this.renderer3DObject.move3DObject("down", uuid);
                        break;
                    }
                    case "moveLeft": {
                        VRActivity.this.renderer3DObject.move3DObject("left", uuid);
                        break;
                    }
                    case "rotateDown": {
                        VRActivity.this.renderer3DObject.rotate3DObject("down", uuid);
                        break;
                    }
                    case "rotateLeft": {
                        VRActivity.this.renderer3DObject.rotate3DObject("left", uuid);
                        break;
                    }
                    case "rotateUp": {
                        VRActivity.this.renderer3DObject.rotate3DObject("up", uuid);
                        break;
                    }
                    case "reset": {
                        VRActivity.this.renderer3DObject.reset3DPosition(uuid);
                        break;
                    }
                    case "moveRight": {
                        VRActivity.this.renderer3DObject.move3DObject("right", uuid);
                        break;
                    }
                    case "rotateRight": {
                        VRActivity.this.renderer3DObject.rotate3DObject("right", uuid);
                        break;
                    }
                    case "scaleDecre": {
                        VRActivity.this.renderer3DObject.scale3DObject("decrease", uuid);
                        break;
                    }
                    case "scaleIncre": {
                        VRActivity.this.renderer3DObject.scale3DObject("increase", uuid);
                        break;
                    }
                    default:
                        break;
                }
            }
        });
    }
    
    public void onCreate(final Bundle savedInstanceState) {
        (this.mShaker = new ShakeListener((Context)this)).setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                final Intent intent2 = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.shake");
                LocalBroadcastManager.getInstance(VRActivity.this.mShaker.mContext).sendBroadcast(intent2);
            }
        });
        this.gestureDetector = new GestureDetector((Context)this, (GestureDetector.OnGestureListener)new GestureDetector.SimpleOnGestureListener() {
            public boolean onDoubleTap(final MotionEvent e) {
                final Intent intentTouchDoubleTap = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.touchDobleTapScreen");
                LocalBroadcastManager.getInstance((Context)VRActivity.this.mActivity).sendBroadcast(intentTouchDoubleTap);
                Log.v("ON-DOBLETAP", "DOBLETAP");
                return false;
            }
            
            public boolean onSingleTapConfirmed(final MotionEvent e) {
                final Intent intentTouchSingleTap = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.touchSingleTapScreen");
                LocalBroadcastManager.getInstance((Context)VRActivity.this.mActivity).sendBroadcast(intentTouchSingleTap);
                Log.v("ON-SINGLETAP", "SINGLETAP");
                return false;
            }
            
            public void onLongPress(final MotionEvent e) {
                final Intent intentTouchLongPress = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.touchLongPressScreen");
                LocalBroadcastManager.getInstance((Context)VRActivity.this.mActivity).sendBroadcast(intentTouchLongPress);
                Log.v("ON-LONGPRESS", "LOGNPRESS");
            }
        });
        super.onCreate(savedInstanceState);
        try {
            this.listAssets();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        final RelativeLayout rl = new RelativeLayout((Context)this);
        this.setContentView((View)rl);
        Log.v("CONTEXT", "relative creado");
        rl.addView((View)(this.cardboardView = new CardboardView((Context)this)));
        Log.v("CONTEXT", "cardboardView creado");
        this.setCardboardView(this.cardboardView);
        this.mOverlayView = new CardboardOverlayView((Context)this, null);
        Config.glTransparencyOffset = 0.0f;
        Config.glTransparencyMul = 0.01f;
        this.extras = this.getIntent().getExtras();
        this.object3D = this.extras.getBoolean("Object3D");
        this.image360 = this.extras.getBoolean("Image360");
        this.video360 = this.extras.getBoolean("Video360");
        this.stereoMode = this.extras.getBoolean("StereoMode");
        this.hasController = this.extras.getBoolean("Controller");
        this.isCompanion = this.extras.getBoolean("com.google.appinventor.components.runtime.vr4ai.VRActivity.isCompanion");
        Log.v("COMPANION", String.valueOf(this.isCompanion) + "estado companion");
        if (!this.stereoMode) {
            this.cardboardView.setVRModeEnabled(false);
        }
        try {
            this.renderFilter();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    private void processJoystickInput(final MotionEvent event, final int historyPos) {
        final InputDevice mInputDevice = event.getDevice();
        float x = getCenteredAxis(event, mInputDevice, 0, historyPos);
        if (x == 0.0f) {
            x = getCenteredAxis(event, mInputDevice, 15, historyPos);
        }
        if (x == 0.0f) {
            x = getCenteredAxis(event, mInputDevice, 11, historyPos);
        }
        float y = getCenteredAxis(event, mInputDevice, 1, historyPos);
        if (y == 0.0f) {
            y = getCenteredAxis(event, mInputDevice, 16, historyPos);
        }
        if (y == 0.0f) {
            y = getCenteredAxis(event, mInputDevice, 14, historyPos);
        }
        Log.v("EJE_X", new StringBuilder(String.valueOf(x)).toString());
        Log.v("EJE_Y", new StringBuilder(String.valueOf(y)).toString());
        this.lastX = x;
        this.lastY = y;
        if (this.lastX == 0.0f && this.lastY != 0.0f) {
            if (this.lastY > 0.0f) {
                final Intent intentJoystickUp = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.pressUpJoystick");
                LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intentJoystickUp);
            }
            else {
                final Intent intentJoystickDown = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.pressDownJoystick");
                LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intentJoystickDown);
            }
        }
        else if (this.lastX != 0.0f && this.lastY == 0.0f) {
            if (this.lastX > 0.0f) {
                final Intent intentJoystickRight = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.pressRightJoystick");
                LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intentJoystickRight);
            }
            else {
                final Intent intentJoystickLeft = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.pressLeftJoystick");
                LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intentJoystickLeft);
            }
        }
        else if (this.lastX != 0.0f && this.lastY != 0.0f) {
            if (this.lastX < 0.0f && this.lastY > 0.0f) {
                final Intent intentJoystickUpLeft = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.pressUpLeftJoystick");
                LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intentJoystickUpLeft);
            }
            else if (this.lastX < 0.0f && this.lastY < 0.0f) {
                final Intent intentJoystickDownLeft = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.pressDownLeftJoystick");
                LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intentJoystickDownLeft);
            }
            else if (this.lastX > 0.0f && this.lastY > 0.0f) {
                final Intent intentJoystickUpRight = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.pressUpRightJoystick");
                LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intentJoystickUpRight);
            }
            else if (this.lastX > 0.0f && this.lastY < 0.0f) {
                final Intent intentJoystickDownRight = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.pressDownRightJoystick");
                LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intentJoystickDownRight);
            }
        }
    }
    
    private static float getCenteredAxis(final MotionEvent event, final InputDevice device, final int axis, final int historyPos) {
        final InputDevice.MotionRange range = device.getMotionRange(axis, event.getSource());
        if (range != null) {
            final float flat = range.getFlat();
            final float value = (historyPos < 0) ? event.getAxisValue(axis) : event.getHistoricalAxisValue(axis, historyPos);
            if (Math.abs(value) > flat) {
                return value;
            }
        }
        return 0.0f;
    }
    
    public boolean onGenericMotionEvent(final MotionEvent event) {
        Log.v("CODIGO DISPOSITIVO", "16777232");
        Log.v("DISPOSITIVO", new StringBuilder(String.valueOf(event.getSource())).toString());
        Log.v("ACTION", new StringBuilder(String.valueOf(event.getAction())).toString());
        if (this.hasController && (event.getSource() & 0x1000010) == 0x1000010 && event.getAction() == 2) {
            for (int historySize = event.getHistorySize(), i = 0; i < historySize; ++i) {
                this.processJoystickInput(event, i);
            }
            this.processJoystickInput(event, -1);
            return true;
        }
        return super.onGenericMotionEvent(event);
    }
    
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        Log.v("KEYEVENT", new StringBuilder(String.valueOf(keyCode)).toString());
        if (this.hasController) {
            if (keyCode == 4) {
                Log.v("BOTONPULSADO", "D");
                final Intent intentKeyD = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.pressD");
                LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intentKeyD);
                return true;
            }
            if (keyCode == 23) {
                Log.v("BOTONPULSADO", "B");
                final Intent intentKeyB = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.pressB");
                LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intentKeyB);
                return true;
            }
            if (keyCode == 67) {
                Log.v("BOTONPULSADO", "C");
                final Intent intentKeyC = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.pressC");
                LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intentKeyC);
                return true;
            }
            if (keyCode == 62) {
                Log.v("BOTONPULSADO", "A");
                final Intent intentKeyA = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.pressA");
                LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intentKeyA);
                return true;
            }
            if (keyCode == 102) {
                Log.v("BOTONPULSADO", "Z");
                final Intent intentKeyZ = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.pressZ");
                LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intentKeyZ);
                return true;
            }
            if (keyCode == 103) {
                Log.v("BOTONPULSADO", "X");
                final Intent intentKeyX = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.pressX");
                LocalBroadcastManager.getInstance((Context)this).sendBroadcast(intentKeyX);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
    
    public boolean onTouchEvent(final MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    
    public void onCardboardTrigger() {
        Log.i("EVENTO", "onCardboardTrigger");
    }
    
    private void renderFilter() throws IOException {
        TextureManager.getInstance().flush();
        if (this.object3D) {
            this.getExtraIntent();
            this.renderer3DObject = new VR3DObjectRenderScene(this);
            this.cardboardView.setRenderer((CardboardView.StereoRenderer)this.renderer3DObject);
        }
        if (this.image360) {
            this.getExtraIntent();
            final VRImage360RenderScene rendererImage360 = new VRImage360RenderScene(this);
            this.cardboardView.setRenderer((CardboardView.StereoRenderer)rendererImage360);
        }
        if (this.video360) {
            this.getExtraIntent();
            this.rendererVideo360 = new VRVideo360RenderScene(this);
            this.cardboardView.setRenderer((CardboardView.StereoRenderer)this.rendererVideo360);
        }
    }
    
    public void getExtraIntent() {
        if (this.object3D) {
        	 this.skyboxPath = this.extras.getString("SkyboxPath");
             this.rotateSpeed = this.extras.getFloat("RotateSpeed");
             this.moveSpeed = this.extras.getFloat("MoveSpeed");
             this.object3DListAi2 = getIntent().getParcelableArrayListExtra("Object3DList");
             registerReceivers();
        }
        if (this.image360) {
            Log.v("IMAGE360", this.image360Path = this.extras.getString("Image360Path"));
            this.registerReceivers();
        }
        if (this.video360) {
            final Video360Parcelable video360Par = (Video360Parcelable)this.extras.getParcelable("Video360Object");
            this.video360Path = video360Par.getVideo360Path();
            if (video360Par.getIsURL() == 0) {
                this.isURL = false;
            }
            else {
                this.isURL = true;
            }
            if (video360Par.getIsLoop() == 0) {
                this.isLoop = false;
            }
            else {
                this.isLoop = true;
            }
            this.video360Volume = video360Par.getVideo360Volumen();
            this.video360Quality = video360Par.getVideo360Quality();
            Log.v("VIDEO360", this.video360Path);
            this.registerReceivers();
        }
    }
    
    public void listAssets() throws IOException {
        final String[] listOfFiless = this.getApplicationContext().getAssets().list("");
        for (int i = 0; i < listOfFiless.length; ++i) {
            Log.d("ASSETS", listOfFiless[i]);
        }
    }
    
    @Override
    protected void onPause() {
        this.unregisterReceivers();
        super.onPause();
    }
    
    @Override
    protected void onDestroy() {
        this.unregisterReceivers();
        super.onDestroy();
        if (this.video360) {
            this.videoState("release");
        }
    }
    
    @Override
    protected void onResume() {
        Log.v("ONRESUME", "HELLO");
        super.onResume();
        if (this.primeraVez) {
            this.primeraVez = false;
        }
        else {
            this.registerReceivers();
        }
    }
    
    @Override
    public void onBackPressed() {
        this.unregisterReceivers();
        super.onBackPressed();
    }
    
    
    public void registerReceivers() {
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.stopBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.stop"));
        if (this.object3D) {
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.setFocusObjectEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectSetFocus"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.moveFocusNextEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.moveFocusNext"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.moveFocusPreviousEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.moveFocusPrevious"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.reset3DObjectEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectReset"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.rotateLeft3DObjectEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectRotateLeft"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.rotateRight3DObjectEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectRotateRight"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.rotateUp3DObjectEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectRotateUp"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.rotateDown3DObjectEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectRotateDown"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.zoomIn3DObjectEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectRotateIn"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.zoomOut3DObjectEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectRotateOut"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.scaleDecre3DObjectEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectScaleDe"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.scaleIncre3DObjectEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectScaleIn"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.moveUp3DObjectEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectMoveUp"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.moveDown3DObjectEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectMoveDown"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.moveLeft3DObjectEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectMoveLeft"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.moveRight3DObjectEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.3dObjectMoveRight"));
        }
        else if (this.video360) {
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.stopVideoEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.stopVideo"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.playVideoEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.playVideo"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.pauseVideoEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.pauseVideo"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.resetVideoEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.resetVideo"));
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.seekToVideoEventBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.seekToVideo"));
           //Edson
            LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.PlaySectionBroadCastReceiver, new IntentFilter("com.google.appinventor.components.runtime.vr4ai.VRActivity.playSection"));
        }
    }
    
    public void unregisterReceivers() {
        LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.stopBroadCastReceiver);
        if (this.video360) {
            this.videoState("pause");
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.stopVideoEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.playVideoEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.pauseVideoEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.resetVideoEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.seekToVideoEventBroadCastReceiver);
            //Edson
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.PlaySectionBroadCastReceiver);
        }
        if (this.object3D) {
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.reset3DObjectEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.rotateLeft3DObjectEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.rotateRight3DObjectEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.rotateUp3DObjectEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.rotateDown3DObjectEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.zoomIn3DObjectEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.zoomOut3DObjectEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.scaleDecre3DObjectEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.scaleIncre3DObjectEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.moveUp3DObjectEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.moveDown3DObjectEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.moveLeft3DObjectEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.moveRight3DObjectEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.moveFocusNextEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.moveFocusPreviousEventBroadCastReceiver);
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.setFocusObjectEventBroadCastReceiver);
        }
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
