/**
 * 
 */
package com.google.appinventor.components.runtime.ld4ai;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * @author ruizrube
 *
 */
public class SPARQLClient {

	private static SPARQLClient instance;

	public static SPARQLClient getInstance() {
		if (instance == null) {
			instance = new SPARQLClient();
		}
		return instance;
	}

	public List<List<String>> selectProperties(String conceptURI, String preferredLanguage, String secondLanguage) {
		List<List<String>> result = null;
		String specificQuery = "SELECT ?class ?classname ?item ?itemLabel WHERE {" + "?tree0 (wdt:P279)* ?class ."
				+ "BIND (wd:" + conceptURI + " AS ?tree0)" + "?class wdt:P1963 ?item ." + "SERVICE wikibase:label {"
				+ "  bd:serviceParam wikibase:language \"" + preferredLanguage + "," + secondLanguage + "\" ."
				+ "  ?class rdfs:label ?classname ." + "  ?item rdfs:label ?itemLabel ." + "}" + " } ORDER BY ASC(fn:lower-case(?itemLabel))";

		InputStream response = runSparqlQuery(specificQuery);

		if (response != null) {
			result = dispatchQuery(response);

		}

		return result;
	}

	public List<List<String>> selectSuperclasses(String conceptURI, String preferredLanguage, String secondLanguage) {
		List<List<String>> result = null;
		String specificQuery = "SELECT ?item ?itemLabel " + "WHERE " + "{ wd:" + conceptURI + " wdt:P279 ?item" + " . "
				+ "SERVICE wikibase:label { bd:serviceParam wikibase:language \"" + preferredLanguage + ","
				+ secondLanguage + "\" } " + "} ORDER BY ASC(fn:lower-case(?itemLabel))";

		InputStream response = runSparqlQuery(specificQuery);

		if (response != null) {
			result = dispatchQuery(response);

		}

		return result;
	}

	public List<List<String>> selectSubclasses(String conceptURI, String preferredLanguage, String secondLanguage) {
		List<List<String>> result = null;
		String specificQuery = "SELECT ?item ?itemLabel " + "WHERE " + "{ " + "?item wdt:P279 wd:" + conceptURI + " . "
				+ "SERVICE wikibase:label { bd:serviceParam wikibase:language \"" + preferredLanguage + ","
				+ secondLanguage + "\" } " + "} ORDER BY ASC(fn:lower-case(?itemLabel))";

		InputStream response = runSparqlQuery(specificQuery);

		if (response != null) {
			result = dispatchQuery(response);

		}

		return result;
	}

	public List<List<String>> selectInstances(String classifierID, String preferredLanguage, String secondLanguage, int limit,
			int offset) {
		List<List<String>> result = null;
		String specificQuery = "SELECT ?item ?itemLabel " + "WHERE" + "{" + "	?item wdt:P31 wd:" + classifierID + "."
				+ "	SERVICE wikibase:label { bd:serviceParam wikibase:language \"" + preferredLanguage + ","
				+ secondLanguage + "\" }" + "} ORDER BY ASC(fn:lower-case(?itemLabel)) LIMIT " + limit + " OFFSET " + offset;
			
		InputStream response = runSparqlQuery(specificQuery);

		if (response != null) {
			result = dispatchQuery(response);

		}

		return result;
	}

	
	public List<List<String>> selectInstances(String classifierID, String preferredLanguage, String secondLanguage) {
		List<List<String>> result = null;
		String specificQuery = "SELECT ?item ?itemLabel " + "WHERE" + "{" + "	?item wdt:P31 wd:" + classifierID + "."
				+ "	SERVICE wikibase:label { bd:serviceParam wikibase:language \"" + preferredLanguage + ","
				+ secondLanguage + "\" }" + "} ORDER BY ASC(fn:lower-case(?itemLabel))";

		InputStream response = runSparqlQuery(specificQuery);

		if (response != null) {
			result = dispatchQuery(response);

		}

		return result;
	}

	public List<List<String>> selectClasses(String conceptURI, String preferredLanguage, String secondLanguage) {
		List<List<String>> result = null;
		String specificQuery = "SELECT ?item ?itemLabel " + "WHERE " + "{ wd:" + conceptURI + " wdt:P279 ?item" + " . "
				+ "SERVICE wikibase:label { bd:serviceParam wikibase:language \"" + preferredLanguage + ","
				+ secondLanguage + "\" } " + "} ORDER BY ASC(fn:lower-case(?itemLabel))";

		InputStream response = runSparqlQuery(specificQuery);

		if (response != null) {
			result = dispatchQuery(response);

		}

		return result;
	}

	/**
	 * Executes a given SPARQL query and returns a stream with the result in
	 * JSON format.
	 *
	 * @param query
	 * @return
	 * @throws IOException
	 */
	public InputStream runSparqlQuery(String query) {
		HttpURLConnection connection;
		InputStream result = null;
		try {
			String endpoint = "https://query.wikidata.org/sparql?";
			String prefixes = "PREFIX wdt: <http://www.wikidata.org/prop/direct/> "
					+ "PREFIX wd: <http://www.wikidata.org/entity/>\n ";

			System.out.println(query);

			String queryString = endpoint + "query=" + URLEncoder.encode(prefixes + query, "UTF-8") + "&format=json";
			URL url = new URL(queryString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			result = connection.getInputStream();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return result;

	}

	public List<List<String>> dispatchQuery(InputStream response) {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String>pair;
		
	
		String inputStreamString = new Scanner(response, "UTF-8").useDelimiter("\\A").next();
		
		JsonReader reader = Json.createReader(new StringReader(inputStreamString));
		JsonObject jsonst = (JsonObject) reader.read();
		JsonArray results = jsonst.getJsonObject("results").getJsonArray("bindings");

		for (int i = 0; i < results.size(); i++) {
			pair = new ArrayList();
			pair.add(results.getJsonObject(i).getJsonObject("itemLabel").getJsonString("value").getString());
			pair.add(results.getJsonObject(i).getJsonObject("item").getJsonString("value").getString()
					.replace("http://www.wikidata.org/entity/", ""));
			result.add(pair);			
		}

		// TODO Auto-generated method stub
		return result;
	}

	
}
