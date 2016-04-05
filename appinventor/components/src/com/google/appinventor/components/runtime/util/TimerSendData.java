package com.google.appinventor.components.runtime.util;

import java.util.TimerTask;

import com.google.appinventor.components.runtime.util.ActivityTrackerManager;

public class TimerSendData extends TimerTask {
	
	private ActivityTrackerManager currentActivityTrackerManager;
	
	public TimerSendData(ActivityTrackerManager currentActivityTrackerManager) {
		this.currentActivityTrackerManager = currentActivityTrackerManager;
	}
	
	@Override
	public void run() {
		currentActivityTrackerManager.recordDataBatch();
	}

}
