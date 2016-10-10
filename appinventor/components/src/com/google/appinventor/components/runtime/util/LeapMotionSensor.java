/**
 * 
 */
package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.runtime.HandGestureSensor;
import com.leapmotion.leap.Arm;
import com.leapmotion.leap.Bone;
import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Gesture.State;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.ScreenTapGesture;
import com.leapmotion.leap.SwipeGesture;
import com.leapmotion.leap.Tool;
import com.leapmotion.leap.Vector;

/**
 * @author ivanruizrube
 *
 */
public class LeapMotionSensor extends Listener {

	private boolean previousLeftHand = false;
	private boolean previousRightHand = false;
	private boolean currentLeftHand = false;
	private boolean currentRightHand = false;

	HandGestureSensor component;

	public HandGestureSensor getComponent() {
		return component;
	}

	public void setComponent(HandGestureSensor component) {
		this.component = component;
	}

	public void onInit(Controller controller) {
		System.out.println("Initialized");
	}

	public void onConnect(Controller controller) {
		System.out.println("Connected");
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
		controller.config().setFloat("Gesture.Swipe.MinLength", 100.0f);
		controller.config().setFloat("Gesture.Swipe.MinVelocity", 750f);
		controller.config().setFloat("Gesture.KeyTap.MinDownVelocity", 40.0f);
		controller.config().setFloat("Gesture.KeyTap.HistorySeconds", .2f);
		controller.config().setFloat("Gesture.KeyTap.MinDistance", 1.0f);
		controller.config().save();
		
		
	}

	public void onDisconnect(Controller controller) {
		// Note: not dispatched when running in a debugger.
		System.out.println("Disconnected");
	}

	public void onExit(Controller controller) {
		System.out.println("Exited");
	}

	public void onFrame(Controller controller) {
		// Get the most recent frame and report some basic information
		Frame frame = controller.frame();
		// System.out.println("Frame id: " + frame.id()
		// + ", timestamp: " + frame.timestamp()
		// + ", hands: " + frame.hands().count()
		// + ", fingers: " + frame.fingers().count()
		// + ", tools: " + frame.tools().count()
		// + ", gestures " + frame.gestures().count());

		manageHands(frame);

		manageTools(frame);

		manageGestures(controller);

	}

