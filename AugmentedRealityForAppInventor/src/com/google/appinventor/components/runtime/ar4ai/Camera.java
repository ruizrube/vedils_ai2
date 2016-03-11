package com.google.appinventor.components.runtime.ar4ai;

import android.os.Parcel;
import android.os.Parcelable;

public class Camera implements Parcelable {
	private int requestCode;
	private boolean stereo=true;
	private boolean frontCamera=false;
	private int screenOrientation=1;
	private String pathTargetDBXML;
	private String pathTargetDBDAT;
	private String title;
	private String subtitle;
	private String pathUIXML;
	private UIVariables uivariables;
	

	public Camera() {
	}

	
	/**
	 * @return the pathTargetDBXML
	 */
	public String getPathTargetDBXML() {
		return pathTargetDBXML;
	}

	/**
	 * @param pathTargetDBXML the pathTargetDBXML to set
	 */
	public void setPathTargetDBXML(String pathTargetDBXML) {
		this.pathTargetDBXML = pathTargetDBXML;
	}

	/**
	 * @return the pathTargetDBDAT
	 */
	public String getPathTargetDBDAT() {
		return pathTargetDBDAT;
	}

	/**
	 * @param pathTargetDBDAT the pathTargetDBDAT to set
	 */
	public void setPathTargetDBDAT(String pathTargetDBDAT) {
		this.pathTargetDBDAT = pathTargetDBDAT;
	}

	
	
	public int getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}

	public boolean isStereo() {
		return stereo;
	}

	public void setStereo(boolean stereo) {
		this.stereo = stereo;
	}

	public boolean isFrontCamera() {
		return frontCamera;
	}

	public void setFrontCamera(boolean frontCamera) {
		this.frontCamera = frontCamera;
	}

	public int getScreenOrientation() {
		return screenOrientation;
	}

	public void setScreenOrientation(int screenOrientation) {
		this.screenOrientation = screenOrientation;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @return the subtitle
	 */
	public String getSubtitle() {
		return subtitle;
	}


	/**
	 * @param subtitle the subtitle to set
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getPathUIXML() {
		return pathUIXML;
	}


	public void setPathUIXML(String pathUIXML) {
		this.pathUIXML = pathUIXML;
	}

	public UIVariables getUivariables() {
		return uivariables;
	}


	public void setUivariables(UIVariables uivariables) {
		this.uivariables = uivariables;
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		// parcel. When we read from parcel, they
		// will come back in the same order
		dest.writeInt(requestCode);
		dest.writeByte((byte) (stereo ? 1 : 0)); 
		dest.writeByte((byte) (frontCamera ? 1 : 0)); 
		dest.writeInt(screenOrientation);
		dest.writeString(pathTargetDBXML);
		dest.writeString(pathTargetDBDAT);
		dest.writeString(title);
		dest.writeString(subtitle);
		dest.writeParcelable(uivariables, flags);
		
	}

	/**
	 *
	 * Called from the constructor to create this object from a parcel.
	 *
	 * @param in
	 *            parcel from which to re-create object
	 */
	private void readFromParcel(Parcel in) {

		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		this.setRequestCode(in.readInt());
		this.setStereo(in.readByte() != 0);
		this.setFrontCamera(in.readByte() != 0);
		this.setScreenOrientation(in.readInt());
		this.setPathTargetDBXML(in.readString());
		this.setPathTargetDBDAT(in.readString());
		this.setTitle(in.readString());
		this.setSubtitle(in.readString());
		this.setUivariables((UIVariables) in.readParcelable(UIVariables.class.getClassLoader()));
		
	}

	/**
	 *
	 * This field is needed for Android to be able to create new objects,
	 * individually or as arrays.
	 *
	 * This also means that you can use use the default constructor to create
	 * the object and use another method to hyrdate it as necessary.
	 *
	 * I just find it easier to use the constructor. It makes sense for the way
	 * my brain thinks ;-)O
	 *
	 */
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Camera createFromParcel(Parcel in) {
			return new Camera(in);
		}

		public Camera[] newArray(int size) {
			return new Camera[size];
		}
	};

	/**
	 *
	 * Constructor to use when re-constructing object from a parcel
	 *
	 * @param in
	 *            a parcel from which to read this object
	 */
	public Camera(Parcel in) {
		readFromParcel(in);
	}

}