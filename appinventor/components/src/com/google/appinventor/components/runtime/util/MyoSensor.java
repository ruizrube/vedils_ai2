package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.runtime.ArmbandGestureSensor;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.XDirection;

/**
 * 
 * @author SPI-FM
 *
 */
public class MyoSensor extends AbstractDeviceListener {
	
	ArmbandGestureSensor component;
	
	public ArmbandGestureSensor getComponent() {
		return component;
	}
	
	public void setComponent(ArmbandGestureSensor component) {
		this.component = component;
	}
		
	// onConnect() is called whenever a Myo has been connected.
    @Override
    public void onConnect(Myo myo, long timestamp) {
        System.out.println("Myo Connected");
        getComponent().DeviceConnected();
    }
    
    // onDisconnect() is called whenever a Myo has been disconnected.
    @Override
    public void onDisconnect(Myo myo, long timestamp) {
    	System.out.println("Myo Disconnected");
    	getComponent().DeviceDisconnected();
    }
    
    // onArmSync() is called whenever Myo has recognized a Sync Gesture after someone has put it on their
    // arm. This lets Myo know which arm it's on and which way it's facing.
    @Override
    public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
    	System.out.println("Myo Sync Gesture recognized");
    	getComponent().DeviceSynchronized();
    }
    
    // onArmUnsync() is called whenever Myo has detected that it was moved from a stable position on a person's arm after
    // it recognized the arm. Typically this happens when someone takes Myo off of their arm, but it can also happen
    // when Myo is moved around on the arm.
    @Override
    public void onArmUnsync(Myo myo, long timestamp) {
    	System.out.println("Myo UnSync");
    	getComponent().DeviceUnsynchronized();
    }
    
    // onPose() is called whenever a Myo provides a new pose.
    @Override
    public void onPose(Myo myo, long timestamp, Pose pose) {
        // Handle the cases of the Pose enumeration, and change the text of the text view
        // based on the pose we receive.
        switch (pose) {
        	case UNKNOWN:
			break;
        	case REST:
            case DOUBLE_TAP:
            	switch (myo.getArm()) {
                case LEFT:
                	this.getComponent().KeyTapGesture(0);
                    break;
                case RIGHT:
                	this.getComponent().KeyTapGesture(1);
                    break;
            	}
                break;
            case FIST:
            	switch (myo.getArm()) {
                case LEFT:
                	this.getComponent().FistGesture(0);
                    break;
                case RIGHT:
                	this.getComponent().FistGesture(1);
                    break;
            	}
                break;
            case WAVE_IN:
            	switch (myo.getArm()) {
                case LEFT:
                	this.getComponent().WaveInGesture(0);
                    break;
                case RIGHT:
                	this.getComponent().WaveInGesture(1);
                    break;
            	}
                break;
            case WAVE_OUT:
            	switch (myo.getArm()) {
                case LEFT:
                	this.getComponent().WaveOutGesture(0);
                    break;
                case RIGHT:
                	this.getComponent().WaveOutGesture(1);
                    break;
            	}
                break;
            case FINGERS_SPREAD:
            	switch (myo.getArm()) {
                case LEFT:
                	this.getComponent().FingersSpreadGesture(0);
                    break;
                case RIGHT:
                	this.getComponent().FingersSpreadGesture(1);
                    break;
            	}
                break;
        }
        if (pose != Pose.UNKNOWN && pose != Pose.REST) {
            // Tell the Myo to stay unlocked until told otherwise. We do that here so you can
            // hold the poses without the Myo becoming locked.
            myo.unlock(Myo.UnlockType.HOLD);
            // Notify the Myo that the pose has resulted in an action, in this case changing
            // the text on the screen. The Myo will vibrate.
            myo.notifyUserAction();
        } else {
            // Tell the Myo to stay unlocked only for a short period. This allows the Myo to
            // stay unlocked while poses are being performed, but lock after inactivity.
            myo.unlock(Myo.UnlockType.TIMED);
        }
    }
    
    // onOrientationData() is called whenever a Myo provides its current orientation,
    // represented as a quaternion.
    @Override
    public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
        // Calculate Euler angles (roll, pitch, and yaw) from the quaternion.
        float roll = (float) Math.toDegrees(Quaternion.roll(rotation));
        float pitch = (float) Math.toDegrees(Quaternion.pitch(rotation));
        float yaw = (float) Math.toDegrees(Quaternion.yaw(rotation));
        // Adjust roll and pitch for the orientation of the Myo on the arm.
        if (myo.getXDirection() == XDirection.TOWARD_ELBOW) {
            roll *= -1;
            pitch *= -1;
        }
        getComponent().OrientationData(pitch, yaw, roll);
    }
}