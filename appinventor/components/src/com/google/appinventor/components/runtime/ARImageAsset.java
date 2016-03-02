/**
 * 
 */
package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.ar4ai.VirtualObject;
import com.google.appinventor.components.runtime.util.MediaUtil;

/**
 * @author ivanruizrube
 *
 */
@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "Augmented Reality 2D Image Asset Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSAUGMENTEDREALITY, iconName = "images/arImageAsset.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.CAMERA, android.permission.ACCESS_NETWORK_STATE,android.permission.WRITE_EXTERNAL_STORAGE, android.permission.READ_EXTERNAL_STORAGE")

public class ARImageAsset extends ARVirtualObject{

	public ARImageAsset(ComponentContainer container) {
		super(container);
		this.getData().setVisualAssetType(VirtualObject.ASSET_IMAGE);
	}

	/**
	 * Returns the path of the 2d image to render.
	 *
	 * @return the path of the 2d image to render
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String OverlaidImage() {
		return data.getOverlaidImage();
	}

	/**
	 * Specifies the path of the 2d image to render.
	 *
	 * <p/>
	 * See {@link MediaUtil#determineMediaSource} for information about what a
	 * path can be.
	 *
	 * @param path
	 *            the path of the 2d image to render
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET_IMAGE, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void OverlaidImage(String path) {
		data.setOverlaidImage((path == null) ? "" : path);

	}



}
