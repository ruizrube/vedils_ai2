package com.google.appinventor.components.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesAssets;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.PropertyTypeConstants;

/**
 * ActivityAnalyzerComponent
 * 
 * @author SPI-FM at UCA
 *
 */
@UsesAssets(fileNames = "ruizrube-cd84632c4ea8.p12, ruizrube-4718dd8c5168.json")
@UsesLibraries(libraries = "fusiontables.jar," + "google-api-client-beta.jar," + "google-api-client-android2-beta.jar,"
		+ "google-http-client-beta.jar," + "google-http-client-android2-beta.jar,"
		+ "google-http-client-android3-beta.jar," + "google-oauth-client-beta.jar," + "guava-14.0.1.jar,"
		+ "gson-2.1.jar," + "la4ai.jar")
@UsesPermissions(permissionNames = "android.permission.INTERNET, " + "android.permission.ACCESS_NETWORK_STATE,"
		+ "android.permission.WRITE_EXTERNAL_STORAGE, " + "android.permission.READ_EXTERNAL_STORAGE,"
		+ "android.permission.ACCESS_FINE_LOCATION," + "android.permission.ACCESS_COARSE_LOCATION,"
		+ "android.permission.ACCESS_MOCK_LOCATION," + "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS,"
		+ "android.permission.READ_PHONE_STATE")
@SimpleObject
public abstract class ActivityProcessor extends AndroidNonvisibleComponent implements Component {

	private String tableId;

	private String additionalFilter;
	private String filterByUserId;
	private String filterByIP;
	private String filterByMAC;
	private String filterByIMEI;
	private String filterByAppId;
	private String filterByScreenId;
	private String filterByComponentType;
	private String filterByComponentId;
	private String filterByActionType;
	private String filterByActionId;


	public ActivityProcessor(ComponentContainer componentContainer) {
		super(componentContainer.$form());

	}

	@Override
	public void ActivitiesToTrack(String activitiesNames) {
	}

	////////////////
	// PROPERTIES //
	////////////////

	/**
	 * Specifies the additionalFilter of the query.
	 * 
	 * @param additionalFilter
	 */
	@SimpleProperty
	public void AdditionalFilter(String additionalFilter) {
		this.additionalFilter = additionalFilter;

	}

	/**
	 * Return the additionalFilter of the query.
	 * 
	 * Return additionalFilter
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the additionalFilter of the query.", userVisible = true)
	public String AdditionalFilter() {
		return this.additionalFilter;
	}

	/**
	 * Specifies the filterByIP of the query.
	 * 
	 * @param filterByIP
	 */
	@SimpleProperty
	public void FilterByIP(String filterByIP) {
		this.filterByIP = filterByIP;

	}

	/**
	 * Return the filterByIP of the query.
	 * 
	 * Return filterByIP
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByIP of the query.", userVisible = true)
	public String FilterByIP() {
		return this.filterByIP;
	}


	/**
	 * Specifies the filterByMAC of the query.
	 * 
	 * @param filterByMAC
	 */
	@SimpleProperty
	public void FilterByMAC(String filterByMAC) {
		this.filterByMAC = filterByMAC;

	}

	/**
	 * Return the filterByMAC of the query.
	 * 
	 * Return filterByMAC
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByMAC of the query.", userVisible = true)
	public String FilterByMAC() {
		return this.filterByMAC;
	}

	/**
	 * Specifies the filterByIMEI of the query.
	 * 
	 * @param filterByIMEI
	 */
	@SimpleProperty
	public void FilterByIMEI(String filterByIMEI) {
		this.filterByIMEI = filterByIMEI;

	}

	/**
	 * Return the filterByIMEI of the query.
	 * 
	 * Return filterByIMEI
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByIMEI of the query.", userVisible = true)
	public String FilterByIMEI() {
		return this.filterByIMEI;
	}

	
	/**
	 * Specifies the filterByUserId of the query.
	 * 
	 * @param filterByUserId
	 */
	@SimpleProperty
	public void FilterByUserId(String filterByUserId) {
		this.filterByUserId = filterByUserId;

	}

