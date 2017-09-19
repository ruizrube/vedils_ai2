package com.google.appinventor.client.widgets.properties;

import com.google.appinventor.client.editor.youngandroid.YaFormEditor;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Property to define GWT Textbox and Hyperlink for ActivityTracker tableId property.
 * @author SPI-FM at UCA.
 *
 */
public class StringAndHyperlinkPropertyEditorForActivityTracker extends TextPropertyEditorBase {
	
	public StringAndHyperlinkPropertyEditorForActivityTracker(final YaFormEditor editor) {
		super(new TextBox(), editor);
	}
}
