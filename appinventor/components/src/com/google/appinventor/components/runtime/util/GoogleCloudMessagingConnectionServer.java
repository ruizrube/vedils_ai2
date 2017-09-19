package com.google.appinventor.components.runtime.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.google.android.gms.iid.InstanceID;
import com.google.appinventor.components.runtime.la4ai.util.DeviceInfoFunctions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.os.AsyncTask;
import gnu.math.IntNum;

public class GoogleCloudMessagingConnectionServer {
	
	private static final String SENDER_ID = "88964608807";
	private String token;
	private Context context;
	private String APP_NAME;
	private String IMEI;
	
	//SERVER:
	private static final String URL_SERVER_INSERT = "http://vedils.uca.es:8080/VedilsWS/GcmServer/registrationClient/insertRegistrationClient"; 
	private static final String URL_SERVER_SEND_MESSAGE = "http://vedils.uca.es:8080/VedilsWS/GcmServer/sendData/sendTextMessage";
	private static final String URL_SERVER_DELETE = "http://vedils.uca.es:8080/VedilsWS/GcmServer/registrationClient/deleteRegistrationClient";
	private static final String URL_SERVER_DELETE_ALL = "http://vedils.uca.es:8080/VedilsWS/GcmServer/registrationClient/deleteAllRegistrationClients";
	
//	//local UCA
//	private static final String URL_SERVER_INSERT = "http://10.162.158.24:8080/VedilsWS/GcmServer/registrationClient/insertRegistrationClient"; 
//	private static final String URL_SERVER_SEND_MESSAGE = "http://10.162.158.24:8080/VedilsWS/GcmServer/sendData/sendTextMessage";
//	private static final String URL_SERVER_DELETE = "http://10.162.158.24:8080/VedilsWS/GcmServer/registrationClient/deleteRegistrationClient";
//	private static final String URL_SERVER_DELETE_ALL = "http://10.162.158.24:8080/VedilsWS/GcmServer/registrationClient/deleteAllRegistrationClients";	

	//local CASA
//	private static final String URL_SERVER_INSERT = "http://192.168.1.39:8080/VedilsWS/GcmServer/registrationClient/insertRegistrationClient"; 
//	private static final String URL_SERVER_SEND_MESSAGE = "http://192.168.1.39:8080/VedilsWS/GcmServer/sendData/sendTextMessage";
//	private static final String URL_SERVER_DELETE = "http://192.168.1.39:8080/VedilsWS/GcmServer/registrationClient/deleteRegistrationClient";
//	private static final String URL_SERVER_DELETE_ALL = "http://192.168.1.39:8080/VedilsWS/GcmServer/registrationClient/deleteAllRegistrationClients";	
	
	
	
