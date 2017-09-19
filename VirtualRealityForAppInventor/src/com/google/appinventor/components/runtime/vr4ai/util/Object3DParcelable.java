package com.google.appinventor.components.runtime.vr4ai.util;

import android.os.Parcel;
import android.os.Parcelable;

public class Object3DParcelable implements Parcelable{
	
	
	private String model3DPath;
	private String material3DPath;
	private int positionX;
	private int positionY;
	private int positionZ;
	private int scale;
	private String id;
	

	public Object3DParcelable()
	{
		
	}
	//getters
	
	public String getModel3DPath(){return model3DPath;}
	public String getMaterial3DPath(){return material3DPath;}
	public int getPositionX(){return positionX;}
	public int getPositionY(){return positionY;}
	public int getPositionZ(){return positionZ;}
	public int getScale(){return scale;}
	public String getId(){return id;}
	
	//setters
	
	public void setModel3DPath(String path){this.model3DPath=path;}
	public void setMaterial3DPath(String path){this.material3DPath=path;}
	public void setPositionX(int value){this.positionX=value;}
	public void setPositionY(int value){this.positionY=value;}
	public void setPositionZ(int value){this.positionZ=value;}
	public void setScale(int value){this.scale=value;}
	public void setId(String value){this.id=value;}

	//write object values to parcel for storage
	public void writeToParcel(Parcel dest, int flags){
	    
		dest.writeString(model3DPath);
		dest.writeString(material3DPath);
		dest.writeInt(positionX);
		dest.writeInt(positionY);
		dest.writeInt(positionZ);
		dest.writeInt(scale);
		dest.writeString(id);
	}

	//constructor used for parcel
	public Object3DParcelable(Parcel parcel){
	    
		model3DPath = parcel.readString();
		material3DPath= parcel.readString();
		positionX=parcel.readInt();
		positionY=parcel.readInt();
		positionZ=parcel.readInt();
		scale=parcel.readInt();
		id=parcel.readString();
	}

	//creator - used when un-parceling our parcle (creating the object)
	public static final Parcelable.Creator<Object3DParcelable> CREATOR = new Parcelable.Creator<Object3DParcelable>(){

	    @Override
	    public Object3DParcelable createFromParcel(Parcel parcel) {
	        return new Object3DParcelable(parcel);
	    }

	    @Override
	    public Object3DParcelable[] newArray(int size) {
	        return new Object3DParcelable[size];
	    }
	};

	//return hashcode of object
	public int describeContents() {
	    return hashCode();
	}


}
