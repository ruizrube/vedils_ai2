package com.google.appinventor.components.runtime.vr4ai;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.GLSLShader;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.ShaderLocator;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;
import com.threed.jpct.util.SkyBox;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.appinventor.components.runtime.vr4ai.util.Object3DParcelable;
import com.google.appinventor.components.runtime.vr4ai.util.Util;;

public class VR3DObjectRenderScene implements CardboardView.StereoRenderer{

	private VRActivity mActivity;
	Object3D object3D;
	private World world;
	private FrameBuffer frameBuffer;
    private SkyBox skyBox;
    private final Matrix tempTransform = new Matrix();
    private float[] mHeadView = new float[16];
    private Light sun;
    Object3D[] object3DArray=null;
    Object3D focusObject=null;
    Map<String, Object3D> objectsUUIDMap = new HashMap<String, Object3D>();
   


   public void setFocusObject(String uuid)
   {
	   
	   focusObject=objectsUUIDMap.get(uuid);
   }
   public void moveFocusObject()
   {
	   int position=0;
	   for(int i=0;i<object3DArray.length;i++)
	   {
		   if(object3DArray[i]==focusObject)
		   {
			   position=i;
		   }
	   }
	   position=position+1;
	   if(position>object3DArray.length-1)
	   {
		   focusObject=object3DArray[0];
	   }
	   else
	   {
		   focusObject=object3DArray[position];
	   }
	   
	   
   }
    public void  reset3DPosition()
    {
    	
    	//focusObject.clearTranslation();
    	//focusObject.clearRotation();
    	//focusObject.translate(mActivity.positionX, mActivity.positionY, mActivity.positionZ);
    	//focusObject.scale(mActivity.scale);
    }
    
    public  void move3DObject(String direction)
    {
    	
    	float  posX=focusObject.getTranslation().x;
    	float  posY=focusObject.getTranslation().y;
    	float  posZ=focusObject.getTranslation().z;
    	focusObject.clearTranslation();

    	switch(direction)
    	{
    	case"up":
    		focusObject.translate(posX,posY-mActivity.moveSpeed, posZ);
    		break;
    	case"down":
    		focusObject.translate(posX,posY+mActivity.moveSpeed, posZ);
    		break;
    	case"left":
    		focusObject.translate(posX-mActivity.moveSpeed, posY, posZ);
    		break;
    	case"right":
    		focusObject.translate(posX+mActivity.moveSpeed, posY, posZ);
    		break;
    	}
    	
    }
    public  void rotate3DObject(String direction)
    {
    	
    	switch(direction)
    	{
    	case"up":
    		focusObject.rotateX(mActivity.rotateSpeed);
    		break;
    	case"down":
    		focusObject.rotateX(-mActivity.rotateSpeed);
    		break;
    	case"left":
    		focusObject.rotateY(mActivity.rotateSpeed);
    		break;
    	case"right":
    		focusObject.rotateY(-mActivity.rotateSpeed);
    		break;
    	}
    	
    }
    public  void zoom3DObject(String mode)
    {
    	
    	switch(mode)
    	{
    	case "In":
    		focusObject.setScale(focusObject.getScale()+0.2f);
    		break;
    	case "Out":
    		if(focusObject.getScale()-0.2f>0){
    			focusObject.setScale(focusObject.getScale()-0.2f);
    		}
    	break;
    	
    		
    	
    	}
    }
	
    public VR3DObjectRenderScene(VRActivity vrActivity) throws IOException {
		this.mActivity=vrActivity;
		TextureManager.getInstance().addTexture("skybox", new Texture(mActivity.getAssets().open(mActivity.skyboxPath)));
		
    }

	
	@Override
	public void onDrawEye(Eye eye) {
		
		if (frameBuffer == null) {
            frameBuffer = new FrameBuffer(eye.getViewport().width, eye.getViewport().height); // OpenGL ES 2.0 constructor
        }


        Matrix camBack = world.getCamera().getBack();
        camBack.setIdentity();

        tempTransform.setDump(eye.getEyeView());
        tempTransform.transformToGL();
        camBack.matMul(tempTransform);

        // TODO what todo with perspective
//        tempTransform.setDump(eye.getPerspective(Config.nearPlane, Config.farPlane));
//        tempTransform.transformToGL();
//        camBack.matMul(tempTransform);

       // object3D.rotateX(0.01f);
        world.getCamera().setBack(camBack);

        frameBuffer.clear(RGBColor.BLACK);
        //frameBuffer.clear();

        skyBox.render(world, frameBuffer);
        frameBuffer.clearZBufferOnly();

        world.renderScene(frameBuffer);
        world.draw(frameBuffer);


        frameBuffer.display();
		
	}