	public GoogleCloudMessagingConnectionServer(Context context) {
		this.context = context;
		APP_NAME = context.getApplicationInfo().packageName;
		//Get the IMEI code of the device to get the registration token.
		IMEI = DeviceInfoFunctions.getIMEI(this.context);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void register() {
		//All GCM Components (in the same application) will be registered on the server with the same token.
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... arg0) {
				InstanceID instanceID = InstanceID.getInstance(context);
				System.out.println("Sender ID = "+ SENDER_ID + " - GCMTest");
				try {
			        token = instanceID.getToken(SENDER_ID,
			        		com.google.android.gms.gcm.GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
			        
			        System.out.println("Register Token = " + token + " - GCMTest");
			        System.out.println("Imei = " + IMEI + " - GCMTest");
					JSONObject information = new JSONObject();
					
					information.put("imei", IMEI);
					information.put("token", token);
					information.put("appname", APP_NAME);
					
					System.out.println("JSONObject creado - GCMTest");
					
					String request = establishCommunicationWithServer(information, URL_SERVER_INSERT);
					  	 
				  	if(!request.equals("ESTABLISHED_CONNECTION")) {
				  		throw new Exception("Error while intent to register client on server: " + request + " - GCMTest");
				  	}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Some error - GCMTest" + e.getMessage());
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Object result) {}
		}.execute();
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void unRegister() {
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... arg0) {
				try {
					
					System.out.println("Register Token = " + token + " - GCMTest");
			        System.out.println("Imei = " + IMEI + " - GCMTest");
					JSONObject information = new JSONObject();
					
					information.put("imei", IMEI);
					information.put("token", token);
					information.put("appname", APP_NAME);
					
					System.out.println("JSONObject creado - GCMTest");
					
					String request = establishCommunicationWithServer(information, URL_SERVER_DELETE);
					  	 
				  	if(!request.equals("ESTABLISHED_CONNECTION")) {
				  		throw new Exception("Error while intent to unregister client on server: " + request + " - GCMTest");
				  	}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Some error - GCMTest" + e.getMessage());
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Object result) {}
		}.execute();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void unRegisterAll() {
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... arg0) {
				try {
					
					System.out.println("Register Token = " + token + " - GCMTest");
			        System.out.println("Imei = " + IMEI + " - GCMTest");
					JSONObject information = new JSONObject();
					
					information.put("imei", IMEI);
					information.put("token", token);
					information.put("appname", APP_NAME);
					
					System.out.println("JSONObject creado - GCMTest");
					
					String request = establishCommunicationWithServer(information, URL_SERVER_DELETE_ALL);
					  	 
				  	if(!request.equals("ESTABLISHED_CONNECTION")) {
				  		throw new Exception("Error while intent to unregister all clients on server: " + request + " - GCMTest");
				  	}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Some error - GCMTest" + e.getMessage());
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Object result) {}
		}.execute();
	}

//	/*************************************************************************************************************** EDSON*/
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public void sendObjectsMessage(final List<Object> objects) {	
//		new AsyncTask() {
//			@Override
//			protected Object doInBackground(Object... arg0) {
//				try {
//					
//					System.out.println("Prepare message - GCMTest");
//					
//					System.out.println("JSONObject creado - GCMTest");
//					
//					ArrayList<Object> listObjects = new ArrayList<Object>(objects);
//					listObjects = removeTypesNoAccepted(listObjects);
//					listObjects = castNumbersToString(listObjects);
//					
//					Type listType = new TypeToken<ArrayList<Object>>(){}.getType();
//					Gson gson = new GsonBuilder()./*registerTypeAdapter(listType, ListObjectsSerializer.class).*/create();
//					
//					String objectsJson = gson.toJson(listObjects, listType);
//					System.out.println("Object String Send to Server = " + objectsJson + " - GCMTest ");
//					
//					JSONObject information = new JSONObject();
//					
//					information.put("message", objectsJson);
//					information.put("action", "ObjectListGCM");
//					information.put("imei", IMEI);
//					information.put("appname", APP_NAME);
//					
//					System.out.println("JSONObject creado - GCMTest");
//					
//					String request = establishCommunicationWithServer(information, URL_SERVER_SEND_MESSAGE);
//				  	 
//				  	if(!request.equals("ESTABLISHED_CONNECTION")) {
//				  		throw new Exception("Error while intent to send message: " + request + " - GCMTest");
//				  	}
//				  	
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					System.out.println("Some error - GCMTest" + e.getMessage());
//					e.printStackTrace();
//				}
//				return null;
//			}
//			
//			@Override
//			protected void onPostExecute(Object result) {}
//		}.execute();
//	}
	
	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public void sendMessage(final String message, final String action) {	
//		new AsyncTask() {
//			@Override
//			protected Object doInBackground(Object... arg0) {
//				try {
//					
//					System.out.println("Prepare message - GCMTest");
//					
//					System.out.println("JSONObject creado - GCMTest");
//					
//					JSONObject information = new JSONObject();
//					
//					information.put("message", message);
//					information.put("action", action);
//					information.put("imei", IMEI);				
//					information.put("appname", APP_NAME);
//					
//					System.out.println("JSONObject creado - GCMTest");
//					
//					String request = establishCommunicationWithServer(information, URL_SERVER_SEND_MESSAGE);
//				  	 
//				  	if(!request.equals("ESTABLISHED_CONNECTION")) {
//				  		throw new Exception("Error while intent to send message: " + request + " - GCMTest");
//				  	}
//				  	
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					System.out.println("Some error - GCMTest" + e.getMessage());
//					e.printStackTrace();
//				}
//				return null;
//			}
//			
//			@Override
//			protected void onPostExecute(Object result) {}
//		}.execute();
//	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void sendMessage(final String message, final String action, final String imei_receiver) {				/*EDSON*/
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... arg0) {
				try {
					
					System.out.println("Prepare message - GCMTest");
					
					System.out.println("JSONObject creado - GCMTest");
					
					JSONObject information = new JSONObject();
					
					information.put("message", message);
					information.put("action", action);
					information.put("imei", IMEI);
					information.put("imei_receiver", imei_receiver);					/*EDSON*/
					information.put("appname", APP_NAME);
					
					System.out.println("JSONObject creado - GCMTest");
					
					String request = establishCommunicationWithServer(information, URL_SERVER_SEND_MESSAGE);
				  	 
				  	if(!request.equals("ESTABLISHED_CONNECTION")) {
				  		throw new Exception("Error while intent to send message: " + request + " - GCMTest");
				  	}
				  	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Some error - GCMTest" + e.getMessage());
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Object result) {}
		}.execute();
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sendObjectsMessage(final List<Object> objects, final String imei_receiver) {					/*EDSON*/
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... arg0) {
				try {
					
					System.out.println("Prepare message - GCMTest");
					
					System.out.println("JSONObject creado - GCMTest");
					
					ArrayList<Object> listObjects = new ArrayList<Object>(objects);
					listObjects = removeTypesNoAccepted(listObjects);
					listObjects = castNumbersToString(listObjects);
					
					Type listType = new TypeToken<ArrayList<Object>>(){}.getType();
					Gson gson = new GsonBuilder()./*registerTypeAdapter(listType, ListObjectsSerializer.class).*/create();
					
					String objectsJson = gson.toJson(listObjects, listType);
					System.out.println("Object String Send to Server = " + objectsJson + " - GCMTest ");
					
					JSONObject information = new JSONObject();
					
					information.put("message", objectsJson);
					information.put("action", "ObjectListGCM");
					information.put("imei", IMEI);
					information.put("imei_receiver", imei_receiver);					/*EDSON*/
					information.put("appname", APP_NAME);
					
					System.out.println("JSONObject creado - GCMTest");
					
					String request = establishCommunicationWithServer(information, URL_SERVER_SEND_MESSAGE);
				  	 
				  	if(!request.equals("ESTABLISHED_CONNECTION")) {
				  		throw new Exception("Error while intent to send message: " + request + " - GCMTest");
				  	}
				  	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Some error - GCMTest" + e.getMessage());
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Object result) {}
		}.execute();
	}
			
	/*********************************************************************************************************************************************EDSON*/
	
	private ArrayList<Object> castNumbersToString(ArrayList<Object> listObjects) {
		ArrayList<Object> listObjectsAux = new ArrayList<Object>();
		
		for(Object object: listObjects) {
			if(object instanceof IntNum) {
				IntNum number = (IntNum) object;
				String numberString = String.valueOf(number.ival);
				listObjectsAux.add(numberString);
			} else {
				listObjectsAux.add(object);
			}
		}
		
		return listObjectsAux;
	}
	
	private ArrayList<Object> removeTypesNoAccepted(ArrayList<Object> listObjects) {
		ArrayList<Object> listObjectsAux = new ArrayList<Object>();
		
		for(Object object: listObjects) {
			if(object instanceof Number || object instanceof String || object instanceof Boolean) {
				listObjectsAux.add(object);
			}
		}
		
		return listObjectsAux;
	}
	
		
	private String establishCommunicationWithServer(JSONObject information, String URL) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(
				URL);
		
			StringEntity input = new StringEntity(information.toString());
			input.setContentType("application/json");
			postRequest.setEntity(input);
		
			HttpResponse response = httpClient.execute(postRequest);
			  	 
			System.out.println("Open connection - GCMTest");
			
			System.out.println("Reponse code " + response.getStatusLine().getStatusCode() +"  - GCMTest");
			
			String respuesta = "";
			
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				
				System.out.println("OS close - GCMTest");
				
		        StringBuilder sb = new StringBuilder();
		
		        BufferedReader br = new BufferedReader(
		                        new InputStreamReader((response.getEntity().getContent())));
		        String line = "";
		        while ((line = br.readLine()) != null) {
		            sb.append(line);
		        }
		        br.close();
		        respuesta = sb.toString();
		        System.out.println("Output WS:" + sb.toString() + " - GCMTest");
			}
			
			return respuesta;
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Some error - GCMTest" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	/*class ListObjectsSerializer implements JsonSerializer<ArrayList<Object>> {

		@Override
		public JsonElement serialize(ArrayList<Object> objects, Type type, JsonSerializationContext context) {
			
			JsonArray jsonArray = new JsonArray();
			
			for(Object object: objects) {
				if(object instanceof String) {
					String value = (String) object;
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("object", value + ":type:" + "String");
					jsonArray.add(jsonObject);
				} else if(object instanceof Number) {
					Integer value = (Integer) object;
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("object", value + ":type:" + "Number");
					jsonArray.add(jsonObject);
				} else if(object instanceof Boolean) {
					Boolean value = (Boolean) object;
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("object", value + ":type:" + "Boolean");
					jsonArray.add(jsonObject);
				} else {
					//Skip types
					System.out.println("Skipping type..");
				}
			}
			return jsonArray;
		}
	 }*/
	
}
