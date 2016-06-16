package com.google.appinventor.client.widgets.properties;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;


/**
 * Property to define GWT Hyperlink 
 * @author Taty
 *
 */
public class HyperlinkPropertyEditor extends PropertyEditor {
	
	private final Anchor hyperlink;
	
	public HyperlinkPropertyEditor(final String URL) {
		hyperlink = new Anchor(URL,"");
		hyperlink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.open(URL, "_blank", "");
			}
		});
		initWidget(hyperlink);
	}

	@Override
	protected void updateValue() {}
}
