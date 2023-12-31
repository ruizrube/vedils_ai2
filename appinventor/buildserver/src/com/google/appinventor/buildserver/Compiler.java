// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.buildserver;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import com.android.sdklib.build.ApkBuilder;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Main entry point for the YAIL compiler.
 *
 * <p>
 * Supplies entry points for building Young Android projects.
 *
 * @author markf@google.com (Mark Friedman)
 * @author lizlooney@google.com (Liz Looney)
 */
public final class Compiler {
	public static int currentProgress = 10;

	// Kawa and DX processes can use a lot of memory. We only launch one Kawa or
	// DX process at a time.
	private static final Object SYNC_KAWA_OR_DX = new Object();

	// IRR
	private static final String ARSCENE_ACTIVITY_CLASS = "com.google.appinventor.components.runtime.ar4ai.vuforia.VuforiaARActivity";

	//3DAI
	private static final String JPCT_ACTIVITY_CLASS = "com.google.appinventor.components.runtime.jpctsource.JPCTActivity";
	private static final String JPCT_ACTIVITY_ACTION = "com.mobile.jpctlauncher.LAUNCH_ACTIVITY";
	
	//VR4AI
	
	private static final String VR_ACTIVITY_CLASS = "com.google.appinventor.components.runtime.vr4ai.VRActivity";

	//Edson
	private static final String GVR_ACTIVITY_CLASS = "com.google.appinventor.components.runtime.gvr.VideoActivity";	

	// IRR
	private static final String ARSCENE_JPCT_SHADERS = "jpct_shaders.zip";
	// "/Users/ivanruizrube/Documents/Proyectos/workspaceRA/appinventor/lib/ar4ai/jpct_shaders.zip";

	// TODO(sharon): temporary until we add support for new activities
	private static final String LIST_ACTIVITY_CLASS = "com.google.appinventor.components.runtime.ListPickerActivity";

	private static final String WEBVIEW_ACTIVITY_CLASS = "com.google.appinventor.components.runtime.WebViewActivity";

	// Copied from SdkLevel.java (which isn't in our class path so we duplicate
	// it here)
	private static final String LEVEL_GINGERBREAD_MR1 = "10";
	private static final String LEVEL_LOLLIPOP = "21";

	public static final String RUNTIME_FILES_DIR = "/files/";

	// Build info constants. Used for permissions, libraries and assets.
	private static final String ARMEABI_V7A_DIRECTORY = "armeabi-v7a";
	// Must match ComponentProcessor.ARMEABI_V7A_SUFFIX
	private static final String ARMEABI_V7A_SUFFIX = "-v7a";
	// Must match ComponentListGenerator.PERMISSIONS_TARGET
	private static final String PERMISSIONS_TARGET = "permissions";
	// Must match ComponentListGenerator.LIBRARIES_TARGET
	public static final String LIBRARIES_TARGET = "libraries";
	// Must match ComponentListGenerator.NATIVE_TARGET
	public static final String NATIVE_TARGET = "native";
	// Must match ComponentListGenerator.ASSETS_TARGET
	private static final String ASSETS_TARGET = "assets";
	// Must match Component.ASSET_DIRECTORY
	private static final String ASSET_DIRECTORY = "component";

	// Native library directory names
	private static final String LIBS_DIR_NAME = "libs";
	private static final String APK_LIB_DIR_NAME = "lib";
	private static final String ARMEABI_DIR_NAME = "armeabi";
	private static final String ARMEABI_V7A_DIR_NAME = "armeabi-v7a";

	private static final String DEFAULT_ICON = RUNTIME_FILES_DIR + "ya.png";

	private static final String DEFAULT_VERSION_CODE = "1";
	private static final String DEFAULT_VERSION_NAME = "1.0";
	private static final String DEFAULT_APP_NAME = "";
	private static final String DEFAULT_MIN_SDK = "8";

	private static final String COMPONENT_BUILD_INFO = RUNTIME_FILES_DIR + "simple_components_build_info.json";

	/*
	 * Resource paths to yail runtime, runtime library files and sdk tools. To
	 * get the real file paths, call getResource() with one of these constants.
	 */
	private static final String SIMPLE_ANDROID_RUNTIME_JAR = RUNTIME_FILES_DIR + "AndroidRuntime.jar";
	private static final String ANDROID_RUNTIME = RUNTIME_FILES_DIR + "android.jar";
	private static final String MAC_AAPT_TOOL = "/tools/mac/aapt";
	private static final String WINDOWS_AAPT_TOOL = "/tools/windows/aapt";
	private static final String LINUX_AAPT_TOOL = "/tools/linux/aapt";
	private static final String KAWA_RUNTIME = RUNTIME_FILES_DIR + "kawa.jar";
	private static final String ACRA_RUNTIME = RUNTIME_FILES_DIR + "acra-4.4.0.jar";
	private static final String DX_JAR = RUNTIME_FILES_DIR + "dx.jar";

	@VisibleForTesting
	static final String YAIL_RUNTIME = RUNTIME_FILES_DIR + "runtime.scm";
	private static final String MAC_ZIPALIGN_TOOL = "/tools/mac/zipalign";
	private static final String WINDOWS_ZIPALIGN_TOOL = "/tools/windows/zipalign";
	private static final String LINUX_ZIPALIGN_TOOL = "/tools/linux/zipalign";

	// Logging support
	private static final Logger LOG = Logger.getLogger(Compiler.class.getName());

	private final ConcurrentMap<String, Set<String>> componentPermissions = new ConcurrentHashMap<String, Set<String>>();

	private final ConcurrentMap<String, Set<String>> componentLibraries = new ConcurrentHashMap<String, Set<String>>();

	private final ConcurrentMap<String, Set<String>> componentNativeLibraries = new ConcurrentHashMap<String, Set<String>>();

	private final ConcurrentMap<String, Set<String>> componentAssets = new ConcurrentHashMap<String, Set<String>>();

	/**
	 * Map used to hold the names and paths of resources that we've written out
	 * as temp files. Don't use this map directly. Please call getResource()
	 * with one of the constants above to get the (temp file) path to a
	 * resource.
	 */
	private static final ConcurrentMap<String, File> resources = new ConcurrentHashMap<String, File>();

	// TODO(user,lizlooney): i18n here and in lines below that call
	// String.format(...)
	private static final String ERROR_IN_STAGE = "Error: Your build failed due to an error in the %s stage, "
			+ "not because of an error in your program.\n";
	private static final String ICON_ERROR = "Error: Your build failed because %s cannot be used as the application icon.\n";
	private static final String NO_USER_CODE_ERROR = "Error: No user code exists.\n";
	private static final String COMPILATION_ERROR = "Error: Your build failed due to an error when compiling %s.\n";

	private final Project project;
	private final Set<String> componentTypes;
	private final PrintStream out;
	private final PrintStream err;
	private final PrintStream userErrors;
	private final boolean isForCompanion;
	// Maximum ram that can be used by a child processes, in MB.
	private final int childProcessRamMb;
	private Set<String> librariesNeeded; // Set of component libraries
	private Set<String> nativeLibrariesNeeded; // Set of component native
												// libraries
	private Set<String> assetsNeeded; // Set of component assets
	private File libsDir; // The directory that will contain any native
							// libraries for packaging
	private String dexCacheDir;
	private boolean hasSecondDex = false; // True if classes2.dex should be
											// added to the APK
	private boolean hasThirdDex = false;
	
	private int numberDexFiles = 2;
	
	/*
	 * Generate the set of Android permissions needed by this project.
	 */
	@VisibleForTesting
	Set<String> generatePermissions() {
		// Before we can use componentPermissions, we have to call
		// loadJsonInfo() for permissions.
		try {
			loadJsonInfo(componentPermissions, PERMISSIONS_TARGET);
			if (project != null) { // Only do this if we have a project (testing
									// doesn't provide one :-( ).
				LOG.log(Level.INFO, "usesLocation = " + project.getUsesLocation());
				if (project.getUsesLocation().equals("True")) { // Add location
																// permissions
																// if any
																// WebViewer
																// requests it
					Set<String> locationPermissions = Sets.newHashSet(); // via
																			// a
																			// Property.
					// See ProjectEditor.recordLocationSettings()
					locationPermissions.add("android.permission.ACCESS_FINE_LOCATION");
					locationPermissions.add("android.permission.ACCESS_COARSE_LOCATION");
					locationPermissions.add("android.permission.ACCESS_MOCK_LOCATION");
					componentPermissions.put("WebViewer", locationPermissions);
				}
			}
		} catch (IOException e) {
			// This is fatal.
			e.printStackTrace();
			userErrors.print(String.format(ERROR_IN_STAGE, "Permissions"));
			return null;
		} catch (JSONException e) {
			// This is fatal, but shouldn't actually ever happen.
			e.printStackTrace();
			userErrors.print(String.format(ERROR_IN_STAGE, "Permissions"));
			return null;
		}

		Set<String> permissions = Sets.newHashSet();
		for (String componentType : componentTypes) {
			permissions.addAll(componentPermissions.get(componentType));
		}
		if (isForCompanion) { // This is so ACRA can do a logcat on phones older
								// then Jelly Bean
			permissions.add("android.permission.READ_LOGS");
		}

		return permissions;
	}

