// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

//YoungAndroidSynchronizationModeChoisePropertyEditor for ActivityTracker component (by SPI-FM at UCA).

package com.google.appinventor.client.editor.youngandroid.properties;

import static com.google.appinventor.client.Ode.MESSAGES;
import com.google.appinventor.client.widgets.properties.ChoicePropertyEditor;

/**
 * Property editor for synchronization mode of ActivityTracker.
 *
 */
public class YoungAndroidChartTypeChoicePropertyEditor extends ChoicePropertyEditor {

  // Chart type choices
  private static final Choice[] chartTypes = new Choice[] {
    new Choice(MESSAGES.barChart(), "0"),
    new Choice(MESSAGES.lineChart() , "1"),
    new Choice(MESSAGES.columnChart(), "2")
 //   new Choice(MESSAGES.pieChart(), "2"),
  };

  public YoungAndroidChartTypeChoicePropertyEditor() {
    super(chartTypes);
  }
}
