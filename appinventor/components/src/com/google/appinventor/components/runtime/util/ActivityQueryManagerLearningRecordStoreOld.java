package com.google.appinventor.components.runtime.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.appinventor.components.runtime.ActivityProcessor;

import gov.adlnet.xapi.client.StatementClient;

public class ActivityQueryManagerLearningRecordStoreOld implements ActivityQueryManager {
	
	private ActivityProcessor currentActivityProcessor;
	
	//LRS information
	private StatementClient client;
	private String LRS_URI = "http://vedilsanalytics.uca.es/data/xAPI/statements";
	private JSONObject information;
	protected List<String> fields;
	
	public ActivityQueryManagerLearningRecordStoreOld(ActivityProcessor currentActivityProcessor) {
		this.currentActivityProcessor = currentActivityProcessor;
		information = new JSONObject();
		fields = new ArrayList<String>();
	}
	
	public StatementClient getLRSClient() {
		return this.client;
	}
	
	@Override
	public ActivityProcessor getComponent() {
		return this.currentActivityProcessor;
	}

	@Override
	public void sendQuery() {
		new AsyncHttpRequestManager(LRS_URI, "GET", information, this, false).execute();
	}

	@Override
	public String generateQueryStatement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String makeSelect(List<String> fields, List<String> aggregations) {
		
		fields = this.currentActivityProcessor.obtainFields();
		
		return "";
	}

	@Override
	public String makeFrom() {
		return "";
	}

	@Override
	public String makeWhere() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String makeGroupBy(List<String> groupByColumns) {
		// TODO Auto-generated method stub
		return null;
	}

}
