/*===============================================================================
Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of QUALCOMM Incorporated, registered in the United States 
and other countries. Trademarks of QUALCOMM Incorporated are used with permission.
===============================================================================*/

/*Conversores de fomato
http://www.greentoken.de/onlineconv/
http://a3dplayer.com/
https://360.autodesk.com/ViewerPage?id=dXJuOmFkc2sub2JqZWN0czpvcy5vYmplY3Q6YTM2MHZpZXdlci90MTQ0MzMwMDg4NTg4Nl8wMzM2ODQ2NjM4Mzc3Nzg1Ny4zZHM%3D
 */

package com.google.appinventor.components.runtime.ar4ai.vuforia;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.google.appinventor.components.runtime.ar4ai.PhysicalObject;
import com.google.appinventor.components.runtime.ar4ai.VirtualObject;
import com.google.appinventor.components.runtime.ar4ai.common.VuforiaApplicationSession;
import com.google.appinventor.components.runtime.ar4ai.utils.CubeShaders;
import com.google.appinventor.components.runtime.ar4ai.utils.SampleMath;
import com.google.appinventor.components.runtime.ar4ai.utils.SampleUtils;
import com.google.appinventor.components.runtime.ar4ai.utils.Ticker;
import com.qualcomm.vuforia.CameraCalibration;
import com.qualcomm.vuforia.CameraDevice;
import com.qualcomm.vuforia.Matrix34F;
import com.qualcomm.vuforia.Matrix44F;
import com.qualcomm.vuforia.Renderer;
import com.qualcomm.vuforia.State;
import com.qualcomm.vuforia.Tool;
import com.qualcomm.vuforia.TrackableResult;
import com.qualcomm.vuforia.VIDEO_BACKGROUND_REFLECTION;
import com.qualcomm.vuforia.Vec2F;
import com.qualcomm.vuforia.Vuforia;
import com.qualcomm.vuforia.Word;
import com.threed.jpct.Camera;
import com.threed.jpct.Config;
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
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;

// The renderer class for the FrameMarkers sample. 
public class JpctRenderer implements GLSurfaceView.Renderer {

	////////////////////////////
	// Variables de contexto
	private Context mContext;
	VuforiaApplicationSession vuforiaAppSession;
	VuforiaARActivity mActivity;
	////////////////////////////

	public ExtendedWorld eworld = null;
	//public World world = null;
	//private float [] modelViewMat;
	//public Map<String, WorldInfo> world_info_list = new HashMap<String, WorldInfo>();

	private static final String LOGTAG = "ar4ai-VuforiaARActivity";
	public boolean mIsActive = false;

	// Variables de JPCT-Ae
	private Light sun;

	private FrameBuffer mFrameBuffer = null;
	public int shaderProgramID = 0;

	private float fov;

	private float fovy;

	float[] result;
	private boolean primeraVez;
	private float posXinit;
	private float posYinit;
	
	private float index = 0;
	
	private Ticker ticker = new Ticker(16);
	
	private ScaleGestureDetector scaleGestureDetector;
	private float currentScaleFactorApplied = 1.f;

	public JpctRenderer(VuforiaARActivity activity, VuforiaApplicationSession session, Context applicationContext) throws Exception {
		mActivity = activity;
		vuforiaAppSession = session;
		this.mContext = applicationContext;
		

		Config.viewportOffsetAffectsRenderTarget = true;
		
		//FIXME pruebas para el zoom
		scaleGestureDetector = new ScaleGestureDetector(mContext, new OnScaleGestureListener() {
		    
			@Override
		    public void onScaleEnd(ScaleGestureDetector detector) {}
		    
		    @Override
		    public boolean onScaleBegin(ScaleGestureDetector detector) { return true; }
		    
		    @Override
		    public boolean onScale(ScaleGestureDetector detector) {
		        Log.d(LOGTAG, "scale: " + detector.getScaleFactor());
		        System.out.println("scale:" + detector.getScaleFactor() + " - zoom");
		        
		        for (VirtualObject vo : mActivity.arrayOfVirtualObjects) {
		        	if (getWorldForVO(vo.getId()) != null && vo.getZoomActivated()) {
						Object3D object = getWorldForVO(vo.getId()).getObjectByName(vo.getId());
						currentScaleFactorApplied = Math.max(vo.getZoomMinSize(), Math.min(vo.getScale() * detector.getScaleFactor(), vo.getZoomMaxSize())); // Control the object size.
						vo.setScale(currentScaleFactorApplied);
						object.setScale(currentScaleFactorApplied);
						System.out.println("Size applied: " + vo.getScale());
						
					}
				}
		        return false;
		    }
		});

		createWorld();

	}

