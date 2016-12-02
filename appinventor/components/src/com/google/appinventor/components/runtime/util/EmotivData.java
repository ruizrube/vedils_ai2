package com.google.appinventor.components.runtime.util;

import java.util.Map;

import com.emotiv.insight.IEdk.IEE_DataChannel_t;

public class EmotivData {
	private float runningTime = 0;

	Map<IEE_DataChannel_t, double[]> fftData;
	
	Map<IEE_DataChannel_t, Integer> contactQuality;
	
	private int batteryLevel = 0;
	
	private float excitamentLevel = 0;
	private float interestLevel = 0;
	private float stressLevel = 0;
	private float engagementLevel = 0;
	private float attentionLevel = 0;
	private float meditationLevel = 0;

	private double XAngularVelocity = 0;
	private double YAngularVelocity = 0;
	private double ZAngularVelocity = 0;
	private double XAcceleration = 0;
	private double YAcceleration = 0;
	private double ZAcceleration = 0;
	private double XMagnetization = 0;
	private double YMagnetization = 0;
	private double ZMagnetization = 0;

	private float powerForDisappearCommand;
	private float powerForDropCommand;
	private float powerForLeftCommand;
	private float powerForLiftCommand;
	private float powerForNeutralCommand;
	private float powerForPullCommand;
	private float powerForPushCommand;
	private float powerForRightCommand;
	private float powerForRotateClockwiseCommand;
	private float powerForRotateCounterClockwiseCommand;
	private float powerForRotateForwardsCommand;
	private float powerForRotateLeftCommand;
	private float powerForRotateReverseCommand;
	private float powerForRotateRightCommand;
	private float powerForNeutralExpression;
	private float powerForSimileExpression;
	private float powerForFrownExpression;
	private float powerForClenchExpression;

	public Map<IEE_DataChannel_t, double[]> getFFTData() {
		return fftData;
	}

	public void setFFTData(Map<IEE_DataChannel_t, double[]> data) {
		this.fftData = data;
	}

	public int getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public Map<IEE_DataChannel_t, Integer> getContactQuality() {
		return contactQuality;
	}

	public void setContactQuality(Map<IEE_DataChannel_t, Integer> contactQuality) {
		this.contactQuality = contactQuality;
	}

	public float getMeditationLevel() {
		return meditationLevel;
	}

	public void setMeditationLevel(int meditationLevel) {
		this.meditationLevel = meditationLevel;
	}

	public float getEngagementLevel() {
		return engagementLevel;
	}

	public void setEngagementLevel(float engagementLevel) {
		this.engagementLevel = engagementLevel;
	}

	public double getXAngularVelocity() {
		return XAngularVelocity;
	}

	public void setXAngularVelocity(double xAngularVelocity) {
		XAngularVelocity = xAngularVelocity;
	}

	public double getYAngularVelocity() {
		return YAngularVelocity;
	}

	public void setYAngularVelocity(double yAngularVelocity) {
		YAngularVelocity = yAngularVelocity;
	}

	public double getZAngularVelocity() {
		return ZAngularVelocity;
	}

	public void setZAngularVelocity(double zAngularVelocity) {
		ZAngularVelocity = zAngularVelocity;
	}

	public double getXAcceleration() {
		return XAcceleration;
	}

	public void setXAcceleration(double xAcceleration) {
		XAcceleration = xAcceleration;
	}

	public double getYAcceleration() {
		return YAcceleration;
	}

	public void setYAcceleration(double yAcceleration) {
		YAcceleration = yAcceleration;
	}

	public double getZAcceleration() {
		return ZAcceleration;
	}

	public void setZAcceleration(double zAcceleration) {
		ZAcceleration = zAcceleration;
	}

	public double getXMagnetization() {
		return XMagnetization;
	}

	public void setXMagnetization(double xMagnetization) {
		XMagnetization = xMagnetization;
	}

	public double getYMagnetization() {
		return YMagnetization;
	}

	public void setYMagnetization(double yMagnetization) {
		YMagnetization = yMagnetization;
	}

	public double getZMagnetization() {
		return ZMagnetization;
	}

	public void setZMagnetization(double zMagnetization) {
		ZMagnetization = zMagnetization;
	}

	public float getPowerForDisappearCommand() {
		return powerForDisappearCommand;
	}

	public void setPowerForDisappearCommand(float powerForDisappearCommand) {
		this.powerForDisappearCommand = powerForDisappearCommand;
	}

	public float getPowerForDropCommand() {
		return powerForDropCommand;
	}

	public void setPowerForDropCommand(float powerForDropCommand) {
		this.powerForDropCommand = powerForDropCommand;
	}