	/**
	 * Return the filterByUserId of the query.
	 * 
	 * Return filterByUserId
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByUserId of the query.", userVisible = true)
	public String FilterByUserId() {
		return this.filterByUserId;
	}

	/**
	 * Specifies the filterByAppId of the query.
	 * 
	 * @param filterByAppId
	 */
	@SimpleProperty
	public void FilterByAppId(String filterByAppId) {
		this.filterByAppId = filterByAppId;

	}

	/**
	 * Return the filterByAppId of the query.
	 * 
	 * Return filterByAppId
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByAppId of the query.", userVisible = true)
	public String FilterByAppId() {
		return this.filterByAppId;
	}

	/**
	 * Specifies the filterByScreenId of the query.
	 * 
	 * @param filterByScreenId
	 */
	@SimpleProperty
	public void FilterByScreenId(String filterByScreenId) {
		this.filterByScreenId = filterByScreenId;

	}

	/**
	 * Return the filterByScreenId of the query.
	 * 
	 * Return filterByScreenId
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByScreenId of the query.", userVisible = true)
	public String FilterByScreenId() {
		return this.filterByScreenId;
	}

	/**
	 * Specifies the filterByComponentType of the query.
	 * 
	 * @param filterByComponentType
	 */
	@SimpleProperty
	public void FilterByComponentType(String filterByComponentType) {
		this.filterByComponentType = filterByComponentType;

	}

	/**
	 * Return the filterByComponentType of the query.
	 * 
	 * Return filterByComponentType
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByComponentType of the query.", userVisible = true)
	public String FilterByComponentType() {
		return this.filterByComponentType;
	}

	/**
	 * Specifies the filterByComponentId of the query.
	 * 
	 * @param filterByComponentId
	 */
	@SimpleProperty
	public void FilterByComponentId(String filterByComponentId) {
		this.filterByComponentId = filterByComponentId;

	}

	/**
	 * Return the filterByComponentId of the query.
	 * 
	 * Return filterByComponentId
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByComponentId of the query.", userVisible = true)
	public String FilterByComponentId() {
		return this.filterByComponentId;
	}

	/**
	 * Specifies the filterByActionType of the query.
	 * 
	 * @param filterByActionType
	 */
	@SimpleProperty
	public void FilterByActionType(String filterByActionType) {
		this.filterByActionType = filterByActionType;

	}

	/**
	 * Return the filterByActionType of the query.
	 * 
	 * Return filterByActionType
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByActionType of the query.", userVisible = true)
	public String FilterByActionType() {
		return this.filterByActionType;
	}

	/**
	 * Specifies the filterByActionId of the query.
	 * 
	 * @param filterByActionId
	 */
	@SimpleProperty
	public void FilterByActionId(String filterByActionId) {
		this.filterByActionId = filterByActionId;

	}

	/**
	 * Return the filterByActionId of the query.
	 * 
	 * Return filterByActionId
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByActionId of the query.", userVisible = true)
	public String FilterByActionId() {
		return this.filterByActionId;
	}

	/**
	 * Specifies the tableId of Fusion Table (Google) to establish the
	 * connection.
	 * 
	 * @param tableId
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty
	public void TableId(String newtableId) {
		tableId = newtableId;
	}

	/**
	 * Returns the id of the current Fusion Table.
	 * 
	 * @return tableId
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the id of the current Fusion Table", userVisible = true)
	public String TableId() {
		return tableId;
	}

	//////////////
	// FUNCTIONS //
	//////////////

	/**
	 * Function to send the query to analyze activities.
	 * 
	 * @param actionId
	 */
	@SimpleFunction(description = "Function to send the query to analyze activities.")
	public void SendQuery() {
		System.out.println("enviando datos");
	}

	////////////
	// EVENTS //
	////////////

	/**
	 * Event to be raised after query results are catched
	 */
	@SimpleEvent(description = "Event to be raised after query results are catched", userVisible = true)
	public void DataReceived(List data) {
		EventDispatcher.dispatchEvent(this, "DataReceived", data);
	}

	public String generateSQLStatement() {
		StringBuffer result = new StringBuffer();

		List<String> fields = obtainFields();
		List<String> aggregations = obtainAggregations();
		List<String> groupingColumns = obtainGroupingColumns();

		String selectSQL = makeSelect(fields, aggregations);
		
		String fromSQL = makeFrom(this.TableId());
		
		String whereSQL = makeWhere();
		
		String groupBySQL = makeGroupBy(groupingColumns);
		
		result.append(selectSQL).append(fromSQL).append(whereSQL).append(groupBySQL);
		System.out.println(">>>>>>>>> Launching Query to Fusion Table >>>>>>>>>" + result.toString());

		return result.toString();
	}

