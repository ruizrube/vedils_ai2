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
 * Property editor for selecting methods name for Aggregated Data.
 * By SPI-FM.
 */
public final class YoungAndroidCheckableTreeSelectorForAggregatedData extends YoungAndroidCheckableTreeSelector {

	private final SimpleComponentDatabase COMPONENT_DATABASE;
	private List<YaFormEditor> editors;
	private final YaProjectEditor projectEditor;
	private final TreeItem parentNode;
	private final TreeItem interactionDataDynamic;
	private final YaFormEditor currentEditor;
	private final TreeItem userSpecificDataDynamic;
	private final List<String> aggregators;
	private List<String> userParametersAdded;
	
	//Constants definition
	private final String notifyWithDataOP = "<mutation component_type=\"ActivityTracker\" method_name=\"NotifyWithData\"";
	private final String notifyWithThreeArgumentsOP = "<mutation component_type=\"ActivityTracker\" method_name=\"NotifyWithThreeArguments\"";
	private final String notifyWithTwoArgumentsOP = "<mutation component_type=\"ActivityTracker\" method_name=\"NotifyWithTwoArguments\"";
	private final String notifyWithOneArgumentOP = "<mutation component_type=\"ActivityTracker\" method_name=\"NotifyWithOneArgument\"";
	private final String endComponentBlock = "<mutation component_type=";
	private final String firstParameter = "<value name=\"ADD0\">";
	private final String nameParameter = "<field name=\"TEXT\">";
	
	public YoungAndroidCheckableTreeSelectorForAggregatedData(final YaFormEditor editor, final SimpleComponentDatabase COMPONENT_DATABASE) {
		
		this.currentEditor = editor;
		this.COMPONENT_DATABASE = COMPONENT_DATABASE;
		this.projectEditor = (YaProjectEditor) editor.getProjectEditor();
		this.recordedItems = "";
		this.aggregators = Arrays.asList("Count", "Maximum", "Minimum", "Sum", "Average");
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
		
		addUserSpecificDataDynamicNodes();
		
		parentNode.addItem(userSpecificDataDynamic);
		
		tree.addItem(parentNode);
		treePanel.add(tree);
		
		//Customize widget
	    summary.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  OdeLog.log("Loading data tree...");
	    	  addInteractionDataDynamicNodes();
	    	  addUserSpecificDataDynamicNodes();
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
		// Activity
		addNode(parentNode, Arrays.asList("Count"), Arrays.asList("Count()"),
				"<b> Activity </b>", "Activity");
		
		// Date
		addNode(parentNode, Arrays.asList("Maximum", "Minimum"), 
				Arrays.asList("Maximum(Date)", "Minimum(Date)"), "<b> Date </b>", "Date");
	}
	
	//
	// Add dynamic nodes
	//
	
	////Interaction Data
	private void addInteractionDataDynamicNodes() {
		interactionDataDynamic.removeItems(); //Clear subtree
		
		editors = new ArrayList<YaFormEditor>();
		
		if(projectEditor != null) {
			editors = projectEditor.getAllFormEditors();
		}
		
		Set<String> componentsTypesWithoutRepetitions = new LinkedHashSet<String>();
		
		for(YaFormEditor editor: editors) {  //Get mocks by screen
			Collection<MockComponent> mocks = editor.getComponents().values();
			List<String> componentsTypes = new ArrayList<String>();
			
			for(MockComponent mock: mocks) {
				if(!(mock instanceof MockForm)) {
					componentsTypes.add(mock.getType());
				}
			}
			
			Collections.sort(componentsTypes);
			Set<String> componentsTypesWithoutRepetitionsForEditor = new LinkedHashSet<String>(componentsTypes);
			
			if(componentsTypesWithoutRepetitionsForEditor.contains(ACTIVITYTRACKER_COMPONENT)) { //Only the screens with ActivityTracker
				componentsTypesWithoutRepetitionsForEditor.remove(ACTIVITYTRACKER_COMPONENT);
				componentsTypesWithoutRepetitionsForEditor.remove(ACTIVITYAGGREGATIONQUERY_COMPONENT);
				componentsTypesWithoutRepetitionsForEditor.remove(ACTIVITYSIMPLEQUERY_COMPONENT);
				componentsTypesWithoutRepetitions.addAll(componentsTypesWithoutRepetitionsForEditor);
			}
		}

		for (String componentType : componentsTypesWithoutRepetitions) {
			TreeItem componentTypeNode = new CheckableTreeItem("ComponentType:" + componentType);
			componentTypeNode.setHTML(componentType);
			componentTypeNode.setTitle("ComponentType:" + componentType);
			componentTypeNode.setUserObject("ComponentType:" + componentType);
			
			MockComponent currentComponent = currentEditor.selectedComponent;
			String groupBy = "";
			
			if(currentComponent != null && currentComponent.getType().equals(ACTIVITYAGGREGATIONQUERY_COMPONENT)) {
				if(currentComponent.hasProperty("GroupBy")) {
					groupBy = currentComponent.getPropertyValue("GroupBy");
				}
			}
			
			if(!groupBy.equals("Nothing") && !groupBy.isEmpty()) {
				if(COMPONENT_DATABASE.isComponent(componentType)) {
					addComponentsDataNodes(componentType, componentTypeNode, groupBy);
				}
			}
			
			if(componentTypeNode.getChildCount() != 0) {
				interactionDataDynamic.addItem(componentTypeNode);
			}
		}
	}
	
