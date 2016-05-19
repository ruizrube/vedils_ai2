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
@DesignerComponent(nonVisible = true, version = 1, description = "Augmented Reality Object Tracker Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSAUGMENTEDREALITY, iconName = "images/arObjectTracker.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.CAMERA, android.permission.ACCESS_NETWORK_STATE,android.permission.WRITE_EXTERNAL_STORAGE, android.permission.READ_EXTERNAL_STORAGE")

public class ARObjectTracker extends ARPhysicalObject {

	public ARObjectTracker(ComponentContainer container) {
		super(container);
		this.data.setTrackerType(PhysicalObject.TRACKER_TARGETDB);

	}

	/**
	 * @return the ObjectTracker
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public String ObjectTracker() {
		return data.getTargetDBTracker();
	}

	/**
	 * @param ObjectTracker
	 *            the ObjectTracker to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(description = "Specifies the object name in the tracker database", userVisible = true)
	public void ObjectTracker(String targetDB) {
		this.data.setTargetDBTracker(targetDB);
	}
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public boolean ExtendedTracking() {
		return data.isExtendedTrackingEnabled();
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = 0.0f+"")
	@SimpleProperty(description = "Keeps the object on screen for a while when the mark disappears")
	public void ExtendedTracking(boolean enabled) {
		data.setExtendedTracking(enabled);
	}

	

}
