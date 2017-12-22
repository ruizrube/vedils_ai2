package com.google.appinventor.components.runtime.util;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.UUID;

import com.google.appinventor.components.runtime.ActivityDescription;
import com.google.appinventor.components.runtime.ActivityTracker;
import com.google.appinventor.components.runtime.Clock;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.User;
import com.google.appinventor.components.runtime.la4ai.util.DeviceInfoFunctions;
import com.google.appinventor.components.runtime.la4ai.util.GPSTracker;
import com.google.appinventor.components.runtime.la4ai.util.TinyDB;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import android.os.AsyncTask;
import gov.adlnet.xapi.client.StatementClient;
import gov.adlnet.xapi.model.Activity;
import gov.adlnet.xapi.model.ActivityDefinition;
import gov.adlnet.xapi.model.Agent;
import gov.adlnet.xapi.model.Attachment;
import gov.adlnet.xapi.model.Context;
import gov.adlnet.xapi.model.Result;
import gov.adlnet.xapi.model.Score;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.SubStatement;
import gov.adlnet.xapi.model.Verb;

//Used Java Library for xAPI: https://github.com/adlnet/jxapi
public class ActivityTrackerManagerLearningRecordStore implements ActivityTrackerManager {
	
	private ActivityTracker currentActivityTracker;
	private ComponentContainer componentContainer;
	private Statement statement;
	
	//LRS information
	private StatementClient client;
	private String LRS_URI = "http://vedilslearning.ddns.net/data/xAPI";
	//private String USERNAME = "b532f5c2139c70209087207b79bdf21f9e59abec";
	//private String PASSWORD = "30085a28262e00554c3e82ae0eba260108e9ea07";
	//private String BASIC_OAUTH = "YjUzMmY1YzIxMzljNzAyMDkwODcyMDdiNzliZGYyMWY5ZTU5YWJlYzozMDA4NWEyODI2MmUwMDU1NGMzZTgyYWUwZWJhMjYwMTA4ZTllYTA3";
	
	//Record data when not internet access
	private TinyDB tinyDB;
	private int tagDB;
			
	private TimerSendData timerSendData;
	private String currentIP;
	private GPSTracker gpsTracker;
	private String URI = "http://vedils.uca.es/";
	
	
	public ActivityTrackerManagerLearningRecordStore(ActivityTracker currentActivityTracker, ComponentContainer componentContainer) {
		this.currentActivityTracker = currentActivityTracker;
		this.componentContainer = componentContainer;
		this.tinyDB = new TinyDB(componentContainer.$context());
		this.tagDB = 0;
		this.timerSendData = new TimerSendData(this);
		this.gpsTracker = new GPSTracker(componentContainer.$context());
	}
	
