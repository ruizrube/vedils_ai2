package com.google.appinventor.components.runtime.util;

import java.util.TimerTask;
import com.google.appinventor.components.runtime.ThingSpeakLocationSensor;

public class TimerThingSpeakData extends TimerTask {
	
	private ThingSpeakLocationSensor thingSpeakLocationSensor;
	
	public TimerThingSpeakData(ThingSpeakLocationSensor thingSpeakLocationSensor) {
		this.thingSpeakLocationSensor = thingSpeakLocationSensor;
	}
	
	@Override
	public void run() {
		this.thingSpeakLocationSensor.SendLocationDataToThingSpeak();
	}

}
