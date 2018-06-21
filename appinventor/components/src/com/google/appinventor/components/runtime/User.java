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
@DesignerComponent(version = YaVersion.USER_COMPONENT_VERSION,
description = "A component for defining the data of the user of the application. ", 
category = ComponentCategory.VEDILSLEARNINGANALYTICS, iconName = "images/user_icon.png", nonVisible= true)
public class User extends AndroidNonvisibleComponent implements Component {
	
	private String id;
	private String name;
	private String surname;
	private String email;
	private String twitterAccount;
	private String facebookAccount;
	private String linkedinAccount;
	
	public User(ComponentContainer componentContainer) {
		super(componentContainer.$form());
		this.id = "";
		this.name = "";
		this.surname = "";
		this.email = "";
		this.twitterAccount = "";
		this.facebookAccount = "";
		this.linkedinAccount = "";
	}
	
	/**
	 * Specifies the id of the user of the application.
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
	 * Return the id of the user of the application.
	 * 
	 * Return id 
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the id of the user of the application.", userVisible = true)
	public String Id() {
		return this.id;
	}
	
	/**
	 * Specifies the name of the user of the application.
	 * 
	 * @param name
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "")
	@SimpleProperty
	public void UserName(String name) {
		this.name = name;
	}
	
	
	/**
	 * Return the name of the user of the application.
	 * 
	 * Return name 
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the name of the user of the application.", userVisible = true)
	public String UserName() {
		return this.name;
	}
	
	
	/**
	 * Specifies the surname of the user of the application.
	 * 
	 * @param surname
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "")
	@SimpleProperty
	public void Surname(String surname) {
		this.surname = surname;
	}
	
	/**
	 * Return the surname of the user of the application.
	 * 
	 * Return surname 
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the surname of the user of the application.", userVisible = true)
	public String Surname() {
		return this.surname;
	}
	
	
	/**
	 * Specifies the e-mail of the user of the application.
	 * 
	 * @param email 
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "")
	@SimpleProperty
	public void Email(String email) {
		this.email = email;
	}
	
	
	/**
	 * Return the e-mail of the user of the application.
	 * 
	 * Return email
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the e-mail of the user of the application.", userVisible = true)
	public String Email() {
		return this.email;
	}
	
	
	/**
	 * Specifies the twitter account of the user of the application.
	 * 
	 * @param twitterAccount
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "")
	@SimpleProperty
	public void TwitterAccount(String twitterAccount) {
		this.twitterAccount = twitterAccount;
	}
	
	
	/**
	 * Return the twitter account of the user of the application.
	 * 
	 * Return twitterAccount
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the twitter account of the user of the application.", userVisible = true)
	public String TwitterAccount() {
		return this.twitterAccount;
	}
	
	
	/**
	 * Specifies the facebook account of the user of the application.
	 * 
	 * @param twitterAccount
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "")
	@SimpleProperty
	public void FacebookAccount(String facebookAccount) {
		this.facebookAccount = facebookAccount;
	}
	
	/**
	 * Return the facebook account of the user of the application.
	 * 
	 * Return facebookAccount
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the facebook account of the user of the application.", userVisible = true)
	public String FacebookAccount() {
		return this.facebookAccount;
	}
	
	
	/**
	 * Specifies the linkedin account of the user of the application.
	 * 
	 * @param linkedinAccount
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
		      defaultValue = "")
	@SimpleProperty
	public void LinkedinAccount(String linkedinAccount) {
		this.linkedinAccount = linkedinAccount;
	}
	
	/**
	 * Return the linkedin account of the user of the application.
	 * 
	 * Return linkedinAccount
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Return the linkedin account of the user of the application.", userVisible = true)
	public String LinkedinAccount() {
		return this.facebookAccount;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getSurname() {
		return this.surname;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getTwitterAccount() {
		return this.twitterAccount;
	}
	
	public String getFacebookAccount() {
		return this.facebookAccount;
	}
	
	public String getLinkedinAccount() {
		return this.linkedinAccount;
	}
	
	public String getId() {
		return this.id;
	}
}
