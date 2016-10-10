/**
 * 
 */
package com.google.appinventor.components.runtime;

import java.util.ArrayList;

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
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.SpheroMacros;
import com.orbotix.ConvenienceRobot;
import com.orbotix.DualStackDiscoveryAgent;
import com.orbotix.async.CollisionDetectedAsyncData;
import com.orbotix.async.DeviceSensorAsyncMessage;
import com.orbotix.common.DiscoveryException;
import com.orbotix.common.ResponseListener;
import com.orbotix.common.Robot;
import com.orbotix.common.RobotChangedStateListener;
import com.orbotix.common.internal.AsyncMessage;
import com.orbotix.common.internal.DeviceResponse;
import com.orbotix.common.sensor.AccelerometerData;
import com.orbotix.common.sensor.DeviceSensorsData;
import com.orbotix.common.sensor.GyroData;
import com.orbotix.common.sensor.LocatorData;
import com.orbotix.common.sensor.SensorFlag;
import com.orbotix.le.RobotLE;
import com.orbotix.subsystem.SensorControl;

import android.graphics.Color;

/**
 * A component for managing the Sphero robot
 *
 * @author iruizrube
 */

// Manifest.permission.ACCESS_COARSE_LOCATION) ==
// PackageManager.PERMISSION_GRANTED) {

@DesignerComponent(version = YaVersion.SPHEROCONTROLLER_COMPONENT_VERSION, description = "A component for managing the Sphero robot. ", category = ComponentCategory.VEDILSROBOTICS, iconName = "images/sphero_icon.png", nonVisible = true)

