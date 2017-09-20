package com.google.appinventor.components.runtime;

import java.util.ArrayList;
import java.util.UUID;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.jpctsource.Object3DAnimationParcelable;
import com.google.appinventor.components.runtime.util.OnInitializeListener;
import com.threed.jpct.RGBColor;

import android.content.Intent;
import android.graphics.Color;


@UsesLibraries(libraries = "3d4ai.jar, jpct_ae.jar, bones.jar")
@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "Viewer for 3D Objects Component (by SPI-FM at UCA)", category = ComponentCategory.EXPERIMENTAL, iconName = "images/model3DIcon.png")
public class Model3DViewer extends AndroidNonvisibleComponent implements OnInitializeListener, OnResumeListener, OnPauseListener, OnStopListener, OnDestroyListener, ActivityResultListener{

	private ComponentContainer container;
	private static final String JPCT_ACTIVITY_CLASS = "com.google.appinventor.components.runtime.jpctsource.JPCTActivity";
	//el action es una referencia que esta en el manifest que enlaza con el activity
	private static final String JPCT_ACTIVITY_ACTION = "com.mobile.jpctlauncher.LAUNCH_ACTIVITY";
	private final int requestCode;
	//puedo lanzar intent solo pasandole el nombre del action
	//Intent intent = new Intent(JPCT_ACTIVITY_ACTION);
	Intent intent = new Intent();
	
	
	public String model3DPath;
	public String material3DPath;
	private int positionX=0;
	private int positionY=0;
	private int positionZ=0;
	
	private float rotationX=0;
	private float rotationY=0;
	private float rotationZ=0;
	
	private float zoomMax=10;
	private float zoomMin=1;
	//private boolean zoomActivated=false;
	//private boolean rotateActivated=false;
	//private boolean moveActivated=false;
	private int animationSecuence=0;
	private int animationSpeed=1;
	private  ArrayList<String> texturePath=new ArrayList<String>();
	
	private boolean animationActivated=false;
	
	private boolean wideframeActivated=false;

	
	private float rotateSpeed=1;
	private float moveSpeed=5;
	private float zoomSpeed=5;
	private int scale=1;
	private String id=UUID.randomUUID()+"";
	
	private int rBGColor=100;
	private int gBGColor=100;
	private int bBGColor=100;
	
	public Object3DAnimationParcelable object3DParcel;


	private void setDefaultValues()
	{
		object3DParcel= new Object3DAnimationParcelable();
		
		object3DParcel.setPositionX(positionX);
		object3DParcel.setPositionY(positionY);
		object3DParcel.setPositionZ(positionZ);
		object3DParcel.setMoveSpeed(moveSpeed);
		
		
		object3DParcel.setRotationX(rotationX);
		object3DParcel.setRotationY(rotationY);
		object3DParcel.setRotationZ(rotationZ);
	
		object3DParcel.setRotateSpeed(rotateSpeed);
		
		
		object3DParcel.setScale(scale);
	
		object3DParcel.setZoomMax(zoomMax);
		object3DParcel.setZoomMin(zoomMin);
		object3DParcel.setZoomSpeed(zoomSpeed);
		
		
		object3DParcel.setBGColor(rBGColor, gBGColor, bBGColor);
		
		object3DParcel.setAnimationSpeed(animationSpeed);
		object3DParcel.setIsAnimated(animationActivated);
		object3DParcel.setAnimationSecuence(animationSecuence);
		
		
		object3DParcel.setTexturePath(texturePath);
		object3DParcel.setWideframeActivated(wideframeActivated);
		object3DParcel.setId(id);
		
		
		
		
	}
	public Model3DViewer(ComponentContainer container) {
		super(container.$form());
		
		this.container = container;

		container.$form().registerForOnInitialize(this);
		container.$form().registerForOnResume(this);
		container.$form().registerForOnPause(this);
		container.$form().registerForOnStop(this);
		container.$form().registerForOnDestroy(this);
		
		setDefaultValues();
		
		requestCode = form.registerForActivityResult(this);
		
		intent.setClassName(container.$context(), JPCT_ACTIVITY_CLASS);
	}

	
	
