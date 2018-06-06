// 
// Decompiled by Procyon v0.5.30
// 

package com.google.appinventor.components.runtime.vr4ai;

import javax.microedition.khronos.egl.EGLConfig;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.util.MemoryHelper;
import android.util.Log;
import com.google.appinventor.components.runtime.vr4ai.util.Object3DParcelable;
import com.threed.jpct.GLSLShader;
import com.threed.jpct.ShaderLocator;
import com.threed.jpct.Loader;
import com.google.appinventor.components.runtime.vr4ai.util.Util;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.threed.jpct.RGBColor;
import com.google.vrtoolkit.cardboard.Eye;
import java.io.IOException;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.threed.jpct.Light;
import com.threed.jpct.Matrix;
import com.threed.jpct.util.SkyBox;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.World;
import com.threed.jpct.Object3D;
import com.google.vrtoolkit.cardboard.CardboardView;

public class VR3DObjectRenderScene implements CardboardView.StereoRenderer
{
    private VRActivity mActivity;
    Object3D object3D;
    private World world;
    private FrameBuffer frameBuffer;
    private SkyBox skyBox;
    private final Matrix tempTransform;
    private float[] mHeadView;
    private Light sun;
    private String activeUUID;
    Object3D[] object3DArray;
    Object3D focusObject;
    Map<String, Object3D> objectsUUIDMap;
    Map<String, Float> objectsMoveSpeedMap;
    Map<String, Float> objectsRotateSpeedMap;
    Map<String, Integer> objectsPositionXMap;
    Map<String, Integer> objectsPositionYMap;
    Map<String, Integer> objectsPositionZMap;
    Map<String, Integer> objectsScaleMap;
    
    public void setFocusObject(final String UUID) {
        if (!UUID.equals("genericAction")) {
            this.focusObject = this.objectsUUIDMap.get(UUID);
            this.activeUUID = UUID;
            this.setLightToFocusObject();
        }
        else {
            this.activeUUID = this.getFocusObjectUUID();
        }
    }
    
    public String getFocusObjectUUID() {
        String UUID = null;
        for (final Map.Entry<String, Object3D> entry : this.objectsUUIDMap.entrySet()) {
            final Object3D myObject = entry.getValue();
            final String myUUID = entry.getKey();
            if (myObject == this.focusObject) {
                UUID = myUUID;
            }
        }
        return UUID;
    }
    
    public void setLightToFocusObject() {
        for (int i = 0; i < this.object3DArray.length; ++i) {
            this.object3DArray[i].setLighting(1);
        }
        this.focusObject.setLighting(0);
    }
    
    public void moveFocusNextObject() {
        int position = 0;
        for (int i = 0; i < this.object3DArray.length; ++i) {
            if (this.object3DArray[i] == this.focusObject) {
                position = i;
            }
        }
        if (++position > this.object3DArray.length - 1) {
            this.focusObject = this.object3DArray[0];
        }
        else {
            this.focusObject = this.object3DArray[position];
        }
        this.setLightToFocusObject();
    }
    
    public void moveFocusPreviousObject() {
        int position = 0;
        for (int i = 0; i < this.object3DArray.length; ++i) {
            if (this.object3DArray[i] == this.focusObject) {
                position = i;
            }
        }
        if (--position < 0) {
            this.focusObject = this.object3DArray[this.object3DArray.length - 1];
        }
        else {
            this.focusObject = this.object3DArray[position];
        }
        this.setLightToFocusObject();
    }
    
    public void reset3DPosition(final String UUID) {
        this.setFocusObject(UUID);
        this.focusObject.clearTranslation();
        this.focusObject.clearRotation();
        this.focusObject.translate((float)this.objectsPositionXMap.get(this.activeUUID), (float)this.objectsPositionYMap.get(this.activeUUID), (float)this.objectsPositionZMap.get(this.activeUUID));
        this.focusObject.scale((float)this.objectsScaleMap.get(this.activeUUID));
    }
    
