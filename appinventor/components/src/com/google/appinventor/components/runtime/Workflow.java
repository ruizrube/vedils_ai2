// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.components.runtime;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Documentation;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.Event;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.camunda.bpm.model.bpmn.instance.InclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.ParallelGateway;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.Task;
import org.camunda.bpm.model.bpmn.instance.UserTask;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;

import android.util.Log;

/**
 * Component for using the built in VoiceRecognizer to convert speech to text.
 * For more details, please see:
 * http://developer.android.com/reference/android/speech/RecognizerIntent.html
 *
 */

@UsesLibraries(libraries = "camunda-bpmn-model-7.8.0.jar,camunda-xml-model-7.8.0.jar,sax-2.0.1.jar")
@DesignerComponent(version = YaVersion.CONVERSATION_COMPONENT_VERSION, description = "Component for using a workflow during the execution of the app", category = ComponentCategory.VEDILSINTERACTIONS, nonVisible = true, iconName = "images/sharing.png")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.READ_EXTERNAL_STORAGE,android.permission.WRITE_EXTERNAL_STORAGE")
public class Workflow extends AndroidNonvisibleComponent implements Component {

	private final ComponentContainer container;

	private boolean running;

	private Map<String, Object> data;

	private Node currentNode;

	private Map<String, Node> nodes;

	private List<Transition> transitions;

	private BpmnModelInstance modelInstance;

	private String bpmnPath = "";

	/**
	 * Creates a SpeechRecognizer component.
	 *
	 * @param container
	 *            container, component will be placed in
	 */
	public Workflow(ComponentContainer container) {
		super(container.$form());
		this.container = container;
		data = new HashMap<String, Object>();
		nodes = new HashMap<String, Node>();
		transitions = new ArrayList<Transition>();

	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE)
	public String Definition() {
		return bpmnPath;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET, defaultValue = "")
	@SimpleProperty
	public void Definition(String path) {
		bpmnPath = (path == null) ? "" : path;

		this.LoadWorkflowDefinition(bpmnPath);
	}

	@SimpleFunction
	public void AddNode(String nodeId, String nodeType, String nodeSubType, Object arguments) {

		this.nodes.put(nodeId, new Node(nodeId, nodeType, nodeSubType, arguments));

	}

	@SimpleFunction
	public void AddTransition(String sourceNode, String targetNode, String condition) {

		Node source = this.nodes.get(sourceNode);
		Node target = this.nodes.get(targetNode);

		this.transitions.add(new Transition(source, target, condition));

	}

	private InputStream getAssetPath(String path) throws IOException {

		if (this.isAiCompanionActive()) {
			Log.d("Workflow", "Trying to open file with name: " + "/sdcard/AppInventor/assets/" + path);

			return new FileInputStream("/sdcard/AppInventor/assets/" + path);
		} else {
			Log.d("Workflow", "Trying to open file: " + path);

			// Log.d(LOGTAG, "Aplicacion arrancando desde la apk");
			return this.container.$context().getAssets().open(path);
		}
	}

	private boolean isAiCompanionActive() {
		return container.$form() instanceof ReplForm;
	}

