package com.google.vedils.gcm.server.send;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.http.MediaType;

@Path("sendData")
public class SendDataREST {
	
	/*
	 * REST operation to check server functionality
	 * URL: http://localhost:8080/VedilsWS/GcmServer/sendData/
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN_VALUE)
	public String infoSendDataServer() {
		return "Send Data for GCM Server (Vedils)";
	}
	
	/*
	 * REST operation to send text message a all devices registered for an application
	 * URL: http://localhost:8080/VedilsWS/GcmServer/sendData/sendTextMessage
	 */
	@POST
	@Consumes("application/json")
	@Produces(MediaType.TEXT_PLAIN_VALUE)
	@Path("sendTextMessage")
	public String sendTextMessage(SendTextMessageBean sendTextMessage) {
		System.out.println("The message is: " +sendTextMessage.message);
		System.out.println("The action is: " +sendTextMessage.action);
		System.out.println("The imei_receiver is: " +sendTextMessage.imei_receiver); 			/***Edson****/
		SendDataDAO sendDataDAO = new SendDataDAO();
		return sendDataDAO.sendTextMessage(sendTextMessage);
	}
}
