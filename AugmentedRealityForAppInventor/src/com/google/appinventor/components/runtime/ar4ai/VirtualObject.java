package com.google.appinventor.components.runtime.ar4ai;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

public class VirtualObject implements Parcelable {
	public final static int ASSET_TEXT = 0;
	public final static int ASSET_IMAGE = 1;
	public final static int ASSET_3DMODEL = 2;
	
	
	
	private boolean enabled=true;
	private String id;
	private int visualAssetType;
	
	private float positionX;
	private float positionY;
	private float positionZ;
	private float rotationX;
	private float rotationY;
	private float rotationZ;
	private float translationX;
	private float translationY;
	private float translationZ;
	private float scale = 1f;
	private int transparency;
	private boolean animated=false;
	private int animationSecuence;
	
	private String overlaid3DModel;
	private String overlaidText;
	private String overlaidImage;
	
	//private PhysicalObject poObject;

	private float mass;
	private int colorTexture=Color.TRANSPARENT;
	private String imageTexture;
	private String material;
	
	private boolean zoomActivated = true;
	private boolean rotateActivated = true;
	private float zoomMaxSize = 10;
	private float zoomMinSize = 1;
	

	public VirtualObject(String id) {
		this.id=id;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}



	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the visualAssetType
	 */
	public int getVisualAssetType() {
		return visualAssetType;
	}

	/**
	 * @param visualAssetType the visualAssetType to set
	 */
	public void setVisualAssetType(int visualAssetType) {
		this.visualAssetType = visualAssetType;
	}



	/**
	 * @return the colorTexture
	 */
	public int getColorTexture() {
		return colorTexture;
	}

	/**
	 * @param colorTexture the colorTexture to set
	 */
	public void setColorTexture(int colorTexture) {
		this.colorTexture = colorTexture;
	}

	/**
	 * @return the imageTexture
	 */
	public String getImageTexture() {
		return imageTexture;
	}

	/**
	 * @param imageTexture the imageTexture to set
	 */
	public void setImageTexture(String imageTexture) {
		this.imageTexture = imageTexture;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	public float getPositionX() {
		return positionX;
	}

	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}

