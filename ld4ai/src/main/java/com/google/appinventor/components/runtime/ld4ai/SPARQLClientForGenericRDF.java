package com.google.appinventor.components.runtime.ld4ai;

import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class SPARQLClientForGenericRDF implements SPARQLClient {
	
	private String endpoint;
	private static String prefixes = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + "\n" + 
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "\n" + 
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "\n" + 
			"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "\n";
	
	public SPARQLClientForGenericRDF(String endpoint) {
		this.endpoint = endpoint;
	}
	
	public List<List<String>> selectProperties(String conceptURI, String preferredLanguage, String secondLanguage) {
		
		conceptURI = decodeClassifier(conceptURI);
		
		List<List<String>> result = new ArrayList<List<String>>();
		
		String query = "SELECT DISTINCT ?p ?label " +
				"WHERE { " +
				"?s a <" + conceptURI + "> ; " +
				"?p ?o . " +
				" OPTIONAL { ?p rdfs:label ?label } . " +
				" OPTIONAL { FILTER(LANG(?label) = \"\" || "
	  			  + "LANGMATCHES(LANG(?label), \""+ preferredLanguage + "\")"
	  			  + "|| LANGMATCHES(LANG(?label), \""+ secondLanguage + "\")) } " +
				"}";
		
		result = queryEndpoint(query, true);
		 
		System.out.println("selectProperties result = " + result);
		
		return result;
	}
	
	public List<List<String>> selectSuperclasses(String conceptURI, String preferredLanguage, String secondLanguage) {
		
		conceptURI = decodeClassifier(conceptURI);
		
		List<List<String>> result = new ArrayList<List<String>>();
		String query = "SELECT ?p ?label WHERE { " +
		        "<"+ conceptURI +"> rdfs:subClassOf ?p . " +
		        " OPTIONAL { ?p rdfs:label ?label } . " +
		        " OPTIONAL { FILTER(LANG(?label) = \"\" || "
				  + "LANGMATCHES(LANG(?label), \""+ preferredLanguage + "\")"
				  + "|| LANGMATCHES(LANG(?label), \""+ secondLanguage + "\")) } " +
			"} " +
			"ORDER BY ASC(?label)";
		
		result = queryEndpoint(query, true);
		 
		 System.out.println("selectSuperclasses result = " + result);
		
		return result;
	}
	
	public List<List<String>> selectSubclassesEncoded(String conceptURI, String preferredLanguage, String secondLanguage) {
		
		conceptURI = decodeClassifier(conceptURI);
		
		List<List<String>> result = new ArrayList<List<String>>();
		
		String query = "";
		
		if(conceptURI.isEmpty()) {
			query = "SELECT DISTINCT ?p ?label { " +
					"[] a ?p . " + 
					" OPTIONAL { ?p rdfs:label ?label } . " +
					" OPTIONAL { FILTER(LANG(?label) = \"\" || "
		  			  + "LANGMATCHES(LANG(?label), \""+ preferredLanguage + "\")"
		  			  + "|| LANGMATCHES(LANG(?label), \""+ secondLanguage + "\")) } " +
	    		"} " +
	    		"ORDER BY ASC(?label)";
		} else {
			query = "SELECT ?p ?label WHERE { " +
			        "?p rdfs:subClassOf <"+ conceptURI +"> . " +
			        " OPTIONAL { ?p rdfs:label ?label } . " +
			        " OPTIONAL { FILTER(LANG(?label) = \"\" || "
					  + "LANGMATCHES(LANG(?label), \""+ preferredLanguage + "\")"
					  + "|| LANGMATCHES(LANG(?label), \""+ secondLanguage + "\")) } " +
				"} " +
				"ORDER BY ASC(?label)";
				
		}
		
		result = queryEndpoint(query, false);
		 
		 System.out.println("selectSubclasses result = " + result);
		
		return result;
	}
	
	public List<List<String>> selectSubclasses(String conceptURI, String preferredLanguage, String secondLanguage) {
		
		conceptURI = decodeClassifier(conceptURI);
		
		List<List<String>> result = new ArrayList<List<String>>();
		
		String query = "";
		
		if(conceptURI.isEmpty()) {
			query = "SELECT DISTINCT ?p ?label { " +
					"[] a ?p . " + 
					" OPTIONAL { ?p rdfs:label ?label } . " +
					" OPTIONAL { FILTER(LANG(?label) = \"\" || "
		  			  + "LANGMATCHES(LANG(?label), \""+ preferredLanguage + "\")"
		  			  + "|| LANGMATCHES(LANG(?label), \""+ secondLanguage + "\")) } " +
	    		"} " +
	    		"ORDER BY ASC(?label)";
		} else {
			query = "SELECT ?p ?label WHERE { " +
			        "?p rdfs:subClassOf <"+ conceptURI +"> . " +
			        " OPTIONAL { ?p rdfs:label ?label } . " +
			        " OPTIONAL { FILTER(LANG(?label) = \"\" || "
					  + "LANGMATCHES(LANG(?label), \""+ preferredLanguage + "\")"
					  + "|| LANGMATCHES(LANG(?label), \""+ secondLanguage + "\")) } " +
				"} " +
				"ORDER BY ASC(?label)";
				
		}
		
		result = queryEndpoint(query, true);
		 
		 System.out.println("selectSubclasses result = " + result);
		
		return result;
	}
	
	public List<List<String>> selectInstancesByLabel(String label, String preferredLanguage, String secondLanguage, int limit,
			int offset) {
		
		List<List<String>> result = new ArrayList<List<String>>();
		String query = "SELECT DISTINCT ?p ?label " + 
				"WHERE { "  + 
				" ?p rdfs:label ?label . " +
				" FILTER(LANG(?label) = \"\" || "
				  + "LANGMATCHES(LANG(?label), \""+ preferredLanguage + "\")"
				  + "|| LANGMATCHES(LANG(?label), \""+ secondLanguage + "\")) " +
				  " FILTER(REGEX(str(?label), " + "\"" + label + "\"" + ")) " +
				"} " +
				"ORDER BY ASC(?label) " +
				"LIMIT " + limit + " OFFSET " + offset;
		
		result = queryEndpoint(query, true);
		 
		 System.out.println("selectInstances with limit result = " + result);
		
		return result;
	}
	
	public List<List<String>> selectInstances(String classifierID, String preferredLanguage, String secondLanguage, int limit,
			int offset) {
		
		classifierID = decodeClassifier(classifierID);
		
		List<List<String>> result = new ArrayList<List<String>>();
		String query = "SELECT DISTINCT ?p ?label " + 
				"WHERE { "  + 
				"?p rdf:type <" + classifierID + "> . "  +
				" OPTIONAL { ?p rdfs:label ?label } . " +
				" OPTIONAL { FILTER(LANG(?label) = \"\" || "
				  + "LANGMATCHES(LANG(?label), \""+ preferredLanguage + "\")"
				  + "|| LANGMATCHES(LANG(?label), \""+ secondLanguage + "\")) } " +
				"} " +
				"ORDER BY ASC(?label) " +
				"LIMIT " + limit + " OFFSET " + offset;
		
		result = queryEndpoint(query, true);
		 
		 System.out.println("selectInstances with limit result = " + result);
		
		return result;
	}
	
	public List<List<String>> selectInstancesByLabel(String label, String preferredLanguage, String secondLanguage) {
		
		List<List<String>> result = new ArrayList<List<String>>();
		String query = "SELECT DISTINCT ?p ?label " + 
				"WHERE { "  + 
				" ?p rdfs:label ?label . " +
				" FILTER(LANG(?label) = \"\" || "
				  + "LANGMATCHES(LANG(?label), \""+ preferredLanguage + "\")"
				  + "|| LANGMATCHES(LANG(?label), \""+ secondLanguage + "\")) " +
				  " FILTER(REGEX(str(?label), " + "\"" + label + "\"" + ")) " +
				"} " +
				"ORDER BY ASC(?label) ";
		
		result = queryEndpoint(query, true);
		 
		 System.out.println("selectInstances without limit result = " + result);
		
		return result;
	}
	
	public List<List<String>> selectInstances(String classifierID, String preferredLanguage, String secondLanguage) {
		
		classifierID = decodeClassifier(classifierID);
		
		List<List<String>> result = new ArrayList<List<String>>();
		String query = "SELECT DISTINCT ?p ?label " + 
				"WHERE { "  +
				"?p rdf:type <" + classifierID + "> . "  +
				" OPTIONAL { ?p rdfs:label ?label } . " +
				" OPTIONAL { FILTER(LANG(?label) = \"\" || "
				  + "LANGMATCHES(LANG(?label), \""+ preferredLanguage + "\")"
				  + "|| LANGMATCHES(LANG(?label), \""+ secondLanguage + "\")) } " +
				"} " +
				"ORDER BY ASC(?label)";
		
		result = queryEndpoint(query, true);
		 
		 System.out.println("selectInstances without limit result = " + result);
		
		return result;
	}
	
	public List<List<String>> selectClasses(String conceptURI, String preferredLanguage, String secondLanguage) {
		
		conceptURI = decodeClassifier(conceptURI);
		
		List<List<String>> result = new ArrayList<List<String>>();
		
		String query = "SELECT DISTINCT ?p ?label " +
					"WHERE { " +
					"?s a <" + conceptURI + "> ; " +
					"?p ?o . " +
					" OPTIONAL { ?p rdfs:label ?label } . " +
					" OPTIONAL { FILTER(LANG(?label) = \"\" || "
					  + "LANGMATCHES(LANG(?label), \""+ preferredLanguage + "\")"
					  + "|| LANGMATCHES(LANG(?label), \""+ secondLanguage + "\")) } " +
					"} " +
					"ORDER BY ASC(?label)";
		
		result = queryEndpoint(query, false);
		 
		 System.out.println("selectClasses result = " + result);
		
		return result;
	}
	
	public List<List<String>> queryEndpoint(String query, boolean onlyLabels) {
		
		List<List<String>> result = new ArrayList<List<String>>();	
		HttpURLConnection connection;
		InputStream resultStream = null;
		
		try {

			String queryString = endpoint + "?query=" +  URLEncoder.encode(prefixes + query, "UTF-8") + "&format=json";
			
			System.out.println("Query: " + queryString);
			
			URL url = new URL(queryString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(60000); // 60 secs
			connection.setReadTimeout(60000); // 60 secs
			connection.setRequestMethod("GET");
			resultStream = connection.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		if (resultStream != null) {
			result = dispatchQuery(resultStream, onlyLabels);
		}
		 
		 return result;
	}
	
	@SuppressWarnings("resource")
	public List<List<String>> dispatchQuery(InputStream response, boolean onlyLabels) {
		List<List<String>> result = new ArrayList<List<String>>();
		List<String>pair;
	
		String inputStreamString = new Scanner(response, "UTF-8").useDelimiter("\\A").next();
		
		System.out.println("Reponse: " + inputStreamString);
		
		JsonReader reader = Json.createReader(new StringReader(inputStreamString));
		JsonObject jsonst = (JsonObject) reader.read();
		JsonArray results = jsonst.getJsonObject("results").getJsonArray("bindings");

		for (int i = 0; i < results.size(); i++) {
			
			pair = new ArrayList<String>();
			
			if(!onlyLabels) {
				if(results.getJsonObject(i).containsKey("label")) {
					pair.add(results.getJsonObject(i).getJsonObject("label").getJsonString("value").getString());
				} else {
					pair.add(results.getJsonObject(i).getJsonObject("p").getJsonString("value").getString());
				}
				try {
					pair.add(URLEncoder.encode(results.getJsonObject(i).getJsonObject("p").getJsonString("value").getString(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				if(results.getJsonObject(i).containsKey("label")) {
					pair.add(results.getJsonObject(i).getJsonObject("label").getJsonString("value").getString());
					pair.add(results.getJsonObject(i).getJsonObject("p").getJsonString("value").getString());
				} else {
					pair.add(results.getJsonObject(i).getJsonObject("p").getJsonString("value").getString());
					pair.add(results.getJsonObject(i).getJsonObject("p").getJsonString("value").getString());
				}
			}
			
			result.add(pair);
		}

		return result;
	}
	
	//Used here because AppEngine no support URLEncoder package
	private String decodeClassifier(String classifier) {
		String classifierDecode = "";
		
		try {
			classifierDecode = URLDecoder.decode(classifier, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if(!classifierDecode.isEmpty()) {
			return classifierDecode;
		} else {
			return classifier;
		}
	}
	
}
