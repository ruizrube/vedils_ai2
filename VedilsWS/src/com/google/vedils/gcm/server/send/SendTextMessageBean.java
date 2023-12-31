package com.google.vedils.gcm.server.send;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SendTextMessageBean {
	
	@JsonProperty("message")
	protected String message;
	
	@JsonProperty("action")
	protected String action;
	
	@JsonProperty("imei")
	protected String imei;
	
/*************************************************** EDSON*/
	@JsonProperty("imei_receiver")
	protected String imei_receiver;
/*************************************************** EDSON*/
	
	@JsonProperty("appname")
	protected String appname;
}
