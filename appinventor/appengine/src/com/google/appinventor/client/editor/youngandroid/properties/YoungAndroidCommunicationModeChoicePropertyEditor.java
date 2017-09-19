// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

//YoungAndroidCommunicationModeChoisePropertyEditor for ActivityTracker component (by SPI-FM at UCA).

package com.google.appinventor.client.editor.youngandroid.properties;

import static com.google.appinventor.client.Ode.MESSAGES;
import com.google.appinventor.client.widgets.properties.ChoicePropertyEditor;

/**
 * Property editor for communication mode of ActivityTracker.
 *
 */
public class YoungAndroidCommunicationModeChoicePropertyEditor extends ChoicePropertyEditor {

  // Communication modes choices
  private static final Choice[] communicationModes = new Choice[] {
    new Choice(MESSAGES.onlywifiCommunicationMode(), "0"),
    new Choice(MESSAGES.indifferentCommunicationMode() , "1"),
  };

  public YoungAndroidCommunicationModeChoicePropertyEditor() {
    super(communicationModes);
  }
}
