// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.components.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.collect.Maps;
import com.google.appinventor.components.runtime.util.DialogConfig;
import com.google.appinventor.components.runtime.util.DialogLanguageConfig;
import com.google.appinventor.components.runtime.util.ai.api.AIListener;
import com.google.appinventor.components.runtime.util.ai.api.android.AIConfiguration;
import com.google.appinventor.components.runtime.util.ai.api.android.AIService;
import com.google.appinventor.components.runtime.util.ai.api.android.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import ai.api.RequestExtras;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIOutputContext;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;

/**
 * Component for using the built in VoiceRecognizer to convert speech to text.
 * For more details, please see:
 * http://developer.android.com/reference/android/speech/RecognizerIntent.html
 *
 */

@UsesLibraries(libraries = "libai-1.6.12.jar")
@DesignerComponent(version = YaVersion.CONVERSATION_COMPONENT_VERSION, description = "Component for using Voice Recognition to maintain a dialag", category = ComponentCategory.MEDIA, nonVisible = true, iconName = "images/speechRecognizer.png")
@SimpleObject
public class Dialog extends AndroidNonvisibleComponent implements Component, AIListener {

	private AIService aiService;

	private final ComponentContainer container;
	private Gson gson = GsonFactory.getGson();

	private String language;

	private boolean tts;
	private static final Map<String, Locale> iso3LanguageToLocaleMap = Maps.newHashMap();

	private static TextToSpeech textToSpeech;

	/**
	 * Creates a SpeechRecognizer component.
	 *
	 * @param container
	 *            container, component will be placed in
	 */
	public Dialog(ComponentContainer container) {
		super(container.$form());
		this.container = container;
		Language(Component.DEFAULT_VALUE_TEXT_TO_SPEECH_LANGUAGE);
	}

	/*****************************************************************************************
	 * EDSON
	 */
	/**
	 * Sets the language for this SpeechRecognizer component.
	 *
	 * @param language
	 *            is the ISO2 (i.e. 2 letter) or ISO3 (i.e. 3 letter) language code
	 *            to set this SpeechRecognizer component to.
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_TEXT_TO_SPEECH_LANGUAGES, defaultValue = Component.DEFAULT_VALUE_TEXT_TO_SPEECH_LANGUAGE)
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Sets the language for SpeechRecognizer.")
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
	 * Gets the language for this SpeechRecognizer component. This will be either an
	 * ISO2 (i.e. 2 letter) or ISO3 (i.e. 3 letter) code depending on which kind of
	 * code the property was set with.
	 *
	 * @return the language code for this SpeechRecognizer component.
	 */
	@SimpleProperty
	public String Language() {
		return language;
	}

	private void initService(final DialogLanguageConfig languages) {
		final AIConfiguration.SupportedLanguages lang = AIConfiguration.SupportedLanguages
				.fromLanguageTag(languages.getLanguageCode());
		final AIConfiguration config = new AIConfiguration(languages.getAccessToken(), lang,
				AIConfiguration.RecognitionEngine.System);

		if (aiService != null) {
			aiService.pause();
		}

		aiService = AIService.getService(container.$context(), config);
		aiService.setListener(this);
	}

	@SimpleFunction
	public void StartRecognition() {
		initService(DialogConfig.languages[0]);

		aiService.startListening();
	}

	@SimpleFunction
	public void StartRecognition(String context) {
		initService(DialogConfig.languages[0]);

		if (!TextUtils.isEmpty(context)) {
			final List<AIContext> contexts = Collections.singletonList(new AIContext(context));
			final RequestExtras requestExtras = new RequestExtras(contexts, null);
			aiService.startListening(requestExtras);
		}

	}

	@SimpleFunction
	public void StopRecognition() {
		aiService.stopListening();
	}

	@SimpleFunction
	public void CancelRecognition() {
		aiService.cancel();
	}

