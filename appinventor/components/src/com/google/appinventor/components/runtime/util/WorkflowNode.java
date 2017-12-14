package com.google.appinventor.components.runtime.util;

import java.util.List;

public class WorkflowNode {
		private String id;
		private String name;
		private String nodeType;
		private String subType;
		private List arguments;

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			WorkflowNode other = (WorkflowNode) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public WorkflowNode(String nodeId, String nodeName, String nodeType, String nodeSubType, List<Object> arguments) {
			this.id = nodeId;
			if(nodeName==null || nodeName.equals("")) {
				this.name=nodeId;
			} else {
				this.name=nodeName;	
			}
			this.nodeType = nodeType;
			this.subType = nodeSubType;
			this.arguments = arguments;

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