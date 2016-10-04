/**
 * 
 */
package com.google.appinventor.client.editor.youngandroid.properties;

import static com.google.appinventor.client.Ode.MESSAGES;

import com.google.appinventor.client.widgets.properties.ChoicePropertyEditor;
import com.google.appinventor.client.widgets.properties.ChoicePropertyEditor.Choice;

/**
 * @author ruizrube
 *
 */
public class YoungAndroidColumntToAggregateChoicePropertyEditor extends ChoicePropertyEditor {

	  // Synchronization modes choices
	  private static final Choice[] columns = new Choice[] {
	    new Choice(MESSAGES.userIDColumn(), "0"),
	    new Choice(MESSAGES.userIPColumn(), "1"),
	    new Choice(MESSAGES.userIMEIColumn(), "2"),
	    new Choice(MESSAGES.appIPColumn(), "3"),
	    new Choice(MESSAGES.screenIDColumn(), "4"),
	    new Choice(MESSAGES.componentIDColumn(), "5"),
	    new Choice(MESSAGES.componentTypeColumn(), "6"),
	    new Choice(MESSAGES.actionIDColumn(), "7"),
	    new Choice(MESSAGES.actionTypeColumn(), "8"),
	    
	  
	  };

	  public YoungAndroidColumntToAggregateChoicePropertyEditor() {
	    super(columns);
	  }



}
