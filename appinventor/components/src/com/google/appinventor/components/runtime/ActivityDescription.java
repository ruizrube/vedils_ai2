package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
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
	 * Specifies the activity duration.
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
	 * Return the activity duration.
	 * 
	 * Return completion
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the activity duration.", userVisible = true)
	public String Duration() {
		return this.duration;
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
		      defaultValue = "")
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
		      defaultValue = "")
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
		      defaultValue = "")
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
		      defaultValue = "")
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

}
