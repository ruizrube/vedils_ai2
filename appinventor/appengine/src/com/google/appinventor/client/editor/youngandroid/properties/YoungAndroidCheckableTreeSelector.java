package com.google.appinventor.client.editor.youngandroid.properties;

import java.util.Arrays;
import java.util.List;

import com.google.appinventor.client.editor.simple.SimpleComponentDatabase;
import com.google.appinventor.client.output.OdeLog;
import com.google.appinventor.client.widgets.properties.AdditionalChoicePropertyEditor;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.shared.simple.ComponentDatabaseInterface.EventDefinition;
import com.google.appinventor.shared.simple.ComponentDatabaseInterface.MethodDefinition;
import com.google.appinventor.shared.simple.ComponentDatabaseInterface.ParameterDefinition;
import com.google.appinventor.shared.simple.ComponentDatabaseInterface.PropertyDefinition;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Abstract Tree Property editor to help subclasses.
 * By SPI-FM.
 */
public abstract class YoungAndroidCheckableTreeSelector extends AdditionalChoicePropertyEditor {

	protected String recordedItems;
	protected CheckableTree tree;
	private List<String> ignoreNodes = Arrays.asList("Getters", "Setters", "Functions", "Events", "User Info",
			"Context", "Activity", "Interaction Data", "all");
	
	protected final String ACTIVITYTRACKER_COMPONENT = "ActivityTracker";
	protected final String ACTIVITYAGGREGATIONQUERY_COMPONENT = "ActivityAggregationQuery";
	protected final String ACTIVITYSIMPLEQUERY_COMPONENT = "ActivitySimpleQuery";

	@Override
	protected void openAdditionalChoiceDialog() {
		super.openAdditionalChoiceDialog();
		tree.setFocus(true);
	}

	@Override
	protected String getPropertyValueSummary() {
		loadSelectedNodes(property.getValue());
		return property.getValue();
	}
	
	@Override
	protected boolean okAction() {
		// When option "OK" is clicked the elements of tree are recorded.
		retrieveSelectedNodes();
		if (this.recordedItems.equals("")) {
			property.setValue("Nothing");
			Window.alert("Nothing selected.");
			return true;
		}
		property.setValue("Record elements: " + this.recordedItems);
		return true;
	}
	
	//
	// Retrieve Tree values
	//
	
	private void retrieveSelectedNodes() {
		this.recordedItems = "";

		for (int i = 0; i < tree.getItem(0).getChildCount(); i++) { // Child nodes of "All".
			CheckableTreeItem itemCategory = (CheckableTreeItem) tree.getItem(0).getChild(i);

			if (itemCategory.getStatus() == CheckableTreeItem.SELECTION_ALL
					|| itemCategory.getStatus() == CheckableTreeItem.SELECTION_SOME) { //Items Level 1 are categories 

				// Search the child nodes
				for (int j = 0; j < itemCategory.getChildCount(); j++) {
					CheckableTreeItem itemNode = (CheckableTreeItem) itemCategory.getChild(j);
					this.recordedItems = this.recordedItems + retrieveSelectedNodesRec(itemNode) + " ";
				}
			}
		}
	}
	
	private String retrieveSelectedNodesRec(CheckableTreeItem node) {
		String value = "";
		if(node.getStatus() == CheckableTreeItem.SELECTION_ALL || 
				node.getStatus() == CheckableTreeItem.SELECTION_SOME) {		
			if(node.getChildCount() == 0) { //Is a leaf node
				return node.getUserObject().toString() + " -";
			} else { //Retrieve child node values
				if(!ignoreNodes.contains(node.getUserObject().toString())) {
					value = value + node.getUserObject().toString() + " -";
				}
				
				for(int i = 0; i < node.getChildCount() ; i++) {
					if(node.getStatus() == CheckableTreeItem.SELECTION_ALL || 
							node.getStatus() == CheckableTreeItem.SELECTION_SOME) {
						value = value + " " + retrieveSelectedNodesRec((CheckableTreeItem)node.getChild(i));
					}
				}
				return value;
			}
		} else {
			return "";
		}
	}
	
	//
	// Load Tree values
	//
	
