package com.google.vedils.gcm.server.registrationclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.http.MediaType;

//ALTER TABLE RegistrationClients MODIFY authtoken VARCHAR(300);

@Path("registrationClient")
public class RegistrationClientREST {
	
	/*
	 * REST operation to check server functionality
	 * URL: http://localhost:8080/VedilsWS/GcmServer/registrationClient/
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN_VALUE)
	public String infoRegistrationClientServer() {
		return "Registration client for GCM Server (Vedils)";
	}
	
	/*
	 * REST operation to register client on GCM Server (Insert in MySQL BD).
	 * URL: http://localhost:8080/VedilsWS/GcmServer/registrationClient/insertRegistrationClient
	 */
	@POST
	@Consumes("application/json")
	@Produces(MediaType.TEXT_PLAIN_VALUE)
	@Path("insertRegistrationClient")
	public String insertRegistrationClient(RegistrationClientBean registrationClient) {
		RegistrationClientDAO registrationClientDAO = new RegistrationClientDAO();
		return registrationClientDAO.insertRegistrationClient(registrationClient);
	}
	
	/*
	 * REST operation to unregister client on GCM Server (Delete in MysqlDB)
	 * URL: http://localhost:8080/VedilsWS/GcmServer/registrationClient/deleteRegistrationClient
	 */
	@DELETE
	@Consumes(MediaType.TEXT_PLAIN_VALUE)
	@Produces(MediaType.TEXT_PLAIN_VALUE)
	@Path("deleteRegistrationClient")
	public String deleteRegistrationClient(String imei) {
		RegistrationClientDAO registrationClientDAO = new RegistrationClientDAO();
		return registrationClientDAO.deleteRegistrationClient(imei);
	}
	
	/*
	 * REST operation to unregister all clients (same application) on GCM Server (Delete in MysqlDB)
	 * URL: http://localhost:8080/VedilsWS/GcmServer/registrationClient/deleteAllRegistrationClients
	 */
	@DELETE
	@Consumes(MediaType.TEXT_PLAIN_VALUE)
	@Produces(MediaType.TEXT_PLAIN_VALUE)
	@Path("deleteAllRegistrationClients")
	public String deleteAllRegistrationClients(String appName) {
		RegistrationClientDAO registrationClientDAO = new RegistrationClientDAO();
		return registrationClientDAO.deleteAllRegistrationClients(appName);
	}
	
}
