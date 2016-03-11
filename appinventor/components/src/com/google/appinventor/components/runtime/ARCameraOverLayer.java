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
import com.google.appinventor.components.runtime.ar4ai.UIVariables;


@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "Augmented Reality Camera Over Layer Component (by SPI-FM at UCA)", category = ComponentCategory.VEDILSAUGMENTEDREALITY, iconName = "images/arCamera.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET, android.permission.CAMERA, android.permission.ACCESS_NETWORK_STATE,android.permission.WRITE_EXTERNAL_STORAGE, android.permission.READ_EXTERNAL_STORAGE")
public class ARCameraOverLayer extends AndroidNonvisibleComponent {
	
	public UIVariables data;
	
	public ARCameraOverLayer(ComponentContainer container) {
		super(container.$form());
		data = new UIVariables();
	}
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public boolean LeftButtonEnabled() {
		return data.isLeftBtEnabled();
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
	@SimpleProperty(description = "Enables the left button on the user interface over the camera", userVisible = true)
	public void LeftButtonEnabled(boolean enable) {
		this.data.setLeftBtEnabled(enable);
	}
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = true)
	public boolean RightButtonEnabled() {
		return data.isRightBtEnabled();
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "False")
	@SimpleProperty(description = "Enables the right button on the user interface over the camera", userVisible = true)
	public void RightButtonEnabled(boolean enable) {
		this.data.setRightBtEnabled(enable);
	}
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String LeftButtonText() {
		return data.getLeftBtText();
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(description = "Sets the text for the left button on the user interface over the camera", userVisible = true)
	public void LeftButtonText(String text) {
		this.data.setLeftBtText(text);
	}
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String RightButtonText() {
		return data.getRightBtText();
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(description = "Sets the text for the left button on the user interface over the camera", userVisible = true)
	public void RightButtonText(String text) {
		this.data.setRightBtText(text);
	}
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String FloatingText() {
		return data.getFloatingText();
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "")
	@SimpleProperty(description = "Sets the text in the middle of the buttons on the user interface over the camera", userVisible = true)
	public void FloatingText(String text) {
		this.data.setFloatingText(text);
	}
	
	@SimpleEvent(description = "When the left button is clicked", userVisible = true)
	public void LeftButtonClick() {
		EventDispatcher.dispatchEvent(this, "LeftButtonClick");
	}
	
	@SimpleEvent(description = "When the right button is clicked", userVisible = true)
	public void RightButtonClick() {
		EventDispatcher.dispatchEvent(this, "RightButtonClick");
	}
	
	
}