	private int loadSelectedNodesRec(CheckableTreeItem node, String selectedNodes) {
		int value = 0;
		
		if(selectedNodes.contains(node.getUserObject().toString()) &&
				!ignoreNodes.contains(node.getUserObject().toString())) { //count current node
			value += 1;
		}
		
		for(int i = 0; i < node.getChildCount() ; i++) {
			value += loadSelectedNodesRec((CheckableTreeItem)node.getChild(i), selectedNodes);
		}
		
		//Finally, change the node status
		if(value == getDescendantsNumber(node) && value != 0) {
			node.setStatus(CheckableTreeItem.SELECTION_ALL);
		} else if(value != 0) {
			node.setStatus(CheckableTreeItem.SELECTION_SOME);
		}
		
		return value;
	}
	
	public void loadSelectedNodes(String selectedNodes) {
		loadSelectedNodesRec((CheckableTreeItem)tree.getItem(0), selectedNodes);
	}
	
	private int getDescendantsNumber(CheckableTreeItem node) {
		int descendantsNumber = 1;
		
		if(ignoreNodes.contains(node.getUserObject().toString())) { //filter nodes
			descendantsNumber = 0;
		}
		
		for(int i = 0; i < node.getChildCount() ; i++) {
			descendantsNumber += getDescendantsNumber((CheckableTreeItem)node.getChild(i));
		}
		
		return descendantsNumber;
	}
	
	
	//
	// Add category nodes to Tree
	//
	
	public void addNode(TreeItem parentNode, List<String> itemsHTML, List<String> itemsValue, String categoryHTML, String categoryValue) {
		TreeItem categoryNode = new CheckableTreeItem(categoryValue);
		categoryNode.setHTML(categoryHTML);
		categoryNode.setTitle(categoryValue);
		categoryNode.setUserObject(categoryValue);
		categoryNode.setState(true, false);
		
		for (String item : itemsHTML) {
			TreeItem itemNode = new CheckableTreeItem(itemsValue.get(itemsHTML.indexOf(item)));
			itemNode.setHTML(item);
			itemNode.setTitle(itemsValue.get(itemsHTML.indexOf(item)));
			itemNode.setUserObject(itemsValue.get(itemsHTML.indexOf(item)));			
			categoryNode.addItem(itemNode);
		}

		parentNode.addItem(categoryNode);
	}
	
	//
	// Add Property nodes to Tree
	//
	