	@Override
	public void onFinishFrame(Viewport arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewFrame(HeadTransform headTransform) {
		
		 headTransform.getHeadView(mHeadView, 0);
		
	}

	@Override
	public void onRendererShutdown() {
		
		 if (frameBuffer != null) {
	            frameBuffer.dispose();
	        	frameBuffer = null;
	        }
			
		
	}

	private Object3D load3DObject(Object3D object3D,String model3D, String material3D) {
		//object3D = new Object3D(Primitives.getCube(1));

		//Log.d("VR3DSCENE",mActivity.model3DPath);
		//Log.d("VR3DSCENE",mActivity.material3DPath);
	    
		/*
		 * Creacion de los objetos
		 */
		try {
			if (model3D.toLowerCase().endsWith("obj")) {
				if (material3D.toLowerCase().endsWith("mtl")) {
					//object3D = Object3D.mergeAll(Loader.loadOBJ(mActivity.getModel3D(), mActivity.getMaterial3D(), 1f));
					object3D=Object3D.mergeAll(Util.loadOBJ(mActivity.getAssets().open(model3D),mActivity.getAssets().open(material3D), 1f));
				}
			} else if (model3D.toLowerCase().endsWith("md2")) {
				object3D = Object3D.mergeAll(Loader.loadMD2(mActivity.getAssets().open(model3D), 1f));
			} else if (model3D.toLowerCase().endsWith("3ds")) {
				object3D = Object3D.mergeAll(Util.load3DS(mActivity.getAssets().open(model3D), 1f));
			} else if (model3D.toLowerCase().endsWith("asc")) {
				object3D = Object3D.mergeAll(Loader.loadASC(mActivity.getAssets().open(model3D), 1f, false));
			}
			object3D.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return object3D;
	}
	@Override
	public void onSurfaceChanged(int arg0, int arg1) {
		
		 if (world != null)
	            return;
		 
		 world = new World();
		 //tengo que implementar el seteo de luces que hago en ai2
	     world.setAmbientLight(100, 100, 100);
	     skyBox = new SkyBox("skybox", "skybox", "skybox", "skybox", "skybox", "skybox", 10);
	     /*
			 * Las siguientes dos lineas son necesarias en AI2 para que este
			 * localice donde se encuentran los shaders
			 */

			ShaderLocator s = new ShaderLocator(this.mActivity.getAssets());
			GLSLShader.setShaderLocator(s);
			object3DArray= new Object3D[mActivity.object3DListAi2.size()];
			int i=0;
			Iterator<Object3DParcelable> itr = mActivity.object3DListAi2.iterator(); 
			while(itr.hasNext()) {
				Object3DParcelable myObject = itr.next(); 
				Log.v("MODEL3D_ENTRADO", myObject.getModel3DPath());
				object3DArray[i]=load3DObject(object3DArray[i],myObject.getModel3DPath(),myObject.getMaterial3DPath());
				Log.v("OBJETO INICIALIZADO", "INIT");
				object3DArray[i].translate(myObject.getPositionX(),myObject.getPositionY(),myObject.getPositionZ());
				Log.v("OBJETO POSICIONADO", "INIT");
				object3DArray[i].setScale(myObject.getScale());
				Log.v("OBJETO ESCALA", "INIT");
				object3DArray[i].setSortOffset(-10f);
			     //si no pongo estos dos valores asi el objeto 3d se muestra de forma incorrecta
				object3DArray[i].setTransparency(-1);
				object3DArray[i].setCulling(Object3D.CULLING_DISABLED);
				object3DArray[i].build();
			       
		        world.addObject(object3DArray[i]);
		        sun = new Light(world);
		        sun.setIntensity(250, 250, 250);

		        SimpleVector sunPosition = new SimpleVector();
		        sunPosition.set(object3DArray[i].getTransformedCenter());
		        sunPosition.y -= 50;
		        sunPosition.z -= 100;
		        sun.setPosition(sunPosition);
		        MemoryHelper.compact();
		        objectsUUIDMap.put(myObject.getId(), object3DArray[i]);
			i++;
		}
			focusObject=object3DArray[0];
	     //load3DObject();
	     //object3D.translate(mActivity.positionX,mActivity.positionY,mActivity.positionZ);
	     //object3D.setScale(mActivity.scale);
	     //object3D.setSortOffset(-10f);
	     //si no pongo estos dos valores asi el objeto 3d se muestra de forma incorrecta
	     //object3D.setTransparency(-1);
	     //object3D.setCulling(Object3D.CULLING_DISABLED);
	    
	        
	        
	     /*object3D.build();
	       
	        world.addObject(object3D);
	        sun = new Light(world);
	        sun.setIntensity(250, 250, 250);

	        SimpleVector sunPosition = new SimpleVector();
	        sunPosition.set(object3D.getTransformedCenter());
	        sunPosition.y -= 50;
	        sunPosition.z -= 100;
	        sun.setPosition(sunPosition);
	        MemoryHelper.compact();*/
		
	}

	@Override
	public void onSurfaceCreated(EGLConfig arg0) {
		// TODO Auto-generated method stub
		
	}

}
