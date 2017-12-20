package com.google.appinventor.components.runtime.ld4ai;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.apache.commons.lang3.ArrayUtils;

import com.google.appinventor.components.runtime.ld4ai.utils.GenericRDFClient;

public class ConceptForGenericRDF implements Concept {
	
	private String endpoint;
	private String prefixes = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + "\n" + 
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "\n" + 
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "\n" + 
			"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "\n";
	private String preferredLanguage = "es";
	private String secondLanguage = "en";
	private String identifier;
	private JsonArray data;
	
	private GenericRDFClient dataProvider = GenericRDFClient.getInstance();
	
	public ConceptForGenericRDF(String endpoint) {
		this.endpoint = endpoint;
	}
	
	public void LoadResource() {		
		this.data = dataProvider.LoadResource(this.identifier, this.endpoint, this.prefixes);
	}
	
	public String getEndpoint() {
		return this.endpoint;
	}
	
	public String getPrefixes() {
		return this.prefixes;
	}
	
	public String PreferredLanguage() {
		return this.preferredLanguage;
	}

	public void PreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	public String SecondLanguage() {
		return this.secondLanguage;
	}

	public void SecondLanguage(String secondLanguage) {
		this.secondLanguage = secondLanguage;
	}
	
	public void Identifier(String identifier) {
		
		String identifierDecode = "";
		
		try {
			identifierDecode = URLDecoder.decode(identifier, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		
		
		this.identifier = identifierDecode;
	}

	public String Identifier() {
		return this.identifier;
	}
	

	public String Label() {
		if(this.data != null) {
			for(JsonValue item: data) {
				JsonReader jsonReader = Json.createReader(new StringReader(item.toString()));
				JsonObject itemData = jsonReader.readObject();
				if(itemData.getJsonObject("property").getString("value")
						.equals("http://www.w3.org/2000/01/rdf-schema#label")) { 
					return itemData.getJsonObject("hasValue").getString("value");
				}
			}
		}
		return "";
	}

	public long RevisionId() {
		if(this.data != null) {
			for(JsonValue item: data) {
				JsonReader jsonReader = Json.createReader(new StringReader(item.toString()));
				JsonObject itemData = jsonReader.readObject();
				if(Pattern.compile(Pattern.quote("RevisionId"), 
						Pattern.CASE_INSENSITIVE)
						.matcher(itemData.getJsonObject("property")
								.getString("value")).find()) {
					return Long.parseLong(itemData.getJsonObject("hasValue")
							.getString("value"));
					
				}
					
			}
		}
		return -1;
	}

	public String Description() {
		if(this.data != null) {
			for(JsonValue item: data) {
				JsonReader jsonReader = Json.createReader(new StringReader(item.toString()));
				JsonObject itemData = jsonReader.readObject();
				if(itemData.getJsonObject("property").getString("value")
						.equals("http://www.w3.org/2000/01/rdf-schema#comment")) {
					return itemData.getJsonObject("hasValue")
							.getString("value");
					
				}
					
			}
		}
		return "";
	}

	public String ImageURL() {
		if(this.data != null) {
			for(JsonValue item: data) {
				JsonReader jsonReader = Json.createReader(new StringReader(item.toString()));
				JsonObject itemData = jsonReader.readObject();
				if(Pattern.compile(Pattern.quote("ImageURL"), 
						Pattern.CASE_INSENSITIVE)
						.matcher(itemData.getJsonObject("property")
								.getString("value")).find()) {
					return itemData.getJsonObject("hasValue")
							.getString("value");
					
				}
					
			}
		}
		return "";
	}

	public String Alias() {
		List<String> aliases = new ArrayList<String>();
		
		if(this.data != null) {
			for(JsonValue item: data) {
				JsonReader jsonReader = Json.createReader(new StringReader(item.toString()));
				JsonObject itemData = jsonReader.readObject();
				if(itemData.getJsonObject("property").getString("value")
						.equals("Alias")) {
					aliases.add(itemData.getJsonObject("hasValue").getString("value"));
				}	
			}
			if(!aliases.isEmpty()) {
				return aliases.get(0);
			}
		}
		return "";
	}

	public List<String> RetrieveAliases() {
		List<String> aliases = new ArrayList<String>();
		
		if(this.data != null) {
			for(JsonValue item: data) {
				JsonReader jsonReader = Json.createReader(new StringReader(item.toString()));
				JsonObject itemData = jsonReader.readObject();
				if(itemData.getJsonObject("property").getString("value")
						.equals("Alias")) {
					aliases.add(itemData.getJsonObject("hasValue").getString("value"));
				}	
			}
			if(!aliases.isEmpty()) {
				return aliases;
			}
		}
		return new ArrayList<String>();
	}

	public String RetrieveLinkedConcept(String property) {
		System.out.println("Retrieve Linked Concept property = " + property);
		List<String> linkedConcepts = new ArrayList<String>();
		
		if(this.data != null) {
			for(JsonValue item: data) {
				JsonReader jsonReader = Json.createReader(new StringReader(item.toString()));
				JsonObject itemData = jsonReader.readObject();
				if(itemData.getJsonObject("property").getString("value")
						.equals(property)) {
					linkedConcepts.add(itemData.getJsonObject("hasValue").getString("value"));
				}	
			}
			if(!linkedConcepts.isEmpty()) {
				return linkedConcepts.get(0);
			}
		}
		return "";
	}

	public List<String> RetrieveLinkedConcepts(String property) {
		System.out.println("Retrieve Linked Concepts property = " + property);
		List<String> linkedConcepts = new ArrayList<String>();
		
		if(this.data != null) {
			for(JsonValue item: data) {
				JsonReader jsonReader = Json.createReader(new StringReader(item.toString()));
				JsonObject itemData = jsonReader.readObject();
				if(itemData.getJsonObject("property").getString("value")
						.equals(property)) {
					linkedConcepts.add(itemData.getJsonObject("hasValue").getString("value"));
				}	
			}
			if(!linkedConcepts.isEmpty()) {
				return linkedConcepts;
			}
		}
		return new ArrayList<String>();
	}
	
	public String RetrieveStringValue(String property) {
		List<String> stringValues = new ArrayList<String>();
		
		if(this.data != null) {
			for(JsonValue item: data) {
				JsonReader jsonReader = Json.createReader(new StringReader(item.toString()));
				JsonObject itemData = jsonReader.readObject();
				if(itemData.getJsonObject("property").getString("value")
						.equals(property)) {
					stringValues.add(itemData.getJsonObject("hasValue").getString("value"));
				}	
			}
			if(!stringValues.isEmpty()) {
				return stringValues.get(0);
			}
		}
		return "";
	}

	public List<String> RetrieveStringValues(String property) {
		List<String> stringValues = new ArrayList<String>();
		
		if(this.data != null) {
			for(JsonValue item: data) {
				JsonReader jsonReader = Json.createReader(new StringReader(item.toString()));
				JsonObject itemData = jsonReader.readObject();
				if(itemData.getJsonObject("property").getString("value")
						.equals(property)) {
					stringValues.add(itemData.getJsonObject("hasValue").getString("value"));
				}	
			}
			if(!stringValues.isEmpty()) {
				return stringValues;
			}
		}
		return new ArrayList<String>();
	}
	
	public float RetrieveNumberValue(String property) {
		List<Long> numberValues = new ArrayList<Long>();
		
		if(this.data != null) {
			for(JsonValue item: data) {
				JsonReader jsonReader = Json.createReader(new StringReader(item.toString()));
				JsonObject itemData = jsonReader.readObject();
				if(itemData.getJsonObject("property").getString("value")
						.equals(property)) {
					numberValues.add(Long.parseLong(itemData.getJsonObject("hasValue")
							.getString("value")));
				}	
			}
			if(!numberValues.isEmpty()) {
				return numberValues.get(0);
			}
		}
		return -1;
	}
	
	public float[] RetrieveNumberValues(String property) {
		List<Long> numberValues = new ArrayList<Long>();
		
		if(this.data != null) {
			for(JsonValue item: data) {
				JsonReader jsonReader = Json.createReader(new StringReader(item.toString()));
				JsonObject itemData = jsonReader.readObject();
				if(itemData.getJsonObject("property").getString("value")
						.equals(property)) {
					numberValues.add(Long.parseLong(itemData.getJsonObject("hasValue")
							.getString("value")));
				}	
			}
			if(!numberValues.isEmpty()) {
				return ArrayUtils.toPrimitive(numberValues.toArray(new Float[0]), 0.0F);
			}
		}
		return ArrayUtils.toPrimitive(new ArrayList<Float>().toArray(new Float[0]), 0.0F);
	}

	public String RetrieveLabelProperty(String property) {
		List<String> labelValues = new ArrayList<String>();
		
		if(this.data != null) {
			for(JsonValue item: data) {
				JsonReader jsonReader = Json.createReader(new StringReader(item.toString()));
				JsonObject itemData = jsonReader.readObject();
				if(itemData.getJsonObject("property").getString("value")
						.equals(property)) {
					labelValues.add(itemData.getJsonObject("hasValue").getString("value"));
				}	
			}
			if(!labelValues.isEmpty()) {
				return labelValues.get(0);
			}
		}
		return "";
	}	

	public String ClassifyText(String title) {
		List<String> keys = AvailableProperties();
		for(String key: keys) {
			if(Pattern.compile(Pattern.quote(title), 
					Pattern.CASE_INSENSITIVE)
					.matcher(key).find()) {
				return key;
			}
		}
		return "";
	}

	public List<String> AvailableProperties() {
		List<String> properties = new ArrayList<String>();
		
		if(this.data != null) {
			for(JsonValue item: data) {
				JsonReader jsonReader = Json.createReader(new StringReader(item.toString()));
				JsonObject itemData = jsonReader.readObject();
				if(!properties.contains(itemData.getJsonObject("property").getString("value"))) {
					properties.add(itemData.getJsonObject("property").getString("value"));
				}	
			}
			if(!properties.isEmpty()) {
				return properties;
			}
		}
		return new ArrayList<String>();
	}
	
	@Override
	public String toString() {
		return "Concept [identifier=" + identifier + "]";
	}

}
