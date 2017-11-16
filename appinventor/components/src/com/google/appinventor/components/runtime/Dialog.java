// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.components.runtime;

import java.util.ArrayList;
import java.util.Collections;
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
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.collect.Maps;
import com.google.appinventor.components.runtime.util.DialogConfig;
import com.google.appinventor.components.runtime.util.DialogLanguageConfig;
import com.google.appinventor.components.runtime.util.ai.api.AIListener;
import com.google.appinventor.components.runtime.util.ai.api.android.AIConfiguration;
import com.google.appinventor.components.runtime.util.ai.api.android.AIService;
import com.google.gson.JsonElement;

import ai.api.RequestExtras;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIEvent;
import ai.api.model.AIOutputContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;
import android.text.TextUtils;
import android.util.Log;

/**
 * Component for using the built in VoiceRecognizer to convert speech to text.
 * For more details, please see:
 * http://developer.android.com/reference/android/speech/RecognizerIntent.html
 *
 */

@UsesLibraries(libraries = "libai-1.6.12.jar, slf4j-api-1.7.25.jar, gson-2.8.1.jar")
@DesignerComponent(version = YaVersion.CONVERSATION_COMPONENT_VERSION, description = "Component for using Voice Recognition to maintain a dialag", category = ComponentCategory.VEDILSINTERACTIONS, nonVisible = true, iconName = "images/speechRecognizer.png")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.RECORD_AUDIO")
public class Dialog extends AndroidNonvisibleComponent implements Component, AIListener {

	private com.google.appinventor.components.runtime.TextToSpeech textToSpeech;

	private AIService aiService;

	private final ComponentContainer container;

	private String language;

	private String apiKey = "e2b1579b9838458196adc88b10d9d278";

	private static final Map<String, Locale> iso3LanguageToLocaleMap = Maps.newHashMap();

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

	private static Locale iso3LanguageToLocale(String iso3Language) {
		Locale mappedLocale = iso3LanguageToLocaleMap.get(iso3Language);
		if (mappedLocale == null) {
			// Language codes should be lower case, but maybe the user doesn't know that.
			mappedLocale = iso3LanguageToLocaleMap.get(iso3Language.toLowerCase(Locale.ENGLISH));
		}
		return mappedLocale == null ? Locale.getDefault() : mappedLocale;
	}

	private void initService() {

		Log.i("Dialog", "Setting DialogFlow language to " + this.language + " using " + apiKey);

		DialogLanguageConfig language = DialogConfig.languages.get(this.language);

		final AIConfiguration.SupportedLanguages lang = AIConfiguration.SupportedLanguages
				.fromLanguageTag(language.getLanguageCode());

		final AIConfiguration config = new AIConfiguration(apiKey, lang, AIConfiguration.RecognitionEngine.System);

		// if (aiService != null) {
		// aiService.pause();
		// }

		Log.i("Dialog", "Setting AIService..");

		aiService = AIService.getService(container.$context(), config);
		aiService.setListener(this);

	}

	@SimpleFunction
	public void InvokeDialog(String event, String context) {

		if (aiService == null) {
			Log.i("Dialog", "Init DialogFlow Service...");

			initService();
		}

		Log.i("Dialog", "Invoking intent by a event name...");

		final AIRequest request = new AIRequest();
		request.setEvent(new AIEvent(event));
			
		if (!TextUtils.isEmpty(context)) {
			final List<AIContext> contexts = Collections.singletonList(new AIContext(context));
			request.setContexts(contexts);
		} 
		
		aiService.sendEvent(request);
		
	}

	/**
	 * Solicits silently speech input from the user. After the speech is converted
	 * to text, the AfterGettingText event will be raised.
	 */
	@SimpleFunction
	public void GetText() {

		if (aiService == null) {
			Log.i("Dialog", "Init DialogFlow Service...");

			initService();
		}

		Log.i("Dialog", "Start Listening...");

		aiService.startListeningWithoutIA();

	}

	@SimpleFunction
	public void StartRecognition(String context) {

		if (aiService == null) {
			Log.i("Dialog", "Init DialogFlow Service...");

			initService();
		}

		Log.i("Dialog", "Start Listening...");

		if (!TextUtils.isEmpty(context)) {
			final List<AIContext> contexts = Collections.singletonList(new AIContext(context));
			final RequestExtras requestExtras = new RequestExtras(contexts, null);
			aiService.startListening(requestExtras);
		} else {
			aiService.startListening();
		}
	}

