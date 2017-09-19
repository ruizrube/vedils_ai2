package vedils.mongodb.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import vedils.utils.StatusMessagesCreator;

public class MongoDBClientDAO {
	
	protected static MongoClient connection;
	private static Gson gson;
	
	protected MongoDBClientDAO() {
		if(connection == null) {
			System.out.println("Estoy creando la conexión");
			MongoClientOptions options = MongoClientOptions.builder().socketKeepAlive(true).build();
			connection = new MongoClient("localhost:27017", options);
		}
		if(gson == null) {
			System.out.println("Estoy creando el gson");
			gson = new Gson();
		}
	}
	
	/*
	 * 2)) Operation to insert data (JSON format) on MongoDB
	 */
	
	//{ "_id" : ObjectId("59542dc99785c3250098f4f0"), "UserID" : "Tatiana", "IP" : "192.168.1.4", "MAC" : "42:40:A7:58:81:F2", "IMEI" : "354188073617271", "APILevel" : 23, "Latitude" : 36.51033666666667, "Longitude" : -6.272811666666667, "Date" : "06/29/2017 00:29:28", "AppID" : "appinventor.ai_tatyperson22.KafkaAndFlinkTesting", "ScreenID" : "Screen1", "ComponentType" : "Button", "ComponentID" : "Button1", "ActionType" : "Event", "ActionID" : "Click", "click" : "" }
	
	protected String insert(String database, String collection, JsonNode data) {
		System.out.println("Insert on MongoDB input: " + data.toString());
		System.out.println("database: " + database + ", collection: " + collection);
		
		//LinkedHashMap to maintain the items order
		LinkedHashMap<String, Object> dataMap = gson.fromJson(data.toString(), 
				new TypeToken<LinkedHashMap<String, Object>>() {}.getType()); 
		
		MongoDatabase db = connection.getDatabase(database.replaceAll("\\.", "_"));
		MongoCollection<Document> mongoDBCollection = db.getCollection(collection);
		
		Document document = new Document();
		document.putAll(dataMap);
		
		System.out.println("Insert on MongoDB output: " + document.toJson());
		mongoDBCollection.insertOne(document);
		return StatusMessagesCreator.create("0", "Data stored correctly");
	}
	
	/*
	 * 3)) Operation to insert data (JSON format) on MongoDB
	 */
	
	protected String insertMany(String database, String collection, String data) {
		System.out.println("Insert on MongoDB input: " + data.toString());
		System.out.println("database: " + database + ", collection: " + collection);
		
		List<Document> documents = new ArrayList<Document>();
		String[] jsonData = data.toString().split(";");
		
		for(String jsonElement: jsonData) {
			System.out.println("Insert on MongoDB output: " +jsonElement);
			LinkedHashMap<String, Object> jsonMapAux = gson.fromJson(jsonElement,  
					new TypeToken<LinkedHashMap<String, Object>>() {}.getType());
			Document document = new Document();
			document.putAll(jsonMapAux);
			documents.add(document);
		}
		
		MongoDatabase db = connection.getDatabase(database.replaceAll("\\.", "_"));
		MongoCollection<Document> mongoDBCollection = db.getCollection(collection);
		mongoDBCollection.insertMany(documents);
		return StatusMessagesCreator.create("0", "Data stored correctly");
	}
	
	/*
	 * 5)) REST operation to get query result of MongoDB (result in CSV format)
	 */
	