	/*
	 * Generate the set of Android libraries needed by this project.
	 */
	@VisibleForTesting
	void generateLibraryNames() {
		// Before we can use componentLibraries, we have to call
		// loadComponentLibraries().
		try {
			loadJsonInfo(componentLibraries, LIBRARIES_TARGET);
		} catch (IOException e) {
			// This is fatal.
			e.printStackTrace();
			userErrors.print(String.format(ERROR_IN_STAGE, "Libraries"));
		} catch (JSONException e) {
			// This is fatal, but shouldn't actually ever happen.
			e.printStackTrace();
			userErrors.print(String.format(ERROR_IN_STAGE, "Libraries"));
		}

		librariesNeeded = Sets.newHashSet();
		for (String componentType : componentTypes) {
			librariesNeeded.addAll(componentLibraries.get(componentType));
		}
		
		removeSimilarLibraries(librariesNeeded);
		System.out.println("Libraries needed, n= " + librariesNeeded.size());
	}

	private void removeSimilarLibraries(Set<String> librariesNeeded) {
		//if(librariesNeeded.contains("google-http-client-android3-beta.jar") && librariesNeeded.contains("google-http-client-android2-beta.jar") ) {
		//	librariesNeeded.remove("google-http-client-android2-beta.jar");
		//} Fail for FusionTables and ActivityTracker FusionTables mode.
		
		if(librariesNeeded.contains("gson-2.1.jar") && librariesNeeded.contains("gson-2.8.1.jar") ) {
			librariesNeeded.remove("gson-2.1.jar");
		}
		
		if(librariesNeeded.contains("slf4j-api-1.7.25.jar") && librariesNeeded.contains("ld4ai-0.1.jar") ) {
			librariesNeeded.remove("slf4j-api-1.7.25.jar");
		}
		
		
	}

	/*
	 * Generate the set of conditionally included libraries needed by this
	 * project.
	 */
	@VisibleForTesting
	void generateNativeLibraryNames() {
		// Before we can use componentLibraries, we have to call
		// loadComponentLibraries().
		try {
			loadJsonInfo(componentNativeLibraries, NATIVE_TARGET);
		} catch (IOException e) {
			// This is fatal.
			e.printStackTrace();
			userErrors.print(String.format(ERROR_IN_STAGE, "Native Libraries"));
		} catch (JSONException e) {
			// This is fatal, but shouldn't actually ever happen.
			e.printStackTrace();
			userErrors.print(String.format(ERROR_IN_STAGE, "Native Libraries"));
		}

		nativeLibrariesNeeded = Sets.newHashSet();
		for (String componentType : componentTypes) {
			nativeLibrariesNeeded.addAll(componentNativeLibraries.get(componentType));
		}
		System.out.println("Native Libraries needed, n= " + nativeLibrariesNeeded.size());
	}

	/*
	 * Generate the set of conditionally included assets needed by this project.
	 */
	@VisibleForTesting
	void generateAssets() {
		// Before we can use componentAssets, we have to call loadJsonInfo() for
		// assets.
		try {
			loadJsonInfo(componentAssets, ASSETS_TARGET);
		} catch (IOException e) {
			// This is fatal.
			e.printStackTrace();
			userErrors.print(String.format(ERROR_IN_STAGE, "Assets"));
		} catch (JSONException e) {
			// This is fatal, but shouldn't actually ever happen.
			e.printStackTrace();
			userErrors.print(String.format(ERROR_IN_STAGE, "Assets"));
		}

		assetsNeeded = Sets.newHashSet();
		for (String componentType : componentTypes) {
			assetsNeeded.addAll(componentAssets.get(componentType));
		}
		System.out.println("Component assets needed, n= " + assetsNeeded.size());
	}

	// This patches around a bug in AAPT (and other placed in Android)
	// where an ampersand in the name string breaks AAPT.
	private String cleanName(String name) {
		return name.replace("&", "and");
	}

	/*
	 * Creates an AndroidManifest.xml file needed for the Android application.
	 */
	private boolean writeAndroidManifest(File manifestFile, Set<String> permissionsNeeded) {
		// Create AndroidManifest.xml
		String mainClass = project.getMainClass();
		String packageName = Signatures.getPackageName(mainClass);
		String className = Signatures.getClassName(mainClass);
		String projectName = project.getProjectName();
		String vCode = (project.getVCode() == null) ? DEFAULT_VERSION_CODE : project.getVCode();
		String vName = (project.getVName() == null) ? DEFAULT_VERSION_NAME : cleanName(project.getVName());
		String aName = (project.getAName() == null) ? DEFAULT_APP_NAME : cleanName(project.getAName());
		String minSDK = DEFAULT_MIN_SDK;
		LOG.log(Level.INFO, "VCode: " + project.getVCode());
		LOG.log(Level.INFO, "VName: " + project.getVName());

		// TODO(user): Use com.google.common.xml.XmlWriter
		try {
			BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(manifestFile), "UTF-8"));
			out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			// TODO(markf) Allow users to set versionCode and versionName
			// attributes.
			// See http://developer.android.com/guide/publishing/publishing.html
			// for
			// more info.
			out.write("<manifest " + "xmlns:android=\"http://schemas.android.com/apk/res/android\" " + "package=\""
					+ packageName + "\" " +
					// TODO(markf): uncomment the following line when we're
					// ready to enable publishing to the
					// Android Market.
					"android:versionCode=\"" + vCode + "\" " + "android:versionName=\"" + vName + "\" " + ">\n");

			// If we are building the Wireless Debugger (AppInventorDebugger)
			// add the uses-feature tag which
			// is used by the Google Play store to determine which devices the
			// app is available for. By adding
			// these lines we indicate that we use these features BUT THAT THEY
			// ARE NOT REQUIRED so it is ok
			// to make the app available on devices that lack the feature.
			// Without these lines the Play Store
			// makes a guess based on permissions and assumes that they are
			// required features.
			if (isForCompanion) {
				out.write(
						"  <uses-feature android:name=\"android.hardware.bluetooth\" android:required=\"false\" />\n");
				out.write("  <uses-feature android:name=\"android.hardware.location\" android:required=\"false\" />\n");
				out.write(
						"  <uses-feature android:name=\"android.hardware.telephony\" android:required=\"false\" />\n");
				out.write(
						"  <uses-feature android:name=\"android.hardware.location.network\" android:required=\"false\" />\n");
				out.write(
						"  <uses-feature android:name=\"android.hardware.location.gps\" android:required=\"false\" />\n");
				out.write(
						"  <uses-feature android:name=\"android.hardware.microphone\" android:required=\"false\" />\n");
				out.write(
						"  <uses-feature android:name=\"android.hardware.touchscreen\" android:required=\"false\" />\n");
				out.write("  <uses-feature android:name=\"android.hardware.camera\" android:required=\"false\" />\n");
				out.write(
						"  <uses-feature android:name=\"android.hardware.camera.autofocus\" android:required=\"false\" />\n");
				out.write("  <uses-feature android:name=\"android.hardware.wifi\" />\n"); // We
																							// actually
																							// require
																							// wifi
			}

			// Firebase requires at least API 10 (Gingerbread MR1)
			if (componentTypes.contains("FirebaseDB") && !isForCompanion) {
				minSDK = LEVEL_GINGERBREAD_MR1;
			}

			// BrainwaveSensor requires at least API 21 (Lollipop)
			if ((componentTypes.contains("BrainwaveSensor")||componentTypes.contains("VRScene")) && !isForCompanion) {
				minSDK = LEVEL_LOLLIPOP;
			}
			

			for (String permission : permissionsNeeded) {
				out.write("  <uses-permission android:name=\"" + permission + "\" />\n");
			}

			// TODO(markf): Change the minSdkVersion below if we ever require an
			// SDK beyond 1.5.
			// The market will use the following to filter apps shown to devices
			// that don't support
			// the specified SDK version. We might also want to allow users to
			// specify minSdkVersion
			// or have us specify higher SDK versions when the program uses a
			// component that uses
			// features from a later SDK (e.g. Bluetooth).
			out.write("  <uses-sdk android:minSdkVersion=\"" + minSDK + "\" android:targetSdkVersion=\"26\" />\n");

			// IRR out.write(" <uses-sdk android:minSdkVersion=\"3\" />\n");

			// IRR out.write(" <uses-feature android:glEsVersion=\"0x00020000\"
			// android:required=\"true\" />");

