/**
 * 
 */
package com.google.appinventor.components.runtime;

import java.io.Serializable;
import java.util.UUID;

import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.ar4ai.ARActivity;
import com.google.appinventor.components.runtime.ar4ai.PhysicalObject;
import com.google.appinventor.components.runtime.ar4ai.VirtualObject;
import com.google.appinventor.components.runtime.util.OnInitializeListener;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;

/**
 * @author ivanruizrube
 *
 */

@SimpleObject
public class ARVirtualObject extends AndroidNonvisibleComponent
		implements Serializable, OnInitializeListener, OnResumeListener, OnStopListener, OnPauseListener, OnDestroyListener {

	/////////////////
	// CONSTANTS //
	/////////////////

	private static final long serialVersionUID = 3L;

	private static final float DEFAULT_POSITION = 0.0f;
	private static final float DEFAULT_ROTATION = 0.0f;
	private static final float DEFAULT_TRANSLATION = 0f;
	private static final float DEFAULT_SCALE = 1f;

	private static final float DEFAULT_MASS = 1;

	/////////////////////
	// LOCAL ATTRIBUTES //
	/////////////////////

	private final ComponentContainer container;
	private final Handler androidUIHandler; // for posting actions
	
	protected ARPhysicalObject arPO;
	protected ARCamera camera;

	protected VirtualObject data;

	
	/////////////////
	// CONSTRUCTOR //
	/////////////////

	public ARVirtualObject(ComponentContainer container, Handler handler) {
		super(container.$form());
		this.container = container;
		androidUIHandler = handler;

		container.$form().registerForOnInitialize(this);
		container.$form().registerForOnResume(this);
		container.$form().registerForOnPause(this);
		container.$form().registerForOnStop(this);
		container.$form().registerForOnDestroy(this);

		data = new VirtualObject(UUID.randomUUID().toString());

		//ARCamera.mapOfARVirtualObjects.put(this.getData().getId(), this);

		
	}

	public ARVirtualObject(ComponentContainer container) {
		// Note that although this is creating a new Handler, there is
		// only one UI thread in an Android app and posting to this
		// handler queues up a Runnable for execution on that thread.
		this(container, new Handler());
	}

	/////////////////////
	// ACCESS METHODS //
	/////////////////////

	/**
	 * @return the data
	 */
	public VirtualObject getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(VirtualObject data) {
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

	//////////////////////
	// INTERNAL METHODS //
	//////////////////////

	/**
	 * Posts a dispatch for the specified event. This guarantees that event
	 * handlers run with serial semantics, e.g., appear atomic relative to each
	 * other.
	 *
	 * This method is overridden in tests.
	 *
	 * @param vo
	 *            the instance on which the event takes place
	 * @param eventName
	 *            the name of the event
	 * @param args
	 *            the arguments to the event handler
	 */
	protected void postEvent(final ARVirtualObject vo, final String eventName, final Object... args) {
		androidUIHandler.post(new Runnable() {
			public void run() {
				EventDispatcher.dispatchEvent(vo, eventName, args);
			}
		});
	}
	
	private Intent getIntent(String name, float value) {
		Intent intent = new Intent(ARActivity.AR_ACTIVITY_SIGNAL_REFRESH_MODELS);
		
		intent.putExtra("uuid", data.getId());
		intent.putExtra("parameter", name);
		intent.putExtra("value", value);
		
		return intent;
	}

	///////////////////////
	// PROPERTIES //
	///////////////////////

	/**
	 * @return the state of the virtual object
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public boolean Enabled() {
		return data.isEnabled();
	}

	/**
	 * @param the
	 *            state of the virtual object the state of the virtual object to
	 *            set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "True")
	@SimpleProperty(description = "Specifies if the virtual object is enabled", userVisible = true)
	public void Enabled(boolean value) {
		this.data.setEnabled(value);
	}

	/**
	 * @return the positionX
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the virtual object's position in the X axis")
	public float PositionX() {
		return data.getPositionX();
	}

	/**
	 * @param positionX
	 *            the positionX to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT, defaultValue = DEFAULT_POSITION + "")
	@SimpleProperty(description = "Specifies the virtual object's position in the X axis.")
	public void PositionX(float positionX) {
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(getIntent("PositionX", positionX));
		this.data.setPositionX(positionX);
	}

	/**
	 * @return the positionY
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the virtual object's position in the Y axis")
	public float PositionY() {
		return data.getPositionY();
	}

	/**
	 * @param positionY
	 *            the positionY to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT, defaultValue = DEFAULT_POSITION + "")
	@SimpleProperty(description = "Specifies the virtual object's position in the Y axis.")
	public void PositionY(float positionY) {
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(getIntent("PositionY", positionY));
		this.data.setPositionY(positionY);
	}

	/**
	 * @return the positionZ
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the virtual object's position in the Z axis")
	public float PositionZ() {
		return data.getPositionZ();
	}

	/**
	 * @param positionZ
	 *            the positionZ to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT, defaultValue = DEFAULT_POSITION + "")
	@SimpleProperty(description = "Specifies the virtual object's position in the Z axis.")
	public void PositionZ(float positionZ) {
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(getIntent("PositionZ", positionZ));
		this.data.setPositionZ(positionZ);
	}

	/**
	 * @return the rotationX
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the virtual object's rotation in the X axis")
	public float RotationX() {
		return data.getRotationX();
	}

	/**
	 * @param rotationX
	 *            the rotationX to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT, defaultValue = DEFAULT_ROTATION + "")
	@SimpleProperty(description = "Specifies the virtual object's rotation in the X axis.")
	public void RotationX(float rotationX) {
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(getIntent("RotationX", rotationX - data.getRotationX()));
		this.data.setRotationX(rotationX);
	}

	/**
	 * @return the rotationY
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the virtual object's rotation in the Y axis")
	public float RotationY() {
		return data.getRotationY();
	}

	/**
	 * @param rotationY
	 *            the rotationY to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT, defaultValue = DEFAULT_ROTATION + "")
	@SimpleProperty(description = "Specifies the virtual object's rotation in the Y axis.")
	public void RotationY(float rotationY) {
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(getIntent("RotationY", rotationY - data.getRotationY()));
		this.data.setRotationY(rotationY);
	}

	/**
	 * @return the rotationZ
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the virtual object's rotation in the Z axis")
	public float RotationZ() {
		return data.getRotationZ();
	}

	/**
	 * @param rotationZ
	 *            the rotationZ to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT, defaultValue = DEFAULT_ROTATION + "")
	@SimpleProperty(description = "Specifies the virtual object's rotation in the Z axis.")
	public void RotationZ(float rotationZ) {
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(getIntent("RotationZ", rotationZ - data.getRotationZ()));
		this.data.setRotationZ(rotationZ);
	}

	/**
	 * @return the translationX
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the virtual object's translation in the X axis")
	public float TranslationX() {
		return data.getTranslationX();
	}

	/**
	 * @param translationX
	 *            the translationX to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT, defaultValue = DEFAULT_TRANSLATION + "")
	@SimpleProperty(description = "Specifies the virtual object's translation in the X axis.")
	public void TranslationX(float translationX) {
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(getIntent("TranslationX", translationX - data.getTranslationX()));
		this.data.setTranslationX(translationX);
	}

	/**
	 * @return the translationY
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the virtual object's translation in the Y axis")
	public float TranslationY() {
		return data.getTranslationY();
	}

	/**
	 * @param translationY
	 *            the translationY to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT, defaultValue = DEFAULT_TRANSLATION + "")
	@SimpleProperty(description = "Specifies the virtual object's translation in the Y axis.")
	public void TranslationY(float translationY) {
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(getIntent("TranslationY", translationY - data.getTranslationY()));
		this.data.setTranslationY(translationY);
	}

	/**
	 * @return the translationZ
	 */
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the virtual object's translation in the Z axis")
	public float TranslationZ() {
		return data.getTranslationZ();
	}

	/**
	 * @param translationZ
	 *            the translationZ to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT, defaultValue = DEFAULT_TRANSLATION + "")
	@SimpleProperty(description = "Specifies the virtual object's translation in the Z axis.")
	public void TranslationZ(float translationZ) {
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(getIntent("TranslationZ", translationZ - data.getTranslationZ()));
		this.data.setTranslationZ(translationZ);
	}
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the scale of the object")
	public float Scale() {
		return data.getScale();
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT, defaultValue = DEFAULT_SCALE + "")
	@SimpleProperty(userVisible = true, description ="Specifies the virtual object's scale")
	public void Scale(float scale) {
		LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(getIntent("Scale", scale - data.getScale()));
		this.data.setScale(scale);
	}
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public ARPhysicalObject StickTo() {
		return arPO;
	}
	
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_KIND_OF_TRACKERS, defaultValue = "")
	@SimpleProperty(description = "Stick the virtual object to a given physical Object", userVisible = true)
	public void StickTo(ARPhysicalObject arPO) {
		if (this.arPO != null)
			this.data.removePhysicalObject(this.arPO.getData());
		this.arPO = arPO;
		this.data.setPhysicalObject(arPO.getData());
		if (camera != null)
			camera.mapOfARPhysicalObjects.put(arPO.getData().getId(), arPO);
	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public ARCamera ARCamera() {
		return camera;
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ONLY_ARCAMERA, defaultValue = "")
	@SimpleProperty(description = "Stick the virtual object to a given Camera", userVisible = true)
	public void ARCamera(ARCamera camera) {
		if (camera != null) {
			camera.mapOfARVirtualObjects.put(this.getData().getId(), this);
			if (arPO != null)
				camera.mapOfARPhysicalObjects.put(arPO.getData().getId(), arPO);
		}
		else
			this.camera.mapOfARVirtualObjects.remove(this.getData().getId());
		this.camera = camera;
	}
	

	/**
	 * @return the mass
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the virtual object's mass for gravity calculation")
	public float Mass() {
		return data.getMass();
	}

	/**
	 * @param mass
	 *            the mass to set
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_FLOAT, defaultValue = DEFAULT_MASS + "")
	@SimpleProperty(description = "Specifies the virtual object's mass for gravity calculation")
	public void Mass(float mass) {
		this.data.setMass(mass);
	}

		
	
	


	////////////////
	// FUNCTIONS //
	////////////////

	@SimpleEvent(description = "Event to be raised after a virtual object collides with another", userVisible = true)
	public void CollidedWith(ARVirtualObject other) {
		postEvent(this, "CollidedWith", other);
	}

}
