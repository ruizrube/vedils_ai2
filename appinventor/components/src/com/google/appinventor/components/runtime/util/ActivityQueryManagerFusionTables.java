package com.google.appinventor.components.runtime.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.appinventor.components.runtime.ActivityProcessor;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;

/**
 * Class to allow queries to Google Fusion Tables.
 * @author SPI-FM at UCA
 */
public class ActivityQueryManagerFusionTables implements ActivityQueryManager {
	
	private ActivityProcessor currentActivityProcessor;
	
	private String email = "activitytracker-vedils@activitytracker-vedils.iam.gserviceaccount.com";
	private String apiKey = "e804e05b5eb3d9ac77eff415bd43fb674302968f";
	private String path = Component.ASSET_DIRECTORY + '/' + "ActivityTrackerVEDILS-e804e05b5eb3.p12";
	private FusionTablesConnection fusionTablesConnection;
	
	public ActivityQueryManagerFusionTables(ActivityProcessor currentActivityProcessor, ComponentContainer componentContainer) {
		this.currentActivityProcessor = currentActivityProcessor;
		this.fusionTablesConnection = new FusionTablesConnection(apiKey, path, email, componentContainer, true, this); 
	}
	
	@Override
	public ActivityProcessor getComponent() {
		return this.currentActivityProcessor;
	}
	
	@Override
	public void sendQuery() {
		this.fusionTablesConnection.sendQuery(generateQueryStatement());
	}
	
	@Override
	public String generateQueryStatement() {
		StringBuffer result = new StringBuffer();

		List<String> fields = currentActivityProcessor.obtainFields();
		List<String> aggregations = getColumnNameForAggregations(currentActivityProcessor.obtainAggregations());
		List<String> groupingColumns = currentActivityProcessor.obtainGroupingColumns();
		
		fields.addAll(addColumnNameForParameters());
		
		//Is not a SimpleQuery
		if(!currentActivityProcessor.obtainGroupingColumns().isEmpty() && !currentActivityProcessor.obtainAggregations().isEmpty()) {
			groupingColumns.addAll(addColumnNameForParameters());
		}

		String selectSQL = makeSelect(fields, aggregations);
		
		String fromSQL = makeFrom();
		
		String whereSQL = makeWhere();
		
		String groupBySQL = makeGroupBy(groupingColumns);
		
		result.append(selectSQL).append(fromSQL).append(whereSQL).append(groupBySQL);
		System.out.println(">>>>>>>>> Launching Query to Fusion Table >>>>>>>>>" + result.toString());

		return result.toString();
	}
	