	@Override
	public void prepareQueryAutomatic(String actionType, String actionId, String componentType, String componentId,
			String[] paramsName, Object[] paramsValue, String returnParamValue) {
		
		this.statement = new Statement();
		this.statement.setId(UUID.randomUUID().toString());
		this.statement.setTimestamp(Clock.FormatDate(Clock.Now(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
		
		Agent actor = new Agent();
		actor.setName(this.currentActivityTracker.getUser().getName() + 
				" " + this.currentActivityTracker.getUser().getSurname());
		actor.setMbox("mailto:"+this.currentActivityTracker.getUser().getEmail());
		
		Verb verb = new Verb();
		verb.setId(URI + "verbs/" + actionId);
		
		Activity activity = new Activity();
		activity.setId(URI + "activities/" + componentId);
		ActivityDefinition definition = new ActivityDefinition();
		definition.setType(URI + "activities/" + componentType);
		activity.setDefinition(definition);

		this.statement.setActor(actor);
		this.statement.setVerb(verb);
		this.statement.setObject(activity);
		
		Context context = new Context();
		context.setPlatform("VEDILS");
		HashMap<String, JsonElement> extensions = new HashMap<String, JsonElement>();
		extensions.put(URI + "context/appContext", addContextNotificationData());
		context.setExtensions(extensions);
		this.statement.setContext(context);
		
		System.out.println("LRS automatic statement = " + this.statement.serialize().getAsJsonObject().toString());
		
		//And try to send data to LRS
		recordData();
	}

	@Override
	public void prepareQueryManual(String actionId, Object data) {
		
		this.statement = new Statement();
		this.statement.setId(UUID.randomUUID().toString());
		this.statement.setTimestamp(Clock.FormatDate(Clock.Now(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
		
		Agent actor = new Agent();
		actor.setName(this.currentActivityTracker.getUser().getName() + 
				" " + this.currentActivityTracker.getUser().getSurname());
		actor.setMbox("mailto:"+this.currentActivityTracker.getUser().getEmail());
		
		Verb verb = new Verb();
		if(!actionId.contains("http")) {
			verb.setId(URI + "verbs/" + actionId);
		} else {
			verb.setId(actionId);
		}
		
		this.statement.setActor(actor);
		this.statement.setVerb(verb);
		
		if(data instanceof User) { //Statement type: Actor + Verb + Actor
			User user = (User) data;
			Agent actorSecond = new Agent();
			actorSecond.setName(user.getName() + 
					" " + user.getSurname());
			actorSecond.setMbox("mailto:"+user.getEmail());
			this.statement.setObject(actorSecond);
		} else if(data instanceof Statement) { //Statement type: Actor + Verb + Statement
			Statement statementSecond = (Statement) data;
			SubStatement statementRef = new SubStatement();
			statementRef.setTimestamp(statementSecond.getTimestamp());
			statementRef.setAttachments(new ArrayList<Attachment>()); //for library bug
			statementRef.setActor(statementSecond.getActor());
			statementRef.setVerb(statementSecond.getVerb());
			statementRef.setObject(statementSecond.getObject());
			this.statement.setObject(statementRef);
		} else if(data instanceof String) { //Statement type: Actor + Verb + Activity
			String activityName = (String) data;
			Activity activity = new Activity();
			
			if(!activityName.contains("http")) {
				activity.setId(URI + "activities/" + activityName);
			} else {
				activity.setId(activityName);
			}
			
			ActivityDefinition definition = new ActivityDefinition();
			definition.setType(URI + "activities/specific");
			activity.setDefinition(definition);
			this.statement.setObject(activity);
		} else if(data instanceof ActivityDescription) {
			ActivityDescription activityDescription = (ActivityDescription) data;
			
			Activity activity = new Activity();
			
			if(!activityDescription.Id().contains("http")) {
				activity.setId(URI + "activities/" + activityDescription.Id());
			} else {
				activity.setId(activityDescription.Id());
			}
			
			ActivityDefinition definition = new ActivityDefinition();
			definition.setType(URI + "activities/specific");
			activity.setDefinition(definition);
			statement.setObject(activity);
			
			Result result = new Result();
			result.setCompletion(activityDescription.Completion());
			result.setSuccess(activityDescription.Success());
			
			if(!activityDescription.Duration().isEmpty()) {
				result.setDuration(activityDescription.Duration());
			}
			
			if(activityDescription.ScaledScore() != 0) {
				Score score = new Score();
				score.setScaled(activityDescription.ScaledScore());
				result.setScore(score);
			} else if(activityDescription.MaxScore() != 0 &&
					activityDescription.MinScore() != 0 &&
					activityDescription.RawScore() != 0) {
				Score score = new Score();
				score.setMax(activityDescription.MaxScore());
				score.setMin(activityDescription.MinScore());
				score.setRaw(activityDescription.RawScore());
				result.setScore(score);
			}
			
			statement.setResult(result);
		}
		
		if(!(data instanceof User) && !(data instanceof Statement)) {
			Context context = new Context();
			context.setPlatform("VEDILS");
			HashMap<String, JsonElement> extensions = new HashMap<String, JsonElement>();
			extensions.put(URI + "context/appContext", addContextNotificationData());
			context.setExtensions(extensions);
			this.statement.setContext(context);
		}
		
		System.out.println("LRS automatic statement = " + this.statement.serialize().getAsJsonObject().toString());
		
		//And try to send data to LRS
		recordData();
	}
	
	@Override
	public Object prepareQueryManualWithReturn(String actionId, Object data) {
		
		Statement statement = new Statement();
		statement.setId(UUID.randomUUID().toString());
		statement.setTimestamp(Clock.FormatDate(Clock.Now(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
		
		Agent actor = new Agent();
		actor.setName(this.currentActivityTracker.getUser().getName() + 
				" " + this.currentActivityTracker.getUser().getSurname());
		actor.setMbox("mailto:"+this.currentActivityTracker.getUser().getEmail());
		
		Verb verb = new Verb();
		if(!actionId.contains("http")) {
			verb.setId(URI + "verbs/" + actionId);
		} else {
			verb.setId(actionId);
		}
		
		statement.setActor(actor);
		statement.setVerb(verb);
		
		if(data instanceof User) { //Statement type: Actor + Verb + Actor
			User user = (User) data;
			Agent actorSecond = new Agent();
			actorSecond.setName(user.getName() + 
					" " + user.getSurname());
			actorSecond.setMbox("mailto:"+user.getEmail());
			statement.setObject(actorSecond);
		} else if(data instanceof Statement) { //Statement type: Actor + Verb + Statement
			Statement statementSecond = (Statement) data;
			SubStatement statementRef = new SubStatement();
			statementRef.setActor(statementSecond.getActor());
			statementRef.setVerb(statementSecond.getVerb());
			statementRef.setObject(statementSecond.getObject());
			statement.setObject(statementRef);
		} else if(data instanceof String) { //Statement type: Actor + Verb + Activity
			String activityName = (String) data;
			Activity activity = new Activity();
			
			if(!activityName.contains("http")) {
				activity.setId(URI + "activities/" + activityName);
			} else {
				activity.setId(activityName);
			}
			
			ActivityDefinition definition = new ActivityDefinition();
			definition.setType(URI + "activities/specific");
			activity.setDefinition(definition);
			statement.setObject(activity);
		} else if(data instanceof ActivityDescription) {
			ActivityDescription activityDescription = (ActivityDescription) data;
			
			Activity activity = new Activity();
			
			if(!activityDescription.Id().contains("http")) {
				activity.setId(URI + "activities/" + activityDescription.Id());
			} else {
				activity.setId(activityDescription.Id());
			}
			
			ActivityDefinition definition = new ActivityDefinition();
			definition.setType(URI + "activities/specific");
			activity.setDefinition(definition);
			statement.setObject(activity);
			
			Result result = new Result();
			result.setCompletion(activityDescription.Completion());
			result.setSuccess(activityDescription.Success());
			
			if(!activityDescription.Duration().isEmpty()) {
				result.setDuration(activityDescription.Duration());
			}
			
			if(activityDescription.ScaledScore() != 0) {
				Score score = new Score();
				score.setScaled(activityDescription.ScaledScore());
				result.setScore(score);
			} else if(activityDescription.MaxScore() != 0 &&
					activityDescription.MinScore() != 0 &&
					activityDescription.RawScore() != 0) {
				Score score = new Score();
				score.setMax(activityDescription.MaxScore());
				score.setMin(activityDescription.MinScore());
				score.setRaw(activityDescription.RawScore());
				result.setScore(score);
			}
			
			statement.setResult(result);
		}
		
		System.out.println("LRS automatic statement = " + statement.serialize().getAsJsonObject().toString());
		
		//And return the content
		return statement;
	}
	
	@Deprecated
	@Override
	public void prepareQueryManual(String actionId, String param1, String param2, String param3) {}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void recordData() {
		
		if(DeviceInfoFunctions.checkInternetConnection(componentContainer.$context()) && currentActivityTracker.getSynchronizationMode() == Component.REALTIME) {
			if(this.client == null) {
				System.out.println("LRS OAUTH = " + currentActivityTracker.TableId());
				try {
					this.client = new StatementClient(LRS_URI, currentActivityTracker.TableId());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
			
			new AsyncTask() {
				@Override
				protected Object doInBackground(Object... arg0) {
					try {
						client.postStatement(statement);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("LRS error = " + e.getMessage());
					}
					return null;
				}
			}.execute();
		} else {
			if(currentActivityTracker.getSynchronizationMode() == Component.BATCH && tagDB == 0) { 
				Timer timer = new Timer();
				timer.schedule(timerSendData, 0, currentActivityTracker.getBatchTime() * 1000);
			}
			tinyDB.StoreValue(Integer.toString(tagDB), statement);
			System.out.println("Store value in TinyDB: " +statement.toString());
			tagDB++;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void recordDataBatch() {		
		if(DeviceInfoFunctions.checkInternetConnection(componentContainer.$context())) {
			if(this.client == null) {
				System.out.println("LRS OAUTH = " + currentActivityTracker.TableId());
				try {
					this.client = new StatementClient(LRS_URI, currentActivityTracker.TableId());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
			
			new AsyncTask() {
				@Override
				protected Object doInBackground(Object... arg0) {
					try {
						client.postStatement(statement);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("LRS error = " + e.getMessage());
					}
					return null;
				}
			}.execute();
		}
	}
	
	private JsonObject addContextNotificationData() {
		JsonObject contextData = new JsonObject();
		contextData.addProperty(URI + "context/IP", DeviceInfoFunctions.getCurrentIP(currentActivityTracker.getCommunicationMode(), this.componentContainer.$context()));
		contextData.addProperty(URI + "context/MAC", DeviceInfoFunctions.getMAC(componentContainer.$context()));
		contextData.addProperty(URI + "context/IMEI", DeviceInfoFunctions.getIMEI(componentContainer.$context()));
		contextData.addProperty(URI + "context/APILevel", DeviceInfoFunctions.getAndroidAPIVersion());
		contextData.addProperty(URI + "context/Latitude", gpsTracker.getLatitude());
		contextData.addProperty(URI + "context/Longitude", gpsTracker.getLongitude());
		contextData.addProperty(URI + "context/Date", Clock.FormatDate(Clock.Now(), "MM/dd/yyyy HH:mm:ss"));
		contextData.addProperty(URI + "context/AppID", componentContainer.$context().getApplicationInfo().packageName);
		contextData.addProperty(URI + "context/ScreenID", componentContainer.$form().getLocalClassName());
		return contextData;
	}
}
