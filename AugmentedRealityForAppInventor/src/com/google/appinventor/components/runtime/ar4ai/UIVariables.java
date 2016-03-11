package com.google.appinventor.components.runtime.ar4ai;

import android.os.Parcel;
import android.os.Parcelable;

public class UIVariables implements Parcelable {
	
	private boolean leftBtEnabled=false;
	private boolean rightBtEnabled=false;
	private String leftBtText;
	private String rightBtText;
	private String floatingText;
	
	public UIVariables(){
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
		dest.writeByte((byte) (leftBtEnabled ? 1 : 0));
		dest.writeByte((byte) (rightBtEnabled ? 1 : 0));
		dest.writeString(leftBtText);
		dest.writeString(rightBtText);
		dest.writeString(floatingText);
	}
	
	private void readFromParcel(Parcel in) {
		this.setLeftBtEnabled(in.readByte() != 0);
		this.setRightBtEnabled(in.readByte() != 0);
		this.setLeftBtText(in.readString());
		this.setRightBtText(in.readString());
		this.setFloatingText(in.readString());
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public UIVariables createFromParcel(Parcel in) {
			return new UIVariables(in);
		}
		
		public UIVariables[] newArray(int size) {
			return new UIVariables[size];
		}
		
	};
	
	public UIVariables(Parcel in) {
		readFromParcel(in);
	}

}
