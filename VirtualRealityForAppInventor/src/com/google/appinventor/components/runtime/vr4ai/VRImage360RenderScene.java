// 
// Decompiled by Procyon v0.5.30
// 

package com.google.appinventor.components.runtime.vr4ai;

import javax.microedition.khronos.egl.EGLConfig;
import com.threed.jpct.util.MemoryHelper;
import com.google.appinventor.components.runtime.vr4ai.util.Util;
import com.threed.jpct.GLSLShader;
import com.threed.jpct.ShaderLocator;
import android.util.Log;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.threed.jpct.RGBColor;
import com.google.vrtoolkit.cardboard.Eye;
import android.view.MotionEvent;
import java.io.IOException;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.World;
import com.threed.jpct.FrameBuffer;
import android.view.View;
import com.google.vrtoolkit.cardboard.CardboardView;

public class VRImage360RenderScene implements CardboardView.StereoRenderer, View.OnTouchListener
{
    private FrameBuffer frameBuffer;
    public VRActivity mActivity;
    private World world;
    private Object3D object3D;
    private final Matrix tempTransform;
    private float[] mHeadView;
    
    public VRImage360RenderScene(final VRActivity vrActivity) throws IOException {
        this.tempTransform = new Matrix();
        this.mHeadView = new float[16];
        this.mActivity = vrActivity;
        TextureManager.getInstance().addTexture("panorama", new Texture(this.mActivity.getAssets().open(this.mActivity.image360Path)));
    }
    
    public boolean onTouch(final View v, final MotionEvent event) {
        return false;
    }
    
    public void onDrawEye(final Eye eye) {
        if (this.frameBuffer == null) {
            this.frameBuffer = new FrameBuffer(eye.getViewport().height, eye.getViewport().height);
        }
        final Matrix camBack = this.world.getCamera().getBack();
        camBack.setIdentity();
        this.tempTransform.setDump(eye.getEyeView());
        this.tempTransform.transformToGL();
        camBack.matMul(this.tempTransform);
        this.world.getCamera().setBack(camBack);
        this.frameBuffer.clear(RGBColor.BLACK);
        this.world.renderScene(this.frameBuffer);
        this.world.draw(this.frameBuffer);
        this.frameBuffer.display();
    }
    
    public void onFinishFrame(final Viewport arg0) {
    }
    
    public void onNewFrame(final HeadTransform headTransform) {
        headTransform.getHeadView(this.mHeadView, 0);
    }
    
    public void onRendererShutdown() {
        Log.v("RENDEROFF", "");
        if (this.frameBuffer != null) {
            this.frameBuffer.dispose();
            this.frameBuffer = null;
        }
    }
    
    public void onSurfaceChanged(final int arg0, final int arg1) {
        if (this.world != null) {
            return;
        }
        (this.world = new World()).setAmbientLight(255, 255, 255);
        final ShaderLocator s = new ShaderLocator(this.mActivity.getAssets());
        GLSLShader.setShaderLocator(s);
        try {
            this.object3D = Object3D.mergeAll(new Object3D[] { Util.loadOBJ(this.mActivity.getApplicationContext().getAssets().open("sphere_paranomic.obj"), 1.0f) });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.object3D.setTexture("panorama");
        this.object3D.setTransparency(100);
        this.object3D.rotateZ(3.1415927f);
        this.object3D.rotateMesh();
        this.object3D.getRotationMatrix().setIdentity();
        this.object3D.build();
        this.world.addObject(this.object3D);
        MemoryHelper.compact();
    }
    
    public void onSurfaceCreated(final EGLConfig arg0) {
    }
}
