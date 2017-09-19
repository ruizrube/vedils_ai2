package com.google.appinventor.components.runtime.vr4ai;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import com.google.appinventor.components.runtime.vr4ai.util.TextureRenderer;
import com.google.appinventor.components.runtime.vr4ai.util.Util;
import com.google.appinventor.components.runtime.vr4ai.util.YouTubeExtractor;
import com.google.appinventor.components.runtime.vr4ai.util.YouTubeExtractor.YouTubeExtractorResult;
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
import com.threed.jpct.RGBColor;
import com.threed.jpct.ShaderLocator;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;
import com.threed.jpct.util.SkyBox;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.webkit.URLUtil;;;

public class VRVideo360RenderScene
		implements CardboardView.StereoRenderer, YouTubeExtractor.YouTubeExtractorListener {

	public MediaPlayer mediaPlayer;
	public VRActivity mActivity;
	private FrameBuffer frameBuffer;
	private World world;
	private World waitWorld;
	private Object3D object3D;
	private final Matrix tempTransform = new Matrix();
	private float[] mHeadView = new float[16];
	private SurfaceTexture surfaceTexture;
	private TextureRenderer textureRenderer = new TextureRenderer();
	private Texture externalTexture;
	private boolean frameAvailable = false;
	private String url;
	//private String url = "https://ia801704.us.archive.org/28/items/Movie_678/Sed.105.mp4";
	// private String url="pepe";
	private YouTubeExtractor youTubeExtractor;
	public String urlToPlay = "";
	public String videoId;
	public boolean conversionOk = false;
	private YouTubeExtractorResult result;
	Surface surface;
	private boolean mediaPlayerIsInit;
	private List<Integer> preferredVideoQualities;
	private boolean mediaPlayerIsPrepared;
	int media_length = -1;
	public boolean mediaPlayerResume = false;
	private Light sun;
	private boolean renderWaitWorld = true;
	AlertDialog alert11;
	private SkyBox skyBox;

	/**
	 * cuando se carga al rotar o cualquier movimiento se queda el estado
	 * inicial estatico, falla
	 */
	public void loadWaitScene() {
		waitWorld = new World();
		waitWorld.setAmbientLight(200, 200, 200);
		 /*
		 * Las siguientes dos lineas son necesarias en AI2 para que este
		 * localice donde se encuentran los shaders
		 */

		skyBox = new SkyBox("skybox", "skybox", "skybox", "skybox", "skybox", "skybox", 10);
		ShaderLocator s = new ShaderLocator(this.mActivity.getAssets());
		GLSLShader.setShaderLocator(s);
		/*try {
			//object3D = Object3D.mergeAll(Util.loadOBJ(mActivity.getApplicationContext().getAssets().open("LEGO_Man.obj"), 1f));
			object3D = Object3D.mergeAll(Loader.loadOBJ(mActivity.getAssets().open("LEGO_Man.obj"), mActivity.getAssets().open("LEGO_Man.mtl"), 1f));
		} catch (IOException e) {
			e.printStackTrace();
		}
		object3D.translate(0, 0, -20);
		object3D.setTransparency(100);
		object3D.build();
		
		waitWorld.addObject(object3D);
		sun = new Light(waitWorld);
		sun.setIntensity(250, 250, 250);

		SimpleVector sunPosition = new SimpleVector();
		sunPosition.set(object3D.getTransformedCenter());
		sunPosition.y -= 50;
		sunPosition.z -= 100;
		sun.setPosition(sunPosition);*/
	}

	public void load360Scene() {
		
		Log.v("CARGAESCENA", "");
		frameBuffer.flush();
		world = new World();
		world.setAmbientLight(255, 255, 255);

		try {
			object3D = Object3D.mergeAll(
					Util.loadOBJ(mActivity.getApplicationContext().getAssets().open("sphere_paranomic.obj"), 1f));
		} catch (IOException e) {
			e.printStackTrace();
		}

		object3D.setTransparency(100);

		// TODO for some reason (because sphere is inverted) sphere looks upside
		// down, fix it
		object3D.rotateZ((float) Math.PI);

		object3D.rotateMesh();

		object3D.getRotationMatrix().setIdentity();

		try {

			String vertexShader = Util
					.readContents(mActivity.getApplicationContext().getAssets().open("defaultVertexShaderTex0.src"));
			String fragmentShader = Util
					.readContents(mActivity.getApplicationContext().getAssets().open("surface_fragment_shader.txt"));

			GLSLShader shader = new GLSLShader(vertexShader, fragmentShader);
			object3D.setShader(shader);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		object3D.setTexture("video_texture");
		object3D.build();

		world.addObject(object3D);

		if (surfaceTexture != null)
			surfaceTexture.release();

		textureRenderer.surfaceCreated();

		surfaceTexture = new SurfaceTexture(textureRenderer.getTextureId());
		// videoTextId = videoRenderer.getTextureId();
		surface = new Surface(surfaceTexture);

		// surface.release();
		mediaPlayer.setSurface(surface);

		//externalTexture.setExternalId(textureRenderer.getTextureId(), GLES11Ext.GL_TEXTURE_EXTERNAL_OES);
		

		surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
			@Override
			public void onFrameAvailable(SurfaceTexture surfaceTexture) {
				synchronized (VRVideo360RenderScene.this) {
					frameAvailable = true;
					// System.out.println("onFrameAvailable");
				}
			}
		});

		/*
		 * synchronized (this) { if (!mediaPlayer.isPlaying()) {
		 * mediaPlayer.setLooping(true); mediaPlayer.start(); } frameAvailable =
		 * false; }
		 */
	}
	/**
	 * Comprueba que la url que mandamos es valida
	 * 
	 * @param urlString
	 * @return
	 */
	private boolean isValid(String urlString) {
		try {
			URL url = new URL(urlString);
			return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches();
		} catch (MalformedURLException e) {

		}

		return false;
	}
	/**
	 * Crea la ventana que informa que la url no es valida
	 */
	public void noValidURLWindow() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
		builder1.setMessage("No ha introducido una URL valida");
		builder1.setCancelable(true);

		builder1.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				mActivity.finish();
			}
		});

		alert11 = builder1.create();

	}