	// F01
	// Called when the surface is created or recreated.
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.d(LOGTAG, "onSurfaceCreated");

		// Shader
		ShaderLocator s = new ShaderLocator(this.mActivity.getAssets());
		GLSLShader.setShaderLocator(s);

		// Call function to initialize rendering:
		initRendering();

		// Call Vuforia function to (re)initialize rendering after first use
		// or after OpenGL ES context was lost (e.g. after onPause/onResume):
		vuforiaAppSession.onSurfaceCreated();
	}

	// F02
	// Called when the surface changed size.
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Log.d(LOGTAG, "onSurfaceChanged");
		////// jPCT-AE
		if (mFrameBuffer != null) {
			mFrameBuffer.dispose();
		}
		mFrameBuffer = new FrameBuffer(width, height);

		Config.viewportOffsetAffectsRenderTarget = true;

		// Call Vuforia function to handle render surface size changes:
		vuforiaAppSession.onSurfaceChanged(width, height);
		
		mActivity.calculateROI();

		updateRendering(width, height);
	}

	// F03
	// Aqui faltan un monton de opciones para poner la configuración de GL11
	// Called to draw the current frame.
	@Override
	public void onDrawFrame(GL10 gl) {
		if (!mIsActive)
			return;

		// IRR: this.mActivity.updateRenderView();
		
		//Actualiza las animaciones de los modelos
		updateAnimations();

		// Call our function to render content
		renderFrame();

		// Actualizo posiciones
		updateCameraWorld();

		mFrameBuffer.display();

	}
	
	private void updateAnimations() {
		for (VirtualObject vo : this.mActivity.arrayOfVirtualObjects) {
			if (vo.isAnimated()) {
				int ticks = ticker.getTicks();
				if (ticks > 0) {
					index += 0.016f * ticks;
					if (index > 1)
						index -= 1;
					if (getWorldForVO(vo.getId()) != null) {
						Object3D object = getWorldForVO(vo.getId()).getObjectByName(vo.getId());
						Log.d(LOGTAG, "Sacado el objeto "+object.getName()+ " con index "+index);
						object.animate(index, vo.getAnimationSecuence());
					}
				}
			}
		}
	}
	
	private void checkCollision(Object3D myobject, SimpleVector direction, float value) {
		int objectCollided = myobject.checkForCollision(direction, Math.abs(value));
		if (objectCollided != Object3D.NO_OBJECT) {
			this.mActivity.onCollision(myobject.getName(), getWorldForVO(myobject.getName()).getObject(objectCollided).getName());
		}
	}
	
	public void updateModelParameter(String uuid, String parameter, float value) {
		Log.d(LOGTAG, "uuid recibida: "+uuid);
		if (getWorldForVO(uuid) != null) {
			World world = getWorldForVO(uuid);
			if (world.getObjectByName(uuid) != null) {
				Object3D object = world.getObjectByName(uuid);
				if (object != null) {
					if (parameter.equals("PositionX")) {
						SimpleVector origin = object.getOrigin();
						origin.x = value;
						object.setOrigin(origin);
					}
					else if (parameter.equals("PositionY")) {
						SimpleVector origin = object.getOrigin();
						origin.y = value;
						object.setOrigin(origin);
					}
					else if (parameter.equals("PositionZ")) {
						SimpleVector origin = object.getOrigin();
						origin.x = value;
						object.setOrigin(origin);
					}
					else if (parameter.equals("RotationX"))
						object.rotateX((float) (value*Math.PI/180));
					else if (parameter.equals("RotationY"))
						object.rotateY((float) (value*Math.PI/180));
					else if (parameter.equals("RotationZ"))
						object.rotateZ((float) (value*Math.PI/180));
					else if (parameter.equals("TranslationX")) {
						checkCollision(object, new SimpleVector(value, 0, 0),value);
						object.translate(value, 0, 0);
					}
					else if (parameter.equals("TranslationY")) {
						checkCollision(object, new SimpleVector(0, value, 0),value);
						object.translate(0, value, 0);
					}
					else if (parameter.equals("TranslationZ")) {
						checkCollision(object, new SimpleVector(0, 0, value),value);
						object.translate(0, 0, value);
					}
					else if (parameter.equals("Scale"))
						object.scale(value);
				}
			}
		}
	}

	private void updateCameraWorld() {
		/*float [] m = this.vuforiaAppSession.getProjectionMatrix().getData();
		int i=0;
		for (float value : m) {
			Log.i(LOGTAG, "Valor de m["+i+"]: "+value);
			i++;
		}*/
		
		
		for (PhysicalObject po : this.mActivity.getArrayOfPhysicalObject()) {

			WorldInfo world_info = eworld.getInfo(po.getId());

			if (world_info != null && world_info.isVisible()) {

				if (world_info.getMat() == null) {
					Log.d(LOGTAG, "matriz sin definir");
					continue;
				}

				Matrix m2 = new Matrix();
				m2.setTo(world_info.getMat());

				// --- INICIO - Cambio respecto a original
				float[] m = m2.getDump();
				final SimpleVector camUp;
				
				/*int i=0;
				for (float value : m) {
					Log.i(LOGTAG, "Matriz de "+ vo.getId()+"["+i+"]: "+value);
					i++;
				}*/

				// Modificao por mi
				if (mActivity.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
					camUp = new SimpleVector(-m[0], -m[1], -m[2]);
				} else {
					camUp = new SimpleVector(-m[4], -m[5], -m[6]);
				}

				final SimpleVector camDirection = new SimpleVector(m[8], m[9], m[10]);
				final SimpleVector camPosition = new SimpleVector(m[12], m[13], m[14]);
				// --- FIN - Cambio respecto a original

				World world = this.eworld.getWorld(po.getId());
				if (world != null) {
					Camera cam = world.getCamera();
					cam.setOrientation(camDirection, camUp);
					cam.setPosition(camPosition);
					cam.setFOV(fov);
					cam.setYFOV(fovy);
					
					world.renderScene(mFrameBuffer);
					world.draw(mFrameBuffer);
	
					Log.d(LOGTAG, "------->¡HOLA2Renderizando el Mundo de " + po.getId());
				}
			}
		}
	}

	void initRendering() {
		Log.d(LOGTAG, "initRendering");

		// Define clear color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, Vuforia.requiresAlpha() ? 0.0f : 1.0f);

		shaderProgramID = SampleUtils.createProgramFromShaderSrc(CubeShaders.CUBE_MESH_VERTEX_SHADER, CubeShaders.CUBE_MESH_FRAGMENT_SHADER);

	}

	public void renderFrame() {
		// Clear color and depth buffer
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		// Get the state from Vuforia and mark the beginning of a rendering
		// section
		State state = Renderer.getInstance().begin();

		// Explicitly render the Video Background
		Renderer.getInstance().drawVideoBackground();

		// GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glCullFace(GLES20.GL_BACK);

		// IRR2
		if (Renderer.getInstance().getVideoBackgroundConfig().getReflection() == VIDEO_BACKGROUND_REFLECTION.VIDEO_BACKGROUND_REFLECTION_ON)
			GLES20.glFrontFace(GLES20.GL_CW); // Front camera
		else
			GLES20.glFrontFace(GLES20.GL_CCW); // Back camera

		Log.d(LOGTAG, "Se han detectado un Numero de trakers igual a " + Integer.toString(state.getNumTrackableResults()));

		// Get the trackable:
		for (int tIdx = 0; tIdx < state.getNumTrackableResults(); tIdx++) {
			Log.d(LOGTAG, "Trabajando marcador: " + Integer.toString(tIdx) + " de " + Integer.toString(state.getNumTrackableResults()));

			// Get the trackable:
			TrackableResult trackableResult = state.getTrackableResult(tIdx);
			PhysicalObject po;
			if (trackableResult.getTrackable().isOfType(Word.getClassType())) {
				Word word = (Word) trackableResult.getTrackable();
				po = mActivity.getPoforWord(word.getStringU());
			}
			else
				po = (PhysicalObject) trackableResult.getTrackable().getUserData();
			if (po != null) {
				Matrix34F matrix34F = trackableResult.getPose();
				// Va mejor sin esto debido a que la luz está mejor. Además
				// pierdo
				// la rotación
				// Tool.setRotation(matrix34F, new Vec3F(1.0f, 0.0f, .0f),
				// 90.0f);

				Matrix44F modelVieMatrix = Tool.convertPose2GLMatrix(matrix34F);
				Matrix44F inverseMV = SampleMath.Matrix44FInverse(modelVieMatrix);
				Matrix44F invTranspMV = SampleMath.Matrix44FTranspose(inverseMV);
				//Captura de nulo para evitar crash durante el refresco de la camara.
				if (eworld.getInfo(po.getId()) != null)
					eworld.getInfo(po.getId()).setMat(invTranspMV.getData());
			} else {
				Log.d(LOGTAG, "No se encuentra el PO asociado al tracker");

			}

		}
		// GLES20.glDisable(GLES20.GL_CULL_FACE);
		//GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		Renderer.getInstance().end();

	}
	
	private InputStream getAssetPath(String path) throws IOException {
		if (mActivity.isAiCompanionActive()) {
	    	Log.d(LOGTAG, "Trabajando con aiCompanion");
	    	return new FileInputStream("/sdcard/AppInventor/assets/"+path);
		}
		else {
			Log.d(LOGTAG, "Aplicacion arrancando desde la apk");
			return mContext.getAssets().open(path);
		}
	}
	
	private World getWorldForVO(String myVO) {
		for (PhysicalObject po : this.mActivity.getArrayOfPhysicalObject()) {
			for (VirtualObject vo : po.getVirtualObject()) {
				if (myVO.equals(vo.getId())) {
					return eworld.getWorld(po.getId());
				}
			}
		}
		return null;
	}

	public void createWorld() throws Exception {
		Config.farPlane = 20000;
		Config.nearPlane = 20;
		Config.glTrilinear = true;

		eworld = new ExtendedWorld();
		World world = null;
		for (PhysicalObject po : this.mActivity.getArrayOfPhysicalObject()) {
			world = new World();
			world.setAmbientLight(100, 100, 100);
			eworld.putWorld(po.getId(), world);
			eworld.putInfo(po.getId(), new WorldInfo());
		}

		Log.d(LOGTAG, "Eliminando texturas previas");
		TextureManager textureManager = TextureManager.getInstance();

		for (Object s : textureManager.getNames().toArray()) {
			textureManager.removeTexture((String) s);

		}

		try {
			Log.d(LOGTAG, "Cargando Modelos y Texturas");

			Object3D myObject3D = null;
			for (VirtualObject myVO : this.mActivity.getArrayOfVirtualObjects()) {
				world = getWorldForVO(myVO.getId());
				if (myVO.isEnabled() && world != null) {
					if (myVO.getVisualAssetType() == VirtualObject.ASSET_TEXT) {
						myObject3D = createAssetText(myVO);
					} else if (myVO.getVisualAssetType() == VirtualObject.ASSET_IMAGE) {
						myObject3D = createAssetImage(myVO);
					} else if (myVO.getVisualAssetType() == VirtualObject.ASSET_3DMODEL) {
						myObject3D = createAsset3DModel(myVO);
					}

					if (myObject3D != null) {
					
						sun = new Light(world);
						sun.setIntensity(250, 250, 250);

						SimpleVector sv = new SimpleVector();
						sv.set(myObject3D.getTransformedCenter());
						sv.y += 100;
						sv.z += 100;
						//sv.x -= 500;
						sun.setPosition(sv);

						myObject3D.setLighting(Object3D.LIGHTING_ALL_ENABLED);
						myObject3D.strip();
						myObject3D.build();
						world.addObject(myObject3D);

						SimpleVector elcentro = myObject3D.getCenter();
						elcentro.scalarMul(-1);
						myObject3D.translate(elcentro);
						myObject3D.translateMesh();
						myObject3D.clearTranslation();
						myObject3D.setVisibility(true);						
						myObject3D.setUserObject(myVO);
						myObject3D.rotateY((float) (Math.PI));
						myObject3D.rotateZ((float) (Math.PI));
						myObject3D.setCollisionMode(Object3D.COLLISION_CHECK_SELF | Object3D.COLLISION_CHECK_OTHERS);
						myObject3D.build();
						Log.d(LOGTAG, "Centrando modelos");
						
						myObject3D.scale(myVO.getScale());
						
						Log.d(LOGTAG, "Escalando modelos");

						//
						// compile object?

						// Set position
						myObject3D.setOrigin(new SimpleVector(myVO.getPositionX(),
						myVO.getPositionY(), myVO.getPositionZ()));
						// myObject3D.translate(0,0,0);
						
						Log.d(LOGTAG, "Posicionando modelos");

						// Set rotation
						myObject3D.rotateX((float) (myVO.getRotationX()*Math.PI/180));
						myObject3D.rotateY((float) (myVO.getRotationY()*Math.PI/180));
						myObject3D.rotateZ((float) (myVO.getRotationZ()*Math.PI/180));
						
						Log.d(LOGTAG, "Rotando modelos");

						// Set translation
						myObject3D.translate(myVO.getTranslationX(),
						myVO.getTranslationY(), myVO.getTranslationZ());
						
						Log.d(LOGTAG, "Moviendo modelos");

						// Ahora los añados a sus mundos
						
					}

				}

			}

		} catch (IOException e) {
			Log.d(LOGTAG, "Error Carga Modelos");
			throw e;
		}

		// Finalizando
		MemoryHelper.compact();
		Log.d(LOGTAG, "Finalizado Creación de Mundo");
	}

	private Object3D createAssetText(VirtualObject myVO) {
		// VIRTUAL OBJECT: TEXTO
		Log.d(LOGTAG, "Creating in the world the asset Text" + myVO.getId());
		// myObject3D.setName(myVO.getId());

		// TODO Auto-generated method stub
		return null;
	}

	private Object3D createAsset3DModel(VirtualObject myVO) throws IOException {
		Object3D myObject3D = null;
		// VIRTUAL OBJECT: 3d Model
		if (myVO.getOverlaid3DModel().toLowerCase().endsWith("md2")) {
			myObject3D = Loader.loadMD2(getAssetPath(myVO.getOverlaid3DModel()), 1f);
		    Log.d(LOGTAG, "Creating in the world the asset MD2 Model3d" + myVO.getId());
		} else if (myVO.getOverlaid3DModel().toLowerCase().endsWith("obj")) {
			try {
				if (myVO.getMaterial() != null && myVO.getMaterial().toLowerCase().endsWith("mtl")) {
					Config.useNormalsFromOBJ = true;
					//myObject3D = loadModel(mContext.getAssets().open(myVO.getOverlaid3DModel()), mContext.getAssets().open(myVO.getMaterial()), 1f);
					myObject3D = Object3D.mergeAll(Loader.loadOBJ(getAssetPath(myVO.getOverlaid3DModel()), getAssetPath(myVO.getMaterial()), 1f));
					Log.d(LOGTAG, "Creating in the world the asset OBJ Model3d with materials" + myVO.getId());
				} else {
					Config.useNormalsFromOBJ = true;
					//myObject3D = loadModel(mContext.getAssets().open(myVO.getOverlaid3DModel()), null, 1f);
					myObject3D = Object3D.mergeAll(Loader.loadOBJ(getAssetPath(myVO.getOverlaid3DModel()), null, 1f));
					Log.d(LOGTAG, "Creating in the world the asset OBJ Model3d without materials" + myVO.getId());
				}
			} catch(ArrayIndexOutOfBoundsException e) { //Config.useNormalsFromOBJ error appears.
				e.printStackTrace();
			}
			if(myObject3D == null) { //Retry without Config.useNormalsFromOBJ option active.
				if (myVO.getMaterial() != null && myVO.getMaterial().toLowerCase().endsWith("mtl")) {
					Config.useNormalsFromOBJ = false;
					//myObject3D = loadModel(mContext.getAssets().open(myVO.getOverlaid3DModel()), mContext.getAssets().open(myVO.getMaterial()), 1f);
					myObject3D = Object3D.mergeAll(Loader.loadOBJ(getAssetPath(myVO.getOverlaid3DModel()), getAssetPath(myVO.getMaterial()), 1f));
					Log.d(LOGTAG, "Creating in the world the asset OBJ Model3d with materials (without useNormalsFromOBJ)" + myVO.getId());
				} else {
					Config.useNormalsFromOBJ = false;
					//myObject3D = loadModel(mContext.getAssets().open(myVO.getOverlaid3DModel()), null, 1f);
					myObject3D = Object3D.mergeAll(Loader.loadOBJ(getAssetPath(myVO.getOverlaid3DModel()), null, 1f));
					Log.d(LOGTAG, "Creating in the world the asset OBJ Model3d without materials (without useNormalsFromOBJ)" + myVO.getId());
				}
			}
		} else if (myVO.getOverlaid3DModel().toLowerCase().endsWith("3ds")) {
			myObject3D = Object3D.mergeAll(Loader.load3DS(getAssetPath(myVO.getOverlaid3DModel()), 0.2f));
			Log.d(LOGTAG, "Creating in the world the asset 3DS Model3d" + myVO.getId());

		} else if (myVO.getOverlaid3DModel().toLowerCase().endsWith("asc")) {
			myObject3D = Loader.loadASC(getAssetPath(myVO.getOverlaid3DModel()), 1f, false);
			Log.d(LOGTAG, "Creating in the world the asset ASC Model3d" + myVO.getId());
		}
		
		myObject3D.setName(myVO.getId());
		myObject3D.rotateY((float)(Math.PI/2));
		
		Texture myTexture;
		if (myVO.getImageTexture() != null && !myVO.getImageTexture().trim().equals("")) {
			// loading texture with Image
			//FIXME problema al cargar imágenes grandes parece.
			myTexture = new com.threed.jpct.Texture(getAssetPath(myVO.getImageTexture()));
			try {
				TextureManager.getInstance().addTexture(myVO.getImageTexture(), myTexture);
			} catch (RuntimeException e) {
				Log.w(LOGTAG, "Textura ya incluida. No se volverá a incluir");
			}
			//myObject3D.calcTextureWrap();
			myObject3D.setTexture(myVO.getImageTexture());
		} else if (myVO.getColorTexture() != Color.TRANSPARENT) {
			// loading texture with color
			RGBColor myColor = new com.threed.jpct.RGBColor(Color.red(myVO.getColorTexture()), Color.green(myVO.getColorTexture()), Color.blue(myVO.getColorTexture()));
			myTexture = new com.threed.jpct.Texture(150, 150, myColor);
			try {
				TextureManager.getInstance().addTexture("ColorForVO" + myVO.getId(), myTexture);
			} catch (RuntimeException e) {
				Log.w(LOGTAG, "Textura ya incluida. No se volverá a incluir");
			}
			//myObject3D.calcTextureWrap();
			myObject3D.setTexture("ColorForVO" + myVO.getId());
		}

		return myObject3D;
	}

	private Object3D createAssetImage(VirtualObject myVO) throws Exception {
		// VIRTUAL OBJECT: IMAGE
		Log.d(LOGTAG, "Creating in the world the asset Image" + myVO.getId());
		Object3D myObject3D = new Object3D(Primitives.getPlane(1, 70));
		//myObject3D.setEnvmapped(Object3D.ENVMAP_ENABLED);
		//myObject3D.rotateY((float) (Math.PI));
		//myObject3D.rotateZ((float) (Math.PI));
		// myObject3D.translate(0, 0, 0.5f);
		myObject3D.setName(myVO.getId());

		// loading texture with the image to render
		Texture myTexture = new com.threed.jpct.Texture(BitmapHelper.rescale(BitmapHelper.loadImage(getAssetPath(myVO.getOverlaidImage())), 128, 128), true);
		try {
			TextureManager.getInstance().addTexture(myVO.getOverlaidImage(), myTexture);
		} catch (RuntimeException e) {
			Log.w(LOGTAG, "Textura ya incluida. No se volverá a incluir");
		}
		//myObject3D.calcTextureWrap();
		myObject3D.setTransparency(100);
		myObject3D.setTexture(myVO.getOverlaidImage());

		return myObject3D;
	}
	
	private Object3D loadModel(InputStream obj, InputStream material, float scale) {
        Loader.setVertexOptimization(false);
        Object3D[] model = Loader.loadOBJ(obj, material, scale);

        Object3D o3d = new Object3D(0);

        Object3D temp = null;

        for (int i = 0; i < model.length; i++) {
            temp = model[i];
            temp.setCenter(SimpleVector.ORIGIN);
            temp.rotateX((float)( -.5*Math.PI));
            temp.rotateMesh();
            temp.setRotationMatrix(new Matrix());
            o3d = Object3D.mergeObjects(o3d, temp);
            o3d.build();
        }
        return o3d;
    }

	private void printMatrix(float[] matrix, String name) {
		String cadena;
		Log.d(LOGTAG, "MATRIX " + name);
		for (int i = 0; i < 4; i++) {
			cadena = "";
			for (int j = 0; j < 4; j++) {
				cadena = cadena + " " + matrix[i + j] + " ";
			}
			Log.d(LOGTAG, "MATRIX  fila " + Integer.toString(i) + " >> " + cadena);
		}

	}

	private void updateRendering(int width, int height) {

		// // ULTIMO: Update screen dimensions
		// mActivity.getVuforiaAppSession().mScreenWidth = width;
		// mActivity.getVuforiaAppSession().mScreenHeight = height;
		// mActivity.getVuforiaAppSession().configureVideoBackground();

		// Setting up the FOV
		CameraCalibration cc = CameraDevice.getInstance().getCameraCalibration();
		Vec2F size = cc.getSize();
		Vec2F focalLength = cc.getFocalLength();
		float fovyRadians = (float) (2 * Math.atan(0.5f * size.getData()[1] / focalLength.getData()[1]));
		float fovRadians = (float) (2 * Math.atan(0.5f * size.getData()[0] / focalLength.getData()[0]));
		//
		// if (mActivity.getCamera().getScreenOrientation() ==
		// ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
		// this.fov = fovyRadians*2;
		// this.fovy = fovRadians*2;
		//
		// } else {
		// this.fov = fovRadians*2;
		// this.fovy = fovyRadians*2;
		//
		// }

		this.fov = fovRadians * 2;
		this.fovy = fovyRadians * 2;

	}

	public void onTouch(MotionEvent e) {
		// MotionEvent reports input details from the touch screen
		// and other input controls. In this case, you are only
		// interested in events where the touch position changed.
		
		//Applying zoom.
		scaleGestureDetector.onTouchEvent(e);
		
		Log.d(LOGTAG, "TOCATA 2");
		int action = e.getAction();

		Log.d(LOGTAG, "TOCATA 3 " + action);
		if (action == MotionEvent.ACTION_MOVE) {
			if (primeraVez) {
				posXinit = e.getX();
				posYinit = e.getY();
				primeraVez = false;
			} else {
				moverMatrix(e.getX() - posXinit, e.getY() - posYinit);
				posXinit = e.getX();
				posYinit = e.getY();
			}
		} else {
			primeraVez = true;
		}
	}

	private void moverMatrix(float movX, float movY) {

		for (PhysicalObject po : this.mActivity.getArrayOfPhysicalObject()) {

			WorldInfo world_info = eworld.getInfo(po.getId());

			if (world_info != null && world_info.isVisible()) {

				if (world_info.getMat() == null) {
					Log.d(LOGTAG, "matriz sin definir");
					continue;
				}

				// Esto esta mal pero al menos para probar.
				// Hay que hacerlo con todos los mundos pues nos sabemos
				// a cual se está refiriendo
				
				World world = eworld.getWorld(po.getId());
				
				for (VirtualObject vo : po.getVirtualObject()) {
					if (vo.getVisualAssetType() == VirtualObject.ASSET_3DMODEL && vo.getRotateActivated()) {
						Log.d(LOGTAG, "TOCATA ROTACION  " + movX + " y " + movY);
						Object3D objeto = world.getObjectByName(vo.getId());
						objeto.rotateX(-movY/100);
						objeto.rotateY(movX/100);
						
					}
				}
			}
		}

	}

}
