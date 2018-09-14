// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2014 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.editor.youngandroid.palette;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.appinventor.client.TranslationDesignerPallete;
import com.google.appinventor.client.editor.simple.SimpleComponentDatabase;
import com.google.appinventor.client.editor.simple.components.MockComponent;
import com.google.appinventor.client.editor.simple.palette.DropTargetProvider;
import com.google.appinventor.client.editor.simple.palette.SimpleComponentDescriptor;
import com.google.appinventor.client.editor.simple.palette.SimplePaletteItem;
import com.google.appinventor.client.editor.simple.palette.SimplePalettePanel;
import com.google.appinventor.client.editor.youngandroid.YaFormEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidAccelerometerSensitivityChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidAlignmentChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidAnchorProperty;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidAssetSelectorPropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidBooleanPropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidButtonShapeChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidChartTypeChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidCheckableTreeSelectorForActivityTracker;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidCheckableTreeSelectorForAggregatedData;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidCheckableTreeSelectorForData;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidCheckableTreeSelectorForTextures;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidColorChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidColumntToAggregateChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidCommunicationModeChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidComponentSelectorPropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidDefaultURLPropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidEEGDeviceChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidFontTypefaceChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidHorizontalAlignmentChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidLegoNxtSensorPortChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidScreenAnimationChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidScreenOrientationChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidQualityYoutubeChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidQueryStorageModeChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidSensorDistIntervalChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidSensorTimeIntervalChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidSizingChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidStorageModeChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidSynchronizationModeChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidTextReceivingPropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidToastLengthChoicePropertyEditor;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidTreeSelectorForSemanticType;
import com.google.appinventor.client.editor.youngandroid.properties.YoungAndroidVerticalAlignmentChoicePropertyEditor;
import com.google.appinventor.client.output.OdeLog;
import com.google.appinventor.client.widgets.properties.CountryChoicePropertyEditor;
import com.google.appinventor.client.widgets.properties.FloatPropertyEditor;
import com.google.appinventor.client.widgets.properties.IntegerPropertyEditor;
import com.google.appinventor.client.widgets.properties.LanguageChoicePropertyEditor;
import com.google.appinventor.client.widgets.properties.NonNegativeFloatPropertyEditor;
import com.google.appinventor.client.widgets.properties.NonNegativeIntegerPropertyEditor;
import com.google.appinventor.client.widgets.properties.PropertyEditor;
import com.google.appinventor.client.widgets.properties.ScalingChoicePropertyEditor;
import com.google.appinventor.client.widgets.properties.StringAndHyperlinkPropertyEditorForActivityTracker;
import com.google.appinventor.client.widgets.properties.StringPropertyEditor;
import com.google.appinventor.client.widgets.properties.TextAreaPropertyEditor;
import com.google.appinventor.client.widgets.properties.TextPropertyEditor;
import com.google.appinventor.common.version.AppInventorFeatures;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.shared.simple.ComponentDatabaseInterface.PropertyDefinition;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.appinventor.client.ComponentsTranslation;


