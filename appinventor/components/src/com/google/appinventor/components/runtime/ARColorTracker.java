/**
 * 
 */
package com.google.appinventor.components.runtime;

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

/**
 * @author ivanruizrube
 *
 */
@SimpleObject
//@DesignerComponent(nonVisible = true, version = 1, description = "Augmented Reality Color Tracker Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSAUGMENTEDREALITY, iconName = "images/arColorTracker.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.CAMERA, android.permission.ACCESS_NETWORK_STATE,android.permission.WRITE_EXTERNAL_STORAGE, android.permission.READ_EXTERNAL_STORAGE")

public class ARColorTracker extends ARPhysicalObject {

	public ARColorTracker(ComponentContainer container) {
		super(container);
		this.data.setTrackerType(PhysicalObject.TRACKER_IMAGE);

	}

	/**
	 * Returns the button's background color as an alpha-red-green-blue integer.
	 *
	 * @return background RGB color with alpha
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the tracker's color")
	public int ColorTracker() {
		return data.getColorTracker();
	}

	/**
	 * Specifies the tracker's background color as an alpha-red-green-blue
	 * integer. If the parameter is {@link Component#COLOR_DEFAULT}
	 *
	 * @param argb
	 *            background RGB color with alpha
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = Component.DEFAULT_VALUE_COLOR_DEFAULT)
	@SimpleProperty(description = "Specifies the tracker's background color.")
	public void ColorTracker(int argb) {
		data.setColorTracker(argb);
	}
	

}