	private void manageGestures(Controller controller) {
		GestureList gestures = controller.frame().gestures();
		for (int i = 0; i < gestures.count(); i++) {
			Gesture gesture = gestures.get(i);

			switch (gesture.type()) {
			case TYPE_CIRCLE:
				CircleGesture circle = new CircleGesture(gesture);

				// Calculate clock direction using the angle between circle
				// normal and pointable
				int clockwiseness;
				if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI / 2) {
					// Clockwise if angle is less than 90 degrees
					clockwiseness = 0; // "clockwise";
				} else {
					clockwiseness = 1; // "counterclockwise";
				}

				// Calculate angle swept since last frame
				double sweptAngle = 0;
				if (circle.state() != State.STATE_START) {
					CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
					sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
				}

				// System.out.println(" Circle id: " + circle.id() + ", " +
				// circle.state() + ", progress: "
				// + circle.progress() + ", radius: " + circle.radius() + ",
				// angle: " + Math.toDegrees(sweptAngle)
				// + ", " + clockwiseness);

				for (Hand hand : gesture.hands()) {
					for (Pointable pointable : gesture.pointables()) {
						if (pointable.isFinger()) {
							Finger finger = new Finger(pointable);
							int idFinger = finger.id() % 10 + 1;
							this.getComponent().CircleGesture((hand.isLeft() ? 0 : 1), idFinger, clockwiseness,
									circle.progress());
						}
					}

				}

				break;
			case TYPE_SWIPE:
				SwipeGesture swipe = new SwipeGesture(gesture);
				System.out.println("  Swipe id: " + swipe.id() + ", " + swipe.state() + ", position: "
						+ swipe.position() + ", direction: " + swipe.direction() + ", speed: " + swipe.speed());

				boolean isHorizontal = Math.abs(swipe.direction().getX()) > Math.abs(swipe.direction().getY());
				int swipeDirection;
				// Classify as right-left or up-down
				if (isHorizontal) {
					if (swipe.direction().getX() > 0) {
						swipeDirection = 1; // "right";
					} else {
						swipeDirection = 0; // "left";
					}
				} else { // vertical
					if (swipe.direction().getY() > 0) {
						swipeDirection = 2; // "up";
					} else {
						swipeDirection = 3; // "down";
					}
				}

				for (Hand hand : gesture.hands()) {
					for (Pointable pointable : gesture.pointables()) {
						if (pointable.isFinger()) {
							Finger finger = new Finger(pointable);
							int idFinger = finger.id() % 10 + 1;
							this.getComponent().SwipeGesture((hand.isLeft() ? 0 : 1), idFinger, swipeDirection);
						}
					}

				}

				break;
			case TYPE_SCREEN_TAP:
				ScreenTapGesture screenTap = new ScreenTapGesture(gesture);
				// System.out.println(" Screen Tap id: " + screenTap.id() + ", "
				// + screenTap.state() + ", position: "
				// + screenTap.position() + ", direction: " +
				// screenTap.direction());

				for (Hand hand : screenTap.hands()) {
					for (Pointable pointable : screenTap.pointables()) {
						if (pointable.isFinger()) {
							Finger finger = new Finger(pointable);
							int idFinger = finger.id() % 10 + 1;
							this.getComponent().ScreenTapGesture((hand.isLeft() ? 0 : 1), idFinger);
						}
					}
				}

				break;
			case TYPE_KEY_TAP:
				KeyTapGesture keyTap = new KeyTapGesture(gesture);
				// System.out.println(" Key Tap id: " + keyTap.id() + ", " +
				// keyTap.state() + ", position: "
				// + keyTap.position() + ", direction: " + keyTap.direction());

				for (Hand hand : gesture.hands()) {
					for (Pointable pointable : gesture.pointables()) {
						if (pointable.isFinger()) {
							Finger finger = new Finger(pointable);
							int idFinger = finger.id() % 10 + 1;
							this.getComponent().KeyTapGesture((hand.isLeft() ? 0 : 1), idFinger);
						}
					}

				}

				break;
			default:
				System.out.println("Unknown gesture type.");
				break;
			}
		}

	}

	private void manageTools(Frame frame) {
		// Get tools
		for (Tool tool : frame.tools()) {
			System.out.println("  Tool id: " + tool.id() + ", position: " + tool.tipPosition() + ", direction: "
					+ tool.direction());
		}

	}

	private void manageHands(Frame frame) {
		currentLeftHand = false;
		currentRightHand = false;

		// Get hands
		for (Hand hand : frame.hands()) {

			if (hand.isLeft()) {
				currentLeftHand = true;
			}
			if (hand.isRight()) {
				currentRightHand = true;
			}

			String handType = hand.isLeft() ? "Left hand" : "Right hand";
			// System.out.println(" " + handType + ", id: " + hand.id()
			// + ", palm position: " + hand.palmPosition());

			// Get the hand's normal vector and direction
			Vector normal = hand.palmNormal();
			Vector direction = hand.direction();

			// Calculate the hand's pitch, roll, and yaw angles
			// System.out.println(" pitch: " + Math.toDegrees(direction.pitch())
			// + " degrees, "
			// + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
			// + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");

			// Get arm bone
			Arm arm = hand.arm();
			// System.out.println(" Arm direction: " + arm.direction()
			// + ", wrist position: " + arm.wristPosition()
			// + ", elbow position: " + arm.elbowPosition());

			// Get fingers
			for (Finger finger : hand.fingers()) {
				// System.out.println(" " + finger.type() + ", id: " +
				// finger.id()
				// + ", length: " + finger.length()
				// + "mm, width: " + finger.width() + "mm");

				// Get Bones
				for (Bone.Type boneType : Bone.Type.values()) {
					Bone bone = finger.bone(boneType);
					// System.out.println(" " + bone.type()
					// + " bone, start: " + bone.prevJoint()
					// + ", end: " + bone.nextJoint()
					// + ", direction: " + bone.direction());
				}
			}
		}

		// NOTIFICAMOS APARICIÓN DE MANOS
		if (previousLeftHand == false && currentLeftHand == true) {
			this.getComponent().HandAppears(0);
		}

		if (previousRightHand == false && currentRightHand == true) {
			this.getComponent().HandAppears(1);
		}

		if (previousLeftHand == true && currentLeftHand == false) {
			this.getComponent().HandDisappears(0);
		}

		if (previousRightHand == true && currentRightHand == false) {
			this.getComponent().HandDisappears(1);
		}

		previousLeftHand = currentLeftHand;
		previousRightHand = currentRightHand;

	}
}
