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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.google.appinventor.components.runtime.util.WorkflowDefinition;
import com.google.appinventor.components.runtime.util.WorkflowLoader;
import com.google.appinventor.components.runtime.util.WorkflowNode;
import com.google.appinventor.components.runtime.util.WorkflowTransition;

import android.util.Log;
import bsh.EvalError;
import bsh.Interpreter;

/**
 * Component for using the built in VoiceRecognizer to convert speech to text.
 * For more details, please see:
 * http://developer.android.com/reference/android/speech/RecognizerIntent.html
 *
 */

@UsesLibraries(libraries = "bsh-2.0b4.jar")
@DesignerComponent(version = YaVersion.CONVERSATION_COMPONENT_VERSION, description = "Component for using a workflow during the execution of the app", category = ComponentCategory.VEDILSINTERACTIONS, nonVisible = true, iconName = "images/sharing.png")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.READ_EXTERNAL_STORAGE,android.permission.WRITE_EXTERNAL_STORAGE")
public class Workflow extends AndroidNonvisibleComponent implements Component {

	private final ComponentContainer container;

	private boolean running;

	private Map<String, Object> data;

	private WorkflowNode currentNode;

	private WorkflowDefinition process;

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
	public void LoadWorkflowDefinition(String fileName) {

		try {

			// prepareXMLParser();

			InputStream stream = getAssetPath(fileName);
			Log.d("Workflow", "Stream obtained...");

			WorkflowLoader loader = new WorkflowLoader();
			Log.d("Workflow", "Loading workflow...");
			this.process = loader.readStream(stream);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Workflow", "Error Loading workflow...");
			Log.d("Workflow", "Error: " + e.getMessage());

		}

	}

	@SimpleFunction
	public void AddNode(String nodeId, String nodeType, String nodeSubType, Object arguments) {

		List list = new ArrayList();
		list.add(arguments);
		this.process.getNodes().put(nodeId, new WorkflowNode(nodeId, nodeId, nodeType, nodeSubType, list));

	}

	@SimpleFunction
	public void AddTransition(String sourceNode, String targetNode, String condition) {

		// WorkflowNode source = this.process.getNodes().get(sourceNode);
		// WorkflowNode target = this.process.getNodes().get(targetNode);

		this.process.getTransitions().add(new WorkflowTransition(sourceNode, targetNode, condition));

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
	public void PutData(String dataKey, Object dataValue) {

		this.data.put(dataKey, dataValue);

	}

	@SimpleFunction
	public void StartWorkflow() {

		if (this.process == null) {
			this.LoadWorkflowDefinition(bpmnPath);
		}

		if (this.process != null) {
			this.running = true;
			Log.d("Workflow", "Comenzando... ");

			this.currentNode = null;
			this.data = new HashMap<String, Object>();

			for (WorkflowNode node : this.process.getNodes().values()) {
				if (node.getNodeType().equals("EVENT") && node.getSubType().equals("START")) {
					this.currentNode = node;
				}
			}
			WorkflowStarted();
			
			CompleteTask();
		}

	}

	@SimpleFunction
	public void CompleteTask() {

		if (running) {
			this.currentNode = retrieveNextNode(currentNode);

			if (this.currentNode != null) {
				NodeChanged(this.currentNode.getName());
				if (currentNode.getNodeType().equals("TASK")) {
					dispatchTask(currentNode);
				} else if (currentNode.getNodeType().equals("EVENT")) {
					dispatchEvent(currentNode);
				} else if (currentNode.getNodeType().equals("GATEWAY")) {
					dispatchGateway(currentNode);
				} else {
					WorkflowError("CurrentNodeType unknown");
				}
			} else {
					WorkflowError("No viable ways. Data: "+this.data.toString());				
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
		this.running = false;
		
		EventDispatcher.dispatchEvent(this, "WorkflowAborted");
	}

	@SimpleEvent
	public void WorkflowEnded() {
		this.running = false;
		
		EventDispatcher.dispatchEvent(this, "WorkflowEnded");
	}

	@SimpleEvent
	public void NodeChanged(String currentNode) {

		EventDispatcher.dispatchEvent(this, "NodeChanged", currentNode);
	}

	@SimpleEvent
	public void WorkflowError(String errorType) {
		this.running = false;
		
		EventDispatcher.dispatchEvent(this, "WorkflowError", errorType);
	}

	@SimpleEvent
	public void TaskLaunched(String taskType, List taskArguments) {

		EventDispatcher.dispatchEvent(this, "TaskLaunched", taskType, taskArguments);
	}

	private WorkflowNode retrieveNextNode(WorkflowNode node) {

		WorkflowNode result = null;
		for (WorkflowTransition transition : this.process.getTransitions()) {
			if (result == null && transition.getSource() != null
					&& transition.getSource().equals(this.currentNode.getId())
					&& conditionSatisfied(transition.getCondition())) {
				result = this.process.getNodes().get(transition.getTarget());
				Log.d("Workflow", "Proximo nodo: " + result.getName());
			}
		}

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bpmnPath == null) ? 0 : bpmnPath.hashCode());
		return result;
	}

	private boolean conditionSatisfied(String condition) {
		boolean result = true;

		// ScriptEngineManager mgr = new ScriptEngineManager();
		// ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");

		if (condition != null && !condition.equals("")) {
			Log.d("Workflow", "Pre-Executing in script environment... " + condition);

			for (String dataIndex : this.data.keySet()) {
				condition = condition.replace(dataIndex, this.data.get(dataIndex).toString());
			}

			Log.d("Workflow", "Executing in script environment... " + condition);
			try {

				Interpreter interpreter = new Interpreter();
				interpreter.eval("result = " + condition);
				String aux = interpreter.get("result").toString();
				Log.d("Workflow", "Resulting in script environment..." + aux);
				result = aux.equals("true");
			} catch (EvalError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = false;
			}
		}
		return result;
	}

	private void dispatchGateway(WorkflowNode node) {
		this.CompleteTask();

	}

	private void dispatchEvent(WorkflowNode node) {
		if (currentNode.getSubType().equals("START")) {
			WorkflowStarted();
		} else if (currentNode.getSubType().equals("END")) {
			WorkflowEnded();
		} else {
			WorkflowError("CurrentNodeSubType unknown");

		}

	}

	private void dispatchTask(WorkflowNode node) {

		TaskLaunched(node.getSubType(), node.getArguments());
		// WorkflowError("CurrentNodeType unknown");
	}

}
