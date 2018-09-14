package com.google.appinventor.components.runtime.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.appinventor.components.runtime.Chart;
import com.google.appinventor.components.runtime.DataTable;

import android.os.AsyncTask;
import edu.emory.mathcs.backport.java.util.Arrays;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementResult;

@SuppressWarnings("deprecation")
public class AsyncHttpRequestManager extends AsyncTask<String, Boolean, String> {
	
	private String URL;
	private Object component;
	private JSONObject data;
	private boolean timeout;
	private String method;
	
	public AsyncHttpRequestManager(String URL, String method, JSONObject data, Object component, boolean timeout){
        this.URL = URL;
        this.data = data;
        this.component = component;
        this.timeout = timeout;
        this.method = method;
    }
	
	@Override
	protected String doInBackground(String... args) {
		System.out.println("AsyncHttpRequestManager: Start request on " + URL);
		
		try {
			DefaultHttpClient httpClient;
			
			if(timeout) {
				HttpParams httpParams = new BasicHttpParams();
			    HttpConnectionParams.setConnectionTimeout(httpParams, 1000);
			    HttpConnectionParams.setSoTimeout(httpParams, 1000);
				httpClient = new DefaultHttpClient(httpParams);
				System.out.println("AsyncHttpRequestManager: Adding timeout for request on " + URL);
			} else {
				httpClient = new DefaultHttpClient();
			}
			
			HttpResponse response = null;
			
			if(this.method.equals("GET")) {
				HttpGet getRequest = new HttpGet(URL);
				response = httpClient.execute(getRequest);
			} else {
				HttpPost postRequest = new HttpPost(URL);
				StringEntity input = new StringEntity(data.toString(), "UTF-8");
				input.setContentType("application/json");
				postRequest.setEntity(input);
				response = httpClient.execute(postRequest);
			}
			
			System.out.println("AsyncHttpRequestManager: Reponse code " + response.getStatusLine().getStatusCode() +"  with request on " + URL);
			
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
		        //StringBuilder stringBuilder = new StringBuilder();
				String result = "";
				System.out.println("AsyncHttpRequestManager: headers = " + Arrays.toString(response.getAllHeaders()));
		        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
		        String line = "";
		        while ((line = bufferedReader.readLine()) != null) {
		        	//stringBuilder.append(line).append("\n");
		        	result = result + line + "\n";
		        }
				//result = EntityUtils.toString(response.getEntity());
				System.out.println("AsyncHttpRequestManager: Result size = " + result.length());
		        //bufferedReader.close();
		        System.out.println("AsyncHttpRequestManager: Output WS:" + result + " with request on " + URL);
		        return result;
			} else {
				return null;
			}
		
		} catch(Exception e) {
			System.out.println("AsyncHttpRequestManager: Exception with request on " + URL);
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void onPostExecute(final String result) {
		if(component instanceof ActivityQueryManager) { //Fire event for component ActivityProcessor
			if(component instanceof ActivityQueryManagerStream) {
				final ActivityQueryManagerStream queryManager = (ActivityQueryManagerStream) component;
        		//Dispatch the event
				queryManager.getComponent().componentContainer.$context().runOnUiThread(new Runnable() {
		            @Override
		            public void run() {
		            	try {	
		            		queryManager.getComponent().StreamDataReceived(CsvUtil.fromCsvTable(result));
		            	} catch(Exception e) {
		            		e.printStackTrace();
		            	}
		            }
		        });
			} else {
				final ActivityQueryManager queryManager = (ActivityQueryManager) component;
        		//Dispatch the event
				queryManager.getComponent().componentContainer.$context().runOnUiThread(new Runnable() {
		            @Override
		            public void run() {
		            	try {	
		            		queryManager.getComponent().DataReceived(CsvUtil.fromCsvTable(result));
		            	} catch(Exception e) {
		            		e.printStackTrace();
		            	}
		            }
		        });
			}
		} else if(component instanceof DataTable) { //Refresh automatic query
			final DataTable dataTable = (DataTable) component;
			try {
				dataTable.Data(CsvUtil.fromCsvTable(result));
			} catch (Exception e) {
				System.out.println("AsyncHttpRequestManager: DataTable exception with request on " + URL);
				e.printStackTrace();
			}
    		//Dispatch the event
			dataTable.Query().getQueryManager().getComponent().componentContainer.$context().runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	            	try {	
	            		dataTable.Refresh();
	            	} catch(Exception e) {
	            		e.printStackTrace();
	            	}
	            }
	        });
		} else if(component instanceof Chart) { //Refresh automatic query
			final Chart chart = (Chart) component;
    		try {
				chart.Data(CsvUtil.fromCsvTable(result));
			} catch (Exception e) {
				System.out.println("AsyncHttpRequestManager: Chart exception with request on " + URL);
				e.printStackTrace();
			}
    		//Dispatch the event
			chart.Query().getQueryManager().getComponent().componentContainer.$context().runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	            	try {	
	            		chart.Refresh();
	            	} catch(Exception e) {
	            		e.printStackTrace();
	            	}
	            }
	        });
		}
    }
}