	////Specific Data
	private void addUserSpecificDataDynamicNodes() {
		userSpecificDataDynamic.removeItems(); //Clear subtree
		
		List<YaBlocksEditor> blocksEditors = new ArrayList<YaBlocksEditor>();
		
		if(projectEditor != null) {
			blocksEditors = projectEditor.getAllBlocksEditors();
		}
		
		userParametersAdded.clear(); //Clear user parameters
		
		for(YaBlocksEditor blocksEditor: blocksEditors) {
			String workspaceData = blocksEditor.getBlocksArea().getBlocksContent();
			
			if(workspaceData.contains(notifyWithDataOP)) {	
				String[] lines = workspaceData.split("\\r?\\n");
				List<String> userColumns = new ArrayList<String>();
				
				String userColumn = "";
				boolean value = false;
				boolean notifyWithDataBlock = false;
				
				for(String line: lines) {
					if(line.contains(notifyWithDataOP)) {
						notifyWithDataBlock = true;
					} else if(line.contains(endComponentBlock)) {
						notifyWithDataBlock = false;
					}
					
					if(line.contains(firstParameter)) {
						value = true;
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
				
				MockComponent currentComponent = currentEditor.selectedComponent;
				String groupBy = "";
				
				if(currentComponent != null) {
					groupBy = currentComponent.getPropertyValue("GroupBy");
				}
				
				for(String column: userColumns) {
					String processColumn = column.replaceAll("\\s+",""); //Remove white spaces
					if(groupBy.contains(processColumn)) {
						TreeItem userNode = new CheckableTreeItem(processColumn);
						userNode.setHTML(processColumn);
						userNode.setUserObject(processColumn);
						userNode.setState(true, false);
						
						for (String aggregator : aggregators) {
							TreeItem aggregatorNode = new CheckableTreeItem(aggregator);
							aggregatorNode.setHTML(aggregator);
							aggregatorNode.setTitle(aggregator);
							int index = userColumns.indexOf(column) + 1;
							aggregatorNode.setUserObject("User" + ":" + aggregator + "(" + processColumn + ")" +  "" + ":Param:" + index);
							userNode.addItem(aggregatorNode);
						}
						userSpecificDataDynamic.addItem(userNode);
					}
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
	
	private void addComponentsDataNodes(final String componentType, TreeItem componentTypeNode, String groupBy) {

		// Properties
		addPropertiesNodes(COMPONENT_DATABASE, componentType, componentType, aggregators, componentTypeNode, groupBy);

		// Functions
		addFunctionsNodes(COMPONENT_DATABASE, componentType, componentType, aggregators, componentTypeNode, true, groupBy);

		// Events		
		addEventsNodes(COMPONENT_DATABASE, componentType, componentType, aggregators, componentTypeNode, true, groupBy);
	}
	
	private void addUserParameters(int numParameters) {
		MockComponent currentComponent = currentEditor.selectedComponent;
		String groupBy = "";
		
		if(currentComponent != null) {
			if(currentComponent.hasProperty("GroupBy")) {
				groupBy = currentComponent.getPropertyValue("GroupBy");
			}
		}
		
		for(int i=0; i<numParameters; i++) {
			int index = i+1;
			if(groupBy.contains("param" + index) && !userParametersAdded.contains("param" + index)) {
				TreeItem userNode = new CheckableTreeItem("User:Param:param" + index);
				userNode.setHTML("param" + index);
				userNode.setUserObject("User:Param:param" + index);
				userNode.setState(true, false);
				
				for (String aggregator : aggregators) {
					TreeItem aggregatorNode = new CheckableTreeItem(aggregator);
					aggregatorNode.setHTML(aggregator);
					aggregatorNode.setTitle(aggregator);
					aggregatorNode.setUserObject("User" + ":" + aggregator + "(" + "param" + index + ")" +  "" + ":Param:" + index);
					userNode.addItem(aggregatorNode);
					userNode.addItem(aggregatorNode);
				}
				userSpecificDataDynamic.addItem(userNode);
				userParametersAdded.add("param" + index);
			}
		}
	}
}