	@SimpleFunction
	public void StopRecognition() {
		Log.i("Dialog", "Stop Listening...");

		aiService.stopListening();
	}

	@SimpleFunction
	public void CancelRecognition() {
		Log.i("Dialog", "Cancel Listening...");

		aiService.cancel();
	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String ApiKey() {
		return apiKey;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_TEXT, defaultValue = "e2b1579b9838458196adc88b10d9d278")
	@SimpleProperty(description = "Specifies the token key of the external Dialog Service", userVisible = true)
	public void ApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public TextToSpeech TextToSpeech() {
		return textToSpeech;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ONLY_TTS, defaultValue = "")
	@SimpleProperty(description = "Specifies if TTS is used to reproduce results ", userVisible = true)
	public void TextToSpeech(TextToSpeech textToSpeech) {
		this.textToSpeech = textToSpeech;
	}

	@Override
	public void onRecognizement(String result) {
		AfterGettingText(result);

	}

	@Override
	public void onResult(final AIResponse response) {
		container.$context().runOnUiThread(new Runnable() {
			@Override
			public void run() {

				final Status status = response.getStatus();

				Log.i("Dialog", "Status code: " + status.getCode());
				Log.i("Dialog", "Status type: " + status.getErrorType());

				if (status.getCode() != 200) {
					DialogError(status.getErrorType());
				} else {

					final Result result = response.getResult();
					Log.i("Dialog", "Result obtained: " + result.toString());

					final Metadata metadata = result.getMetadata();
					if (metadata != null) {
						Log.i("Dialog", "Intent id: " + metadata.getIntentId());
						Log.i("Dialog", "Intent name: " + metadata.getIntentName());
					}

					Log.i("Dialog", "Resolved query: " + result.getResolvedQuery());

					if (metadata != null) {
						Log.i("Dialog", "Intent id: " + metadata.getIntentId());
						Log.i("Dialog", "Intent name: " + metadata.getIntentName());
					}

					final String speech = result.getFulfillment().getSpeech();

					final String resolvedQuery = result.getResolvedQuery();
					Log.i("Dialog", "Resolved Query: " + resolvedQuery);

					final String action = result.getAction();

					// Context
					List<String> ctxs = new ArrayList<String>();

					if (response.getResult().getContexts() != null && !response.getResult().getContexts().isEmpty()) {
						for (AIOutputContext ctx : response.getResult().getContexts()) {
							ctxs.add(ctx.getName());
						}
					}

					// Action paramaters
					List<List<String>> params = new ArrayList<List<String>>();
					List<String> aux = new ArrayList<String>();

					if (response.getResult().getParameters() != null
							&& !response.getResult().getParameters().isEmpty()) {
						for (final Map.Entry<String, JsonElement> entry : response.getResult().getParameters()
								.entrySet()) {

							aux = new ArrayList<String>();

							aux.add(entry.getKey());

							aux.add(entry.getValue().toString());

							params.add(aux);
						}
					}

					// si tenemos asociado un TTS...
					if (textToSpeech != null) {
						textToSpeech.Speak(speech);
					}

					if (action.equals("input.unknown")) {
						UnknownActionReceived(resolvedQuery, ctxs, speech, params);
					} else {
						ActionReceived(resolvedQuery, ctxs, speech, action, params, !result.isActionIncomplete());

					}
				}
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
	 */
	@SimpleEvent
	public void AfterGettingText(String result) {

		EventDispatcher.dispatchEvent(this, "AfterGettingText", result);
	}

	/**
	 * Simple event to raise after an specific intent has been triggered by a user’s
	 * input activity
	 * 
	 */
	@SimpleEvent
	public void ActionReceived(String inputQuery, List<String> ctxs, String speech, String action,
			List<List<String>> params, boolean complete) {

		EventDispatcher.dispatchEvent(this, "ActionReceived", inputQuery, ctxs, speech, action, params, complete);
	}

	/**
	 * Simple event to raise after no specific intent has been triggered by a user’s
	 * input activity
	 * 
	 */
	@SimpleEvent
	public void UnknownActionReceived(String inputQuery, List<String> ctxs, String speech, List<List<String>> params) {

		EventDispatcher.dispatchEvent(this, "UnknownActionReceived", inputQuery, ctxs, speech, params);
	}

	/**
	 * Simple event to raise after the VoiceReco activity has not returned well
	 * 
	 */
	@SimpleEvent
	public void DialogError(String errorType) {

		EventDispatcher.dispatchEvent(this, "DialogError", errorType);
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