			// If we set the targetSdkVersion to 4, we can run full size apps on
			// tablets.
			// On non-tablet hi-res devices like a Nexus One, the screen
			// dimensions will be the actual
			// device resolution. Unfortunately, images, canvas, sprites, and
			// buttons with images are not
			// sized appropriately. For example, an image component with an
			// image that is 60x60, width
			// and height properties set to automatic, is sized as 40x40. So
			// they appear on the screen
			// much smaller than they should be. There is code in Canvas and
			// ImageSprite to work around
			// this problem, but images and buttons are still an unsolved
			// problem. We'll have to solve
			// that before we can set the targetSdkVersion to 4 here.
			// out.write(" <uses-sdk android:targetSdkVersion=\"4\" />\n");

			// The market will use the following to filter apps shown to devices
			// that don't support
			// the specified SDK version. We right now support building for
			// minSDK 4.
			// We might also want to allow users to specify minSdk version or
			// targetSDK version.

			// Comentado por version seleccionada para IRR (arriba)
			// out.write(" <uses-sdk android:minSdkVersion=\"" + minSDK + "\"
			// />\n");

			out.write("  <application ");

			// TODO(markf): The preparing to publish doc at
			// http://developer.android.com/guide/publishing/preparing.html
			// suggests removing the
			// 'debuggable=true' but I'm not sure that our users would want that
			// while they're still
			// testing their packaged apps. Maybe we should make that an option,
			// somehow.
			// TODONE(jis): Turned off debuggable. No one really uses it and it
			// represents a security
			// risk for App Inventor App end-users.
			out.write("android:debuggable=\"false\" ");
			if (aName.equals("")) {
				out.write("android:label=\"" + projectName + "\" ");
			} else {
				out.write("android:label=\"" + aName + "\" ");
			}
			out.write("android:icon=\"@drawable/ya\" ");
			if (isForCompanion) { // This is to hook into ACRA
				out.write("android:name=\"com.google.appinventor.components.runtime.ReplApplication\" ");
			} else {
				out.write("android:name=\"com.google.appinventor.components.runtime.multidex.MultiDexApplication\" ");
			}

			// IRR: Material design requires Android 5.0 (API nivel 21)
			if(Integer.valueOf(minSDK).intValue() >= Integer.valueOf(LEVEL_LOLLIPOP).intValue()){
				out.write("android:theme=\"@android:style/Theme.Material.Light\"");			
			}
			
			//For HTML5 video support on WebViewer
			if(componentTypes.contains("WebViewer")) {
				out.write("android:hardwareAccelerated=\"true\""); //Only works in API level 11+
			}
	
			out.write(">\n");

			for (Project.SourceDescriptor source : project.getSources()) {
				String formClassName = source.getQualifiedName();
				// String screenName =
				// formClassName.substring(formClassName.lastIndexOf('.') + 1);
				boolean isMain = formClassName.equals(mainClass);

				if (isMain) {
					// The main activity of the application.
					out.write("    <activity android:name=\"." + className + "\" ");
				} else {
					// A secondary activity of the application.
					out.write("    <activity android:name=\"" + formClassName + "\" ");
				}

				// This line is here for NearField and NFC. It keeps the
				// activity from
				// restarting every time NDEF_DISCOVERED is signaled.
				// TODO: Check that this doesn't screw up other components.
				// Also, it might be
				// better to do this programmatically when the NearField
				// component is created, rather
				// than here in the manifest.
				if (componentTypes.contains("NearField") && !isForCompanion && isMain) {
					out.write("android:launchMode=\"singleTask\" ");
				} else if (isMain && isForCompanion) {
					out.write("android:launchMode=\"singleTop\" ");
				}

				out.write("android:windowSoftInputMode=\"stateHidden\" ");

				// The keyboard option prevents the app from stopping when a
				// external (bluetooth)
				// keyboard is attached.
				out.write("android:configChanges=\"orientation|keyboardHidden|keyboard\">\n");
				out.write("      <intent-filter>\n");
				out.write("        <action android:name=\"android.intent.action.MAIN\" />\n");
				if (isMain) {
					out.write("        <category android:name=\"android.intent.category.LAUNCHER\" />\n");
				}
				if (componentTypes.contains("VRScene")) 
				{
					out.write("        <category android:name=\"com.google.intent.category.CARDBOARD\" />\n");
				}
				out.write("      </intent-filter>\n");

				if (componentTypes.contains("NearField") && !isForCompanion && isMain) {
					// make the form respond to NDEF_DISCOVERED
					// this will trigger the form's onResume method
					// For now, we're handling text/plain only,but we can add
					// more and make the Nearfield
					// component check the type.
					out.write("      <intent-filter>\n");
					out.write("        <action android:name=\"android.nfc.action.NDEF_DISCOVERED\" />\n");
					out.write("        <category android:name=\"android.intent.category.DEFAULT\" />\n");
					out.write("        <data android:mimeType=\"text/plain\" />\n");
					out.write("      </intent-filter>\n");
				}
				out.write("    </activity>\n");
			}

			// Add ListPickerActivity to the manifest only if a ListPicker
			// component is used in the app
			if (componentTypes.contains("ListPicker")) {
				out.write("    <activity android:name=\"" + LIST_ACTIVITY_CLASS + "\" "
						+ "android:configChanges=\"orientation|keyboardHidden\" "
						+ "android:screenOrientation=\"behind\">\n");
				out.write("    </activity>\n");
			}

			// IRR
			// Add VuforiaARActivity to the manifest only if a ARScene component
			// is used in the app
			if (componentTypes.contains("ARCamera")) {
				out.write("    <activity android:name=\"" + ARSCENE_ACTIVITY_CLASS + "\" "
						+ "android:configChanges=\"orientation|keyboardHidden|screenSize|smallestScreenSize\" "
						+ "android:screenOrientation=\"behind\">\n");
				out.write("    </activity>\n");
			}
			
			// LA4AI
			
			//Add permissions to set background service
			if(componentTypes.contains("ActivityTracker")) {
				
				/*
				 * <service
    android:name=".MyService"
    android:label="@string/app_name"
    android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE">
       <intent-filter>
          <action android:name="android.accessibilityservice.AccessibilityService" />
       </intent-filter>
 </service>
				 * 
				 * 
				 */
				
				/*
				
				out.write("<service \n");
				out.write(
						"android:name=\"com.google.appinventor.components.runtime.ActivityTrackerBackgroundService\" \n");
				out.write("android:enabled=\"true\" \n");
				out.write("android:exported=\"false\" \n");
				out.write("android:permission=\"android.permission.BIND_ACCESSIBILITY_SERVICE\"> \n");
				out.write("<intent-filter> \n");
				out.write("<action android:name=\"android.accessibilityservice.AccessibilityService\" /> \n");
				out.write("</intent-filter> \n");
				out.write("</service> \n");
				
				*/
				
			}
			
			// 3D4Ai
					
						// is used in the app
						if (componentTypes.contains("Model3DViewer")) {
							out.write("    <activity android:name=\"" + JPCT_ACTIVITY_CLASS + "\" "
									+ "android:configChanges=\"orientation|keyboardHidden|screenSize|smallestScreenSize\" "
									+ "android:screenOrientation=\"behind\">\n");
							out.write("    </activity>\n");
						}
						
			//VR4AI
				
						if (componentTypes.contains("VRScene")) {
							out.write("    <activity android:name=\"" + VR_ACTIVITY_CLASS + "\" "
									+ "android:configChanges=\"orientation|screenSize|uiMode|navigation\" "
									+ "android:theme=\"@android:style/Theme.NoTitleBar.Fullscreen\" "
									+ "android:screenOrientation=\"landscape\">\n");
							out.write("    </activity>\n");
						}

			// Edson GVR4AI
						
						if (componentTypes.contains("VRScene")) {
							out.write("    <activity android:name=\"" + GVR_ACTIVITY_CLASS + "\" "
									+ "android:configChanges=\"orientation|screenSize|uiMode|navigation\" "
									//+ "android:configChanges=\"orientation|screenSize\" "
									+ "android:theme=\"@android:style/Theme.NoTitleBar.Fullscreen\" "
									+ "android:screenOrientation=\"landscape\">\n");
							out.write("    </activity>\n");
						}

