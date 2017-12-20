package com.google.appinventor.components.runtime.ld4ai;

import java.util.List;

public interface SPARQLClient {
	public List<List<String>> selectProperties(String conceptURI, String preferredLanguage, String secondLanguage);
	public List<List<String>> selectSuperclasses(String conceptURI, String preferredLanguage, String secondLanguage);
	public List<List<String>> selectSubclasses(String conceptURI, String preferredLanguage, String secondLanguage);
	public List<List<String>> selectInstances(String classifierID, String preferredLanguage, String secondLanguage, int limit,int offset);
	public List<List<String>> selectInstances(String classifierID, String preferredLanguage, String secondLanguage);
	public List<List<String>> selectInstancesByLabel(String classifierID, String preferredLanguage, String secondLanguage, int limit,int offset);
	public List<List<String>> selectInstancesByLabel(String classifierID, String preferredLanguage, String secondLanguage);
	public List<List<String>> selectClasses(String conceptURI, String preferredLanguage, String secondLanguage);
}
