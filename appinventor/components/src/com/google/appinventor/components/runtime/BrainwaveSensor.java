/**
 * 
 */
package com.google.appinventor.components.runtime;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
//import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.annimon.stream.Stream;
//import com.annimon.stream.Stream;
import com.emotiv.insight.IEdk.IEE_DataChannel_t;
import com.emotiv.insight.IEmoStateDLL.IEE_FacialExpressionAlgo_t;
import com.emotiv.insight.IEmoStateDLL.IEE_MentalCommandAction_t;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesNativeLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.util.BrainwaveSensorStreamEventsTimer;
//import com.google.appinventor.components.runtime.util.EmotivBandsData;
import com.google.appinventor.components.runtime.util.EmotivController;
import com.google.appinventor.components.runtime.util.EmotivData;
import com.google.appinventor.components.runtime.util.JellybeanMR2Util;
import com.google.appinventor.components.runtime.util.OnInitializeListener;
import com.google.appinventor.components.runtime.util.SdkLevel;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @author ivanruizrube
 *
 */
@UsesLibraries(libraries = "bedk.jar," + "stream-1.1.7.jar")
@UsesNativeLibraries(v7aLibraries = "libbedk.so")
@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "Brainwave Sensor Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSINTERACTIONS, iconName = "images/brain.png")
@UsesPermissions(permissionNames = "android.permission.KILL_BACKGROUND_PROCESSES, android.permission.INTERNET, android.permission.WAKE_LOCK, android.permission.ACCESS_NETWORK_STATE,android.permission.WRITE_EXTERNAL_STORAGE, android.permission.READ_EXTERNAL_STORAGE, android.permission.BLUETOOTH_ADMIN, android.permission.BLUETOOTH,android.permission.ACCESS_COARSE_LOCATION, android.permission.ACCESS_FINE_LOCATION")

