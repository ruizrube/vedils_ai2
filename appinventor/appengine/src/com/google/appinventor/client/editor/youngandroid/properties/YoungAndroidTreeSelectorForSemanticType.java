// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.editor.youngandroid.properties;

import com.google.appinventor.client.editor.simple.components.MockComponent;
import com.google.appinventor.client.editor.youngandroid.YaFormEditor;
import com.google.appinventor.client.output.OdeLog;
import com.google.appinventor.client.properties.json.ClientJsonParser;
import com.google.appinventor.client.widgets.properties.AdditionalChoicePropertyEditor;
import com.google.appinventor.shared.properties.json.JSONArray;
import com.google.appinventor.shared.properties.json.JSONParser;
import com.google.appinventor.shared.properties.json.JSONValue;
import com.google.appinventor.shared.rpc.ServerLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Property editor for selecting data from wikidata. By SPI-FM.
 */
public final class YoungAndroidTreeSelectorForSemanticType extends AdditionalChoicePropertyEditor {

	Tree tree;
	private String recordedItems;
	private final YaFormEditor editor;
	private String RDF_URI;
	TreeItem mainTree;
	
	//private static String ENTITY_ID="Q35120";
	private static String ENTITY_ID="";
	
	private static final JSONParser JSON_PARSER = new ClientJsonParser();

	public YoungAndroidTreeSelectorForSemanticType(final YaFormEditor editor) {
		ScrollPanel selectorPanel = new ScrollPanel();
		selectorPanel.setSize("190px", "290px");

		tree = new Tree();
		tree.setWidth("60px");

		mainTree = new TreeItem();
		mainTree.setHTML("<b> Entity </b>");
		mainTree.setState(true, true);
		mainTree.setUserObject(ENTITY_ID);

		tree.addSelectionHandler(new MyHandler());

		tree.addOpenHandler(new MyOpenHandler());
		
		this.editor = editor;

		// Cargamos el primer nivel
		//retrieveChildren(mainTree);
		
		tree.addItem(mainTree);
		
		selectorPanel.add(tree);

		this.recordedItems = "";
		
		//Customize widget
	    summary.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  MockComponent currentComponent = editor.selectedComponent;
				
				if(currentComponent != null) {
					OdeLog.log("Knowledge componentName = " + currentComponent.getName());
					RDF_URI = currentComponent.getPropertyValue("EndpointRDF");
					
					OdeLog.log("URI = " + RDF_URI);
					
					// Load the first level on first time
					if(mainTree.getChildCount() == 0) {
						retrieveChildren(mainTree);
					}
				}
	      }
	    });

		initAdditionalChoicePanel(selectorPanel);

	}

	private native String makeSyncAjaxCall(String url, String msgText, String conType)/*-{
		var xhReq = new XMLHttpRequest();
		xhReq.open(conType, url, false);
		if(conType == "POST") xhReq.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
		xhReq.send(msgText);
		var serverResponse = xhReq.status + "--" + xhReq.responseText;
		return xhReq.responseText;
		}-*/;
	
	private native String makeASyncAjaxCall(String url, String msgText, String conType)/*-{
		var xhReq = new XMLHttpRequest();
		xhReq.open(conType, url, true);
		if(conType == "POST") xhReq.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
		xhReq.send(msgText);
		var serverResponse = xhReq.status + "--" + xhReq.responseText;
		return xhReq.responseText;
		}-*/;


	public class MyOpenHandler implements OpenHandler<TreeItem> {

		@Override
		public void onOpen(OpenEvent<TreeItem> event) {

			return;

			// if (200 == response.getStatusCode()) {
		}

	}

	public class MyHandler implements SelectionHandler<TreeItem> {

		@Override
		public void onSelection(SelectionEvent<TreeItem> event) {
			//recordedItems = event.getSelectedItem().getText() + " [" + event.getSelectedItem().getUserObject() + "]";
			recordedItems=event.getSelectedItem().getUserObject().toString();
			if (event.getSelectedItem().getChildCount() == 0) {

				retrieveChildren(event.getSelectedItem());
			}
		}

	}
	
	@Override
	protected boolean okAction() {
		// When option "OK" is clicked the elements of tree are recorded.
		if (this.recordedItems.equals("")) {
			//property.setValue("Nothing");
			Window.alert("Nothing selected.");
			return true;
		}
		//property.setValue("Record elements: " + this.recordedItems);
		property.setValue(this.recordedItems);
		String url = GWT.getHostPageBaseURL() + ServerLayout.EXPORTRDFDATA_SERVLET_BASE + "?action=getProperties&preferredLanguage=en&secondLanguage=es&semanticType="
				+ this.recordedItems + "&endpointRDF=" + RDF_URI; 
		OdeLog.log("la URL para la llamada es = " + url);
		makeASyncAjaxCall(url, "", "GET"); //Prepare the properties for this type
		return true;
	}

	private void retrieveChildren(TreeItem treeItem) {
		
		String url = "";
		
		if(RDF_URI.contains("dbpedia") && treeItem.getUserObject().toString().isEmpty()) {
			url = GWT.getHostPageBaseURL() + ServerLayout.EXPORTRDFDATA_SERVLET_BASE + "?action=getClassifiers&preferredLanguage=en&secondLanguage=es&semanticType="
					+ "Q35120" + "&endpointRDF=" + RDF_URI;
		} else {
			url = GWT.getHostPageBaseURL() + ServerLayout.EXPORTRDFDATA_SERVLET_BASE + "?action=getClassifiers&preferredLanguage=en&secondLanguage=es&semanticType="
					+ treeItem.getUserObject().toString() + "&endpointRDF=" + RDF_URI; //+ "&prefixesRDF=" + RDF_prefixes;
		}
		
		OdeLog.log("la URL para la llamada es = " + url);
		String serverResponse = makeSyncAjaxCall(url, "", "GET");
		TreeItem itemw;
		OdeLog.log("la respuesta es = " + serverResponse);

		JSONArray data = JSON_PARSER.parse(serverResponse).asArray();
	
		if (data != null) {
			for (JSONValue aux : data.getElements()) {
	
				itemw = new TreeItem();
				itemw.setHTML(aux.asArray().get(0).asString().getString());
				itemw.setTitle(aux.asArray().get(0).asString().getString());
				itemw.setUserObject(aux.asArray().get(1).asString().getString());
				itemw.setState(true, true);

				treeItem.addItem(itemw);

			}
		}
	}

	@Override
	protected void cancelAction() {
		// TODO Auto-generated method stub
		
	}
}
