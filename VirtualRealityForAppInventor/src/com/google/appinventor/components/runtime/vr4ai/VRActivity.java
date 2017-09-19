package com.google.appinventor.components.runtime.vr4ai;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.CardboardActivity.VolumeKeys;
import com.threed.jpct.Config;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.util.AAConfigChooser;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Display;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.google.appinventor.components.runtime.vr4ai.view.CardboardOverlayView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.appinventor.components.runtime.vr4ai.VR3DObjectRenderScene;
import com.google.appinventor.components.runtime.vr4ai.util.Object3DParcelable;
import com.google.appinventor.components.runtime.vr4ai.util.ShakeListener;
import com.google.appinventor.components.runtime.vr4ai.util.YouTubeExtractor;
import com.google.appinventor.components.runtime.vr4ai.util.YouTubeExtractor.YouTubeExtractorResult;

public class VRActivity extends CardboardActivity {

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
	public VRVideo360RenderScene rendererVideo360 = null;
	VR3DObjectRenderScene renderer3DObject = null;
	private int media_length;
	public boolean object3D = false;
	public boolean image360 = false;
	public boolean video360 = false;
	public boolean hasController = false;
	Bundle extras;
	public boolean isURL;
	public boolean isLoop;
	public int video360Volume;
	public boolean stereoMode = false;
	public String video360Quality;
	public int positionX;
	public int positionY;
	public int positionZ;
	public int scale;
	private float lastX;
	private float lastY;
	public boolean joystickRotateAction = false;
	private boolean primeraVez=true;

	public static final String VR_ACTIVITY_CLASS = "com.google.appinventor.components.runtime.vr4ai.VRActivity";
	// RECIBIDOS
	// VIDEO

	public static final String VR_VIDEO_SEEKTO = VR_ACTIVITY_CLASS + ".seekToVideo";
	public static final String VR_VIDEO_STOP = VR_ACTIVITY_CLASS + ".stopVideo";
	public static final String VR_VIDEO_PLAY = VR_ACTIVITY_CLASS + ".playVideo";
	public static final String VR_VIDEO_PAUSE = VR_ACTIVITY_CLASS + ".pauseVideo";
	public static final String VR_VIDEO_RESET = VR_ACTIVITY_CLASS + ".resetVideo";
	
	//VRSCENE
	public static final String VR_STOP = VR_ACTIVITY_CLASS + ".stop";
	public static final String VR_MOVEFOCUS = VR_ACTIVITY_CLASS + ".moveFocus";
	
	// OBJETO3D
	public static final String VR_3DOBJECT_RESET = VR_ACTIVITY_CLASS + ".3dObjectReset";
	public static final String VR_3DOBJECT_ROTATE_LEFT = VR_ACTIVITY_CLASS + ".3dObjectRotateLeft";
	public static final String VR_3DOBJECT_ROTATE_RIGHT = VR_ACTIVITY_CLASS + ".3dObjectRotateRight";
	public static final String VR_3DOBJECT_ROTATE_UP = VR_ACTIVITY_CLASS + ".3dObjectRotateUp";
	public static final String VR_3DOBJECT_ROTATE_DOWN = VR_ACTIVITY_CLASS + ".3dObjectRotateDown";

	public static final String VR_3DOBJECT_ZOOM_IN = VR_ACTIVITY_CLASS + ".3dObjectRotateIn";
	public static final String VR_3DOBJECT_ZOOM_OUT = VR_ACTIVITY_CLASS + ".3dObjectRotateOut";
	
	public static final String VR_3DOBJECT_MOVE_DOWN = VR_ACTIVITY_CLASS + ".3dObjectMoveDown";
	public static final String VR_3DOBJECT_MOVE_UP = VR_ACTIVITY_CLASS + ".3dObjectMoveUp";
	public static final String VR_3DOBJECT_MOVE_LEFT = VR_ACTIVITY_CLASS + ".3dObjectMoveLeft";
	public static final String VR_3DOBJECT_MOVE_RIGHT = VR_ACTIVITY_CLASS + ".3dObjectMoveRight";
	

