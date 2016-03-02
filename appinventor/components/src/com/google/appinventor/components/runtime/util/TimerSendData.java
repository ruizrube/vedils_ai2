package com.google.appinventor.components.runtime.util;

import java.util.List;
import java.util.TimerTask;

import com.google.appinventor.components.runtime.TinyDB;

public class TimerSendData extends TimerTask {
	
	private TinyDB tinyDB;
	private FusionTablesConnection fusionTablesConnection;
	private String ip;
	private String tableId;
	
	public TimerSendData(TinyDB tinyDB, FusionTablesConnection fusionTablesConnection, String ip, String tableId) {
		this.tinyDB = tinyDB;
		this.fusionTablesConnection = fusionTablesConnection;
		this.ip = ip;
		this.tableId = tableId;
	}
	
	public void updateIP(String currentIp) {
		this.ip = currentIp;
	}
	
	public TinyDB updateTinyDB() {
		return this.tinyDB;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		List<String> listTags = (List<String>) tinyDB.GetTags();
		
		for(String tagAux: listTags) {
			//Check the connection if it changes
			if(fusionTablesConnection.internetAccess()) {
				fusionTablesConnection.insertRow(tinyDB.GetValue(tagAux, "").toString().replaceAll("0.0.0.0", this.ip), this.tableId);
				tinyDB.ClearTag(tagAux);
			}
		}
	}

}
