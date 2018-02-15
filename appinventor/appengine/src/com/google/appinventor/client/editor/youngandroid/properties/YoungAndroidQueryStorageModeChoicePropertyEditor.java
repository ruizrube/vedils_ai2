package com.google.appinventor.client.editor.youngandroid.properties;

import static com.google.appinventor.client.Ode.MESSAGES;

import com.google.appinventor.client.widgets.properties.ChoicePropertyEditor;

/**
 * Property editor for storage mode of ActivitySimpleQuery y ActivityAggregationQuery.
 *
 */
public class YoungAndroidQueryStorageModeChoicePropertyEditor extends ChoicePropertyEditor {
	  
  // Query storage modes choices
  private static final Choice[] storageModes = new Choice[] {
     new Choice(MESSAGES.fusionTablesStorageMode(), "0"),
	 new Choice(MESSAGES.mongoDBStorageMode() , "1"),
  };

  public YoungAndroidQueryStorageModeChoicePropertyEditor() {
	 super(storageModes);
  }
}
