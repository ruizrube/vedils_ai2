package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.vr4ai.VRActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.InputDevice;

@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "Controller source for VR (by SPI-FM at UCA)", category = ComponentCategory.VEDILSVIRTUALREALITY, iconName = "images/virtualRealityController.png")
public class VRController extends AndroidNonvisibleComponent {

	public float rotationSpeed=1;
	public float moveSpeed=1;
	private static final int DEFAULT_ROTATION = 1;
	private ComponentContainer container;

	public BroadcastReceiver pressAEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			PressAButton();
		}

	};
	public BroadcastReceiver pressBEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			PressBButton();
		}

	};
	public BroadcastReceiver pressCEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			PressCButton();
		}

	};
	public BroadcastReceiver pressDEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			PressDButton();
		}

	};
	public BroadcastReceiver pressZEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			PressZButton();
		}

	};
	public BroadcastReceiver pressXEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			PressXButton();
		}

	};
	public BroadcastReceiver pressUpJoystickEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			PressUpJoystick();
		}

	};
	public BroadcastReceiver pressDownJoystickEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			PressDownJoystick();
		}

	};
	public BroadcastReceiver pressUpLeftJoystickEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			PressUpLeftJoystick();
		}

	};
	public BroadcastReceiver pressUpRightJoystickEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			PressUpRightJoystick();
		}

	};
	public BroadcastReceiver pressDownLeftJoystickEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			PressDownLeftJoystick();
		}

	};
	public BroadcastReceiver pressDownRightJoystickEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			PressDownRightJoystick();
		}

	};
	public BroadcastReceiver pressRightJoystickEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			PressRightJoystick();
		}

	};
	public BroadcastReceiver pressLeftJoystickEventBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			PressLeftJoystick();
		}

	};

	public VRController(ComponentContainer container) {
		super(container.$form());
		this.container = container;
	}

	@SimpleFunction(description = "check connection controller", userVisible = true)
	public boolean CheckControllerConection() {
	   boolean hasController=false;
		 int[] deviceIds = InputDevice.getDeviceIds();
		    for (int deviceId : deviceIds) {
		        InputDevice dev = InputDevice.getDevice(deviceId);
		        int sources = dev.getSources();

		        // Verify that the device has gamepad buttons, control sticks, or both.
		        if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
		                || ((sources & InputDevice.SOURCE_JOYSTICK)
		                == InputDevice.SOURCE_JOYSTICK)) {
		        	hasController=true;
		            }
		    }
			return hasController;

	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ONLY_VRSCENE, defaultValue = "")
	@SimpleProperty(description = "Stick the virtual object to a given Camera", userVisible = true)
	public void VRScene(VRScene scene) {
		if (scene != null) {
			scene.hasController = true;
			scene.setControllerComponent(this);
			

		}

	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = DEFAULT_ROTATION+"")
	@SimpleProperty(description = "speed of rotate", userVisible = true)
	public void RotateSpeed(int rotateSpeed) {
		if(rotateSpeed>0&&rotateSpeed<11){
		this.rotationSpeed=rotateSpeed/10.0f;
		}
		else
		{
			this.rotationSpeed=0.2f;
		}

	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = DEFAULT_ROTATION+"")
	@SimpleProperty(description = "speed of move", userVisible = true)
	public void MoveSpeed(int moveSpeed) {
		if(moveSpeed>0&&moveSpeed<11){
		this.moveSpeed=moveSpeed/10.0f;
		}
		else
		{
			this.moveSpeed=0.2f;
		}

	}
	
	// declarare el receiver en el VRScene si el hascontroller es true
	@SimpleEvent
	public void PressAButton() {
		EventDispatcher.dispatchEvent(this, "PressAButton");
	}

	@SimpleEvent
	public void PressBButton() {
		EventDispatcher.dispatchEvent(this, "PressBButton");
	}

	@SimpleEvent
	public void PressDButton() {
		EventDispatcher.dispatchEvent(this, "PressDButton");
	}

	@SimpleEvent
	public void PressCButton() {
		EventDispatcher.dispatchEvent(this, "PressCButton");
	}
	@SimpleEvent
	public void PressXButton() {
		EventDispatcher.dispatchEvent(this, "PressXButton");
	}
	@SimpleEvent
	public void PressZButton() {
		EventDispatcher.dispatchEvent(this, "PressZButton");
	}
	@SimpleEvent
	public void PressUpJoystick() {
		EventDispatcher.dispatchEvent(this, "PressUpJoystick");
	}
	@SimpleEvent
	public void PressDownJoystick() {
		EventDispatcher.dispatchEvent(this, "PressDownJoystick");
	}
	@SimpleEvent
	public void PressLeftJoystick() {
		EventDispatcher.dispatchEvent(this, "PressLeftJoystick");
	}
	@SimpleEvent
	public void PressRightJoystick() {
		EventDispatcher.dispatchEvent(this, "PressRightJoystick");
	}
	@SimpleEvent
	public void PressUpLeftJoystick() {
		EventDispatcher.dispatchEvent(this, "PressUpLeftJoystick");
	}
	@SimpleEvent
	public void PressUpRightJoystick() {
		EventDispatcher.dispatchEvent(this, "PressUpRightJoystick");
	}
	@SimpleEvent
	public void PressDownLeftJoystick() {
		EventDispatcher.dispatchEvent(this, "PressDownLeftJoystick");
	}
	@SimpleEvent
	public void PressDownRightJoystick() {
		EventDispatcher.dispatchEvent(this, "PressDownRightJoystick");
	}
	
	

	@SimpleFunction(description = "Reset position  object3D", userVisible = true)
	public void Reset() {
	
		    Intent resetIntent = new Intent(VRActivity.VR_3DOBJECT_RESET);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(resetIntent);

	}
	
	@SimpleFunction(description = "Rotate left position  object3D", userVisible = true)
	public void RotateLeft() {
	
		    Intent rotateLeft = new Intent(VRActivity.VR_3DOBJECT_ROTATE_LEFT);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(rotateLeft);

	}
	@SimpleFunction(description = "Rotate right position  object3D", userVisible = true)
	public void RotateRight() {
	
		    Intent rotateRight = new Intent(VRActivity.VR_3DOBJECT_ROTATE_RIGHT);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(rotateRight);

	}
	@SimpleFunction(description = "Rotate up position  object3D", userVisible = true)
	public void RotateUp() {
	
		    Intent rotateUp = new Intent(VRActivity.VR_3DOBJECT_ROTATE_UP);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(rotateUp);

	}
	@SimpleFunction(description = "Rotate down position  object3D", userVisible = true)
	public void RotateDown() {
	
		    Intent rotateDown = new Intent(VRActivity.VR_3DOBJECT_ROTATE_DOWN);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(rotateDown);

	}
	@SimpleFunction(description = "Zoom in position  object3D", userVisible = true)
	public void ZoomIn() {
	
		    Intent zoomIn = new Intent(VRActivity.VR_3DOBJECT_ZOOM_IN);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(zoomIn);

	}
	@SimpleFunction(description = "Zoom out position  object3D", userVisible = true)
	public void ZoomOut() {
	
		    Intent zoomOut = new Intent(VRActivity.VR_3DOBJECT_ZOOM_OUT);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(zoomOut);

	}
	@SimpleFunction(description = " Move up object3D", userVisible = true)
	public void MoveUp() {
	
		    Intent moveUp = new Intent(VRActivity.VR_3DOBJECT_MOVE_UP);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(moveUp);

	}
	@SimpleFunction(description = "Move down object3D", userVisible = true)
	public void MoveDown() {
	
		    Intent moveDown = new Intent(VRActivity.VR_3DOBJECT_MOVE_DOWN);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(moveDown);

	}
	@SimpleFunction(description = "Zoom out position  object3D", userVisible = true)
	public void MoveLeft() {
	
		    Intent moveLeft = new Intent(VRActivity.VR_3DOBJECT_MOVE_LEFT);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(moveLeft);

	}
	@SimpleFunction(description = "Zoom out position  object3D", userVisible = true)
	public void MoveRight() {
	
		    Intent moveRight = new Intent(VRActivity.VR_3DOBJECT_MOVE_RIGHT);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(moveRight);

	}
	
}
