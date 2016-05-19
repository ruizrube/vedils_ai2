package com.example.libarpruebas;

import java.util.ArrayList;
import java.util.UUID;

import com.google.appinventor.components.runtime.ar4ai.Camera;
import com.google.appinventor.components.runtime.ar4ai.PhysicalObject;
import com.google.appinventor.components.runtime.ar4ai.VirtualObject;
import com.google.appinventor.components.runtime.ar4ai.UIVariables;
import com.google.appinventor.components.runtime.ar4ai.vuforia.VuforiaARActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private static final String TAG = "MyActivity DEMO";
	private static final String AR_ACTIVITY_CLASS = "com.google.appinventor.components.runtime.ar4ai.vuforia.VuforiaARActivity";
	Button button;
	boolean buttonPressed = false;
	UIVariables uivariables;

	// Elementos para comunicar al Intent
	private Camera data = new Camera();
	private ArrayList<VirtualObject> arrayOfVirtualObjects;
	private ArrayList<PhysicalObject> arrayOfPhysicalObject;

	// Intent Principal
	private Intent mIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.v(TAG, "Antes de configurar variables ");
		data = new Camera();
		data.setTitle("HOLA!");
		data.setScreenOrientation(-1);
		data.setPathTargetDBDAT("UALGise.dat");
		data.setPathTargetDBXML("UALGise.xml");
		uivariables = new UIVariables();
		uivariables.setLeftBtEnabled(true);
		uivariables.setLeftBtText("Reft");
		uivariables.setRightBtEnabled(true);
		uivariables.setRightBtText("Ruait");
		uivariables.setFloatingText("Textico");
		data.setUivariables(uivariables);
//		
//		data.setPathTargetDBDAT("IvanDatabase.dat");
//		data.setPathTargetDBXML("IvanDatabase.xml");
		
//		data.setPathTargetDBDAT("StonesAndChips.dat");
//		data.setPathTargetDBXML("StonesAndChips.xml");
		
		arrayOfVirtualObjects = new ArrayList<VirtualObject>();
		arrayOfPhysicalObject = new ArrayList<PhysicalObject>();

		Log.v(TAG, "Después de configurar variables ");

		PhysicalObject mPhysicalObject1 = new PhysicalObject(UUID.randomUUID().toString());
		mPhysicalObject1.setTrackerType(PhysicalObject.TRACKER_MARKER);
		mPhysicalObject1.setMarkerTracker(1);
//		
		PhysicalObject mPhysicalObject3 = new PhysicalObject(UUID.randomUUID().toString());
		mPhysicalObject3.setTrackerType(PhysicalObject.TRACKER_MARKER);
		mPhysicalObject3.setMarkerTracker(3);
		
		PhysicalObject mPhysicalObject2 = new PhysicalObject(UUID.randomUUID().toString());
		mPhysicalObject2.setTrackerType(PhysicalObject.TRACKER_TEXT);
		mPhysicalObject2.setTextTracker("Hola");
		
//		PhysicalObject mPhysicalObject1 = new PhysicalObject(UUID.randomUUID().toString());
//		mPhysicalObject1.setTrackerType(PhysicalObject.TRACKER_TARGETDB);
//		mPhysicalObject1.setTargetDBTracker("EX1_1");
		
//		PhysicalObject mPhysicalObject3 = new PhysicalObject(UUID.randomUUID().toString());
//		mPhysicalObject3.setTrackerType(PhysicalObject.TRACKER_TARGETDB);
//		mPhysicalObject3.setTargetDBTracker("EX1_2");
		
//		PhysicalObject mPhysicalObject1 = new PhysicalObject(UUID.randomUUID().toString());
//		mPhysicalObject1.setTrackerType(PhysicalObject.TRACKER_TARGETDB);
//		mPhysicalObject1.setTargetDBTracker("stones");
//		
//		PhysicalObject mPhysicalObject3 = new PhysicalObject(UUID.randomUUID().toString());
//		mPhysicalObject3.setTrackerType(PhysicalObject.TRACKER_TARGETDB);
//		mPhysicalObject3.setTargetDBTracker("chips");

		
		
		
		VirtualObject mVirtualObject1 = new VirtualObject(UUID.randomUUID().toString());
		mVirtualObject1.setVisualAssetType(VirtualObject.ASSET_3DMODEL);	
		mVirtualObject1.setOverlaid3DModel("7_Exercicio_5.obj");
		mVirtualObject1.setMaterial("7_Exercicio_5.mtl");
		mVirtualObject1.setRotationZ(270);
		mVirtualObject1.setColorTexture(Color.BLUE);
		//mVirtualObject1.setVisualAssetType(VirtualObject.ASSET_IMAGE);
		//mVirtualObject1.setOverlaidImage("amyWinehouse.png");
		
		VirtualObject mVirtualObject3 = new VirtualObject(UUID.randomUUID().toString());
		mVirtualObject3.setVisualAssetType(VirtualObject.ASSET_IMAGE);
		mVirtualObject3.setOverlaidImage("amyWinehouse.png");
		mVirtualObject3.setPositionX(90);
		mVirtualObject3.setTranslationY(90);
