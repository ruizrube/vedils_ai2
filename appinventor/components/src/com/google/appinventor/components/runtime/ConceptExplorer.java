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
import com.google.appinventor.components.runtime.ld4ai.SPARQLClient;
import com.google.appinventor.components.runtime.ld4ai.SPARQLClientForGenericRDF;
import com.google.appinventor.components.runtime.ld4ai.SPARQLClientForWikiData;

import android.os.StrictMode;

/**
 * @author ivanruizrube
 *
 */
@UsesLibraries(libraries = "ld4ai-0.1.jar")
@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "Concept Explorer (by SPI-FM at UCA)", category = ComponentCategory.VEDILSKNOWLEDGE, iconName = "images/knexplorer.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.ACCESS_NETWORK_STATE")

public class ConceptExplorer extends AndroidNonvisibleComponent implements Serializable {

	/////////////////////
	// CLASS ATTRIBUTES //
	/////////////////////

	private SPARQLClient dataProvider = SPARQLClientForWikiData.getInstance();
	
	private final String DBPEDIA_KEYWORD = "dbpedia";
	
	private static final long serialVersionUID = 1L;

	/////////////////
	// CONSTANTS //
	/////////////////

	/////////////////////
	// LOCAL ATTRIBUTES //
	/////////////////////
	
	private String preferredLanguage = "es";

	private String secondLanguage = "en";

	private String classifierID;
	
	private String endpointRDF = "";
	

	/////////////////
	// CONSTRUCTOR //
	/////////////////

	public ConceptExplorer(ComponentContainer container) {
		super(container.$form());

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy);

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
			dataProvider = new SPARQLClientForGenericRDF(this.endpointRDF);
		} else {
			dataProvider = SPARQLClientForWikiData.getInstance();
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
		this.classifierID = type;
	}

	public String Classifier() {
		return this.classifierID;
	}

	/**
	 * @return the PreferredLanguage
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String PreferredLanguage() {
		return this.preferredLanguage;
	}

	/**
	 * @param PreferredLanguage
	 *            the PreferredLanguage to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_TEXT_TO_SPEECH_LANGUAGES, defaultValue = "es")
	@SimpleProperty(description = "Specifies the Preferred Language", userVisible = true)
	public void PreferredLanguage(String preferredLanguage) {
		this.preferredLanguage=preferredLanguage;
	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String SecondLanguage() {
		return this.secondLanguage;
	}

	/**
	 * @param SecondLanguage
	 *            the SecondLanguage to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_TEXT_TO_SPEECH_LANGUAGES, defaultValue = "en")
	@SimpleProperty(description = "Specifies the Second Language", userVisible = true)
	public void SecondLanguage(String secondLanguage) {
		this.secondLanguage=secondLanguage;
	}


	////////////////
	// FUNCTIONS //
	////////////////
		
	@SimpleFunction(description = "Retrieve the list of instances of the given classifier", userVisible = true)
	public List<List<String>> RetrieveInstances(){
		return dataProvider.selectInstances(this.classifierID, preferredLanguage, secondLanguage);
	}
	
	@SimpleFunction(description = "Retrieve the list of instances of the given classifier", userVisible = true)
	public List<List<String>> RetrievePaginatedInstances(int limit, int offset){
		return dataProvider.selectInstances(this.classifierID, preferredLanguage, secondLanguage,limit,offset);
	}

	@SimpleFunction(description = "Retrieve the list of instances of the given classifier", userVisible = true)
	public List<List<String>> RetrieveInstancesByClassifierID(String classifierID){
		return dataProvider.selectInstances(classifierID, preferredLanguage, secondLanguage);
	}
	
	@SimpleFunction(description = "Retrieve the list of instances of the given classifier", userVisible = true)
	public List<List<String>> RetrievePaginatedInstancesByClassifierID(String classifierID, int limit, int offset){
		return dataProvider.selectInstances(classifierID, preferredLanguage, secondLanguage,limit,offset);
	}
	
	@SimpleFunction(description = "Retrieve the list of instances of the given label", userVisible = true)
	public List<List<String>> RetrieveInstancesByLabel(String label){
		return dataProvider.selectInstancesByLabel(label, preferredLanguage, secondLanguage);
	}
	
	@SimpleFunction(description = "Retrieve the list of instances of the given label", userVisible = true)
	public List<List<String>> RetrievePaginatedInstancesByLabel(String label, int limit, int offset){
		return dataProvider.selectInstancesByLabel(classifierID, preferredLanguage, secondLanguage,limit,offset);
	}

	@SimpleFunction(description = "Retrieve the list of properties of the given classifier", userVisible = true)
	public List<List<String>> RetrieveProperties(){
		return dataProvider.selectProperties(this.classifierID, preferredLanguage, secondLanguage);
	}

	@SimpleFunction(description = "Retrieve the list of properties of the given classifier", userVisible = true)
	public List<List<String>> RetrievePropertiesByClassifierID(String classifierID){
		return dataProvider.selectProperties(classifierID, preferredLanguage, secondLanguage);
	}

	@SimpleFunction(description = "Retrieve the list of ancestors of the given classifier", userVisible = true)
	public List<List<String>> RetrieveAncestors(){
		return dataProvider.selectSuperclasses(this.classifierID, preferredLanguage, secondLanguage);
	}
	
	@SimpleFunction(description = "Retrieve the list of ancestors of the given classifier", userVisible = true)
	public List<List<String>> RetrieveAncestorsByClassifierID(String classifierID){
		return dataProvider.selectSuperclasses(classifierID, preferredLanguage, secondLanguage);
	}
	
	@SimpleFunction(description = "Retrieve the list of descendants of the given classifier", userVisible = true)
	public List<List<String>> RetrieveDescendants(){
		return dataProvider.selectSubclasses(this.classifierID, preferredLanguage, secondLanguage);
	}

	@SimpleFunction(description = "Retrieve the list of descendants of the given classifier", userVisible = true)
	public List<List<String>> RetrieveDescendantsByClassifierID(String classifierID){
		return dataProvider.selectSubclasses(classifierID, preferredLanguage, secondLanguage);
	}


	////////////////
	// EVENTS //
	////////////////

}
