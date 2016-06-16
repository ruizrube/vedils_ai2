package com.google.vedils.gcm.server.registrationclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationClientBean {
	
	@JsonProperty("imei")
	protected String imei;
	
	@JsonProperty("token")
	protected String token;
	
	@JsonProperty("appname")
	protected String appname;
}
