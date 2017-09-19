package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.util.OnInitializeListener;

import android.content.Intent;


@UsesLibraries(libraries = "3d4ai.jar, jpct_ae.jar")
@SimpleObject
@DesignerComponent(nonVisible = true, version = 1, description = "Viewer for 3D Objects Component (by SPI-FM at UCA)", category = ComponentCategory.EXPERIMENTAL, iconName = "images/arCamera.png")
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


	public Model3DViewer(ComponentContainer container) {
		super(container.$form());
		
		this.container = container;

		container.$form().registerForOnInitialize(this);
		container.$form().registerForOnResume(this);
		container.$form().registerForOnPause(this);
		container.$form().registerForOnStop(this);
		container.$form().registerForOnDestroy(this);
		
		requestCode = form.registerForActivityResult(this);
		
		intent.setClassName(container.$context(), JPCT_ACTIVITY_CLASS);
	}

	
	
	@SimpleFunction(description = "Start 3d viewer", userVisible = true)
	public void Start() {
		
		//lanzo el activity que pinta objetos en 3D pasandole el intent que he creado antes
		intent.putExtra("3Dmodel",this.getModel3D());
		intent.putExtra("3Dmaterial",this.getMaterial3D());
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
		
	}
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET_MATERIAL, defaultValue = "")
	@SimpleProperty(userVisible = true)
	public void setMaterial3D(String path) {
		
		this.material3DPath=path;
		
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
