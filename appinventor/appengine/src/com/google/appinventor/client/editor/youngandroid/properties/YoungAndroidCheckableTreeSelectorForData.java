// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.editor.youngandroid.properties;

import java.util.Arrays;
import java.util.List;

import com.google.appinventor.client.editor.simple.SimpleComponentDatabase;
import com.google.appinventor.client.editor.youngandroid.YaFormEditor;
import com.google.appinventor.client.widgets.properties.AdditionalChoicePropertyEditor;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Property editor for selecting methods name for ActivityTracker.
 *
 */
public final class YoungAndroidCheckableTreeSelectorForData extends AdditionalChoicePropertyEditor {

	private String fieldsToRetrieve;
	private CheckableTree tree;

	public YoungAndroidCheckableTreeSelectorForData(final SimpleComponentDatabase COMPONENT_DATABASE) {
		ScrollPanel selectorPanel = new ScrollPanel();
		selectorPanel.setSize("190px", "290px");

		tree = new CheckableTree();
		tree.setWidth("60px");
		TreeItem all = new CheckableTreeItem("all");
		all.setHTML("<b> all </b>");

		
		/// USER INFO
		List<String> userInfoData = Arrays.asList("UserID", "IP", "MAC", "IMEI", "Latitude", "Longitude");
		TreeItem userInfoTree = new CheckableTreeItem("User Info");
		userInfoTree.setHTML("<b> User Info </b>");
		userInfoTree.setState(true, false);

		for (String aux : userInfoData) {
			TreeItem item = new CheckableTreeItem(aux);
			item.setHTML(aux);
			item.setTitle(aux);
			item.setUserObject(aux);			
			userInfoTree.addItem(item);
		}

		all.addItem(userInfoTree);

		//// CONTEXT DATA
		List<String> contextData = Arrays.asList("Date", "AppID", "ScreenID", "ComponentID",
				"Component Type");

		TreeItem contextTree = new CheckableTreeItem("Context");
		contextTree.setHTML("<b> Context </b>");
		contextTree.setState(true, false);

		for (String aux : contextData) {
			TreeItem item = new CheckableTreeItem(aux);
			item.setHTML(aux);
			item.setTitle(aux);
			item.setUserObject(aux);			
			contextTree.addItem(item);
		}

		all.addItem(contextTree);

		//// ACTIVITY DATA
		List<String> activityData = Arrays.asList("ActionID", "ActionType");

		TreeItem activityTree = new CheckableTreeItem("Activity");
		activityTree.setHTML("<b> Activity </b>");
		activityTree.setState(true, false);

		for (String aux : activityData) {
			TreeItem item = new CheckableTreeItem(aux);
			item.setHTML(aux);
			item.setTitle(aux);
			item.setUserObject(aux);			
			activityTree.addItem(item);
		}

		all.addItem(activityTree);

		//// Specific Data
		List<String> specificData = Arrays.asList("InputParam1", "InputParam2", "InputParam3", "OutputParam");

		TreeItem specificTree = new CheckableTreeItem("Specific Data");
		specificTree.setHTML("<b> Specific Data </b>");
		specificTree.setState(true, false);

		for (String aux : specificData) {
			TreeItem item = new CheckableTreeItem(aux);
			item.setHTML(aux);
			item.setTitle(aux);
			item.setUserObject("Text"+aux);  			
			specificTree.addItem(item);
		}

		all.addItem(specificTree);

		tree.addItem(all);
		selectorPanel.add(tree);

		this.fieldsToRetrieve = "";
		initAdditionalChoicePanel(selectorPanel);

		


		//// Interaction Data

//		TreeItem interactionDataTree = new CheckableTreeItem("Interaction Data");
//		interactionDataTree.setHTML("<b> Interaction Data </b>");
//		interactionDataTree.setState(true, false);
//
//		for (String componentType : COMPONENT_DATABASE.getComponentNames()) {
//
//			TreeItem item = new CheckableTreeItem(componentType);
//			item.setHTML(componentType);
//			item.setTitle(componentType);
//			makeTreeForComponentType(COMPONENT_DATABASE, componentType, item);
//
//			interactionDataTree.addItem(item);
//
//		}
		//		all.addItem(interactionDataTree);


	}

	@Override
	protected void openAdditionalChoiceDialog() {
	//	property.setValue("Nothing");
		super.openAdditionalChoiceDialog();
		tree.setFocus(true);
	}

	@Override
	protected String getPropertyValueSummary() {
		return property.getValue();
	}

	private void recordSelectedItems() {
		if (!this.fieldsToRetrieve.equals("")) {
			this.fieldsToRetrieve = "";
		}

		for (int i = 0; i < tree.getItem(0).getChildCount(); i++) // Child nodes
																	// of "All".
		{
			CheckableTreeItem item = (CheckableTreeItem) tree.getItem(0).getChild(i);

			if (item.getStatus() == CheckableTreeItem.SELECTION_ALL
					|| item.getStatus() == CheckableTreeItem.SELECTION_SOME) {

				// Search the child nodes
				for (int j = 0; j < item.getChildCount(); j++) {

					CheckableTreeItem itemLeaf = (CheckableTreeItem) item.getChild(j);

					if (itemLeaf.getStatus() == CheckableTreeItem.SELECTION_ALL) { // Are
																					// leaf
						this.fieldsToRetrieve = this.fieldsToRetrieve + itemLeaf.getUserObject() + " - ";
					}

				}

			}
		}
	}

	@Override
	protected boolean okAction() {
		// When option "OK" is clicked the elements of tree are recorded.
		recordSelectedItems();
		if (this.fieldsToRetrieve.equals("")) {
			property.setValue("Nothing");
			Window.alert("Nothing selected.");
			return true;
		}
		property.setValue("Record elements: " + this.fieldsToRetrieve);
		// Window.alert("Component name: " +componentName + " Options selected:
		// "+this.methodsSelectedNames);
		return true;
	}
}