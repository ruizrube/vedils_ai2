package com.google.appinventor.components.runtime;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.ActivityQueryManagerMongoDB;
import com.google.appinventor.components.runtime.util.AsyncHttpRequestManager;

import android.view.View;
import gnu.mapping.SimpleSymbol;

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
	private List<Object> data;
	private String valuesTitle;
	private ActivityProcessor query;
	private String url="http://vedils.uca.es/web/graph/Table.html";
	//private String url = "http://192.168.1.22:8888/web/graph/Table.html"; //For local test
	
	private int refreshInterval;

	public DataTable(ComponentContainer container) {
		super(container);
		webviewer = new WebViewer(container);
		webviewer.WidthPercent(98);
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
	public List<Object> Data() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	@SimpleProperty(description = "Specifies the data to render in the table", userVisible = true)
	public void Data(List<Object> data) {
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
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = "5")
	@SimpleProperty(description = "Time to refresh the current graph", userVisible = true)
	public void RefreshInterval(int refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
	
	/**
	 * @return the valuesTitle separated by commas.
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String ValuesTitle() {
		return valuesTitle;
	}

	/**
	 * Specifies the valuesTitle separated by commas.
	 * 
	 * @param valuesTitle
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(description = "Values title separated by commas.", userVisible = true)
	public void ValuesTitle(String valuesTitle) {
		this.valuesTitle = valuesTitle;
	}

	//////////////
	// FUNCTIONS //
	//////////////
	
	/**
	 * Function to add a row in the current table (graph).
	 * 
	 * @param dataRow
	 */
	@SimpleFunction(description = "Function to add a row in the current table (graph)")
	public void AppendData(List<Object> dataRow) {
		System.out.println("Adding row..");
		//Update data in webviewer
		JSONObject information = new JSONObject();
		try {
			information.put("row", prepareRow(dataRow));
		} catch(JSONException e) {
			System.out.println("Error to prepare information in JSONObject");
		}
		System.out.println("informationToSend = " +information.toString());
		this.webviewer.WebViewString(information.toString());
	}

	/**
	 * Function to send the query to analyze activities.
	 * 
	 * @param actionId
	 */
	@SimpleFunction(description = "Function to refresh the current table.")
	public void Refresh() {
		System.out.println("refrescando tabla");
		
		JSONObject information = new JSONObject();
		JSONArray table = new JSONArray();
		
		try {
			if(this.Query() != null && this.Query().StorageMode() == Component.MONGODB
					&& this.Data() == null) { //automatic query MongoDB 
				new AsyncHttpRequestManager(((ActivityQueryManagerMongoDB)this.Query().getQueryManager()).URL_SERVER_QUERY, 
						new JSONObject(this.Query().getQueryManager().generateQueryStatement()), this, false).execute();
			} else {
				if(this.Query() != null && this.Query().StorageMode() == Component.FUSIONTABLES
						&& this.Data() == null) { //automatic query Google Fusion Tables
					information.put("querySQL", this.Query().getQueryManager().generateQueryStatement());
					information.put("valuesTitle", this.valuesTitle);
					information.put("refreshInterval", this.refreshInterval);
				} else if(this.Query() != null && this.Data() != null) { //manual user query (FusionTables or MongoDB)
					int columnNamesRow = 1;
					
					for(Object row: this.data) {
						if(!(row instanceof SimpleSymbol) && this.data.indexOf(row) != columnNamesRow) {
							table.put(prepareRow((List<Object>)row));
						}
					}
					
					String columnNames = "";
					boolean first = true;
					
					if(this.data.size() > 1) { //Not empty result for query
						for(Object name: (List<Object>) this.data.get(1)) {
							if(name instanceof String) { //Is a column name
								if(!((String) name).isEmpty()) {
									if(first) {
										columnNames = columnNames + name;
									} else {
										columnNames = columnNames + "," + name;
									}
									first = false;
								}
							}
						}
					}
					
					this.valuesTitle = columnNames;
					
					information.put("table", table);
					information.put("valuesTitle", this.valuesTitle);
				} else {
					if(Data() != null) {
						for(Object row: this.data) {
							if(!(row instanceof SimpleSymbol)) {
								table.put(prepareRow((List<Object>)row));
							}
						}
					}
					
					information.put("table", table);
					information.put("valuesTitle", this.valuesTitle);
				}
				
				System.out.println("informationToSend = " +information.toString());
				
				//And send the information
				this.webviewer.WebViewString(information.toString());
				
				// clear the history, since changing Home is a kind of reset
				this.webviewer.HomeUrl(url);
			}
		} catch(JSONException e) {
			System.out.println("Error to prepare information in JSONObject");
		}
	}
	
	/**
	 * Function to prepare the JSONArray with the values stored in data.
	 * @param data
	 * @return JSONArray with the values of data.
	 */
	private JSONArray prepareRow(List<Object> data) {
		JSONArray row = new JSONArray();
		for(Object param: data) {
			if(param instanceof String) {
				row.put((String) param);
			} else if(param instanceof Number) {
				row.put((Number) param);
			} else if(param instanceof Boolean) {
				row.put((Boolean) param);
			}
		}
		return row;
	}
}
