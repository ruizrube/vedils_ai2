package com.google.appinventor.server.storage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appinventor.components.runtime.ld4ai.SPARQLClient;
import com.google.appinventor.server.OdeRemoteServiceServlet;
import com.google.appinventor.server.storage.StoredData.TAGExcluir;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet to export WIKIDA information.
 *
 *
 */
public class ExportWikiData<T> extends OdeRemoteServiceServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson;

	static Map<String, List<List<String>>> propertiesCache = new HashMap<String, List<List<String>>>();
	static Map<String, List<List<String>>> classifiersCache = new HashMap<String, List<List<String>>>();

	@SuppressWarnings("unchecked")
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = new PrintWriter(resp.getWriter());

		gson = new GsonBuilder().setExclusionStrategies(new MyExclusionStrategy(String.class)).serializeNulls()
				.create();

		String action = req.getParameter("action");
		String semanticType = req.getParameter("semanticType");
		String preferredLanguage = req.getParameter("preferredLanguage");
		String secondLanguage = req.getParameter("secondLanguage");
		System.out.println("PETICION: " + action + "--" + semanticType);

		resp.setContentType("application/json; charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		
		if (action.equals("getProperties")) {
			out.println(gson.toJson(getDataProperties(semanticType,preferredLanguage,secondLanguage)));
			
		} else if (action.equals("getClassifiers")) {
			out.println(gson.toJson(getClassifiers(semanticType,preferredLanguage,secondLanguage)));
		}
	}

	private List<List<String>> getClassifiers(String semanticType, String preferredLanguage, String secondLanguage) {
		List<List<String>> data;

		if (classifiersCache.containsKey(semanticType)) {
			data = classifiersCache.get(semanticType);
			System.out.println("Recuperado clasificadores desde cache...");

		} else {
			System.out.println("Recuperando clasificadores desde WikiData");
			data = SPARQLClient.getInstance().selectSubclasses(semanticType, preferredLanguage, secondLanguage);
			classifiersCache.put(semanticType, data);
		}

		return data;

	}

	public static List<List<String>> getDataProperties(String semanticType, String preferredLanguage, String secondLanguage) { // "Q35120"
		List<List<String>> data;

		if (propertiesCache.containsKey(semanticType)) {
			data = propertiesCache.get(semanticType);
			System.out.println("Recuperado propiedades desde cache...");

		} else {
			System.out.println("Recuperando propiedades desde WikiData");
			data = SPARQLClient.getInstance().selectProperties(semanticType, preferredLanguage, secondLanguage);
			propertiesCache.put(semanticType, data);
		}

		return data;

		// [[ "head of government", "P6"],
		// [ "population", "P1082"],
		// ];

	}

	private static String prepareData(List<String[]> data) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");

		for (int i = 0; i < data.size(); i++) {
			String[] pair = data.get(i);
			sb.append("[\"").append(pair[1]).append("\",\" ").append(pair[0]).append("\"]");
			sb.append("[\"").append(pair[1]).append("\",\" ").append(pair[0]).append("\"]");
			if (i < data.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");

		System.out.println("GENERANDO: " + sb.toString());
		return sb.toString();

	}

	public class MyExclusionStrategy implements ExclusionStrategy {
		private final Class<?> typeToSkip;

		public MyExclusionStrategy(Class<?> typeToSkip) {
			this.typeToSkip = typeToSkip;
		}

		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}

		public boolean shouldSkipField(FieldAttributes f) {
			return f.getAnnotation(TAGExcluir.class) != null;
		}
	}

}
