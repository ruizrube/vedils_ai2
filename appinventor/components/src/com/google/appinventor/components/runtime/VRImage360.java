package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;

import android.util.Log;

@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "Image360 source for VR (by SPI-FM at UCA)", category = ComponentCategory.VEDILSVIRTUALREALITY, iconName = "images/virtualRealityImage.png")
public class VRImage360 extends AndroidNonvisibleComponent{

	private ComponentContainer container;
	public String image360Path;
	public static VRImage360 instance;

	public VRImage360(ComponentContainer container) {
		super(container.$form());
		this.container = container;
		instance = this;
		
	}
	public static VRImage360 getInstance() {
		return instance;
	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ONLY_VRSCENE, defaultValue = "")
	@SimpleProperty(description = "Stick the virtual object to a given Camera", userVisible = true)
	public void VRScene(VRScene scene) {
		if (scene != null) {
			scene.isImage360=true;
			scene.setAssetToExtract(this);
			
		}
		
	}
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String getImage360() {
		
		return image360Path;
	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET_IMAGE, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void setImage360(String path) {
		
		this.image360Path=path;
		
		
		  Log.d("image360", path);
		
	}

}