	@SimpleFunction
	public void LoadWorkflowDefinition(String fileName) {

		// read a model from a file
		// File file = new File(fileName);
		try {
			
			InputStream stream=getAssetPath(fileName);
			Log.d("Workflow", "Stream obtained...");

			modelInstance = Bpmn.readModelFromStream(stream);

			Log.d("Workflow", "Loading workflow...");

			Collection<Task> tasks = modelInstance.getModelElementsByType(Task.class);

			for (Task task : tasks) {

				if (task instanceof ServiceTask) {
					Iterator<Documentation> it = task.getDocumentations().iterator();
					while (it.hasNext()) {
						Documentation doc = it.next();
						this.AddNode(task.getName(), "TASK", "SERVICE TASK", doc.getTextContent());
					}
				} else if (task instanceof UserTask) {
					this.AddNode(task.getName(), "TASK", "USER TASK", "");

					Log.d("Workflow", "USER TASK " + task.getName() + ":" + "...");

				}
			}

			Collection<Event> events = modelInstance.getModelElementsByType(Event.class);

			for (Event event : events) {
				if (event instanceof StartEvent) {
					this.AddNode(event.getName(), "EVENT", "START", "");
					Log.d("Workflow", "START EVENT " + event.getName());
				} else if (event instanceof EndEvent) {
					this.AddNode(event.getName(), "EVENT", "END", "");
					Log.d("Workflow", "END EVENT " + event.getName());
				} else {
					this.AddNode(event.getName(), "EVENT", "INTERMEDIATE", "");
					Log.d("Workflow", "INTERMEDIATE EVENT " + event.getName());
				}
			}

			Collection<Gateway> gateways = modelInstance.getModelElementsByType(Gateway.class);
			for (Gateway gateway : gateways) {
				if (gateway instanceof ParallelGateway) {
					this.AddNode(gateway.getName(), "GATEWAY", "AND", "");
					Log.d("Workflow", "AND GATEWAY " + gateway.getName());
				} else if (gateway instanceof ExclusiveGateway) {
					this.AddNode(gateway.getName(), "GATEWAY", "XOR", "");
					Log.d("Workflow", "XOR GATEWAY " + gateway.getName());
				} else if (gateway instanceof InclusiveGateway) {
					this.AddNode(gateway.getName(), "GATEWAY", "OR", "");
					Log.d("Workflow", "OR GATEWAY " + gateway.getName());
				} else {
					Log.d("Workflow", "OTHER GATEWAY " + gateway.getName());
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Workflow", "Error Loading workflow...");
			Log.d("Workflow", "Error: " + e.getMessage());

		}

	}

	@SimpleFunction
	public void PutData(String dataKey, Object dataValue) {

		this.data.put(dataKey, dataValue);

	}

	@SimpleFunction
	public void StartWorkflow() {
		this.running = true;
		Log.d("Workflow", "Comenzando... ");

		this.currentNode = null;
		
		
		if(this.nodes.size()==0){
			this.LoadWorkflowDefinition(bpmnPath);
		}
		
		for (Node node : this.nodes.values()) {
			if (node.getNodeType().equals("EVENT") && node.getSubType().equals("START")) {
				this.currentNode = node;
			}
		}
		CompleteTask();

	}

	@SimpleFunction
	public void CompleteTask() {

		if (running) {
			this.currentNode = retrieveNextNode(currentNode);

			if (currentNode.getNodeType().equals("TASK")) {
				dispatchTask(currentNode);
			} else if (currentNode.getNodeType().equals("EVENT")) {
				dispatchEvent(currentNode);
			} else if (currentNode.getNodeType().equals("GATEWAY")) {
				dispatchGateway(currentNode);
			} else {
				WorkflowError("CurrentNodeType unknown");
			}
		}

	}

	@SimpleFunction
	public void AbortTask() {
		if (running) {
			this.running = false;
			EventDispatcher.dispatchEvent(this, "WorkflowAborted");
		}

	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public boolean IsRunning() {
		return running;
	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public Object Data() {
		return data;
	}

	@SimpleEvent
	public void WorkflowStarted() {

		EventDispatcher.dispatchEvent(this, "WorkflowStarted");
	}

	@SimpleEvent
	public void WorkflowAborted() {

		EventDispatcher.dispatchEvent(this, "WorkflowAborted");
	}

	@SimpleEvent
	public void WorkflowEnded() {

		EventDispatcher.dispatchEvent(this, "WorkflowEnded");
	}

	@SimpleEvent
	public void NodeChanged(String currentNode) {

		EventDispatcher.dispatchEvent(this, "NodeChanged", currentNode);
	}

	@SimpleEvent
	public void WorkflowError(String errorType) {

		EventDispatcher.dispatchEvent(this, "WorkflowError", errorType);
	}

	@SimpleEvent
	public void TaskLaunched(String taskType, List taskArguments) {

		EventDispatcher.dispatchEvent(this, "TaskLaunched", taskType, taskArguments);
	}

	private Node retrieveNextNode(Node node) {

		Node result = node;
		for (Transition transition : this.transitions) {
			if (result == node && transition.getSource().equals(this.currentNode)
					&& conditionSatisfied(transition.getCondition())) {
				result = transition.getTarget();
			}
		}

		Log.d("Workflow", "Proximo nodo: " + result.getId());

		return result;
	}

	private boolean conditionSatisfied(String condition) {
		boolean result = false;

		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");

		for (String dataIndex : this.data.keySet()) {
			condition = condition.replace(dataIndex, this.data.get(dataIndex).toString());
		}

		Log.d("Workflow", "Executing in script environment...");
		try {

			Object aux = jsEngine.eval(condition);
			Log.d("Workflow", "Resulting in script environment..." + aux);
			result = aux.toString().equals("true");
		} catch (ScriptException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	private void dispatchGateway(Node node) {
		this.CompleteTask();

	}

	private void dispatchEvent(Node node) {
		if (currentNode.getSubType().equals("START")) {
			WorkflowStarted();
		} else if (currentNode.getSubType().equals("END")) {
			WorkflowEnded();
		} else {
			WorkflowError("CurrentNodeSubType unknown");

		}

	}

	private void dispatchTask(Node node) {

		TaskLaunched(node.getSubType(), node.getArguments());
		// WorkflowError("CurrentNodeType unknown");
	}

	public class Transition {
		private Node source;
		private Node target;
		private String condition;

		public Transition(Node source, Node target, String condition) {
			this.source = source;
			this.target = target;
			this.condition = condition;
		}

		public String getCondition() {
			return condition;
		}

		public void setCondition(String condition) {
			this.condition = condition;
		}

		public Node getSource() {
			return source;
		}

		public void setSource(Node source) {
			this.source = source;
		}

		public Node getTarget() {
			return target;
		}

		public void setTarget(Node target) {
			this.target = target;
		}

	}

	public class Node {
		private String id;
		private String nodeType;
		private String subType;
		private List arguments;

		public Node(String nodeId, String nodeType, String nodeSubType, Object arguments) {
			this.id = nodeId;
			this.nodeType = nodeType;
			this.subType = nodeSubType;
			this.arguments = (List) arguments;

		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getNodeType() {
			return nodeType;
		}

		public void setNodeType(String nodeType) {
			this.nodeType = nodeType;
		}

		public String getSubType() {
			return subType;
		}

		public void setSubType(String subType) {
			this.subType = subType;
		}

		public List getArguments() {
			return arguments;
		}

		public void setArguments(List arguments) {
			this.arguments = arguments;
		}

	}

}
