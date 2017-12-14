/**
 * 
 */
package com.google.appinventor.components.runtime.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

/**
 * @author ivanruizrube
 *
 */
public class WorkflowLoader {

	// We don't use namespaces
	private static final String ns = "bpmn2";

	public WorkflowDefinition readStream(InputStream stream) {
		WorkflowDefinition aux = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        factory.setNamespaceAware(true);
	        XmlPullParser parser = factory.newPullParser();
	        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
	        //parser.setFeature(XmlPullParser.FEATURE_VALIDATION, true);	        
			parser.setInput(stream, "utf-8");
			parser.nextTag();
			aux = readBPMN(parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return aux;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			//Log.d("Workflow", "Processing node [Skip] "+parser.getName() + " with name "+ parser.getAttributeValue(null, "name"));

			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}

	private WorkflowDefinition readBPMN(XmlPullParser parser) throws XmlPullParserException, IOException {
		// List processes = new ArrayList();
		WorkflowDefinition aux = null;
		Log.d("Workflow", "Reading definitions...");
		//parser.require(XmlPullParser.START_TAG, ns, "definitions");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("process")) {
				aux = readProcess(parser);
			} else {
				skip(parser);
			}
		}
		return aux;
	}

	private WorkflowDefinition readProcess(XmlPullParser parser) throws XmlPullParserException, IOException {
		// List processes = new ArrayList();
		WorkflowDefinition aux = new WorkflowDefinition();
		Log.d("Workflow", "Reading process...");

		//parser.require(XmlPullParser.START_TAG, ns, "process");
		while (parser.next() != XmlPullParser.END_TAG) {
			String name = parser.getAttributeValue(null, "name");
			String nodeType = parser.getName();
			String id = parser.getAttributeValue(null, "id");

			//Log.d("Workflow", "Processing node [MAIN] "+nodeType + " with name "+ name);

			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			List<Object> list=new ArrayList();
			
			// Starts by looking for the entry tag
			if (nodeType.equals("startEvent")) {
				Log.d("Workflow", "Reading node...startEvent");
				aux.getNodes().put(id, new WorkflowNode(id, name, "EVENT", "START", list));
				readNode(parser);
			} else if (nodeType.equals("endEvent")) {
				Log.d("Workflow", "Reading node...endEvent");
				aux.getNodes().put(id, new WorkflowNode(id, name, "EVENT", "END", list));
				readNode(parser);
			} else if (nodeType.equals("scriptTask")) {
				String text=readDocumentation(parser);
				list.add(text);
				Log.d("Workflow", "Reading node...scriptTask. " + name + ". Argument: " + text);
				aux.getNodes().put(id, new WorkflowNode(id, name, "TASK", "SCRIPT TASK",list));
				//readNode(parser);
			} else if (nodeType.equals("serviceTask")) {
				String uri=readDocumentation(parser);
				list.add(uri);
				Log.d("Workflow", "Reading node...serviceTask. " + name + ". Argument: " + uri);
				aux.getNodes().put(id, new WorkflowNode(id, name, "TASK", "SERVICE TASK",list));
				//readNode(parser);
			} else if (nodeType.equals("userTask")) {
				String parameter=readDocumentation(parser);
				list.add(parameter);
				Log.d("Workflow", "Reading node...userTask. " + name + ". Argument: " + parameter);
				aux.getNodes().put(id, new WorkflowNode(id, name,  "TASK", "USER TASK", list));
				//readNode(parser);
			} else if (nodeType.equals("manualTask")) {
				String message=readDocumentation(parser);
				list.add(message);
				Log.d("Workflow", "Reading node...manualTask. " + name + ". Argument: " + message);
				aux.getNodes().put(id, new WorkflowNode(id, name,  "TASK", "MANUAL TASK", list));
				//readNode(parser);
			} else if (nodeType.equals("task")) {
				String parameter=readDocumentation(parser);
				list.add(parameter);
				Log.d("Workflow", "Reading node...task. " + name + ". Argument: " + parameter);
				aux.getNodes().put(id, new WorkflowNode(id, name,  "TASK", "TASK", list));
				//readNode(parser);
			} else if (nodeType.equals("exclusiveGateway")) {
				Log.d("Workflow", "Reading node...exclusiveGateway");
				aux.getNodes().put(id, new WorkflowNode(id, name, "GATEWAY", "XOR", list));
				readNode(parser);
			} else if (nodeType.equals("inclusiveGateway")) {
				Log.d("Workflow", "Reading node...inclusiveGateway");
				aux.getNodes().put(id, new WorkflowNode(id, name, "GATEWAY", "OR", list));
				readNode(parser);
			} else if (nodeType.equals("parallelGateway")) {
				Log.d("Workflow", "Reading node...parallelGateway");
				aux.getNodes().put(id, new WorkflowNode(id, name, "GATEWAY", "AND", list));
				readNode(parser);
			} else if (nodeType.equals("sequenceFlow")) {
				String sourceNode = parser.getAttributeValue(null, "sourceRef");
				String targetNode = parser.getAttributeValue(null, "targetRef");
				String condition=readCondition(parser);
				
				Log.d("Workflow", "Reading node...sequenceFlow. "+sourceNode + " --> "+targetNode + ". Condition: "+condition);
				//WorkflowNode source = aux.getNodes().get(sourceNode);
				//WorkflowNode target = aux.getNodes().get(targetNode);
				aux.getTransitions().add(new WorkflowTransition(sourceNode, targetNode, condition));
				//readNode(parser);

				
			} else {
				skip(parser);
			}

		}

		return aux;
	}


	private void readNode(XmlPullParser parser) throws XmlPullParserException, IOException {
		//parser.require(XmlPullParser.START_TAG, ns, "serviceTask");
		String result = "";
		while (parser.next() != XmlPullParser.END_TAG) {
			//Log.d("Workflow", "Processing node [readNode] "+parser.getName() + " with name "+ parser.getAttributeValue(null, "name"));

			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			skip(parser);

		}

	}



	private String readDocumentation(XmlPullParser parser) throws XmlPullParserException, IOException {
		//parser.require(XmlPullParser.START_TAG, ns, "serviceTask");
		String result = "";
        while (parser.next() != XmlPullParser.END_TAG) {
			//Log.d("Workflow", "Processing node [readDocumentation] "+parser.getName() + " with name "+ parser.getAttributeValue(null, "name"));
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();

			if (name.equals("documentation")) {
	            if (parser.next() == XmlPullParser.TEXT) {
	                result = parser.getText();
	                parser.nextTag();
	            }
	            
	        } else {
	            skip(parser);
	        }
	    }
        return result;
        
	}

	
	
	private String readCondition(XmlPullParser parser) throws XmlPullParserException, IOException {
		//parser.require(XmlPullParser.START_TAG, ns, "sequenceFlow");
		String result = "";
        while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
			//Log.d("Workflow", "Processing node [readCOndition] "+parser.getName() + " with name "+ parser.getAttributeValue(null, "name"));

			if (name.equals("conditionExpression")) {
	            if (parser.next() == XmlPullParser.TEXT) {  //CDSECT
	                result = parser.getText();
	                parser.nextTag();
	            }
	            
	        } else {
	            skip(parser);
	        }
	    }
        return result;
        
	}

}