	/**
	 * @return the DistinctResults
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public boolean TTS() {
		return tts;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
	@SimpleProperty(description = "Specifies if duplicated results are not allowed", userVisible = true)
	public void TTS(boolean tts) {
		this.tts = tts;
	}

	@Override
	public void onResult(final AIResponse response) {
		container.$context().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				final Status status = response.getStatus();
				//Log.i(TAG, "Status code: " + status.getCode());
				//Log.i(TAG, "Status type: " + status.getErrorType());
				
				final Result result = response.getResult();
				
				final Metadata metadata = result.getMetadata();
				if (metadata != null) {
				  //Log.i(TAG, "Intent id: " + metadata.getIntentId());
				  //Log.i(TAG, "Intent name: " + metadata.getIntentName());
				}
				
				// // this is example how to get different parts of result object
				// final Status status = response.getStatus();
				// Log.i(TAG, "Status code: " + status.getCode());
				// Log.i(TAG, "Status type: " + status.getErrorType());
				//
				// final Result result = response.getResult();
				// Log.i(TAG, "Resolved query: " + result.getResolvedQuery());
				//
				// final Metadata metadata = result.getMetadata();
				// if (metadata != null) {
				// Log.i(TAG, "Intent id: " + metadata.getIntentId());
				// Log.i(TAG, "Intent name: " + metadata.getIntentName());
				// }
				//
				
				final String speech = result.getFulfillment().getSpeech();
				
				final String resolvedQuery= result.getResolvedQuery();
				final String action= result.getAction();
				
				// Context
				List<String> ctxs = new ArrayList<String>();

				if (response.getResult().getContexts() != null && !response.getResult().getContexts().isEmpty()) {
					for (AIOutputContext ctx: response.getResult().getContexts()) {						
						ctxs.add(ctx.getName());
					}
				}

				
				// Action paramaters
				List<List<String>> params = new ArrayList<List<String>>();
				List<String> aux = new ArrayList<String>();

				if (response.getResult().getParameters() != null && !response.getResult().getParameters().isEmpty()) {
					for (final Map.Entry<String, JsonElement> entry : response.getResult().getParameters().entrySet()) {

						aux = new ArrayList<String>();

						aux.add(entry.getKey());

						aux.add(entry.getValue().toString());

						params.add(aux);
					}
				}
				//gson.toJson(response);
				
				
				if (tts) {
					if (textToSpeech == null) {
						textToSpeech = new TextToSpeech(container.$context(), new TextToSpeech.OnInitListener() {
							@Override
							public void onInit(int i) {

							}
						});
					}
					textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
				}

				

				
				AfterGettingText(ctxs, speech, action,params);

				
			}

		});

	}

	@Override
	public void onError(final AIError error) {
		container.$context().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// resultTextView.setText(error.toString());
			}
		});
	}

	@Override
	public void onAudioLevel(final float level) {
		container.$context().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				float positiveLevel = Math.abs(level);

				if (positiveLevel > 100) {
					positiveLevel = 100;
				}
				// progressBar.setProgress((int) positiveLevel);
			}
		});
	}

	@Override
	public void onListeningStarted() {
		container.$context().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// recIndicator.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void onListeningCanceled() {
		container.$context().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// recIndicator.setVisibility(View.INVISIBLE);
				// resultTextView.setText("");
			}
		});
	}

	@Override
	public void onListeningFinished() {
		container.$context().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// recIndicator.setVisibility(View.INVISIBLE);
			}
		});
	}

	/**
	 * Simple event to raise after the VoiceReco activity has returned
	 * 
	 * @param list
	 * @param hashMap
	 * @param string
	 */
	@SimpleEvent
	public void AfterGettingText(List<String> ctxs, String speech, String action,
			List<List<String>> params) {

		
		EventDispatcher.dispatchEvent(this, "AfterGettingText", ctxs, speech, action, params);
	}

	public class LanguageConfig {
		private final String languageCode;
		private final String accessToken;

		public LanguageConfig(final String languageCode, final String accessToken) {
			this.languageCode = languageCode;
			this.accessToken = accessToken;
		}

		public String getLanguageCode() {
			return languageCode;
		}

		public String getAccessToken() {
			return accessToken;
		}

		@Override
		public String toString() {
			return languageCode;
		}
	}

}
