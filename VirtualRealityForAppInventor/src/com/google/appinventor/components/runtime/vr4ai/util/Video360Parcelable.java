package com.google.appinventor.components.runtime.vr4ai.util;

import android.os.Parcel;
import android.os.Parcelable;

public class Video360Parcelable implements Parcelable {

	private String video360Path;
	private String video360Quality;
	private int  isURL;
	private int  isLoop;
	private int video360Volumen;
	private String id;
	
	
	public Video360Parcelable()
	{
		
	}
	//getters
	
	public String getVideo360Path(){return video360Path;}
	public String getVideo360Quality(){return video360Quality;}
	public int getIsURL(){return isURL;}
	public int getIsLoop(){return isLoop;}
	public int getVideo360Volumen(){return video360Volumen;}
	public String getId(){return id;}
	
	//setters
	
		public void setVideo360Path(String path){this.video360Path=path;}
		public void setVideo360Quality(String path){this.video360Quality=path;}
		public void setIsURL(int value){this.isURL=value;}
		public void setIsLoop(int value){this.isLoop=value;}
		public void setVideo360Volumen(int value){this.video360Volumen=value;}
		public void setId(String value){this.id=value;}
		
		//write object values to parcel for storage
		public void writeToParcel(Parcel dest, int flags){
		    
			dest.writeString(video360Path);
			dest.writeString(video360Quality);
			dest.writeInt(isURL);
			dest.writeInt(isLoop);
			dest.writeInt(video360Volumen);
			dest.writeString(id);
			
			
		}
		//constructor used for parcel
		public Video360Parcelable(Parcel parcel){
		    
			video360Path = parcel.readString();
			video360Quality= parcel.readString();
			isURL=parcel.readInt();
			isLoop=parcel.readInt();
			video360Volumen=parcel.readInt();
			id=parcel.readString();

			
		}
		//creator - used when un-parceling our parcle (creating the object)
		public static final Parcelable.Creator<Video360Parcelable> CREATOR = new Parcelable.Creator<Video360Parcelable>(){

		    @Override
		    public Video360Parcelable createFromParcel(Parcel parcel) {
		        return new Video360Parcelable(parcel);
		    }

		    @Override
		    public Video360Parcelable[] newArray(int size) {
		        return new Video360Parcelable[size];
		    }
		};

		//return hashcode of object
		public int describeContents() {
		    return hashCode();
		}
	
}
