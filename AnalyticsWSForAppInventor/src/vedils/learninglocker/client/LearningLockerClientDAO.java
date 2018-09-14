package vedils.learninglocker.client;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.util.StringUtils;

import com.google.common.collect.ImmutableMap;
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

public class LearningLockerClientDAO {
	
	protected static MongoClient connection;
	private static Gson gson;
	private static String learningLockerDB = "learninglocker_v2";
	private static String learningLockerStatements = "statements";
	private static String learningLockerClient = "client";
	
	static Map<String, String> XAPI_VEDILS_FIELDS;
	
	protected LearningLockerClientDAO() {
		
		if(XAPI_VEDILS_FIELDS == null) {
			System.out.println("Creando mapping de los campos..");
			
				XAPI_VEDILS_FIELDS = ImmutableMap.<String, String>builder()
						
					//-- UserInfo category --	
					.put("UserID", "statement.actor.name") 
					.put("IP", "statement.context.extensions.http://vedils&46;uca&46;es/xapi/context/appContext.http://vedils&46;uca&46;es/xapi/context/IP") 
					.put("MAC", "statement.context.extensions.http://vedils&46;uca&46;es/xapi/context/appContext.http://vedils&46;uca&46;es/xapi/context/MAC") 
					.put("IMEI", "statement.context.extensions.http://vedils&46;uca&46;es/xapi/context/appContext.http://vedils&46;uca&46;es/xapi/context/IMEI") 
					.put("Latitude", "statement.context.extensions.http://vedils&46;uca&46;es/xapi/context/appContext.http://vedils&46;uca&46;es/xapi/context/Latitude") 
					.put("Longitude", "statement.context.extensions.http://vedils&46;uca&46;es/xapi/context/appContext.http://vedils&46;uca&46;es/xapi/context/Longitude")
					
					//-- Context category --
					.put("Date", "statement.context.extensions.http://vedils&46;uca&46;es/xapi/context/appContext.http://vedils&46;uca&46;es/xapi/context/Date") 
					.put("AppID", "statement.context.extensions.http://vedils&46;uca&46;es/xapi/context/appContext.http://vedils&46;uca&46;es/xapi/context/AppID") 
					.put("ScreenID", "statement.context.extensions.http://vedils&46;uca&46;es/xapi/context/appContext.http://vedils&46;uca&46;es/xapi/context/ScreenID")
					.put("ComponentID", "statement.object.id")
					.put("ComponentType", "statement.object.definition.type")
					
					//-- Activity category --
					.put("ActionID", "statement.verb.id")
					.put("ActionType", "")
					
					//-- Specific xAPI-Fields (result) --
					.put("success", "statement.result.success")
					.put("scaled", "statement.result.score.scaled")
					.put("raw", "statement.result.score.raw")
					.put("min", "statement.result.score.min")
					.put("max", "statement.result.score.max")
					.put("duration", "statement.result.duration")
					.put("completion", "statement.result.completion")
					
					//-- Custom User-Fields -- 
					.put("ActivityExtensions", "statement.object.definition.extensions")
					.put("ResultExtensions", "statement.result.extensions")
					.build();
		}
		
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
	 * 2)) REST operation to get query result of MongoDB (result in CSV format)
	 */
	
	protected String query(String database, String collection, String selectFields,
			String filterFields, String groupByFields) {
		
		MongoDatabase db = connection.getDatabase(learningLockerDB);
		MongoCollection<Document> statementsCollection = db.getCollection(learningLockerStatements);
		String basicSecret = obtainBasicSecret(collection);
		String lrs = obtainLRS(basicSecret, db);
		
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
		
		processedFilters = replaceVedilsKeywordsForxAPIKeywords(processedFilters);
		//processedFilters = replaceUserSpecificKeywords(processedFilters);
				
		System.out.println("processedFilters = " + processedFilters);
		
		BasicDBObject filter = (BasicDBObject) JSON.parse("{" + processedFilters + "}");
		filter.putAll(orFilters.toMap());
		filter.put("client", new ObjectId(lrs));
		
		String xAPIselectFields = replaceVedilsKeywordsForxAPIKeywords(selectFields);
		//xAPIselectFields = replaceUserSpecificKeywords(xAPIselectFields);
		String xAPIgroupByFields = replaceVedilsKeywordsForxAPIKeywords(groupByFields);
		//xAPIgroupByFields = replaceUserSpecificKeywords(groupByFields);
		
		//BasicDBObject select = (BasicDBObject) JSON.parse(selectFields); com.mongodb.util.JSONParseException
		
		xAPIselectFields = xAPIselectFields.replace("{", "").replace("}", "")
				.replaceAll(":1", "").replaceAll(" ", "");
		List<String> xAPIselectFieldsList = Arrays.asList(xAPIselectFields.split(","));
		BasicDBObject select = new BasicDBObject();
		
		for(String selectItem: xAPIselectFieldsList) {
			select.put(selectItem, 1);
		}
		
		System.out.println("select = " + select.toJson());
		System.out.println("selectFields = " + selectFields);
		System.out.println("filterFields = " + filterFields);
		System.out.println("groupByFields = " + groupByFields);
		System.out.println("filter = " + filter.toString());
		
		String result = "";
		
		if(groupByFields.isEmpty() && !checkAggregatedFields(selectFields)) {
			result = executeSimpleQuery(statementsCollection, select, filter, selectFields, xAPIselectFieldsList);
		} else {
			result = executeAggregationQuery(statementsCollection, select, filter, groupByFields, xAPIgroupByFields);
		}
		
		System.out.println("result = " + result);
		
		return result;
	}
	
	private String executeSimpleQuery(MongoCollection<Document> collection, BasicDBObject select, BasicDBObject filter, String selectedFields, List<String> xAPIselectFieldsList) {
		String result = "";
		
		//exclude _id field
		select.put("_id", 0);
		
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
		result = result + collectResultValues(cursor, new ArrayList<String>(jsonMap.keySet()), xAPIselectFieldsList);
		
		return result;
	}
	
	private String executeAggregationQuery(MongoCollection<Document> collection, BasicDBObject select, BasicDBObject filter, String groupByFields, String xAPIgroupByFields) {
		String result = "";
		
		String[] groupByKeysxAPI = xAPIgroupByFields.split(",");
		String[] groupByKeys = groupByFields.split(",");
		
		String columns = "";
		DBObject groupBy = new BasicDBObject();
		
		//First, add the groupBy fields
		for(String groupByKey: groupByKeys) {
			if(!groupByKey.isEmpty()) {
				columns = columns + groupByKey + ",";
			}
		}
		
		for(String groupByKey: groupByKeysxAPI) {
			if(!groupByKey.isEmpty()) {
				groupBy.put(groupByKey.replaceAll("\\.", "="), "$" + groupByKey);
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
			result = result + collectResultValues(cursor, Arrays.asList(groupByFields), Arrays.asList(groupByKeysxAPI));
		}
		
		return result;
	}
	
	private String obtainBasicSecret(String authKey) {
		byte[] decoded = Base64.getDecoder().decode(authKey);
		String authDecoded = new String(decoded, StandardCharsets.UTF_8);
		String[] authParams = authDecoded.split(":");
		String basicKey = authParams[0];
		String basicSecret = authParams[1];
		
		System.out.println("basicKey = " + basicKey);
		System.out.println("basicSecret = " + basicSecret);
		
		return basicSecret;
	}
	
	private String obtainLRS(String basicSecret, MongoDatabase db) {
		MongoCollection<Document> clientCollection = db.getCollection(learningLockerClient);
		BasicDBObject filterByLRSCredential = (BasicDBObject) JSON.parse("{\"api.basic_secret\":\"" + basicSecret + "\"}");
		BasicDBObject selectLRS = (BasicDBObject) JSON.parse("{\"_id\":1}");
		FindIterable<Document> cursor = clientCollection.find(filterByLRSCredential).projection(selectLRS);
		String lrs = "";
		
		for(Document document: cursor) {
			for(Object value: document.values()) {
				System.out.println("value =" + value.toString());
				lrs = value.toString();
			}
		}
		
		return lrs;
	}
	
	private String replaceUserSpecificKeywords(String fields) {
		String fieldsProcess = fields.replace("{", "").replace("}", "")
				.replaceAll(":1", "").replaceAll(" ", "");
		List<String> fieldsList = Arrays.asList(fieldsProcess.split(","));
		
		for(String field: fieldsList) {
			String[] valuesField = field.split(":");
			if(!XAPI_VEDILS_FIELDS.keySet().contains(valuesField[0]) && 
					!valuesField[0].contains("\\.")) { //Field defined by the user
				System.out.println("UserField: " + valuesField[0]);
				fields = fields.replace(field, XAPI_VEDILS_FIELDS.get("ActivityExtensions") + ":" 
						+ "http://vedils&46;uca&46;es/xapi/" + valuesField[0]); //+ ", "
						//+ XAPI_VEDILS_FIELDS.get("ResultExtensions") +  "http://vedils&46;uca&46;es/xapi/" + field);
			}
		}
		
		return fields;
	}
	
	private String replaceVedilsKeywordsForxAPIKeywords(String fields) {
		fields = fields.replace("UserID", XAPI_VEDILS_FIELDS.get("UserID"))
				.replace("IP", XAPI_VEDILS_FIELDS.get("IP"))
				.replace("MAC", XAPI_VEDILS_FIELDS.get("MAC"))
				.replace("IMEI", XAPI_VEDILS_FIELDS.get("IMEI"))
				.replace("Latitude", XAPI_VEDILS_FIELDS.get("Latitude"))
				.replace("Longitude", XAPI_VEDILS_FIELDS.get("Longitude"))
				.replace("Date", XAPI_VEDILS_FIELDS.get("Date"))
				.replace("AppID", XAPI_VEDILS_FIELDS.get("AppID"))
				.replace("ScreenID", XAPI_VEDILS_FIELDS.get("ScreenID"))
				.replace("ComponentID", XAPI_VEDILS_FIELDS.get("ComponentID"))
				.replace("ComponentType", XAPI_VEDILS_FIELDS.get("ComponentType"))
				.replace("ActionID", XAPI_VEDILS_FIELDS.get("ActionID"))
				.replace("success", XAPI_VEDILS_FIELDS.get("success"))
				.replace("scaled", XAPI_VEDILS_FIELDS.get("scaled"))
				.replace("raw", XAPI_VEDILS_FIELDS.get("raw"))
				.replace("min", XAPI_VEDILS_FIELDS.get("min"))
				.replace("max", XAPI_VEDILS_FIELDS.get("max"))
				.replace("duration", XAPI_VEDILS_FIELDS.get("duration"))
				.replace("completion", XAPI_VEDILS_FIELDS.get("completion"));
		
		return fields;
	}
	
	private String collectResultValues(Iterable<Document> cursor, List<String> fields, List<String> xAPIselectFieldsList) {
		String results = "";
		
		for(Document document: cursor) {
			
			System.out.println("document = " + document.toJson());
			
			int i = 1;
			for(Object value: document.values()) {
				String fieldProccesed = value.toString().replace("Document", "").replace("{", "").replace("}", "")
						.replace("http://vedils.uca.es/xapi/activities/", "")
						.replace("http://vedils.uca.es/xapi/verbs/", "");
				System.out.println("fieldProcessed = " + fieldProccesed);
				
				for(String field: xAPIselectFieldsList) {
					field = field.replaceAll("\\.", "=");
					System.out.println("field = " + field);
					fieldProccesed = fieldProccesed.replace(field + "=", "");
					field = field.replace("statement=", "");
					fieldProccesed = fieldProccesed.replace(field + "=", "");
					field = field.replace("context=extensions=http://vedils&46;uca&46;es/xapi/context/appContext=", "")
							.replace("object=", "").replace("=id", "");
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
}