	// ENVIADOS
	public static final String VR_EVENT_VIDEO_END = VR_ACTIVITY_CLASS + ".endVideo";
	public static final String VR_EVENT_VIDEO_START = VR_ACTIVITY_CLASS + ".startVideo";
	public static final String VR_EVENT_SHAKE = VR_ACTIVITY_CLASS + ".shake";
	public static final String VR_EVENT_PRESS_A_BUTTON = VR_ACTIVITY_CLASS + ".pressA";
	public static final String VR_EVENT_PRESS_B_BUTTON = VR_ACTIVITY_CLASS + ".pressB";
	public static final String VR_EVENT_PRESS_C_BUTTON = VR_ACTIVITY_CLASS + ".pressC";
	public static final String VR_EVENT_PRESS_D_BUTTON = VR_ACTIVITY_CLASS + ".pressD";
	public static final String VR_EVENT_PRESS_X_BUTTON = VR_ACTIVITY_CLASS + ".pressX";
	public static final String VR_EVENT_PRESS_Z_BUTTON = VR_ACTIVITY_CLASS + ".pressZ";

	// ENVIADOS JOYSTICK
	public static final String VR_EVENT_PRESS_UP_JOYSTICK = VR_ACTIVITY_CLASS + ".pressUpJoystick";
	public static final String VR_EVENT_PRESS_DOWN_JOYSTICK = VR_ACTIVITY_CLASS + ".pressDownJoystick";
	public static final String VR_EVENT_PRESS_LEFT_JOYSTICK = VR_ACTIVITY_CLASS + ".pressLeftJoystick";
	public static final String VR_EVENT_PRESS_RIGHT_JOYSTICK = VR_ACTIVITY_CLASS + ".pressRightJoystick";
	public static final String VR_EVENT_PRESS_UP_LEFT_JOYSTICK = VR_ACTIVITY_CLASS + ".pressUpLeftJoystick";
	public static final String VR_EVENT_PRESS_UP_RIGHT_JOYSTICK = VR_ACTIVITY_CLASS + ".pressUpRightJoystick";
	public static final String VR_EVENT_PRESS_DOWN_LEFT_JOYSTICK = VR_ACTIVITY_CLASS + ".pressDownLeftJoystick";
	public static final String VR_EVENT_PRESS_DOWN_RIGHT_JOYSTICK = VR_ACTIVITY_CLASS + ".pressDownRightJoystick";

	private ShakeListener mShaker;

	///////////////
	// RECEIVER //
	//////////////

	// GLOBAL

