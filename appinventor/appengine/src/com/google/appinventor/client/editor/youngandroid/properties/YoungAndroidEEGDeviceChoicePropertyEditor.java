/**
 * 
 */
package com.google.appinventor.client.editor.youngandroid.properties;

import static com.google.appinventor.client.Ode.MESSAGES;

import com.google.appinventor.client.widgets.properties.ChoicePropertyEditor;

/**
 * @author ruizrube
 *
 */
public class YoungAndroidEEGDeviceChoicePropertyEditor extends ChoicePropertyEditor {

	  private static final Choice[] types = new Choice[] {
	    new Choice(MESSAGES.emotivEPOCPlus(), "0"),
	    new Choice(MESSAGES.emotivInsight() , "1"),
	  };

	  public YoungAndroidEEGDeviceChoicePropertyEditor() {
	    super(types);
	  }

}

