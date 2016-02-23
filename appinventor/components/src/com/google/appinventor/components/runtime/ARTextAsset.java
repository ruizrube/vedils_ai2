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
@DesignerComponent(nonVisible = true, version = 1, description = "Augmented Reality Text Asset Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSAUGMENTEDREALITY, iconName = "images/arTextAsset.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.CAMERA, android.permission.ACCESS_NETWORK_STATE,android.permission.WRITE_EXTERNAL_STORAGE, android.permission.READ_EXTERNAL_STORAGE")

public class ARTextAsset extends ARVirtualObject{

	public ARTextAsset(ComponentContainer container) {
		super(container);
		this.getData().setVisualAssetType(VirtualObject.ASSET_TEXT);
	}
	
	

/**
	 * @return the overlaidText
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String OverlaidText() {
		return data.getOverlaidText();
	}

	/**
	 * @param overlaidText
	 *            the overlaidText to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(description = "Specifies the text to overlay", userVisible = true)
	public void OverlaidText(String overlaidText) {
		this.data.setOverlaidText(overlaidText);
	}



}
