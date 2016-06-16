package com.google.appinventor.components.runtime.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.google.android.gms.iid.InstanceID;
import com.google.appinventor.components.runtime.la4ai.util.DeviceInfoFunctions;

import android.content.Context;
import android.os.AsyncTask;

public class GoogleCloudMessagingConnectionServer {
	
	private static final String SENDER_ID = "88964608807";
	private String token;
	private Context context;
	private String APP_NAME;
	private boolean registered;
	private String IMEI;
	
	//Casa:
	private static final String URL_SERVER_INSERT = "http://192.168.1.9:8080/VedilsWS/GcmServer/registrationClient/insertRegistrationClient"; 
	private static final String URL_SERVER_SEND_MESSAGE = "http://192.168.1.9:8080/VedilsWS/GcmServer/sendData/sendTextMessage";
	private static final String URL_SERVER_DELETE = "http://192.168.1.9:8080/VedilsWS/GcmServer/registrationClient/deleteRegistrationClient";
	private static final String URL_SERVER_DELETE_ALL = "http://192.168.1.9:8080/VedilsWS/GcmServer/registrationClient/deleteAllRegistrationClients";
	
	//UCA
	//private static final String URL_SERVER_INSERT = "http://10.182.111.83:8080/VedilsWS/GcmServer/registrationClient/insertRegistrationClient"; 
	//private static final String URL_SERVER_SEND_MESSAGE = "http://10.182.111.83:8080/VedilsWS/GcmServer/sendData/sendTextMessage";
	//private static final String URL_SERVER_DELETE = "http://10.182.111.83:8080/VedilsWS/GcmServer/registrationClient/deleteRegistrationClient";
	//private static final String URL_SERVER_DELETE_ALL = "http://10.182.111.83:8080/VedilsWS/GcmServer/registrationClient/deleteAllRegistrationClients";
	
	public GoogleCloudMessagingConnectionServer(Context context) {
		this.context = context;
		APP_NAME = context.getApplicationInfo().packageName;
		registered = false;
		//Get the IMEI code of the device to get the registration token.
		IMEI = DeviceInfoFunctions.getIMEI(this.context);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void register() {
		//All GCM Components (in the same application) will be registered on the server with the same token.
		if(!registered) {
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
						
					  	registered = true;
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
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void unRegister() {
		if(registered) {
			new AsyncTask() {
				@Override
				protected Object doInBackground(Object... arg0) {
					try {
						String request = establishCommunicationWithServer(URL_SERVER_DELETE);
						  	 
					  	if(!request.equals("ESTABLISHED_CONNECTION")) {
					  		throw new Exception("Error while intent to register client on server: " + request + " - GCMTest");
					  	}
						
					  	registered = true;
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
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void unRegisterAll() {
		if(registered) {
			new AsyncTask() {
				@Override
				protected Object doInBackground(Object... arg0) {
					try {
						String request = establishCommunicationWithServer(URL_SERVER_DELETE_ALL);
						  	 
					  	if(!request.equals("ESTABLISHED_CONNECTION")) {
					  		throw new Exception("Error while intent to register client on server: " + request + " - GCMTest");
					  	}
						
					  	registered = true;
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
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void sendMessage(final String message) {
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... arg0) {
				try {
					
					System.out.println("Prepare message - GCMTest");
					
					System.out.println("JSONObject creado - GCMTest");
					
					JSONObject information = new JSONObject();
					
					information.put("message", message);
					information.put("imei", IMEI);
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
	
	private String establishCommunicationWithServer(String URL) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(
				URL);
		
			StringEntity input = new StringEntity(this.IMEI);
			input.setContentType("text/plain");
			postRequest.setEntity(input);
		
			HttpResponse response = httpClient.execute(postRequest);
			  	 
			System.out.println("Conexión abierta - GCMTest");
			
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
	
	private String establishCommunicationWithServer(JSONObject information, String URL) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(
				URL);
		
			StringEntity input = new StringEntity(information.toString());
			input.setContentType("application/json");
			postRequest.setEntity(input);
		
			HttpResponse response = httpClient.execute(postRequest);
			  	 
			System.out.println("Conexión abierta - GCMTest");
			
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
	
	
	
}
