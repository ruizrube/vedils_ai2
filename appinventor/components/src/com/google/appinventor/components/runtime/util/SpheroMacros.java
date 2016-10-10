/**
 * 
 */
package com.google.appinventor.components.runtime.util;

import com.orbotix.ConvenienceRobot;
import com.orbotix.macro.AbortMacroCommand;
import com.orbotix.macro.MacroObject;
import com.orbotix.macro.cmd.BackLED;
import com.orbotix.macro.cmd.Delay;
import com.orbotix.macro.cmd.Fade;
import com.orbotix.macro.cmd.LoopEnd;
import com.orbotix.macro.cmd.LoopStart;
import com.orbotix.macro.cmd.RGB;
import com.orbotix.macro.cmd.RawMotor;
import com.orbotix.macro.cmd.Roll;
import com.orbotix.macro.cmd.RotateOverTime;
import com.orbotix.macro.cmd.Stabilization;

/**
 * @author ruizrube
 *
 */
public class SpheroMacros {

	// Set the robot to a default 'clean' state between running macros
	public void setRobotToDefaultState(ConvenienceRobot mRobot) {
		if (mRobot == null)
			return;

		mRobot.sendCommand(new AbortMacroCommand());
		mRobot.setLed(1.0f, 1.0f, 1.0f);
		mRobot.enableStabilization(true);
		mRobot.setBackLedBrightness(0.0f);
		mRobot.stop();
	}

		

	// Fade the robot LED between three colors in a loop
	public void runColorMacro(ConvenienceRobot mRobot, int delay, int numLoops) {
		if (mRobot == null)
			return;

		setRobotToDefaultState(mRobot);

		MacroObject macro = new MacroObject();

		// Loop as many times as the loop seekbar value
		macro.addCommand(new LoopStart(numLoops));

		// Fade to cyan. Duration of the fade is set to the value of the delay
		// seekbar
		macro.addCommand(new Fade(0, 255, 255, delay));
		// Set a delay so that the next command isn't processed until the
		// previous one is done
		macro.addCommand(new Delay(delay));

		// Fade to magenta. Duration of the fade is set to the value of the
		// delay seekbar
		macro.addCommand(new Fade(255, 0, 255, delay));
		// Set a delay so that the next command isn't processed until the
		// previous one is done
		macro.addCommand(new Delay(delay));

		// Fade to yellow. Duration of the fade is set to the value of the delay
		// seekbar
		macro.addCommand(new Fade(255, 255, 0, delay));
		// Set a delay so that the next command isn't processed until the
		// previous one is done
		macro.addCommand(new Delay(delay));

		// End the current loop and go back to LoopStart if more iterations
		// expected
		macro.addCommand(new LoopEnd());

		// Send the macro to the robot and play
		macro.setMode(MacroObject.MacroObjectMode.Normal);
		mRobot.playMacro(macro);
	}

	
	// Move the robot in the shape of a square with each edge being a new color
	public void runSquareMacro(ConvenienceRobot mRobot, int delay, float speed) {
		if (mRobot == null)
			return;

		setRobotToDefaultState(mRobot);
	
		MacroObject macro = new MacroObject();

		// Set the robot LED to green
		macro.addCommand(new RGB(0, 255, 0, 255));
		// Move the robot forward
		macro.addCommand(new Roll(speed, 0, 0));
		// Wait until the robot should stop moving
		macro.addCommand(new Delay(delay));
		// Stop
		macro.addCommand(new Roll(0.0f, 0, 255));

		// Set the robot LED to blue
		macro.addCommand(new RGB(0, 0, 255, 255));
		// Move the robot to the right
		macro.addCommand(new Roll(speed, 90, 0));
		// Wait until the robot should stop moving
		macro.addCommand(new Delay(delay));
		// Stop
		macro.addCommand(new Roll(0.0f, 90, 255));

		// Set the robot LED to yellow
		macro.addCommand(new RGB(255, 255, 0, 255));
		// Move the robot backwards
		macro.addCommand(new Roll(speed, 180, 0));
		// Wait until the robot should stop moving
		macro.addCommand(new Delay(delay));
		// Stop
		macro.addCommand(new Roll(0.0f, 180, 255));

		// Set the robot LED to red
		macro.addCommand(new RGB(255, 0, 0, 255));
		// Move the robot to the left
		macro.addCommand(new Roll(255, 270, 0));
		// Wait until the robot should stop moving
		macro.addCommand(new Delay(delay));
		// Stop
		macro.addCommand(new Roll(0.0f, 270, 255));

		// Send the macro to the robot and play
		macro.setMode(MacroObject.MacroObjectMode.Normal);
		macro.setRobot(mRobot.getRobot());
		macro.playMacro();

	}

