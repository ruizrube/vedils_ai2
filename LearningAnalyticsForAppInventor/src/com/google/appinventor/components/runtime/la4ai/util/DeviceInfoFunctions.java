package com.google.appinventor.components.runtime.la4ai.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

public class DeviceInfoFunctions {
	
	private static String TAG = "getMobileIP() method exception.";
	
	/**
	 * @param communicationMode
	 * @param context
	 * @return the current IP in function of select communicationMode.
	 */
	public static String getCurrentIP(int communicationMode, Context context) {
		if(communicationMode == 0) { //ONLY_WIFI
			return getWifiIP(context);
		} else { //INDIFFERENT
			if(getWifiIP(context).equals("0.0.0.0")) {
				return getMobileIP();
			} else {
				return getWifiIP(context);
			}
		}
	}
	
	/**
	 * @param context
	 * @return the MAC address of the use mobile phone.
	 */
	public static String getMAC(Context context) {
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wm.getConnectionInfo().getMacAddress();
	}
	
	/**
	 * 
	 * @param context
	 * @return the IMEI code of the use mobile phone.
	 */
	public static String getIMEI(Context context) {
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return manager.getDeviceId();
	}
	
	/**
	 * @return the current IP of 3G/4G connection.
	 */
	private static String getMobileIP() {
	  String ipAddress = "0.0.0.0";
	  try {
	    for (Enumeration<NetworkInterface> en = NetworkInterface
	    .getNetworkInterfaces(); en.hasMoreElements();) {
	       NetworkInterface intf = (NetworkInterface) en.nextElement();
	       for (Enumeration<InetAddress> enumIpAddr = intf
	          .getInetAddresses(); enumIpAddr.hasMoreElements();) {
	          InetAddress inetAddress = enumIpAddr.nextElement();
	          if (!inetAddress.isLoopbackAddress()) {
	             ipAddress = inetAddress .getHostAddress().toString();
	             return ipAddress.replaceAll("%rmnet0", ""); //Delete this fragment.
	          }
	       }
	    }
	  } catch (SocketException ex) {
	     Log.e(TAG, "Exception in Get IP Address: " + ex.toString());
	  }
	  return ipAddress;
	}
	
	/**
	 * @param context
	 * @return the current IP of Wifi connection.
	 */
	@SuppressWarnings("deprecation")
	private static String getWifiIP(Context context) {
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
	}
	
	
}
