// 
// Decompiled by Procyon v0.5.30
// 

package com.google.appinventor.components.runtime.vr4ai;

import javax.microedition.khronos.egl.EGLConfig;
import com.threed.jpct.util.MemoryHelper;
import android.opengl.GLES20;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.threed.jpct.RGBColor;
import com.google.vrtoolkit.cardboard.Eye;
import com.threed.jpct.TextureManager;
import java.util.ArrayList;
import android.content.res.AssetFileDescriptor;
import java.io.FileDescriptor;
import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;
import java.io.FileInputStream;
import android.content.DialogInterface;
import android.content.Context;
import java.net.MalformedURLException;
import android.util.Patterns;
import android.webkit.URLUtil;
import java.net.URL;
import java.io.IOException;
import com.google.appinventor.components.runtime.vr4ai.util.Util;
import android.util.Log;
import com.threed.jpct.GLSLShader;
import com.threed.jpct.ShaderLocator;
import com.threed.jpct.util.SkyBox;
import android.app.AlertDialog;
import com.threed.jpct.Light;
import java.util.List;
import android.view.Surface;
import com.threed.jpct.Texture;
import com.google.appinventor.components.runtime.vr4ai.util.TextureRenderer;
import android.graphics.SurfaceTexture;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.World;
import com.threed.jpct.FrameBuffer;
import android.media.MediaPlayer;
import com.google.appinventor.components.runtime.vr4ai.util.YouTubeExtractor;
import com.google.vrtoolkit.cardboard.CardboardView;

public class VRVideo360RenderScene implements CardboardView.StereoRenderer, YouTubeExtractor.YouTubeExtractorListener
{
    public MediaPlayer mediaPlayer;
    public VRActivity mActivity;
    private FrameBuffer frameBuffer;
    private World world;
    private World waitWorld;
    private Object3D object3D;
    private final Matrix tempTransform;
    private float[] mHeadView;
    private SurfaceTexture surfaceTexture;
    private TextureRenderer textureRenderer;
    private Texture externalTexture;
    private boolean frameAvailable;
    private String url;
    private YouTubeExtractor youTubeExtractor;
    public String urlToPlay;
    public String videoId;
    public boolean conversionOk;
    private YouTubeExtractor.YouTubeExtractorResult result;
    Surface surface;
    private boolean mediaPlayerIsInit;
    private List<Integer> preferredVideoQualities;
    private boolean mediaPlayerIsPrepared;
    int media_length;
    public boolean mediaPlayerResume;
    private Light sun;
    private boolean renderWaitWorld;
    AlertDialog alert11;
    private SkyBox skyBox;
    
    public void loadWaitScene() {
        (this.waitWorld = new World()).setAmbientLight(200, 200, 200);
        this.skyBox = new SkyBox("skybox", "skybox", "skybox", "skybox", "skybox", "skybox", 10.0f);
        final ShaderLocator s = new ShaderLocator(this.mActivity.getAssets());
        GLSLShader.setShaderLocator(s);
    }
    
    public void load360Scene() {
        Log.v("CARGAESCENA", "");
        this.frameBuffer.flush();
        (this.world = new World()).setAmbientLight(255, 255, 255);
        try {        
            this.object3D = Object3D.mergeAll(new Object3D[] { Util.loadOBJ(this.mActivity.getApplicationContext().getAssets().open("sphere_paranomic.obj"), 1.0f) });            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.object3D.setTransparency(100);
        this.object3D.rotateZ(3.1415927f);
        this.object3D.rotateMesh();
        this.object3D.getRotationMatrix().setIdentity();        
        try {
            final String vertexShader = Util.readContents(this.mActivity.getApplicationContext().getAssets().open("defaultVertexShaderTex0.src"));
            final String fragmentShader = Util.readContents(this.mActivity.getApplicationContext().getAssets().open("surface_fragment_shader.txt"));
            final GLSLShader shader = new GLSLShader(vertexShader, fragmentShader);
            this.object3D.setShader(shader);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.object3D.setTexture("video_texture");        
        
        this.object3D.build();
        this.world.addObject(this.object3D);
        if (this.surfaceTexture != null) {
            this.surfaceTexture.release();
        }
        this.textureRenderer.surfaceCreated();
        this.surfaceTexture = new SurfaceTexture(this.textureRenderer.getTextureId());
        this.surface = new Surface(this.surfaceTexture);
        this.mediaPlayer.setSurface(this.surface);
        this.surfaceTexture.setOnFrameAvailableListener((SurfaceTexture.OnFrameAvailableListener)new SurfaceTexture.OnFrameAvailableListener() {
            public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
                synchronized (VRVideo360RenderScene.this) {
                    VRVideo360RenderScene.access$0(VRVideo360RenderScene.this, true);
                }
                // monitorexit(this.this$0)
            }
        });
    }
    
    private boolean isValid(final String urlString) {
        try {
            final URL url = new URL(urlString);
            return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches();
        }
        catch (MalformedURLException ex) {
            return false;
        }
    }
    
    public void noValidURLWindow() {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder((Context)this.mActivity);
        builder1.setMessage((CharSequence)"No ha introducido una URL valida");
        builder1.setCancelable(true);
        builder1.setPositiveButton((CharSequence)"Salir", (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                dialog.cancel();
                VRVideo360RenderScene.this.mActivity.finish();
            }
        });
        this.alert11 = builder1.create();
    }
    