	// Drive the robot in a figure 8 formation
	public void runFigureEightMacro(ConvenienceRobot mRobot, int delay, int numLoops) {
		if (mRobot == null)
			return;

		setRobotToDefaultState(mRobot);

		float speed = numLoops * 0.01f;

		MacroObject macro = new MacroObject();

		macro.addCommand(new Roll(speed, 0, 1000));
		macro.addCommand(new LoopStart(numLoops));
		// Pivot
		macro.addCommand(new RotateOverTime(360, delay));
		macro.addCommand(new Delay(delay));
		// Pivot
		macro.addCommand(new RotateOverTime(-360, delay));
		macro.addCommand(new Delay(delay));
		macro.addCommand(new LoopEnd());
		// Stop
		macro.addCommand(new Roll(0.0f, 0, 255));

		// Send the macro to the robot and play
		macro.setMode(MacroObject.MacroObjectMode.Normal);
		macro.setRobot(mRobot.getRobot());
		macro.playMacro();
	}

	// Flip the robot forward and backwards while changing the LED color
	public void runVibrateMacro(ConvenienceRobot mRobot, int delay, int numLoops) {
		if (mRobot == null)
			return;

		setRobotToDefaultState(mRobot);

		MacroObject macro = new MacroObject();

		// Stabilization must be turned off before you can issue motor commands
		macro.addCommand(new Stabilization(false, 0));

		macro.addCommand(new LoopStart(numLoops));
		// Change the LED to red
		macro.addCommand(new RGB(255, 0, 0, 0));
		// Run the robot's motors backwards
		macro.addCommand(new RawMotor(RawMotor.DriveMode.REVERSE, 255, RawMotor.DriveMode.REVERSE, 255, 100));
		macro.addCommand(new Delay(100));
		// Change the LED to green
		macro.addCommand(new RGB(0, 255, 0, 0));
		// Run the robot's motors forward
		macro.addCommand(new RawMotor(RawMotor.DriveMode.FORWARD, 255, RawMotor.DriveMode.FORWARD, 255, 100));
		macro.addCommand(new Delay(100));
		macro.addCommand(new LoopEnd());

		// Turn stabilization back on
		macro.addCommand(new Stabilization(true, 0));

		// Send the macro to the robot and play
		macro.setMode(MacroObject.MacroObjectMode.Normal);
		macro.setRobot(mRobot.getRobot());
		macro.playMacro();
	}

	// Spin the robot in circles
	public void runSpinMacro(ConvenienceRobot mRobot, int numLoops) {
		setRobotToDefaultState(mRobot);
		float speed = numLoops * 0.01f;

		MacroObject macro = new MacroObject();

		// Set the back LED to full brightness
		macro.addCommand(new BackLED(255, 0));

		// Loop through rotating the robot
		macro.addCommand(new LoopStart(numLoops));
		macro.addCommand(new RotateOverTime(360, (int) (500 * speed)));
		macro.addCommand(new Delay((int) (500 * speed)));
		macro.addCommand(new LoopEnd());

		// Dim the back LED
		macro.addCommand(new BackLED(0, 0));

		// Send the macro to the robot and play
		macro.setMode(MacroObject.MacroObjectMode.Normal);
		macro.setRobot(mRobot.getRobot());
		macro.playMacro();
	}
}
