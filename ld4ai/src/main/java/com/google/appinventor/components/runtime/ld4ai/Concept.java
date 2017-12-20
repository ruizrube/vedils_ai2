package com.google.appinventor.components.runtime.ld4ai;

import java.util.List;

public interface Concept {
	public String PreferredLanguage();
	public void PreferredLanguage(String preferredLanguage);
	public String SecondLanguage();
	public void SecondLanguage(String secondLanguage);
	public String Label();
	public long RevisionId();
	public String Description();
	public void Identifier(String id);
	public String Identifier();
	public String ImageURL();
	public String Alias();
	public List<String> RetrieveAliases();
	public String RetrieveLinkedConcept(String property);
	public List<String> RetrieveLinkedConcepts(String property);
	public List<String> RetrieveStringValues(String property);
	public String RetrieveLabelProperty(String property);
	public String RetrieveStringValue(String property);
	public float[] RetrieveNumberValues(String property);
	public float RetrieveNumberValue(String property);
	public String ClassifyText(String title);
	public void LoadResource();
	public List<String> AvailableProperties();
}
