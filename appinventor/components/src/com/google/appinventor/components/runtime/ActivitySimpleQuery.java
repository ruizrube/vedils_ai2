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
 * ActivitySimpleQuery component
 * 
 * @author SPI-FM at UCA
 *
 */
@DesignerComponent(version = YaVersion.ACTIVITYSIMPLEPROCESSOR_COMPONENT_VERSION, description = "A component for analyzing activities in an application. "
		+ "Actions to analyze can be queried. ", category = ComponentCategory.VEDILSLEARNINGANALYTICS, iconName = "images/activitySimpleProcessor_icon.png", nonVisible = true)
public class ActivitySimpleQuery extends ActivityProcessor {

	private boolean distinctResults;
	private String fieldsToRetrieve;

	public ActivitySimpleQuery(ComponentContainer componentContainer) {
		super(componentContainer.$form());

	}

	////////////////
	// PROPERTIES //
	////////////////

	/**
	 * Specifies the fields To Retrieve .
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_CHECKABLETREEFORDATA, defaultValue = "")
	@SimpleProperty(userVisible = false)
	public void FieldsToRetrieve(String fieldsToRetrieve) {
		this.fieldsToRetrieve = fieldsToRetrieve;
	}
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = false)
	public String FieldsToRetrieve() {
		return this.fieldsToRetrieve;
	}

	/**
	 * @return the DistinctResults
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public boolean DistinctResults() {
		return distinctResults;
	}

	/**
	 * @param DistinctResults
	 *            the DistinctResults to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
	@SimpleProperty(description = "Specifies if duplicated results are not allowed", userVisible = true)
	public void DistinctResults(boolean distinctResults) {
		this.distinctResults = distinctResults;
	}

	
	@Override
	public List<String> obtainAggregations(){
		return new ArrayList<String>();
	
	}

	
	@Override
	public  List<String> obtainGroupingColumns(){
		return new ArrayList<String>();
	}

	@Override
	public List<String> obtainFields() {
		
		List<String> fields = new ArrayList<String>();
		
		System.out.println("tree selected nodes: "+this.FieldsToRetrieve());

		String data = this.FieldsToRetrieve();
		if (data.equals("Nothing")) {

		} else {
			data=data.replace("Record elements: ", "");
			StringTokenizer st = new StringTokenizer(data, " - ");

			while (st.hasMoreTokens()) {
				String field = st.nextToken();
				fields.add(field);
			}
		}
		
		//Remove the special fields (if the tree has used)
		fields = processTreeFields(fields);
		return fields;
	}
	
	//////////////
	// FUNCTIONS //
	//////////////

	////////////
	// EVENTS //
	////////////
}