			// Add permiss to get notifications.
			if (componentTypes.contains("GoogleCloudMessaging")) {

				// PERMISSIONS

				out.write(
						"<permission android:name=\"com.google.appinventor.permission.C2D_MESSAGE\" android:protectionLevel=\"signature\" />\n");
				out.write("<uses-permission android:name=\"com.google.appinventor.permission.C2D_MESSAGE\" />\n");
				out.write("<permission android:name=\"" + packageName
						+ ".permission.C2D_MESSAGE\" android:protectionLevel=\"signature\" />\n");
				out.write("<uses-permission android:name=\"" + packageName + ".permission.C2D_MESSAGE\" />\n");

				// LISTENER

				out.write("<service \n");
				out.write(
						"android:name=\"com.google.appinventor.components.runtime.util.GoogleCloudMessagingTokenRefreshService\" \n");
				out.write("android:exported=\"false\" > \n");
				out.write("<intent-filter> \n");
				out.write("<action android:name=\"com.google.android.gms.iid.InstanceID\" /> \n");
				out.write("</intent-filter> \n");
				out.write("</service> \n");

				out.write("<service \n");
				out.write(
						"android:name=\"com.google.appinventor.components.runtime.util.GoogleCloudMessagingListenerService\" \n");
				// out.write("android:exported=\"false\" > \n");
				out.write("android:enabled=\"true\" \n");
				out.write("android:exported=\"true\" >\n ");
				out.write("<intent-filter> \n");
				out.write("<action android:name=\"com.google.android.c2dm.intent.RECEIVE\" /> \n");
				out.write("</intent-filter> \n");
				out.write("</service> \n");

				// RECEIVER

				out.write("<receiver \n");
				out.write("android:name=\"com.google.android.gms.gcm.GcmReceiver\" \n");
				out.write("android:exported=\"true\" \n");
				out.write("android:permission=\"com.google.android.c2dm.permission.SEND\" > \n");
				out.write("<intent-filter> \n");
				out.write("<action android:name=\"com.google.android.c2dm.intent.RECEIVE\" /> \n");
				out.write("<category android:name=\"com.example.gcm\" /> \n");
				out.write("</intent-filter> \n");
				out.write("</receiver> \n");

			}

			// Add WebViewActivity to the manifest only if a Twitter component
			// is used in the app
			if (componentTypes.contains("Twitter")) {
				out.write("    <activity android:name=\"" + WEBVIEW_ACTIVITY_CLASS + "\" "
						+ "android:configChanges=\"orientation|keyboardHidden\" "
						+ "android:screenOrientation=\"behind\">\n");
				out.write("      <intent-filter>\n");
				out.write("        <action android:name=\"android.intent.action.MAIN\" />\n");
				out.write("      </intent-filter>\n");
				out.write("    </activity>\n");
			}

			if (componentTypes.contains("BarcodeScanner")) {
				// Barcode Activity
				out.write("    <activity android:name=\"com.google.zxing.client.android.AppInvCaptureActivity\"\n");
				out.write("              android:screenOrientation=\"landscape\"\n");
				out.write("              android:stateNotNeeded=\"true\"\n");
				out.write("              android:configChanges=\"orientation|keyboardHidden\"\n");
				out.write("              android:theme=\"@android:style/Theme.NoTitleBar.Fullscreen\"\n");
				out.write("              android:windowSoftInputMode=\"stateAlwaysHidden\" />\n");
			}

			// Edson Remove Google's Forbidden Permissions
			// BroadcastReceiver for Texting Component
			/*if (componentTypes.contains("Texting")) {
				System.out.println("Android Manifest: including <receiver> tag");
				out.write("<receiver \n"
						+ "android:name=\"com.google.appinventor.components.runtime.util.SmsBroadcastReceiver\" \n"
						+ "android:enabled=\"true\" \n" + "android:exported=\"true\" >\n " + "<intent-filter> \n"
						+ "<action android:name=\"android.provider.Telephony.SMS_RECEIVED\" /> \n" + "<action \n"
						+ "android:name=\"com.google.android.apps.googlevoice.SMS_RECEIVED\" \n"
						+ "android:permission=\"com.google.android.apps.googlevoice.permission.RECEIVE_SMS\" /> \n"
						+ "</intent-filter>  \n" + "</receiver> \n");
			}*/	

			out.write("  </application>\n");
			out.write("</manifest>\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			userErrors.print(String.format(ERROR_IN_STAGE, "manifest"));
			return false;
		}