//		mVirtualObject3.setVisualAssetType(VirtualObject.ASSET_3DMODEL);	
//		mVirtualObject3.setOverlaid3DModel("EX1_2.obj");
//		mVirtualObject3.setMaterial("EX1_2.mtl");
//		mVirtualObject3.setColorTexture(Color.YELLOW);
		
		VirtualObject mVirtualObject2 = new VirtualObject(UUID.randomUUID().toString());
		mVirtualObject2.setVisualAssetType(VirtualObject.ASSET_3DMODEL);	
		mVirtualObject2.setOverlaid3DModel("ogro.md2");
		mVirtualObject2.setColorTexture(Color.BLUE);
		mVirtualObject2.setAnimated(true);
		mVirtualObject2.setAnimationSecuence(2);
//		
//		VirtualObject mVirtualObject1 = new VirtualObject(UUID.randomUUID().toString());
//		mVirtualObject1.setVisualAssetType(VirtualObject.ASSET_3DMODEL);	
//		mVirtualObject1.setOverlaid3DModel("Arrow1.md2");
//		mVirtualObject1.setColorTexture(Color.YELLOW);
//	
//		VirtualObject mVirtualObject3 = new VirtualObject(UUID.randomUUID().toString());
//		mVirtualObject3.setVisualAssetType(VirtualObject.ASSET_3DMODEL);	
//		mVirtualObject3.setOverlaid3DModel("Arrow1.md2");
//		mVirtualObject3.setColorTexture(Color.BLUE);
	
		
		
		mVirtualObject1.setPhysicalObject(mPhysicalObject2);
		mVirtualObject3.setPhysicalObject(mPhysicalObject3);
		mVirtualObject2.setPhysicalObject(mPhysicalObject1);
		

		arrayOfVirtualObjects.add(mVirtualObject1);
		arrayOfVirtualObjects.add(mVirtualObject3);
		arrayOfVirtualObjects.add(mVirtualObject2);
		arrayOfPhysicalObject.add(mPhysicalObject1);
		arrayOfPhysicalObject.add(mPhysicalObject3);
		arrayOfPhysicalObject.add(mPhysicalObject2);

		setContentView(R.layout.activity_main);

		Log.v(TAG, "Antes crear Intent ");
		addListenerOnButton();
		mIntent = getIntent();
		Log.v(TAG, "Despues crear Intent");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	public void addListenerOnButton() {
		Log.v(TAG, "Confirguro Acción del boton");
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v(TAG, "Boton Pulsado");
				if (buttonPressed) {
					//arrayOfVirtualObjects.get(0).removePhysicalObject(arrayOfPhysicalObject.get(0));
					//arrayOfVirtualObjects.get(2).setPhysicalObject(arrayOfPhysicalObject.get(0));
					data.getUivariables().setFloatingText("Botonaco pursao");
					uivariables.setLeftBtText("Trucaso");
				}
				else
					buttonPressed = true;
				
				startActivity(mIntent);
			}

		});
	}

	public Intent getIntent() {
		Intent intent = new Intent();

		intent.setClassName(this, AR_ACTIVITY_CLASS);

		intent.putExtra(VuforiaARActivity.AR_ACTIVITY_ARG_CAMERAOBJECT, this.data);
		intent.putExtra(VuforiaARActivity.AR_ACTIVITY_ARG_VIRTUALOBJECTS, this.getArrayOfVirtualObjects());
		intent.putExtra(VuforiaARActivity.AR_ACTIVITY_ARG_PHYSICALOBJECTS, this.getArrayOfPhysicalObject());

		return intent;
	}

	public ArrayList<VirtualObject> getArrayOfVirtualObjects() {
		return arrayOfVirtualObjects;
	}

	public ArrayList<PhysicalObject> getArrayOfPhysicalObject() {
		return arrayOfPhysicalObject;
	}

}
