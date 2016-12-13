/**
 * 
 */
package com.google.appinventor.components.runtime;

import java.io.Serializable;

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
import com.google.appinventor.components.runtime.ld4ai.VedilsStatement;

/**
 * @author ivanruizrube
 *
 */
@UsesLibraries(libraries = "ld4ai-0.1.jar")
@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "Semantic Concept Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSKNOWLEDGE, iconName = "images/arCamera.png")
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

	private final ComponentContainer container;

	public Concept data;

	/////////////////
	// CONSTRUCTOR //
	/////////////////

	public SemanticConcept(ComponentContainer container) {
		super(container.$form());
		this.container = container;

		data = new Concept();
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
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_TEXT_TO_SPEECH_LANGUAGES, defaultValue = "False")
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
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_TEXT_TO_SPEECH_LANGUAGES, defaultValue = "False")
	@SimpleProperty(description = "Specifies the Second Language", userVisible = true)
	public void SecondLanguage(String secondLanguage) {
		data.SecondLanguage(secondLanguage);
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
	public String Identifier() {
		return data.Identifier();
	}

	////////////////
	// FUNCTIONS //
	////////////////

	@SimpleFunction(description = "Load a specific concept", userVisible = true)
	public void Load(String text) {

		System.out.println("*** Fetching data for entity... " + text);
		String entityId = data.ClassifyText(text);
		data.Identifier(entityId);
	}

	//@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public VedilsStatement RetrieveClaim(String property) {
		return data.RetrieveClaim(property);
	}

	//@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public VedilsStatement[] RetrieveClaims(String property) {
		return data.RetrieveClaims(property);
	}

	//@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public Concept RetrieveRelatedConcept(String property) {
		return data.RetrieveRelatedConcept(property);
	}

	//@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public Concept[] RetrieveRelatedConcepts(String property) {
		return data.RetrieveRelatedConcepts(property);
	}

	
	//@SimpleFunction(description = "Retrieve string values", userVisible = true)
	public String[] RetrieveStringValues(String property) {
		return data.RetrieveStringValues(property);
	}

	@SimpleFunction(description = "Retrieve a string value", userVisible = true)
	public String RetrieveStringValue(String property) {
		return data.RetrieveStringValue(property);
	}

	//@SimpleFunction(description = "Retrieve numeric values", userVisible = true)
	public float[] RetrieveNumericValues(String property) {
		return data.RetrieveNumberValues(property);
	}

	@SimpleFunction(description = "Retrieve a numeric value", userVisible = true)
	public float RetrieveNumericValue(String property) {
		return data.RetrieveNumberValue(property);
	}

	@SimpleFunction(description = "Explain an specific concept", userVisible = true)
	public String ExplainConcept() {
		return data.ExplainConcept();
	}

	////////////////
	// FUNCTIONS //
	////////////////

	////////////////
	// EVENTS //
	////////////////

}
