package com.google.appinventor.components.runtime.vr4ai;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.GLSLShader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.ShaderLocator;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.appinventor.components.runtime.vr4ai.util.Util;;



public class VRImage360RenderScene implements CardboardView.StereoRenderer,View.OnTouchListener  {

    private FrameBuffer frameBuffer;
    public VRActivity mActivity;
    private World world;
    private Object3D object3D;
    private final Matrix tempTransform = new Matrix();
    private float[] mHeadView = new float[16];

	public VRImage360RenderScene(VRActivity vrActivity) throws IOException {
		
		this.mActivity=vrActivity;
		TextureManager.getInstance().addTexture("panorama", new Texture(mActivity.getAssets().open(mActivity.image360Path)));

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDrawEye(Eye eye) {
		
		if (frameBuffer == null) {
            frameBuffer = new FrameBuffer(eye.getViewport().height, eye.getViewport().height); // OpenGL ES 2.0 constructor
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

        world.getCamera().setBack(camBack);

        frameBuffer.clear(RGBColor.BLACK);

        world.renderScene(frameBuffer);
        world.draw(frameBuffer);
//        world.drawWireframe(frameBuffer, RGBColor.WHITE, 1, false);

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
		Log.v("RENDEROFF", "");
		if (frameBuffer != null) {
            frameBuffer.dispose();
        	frameBuffer = null;
        }
		
	}

	@Override
	public void onSurfaceChanged(int arg0, int arg1) {
		
		 if (world != null)
	            return;


	        world = new World();
	        world.setAmbientLight(255, 255, 255);
	        ShaderLocator s = new ShaderLocator(this.mActivity.getAssets());
			GLSLShader.setShaderLocator(s);
	       // object3D = new Object3D(Primitives.getSphere(1));
	        try {
				object3D=Object3D.mergeAll(Util.loadOBJ(mActivity.getApplicationContext().getAssets().open("sphere_paranomic.obj"), 1f));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        object3D.setTexture("panorama");
	        object3D.setTransparency(100);
	        
	        // TODO for some reason (because sphere is inverted) sphere looks upside down, fix it
	        object3D.rotateZ((float) Math.PI);
	        object3D.rotateMesh();
	        object3D.getRotationMatrix().setIdentity();

	        object3D.build();
	        world.addObject(object3D);

	        MemoryHelper.compact();
		
	}

	@Override
	public void onSurfaceCreated(EGLConfig arg0) {
		// TODO Auto-generated method stub
		
	}

}
