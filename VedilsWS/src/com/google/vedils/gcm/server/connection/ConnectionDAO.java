package com.google.vedils.gcm.server.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public abstract class ConnectionDAO {
	
	protected Connection connection;
	protected String apiKey = "AIzaSyAuN6Q3iTniYPAsxCpC2elPc2nmVwjKnxo";
	protected Logger logger = Logger.getLogger(ConnectionDAO.class);
	protected final String ESTABLISHED_CONNECTION = "ESTABLISHED_CONNECTION";
	protected final String NOT_ESTABLISHED_CONNECTION = "NOT_ESTABLISHED_CONNECTION";
	
	protected ConnectionDAO() {
		BasicConfigurator.configure();
		
		String databaseURL = "jdbc:mysql://localhost:3306/VedilsGcmClients";
		String user = "root";
		String password = "root";
		
		//Open the connection with the database.
		
		try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(databaseURL, user, password);
            if (connection != null) {	
                logger.debug("Connected to the database");
            }
        } catch (ClassNotFoundException ex) {
            logger.debug("Could not find database driver class");
            ex.printStackTrace();
        } catch (SQLException ex) {
            logger.debug("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        }
		
	}
	
	protected void closeConnection() throws SQLException {
		logger.debug("Close connection to the database");
		connection.close();
	}
	
}
