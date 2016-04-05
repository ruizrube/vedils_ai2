package com.google.appinventor.components.runtime.la4ai.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;

public class TinyDB {
	
	private SharedPreferences sharedPreferences;
	@SuppressWarnings("unused")
	private Context context;
	
	public TinyDB(Context context) {
	    this.context = context;
	    sharedPreferences = context.getSharedPreferences("TinyDB1", Context.MODE_PRIVATE);
   }
	
	
	public void StoreValue(final String tag, final Object valueToStore) {
	    final SharedPreferences.Editor sharedPrefsEditor = sharedPreferences.edit();
	    try {
	      sharedPrefsEditor.putString(tag, JsonUtil.getJsonRepresentation(valueToStore));
	      sharedPrefsEditor.commit();
	    } catch (JSONException e) {
	    	System.out.println("Error JsonException");
	      //throw new YailRuntimeError("Value failed to convert to JSON.", "JSON Creation Error.");
	    }
	  }
	
	
	public Object GetValue(final String tag, final Object valueIfTagNotThere) {
	    try {
	      String value = sharedPreferences.getString(tag, "");
	      // If there's no entry with tag as a key then return the empty string.
	      //    was  return (value.length() == 0) ? "" : JsonUtil.getObjectFromJson(value);
	      return (value.length() == 0) ? valueIfTagNotThere : JsonUtil.getObjectFromJson(value);
	    } catch (JSONException e) {
	      System.out.println("Error JsonException");
	      return null;
	      //throw new YailRuntimeError("Value failed to convert from JSON.", "JSON Creation Error.");
	    }
	  }
	
	
	public Object GetTags() {
	    List<String> keyList = new ArrayList<String>();
	    Map<String,?> keyValues = sharedPreferences.getAll();
	    // here is the simple way to get keys
	    keyList.addAll(keyValues.keySet());
	    java.util.Collections.sort(keyList);
	    return keyList;
	  }
	
	
	public void ClearAll() {
	    final SharedPreferences.Editor sharedPrefsEditor = sharedPreferences.edit();
	    sharedPrefsEditor.clear();
	    sharedPrefsEditor.commit();
    }
	
}
