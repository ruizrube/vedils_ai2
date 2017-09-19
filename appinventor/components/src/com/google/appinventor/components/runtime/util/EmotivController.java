/**
 * 
 */
package com.google.appinventor.components.runtime.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.emotiv.emotivcloud.EmotivCloudClient;
import com.emotiv.emotivcloud.EmotivCloudErrorCode;
import com.emotiv.insight.FacialExpressionDetection;
import com.emotiv.insight.FacialExpressionDetection.IEE_FacialExpressionEvent_t;
import com.emotiv.insight.FacialExpressionDetection.IEE_FacialExpressionThreshold_t;
import com.emotiv.insight.FacialExpressionDetection.IEE_FacialExpressionTrainingControl_t;
import com.emotiv.insight.IEdk;
import com.emotiv.insight.IEdk.IEE_DataChannel_t;
import com.emotiv.insight.IEdk.IEE_Event_t;
import com.emotiv.insight.IEdk.IEE_MotionDataChannel_t;
import com.emotiv.insight.IEdkErrorCode;
import com.emotiv.insight.IEmoStateDLL;
import com.emotiv.insight.IEmoStateDLL.IEE_FacialExpressionAlgo_t;
import com.emotiv.insight.IEmoStateDLL.IEE_MentalCommandAction_t;
import com.emotiv.insight.MentalCommandDetection;
import com.emotiv.insight.MentalCommandDetection.IEE_MentalCommandEvent_t;
import com.emotiv.insight.MentalCommandDetection.IEE_MentalCommandTrainingControl_t;
import com.google.appinventor.components.runtime.BrainwaveSensor;
import com.google.appinventor.components.runtime.Component;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * @author ruizrube
 *
 */
public class EmotivController implements Runnable {

	private static IEE_MotionDataChannel_t[] MOTION_CHANNEL_LIST = { IEdk.IEE_MotionDataChannel_t.IMD_COUNTER,
			IEdk.IEE_MotionDataChannel_t.IMD_GYROX, IEdk.IEE_MotionDataChannel_t.IMD_GYROY,
			IEdk.IEE_MotionDataChannel_t.IMD_GYROZ, IEdk.IEE_MotionDataChannel_t.IMD_ACCX,
			IEdk.IEE_MotionDataChannel_t.IMD_ACCY, IEdk.IEE_MotionDataChannel_t.IMD_ACCZ,
			IEdk.IEE_MotionDataChannel_t.IMD_MAGX, IEdk.IEE_MotionDataChannel_t.IMD_MAGY,
			IEdk.IEE_MotionDataChannel_t.IMD_MAGZ, IEdk.IEE_MotionDataChannel_t.IMD_TIMESTAMP };

	public static final Map<String, IEE_DataChannel_t> DATA_CHANNEL;
	static {
		DATA_CHANNEL = new LinkedHashMap<String, IEE_DataChannel_t>();
		DATA_CHANNEL.put("AF3", IEE_DataChannel_t.IED_AF3); // IEE
															// InputChannels:3
		DATA_CHANNEL.put("F7", IEE_DataChannel_t.IED_F7);
		DATA_CHANNEL.put("F3", IEE_DataChannel_t.IED_F3);
		DATA_CHANNEL.put("FC5", IEE_DataChannel_t.IED_FC5);
		DATA_CHANNEL.put("T7", IEE_DataChannel_t.IED_T7);
		DATA_CHANNEL.put("P7", IEE_DataChannel_t.IED_P7);
		DATA_CHANNEL.put("Pz", IEE_DataChannel_t.IED_Pz);//
		DATA_CHANNEL.put("O1", IEE_DataChannel_t.IED_O1);
		DATA_CHANNEL.put("O2", IEE_DataChannel_t.IED_O2);
		DATA_CHANNEL.put("P8", IEE_DataChannel_t.IED_P8);
		DATA_CHANNEL.put("T8", IEE_DataChannel_t.IED_T8);
		DATA_CHANNEL.put("FC6", IEE_DataChannel_t.IED_FC6);
		DATA_CHANNEL.put("F4", IEE_DataChannel_t.IED_F4);
		DATA_CHANNEL.put("F8", IEE_DataChannel_t.IED_F8);
		DATA_CHANNEL.put("AF4", IEE_DataChannel_t.IED_AF4);
		// IEE_InputChannels_enum 17. Hay otro mas
	}

	public static final Map<String, IEE_FacialExpressionAlgo_t> TRAINABLE_FACIAL_EXPRESSIONS;
	static {
		TRAINABLE_FACIAL_EXPRESSIONS = new LinkedHashMap<String, IEE_FacialExpressionAlgo_t>();
		TRAINABLE_FACIAL_EXPRESSIONS.put("Neutral", IEE_FacialExpressionAlgo_t.FE_NEUTRAL);
		TRAINABLE_FACIAL_EXPRESSIONS.put("Smile", IEE_FacialExpressionAlgo_t.FE_SMILE);
		TRAINABLE_FACIAL_EXPRESSIONS.put("Frown", IEE_FacialExpressionAlgo_t.FE_FROWN);
		TRAINABLE_FACIAL_EXPRESSIONS.put("Clench", IEE_FacialExpressionAlgo_t.FE_CLENCH);
	}

