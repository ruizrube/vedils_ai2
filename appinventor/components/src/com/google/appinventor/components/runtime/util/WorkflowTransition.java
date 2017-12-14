package com.google.appinventor.components.runtime.util;
public class WorkflowTransition {
		private String source;
		private String target;
		private String condition;

		public WorkflowTransition(String source, String target, String condition) {
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

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getTarget() {
			return target;
		}

		public void setTarget(String target) {
			this.target = target;
		}

	}