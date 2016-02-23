package com.google.appinventor.components.runtime.ar4ai.vuforia;

import com.threed.jpct.Matrix;

public class WorldInfo {
	Matrix mat;
	boolean visible=false;
	float[] marker_pos;
	
	public float[] getMarker_pos() {
		return marker_pos;
	}

	public void setMarker_pos(float[] marker_pos) {
		this.marker_pos = marker_pos;
	}

	WorldInfo(){
		this.visible = false;
		this.mat = new Matrix();
	}

	public Matrix getMat() {
		return mat;
	}

	public void setMat(Matrix mat) {
		this.mat = mat;
	}

	public void setMat(float [] float_mat) {
		this.mat.setDump(float_mat);
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisibility(boolean visible) {
		this.visible = visible;
	}

}