	@SimpleFunction(description = "Start 3d viewer", userVisible = true)
	public void Start() {
		
		//lanzo el activity que pinta objetos en 3D pasandole el intent que he creado antes
		intent.putExtra("objeto3DParcel", object3DParcel);
		container.$context().startActivityForResult(intent, requestCode);

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
	public void setModel3D(String path) {
		
		this.model3DPath=path;
		object3DParcel.setModel3DPath(model3DPath);
		
	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET_MATERIAL, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void setMaterial3D(String path) {
		
		this.material3DPath=path;
		object3DParcel.setMaterial3DPath(material3DPath);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET_TEXTURES_LIST, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void setTextures(String value) {
		
		System.out.println("TEXTURASSSS : "+value);
		String splitRecord[]=value.split("Record elements: ");
		
		String textures[]=splitRecord[1].split(" - ");
		
		
		for(int i=0;i<textures.length;i++)
		{
			texturePath.add(textures[i]);
		}
		
		object3DParcel.setTexturePath(texturePath);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = "0")
	@SimpleProperty(userVisible = true)
	public void setPositionX(int value) {
		
		this.positionX=value;
		object3DParcel.setPositionX(positionX);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = "0")
	@SimpleProperty(userVisible = true)
	public void setPositionY(int value) {
		
		this.positionY=value;
		object3DParcel.setPositionY(positionY);
		
	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = "0")
	@SimpleProperty(userVisible = true)
	public void setPositionZ(int value) {
		
		this.positionZ=value;
		object3DParcel.setPositionZ(positionZ);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = "1")
	@SimpleProperty(userVisible = true)
	public void setScale(int value) {
		
		this.scale=value;
		object3DParcel.setScale(scale);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT, defaultValue ="5" )
	@SimpleProperty(userVisible = true)
	public void setMoveSpeed(float value) {
		
		this.moveSpeed=value;
		object3DParcel.setMoveSpeed(moveSpeed);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT, defaultValue = "0")
	@SimpleProperty(userVisible = true)
	public void setRotationX(float value) {
		
		this.rotationX=value;
		object3DParcel.setRotationX(rotationX);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT, defaultValue = "0")
	@SimpleProperty(userVisible = true)
	public void setRotationY(float value) {
		
		this.rotationY=value;
		object3DParcel.setRotationY(rotationY);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT, defaultValue = "0")
	@SimpleProperty(userVisible = true)
	public void setRotationZ(float value) {
		
		this.rotationZ=value;
		object3DParcel.setRotationZ(rotationZ);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT, defaultValue ="1" )
	@SimpleProperty(userVisible = true)
	public void setRotateSpeed(float value) {
		
		this.rotateSpeed=value;
		object3DParcel.setRotateSpeed(animationSpeed);
		
	}
	
	
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT, defaultValue = "10")
	@SimpleProperty(userVisible = true)
	public void setZoomMax(float value) {
		
		this.zoomMax=value;
		object3DParcel.setZoomMax(zoomMax);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT, defaultValue = "1")
	@SimpleProperty(userVisible = true)
	public void setZoomMin(float value) {
		
		this.zoomMin=value;
		object3DParcel.setZoomMin(zoomMin);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_FLOAT, defaultValue ="5" )
	@SimpleProperty(userVisible = true)
	public void setZoomSpeed(float value) {
		
		this.zoomSpeed=value;
		object3DParcel.setZoomSpeed(value);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue ="0" )
	@SimpleProperty(userVisible = true)
	public void setAnimationSecuence(int value) {
		
		this.animationSecuence=value;
		object3DParcel.setAnimationSecuence(animationSecuence);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue ="0" )
	@SimpleProperty(userVisible = true)
	public void setAnimationSpeed(int value) {
		
		this.animationSpeed=value;
		object3DParcel.setAnimationSpeed(animationSpeed);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue ="false" )
	@SimpleProperty(userVisible = true)
	public void setAnimationActivated(boolean value) {
		
		this.animationActivated=value;
		object3DParcel.setIsAnimated(animationActivated);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR , defaultValue = Component.DEFAULT_VALUE_COLOR_BLACK)
	@SimpleProperty(userVisible = true)
	public void setBGColor(int value) {
		//RGBColor myColor = new com.threed.jpct.RGBColor(Color.red(value), Color.green(value), Color.blue(value));
		this.rBGColor=Color.red(value);
		this.gBGColor=Color.green(value);
		this.bBGColor=Color.blue(value);
		object3DParcel.setBGColor(rBGColor, gBGColor, bBGColor);
		
	}
	
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue ="false" )
	@SimpleProperty(userVisible = true)
	public void setWideframeActivated(boolean value) {
		
		this.wideframeActivated=value;
		object3DParcel.setWideframeActivated(wideframeActivated);
		
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resultReturned(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
	}

}
