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
 * ActivityDataTable
 * 
 * @author SPI-FM at UCA
 *
 */
@SimpleObject
@DesignerComponent(version = YaVersion.CHART_COMPONENT_VERSION, description = "A component for displaying data about activities in a table. ", category = ComponentCategory.VEDILSLEARNINGANALYTICS, iconName = "images/chart_icon.png", nonVisible = false)
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class Chart extends AndroidViewComponent {

	////////////////
	// INTERNAL /////
	////////////////

	WebViewer webviewer;

	private String categoryAxisTitle;
	private String valueAxisTitle;

	private int indexForCategoryAxis = 1;
	private int indexForValueAxis = 2;
	private int chartType = 0;
	private int refreshInterval = 5;

	private List<Object> data;
	private String valuesTitle;

	private ActivityProcessor query;

	//private String url_base = "http://vedils.uca.es/web/graph/";
	private String url_base = "http://192.168.1.22:8888/web/graph/"; //For local test	 

	public Chart(ComponentContainer container) {
		super(container);
		webviewer = new WebViewer(container);
		webviewer.WidthPercent(100);
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return this.webviewer.getView();
	}

	////////////////
	// PROPERTIES //
	////////////////

	/**
	 * @return the indexForCategoryAxis
	 */
	@Deprecated
	//@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = false)
	public int IndexForCategoryAxis() {
		return indexForCategoryAxis;
	}

	/**
	 * @param indexForCategoryAxis
	 *            the indexForCategoryAxis to set
	 */
	@Deprecated
	//@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = "1")
	//@SimpleProperty(description = "Specifies the column index for represent the category axis in the graph", userVisible = true)
	public void IndexForCategoryAxis(int indexForCategoryAxis) {
		this.indexForCategoryAxis = indexForCategoryAxis;
	}

	/**
	 * @return the indexForValueAxis
	 */
	@Deprecated
	//@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public int IndexForValueAxis() {
		return indexForValueAxis;
	}

	/**
	 * @param indexForValueAxis
	 *            the indexForValueAxis to set
	 */
	@Deprecated
	//@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = "2")
	//@SimpleProperty(description = "Specifies the column index for represent the value axis in the graph", userVisible = true)
	public void IndexForValueAxis(int indexForValueAxis) {
		this.indexForValueAxis = indexForValueAxis;
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
	@SimpleProperty(description = "Specifies the data to render in the graph", userVisible = true)
	public void Data(List<Object> data) {
		this.data = data;
	}

	/**
	 * Specifies the chartType.
	 * 
	 * @param chartType
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_CHARTTYPE, defaultValue = Component.BAR + "")
	@SimpleProperty(userVisible = false)
	public void ChartType(int chartType) {
		this.chartType = chartType;
	}

	/**
	 * Returns the aggregation operator for the value argument 1
	 * 
	 * @return one of {@link Component#BAR}, {@link Component#LINE} or
	 *         {@link Component#PIE}
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Chart type", userVisible = false)
	public int ChartType() {
		return this.chartType;
	}

	/**
	 * @return the categoryAxisTitle
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String CategoryAxisTitle() {
		return categoryAxisTitle;
	}

	/**
	 * Specifies the categoryAxisTitle.
	 * 
	 * @param categoryAxisTitle
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(userVisible = false)
	public void CategoryAxisTitle(String categoryAxisTitle) {
		this.categoryAxisTitle = categoryAxisTitle;
	}

	/**
	 * @return the valueAxisTitle
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String ValueAxisTitle() {
		return valueAxisTitle;
	}

	/**
	 * Specifies the valueAxisTitle.
	 * 
	 * @param valueAxisTitle
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(userVisible = false)
	public void ValueAxisTitle(String valueAxisTitle) {
		this.valueAxisTitle = valueAxisTitle;
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
	@SimpleProperty(description = "Set the values title separated by commas.", userVisible = true)
	public void ValuesTitle(String valuesTitle) {
		this.valuesTitle = valuesTitle;
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

	//////////////
	// FUNCTIONS //
	//////////////
	
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
	@SimpleFunction(description = "Function to refresh the current graph.")
	public void Refresh() {
		System.out.println("Refrescando grafico");
		
		String url = url_base;
		
		JSONObject information = new JSONObject();
		JSONArray table = new JSONArray();
		
		try {
			if(this.Query() != null && this.Query().StorageMode() == Component.MONGODB
					&& this.Data() == null) { //automatic query MongoDB 
				new AsyncHttpRequestManager(((ActivityQueryManagerMongoDB)this.Query().getQueryManager()).URL_SERVER_QUERY, 
						"POST", new JSONObject(this.Query().getQueryManager().generateQueryStatement()), this, false).execute();
			} else {
				if (this.Query() != null && this.Query().StorageMode() == Component.FUSIONTABLES
						&& this.Data() == null) { //Prepare JSONObject to send the information (SQL option).	
					information.put("querySQL", this.Query().getQueryManager().generateQueryStatement());
					information.put("categoryAxisTitle", this.categoryAxisTitle);
					information.put("valueAxisTitle", this.valueAxisTitle);
					information.put("valuesTitle", this.valuesTitle);
					information.put("indexForCategory", this.indexForCategoryAxis);
					information.put("indexForValue", this.indexForValueAxis);
					information.put("refreshInterval", this.refreshInterval);
				} else { //Prepare JSONObject to send the information (Data list option).
					int columnNamesRow = 1;
					
					if(Data() != null) {
						
						System.out.println("El size es (data): " + this.data.size());
						
						if(this.data.size() > 1) {
							
							for(Object row: this.data) {
								if(!(row instanceof SimpleSymbol) && this.data.indexOf(row) != columnNamesRow) {
									table.put(prepareRow((List<Object>)row));
								}
							}
							
							if(this.valuesTitle == null) {
								String columnNames = "";
								boolean first = true;
								
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
								
								this.valuesTitle = columnNames;
							}
							
						} else if(this.data.size() > 0) {
							
							for(Object row: this.data) {
								if(!(row instanceof SimpleSymbol)) {
									table.put(prepareRow((List<Object>)row));
								}
							}
							
							if(this.valuesTitle == null) {
								String columnNames = "";
								boolean first = true;
								
								for(int i=0; i<((List<Object>) this.data.get(1)).size(); i++) {
									if(first) {
										columnNames = columnNames + "param" + i;
									} else {
										columnNames = columnNames + "," + "param" + i;
									}
									first = false;
								}
								
								this.valuesTitle = columnNames;
							}
						}
					}
					
					information.put("table", table);
					information.put("categoryAxisTitle", this.categoryAxisTitle);
					information.put("valueAxisTitle", this.valueAxisTitle);
					information.put("valuesTitle", this.valuesTitle);
					information.put("indexForCategory", this.indexForCategoryAxis);
					information.put("indexForValue", this.indexForValueAxis);
				}
				
				System.out.println("informationToSend = " +information.toString());
				
				//And send the information
				this.webviewer.WebViewString(information.toString());
				
				if (this.ChartType() == 1) { // 1: Line
					// clear the history, since changing Home is a kind of reset
					this.webviewer.HomeUrl(url+"LineChart.html");
				} else if(this.ChartType() == 2) { // 2: Column
					this.webviewer.HomeUrl(url+"ColumnChart.html");
				} else {
					this.webviewer.HomeUrl(url+"BarChart.html");
				}
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
