package com.google.appinventor.client.editor.youngandroid.properties;

import java.util.ArrayList;
import java.util.List;

import com.google.appinventor.client.editor.simple.SimpleComponentDatabase;
import com.google.appinventor.client.editor.simple.components.MockComponent;
import com.google.appinventor.client.editor.youngandroid.YaFormEditor;
import com.google.appinventor.client.output.OdeLog;
import com.google.appinventor.shared.simple.ComponentDatabaseInterface.EventDefinition;
import com.google.appinventor.shared.simple.ComponentDatabaseInterface.MethodDefinition;
import com.google.appinventor.shared.simple.ComponentDatabaseInterface.PropertyDefinition;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Property editor for selecting methods name for ActivityTracker.
 * By SPI-FM.
 */
public class YoungAndroidCheckableTreeSelectorForActivityTracker extends YoungAndroidCheckableTreeSelector {
	
	private TreeItem all;
	
	public YoungAndroidCheckableTreeSelectorForActivityTracker(final YaFormEditor editor, final SimpleComponentDatabase COMPONENT_DATABASE, final String componentType) {
		ScrollPanel selectorPanel = new ScrollPanel();
		selectorPanel.setSize("190px", "290px");
		
		tree = new CheckableTree();
		tree.setWidth("60px");
		all = new CheckableTreeItem("all");
		all.setHTML("<b> all </b>");
		all.setUserObject("all");

		addComponentData(COMPONENT_DATABASE, componentType, all, editor);

		tree.addItem(all);
		selectorPanel.add(tree);

		this.recordedItems = "";
		
		//Customize widget
	    summary.addClickHandler(new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	    	  OdeLog.log("Loading data tree...");
	    	  addComponentData(COMPONENT_DATABASE, componentType, all, editor);
	    	  loadSelectedNodes(property.getValue());
	    	  openAdditionalChoiceDialog();
	      }
	    });
		
		initAdditionalChoicePanel(selectorPanel);
	}
	
	private void addComponentData(final SimpleComponentDatabase COMPONENT_DATABASE,
			final String componentType, TreeItem all, final YaFormEditor editor) {
		
		all.removeItems(); //Clear tree
		
		MockComponent currentComponent = editor.selectedComponent;
		
		TreeItem propertiesNode = new CheckableTreeItem("<b>Properties</b>");
		propertiesNode.setHTML("<b>Properties</b>");
		propertiesNode.setUserObject("Properties");
		propertiesNode.setState(true, false);
		
		if(currentComponent != null) {
			for (PropertyDefinition property : COMPONENT_DATABASE.getPropertyDefinitions(componentType)) {
				if (!property.getName().equals("ComponentName") && !property.getName().equals("ActivitiesToTrack")) {
					TreeItem propertyNode = new CheckableTreeItem(property.getName());
					propertyNode.setHTML(property.getName());
					propertyNode.setUserObject("ActionID:" + property.getName() + ":"  + "ComponentType:" + componentType + ":" + currentComponent.getName());
					propertyNode.setState(true, false);
					
					TreeItem setNode = new CheckableTreeItem("Set");
					setNode.setHTML("Set");
					setNode.setUserObject("ActionType:Set:ActionID:" + property.getName() + ":" + "ComponentType:" + componentType + ":" + currentComponent.getName());
					setNode.setState(true, false);
					propertyNode.addItem(setNode);
					
					TreeItem getNode = new CheckableTreeItem("Get");
					getNode.setHTML("Get");
					getNode.setUserObject("ActionType:Get:ActionID:" + property.getName() + ":" + "ComponentType:" + componentType + ":" + currentComponent.getName());
					getNode.setState(true, false);
					propertyNode.addItem(getNode);
					
					propertiesNode.addItem(propertyNode);
				}
			}
			
			all.addItem(propertiesNode);

			// Functions
			
			if(!COMPONENT_DATABASE.getMethodDefinitions(componentType).isEmpty()) { //Skip empty nodes
				
				List<String> functionsHTML = new ArrayList<String>();
				List<String> functionsValues = new ArrayList<String>();
				
				for (MethodDefinition function : COMPONENT_DATABASE.getMethodDefinitions(componentType)) {
					functionsValues.add("ActionType:Function:ActionID:" + function.getName() + ":" + "ComponentType:" + componentType + ":" + currentComponent.getName());
					functionsHTML.add(function.getName());
				}

				addNode(all, functionsHTML, functionsValues, "<b> Functions </b>", "Functions");
			}

			// Events
			
			if(!COMPONENT_DATABASE.getEventDefinitions(componentType).isEmpty()) { //Skip empty nodes

				List<String> eventsHTML = new ArrayList<String>();
				List<String> eventsValues = new ArrayList<String>();
				
				for (EventDefinition event : COMPONENT_DATABASE.getEventDefinitions(componentType)) {
					eventsValues.add("ActionType:Event:ActionID:" + event.getName() + ":" + "ComponentType:" + componentType + ":" + currentComponent.getName());
					eventsHTML.add(event.getName());
				}
				
				addNode(all, eventsHTML, eventsValues, "<b> Events </b>", "Events");
			}
		}
	}
}
