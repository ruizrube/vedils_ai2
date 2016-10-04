package com.google.appinventor.components.runtime;

import java.util.List;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesAssets;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;

import android.view.View;
import android.webkit.WebView;

/**
 * DataTable
 * 
 * @author SPI-FM at UCA
 *
 */
@SimpleObject
@DesignerComponent(version = YaVersion.DATATABLE_COMPONENT_VERSION, description = "A component for displaying data about activities in a table. ", category = ComponentCategory.VEDILSLEARNINGANALYTICS, iconName = "images/datatable_icon.png", nonVisible = false)
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class DataTable extends AndroidViewComponent {

	////////////////
	// INTERNAL /////
	////////////////

	WebViewer webviewer;
	private List data;
	private ActivityProcessor query;
	private String url="http://vedils.uca.es/web/graph/Table.html";

	public DataTable(ComponentContainer container) {
		super(container);
		webviewer = new WebViewer(container);
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return this.webviewer.getView();
	}

	/**
	 * @return the data
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public List Data() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	@SimpleProperty(description = "Specifies the data to render in the table", userVisible = true)
	public void Data(List data) {
		this.data = data;
	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public ActivityProcessor Query() {
		return query;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ONLY_QUERY, defaultValue = "")
	@SimpleProperty(description = "Link the chart to a given data query", userVisible = true)
	public void Query(ActivityProcessor query) {
		this.query = query;
	}

	//////////////
	// FUNCTIONS //
	//////////////

	/**
	 * Function to send the query to analyze activities.
	 * 
	 * @param actionId
	 */
	@SimpleFunction(description = "Function to refresh the current table.")
	public void Refresh() {
		System.out.println("refrescando tabla");
		
		if(this.Query()!=null){
			this.webviewer.WebViewString(this.Query().generateSQLStatement());
			
			// clear the history, since changing Home is a kind of reset
			this.webviewer.HomeUrl(url);
			
		}
		
		
	}

	

}
