package com.google.appinventor.components.runtime.util;

import java.util.TimerTask;

import com.google.appinventor.components.runtime.ActivityTracker;

public class TimerSendData extends TimerTask {
	
	private ActivityTracker currentActivityTracker;
	
	public TimerSendData(ActivityTracker currentActivityTracker) {
		this.currentActivityTracker = currentActivityTracker;
	}
	
	@Override
	public void run() {
		currentActivityTracker.sendDataBatch();
	}

}
