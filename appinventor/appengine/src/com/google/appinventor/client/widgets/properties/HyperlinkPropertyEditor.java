package com.google.appinventor.client.widgets.properties;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;

/**
 * Property to define a constant GWT Hyperlink.
 * @author SPI-FM at UCA.
 *
 */
public class HyperlinkPropertyEditor extends PropertyEditor {
	
	private final Anchor hyperlink;
	
	public HyperlinkPropertyEditor() {
		hyperlink = new Anchor();
		hyperlink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.open(hyperlink.getText(), "_blank", "");
			}
		});
		
		initWidget(hyperlink);
	}

	@Override
	protected void updateValue() {
		hyperlink.setText(property.getValue());
	}
}
