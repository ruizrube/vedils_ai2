package com.google.appinventor.client.widgets.properties;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.lang.StringBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class HyperlinkPropertyEditor extends PropertyEditor implements ClickHandler {
	
	private final Anchor hyperlink;
	
	public HyperlinkPropertyEditor() {
		hyperlink = new Anchor("Hola!");
		hyperlink.addClickHandler(this);
		
		initWidget(hyperlink);
	}
	
	// Updates the property value shown in the editor
	  @Override
	  protected void updateValue() {
	    hyperlink.setText("Update value");
	  }
	 
	// ValueChangeHandler implementation

	  @Override
	  public void onClick(ClickEvent event) {
	    property.setValue("Value changue");
	    Window.alert("Click");
	    hyperlink.setText("1xZCj24xYWpj6jHWN2IK2xiErYPY7XbeHAqXVR4Bw");
	    //FusionTablesManager manager = FusionTablesManager();
	    
	  }
	
}
