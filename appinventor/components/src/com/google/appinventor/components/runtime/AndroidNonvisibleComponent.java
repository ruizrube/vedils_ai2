// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;

import android.view.View;

/**
 * Base class for all non-visible components.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
@SimpleObject
public abstract class AndroidNonvisibleComponent implements Component {

  protected final Form form;
  private boolean notificable;

  /**
   * Creates a new AndroidNonvisibleComponent.
   *
   * @param form the container that this component will be placed in
   */
  protected AndroidNonvisibleComponent(Form form) {
    this.form = form;
    this.notificable = true;
  }

  // Component implementation

  @Override
  public HandlesEventDispatching getDispatchDelegate() {
    return form;
  }

  /**
	 * Specifies when the component are tracked.
	 * @param no
	 */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
	      defaultValue = "True")
  @SimpleProperty
  public void Notificable(boolean notificable) {
	  this.notificable = notificable;
  }
  
  public boolean getNotificable() {
	  return this.notificable;
  }
  
}
