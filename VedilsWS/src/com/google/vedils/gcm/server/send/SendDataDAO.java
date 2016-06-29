package com.google.vedils.gcm.server.send;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.vedils.gcm.server.connection.ConnectionDAO;

public class SendDataDAO extends ConnectionDAO {
	
	protected SendDataDAO() {
		super();
	}
	
	/**
	 * Example format JSON accepted: 	{
  								"message":"hello world",
  								"imei":"353255067815897", 
  								"appname":"appinventor.ai_tatyperson22.TestGCM"
								}
	 * @param sendTextMessage
	 * @return
	 */
	protected String sendTextMessage(SendTextMessageBean sendTextMessage) {
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM RegistrationClients WHERE imei = '"+sendTextMessage.imei+"'");
			if(ps.executeQuery().next()) { //Client exists in DB
				PreparedStatement psAux = connection.prepareStatement("SELECT token FROM RegistrationClients WHERE imei != '"+sendTextMessage.imei+"' AND appname = '"+sendTextMessage.appname+"'");
				ResultSet clients = psAux.executeQuery();
				while(clients.next()) {
					//Send message to all clients.
					try {
						Sender sender = new Sender(apiKey);
						Message message = new Message.Builder()
						    .addData("message", sendTextMessage.message)
						    .addData("action", sendTextMessage.action)
						    .build();
						Result result = sender.send(message, clients.getString("token"), 2);
						System.out.println("Result Code to send message GCM: " +result.getErrorCodeName());
					} catch(IOException e) {
						System.out.println("Any error when try send message");
						e.printStackTrace();
					}
				}
				closeConnection();
				return ESTABLISHED_CONNECTION;
			} else {
				System.out.println("Client IMEI not exists in DB");
				closeConnection();
				return ESTABLISHED_CONNECTION;
			}
		} catch(SQLException e) {
			// TODO Auto-generated catch block
			logger.debug(NOT_ESTABLISHED_CONNECTION);
			e.printStackTrace();
			return NOT_ESTABLISHED_CONNECTION;
		}
	}
}