    public void move3DObject(final String direction, final String UUID) {
        this.setFocusObject(UUID);
        final float posX = this.focusObject.getTranslation().x;
        final float posY = this.focusObject.getTranslation().y;
        final float posZ = this.focusObject.getTranslation().z;
        final float moveSpeed = this.objectsMoveSpeedMap.get(this.activeUUID);
        this.focusObject.clearTranslation();
        switch (direction) {
            case "zoomIn": {
                this.focusObject.translate(posX, posY, posZ - moveSpeed);
                break;
            }
            case "zoomOut": {
                this.focusObject.translate(posX, posY, posZ + moveSpeed);
                break;
            }
            case "up": {
                this.focusObject.translate(posX, posY - moveSpeed, posZ);
                break;
            }
            case "down": {
                this.focusObject.translate(posX, posY + moveSpeed, posZ);
                break;
            }
            case "left": {
                this.focusObject.translate(posX + moveSpeed, posY, posZ);
                break;
            }
            case "right": {
                this.focusObject.translate(posX - moveSpeed, posY, posZ);
                break;
            }
            default:
                break;
        }
    }
    
    public void rotate3DObject(final String direction, final String uUID) {
        this.setFocusObject(uUID);
        final float rotateSpeed = this.objectsRotateSpeedMap.get(this.activeUUID);
        switch (direction) {
            case "up": {
                this.focusObject.rotateX(rotateSpeed);
                break;
            }
            case "down": {
                this.focusObject.rotateX(-rotateSpeed);
                break;
            }
            case "left": {
                this.focusObject.rotateY(rotateSpeed);
                break;
            }
            case "right": {
                this.focusObject.rotateY(-rotateSpeed);
                break;
            }
            default:
                break;
        }
    }
    
    public void scale3DObject(final String mode, final String uUID) {
        this.setFocusObject(uUID);
        switch (mode) {
            case "increase": {
                this.focusObject.setScale(this.focusObject.getScale() + 0.2f);
                break;
            }
            case "decrease": {
                if (this.focusObject.getScale() - 0.2f > 0.0f) {
                    this.focusObject.setScale(this.focusObject.getScale() - 0.2f);
                    break;
                }
                break;
            }
            default:
                break;
        }
    }
    
    public VR3DObjectRenderScene(final VRActivity vrActivity) throws IOException {
        this.tempTransform = new Matrix();
        this.mHeadView = new float[16];
        this.activeUUID = null;
        this.object3DArray = null;
        this.focusObject = null;
        this.objectsUUIDMap = new HashMap<String, Object3D>();
        this.objectsMoveSpeedMap = new HashMap<String, Float>();
        this.objectsRotateSpeedMap = new HashMap<String, Float>();
        this.objectsPositionXMap = new HashMap<String, Integer>();
        this.objectsPositionYMap = new HashMap<String, Integer>();
        this.objectsPositionZMap = new HashMap<String, Integer>();
        this.objectsScaleMap = new HashMap<String, Integer>();
        this.mActivity = vrActivity;
        TextureManager.getInstance().addTexture("skybox", new Texture(this.mActivity.openAsset(this.mActivity.skyboxPath)));
    }
    
    public void onDrawEye(final Eye eye) {
        if (this.frameBuffer == null) {
            this.frameBuffer = new FrameBuffer(eye.getViewport().width, eye.getViewport().height);
        }
        final Matrix camBack = this.world.getCamera().getBack();
        camBack.setIdentity();
        this.tempTransform.setDump(eye.getEyeView());
        this.tempTransform.transformToGL();
        camBack.matMul(this.tempTransform);
        this.world.getCamera().setBack(camBack);
        this.frameBuffer.clear(RGBColor.BLACK);
        this.skyBox.render(this.world, this.frameBuffer);
        this.frameBuffer.clearZBufferOnly();
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
        if (this.frameBuffer != null) {
            this.frameBuffer.dispose();
            this.frameBuffer = null;
        }
    }
    