	protected void addPropertiesNodes(final SimpleComponentDatabase COMPONENT_DATABASE, final String componentType, 
			String componentName, List<String> aggregators, TreeItem component, String selectedNodes) {
		
		for (PropertyDefinition property : COMPONENT_DATABASE.getPropertyDefinitions(componentType)) {
			if (!property.getName().equals("ComponentName") && !property.getName().equals("ActivitiesToTrack")) {
				
				if((selectedNodes.contains("ActionType:Get:ActionID:"+ property.getName() + ":ComponentType:" + componentType) || 
						selectedNodes.contains("ActionType:Set:ActionID:"+ property.getName() + ":ComponentType:" + componentType))) {
					
					TreeItem itemProperty = new CheckableTreeItem("ActionID:" + property.getName() + componentName);
					itemProperty.setHTML("<b> Property </b> " + property.getName());
					itemProperty.setTitle("ActionID:" + property.getName() + componentName);
					itemProperty.setUserObject("ActionID:" + property.getName() + componentName);
					
					if(selectedNodes.contains("ActionType:Get:ActionID:"+ property.getName())) {
						
						TreeItem itemPropertyget = new CheckableTreeItem("ActionType:Get:ActionID:" + property.getName() + ":" + "ComponentType:" + componentType 
								+ ":" + "ComponentID:" + componentName + " - Get:Param:" + property.getName().toLowerCase() + ":" + componentName);
						itemPropertyget.setHTML("Get");
						itemPropertyget.setTitle("ActionType:Get:ActionID:" + property.getName() + ":" + "ComponentType:" + componentType 
								+ ":" + "ComponentID:" + componentName + " - Get:Param:" + property.getName().toLowerCase() + ":" + componentName);
						itemPropertyget.setUserObject("ActionType:Get:ActionID:" + property.getName() + ":" + "ComponentType:" + componentType 
								+ ":" + "ComponentID:" + componentName + " - Get:Param:" + property.getName().toLowerCase() + ":" + componentName);
						
						if(aggregators != null) {
							if(property.getEditorType().equals(PropertyTypeConstants.PROPERTY_TYPE_INTEGER) ||
								property.getEditorType().equals(PropertyTypeConstants.PROPERTY_TYPE_FLOAT) ||
								property.getEditorType().equals(PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER) ||
								property.getEditorType().equals(PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT)) {
									
								addAggregatorsNodes("number", property.getName(), "Get", componentName, itemPropertyget, aggregators, 1);	
							} else {
								addAggregatorsNodes("other", property.getName(), "Get", componentName, itemPropertyget, aggregators, 1);
							}
						}
						
						itemProperty.addItem(itemPropertyget);
						
					}
					
					if(selectedNodes.contains("ActionType:Set:ActionID:"+ property.getName())) {
						
						TreeItem itemPropertyset = new CheckableTreeItem("ActionType:Set:ActionID:" + property.getName() + ":" + "ComponentType:" + componentType 
								+ ":" + "ComponentID:" + componentName + " - Set:Param:" + property.getName().toLowerCase() + ":" + componentName);
						itemPropertyset.setHTML("Set");
						itemPropertyset.setTitle("ActionType:Set:ActionID:" + property.getName() + ":" + "ComponentType:" + componentType 
								+ ":" + "ComponentID:" + componentName + " - Set:Param:" + property.getName().toLowerCase() + ":"+ componentName);
						itemPropertyset.setUserObject("ActionType:Set:ActionID:" + property.getName() + ":" + "ComponentType:" 
								+ componentType + ":" + "ComponentID:" + componentName + " - Set:Param:" + property.getName().toLowerCase() + ":" + componentName);
						
						if(aggregators != null) {
							if(property.getEditorType().equals(PropertyTypeConstants.PROPERTY_TYPE_INTEGER) ||
								property.getEditorType().equals(PropertyTypeConstants.PROPERTY_TYPE_FLOAT) ||
								property.getEditorType().equals(PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER) ||
								property.getEditorType().equals(PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT)) {
									
								addAggregatorsNodes("number", property.getName(), "Set", componentName, itemPropertyset, aggregators, 1);
							} else {
								addAggregatorsNodes("other", property.getName(), "Set", componentName, itemPropertyset, aggregators, 1);
							}
						}
						itemProperty.addItem(itemPropertyset);
					}
					component.addItem(itemProperty);
				}
			}
		}
	}
	
	//
	// Add Function nodes to Tree
	//
	
	protected void addFunctionsNodes(final SimpleComponentDatabase COMPONENT_DATABASE, String componentType, 
			String componentName, List<String> aggregators, TreeItem functions, boolean ignoreLeafNodes, String selectedNodes) {
		
		for (MethodDefinition function : COMPONENT_DATABASE.getMethodDefinitions(componentType)) {
			if(selectedNodes.contains("ActionType:Function:ActionID:"+ function.getName() + ":ComponentType:" + componentType)) {
				if(!(ignoreLeafNodes && function.getParam().isEmpty())) {
					TreeItem itemFunction = new CheckableTreeItem("ActionType:Function:ActionID:" + function.getName() + ":" + "ComponentType:" + componentType 
							+ ":" + "ComponentID:" + componentName);
					itemFunction.setHTML("<b> Function </b>" + function.getName());
					itemFunction.setTitle("ActionType:Function:ActionID:" + function.getName() + ":" + "ComponentType:" + componentType 
							+ ":" + "ComponentID:" + componentName);
					itemFunction.setUserObject("ActionType:Function:ActionID:" + function.getName() + ":" + "ComponentType:" + componentType 
							+ ":" + "ComponentID:" + componentName);
					
					int index = 0;
					
					//Return parameter added (for all methods..)
					if(aggregators == null || selectedNodes.contains("Function:Param:"+ function.getName().toLowerCase())) {
						TreeItem param = new CheckableTreeItem("Function:Param:" + function.getName().toLowerCase() + ":" + componentName + ":" + 0);
						param.setHTML(function.getName().toLowerCase());
						param.setTitle("Function:Param:" + function.getName().toLowerCase() + ":" + componentName + ":" + 0);
						param.setUserObject("Function:Param:" + function.getName().toLowerCase() + ":" + componentName + ":" + 0);
						
						if(aggregators != null) {
							addAggregatorsNodes("number", function.getName().toLowerCase(), "Function", componentName, param, aggregators, 0);
						}
						
						itemFunction.addItem(param);
						index++;
					}
					
					//Add the parameters					
					for(ParameterDefinition parameter : function.getParam()) {
						if(aggregators == null || selectedNodes.contains("Function:Param:"+ parameter.getName())) {
							TreeItem param = new CheckableTreeItem("Function:Param:" + parameter.getName() + ":" + componentName + ":" + index);
							param.setHTML(parameter.getName());
							param.setTitle("Function:Param:" + parameter.getName() + ":" + componentName + ":" + index);
							param.setUserObject("Function:Param:" + parameter.getName() + ":" + componentName + ":" + index);
							
							if(aggregators != null) {
								addAggregatorsNodes(parameter.getType(), parameter.getName(), "Function", componentName, param, aggregators, index);
							}
							
							itemFunction.addItem(param);
							index++;
						}
					}
					functions.addItem(itemFunction);
				}
			}
		}
	}
	
