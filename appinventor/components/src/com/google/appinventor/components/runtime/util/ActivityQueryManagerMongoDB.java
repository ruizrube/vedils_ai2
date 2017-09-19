package com.google.appinventor.components.runtime.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.appinventor.components.runtime.ActivityProcessor;

public class ActivityQueryManagerMongoDB implements ActivityQueryManager {
	
	private ActivityProcessor currentActivityProcessor;
	public String URL_SERVER_QUERY = "http://vedilsanalytics.uca.es:8080/AnalyticsWSForAppInventor/MongoDBClient/query";
	
	public ActivityQueryManagerMongoDB(ActivityProcessor currentActivityProcessor) {
		this.currentActivityProcessor = currentActivityProcessor;
	}

	@Override
	public ActivityProcessor getComponent() {
		return this.currentActivityProcessor;
	}

	@Override
	public void sendQuery() {
		JSONObject information = new JSONObject();
		try {
			information = new JSONObject(generateQueryStatement());
		} catch(Exception e) {
			e.printStackTrace();
		}
		new AsyncHttpRequestManager(URL_SERVER_QUERY, information, this, false).execute();
	}

	@Override
	public String generateQueryStatement() {
		JSONObject result = new JSONObject();

		List<String> fields = currentActivityProcessor.obtainFields();
		List<String> aggregations = getColumnNameForAggregations(currentActivityProcessor.obtainAggregations());
		List<String> groupingColumns = currentActivityProcessor.obtainGroupingColumns();
		
		fields.addAll(currentActivityProcessor.getPropertyGetterParameters());
		
		//Skip repetitions (set and get parameter has the same name)
		for(String setParameter: currentActivityProcessor.getPropertySetterParameters()) {
			if(!fields.contains(setParameter)) {
				fields.add(setParameter);
			}
		}
		
		fields.addAll(addColumnNameForParameters());
		
		//Is not a SimpleQuery
		if(!currentActivityProcessor.obtainGroupingColumns().isEmpty() && !currentActivityProcessor.obtainAggregations().isEmpty()) {
			groupingColumns.addAll(currentActivityProcessor.getPropertyGetterParameters());
			
			//Skip repetitions (set and get parameter has the same name)
			for(String setParameter: currentActivityProcessor.getPropertySetterParameters()) {
				if(!groupingColumns.contains(setParameter)) {
					groupingColumns.add(setParameter);
				}
			}
			
			groupingColumns.addAll(addColumnNameForParameters());
		}
		
		try {
			result.put("database", this.currentActivityProcessor.componentContainer.$context().getApplicationInfo().packageName);
			result.put("collection", makeFrom());
			result.put("selectFields", makeSelect(fields, aggregations));
			result.put("filterFields", makeWhere());
			result.put("groupByFields", makeGroupBy(groupingColumns));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(">>>>>>>>> Launching Query to MongoDB >>>>>>>>>" + result.toString());

		return result.toString();
	}

	@Override
	public String makeSelect(List<String> fields, List<String> aggregations) {
		List<String> columns=new ArrayList<String>();
		columns.addAll(fields);
		columns.addAll(aggregations);
		
		StringBuffer result = new StringBuffer("");
		result.append("{_id:0");

		for (String col : columns) {
			result.append(", ");
			result.append(col).append(":1");
		}
		
		result.append("}");
		
		return result.toString();
	}

	@Override
	public String makeFrom() {
		return currentActivityProcessor.TableId();
	}

	@Override
	public String makeWhere() {
		StringBuffer filter = new StringBuffer();

		if (currentActivityProcessor.FilterByActionId() != null && !currentActivityProcessor.FilterByActionId().equals("")) {
			filter.append("'ActionID':'").append(currentActivityProcessor.FilterByActionId()).append("' ");
		}
		
		filter.append(addTreeFilters(currentActivityProcessor.getFiltersByActionId(), currentActivityProcessor.FilterByActionId(), "ActionID"));

		if (currentActivityProcessor.FilterByActionType() != null && !currentActivityProcessor.FilterByActionType().equals("")) {

			if (filter.length() > 0) {
				filter.append(",");
			}

			filter.append("'ActionType':'").append(currentActivityProcessor.FilterByActionType()).append("' ");
		}
		
		filter.append(addTreeFilters(currentActivityProcessor.getFiltersByActionType(), currentActivityProcessor.FilterByActionType(), "ActionType"));

		if (currentActivityProcessor.FilterByAppId() != null && !currentActivityProcessor.FilterByAppId().equals("")) {

			if (filter.length() > 0) {
				filter.append(",");
			}

			filter.append("'AppID':'").append(currentActivityProcessor.FilterByAppId()).append("' ");
		}

		if (currentActivityProcessor.FilterByComponentId() != null && !currentActivityProcessor.FilterByComponentId().equals("")) {

			if (filter.length() > 0) {
				filter.append(",");
			}

			filter.append("'ComponentID':'").append(currentActivityProcessor.FilterByComponentId()).append("' ");
		}
		
		filter.append(addTreeFilters(currentActivityProcessor.getFiltersByComponentId(), currentActivityProcessor.FilterByComponentId(), "ComponentID"));

		if (currentActivityProcessor.FilterByComponentType() != null && !currentActivityProcessor.FilterByComponentType().equals("")) {

			if (filter.length() > 0) {
				filter.append(",");
			}

			filter.append("'ComponentType':'").append(currentActivityProcessor.FilterByComponentType()).append("' ");
		}
		
		filter.append(addTreeFilters(currentActivityProcessor.getFiltersByComponentType(), currentActivityProcessor.FilterByComponentType(), "ComponentType"));

		if (currentActivityProcessor.FilterByScreenId() != null && !currentActivityProcessor.FilterByScreenId().equals("")) {

			if (filter.length() > 0) {
				filter.append(",");
			}

			filter.append("'ScreenID':'").append(currentActivityProcessor.FilterByScreenId()).append("' ");
		}
		
		filter.append(addTreeFilters(currentActivityProcessor.getFiltersByScreenId(), currentActivityProcessor.FilterByScreenId(), "ScreenID"));

		if (currentActivityProcessor.FilterByUserId() != null && !currentActivityProcessor.FilterByUserId().equals("")) {

			if (filter.length() > 0) {
				filter.append(",");
			}

			filter.append("'UserID':'").append(currentActivityProcessor.FilterByUserId()).append("' ");
		}

		if (currentActivityProcessor.FilterByIP() != null && !currentActivityProcessor.FilterByIP().equals("")) {

			if (filter.length() > 0) {
				filter.append(",");
			}

			filter.append("'IP':'").append(currentActivityProcessor.FilterByIP()).append("' ");
		}

		if (currentActivityProcessor.FilterByMAC() != null && !currentActivityProcessor.FilterByMAC().equals("")) {

			if (filter.length() > 0) {
				filter.append(",");
			}

			filter.append("'MAC':'").append(currentActivityProcessor.FilterByMAC()).append("' ");
		}
		
		if (currentActivityProcessor.FilterByIMEI() != null && !currentActivityProcessor.FilterByIMEI().equals("")) {

			if (filter.length() > 0) {
				filter.append(",");
			}

			filter.append("'IMEI':'").append(currentActivityProcessor.FilterByIMEI()).append("' ");
		}
		
		if (currentActivityProcessor.AdditionalFilter() != null && !currentActivityProcessor.AdditionalFilter().equals("")) {

			if (filter.length() > 0) {
				filter.append(",");
			}

			filter.append(currentActivityProcessor.AdditionalFilter());
		}
		
		if (filter.length() > 0) {
			return "{" + filter + "}";
		} else {
			return "{}";
		}
	}
	
	private String addTreeFilters(List<String> treeFilters, String propertyFilter, String column) {
		StringBuffer treeFiltersQuery = new StringBuffer();
		
		if(!treeFilters.isEmpty()) {
			if(propertyFilter != null && !propertyFilter.isEmpty()) {
				treeFiltersQuery.append(",");
			}
			
			for(String filter: treeFilters) {
				
				if(treeFilters.indexOf(filter) != 0) {
					treeFiltersQuery.append(",");
				}
				
				treeFiltersQuery.append("'" + column + "':'").append(filter).append("' ");
			}
			treeFiltersQuery.append(",");
		}
		
		return treeFiltersQuery.toString();
	}

	@Override
	public String makeGroupBy(List<String> groupByColumns) {
		StringBuffer result = new StringBuffer("");

		if (groupByColumns.size() > 0) {
			
			boolean first = true;

			for (String col : groupByColumns) {
				if (!first) {
					result.append(",");
				}
				result.append(col);
				first = false;

			}
		}
		return result.toString();
	}
	
	private List<String> getColumnNameForAggregations(List<String> aggregations) {
		List<String> columns = new ArrayList<String>();
		
		for(String aggregator: aggregations) {			
			if(aggregator.contains("Count")) {
				if(!columns.contains("Count()")) {
					columns.add("Count()");
				}
			} else {
				String[] aggregatorList = aggregator.split(":");
				columns.add(aggregatorList[1]);
			}
		}
		return columns;
	}
	
	private List<String> addColumnNameForParameters() {
		
		List<String> columns = new ArrayList<String>();
		
		for(String parameter: currentActivityProcessor.getFunctionParameters()) {
			String[] paramFields = parameter.split(":");
			
			if(!columns.contains(paramFields[0])) {
				columns.add(paramFields[0]);
			}
		}
		
		for(String parameter: currentActivityProcessor.getEventParameters()) {
			String[] paramFields = parameter.split(":");
			
			if(!columns.contains(paramFields[0])) {
				columns.add(paramFields[0]);
			}
		}
		
		for(String parameter: currentActivityProcessor.getUserParameters()) {
			String[] paramFields = parameter.split(":");
			
			if(!columns.contains(paramFields[0])) {
				columns.add(paramFields[0]);
			}
		}
		return columns;
	}
}