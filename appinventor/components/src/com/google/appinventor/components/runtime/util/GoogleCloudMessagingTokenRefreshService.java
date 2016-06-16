package com.google.appinventor.components.runtime.util;

import com.google.android.gms.iid.InstanceIDListenerService;

public class GoogleCloudMessagingTokenRefreshService extends InstanceIDListenerService {
	
	public void onTokenRefresh() {
		GoogleCloudMessagingConnectionServer connectionServer = new GoogleCloudMessagingConnectionServer(getApplicationContext());
		connectionServer.register();
	}

}
