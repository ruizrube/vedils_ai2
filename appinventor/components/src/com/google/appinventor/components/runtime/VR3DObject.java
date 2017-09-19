package com.google.appinventor.components.runtime;

import java.util.UUID;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.vr4ai.VRActivity;
import com.google.appinventor.components.runtime.vr4ai.util.Object3DParcelable;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "3D Object source for VR (by SPI-FM at UCA)", category = ComponentCategory.VEDILSVIRTUALREALITY, iconName = "images/virtualReality3DObject.png")
public class VR3DObject extends AndroidNonvisibleComponent {

	public String material3DPath;
	public String model3DPath;
	private ComponentContainer container;
	public String skyboxPath;
	public int positionX=0;
	public int positionY=0;
	public int positionZ=15;
	public int scale=1;
	private int ambientLight;
	public UUID id = UUID.randomUUID();
	public Object3DParcelable object3Dpar= new Object3DParcelable();
	private static VR3DObject instance;

	// Intent intent;
	public VR3DObject(ComponentContainer container) {

		super(container.$form());
		this.container = container;
		instance = this;
		object3Dpar.setId(id+"");
		//valores por defecto por si el usuario no ha introducido ninguno
		
		object3Dpar.setPositionX(positionX);
		object3Dpar.setPositionY(positionY);
		object3Dpar.setPositionZ(positionZ);
		object3Dpar.setScale(scale);
		
		// intent = new Intent("custom-event-name");

	}

	public static VR3DObject getInstance() {
		return instance;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ONLY_VRSCENE, defaultValue = "")
	@SimpleProperty(description = "Stick the virtual object to a given Camera", userVisible = true)
	public void VRScene(VRScene scene) {
		if (scene != null) {
			scene.is3DObject=true;
			scene.object3DList.add(object3Dpar);
			scene.setAssetToExtract(this);
			
		}
		
	}
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String getMaterial3D() {

		return material3DPath;
	}

	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public String getModel3D() {

		return model3DPath;
	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET_3DMODEL, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void Model3D(String path) {

		object3Dpar.setModel3DPath(path);
		this.model3DPath = path;

		Log.d("sender", path);

	}

	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET_MATERIAL, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void Material3D(String path) {

		object3Dpar.setMaterial3DPath(path);
		this.material3DPath = path;

		Log.d("sender", path);

	}

	
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public int getPositionX() {

		return positionX;
	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = "0")
	@SimpleProperty(userVisible = true)
	public void PositionX(int val) {

		object3Dpar.setPositionX(val);
		this.positionX = val;

		Log.d("sender", val+"");

	}
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public int getPositionY() {

		return positionY;
	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = "0")
	@SimpleProperty(userVisible = true)
	public void PositionY(int val) {

		object3Dpar.setPositionY(val);
		this.positionY = val;

		Log.d("sender", val+"");

	}
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public int getPositionZ() {

		return positionZ;
	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = "15")
	@SimpleProperty(userVisible = true)
	public void PositionZ(int val) {

		object3Dpar.setPositionZ(val);
		this.positionZ = val;

		Log.d("sender", val+"");

	}
	
	@SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
	public int getScale() {

		return scale;
	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = "1")
	@SimpleProperty(userVisible = true)
	public void Scale(int val) {

		object3Dpar.setScale(val);
		this.scale = val;

		Log.d("sender", val+"");

	}
	
	
	/*@SimpleFunction(description = "Reset position  object3D", userVisible = true)
	public void Reset() {
	
		    Intent resetIntent = new Intent(VRActivity.VR_3DOBJECT_RESET);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(resetIntent);

	}
	
	@SimpleFunction(description = "Rotate left position  object3D", userVisible = true)
	public void RotateLeft() {
	
		    Intent rotateLeft = new Intent(VRActivity.VR_3DOBJECT_ROTATE_LEFT);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(rotateLeft);

	}
	@SimpleFunction(description = "Rotate right position  object3D", userVisible = true)
	public void RotateRight() {
	
		    Intent rotateRight = new Intent(VRActivity.VR_3DOBJECT_ROTATE_RIGHT);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(rotateRight);

	}
	@SimpleFunction(description = "Rotate up position  object3D", userVisible = true)
	public void RotateUp() {
	
		    Intent rotateUp = new Intent(VRActivity.VR_3DOBJECT_ROTATE_UP);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(rotateUp);

	}
	@SimpleFunction(description = "Rotate down position  object3D", userVisible = true)
	public void RotateDown() {
	
		    Intent rotateDown = new Intent(VRActivity.VR_3DOBJECT_ROTATE_DOWN);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(rotateDown);

	}
	@SimpleFunction(description = "Zoom in position  object3D", userVisible = true)
	public void ZoomIn() {
	
		    Intent zoomIn = new Intent(VRActivity.VR_3DOBJECT_ZOOM_IN);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(zoomIn);

	}
	@SimpleFunction(description = "Zoom out position  object3D", userVisible = true)
	public void ZoomOut() {
	
		    Intent zoomOut = new Intent(VRActivity.VR_3DOBJECT_ZOOM_OUT);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(zoomOut);

	}
	@SimpleFunction(description = " Move up object3D", userVisible = true)
	public void MoveUp() {
	
		    Intent moveUp = new Intent(VRActivity.VR_3DOBJECT_MOVE_UP);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(moveUp);

	}
	@SimpleFunction(description = "Move down object3D", userVisible = true)
	public void MoveDown() {
	
		    Intent moveDown = new Intent(VRActivity.VR_3DOBJECT_MOVE_DOWN);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(moveDown);

	}
	@SimpleFunction(description = "Zoom out position  object3D", userVisible = true)
	public void MoveLeft() {
	
		    Intent moveLeft = new Intent(VRActivity.VR_3DOBJECT_MOVE_LEFT);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(moveLeft);

	}
	@SimpleFunction(description = "Zoom out position  object3D", userVisible = true)
	public void MoveRight() {
	
		    Intent moveRight = new Intent(VRActivity.VR_3DOBJECT_MOVE_RIGHT);
			LocalBroadcastManager.getInstance(container.$context()).sendBroadcast(moveRight);

	}*/

	

}
