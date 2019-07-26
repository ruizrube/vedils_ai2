package com.google.appinventor.components.runtime.util;

import java.util.TimerTask;

import com.annimon.stream.Stream;
import com.google.appinventor.components.runtime.BluetoothClient;

public class BluetoothStreamEventsTimerInput extends TimerTask {
	
	private BluetoothClient bluetoothComponent;
	
	public BluetoothStreamEventsTimerInput(BluetoothClient bluetoothComponent) {
		this.bluetoothComponent = bluetoothComponent;
	}

	@Override
	public void run() {
		bluetoothComponent.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(bluetoothComponent.IsConnected()) {
					int receivedByte = bluetoothComponent.ReceiveSigned1ByteNumber();
					if(receivedByte != 0) {
						if(bluetoothComponent.availableSignedDataStream != null) {
							bluetoothComponent.availableSignedDataStream = Stream.concat(Stream.of(receivedByte), bluetoothComponent.availableSignedDataStream);
						} else {
							bluetoothComponent.availableSignedDataStream = Stream.of(receivedByte);
						}
					}	
				}	
			}
		});
	}
}
