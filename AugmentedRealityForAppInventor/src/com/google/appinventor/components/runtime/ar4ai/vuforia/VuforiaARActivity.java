package com.google.appinventor.components.runtime.ar4ai.vuforia;

import java.util.ArrayList;
import java.util.List;
import com.google.appinventor.components.runtime.ar4ai.ARActivity;
import com.google.appinventor.components.runtime.ar4ai.PhysicalObject;
import com.google.appinventor.components.runtime.ar4ai.VirtualObject;
import com.google.appinventor.components.runtime.ar4ai.common.VuforiaApplicationControl;
import com.google.appinventor.components.runtime.ar4ai.common.VuforiaApplicationException;
import com.google.appinventor.components.runtime.ar4ai.common.VuforiaApplicationSession;
import com.google.appinventor.components.runtime.ar4ai.utils.SampleApplicationGLView;
import com.google.appinventor.components.runtime.ar4ai.utils.UserInterface;
import com.qualcomm.vuforia.CameraDevice;
import com.qualcomm.vuforia.DataSet;
import com.qualcomm.vuforia.Marker;
import com.qualcomm.vuforia.MarkerTracker;
import com.qualcomm.vuforia.ObjectTracker;
import com.qualcomm.vuforia.STORAGE_TYPE;
import com.qualcomm.vuforia.State;
import com.qualcomm.vuforia.Tool;
import com.qualcomm.vuforia.Trackable;
import com.qualcomm.vuforia.TrackableResult;
import com.qualcomm.vuforia.Tracker;
import com.qualcomm.vuforia.TrackerManager;
import com.qualcomm.vuforia.Vec2F;
import com.qualcomm.vuforia.Vuforia;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Switch;

@SuppressLint("NewApi")
public class VuforiaARActivity extends ARActivity implements VuforiaApplicationControl {

	private VuforiaApplicationSession vuforiaAppSession;
	private SampleApplicationGLView mGlView;
	private JpctRenderer mRenderer;
	private GestureDetector mGestureDetector;
	private boolean mFlash;
	private boolean mContAutofocus;
	private boolean mIsDroidDevice;
	private View mFlashOptionView;
	private AlertDialog mErrorDialog;

	private Marker[] markerDataSet;
	private DataSet imageDataset;

	private List<TrackableResult> previouslyRecognizedTrackables = new ArrayList<TrackableResult>();
	private List<TrackableResult> currentRecognizedTrackables = new ArrayList<TrackableResult>();

	private UserInterface ui;
	
	/////////////////////////
	// ACTIVITY LIFECYCLE //
	/////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(LOGTAG, "SPI-FM: Vuforia Activity");
		super.onCreate(savedInstanceState);

		// Preparamos layout
		RelativeLayout viewLayout = new RelativeLayout(this);
		setContentView(viewLayout);

		// MIG: Creamos una nueva sesion Vuforia
		setVuforiaAppSession(new VuforiaApplicationSession(this));
		getVuforiaAppSession().initAR(this, this.camera.getScreenOrientation());

		mGestureDetector = new GestureDetector(this, new GestureListener());

		setmIsDroidDevice(android.os.Build.MODEL.toLowerCase().startsWith("droid"));

	}

	// Called when the activity will start interacting with the user.
	@Override
	protected void onResume() {
		Log.d(LOGTAG, "SPI-FM: onResume");
		super.onResume();

		// This is needed for some Droid devices to force portrait
		if (ismIsDroidDevice()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		Log.d(LOGTAG, "SPI-FM: Antes de Intentar iniciar sesión");

		try {
			getVuforiaAppSession().resumeAR();
		} catch (VuforiaApplicationException e) {
			Log.e(LOGTAG, e.getString());
		}

		Log.d(LOGTAG, "SPI-FM: Despues de Intentar iniciar sesión");

		// Resume the GL view:
		if (getmGlView() != null) {
			getmGlView().setVisibility(View.VISIBLE);
			getmGlView().onResume();
		}

	}

	// Called when the system is about to start resuming a previous activity.
	@Override
	protected void onPause() {
		Log.d(LOGTAG, "SPI-FM: onPause");
		super.onPause();

		if (getmGlView() != null) {
			getmGlView().setVisibility(View.INVISIBLE);
			getmGlView().onPause();
		}

		// Turn off the flash
		if (getmFlashOptionView() != null && ismFlash()) {
			// OnCheckedChangeListener is called upon changing the checked state
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				((Switch) getmFlashOptionView()).setChecked(false);
			} else {
				((CheckBox) getmFlashOptionView()).setChecked(false);
			}
		}

		try {
			getVuforiaAppSession().pauseAR();
		} catch (VuforiaApplicationException e) {
			Log.e(LOGTAG, e.getString());
		}

	}

	// The final call you receive before your activity is destroyed.
	@Override
	protected void onDestroy() {
		Log.d(LOGTAG, "SPI-FM:  onDestroy");
		super.onDestroy();

		try {
			getVuforiaAppSession().stopAR();
		} catch (VuforiaApplicationException e) {
			Log.e(LOGTAG, e.getString());
		}

		System.gc();
	}

	//@Overr2ide
	public boolean onTouchEvent2(MotionEvent event) {
		mGestureDetector.onTouchEvent(event); // handle onFling here
		return true;
	}
	
	
	@Override
    public boolean onTouchEvent(MotionEvent event)
    {
		
		Log.d(LOGTAG, "TOCATA 01");
        if(mRenderer!=null){
	          mRenderer.onTouch(event);
	    }
        
        return mGestureDetector.onTouchEvent(event);
    }