    public void initMediaPlayer() throws IOException {
        this.mediaPlayer = new MediaPlayer();
        if (this.mActivity.isURL) {
            this.mediaPlayer.setDataSource(this.urlToPlay);
        }
        else if (this.mActivity.isCompanion) {
            final FileInputStream fi = new FileInputStream("/sdcard/AppInventor/assets/" + this.mActivity.video360Path);
            final FileDescriptor fd = fi.getFD();
            this.mediaPlayer.setDataSource(fd);
        }
        else {
            final AssetFileDescriptor afd = this.mActivity.getAssets().openFd(this.mActivity.video360Path);
            this.mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
        }
        this.mediaPlayer.prepareAsync();
        if (this.mActivity.isLoop) {
            this.mediaPlayer.setLooping(true);
        }
        else {
            this.mediaPlayer.setLooping(false);
        }
        this.mediaPlayer.setVolume(this.mActivity.video360Volume / 100.0f, this.mActivity.video360Volume / 100.0f);
        this.mediaPlayer.setOnBufferingUpdateListener((MediaPlayer.OnBufferingUpdateListener)new MediaPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(final MediaPlayer mediaPlayer, final int percent) {
                Log.v("BUFFERING", new StringBuilder(String.valueOf(percent)).toString());
            }
        });
        this.mediaPlayer.setOnCompletionListener((MediaPlayer.OnCompletionListener)new MediaPlayer.OnCompletionListener() {
            public void onCompletion(final MediaPlayer mp) {
                final Intent intentVideoEnd = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.endVideo");
                LocalBroadcastManager.getInstance((Context)VRVideo360RenderScene.this.mActivity).sendBroadcast(intentVideoEnd);
                Log.v("MEDIACOMPLETADO", "video acabado");
            }
        });
        this.mediaPlayer.setOnPreparedListener((MediaPlayer.OnPreparedListener)new MediaPlayer.OnPreparedListener() {
            public void onPrepared(final MediaPlayer mediaPlayer) {
                VRVideo360RenderScene.access$1(VRVideo360RenderScene.this, true);
                mediaPlayer.start();
                final Intent intentVideoStart = new Intent("com.google.appinventor.components.runtime.vr4ai.VRActivity.startVideo");
                intentVideoStart.putExtra("VideoDuration", mediaPlayer.getDuration());
                LocalBroadcastManager.getInstance((Context)VRVideo360RenderScene.this.mActivity).sendBroadcast(intentVideoStart);
            }
        });
    }
    
    public VRVideo360RenderScene(final VRActivity vrActivity) throws IOException {
        this.tempTransform = new Matrix();
        this.mHeadView = new float[16];
        this.textureRenderer = new TextureRenderer();
        this.frameAvailable = false;
        this.urlToPlay = "";
        this.conversionOk = false;
        this.media_length = -1;
        this.mediaPlayerResume = false;
        this.renderWaitWorld = true;
        this.mActivity = vrActivity;
        if (this.mActivity.isURL) {
            this.url = this.mActivity.video360Path;
            if (!this.isValid(this.url)) {
                this.noValidURLWindow();
                this.alert11.show();
            }
            if (this.url.contains("youtube")) {
                if (this.url != null && !this.url.contentEquals("")) {
                    final int startIndex = this.url.indexOf("=");
                    Log.v("VIDEOID", this.videoId = this.url.substring(startIndex + 1, this.url.length()));
                    this.youTubeExtractor = new YouTubeExtractor(this.videoId);
                    (this.preferredVideoQualities = new ArrayList<Integer>()).add(this.setYoutubeQuality(this.mActivity.video360Quality));
                    this.youTubeExtractor.setPreferredVideoQualities(this.preferredVideoQualities);
                    this.youTubeExtractor.startExtracting(this);
                }
            }
            else {
                this.urlToPlay = this.url;
            }
        }
        this.externalTexture = new Texture(32, 32);
        TextureManager.getInstance().addTexture("video_texture", this.externalTexture);
        TextureManager.getInstance().addTexture("skybox", new Texture(this.mActivity.getAssets().open("loadIMG.png")));
    }
    
    private Integer setYoutubeQuality(final String video360Quality) {
        Log.v("VIDEOQUALITY", video360Quality);
        switch (video360Quality) {
            case "240": {
                return 36;
            }
            case "360": {
                return 18;
            }
            case "720": {
                return 22;
            }
            case "1080": {
                return 137;
            }
            default:
                break;
        }
        return null;
    }
    
    
    
    public void onDrawEye(final Eye eye) {
        if (this.frameBuffer == null) {
            this.frameBuffer = new FrameBuffer(eye.getViewport().width,eye.getViewport().height);
        }
        if (this.renderWaitWorld) {
            this.world = this.waitWorld;
        }
        final Matrix camBack = this.world.getCamera().getBack();
        camBack.setIdentity();      
        this.tempTransform.setDump(eye.getEyeView());
        this.tempTransform.transformToGL();
        //Edson
        float var= (float) - 1.047198; //Edson 60 grados aproximado va bien 
        tempTransform.rotateY(var);       
        //Edson                
        camBack.matMul(this.tempTransform);        
        this.world.getCamera().setBack(camBack);
        this.frameBuffer.clear(RGBColor.BLACK);
        if (this.renderWaitWorld) {
            this.skyBox.render(this.world, this.frameBuffer);
            this.frameBuffer.clearZBufferOnly();
        }
        this.world.renderScene(this.frameBuffer);        
        this.world.draw(this.frameBuffer);
        this.frameBuffer.display();
    }
    
    public void onFinishFrame(final Viewport arg0) {
    }
    
    public void onNewFrame(final HeadTransform headTransform) {
        if (!this.urlToPlay.equals("") || !this.mActivity.isURL) {
            if (!this.mediaPlayerIsInit) {
                try {
                    this.initMediaPlayer();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                this.mediaPlayerIsInit = true;
            }
            if (this.mediaPlayerIsPrepared) {
                this.renderWaitWorld = false;
                this.load360Scene();
                this.mediaPlayerIsPrepared = false;
            }
        }
        headTransform.getHeadView(this.mHeadView, 0);
        synchronized (this) {
            if (this.frameAvailable) {
                final int error = GLES20.glGetError();
                this.surfaceTexture.updateTexImage();
                this.frameAvailable = false;
            }
        }        
    }
    
    public void onRendererShutdown() {
        Log.v("shutdown", "");
        if (this.frameBuffer != null) {
            this.frameBuffer.dispose();
            this.frameBuffer = null;
        }
    }
    
    public void onSurfaceChanged(final int arg0, final int arg1) {
        Log.v("SURFACECHANGE", "PI");
        this.loadWaitScene();
        MemoryHelper.compact();       
    }
    
    public void onSurfaceCreated(final EGLConfig arg0) {
    }
    
    public void onSuccess(final YouTubeExtractor.YouTubeExtractorResult result) {
        this.urlToPlay = result.getVideoUri().toString();
        Log.v("CONVERTIDO", result.getVideoUri().toString());
    }
    
    public void onFailure(final Error error) {
    	
    	
    }
    
    static /* synthetic */ void access$0(final VRVideo360RenderScene vrVideo360RenderScene, final boolean frameAvailable) {
        vrVideo360RenderScene.frameAvailable = frameAvailable;
    }
    
    static /* synthetic */ void access$1(final VRVideo360RenderScene vrVideo360RenderScene, final boolean mediaPlayerIsPrepared) {
        vrVideo360RenderScene.mediaPlayerIsPrepared = mediaPlayerIsPrepared;
    }
}