@SimpleObject
@UsesPermissions(permissionNames = "android.permission.BLUETOOTH_ADMIN, android.permission.BLUETOOTH, android.permission.ACCESS_COARSE_LOCATION")
@UsesLibraries(libraries = "RobotLibrary.jar")
public class SpheroController extends AndroidNonvisibleComponent implements Component, RobotChangedStateListener,
		OnResumeListener, OnStopListener, OnPauseListener, OnDestroyListener {

	/////////////////////////
	// PRIVATE ATTRIBUTES //
	////////////////////////

	private ConvenienceRobot mRobot;

	private SpheroMacros macros;
	private SpheroResponseListener listener;

	private int color;
	private int defaultColor;

	private Form _form;

	private int baseAngle = 0;

	double accelerometerX = 0.0;
	double accelerometerY = 0.0;
	double accelerometerZ = 0.0;

	double gyroX = 0.0;
	double gyroY = 0.0;
	double gyroZ = 0.0;

	float positionX = 0.0f;
	float positionY = 0.0f;
	float velocityX = 0.0f;
	float velocityY = 0.0f;

	////////////////
	// INTERNAL METHODS //
	////////////////

	public SpheroController(ComponentContainer container) {
		super(container.$form());

		this._form = container.$form();

		DualStackDiscoveryAgent.getInstance().addRobotStateListener(this);

	}

	@Override
	public void onResume() {
		System.out.println("IRR>>>>>>Resumiendo");

		// startDiscovery();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		disconnectRobot();

	}

	@Override
	public void onStop() {
		//disconnectRobot();

	}

	@Override
	public void onDestroy() {
		DualStackDiscoveryAgent.getInstance().addRobotStateListener(null);
		// If a robot is connected to the device, disconnect it
//		if (mRobot != null) {
//			mRobot = null;
//		}
	}

	private void connectRobot() {
		System.out.println("IRR>>>>>>Iniciando DISCOVERING");

		macros = new SpheroMacros();

		// If the DiscoveryAgent is not already looking for robots, start
		// discovery.
		if (!DualStackDiscoveryAgent.getInstance().isDiscovering()) {
			System.out.println("IRR>>>>>>Iniciando DISCOVERING (segunda fase)");

			try {
				DualStackDiscoveryAgent.getInstance().startDiscovery(_form);
			} catch (DiscoveryException e) {
				System.out.println("DiscoveryException" + e.getMessage());
			}
		}
	}

	
	private void disconnectRobot() {
		// If the DiscoveryAgent is in discovery mode, stop it.
		if (DualStackDiscoveryAgent.getInstance().isDiscovering()) {
			DualStackDiscoveryAgent.getInstance().stopDiscovery();
		}

		// If a robot is connected to the device, disconnect it
		if (mRobot != null) {
			mRobot.disconnect();
			mRobot=null;
		}
	}

	@Override
	public void handleRobotChangedState(Robot robot, RobotChangedStateNotificationType type) {
		System.out.println("IRR>>>>>>Recibimos evento");

		switch (type) {
		case Online: {

			System.out.println("IRR>>>>>>El robot esta online");

			// If robot uses Bluetooth LE, Developer Mode can be turned on.
			// This turns off DOS protection. This generally isn't required.
			if (robot instanceof RobotLE) {
				((RobotLE) robot).setDeveloperMode(true);
			}

			// Save the robot as a ConvenienceRobot for additional utility
			// methods
			mRobot = new ConvenienceRobot(robot);
			// mRobot.enableStabilization(true);
			mRobot.setBackLedBrightness(0.5f);

			// Enables collisions
			mRobot.enableCollisions(true);
			long sensorFlag = SensorFlag.VELOCITY.longValue() | SensorFlag.LOCATOR.longValue()
					| SensorFlag.ACCELEROMETER_NORMALIZED.longValue() | SensorFlag.GYRO_NORMALIZED.longValue();
			mRobot.enableSensors(sensorFlag, SensorControl.StreamingRate.STREAMING_RATE10);

			if (listener == null) {
				listener = new SpheroResponseListener(this);
			}
			mRobot.addResponseListener(listener);

			// Setup
			changeBaseAngle(baseAngle);
			changeColor(color);
			changeDefaultColor(defaultColor);

			this.DeviceConnected();

			break;

		}
		case Disconnected:
			mRobot = null;
			this.DeviceDisconnected();

			break;
		}

	}

	private void changeBaseAngle(int basePosition) {
		// Forward
		System.out.println("IRR>>>>>>Ajustamos posicion base a " + basePosition);

		mRobot.setBackLedBrightness(0.5f);

		mRobot.calibrating(true);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}

		mRobot.rotate(basePosition);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}

		mRobot.stop();
		mRobot.calibrating(false);

		// mRobot.setZeroHeading();

	}

	private void changeColor(int argb) {
		System.out.println("IRR>>>>>>Poniendo color a  " + Color.red(argb) / 255 + "--" + Color.green(argb) / 255 + "--"
				+ Color.blue(argb) / 255);

		mRobot.setLed(Color.red(argb) / 255.0f, Color.green(argb) / 255.0f, Color.blue(argb) / 255.0f);
	}

	private void changeDefaultColor(int argb) {
		System.out.println("IRR>>>>>>Poniendo defaultcolor a  " + Color.red(argb) / 255 + "--" + Color.green(argb) / 255
				+ "--" + Color.blue(argb) / 255);
		mRobot.setLedDefault(Color.red(argb) / 255.0f, Color.green(argb) / 255.0f, Color.blue(argb) / 255.0f);
	}


	// }

	////////////////
	// PROPERTIES //
	////////////////

	/**
	 * Color property getter method.
	 *
	 * @return RGB color with alpha
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE)
	public int DefaultColor() {
		return defaultColor;
	}

	/**
	 * Color property setter method.
	 *
	 * @param argb
	 *            RGB color with alpha
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = Component.DEFAULT_VALUE_COLOR_BLUE)
	@SimpleProperty
	public void DefaultColor(int argb) {
		color = argb;

		if (mRobot == null) {
			System.out.println("IRR>>>>>>No hay robot todavia para DEFAULT COLOR");
			return;
		} else {
			changeDefaultColor(argb);
		}

	}

	/**
	 * Color property getter method.
	 *
	 * @return RGB color with alpha
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE)
	public int Color() {
		return color;
	}

	/**
	 * Color property setter method.
	 *
	 * @param argb
	 *            RGB color with alpha
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = Component.DEFAULT_VALUE_COLOR_BLACK)
	@SimpleProperty
	public void Color(int argb) {
		color = argb;

		if (mRobot == null) {
			System.out.println("IRR>>>>>>No hay robot todavia para BASE COLOR");
			return;
		} else {
			changeColor(argb);
		}

	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR)
	public double AccelerometerX() {
		return accelerometerX;
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR)
	public double AccelerometerY() {
		return accelerometerY;
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR)
	public double AccelerometerZ() {
		return accelerometerZ;
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR)
	public double GyroX() {
		return gyroX;
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR)
	public double GyroY() {
		return gyroY;
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR)
	public double GyroZ() {
		return gyroZ;
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR)
	public float PositionX() {
		return positionX;
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR)
	public float PositionY() {
		return positionY;
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR)
	public float VelocityX() {
		return velocityX;
	}

	@SimpleProperty(category = PropertyCategory.BEHAVIOR)
	public float VelocityY() {
		return velocityY;
	}

	public void setGyroX(double gyroX) {
		this.gyroX = gyroX;
	}

	public void setAccelerometerX(double accelerometerX) {
		this.accelerometerX = accelerometerX;
	}

	public void setAccelerometerY(double accelerometerY) {
		this.accelerometerY = accelerometerY;
	}

	public void setAccelerometerZ(double accelerometerZ) {
		this.accelerometerZ = accelerometerZ;
	}

	public void setGyroY(double gyroY) {
		this.gyroY = gyroY;
	}

	public void setGyroZ(double gyroZ) {
		this.gyroZ = gyroZ;
	}

	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}

	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}

	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}

	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}

	//////////////
	// FUNCTIONS //
	//////////////

	/**
	 * Function to connect with the Sphero device.
	 */
	@SimpleFunction(description = "Function to connect with the Sphero device.")
	public void Connect() {

		/*
		 * Associate a listener for robot state changes with the
		 * DualStackDiscoveryAgent. DualStackDiscoveryAgent checks for both
		 * Bluetooth Classic and Bluetooth LE. DiscoveryAgentClassic checks only
		 * for Bluetooth Classic robots. DiscoveryAgentLE checks only for
		 * Bluetooth LE robots.
		 */
		System.out.println("IRR>>>>>>Conectando");

		connectRobot();

	}

	/**
	 * Function to disconnect from the Sphero device.
	 */
	@SimpleFunction(description = "Function to disconnect from the Sphero device.")
	public void Disconnect() {

		System.out.println("IRR>>>>>>Desconectando");

		disconnectRobot();
		
	}

	/**
	 * Function to move the Sphero device.
	 * 
	 * @param lit
	 */
	@SimpleFunction(description = "Function to move the Sphero device.")
	public void DriveAndRotate(float angle, float velocity) {
		// If the robot is null, then it is probably not connected and nothing
		// needs to be done
		if (mRobot == null) {
			System.out.println("IRR>>>>>>No hay robot para DRIVE");
			return;
		} else {
			System.out.println("IRR>>>>>>Moviendo");

			// Forward
			mRobot.drive(angle, velocity);
		}
	}

	/**
	 * Function to rotate the Sphero device.
	 * 
	 * @param lit
	 */
	@SimpleFunction(description = "Function to rotate the Sphero device.")
	public void Rotate(float angle) {

		if (mRobot == null) {
			System.out.println("IRR>>>>>>No hay robot para ROTATE");
			return;
		} else {
			mRobot.drive(angle, 0);
		}
	}

	/**
	 * Function to drive the Sphero device.
	 * 
	 * @param lit
	 */
	@SimpleFunction(description = "Function to drive the Sphero device.")
	public void Drive(float velocity) {
		if (mRobot == null) {
			System.out.println("IRR>>>>>>No hay robot para DRIVE");
			return;
		} else {

			mRobot.drive(mRobot.getLastHeading(), velocity);
		}

	}

	/**
	 * Function to stop the Sphero device.
	 * 
	 * @param lit
	 */
	@SimpleFunction(description = "Function to stop the Sphero device.")
	public void Stop() {
		// If the robot is null, then it is probably not connected and nothing
		// needs to be done
		if (mRobot == null) {
			System.out.println("IRR>>>>>>No hay robot para STOP");
			return;
		} else {
			System.out.println("IRR>>>>>>Parando");

			this.macros.setRobotToDefaultState(mRobot);
		}
	}

	/**
	 * Function to Rotate the Base Angle.
	 * 
	 * @param lit
	 */
	@SimpleFunction(description = "Function to Rotate the Base Angle.")
	public void RotateBaseAngle(int angle) {

		this.baseAngle = this.baseAngle + angle;

		if (this.baseAngle > 359) {
			this.baseAngle = 0;
		}

		if (this.baseAngle < 0) {
			baseAngle = 359;
		}

		if (mRobot == null) {
			System.out.println("IRR>>>>>>No hay robot todavia para RotateBaseAngle");
			return;
		} else {
			changeBaseAngle(baseAngle);
		}

	}

	/**
	 * Function to RunColorMacro.
	 */
	@SimpleFunction(description = "Function to RunColorMacro.")
	public void RunColorMacro(int delay, int numLoops) {
		if (mRobot == null) {
			System.out.println("IRR>>>>>>No hay robot para RunColorMacro");
			return;

		}

		this.macros.runColorMacro(mRobot, delay, numLoops);
	}

	/**
	 * Function to RunFigureEightMacro.
	 */
	@SimpleFunction(description = "Function to Run Figure Eight Macro.")
	public void RunFigureEightMacro(int delay, int numLoops) {
		if (mRobot == null) {
			System.out.println("IRR>>>>>>No hay robot para RunFigureEightMacro");
			return;

		}

		this.macros.runFigureEightMacro(mRobot, delay, numLoops);
	}

	/**
	 * Function to RunSpinMacro.
	 */
	@SimpleFunction(description = "Function to Run Spin Macro.")
	public void RunSpinMacro(int numLoops) {
		if (mRobot == null) {
			System.out.println("IRR>>>>>>No hay robot para RunSpinMacro");
			return;

		}

		this.macros.runSpinMacro(mRobot, numLoops);
	}

	/**
	 * Function to RunSquareMacro.
	 */
	@SimpleFunction(description = "Function to Run Square Macro.")
	public void RunSquareMacro(int delay, float speed) {
		if (mRobot == null) {
			System.out.println("IRR>>>>>>No hay robot para RunSquareMacro");
			return;

		}

		this.macros.runSquareMacro(mRobot, delay, speed);
	}

	/**
	 * Function to RunVibrateMacro.
	 */
	@SimpleFunction(description = "Function to Run Vibrate Macro.")
	public void RunVibrateMacro(int delay, int numLoops) {
		if (mRobot == null) {
			System.out.println("IRR>>>>>>No hay robot para RunVibrateMacro");
			return;

		}

		this.macros.runVibrateMacro(mRobot, delay, numLoops);
	}

	////////////
	// EVENTS //
	////////////

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
		EventDispatcher.dispatchEvent(this, "DeviceDisconnected");
	}

	/**
	 * Event to be raised after the device has collisioned with something
	 */
	@SimpleEvent(description = "Event to be raised after the device has collisioned with something", userVisible = true)
	public void Collision() {
		EventDispatcher.dispatchEvent(this, "Collision");
	}

	private class SpheroResponseListener implements ResponseListener {

		private SpheroController controller;

		public SpheroResponseListener(SpheroController controller) {
			this.controller = controller;
		}

		@Override
		public void handleAsyncMessage(AsyncMessage asyncMessage, Robot robot) {
			if (asyncMessage instanceof CollisionDetectedAsyncData) {
				controller.Collision();
			} else if (asyncMessage instanceof DeviceSensorAsyncMessage) {

				ArrayList<DeviceSensorsData> dataList = ((DeviceSensorAsyncMessage) asyncMessage).getAsyncData();

				if (dataList != null) {
					DeviceSensorsData data = dataList.get(0);

					if (data != null) {
						AccelerometerData accelerometerData = data.getAccelerometerData();
						if (accelerometerData != null) {
							this.controller.setAccelerometerX(accelerometerData.getFilteredAcceleration().x);
							this.controller.setAccelerometerY(accelerometerData.getFilteredAcceleration().y);
							this.controller.setAccelerometerZ(accelerometerData.getFilteredAcceleration().z);
						}

						GyroData gyroData = data.getGyroData();

						if (gyroData != null) {
							this.controller.setGyroX(gyroData.getRotationRateFiltered().x);
							this.controller.setGyroY(gyroData.getRotationRateFiltered().y);
							this.controller.setGyroZ(gyroData.getRotationRateFiltered().z);
						}

						LocatorData locationData = data.getLocatorData();
						if (locationData != null) {
							this.controller.setPositionX(locationData.getPositionX());
							this.controller.setPositionY(locationData.getPositionY());

							this.controller.setVelocityX(locationData.getVelocity().x);
							this.controller.setVelocityY(locationData.getVelocity().y);
						}

					}
				}

			}
		}

		@Override
		public void handleResponse(DeviceResponse arg0, Robot arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void handleStringResponse(String arg0, Robot arg1) {
			// TODO Auto-generated method stub

		}

	}

}