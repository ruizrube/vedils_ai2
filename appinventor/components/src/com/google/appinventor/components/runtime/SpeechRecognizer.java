// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.collect.Maps;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/**
 * Component for using the built in VoiceRecognizer to convert speech to text.
 * For more details, please see:
 * http://developer.android.com/reference/android/speech/RecognizerIntent.html
 *
 */
@DesignerComponent(version = YaVersion.SPEECHRECOGNIZER_COMPONENT_VERSION,
    description = "Component for using Voice Recognition to convert from speech to text",
    category = ComponentCategory.MEDIA,
    nonVisible = true,
    iconName = "images/speechRecognizer.png")
@SimpleObject
public class SpeechRecognizer extends AndroidNonvisibleComponent
    implements Component, ActivityResultListener {

  private final ComponentContainer container;
  private String result;
  /*****************************************************************************************  EDSON*/
  private String language;						
  private String iso2Language;					
  private static final Map<String, Locale> iso3LanguageToLocaleMap = Maps.newHashMap();
  /*****************************************************************************************  EDSON*/
  
  /* Used to identify the call to startActivityForResult. Will be passed back
     into the resultReturned() callback method. */
  private int requestCode;

  /**
   * Creates a SpeechRecognizer component.
   *
   * @param container container, component will be placed in
   */
  public SpeechRecognizer(ComponentContainer container) {
    super(container.$form());
    this.container = container;
    result = "";
    Language(Component.DEFAULT_VALUE_TEXT_TO_SPEECH_LANGUAGE);
  }

  /**
   * Result property getter method.
   */
  @SimpleProperty(
      category = PropertyCategory.BEHAVIOR)
  public String Result() {
    return result;
  }

  /*****************************************************************************************  EDSON*/
  /**
   * Sets the language for this SpeechRecognizer component.
   *
   * @param language is the ISO2 (i.e. 2 letter) or ISO3 (i.e. 3 letter) language code to set this
   * SpeechRecognizer component to.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_TEXT_TO_SPEECH_LANGUAGES,
    defaultValue = Component.DEFAULT_VALUE_TEXT_TO_SPEECH_LANGUAGE)
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,
  description = "Sets the language for SpeechRecognizer.")
  public void Language(String language) {
    Locale locale;
    switch (language.length()) {
    case 3:
      locale = iso3LanguageToLocale(language);
      this.language = locale.getISO3Language();
      break;
    case 2:
      locale = new Locale(language);
      this.language = locale.getLanguage();
      break;
    default:
      locale = Locale.getDefault();
      this.language = locale.getLanguage();
      break;
    }
    iso2Language = locale.getLanguage();
  }

  private static Locale iso3LanguageToLocale(String iso3Language) {
    Locale mappedLocale = iso3LanguageToLocaleMap.get(iso3Language);
    if (mappedLocale == null) {
      // Language codes should be lower case, but maybe the user doesn't know that.
      mappedLocale = iso3LanguageToLocaleMap.get(iso3Language.toLowerCase(Locale.ENGLISH));
    }
    return mappedLocale == null ? Locale.getDefault() : mappedLocale;
  }
  
  /**
   * Gets the language for this SpeechRecognizer component.  This will be either an ISO2 (i.e. 2 letter)
   * or ISO3 (i.e. 3 letter) code depending on which kind of code the property was set with.
   *
   * @return the language code for this SpeechRecognizer component.
   */
  @SimpleProperty
  public String Language() {
    return language;
  }
  
  /*****************************************************************************************  EDSON*/
  
  
  
  /**
   * Solicits speech input from the user.  After the speech is converted to
   * text, the AfterGettingText event will be raised.
   */
  @SimpleFunction
  public void GetText() {
    BeforeGettingText();
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
    				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    
    /*****************************************************************************************  EDSON*/
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Language());
    //intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Sprich jetzt!"); /*Modificado para aplicacion de Anke*/
    /*****************************************************************************************  EDSON*/  
    
    if (requestCode == 0) {
      requestCode = form.registerForActivityResult(this);
    }
    container.$context().startActivityForResult(intent, requestCode);
  }

  @Override
  public void resultReturned(int requestCode, int resultCode, Intent data) {
    if (requestCode == this.requestCode && resultCode == Activity.RESULT_OK) {
      if (data.hasExtra(RecognizerIntent.EXTRA_RESULTS)) {
        ArrayList<String> results;
        results = data.getExtras().getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
        result = results.get(0);
      } else {
        result = "";
      }
      AfterGettingText(result);
    }
  }

  /**
   * Simple event to raise when VoiceReco is invoked but before the VoiceReco
   * activity is started.
   */
  @SimpleEvent
  public void BeforeGettingText() {
    EventDispatcher.dispatchEvent(this, "BeforeGettingText");
  }

  /**
   * Simple event to raise after the VoiceReco activity has returned
   */
  @SimpleEvent
  public void AfterGettingText(String result) {
    EventDispatcher.dispatchEvent(this, "AfterGettingText", result);
  }
 
}
