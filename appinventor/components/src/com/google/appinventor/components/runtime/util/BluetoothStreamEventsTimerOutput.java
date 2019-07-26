package com.google.appinventor.components.runtime.util;

import java.util.TimerTask;

import com.google.appinventor.components.runtime.BluetoothClient;

public class BluetoothStreamEventsTimerOutput extends TimerTask {
	
	private BluetoothClient bluetoothComponent;
	
	public BluetoothStreamEventsTimerOutput(BluetoothClient bluetoothComponent) {
		this.bluetoothComponent = bluetoothComponent;
	}

	@Override
	public void run() {
		bluetoothComponent.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(bluetoothComponent.IsConnected()) {
					if(bluetoothComponent.availableSignedDataStream != null) {
						bluetoothComponent.StreamDataReceived(bluetoothComponent.availableSignedDataStream);
						bluetoothComponent.availableSignedDataStream = null; 
					}
				}
			}
		});
	}
}