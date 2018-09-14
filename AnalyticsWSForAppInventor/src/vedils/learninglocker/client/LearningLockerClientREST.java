package vedils.learninglocker.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import vedils.beans.QueryBean;
import vedils.utils.StatusMessagesCreator;

@Path("LearningLockerClient")
public class LearningLockerClientREST {
	
	/*
	 * 1)) REST operation to check server functionality
	 * URL: http://localhost:8080/AnalyticsWSForAppInventor/LearningLockerClient/
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String statusLearningLockerClientAPI() {
		return StatusMessagesCreator.create("0", "Connection established with the Analytics Web Service For App Inventor (LearningLocker client).");
	}
	
	/*
	 * 2)) REST operation to get query result of LearningLocker (result in CSV format)
	 * URL: http://localhost:8080/AnalyticsWSForAppInventor/LearningLockerClient/query
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("query")
	public String query(QueryBean queryBean) {
		LearningLockerClientDAO client = new LearningLockerClientDAO();
		return client.query(queryBean.database, queryBean.collection, queryBean.selectFields,
				queryBean.filterFields, queryBean.groupByFields);
	}
}