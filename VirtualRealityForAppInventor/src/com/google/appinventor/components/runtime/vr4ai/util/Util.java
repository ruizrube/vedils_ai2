package com.google.appinventor.components.runtime.vr4ai.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.NoSuchElementException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

/**
 * @author hakan eryargi (r a f t)
 */
@SuppressLint("SimpleDateFormat")
public class Util {

	@SuppressWarnings("unchecked")
	public static <T extends View> T findView(View view, int id) {
		View v = view.findViewById(id);
		if (v == null)
			throw new NoSuchElementException("view with id " + id + " not found");
		return (T) v;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends View> T findView(Activity activity, int id) {
		View v = activity.findViewById(id);
		if (v == null)
			throw new NoSuchElementException("view with id " + id + " not found");
		return (T) v;
	}

	public static void writeToFile(File file, String data) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);
        try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(stream);
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
        } finally {
        	stream.close();
        }
    }    
	
    public static SimpleVector interpolate(SimpleVector source, SimpleVector dest, float weight, SimpleVector fill) {
    	if (fill == null)
    		fill = new SimpleVector();
    	
    	fill.x = (1-weight) * source.x + weight * dest.x;
    	fill.y = (1-weight) * source.y + weight * dest.y;
    	fill.z = (1-weight) * source.z + weight * dest.z;
    	
    	return fill;
    }
    
    public static float[] average(List<float[]> list, float[] fill) {
    	final int size = list.get(0).length;
    	
    	if (fill == null)
    		fill = new float[size];
    	
    	for (float[] array : list) {
    		for (int i = 0; i < size; i++) {
    			fill[i] += array[i];
    		}
    	}
		for (int i = 0; i < size; i++) {
			fill[i] /= list.size();
		}
		return fill;
    }
    
    /** returns the minimum 2^n size greater than or equal to given size. */
	public static int get2NSize(int imageSize) {
		int size = 2;
		while (size < imageSize) {
			size = size << 1; // x2
		}
		return size;
	}
    
	public static String readContents(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		String contents = "";
		String line = null;
		while ((line = reader.readLine()) != null) {
			contents += line + "\n";
		}
		return contents;
	}

	public static Object3D load3DS(Context context, int rawResourceId, float scale) {
		return load3DS(context.getResources().openRawResource(rawResourceId), scale);
	}
	
	public static Object3D load3DS(InputStream in, float scale) {

		Object3D[] array = Loader.load3DS(in, scale);
		for (Object3D object : array) {
			prepare3DS(object);
		}
		Object3D object = Object3D.mergeAll(array);
		
		return object;
	}
	
	public static Object3D[] load3DSArray(Context context, int rawResourceId, float scale) {
		return load3DSArray(context.getResources().openRawResource(rawResourceId), scale);
	}
	
	public static Object3D[] load3DSArray(InputStream in, float scale) {
		Object3D[] array = Loader.load3DS(in, scale);
		for (Object3D object : array) {
			prepare3DS(object);
		}
		return array;
	}
	
	private static Object3D prepare3DS(Object3D object) {
		object.rotateX((float)Math.PI);
		//object.rotateZ( (float) Math.PI );		
		object.rotateMesh();
		object.getRotationMatrix().setIdentity();
//		object.build();
		return object;
	}
	
	/*public static Object3D loadOBJ(Context context, int rawResourceId, float scale) {
	return loadOBJ(context.getResources().openRawResource(rawResourceId), scale);
}*/

public static Object3D loadOBJ(InputStream in,InputStream material, float scale) {
	
	Object3D[] array = Loader.loadOBJ(in, material, scale);
	for (Object3D object : array) {
		prepare3DS(object);
	}
	Object3D object = Object3D.mergeAll(array);
	
	return object;
}
public static Object3D loadOBJ(InputStream in, float scale) {

	Object3D[] array = Loader.loadOBJ(in, null, scale);
	for (Object3D object : array) {
		prepare3DS(object);
	}
	Object3D object = Object3D.mergeAll(array);
	
	return object;
}
	
//	public static void scaleObject(Object3D object, float xScale, float yScale, float zScale) {
//		object.getMesh().setVertexController(new MeshScaler(xScale, yScale, zScale), 
//				IVertexController.PRESERVE_SOURCE_MESH);
//		
//		object.getMesh().applyVertexController();
//		object.getMesh().removeVertexController();
//	}

	public static void showInfoMessage(Context context, String message) {
		showMessage(context, "Info", message);
	}
	
	public static void showErrorMessage(Context context, String message) {
		showMessage(context, "Error", message);
	}
	
	public static void showMessage(Context context, String title, String message) {
		new AlertDialog.Builder(context)
			.setMessage(message)
			.setTitle(title)
			.setPositiveButton("Ok", null)
			.create().show();
	}
}
