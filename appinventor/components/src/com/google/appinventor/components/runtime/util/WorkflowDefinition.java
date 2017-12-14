package com.google.appinventor.components.runtime.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowDefinition {
		private Map<String, WorkflowNode> nodes;

		private List<WorkflowTransition> transitions;

		public WorkflowDefinition() {
			nodes = new HashMap<String, WorkflowNode>();
			transitions = new ArrayList<WorkflowTransition>();

		}

		public Map<String, WorkflowNode> getNodes() {
			return nodes;
		}

		public void setNodes(Map<String, WorkflowNode> nodes) {
			this.nodes = nodes;
		}

		public List<WorkflowTransition> getTransitions() {
			return transitions;
		}

		public void setTransitions(List<WorkflowTransition> transitions) {
			this.transitions = transitions;
		}

	}
