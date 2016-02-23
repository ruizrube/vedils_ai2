package com.google.appinventor.components.runtime.ar4ai;

import android.os.Parcel;
import android.os.Parcelable;

public class PhysicalObject implements Parcelable {

	public final static int TRACKER_COLOR = 0;
	public final static int TRACKER_IMAGE = 1;
	public final static int TRACKER_MARKER = 2;
	public final static int TRACKER_TEXT = 3;
	public final static int TRACKER_TARGETDB = 4;

	public final static int STATUS_INVISIBLE = 0;
	public final static int STATUS_APPEARS = 1;
	public final static int STATUS_DISAPPEARS = 2;
	public final static int STATUS_CHANGEDPOSITION = 3;
	public final static int STATUS_VISIBLE = 4;

	private boolean enabled = true;

	private VirtualObject voObject;

	private String id;
	private int trackerType;
	private int colorTracker;
	private int markerTracker;
	private String pathImageTracker;
	private String textTracker;
	private String targetDBTacker;

	private float sensitivityThresholdX;
	private float sensitivityThresholdY;
	private float sensitivityThresholdZ;

	// Tracking attributes // NO SON PARCELADOS!
	private int status = STATUS_INVISIBLE;
	private float x = 0.0f;
	private float y = 0.0f;
	private float z = 0.0f;
	private float offsetX = 0.0f;
	private float offsetY = 0.0f;
	private float offsetZ = 0.0f;
	private boolean extendedTracking = false;

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the sensitivityThresholdX
	 */
	public float getSensitivityThresholdX() {
		return sensitivityThresholdX;
	}

	/**
	 * @param sensitivityThresholdX
	 *            the sensitivityThresholdX to set
	 */
	public void setSensitivityThresholdX(float sensitivityThresholdX) {
		this.sensitivityThresholdX = sensitivityThresholdX;
	}

	/**
	 * @return the sensitivityThresholdY
	 */
	public float getSensitivityThresholdY() {
		return sensitivityThresholdY;
	}

	/**
	 * @param sensitivityThresholdY
	 *            the sensitivityThresholdY to set
	 */
	public void setSensitivityThresholdY(float sensitivityThresholdY) {
		this.sensitivityThresholdY = sensitivityThresholdY;
	}

	/**
	 * @return the sensitivityThresholdZ
	 */
	public float getSensitivityThresholdZ() {
		return sensitivityThresholdZ;
	}

	/**
	 * @param sensitivityThresholdZ
	 *            the sensitivityThresholdZ to set
	 */
	public void setSensitivityThresholdZ(float sensitivityThresholdZ) {
		this.sensitivityThresholdZ = sensitivityThresholdZ;
	}

	/**
	 * @return the targetDBTracker
	 */
	public String getTargetDBTracker() {
		return targetDBTacker;
	}

	/**
	 * @param targetDBTracker
	 *            the targetDBTracker to set
	 */
	public void setTargetDBTracker(String targetDBTracker) {
		this.targetDBTacker = targetDBTracker;
	}

	public PhysicalObject(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public int getTrackerType() {
		return trackerType;
	}

	public void setTrackerType(int trigger) {
		this.trackerType = trigger;
	}

	public int getColorTracker() {
		return colorTracker;
	}

	public void setColorTracker(int colorTracker) {
		this.colorTracker = colorTracker;
	}

	public int getMarkerTracker() {
		return markerTracker;
	}

	public void setMarkerTracker(int markerTracker) {
		this.markerTracker = markerTracker;
	}

	public String getPathImageTracker() {
		return pathImageTracker;
	}

	public void setPathImageTracker(String pathImageTracker) {
		this.pathImageTracker = pathImageTracker;
	}

	public String getTextTracker() {
		return textTracker;
	}

	public void setTextTracker(String textTracker) {
		this.textTracker = textTracker;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public float getZ() {
		return z;
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * @return the offsetX
	 */
	public float getOffsetX() {
		return offsetX;
	}

	/**
	 * @param offsetX
	 *            the offsetX to set
	 */
	public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;
	}

	/**
	 * @return the offsetY
	 */
	public float getOffsetY() {
		return offsetY;
	}

	/**
	 * @param offsetY
	 *            the offsetY to set
	 */
	public void setOffsetY(float offsetY) {
		this.offsetY = offsetY;
	}

	/**
	 * @return the offsetZ
	 */
	public float getOffsetZ() {
		return offsetZ;
	}

	/**
	 * @param offsetZ
	 *            the offsetZ to set
	 */
	public void setOffsetZ(float offsetZ) {
		this.offsetZ = offsetZ;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the voObject
	 */
	public VirtualObject getVirtualObject() {
		return voObject;
	}

	/**
	 * @param voObject
	 *            the voObject to set
	 */
	public void setVirtualObject(VirtualObject voObject) {
		this.voObject = voObject;
	}
	
	public boolean isExtendedTrackingEnabled() {
		return extendedTracking;
	}
	
	public void setExtendedTracking(boolean extendedTracking) {
		this.extendedTracking = extendedTracking;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		// The writeParcel method needs the flag
		// as well - but thats easy.
		dest.writeParcelable(voObject, flags);

		// parcel. When we read from parcel, they
		// will come back in the same order
		dest.writeValue(enabled);
		dest.writeString(id);
		dest.writeInt(trackerType);
		dest.writeString(pathImageTracker);
		dest.writeString(textTracker);
		dest.writeInt(colorTracker);
		dest.writeInt(markerTracker);
		dest.writeString(targetDBTacker);
		dest.writeFloat(sensitivityThresholdX);
		dest.writeFloat(sensitivityThresholdY);
		dest.writeFloat(sensitivityThresholdZ);
		dest.writeInt(status);
		dest.writeValue(extendedTracking);

	}

	/**
	 *
	 * Called from the constructor to create this object from a parcel.
	 *
	 * @param in
	 *            parcel from which to re-create object
	 */
	private void readFromParcel(Parcel in) {

		// readParcelable needs the ClassLoader
		// but that can be picked up from the class
		// This will solve the BadParcelableException
		// because of ClassNotFoundException
		voObject = in.readParcelable(VirtualObject.class.getClassLoader());

		// We just need to read back each
		// field in the order that it was
		// written to the parcel
		this.setEnabled((Boolean) in.readValue(null));
		this.setId(in.readString());
		this.setTrackerType(in.readInt());
		this.setPathImageTracker(in.readString());
		this.setTextTracker(in.readString());
		this.setColorTracker(in.readInt());
		this.setMarkerTracker(in.readInt());
		this.setTargetDBTracker(in.readString());
		this.setSensitivityThresholdX(in.readFloat());
		this.setSensitivityThresholdY(in.readFloat());
		this.setSensitivityThresholdZ(in.readFloat());
		this.setStatus(in.readInt());
		this.setExtendedTracking((Boolean)in.readValue(null));

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
		public PhysicalObject createFromParcel(Parcel in) {
			return new PhysicalObject(in);
		}

		public PhysicalObject[] newArray(int size) {
			return new PhysicalObject[size];
		}
	};

	/**
	 *
	 * Constructor to use when re-constructing object from a parcel
	 *
	 * @param in
	 *            a parcel from which to read this object
	 */
	public PhysicalObject(Parcel in) {
		readFromParcel(in);
	}

}