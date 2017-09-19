package com.google.appinventor.components.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;

/**
 * ActivityAggregationQuery component
 * 
 * @author SPI-FM at UCA
 *
 */
@DesignerComponent(version = YaVersion.ACTIVITYAGGREGATIONPROCESSOR_COMPONENT_VERSION, description = "A component for analyzing activities in an application. "
		+ "Actions to analyze can be queried. ", category = ComponentCategory.VEDILSLEARNINGANALYTICS, iconName = "images/activityAggregationProcessor_icon.png", nonVisible = true)
public class ActivityAggregationQuery extends ActivityProcessor {

	private String groupBy;

	private String metricsToRetrieve; 

	public ActivityAggregationQuery(ComponentContainer componentContainer) {
		super(componentContainer.$form());

	}

	////////////////
	// PROPERTIES //
	////////////////

	/**
	 * Specifies the metrics To Retrieve .
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_CHECKABLETREEFORAGGREGATEDDATA, defaultValue = "")
	@SimpleProperty(userVisible = false)
	public void MetricsToRetrieve(String metricsToRetrieve) {
		this.metricsToRetrieve = metricsToRetrieve;
	}

	public String MetricsToRetrieve() {
		return this.metricsToRetrieve;
	}

	/**
	 * Specifies the groupBy.
	 * 
	 * @param groupBy
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_CHECKABLETREEFORDATA, defaultValue = "")
	@SimpleProperty(userVisible = false)
	public void GroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	/**
	 * Returns the desired data item to group the query
	 * 
	 * @return one of
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Desired data item to group the query", userVisible = false)
	public String GroupBy() {
		return this.groupBy;
	}

	@Override
	public List<String> obtainGroupingColumns() {
		List<String> groupingColumns = new ArrayList<String>();

		System.out.println("!!!!!! las agtupamientos son:" + this.GroupBy());

		String data = this.GroupBy();
		
		if(!data.equals("Nothing")) {
			data = data.replace("Record elements: ", "");

			StringTokenizer st = new StringTokenizer(data, " - ");
			
			while (st.hasMoreTokens()) {
				String groupingColumn = st.nextToken();
				groupingColumns.add(groupingColumn);
			}
		}
		
		groupingColumns = processTreeFields(groupingColumns);
		removeFieldsWithNumericAggregators();
		return groupingColumns;
	}

	@Override
	public List<String> obtainFields() {
		return obtainGroupingColumns();
	}

	@Override
	public List<String> obtainAggregations() {

		List<String> aggregations = new ArrayList<String>();

		System.out.println("!!!!!! las metricas son:" + this.MetricsToRetrieve());

		String data = this.MetricsToRetrieve();
		if(!data.equals("Nothing")) {
			data = data.replace("Record elements: ", "");

			StringTokenizer st = new StringTokenizer(data, " - ");
			
			while (st.hasMoreTokens()) {
				String groupingColumn = st.nextToken();
				String processedGroupingColumn = groupingColumn.replace(":" + CATEGORY, "");
				aggregations.add(processedGroupingColumn);
			}
		}
		
		aggregations = processTreeAggregations(aggregations);
		
		return aggregations;
	}
	
	private List<String> processTreeAggregations(List<String> aggregations) {
		List<String> operators = Arrays.asList("Count", "Maximum", "Minimum", "Sum", "Average");
		List<String> processedAggregations = new ArrayList<String>();
		
		for(String aggregation: aggregations) {
			for(String operator: operators) {
				if(aggregation.contains(operator)) {
					String field;
					if(operator.equals("Count")) {
						field = operator + "()";
					} else {
						String[] aggregationField = aggregation.split(":");
						field = aggregationField[0] + ":" + aggregationField[1] + ":" + aggregationField[3];
					}
					processedAggregations.add(field);
				}
			}
		}
		return processedAggregations;
	}
	
	public void removeFieldsWithNumericAggregators() {
		List<String> parameters = new ArrayList<String>(propertySetterParameters);
		for(String param: parameters) {
			if(containNumericAggregator(param)) {
				propertySetterParameters.remove(param);
			}
		}
		
		parameters = new ArrayList<String>(propertyGetterParameters);
		for(String param: parameters) {
			if(containNumericAggregator(param)) {
				propertyGetterParameters.remove(param);
			}
		}
		
		parameters = new ArrayList<String>(functionsParameters);
		for(String param: parameters) {
			String[] paramFields = param.split(":");
			
			if(containNumericAggregator(paramFields[0])) {
				functionsParameters.remove(param);
			}
		}
		
		parameters = new ArrayList<String>(eventsParameters);
		for(String param: parameters) {
			String[] paramFields = param.split(":");
			
			if(containNumericAggregator(paramFields[0])) {
				eventsParameters.remove(param);
			}
		}
		
		parameters = new ArrayList<String>(userParameters);
		for(String param: parameters) {
			String[] paramFields = param.split(":");
			
			if(containNumericAggregator(paramFields[0])) {
				userParameters.remove(param);
			}
		}
	}
	
	private boolean containNumericAggregator(String param) {
		return this.metricsToRetrieve.contains("Maximum(" + param + ")") ||
				this.metricsToRetrieve.contains("Minimum(" + param + ")") ||
				this.metricsToRetrieve.contains("Sum(" + param + ")") ||
				this.metricsToRetrieve.contains("Average(" + param + ")");
	}

	//////////////
	// FUNCTIONS //
	//////////////

	////////////
	// EVENTS //
	////////////
}