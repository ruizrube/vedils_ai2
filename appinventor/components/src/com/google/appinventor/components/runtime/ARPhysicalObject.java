/**
 * 
 */
package com.google.appinventor.components.runtime;

import java.util.UUID;

import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.ar4ai.PhysicalObject;
import com.google.appinventor.components.runtime.util.OnInitializeListener;

/**
 * @author ivanruizrube
 *
 */

@SimpleObject
public abstract class ARPhysicalObject extends AndroidNonvisibleComponent implements OnInitializeListener, OnResumeListener, OnStopListener, OnPauseListener, OnDestroyListener {

	private static final float DEFAULT_SENSITIVITYTHRESHOLD = 0.3f;

	/////////////////////
	// LOCAL ATTRIBUTES //
	/////////////////////

	protected final ComponentContainer container;

	protected PhysicalObject data;

	protected ARVirtualObject arVO;

	/////////////////////
	// CONSTRUCTOR //
	/////////////////////

	public ARPhysicalObject(ComponentContainer container) {
		super(container.$form());
		this.container = container;

		container.$form().registerForOnInitialize(this);
		container.$form().registerForOnResume(this);
		container.$form().registerForOnPause(this);
		container.$form().registerForOnStop(this);
		container.$form().registerForOnDestroy(this);

		data = new PhysicalObject(UUID.randomUUID().toString());

		ARCamera.mapOfARPhysicalObjects.put(this.getData().getId(), this);

	}

	/////////////////////
	// ACCESS METHODS //
	/////////////////////

	/**
	 * @return the data
	 */
	public PhysicalObject getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(PhysicalObject data) {
		this.data = data;
	}

	///////////////////////
	// OVERRIDED METHODS //
	///////////////////////

	@Override
	public void onInitialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}

	///////////////////////
	// PROPERTIES //
	///////////////////////

	/**
	 * @return the state of the physical object
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public boolean Enabled() {
		return data.isEnabled();
	}

	/**
	 * @param the
	 *            state of the physical object the state of the physical object
	 *            to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "True")
	@SimpleProperty(description = "Specifies if the physical object is enabled", userVisible = true)
	public void Enabled(boolean value) {
		this.data.setEnabled(value);
	}

	/**
	 * @return the sensitivityThresholdX
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the physical object's threshold for the sensitivy in the X axis")
	public float SensitivityThresholdX() {
		return data.getSensitivityThresholdX();
	}

	/**
	 * @param sensitivityThresholdX
	 *            the sensitivityThresholdX to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT, defaultValue = DEFAULT_SENSITIVITYTHRESHOLD + "")
	@SimpleProperty(description = "Specifies the physical object's threshold for the sensitivy in the X axis.")
	public void SensitivityThresholdX(float sensitivityThresholdX) {
		this.data.setSensitivityThresholdX(sensitivityThresholdX);
	}

	/**
	 * @return the sensitivityThresholdY
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the physical object's threshold for the sensitivy in the Y axis")
	public float SensitivityThresholdY() {
		return data.getSensitivityThresholdY();
	}

	/**
	 * @param sensitivityThresholdY
	 *            the sensitivityThresholdY to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT, defaultValue = DEFAULT_SENSITIVITYTHRESHOLD + "")
	@SimpleProperty(description = "Specifies the physical object's threshold for the sensitivy in the Y axis.")
	public void SensitivityThresholdY(float sensitivityThresholdY) {
		this.data.setSensitivityThresholdY(sensitivityThresholdY);
	}

	/**
	 * @return the sensitivityThresholdZ
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the physical object's threshold for the sensitivy in the Z axis")
	public float SensitivityThresholdZ() {
		return data.getSensitivityThresholdZ();
	}

	/**
	 * @param sensitivityThresholdZ
	 *            the sensitivityThresholdZ to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT, defaultValue = DEFAULT_SENSITIVITYTHRESHOLD + "")
	@SimpleProperty(description = "Specifies the physical object's threshold for the sensitivy in the Z axis.")
	public void SensitivityThresholdZ(float sensitivityThresholdZ) {
		this.data.setSensitivityThresholdZ(sensitivityThresholdZ);
	}

	/**
	 * @return the ARVirtualObject
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public ARVirtualObject StickTo() {
		return arVO;

	}

	/**
	 * @param ARVirtualObject
	 *            the ARVirtualObject to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COMPONENT, defaultValue = "")
	@SimpleProperty(description = "Stick the physical object to a given virtual Object", userVisible = true)
	public void StickTo(ARVirtualObject vo) {
		arVO = vo;
		this.data.setVirtualObject(arVO.getData());

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

	////////////
	// EVENTS //
	////////////

	/**
	 * Event to be raised after a physical object has appeared
	 */
	@SimpleEvent(description = "Event to be raised after a physical object has appeared", userVisible = true)
	public void Appears(float x, float y, float z) {
			EventDispatcher.dispatchEvent(this, "Appears", x, y, z);
	}

	/**
	 * Event to be raised after a physical object has disappeared
	 */
	@SimpleEvent(description = "Event to be raised after a physical object has disappeared", userVisible = true)
	public void Disappears() {
		EventDispatcher.dispatchEvent(this, "Disappears");
	}

	/**
	 * Event to be raised after a physical object has changed its position
	 */
	@SimpleEvent(description = "Event to be raised after a physical object has changed its position", userVisible = true)
	public void ChangedPosition(float x, float y, float z) {
		EventDispatcher.dispatchEvent(this, "ChangedPosition", x, y, z);
	}

}
