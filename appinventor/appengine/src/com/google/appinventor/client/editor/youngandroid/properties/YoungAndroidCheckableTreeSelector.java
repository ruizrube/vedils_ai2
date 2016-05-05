// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.editor.youngandroid.properties;

import com.google.appinventor.client.editor.youngandroid.YaFormEditor;
import com.google.appinventor.client.widgets.properties.AdditionalChoicePropertyEditor;
import com.google.appinventor.shared.simple.ComponentDatabaseInterface.EventDefinition;
import com.google.appinventor.shared.simple.ComponentDatabaseInterface.MethodDefinition;
import com.google.appinventor.shared.simple.ComponentDatabaseInterface.PropertyDefinition;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.appinventor.client.editor.simple.SimpleComponentDatabase;

/**
 * Property editor for selecting methods name for ActivityTracker.
 *
 */
public final class YoungAndroidCheckableTreeSelector extends AdditionalChoicePropertyEditor {

  private String activitiesToNotify;
  private CheckableTree tree;
  
  public YoungAndroidCheckableTreeSelector(final YaFormEditor editor, final SimpleComponentDatabase COMPONENT_DATABASE, final String componentType) {
    ScrollPanel selectorPanel = new ScrollPanel();
    selectorPanel.setSize("190px", "190px");
    
    tree = new CheckableTree();
    tree.setWidth("60px");
    TreeItem all = new CheckableTreeItem("all");
    all.setHTML("<b> all </b>");
    
    //Getters and Setters
    TreeItem set = new CheckableTreeItem("Setters");
    set.setHTML("<b> Setters </b>");
    TreeItem get = new CheckableTreeItem("Getters"); 
    get.setHTML("<b> Getters </b>");
    
	for(PropertyDefinition property: COMPONENT_DATABASE.getPropertyDefinitions(componentType)) {
		TreeItem itemPropertyget = new CheckableTreeItem("Get - "+property.getName());
		itemPropertyget.setHTML(property.getName());
		itemPropertyget.setTitle("Get - "+property.getName());
		get.addItem(itemPropertyget);
		TreeItem itemPropertyset = new CheckableTreeItem("Set - "+property.getName());
		itemPropertyset.setHTML(property.getName());
		set.addItem(itemPropertyset);
		itemPropertyset.setTitle("Set - "+property.getName());
	}
	
    all.addItem(set);
    all.addItem(get);
	
	//Functions	
	TreeItem functions = new CheckableTreeItem("Functions");
	functions.setHTML("<b> Functions </b>");
	functions.setState(true, false);
	
	for(MethodDefinition function: COMPONENT_DATABASE.getMethodDefinitions(componentType)) {
		TreeItem itemFunction = new CheckableTreeItem(function.getName());
		itemFunction.setHTML(function.getName());
		itemFunction.setTitle(function.getName());
		functions.addItem(itemFunction);
	}
	
	all.addItem(functions);
	
	//Events
	TreeItem events = new CheckableTreeItem("Events");
	events.setHTML("<b> Events </b>");
	
	for(EventDefinition event: COMPONENT_DATABASE.getEventDefinitions(componentType)) {
		TreeItem itemEvent = new CheckableTreeItem(event.getName());
		itemEvent.setHTML(event.getName());
		itemEvent.setTitle(event.getName());
		events.addItem(itemEvent);
	}
	
	all.addItem(events);

	tree.addItem(all);
	selectorPanel.add(tree);
	
	this.activitiesToNotify = "";
    initAdditionalChoicePanel(selectorPanel);
  }
  
  @Override
  protected void openAdditionalChoiceDialog() {
	property.setValue("Nothing");
    super.openAdditionalChoiceDialog();
    tree.setFocus(true);
  }

  @Override
  protected String getPropertyValueSummary() {
	  return property.getValue();
  }
  
  private void recordSelectedItems() {
	  if(!this.activitiesToNotify.equals("")) {
		  this.activitiesToNotify = "";
	  }
	  
	  for(int i = 0; i < tree.getItem(0).getChildCount(); i++) //Child nodes of "All".
	  {
		  CheckableTreeItem item = (CheckableTreeItem) tree.getItem(0).getChild(i); 

	      if(item.getStatus() == CheckableTreeItem.SELECTION_ALL || item.getStatus() == CheckableTreeItem.SELECTION_SOME) {
	    	  
	    	  //Search the child nodes
	    	  for(int j = 0; j < item.getChildCount(); j++) {
	    		  
	    		  CheckableTreeItem itemLeaf = (CheckableTreeItem) item.getChild(j);
	    		  
	    		  if(itemLeaf.getStatus() == CheckableTreeItem.SELECTION_ALL) { //Are leaf
	    			  this.activitiesToNotify = this.activitiesToNotify + itemLeaf.getTitle() + " - ";
	    		  }
	    		  
	    	  }
	    	  
	      }
	  }
  }

  @Override
  protected boolean okAction() {
	 //When option "OK" is clicked the elements of tree are recorded.
	recordSelectedItems();
    if (this.activitiesToNotify.equals("")) {
      property.setValue("Nothing");
      Window.alert("Nothing selected.");
      return true;
    } 
    property.setValue("Record elements: "+this.activitiesToNotify);
    //Window.alert("Component name: " +componentName + " Options selected: "+this.methodsSelectedNames);
    return true;
  }
}