/**
 * Inicia el mediaPlayer 
 * @throws IOException
 */
	public void initMediaPlayer() throws IOException {
		this.mediaPlayer = new MediaPlayer();
		if(mActivity.isURL)
		{
			mediaPlayer.setDataSource(urlToPlay);
		}
		else
		{
			AssetFileDescriptor afd = mActivity.getAssets().openFd(mActivity.video360Path);
			mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			afd.close();
		}
		
		mediaPlayer.prepareAsync();
		if(mActivity.isLoop){
		mediaPlayer.setLooping(true);
		}else
		{
		mediaPlayer.setLooping(false);
		}
		 mediaPlayer.setVolume(((float) mActivity.video360Volume) / 100, ((float) mActivity.video360Volume) / 100);

		/*mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {

			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {

				Log.v("MEDIA-TRACKINFO", mp.getTrackInfo() + "");
				Log.v("MEDIA-DURATION", mp.getDuration() + "");
				Log.v("MEDIA-CURRENTPOSITION", mp.getCurrentPosition() + "");
				
				Intent intentVideoDuration = new Intent(VRActivity.VR_EVENT_VIDEO_DURATION);
				intentVideoDuration.putExtra("VideoDuration", mp.getDuration());
				LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intentVideoDuration);
				
				Log.v("MEDIACOMPLETADO", "video acabado");
				return false;
			}
		});*/

		mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

			@Override
			public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {

				Log.v("BUFFERING", percent + "");
			}
		});
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				Intent intentVideoEnd = new Intent(VRActivity.VR_EVENT_VIDEO_END);
				LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intentVideoEnd);
				Log.v("MEDIACOMPLETADO", "video acabado");
				
			}
		});
		mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				// cuando el video ya este preparado, lo notifico para cambiar
				// la pantalla de espera a la del video en si
				mediaPlayerIsPrepared = true;
				mediaPlayer.start();
				Intent intentVideoStart = new Intent(VRActivity.VR_EVENT_VIDEO_START);
				intentVideoStart.putExtra("VideoDuration", mediaPlayer.getDuration());
				LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intentVideoStart);
			}
		});
		/*
		 * mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
		 * 
		 * @Override public boolean onError(MediaPlayer mp, int what, int extra)
		 * { Logger.w("media player error " + what + ":" + extra); return true;
		 * } });
		 */

	}

	public VRVideo360RenderScene(VRActivity vrActivity) throws IOException {

		this.mActivity = vrActivity;
		if(mActivity.isURL){
			url=mActivity.video360Path;
		// si la url no es valida
		if (!isValid(url)) {
			// muestro la ventana y cierro el activity
		    // creo la pantalla de url no valida por si la necesitare mas adelante
			noValidURLWindow();
			alert11.show();
		}
		if (url.contains("youtube")) {
			if (url != null && !url.contentEquals("")) {
				int startIndex = url.indexOf("=");
				videoId = url.substring(startIndex + 1, url.length());
				Log.v("VIDEOID", videoId);
				youTubeExtractor = new YouTubeExtractor(videoId);
				preferredVideoQualities = new ArrayList<Integer>();
				preferredVideoQualities.add(setYoutubeQuality(mActivity.video360Quality));
				youTubeExtractor.setPreferredVideoQualities(preferredVideoQualities);
				youTubeExtractor.startExtracting(this);
			}

		} else {
			urlToPlay = url;
		}
		}
		this.externalTexture = new Texture(32, 32);

		TextureManager.getInstance().addTexture("video_texture", externalTexture);
		TextureManager.getInstance().addTexture("skybox", new Texture(mActivity.getAssets().open("loadIMG.png")));

	}


	private Integer setYoutubeQuality(String video360Quality) {
		
		Log.v("VIDEOQUALITY", video360Quality);
		switch(video360Quality)
		{
		case "240":
			return YouTubeExtractor.YOUTUBE_VIDEO_QUALITY_SMALL_240;
		case "360":
			return YouTubeExtractor.YOUTUBE_VIDEO_QUALITY_MEDIUM_360;
		case "720":
			return YouTubeExtractor.YOUTUBE_VIDEO_QUALITY_HD_720;
		case "1080":
			return YouTubeExtractor.YOUTUBE_VIDEO_QUALITY_HD_1080;
	
		
		}
		return null;
	}

	@Override
	public void onDrawEye(Eye eye) {

		if (frameBuffer == null) {
			frameBuffer = new FrameBuffer(eye.getViewport().width, eye.getViewport().height); 
		}

		if (renderWaitWorld) {
			world = waitWorld;
			
		}
		Matrix camBack = world.getCamera().getBack();
		camBack.setIdentity();

		tempTransform.setDump(eye.getEyeView());
		tempTransform.transformToGL();
		camBack.matMul(tempTransform);

		// TODO what todo with perspective
		// tempTransform.setDump(eye.getPerspective(Config.nearPlane,
		// Config.farPlane));
		// tempTransform.transformToGL();
		// camBack.matMul(tempTransform);

		world.getCamera().setBack(camBack);

		frameBuffer.clear(RGBColor.BLACK);
		if(renderWaitWorld)
		{
			skyBox.render(world, frameBuffer);
			frameBuffer.clearZBufferOnly();
		}

		world.renderScene(frameBuffer);
		world.draw(frameBuffer);

		// glFont.blitString(frameBuffer, mediaPlayer.isPlaying() ? "Playing" :
		// "NOT Playing", 50, 100, 100, RGBColor.WHITE);

		frameBuffer.display();

	}

	@Override
	public void onFinishFrame(Viewport arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNewFrame(HeadTransform headTransform) {
		// Log.v("ONNEWFRAME","PI");
		if (!urlToPlay.equals("")||!mActivity.isURL) {
			// cuando ya se haya convertido el enlace
			if (!mediaPlayerIsInit) {
				// inicio todo una sola vez
				try {
					initMediaPlayer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mediaPlayerIsInit = true;
			}
			// cuando el video este preparado cambio de escena de carga a la
			// escena 360, este booleano se controla en el listener de
			// mediaplayer de initmediaplayer()
			if (mediaPlayerIsPrepared) {
				// paso a false el render del waitworld
				renderWaitWorld = false;
				// cargo el video360, con su correspondiente mundo
				load360Scene();
				mediaPlayerIsPrepared = false;
			}

		}

		headTransform.getHeadView(mHeadView, 0);

		synchronized (this) {
			if (frameAvailable) {
				int error = GLES20.glGetError();
				if (error != 0) {
					// Logger.w("gl error before updateTexImage" + error + ": "
					// + GLU.gluErrorString(error));
				}
				surfaceTexture.updateTexImage();
				frameAvailable = false;
				// System.out.println("updateTexImage");
			}
		}

	}

	@Override
	public void onRendererShutdown() {

		Log.v("shutdown", "");
		if (frameBuffer != null) {
			frameBuffer.dispose();
			frameBuffer = null;
		}

	}

	@Override
	public void onSurfaceChanged(int arg0, int arg1) {
		Log.v("SURFACECHANGE", "PI");
		// la primera vez siempre va a estar sin el enlace cargado por lo cual
		// siempre de inicio carga el de espera

		loadWaitScene();
		// pantalla de espera

		MemoryHelper.compact();
	}

	@Override
	public void onSurfaceCreated(EGLConfig arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(YouTubeExtractorResult result) {
		urlToPlay = result.getVideoUri().toString();
		Log.v("CONVERTIDO", result.getVideoUri().toString());

	}

	@Override
	public void onFailure(Error error) {
		// TODO Auto-generated method stub

	}

	

}
