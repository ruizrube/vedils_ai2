package vedils.flink.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import vedils.beans.InsertBean;
import vedils.beans.StreamQueryBean;
import vedils.utils.StatusMessagesCreator;

@Path("FlinkClient")
public class FlinkClientREST {
	
	/*
	 * 1)) REST operation to check server functionality
	 * URL: http://localhost:8080/AnalyticsWSForAppInventor/FlinkClient/
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String statusFlinkClientAPI() {
		return StatusMessagesCreator.create("0", "Connection established with the Analytics Web Service For App Inventor (Flink client).");
	}
	
	/*
	 * 2)) REST operation to add message to Kafka queue
	 * URL: http://localhost:8080/AnalyticsWSForAppInventor/FlinkClient/addToKafkaQueue/
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("addToKafkaQueue")
	public String addToKafkaQueue(InsertBean insertBean) throws Exception {
		FlinkClientDAO client = new FlinkClientDAO();
		return StatusMessagesCreator.create("0", client.addToKafkaQueue(insertBean.database, insertBean.data));
	}
	
	/*
	 * 3)) REST operation to send query to Flink
	 * URL: http://localhost:8080/AnalyticsWSForAppInventor/FlinkClient/query/
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("query")
	public String query(StreamQueryBean streamQueryBean) throws Exception {
		FlinkClientDAO client = new FlinkClientDAO();
		return client.query(streamQueryBean.idQuery, streamQueryBean.timeQuery, streamQueryBean.topic,
				streamQueryBean.selectFields, streamQueryBean.filterFields, streamQueryBean.groupByFields);
	}
	
	
	/*
	 * 4)) REST operation to stop query from Flink
	 * URL: http://localhost:8080/AnalyticsWSForAppInventor/FlinkClient/stopQuery/
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("stopQuery")
	public String stopQuery(StreamQueryBean streamQueryBean) throws Exception {
		FlinkClientDAO client = new FlinkClientDAO();
		return client.stopQuery(streamQueryBean.idQuery);
	}
	
	/*
	 * 5)) REST operation to read content of Kafka queue
	 * URL: http://localhost:8080/AnalyticsWSForAppInventor/FlinkClient/readFromKafkaQueue/
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("readFromKafkaQueue")
	public String readFromKafkaQueue(StreamQueryBean streamQueryBean) throws Exception {
		FlinkClientDAO client = new FlinkClientDAO();
		return client.readFromKafkaQueue(streamQueryBean.idQuery, streamQueryBean.topic);
	}
}