	public static final Map<String, IEE_MentalCommandAction_t> TRAINABLE_MENTAL_COMMANDS;
	static {
		TRAINABLE_MENTAL_COMMANDS = new LinkedHashMap<String, IEE_MentalCommandAction_t>();
		TRAINABLE_MENTAL_COMMANDS.put("Neutral", IEE_MentalCommandAction_t.MC_NEUTRAL);
		TRAINABLE_MENTAL_COMMANDS.put("Pull", IEE_MentalCommandAction_t.MC_PULL);
		TRAINABLE_MENTAL_COMMANDS.put("Push", IEE_MentalCommandAction_t.MC_PUSH);
		TRAINABLE_MENTAL_COMMANDS.put("Disappear", IEE_MentalCommandAction_t.MC_DISAPPEAR);
		TRAINABLE_MENTAL_COMMANDS.put("Lift", IEE_MentalCommandAction_t.MC_LIFT);
		TRAINABLE_MENTAL_COMMANDS.put("Drop", IEE_MentalCommandAction_t.MC_DROP);
		TRAINABLE_MENTAL_COMMANDS.put("Left", IEE_MentalCommandAction_t.MC_LEFT);
		TRAINABLE_MENTAL_COMMANDS.put("Right", IEE_MentalCommandAction_t.MC_RIGHT);
		TRAINABLE_MENTAL_COMMANDS.put("Rotate Clockwise", IEE_MentalCommandAction_t.MC_ROTATE_CLOCKWISE);
		TRAINABLE_MENTAL_COMMANDS.put("Rotate Counterclockwise", IEE_MentalCommandAction_t.MC_ROTATE_COUNTER_CLOCKWISE);
		TRAINABLE_MENTAL_COMMANDS.put("Rotate Fordwards", IEE_MentalCommandAction_t.MC_ROTATE_FORWARDS);
		TRAINABLE_MENTAL_COMMANDS.put("Rotate Reverse", IEE_MentalCommandAction_t.MC_ROTATE_REVERSE);
		TRAINABLE_MENTAL_COMMANDS.put("Rotate Left", IEE_MentalCommandAction_t.MC_ROTATE_LEFT);
		TRAINABLE_MENTAL_COMMANDS.put("Rotate Right", IEE_MentalCommandAction_t.MC_ROTATE_RIGHT);

	}

	private static EmotivController uniqInstance;

	private Context context;

	private Handler mHandler;

	private EmotivData data;

	private int deviceType;

	private int userId;

	private int userCloudID;

	private int engineUserID;

	private boolean isConnected;

	private boolean cloudConnected;

	private boolean has_successd;

	private boolean desconectar;

	private EmotivController() {
	}

	public static EmotivController getInstance() {
		if (uniqInstance == null) {
			uniqInstance = new EmotivController();
		}
		return uniqInstance;
	}

	public void connect(BrainwaveSensor brainwaveSensor) {

		this.context = brainwaveSensor.getContext();
		this.mHandler = brainwaveSensor.getMHandler();
		this.deviceType = brainwaveSensor.EEGDeviceType();
		this.data = new EmotivData();

		this.isConnected = false;
		this.desconectar = false;
		this.cloudConnected = false;
		this.has_successd = false;
		this.userCloudID = -1;
		this.engineUserID = 0;

		IEdk.IEE_EngineConnect(context, "");

		IEdk.IEE_MotionDataCreate();

	}

	public void disconnect() {
		Log.e("EmotivController", "Procediendo a desconectar emotiv");
		IEdk.IEE_EngineDisconnect();
		this.desconectar = true;
	}