//	
	

	////////////////////////////////
	// VUFORIA SESSION MANAGEMENT //
	////////////////////////////////

	// MIG: Inicializa la aplicación y la cámara.
	@Override
	public void onInitARDone(VuforiaApplicationException exception) {

		if (exception == null) {
			initApplicationAR();

			getmRenderer().mIsActive = true;
			// Now add the GL surface view. It is important
			// that the OpenGL ES surface view gets added
			// BEFORE the camera is started and video
			// background is configured.
			addContentView(getmGlView(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			getmGlView().setZOrderMediaOverlay(true);
			//View view = LayoutInflater.from(this).inflate(R.layout.uilayout, null);
			//addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			ui = new UserInterface(this, camera.getUivariables());
			ViewGroup.LayoutParams uiParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			addContentView(ui, uiParams);
			try {

				if (this.camera.isFrontCamera()) {
					getVuforiaAppSession().startAR(CameraDevice.CAMERA.CAMERA_FRONT);
				} else {
					getVuforiaAppSession().startAR(CameraDevice.CAMERA.CAMERA_BACK);
				}

			} catch (VuforiaApplicationException e) {
				Log.e(LOGTAG, e.getString());
			}

			boolean result = CameraDevice.getInstance().setFocusMode(CameraDevice.FOCUS_MODE.FOCUS_MODE_CONTINUOUSAUTO);

			if (result)
				setmContAutofocus(true);
			else
				Log.e(LOGTAG, "SPI-FM: Unable to enable continuous autofocus");

		} else {
			Log.e(LOGTAG, exception.getString());
			showErrorMessage(exception.getString());
			exception.printStackTrace();
		}
	}

	@Override
	public void onQCARUpdate(State state) {

		// STATUS
		// static final int UNKNOWN = 0
		// static final int UNDEFINED = UNKNOWN + 1
		// static final int DETECTED = UNDEFINED + 1
		// static final int TRACKED = DETECTED + 1
		// static final int EXTENDED_TRACKED = TRACKED + 1

		Log.d(LOGTAG, "READING STATE AND BROADCAST EVENTS..");

		// Cleaning the current recognized trackables
		currentRecognizedTrackables.clear();

		// Recovering the current recognized trackables
		for (int i = 0; i < state.getNumTrackableResults(); ++i) {
			TrackableResult result = state.getTrackableResult(i);
			Log.d(LOGTAG, "Reconocemos un trackable");

			if (result.getTrackable() != null && result.getTrackable().getUserData() != null) {
				currentRecognizedTrackables.add(result);
			}
		}

		// Setting the disappeared trackables
		for (TrackableResult trackableResult : previouslyRecognizedTrackables) {
			if (contiene(currentRecognizedTrackables, trackableResult) == null) {
				PhysicalObject po = (PhysicalObject) trackableResult.getTrackable().getUserData();
				po.setStatus(PhysicalObject.STATUS_DISAPPEARS);

					// notificar que se ha borrado
					Intent intent = new Intent(ARActivity.AR_ACTIVITY_EVENT_PO);
					intent.putExtra("uuid", (po.getId()));
					intent.putExtra("status", po.getStatus());
					LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
					Log.d(LOGTAG, "Notificamos disappears PO"+po.getId());
	
					for (VirtualObject vo : po.getVirtualObject())
						if (this.mRenderer.eworld.getInfo(vo.getId()) != null)
							this.mRenderer.eworld.getInfo(vo.getId()).setVisibility(false);
					//if (po.getVirtualObject() != null) {
						//this.mRenderer.eworld.getInfo(po.getVirtualObject().getId()).setVisibility(false);
						//obj3D.build();
					//}

			}
		}

		// Setting the appeared trackables
		for (TrackableResult trackableResult : currentRecognizedTrackables) {
			if (contiene(previouslyRecognizedTrackables, trackableResult) == null) {
				PhysicalObject po = (PhysicalObject) trackableResult.getTrackable().getUserData();
				po.setStatus(PhysicalObject.STATUS_APPEARS);
				
				
				// Updating coordinates...
				float[] modelViewMatrix = Tool.convertPose2GLMatrix(trackableResult.getPose()).getData();
				po.setX(-modelViewMatrix[12]);
				po.setY(-modelViewMatrix[13]);
				po.setZ(modelViewMatrix[14]);

				// notificar nuevo
				Intent intent = new Intent(ARActivity.AR_ACTIVITY_EVENT_PO);
				intent.putExtra("uuid", (po.getId()));
				intent.putExtra("status", po.getStatus());
				intent.putExtra("x", po.getX());
				intent.putExtra("y", po.getY());
				intent.putExtra("z", po.getZ());
				Log.d(LOGTAG, "Notificamos appears PO"+po.getId());

				LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

				for (VirtualObject vo : po.getVirtualObject()) {
					if (vo.isEnabled() && mRenderer.eworld.getInfo(vo.getId()) != null)
						this.mRenderer.eworld.getInfo(vo.getId()).setVisibility(true);
				}
				//if (po.getVirtualObject() != null && po.getVirtualObject().isEnabled()) {
					//this.mRenderer.eworld.getInfo(po.getVirtualObject().getId()).setVisibility(true);
					
					//Object3D obj3D = this.mRenderer.mWorld.getObjectByName(po.getVirtualObject().getId());
					//obj3D.setVisibility(true);
					//obj3D.build();
				//}

			}
		}

		// Setting the trackables that are in the FOV
	
		float previousTrackableX, previousTrackableY, previousTrackableZ;
		float differenceTrackableX = 0, differenceTrackableY = 0, differenceTrackableZ = 0;

		TrackableResult previousTrackable;
		for (TrackableResult trackableResult : currentRecognizedTrackables) {
			previousTrackable = contiene(previouslyRecognizedTrackables, trackableResult);
			if (previousTrackable != null) {

				// Recover the previous coordinates
				PhysicalObject po = (PhysicalObject) previousTrackable.getTrackable().getUserData();
				previousTrackableX = po.getX();
				previousTrackableY = po.getY();
				previousTrackableZ = po.getZ();

				// Updating current coordinates...
				float[] currentModelViewMatrix = Tool.convertPose2GLMatrix(trackableResult.getPose()).getData();
				po.setX(-currentModelViewMatrix[12]);
				po.setY(-currentModelViewMatrix[13]);
				po.setZ(currentModelViewMatrix[14]);

				// Calculating differences
				differenceTrackableX = Math.abs(po.getX() - previousTrackableX);
				differenceTrackableY = Math.abs(po.getY() - previousTrackableY);
				differenceTrackableZ = Math.abs(po.getZ() - previousTrackableZ);
				po.setOffsetX(differenceTrackableX);
				po.setOffsetY(differenceTrackableY);
				po.setOffsetZ(differenceTrackableZ);

				if (differenceTrackableX > po.getSensitivityThresholdX() && differenceTrackableY > po.getSensitivityThresholdY()
						&& differenceTrackableZ > po.getSensitivityThresholdZ()) {
					po.setStatus(PhysicalObject.STATUS_CHANGEDPOSITION);
			
					// notificar cambios
					Intent intent = new Intent(ARActivity.AR_ACTIVITY_EVENT_PO);
					intent.putExtra("uuid", (po.getId()));
					intent.putExtra("status", po.getStatus());
					intent.putExtra("x", po.getX());
					intent.putExtra("y", po.getY());
					intent.putExtra("z", po.getZ());
					Log.d(LOGTAG, "Notificamos changeposition PO"+po.getId());

					LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
				} else {
					po.setStatus(PhysicalObject.STATUS_VISIBLE);
					Log.d(LOGTAG, "--------> STATUS_VISIBLE");
					Log.d(LOGTAG, "No Notificamos nada de "+po.getId());

				}

			}

		}

		// Volcamos los actuales en los previos
		previouslyRecognizedTrackables.clear();
		previouslyRecognizedTrackables.addAll(currentRecognizedTrackables);

	}

	private TrackableResult contiene(List<TrackableResult> trackableResults, TrackableResult trackableResultToLocate) {
		PhysicalObject poToLocate = (PhysicalObject) trackableResultToLocate.getTrackable().getUserData();
		PhysicalObject poAux;
		TrackableResult result = null;

		for (TrackableResult trackableResultAux : trackableResults) {
			poAux = (PhysicalObject) trackableResultAux.getTrackable().getUserData();

			if (poAux!=null &&poAux.getId().equals(poToLocate.getId())) {
				result = trackableResultAux;
				break;
			}
		}
		return result;
	}

	@Override
	public boolean doInitTrackers() {
		// Indicate if the trackers were initialized correctly
		boolean result = true;

		// Initialize the marker tracker:
		TrackerManager trackerManager = TrackerManager.getInstance();
		Tracker trackerBase = trackerManager.initTracker(MarkerTracker.getClassType());
		MarkerTracker markerTracker = (MarkerTracker) (trackerBase);

		if (markerTracker == null) {
			Log.e(LOGTAG, "markerTracker not initialized. Tracker already initialized or the camera is already started");
			result = false;
		} else {
			Log.i(LOGTAG, "markerTracker successfully initialized");
		}

		// Trying to initialize the image tracker
		ObjectTracker objectTracker = (ObjectTracker) trackerManager.initTracker(ObjectTracker.getClassType());
		if (objectTracker == null) {
			Log.e(LOGTAG, "objectTracker not initialized. Tracker already initialized or the camera is already started");
			result = false;
		} else {
			Log.i(LOGTAG, "objectTracker successfully initialized");
		}

		return result;

	}

	// To be called to load the trackers' data
	public boolean doLoadTrackersData() {
		Log.d(LOGTAG, "LOADING TRACKERS DATA..");

		TrackerManager tManager = TrackerManager.getInstance();

		MarkerTracker markerTracker = (MarkerTracker) tManager.getTracker(MarkerTracker.getClassType());
		if (markerTracker == null)
			return false;

		ObjectTracker objectTracker = (ObjectTracker) tManager.getTracker(ObjectTracker.getClassType());
		if (objectTracker == null)
			return false;

		// Carga del dataset de imagenes

		if (this.getCamera().getPathTargetDBXML() != null && !this.getCamera().getPathTargetDBXML().trim().equals("")) {
			if (imageDataset == null)
				imageDataset = objectTracker.createDataSet();

			if (imageDataset == null)
				return false;
			if (isAiCompanionActive()) {
				if (!imageDataset.load("/sdcard/AppInventor/assets/"+this.getCamera().getPathTargetDBXML(), STORAGE_TYPE.STORAGE_ABSOLUTE))
					return false;
			}
			else {
				if (!imageDataset.load(this.getCamera().getPathTargetDBXML(), STORAGE_TYPE.STORAGE_APPRESOURCE))
					return false;
			}
			

			if (!objectTracker.activateDataSet(imageDataset))
				return false;
		}

		Marker markerAux;
		PhysicalObject poAux;

		int totalPO = this.getArrayOfPhysicalObject().size();
		this.markerDataSet = new Marker[totalPO];	
		
		for (int i = 0; i < totalPO; i++) {
			poAux = this.getArrayOfPhysicalObject().get(i);
			
			if (poAux.isEnabled()) {
				if (poAux.getTrackerType() == PhysicalObject.TRACKER_TARGETDB) {

					// Localizamos el trackable asociado
					for (int j = 0; j <= imageDataset.getNumTrackables(); j++) {
						Trackable trackable = imageDataset.getTrackable(j);
						if (trackable != null && trackable.getName().equals(poAux.getTargetDBTracker())) {
							if (poAux.isExtendedTrackingEnabled()) { // isExtendedTrackingActive()
								trackable.startExtendedTracking();
							}
							trackable.setUserData(poAux);
							

						}
					}

				} else if (poAux.getTrackerType() == PhysicalObject.TRACKER_MARKER) {
					markerAux = markerTracker.createFrameMarker(poAux.getMarkerTracker(), poAux.getId(), new Vec2F(50, 50));
					if (poAux.isExtendedTrackingEnabled()) { // isExtendedTrackingActive()
						markerAux.startExtendedTracking();
					}
					markerAux.setUserData(poAux);

					if (markerAux == null) {
						Log.e(LOGTAG, "Failed to create frame marker " + "FrameMarker#" + poAux.getMarkerTracker());
						return false;
					} else {
						this.markerDataSet[i] = markerAux;

					}
				} else if (poAux.getTrackerType() == PhysicalObject.TRACKER_COLOR) {
					// TODO
				} else if (poAux.getTrackerType() == PhysicalObject.TRACKER_IMAGE) {
					// TODO
				} else if (poAux.getTrackerType() == PhysicalObject.TRACKER_TEXT) {
					// TODO

				}
			}

		}

		Log.i(LOGTAG, "Successfully initialized MarkerTracker.");

		return true;
	}

	// To be called to start tracking with the initialized trackers and their
	// loaded data
	public boolean doStartTrackers() {
		// Indicate if the trackers were started correctly
		boolean result = true;

		TrackerManager tManager = TrackerManager.getInstance();
		MarkerTracker markerTracker = (MarkerTracker) tManager.getTracker(MarkerTracker.getClassType());
		if (markerTracker != null)
			markerTracker.start();

		Vuforia.setHint (com.qualcomm.vuforia.HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, ARActivity.MAX_TRACKERS );
		Vuforia.setHint (com.qualcomm.vuforia.HINT.HINT_MAX_SIMULTANEOUS_OBJECT_TARGETS, ARActivity.MAX_TRACKERS );

		ObjectTracker objectTracker = (ObjectTracker) tManager.getTracker(ObjectTracker.getClassType());
		if (objectTracker != null)
			objectTracker.start();

		return result;
	};

	// To be called to stop the trackers
	public boolean doStopTrackers() {
		// Indicate if the trackers were stopped correctly
		boolean result = true;

		TrackerManager tManager = TrackerManager.getInstance();
		MarkerTracker markerTracker = (MarkerTracker) tManager.getTracker(MarkerTracker.getClassType());
		if (markerTracker != null)
			markerTracker.stop();

		ObjectTracker objectTracker = (ObjectTracker) tManager.getTracker(ObjectTracker.getClassType());
		if (objectTracker != null)
			objectTracker.stop();

		return result;
	};

	// To be called to destroy the trackers' data
	public boolean doUnloadTrackersData() {
		// Indicate if the trackers were unloaded correctly
		// Indicate if the trackers were unloaded correctly
		boolean result = true;

		TrackerManager tManager = TrackerManager.getInstance();

		MarkerTracker markerTracker = (MarkerTracker) tManager.getTracker(MarkerTracker.getClassType());
		if (markerTracker == null)
			return false;

		while (markerTracker.getNumMarkers() > 0) {
			markerTracker.destroyMarker(markerTracker.getMarker(0));
		}

		ObjectTracker objectTracker = (ObjectTracker) tManager.getTracker(ObjectTracker.getClassType());
		if (objectTracker == null)
			return false;

		if (imageDataset != null && imageDataset.isActive()) {
			if (objectTracker.getActiveDataSet().equals(imageDataset) && !objectTracker.deactivateDataSet(imageDataset)) {
				result = false;
			} else if (!objectTracker.destroyDataSet(imageDataset)) {
				result = false;
			}

			imageDataset = null;
		}

		return result;

	};

	// To be called to deinitialize the trackers
	public boolean doDeinitTrackers() {
		// Indicate if the trackers were deinitialized correctly
		boolean result = true;

		TrackerManager tManager = TrackerManager.getInstance();
		tManager.deinitTracker(MarkerTracker.getClassType());

		tManager.deinitTracker(ObjectTracker.getClassType());

		return result;
	};

	//////////////////////
	// ACCESSOR METHODS //
	//////////////////////

	public VuforiaApplicationSession getVuforiaAppSession() {
		return vuforiaAppSession;
	}

	public void setVuforiaAppSession(VuforiaApplicationSession vuforiaAppSession) {
		this.vuforiaAppSession = vuforiaAppSession;
	}

	public SampleApplicationGLView getmGlView() {
		return mGlView;
	}

	public void setmGlView(SampleApplicationGLView mGlView) {
		this.mGlView = mGlView;
	}

	public JpctRenderer getmRenderer() {
		return mRenderer;
	}

	public void setmRenderer(JpctRenderer mRenderer) {
		this.mRenderer = mRenderer;
	}

	public boolean ismFlash() {
		return mFlash;
	}

	public void setmFlash(boolean mFlash) {
		this.mFlash = mFlash;
	}

	public boolean ismContAutofocus() {
		return mContAutofocus;
	}

	public void setmContAutofocus(boolean mContAutofocus) {
		this.mContAutofocus = mContAutofocus;
	}

	public View getmFlashOptionView() {
		return mFlashOptionView;
	}

	public void setmFlashOptionView(View mFlashOptionView) {
		this.mFlashOptionView = mFlashOptionView;
	}

	public AlertDialog getmErrorDialog() {
		return mErrorDialog;
	}

	public void setmErrorDialog(AlertDialog mErrorDialog) {
		this.mErrorDialog = mErrorDialog;
	}

	public boolean ismIsDroidDevice() {
		return mIsDroidDevice;
	}

	public void setmIsDroidDevice(boolean mIsDroidDevice) {
		this.mIsDroidDevice = mIsDroidDevice;
	}

	//////////////////////
	// INTERNAL METHODS //
	//////////////////////

	// Initializes AR application components.
	// MIG: LLamado en la inicialización.
	private void initApplicationAR() {
		// Create OpenGL ES view:
		int depthSize = 16;
		int stencilSize = 0;
		boolean translucent = Vuforia.requiresAlpha();

		setmGlView(new SampleApplicationGLView(this));
		getmGlView().init(translucent, depthSize, stencilSize);

		// MIG: JPCT. Le añado el contexto que me hace falta luego en el Render
		// mRenderer = new FrameMarkerRenderer(this, vuforiaAppSession);
		try {
			setmRenderer(new JpctRenderer(this, getVuforiaAppSession(), this.getApplicationContext()));
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorMessage(e.getMessage());

		}
		getmGlView().setEGLContextClientVersion(2);
		getmGlView().setRenderer(getmRenderer());
	}

	// Shows error messages as System dialogs
	public void showErrorMessage(String message) {
		final String errorMessage = message;
		runOnUiThread(new Runnable() {
			public void run() {
				if (getmErrorDialog() != null) {
					getmErrorDialog().dismiss();
				}

				// Generates an Alert Dialog to show the error message
				AlertDialog.Builder builder = new AlertDialog.Builder(VuforiaARActivity.this);
				builder.setMessage(errorMessage).setTitle("Error").setCancelable(false).setIcon(0).setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				});

				setmErrorDialog(builder.create());
				getmErrorDialog().show();
			}
		});
	}

	//////////////////////
	// GESTURE LISTENER //
	//////////////////////

	// Process Single Tap event to trigger autofocus
	private class GestureListener extends GestureDetector.SimpleOnGestureListener {
		// Used to set autofocus one second after a manual focus is triggered
		private final Handler autofocusHandler = new Handler();

		
		
		
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// Generates a Handler to trigger autofocus
			// after 1 second
			autofocusHandler.postDelayed(new Runnable() {
				public void run() {
					boolean result = CameraDevice.getInstance().setFocusMode(CameraDevice.FOCUS_MODE.FOCUS_MODE_TRIGGERAUTO);

					if (!result)
						Log.e("SingleTapUp", "Unable to trigger focus");
				}
			}, 1000L);

			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {

			float x = Math.max(0, (int) e.getX());
			float y = Math.max(0, (int) e.getY());

			Intent intent = new Intent(ARActivity.AR_ACTIVITY_EVENT_CAMERA);
			intent.putExtra("status", AR_ACTIVITY_EVENT_CAMERA_LONGPRESS);
			intent.putExtra("x", x);
			intent.putExtra("y", y);

			LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);

		}

	}

	@Override
	public void refreshARScene() {
		Log.d(LOGTAG, "Refrescando la scene AR");
		doUnloadTrackersData();
		ui.updateInterface(camera.getUivariables());
		doReloadRenderData();
		doLoadTrackersData();

	}

	private void doReloadRenderData() {
		try {
			getmRenderer().createWorld();
		} catch (Exception e) {
			showErrorMessage(e.getMessage());

			e.printStackTrace();
		}

	}

	@Override
	protected void refreshModels(String uuid, String parameter, float value) {
		getmRenderer().updateModelParameter(uuid, parameter, value);
	}
	
	

}
