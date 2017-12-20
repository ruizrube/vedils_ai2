/**
 * 
 */
package com.google.appinventor.components.runtime;

import java.io.Serializable;
import java.util.List;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.ld4ai.Concept;
import com.google.appinventor.components.runtime.ld4ai.ConceptForGenericRDF;
import com.google.appinventor.components.runtime.ld4ai.ConceptForWikiData;

import android.os.StrictMode;

/**
 * @author ivanruizrube
 *
 */
@UsesLibraries(libraries = "ld4ai-0.1.jar")
@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "Semantic Concept Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSKNOWLEDGE, iconName = "images/knsemantic.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.ACCESS_NETWORK_STATE")

public class SemanticConcept extends AndroidNonvisibleComponent implements Serializable {

	/////////////////////
	// CLASS ATTRIBUTES //
	/////////////////////

	private static final long serialVersionUID = 1L;

	/////////////////
	// CONSTANTS //
	/////////////////

	/////////////////////
	// LOCAL ATTRIBUTES //
	/////////////////////

	public Concept data;

	private String conceptURI;
	
	private String endpointRDF = "";
	
	private final String DBPEDIA_KEYWORD = "dbpedia";
	
	//private String prefixes = "";
	

	/////////////////
	// CONSTRUCTOR //
	/////////////////

	public SemanticConcept(ComponentContainer container) {
		super(container.$form());

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy);

		data = new ConceptForWikiData();
	}

	///////////////////////
	// OVERRIDED METHODS //
	///////////////////////

	//////////////////////
	// INTERNAL METHODS //
	//////////////////////

	////////////////
	// PROPERTIES //
	////////////////
	
	
	/**
	 * Specifies the endpoint URL for the RDF query
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(userVisible = false)
	public void EndpointRDF(String endpointRDF) {
		this.endpointRDF = endpointRDF;
		
		if(!this.endpointRDF.contains(DBPEDIA_KEYWORD)) {
			data = new ConceptForGenericRDF(this.endpointRDF);
		} else {
			data = new ConceptForWikiData();
		}
	}

	public String EndpointRDF() {
		return this.endpointRDF;
	}

	/**
	 * Specifies a classifier.
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_TREEFORSEMANTICTYPE, defaultValue = "")
	@SimpleProperty(userVisible = false)
	public void Classifier(String type) {
		this.conceptURI = type;
	}

	public String Classifier() {
		return this.conceptURI;
	}

	
	/**
	 * @return the PreferredLanguage
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String PreferredLanguage() {
		return data.PreferredLanguage();
	}

	/**
	 * @param PreferredLanguage
	 *            the PreferredLanguage to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_TEXT_TO_SPEECH_LANGUAGES, defaultValue = "es")
	@SimpleProperty(description = "Specifies the Preferred Language", userVisible = true)
	public void PreferredLanguage(String preferredLanguage) {
		data.PreferredLanguage(preferredLanguage);
	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String SecondLanguage() {
		return data.SecondLanguage();
	}

	/**
	 * @param SecondLanguage
	 *            the SecondLanguage to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_TEXT_TO_SPEECH_LANGUAGES, defaultValue = "en")
	@SimpleProperty(description = "Specifies the Second Language", userVisible = true)
	public void SecondLanguage(String secondLanguage) {
		data.SecondLanguage(secondLanguage);
	}

	
	/**
	 * @param Identifier
	 *            the Identifier to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(description = "Specifies the Identifier", userVisible = true)
	public void Identifier(String id) {
		data.Identifier(id);
	}

	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String Identifier() {
		return data.Identifier();
	}
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String Label() {
		return data.Label();
	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String Description() {
		return data.Description();
	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String ImageURL() {
		return data.ImageURL();
	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String Alias() {
		return data.Alias();
	}

	
	
	////////////////
	// FUNCTIONS //
	////////////////

	@SimpleFunction(description = "Classify a text", userVisible = true)
	public String ClassifyText(String text) {
		String entityId = data.ClassifyText(text);
		return entityId;
		
	}

	@SimpleFunction(description = "Load a specific concept", userVisible = true)
	public void Load() {
		System.out.println("*** Fetching data for entity... " + this.Identifier());
		this.data.LoadResource();
		
	}

	@SimpleFunction(description = "Retrieve the label of the given property", userVisible = true)
	public String RetrievePropertyLabel(String property) {
		return data.RetrieveLabelProperty(property);
	}

	
	@SimpleFunction(description = "Retrieve the properties available for the current concept", userVisible = true)
	public List<String> AvailableProperties() {
		return data.AvailableProperties();
	}



	
	@SimpleFunction(description = "Retrieve the aliases of the current concept", userVisible = true)
	public List<String> RetrieveAliases() {
		return data.RetrieveAliases();
	}

	
	@SimpleFunction(description = "Retrieve concepts linked with the current one through a specific property", userVisible = true)
	public String RetrieveLinkedConcept(String property) {
		String result = data.RetrieveLinkedConcept(property);
		System.out.println("Retrieve Linked Concept = " + result);
		return result;
	}
	
	@SimpleFunction(description = "Retrieve a concept linked with the current one through a specific property", userVisible = true)
	public List<String> RetrieveLinkedConcepts(String property) {
		List<String> result = data.RetrieveLinkedConcepts(property);
		System.out.println("Retrieve Linked Concepts = " + result);
		return result;
	}

	@SimpleFunction(description = "Retrieve concepts linked with the current one through a specific property", userVisible = true)
	public String RetrieveAssistedLinkedConcept(String input) {
		String result = data.RetrieveLinkedConcept(input.replace("package ", ""));
		System.out.println("Retrieve Linked Concept = " + result);
		return result;
	}

	@SimpleFunction(description = "Retrieve a concept linked with the current one through a specific property", userVisible = true)
	public List<String> RetrieveAssistedLinkedConcepts(String input) {
		List<String> result = data.RetrieveLinkedConcepts(input.replace("package ", ""));
		System.out.println("Retrieve Linked Concepts = " + result);
		return result;
	}
	
	
	@SimpleFunction(description = "Retrieve a set of string values of a given property", userVisible = true)
	public List<String> RetrieveStringValues(String property) {
		return data.RetrieveStringValues(property);
	}

	@SimpleFunction(description = "Retrieve a set of string values of a given property", userVisible = true)
	public List<String> RetrieveAssistedStringValues(String input) {
		return data.RetrieveStringValues(input.replace("package ", ""));
	}

	@SimpleFunction(description = "Retrieve the string value of a given property", userVisible = true)
	public String RetrieveStringValue(String property) {
		return data.RetrieveStringValue(property);
	}

	@SimpleFunction(description = "Retrieve the string value of a given property", userVisible = true)
	public String RetrieveAssistedStringValue(String input) {
		return data.RetrieveStringValue(input.replace("package ", ""));
	}
	
	@SimpleFunction(description = "Retrieve the numeric value of a given property", userVisible = true)
	public float RetrieveNumericValue(String property) {
		return data.RetrieveNumberValue(property);
	}

	@SimpleFunction(description = "Retrieve the numeric value of a given property", userVisible = true)
	public float RetrieveAssistedNumericValue(String input) {
		return data.RetrieveNumberValue((input.replace("package ", "")));
	}

	

	////////////////
	// EVENTS //
	////////////////

}
