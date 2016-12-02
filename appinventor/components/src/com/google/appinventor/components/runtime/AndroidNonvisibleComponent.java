// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;

/**
 * Base class for all non-visible components.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
@SimpleObject
public abstract class AndroidNonvisibleComponent implements Component {

  protected final Form form;
  
  // Fields for ActivityTracker Component.
  private String activitiesNames;
  private String name;

  /**
   * Creates a new AndroidNonvisibleComponent.
   *
   * @param form the container that this component will be placed in
   */
  protected AndroidNonvisibleComponent(Form form) {
    this.form = form;
    this.activitiesNames = "";
  }

  // Component implementation

  @Override
  public HandlesEventDispatching getDispatchDelegate() {
    return form;
  }
  
  /**
   * Specifies the activities to record for ActivityTracker notification.
   */
   @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_CHECKABLETREEFORACTIVITYTRACKER,
 		      defaultValue = "")
 		  @SimpleProperty(
 		      userVisible = false)
   public void ActivitiesToTrack(String activitiesNames) {
 		this.activitiesNames = activitiesNames;
   }
   
   public String getActivitiesToTrack() {
	   return this.activitiesNames;
   }
   
   /**
    * Specifies the instance component name
    */
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
  		      defaultValue = "")
  		  @SimpleProperty(
  		      userVisible = false)
    public void ComponentName(String name) {
  		this.name = name;
    }
    
    public String getName() {
    	return this.name;
    }
}