	protected String query(String database, String collection, String selectFields,
			String filterFields, String groupByFields) {
		MongoDatabase db = connection.getDatabase(database.replaceAll("\\.", "_"));
		MongoCollection<Document> mongoDBCollection = db.getCollection(collection);
		
		//Process filters (check OR operators)
		String filtersString = filterFields.replace("{", "").replace("}", "");
		String[] filters = filtersString.split(",");
		ArrayList<String> filtersList = new ArrayList<String>(Arrays.asList(filters));
		List<String> keys = Arrays.asList("ScreenID", "ComponentID", "ActionID",
				"ComponentType", "ActionType");
		
		BasicDBObject orFilters = new BasicDBObject();
		BasicDBList or = new BasicDBList();
		
		for(String key: keys) {
			if(StringUtils.countOccurrencesOf(filterFields, key) > 1) { //If more than 1 then is a OR condition
				for(String filterStringKey: filters) {
					if(filterStringKey.contains(key)) {
						filtersList.remove(filterStringKey);
						DBObject clause = (BasicDBObject) JSON.parse("{" + filterStringKey + "}");
						or.add(clause);
					}
				}
			}
		}
		
		if(!or.isEmpty()) {
			orFilters.put("$or", or);
		}
		
		String processedFilters = filtersList.toString().replace("[", "").replace("]", "")
	            .replace(", ", ",").replace(",,", ",");
		
		System.out.println("processedFilters = " + processedFilters);
		
		BasicDBObject filter = (BasicDBObject) JSON.parse("{" + processedFilters + "}");
		filter.putAll(orFilters.toMap());
		BasicDBObject select = (BasicDBObject) JSON.parse(selectFields);
		
		System.out.println("selectFields = " + selectFields);
		System.out.println("filterFields = " + filterFields);
		System.out.println("groupByFields = " + groupByFields);
		System.out.println("filter = " + filter.toString());
		
		String result = "";
		
		if(groupByFields.isEmpty() && !checkAggregatedFields(selectFields)) {
			result = executeSimpleQuery(mongoDBCollection, select, filter, selectFields);
		} else {
			result = executeAggregationQuery(mongoDBCollection, select, filter, groupByFields);
		}
		
		System.out.println("result = " + result);
		
		return result;
	}
	
	private String executeSimpleQuery(MongoCollection<Document> collection, BasicDBObject select, BasicDBObject filter, String selectedFields) {
		String result = "";
		
		FindIterable<Document> cursor = collection.find(filter).projection(select);

		boolean first = true;
		
		LinkedHashMap<String, Object> jsonMap =  gson.fromJson(selectedFields,  new TypeToken<LinkedHashMap<String, Object>>() {}.getType());
		
		for(String column: jsonMap.keySet()) {
			if(!column.equals("_id")) {
				if(first) {
					result = result + column;
				} else {
					result = result + "," + column;
				}
			}
			first = false;
		}
		
		result = result + "\n";
		result = result + collectResultValues(cursor, new ArrayList<String>(jsonMap.keySet()));
		
		return result;
	}
	
	private String executeAggregationQuery(MongoCollection<Document> collection, BasicDBObject select, BasicDBObject filter, String groupByFields) {
		String result = "";
		
		String[] groupByKeys = groupByFields.split(",");
		
		String columns = "";
		DBObject groupBy = new BasicDBObject();
		
		//First, add the groupBy fields
		for(String groupByKey: groupByKeys) {
			if(!groupByKey.isEmpty()) {
				columns = columns + groupByKey + ",";
				groupBy.put(groupByKey, "$" + groupByKey);
			}
		}
		
		DBObject groupFields = new BasicDBObject("_id", groupBy);
		
		System.out.println("Group by keys: " + groupFields.toString());
		
		//Second, add the metrics
		int i = 1;
		
		for(String metric: select.keySet()) {
			if(metric.contains("Count(")) {
				if(metric.equals("Count()")) {
					groupFields.put("count()", new BasicDBObject("$sum", 1));
					columns = columns + "count()";
				} else {
					String field = metric.replace("Count(", "").replace(")", "");
					groupFields.put("count(" + field + ")", new BasicDBObject("$sum", 1));
					columns = columns + "count(" + field + ")";
				}
				
				if(i < select.keySet().size()) {
					columns = columns + ",";
				}
				
			} else if(metric.contains("Maximum(")) {
				String field = metric.replace("Maximum(", "").replace(")", "");
				groupFields.put("max("+ field + ")", new BasicDBObject("$max", "$" + metric.replace("Maximum(", "").replace(")", "")));
				columns = columns + "max("+ field + ")";
				
				if(i < select.keySet().size()) {
					columns = columns + ",";
				}
				
			} else if(metric.contains("Minimum(")) {
				String field = metric.replace("Minimum(", "").replace(")", "");
				groupFields.put("min(" + field + ")", new BasicDBObject("$min", "$" + metric.replace("Minimum(", "").replace(")", "")));
				columns = columns + "min(" + field + ")";
				
				if(i < select.keySet().size()) {
					columns = columns + ",";
				}
				
			} else if(metric.contains("Sum(")) {
				String field = metric.replace("Sum(", "").replace(")", "");
				groupFields.put("sum(" + field + ")", new BasicDBObject("$sum", "$" + metric.replace("Sum(", "").replace(")", "")));
				columns = columns + "sum(" + field + ")";
				
				if(i < select.keySet().size()) {
					columns = columns + ",";
				}
				
			} else if(metric.contains("Average(")) {
				String field = metric.replace("Average(", "").replace(")", "");
				groupFields.put("avg(" + field + ")", new BasicDBObject("$avg", "$" + metric.replace("Average(", "").replace(")", "")));
				columns = columns + "avg(" + field + ")";
				
				if(i < select.keySet().size()) {
					columns = columns + ",";
				}
			}
			i++;
		}
		
		AggregateIterable<Document> cursor = collection.aggregate(Arrays.asList(
				new Document("$match", filter),
				new Document("$group", groupFields)));
		
		//Finally, add the values
		if(cursor.first() != null) { //Not empty response
			System.out.println("Documents = " + cursor.first().toJson());
			System.out.println("Count Query = " + groupFields.toString());
		
			result = result + columns + "\n";
			result = result + collectResultValues(cursor, Arrays.asList(groupByKeys));
		}
		
		return result;
	}
	
