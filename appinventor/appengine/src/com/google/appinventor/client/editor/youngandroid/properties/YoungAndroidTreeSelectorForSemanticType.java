// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.editor.youngandroid.properties;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appinventor.client.properties.json.ClientJsonParser;
import com.google.appinventor.client.widgets.properties.AdditionalChoicePropertyEditor;
import com.google.appinventor.shared.properties.json.JSONArray;
import com.google.appinventor.shared.properties.json.JSONParser;
import com.google.appinventor.shared.properties.json.JSONValue;
import com.google.appinventor.shared.rpc.ServerLayout;
import com.google.gwt.core.client.GWT;
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

	
	private static String ENTITY_ID="Q35120";
	
	private static final JSONParser JSON_PARSER = new ClientJsonParser();

	public YoungAndroidTreeSelectorForSemanticType() {
		ScrollPanel selectorPanel = new ScrollPanel();
		selectorPanel.setSize("190px", "290px");

		tree = new Tree();
		tree.setWidth("60px");

		TreeItem mainTree = new TreeItem();
		mainTree.setHTML("<b> Entity </b>");
		mainTree.setState(true, true);
		mainTree.setUserObject(ENTITY_ID);

		tree.addItem(mainTree);

		tree.addSelectionHandler(new MyHandler());

		tree.addOpenHandler(new MyOpenHandler());

		// Cargamos el primer nivel
		retrieveChildren(mainTree);
		
		
		selectorPanel.add(tree);

		this.recordedItems = "";

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
		return true;
	}

	private void retrieveChildren(TreeItem treeItem) {
		
		String url = GWT.getHostPageBaseURL() + ServerLayout.EXPORTWIKIDATA_SERVLET_BASE + "?action=getClassifiers&preferredLanguage=en&secondLanguage=es&semanticType="
				+ treeItem.getUserObject().toString();
		String serverResponse = makeSyncAjaxCall(url, "", "GET");
		TreeItem itemw;

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
}