/**
 * Panel showing Simple components which can be dropped onto the Young Android
 * visual designer panel.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
public class YoungAndroidPalettePanel extends Composite implements SimplePalettePanel {

	// Component database: information about components (including their
	// properties and events)
	private static final SimpleComponentDatabase COMPONENT_DATABASE = SimpleComponentDatabase.getInstance();

	// Associated editor
	private final YaFormEditor editor;

	private final StackPanel stackPalette;
	private final Map<ComponentCategory, VerticalPanel> categoryPanels;

	// Lista de objetos de tipo tracker
	private static final String[] TRACKERS_TYPES = { "ARTextTracker", "ARImageTracker", "ARMarkerTracker",
			"ARObjectTracker" };
	// Lista de objetos de tipo asset
	private static final String[] ASSETS_TYPES = { "AR3DModelAsset", "ARImageAsset", "ARTextAsset" };
	// Listas de terminaciones de ficheros
	private static final String[] MODEL_FILETYPES = { "md2", "obj", "3ds", "asc","bones" };

	private static final String[] MATERIAL_FILETYPES = { "mtl" };

	private static final String[] IMAGE_FILETYPES = { "png", "jpg", "jpeg" };

	private final Map<ComponentCategory, PaletteHelper> paletteHelpers;

	/**
	 * Creates a new component palette panel.
	 *
	 * @param editor
	 *            parent editor of this panel
	 */
	public YoungAndroidPalettePanel(YaFormEditor editor) {
		this.editor = editor;

		stackPalette = new StackPanel();
		paletteHelpers = new HashMap<ComponentCategory, PaletteHelper>();
		// If a category has a palette helper, add it to the paletteHelpers map
		// here.
		paletteHelpers.put(ComponentCategory.LEGOMINDSTORMS, new NxtPaletteHelper());
		categoryPanels = new HashMap<ComponentCategory, VerticalPanel>();

		for (ComponentCategory category : ComponentCategory.values()) {
			if (showCategory(category)) {
				VerticalPanel categoryPanel = new VerticalPanel();
				categoryPanel.setWidth("100%");
				categoryPanels.put(category, categoryPanel);
				stackPalette.add(categoryPanel, TranslationDesignerPallete.getCorrespondingString(category.getName()));
			}
		}

		stackPalette.setWidth("100%");
		initWidget(stackPalette);
	}

	private static boolean showCategory(ComponentCategory category) {
		if (category == ComponentCategory.UNINITIALIZED) {
			return false;
		}
		if (category == ComponentCategory.INTERNAL && !AppInventorFeatures.showInternalComponentsCategory()) {
			return false;
		}
		return true;
	}

	/**
	 * Loads all components to be shown on this palette. Specifically, for each
	 * component (except for those whose category is UNINITIALIZED, or whose
	 * category is INTERNAL and we're running on a production server, or who are
	 * specifically marked as not to be shown on the palette), this creates a
	 * corresponding {@link SimplePaletteItem} with the passed
	 * {@link DropTargetProvider} and adds it to the panel corresponding to its
	 * category.
	 *
	 * @param dropTargetProvider
	 *            provider of targets that palette items can be dropped on
	 */
	@Override
	public void loadComponents(DropTargetProvider dropTargetProvider) {
		for (String component : COMPONENT_DATABASE.getComponentNames()) {
			String categoryString = COMPONENT_DATABASE.getCategoryString(component);
			String helpString = COMPONENT_DATABASE.getHelpString(component);
			String categoryDocUrlString = COMPONENT_DATABASE.getCategoryDocUrlString(component);
			Boolean showOnPalette = COMPONENT_DATABASE.getShowOnPalette(component);
			Boolean nonVisible = COMPONENT_DATABASE.getNonVisible(component);
			ComponentCategory category = ComponentCategory.valueOf(categoryString);
			if (showOnPalette && showCategory(category)) {
				addPaletteItem(new SimplePaletteItem(new SimpleComponentDescriptor(component, editor, helpString,
						categoryDocUrlString, showOnPalette, nonVisible), dropTargetProvider), category);
			}
		}
	}

	@Override
	public void configureComponent(MockComponent mockComponent) {
		for (PropertyDefinition property : COMPONENT_DATABASE.getPropertyDefinitions(mockComponent.getType())) {
			mockComponent.addProperty(property.getName(), property.getDefaultValue(),
					ComponentsTranslation.getPropertyName(property.getCaption()),
					createPropertyEditor(property.getEditorType(), mockComponent.getType()));
			/*
			 * OdeLog.log("Property Caption: " + property.getCaption() + ", " +
			 * TranslationComponentProperty.getName(property.getCaption()));
			 */
		}
	}

	/*
	 * Creates a new property editor.
	 */
	private PropertyEditor createPropertyEditor(String editorType, String componentType) {
		if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_HORIZONTAL_ALIGNMENT)) {
			return new YoungAndroidHorizontalAlignmentChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_VERTICAL_ALIGNMENT)) {
			return new YoungAndroidVerticalAlignmentChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_ASSET)) {
			return new YoungAndroidAssetSelectorPropertyEditor(editor);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_BLUETOOTHCLIENT)) {
			return new YoungAndroidComponentSelectorPropertyEditor(editor,
					// Pass the set of component types that will be shown in the
					// property editor,
					// in this case, just "BluetoothClient".
					Collections.singleton("BluetoothClient"), false);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN)) {
			return new YoungAndroidBooleanPropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_BUTTON_SHAPE)) {
			return new YoungAndroidButtonShapeChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_COLOR)) {
			return new YoungAndroidColorChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_COMPONENT)) {
			return new YoungAndroidComponentSelectorPropertyEditor(editor);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_FLOAT)) {
			return new FloatPropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_INTEGER)) {
			return new IntegerPropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_LEGO_NXT_SENSOR_PORT)) {
			return new YoungAndroidLegoNxtSensorPortChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_LEGO_NXT_GENERATED_COLOR)) {
			return new YoungAndroidColorChoicePropertyEditor(
					YoungAndroidColorChoicePropertyEditor.NXT_GENERATED_COLORS);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT)) {
			return new NonNegativeFloatPropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER)) {
			return new NonNegativeIntegerPropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_SCREEN_ORIENTATION)) {
			return new YoungAndroidScreenOrientationChoicePropertyEditor();
		}else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_QUALITY_YOUTUBE)) {
				return new YoungAndroidQualityYoutubeChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_SCREEN_ANIMATION)) {
			return new YoungAndroidScreenAnimationChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_SENSOR_DIST_INTERVAL)) {
			return new YoungAndroidSensorDistIntervalChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_SENSOR_TIME_INTERVAL)) {
			return new YoungAndroidSensorTimeIntervalChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_STRING)) {
			return new StringPropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_TEXTALIGNMENT)) {
			return new YoungAndroidAlignmentChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_TEXTAREA)) {
			return new TextAreaPropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_TOAST_LENGTH)) {
			return new YoungAndroidToastLengthChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_TYPEFACE)) {
			return new YoungAndroidFontTypefaceChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_CHECKABLETREEFORACTIVITYTRACKER)) {
			return new YoungAndroidCheckableTreeSelectorForActivityTracker(editor, COMPONENT_DATABASE, componentType);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_TEXTBOX_AND_HYPERLINK_FORACTIVITYTRACKER)) {
			return new StringAndHyperlinkPropertyEditorForActivityTracker(editor);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_CONSTANT_HYPERLINK)) {
			return new YoungAndroidAnchorProperty();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_SYNCHRONIZATIONMODE)) {
			return new YoungAndroidSynchronizationModeChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_CHARTTYPE)) {
			return new YoungAndroidChartTypeChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_CHECKABLETREEFORDATA)) {
			return new YoungAndroidCheckableTreeSelectorForData(editor, COMPONENT_DATABASE);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_CHECKABLETREEFORAGGREGATEDDATA)) {
			return new YoungAndroidCheckableTreeSelectorForAggregatedData(editor, COMPONENT_DATABASE);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_COLUMNTOAGGREGATE)) {
			return new YoungAndroidColumntToAggregateChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_COMMUNICATIONMODE)) {
			return new YoungAndroidCommunicationModeChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_STORAGEMODE)) {
			return new YoungAndroidStorageModeChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_VISIBILITY)) {
			return new YoungAndroidBooleanPropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_TEXT_RECEIVING)) {
			return new YoungAndroidTextReceivingPropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_ACCELEROMETER_SENSITIVITY)) {
			return new YoungAndroidAccelerometerSensitivityChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_TEXT_TO_SPEECH_COUNTRIES)) {
			return new CountryChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_TEXT_TO_SPEECH_LANGUAGES)) {
			return new LanguageChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_SIZING)) {
			return new YoungAndroidSizingChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_KIND_OF_TRACKERS)) {
			return new YoungAndroidComponentSelectorPropertyEditor(editor,
					new HashSet<String>(Arrays.asList(TRACKERS_TYPES)), false);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_KIND_OF_VISUALASSETS)) {
			return new YoungAndroidComponentSelectorPropertyEditor(editor,
					new HashSet<String>(Arrays.asList(ASSETS_TYPES)), false);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_ONLY_VRSCENE)) {
			return new YoungAndroidComponentSelectorPropertyEditor(editor, Collections.singleton("VRScene"), false);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_ONLY_TTS)) {
			return new YoungAndroidComponentSelectorPropertyEditor(editor, Collections.singleton("TextToSpeech"), false);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_ONLY_ARCAMERA)) {
			return new YoungAndroidComponentSelectorPropertyEditor(editor, Collections.singleton("ARCamera"), false);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_ONLY_ARCAMERAOVERLAYER)) {
			return new YoungAndroidComponentSelectorPropertyEditor(editor, Collections.singleton("ARCameraOverLayer"), false);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_ONLY_QUERY)) {

			HashSet<String> set = new HashSet<String>();
			set.add("ActivitySimpleQuery");
			set.add("ActivityAggregationQuery");
			return new YoungAndroidComponentSelectorPropertyEditor(editor, set, false);

		} else if(editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_ONLY_USER)) {
			HashSet<String> set = new HashSet<String>();
			set.add("User");
			return new YoungAndroidComponentSelectorPropertyEditor(editor, set, true);
			
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_ASSET_3DMODEL)) {
			return new YoungAndroidAssetSelectorPropertyEditor(editor, MODEL_FILETYPES);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_ASSET_MATERIAL)) {
			return new YoungAndroidAssetSelectorPropertyEditor(editor, MATERIAL_FILETYPES);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_ASSET_IMAGE)) {
			return new YoungAndroidAssetSelectorPropertyEditor(editor, IMAGE_FILETYPES);
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_ASSET_DATABASE_DAT)) {
			return new YoungAndroidAssetSelectorPropertyEditor(editor, new String[] { "dat" });
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_ASSET_DATABASE_XML)) {
			return new YoungAndroidAssetSelectorPropertyEditor(editor, new String[] { "xml" });
		}else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_ASSET_TEXTURES_LIST)) {
			return new YoungAndroidCheckableTreeSelectorForTextures(editor,IMAGE_FILETYPES);
		}
		else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_SCALING)) {
			return new ScalingChoicePropertyEditor();
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_FIREBASE_URL)) {
			return new YoungAndroidDefaultURLPropertyEditor("DEFAULT");
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_EEG)) {
			return new YoungAndroidEEGDeviceChoicePropertyEditor();	
		} else if (editorType.equals(PropertyTypeConstants.PROPERTY_TYPE_TREEFORSEMANTICTYPE)) {
			return new YoungAndroidTreeSelectorForSemanticType(editor);
		} else {
			return new TextPropertyEditor();
		}
	}

	/*
	 * Adds a component entry to the palette.
	 */
	private void addPaletteItem(SimplePaletteItem component, ComponentCategory category) {
		VerticalPanel panel = categoryPanels.get(category);
		PaletteHelper paletteHelper = paletteHelpers.get(category);
		if (paletteHelper != null) {
			paletteHelper.addPaletteItem(panel, component);
		} else {
			panel.add(component);
		}
	}
}