    private Object3D load3DObject(Object3D object3D, final String model3D, final String material3D) {
        try {
            if (model3D.toLowerCase().endsWith("obj")) {
                if (material3D.toLowerCase().endsWith("mtl")) {
                    object3D = Util.loadOBJ(this.mActivity.openAsset(model3D), this.mActivity.openAsset(material3D), 1.0f);
                }
            }
            else if (model3D.toLowerCase().endsWith("md2")) {
                object3D = Object3D.mergeAll(new Object3D[] { Loader.loadMD2(this.mActivity.openAsset(model3D), 1.0f) });
            }
            else if (model3D.toLowerCase().endsWith("3ds")) {
                object3D =  Util.load3DS(this.mActivity.openAsset(model3D), 1.0f);
            }
            else if (model3D.toLowerCase().endsWith("asc")) {
                object3D = Object3D.mergeAll(new Object3D[] { Loader.loadASC(this.mActivity.openAsset(model3D), 1.0f, false) });
            }
            object3D.setCollisionMode(1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return object3D;
    }
    
    public void onSurfaceChanged(final int arg0, final int arg1) {
        if (this.world != null) {
            return;
        }
        (this.world = new World()).setAmbientLight(100, 100, 100);
        this.skyBox = new SkyBox("skybox", "skybox", "skybox", "skybox", "skybox", "skybox", 10.0f);
        final ShaderLocator s = new ShaderLocator(this.mActivity.getAssets());
        GLSLShader.setShaderLocator(s);
        this.object3DArray = new Object3D[this.mActivity.object3DListAi2.size()];
        int i = 0;
        for (final Object3DParcelable myObject : this.mActivity.object3DListAi2) {
            Log.v("MODEL3D_ENTRADO", myObject.getModel3DPath());
            this.object3DArray[i] = this.load3DObject(this.object3DArray[i], myObject.getModel3DPath(), myObject.getMaterial3DPath());
            Log.v("OBJETO INICIALIZADO", "INIT");
            this.object3DArray[i].translate((float)myObject.getPositionX(), (float)myObject.getPositionY(), (float)myObject.getPositionZ());
            Log.v("OBJETO POSICIONADO", "INIT");
            this.object3DArray[i].setScale((float)myObject.getScale());
            Log.v("OBJETO ESCALA", "INIT");
            this.object3DArray[i].setSortOffset(-10.0f);
            this.object3DArray[i].setTransparency(-1);
            this.object3DArray[i].setCulling(false);
            this.object3DArray[i].build();
            this.world.addObject(this.object3DArray[i]);
            MemoryHelper.compact();
            this.objectsUUIDMap.put(myObject.getId(), this.object3DArray[i]);
            this.objectsMoveSpeedMap.put(myObject.getId(), myObject.getMoveSpeed());
            this.objectsRotateSpeedMap.put(myObject.getId(), myObject.getRotateSpeed());
            this.objectsPositionXMap.put(myObject.getId(), myObject.getPositionX());
            this.objectsPositionYMap.put(myObject.getId(), myObject.getPositionY());
            this.objectsPositionZMap.put(myObject.getId(), myObject.getPositionZ());
            this.objectsScaleMap.put(myObject.getId(), myObject.getScale());
            ++i;
        }
        this.focusObject = this.object3DArray[0];
        (this.sun = new Light(this.world)).setIntensity(250.0f, 250.0f, 250.0f);
        final SimpleVector sunPosition = new SimpleVector();
        sunPosition.set(this.focusObject.getTransformedCenter());
        final SimpleVector simpleVector = sunPosition;
        simpleVector.y -= 50.0f;
        final SimpleVector simpleVector2 = sunPosition;
        simpleVector2.z -= 100.0f;
        this.sun.setPosition(sunPosition);
        this.setLightToFocusObject();
    }
    
    public void onSurfaceCreated(final EGLConfig arg0) {
    }
}
