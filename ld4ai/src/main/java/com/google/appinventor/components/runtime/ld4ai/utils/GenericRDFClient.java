package com.google.appinventor.components.runtime.ld4ai.utils;

import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class GenericRDFClient {
	
	static GenericRDFClient instance;
	
	private GenericRDFClient() {}
	
	public static GenericRDFClient getInstance() {
		if(instance == null) {
			instance = new GenericRDFClient();
		} 
		return instance;
	}
	
	@SuppressWarnings("resource")
	public JsonArray LoadResource(String identifier, String endpoint, String prefixes) {
		if(identifier != null && !identifier.isEmpty()) {
			HttpURLConnection connection;
			InputStream resultStream = null;
			String query = "SELECT DISTINCT ?property ?hasValue " +
				"WHERE { " +
					"{ <" + identifier + "> ?property ?hasValue } " +
					"UNION " +
					"{ ?hasValue ?property <"+ identifier + "> } " +
				"} " +
				"ORDER BY (!BOUND(?hasValue)) ?property ?hasValue";	
			
			String queryString = "";
			try {
				queryString = endpoint + "?query=" +  URLEncoder.encode(prefixes + query, "UTF-8") + "&format=json";
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			
			try {
				URL url = new URL(queryString);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(60000); // 60 secs
				connection.setReadTimeout(60000); // 60 secs
				connection.setRequestMethod("GET");
				resultStream = connection.getInputStream();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			String inputStreamString = new Scanner(resultStream, "UTF-8").useDelimiter("\\A").next();
			
			System.out.println("Request: " + inputStreamString);
			
			JsonReader jsonReader = Json.createReader(new StringReader(inputStreamString));
			JsonObject result = jsonReader.readObject();
			
			JsonObject resultsObject = result.getJsonObject("results");
			jsonReader = Json.createReader(new StringReader(resultsObject.toString()));
			result = jsonReader.readObject();
			
			JsonArray resultArray = result.getJsonArray("bindings");
			String stringJsonArray = resultArray.toString();
			jsonReader = Json.createReader(new StringReader(stringJsonArray));
			resultArray = jsonReader.readArray();
			jsonReader.close();
			
			System.out.println("jsonArray = " + resultArray.toString());
			
			return resultArray;
		}
		
		return null;
	}

}
