// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.editor.youngandroid.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.appinventor.client.editor.simple.SimpleComponentDatabase;
import com.google.appinventor.client.editor.simple.components.MockComponent;
import com.google.appinventor.client.editor.simple.components.MockForm;
import com.google.appinventor.client.editor.youngandroid.YaBlocksEditor;
import com.google.appinventor.client.editor.youngandroid.YaFormEditor;
import com.google.appinventor.client.editor.youngandroid.YaProjectEditor;
import com.google.appinventor.client.output.OdeLog;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Property editor for selecting methods name for Data.
 * By SPI-FM.
 */
public final class YoungAndroidCheckableTreeSelectorForData extends YoungAndroidCheckableTreeSelector /*implements FormChangeListener*/ {
	
	private final SimpleComponentDatabase COMPONENT_DATABASE;
	private List<YaFormEditor> editors;
	private final YaProjectEditor projectEditor;
	private final TreeItem parentNode;
	private final TreeItem interactionDataDynamic;
	private final TreeItem userSpecificDataDynamic;
	private List<String> userParametersAdded;
	
	//Constants definition
	private final String notifyWithDataOP = "<mutation component_type=\"ActivityTracker\" method_name=\"NotifyWithData\"";
	private final String notifyWithThreeArgumentsOP = "<mutation component_type=\"ActivityTracker\" method_name=\"NotifyWithThreeArguments\"";
	private final String notifyWithTwoArgumentsOP = "<mutation component_type=\"ActivityTracker\" method_name=\"NotifyWithTwoArguments\"";
	private final String notifyWithOneArgumentOP = "<mutation component_type=\"ActivityTracker\" method_name=\"NotifyWithOneArgument\"";
	private final String endComponentBlock = "method_name=";
	private final String firstParameter = "<value name=\"ADD0\">";
	private final String nameParameter = "<field name=\"TEXT\">";
	private final String extensionsOP = "<mutation component_type=\"ActivityDescription\" method_name=\"Extensions\"";
	private final String resultExtensionsOP = "<mutation component_type=\"ActivityDescription\" method_name=\"ResultExtensions\"";
	private final String lastParameter = "<value name=\"ARG0\">";
	private final String learningRecordStoreOption = "2";
	
