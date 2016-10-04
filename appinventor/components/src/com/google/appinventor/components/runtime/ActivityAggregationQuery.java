package com.google.appinventor.components.runtime;

import java.util.ArrayList;
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
 * ActivityAnalyzerComponent
 * 
 * @author SPI-FM at UCA
 *
 */
@DesignerComponent(version = YaVersion.ACTIVITYAGGREGATIONPROCESSOR_COMPONENT_VERSION, description = "A component for analyzing activities in an application. "
		+ "Actions to analyze can be queried. ", category = ComponentCategory.VEDILSLEARNINGANALYTICS, iconName = "images/activityAggregationProcessor_icon.png", nonVisible = true)
public class ActivityAggregationQuery extends ActivityProcessor implements Component {

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
		List<String> result = new ArrayList<String>();

		System.out.println("!!!!!! las agtupamientos son:" + this.GroupBy());

		String data = this.GroupBy();
		data = data.replace("Record elements: ", "");

		if (data != null && !data.equals("")) {

			StringTokenizer stGroup = new StringTokenizer(data, " - ");
			while (stGroup.hasMoreTokens()) {
				result.add((String) stGroup.nextElement());

			}
		}
		return result;

	}

	@Override
	public List<String> obtainFields() {
		
		return obtainGroupingColumns();

	}

	@Override
	public List<String> obtainAggregations() {

		List<String> result = new ArrayList<String>();

		System.out.println("!!!!!! las metricas son:" + this.MetricsToRetrieve());

		String data = this.MetricsToRetrieve();
		if (data.equals("Nothing")) {

		} else {
			data = data.replace("Record elements: ", "");

			StringTokenizer st = new StringTokenizer(data, " - ");
			boolean first = true;

			while (st.hasMoreTokens()) {
				result.add(st.nextToken());
			}
		}
		return result;
	}

	//////////////
	// FUNCTIONS //
	//////////////

	////////////
	// EVENTS //
	////////////

}
