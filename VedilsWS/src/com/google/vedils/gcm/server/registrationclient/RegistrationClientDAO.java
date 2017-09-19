package com.google.vedils.gcm.server.registrationclient;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.vedils.gcm.server.connection.ConnectionDAO;

public class RegistrationClientDAO extends ConnectionDAO {
	
	/*
	 * mysql> CREATE TABLE `RegistrationClients` ( `id` INT NOT NULL AUTO_INCREMENT, `imei` VARCHAR(20) NOT NULL, `token` VARCHAR(100) NOT NULL, `authtoken` VARCHAR(100) NOT NULL, `email` VARCHAR(40) NOT NULL, PRIMARY KEY (`id`));
	 */
	
	protected RegistrationClientDAO() {
		super();
	}
	
	/**
	 * Example format JSON accepted: 	{
  								"imei":"353255067815898",
  								"token":"test",
  								"appname":"appinventor.ai_tatyperson22.TestGCM"
								}
	 * @param registrationClient
	 * @return
	 */
	@SuppressWarnings("resource")
	protected String insertRegistrationClient(RegistrationClientBean registrationClient) {
		System.out.println("Hola/n");
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM RegistrationClients WHERE imei = '"+registrationClient.imei+"'");
			if(!ps.executeQuery().next()) { //New client.
				ps = connection.prepareStatement("INSERT INTO RegistrationClients (imei, token, appname) VALUES ('"+ registrationClient.imei +"','" +registrationClient.token + "','" + registrationClient.appname +"')");
				ps.executeUpdate();
				closeConnection();
				return ESTABLISHED_CONNECTION;
			} else { //Update Client
				ps = connection.prepareStatement("UPDATE RegistrationClients SET token = '"+registrationClient.token+"', appname = '"+registrationClient.appname+"' WHERE imei = '"+registrationClient.imei+"'");
				ps.executeUpdate();
				closeConnection();
				return ESTABLISHED_CONNECTION;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.debug(NOT_ESTABLISHED_CONNECTION);
			e.printStackTrace();
			return NOT_ESTABLISHED_CONNECTION;
		}
	}
	
	/**
	 * This method receives the IMEI of device that you have unregister.
	 * @param imei
	 * @return
	 */
	protected String deleteRegistrationClient(String imei) {
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM RegistrationClients WHERE imei = '"+imei+"'");
			ps.executeUpdate();
			closeConnection();
			return ESTABLISHED_CONNECTION;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.debug(NOT_ESTABLISHED_CONNECTION);
			e.printStackTrace();
			return NOT_ESTABLISHED_CONNECTION;
		}
	}
	
	/**
	 * This method receives the appName to unregister all clients.
	 * @param appName
	 * @return
	 */
	protected String deleteAllRegistrationClients(String appName) {
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM RegistrationClients WHERE appname = '"+appName+"'");
			ps.executeUpdate();
			closeConnection();
			return ESTABLISHED_CONNECTION;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.debug(NOT_ESTABLISHED_CONNECTION);
			e.printStackTrace();
			return NOT_ESTABLISHED_CONNECTION;
		}
	}
}