	@Override
	public String makeSelect(List<String> fields, List<String> aggregations) {
		List<String> columns=new ArrayList<String>();
		columns.addAll(fields);
		
		for(String aggregation: aggregations) {
			if(!columns.contains(aggregation)) {
				columns.add(aggregation);
			}
		}
		
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
	
	@Override
	public String makeFrom() {
		StringBuffer result = new StringBuffer("");

		result.append(" FROM ");
		result.append(currentActivityProcessor.TableId());
		result.append(" ");

		return result.toString();
	}
	
	@Override
	public String makeWhere() {
		StringBuffer filter = new StringBuffer();
		
		//
		// ScreenID filter
		//
		
		filter.append(addPropertyFilter(currentActivityProcessor.FilterByScreenId(),
				currentActivityProcessor.getFiltersByScreenId(), "ScreenID", filter.length() > 0));
		
		filter.append(addTreeFilters(currentActivityProcessor.getFiltersByScreenId(),
				currentActivityProcessor.FilterByScreenId(), "ScreenID"));
		
		
		//
		// ComponentType filter
		//
		
		filter.append(addPropertyFilter(currentActivityProcessor.FilterByComponentType(),
				currentActivityProcessor.getFiltersByComponentType(), "ComponentType", filter.length() > 0));
		
		filter.append(addTreeFilters(currentActivityProcessor.getFiltersByComponentType(),
				currentActivityProcessor.FilterByComponentType(), "ComponentType"));
		
		
		//
		// ComponentID filter
		//
		
		filter.append(addPropertyFilter(currentActivityProcessor.FilterByComponentId(),
				currentActivityProcessor.getFiltersByComponentId(), "ComponentID", filter.length() > 0));
		
		filter.append(addTreeFilters(currentActivityProcessor.getFiltersByComponentId(),
				currentActivityProcessor.FilterByComponentId(), "ComponentID"));
		
		
		//
		// ActionType filter
		//
		
		filter.append(addPropertyFilter(currentActivityProcessor.FilterByActionType(),
				currentActivityProcessor.getFiltersByActionType(), "ActionType", filter.length() > 0));
		
		filter.append(addTreeFilters(currentActivityProcessor.getFiltersByActionType(),
				currentActivityProcessor.FilterByActionType(), "ActionType"));
		
		
		//
		// ActionID filter
		//
		
		filter.append(addPropertyFilter(currentActivityProcessor.FilterByActionId(), 
				currentActivityProcessor.getFiltersByActionId(), "ActionID", filter.length() > 0));
		
		filter.append(addTreeFilters(currentActivityProcessor.getFiltersByActionId(), 
				currentActivityProcessor.FilterByActionId(), "ActionID"));
		
		
		//
		// AppID filter
		//
		
		filter.append(addPropertyFilter(currentActivityProcessor.FilterByAppId(),
				null, "AppID", filter.length() > 0));
		
		
		//
		// UserID filter
		//
		
		filter.append(addPropertyFilter(currentActivityProcessor.FilterByUserId(),
				null, "UserID", filter.length() > 0));
		
		//
		// IP filter
		//
		
		filter.append(addPropertyFilter(currentActivityProcessor.FilterByIP(),
				null, "IP", filter.length() > 0));
		
		//
		// MAC filter
		//
		
		filter.append(addPropertyFilter(currentActivityProcessor.FilterByMAC(),
				null, "MAC", filter.length() > 0));
		
		//
		// IMEI filter
		//
		
		filter.append(addPropertyFilter(currentActivityProcessor.FilterByIMEI(),
				null, "IMEI", filter.length() > 0));
		
		//
		// Additional filter
		//
		
		if (currentActivityProcessor.AdditionalFilter() != null && !currentActivityProcessor.AdditionalFilter().equals("")) {

			if (filter.length() > 0) {
				filter.append(" AND ");
			}

			filter.append(currentActivityProcessor.AdditionalFilter());
		}
		
		if (filter.length() > 0) {
			return " WHERE " + filter;
		} else {
			return "";
		}
	}
	
	@Override
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
	
	//SPI-FM: Changed the "Like" operator to "IN" because the "OR" operator was not supported by Fusion Tables SQL.
	private String addPropertyFilter(String propertyFilter, List<String> treeFilters, String column, boolean notFirstFilter) {
		StringBuffer propertyFilterQuery = new StringBuffer();
		
		if (propertyFilter != null && !propertyFilter.equals("")) {
			
			if (notFirstFilter) {
				propertyFilterQuery.append(" AND ");
			}
			
			propertyFilterQuery.append(column + " IN (").append("'").append(propertyFilter).append("' ");
			
			if(treeFilters == null || treeFilters.isEmpty()) {
				propertyFilterQuery.append(")");
			} else {
				propertyFilterQuery.append(",");
			}
			 
		} else if(treeFilters != null && !treeFilters.isEmpty() && notFirstFilter) {
			propertyFilterQuery.append(" AND ");
		}
		
		return propertyFilterQuery.toString();
	}
	
	private String addTreeFilters(List<String> treeFilters, String propertyFilter, String column) {
		StringBuffer treeFiltersQuery = new StringBuffer();
		
		if(!treeFilters.isEmpty()) {
			
			if(propertyFilter == null || propertyFilter.equals("")) {
				treeFiltersQuery.append(column + " IN (");
			}
			
			for(String filter: treeFilters) {
				
				if(treeFilters.indexOf(filter) != 0) {
					treeFiltersQuery.append(",");
				}
				
				treeFiltersQuery.append("'").append(filter).append("'");
			}
			
			treeFiltersQuery.append(")");	
		}
		
		return treeFiltersQuery.toString();
	}
	
	private List<String> getColumnNameForAggregations(List<String> aggregations) {
		List<String> columns = new ArrayList<String>();
		List<String> numericOperators = Arrays.asList("Maximum", "Minimum", "Sum", "Average");
		
		for(String aggregator: aggregations) {
			
			if(aggregator.contains("Get:")) {
				for(String operator: numericOperators) {
					if(aggregator.contains(operator)) {
						if(!columns.contains(operator + "(" + "NumberOutputParam" + ")")) {
							columns.add(operator + "(" + "NumberOutputParam" + ")");
						}
					}
				}
			}
			
			if(aggregator.contains("Set:")) {
				for(String operator: numericOperators) {
					if(aggregator.contains(operator)) {
						if(!columns.contains(operator + "(" + "NumberInputParam1" + ")")) {
							columns.add(operator + "(" + "NumberInputParam1" + ")");
						}
					}
				}
			}
			
			if(aggregator.contains("Function:") || aggregator.contains("Event:") || aggregator.contains("User:")) {
				
				String[] aggregatorList = aggregator.split(":");
				
				for(String operator: numericOperators) {
					if(aggregator.contains(operator)) {
						if(aggregatorList[2] != "0") {
							if(!columns.contains(operator + "(" + "NumberInputParam" + aggregatorList[2] + ")")) {
								columns.add(operator + "(" + "NumberInputParam" + aggregatorList[2] + ")");
							}
						} else {
							if(!columns.contains(operator + "(" + "NumberOutputParam" + ")")) {
								columns.add(operator + "(" + "NumberOutputParam" + ")");
							}
						}
					}
				}
			}
			
			if(aggregator.contains("Count")) {
				if(!columns.contains("Count()")) {
					columns.add("Count()");
				}
			}
		}
		return columns;
	}
	
	private List<String> addColumnNameForParameters() {
		
		List<String> columns = new ArrayList<String>();
		
		if(!currentActivityProcessor.getPropertyGetterParameters().isEmpty()) {
			columns.add("TextOutputParam");
		}
		
		if(!currentActivityProcessor.getPropertySetterParameters().isEmpty()) {
			columns.add("TextInputParam1");
		}
		
		for(String parameter: currentActivityProcessor.getFunctionParameters()) {
			String[] paramFields = parameter.split(":");
			
			if(!columns.contains("TextInputParam" + paramFields[1])) {
				columns.add("TextInputParam" + paramFields[1]);
			}
		}
		
		for(String parameter: currentActivityProcessor.getEventParameters()) {
			String[] paramFields = parameter.split(":");
			
			if(!columns.contains("TextInputParam" + paramFields[1])) {
				columns.add("TextInputParam" + paramFields[1]);
			}
		}
		
		for(String parameter: currentActivityProcessor.getUserParameters()) {
			String[] paramFields = parameter.split(":");
			
			if(!columns.contains("TextInputParam" + paramFields[1])) {
				columns.add("TextInputParam" + paramFields[1]);
			}
		}
		
		return columns;
	}
}