	public float getPowerForLeftCommand() {
		return powerForLeftCommand;
	}

	public void setPowerForLeftCommand(float powerForLeftCommand) {
		this.powerForLeftCommand = powerForLeftCommand;
	}

	public float getPowerForLiftCommand() {
		return powerForLiftCommand;
	}

	public void setPowerForLiftCommand(float powerForLiftCommand) {
		this.powerForLiftCommand = powerForLiftCommand;
	}

	public float getPowerForNeutralCommand() {
		return powerForNeutralCommand;
	}

	public void setPowerForNeutralCommand(float powerForNeutralCommand) {
		this.powerForNeutralCommand = powerForNeutralCommand;
	}

	public float getPowerForPullCommand() {
		return powerForPullCommand;
	}

	public void setPowerForPullCommand(float powerForPullCommand) {
		this.powerForPullCommand = powerForPullCommand;
	}

	public float getPowerForPushCommand() {
		return powerForPushCommand;
	}

	public void setPowerForPushCommand(float powerForPushCommand) {
		this.powerForPushCommand = powerForPushCommand;
	}

	public float getPowerForRightCommand() {
		return powerForRightCommand;
	}

	public void setPowerForRightCommand(float powerForRightCommand) {
		this.powerForRightCommand = powerForRightCommand;
	}

	public float getPowerForRotateClockwiseCommand() {
		return powerForRotateClockwiseCommand;
	}

	public void setPowerForRotateClockwiseCommand(float powerForRotateClockwiseCommand) {
		this.powerForRotateClockwiseCommand = powerForRotateClockwiseCommand;
	}

	public float getPowerForRotateCounterClockwiseCommand() {
		return powerForRotateCounterClockwiseCommand;
	}

	public void setPowerForRotateCounterClockwiseCommand(float powerForRotateCounterClockwiseCommand) {
		this.powerForRotateCounterClockwiseCommand = powerForRotateCounterClockwiseCommand;
	}

	public float getPowerForRotateForwardsCommand() {
		return powerForRotateForwardsCommand;
	}

	public void setPowerForRotateForwardsCommand(float powerForRotateForwardsCommand) {
		this.powerForRotateForwardsCommand = powerForRotateForwardsCommand;
	}

	public float getPowerForRotateLeftCommand() {
		return powerForRotateLeftCommand;
	}

	public void setPowerForRotateLeftCommand(float powerForRotateLeftCommand) {
		this.powerForRotateLeftCommand = powerForRotateLeftCommand;
	}

	public float getPowerForRotateReverseCommand() {
		return powerForRotateReverseCommand;
	}

	public void setPowerForRotateReverseCommand(float powerForRotateReverseCommand) {
		this.powerForRotateReverseCommand = powerForRotateReverseCommand;
	}

	public float getPowerForRotateRightCommand() {
		return powerForRotateRightCommand;
	}

	public void setPowerForRotateRightCommand(float powerForRotateRightCommand) {
		this.powerForRotateRightCommand = powerForRotateRightCommand;
	}

	public float getPowerForSimileExpression() {
		return powerForSimileExpression;
	}

	public void setPowerForSimileExpression(float powerForSimileExpression) {
		this.powerForSimileExpression = powerForSimileExpression;
	}

	public float getPowerForFrownExpression() {
		return powerForFrownExpression;
	}

	public void setPowerForFrownExpression(float powerForFrownExpression) {
		this.powerForFrownExpression = powerForFrownExpression;
	}

	public float getPowerForClenchExpression() {
		return powerForClenchExpression;
	}

	public void setPowerForClenchExpression(float powerForClenchExpression) {
		this.powerForClenchExpression = powerForClenchExpression;
	}

	public float getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(float runningTime) {
		this.runningTime = runningTime;
	}

	public float getPowerForNeutralExpression() {
		return powerForNeutralExpression;
	}

	public void setPowerForNeutralExpression(float powerForNeutralExpression) {
		this.powerForNeutralExpression = powerForNeutralExpression;
	}

	public float getExcitamentLevel() {
		return excitamentLevel;
	}

	public void setExcitamentLevel(float excitamentLevel) {
		this.excitamentLevel = excitamentLevel;
	}

	public float getInterestLevel() {
		return interestLevel;
	}

	public void setInterestLevel(float interestLevel) {
		this.interestLevel = interestLevel;
	}

	public float getStressLevel() {
		return stressLevel;
	}

	public void setStressLevel(float stressLevel) {
		this.stressLevel = stressLevel;
	}

	public float getAttentionLevel() {
		return attentionLevel;
	}

	public void setAttentionLevel(float attentionLevel) {
		this.attentionLevel = attentionLevel;
	}

}