	private String collectResultValues(Iterable<Document> cursor, List<String> fields) {
		String results = "";
		
		for(Document document: cursor) {
			
			int i = 1;
			for(Object value: document.values()) {
				String fieldProccesed = value.toString().replace("Document", "").replace("{", "").replace("}", "");
				
				for(String field: fields) {
					fieldProccesed = fieldProccesed.replace(field + "=", "");
				}
				
				results = results + fieldProccesed;
				
				if(i < document.values().size() && !fieldProccesed.isEmpty()) {
					results = results + ",";
				} else if(!fieldProccesed.isEmpty()) {
					results = results + " \n";
				}		
				i++;
			}
		}
		return results;
	}
	
	private boolean checkAggregatedFields(String fields) {
		boolean check = false;
		List<String> aggregators = Arrays.asList("Count(", "COUNT(", "Maximum(",
				"Minimum(", "Sum(", "Average(");
		
		for(String aggregator: aggregators) {
			if(fields.contains(aggregator)) {
				check = true;
			}
		}
		return check;
	}
	
	/*
	 * 6)) REST operation to download query result on CSV format
	 */
	
	protected String getCollectionCSVData(String databaseName, String collectionName) {
		MongoDatabase db = connection.getDatabase(databaseName);
		MongoCollection<Document> collection = db.getCollection(collectionName);
		FindIterable<Document> cursor = collection.find();
		String csv = "";
		
		//First, get column names
		
		if(cursor.first() != null) {
			LinkedHashMap<String, Object> first =  gson.fromJson(cursor.first().toJson(),  new TypeToken<LinkedHashMap<String, Object>>() {}.getType());
			Set<String> names = first.keySet();
			names.remove("_id");
			int i = 0;
			
			for(String columnName: names) {
				if(i != 0) {
					csv = csv + "," + columnName;
				} else {
					csv = csv + columnName;
				}
				i++;
			}
			
			csv = csv + "\n";
			
			//Finally, get column values
			for(Document document: cursor) {
				LinkedHashMap<String, Object> documentJson =  gson.fromJson(document.toJson(),  new TypeToken<LinkedHashMap<String, Object>>() {}.getType());
				
				i = 0;
				
				for(String columnName: names) {
					if(i != 0) {
						csv = csv + "," + documentJson.get(columnName);
					} else {
						csv = csv + documentJson.get(columnName);
					}
					i++;
				}
				
				csv = csv + "\n";
			}
		}
		
		return csv;
	}
}