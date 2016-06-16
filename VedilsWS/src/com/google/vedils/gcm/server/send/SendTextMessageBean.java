package com.google.vedils.gcm.server.send;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SendTextMessageBean {
	
	@JsonProperty("message")
	protected String message;
	
	@JsonProperty("imei")
	protected String imei;
	
	@JsonProperty("appname")
	protected String appname;
}