	private BroadcastReceiver stopBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			onBackPressed();
		}

	};

	// VIDEO
	private BroadcastReceiver seekToVideoEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			int position = intent.getIntExtra("SeektoPosition", 0);
			rendererVideo360.mediaPlayer.seekTo(position);

		}

	};
	private BroadcastReceiver stopVideoEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

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

	// 3DOBJECT
	private BroadcastReceiver reset3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			Object3DState("reset");
		}

	};
	private BroadcastReceiver rotateLeft3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			Object3DState("rotateLeft");
		}

	};
	private BroadcastReceiver rotateRight3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			Object3DState("rotateRight");
		}

	};
	private BroadcastReceiver rotateUp3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			Object3DState("rotateUp");
		}

	};
	private BroadcastReceiver rotateDown3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			Object3DState("rotateDown");
		}

	};
	private BroadcastReceiver zoomIn3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			Object3DState("zoomIn");
		}

	};
	private BroadcastReceiver zoomOut3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			Object3DState("zoomOut");
		}

	};
	private BroadcastReceiver moveLeft3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			Object3DState("moveLeft");
		}

	};
	private BroadcastReceiver moveRight3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			Object3DState("moveRight");
		}

	};
	private BroadcastReceiver moveUp3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			Object3DState("moveUp");
		}

	};
	private BroadcastReceiver moveDown3DObjectEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			Object3DState("moveDown");
		}

	};
	private BroadcastReceiver moveFocusEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			renderer3DObject.moveFocusObject();
		}

	};
	
	

	private void videoState(String state) {

		final String stateFinal = state;
		cardboardView.queueEvent(new Runnable() {

			@Override
			public void run() {

				switch (stateFinal) {
				case "stop":
					rendererVideo360.mediaPlayer.stop();
					break;
				case "play":
					rendererVideo360.mediaPlayer.start();
					break;
				case "pause":
					rendererVideo360.mediaPlayer.pause();
					break;
				case "reset":
					rendererVideo360.mediaPlayer.reset();
					break;
				case "release":
					rendererVideo360.mediaPlayer.release();
					;
					break;
				}
			}

		});

	}
	

	private void Object3DState(String state) {
		final String stateFinal = state;
		cardboardView.queueEvent(new Runnable() {

			@Override
			public void run() {
				switch (stateFinal) {
				case "reset":
					renderer3DObject.reset3DPosition();
					break;
				case "rotateLeft":
					renderer3DObject.rotate3DObject("left");
					break;
				case "rotateRight":
					renderer3DObject.rotate3DObject("right");
					break;
				case "rotateUp":
					renderer3DObject.rotate3DObject("up");
					break;
				case "rotateDown":
					renderer3DObject.rotate3DObject("down");
					break;
				case "zoomIn":
					renderer3DObject.zoom3DObject("In");
					break;
				case "zoomOut":
					renderer3DObject.zoom3DObject("Out");
					break;
				case "moveLeft":
					renderer3DObject.move3DObject("left");
					break;
				case "moveRight":
					renderer3DObject.move3DObject("right");
					break;
				case "moveUp":
					renderer3DObject.move3DObject("up");
					break;
				case "moveDown":
					renderer3DObject.move3DObject("down");
					break;

				}
			}

		});

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		mShaker = new ShakeListener(this);
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
			public void onShake() {

				Intent intent2 = new Intent(VR_EVENT_SHAKE);
				LocalBroadcastManager.getInstance(mShaker.mContext).sendBroadcast(intent2);
			}
		});

		super.onCreate(savedInstanceState);

		/*// manda a el activity de ai2
		Intent intent2 = new Intent(VR_ACTIVITY_CLASS + ".mensaje");
		intent2.putExtra("msg", "mensaje");
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);*/

		try {
			listAssets();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		RelativeLayout rl = new RelativeLayout(this);

		setContentView(rl);

		Log.v("CONTEXT", "relative creado");
		cardboardView = new CardboardView(this);
		rl.addView(cardboardView);
		Log.v("CONTEXT", "cardboardView creado");

		setCardboardView(cardboardView);

		mOverlayView = new CardboardOverlayView(this, null);

		// setVolumeKeysMode(VolumeKeys.DISABLED);

		Config.glTransparencyOffset = 0;
		Config.glTransparencyMul = 1f / 100;
		// Config.maxTextureLayers=4;
		
		extras = getIntent().getExtras();
		object3D = extras.getBoolean("Object3D");
		image360 = extras.getBoolean("Image360");
		video360 = extras.getBoolean("Video360");
		stereoMode = extras.getBoolean("StereoMode");
		hasController = extras.getBoolean("Controller");
		if (!stereoMode) {
			cardboardView.setVRModeEnabled(false);
		}
		// cardboardView.setEGLContextClientVersion(2);
		// cardboardView.setEGLConfigChooser(new
		// AAConfigChooser(cardboardView));
		// contentToRender="Object3D";
		// model3DPath="LEGO_Man.obj";
		// material3DPath="LEGO_Man.mtl";
		// skyboxPath="stars_004_1024.png";
		// image360Path="spherical_pano.jpg";
		// video360Path="ff7.mp4";

		try {
			renderFilter();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void processJoystickInput(MotionEvent event, int historyPos) {

		InputDevice mInputDevice = event.getDevice();

		// Calculate the horizontal distance to move by
		// using the input value from one of these physical controls:
		// the left control stick, hat axis, or the right control stick.

		float x = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_X, historyPos);
		if (x == 0) {
			x = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_HAT_X, historyPos);
		}
		if (x == 0) {
			x = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_Z, historyPos);
		}

		// Calculate the vertical distance to move by
		// using the input value from one of these physical controls:
		// the left control stick, hat switch, or the right control stick.
		float y = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_Y, historyPos);
		if (y == 0) {
			y = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_HAT_Y, historyPos);
		}
		if (y == 0) {
			y = getCenteredAxis(event, mInputDevice, MotionEvent.AXIS_RZ, historyPos);
		}

		// Update the ship object based on the new x and y values
		Log.v("EJE_X", x + "");
		Log.v("EJE_Y", y + "");
		lastX = x;
		lastY = y;

		// en base a las coordenadas se la posicion del joystick

		// movimiento en y
		if (lastX == 0 && lastY != 0) {
			if (lastY > 0) {
				Intent intentJoystickLeft = new Intent(VRActivity.VR_EVENT_PRESS_LEFT_JOYSTICK);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intentJoystickLeft);
			} else {
				Intent intentJoystickRight = new Intent(VRActivity.VR_EVENT_PRESS_RIGHT_JOYSTICK);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intentJoystickRight);
			}

		} // movimiento en x
		else if (lastX != 0 && lastY == 0) {
			if (lastX > 0) {
				
				Intent intentJoystickDown = new Intent(VRActivity.VR_EVENT_PRESS_DOWN_JOYSTICK);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intentJoystickDown);
			} else {
				Intent intentJoystickUp = new Intent(VRActivity.VR_EVENT_PRESS_UP_JOYSTICK);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intentJoystickUp);
			}
		} else if (lastX != 0 && lastY != 0) {
			// diagonal superior izquierda
			if (lastX < 0 && lastY > 0) {
				Intent intentJoystickUpLeft = new Intent(VRActivity.VR_EVENT_PRESS_UP_LEFT_JOYSTICK);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intentJoystickUpLeft);
			}
			// diagonal superior derecha
			else if (lastX < 0 && lastY < 0) {
				Intent intentJoystickUpRight = new Intent(VRActivity.VR_EVENT_PRESS_UP_RIGHT_JOYSTICK);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intentJoystickUpRight);
			}
			// diagonal inferior izquierda
			else if (lastX > 0 && lastY > 0) {
				Intent intentJoystickDownLeft = new Intent(VRActivity.VR_EVENT_PRESS_DOWN_LEFT_JOYSTICK);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intentJoystickDownLeft);
			}
			// diagonal inferior derecha
			else if (lastX > 0 && lastY < 0) {
				Intent intentJoystickDownRight = new Intent(VRActivity.VR_EVENT_PRESS_DOWN_RIGHT_JOYSTICK);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intentJoystickDownRight);
			}
		}
		/*
		 * cardboardView.queueEvent(new Runnable() {
		 * 
		 * @Override public void run() {
		 * 
		 * 
		 * renderer3DObject.rotateAllAxis(lastX, lastY);
		 * 
		 * 
		 * }
		 * 
		 * });
		 */

	}

	private static float getCenteredAxis(MotionEvent event, InputDevice device, int axis, int historyPos) {
		final InputDevice.MotionRange range = device.getMotionRange(axis, event.getSource());

		// A joystick at rest does not always report an absolute position of
		// (0,0). Use the getFlat() method to determine the range of values
		// bounding the joystick axis center.
		if (range != null) {
			final float flat = range.getFlat();
			final float value = historyPos < 0 ? event.getAxisValue(axis)
					: event.getHistoricalAxisValue(axis, historyPos);

			// Ignore axis values that are within the 'flat' region of the
			// joystick axis center.
			if (Math.abs(value) > flat) {
				return value;
			}
		}
		return 0;
	}

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {

		Log.v("CODIGO DISPOSITIVO", InputDevice.SOURCE_JOYSTICK + "");
		Log.v("DISPOSITIVO", event.getSource() + "");
		Log.v("ACTION", event.getAction() + "");
		// Check that the event came from a game controller
		if (hasController) {
			if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK
					&& event.getAction() == MotionEvent.ACTION_MOVE) {

				// Process all historical movement samples in the batch
				final int historySize = event.getHistorySize();

				// Process the movements starting from the
				// earliest historical position in the batch
				for (int i = 0; i < historySize; i++) {
					// Process the event at historical position i
					processJoystickInput(event, i);
				}

				// Process the current movement sample in the batch (position
				// -1)
				processJoystickInput(event, -1);

				return true;
			}
		}
		return super.onGenericMotionEvent(event);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		Log.v("KEYEVENT", keyCode + "");

		if (hasController) {

			if (keyCode == 4) {
				Log.v("BOTONPULSADO", "D");
				Intent intentKeyD = new Intent(VRActivity.VR_EVENT_PRESS_D_BUTTON);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intentKeyD);
				return true;
			} else if (keyCode == 23) {
				Log.v("BOTONPULSADO", "B");
				Intent intentKeyB = new Intent(VRActivity.VR_EVENT_PRESS_B_BUTTON);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intentKeyB);
				return true;
			} else if (keyCode == 67) {
				Log.v("BOTONPULSADO", "C");
				Intent intentKeyC = new Intent(VRActivity.VR_EVENT_PRESS_C_BUTTON);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intentKeyC);
				return true;
			} else if (keyCode == 62) {
				Log.v("BOTONPULSADO", "A");
				Intent intentKeyA = new Intent(VRActivity.VR_EVENT_PRESS_A_BUTTON);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intentKeyA);
				return true;
			} else if (keyCode == 102) {
				Log.v("BOTONPULSADO", "Z");
				Intent intentKeyZ = new Intent(VRActivity.VR_EVENT_PRESS_Z_BUTTON);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intentKeyZ);
				return true;
			} else if (keyCode == 103) {
				Log.v("BOTONPULSADO", "X");
				Intent intentKeyX = new Intent(VRActivity.VR_EVENT_PRESS_X_BUTTON);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intentKeyX);
				return true;
			}
		}

		return super.onKeyUp(keyCode, event);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// programar para renders aparte del de video360
		Log.v("ACCIONTOUCH", event.toString());
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Log.v("TOCADO", "abajo");
			/*
			 * if (rendererVideo360 != null) { if
			 * (rendererVideo360.mediaPlayer.isPlaying()) {
			 * rendererVideo360.mediaPlayer.pause(); } else {
			 * rendererVideo360.mediaPlayer.start(); } }
			 */
			if (hasController) {
				Intent intentKeyA = new Intent(VRActivity.VR_EVENT_PRESS_A_BUTTON);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intentKeyA);
			}
			return true;
		}

		return super.onTouchEvent(event);
	}

	/**
	 * Called when the Cardboard trigger is pulled.
	 */
	@Override
	public void onCardboardTrigger() {
		Log.i("EVENTO", "onCardboardTrigger");
	}

	private void renderFilter() throws IOException {

		TextureManager.getInstance().flush();

		if (object3D) {
			getExtraIntent();
			renderer3DObject = new VR3DObjectRenderScene(this);
			cardboardView.setRenderer(renderer3DObject);

		}
		if (image360) {
			getExtraIntent();
			VRImage360RenderScene rendererImage360 = new VRImage360RenderScene(this);
			// TextureManager.getInstance().addTexture("panorama", new
			// Texture(getAssets().open(image360Path)));
			cardboardView.setRenderer(rendererImage360);
		}
		if (video360) {
			getExtraIntent();
			rendererVideo360 = new VRVideo360RenderScene(this);
			cardboardView.setRenderer(rendererVideo360);
		}

	}

	public void getExtraIntent() {

		

		if (object3D) {
			//model3DPath = extras.getString("3DmodelPath");
			//material3DPath = extras.getString("3DmaterialPath");
			skyboxPath = extras.getString("SkyboxPath");
			//positionX = extras.getInt("PositionX");
			//positionY = extras.getInt("PositionY");
			//positionZ = extras.getInt("PositionZ");
			//scale = extras.getInt("Scale");
			rotateSpeed=extras.getFloat("RotateSpeed");
			moveSpeed=extras.getFloat("MoveSpeed");
			
			Intent i = this.getIntent();
			//i.setExtrasClassLoader(Object3DParcelable.class.getClassLoader());
		    ArrayList<Object3DParcelable> cloneObject3DListAi2 = i.getParcelableArrayListExtra("Object3DList");
		    object3DListAi2=cloneObject3DListAi2;
			
			//funciona con un solo objecto, implementar la forma de recorrer esta coleccion en la clase de VR3DObjectRender
			//e ir poniendo los objetos en la escena
			
			//Iterator<Object3DParcelable> itr = object3DListAi2.iterator(); 
			/*while(itr.hasNext()) {
				Object3DParcelable myObject = itr.next(); 
				model3DPath=myObject.getModel3DPath();
				Log.v("MODEL3D_VRACTIVITY", model3DPath);
				material3DPath=myObject.getMaterial3DPath();
				positionX=myObject.getPositionX();
				positionY=myObject.getPositionY();
				positionZ=myObject.getPositionZ();
				scale=myObject.getScale();
			} */

			registerReceivers();
			

		}
		if (image360) {
			image360Path = extras.getString("Image360Path");
			Log.v("IMAGE360", image360Path);
			
		}
		if (video360) {
			video360Path = extras.getString("Video360Path");
			isURL = extras.getBoolean("IsURL");
			isLoop = extras.getBoolean("IsLoop");
			video360Volume = extras.getInt("Video360Volume");
			video360Quality = extras.getString("Video360Quality");
			Log.v("VIDEO360", video360Path);
			registerReceivers();

			

		}
	}

	/*public InputStream getModel3D() throws IOException {

		return this.getApplicationContext().getAssets().open(model3DPath);

	}

	public InputStream getMaterial3D() throws IOException {

		return this.getApplicationContext().getAssets().open(material3DPath);

	}*/

	public void listAssets() throws IOException {
		String[] listOfFiless = this.getApplicationContext().getAssets().list("");

		for (int i = 0; i < listOfFiless.length; i++) {
			Log.d("ASSETS", listOfFiless[i]);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		unregisterReceivers();

		
		/*
		 * if (rendererVideo360 != null) { if (rendererVideo360.mediaPlayer !=
		 * null) { // cuando pauso la aplicacion recojo la posicion del video
		 * media_length = rendererVideo360.mediaPlayer.getCurrentPosition();
		 * rendererVideo360.media_length = media_length;
		 * rendererVideo360.mediaPlayer.pause();
		 * 
		 * LocalBroadcastManager.getInstance(this).unregisterReceiver(this.
		 * stopVideoEventBroadCastReceiver);
		 * LocalBroadcastManager.getInstance(this).unregisterReceiver(this.
		 * playVideoEventBroadCastReceiver);
		 * LocalBroadcastManager.getInstance(this).unregisterReceiver(this.
		 * pauseVideoEventBroadCastReceiver);
		 * LocalBroadcastManager.getInstance(this).unregisterReceiver(this.
		 * resetVideoEventBroadCastReceiver); } }
		 */
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceivers();
		if (video360) {
			videoState("release");
		}
		/*
		 * if (rendererVideo360 != null) { if (rendererVideo360.mediaPlayer !=
		 * null) {
		 * 
		 * rendererVideo360.mediaPlayer.release(); } }
		 */
	}

	@Override
	protected void onResume() {

		Log.v("ONRESUME", "HELLO");
		super.onResume();
		if(primeraVez)
		{
			primeraVez=false;
		}else
		{
			registerReceivers();
		}
	}

	@Override
	public void onBackPressed() {
		unregisterReceivers();
		super.onBackPressed();
	}
	
	public void registerReceivers()
	{
		LocalBroadcastManager.getInstance(this).registerReceiver(stopBroadCastReceiver,
				new IntentFilter(VRActivity.VR_STOP));
		
		if(object3D)
		{
			LocalBroadcastManager.getInstance(this).registerReceiver(this.moveFocusEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_MOVEFOCUS));
			
			LocalBroadcastManager.getInstance(this).registerReceiver(reset3DObjectEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_3DOBJECT_RESET));
			LocalBroadcastManager.getInstance(this).registerReceiver(this.rotateLeft3DObjectEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_3DOBJECT_ROTATE_LEFT));
			LocalBroadcastManager.getInstance(this).registerReceiver(this.rotateRight3DObjectEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_3DOBJECT_ROTATE_RIGHT));
			LocalBroadcastManager.getInstance(this).registerReceiver(this.rotateUp3DObjectEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_3DOBJECT_ROTATE_UP));
			LocalBroadcastManager.getInstance(this).registerReceiver(this.rotateDown3DObjectEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_3DOBJECT_ROTATE_DOWN));
			LocalBroadcastManager.getInstance(this).registerReceiver(this.zoomIn3DObjectEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_3DOBJECT_ZOOM_IN));
			LocalBroadcastManager.getInstance(this).registerReceiver(this.zoomOut3DObjectEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_3DOBJECT_ZOOM_OUT));
			
			LocalBroadcastManager.getInstance(this).registerReceiver(this.moveUp3DObjectEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_3DOBJECT_MOVE_UP));
			LocalBroadcastManager.getInstance(this).registerReceiver(this.moveDown3DObjectEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_3DOBJECT_MOVE_DOWN));
			LocalBroadcastManager.getInstance(this).registerReceiver(this.moveLeft3DObjectEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_3DOBJECT_MOVE_LEFT));
			LocalBroadcastManager.getInstance(this).registerReceiver(this.moveRight3DObjectEventBroadCastReceiver,
					new IntentFilter(VRActivity.VR_3DOBJECT_MOVE_RIGHT));
		}
		else if(video360)
		{
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
		else if(image360){}
	}
	public void unregisterReceivers()
	{
		LocalBroadcastManager.getInstance(this).unregisterReceiver(this.stopBroadCastReceiver);

		if (video360) {
			videoState("pause");
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.stopVideoEventBroadCastReceiver);
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.playVideoEventBroadCastReceiver);
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.pauseVideoEventBroadCastReceiver);
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.resetVideoEventBroadCastReceiver);
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.seekToVideoEventBroadCastReceiver);
		}
		if (object3D) {
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.reset3DObjectEventBroadCastReceiver);
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.rotateLeft3DObjectEventBroadCastReceiver);
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.rotateRight3DObjectEventBroadCastReceiver);
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.rotateUp3DObjectEventBroadCastReceiver);
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.rotateDown3DObjectEventBroadCastReceiver);
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.zoomIn3DObjectEventBroadCastReceiver);
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.zoomOut3DObjectEventBroadCastReceiver);
			
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.moveUp3DObjectEventBroadCastReceiver);
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.moveDown3DObjectEventBroadCastReceiver);
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.moveLeft3DObjectEventBroadCastReceiver);
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.moveRight3DObjectEventBroadCastReceiver);
			LocalBroadcastManager.getInstance(this).unregisterReceiver(this.moveFocusEventBroadCastReceiver);
	  }
	}

}
