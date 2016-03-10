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
	private boolean leftBtEnabled=false;
	private boolean rightBtEnabled=false;
	private String leftBtText;
	private String rightBtText;
	private String floatingText;
	

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

	public boolean isLeftBtEnabled() {
		return leftBtEnabled;
	}


	public void setLeftBtEnabled(boolean leftBtEnabled) {
		this.leftBtEnabled = leftBtEnabled;
	}


	public boolean isRightBtEnabled() {
		return rightBtEnabled;
	}


	public void setRightBtEnabled(boolean rightBtEnabled) {
		this.rightBtEnabled = rightBtEnabled;
	}


	public String getLeftBtText() {
		return leftBtText;
	}


	public void setLeftBtText(String leftBtText) {
		this.leftBtText = leftBtText;
	}


	public String getRightBtText() {
		return rightBtText;
	}


	public void setRightBtText(String rightBtText) {
		this.rightBtText = rightBtText;
	}


	public String getFloatingText() {
		return floatingText;
	}


	public void setFloatingText(String floatingText) {
		this.floatingText = floatingText;
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
		dest.writeByte((byte) (leftBtEnabled ? 1 : 0));
		dest.writeByte((byte) (rightBtEnabled ? 1 : 0));
		dest.writeString(leftBtText);
		dest.writeString(rightBtText);
		dest.writeString(floatingText);
		
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
		this.setLeftBtEnabled(in.readByte() != 0);
		this.setRightBtEnabled(in.readByte() != 0);
		this.setLeftBtText(in.readString());
		this.setRightBtText(in.readString());
		this.setFloatingText(in.readString());
		
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