	public abstract List<String> obtainAggregations();

	public abstract List<String> obtainFields();

	public abstract  List<String> obtainGroupingColumns();

	
	public String makeSelect(List<String> fields, List<String> aggregations) {

		List<String> columns=new ArrayList<String>();
		columns.addAll(fields);
		columns.addAll(aggregations);
		
		StringBuffer result = new StringBuffer("");
		result.append("SELECT ");

		boolean first = true;

		for (String col : columns) {
			if (!first) {
				result.append(", ");
			}
			result.append(col);
			first = false;
		}
		return result.toString();
	}

	private String makeFrom(String tableId) {
		StringBuffer result = new StringBuffer("");

		result.append(" FROM ");
		result.append(tableId);
		result.append(" ");

		return result.toString();
	}

	private String makeWhere() {
		StringBuffer filter = new StringBuffer();

		if (this.FilterByActionId() != null && !this.FilterByActionId().equals("")) {
			filter.append("ActionID like '").append(this.FilterByActionId()).append("' ");
		}

		if (this.FilterByActionType() != null && !this.FilterByActionType().equals("")) {

			if (filter.length() > 0) {
				filter.append(" AND ");
			}

			filter.append("ActionType like '").append(this.FilterByActionType()).append("' ");
		}

		if (this.FilterByAppId() != null && !this.FilterByAppId().equals("")) {

			if (filter.length() > 0) {
				filter.append(" AND ");
			}

			filter.append("AppID like '").append(this.FilterByAppId()).append("' ");
		}

		if (this.FilterByComponentId() != null && !this.FilterByComponentId().equals("")) {

			if (filter.length() > 0) {
				filter.append(" AND ");
			}

			filter.append("ComponentID like '").append(this.FilterByComponentId()).append("' ");
		}

		if (this.FilterByComponentType() != null && !this.FilterByComponentType().equals("")) {

			if (filter.length() > 0) {
				filter.append(" AND ");
			}

			filter.append("ComponentType like '").append(this.FilterByComponentType()).append("' ");
		}

		if (this.FilterByScreenId() != null && !this.FilterByScreenId().equals("")) {

			if (filter.length() > 0) {
				filter.append(" AND ");
			}

			filter.append("ScreenID like '").append(this.FilterByScreenId()).append("' ");
		}

		if (this.FilterByUserId() != null && !this.FilterByUserId().equals("")) {

			if (filter.length() > 0) {
				filter.append(" AND ");
			}

			filter.append("UserID like '").append(this.FilterByUserId()).append("' ");
		}

		if (this.FilterByIP() != null && !this.FilterByIP().equals("")) {

			if (filter.length() > 0) {
				filter.append(" AND ");
			}

			filter.append("IP like '").append(this.FilterByIP()).append("' ");
		}

		if (this.FilterByMAC() != null && !this.FilterByMAC().equals("")) {

			if (filter.length() > 0) {
				filter.append(" AND ");
			}

			filter.append("MAC like '").append(this.FilterByMAC()).append("' ");
		}
		
		if (this.FilterByIMEI() != null && !this.FilterByIMEI().equals("")) {

			if (filter.length() > 0) {
				filter.append(" AND ");
			}

			filter.append("IMEI like '").append(this.FilterByIMEI()).append("' ");
		}
		
		if (this.AdditionalFilter() != null && !this.AdditionalFilter().equals("")) {

			if (filter.length() > 0) {
				filter.append(" AND ");
			}

			filter.append(this.AdditionalFilter());
		}

		
		if (filter.length() > 0) {
			return " WHERE " + filter;
		} else {
			return "";
		}

	}

	public String makeGroupBy(List<String> groupByColumns) {

		StringBuffer result = new StringBuffer("");

		if (groupByColumns.size() > 0) {

			result.append(" GROUP BY ");
			boolean first = true;

			for (String col : groupByColumns) {
				if (!first) {
					result.append(", ");
				}
				result.append(col);
				first = false;

			}
		}
		return result.toString();
	}

}
