package com.google.appinventor.components.runtime.util;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.appinventor.components.runtime.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/*import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;*/
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;

public class GoogleCloudMessagingListenerService extends GcmListenerService {
	
	
	/**
     * Function call when the message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String action = data.getString("action");
        System.out.println("From: " + from);
        System.out.println("Message: " + message);
        
        if(!action.contains("ObjectListGCM")) {
            //Send the message to the GoogleCloudMessaging component.
            GoogleCloudMessaging.handledReceivedMessage(message, action);
        } else {
        	System.out.println("Message Object: " + message);
            try {
            	Type listType = new TypeToken<ArrayList<Object>>(){}.getType();
            	Gson gson = new GsonBuilder()./*registerTypeAdapter(listType, ListObjectsDeserializer.class).*/create();
            	ArrayList<Object> objects = gson.fromJson(message, listType);
            	System.out.println("Size lista recuperada: " + objects.size() + " - GCMTest");
                GoogleCloudMessaging.handledDataListReceived(objects);
            } catch(Exception e) {
            	e.printStackTrace();
            }
        }
    }
    
    /*class ListObjectsDeserializer implements JsonDeserializer<ArrayList<Object>> {

		@Override
		public ArrayList<Object> deserialize(JsonElement json, Type type, JsonDeserializationContext context)
				throws JsonParseException {
			
			ArrayList<Object> objects = new ArrayList<Object>();
			
			JsonArray jsonArray = (JsonArray) json;
			for(JsonElement jsonElement: jsonArray) {
				JsonObject jsonObject = (JsonObject) jsonElement;
				String value = jsonObject.get("object").toString();
				String[] typeAndValue = value.split(":type:");
				objects.add(createObject(typeAndValue[0], typeAndValue[1]));
			}
			
			return objects;
		}
		
		private Object createObject(String value, String type) {
			if(type.equals("String")) {
				String object = value;
				return object;
			} else if(type.equals("Number")) {
				Integer object = Integer.valueOf(value);
				return object;
			} else {
				Boolean object = Boolean.valueOf(value);
				return object;
			}
		}
    	
    }*/
}
