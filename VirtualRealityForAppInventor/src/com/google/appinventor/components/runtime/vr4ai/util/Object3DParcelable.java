// 
// Decompiled by Procyon v0.5.30
// 

package com.google.appinventor.components.runtime.vr4ai.util;

import android.os.Parcel;
import android.os.Parcelable;

public class Object3DParcelable implements Parcelable
{
    private String model3DPath;
    private String material3DPath;
    private int positionX;
    private int positionY;
    private int positionZ;
    private float rotateSpeed;
    private float moveSpeed;
    private int scale;
    private String id;
    public static final Parcelable.Creator<Object3DParcelable> CREATOR;
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<Object3DParcelable>() {
            public Object3DParcelable createFromParcel(final Parcel parcel) {
                return new Object3DParcelable(parcel);
            }
            
            public Object3DParcelable[] newArray(final int size) {
                return new Object3DParcelable[size];
            }
        };
    }
    
    public Object3DParcelable() {
    }
    
    public String getModel3DPath() {
        return this.model3DPath;
    }
    
    public String getMaterial3DPath() {
        return this.material3DPath;
    }
    
    public int getPositionX() {
        return this.positionX;
    }
    
    public int getPositionY() {
        return this.positionY;
    }
    
    public int getPositionZ() {
        return this.positionZ;
    }
    
    public float getRotateSpeed() {
        return this.rotateSpeed;
    }
    
    public float getMoveSpeed() {
        return this.moveSpeed;
    }
    
    public int getScale() {
        return this.scale;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setModel3DPath(final String path) {
        this.model3DPath = path;
    }
    
    public void setMaterial3DPath(final String path) {
        this.material3DPath = path;
    }
    
    public void setPositionX(final int value) {
        this.positionX = value;
    }
    
    public void setPositionY(final int value) {
        this.positionY = value;
    }
    
    public void setPositionZ(final int value) {
        this.positionZ = value;
    }
    
    public void setRotateSpeed(final float value) {
        this.rotateSpeed = value;
    }
    
    public void setMoveSpeed(final float value) {
        this.moveSpeed = value;
    }
    
    public void setScale(final int value) {
        this.scale = value;
    }
    
    public void setId(final String value) {
        this.id = value;
    }
    
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(this.model3DPath);
        dest.writeString(this.material3DPath);
        dest.writeInt(this.positionX);
        dest.writeInt(this.positionY);
        dest.writeInt(this.positionZ);
        dest.writeFloat(this.rotateSpeed);
        dest.writeFloat(this.moveSpeed);
        dest.writeInt(this.scale);
        dest.writeString(this.id);
    }
    
    public Object3DParcelable(final Parcel parcel) {
        this.model3DPath = parcel.readString();
        this.material3DPath = parcel.readString();
        this.positionX = parcel.readInt();
        this.positionY = parcel.readInt();
        this.positionZ = parcel.readInt();
        this.rotateSpeed = parcel.readFloat();
        this.moveSpeed = parcel.readFloat();
        this.scale = parcel.readInt();
        this.id = parcel.readString();
    }
    
    public int describeContents() {
        return this.hashCode();
    }
}
