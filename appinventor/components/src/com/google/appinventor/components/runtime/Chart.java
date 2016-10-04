package com.google.appinventor.components.runtime;

import java.util.List;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesAssets;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.WebViewer.WebViewInterface;
import com.google.appinventor.components.runtime.util.EclairUtil;
import com.google.appinventor.components.runtime.util.FroyoUtil;
import com.google.appinventor.components.runtime.util.SdkLevel;

import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

	private List data;

	private ActivityProcessor query;

	private String url_base = "http://vedils.uca.es/web/graph/";

	public Chart(ComponentContainer container) {
		super(container);
		webviewer = new WebViewer(container);
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
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public int IndexForCategoryAxis() {
		return indexForCategoryAxis;
	}

	/**
	 * @param indexForCategoryAxis
	 *            the indexForCategoryAxis to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = "1")
	@SimpleProperty(description = "Specifies the column index for represent the category axis in the graph", userVisible = true)
	public void IndexForCategoryAxis(int indexForCategoryAxis) {
		this.indexForCategoryAxis = indexForCategoryAxis;
	}

	/**
	 * @return the indexForValueAxis
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public int IndexForValueAxis() {
		return indexForValueAxis;
	}

	/**
	 * @param indexForValueAxis
	 *            the indexForValueAxis to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = "2")
	@SimpleProperty(description = "Specifies the column index for represent the value axis in the graph", userVisible = true)
	public void IndexForValueAxis(int indexForValueAxis) {
		this.indexForValueAxis = indexForValueAxis;
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
	@SimpleProperty(description = "Specifies the data to render in the graph", userVisible = true)
	public void Data(List data) {
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
	@SimpleFunction(description = "Function to refresh the current graph.")
	public void Refresh() {
		System.out.println("Refrescando grafico");

		if (this.Query() != null) {
			this.webviewer.WebViewString(this.Query().generateSQLStatement());
			String url = url_base;

			if (this.ChartType() == 1) { // 1: Line
				// clear the history, since changing Home is a kind of reset
				this.webviewer.HomeUrl(url+"LineChart.html");
			} else {
				this.webviewer.HomeUrl(url+"BarChart.html");
			}

			
		}

	}

}
