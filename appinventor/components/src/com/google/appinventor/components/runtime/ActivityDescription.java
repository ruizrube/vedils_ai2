package com.google.appinventor.components.runtime;

import java.util.List;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;

@SimpleObject
@DesignerComponent(version = YaVersion.ACTIVITYDESCRIPTION_COMPONENT_VERSION, 
description = "A component for defining the activity data created on the application. ", 
category = ComponentCategory.VEDILSLEARNINGANALYTICS, iconName = "images/activityDescription_icon.png", nonVisible= true)
public class ActivityDescription extends AndroidNonvisibleComponent implements Component {

	private String id;
	private boolean completion;
	private String duration;
	private boolean success;
	private float maxScore;
	private float minScore;
	private float rawScore;
	private float scaledScore;
	private List<Object> extensions;
	private List<Object> resultExtensions;
	
	public ActivityDescription(ComponentContainer componentContainer) {
		super(componentContainer.$form());
		this.id = "";
		this.completion = false;
		this.duration = "";
		this.success = false;
		this.maxScore = 0;
		this.minScore = 0;
		this.rawScore = 0;
		this.scaledScore = 0;
	}

	/**
	 * Specifies the id of the activity created on the application.
	 * 
	 * @param id
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "")
	@SimpleProperty
	public void Id(String id) {
		this.id = id;
	}
	
	
	/**
	 * Return the id of the activity created on the application.
	 * 
	 * Return id
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the name of the activity created on the application.", userVisible = true)
	public String Id() {
		return this.id;
	}
	
	/**
	 * Specifies if the activity has been completed.
	 * 
	 * @param completion
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
		      defaultValue = "false")
	@SimpleProperty
	public void Completion(boolean completion) {
		this.completion = completion;
	}
	
	
	/**
	 * Return if the activity has been completed.
	 * 
	 * Return completion
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return if the activity has been completed.", userVisible = true)
	public boolean Completion() {
		return this.completion;
	}
	
	/**
	 * Specifies the activity duration with ISO 8601 standard format.
	 * 
	 * @param duration
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "")
	@SimpleProperty
	public void Duration(String duration) {
		this.duration = duration;
	}
	
	
	/**
	 * Return the activity duration with ISO 8601 standard format.
	 * 
	 * Return completion
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the activity duration with ISO 8601 standard format.", userVisible = true)
	public String Duration() {
		return this.duration;
	}
	
	
	/**
	 *Function to make duration format
	 * 
	 * @param years, months, days, hours, minutes, seconds
	 */
	@SimpleFunction(description="Function to make duration format with ISO 8601 standard.")
	public String MakeDurationFormat(int years, int months, int days, double hours, double minutes, double seconds) {
		String duration = "P";
		
		if(years != 0) {
			duration += years + "Y";
		}
		
		if(months != 0) {
			duration += months + "M";
		}
		
		if(days != 0) {
			duration += days + "D";
		}
		
		duration += "T";
		
		if(hours != 0) {
			duration += hours + "H";
		}
		
		if(minutes != 0) {
			duration += minutes + "M";
		}
		
		if(seconds != 0) {
			duration += seconds + "S";
		}
		
		return duration;
	}
	
	
	/**
	 * Specifies if the activity is done with success.
	 * 
	 * @param success
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
		      defaultValue = "false")
	@SimpleProperty
	public void Success(boolean success) {
		this.success = success;
	}
	
	
	/**
	 * Return if the activity is done with success.
	 * 
	 * Return success
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return if the activity is done with success.", userVisible = true)
	public boolean Success() {
		return this.success;
	}
	
	/**
	 * Specifies the activity max score.
	 * 
	 * @param maxScore
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT,
		      defaultValue = "0")
	@SimpleProperty
	public void MaxScore(float maxScore) {
		this.maxScore = maxScore;
	}
	
	
	/**
	 * Return the activity max score.
	 * 
	 * Return maxScore
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the activity max score.", userVisible = true)
	public float MaxScore() {
		return this.maxScore;
	}
	
	/**
	 * Specifies the activity min score.
	 * 
	 * @param minScore
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT,
		      defaultValue = "0")
	@SimpleProperty
	public void MinScore(float minScore) {
		this.minScore = minScore;
	}
	
	
	/**
	 * Return the activity min score.
	 * 
	 * Return minScore
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the activity min score.", userVisible = true)
	public float MinScore() {
		return this.minScore;
	}
	
	/**
	 * Specifies the activity raw score.
	 * 
	 * @param rawScore
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT,
		      defaultValue = "0")
	@SimpleProperty
	public void RawScore(float rawScore) {
		this.rawScore = rawScore;
	}
	
	
	/**
	 * Return the activity raw score.
	 * 
	 * Return rawScore
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the activity raw score.", userVisible = true)
	public float RawScore() {
		return this.rawScore;
	}
	
	/**
	 * Specifies the activity scaled score.
	 * 
	 * @param scaledScore
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT,
		      defaultValue = "0")
	@SimpleProperty
	public void ScaledScore(float scaledScore) {
		this.scaledScore = scaledScore;
	}
	
	
	/**
	 * Return the activity scaled score.
	 * 
	 * Return scaledScore
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the activity scaled score.", userVisible = true)
	public float ScaledScore() {
		return this.scaledScore;
	}
	
	/**
	 * Specifies the activity extensions.
	 * 
	 * @param extensions
	 */
	/*@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT,
		      defaultValue = "")*/
	@SimpleProperty
	public void Extensions(List<Object> extensions) {
		this.extensions = extensions;
	}
	
	
	/**
	 * Return the activity extensions.
	 * 
	 * Return extensions
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the activity extensions.", userVisible = true)
	public List<Object> Extensions() {
		return this.extensions;
	}
	
	/**
	 * Specifies the result extensions.
	 * 
	 * @param extensions
	 */
	/*@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT,
		      defaultValue = "")*/
	@SimpleProperty
	public void ResultExtensions(List<Object> extensions) {
		this.resultExtensions = extensions;
	}
	
	
	/**
	 * Return the activity extensions.
	 * 
	 * Return extensions
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the result extensions.", userVisible = true)
	public List<Object> ResultExtensions() {
		return this.resultExtensions;
	}

}