	public YoungAndroidCheckableTreeSelectorForData(final YaFormEditor editor, final SimpleComponentDatabase COMPONENT_DATABASE) {
		this.COMPONENT_DATABASE = COMPONENT_DATABASE;
		this.projectEditor = (YaProjectEditor) editor.getProjectEditor();
		this.recordedItems = "";
		this.userParametersAdded = new ArrayList<String>();
		
		ScrollPanel treePanel = new ScrollPanel();
		treePanel.setSize("190px", "290px");
		tree = new CheckableTree();
		tree.setWidth("60px");
		
		parentNode = new CheckableTreeItem("all");
		parentNode.setUserObject("all");
		parentNode.setHTML("<b> all </b>");
		
		addStaticNodes();
		
		interactionDataDynamic = new CheckableTreeItem("Interaction Data");
		interactionDataDynamic.setHTML("<b> Interaction Data </b>");
		interactionDataDynamic.setUserObject("Interaction Data");
		interactionDataDynamic.setState(true, false);
		
		addInteractionDataDynamicNodes();
		
		parentNode.addItem(interactionDataDynamic);
		
		userSpecificDataDynamic = new CheckableTreeItem("User Specific Data");
		userSpecificDataDynamic.setHTML("<b> User Specific Data </b>");
		userSpecificDataDynamic.setUserObject("User Specific Data");
		userSpecificDataDynamic.setState(true, false);
		
		addUserSpecificDataDynamicNodes(editor);
		
		parentNode.addItem(userSpecificDataDynamic);
		
		tree.addItem(parentNode);
		treePanel.add(tree);
	    
	    //Customize widget
	    summary.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  OdeLog.log("Loading data tree...");
	    	  addInteractionDataDynamicNodes();
	    	  addUserSpecificDataDynamicNodes(editor);
	    	  loadSelectedNodes(property.getValue());
	    	  openAdditionalChoiceDialog();
	      }
	    });
	    
	    initAdditionalChoicePanel(treePanel);
	}
	
	//
	// Add static nodes
	//
	
	private void addStaticNodes() {
		/// User info
		addNode(parentNode, Arrays.asList("UserID", "IP", "MAC", "IMEI", "Latitude", "Longitude"),
				Arrays.asList("UserID:Category", "IP:Category", "MAC:Category", "IMEI:Category", "Latitude:Category", "Longitude:Category"),
				"<b> User Info </b>", "User Info");

		//// Context
		addNode(parentNode, Arrays.asList("Date", "AppID", "ScreenID", "ComponentID","ComponentType"), 
				Arrays.asList("Date:Category", "AppID:Category", "ScreenID:Category", "ComponentID:Category","ComponentType:Category"),
				"<b> Context </b>", "Context");
		
		//// Activity
		addNode(parentNode, Arrays.asList("ActionID", "ActionType"), Arrays.asList("ActionID:Category", "ActionType:Category"),
				"<b> Activity </b>", "Activity");
	}
	
	//
	// Add dynamic nodes
	//
	
	////Interaction data
	private void addInteractionDataDynamicNodes() {
		interactionDataDynamic.removeItems(); //Clear subtree
		
		editors = new ArrayList<YaFormEditor>();
		
		if(projectEditor != null) {
			editors = projectEditor.getAllFormEditors();
		}
		
		for(YaFormEditor editor: editors) {  //Get mocks by screen
			Collection<MockComponent> mocks = editor.getComponents().values();
			List<String> componentsTypes = new ArrayList<String>();
			MockForm form = null;
			
			for(MockComponent mock: mocks) {
				if(mock instanceof MockForm) {
					form = (MockForm) mock;
				} else {
					componentsTypes.add(mock.getType());
				}
			}
			
			Collections.sort(componentsTypes);
			Set<String> componentsTypesWithoutRepetitions = new LinkedHashSet<String>(componentsTypes);
			
			if(componentsTypesWithoutRepetitions.contains(ACTIVITYTRACKER_COMPONENT)) { //Skip screens without ActivityTracker
				
				componentsTypesWithoutRepetitions.remove(ACTIVITYTRACKER_COMPONENT);
				componentsTypesWithoutRepetitions.remove(ACTIVITYAGGREGATIONQUERY_COMPONENT);
				componentsTypesWithoutRepetitions.remove(ACTIVITYSIMPLEQUERY_COMPONENT);
				
				String screenName = form.getName();
				
				TreeItem screenNode = new CheckableTreeItem("ScreenID:" + screenName);
				screenNode.setHTML(screenName);
				screenNode.setTitle("ScreenID:" + screenName);
				screenNode.setUserObject("ScreenID:" + screenName);

				for (String componentType : componentsTypesWithoutRepetitions) {
					if(COMPONENT_DATABASE.isComponent(componentType)) {
						addComponentsNodes(COMPONENT_DATABASE, componentType, 
								screenNode, getComponentNamesByType(componentType, mocks), null, editor);			
					}
				}
				
				if(screenNode.getChildCount() != 0) {
					interactionDataDynamic.addItem(screenNode);
				}
			}
		}
	}
	
	////Specific data
	private void addUserSpecificDataDynamicNodes(final YaFormEditor editor) {
		userSpecificDataDynamic.removeItems(); //Clear subtree
		
		//add xAPI data 
		MockComponent currentComponent = editor.selectedComponent;
		String storageMode = "";
		
		if(currentComponent != null) {
			if(currentComponent.hasProperty("StorageMode")) {
				storageMode = currentComponent.getPropertyValue("StorageMode");
			}
			
			OdeLog.log("storageMode = " + storageMode);
			
			if(storageMode.equals(learningRecordStoreOption)) { //Learning Record Store option
				addxAPIParameters();
			}
		}
		
		List<YaBlocksEditor> blocksEditors = new ArrayList<YaBlocksEditor>();
		
		if(projectEditor != null) {
			blocksEditors = projectEditor.getAllBlocksEditors();
		}
		
		userParametersAdded.clear(); //Clear user parameters
		
		for(YaBlocksEditor blocksEditor: blocksEditors) {
			String workspaceData = blocksEditor.getBlocksArea().getBlocksContent();
			
			if(workspaceData.contains(notifyWithDataOP) 
					|| workspaceData.contains(extensionsOP) 
					|| workspaceData.contains(resultExtensionsOP)) {	
				String[] lines = workspaceData.split("\\r?\\n");
				List<String> userColumns = new ArrayList<String>();
				
				String userColumn = "";
				boolean value = false;
				boolean notifyWithDataBlock = false;
				
				for(String line: lines) {
					if(line.contains(notifyWithDataOP) 
							|| line.contains(extensionsOP) 
							|| line.contains(resultExtensionsOP)) {
						notifyWithDataBlock = true;
					} else if(line.contains(endComponentBlock)) {
						notifyWithDataBlock = false;
					}
					
					if(line.contains(firstParameter)) {
						value = true;
					} else if(line.contains(lastParameter)) {
						value = false;
					}
					
					if(line.contains(nameParameter)) {
						userColumn = line.replace(nameParameter, "").replace("</field>", "");
						if(value && notifyWithDataBlock) {
							if(!userColumns.contains(userColumn)) { //Remove repetitions
								userColumns.add(userColumn);
							}
							value = false;
						}
					}
				}
				
				for(String column: userColumns) {
					int index = userColumns.indexOf(column) + 1;
					String processColumn = column.replaceAll("\\s+",""); //Remove white spaces
					TreeItem userNode = new CheckableTreeItem("User:Param:" + processColumn + ":" + index);
					userNode.setHTML(processColumn);
					userNode.setUserObject("User:Param:" + processColumn + ":" + index);
					userNode.setState(true, false);
					this.userSpecificDataDynamic.addItem(userNode);
				}
 			}
			
			if(workspaceData.contains(notifyWithThreeArgumentsOP)) {
				//add param1, param2, param3
				addUserParameters(3);
				
			} else if(workspaceData.contains(notifyWithTwoArgumentsOP)) {
				//add param1, param2
				addUserParameters(2);
				
			} else if(workspaceData.contains(notifyWithOneArgumentOP)) {
				//add param1
				addUserParameters(1);
			}
		}
	}
	
	private void addComponentsNodes(final SimpleComponentDatabase COMPONENT_DATABASE,
			final String componentType, TreeItem screenNode, List<String> componentNames, List<String> aggregators, YaFormEditor editor) {
	  
	 // Components (instance) names
	 for(String componentName: componentNames) {
		MockComponent currentComponent = editor.getComponent(componentName);
		String activitiesToTrack = "";
		
		if(currentComponent != null) {
			if(currentComponent.hasProperty("ActivitiesToTrack")) {
				activitiesToTrack = currentComponent.getPropertyValue("ActivitiesToTrack");
			}
		}
		
		if(!activitiesToTrack.equals("Nothing") && !activitiesToTrack.isEmpty()) {
			TreeItem component = new CheckableTreeItem("ComponentID:" + componentName);
			component.setHTML(componentName);
			component.setTitle("ComponentID:" + componentName);
			component.setUserObject("ComponentID:" + componentName);
			
			// Properties
			addPropertiesNodes(COMPONENT_DATABASE, componentType, componentName, aggregators, component, activitiesToTrack);

			// Functions
			addFunctionsNodes(COMPONENT_DATABASE, componentType, componentName, aggregators, component, false, activitiesToTrack);

			// Events
			addEventsNodes(COMPONENT_DATABASE, componentType, componentName, aggregators, component, false, activitiesToTrack);
			
			screenNode.addItem(component);
		  }
	   }
	}
	
	private List<String> getComponentNamesByType(String type, Collection<MockComponent> components) {
		List<String> componentNames = new ArrayList<String>();
		for(MockComponent mock: components) {
			if(mock.getType().equals(type)) {
				componentNames.add(mock.getName());
			}
		}
		Collections.sort(componentNames);
		return componentNames;
	}
	
	private void addxAPIParameters() {
		TreeItem userNode = new CheckableTreeItem("User:Param:" + "success" + ":" + 0);
		userNode.setHTML("success");
		userNode.setUserObject("User:Param:" + "success" + ":" + 0);
		userNode.setState(true, false);
		this.userSpecificDataDynamic.addItem(userNode);
		
		userNode = new CheckableTreeItem("User:Param:" + "scaled" + ":" + 1);
		userNode.setHTML("scaled");
		userNode.setUserObject("User:Param:" + "scaled" + ":" + 1);
		userNode.setState(true, false);
		
		this.userSpecificDataDynamic.addItem(userNode);
		userNode = new CheckableTreeItem("User:Param:" + "raw" + ":" + 2);
		userNode.setHTML("raw");
		userNode.setUserObject("User:Param:" + "raw" + ":" + 2);
		userNode.setState(true, false);
		
		this.userSpecificDataDynamic.addItem(userNode);
		userNode = new CheckableTreeItem("User:Param:" + "min" + ":" + 3);
		userNode.setHTML("min");
		userNode.setUserObject("User:Param:" + "min" + ":" + 3);
		userNode.setState(true, false);
		this.userSpecificDataDynamic.addItem(userNode);
		
		userNode = new CheckableTreeItem("User:Param:" + "max" + ":" + 4);
		userNode.setHTML("max");
		userNode.setUserObject("User:Param:" + "max" + ":" + 4);
		userNode.setState(true, false);
		this.userSpecificDataDynamic.addItem(userNode);
		
		userNode = new CheckableTreeItem("User:Param:" + "completion" + ":" + 5);
		userNode.setHTML("completion");
		userNode.setUserObject("User:Param:" + "completion" + ":" + 5);
		userNode.setState(true, false);
		this.userSpecificDataDynamic.addItem(userNode);
	}
	
	private void addUserParameters(int numParameters) {
		for(int i=0; i<numParameters; i++) {
			int index = i + 1;
			if(!userParametersAdded.contains("param" + index)) { //Skip user parameters repetition from different blocks area
				TreeItem userNode = new CheckableTreeItem("User:Param:param" + index + ":" + index);
				userNode.setHTML("param" + index);
				userNode.setUserObject("User:Param:param" + index + ":" + index);
				userNode.setState(true, false);
				userSpecificDataDynamic.addItem(userNode);
				userParametersAdded.add("param" + index);
			}
		}
	}
}