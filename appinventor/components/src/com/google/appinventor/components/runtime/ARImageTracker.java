/**
 * 
 */
package com.google.appinventor.components.runtime;

import java.io.IOException;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.ar4ai.PhysicalObject;
import com.google.appinventor.components.runtime.util.MediaUtil;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * @author ivanruizrube
 *
 */
@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "Augmented Reality Image Tracker Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSAUGMENTEDREALITY, iconName = "images/arImageTracker.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.CAMERA, android.permission.ACCESS_NETWORK_STATE,android.permission.WRITE_EXTERNAL_STORAGE, android.permission.READ_EXTERNAL_STORAGE")

public class ARImageTracker extends ARPhysicalObject {

	public ARImageTracker(ComponentContainer container) {
		super(container);
		this.data.setTrackerType(PhysicalObject.TRACKER_IMAGE);

	}

	/**
	 * Returns the path of the image's picture.
	 *
	 * @return the path of the image's picture
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public String ImageTracker() {
		return data.getPathImageTracker();
	}

	/**
	 * Specifies the path of the tracker's picture.
	 *
	 * <p/>
	 * See {@link MediaUtil#determineMediaSource} for information about what a
	 * path can be.
	 *
	 * @param path
	 *            the path of the image's picture
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET_IMAGE, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void ImageTracker(String path) {
		data.setPathImageTracker((path == null) ? "" : path);

		Drawable drawable;
		try {
			drawable = MediaUtil.getBitmapDrawable(container.$form(), data.getPathImageTracker());
		} catch (IOException ioe) {
			Log.e("Image", "Unable to load " + data.getPathImageTracker());
			drawable = null;
		}

		// mandar foto a la bib.

	}
	

}