public class BrainwaveSensor extends AndroidNonvisibleComponent implements Serializable, OnInitializeListener,
		OnResumeListener, OnStopListener, OnPauseListener, OnDestroyListener {

	///////////////
	// CONSTANTS //
	///////////////

	public static final int REQUEST_ENABLE_BT = 1;

	public static final int HANDLER_USER_ADDED = 0;
	public static final int HANDLER_USER_REMOVED = 1;

	public static final int HANDLER_TRAINED_START = 2;
	public static final int HANDLER_TRAINED_FAILED = 3;
	public static final int HANDLER_TRAINED_SUCCEED = 4;
	public static final int HANDLER_TRAINED_REJECT = 5;
	public static final int HANDLER_TRAINED_COMPLETE = 6;
	public static final int HANDLER_TRAINED_ERASED = 7;
	public static final int HANDLER_TRAINED_RESET = 8;

	public static final int HANDLER_EXPRESSION_NEUTRAL = 10;
	public static final int HANDLER_EXPRESSION_BLINK = 11;
	public static final int HANDLER_EXPRESSION_LEFTWINK = 12;
	public static final int HANDLER_EXPRESSION_RIGHTWINK = 13;
	public static final int HANDLER_EXPRESSION_LOOKINGUP = 14;
	public static final int HANDLER_EXPRESSION_LOOKINGDOWN = 15;

	public static final int HANDLER_EXPRESSION_SMILING = 16;
	public static final int HANDLER_EXPRESSION_FROWNING = 17;
	public static final int HANDLER_EXPRESSION_CLENCHING = 18;

	public static final int HANDLER_COMMAND_DISAPPEAR = 20;
	public static final int HANDLER_COMMAND_DROP = 21;
	public static final int HANDLER_COMMAND_LEFT = 22;
	public static final int HANDLER_COMMAND_LIFT = 23;
	public static final int HANDLER_COMMAND_NEUTRAL = 24;
	public static final int HANDLER_COMMAND_PULL = 25;
	public static final int HANDLER_COMMAND_PUSH = 26;
	public static final int HANDLER_COMMAND_RIGHT = 27;
	public static final int HANDLER_COMMAND_ROTATE_CLOCKWISE = 28;
	public static final int HANDLER_COMMAND_ROTATE_COUNTER_CLOCKWISE = 29;
	public static final int HANDLER_COMMAND_ROTATE_FORWARDS = 30;
	public static final int HANDLER_COMMAND_ROTATE_LEFT = 31;
	public static final int HANDLER_COMMAND_ROTATE_REVERSE = 32;
	public static final int HANDLER_COMMAND_ROTATE_RIGHT = 33;
	
	public static final int HANDLER_CHANNELS_DATA = 34;

	/////////////////////
	// CLASS ATTRIBUTES //
	/////////////////////

	private static final long serialVersionUID = 2L;

	private static final int SCALING_FE = 10; // Multiplicamos por 100 para
												// ajustar escala de 0-100 a
												// 0-1000

	/////////////////////
	// LOCAL ATTRIBUTES //
	/////////////////////

	private EmotivController controller;

	private BluetoothAdapter mBluetoothAdapter;

	private EmotivData data = new EmotivData();

	private ComponentContainer container;

	private ExecutorService executorService;

	private boolean isTrainning = false;

	private int eegDeviceType = 0;

	private int mentalComandSensitivity = 50;

	private int facialExpressionSensitivity = 50;

	private String username = "";

	private String password = "";

	private String profile = "";

	private boolean triggerNeutralState = false;
	
	//Channel AF3
	public Stream<Double> channelAF3ThetaStream;
	public Stream<Double> channelAF3AlphaStream;
	public Stream<Double> channelAF3LowBetaStream;
	public Stream<Double> channelAF3HighBetaStream;
	public Stream<Double> channelAF3GammaStream;
	
	//Channel F7
	public Stream<Double> channelF7ThetaStream;
	public Stream<Double> channelF7AlphaStream;
	public Stream<Double> channelF7LowBetaStream;
	public Stream<Double> channelF7HighBetaStream;
	public Stream<Double> channelF7GammaStream;
	
	//Channel F3
	public Stream<Double> channelF3ThetaStream;
	public Stream<Double> channelF3AlphaStream;
	public Stream<Double> channelF3LowBetaStream;
	public Stream<Double> channelF3HighBetaStream;
	public Stream<Double> channelF3GammaStream;
	
	//Channel FC5
	public Stream<Double> channelFC5ThetaStream;
	public Stream<Double> channelFC5AlphaStream;
	public Stream<Double> channelFC5LowBetaStream;
	public Stream<Double> channelFC5HighBetaStream;
	public Stream<Double> channelFC5GammaStream;
	
	//Channel T7
	public Stream<Double> channelT7ThetaStream;
	public Stream<Double> channelT7AlphaStream;
	public Stream<Double> channelT7LowBetaStream;
	public Stream<Double> channelT7HighBetaStream;
	public Stream<Double> channelT7GammaStream;
	
	//Channel P7
	public Stream<Double> channelP7ThetaStream;
	public Stream<Double> channelP7AlphaStream;
	public Stream<Double> channelP7LowBetaStream;
	public Stream<Double> channelP7HighBetaStream;
	public Stream<Double> channelP7GammaStream;
	
	//Channel Pz
	public Stream<Double> channelPzThetaStream;
	public Stream<Double> channelPzAlphaStream;
	public Stream<Double> channelPzLowBetaStream;
	public Stream<Double> channelPzHighBetaStream;
	public Stream<Double> channelPzGammaStream;
	
	//Channel O1
	public Stream<Double> channelO1ThetaStream;
	public Stream<Double> channelO1AlphaStream;
	public Stream<Double> channelO1LowBetaStream;
	public Stream<Double> channelO1HighBetaStream;
	public Stream<Double> channelO1GammaStream;
	
	//Channel O2
	public Stream<Double> channelO2ThetaStream;
	public Stream<Double> channelO2AlphaStream;
	public Stream<Double> channelO2LowBetaStream;
	public Stream<Double> channelO2HighBetaStream;
	public Stream<Double> channelO2GammaStream;
	
	//Channel P8
	public Stream<Double> channelP8ThetaStream;
	public Stream<Double> channelP8AlphaStream;
	public Stream<Double> channelP8LowBetaStream;
	public Stream<Double> channelP8HighBetaStream;
	public Stream<Double> channelP8GammaStream;
	
	//Channel T8
	public Stream<Double> channelT8ThetaStream;
	public Stream<Double> channelT8AlphaStream;
	public Stream<Double> channelT8LowBetaStream;
	public Stream<Double> channelT8HighBetaStream;
	public Stream<Double> channelT8GammaStream;
	
	//Channel FC6
	public Stream<Double> channelFC6ThetaStream;
	public Stream<Double> channelFC6AlphaStream;
	public Stream<Double> channelFC6LowBetaStream;
	public Stream<Double> channelFC6HighBetaStream;
	public Stream<Double> channelFC6GammaStream;
	
	//Channel F4
	public Stream<Double> channelF4ThetaStream;
	public Stream<Double> channelF4AlphaStream;
	public Stream<Double> channelF4LowBetaStream;
	public Stream<Double> channelF4HighBetaStream;
	public Stream<Double> channelF4GammaStream;
	
	//Channel F8
	public Stream<Double> channelF8ThetaStream;
	public Stream<Double> channelF8AlphaStream;
	public Stream<Double> channelF8LowBetaStream;
	public Stream<Double> channelF8HighBetaStream;
	public Stream<Double> channelF8GammaStream;
	
	//Channel FC4
	public Stream<Double> channelAF4ThetaStream;
	public Stream<Double> channelAF4AlphaStream;
	public Stream<Double> channelAF4LowBetaStream;
	public Stream<Double> channelAF4HighBetaStream;
	public Stream<Double> channelAF4GammaStream;
	
	private BrainwaveSensorStreamEventsTimer timer;
	
	//private EmotivBandsData timerEmotivBandsData;
	
	public Activity activity;
	
	/////////////////
	// CONSTRUCTOR //
	/////////////////

	public BrainwaveSensor(ComponentContainer container) {
		super(container.$form());
		this.container = container;

		container.$form().registerForOnInitialize(this);
		container.$form().registerForOnResume(this);
		container.$form().registerForOnPause(this);
		container.$form().registerForOnStop(this);
		container.$form().registerForOnDestroy(this);

		controller = EmotivController.getInstance();

		executorService = Executors.newSingleThreadExecutor();
		
		//TPM: Simulated event
		//this.timerEmotivBandsData = new EmotivBandsData(this);
		this.activity = container.$context();
		this.timer = new BrainwaveSensorStreamEventsTimer(this);
	}

	public Context getContext() {
		return container.$form();
	}

	public Handler getMHandler() {
		return mHandler;
	}

	///////////////////////
	// OVERRIDED METHODS //
	///////////////////////

	@Override
	public void onInitialize() {
		//Log.e("EmotivController", "EVENTO EN VENTANA: onInitialize");

	}

	@Override
	public void onResume() {
		//Log.e("EmotivController", "EVENTO EN VENTANA: onResume");

	}

	@Override
	public void onPause() {
		//Log.e("EmotivController", "EVENTO EN VENTANA: onPause");

	}

	@Override
	public void onStop() {
		//Log.e("EmotivController", "EVENTO EN VENTANA: onStop");

		controller.disconnect();

	}

	@Override
	public void onDestroy() {
		Log.e("EmotivController", "EVENTO EN VENTANA: onDestroy");

		try {
			executorService.shutdownNow();
			while (!executorService.isTerminated()) {
				// Espero a que terminen de ejecutarse todos los procesos
				// para pasar a las siguientes instrucciones
			}

		} catch (Exception e) {
			;
		}

	}

	////////////////
	// FUNCTIONS //
	////////////////

	/**
	 * Save the user profile in the cloud
	 */
	@SimpleFunction(description = "Save the profile in the cloud", userVisible = true)
	public boolean SaveProfile() {
		boolean result = false;
		if (this.controller.isConnected()) {
			if (!this.controller.isCloudConnected()) {
				this.controller.login(username, password);
			}
			result = this.controller.saveOrUpdateProfile(profile);
		}
		return result;
	}

	/**
	 * Load the user profile in the cloud
	 */
	@SimpleFunction(description = "Load the profile in the cloud", userVisible = true)
	public boolean LoadProfile() {
		boolean result = false;
		if (this.controller.isConnected()) {
			if (!this.controller.isCloudConnected()) {
				this.controller.login(username, password);
			}
			result = this.controller.loadProfile(profile);
		}
		return result;
	}

	/**
	 * Delete the user profile in the cloud
	 */
	@SimpleFunction(description = "Delete the user profile in the cloud", userVisible = true)
	public boolean DeleteProfile() {
		boolean result = false;
		if (this.controller.isConnected()) {
			if (!this.controller.isCloudConnected()) {
				this.controller.login(username, password);
			}
			result = this.controller.deleteProfile(profile);
		}
		return result;
	}

	/**
	 * Delete the user profile in the cloud
	 */
	@SimpleFunction(description = "Obtain the list of user's profile", userVisible = true)
	public String ReadProfiles() {
		String result = "";
		if (this.controller.isConnected()) {
			if (!this.controller.isCloudConnected()) {
				this.controller.login(username, password);
			}
			result = this.controller.selectProfiles().toString().replace("[", "").replace("]", "");
		}
		return result;

	}

	/**
	 * Start to track the brain activity
	 */
	@SimpleFunction(description = "Start to track the brain activity", userVisible = true)
	public void Start() {

		// IRR: API level 18 is required. Android 4.3 Nivel de API 18 (julio
		// 2013)
		if (SdkLevel.getLevel() >= SdkLevel.LEVEL_JELLYBEAN_MR2) {

			mBluetoothAdapter = JellybeanMR2Util.obtainBluetoothAdapter(container.$context());

			if (!mBluetoothAdapter.isEnabled()) {
				if (!mBluetoothAdapter.isEnabled()) {
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

					container.$context().startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				}
			}

			// ActivityCompat.requestPermissions(container.$context(),
			// new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
			// Manifest.permission.ACCESS_FINE_LOCATION},
			// PERMISSIONS_CODE);

			controller.connect(this);

		}

		executorService.execute(controller);

	}

	/**
	 * Stop tracking the brain activity
	 */
	@SimpleFunction(description = "Stop tracking the brain activity", userVisible = true)
	public void Stop() {
		controller.disconnect();

	}

	@SimpleFunction(description = "Start training the neutral state", userVisible = true)
	public void BeginTrainingNeutralState() {
		this.controller.startTrainingNeutral();
	}

	@SimpleFunction(description = "Stop training the neutral state", userVisible = true)
	public void FinishTrainingNeutralState() {
		this.controller.stopTrainingNeutral();
	}

	@SimpleFunction(description = "Start training the given mental command", userVisible = true)
	public void TrainMentalCommand(String mentalCommand) {

		IEE_MentalCommandAction_t mc = EmotivController.TRAINABLE_MENTAL_COMMANDS.get(mentalCommand);

		if (mc != null) {

			controller.enableMentalcommandActions(mc);

			if (isTrainning) {
				isTrainning = controller.resetTrainingMentalCommand(mc);
			} else {
				isTrainning = controller.startTrainingMentalCommand(mc);

			}
		}

	}

	@SimpleFunction(description = "Start training the given facial expression", userVisible = true)
	public void TrainFacialExpression(String facialExpression) {

		IEE_FacialExpressionAlgo_t fe = EmotivController.TRAINABLE_FACIAL_EXPRESSIONS.get(facialExpression);

		if (fe != null) {
			controller.setFacialExpressionThreshold(fe,
					scalingFacialExpressionSensitivity(this.facialExpressionSensitivity));

			if (isTrainning) {
				isTrainning = controller.resetTrainingFacialExpression(fe);
			} else {
				isTrainning = controller.startTrainingFacialExpression(fe);

			}
		}

	}

	private int scalingMentalCommandSensitivity(int mcs) {
		return Math.round(1 + mcs * 6 / 100);
	}

	private int scalingFacialExpressionSensitivity(int fes) {
		return fes * SCALING_FE;
	}

	@SimpleFunction(description = "Accept the training of the facial expression", userVisible = true)
	public void AcceptTrainingFacialExpression() {
		controller.acceptTrainingFacialExpression();
	}

	@SimpleFunction(description = "Accept the training of the mental command", userVisible = true)
	public void AcceptTrainingMentalCommand() {
		controller.acceptTrainingMentalCommand();
	}

	@SimpleFunction(description = "Reject the training of the facial expression", userVisible = true)
	public void RejectTrainingFacialExpression() {
		controller.rejectTrainingFacialExpression();
	}

	@SimpleFunction(description = "Reject the training of the mental command", userVisible = true)
	public void RejectTrainingMentalCommand() {
		controller.rejectTrainingMentalCommand();
	}

	@SimpleFunction(description = "Clear the training of the given facial expression", userVisible = true)
	public void ClearTrainingFacialExpression(String facialExpression) {

		IEE_FacialExpressionAlgo_t fe = EmotivController.TRAINABLE_FACIAL_EXPRESSIONS.get(facialExpression);

		if (fe != null) {
			controller.clearTrainingFacialExpression(fe);

		}

	}

	@SimpleFunction(description = "Clear the training of the given mental command", userVisible = true)
	public void ClearTrainingMentalCommand(String mentalCommand) {

		IEE_MentalCommandAction_t mc = EmotivController.TRAINABLE_MENTAL_COMMANDS.get(mentalCommand);

		if (mc != null) {
			controller.clearTrainingMentalCommand(mc);

		}

	}

	@SimpleFunction(description = "Check if the given facial expression has been already trained", userVisible = true)
	public boolean CheckFacialExpressionTrained(String facialExpression) {

		IEE_FacialExpressionAlgo_t fe = EmotivController.TRAINABLE_FACIAL_EXPRESSIONS.get(facialExpression);

		if (fe != null) {
			return controller.checkTrainedFacialExpression(fe);

		} else {
			return false;
		}

	}

	@SimpleFunction(description = "Obtain the mental command skill rating [from 0.0 to 1.0]", userVisible = true)
	public float RetrieveTrainedMentalCommandRating(String mentalCommand) {

		IEE_MentalCommandAction_t mc = EmotivController.TRAINABLE_MENTAL_COMMANDS.get(mentalCommand);

		if (mc != null) {
			return controller.obtainTrainedMentalCommandRating(mc);
	
		} else {
			return 0;
		}

	}

	@SimpleFunction(description = "Check if the given mental command has been already trained", userVisible = true)
	public boolean CheckMentalCommandTrained(String mentalCommand) {

		IEE_MentalCommandAction_t mc = EmotivController.TRAINABLE_MENTAL_COMMANDS.get(mentalCommand);

		if (mc != null) {
			return controller.checkTrainedMentalCommand(mc);
		} else {
			return false;
		}

	}
	
	//TPM: Simulated event (30/05/2017)
	@SimpleEvent(description = "Returns the bands data in the time interval specified in TimeToUpdateBandsData property. The channels are sorted in the list as follows: AF3, F7, F3, FC5, T7, P7, Pz, O1, O2, P8, T8, FC6, F4, F8, AF4", userVisible = true)
	public void BandsDataChanged(List<Double> thetaBand, List<Double> alphaBand, List<Double> lowBetaBand, List<Double> highBetaBand, List<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "BandsDataChanged", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = "0")
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = false)
	public void TimeToStreamBandsData(int seconds) {
		new Timer().schedule(this.timer, 0, seconds * 1000);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel AF3.", userVisible = true)
	public void ChannelAF3Changed(double thetaBand, double alphaBand, double lowBetaBand, double highBetaBand, double gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelAF3Changed", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel AF3 (Stream mode on 10 secs).", userVisible = true)
	public void ChannelAF3ChangedStream(Stream<Double> thetaBand, Stream<Double> alphaBand, Stream<Double> lowBetaBand, Stream<Double> highBetaBand, Stream<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelAF3ChangedStream", thetaBand.toList(), alphaBand.toList(), lowBetaBand.toList(), highBetaBand.toList(), gammaBand.toList());
	}
	
	@SimpleEvent(description = "Returns the band values for the channel F7.", userVisible = true)
	public void ChannelF7Changed(double thetaBand, double alphaBand, double lowBetaBand, double highBetaBand, double gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelF7Changed", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel F7 (Stream mode on 10 secs).", userVisible = true)
	public void ChannelF7ChangedStream(Stream<Double> thetaBand, Stream<Double> alphaBand, Stream<Double> lowBetaBand, Stream<Double> highBetaBand, Stream<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelF7ChangedStream", thetaBand.toList(), alphaBand.toList(), lowBetaBand.toList(), highBetaBand.toList(), gammaBand.toList());
	}
	
	@SimpleEvent(description = "Returns the band values for the channel F3.", userVisible = true)
	public void ChannelF3Changed(double thetaBand, double alphaBand, double lowBetaBand, double highBetaBand, double gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelF3Changed", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel F3 (Stream mode on 10 secs).", userVisible = true)
	public void ChannelF3ChangedStream(Stream<Double> thetaBand, Stream<Double> alphaBand, Stream<Double> lowBetaBand, Stream<Double> highBetaBand, Stream<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelF3ChangedStream", thetaBand.toList(), alphaBand.toList(), lowBetaBand.toList(), highBetaBand.toList(), gammaBand.toList());
	}
	
	@SimpleEvent(description = "Returns the band values for the channel FC5.", userVisible = true)
	public void ChannelFC5Changed(double thetaBand, double alphaBand, double lowBetaBand, double highBetaBand, double gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelFC5Changed", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel FC5 (Stream mode on 10 secs).", userVisible = true)
	public void ChannelFC5ChangedStream(Stream<Double> thetaBand, Stream<Double> alphaBand, Stream<Double> lowBetaBand, Stream<Double> highBetaBand, Stream<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelFC5ChangedStream", thetaBand.toList(), alphaBand.toList(), lowBetaBand.toList(), highBetaBand.toList(), gammaBand.toList());
	}
	
	@SimpleEvent(description = "Returns the band values for the channel T7.", userVisible = true)
	public void ChannelT7Changed(double thetaBand, double alphaBand, double lowBetaBand, double highBetaBand, double gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelT7Changed", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel T7 (Stream mode on 10 secs).", userVisible = true)
	public void ChannelT7ChangedStream(Stream<Double> thetaBand, Stream<Double> alphaBand, Stream<Double> lowBetaBand, Stream<Double> highBetaBand, Stream<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelT7ChangedStream", thetaBand.toList(), alphaBand.toList(), lowBetaBand.toList(), highBetaBand.toList(), gammaBand.toList());
	}
	
	@SimpleEvent(description = "Returns the band values for the channel P7.", userVisible = true)
	public void ChannelP7Changed(double thetaBand, double alphaBand, double lowBetaBand, double highBetaBand, double gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelP7Changed", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel P7 (Stream mode on 10 secs).", userVisible = true)
	public void ChannelP7ChangedStream(Stream<Double> thetaBand, Stream<Double> alphaBand, Stream<Double> lowBetaBand, Stream<Double> highBetaBand, Stream<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelP7ChangedStream", thetaBand.toList(), alphaBand.toList(), lowBetaBand.toList(), highBetaBand.toList(), gammaBand.toList());
	}
	
	@SimpleEvent(description = "Returns the band values for the channel Pz.", userVisible = true)
	public void ChannelPzChanged(double thetaBand, double alphaBand, double lowBetaBand, double highBetaBand, double gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelPzChanged", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel Pz (Stream mode on 10 secs).", userVisible = true)
	public void ChannelPzChangedStream(Stream<Double> thetaBand, Stream<Double> alphaBand, Stream<Double> lowBetaBand, Stream<Double> highBetaBand, Stream<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelPzChangedStream", thetaBand.toList(), alphaBand.toList(), lowBetaBand.toList(), highBetaBand.toList(), gammaBand.toList());
	}
	
	@SimpleEvent(description = "Returns the band values for the channel O1.", userVisible = true)
	public void ChannelO1Changed(double thetaBand, double alphaBand, double lowBetaBand, double highBetaBand, double gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelO1Changed", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel O1 (Stream mode on 10 secs).", userVisible = true)
	public void ChannelO1ChangedStream(Stream<Double> thetaBand, Stream<Double> alphaBand, Stream<Double> lowBetaBand, Stream<Double> highBetaBand, Stream<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelO1ChangedStream", thetaBand.toList(), alphaBand.toList(), lowBetaBand.toList(), highBetaBand.toList(), gammaBand.toList());
	}
	
	@SimpleEvent(description = "Returns the band values for the channel O2.", userVisible = true)
	public void ChannelO2Changed(double thetaBand, double alphaBand, double lowBetaBand, double highBetaBand, double gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelO2Changed", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel O2 (Stream mode on 10 secs).", userVisible = true)
	public void ChannelO2ChangedStream(Stream<Double> thetaBand, Stream<Double> alphaBand, Stream<Double> lowBetaBand, Stream<Double> highBetaBand, Stream<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelO2ChangedStream", thetaBand.toList(), alphaBand.toList(), lowBetaBand.toList(), highBetaBand.toList(), gammaBand.toList());
	}
	
	@SimpleEvent(description = "Returns the band values for the channel P8.", userVisible = true)
	public void ChannelP8Changed(double thetaBand, double alphaBand, double lowBetaBand, double highBetaBand, double gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelP8Changed", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel P8 (Stream mode on 10 secs).", userVisible = true)
	public void ChannelP8ChangedStream(Stream<Double> thetaBand, Stream<Double> alphaBand, Stream<Double> lowBetaBand, Stream<Double> highBetaBand, Stream<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelP8ChangedStream", thetaBand.toList(), alphaBand.toList(), lowBetaBand.toList(), highBetaBand.toList(), gammaBand.toList());
	}
	
	@SimpleEvent(description = "Returns the band values for the channel T8.", userVisible = true)
	public void ChannelT8Changed(double thetaBand, double alphaBand, double lowBetaBand, double highBetaBand, double gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelT8Changed", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel T8 (Stream mode on 10 secs).", userVisible = true)
	public void ChannelT8ChangedStream(Stream<Double> thetaBand, Stream<Double> alphaBand, Stream<Double> lowBetaBand, Stream<Double> highBetaBand, Stream<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelT8ChangedStream", thetaBand.toList(), alphaBand.toList(), lowBetaBand.toList(), highBetaBand.toList(), gammaBand.toList());
	}
	
	@SimpleEvent(description = "Returns the band values for the channel FC6.", userVisible = true)
	public void ChannelFC6Changed(double thetaBand, double alphaBand, double lowBetaBand, double highBetaBand, double gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelFC6Changed", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel FC6 (Stream mode on 10 secs).", userVisible = true)
	public void ChannelFC6ChangedStream(Stream<Double> thetaBand, Stream<Double> alphaBand, Stream<Double> lowBetaBand, Stream<Double> highBetaBand, Stream<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelFC6ChangedStream", thetaBand.toList(), alphaBand.toList(), lowBetaBand.toList(), highBetaBand.toList(), gammaBand.toList());
	}
	
	@SimpleEvent(description = "Returns the band values for the channel F4.", userVisible = true)
	public void ChannelF4Changed(double thetaBand, double alphaBand, double lowBetaBand, double highBetaBand, double gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelF4Changed", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel F4 (Stream mode on 10 secs).", userVisible = true)
	public void ChannelF4ChangedStream(Stream<Double> thetaBand, Stream<Double> alphaBand, Stream<Double> lowBetaBand, Stream<Double> highBetaBand, Stream<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelF4ChangedStream", thetaBand.toList(), alphaBand.toList(), lowBetaBand.toList(), highBetaBand.toList(), gammaBand.toList());
	}
	
	@SimpleEvent(description = "Returns the band values for the channel F8.", userVisible = true)
	public void ChannelF8Changed(double thetaBand, double alphaBand, double lowBetaBand, double highBetaBand, double gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelF8Changed", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel F8 (Stream mode on 10 secs).", userVisible = true)
	public void ChannelF8ChangedStream(Stream<Double> thetaBand, Stream<Double> alphaBand, Stream<Double> lowBetaBand, Stream<Double> highBetaBand, Stream<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelF8ChangedStream", thetaBand.toList(), alphaBand.toList(), lowBetaBand.toList(), highBetaBand.toList(), gammaBand.toList());
	}
	
	@SimpleEvent(description = "Returns the band values for the channel AF4.", userVisible = true)
	public void ChannelAF4Changed(double thetaBand, double alphaBand, double lowBetaBand, double highBetaBand, double gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelAF4Changed", thetaBand, alphaBand, lowBetaBand, highBetaBand, gammaBand);
	}
	
	@SimpleEvent(description = "Returns the band values for the channel AF4 (Stream mode on 10 secs).", userVisible = true)
	public void ChannelAF4ChangedStream(Stream<Double> thetaBand, Stream<Double> alphaBand, Stream<Double> lowBetaBand, Stream<Double> highBetaBand, Stream<Double> gammaBand) {
		EventDispatcher.dispatchEvent(this, "ChannelAF4ChangedStream", thetaBand.toList(), alphaBand.toList(), lowBetaBand.toList(), highBetaBand.toList(), gammaBand.toList());
	}
	

	@SimpleFunction(description = "Retrieve the theta band data for the given channel", userVisible = true)
	public double RetrieveThetaBand(String channelName) {
		return obtainBandValue(channelName, 0);
	}

	@SimpleFunction(description = "Retrieve the alpha band data for the given channel", userVisible = true)
	public double RetrieveAlphaBand(String channelName) {
		return obtainBandValue(channelName, 1);
	}

	@SimpleFunction(description = "Retrieve the low band band data for the given channel", userVisible = true)
	public double RetrieveLowBetaBand(String channelName) {
		return obtainBandValue(channelName, 2);
	}

	@SimpleFunction(description = "Retrieve the high beta band data for the given channel", userVisible = true)
	public double RetrieveHighBetaBand(String channelName) {
		return obtainBandValue(channelName, 3);
	}

	@SimpleFunction(description = "Retrieve the gamma band data for the given channel", userVisible = true)
	public double RetrieveGammaBand(String channelName) {
		return obtainBandValue(channelName, 4);
	}

	@SimpleFunction(description = "Retrieve the quality of the given channel (4 good, 3 fair, 2 poor, 1 very poor, 0 no signal", userVisible = true)
	public int RetrieveContactQuality(String channelName) {

		int result = -1;

		IEE_DataChannel_t channel = EmotivController.DATA_CHANNEL.get(channelName);

		if (channel != null && data.getContactQuality() != null && data.getContactQuality().size() > 0) {
			result = data.getContactQuality().get(channel).intValue();
		}
		return result;

	}

	////////////////////////////////////////////
	// EVENTS FOR DISCRETE FACIAL EXPRESSIONS //
	////////////////////////////////////////////

	@SimpleEvent(description = "When any user's facial expression has been detected. Power between 0.0 and 1.0", userVisible = true)
	public void FacialExpressionDetected(String facialExpression, float power) {
		EventDispatcher.dispatchEvent(this, "FacialExpressionDetected", facialExpression, power);
	}

	/**
	 * Event to be raised when the user is looking up expression
	 */
	@SimpleEvent(description = "when the user is looking up", userVisible = true)
	public void LookingUp() {
		EventDispatcher.dispatchEvent(this, "LookingUp");
	}

	/**
	 * Event to be raised when the user is looking down expression
	 */
	@SimpleEvent(description = "when the user is looking down", userVisible = true)
	public void LookingDown() {
		EventDispatcher.dispatchEvent(this, "LookingDown");
	}

	/**
	 * Event to be raised when a blink is detected in the user's facial
	 * expression
	 */
	@SimpleEvent(description = "When a blink is detected in the user's facial expression", userVisible = true)
	public void Blink() {
		EventDispatcher.dispatchEvent(this, "Blink");
	}

	/**
	 * Event to be raised when a left wink is detected in the user's facial
	 * expression
	 */
	@SimpleEvent(description = "When a left wink is detected in the user's facial expression", userVisible = true)
	public void LeftWink() {
		EventDispatcher.dispatchEvent(this, "LeftWink");
	}

	/**
	 * Event to be raised when a right wink is detected in the user's facial
	 * expression
	 */
	@SimpleEvent(description = "When a right wink is detected in the user's facial expression", userVisible = true)
	public void RightWink() {
		EventDispatcher.dispatchEvent(this, "RightWink");
	}

	///////////////////////////////////////////////
	// EVENTS FOR CONTINUOUS FACIAL EXPRESSIONS //
	//////////////////////////////////////////////

	@SimpleEvent(description = "When the user's facial expression is neutral. Power between 0.0 and 1.0", userVisible = true)
	public void NeutralExpression(float power) {
		EventDispatcher.dispatchEvent(this, "NeutralExpression", power);
	}

	@SimpleEvent(description = "When the user is smiling. Power between 0.0 and 1.0", userVisible = true)
	public void Smile(float power) {
		EventDispatcher.dispatchEvent(this, "Smile", power);
	}

	@SimpleEvent(description = "When the user is frowing. Power between 0.0 and 1.0", userVisible = true)
	public void Frown(float power) {
		EventDispatcher.dispatchEvent(this, "Frown", power);
	}

	@SimpleEvent(description = "When the user is a clenching. Power between 0.0 and 1.0", userVisible = true)
	public void Clench(float power) {
		EventDispatcher.dispatchEvent(this, "Clench", power);
	}

	///////////////////////////////
	// EVENTS FOR MENTAL COMMANDS //
	///////////////////////////////

	/**
	 * Event to be raised when any mental command state is detected in the
	 * user's brain
	 */
	@SimpleEvent(description = "When any mental command is detected in the user's brain", userVisible = true)
	public void MentalCommandDetected(String mentalCommand, float power) {
		EventDispatcher.dispatchEvent(this, "MentalCommandDetected", mentalCommand, power);
	}

	/**
	 * Event to be raised when a neutral mental command state is detected in the
	 * user's brain
	 */
	@SimpleEvent(description = "When a neutral mental command is detected in the user's brain", userVisible = true)
	public void NeutralCommand(float power) {
		EventDispatcher.dispatchEvent(this, "NeutralCommand", power);
	}

	/**
	 * Event to be raised when a Disappear mental command is detected in the
	 * user's brain
	 */
	@SimpleEvent(description = "When a Disappear mental command is detected in the user's brain", userVisible = true)
	public void DisappearCommand(float power) {
		EventDispatcher.dispatchEvent(this, "DisappearCommand", power);
	}

	/**
	 * Event to be raised when a drop mental command is detected in the user's
	 * brain
	 */
	@SimpleEvent(description = "When a drop mental command is detected in the user's brain", userVisible = true)
	public void DropCommand(float power) {
		EventDispatcher.dispatchEvent(this, "DropCommand", power);
	}

	/**
	 * Event to be raised when a lift mental command is detected in the user's
	 * brain
	 */
	@SimpleEvent(description = "When a lift mental command is detected in the user's brain", userVisible = true)
	public void LiftCommand(float power) {
		EventDispatcher.dispatchEvent(this, "LiftCommand", power);
	}

	/**
	 * Event to be raised when a Left mental command is detected in the user's
	 * brain
	 */
	@SimpleEvent(description = "When a Left mental command is detected in the user's brain", userVisible = true)
	public void LeftCommand(float power) {
		EventDispatcher.dispatchEvent(this, "LeftCommand", power);
	}

	/**
	 * Event to be raised when a Right mental command is detected in the user's
	 * brain
	 */
	@SimpleEvent(description = "When a Right mental command is detected in the user's brain", userVisible = true)
	public void RightCommand(float power) {
		EventDispatcher.dispatchEvent(this, "RightCommand", power);
	}

	/**
	 * Event to be raised when a Push mental command is detected in the user's
	 * brain
	 */
	@SimpleEvent(description = "When a Push mental command is detected in the user's brain", userVisible = true)
	public void PushCommand(float power) {
		EventDispatcher.dispatchEvent(this, "PushCommand", power);
	}

	/**
	 * Event to be raised when a Pull mental command is detected in the user's
	 * brain
	 */
	@SimpleEvent(description = "When a Pull mental command is detected in the user's brain", userVisible = true)
	public void PullCommand(float power) {
		EventDispatcher.dispatchEvent(this, "PullCommand", power);
	}

	/**
	 * Event to be raised when a RotateClockwise mental command is detected in
	 * the user's brain
	 */
	@SimpleEvent(description = "When a RotateClockwise mental command is detected in the user's brain", userVisible = true)
	public void RotateClockwiseCommand(float power) {
		EventDispatcher.dispatchEvent(this, "RotateClockwiseCommand", power);
	}

	/**
	 * Event to be raised when a RotateCounterClockwise mental command is
	 * detected in the user's brain
	 */
	@SimpleEvent(description = "When a RotateCounterClockwise mental command is detected in the user's brain", userVisible = true)
	public void RotateCounterClockwiseCommand(float power) {
		EventDispatcher.dispatchEvent(this, "RotateCounterClockwiseCommand", power);
	}

	/**
	 * Event to be raised when a RotateForwards mental command is detected in
	 * the user's brain
	 */
	@SimpleEvent(description = "When a RotateForwards mental command is detected in the user's brain", userVisible = true)
	public void RotateForwardsCommand(float power) {
		EventDispatcher.dispatchEvent(this, "RotateForwardsCommand", power);
	}

	/**
	 * Event to be raised when a RotateReverse mental command is detected in the
	 * user's brain
	 */
	@SimpleEvent(description = "When a RotateReverse mental command is detected in the user's brain", userVisible = true)
	public void RotateReverseCommand(float power) {
		EventDispatcher.dispatchEvent(this, "RotateReverseCommand", power);
	}

	/**
	 * Event to be raised when a RotateLeft mental command is detected in the
	 * user's brain
	 */
	@SimpleEvent(description = "When a RotateLeft mental command is detected in the user's brain", userVisible = true)
	public void RotateLeftCommand(float power) {
		EventDispatcher.dispatchEvent(this, "RotateLeftCommand", power);
	}

	/**
	 * Event to be raised when a RotateRight mental command is detected in the
	 * user's brain
	 */
	@SimpleEvent(description = "When a RotateRight mental command is detected in the user's brain", userVisible = true)
	public void RotateRightCommand(float power) {
		EventDispatcher.dispatchEvent(this, "RotateRightCommand", power);
	}

	/**
	 * Event to be raised after the device has been connected
	 */
	@SimpleEvent(description = "Event to be raised after the device has been connected", userVisible = true)
	public void DeviceConnected() {
		EventDispatcher.dispatchEvent(this, "DeviceConnected");
	}

	/**
	 * Event to be raised after the device has been disconnected
	 */
	@SimpleEvent(description = "Event to be raised after the device has been disconnected", userVisible = true)
	public void DeviceDisconnected() {
		Stop();

		EventDispatcher.dispatchEvent(this, "DeviceDisconnected");
	}


	/**
	 * Event to be raised after the training has been reset
	 */
	@SimpleEvent(description = "Event to be raised after the training has been reset", userVisible = true)
	public void TrainingReset() {
		isTrainning = false;
		EventDispatcher.dispatchEvent(this, "TrainingReset");
	}

	/**
	 * Event to be raised after the training has started
	 */
	@SimpleEvent(description = "Event to be raised after the training has started", userVisible = true)
	public void TrainingStarted() {
		EventDispatcher.dispatchEvent(this, "TrainingStarted");
	}

	/**
	 * Event to be raised after the training has failed
	 */
	@SimpleEvent(description = "Event to be raised after the training has failed", userVisible = true)
	public void TrainingFailed() {
		isTrainning = false;
		EventDispatcher.dispatchEvent(this, "TrainingFailed");

	}

	/**
	 * Event to be raised after the training has been completed successfully
	 */
	@SimpleEvent(description = "Event to be raised after the training has been completed sucessfully", userVisible = true)
	public void TrainingSucceded() {
		isTrainning = false;

		EventDispatcher.dispatchEvent(this, "TrainingSucceded");
	}

	/**
	 * Event to be raised after the training has been rejected
	 */
	@SimpleEvent(description = "Event to be raised after the training has been rejected", userVisible = true)
	public void TrainingRejected() {
		isTrainning = false;

		EventDispatcher.dispatchEvent(this, "TrainingRejected");
	}

	/**
	 * Event to be raised after the training has been completed
	 */
	@SimpleEvent(description = "Event to be raised after the training has been completed", userVisible = true)
	public void TrainingCompleted() {
		isTrainning = false;
		EventDispatcher.dispatchEvent(this, "TrainingCompleted");
	}

	/**
	 * Event to be raised after the training has been erased
	 */
	@SimpleEvent(description = "Event to be raised after the training has been erased", userVisible = true)
	public void TrainingErased() {
		isTrainning = false;

		EventDispatcher.dispatchEvent(this, "TrainingErased");
	}

	////////////////
	// PROPERTIES //
	////////////////

	public double obtainBandValue(String channelName, int band) {

		double result = -1.0;

		IEE_DataChannel_t channel = EmotivController.DATA_CHANNEL.get(channelName);

		if (channel != null && data.getFFTData() != null && data.getFFTData().size() > 0) {
			if (data.getFFTData().get(channel) != null && data.getFFTData().get(channel).length > 0) {
				result = data.getFFTData().get(channel)[band];

			}
		}

		return result;

	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public String InfoDataChannels() {
		return Arrays.toString(EmotivController.DATA_CHANNEL.keySet().toArray()).replace("[", "").replace("]", "");
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public String InfoTrainableMentalCommands() {
		return Arrays.toString(EmotivController.TRAINABLE_MENTAL_COMMANDS.keySet().toArray()).replace("[", "")
				.replace("]", "");
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public String InfoTrainableFacialExpressions() {
		return Arrays.toString(EmotivController.TRAINABLE_FACIAL_EXPRESSIONS.keySet().toArray()).replace("[", "")
				.replace("]", "");
	}

	// Set the overall sensitivity (0-100) for all MentalCommand actions
	// En emotiv se usa (lowest: 1, highest: 7)
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = 50 + "")
	@SimpleProperty(userVisible = true)
	public void MentalComandSensitivity(int sensitivity) {
		this.mentalComandSensitivity = sensitivity;

		this.controller.setActivationLevel(scalingMentalCommandSensitivity(sensitivity));

	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public int MentalComandSensitivity() {
		return this.mentalComandSensitivity;
	}

	// Set the overall sensitivity (0 - 100) for all facial expressions -
	// threshold value
	// En EMOTIV se usa min: 0, max: 1000
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = 10 + "")
	@SimpleProperty(userVisible = true)
	public void FacialExpressionSensitivity(int sensitivity) {
		this.facialExpressionSensitivity = sensitivity;
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public int FacialExpressionSensitivity() {
		return this.facialExpressionSensitivity;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_EEG, defaultValue = Component.EMOTIV_EPOC + "")
	@SimpleProperty(userVisible = true)
	public void EEGDeviceType(int eeg) {
		this.eegDeviceType = eeg;
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public int EEGDeviceType() {
		return eegDeviceType;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void Username(String data) {
		this.username = data;
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public String Username() {
		return username;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void Password(String data) {
		this.password = data;
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public String Password() {
		return password;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void Profile(String data) {
		this.profile = data;
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public String Profile() {
		return profile;
	}

	/**
	 * @return the batteryLevel
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public int InfoBattery() {
		return data.getBatteryLevel();
	}

	/**
	 * @return the meditationLevel
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public float MeditationLevel() {
		return data.getMeditationLevel();
	}

	/**
	 * @return the excitamentLevel
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public float ExcitamentLevel() {
		return data.getExcitamentLevel();
	}

	/**
	 * @return the interestLevel (valence)
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public float InterestLevel() {
		return data.getInterestLevel();
	}

	/**
	 * @return the stressLevel (frustation)
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public float StressLevel() {
		return data.getStressLevel();
	}

	/**
	 * @return the engagementLevel
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public float EngagementLevel() {
		return data.getEngagementLevel();
	}

	/**
	 * @return the attentionLevel (focus)
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public float AttentionLevel() {
		return data.getAttentionLevel();
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public final double XAngularVelocity() {
		return data.getXAngularVelocity();
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public final double YAngularVelocity() {
		return data.getYAngularVelocity();
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public final double ZAngularVelocity() {
		return data.getZAngularVelocity();
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public final double XAcceleration() {
		return data.getXAcceleration();
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public final double YAcceleration() {
		return data.getYAcceleration();
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public final double ZAcceleration() {
		return data.getZAcceleration();
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public final double XMagnetization() {
		return data.getXMagnetization();
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public final double YMagnetization() {
		return data.getYMagnetization();
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public final double ZMagnetization() {
		return data.getZMagnetization();
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public float InfoRunningTime() {
		return data.getRunningTime();
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public boolean IsTrainning() {
		return isTrainning;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
	@SimpleProperty(userVisible = true)
	public void TriggerNeutralState(boolean flag) {
		this.triggerNeutralState = flag;
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public boolean TriggerNeutralState() {
		return triggerNeutralState;
	}

	// star send to sensor
	public final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.obj != null) {
				data = (EmotivData) msg.obj;
			}

			switch (msg.what) {
			case HANDLER_USER_ADDED:
				DeviceConnected();
				break;
			case HANDLER_USER_REMOVED:
				DeviceDisconnected();
				break;
			case HANDLER_TRAINED_START:
				TrainingStarted();
				break;
			case HANDLER_TRAINED_SUCCEED:
				TrainingSucceded();
				break;
			case HANDLER_TRAINED_ERASED:
				TrainingErased();
				break;
			case HANDLER_TRAINED_REJECT:
				TrainingRejected();
				break;
			case HANDLER_TRAINED_COMPLETE:
				TrainingCompleted();
				break;
			case HANDLER_EXPRESSION_BLINK:
				Blink();
				FacialExpressionDetected("Blink", 1);
				break;
			case HANDLER_EXPRESSION_LEFTWINK:
				LeftWink();
				FacialExpressionDetected("Left Wink", 1);
				break;
			case HANDLER_EXPRESSION_RIGHTWINK:
				RightWink();
				FacialExpressionDetected("Right Wink", 1);
				break;
			case HANDLER_EXPRESSION_LOOKINGDOWN:
				LookingDown();
				FacialExpressionDetected("Looking Down", 1);
				break;
			case HANDLER_EXPRESSION_LOOKINGUP:
				LookingUp();
				FacialExpressionDetected("Looking Up", 1);
				break;
			case HANDLER_EXPRESSION_SMILING:
				Smile(data.getPowerForSimileExpression());
				FacialExpressionDetected("Smile", data.getPowerForSimileExpression());
				break;
			case HANDLER_EXPRESSION_FROWNING:
				Frown(data.getPowerForFrownExpression());
				FacialExpressionDetected("Frown", data.getPowerForFrownExpression());
				break;
			case HANDLER_EXPRESSION_CLENCHING:
				Clench(data.getPowerForClenchExpression());
				FacialExpressionDetected("Clench", data.getPowerForClenchExpression());
				break;
			case HANDLER_EXPRESSION_NEUTRAL:
				if (triggerNeutralState) {
					NeutralExpression(data.getPowerForNeutralExpression());
					FacialExpressionDetected("Neutral", data.getPowerForNeutralExpression());
				}
				break;
			case HANDLER_COMMAND_DISAPPEAR:
				DisappearCommand(data.getPowerForDisappearCommand());
				MentalCommandDetected("Disappear", data.getPowerForDisappearCommand());
				break;
			case HANDLER_COMMAND_DROP:
				DropCommand(data.getPowerForDropCommand());
				MentalCommandDetected("Drop", data.getPowerForDropCommand());
				break;
			case HANDLER_COMMAND_LEFT:
				LeftCommand(data.getPowerForLeftCommand());
				MentalCommandDetected("Left", data.getPowerForLeftCommand());
				break;
			case HANDLER_COMMAND_LIFT:
				LiftCommand(data.getPowerForLiftCommand());
				MentalCommandDetected("Lift", data.getPowerForLiftCommand());
				break;
			case HANDLER_COMMAND_NEUTRAL:
				if (triggerNeutralState) {
					NeutralCommand(data.getPowerForNeutralCommand());
					MentalCommandDetected("Neutral", data.getPowerForNeutralCommand());
				}
				break;
			case HANDLER_COMMAND_PULL:
				PullCommand(data.getPowerForPullCommand());
				MentalCommandDetected("Pull", data.getPowerForPullCommand());
				break;
			case HANDLER_COMMAND_PUSH:
				PushCommand(data.getPowerForPushCommand());
				MentalCommandDetected("Push", data.getPowerForPushCommand());
				break;
			case HANDLER_COMMAND_RIGHT:
				RightCommand(data.getPowerForRightCommand());
				MentalCommandDetected("Right", data.getPowerForRightCommand());
				break;
			case HANDLER_COMMAND_ROTATE_CLOCKWISE:
				RotateClockwiseCommand(data.getPowerForRotateClockwiseCommand());
				MentalCommandDetected("Rotate Clockwise", data.getPowerForRotateClockwiseCommand());
				break;
			case HANDLER_COMMAND_ROTATE_COUNTER_CLOCKWISE:
				RotateCounterClockwiseCommand(data.getPowerForRotateCounterClockwiseCommand());
				MentalCommandDetected("Rotate Counterclockwise", data.getPowerForRotateCounterClockwiseCommand());
				break;
			case HANDLER_COMMAND_ROTATE_FORWARDS:
				RotateForwardsCommand(data.getPowerForRotateForwardsCommand());
				MentalCommandDetected("Rotate Fordwards", data.getPowerForRotateForwardsCommand());
				break;
			case HANDLER_COMMAND_ROTATE_LEFT:
				RotateLeftCommand(data.getPowerForRotateLeftCommand());
				MentalCommandDetected("Rotate Left", data.getPowerForRotateLeftCommand());
				break;
			case HANDLER_COMMAND_ROTATE_REVERSE:
				RotateReverseCommand(data.getPowerForRotateReverseCommand());
				MentalCommandDetected("Rotate Reverse", data.getPowerForRotateReverseCommand());
				break;
			case HANDLER_COMMAND_ROTATE_RIGHT:
				RotateRightCommand(data.getPowerForRotateRightCommand());
				MentalCommandDetected("Rotate Right", data.getPowerForRotateRightCommand());
				break;
			case HANDLER_CHANNELS_DATA:
				
				//1) Channel AF3:
				double thetaBand = obtainBandValue("AF3", 0);
				double alphaBand = obtainBandValue("AF3", 1);
				double lowBetaBand = obtainBandValue("AF3", 2);
				double highBetaBand = obtainBandValue("AF3", 3);
				double gammaBand = obtainBandValue("AF3", 4);
				
				ChannelAF3Changed(thetaBand, alphaBand, lowBetaBand,
						highBetaBand, gammaBand);
				
				List<Double> thetaBandStream = Arrays.asList(thetaBand);
				List<Double> alphaBandStream = Arrays.asList(alphaBand);
				List<Double> lowBetaBandStream = Arrays.asList(lowBetaBand);
				List<Double> highBetaBandStream = Arrays.asList(highBetaBand);
				List<Double> gammaBandStream = Arrays.asList(gammaBand);
				
				if(channelAF3AlphaStream != null && channelAF3GammaStream != null &&
						channelAF3HighBetaStream != null && channelAF3LowBetaStream != null &&
						channelAF3ThetaStream != null) {					
					channelAF3AlphaStream = Stream.concat(Stream.of(alphaBandStream), channelAF3AlphaStream);
					channelAF3GammaStream = Stream.concat(Stream.of(gammaBandStream), channelAF3GammaStream);
					channelAF3HighBetaStream = Stream.concat(Stream.of(highBetaBandStream), channelAF3HighBetaStream);
					channelAF3LowBetaStream = Stream.concat(Stream.of(lowBetaBandStream), channelAF3LowBetaStream);
					channelAF3ThetaStream = Stream.concat(Stream.of(thetaBandStream), channelAF3ThetaStream);
					
				} else {
					channelAF3AlphaStream = Stream.of(alphaBandStream);
					channelAF3GammaStream = Stream.of(gammaBandStream);
					channelAF3HighBetaStream = Stream.of(highBetaBandStream);
					channelAF3LowBetaStream = Stream.of(lowBetaBandStream);
					channelAF3ThetaStream = Stream.of(thetaBandStream);
				}
				
				//2) Channel F7:
				thetaBand = obtainBandValue("F7", 0);
				alphaBand = obtainBandValue("F7", 1);
				lowBetaBand = obtainBandValue("F7", 2);
				highBetaBand = obtainBandValue("F7", 3);
				gammaBand = obtainBandValue("F7", 4);
				
				ChannelF7Changed(thetaBand, alphaBand, lowBetaBand,
						highBetaBand, gammaBand);
				
				thetaBandStream = Arrays.asList(thetaBand);
				alphaBandStream = Arrays.asList(alphaBand);
				lowBetaBandStream = Arrays.asList(lowBetaBand);
				highBetaBandStream = Arrays.asList(highBetaBand);
				gammaBandStream = Arrays.asList(gammaBand);
				
				if(channelF7AlphaStream != null && channelF7GammaStream != null &&
						channelF7HighBetaStream != null && channelF7LowBetaStream != null &&
						channelF7ThetaStream != null) {					
					channelF7AlphaStream = Stream.concat(Stream.of(alphaBandStream), channelF7AlphaStream);
					channelF7GammaStream = Stream.concat(Stream.of(gammaBandStream), channelF7GammaStream);
					channelF7HighBetaStream = Stream.concat(Stream.of(highBetaBandStream), channelF7HighBetaStream);
					channelF7LowBetaStream = Stream.concat(Stream.of(lowBetaBandStream), channelF7LowBetaStream);
					channelF7ThetaStream = Stream.concat(Stream.of(thetaBandStream), channelF7ThetaStream);
					
				} else {
					channelF7AlphaStream = Stream.of(alphaBandStream);
					channelF7GammaStream = Stream.of(gammaBandStream);
					channelF7HighBetaStream = Stream.of(highBetaBandStream);
					channelF7LowBetaStream = Stream.of(lowBetaBandStream);
					channelF7ThetaStream = Stream.of(thetaBandStream);
				}
				
				//3) Channel F3:
				thetaBand = obtainBandValue("F3", 0);
				alphaBand = obtainBandValue("F3", 1);
				lowBetaBand = obtainBandValue("F3", 2);
				highBetaBand = obtainBandValue("F3", 3);
				gammaBand = obtainBandValue("F3", 4);
				
				ChannelF3Changed(thetaBand, alphaBand, lowBetaBand,
						highBetaBand, gammaBand);
				
				thetaBandStream = Arrays.asList(thetaBand);
				alphaBandStream = Arrays.asList(alphaBand);
				lowBetaBandStream = Arrays.asList(lowBetaBand);
				highBetaBandStream = Arrays.asList(highBetaBand);
				gammaBandStream = Arrays.asList(gammaBand);
				
				if(channelF3AlphaStream != null && channelF3GammaStream != null &&
						channelF3HighBetaStream != null && channelF3LowBetaStream != null &&
						channelF3ThetaStream != null) {					
					channelF3AlphaStream = Stream.concat(Stream.of(alphaBandStream), channelF3AlphaStream);
					channelF3GammaStream = Stream.concat(Stream.of(gammaBandStream), channelF3GammaStream);
					channelF3HighBetaStream = Stream.concat(Stream.of(highBetaBandStream), channelF3HighBetaStream);
					channelF3LowBetaStream = Stream.concat(Stream.of(lowBetaBandStream), channelF3LowBetaStream);
					channelF3ThetaStream = Stream.concat(Stream.of(thetaBandStream), channelF3ThetaStream);
					
				} else {
					channelF3AlphaStream = Stream.of(alphaBandStream);
					channelF3GammaStream = Stream.of(gammaBandStream);
					channelF3HighBetaStream = Stream.of(highBetaBandStream);
					channelF3LowBetaStream = Stream.of(lowBetaBandStream);
					channelF3ThetaStream = Stream.of(thetaBandStream);
				}
				
				//4) Channel FC5:
				thetaBand = obtainBandValue("FC5", 0);
				alphaBand = obtainBandValue("FC5", 1);
				lowBetaBand = obtainBandValue("FC5", 2);
				highBetaBand = obtainBandValue("FC5", 3);
				gammaBand = obtainBandValue("FC5", 4);
				
				ChannelFC5Changed(thetaBand, alphaBand, lowBetaBand,
						highBetaBand, gammaBand);
				
				thetaBandStream = Arrays.asList(thetaBand);
				alphaBandStream = Arrays.asList(alphaBand);
				lowBetaBandStream = Arrays.asList(lowBetaBand);
				highBetaBandStream = Arrays.asList(highBetaBand);
				gammaBandStream = Arrays.asList(gammaBand);
				
				if(channelFC5AlphaStream != null && channelFC5GammaStream != null &&
						channelFC5HighBetaStream != null && channelFC5LowBetaStream != null &&
						channelFC5ThetaStream != null) {					
					channelFC5AlphaStream = Stream.concat(Stream.of(alphaBandStream), channelFC5AlphaStream);
					channelFC5GammaStream = Stream.concat(Stream.of(gammaBandStream), channelFC5GammaStream);
					channelFC5HighBetaStream = Stream.concat(Stream.of(highBetaBandStream), channelFC5HighBetaStream);
					channelFC5LowBetaStream = Stream.concat(Stream.of(lowBetaBandStream), channelFC5LowBetaStream);
					channelFC5ThetaStream = Stream.concat(Stream.of(thetaBandStream), channelFC5ThetaStream);
					
				} else {
					channelFC5AlphaStream = Stream.of(alphaBandStream);
					channelFC5GammaStream = Stream.of(gammaBandStream);
					channelFC5HighBetaStream = Stream.of(highBetaBandStream);
					channelFC5LowBetaStream = Stream.of(lowBetaBandStream);
					channelFC5ThetaStream = Stream.of(thetaBandStream);
				}
				
				//5) Channel T7:
				thetaBand = obtainBandValue("T7", 0);
				alphaBand = obtainBandValue("T7", 1);
				lowBetaBand = obtainBandValue("T7", 2);
				highBetaBand = obtainBandValue("T7", 3);
				gammaBand = obtainBandValue("T7", 4);
				
				ChannelT7Changed(thetaBand, alphaBand, lowBetaBand,
						highBetaBand, gammaBand);
				
				thetaBandStream = Arrays.asList(thetaBand);
				alphaBandStream = Arrays.asList(alphaBand);
				lowBetaBandStream = Arrays.asList(lowBetaBand);
				highBetaBandStream = Arrays.asList(highBetaBand);
				gammaBandStream = Arrays.asList(gammaBand);
				
				if(channelT7AlphaStream != null && channelT7GammaStream != null &&
						channelT7HighBetaStream != null && channelT7LowBetaStream != null &&
						channelT7ThetaStream != null) {					
					channelT7AlphaStream = Stream.concat(Stream.of(alphaBandStream), channelT7AlphaStream);
					channelT7GammaStream = Stream.concat(Stream.of(gammaBandStream), channelT7GammaStream);
					channelT7HighBetaStream = Stream.concat(Stream.of(highBetaBandStream), channelT7HighBetaStream);
					channelT7LowBetaStream = Stream.concat(Stream.of(lowBetaBandStream), channelT7LowBetaStream);
					channelT7ThetaStream = Stream.concat(Stream.of(thetaBandStream), channelT7ThetaStream);
					
				} else {
					channelT7AlphaStream = Stream.of(alphaBandStream);
					channelT7GammaStream = Stream.of(gammaBandStream);
					channelT7HighBetaStream = Stream.of(highBetaBandStream);
					channelT7LowBetaStream = Stream.of(lowBetaBandStream);
					channelT7ThetaStream = Stream.of(thetaBandStream);
				}
				
				//6) Channel P7:
				thetaBand = obtainBandValue("P7", 0);
				alphaBand = obtainBandValue("P7", 1);
				lowBetaBand = obtainBandValue("P7", 2);
				highBetaBand = obtainBandValue("P7", 3);
				gammaBand = obtainBandValue("P7", 4);
				
				ChannelP7Changed(thetaBand, alphaBand, lowBetaBand,
						highBetaBand, gammaBand);
				
				thetaBandStream = Arrays.asList(thetaBand);
				alphaBandStream = Arrays.asList(alphaBand);
				lowBetaBandStream = Arrays.asList(lowBetaBand);
				highBetaBandStream = Arrays.asList(highBetaBand);
				gammaBandStream = Arrays.asList(gammaBand);
				
				if(channelP7AlphaStream != null && channelP7GammaStream != null &&
						channelP7HighBetaStream != null && channelP7LowBetaStream != null &&
						channelP7ThetaStream != null) {					
					channelP7AlphaStream = Stream.concat(Stream.of(alphaBandStream), channelP7AlphaStream);
					channelP7GammaStream = Stream.concat(Stream.of(gammaBandStream), channelP7GammaStream);
					channelP7HighBetaStream = Stream.concat(Stream.of(highBetaBandStream), channelP7HighBetaStream);
					channelP7LowBetaStream = Stream.concat(Stream.of(lowBetaBandStream), channelP7LowBetaStream);
					channelP7ThetaStream = Stream.concat(Stream.of(thetaBandStream), channelP7ThetaStream);
					
				} else {
					channelP7AlphaStream = Stream.of(alphaBandStream);
					channelP7GammaStream = Stream.of(gammaBandStream);
					channelP7HighBetaStream = Stream.of(highBetaBandStream);
					channelP7LowBetaStream = Stream.of(lowBetaBandStream);
					channelP7ThetaStream = Stream.of(thetaBandStream);
				}
				
				//7) Channel Pz:
				thetaBand = obtainBandValue("Pz", 0);
				alphaBand = obtainBandValue("Pz", 1);
				lowBetaBand = obtainBandValue("Pz", 2);
				highBetaBand = obtainBandValue("Pz", 3);
				gammaBand = obtainBandValue("Pz", 4);
				
				ChannelPzChanged(thetaBand, alphaBand, lowBetaBand,
						highBetaBand, gammaBand);
				
				thetaBandStream = Arrays.asList(thetaBand);
				alphaBandStream = Arrays.asList(alphaBand);
				lowBetaBandStream = Arrays.asList(lowBetaBand);
				highBetaBandStream = Arrays.asList(highBetaBand);
				gammaBandStream = Arrays.asList(gammaBand);
				
				if(channelPzAlphaStream != null && channelPzGammaStream != null &&
						channelPzHighBetaStream != null && channelPzLowBetaStream != null &&
						channelPzThetaStream != null) {					
					channelPzAlphaStream = Stream.concat(Stream.of(alphaBandStream), channelPzAlphaStream);
					channelPzGammaStream = Stream.concat(Stream.of(gammaBandStream), channelPzGammaStream);
					channelPzHighBetaStream = Stream.concat(Stream.of(highBetaBandStream), channelPzHighBetaStream);
					channelPzLowBetaStream = Stream.concat(Stream.of(lowBetaBandStream), channelPzLowBetaStream);
					channelPzThetaStream = Stream.concat(Stream.of(thetaBandStream), channelPzThetaStream);
					
				} else {
					channelPzAlphaStream = Stream.of(alphaBandStream);
					channelPzGammaStream = Stream.of(gammaBandStream);
					channelPzHighBetaStream = Stream.of(highBetaBandStream);
					channelPzLowBetaStream = Stream.of(lowBetaBandStream);
					channelPzThetaStream = Stream.of(thetaBandStream);
				}
				
				//8) Channel O1:	
				thetaBand = obtainBandValue("O1", 0);
				alphaBand = obtainBandValue("O1", 1);
				lowBetaBand = obtainBandValue("O1", 2);
				highBetaBand = obtainBandValue("O1", 3);
				gammaBand = obtainBandValue("O1", 4);
				
				ChannelO1Changed(thetaBand, alphaBand, lowBetaBand,
						highBetaBand, gammaBand);
				
				thetaBandStream = Arrays.asList(thetaBand);
				alphaBandStream = Arrays.asList(alphaBand);
				lowBetaBandStream = Arrays.asList(lowBetaBand);
				highBetaBandStream = Arrays.asList(highBetaBand);
				gammaBandStream = Arrays.asList(gammaBand);
				
				if(channelO1AlphaStream != null && channelO1GammaStream != null &&
						channelO1HighBetaStream != null && channelO1LowBetaStream != null &&
						channelO1ThetaStream != null) {					
					channelO1AlphaStream = Stream.concat(Stream.of(alphaBandStream), channelO1AlphaStream);
					channelO1GammaStream = Stream.concat(Stream.of(gammaBandStream), channelO1GammaStream);
					channelO1HighBetaStream = Stream.concat(Stream.of(highBetaBandStream), channelO1HighBetaStream);
					channelO1LowBetaStream = Stream.concat(Stream.of(lowBetaBandStream), channelO1LowBetaStream);
					channelO1ThetaStream = Stream.concat(Stream.of(thetaBandStream), channelO1ThetaStream);
					
				} else {
					channelO1AlphaStream = Stream.of(alphaBandStream);
					channelO1GammaStream = Stream.of(gammaBandStream);
					channelO1HighBetaStream = Stream.of(highBetaBandStream);
					channelO1LowBetaStream = Stream.of(lowBetaBandStream);
					channelO1ThetaStream = Stream.of(thetaBandStream);
				}
				
				//9) Channel O2:
				thetaBand = obtainBandValue("O2", 0);
				alphaBand = obtainBandValue("O2", 1);
				lowBetaBand = obtainBandValue("O2", 2);
				highBetaBand = obtainBandValue("O2", 3);
				gammaBand = obtainBandValue("O2", 4);
				
				ChannelO2Changed(thetaBand, alphaBand, lowBetaBand,
						highBetaBand, gammaBand);
				
				thetaBandStream = Arrays.asList(thetaBand);
				alphaBandStream = Arrays.asList(alphaBand);
				lowBetaBandStream = Arrays.asList(lowBetaBand);
				highBetaBandStream = Arrays.asList(highBetaBand);
				gammaBandStream = Arrays.asList(gammaBand);
				
				if(channelO2AlphaStream != null && channelO2GammaStream != null &&
						channelO2HighBetaStream != null && channelO2LowBetaStream != null &&
						channelO2ThetaStream != null) {					
					channelO2AlphaStream = Stream.concat(Stream.of(alphaBandStream), channelO2AlphaStream);
					channelO2GammaStream = Stream.concat(Stream.of(gammaBandStream), channelO2GammaStream);
					channelO2HighBetaStream = Stream.concat(Stream.of(highBetaBandStream), channelO2HighBetaStream);
					channelO2LowBetaStream = Stream.concat(Stream.of(lowBetaBandStream), channelO2LowBetaStream);
					channelO2ThetaStream = Stream.concat(Stream.of(thetaBandStream), channelO2ThetaStream);
					
				} else {
					channelO2AlphaStream = Stream.of(alphaBandStream);
					channelO2GammaStream = Stream.of(gammaBandStream);
					channelO2HighBetaStream = Stream.of(highBetaBandStream);
					channelO2LowBetaStream = Stream.of(lowBetaBandStream);
					channelO2ThetaStream = Stream.of(thetaBandStream);
				}
				
				//10) Channel P8:
				thetaBand = obtainBandValue("P8", 0);
				alphaBand = obtainBandValue("P8", 1);
				lowBetaBand = obtainBandValue("P8", 2);
				highBetaBand = obtainBandValue("P8", 3);
				gammaBand = obtainBandValue("P8", 4);
				
				ChannelP8Changed(thetaBand, alphaBand, lowBetaBand,
						highBetaBand, gammaBand);
				
				thetaBandStream = Arrays.asList(thetaBand);
				alphaBandStream = Arrays.asList(alphaBand);
				lowBetaBandStream = Arrays.asList(lowBetaBand);
				highBetaBandStream = Arrays.asList(highBetaBand);
				gammaBandStream = Arrays.asList(gammaBand);
				
				if(channelP8AlphaStream != null && channelP8GammaStream != null &&
						channelP8HighBetaStream != null && channelP8LowBetaStream != null &&
						channelP8ThetaStream != null) {					
					channelP8AlphaStream = Stream.concat(Stream.of(alphaBandStream), channelP8AlphaStream);
					channelP8GammaStream = Stream.concat(Stream.of(gammaBandStream), channelP8GammaStream);
					channelP8HighBetaStream = Stream.concat(Stream.of(highBetaBandStream), channelP8HighBetaStream);
					channelP8LowBetaStream = Stream.concat(Stream.of(lowBetaBandStream), channelP8LowBetaStream);
					channelP8ThetaStream = Stream.concat(Stream.of(thetaBandStream), channelP8ThetaStream);
					
				} else {
					channelP8AlphaStream = Stream.of(alphaBandStream);
					channelP8GammaStream = Stream.of(gammaBandStream);
					channelP8HighBetaStream = Stream.of(highBetaBandStream);
					channelP8LowBetaStream = Stream.of(lowBetaBandStream);
					channelP8ThetaStream = Stream.of(thetaBandStream);
				}
				
				//11) Channel T8:
				thetaBand = obtainBandValue("T8", 0);
				alphaBand = obtainBandValue("T8", 1);
				lowBetaBand = obtainBandValue("T8", 2);
				highBetaBand = obtainBandValue("T8", 3);
				gammaBand = obtainBandValue("T8", 4);
				
				ChannelT8Changed(thetaBand, alphaBand, lowBetaBand,
						highBetaBand, gammaBand);
				
				thetaBandStream = Arrays.asList(thetaBand);
				alphaBandStream = Arrays.asList(alphaBand);
				lowBetaBandStream = Arrays.asList(lowBetaBand);
				highBetaBandStream = Arrays.asList(highBetaBand);
				gammaBandStream = Arrays.asList(gammaBand);
				
				if(channelT8AlphaStream != null && channelT8GammaStream != null &&
						channelT8HighBetaStream != null && channelT8LowBetaStream != null &&
						channelT8ThetaStream != null) {					
					channelT8AlphaStream = Stream.concat(Stream.of(alphaBandStream), channelT8AlphaStream);
					channelT8GammaStream = Stream.concat(Stream.of(gammaBandStream), channelT8GammaStream);
					channelT8HighBetaStream = Stream.concat(Stream.of(highBetaBandStream), channelT8HighBetaStream);
					channelT8LowBetaStream = Stream.concat(Stream.of(lowBetaBandStream), channelT8LowBetaStream);
					channelT8ThetaStream = Stream.concat(Stream.of(thetaBandStream), channelT8ThetaStream);
					
				} else {
					channelT8AlphaStream = Stream.of(alphaBandStream);
					channelT8GammaStream = Stream.of(gammaBandStream);
					channelT8HighBetaStream = Stream.of(highBetaBandStream);
					channelT8LowBetaStream = Stream.of(lowBetaBandStream);
					channelT8ThetaStream = Stream.of(thetaBandStream);
				}
				
				//12) Channel FC6:
				thetaBand = obtainBandValue("FC6", 0);
				alphaBand = obtainBandValue("FC6", 1);
				lowBetaBand = obtainBandValue("FC6", 2);
				highBetaBand = obtainBandValue("FC6", 3);
				gammaBand = obtainBandValue("FC6", 4);
				
				ChannelFC6Changed(thetaBand, alphaBand, lowBetaBand,
						highBetaBand, gammaBand);
				
				thetaBandStream = Arrays.asList(thetaBand);
				alphaBandStream = Arrays.asList(alphaBand);
				lowBetaBandStream = Arrays.asList(lowBetaBand);
				highBetaBandStream = Arrays.asList(highBetaBand);
				gammaBandStream = Arrays.asList(gammaBand);
				
				if(channelFC6AlphaStream != null && channelFC6GammaStream != null &&
						channelFC6HighBetaStream != null && channelFC6LowBetaStream != null &&
						channelFC6ThetaStream != null) {					
					channelFC6AlphaStream = Stream.concat(Stream.of(alphaBandStream), channelFC6AlphaStream);
					channelFC6GammaStream = Stream.concat(Stream.of(gammaBandStream), channelFC6GammaStream);
					channelFC6HighBetaStream = Stream.concat(Stream.of(highBetaBandStream), channelFC6HighBetaStream);
					channelFC6LowBetaStream = Stream.concat(Stream.of(lowBetaBandStream), channelFC6LowBetaStream);
					channelFC6ThetaStream = Stream.concat(Stream.of(thetaBandStream), channelFC6ThetaStream);
					
				} else {
					channelFC6AlphaStream = Stream.of(alphaBandStream);
					channelFC6GammaStream = Stream.of(gammaBandStream);
					channelFC6HighBetaStream = Stream.of(highBetaBandStream);
					channelFC6LowBetaStream = Stream.of(lowBetaBandStream);
					channelFC6ThetaStream = Stream.of(thetaBandStream);
				}
				
				//13) Channel F4:
				thetaBand = obtainBandValue("F4", 0);
				alphaBand = obtainBandValue("F4", 1);
				lowBetaBand = obtainBandValue("F4", 2);
				highBetaBand = obtainBandValue("F4", 3);
				gammaBand = obtainBandValue("F4", 4);
				
				ChannelF4Changed(thetaBand, alphaBand, lowBetaBand,
						highBetaBand, gammaBand);
				
				thetaBandStream = Arrays.asList(thetaBand);
				alphaBandStream = Arrays.asList(alphaBand);
				lowBetaBandStream = Arrays.asList(lowBetaBand);
				highBetaBandStream = Arrays.asList(highBetaBand);
				gammaBandStream = Arrays.asList(gammaBand);
				
				if(channelF4AlphaStream != null && channelF4GammaStream != null &&
						channelF4HighBetaStream != null && channelF4LowBetaStream != null &&
						channelF4ThetaStream != null) {					
					channelF4AlphaStream = Stream.concat(Stream.of(alphaBandStream), channelF4AlphaStream);
					channelF4GammaStream = Stream.concat(Stream.of(gammaBandStream), channelF4GammaStream);
					channelF4HighBetaStream = Stream.concat(Stream.of(highBetaBandStream), channelF4HighBetaStream);
					channelF4LowBetaStream = Stream.concat(Stream.of(lowBetaBandStream), channelF4LowBetaStream);
					channelF4ThetaStream = Stream.concat(Stream.of(thetaBandStream), channelF4ThetaStream);
					
				} else {
					channelF4AlphaStream = Stream.of(alphaBandStream);
					channelF4GammaStream = Stream.of(gammaBandStream);
					channelF4HighBetaStream = Stream.of(highBetaBandStream);
					channelF4LowBetaStream = Stream.of(lowBetaBandStream);
					channelF4ThetaStream = Stream.of(thetaBandStream);
				}
				
				//14) Channel F8:
				thetaBand = obtainBandValue("F8", 0);
				alphaBand = obtainBandValue("F8", 1);
				lowBetaBand = obtainBandValue("F8", 2);
				highBetaBand = obtainBandValue("F8", 3);
				gammaBand = obtainBandValue("F8", 4);
				
				ChannelF8Changed(thetaBand, alphaBand, lowBetaBand,
						highBetaBand, gammaBand);
				
				thetaBandStream = Arrays.asList(thetaBand);
				alphaBandStream = Arrays.asList(alphaBand);
				lowBetaBandStream = Arrays.asList(lowBetaBand);
				highBetaBandStream = Arrays.asList(highBetaBand);
				gammaBandStream = Arrays.asList(gammaBand);
				
				if(channelF8AlphaStream != null && channelF8GammaStream != null &&
						channelF8HighBetaStream != null && channelF8LowBetaStream != null &&
						channelF8ThetaStream != null) {					
					channelF8AlphaStream = Stream.concat(Stream.of(alphaBandStream), channelF8AlphaStream);
					channelF8GammaStream = Stream.concat(Stream.of(gammaBandStream), channelF8GammaStream);
					channelF8HighBetaStream = Stream.concat(Stream.of(highBetaBandStream), channelF8HighBetaStream);
					channelF8LowBetaStream = Stream.concat(Stream.of(lowBetaBandStream), channelF8LowBetaStream);
					channelF8ThetaStream = Stream.concat(Stream.of(thetaBandStream), channelF8ThetaStream);
					
				} else {
					channelF8AlphaStream = Stream.of(alphaBandStream);
					channelF8GammaStream = Stream.of(gammaBandStream);
					channelF8HighBetaStream = Stream.of(highBetaBandStream);
					channelF8LowBetaStream = Stream.of(lowBetaBandStream);
					channelF8ThetaStream = Stream.of(thetaBandStream);
				}
				
				//15) Channel AF4:
				thetaBand = obtainBandValue("AF4", 0);
				alphaBand = obtainBandValue("AF4", 1);
				lowBetaBand = obtainBandValue("AF4", 2);
				highBetaBand = obtainBandValue("AF4", 3);
				gammaBand = obtainBandValue("AF4", 4);
				
				ChannelAF4Changed(thetaBand, alphaBand, lowBetaBand,
						highBetaBand, gammaBand);
				
				thetaBandStream = Arrays.asList(thetaBand);
				alphaBandStream = Arrays.asList(alphaBand);
				lowBetaBandStream = Arrays.asList(lowBetaBand);
				highBetaBandStream = Arrays.asList(highBetaBand);
				gammaBandStream = Arrays.asList(gammaBand);
				
				if(channelAF4AlphaStream != null && channelAF4GammaStream != null &&
						channelAF4HighBetaStream != null && channelAF4LowBetaStream != null &&
						channelAF4ThetaStream != null) {					
					channelAF4AlphaStream = Stream.concat(Stream.of(alphaBandStream), channelAF4AlphaStream);
					channelAF4GammaStream = Stream.concat(Stream.of(gammaBandStream), channelAF4GammaStream);
					channelAF4HighBetaStream = Stream.concat(Stream.of(highBetaBandStream), channelAF4HighBetaStream);
					channelAF4LowBetaStream = Stream.concat(Stream.of(lowBetaBandStream), channelAF4LowBetaStream);
					channelAF4ThetaStream = Stream.concat(Stream.of(thetaBandStream), channelAF4ThetaStream);
					
				} else {
					channelAF4AlphaStream = Stream.of(alphaBandStream);
					channelAF4GammaStream = Stream.of(gammaBandStream);
					channelAF4HighBetaStream = Stream.of(highBetaBandStream);
					channelAF4LowBetaStream = Stream.of(lowBetaBandStream);
					channelAF4ThetaStream = Stream.of(thetaBandStream);
				}
				
				break;
			default:
				break;
			}
		}
	};

}
