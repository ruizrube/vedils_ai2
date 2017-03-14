package com.google.appinventor.components.runtime.la4ai.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

public class DeviceInfoFunctions {
	
	private static String DEFAULT_MAC = "02:00:00:00:00:00";
	private static String DEFAULT_IP = "0.0.0.0";
	private static String TAG = "DeviceInfoFunctions methods.";
	
	/**
	 * @param communicationMode
	 * @param context
	 * @return the current IP in function of select communicationMode.
	 */
	public static String getCurrentIP(int communicationMode, Context context) {
		if(communicationMode == 0) { //ONLY_WIFI
			return getWifiIP(context);
		} else { //INDIFFERENT
			if(getWifiIP(context).equals(DEFAULT_IP)) {
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
		String macAddress = wm.getConnectionInfo().getMacAddress();
		if(!macAddress.equals(DEFAULT_MAC)) {
			return macAddress;
		} else { //Get the MAC address in Android 6.
			try {
				for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
					
					if(!networkInterface.getName().equalsIgnoreCase("wlan0")) {
						byte[] MACBytes = networkInterface.getHardwareAddress();
						
						if(MACBytes != null) {
							StringBuilder MACString = new StringBuilder();
							
							for (byte MACByte : MACBytes) {
				                MACString.append(String.format("%02X:", MACByte));
				            }
							
							if (MACString.length() > 0) {
								MACString.deleteCharAt(MACString.length() - 1);
				            }
							
							return MACString.toString();
						}
					}
				}
			} catch (SocketException e) {
				 Log.e(TAG, "Exception in Get MAC Address in Android 6: " + e.toString());
			}
			return DEFAULT_MAC;
		}
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
	  String ipAddress = DEFAULT_IP;
	  try {
	    for (Enumeration<NetworkInterface> en = NetworkInterface
	    .getNetworkInterfaces(); en.hasMoreElements();) {
	       NetworkInterface intf = (NetworkInterface) en.nextElement();
	       for (Enumeration<InetAddress> enumIpAddr = intf
	          .getInetAddresses(); enumIpAddr.hasMoreElements();) {
	          InetAddress inetAddress = enumIpAddr.nextElement();
	          if (!inetAddress.isLoopbackAddress() && !inetAddress.getHostAddress().toString().contains("dummy")) { //Filtering the localhost IP and dummy interfaces
	             ipAddress = inetAddress.getHostAddress().toString();
	             return ipAddress.replaceAll("%rmnet0", ""); //Delete this fragment.
	          }
	       }
	    }
	  } catch (SocketException e) {
	     Log.e(TAG, "Exception in Get IP Address: " + e.toString());
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
	
	
	/**
	 * @param context
	 * @return if the used device has internet connection.
	 */
	public static boolean checkInternetConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if(networkInfo == null) {
			return false;
		} else {
			return networkInfo.isConnected() && networkInfo.isAvailable();
		}
	}
	
	/**
	 * @return the Android version of the used device.
	 */
	public static int getAndroidAPIVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}
}
