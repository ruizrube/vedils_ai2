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
@DesignerComponent(nonVisible = true, version = 1, description = "Augmented Reality Marker Tracker Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSAUGMENTEDREALITY, iconName = "images/arMarkerTracker.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.CAMERA, android.permission.ACCESS_NETWORK_STATE,android.permission.WRITE_EXTERNAL_STORAGE, android.permission.READ_EXTERNAL_STORAGE")

public class ARMarkerTracker extends ARPhysicalObject {

	public ARMarkerTracker(ComponentContainer container) {
		super(container);
		this.data.setTrackerType(PhysicalObject.TRACKER_MARKER);
	}

	/**
	 * @return the markerTracker
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public int MarkerTracker() {

		return data.getMarkerTracker();
	}

	/**
	 * @param markerTracker
	 *            the markerTracker to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = 0 + "")
	@SimpleProperty(description = "Specifies the tracker's marker.", userVisible = true)
	public void MarkerTracker(int markerTracker) {
		this.data.setMarkerTracker(markerTracker);
	}

}
