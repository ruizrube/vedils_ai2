// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0
package com.google.appinventor.components.common;

/**
 * Constants for specifying which
 * {@link com.google.appinventor.client.widgets.properties.PropertyEditor
 * PropertyEditor} should be used for modifying a property value within the
 * Designer. This is used within
 * {@link com.google.appinventor.components.annotations.DesignerProperty#editorType()}
 * .
 */
public class PropertyTypeConstants {
	private PropertyTypeConstants() {
	}

	/**
	 * User-uploaded assets.
	 * 
	 * @see com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidAssetSelectorPropertyEditor
	 */
	public static final String PROPERTY_TYPE_ASSET = "asset";

	/**
	 * Instances of
	 * {@link com.google.appinventor.components.runtime.BluetoothClient} in the
	 * current project.
	 */
	public static final String PROPERTY_TYPE_BLUETOOTHCLIENT = "BluetoothClient";

	/**
	 * Boolean values.
	 * 
	 * @see com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidBooleanPropertyEditor
	 */
	public static final String PROPERTY_TYPE_BOOLEAN = "boolean";

	/**
	 * Arrangement alignment.
	 * 
	 * @see com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidHorizontalAlignmentChoicePropertyEditor
	 */
	public static final String PROPERTY_TYPE_HORIZONTAL_ALIGNMENT = "horizontal_alignment";
	public static final String PROPERTY_TYPE_VERTICAL_ALIGNMENT = "vertical_alignment";

	/**
	 * Accelerometer sensitivity.
	 * 
	 * @see com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidAccelerometerSensitvityChoicePropertyEditor
	 */
	public static final String PROPERTY_TYPE_ACCELEROMETER_SENSITIVITY = "accelerometer_sensitivity";

	/**
	 * Button shapes.
	 * 
	 * @see com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidButtonShapeChoicePropertyEditor
	 */
	public static final String PROPERTY_TYPE_BUTTON_SHAPE = "button_shape";

	/**
	 * Any of the colors specified in
	 * {@link com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidColorChoicePropertyEditor}
	 * .
	 */
	public static final String PROPERTY_TYPE_COLOR = "color";

	/**
	 * Component instances in the current project.
	 * 
	 * @see com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidComponentSelectorPropertyEditor
	 */
	public static final String PROPERTY_TYPE_COMPONENT = "component";

	/**
	 * Floating-point values.
	 * 
	 * @see com.google.appinventor.client.widgets.properties.FloatPropertyEditor
	 */
	public static final String PROPERTY_TYPE_FLOAT = "float";

	/**
	 * Integer values.
	 * 
	 * @see com.google.appinventor.client.widgets.properties.IntegerPropertyEditor
	 */
	public static final String PROPERTY_TYPE_INTEGER = "integer";

	/**
	 * Lego NXT sensor ports.
	 * 
	 * @see com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidLegoNxtSensorPortChoicePropertyEditor
	 */
	public static final String PROPERTY_TYPE_LEGO_NXT_SENSOR_PORT = "lego_nxt_sensor_port";

	/**
	 * Colors recognizable by Lego NXT sensors.
	 * 
	 * @see com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidColorChoicePropertyEditor#NXT_GENERATED_COLORS
	 */
	public static final String PROPERTY_TYPE_LEGO_NXT_GENERATED_COLOR = "lego_nxt_generated_color";

	/**
	 * Non-negative (positive or zero) floating-point values.
	 * 
	 * @see com.google.appinventor.client.widgets.properties.NonNegativeFloatPropertyEditor
	 */
	public static final String PROPERTY_TYPE_NON_NEGATIVE_FLOAT = "non_negative_float";

	/**
	 * Non-negative (positive or zero) integers.
	 * 
	 * @see com.google.appinventor.client.widgets.properties.NonNegativeIntegerPropertyEditor
	 */
	public static final String PROPERTY_TYPE_NON_NEGATIVE_INTEGER = "non_negative_integer";

	/**
	 * Choices of screen orientations offered by
	 * {@link com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidScreenOrientationChoicePropertyEditor}
	 * .
	 */
	public static final String PROPERTY_TYPE_SCREEN_ORIENTATION = "screen_orientation";

	/**
	 * Choices of screen animations offered by
	 * {@link com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidScreenAnimationChoicePropertyEditor}
	 * .
	 */
	public static final String PROPERTY_TYPE_SCREEN_ANIMATION = "screen_animation";

