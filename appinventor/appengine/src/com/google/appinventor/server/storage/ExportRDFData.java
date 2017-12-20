package com.google.appinventor.server.storage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appinventor.components.runtime.ld4ai.SPARQLClientForGenericRDF;
import com.google.appinventor.components.runtime.ld4ai.SPARQLClientForWikiData;
import com.google.appinventor.server.OdeRemoteServiceServlet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ExportRDFData<T> extends OdeRemoteServiceServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson;
	private static String DBPEDIA_ENDPOINT = "dbpedia";

	static Map<String, List<List<String>>> propertiesCache = new HashMap<String, List<List<String>>>();
	static Map<String, List<List<String>>> classifiersCache = new HashMap<String, List<List<String>>>();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = new PrintWriter(resp.getWriter());

		gson = new GsonBuilder().setExclusionStrategies().serializeNulls()
				.create();
		
		String action = req.getParameter("action");
		String semanticType = req.getParameter("semanticType");
		String preferredLanguage = req.getParameter("preferredLanguage");
		String secondLanguage = req.getParameter("secondLanguage");
		String endpoint = req.getParameter("endpointRDF");
		System.out.println("PETICION: " + action + "--" + semanticType);
		System.out.println("ENDPOINT: " + endpoint);
		
		resp.setContentType("application/json; charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		
		if (action.equals("getProperties")) {
			out.println(gson.toJson(getDataProperties(semanticType,preferredLanguage,secondLanguage, endpoint)));
			
		} else if (action.equals("getClassifiers")) {
			out.println(gson.toJson(getClassifiers(semanticType,preferredLanguage,secondLanguage, endpoint)));
		}
	}
	
	private List<List<String>> getClassifiers(String semanticType, String preferredLanguage, String secondLanguage, String endpointRDF) {
		List<List<String>> data = new ArrayList<List<String>>();

		if (classifiersCache.containsKey(semanticType)) {
			data = classifiersCache.get(semanticType);
			System.out.println("Recuperando clasificadores desde cache (semanticType)...");
		} else if(classifiersCache.containsKey(endpointRDF)
				&& (semanticType.isEmpty() || semanticType == null)) {
			data = classifiersCache.get(endpointRDF);
			System.out.println("Recuperando clasificadores desde cache (endpointRDF)...");
		} else if(endpointRDF != null && endpointRDF.contains(DBPEDIA_ENDPOINT)) {
			System.out.println("Recuperando clasificadores desde WikiData");
			data = SPARQLClientForWikiData.getInstance().selectSubclasses(semanticType, preferredLanguage, secondLanguage);
			if(!semanticType.isEmpty() && semanticType != null) {
				classifiersCache.put(semanticType, data);
			} else {
				classifiersCache.put(endpointRDF, data);
			}
		} else if(endpointRDF != null) {
			System.out.println("Recuperando clasificadores desde RDF");
			data = new SPARQLClientForGenericRDF(endpointRDF).selectSubclassesEncoded(semanticType, preferredLanguage, secondLanguage);
			if(!semanticType.isEmpty() && semanticType != null) {
				classifiersCache.put(semanticType, data);
			} else {
				classifiersCache.put(endpointRDF, data);
			}
		}

		return data;

	}

	public static List<List<String>> getDataProperties(String semanticType, String preferredLanguage, String secondLanguage, String endpointRDF) { // "Q35120"
		List<List<String>> data = new ArrayList<List<String>>();

		if (propertiesCache.containsKey(semanticType) || endpointRDF == null) {
			data = propertiesCache.get(semanticType);
			System.out.println("Recuperando propiedades desde cache...");
		} else if(endpointRDF != null && endpointRDF.contains(DBPEDIA_ENDPOINT)) {
			System.out.println("Recuperando propiedades desde WikiData");
			data = SPARQLClientForWikiData.getInstance().selectProperties(semanticType, preferredLanguage, secondLanguage);
			propertiesCache.put(semanticType, data);
		} else if(endpointRDF != null) {
			System.out.println("Recuperando propiedades desde RDF");
			data = new SPARQLClientForGenericRDF(endpointRDF).selectProperties(semanticType, preferredLanguage, secondLanguage);
			propertiesCache.put(semanticType, data);
		}

		return data;

		// [[ "head of government", "P6"],
		// [ "population", "P1082"],
		// ];

	}
}