		return true;
	}

	/**
	 * Builds a YAIL project.
	 *
	 * @param project
	 *            project to build
	 * @param componentTypes
	 *            component types used in the project
	 * @param out
	 *            stdout stream for compiler messages
	 * @param err
	 *            stderr stream for compiler messages
	 * @param userErrors
	 *            stream to write user-visible error messages
	 * @param keystoreFilePath
	 * @param childProcessRam
	 *            maximum RAM for child processes, in MBs.
	 * @return {@code true} if the compilation succeeds, {@code false} otherwise
	 * @throws JSONException
	 * @throws IOException
	 */
	public static boolean compile(Project project, Set<String> componentTypes, PrintStream out, PrintStream err,
			PrintStream userErrors, boolean isForCompanion, String keystoreFilePath, int childProcessRam,
			String dexCacheDir) throws IOException, JSONException {
		long start = System.currentTimeMillis();

		// Create a new compiler instance for the compilation
		Compiler compiler = new Compiler(project, componentTypes, out, err, userErrors, isForCompanion, childProcessRam,
				dexCacheDir);

		// Get names of component-required libraries and assets.
		compiler.generateLibraryNames();
		compiler.generateNativeLibraryNames();
		compiler.generateAssets();

		// Create build directory.
		File buildDir = createDirectory(project.getBuildDirectory());

		// Prepare application icon.
		out.println("________Preparing application icon");
		File resDir = createDirectory(buildDir, "res");
		File drawableDir = createDirectory(resDir, "drawable");
		if (!compiler.prepareApplicationIcon(new File(drawableDir, "ya.png"))) {
			return false;
		}
		setProgress(10);

		// Create anim directory and animation xml files
		out.println("________Creating animation xml");
		File animDir = createDirectory(resDir, "anim");
		if (!compiler.createAnimationXml(animDir)) {
			return false;
		}

		// Determine android permissions.
		out.println("________Determining permissions");
		Set<String> permissionsNeeded = compiler.generatePermissions();
		if (permissionsNeeded == null) {
			return false;
		}
		setProgress(15);

		// Generate AndroidManifest.xml
		out.println("________Generating manifest file");
		File manifestFile = new File(buildDir, "AndroidManifest.xml");
		if (!compiler.writeAndroidManifest(manifestFile, permissionsNeeded)) {
			return false;
		}
		setProgress(20);

		// Insert native libraries
		out.println("________Attaching native libraries");
		if (!compiler.insertNativeLibraries(buildDir)) {
			return false;
		}

		// Add raw assets to sub-directory of project assets.
		out.println("________Attaching component assets");
		if (!compiler.attachComponentAssets()) {
			return false;
		}

		// Create class files.
		out.println("________Compiling source files");
		File classesDir = createDirectory(buildDir, "classes");
		if (!compiler.generateClasses(classesDir)) {
			return false;
		}
		setProgress(35);

		// Invoke dx on class files
		out.println("________Invoking DX");
		// TODO(markf): Running DX is now pretty slow (~25 sec overhead the
		// first time and ~15 sec
		// overhead for subsequent runs). I think it's because of the need to dx
		// the entire
		// kawa runtime every time. We should probably only do that once and
		// then copy all the
		// kawa runtime dx files into the generated classes.dex (which would
		// only contain the
		// files compiled for this project).
		// Aargh. It turns out that there's no way to manipulate .dex files to
		// do the above. An
		// Android guy suggested an alternate approach of shipping the kawa
		// runtime .dex file as
		// data with the application and then creating a new DexClassLoader
		// using that .dex file
		// and with the original app class loader as the parent of the new one.
		// TODONE(zhuowei): Now using the new Android DX tool to merge dex files
		// Needs to specify a writable cache dir on the command line that
		// persists after shutdown
		// Each pre-dexed file is identified via its MD5 hash (since the
		// standard Android SDK's
		// method of identifying via a hash of the path won't work when files
		// are copied into temporary storage) and processed via a hacked up
		// version of
		// Android SDK's Dex Ant task
		File tmpDir = createDirectory(buildDir, "tmp");
		String dexedClassesDir = tmpDir.getAbsolutePath();
		if (!compiler.runDx(classesDir, dexedClassesDir, false)) {
			return false;
		}
		setProgress(85);

		// Invoke aapt to package everything up
		out.println("________Invoking AAPT");
		File deployDir = createDirectory(buildDir, "deploy");
		String tmpPackageName = deployDir.getAbsolutePath() + File.separatorChar + project.getProjectName() + ".ap_";
		if (!compiler.runAaptPackage(manifestFile, resDir, tmpPackageName)) {
			return false;
		}
		setProgress(90);

		// Seal the apk with ApkBuilder
		out.println("________Invoking ApkBuilder");
		String apkAbsolutePath = deployDir.getAbsolutePath() + File.separatorChar + project.getProjectName() + ".apk";
		if (!compiler.runApkBuilder(apkAbsolutePath, tmpPackageName, dexedClassesDir)) {
			return false;
		}
		setProgress(95);

		// Sign the apk file
		out.println("________Signing the apk file");
		if (!compiler.runJarSigner(apkAbsolutePath, keystoreFilePath)) {
			return false;
		}

		// ZipAlign the apk file
		out.println("________ZipAligning the apk file");
		if (!compiler.runZipAlign(apkAbsolutePath, tmpDir)) {
			return false;
		}

		setProgress(100);

		out.println("Build finished in " + ((System.currentTimeMillis() - start) / 1000.0) + " seconds");

		return true;
	}

	/*
	 * Creates all the animation xml files.
	 */
	private boolean createAnimationXml(File animDir) {
		// Store the filenames, and their contents into a HashMap
		// so that we can easily add more, and also to iterate
		// through creating the files.
		Map<String, String> files = new HashMap<String, String>();
		files.put("fadein.xml", AnimationXmlConstants.FADE_IN_XML);
		files.put("fadeout.xml", AnimationXmlConstants.FADE_OUT_XML);
		files.put("hold.xml", AnimationXmlConstants.HOLD_XML);
		files.put("zoom_enter.xml", AnimationXmlConstants.ZOOM_ENTER);
		files.put("zoom_exit.xml", AnimationXmlConstants.ZOOM_EXIT);
		files.put("zoom_enter_reverse.xml", AnimationXmlConstants.ZOOM_ENTER_REVERSE);
		files.put("zoom_exit_reverse.xml", AnimationXmlConstants.ZOOM_EXIT_REVERSE);
		files.put("slide_exit.xml", AnimationXmlConstants.SLIDE_EXIT);
		files.put("slide_enter.xml", AnimationXmlConstants.SLIDE_ENTER);
		files.put("slide_exit_reverse.xml", AnimationXmlConstants.SLIDE_EXIT_REVERSE);
		files.put("slide_enter_reverse.xml", AnimationXmlConstants.SLIDE_ENTER_REVERSE);
		files.put("slide_v_exit.xml", AnimationXmlConstants.SLIDE_V_EXIT);
		files.put("slide_v_enter.xml", AnimationXmlConstants.SLIDE_V_ENTER);
		files.put("slide_v_exit_reverse.xml", AnimationXmlConstants.SLIDE_V_EXIT_REVERSE);
		files.put("slide_v_enter_reverse.xml", AnimationXmlConstants.SLIDE_V_ENTER_REVERSE);

		for (String filename : files.keySet()) {
			File file = new File(animDir, filename);
			if (!writeXmlFile(file, files.get(filename))) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Writes the given string input to the provided file.
	 */
	private boolean writeXmlFile(File file, String input) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(input);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Runs ApkBuilder by using the API instead of calling its main method
	 * because the main method can call System.exit(1), which will bring down
	 * our server.
	 */
	private boolean runApkBuilder(String apkAbsolutePath, String zipArchive, String dexedClassesDir) {
		try {
			ApkBuilder apkBuilder = new ApkBuilder(apkAbsolutePath, zipArchive,
					dexedClassesDir + File.separator + "classes.dex", null, System.out);
			/*if (hasSecondDex) {
				apkBuilder.addFile(new File(dexedClassesDir + File.separator + "classes2.dex"), "classes2.dex");
			}
			
			if (hasThirdDex) {
				apkBuilder.addFile(new File(dexedClassesDir + File.separator + "classes3.dex"), "classes3.dex");
			}*/
			
			for(int i=2; i<numberDexFiles; i++) {
				apkBuilder.addFile(new File(dexedClassesDir + File.separator + "classes" + i + ".dex"), "classes" + i + ".dex");
			}
			
			apkBuilder.sealApk();
			return true;
		} catch (Exception e) {
			// This is fatal.
			e.printStackTrace();
			LOG.warning("YAIL compiler - ApkBuilder failed.");
			err.println("YAIL compiler - ApkBuilder failed.");
			userErrors.print(String.format(ERROR_IN_STAGE, "ApkBuilder"));
			return false;
		}
	}

	/**
	 * Creates a new YAIL compiler.
	 *
	 * @param project
	 *            project to build
	 * @param componentTypes
	 *            component types used in the project
	 * @param out
	 *            stdout stream for compiler messages
	 * @param err
	 *            stderr stream for compiler messages
	 * @param userErrors
	 *            stream to write user-visible error messages
	 * @param childProcessMaxRam
	 *            maximum RAM for child processes, in MBs.
	 */
	@VisibleForTesting
	Compiler(Project project, Set<String> componentTypes, PrintStream out, PrintStream err, PrintStream userErrors,
			boolean isForCompanion, int childProcessMaxRam, String dexCacheDir) {
		this.project = project;
		this.componentTypes = componentTypes;
		this.out = out;
		this.err = err;
		this.userErrors = userErrors;
		this.isForCompanion = isForCompanion;
		this.childProcessRamMb = childProcessMaxRam;
		this.dexCacheDir = dexCacheDir;
	}

	/*
	 * Runs the Kawa compiler in a separate process to generate classes. Returns
	 * false if not able to create a class file for every source file in the
	 * project.
	 */
	private boolean generateClasses(File classesDir) {
		try {
			List<Project.SourceDescriptor> sources = project.getSources();
			List<String> sourceFileNames = Lists.newArrayListWithCapacity(sources.size());
			List<String> classFileNames = Lists.newArrayListWithCapacity(sources.size());
			boolean userCodeExists = false;
			for (Project.SourceDescriptor source : sources) {
				String sourceFileName = source.getFile().getAbsolutePath();
				LOG.log(Level.INFO, "source file: " + sourceFileName);
				int srcIndex = sourceFileName.indexOf("/../src/");
				String sourceFileRelativePath = sourceFileName.substring(srcIndex + 8);
				String classFileName = (classesDir.getAbsolutePath() + "/" + sourceFileRelativePath)
						.replace(YoungAndroidConstants.YAIL_EXTENSION, ".class");
				if (System.getProperty("os.name").startsWith("Windows")) {
					classFileName = classesDir.getAbsolutePath().replace(YoungAndroidConstants.YAIL_EXTENSION,
							".class");
				}

				// Check whether user code exists by seeing if a left
				// parenthesis exists at the beginning of
				// a line in the file
				// TODO(user): Replace with more robust test of empty source
				// file.
				if (!userCodeExists) {
					Reader fileReader = new FileReader(sourceFileName);
					try {
						while (fileReader.ready()) {
							int c = fileReader.read();
							if (c == '(') {
								userCodeExists = true;
								break;
							}
						}
					} finally {
						fileReader.close();
					}
				}
				sourceFileNames.add(sourceFileName);
				classFileNames.add(classFileName);
			}

			if (!userCodeExists) {
				userErrors.print(NO_USER_CODE_ERROR);
				return false;
			}

			// Construct the class path including component libraries (jars)
			String classpath = getResource(KAWA_RUNTIME) + File.pathSeparator + getResource(ACRA_RUNTIME)
					+ File.pathSeparator + getResource(SIMPLE_ANDROID_RUNTIME_JAR) + File.pathSeparator;

			// Add component library names to classpath
			System.out.println("Libraries Classpath, n " + librariesNeeded.size());
			for (String library : librariesNeeded) {
				classpath += getResource(RUNTIME_FILES_DIR + library) + File.pathSeparator;
			}

			classpath += getResource(ANDROID_RUNTIME);

			System.out.println("Libraries Classpath = " + classpath);

			String yailRuntime = getResource(YAIL_RUNTIME);
			List<String> kawaCommandArgs = Lists.newArrayList();
			int mx = childProcessRamMb - 200;
			Collections.addAll(kawaCommandArgs, System.getProperty("java.home") + "/bin/java", "-Dfile.encoding=UTF-8",
					"-mx" + mx + "M", "-cp", classpath, "kawa.repl", "-f", yailRuntime, "-d",
					classesDir.getAbsolutePath(), "-P", Signatures.getPackageName(project.getMainClass()) + ".", "-C");
			// TODO(lizlooney) - we are currently using (and have always used)
			// absolute paths for the
			// source file names. The resulting .class files contain references
			// to the source file names,
			// including the name of the tmp directory that contains them. We
			// may be able to avoid that
			// by using source file names that are relative to the project root
			// and using the project
			// root as the working directory for the Kawa compiler process.
			kawaCommandArgs.addAll(sourceFileNames);
			kawaCommandArgs.add(yailRuntime);
			String[] kawaCommandLine = kawaCommandArgs.toArray(new String[kawaCommandArgs.size()]);

			long start = System.currentTimeMillis();
			// Capture Kawa compiler stderr. The ODE server parses out the
			// warnings and errors and adds
			// them to the protocol buffer for logging purposes. (See
			// buildserver/ProjectBuilder.processCompilerOutout.
			ByteArrayOutputStream kawaOutputStream = new ByteArrayOutputStream();
			boolean kawaSuccess;
			synchronized (SYNC_KAWA_OR_DX) {
				kawaSuccess = Execution.execute(null, kawaCommandLine, System.out, new PrintStream(kawaOutputStream));
			}
			if (!kawaSuccess) {
				LOG.log(Level.SEVERE, "Kawa compile has failed.");
			}
			String kawaOutput = kawaOutputStream.toString();
			out.print(kawaOutput);
			String kawaCompileTimeMessage = "Kawa compile time: " + ((System.currentTimeMillis() - start) / 1000.0)
					+ " seconds";
			out.println(kawaCompileTimeMessage);
			LOG.info(kawaCompileTimeMessage);

			// Check that all of the class files were created.
			// If they weren't, return with an error.
			for (String classFileName : classFileNames) {
				File classFile = new File(classFileName);
				if (!classFile.exists()) {
					LOG.log(Level.INFO, "Can't find class file: " + classFileName);
					String screenName = classFileName.substring(classFileName.lastIndexOf('/') + 1,
							classFileName.lastIndexOf('.'));
					userErrors.print(String.format(COMPILATION_ERROR, screenName));
					return false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			userErrors.print(String.format(ERROR_IN_STAGE, "compile"));
			return false;
		}

		return true;
	}

	private boolean runJarSigner(String apkAbsolutePath, String keystoreAbsolutePath) {
		// TODO(user): maybe make a command line flag for the jarsigner location
		String javaHome = System.getProperty("java.home");
		// This works on Mac OS X.
		File jarsignerFile = new File(javaHome + File.separator + "bin" + File.separator + "jarsigner");
		if (!jarsignerFile.exists()) {
			// This works when a JDK is installed with the JRE.
			jarsignerFile = new File(
					javaHome + File.separator + ".." + File.separator + "bin" + File.separator + "jarsigner");
			if (System.getProperty("os.name").startsWith("Windows")) {
				jarsignerFile = new File(
						javaHome + File.separator + ".." + File.separator + "bin" + File.separator + "jarsigner.exe");
			}
			if (!jarsignerFile.exists()) {
				LOG.warning("YAIL compiler - could not find jarsigner.");
				err.println("YAIL compiler - could not find jarsigner.");
				userErrors.print(String.format(ERROR_IN_STAGE, "JarSigner"));
				return false;
			}
		}

		String[] jarsignerCommandLine = { jarsignerFile.getAbsolutePath(), "-digestalg", "SHA1", "-sigalg",
				"MD5withRSA", "-keystore", keystoreAbsolutePath, "-storepass", "android", apkAbsolutePath,
				"AndroidKey" };
		if (!Execution.execute(null, jarsignerCommandLine, System.out, System.err)) {
			LOG.warning("YAIL compiler - jarsigner execution failed.");
			err.println("YAIL compiler - jarsigner execution failed.");
			userErrors.print(String.format(ERROR_IN_STAGE, "JarSigner"));
			return false;
		}

		return true;
	}

	private boolean runZipAlign(String apkAbsolutePath, File tmpDir) {
		// TODO(user): add zipalign tool appinventor->lib->android->tools->linux
		// and windows
		// Need to make sure assets directory exists otherwise zipalign will
		// fail.
		createDirectory(project.getAssetsDirectory());
		String zipAlignTool;
		String osName = System.getProperty("os.name");
		if (osName.equals("Mac OS X")) {
			zipAlignTool = MAC_ZIPALIGN_TOOL;
		} else if (osName.equals("Linux")) {
			zipAlignTool = LINUX_ZIPALIGN_TOOL;
		} else if (osName.startsWith("Windows")) {
			zipAlignTool = WINDOWS_ZIPALIGN_TOOL;
		} else {
			LOG.warning("YAIL compiler - cannot run ZIPALIGN on OS " + osName);
			err.println("YAIL compiler - cannot run ZIPALIGN on OS " + osName);
			userErrors.print(String.format(ERROR_IN_STAGE, "ZIPALIGN"));
			return false;
		}
		// TODO: create tmp file for zipaling result
		String zipAlignedPath = tmpDir.getAbsolutePath() + File.separator + "zipaligned.apk";
		// zipalign -f -v 4 infile.zip outfile.zip
		String[] zipAlignCommandLine = { getResource(zipAlignTool), "-f", "4", apkAbsolutePath, zipAlignedPath };
		long startZipAlign = System.currentTimeMillis();
		// Using System.err and System.out on purpose. Don't want to pollute
		// build messages with
		// tools output
		if (!Execution.execute(null, zipAlignCommandLine, System.out, System.err)) {
			LOG.warning("YAIL compiler - ZIPALIGN execution failed.");
			err.println("YAIL compiler - ZIPALIGN execution failed.");
			userErrors.print(String.format(ERROR_IN_STAGE, "ZIPALIGN"));
			return false;
		}
		if (!copyFile(zipAlignedPath, apkAbsolutePath)) {
			LOG.warning("YAIL compiler - ZIPALIGN file copy failed.");
			err.println("YAIL compiler - ZIPALIGN file copy failed.");
			userErrors.print(String.format(ERROR_IN_STAGE, "ZIPALIGN"));
			return false;
		}
		String zipALignTimeMessage = "ZIPALIGN time: " + ((System.currentTimeMillis() - startZipAlign) / 1000.0)
				+ " seconds";
		out.println(zipALignTimeMessage);
		LOG.info(zipALignTimeMessage);
		return true;
	}

	/*
	 * Loads the icon for the application, either a user provided one or the
	 * default one.
	 */
	private boolean prepareApplicationIcon(File outputPngFile) {
		String userSpecifiedIcon = Strings.nullToEmpty(project.getIcon());
		try {
			BufferedImage icon;
			if (!userSpecifiedIcon.isEmpty()) {
				File iconFile = new File(project.getAssetsDirectory(), userSpecifiedIcon);
				icon = ImageIO.read(iconFile);
				if (icon == null) {
					// This can happen if the iconFile isn't an image file.
					// For example, icon is null if the file is a .wav file.
					// TODO(lizlooney) - This happens if the user specifies a
					// .ico file. We should fix that.
					userErrors.print(String.format(ICON_ERROR, userSpecifiedIcon));
					return false;
				}
			} else {
				// Load the default image.
				icon = ImageIO.read(Compiler.class.getResource(DEFAULT_ICON));
			}
			ImageIO.write(icon, "png", outputPngFile);
		} catch (Exception e) {
			e.printStackTrace();
			// If the user specified the icon, this is fatal.
			if (!userSpecifiedIcon.isEmpty()) {
				userErrors.print(String.format(ICON_ERROR, userSpecifiedIcon));
				return false;
			}
		}

		return true;
	}

	private boolean runDx(File classesDir, String dexedClassesDir, boolean secondTry) {
		List<File> libList = new ArrayList<File>();
		List<File> inputList = new ArrayList<File>();
		List<File> class2List = new ArrayList<File>();
		inputList.add(classesDir); // this is a directory, and won't be cached
									// into the dex cache
		inputList.add(new File(getResource(SIMPLE_ANDROID_RUNTIME_JAR)));
		inputList.add(new File(getResource(KAWA_RUNTIME)));
		inputList.add(new File(getResource(ACRA_RUNTIME)));
		
		for (String library : librariesNeeded) {
			System.out.println("----------------> LIB: "+library);
			libList.add(new File(getResource(RUNTIME_FILES_DIR + library)));
		}

		int offset = libList.size();
		// Note: The choice of 12 libraries is arbitrary. We note that things
		// worked to put all libraries into the first classes.dex file when we
		// had 16 libraries and broke at 17. So this is a conservative number
		// to try.
		if (!secondTry) { // First time through, try base + 12 libraries
			if (offset > 8)
				offset = 8;
		} else {
			offset = 0; // Add NO libraries the second time through!
		}
		for (int i = 0; i < offset; i++) {
			inputList.add(libList.get(i));
		}

		if (libList.size() - offset > 0) { // Any left over for classes2?
			for (int i = offset; i < libList.size(); i++) {
				class2List.add(libList.get(i));
			}
		}

		DexExecTask dexTask = new DexExecTask();
		dexTask.setExecutable(getResource(DX_JAR));
		dexTask.setOutput(dexedClassesDir + File.separator + "classes.dex");
		dexTask.setChildProcessRamMb(childProcessRamMb);
		if (dexCacheDir == null) {
			dexTask.setDisableDexMerger(true);
		} else {
			createDirectory(new File(dexCacheDir));
			dexTask.setDexedLibs(dexCacheDir);
		}

		long startDx = System.currentTimeMillis();
		// Using System.err and System.out on purpose. Don't want to pollute
		// build messages with
		// tools output
		boolean dxSuccess;
		
		synchronized (SYNC_KAWA_OR_DX) {
			setProgress(50);
			dxSuccess = dexTask.execute(inputList);
			if (dxSuccess && (class2List.size() > 0)) {
				setProgress(60);
				
				dxSuccess = false;
				int top = class2List.size();
				int init = 0;
				
				while(init < class2List.size()) {
					while(!dxSuccess) {
						LOG.info("DX try with the " + numberDexFiles + " dex file with top = " + top);
						dexTask.setOutput(dexedClassesDir + File.separator + "classes" + numberDexFiles + ".dex");
						inputList = new ArrayList<File>();
						dxSuccess = dexTask.execute(class2List.subList(init, top));
						if(!dxSuccess) {
							top = top - 1;
						}
					}
					init = top;
					top = class2List.size();
					numberDexFiles = numberDexFiles + 1;
					if(init < class2List.size()) {
						dxSuccess = false;
					}
				}
				
				/*if(class2List.size() > 12) {
					LOG.info("DX try 3 .dex file");
					dexTask.setOutput(dexedClassesDir + File.separator + "classes2.dex");
					inputList = new ArrayList<File>();
					dxSuccess = dexTask.execute(class2List.subList(0, 12));
					dexTask.setOutput(dexedClassesDir + File.separator + "classes3.dex");
					inputList = new ArrayList<File>();
					dxSuccess = dexTask.execute(class2List.subList(12, class2List.size()));
					setProgress(75);
					hasSecondDex = true;
					hasThirdDex = true;
				} else {
					dexTask.setOutput(dexedClassesDir + File.separator + "classes2.dex");
					inputList = new ArrayList<File>();
					dxSuccess = dexTask.execute(class2List);
					setProgress(75);
					hasSecondDex = true;
				}*/
			} else if (!dxSuccess) {
				// If we get into this block of code, it means that the Dexer
				// returned an error. It *might* be because of overflowing the
				// the fixed table of methods, but we cannot know that for
				// sure so we try Dexing again, but this time we put all
				// support libraries into classes2.dex. If this second pass
				// fails, we return the error to the user.
				LOG.info("DX execution failed, trying with fewer libraries.");
				if (secondTry) { // Already tried the more conservative
									// approach!
					LOG.warning("YAIL compiler - DX execution failed (secondTry!).");
					err.println("YAIL compiler - DX execution failed.");
					userErrors.print(String.format(ERROR_IN_STAGE, "DX"));
					return false;
				} else {
					return runDx(classesDir, dexedClassesDir, true);
				}
			}
		}
		if (!dxSuccess) {
			LOG.warning("YAIL compiler - DX execution failed.");
			err.println("YAIL compiler - DX execution failed.");
			userErrors.print(String.format(ERROR_IN_STAGE, "DX"));
			return false;
		}
		String dxTimeMessage = "DX time: " + ((System.currentTimeMillis() - startDx) / 1000.0) + " seconds";
		out.println(dxTimeMessage);
		LOG.info(dxTimeMessage);

		return true;
	}

	private boolean runAaptPackage(File manifestFile, File resDir, String tmpPackageName) {
		// Need to make sure assets directory exists otherwise aapt will fail.
		createDirectory(project.getAssetsDirectory());
		String aaptTool;
		String osName = System.getProperty("os.name");
		if (osName.equals("Mac OS X")) {
			aaptTool = MAC_AAPT_TOOL;
		} else if (osName.equals("Linux")) {
			aaptTool = LINUX_AAPT_TOOL;
		} else if (osName.startsWith("Windows")) {
			aaptTool = WINDOWS_AAPT_TOOL;
		} else {
			LOG.warning("YAIL compiler - cannot run AAPT on OS " + osName);
			err.println("YAIL compiler - cannot run AAPT on OS " + osName);
			userErrors.print(String.format(ERROR_IN_STAGE, "AAPT"));
			return false;
		}
		String[] aaptPackageCommandLine = { getResource(aaptTool), "package", "-v", "-f", "-M",
				manifestFile.getAbsolutePath(), "-S", resDir.getAbsolutePath(), "-A",
				project.getAssetsDirectory().getAbsolutePath(), "-I", getResource(ANDROID_RUNTIME), "-F",
				tmpPackageName, libsDir.getAbsolutePath() };
		long startAapt = System.currentTimeMillis();
		// Using System.err and System.out on purpose. Don't want to pollute
		// build messages with
		// tools output
		if (!Execution.execute(null, aaptPackageCommandLine, System.out, System.err)) {
			LOG.warning("YAIL compiler - AAPT execution failed.");
			err.println("YAIL compiler - AAPT execution failed.");
			userErrors.print(String.format(ERROR_IN_STAGE, "AAPT"));
			return false;
		}
		String aaptTimeMessage = "AAPT time: " + ((System.currentTimeMillis() - startAapt) / 1000.0) + " seconds";
		out.println(aaptTimeMessage);
		LOG.info(aaptTimeMessage);

		return true;
	}

	private boolean insertNativeLibraries(File buildDir) {
		out.println("________Copying native libraries");
		libsDir = createDirectory(buildDir, LIBS_DIR_NAME);
		File apkLibDir = createDirectory(libsDir, APK_LIB_DIR_NAME); // This dir
																		// will
																		// be
																		// copied
																		// to
																		// apk.
		File armeabiDir = createDirectory(apkLibDir, ARMEABI_DIR_NAME);
		File armeabiV7aDir = createDirectory(apkLibDir, ARMEABI_V7A_DIR_NAME);
		/*
		 * Native libraries are targeted for particular processor architectures.
		 * Here, non-default architectures (ARMv5TE is default) are identified
		 * with suffixes before being placed in the appropriate directory with
		 * their suffix removed.
		 */
		// <<<<<<< HEAD (Lo dejo asÃ­ para pruebas)
		/*
		 * try {
		 * 
		 * for (String library : nativeLibrariesNeeded) {
		 * System.out.println("-------->"+library);
		 * if(!library.trim().equals("")){
		 */
		// =======
		try {
			for (String library : nativeLibrariesNeeded) {
				System.out.println("IRR--------> Processing native library:"+library);
				if (!library.trim().equals("") && library.indexOf(".so")!=-1) {
					// >>>>>>> upstream/master
					if (library.endsWith(ARMEABI_V7A_SUFFIX)) { // Remove suffix
																// and copy.
						library = library.substring(0, library.length() - ARMEABI_V7A_SUFFIX.length());
						Files.copy(new File(getResource(RUNTIME_FILES_DIR + ARMEABI_V7A_DIRECTORY + "/" + library)),
								new File(armeabiV7aDir, library));
					} else {
						Files.copy(new File(getResource(RUNTIME_FILES_DIR + ARMEABI_DIR_NAME + "/" + library)),
								new File(armeabiDir, library));
						// <<<<<<< HEAD
					}
					System.out.println("IRR--------> OK");
				} else {
					System.out.println("IRR--------> Ignored");
					// =======
					// >>>>>>> upstream/master
				}
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			userErrors.print(String.format(ERROR_IN_STAGE, "Native Code"));
			return false;
		}
	}

	private boolean attachComponentAssets() {
		createDirectory(project.getAssetsDirectory()); // Needed to insert
														// resources.
		try {
			// Gather non-library assets to be added to apk's Asset directory.
			// The assets directory have been created before this.
			File componentAssetDirectory = createDirectory(project.getAssetsDirectory(), ASSET_DIRECTORY);
			for (String filename : assetsNeeded) {
				Files.copy(new File(getResource(RUNTIME_FILES_DIR + filename)),
						new File(componentAssetDirectory, filename));
			}
			// IRR Si usamos la camara incorporamos tambien los shaders del JPCT
			if (componentTypes.contains("ARCamera")||componentTypes.contains("Model3DViewer")||componentTypes.contains("VRScene")||componentTypes.contains("VRVideo360")) {

				System.out.println("IRR copiando shaders:" + RUNTIME_FILES_DIR + ARSCENE_JPCT_SHADERS);
				Files.copy(new File(getResource(RUNTIME_FILES_DIR + ARSCENE_JPCT_SHADERS)),
						new File(project.getAssetsDirectory(), "jpct_shaders.zip"));
				System.out.println("IRR  shaders copiados");
				
					System.out.println("RBP copiando objeto de esfera360 para VRScene:" + RUNTIME_FILES_DIR
							+ "sphere_paranomic.obj");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "sphere_paranomic.obj")),
							new File(project.getAssetsDirectory(),  "sphere_paranomic.obj"));
					System.out.println("RBP  esfera360 copiada");
					
					System.out.println("RBP copiando shaders especiales VRScene:" + RUNTIME_FILES_DIR
							+ "surface_fragment_shader.txt");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "surface_fragment_shader.txt")),
							new File(project.getAssetsDirectory(),  "surface_fragment_shader.txt"));
					System.out.println("RBP  shader copiado");
					
					System.out.println("RBP copiando shaders especiales VRScene:" + RUNTIME_FILES_DIR
							+ "defaultVertexShaderTex0.src");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "defaultVertexShaderTex0.src")),
							new File(project.getAssetsDirectory(),  "defaultVertexShaderTex0.src"));
					System.out.println("RBP  shader copiado");
				
				if(componentTypes.contains("VRVideo360"))
						{
					System.out.println("RBP copiando textura de skybox para VRVideo360:" + RUNTIME_FILES_DIR
							+ "loadIMG.png");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "loadIMG.png")),
							new File(project.getAssetsDirectory(),  "loadIMG.png"));
					System.out.println("RBP skybox VRVideo360 copiada");
					
						}
				if (componentTypes.contains("ARTextTracker")) {
					System.out.println("IRR copiando diccionario para TextTracker:" + RUNTIME_FILES_DIR
							+ "Vuforia-English-word.vwl");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR + "Vuforia-English-word.vwl")),
							new File(project.getAssetsDirectory(), "Vuforia-English-word.vwl"));
					System.out.println("IRR  diccionario copiado");
				}
				if(componentTypes.contains("Model3DViewer"))
				{
					System.out.println("RBP copiando iconos de control de animaciones de Model3DView:" + RUNTIME_FILES_DIR
							+ "play.png");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "play.png")),
							new File(project.getAssetsDirectory(),  "play.png"));
					System.out.println("RBP play copiado");
					
					System.out.println("RBP copiando iconos de control de animaciones de Model3DView:" + RUNTIME_FILES_DIR
							+ "pause.png");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "pause.png")),
							new File(project.getAssetsDirectory(),  "pause.png"));
					System.out.println("RBP pause copiado");
					
					System.out.println("RBP copiando iconos de control de animaciones de Model3DView:" + RUNTIME_FILES_DIR
							+ "forward.png");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "forward.png")),
							new File(project.getAssetsDirectory(),  "forward.png"));
					System.out.println("RBP forward copiado");
					
					System.out.println("RBP copiando iconos de control de animaciones de Model3DView:" + RUNTIME_FILES_DIR
							+ "rewind.png");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "rewind.png")),
							new File(project.getAssetsDirectory(),  "rewind.png"));
					System.out.println("RBP rewind copiado");
					
					System.out.println("RBP copiando iconos de control de movimiento de Model3DView:" + RUNTIME_FILES_DIR
							+ "rewind.png");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "menuOFF.png")),
							new File(project.getAssetsDirectory(),  "menuOFF.png"));
					System.out.println("RBP menuOFF copiado");
					
					System.out.println("RBP copiando iconos de control de movimiento de Model3DView:" + RUNTIME_FILES_DIR
							+ "rewind.png");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "menuON.png")),
							new File(project.getAssetsDirectory(),  "menuON.png"));
					System.out.println("RBP menuON copiado");
					
					System.out.println("RBP copiando iconos de control de movimiento de Model3DView:" + RUNTIME_FILES_DIR
							+ "rewind.png");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "moveOFF.png")),
							new File(project.getAssetsDirectory(),  "moveOFF.png"));
					System.out.println("RBP moveOFF copiado");
					
					System.out.println("RBP copiando iconos de control de movimiento de Model3DView:" + RUNTIME_FILES_DIR
							+ "rewind.png");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "moveON.png")),
							new File(project.getAssetsDirectory(),  "moveON.png"));
					System.out.println("RBP moveON copiado");
					
					System.out.println("RBP copiando iconos de control de movimiento de Model3DView:" + RUNTIME_FILES_DIR
							+ "rewind.png");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "rotateOFF.png")),
							new File(project.getAssetsDirectory(),  "rotateOFF.png"));
					System.out.println("RBP rotateOFF copiado");
					
					System.out.println("RBP copiando iconos de control de movimiento de Model3DView:" + RUNTIME_FILES_DIR
							+ "rewind.png");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "rotateON.png")),
							new File(project.getAssetsDirectory(),  "rotateON.png"));
					System.out.println("RBP rotateON copiado");
					
					System.out.println("RBP copiando iconos de control de movimiento de Model3DView:" + RUNTIME_FILES_DIR
							+ "rewind.png");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "scaleOFF.png")),
							new File(project.getAssetsDirectory(),  "scaleOFF.png"));
					System.out.println("RBP scaleOFF copiado");
					
					System.out.println("RBP copiando iconos de control de movimiento de Model3DView:" + RUNTIME_FILES_DIR
							+ "rewind.png");
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "scaleON.png")),
							new File(project.getAssetsDirectory(),  "scaleON.png"));
					System.out.println("RBP scaleON copiado");
					
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "playOFF.png")),
							new File(project.getAssetsDirectory(),  "playOFF.png"));
					System.out.println("RBP playOFF copiado");
					
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "pauseOFF.png")),
							new File(project.getAssetsDirectory(),  "pauseOFF.png"));
					System.out.println("RBP pauseOFF copiado");
					
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "rewindOFF.png")),
							new File(project.getAssetsDirectory(),  "rewindOFF.png"));
					System.out.println("RBP rewindOFF copiado");
					
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "forwardOFF.png")),
							new File(project.getAssetsDirectory(),  "forwardOFF.png"));
					System.out.println("RBP forwardOFF copiado");
					
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "back.png")),
							new File(project.getAssetsDirectory(),  "back.png"));
					System.out.println("RBP back copiado");
					
					Files.copy(new File(getResource(RUNTIME_FILES_DIR +  "reset.png")),
							new File(project.getAssetsDirectory(),  "reset.png"));
					System.out.println("RBP reset copiado");
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
			userErrors.print(String.format(ERROR_IN_STAGE, "Assets"));
			return false;
		}
		return true;
	}

	/**
	 * Writes out the given resource as a temp file and returns the absolute
	 * path. Caches the location of the files, so we can reuse them.
	 *
	 * @param resourcePath
	 *            the name of the resource
	 */
	static synchronized String getResource(String resourcePath) {
		try {
			File file = resources.get(resourcePath);
			if (file == null) {
				String basename = PathUtil.basename(resourcePath);
				String prefix;
				String suffix;
				int lastDot = basename.lastIndexOf(".");
				if (lastDot != -1) {
					prefix = basename.substring(0, lastDot);
					suffix = basename.substring(lastDot);
				} else {
					prefix = basename;
					suffix = "";
				}
				while (prefix.length() < 3) {
					prefix = prefix + "_";
				}
				file = File.createTempFile(prefix, suffix);
				file.setExecutable(true);
				file.deleteOnExit();
				file.getParentFile().mkdirs();
				Files.copy(Resources.newInputStreamSupplier(Compiler.class.getResource(resourcePath)), file);
				resources.put(resourcePath, file);
			}
			return file.getAbsolutePath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * Loads permissions and information on component libraries and assets.
	 */
	private void loadJsonInfo(ConcurrentMap<String, Set<String>> infoMap, String targetInfo)
			throws IOException, JSONException {
		synchronized (infoMap) {
			if (infoMap.isEmpty()) {
				String buildInfoJson = Resources.toString(Compiler.class.getResource(COMPONENT_BUILD_INFO),
						Charsets.UTF_8);

				JSONArray componentsArray = new JSONArray(buildInfoJson);
				int componentsLength = componentsArray.length();
				for (int componentsIndex = 0; componentsIndex < componentsLength; componentsIndex++) {
					JSONObject componentObject = componentsArray.getJSONObject(componentsIndex);
					String name = componentObject.getString("name");

					Set<String> infoGroupForThisComponent = Sets.newHashSet();

					JSONArray infoArray = componentObject.getJSONArray(targetInfo);
					int infoLength = infoArray.length();
					for (int infoIndex = 0; infoIndex < infoLength; infoIndex++) {
						String info = infoArray.getString(infoIndex);
						infoGroupForThisComponent.add(info);
					}

					infoMap.put(name, infoGroupForThisComponent);
				}
			}
		}
	}

	/**
	 * Copy one file to another. If destination file does not exist, it is
	 * created.
	 *
	 * @param srcPath
	 *            absolute path to source file
	 * @param dstPath
	 *            absolute path to destination file
	 * @return {@code true} if the copy succeeds, {@code false} otherwise
	 */
	private static Boolean copyFile(String srcPath, String dstPath) {
		try {
			FileInputStream in = new FileInputStream(srcPath);
			FileOutputStream out = new FileOutputStream(dstPath);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Creates a new directory (if it doesn't exist already).
	 *
	 * @param dir
	 *            new directory
	 * @return new directory
	 */
	private static File createDirectory(File dir) {
		if (!dir.exists()) {
			dir.mkdir();
		}
		return dir;
	}

	/**
	 * Creates a new directory (if it doesn't exist already).
	 *
	 * @param parentDirectory
	 *            parent directory of new directory
	 * @param name
	 *            name of new directory
	 * @return new directory
	 */
	private static File createDirectory(File parentDirectory, String name) {
		File dir = new File(parentDirectory, name);
		if (!dir.exists()) {
			dir.mkdir();
		}
		return dir;
	}

	private static int setProgress(int increments) {
		Compiler.currentProgress = increments;
		LOG.info("The current progress is " + Compiler.currentProgress + "%");
		return Compiler.currentProgress;
	}

	public static int getProgress() {
		if (Compiler.currentProgress == 100) {
			Compiler.currentProgress = 10;
			return 100;
		} else {
			return Compiler.currentProgress;
		}
	}
}