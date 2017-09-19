package com.google.appinventor.client.editor.youngandroid.properties;

import static com.google.appinventor.client.Ode.MESSAGES;
import com.google.appinventor.client.widgets.properties.ChoicePropertyEditor;

/**
 * Property editor for screen orientation
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public class YoungAndroidQualityYoutubeChoicePropertyEditor extends ChoicePropertyEditor {

  // Screen orientation choices
  private static final Choice[] qualityYoutubeChoices = new Choice[] {
    // To avoid confusion, we only show a subset of the available screen orientation values.
    new Choice(MESSAGES.qualityYoutube240(), "240"),
    new Choice(MESSAGES.qualityYoutube360(), "360"),
    new Choice(MESSAGES.qualityYoutube720(), "720"),
    new Choice(MESSAGES.qualityYoutube1080(), "1080"),
    
  };

  public YoungAndroidQualityYoutubeChoicePropertyEditor() {
    super(qualityYoutubeChoices);
  }
}