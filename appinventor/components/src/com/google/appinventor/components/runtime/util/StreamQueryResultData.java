package com.google.appinventor.components.runtime.util;

import java.util.TimerTask;

import org.json.JSONObject;

public class StreamQueryResultData extends TimerTask {
	
	private ActivityQueryManager activityQueryManagerComponent;
	private String URL_SERVER_QUERY = "http://vedilsanalytics.uca.es:80/AnalyticsWSForAppInventor/FlinkClient/readFromKafkaQueue/";
	//private String URL_SERVER_QUERY = "http://192.168.1.22:8080/AnalyticsWSForAppInventor/FlinkClient/readFromKafkaQueue/";
	
	private String topic;
	private String idQuery;
	
	public StreamQueryResultData(ActivityQueryManager activityQueryManagerComponent, String topic, String idQuery) {
		this.activityQueryManagerComponent = activityQueryManagerComponent;
		this.topic = topic;
		this.idQuery = idQuery;
	}
	
	@Override
	public void run() {
		JSONObject information = new JSONObject();
		try {
			information.put("topic", this.topic);
			information.put("idQuery", this.idQuery);
		} catch(Exception e) {
			
		}
		new AsyncHttpRequestManager(URL_SERVER_QUERY, "POST", information, this.activityQueryManagerComponent, false).execute();
	}
}