	//
	// Add Event nodes to Tree
	//
	
	protected void addEventsNodes(final SimpleComponentDatabase COMPONENT_DATABASE, String componentType, 
			String componentName, List<String> aggregators, TreeItem events, boolean ignoreLeafNodes, String selectedNodes) {
		
		for (EventDefinition event : COMPONENT_DATABASE.getEventDefinitions(componentType)) {
			if(selectedNodes.contains("ActionType:Event:ActionID:"+ event.getName() + ":ComponentType:" + componentType)) {
				if(!(ignoreLeafNodes && event.getParam().isEmpty())) {
					TreeItem itemEvent = new CheckableTreeItem("ActionType:Event:ActionID:" + event.getName() + ":" + "ComponentType:" + componentType 
							+ ":" + "ComponentID:" + componentName);
					itemEvent.setHTML("<b> Event </b> " + event.getName());
					itemEvent.setTitle("ActionType:Event:ActionID:" + event.getName() + ":" + "ComponentType:" + componentType 
							+ ":" + "ComponentID:" + componentName);
					itemEvent.setUserObject("ActionType:Event:ActionID:" + event.getName() + ":" + "ComponentType:" + componentType 
							+ ":" + "ComponentID:" + componentName);
					
					int index = 1;
					
					//Add the parameters
					for(ParameterDefinition parameter : event.getParam()) {
						if(aggregators == null || selectedNodes.contains("Event:Param:"+ parameter.getName())) {							OdeLog.log("Estoy entrando 3");
							TreeItem param = new CheckableTreeItem("Event:Param:" + parameter.getName() + ":" + componentName + ":" + index);
							param.setHTML(parameter.getName());
							param.setTitle("Event:Param:" + parameter.getName() + ":" + componentName + ":" + index);
							param.setUserObject("Event:Param:" + parameter.getName() + ":" + componentName + ":" + index);
							
							if(aggregators != null) {
								addAggregatorsNodes(parameter.getType(), parameter.getName(), "Event", componentName, param, aggregators, index);
							}
							
							itemEvent.addItem(param);
							index++;
						}
					}
					events.addItem(itemEvent);
				}
			}
		}
	}
	
	//
	// Add Aggregation nodes to Tree
	//
	
	private void addAggregatorsNodes(String paramType, String paramName, String typeOperation, String componentName, 
			TreeItem paramNode, List<String> aggregators, int index) {
		
		if(paramType.equals("number")) {
			for (String aggregator : aggregators) {
				TreeItem aggregatorNode = new CheckableTreeItem(aggregator);
				aggregatorNode.setHTML(aggregator);
				aggregatorNode.setTitle(aggregator);
				aggregatorNode.setUserObject(typeOperation + ":" + aggregator + "(" + paramName + ")" + ":" + componentName + ":" + index);
				paramNode.addItem(aggregatorNode);
				paramNode.addItem(aggregatorNode);
			}
		} else {
			TreeItem aggregatorNode = new CheckableTreeItem("Count");
			aggregatorNode.setHTML("Count");
			aggregatorNode.setTitle("Count");
			aggregatorNode.setUserObject(typeOperation + ":Count(" + paramName + ")" + ":" + componentName + ":" + index);
			paramNode.addItem(aggregatorNode);
			paramNode.addItem(aggregatorNode);
		}
	}
}