	public float getPositionY() {
		return positionY;
	}

	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}

	public float getPositionZ() {
		return positionZ;
	}

	public void setPositionZ(float positionZ) {
		this.positionZ = positionZ;
	}

	public float getRotationX() {
		return rotationX;
	}

	public void setRotationX(float rotationX) {
		this.rotationX = rotationX;
	}

	public float getRotationY() {
		return rotationY;
	}

	public void setRotationY(float rotationY) {
		this.rotationY = rotationY;
	}

	public float getRotationZ() {
		return rotationZ;
	}

	public void setRotationZ(float rotationZ) {
		this.rotationZ = rotationZ;
	}

	public float getTranslationX() {
		return translationX;
	}

	public void setTranslationX(float translationX) {
		this.translationX = translationX;
	}

	public float getTranslationY() {
		return translationY;
	}

	public void setTranslationY(float translationY) {
		this.translationY = translationY;
	}

	public float getTranslationZ() {
		return translationZ;
	}

	public void setTranslationZ(float translationZ) {
		this.translationZ = translationZ;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public boolean isAnimated() {
		return animated;
	}
	
	public void setAnimated(boolean animated) {
		this.animated = animated;
	}
	
	public int getAnimationSecuence() {
		return animationSecuence;
	}
	
	public void setAnimationSecuence(int animationSecuence) {
		this.animationSecuence = animationSecuence;
	}

	public int getTransparency() {
		return transparency;
	}

	public void setTransparency(int transparency) {
		this.transparency = transparency;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public String getOverlaid3DModel() {
		return overlaid3DModel;
	}

	public void setOverlaid3DModel(String overlaid3DModel) {
		this.overlaid3DModel = overlaid3DModel;
	}

	public String getOverlaidText() {
		return overlaidText;
	}

	public void setOverlaidText(String overlaidText) {
		this.overlaidText = overlaidText;
	}
	
	public void setPhysicalObject(PhysicalObject poObject) {
		poObject.addVirtualObject(this);
	}
	
	public void removePhysicalObject(PhysicalObject poObject) {
		poObject.removeVirtualObject(this);
	}

	
	/**
	 * @return the material
	 */
	public String getMaterial() {
		return material;
	}

	/**
	 * @param material the material to set
	 */
	public void setMaterial(String material) {
		this.material = material;
	}

	/**
	 * @return the overlaidImage
	 */
	public String getOverlaidImage() {
		return overlaidImage;
	}

	/**
	 * @param overlaidImage the overlaidImage to set
	 */
	public void setOverlaidImage(String overlaidImage) {
		this.overlaidImage = overlaidImage;
	}
	
	/**
	 * @return the zoomActivated property (activated or not).
	 */
	public boolean getZoomActivated() {
		return zoomActivated;
	}
	
	/**
	 * @param zoomActivated set the property (activated or not).
	 */
	public void setZoomActivated(boolean zoomActivated) {
		this.zoomActivated = zoomActivated;
	}
	
	/**
	 * @return the rotateActivated property (activated or not).
	 */
	public boolean getRotateActivated() {
		return rotateActivated;
	}
	
	/**
	 * @param rotateActivated set the property (activated or not).
	 */
	public void setRotateActivated(boolean rotateActivated) {
		this.rotateActivated = rotateActivated;
	}
	
	/**
	 * @return the zoomMaxSize property (max object size).
	 */
	public float getZoomMaxSize() {
		return zoomMaxSize;
	}
	
	/**
	 * @param zoomMaxSize set the property (max object size).
	 */
	public void setZoomMaxSize(float zoomMaxSize) {
		this.zoomMaxSize = zoomMaxSize;
	}
	
	/**
	 * @return the zoomMinSize property (min object size).
	 */
	public float getZoomMinSize() {
		return zoomMinSize;
	}
	
	/**
	 * @param zoomMinSize set the property (min object size).
	 */
	public void setZoomMinSize(float zoomMinSize) {
		this.zoomMinSize = zoomMinSize;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		// parcel. When we read from parcel, they
		// will come back in the same order
		//dest.writeParcelable(poObject, flags);
		
		dest.writeValue(enabled);
		dest.writeString(id);
		dest.writeFloat(positionX);
		dest.writeFloat(positionY);
		dest.writeFloat(positionZ);
		dest.writeFloat(rotationX);
		dest.writeFloat(rotationY);
		dest.writeFloat(rotationZ);
		dest.writeFloat(translationX);
		dest.writeFloat(translationY);
		dest.writeFloat(translationZ);
		dest.writeFloat(scale);
		dest.writeByte((byte) (animated ? 1 : 0));
		dest.writeInt(animationSecuence);
		dest.writeInt(transparency);
		dest.writeFloat(mass);
		dest.writeInt(visualAssetType);
		dest.writeString(overlaid3DModel);
		dest.writeInt(colorTexture);
		dest.writeString(imageTexture);	
		dest.writeString(overlaidText);
		dest.writeString(overlaidImage);
		dest.writeString(material);
		dest.writeValue(zoomActivated);
		dest.writeValue(rotateActivated);
		dest.writeFloat(zoomMaxSize);
		dest.writeFloat(zoomMinSize);
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
		//poObject = in.readParcelable(PhysicalObject.class.getClassLoader());
		
		this.setEnabled((Boolean) in.readValue(null));
		this.setId(in.readString());
		this.setPositionX(in.readFloat());
		this.setPositionY(in.readFloat());
		this.setPositionZ(in.readFloat());
		this.setRotationX(in.readFloat());
		this.setRotationY(in.readFloat());
		this.setRotationZ(in.readFloat());
		this.setTranslationX(in.readFloat());
		this.setTranslationY(in.readFloat());
		this.setTranslationZ(in.readFloat());
		this.setScale(in.readFloat());
		this.setAnimated(in.readByte() != 0);
		this.setAnimationSecuence(in.readInt());
		this.setTransparency(in.readInt());
		this.setMass(in.readFloat());
		this.setVisualAssetType(in.readInt());	
		this.setOverlaid3DModel(in.readString());
		this.setColorTexture(in.readInt());
		this.setImageTexture(in.readString());
		this.setOverlaidText(in.readString());
		this.setOverlaidImage(in.readString());
		this.setMaterial(in.readString());
		this.setZoomActivated((Boolean) in.readValue(null));
		this.setRotateActivated((Boolean) in.readValue(null));
		this.setZoomMaxSize(in.readFloat());
		this.setZoomMinSize(in.readFloat());
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
	 * my brain thinks ;-)
	 *
	 */
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public VirtualObject createFromParcel(Parcel in) {
			return new VirtualObject(in);
		}

		public VirtualObject[] newArray(int size) {
			return new VirtualObject[size];
		}
	};

	/**
	 *
	 * Constructor to use when re-constructing object from a parcel
	 *
	 * @param in
	 *            a parcel from which to read this object
	 */
	public VirtualObject(Parcel in) {
		readFromParcel(in);
	}

}