	/**
	 * Minimum distance interval, in meters, that the location sensor will try
	 * to use for sending out location updates. See
	 * {@link com.google.appinventor.components.runtime.LocationSensor}.
	 */
	public static final String PROPERTY_TYPE_SENSOR_DIST_INTERVAL = "sensor_dist_interval";

	/**
	 * Minimum time interval, in milliseconds, that the location sensor use to
	 * send out location updates. See
	 * {@link com.google.appinventor.components.runtime.LocationSensor}.
	 */
	public static final String PROPERTY_TYPE_SENSOR_TIME_INTERVAL = "sensor_time_interval";

	/**
	 * Strings. This has the same effect as, but is preferred in component
	 * definitions to, {@link #PROPERTY_TYPE_TEXT}).
	 * 
	 * @see com.google.appinventor.client.widgets.properties.StringPropertyEditor
	 */
	public static final String PROPERTY_TYPE_STRING = "string";

	/**
	 * Text. This has the same effect as {@link #PROPERTY_TYPE_STRING}, which is
	 * preferred everywhere except as the default value for
	 * {@link com.google.appinventor.components.annotations.DesignerProperty#editorType}
	 * .
	 * 
	 * @see com.google.appinventor.client.widgets.properties.TextPropertyEditor
	 * @see com.google.appinventor.client.widgets.properties.TextAreaPropertyEditor
	 */
	public static final String PROPERTY_TYPE_TEXT = "text";

	public static final String PROPERTY_TYPE_TEXTAREA = "textArea";

	/**
	 * Choices of text alignment (left, center, right) offered by
	 * {@link com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidAlignmentChoicePropertyEditor}
	 * .
	 */
	public static final String PROPERTY_TYPE_TEXTALIGNMENT = "textalignment";

	/**
	 * Choices of toast display length (short, long) offered by
	 * {@link com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidToastLengthChoicePropertyEditor}
	 * .
	 */
	public static final String PROPERTY_TYPE_TOAST_LENGTH = "toast_length";

	/**
	 * Choices of typefaces offered by
	 * {@link com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidFontTypefaceChoicePropertyEditor}
	 * .
	 */
	public static final String PROPERTY_TYPE_TYPEFACE = "typeface";

	public static final String PROPERTY_TYPE_COLUMNTOAGGREGATE = "columnToAggregate";

	/**
	 * Choices of chartType offered by
	 * {@link com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidSynchronizationChartyChoicePropertyEditor}
	 */
	public static final String PROPERTY_TYPE_CHARTTYPE = "chartType";

	/**
	 * Choices of synchronizationModes offered by
	 * {@link com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidSynchronizationModeChoicePropertyEditor}
	 */
	public static final String PROPERTY_TYPE_SYNCHRONIZATIONMODE = "synchronizationMode";

	/**
	 * Choices of synchronizationModes offered by
	 * {@link com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidCommunicationModeChoicePropertyEditor}
	 */
	public static final String PROPERTY_TYPE_COMMUNICATIONMODE = "communicationMode";
	
	/**
	 * Choices of storageModes offered by
	 * {@link com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidStorageModeChoicePropertyEditor}
	 */
	public static final String PROPERTY_TYPE_STORAGEMODE = "storageMode";
	
	/**
	 * Choices of storageModes offered by
	 * {@link com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidQueryStorageModeChoicePropertyEditor}
	 */
	public static final String PROPERTY_TYPE_QUERYSTORAGEMODE = "queryStorageMode";

	/**
	 * Choices in checkableTree offered by
	 * {@link com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidCheckableTreeSelector}
	 */
	public static final String PROPERTY_TYPE_CHECKABLETREEFORACTIVITYTRACKER = "checkableTreeForActivityTracker";

	//userData
	public static final String PROPERTY_TYPE_ONLY_USER = "user";
	
	// Para arbol de datos
	public static final String PROPERTY_TYPE_CHECKABLETREEFORDATA = "checkableTreeForData";

	// Para arbol de datos
	public static final String PROPERTY_TYPE_CHECKABLETREEFORAGGREGATEDDATA = "checkableTreeForAggregatedData";

	// Para asociar query a chart o table
	public static final String PROPERTY_TYPE_ONLY_QUERY = "query";
	
	// para asociar el tts a dialog
	public static final String PROPERTY_TYPE_ONLY_TTS = "tts";


