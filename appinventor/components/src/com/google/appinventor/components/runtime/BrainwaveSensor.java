/**
 * 
 */
package com.google.appinventor.components.runtime;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.google.appinventor.components.runtime.util.EmotivController;
import com.google.appinventor.components.runtime.util.EmotivData;
import com.google.appinventor.components.runtime.util.JellybeanMR2Util;
import com.google.appinventor.components.runtime.util.OnInitializeListener;
import com.google.appinventor.components.runtime.util.SdkLevel;

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
@UsesLibraries(libraries = "bedk.jar")
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

	/////////////////////
	// CLASS ATTRIBUTES //
	/////////////////////

	private static final long serialVersionUID = 1L;

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
		// TODO Auto-generated method stub
		Log.e("EmotivController", "EVENTO EN VENTANA: onInitialize");

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.e("EmotivController", "EVENTO EN VENTANA: onResume");

	}

	@Override
	public void onPause() {
		Log.e("EmotivController", "EVENTO EN VENTANA: onPause");

	}

	@Override
	public void onStop() {
		Log.e("EmotivController", "EVENTO EN VENTANA: onStop");

		controller.disconnect();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
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

			// return
			// Arrays.toString(controller.obtainTrainedMentalCommandRating(mc)).replace("[",
			// "").replace("]", "");

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

	///////

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

	private double obtainBandValue(String channelName, int band) {

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
			default:
				break;
			}
		}
	};

}