	@Override
	public void run() {
		// Moves the current Thread into the background
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		try {

			while (true && !desconectar) {

				Thread.sleep(100);

				if (!has_successd) {
					int numberDevice = 0;

					if (deviceType == Component.EMOTIV_INSIGHT) {
						numberDevice = IEdk.IEE_GetInsightDeviceCount();

						Log.e("EmotivController", "device count " + numberDevice);

					} else {
						IEdk.IEE_EmoSettingMode(IEdk.IEE_Headset_mode.MODE_EPOCPLUS_MOTION_64Hz.toInt());
						IEdk.IEE_FFTSetWindowingType(userId, IEdk.IEE_WindowsType_t.IEE_BLACKMAN);

						numberDevice = IEdk.IEE_GetEpocPlusDeviceCount();

						Log.e("EmotivController!", "device count " + numberDevice);

					}

					if (numberDevice != 0) {

						if (!isConnected) {

							if (deviceType == Component.EMOTIV_INSIGHT) {
								has_successd = IEdk.IEE_ConnectInsightDevice(0);
								Log.e("EmotivController", "connected to insight? " + has_successd);

							} else {
								has_successd = IEdk.IEE_ConnectEpocPlusDevice(0, false);
								Log.e("EmotivController", "connected to epoc? " + has_successd);
							}
						}
					}
				}

				int state = IEdk.IEE_EngineGetNextEvent();

				/////
				if (state == IEdkErrorCode.EDK_OK.ToInt()) { // Dec:1536,Hex:600
					captureEEGData();
					Log.e("EmotivController", "Received event status: " + state);

					int eventType = IEdk.IEE_EmoEngineEventGetType();
					Log.e("EmotivController", "Dispatching event type " + eventType);

					if (eventType == IEE_Event_t.IEE_UserAdded.ToInt()) {
						dispatchUserAdded();

					} else if (eventType == IEE_Event_t.IEE_UserRemoved.ToInt()) {
						dispatchUserRemoved();

					} else if (eventType == IEE_Event_t.IEE_FacialExpressionEvent.ToInt()) {
						if (isConnected) {
							dispatchFacialExpressionTrainingEvent();
						}
					} else if (eventType == IEE_Event_t.IEE_MentalCommandEvent.ToInt()) {
						if (isConnected) {
							dispatchMentalCommandTrainingEvent();
						}
					} else if (eventType == IEE_Event_t.IEE_EmoStateUpdated.ToInt()) {
						if (isConnected) {
							dispatchStateUpdated();
						}
					} else if (eventType == IEE_Event_t.IEE_InternalStateChanged.ToInt()) {
						//TPM: Adding event for bands
						if(isConnected) {
							System.out.println("Ocurriendo internal event!");
						}
					}
				} else if (state == IEdkErrorCode.EDK_NO_EVENT.ToInt()) {
					if (isConnected) {
						captureEEGData();
						Log.e("EmotivController", "No event received"); // Dec:1536,Hex:// 600
					}
				} else if (state == IEdkErrorCode.EDK_EMOENGINE_UNINITIALIZED.ToInt()) {
					Log.e("EmotivController", "Engine unitialized event received"); // dec:
																					// 1280,
																					// hex:500
					dispatchUserRemoved();
				} else {
					Log.e("EmotivController", "Unknown event received: " + state); // Dec:1536,Hex:
																					// 600
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setFacialExpressionThreshold(IEE_FacialExpressionAlgo_t feAction, int sensitivity) {
		FacialExpressionDetection.IEE_FacialExpressionSetThreshold(userId, feAction.ToInt(),
				IEE_FacialExpressionThreshold_t.FE_SENSITIVITY.toInt(), sensitivity);

	}

	public void clearTrainingFacialExpression(IEE_FacialExpressionAlgo_t feAction) {

		FacialExpressionDetection.IEE_FacialExpressionSetTrainingAction(userId, feAction.ToInt());
		if (FacialExpressionDetection.IEE_FacialExpressionSetTrainingControl(userId,
				IEE_FacialExpressionTrainingControl_t.FE_ERASE.getType()) == IEdkErrorCode.EDK_OK.ToInt()) {
		}

	}

	public void clearTrainingMentalCommand(IEE_MentalCommandAction_t mcAction) {
		MentalCommandDetection.IEE_MentalCommandSetTrainingAction(userId, mcAction.ToInt());
		if (MentalCommandDetection.IEE_MentalCommandSetTrainingControl(userId,
				IEE_MentalCommandTrainingControl_t.MC_ERASE.getType()) == IEdkErrorCode.EDK_OK.ToInt()) {
		}
	}

	public void acceptTrainingFacialExpression() {
		if (FacialExpressionDetection.IEE_FacialExpressionSetTrainingControl(userId,
				IEE_FacialExpressionTrainingControl_t.FE_ACCEPT.getType()) == IEdkErrorCode.EDK_OK.ToInt()) {
		}
	}

	public void acceptTrainingMentalCommand() {
		if (MentalCommandDetection.IEE_MentalCommandSetTrainingControl(userId,
				IEE_MentalCommandTrainingControl_t.MC_ACCEPT.getType()) == IEdkErrorCode.EDK_OK.ToInt()) {
		}
	}

	public void rejectTrainingFacialExpression() {
		if (FacialExpressionDetection.IEE_FacialExpressionSetTrainingControl(userId,
				IEE_FacialExpressionTrainingControl_t.FE_REJECT.getType()) == IEdkErrorCode.EDK_OK.ToInt()) {
		}
	}

	public void rejectTrainingMentalCommand() {
		if (MentalCommandDetection.IEE_MentalCommandSetTrainingControl(userId,
				IEE_MentalCommandTrainingControl_t.MC_REJECT.getType()) == IEdkErrorCode.EDK_OK.ToInt()) {
		}
	}

	public boolean checkTrainedFacialExpression(IEE_FacialExpressionAlgo_t feAction) {
		long[] result = FacialExpressionDetection.IEE_FacialExpressionGetTrainedSignatureActions(userId);
		if (result[0] == 0) {
			long _currentTrainedActions = result[1];
			long y = _currentTrainedActions & feAction.ToInt();
			return (y == feAction.ToInt());
		}
		return false;
	}

	public float obtainTrainedMentalCommandRating(IEE_MentalCommandAction_t mcAction) {

		return MentalCommandDetection.IEE_MentalCommandGetActionSkillRating(userId, mcAction.ToInt())[1];

		// float[] aux =
		// MentalCommandDetection.IEE_MentalCommandGetActionSkillRating(userId,
		// mcAction.ToInt());
		// for (int i = 0; i < aux.length; i++) {
		// Log.e("EmotivController", "MentalCommandRating for action " +
		// mcAction.ToInt() + " [" + i + "] " + aux[i]);
		// }
		// return aux;

	}

	public void setActivationLevel(int level) {
		MentalCommandDetection.IEE_MentalCommandSetActivationLevel(userId, level);
	}

	public boolean checkTrainedMentalCommand(IEE_MentalCommandAction_t mcAction) {

		long[] result = MentalCommandDetection.IEE_MentalCommandGetTrainedSignatureActions(userId);
		if (result[0] == IEdkErrorCode.EDK_OK.ToInt()) {
			long y = result[1] & mcAction.ToInt();
			return (y == mcAction.ToInt());
		}
		return false;
	}

	public void startTrainingNeutral() {
		MentalCommandDetection.IEE_MentalCommandStartSamplingNeutral(userId);
	}

	public void stopTrainingNeutral() {
		MentalCommandDetection.IEE_MentalCommandStopSamplingNeutral(userId);
	}

	public boolean startTrainingFacialExpression(IEE_FacialExpressionAlgo_t feAction) {
		if (FacialExpressionDetection.IEE_FacialExpressionSetTrainingAction(userId,
				feAction.ToInt()) == IEdkErrorCode.EDK_OK.ToInt()) {
			if (FacialExpressionDetection.IEE_FacialExpressionSetTrainingControl(userId,
					IEE_FacialExpressionTrainingControl_t.FE_START.getType()) == IEdkErrorCode.EDK_OK.ToInt()) {
				return true;
			}
		}
		return false;

	}

	public boolean startTrainingMentalCommand(IEE_MentalCommandAction_t mc) {
		if (MentalCommandDetection.IEE_MentalCommandSetTrainingAction(userId, mc.ToInt()) == IEdkErrorCode.EDK_OK
				.ToInt()) {
			if (MentalCommandDetection.IEE_MentalCommandSetTrainingControl(userId,
					IEE_MentalCommandTrainingControl_t.MC_START.getType()) == IEdkErrorCode.EDK_OK.ToInt()) {
				return true;
			}
		}
		return false;
	}

	public boolean resetTrainingFacialExpression(IEE_FacialExpressionAlgo_t feAction) {

		if (FacialExpressionDetection.IEE_FacialExpressionSetTrainingControl(userId,
				IEE_FacialExpressionTrainingControl_t.FE_RESET.getType()) == IEdkErrorCode.EDK_OK.ToInt()) {
			return false;
		}

		return false;

	}

	public boolean resetTrainingMentalCommand(IEE_MentalCommandAction_t mc) {

		if (MentalCommandDetection.IEE_MentalCommandSetTrainingControl(userId,
				IEE_MentalCommandTrainingControl_t.MC_RESET.getType()) == IEdkErrorCode.EDK_OK.ToInt()) {
			return false;
		}

		return false;

	}

	public void enableMentalcommandActions(IEE_MentalCommandAction_t _MetalcommandAction) {
		long MetaCommandActions;
		long[] activeAction = MentalCommandDetection.IEE_MentalCommandGetActiveActions(userId);
		if (activeAction[0] == IEdkErrorCode.EDK_OK.ToInt()) {
			long y = activeAction[1] & (long) _MetalcommandAction.ToInt(); // and
			if (y == 0) {
				MetaCommandActions = activeAction[1] | ((long) _MetalcommandAction.ToInt()); // or
				MentalCommandDetection.IEE_MentalCommandSetActiveActions(userId, MetaCommandActions);

			}
		}

	}

	private void dispatchUserAdded() {
		isConnected = true;
		userId = IEdk.IEE_EmoEngineEventGetUserId();
		Log.e("EmotivController", "User Added " + userId);
		mHandler.obtainMessage(BrainwaveSensor.HANDLER_USER_ADDED, data).sendToTarget();

	}

	private void dispatchUserRemoved() {
		isConnected = false;
		userId = -1;
		Log.e("EmotivController", "User Removed");
		mHandler.obtainMessage(BrainwaveSensor.HANDLER_USER_REMOVED, data).sendToTarget();

	}

	private void captureEEGData() {
		dispatchMotionData();
		dispatchFFTData();
		dispatchHeadsetData();

	}

	private void dispatchStateUpdated() {

		Log.e("EmotivController", "Stated updated");

		IEdk.IEE_EmoEngineEventGetEmoState();

		dispatchFacialExpressionDiscreteEvent();
		dispatchFacialExpressionContinuousEvent();
		dispatchMentalCommandEvent();

	}

	private void dispatchMentalCommandTrainingEvent() {
		int type = MentalCommandDetection.IEE_MentalCommandEventGetType();

		if (type == IEE_MentalCommandEvent_t.IEE_MentalCommandTrainingCompleted.getType()) {
			Log.e("EmotivController", "Mental command training completed");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_TRAINED_COMPLETE, data).sendToTarget();
		} else if (type == IEE_MentalCommandEvent_t.IEE_MentalCommandTrainingDataErased.getType()) {
			Log.e("EmotivController", "Mental command training erased");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_TRAINED_ERASED, data).sendToTarget();
		} else if (type == IEE_MentalCommandEvent_t.IEE_MentalCommandTrainingFailed.getType()) {
			Log.e("EmotivController", "Mental command training failed");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_TRAINED_FAILED, data).sendToTarget();
		} else if (type == IEE_MentalCommandEvent_t.IEE_MentalCommandTrainingRejected.getType()) {
			Log.e("EmotivController", "Mental command training rejected");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_TRAINED_REJECT, data).sendToTarget();
		} else if (type == IEE_MentalCommandEvent_t.IEE_MentalCommandTrainingReset.getType()) {
			Log.e("EmotivController", "Mental command training reseted");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_TRAINED_RESET, data).sendToTarget();
		} else if (type == IEE_MentalCommandEvent_t.IEE_MentalCommandTrainingStarted.getType()) {
			Log.e("EmotivController", "Mental command training started");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_TRAINED_START, data).sendToTarget();
		} else if (type == IEE_MentalCommandEvent_t.IEE_MentalCommandTrainingSucceeded.getType()) {
			Log.e("EmotivController", "Mental command training succeeded");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_TRAINED_SUCCEED, data).sendToTarget();
		}

	}

	private void dispatchFacialExpressionTrainingEvent() {

		int type = FacialExpressionDetection.IEE_FacialExpressionEventGetType();

		// EVENT ORDER
		// 1. Started / reset
		// 2. Failed / Succeded
		// 3. Reject / Accept
		// 4. Completed

		if (type == IEE_FacialExpressionEvent_t.IEE_FacialExpressionTrainingCompleted.getType()) {
			Log.e("EmotivController", "Facial expression training completed");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_TRAINED_COMPLETE, data).sendToTarget();
		} else if (type == IEE_FacialExpressionEvent_t.IEE_FacialExpressionTrainingDataErased.getType()) {
			Log.e("EmotivController", "Facial expression training erased");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_TRAINED_ERASED, data).sendToTarget();
		} else if (type == IEE_FacialExpressionEvent_t.IEE_FacialExpressionTrainingFailed.getType()) {
			Log.e("EmotivController", "Facial expression training failed");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_TRAINED_FAILED, data).sendToTarget();
		} else if (type == IEE_FacialExpressionEvent_t.IEE_FacialExpressionTrainingRejected.getType()) {
			Log.e("EmotivController", "Facial expression training rejected");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_TRAINED_REJECT, data).sendToTarget();
		} else if (type == IEE_FacialExpressionEvent_t.IEE_FacialExpressionTrainingReset.getType()) {
			Log.e("EmotivController", "Facial expression training reseted");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_TRAINED_RESET, data).sendToTarget();
		} else if (type == IEE_FacialExpressionEvent_t.IEE_FacialExpressionTrainingStarted.getType()) {
			Log.e("EmotivController", "Facial expression training started");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_TRAINED_START, data).sendToTarget();
		} else if (type == IEE_FacialExpressionEvent_t.IEE_FacialExpressionTrainingSucceeded.getType()) {
			Log.e("EmotivController", "Facial expression training succeeded");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_TRAINED_SUCCEED, data).sendToTarget();
		}

	}

	private void dispatchFacialExpressionDiscreteEvent() {

		// parpadear
		if (IEmoStateDLL.IS_FacialExpressionIsBlink() == 1) {
			Log.e("EmotivController", "Blink");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_EXPRESSION_BLINK, data).sendToTarget();
		}

		// guiñar
		if (IEmoStateDLL.IS_FacialExpressionIsLeftWink() == 1) {
			Log.e("EmotivController", "Left Wink");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_EXPRESSION_LEFTWINK, data).sendToTarget();
			;
		}
		if (IEmoStateDLL.IS_FacialExpressionIsRightWink() == 1) {
			Log.e("EmotivController", "Right Wink");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_EXPRESSION_RIGHTWINK, data).sendToTarget();
		}

		// mirar arriba
		if (IEmoStateDLL.IS_FacialExpressionIsLookingUp() == 1) {
			Log.e("EmotivController", "Looking Up");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_EXPRESSION_LOOKINGUP, data).sendToTarget();
		}

		// mirar abajo
		if (IEmoStateDLL.IS_FacialExpressionIsLookingDown() == 1) {
			Log.e("EmotivController", "Looking Down");
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_EXPRESSION_LOOKINGDOWN, data).sendToTarget();
		}

	}

	private void dispatchHeadsetData() {

		int index = 3; // IEE_InputChannels_enum 3 -> IEE_CHAN_AF3

		Map<IEE_DataChannel_t, Integer> contactQuality = new LinkedHashMap<IEE_DataChannel_t, Integer>();

		for (String channel : DATA_CHANNEL.keySet()) {
			contactQuality.put(DATA_CHANNEL.get(channel), new Integer(IEmoStateDLL.IS_GetContactQuality(index)));
			index++;
		}

		data.setContactQuality(contactQuality);

		if (IEmoStateDLL.IS_GetBatteryChargeLevel()[1] != 0) {
			data.setBatteryLevel(Math.round(
					100 * IEmoStateDLL.IS_GetBatteryChargeLevel()[0] / IEmoStateDLL.IS_GetBatteryChargeLevel()[1]));

		}

		data.setRunningTime(IEmoStateDLL.IS_GetTimeFromStart());

	}

	private void dispatchFFTData() {

		// theta - theta band value (4-8 Hz)
		// alpha - alpha band value (8-12 Hz)
		// low_beta - low-beta value (12-16 Hz)
		// high_beta - high-beta value (16-25 Hz)
		// gamma - gamma value (25-45 Hz)

		Map<IEE_DataChannel_t, double[]> aux = new LinkedHashMap<IEE_DataChannel_t, double[]>();

		for (IEE_DataChannel_t channel : DATA_CHANNEL.values()) {
			aux.put(channel, IEdk.IEE_GetAverageBandPowers(channel));
		}
		data.setFFTData(aux);
		mHandler.obtainMessage(BrainwaveSensor.HANDLER_CHANNELS_DATA, data).sendToTarget();
	}

	private void dispatchMotionData() {
		IEdk.IEE_MotionDataUpdateHandle(userId);
		int sample = IEdk.IEE_MotionDataGetNumberOfSample(userId);
		if (sample > 0) {
			for (int sampleIdx = 0; sampleIdx < sample; sampleIdx++) {
				Log.e("EmotivController",
						"setXAngularVelocity " + IEdk.IEE_MotionDataGet(MOTION_CHANNEL_LIST[1])[sampleIdx]);
				Log.e("EmotivController",
						"setYAngularVelocity=" + IEdk.IEE_MotionDataGet(MOTION_CHANNEL_LIST[2])[sampleIdx]);
				Log.e("EmotivController",
						"setZAngularVelocity=" + IEdk.IEE_MotionDataGet(MOTION_CHANNEL_LIST[3])[sampleIdx]);

				data.setXAngularVelocity(IEdk.IEE_MotionDataGet(MOTION_CHANNEL_LIST[1])[sampleIdx]);
				data.setYAngularVelocity(IEdk.IEE_MotionDataGet(MOTION_CHANNEL_LIST[2])[sampleIdx]);
				data.setZAngularVelocity(IEdk.IEE_MotionDataGet(MOTION_CHANNEL_LIST[3])[sampleIdx]);

				data.setXAcceleration(IEdk.IEE_MotionDataGet(MOTION_CHANNEL_LIST[4])[sampleIdx]);
				data.setYAcceleration(IEdk.IEE_MotionDataGet(MOTION_CHANNEL_LIST[5])[sampleIdx]);
				data.setZAcceleration(IEdk.IEE_MotionDataGet(MOTION_CHANNEL_LIST[6])[sampleIdx]);

				data.setXMagnetization(IEdk.IEE_MotionDataGet(MOTION_CHANNEL_LIST[7])[sampleIdx]);
				data.setYMagnetization(IEdk.IEE_MotionDataGet(MOTION_CHANNEL_LIST[8])[sampleIdx]);
				data.setZMagnetization(IEdk.IEE_MotionDataGet(MOTION_CHANNEL_LIST[9])[sampleIdx]);

			}
		}

	}

	private void dispatchFacialExpressionContinuousEvent() {

		// sonreir
		if (IEmoStateDLL.IS_FacialExpressionGetLowerFaceAction() == IEE_FacialExpressionAlgo_t.FE_SMILE.ToInt()) {
			Log.e("EmotivController", "Lower Smile");
			data.setPowerForSimileExpression(IEmoStateDLL.IS_FacialExpressionGetLowerFaceActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_EXPRESSION_SMILING, data).sendToTarget();
		}
		if (IEmoStateDLL.IS_FacialExpressionGetUpperFaceAction() == IEE_FacialExpressionAlgo_t.FE_SMILE.ToInt()) {
			Log.e("EmotivController", "Upper Smile");
			data.setPowerForSimileExpression(IEmoStateDLL.IS_FacialExpressionGetUpperFaceActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_EXPRESSION_SMILING, data).sendToTarget();
		}

		// fruncir el ceño
		if (IEmoStateDLL.IS_FacialExpressionGetLowerFaceAction() == IEE_FacialExpressionAlgo_t.FE_FROWN.ToInt()) {
			Log.e("EmotivController", "Lower Frown");
			data.setPowerForFrownExpression(IEmoStateDLL.IS_FacialExpressionGetLowerFaceActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_EXPRESSION_FROWNING, data).sendToTarget();
		}

		if (IEmoStateDLL.IS_FacialExpressionGetUpperFaceAction() == IEE_FacialExpressionAlgo_t.FE_FROWN.ToInt()) {
			Log.e("EmotivController", "Upper Frown");
			data.setPowerForFrownExpression(IEmoStateDLL.IS_FacialExpressionGetUpperFaceActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_EXPRESSION_FROWNING, data).sendToTarget();
		}

		// apretar
		if (IEmoStateDLL.IS_FacialExpressionGetLowerFaceAction() == IEE_FacialExpressionAlgo_t.FE_CLENCH.ToInt()) {
			Log.e("EmotivController", "Lower Clench");
			data.setPowerForClenchExpression(IEmoStateDLL.IS_FacialExpressionGetLowerFaceActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_EXPRESSION_CLENCHING, data).sendToTarget();
		}
		if (IEmoStateDLL.IS_FacialExpressionGetUpperFaceAction() == IEE_FacialExpressionAlgo_t.FE_CLENCH.ToInt()) {
			Log.e("EmotivController", "Upper Clench");
			data.setPowerForClenchExpression(IEmoStateDLL.IS_FacialExpressionGetUpperFaceActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_EXPRESSION_CLENCHING, data).sendToTarget();
		}

		// neutral
		if (IEmoStateDLL.IS_FacialExpressionGetLowerFaceAction() == IEE_FacialExpressionAlgo_t.FE_NEUTRAL.ToInt()
				&& IEmoStateDLL.IS_FacialExpressionGetUpperFaceAction() == IEE_FacialExpressionAlgo_t.FE_NEUTRAL
						.ToInt()) {
			Log.e("EmotivController", "Neutral Facial Expression");
			data.setPowerForNeutralExpression(Math.max(IEmoStateDLL.IS_FacialExpressionGetUpperFaceActionPower(),
					IEmoStateDLL.IS_FacialExpressionGetLowerFaceActionPower()));
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_EXPRESSION_NEUTRAL, data).sendToTarget();

		}

	}

	private void dispatchMentalCommandEvent() {

		if (IEmoStateDLL.IS_MentalCommandGetCurrentAction() == IEE_MentalCommandAction_t.MC_DISAPPEAR.ToInt()) {
			Log.e("EmotivController", "Disappear command");
			data.setPowerForDisappearCommand(IEmoStateDLL.IS_MentalCommandGetCurrentActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_COMMAND_DISAPPEAR, data).sendToTarget();
		} else if (IEmoStateDLL.IS_MentalCommandGetCurrentAction() == IEE_MentalCommandAction_t.MC_DROP.ToInt()) {
			Log.e("EmotivController", "Drop command");
			data.setPowerForDropCommand(IEmoStateDLL.IS_MentalCommandGetCurrentActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_COMMAND_DROP, data).sendToTarget();
		} else if (IEmoStateDLL.IS_MentalCommandGetCurrentAction() == IEE_MentalCommandAction_t.MC_LEFT.ToInt()) {
			Log.e("EmotivController", "Left command");
			data.setPowerForLeftCommand(IEmoStateDLL.IS_MentalCommandGetCurrentActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_COMMAND_LEFT, data).sendToTarget();
		} else if (IEmoStateDLL.IS_MentalCommandGetCurrentAction() == IEE_MentalCommandAction_t.MC_LIFT.ToInt()) {
			Log.e("EmotivController", "Lift command");
			data.setPowerForLiftCommand(IEmoStateDLL.IS_MentalCommandGetCurrentActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_COMMAND_LIFT, data).sendToTarget();
		} else if (IEmoStateDLL.IS_MentalCommandGetCurrentAction() == IEE_MentalCommandAction_t.MC_NEUTRAL.ToInt()) {
			Log.e("EmotivController", "Neutral command");
			data.setPowerForNeutralCommand(IEmoStateDLL.IS_MentalCommandGetCurrentActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_COMMAND_NEUTRAL, data).sendToTarget();
		} else if (IEmoStateDLL.IS_MentalCommandGetCurrentAction() == IEE_MentalCommandAction_t.MC_PULL.ToInt()) {
			Log.e("EmotivController", "Pull command");
			data.setPowerForPullCommand(IEmoStateDLL.IS_MentalCommandGetCurrentActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_COMMAND_PULL, data).sendToTarget();
		} else if (IEmoStateDLL.IS_MentalCommandGetCurrentAction() == IEE_MentalCommandAction_t.MC_PUSH.ToInt()) {
			Log.e("EmotivController", "Push command");
			data.setPowerForPushCommand(IEmoStateDLL.IS_MentalCommandGetCurrentActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_COMMAND_PUSH, data).sendToTarget();
		} else if (IEmoStateDLL.IS_MentalCommandGetCurrentAction() == IEE_MentalCommandAction_t.MC_RIGHT.ToInt()) {
			Log.e("EmotivController", "Right command");
			data.setPowerForRightCommand(IEmoStateDLL.IS_MentalCommandGetCurrentActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_COMMAND_RIGHT, data).sendToTarget();
		} else if (IEmoStateDLL.IS_MentalCommandGetCurrentAction() == IEE_MentalCommandAction_t.MC_ROTATE_CLOCKWISE
				.ToInt()) {
			Log.e("EmotivController", "Rotate Clockwise command");
			data.setPowerForRotateClockwiseCommand(IEmoStateDLL.IS_MentalCommandGetCurrentActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_COMMAND_ROTATE_CLOCKWISE, data).sendToTarget();
		} else if (IEmoStateDLL
				.IS_MentalCommandGetCurrentAction() == IEE_MentalCommandAction_t.MC_ROTATE_COUNTER_CLOCKWISE.ToInt()) {
			Log.e("EmotivController", "Rotate counter clockwise command");
			data.setPowerForRotateCounterClockwiseCommand(IEmoStateDLL.IS_MentalCommandGetCurrentActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_COMMAND_ROTATE_COUNTER_CLOCKWISE, data).sendToTarget();
		} else if (IEmoStateDLL.IS_MentalCommandGetCurrentAction() == IEE_MentalCommandAction_t.MC_ROTATE_FORWARDS
				.ToInt()) {
			Log.e("EmotivController", "Rotate fordwards command");
			data.setPowerForRotateForwardsCommand(IEmoStateDLL.IS_MentalCommandGetCurrentActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_COMMAND_ROTATE_FORWARDS, data).sendToTarget();
		} else if (IEmoStateDLL.IS_MentalCommandGetCurrentAction() == IEE_MentalCommandAction_t.MC_ROTATE_LEFT
				.ToInt()) {
			Log.e("EmotivController", "Rotate left command");
			data.setPowerForRotateLeftCommand(IEmoStateDLL.IS_MentalCommandGetCurrentActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_COMMAND_ROTATE_LEFT, data).sendToTarget();
		} else if (IEmoStateDLL.IS_MentalCommandGetCurrentAction() == IEE_MentalCommandAction_t.MC_ROTATE_REVERSE
				.ToInt()) {
			Log.e("EmotivController", "Rotate reverse command");
			data.setPowerForRotateReverseCommand(IEmoStateDLL.IS_MentalCommandGetCurrentActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_COMMAND_ROTATE_REVERSE, data).sendToTarget();
		} else if (IEmoStateDLL.IS_MentalCommandGetCurrentAction() == IEE_MentalCommandAction_t.MC_ROTATE_RIGHT
				.ToInt()) {
			Log.e("EmotivController", "Rotate right command");
			data.setPowerForRotateRightCommand(IEmoStateDLL.IS_MentalCommandGetCurrentActionPower());
			mHandler.obtainMessage(BrainwaveSensor.HANDLER_COMMAND_ROTATE_RIGHT, data).sendToTarget();
		}
	}

	public void login(String username, String password) {
		if (!cloudConnected) {
			Log.d("EmotivController", "LOGIN [1] with " + username + " and " + password + " -->" + cloudConnected);
			if (EmotivCloudClient.EC_Connect((Activity) this.context) == EmotivCloudErrorCode.EC_OK.ToInt()) {
				Log.d("EmotivController", "LOGIN [2] with " + username + " and " + password + " -->" + cloudConnected);

				if (EmotivCloudClient.EC_Login(username, password) == EmotivCloudErrorCode.EC_OK.ToInt()) {
					Log.d("EmotivController",
							"LOGIN [3] with " + username + " and " + password + " -->" + cloudConnected);

					userCloudID = EmotivCloudClient.EC_GetUserDetail();
					if (EmotivCloudClient.EC_GetUserDetail() != -1) {
						Log.d("EmotivController", "LOGIN [4] with " + username + " and " + password + " -->"
								+ cloudConnected + "userCLOUDID es:" + userCloudID);
						cloudConnected = true;
					}

				}

			}
		}

		Log.d("EmotivController", "LOGIN [END] with " + username + " and " + password + " -->" + cloudConnected);
	}

	public boolean loadProfile(String profile) {

		Log.d("EmotivController", "LOAD PROFILE [1] " + profile);

		// emotiv connected?
		if (!isConnected) {
			return false;
		}

		Log.d("EmotivController", "LOAD PROFILE [2] " + profile);

		// login?
		if (!cloudConnected) {
			return false;
		}

		Log.d("EmotivController", "LOAD PROFILE [3] " + profile);

		// profile exists?
		int profileID = EmotivCloudClient.EC_GetProfileId(userCloudID, profile);
		if (profileID < 0) {
			return false;
		}
		Log.d("EmotivController", "LOAD PROFILE [4] " + profile + "  profileID=" + profileID);

		// profile loads?
		if (EmotivCloudClient.EC_LoadUserProfile(userCloudID, engineUserID, profileID, -1) != EmotivCloudErrorCode.EC_OK
				.ToInt()) {
			return false;
		}

		Log.d("EmotivController", "LOAD PROFILE OK " + profile);

		return true;

	}

	public List<String> selectProfiles() {
		List<String> result = new ArrayList<String>();

		// emotiv connected?
		if (isConnected && cloudConnected) {
			int total = EmotivCloudClient.EC_GetAllProfileName(userCloudID);
			Log.d("EmotivController", "TOTAL PROFILES " + total);

			for (int i = 0; i < total; i++) {
				result.add(EmotivCloudClient.EC_ProfileNameAtIndex(userCloudID, i));
				Log.d("EmotivController", "SELECT PROFILES " + result.get(i));
			}

			// String aux="-1";
			// int i=0;
			// while(!aux.equals("")){
			// aux=EmotivCloudClient.EC_ProfileNameAtIndex(userCloudID, i);
			// result.add(EmotivCloudClient.EC_ProfileNameAtIndex(userCloudID,
			// i));
			// Log.d("EmotivController", "SELECT PROFILE: " + result.get(i));
			// i++;
			// }
		}

		return result;

	}

	public boolean saveOrUpdateProfile(String profile) {
		// emotiv connected?
		if (!isConnected) {
			return false;
		}

		// login?
		if (!cloudConnected) {
			return false;
		}

		int profileID = EmotivCloudClient.EC_GetProfileId(userCloudID, profile);
		int resultState;

		if (profileID == -1) { // si no existe el perfil lo creamos
			resultState = EmotivCloudClient.EC_SaveUserProfile(userCloudID, engineUserID, profile,
					EmotivCloudClient.profileFileType.TRAINING.ToInt());
			Log.d("EmotivController", "CREATE PROFILE " + resultState);

		} else {
			resultState = EmotivCloudClient.EC_UpdateUserProfile(userCloudID, engineUserID, profileID, profile);
			Log.d("EmotivController", "UPDATE PROFILE " + resultState);
		}

		if (resultState == EmotivCloudErrorCode.EC_OK.ToInt()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteProfile(String profile) {
		// emotiv connected?
		if (!isConnected) {
			return false;
		}

		// login?
		if (!cloudConnected) {
			return false;
		}

		// profile exists?
		int profileID = EmotivCloudClient.EC_GetProfileId(userCloudID, profile);
		if (profileID < 0) {
			return false;
		}

		if (EmotivCloudClient.EC_DeleteUserProfile(userCloudID, profileID) != EmotivCloudErrorCode.EC_OK.ToInt()) {
			Log.d("EmotivController", "DELETE PROFILE " + profile + " fallo");

			return false;
		} else {
			Log.d("EmotivController", "DELETE PROFILE " + profile + " ok");
			return true;
		}
	}

	public boolean isConnected() {
		return isConnected;
	}

	public boolean isCloudConnected() {
		return cloudConnected;
	}

}