	// Para asociar tipo de dispositivo EEG
	public static final String PROPERTY_TYPE_EEG = "eegType";

	// Para arbol de datos
	public static final String PROPERTY_TYPE_TREEFORSEMANTICTYPE = "treeForSemanticType";

	/**
	 * Display a hyperlink with a constant URL
	/**
	 * Display a hyperlink with a constant URL
	 * record by
	 * {@link com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidAnchorProperty}
	 */
	public static final String PROPERTY_TYPE_CONSTANT_HYPERLINK = "constant_hyperLink";
	
	
	/**
	 * Display textbox and hyperlink with the tableId for ActivityTracker
	 * record by
	 * {@link com.google.appinventor.client.widgets.properties.StringAndHyperlinkPropertyEditorForActivityTracker}
	 */
	public static final String PROPERTY_TYPE_TEXTBOX_AND_HYPERLINK_FORACTIVITYTRACKER = "textbox_and_hyperlink_for_activitytracker";

	/**
	 * Choices of visibility for view components offered by
	 * {@link com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidVisibilityChoicePropertyEditor}
	 * .
	 */
	public static final String PROPERTY_TYPE_VISIBILITY = "visibility";

	/**
	 * Choices of Text Receiving options.
	 * {@link com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidTextReceivingPropertyEditor}
	 * .
	 */
	public static final String PROPERTY_TYPE_TEXT_RECEIVING = "text_receiving";

	/**
	 * Choices of text-to-speech countries.
	 * {@link com.google.appinventor.client.widgets.properties.CountryChoicePropertyEditor}
	 * .
	 */
	public static final String PROPERTY_TYPE_TEXT_TO_SPEECH_COUNTRIES = "countries";

	/**
	 * Choices of text-to-speech languages.
	 * {@link com.google.appinventor.client.widgets.properties.LanguageChoicePropertyEditor}
	 * .
	 */
	public static final String PROPERTY_TYPE_TEXT_TO_SPEECH_LANGUAGES = "languages";

	/**
	 * Choices of the "Sizing" property in Form.java. Used to specify if we are
	 * going to use the true size of the real screen (responsize) or scale
	 * automatically to make all devices look like an old phone (fixed).
	 */
	public static final String PROPERTY_TYPE_SIZING = "sizing";

	/**
	 * Choices of text-to-speech languages.
	 * {@link com.google.appinventor.client.widgets.properties.TrackerChoicePropertyEditor}
	 * .
	 */
	public static final String PROPERTY_TYPE_KIND_OF_TRACKERS = "trackers";

	/**
	 * Choices of text-to-speech languages.
	 * {@link com.google.appinventor.client.widgets.properties.VisualAssetChoicePropertyEditor}
	 * .
	 */
	public static final String PROPERTY_TYPE_KIND_OF_VISUALASSETS = "visualassets";

	public static final String PROPERTY_TYPE_ONLY_ARCAMERA = "arcamera";
	
	public static final String PROPERTY_TYPE_ONLY_VRSCENE = "vrscene";
	
	public static final String PROPERTY_TYPE_QUALITY_YOUTUBE = "youtube_quality";

	public static final String PROPERTY_TYPE_ONLY_ARCAMERAOVERLAYER = "arcameraoverlayer";

	public static final String PROPERTY_TYPE_ASSET_3DMODEL = "asset3dmodel";

	public static final String PROPERTY_TYPE_ASSET_MATERIAL = "assetmaterial";

	public static final String PROPERTY_TYPE_ASSET_IMAGE = "assetimage";

	public static final String PROPERTY_TYPE_ASSET_DATABASE_DAT = "assetdbdat";

	public static final String PROPERTY_TYPE_ASSET_DATABASE_XML = "assetdbxml";
	
	public static final String PROPERTY_TYPE_ASSET_TEXTURES_LIST = "textures list";

	/*
	 * FirebaseURL -- A type of String property that has a special default value
	 * selected via a checkbox.
	 */

	public static final String PROPERTY_TYPE_FIREBASE_URL = "FirbaseURL";

	/**
	 * Specifies how a picture is scaled when its dimensions are changed.
	 * Choices are 0 - Scale proportionally, 1 - Scale to fit See
	 * {@link com.google.appinventor.client.widgets.properties.ScalingChoicePropertyEditor}
	 */
	public static final String PROPERTY_TYPE_SCALING = "scaling";
	
	
}
