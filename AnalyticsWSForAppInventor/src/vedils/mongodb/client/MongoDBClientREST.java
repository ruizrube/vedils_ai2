package vedils.mongodb.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import vedils.beans.InsertBean;
import vedils.beans.InsertManyBean;
import vedils.beans.QueryBean;
import vedils.flink.client.FlinkClientDAO;
import vedils.utils.StatusMessagesCreator;

@Path("MongoDBClient")
public class MongoDBClientREST {
	
	/*
	 * 1)) REST operation to check server functionality
	 * URL: http://localhost:8080/AnalyticsWSForAppInventor/MongoDBClient/
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String statusMongoDBClientAPI() {
		return StatusMessagesCreator.create("0", "Connection established with the Analytics Web Service For App Inventor (MongoDB client).");
	}
	
	/*
	 * 2)) REST operation to insert data (JSON format) on MongoDB
	 * URL: http://localhost:8080/AnalyticsWSForAppInventor/MongoDBClient/insert/
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("insert")
	public String insert(InsertBean insertBean) {
		MongoDBClientDAO client = new MongoDBClientDAO();
		return client.insert(insertBean.database, insertBean.collection, insertBean.data);
	}
	
	/*
	 * 3)) REST operation to insert data (JSON format) on MongoDB
	 * URL: http://localhost:8080/AnalyticsWSForAppInventor/MongoDBClient/insertMany/
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("insertMany")
	public String insertMany(InsertManyBean insertBean) {
		MongoDBClientDAO client = new MongoDBClientDAO();
		return client.insertMany(insertBean.database, insertBean.collection, insertBean.data);
	}
	
	/*
	 * 4)) REST operation to insert data (JSON format) on MongoDB and Kafka queue
	 * URL: http://localhost:8080/AnalyticsWSForAppInventor/MongoDBClient/insertWithStream
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("insertWithStream")
	public String insertWithStream(InsertBean insertBean) {
		MongoDBClientDAO clientMongoDB = new MongoDBClientDAO();
		FlinkClientDAO clientKafka = new FlinkClientDAO();
		String reponse = "";
		try {
			reponse = "InsertMongoDBRequest: " + clientMongoDB.insert(insertBean.database, insertBean.collection, insertBean.data)
			+ ", AddToKafkaQueueRequest: " + clientKafka.addToKafkaQueue(insertBean.database, insertBean.data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return StatusMessagesCreator.create("0", reponse);
	}
	
	/*
	 * 5)) REST operation to get query result of MongoDB (result in CSV format)
	 * URL: http://localhost:8080/AnalyticsWSForAppInventor/MongoDBClient/query
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("query")
	public String query(QueryBean queryBean) {
		MongoDBClientDAO client = new MongoDBClientDAO();
		return client.query(queryBean.database, queryBean.collection, queryBean.selectFields,
				queryBean.filterFields, queryBean.groupByFields);
	}
	
	/*
	 * 6)) REST operation to download query result on CSV format
	 * URL: http://localhost:8080/AnalyticsWSForAppInventor/MongoDBClient/downloadCSV?dbName={a}&cName={b}
	 */
	@GET
	@Produces("text/csv")
	@Path("downloadCSV")
	public String downloadCSV(@QueryParam(value = "dbName") String databaseName, @QueryParam(value = "cName") String collectionName) {
		MongoDBClientDAO client = new MongoDBClientDAO();
		return client.getCollectionCSVData(databaseName, collectionName);
	}
}
