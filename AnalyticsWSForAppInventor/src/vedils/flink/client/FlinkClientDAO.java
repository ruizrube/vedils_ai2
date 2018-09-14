package vedils.flink.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.StandaloneClusterClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;
import org.apache.flink.streaming.connectors.kafka.Kafka010JsonTableSource;
import org.apache.flink.streaming.connectors.kafka.KafkaJsonTableSource;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.api.java.Tumble;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Properties;

@SuppressWarnings("deprecation")
public class FlinkClientDAO {
	
	private static Gson gson;
	protected StreamExecutionEnvironment env;
	
	private final String COUNT = "Count(";
	private final String MAX = "Maximum(";
	private final String MIN = "Minimum(";
	private final String AVG = "Average(";
	private final String SUM = "Sum(";
	
	private final static Logger LOGGER = Logger.getLogger(FlinkClientDAO.class.getName());
	
	public FlinkClientDAO() {
		//env = StreamExecutionEnvironment.createRemoteEnvironment("localhost", 6123, "webapps/AnalyticsWSForAppInventor/WEB-INF/AnalyticsWSForAppInventor-1.0.0.jar");		//environment = ExecutionEnvironment.createRemoteEnvironment("localhost", 6123, "target/VedilsAnalyticsWS-1.0.0.jar");
		env = StreamExecutionEnvironment.createRemoteEnvironment("localhost", 6123, "target/AnalyticsWSForAppInventor-1.0.0.jar");
		gson = new Gson();
		LOGGER.setLevel(Level.INFO);
	}
	

	/*
	 * 2)) REST operation to add message to Kafka queue
	 */
	
	public String addToKafkaQueue(String database, JsonNode data) throws Exception {
		
		LinkedHashMap<String, Object> jsonMap = gson.fromJson(data.toString(), 
				new TypeToken<LinkedHashMap<String, Object>>() {}.getType());
		
		String topic = database;
		String message = gson.toJson(jsonMap);
		
		LOGGER.info("Inserting on Kafka queue.. Topic: " + topic + ", message: " + message);
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "app_producer");
		props.put("key.serializer", StringSerializer.class.getName());
		props.put("value.serializer", StringSerializer.class.getName());
		//props.put("auto.offset.reset", "earliest");
		//props.put("partition.assignment.strategy", "roundrobin");
        //props.put("enable.auto.commit", "true");
		//props.put("acks", "all");
		//props.put("block.on.buffer.full", "true");
		
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
		
		try { 
			producer.send(new ProducerRecord<String, String>(topic, message));
		} catch(Exception e) {
			LOGGER.severe("Error on addToKafkaQueue method with message: " + e.getMessage());
		} finally {
			producer.close();
		}
		
		return "Done";
	}
	
	/*
	 * 3)) REST operation to send query to Flink
	 */
	
	protected String query(String idQuery, String timeQuery, String topic,
			String selectFields, String filterFields, String groupByFields) throws Exception {
		
		//Read from Kafka
		LOGGER.info("timeQuery param value = " + timeQuery);
		Integer time = Integer.valueOf(timeQuery);
		LOGGER.info("time for query obtained = " + time);
		
		LOGGER.info("Preparing query to Flink.. Query fields: timeQuery: " + timeQuery + ", topic: " + topic + ", selectFields: "
				+ selectFields + ", filterFields: " + filterFields + ", groupByFields: " + groupByFields);
		
		// The JSON field names and types
		ArrayList<Class<?>> fieldTypesList = new ArrayList<Class<?>>();
		String[] selectedFieldsList = selectFields.split(",");
		ArrayList<String> fieldsAdded = new ArrayList<String>();
		
		for(int i=0; i<selectedFieldsList.length; i++) {
			String fieldName = selectedFieldsList[i].replaceAll(" ", "")
					.replaceAll("Sum\\(", "").replaceAll("\\)", "")
					.replaceAll("Maximum\\(", "").replaceAll("Minimum\\(", "")
					.replaceAll("Average\\(", "").replaceAll("Count\\(", "");
			if(!fieldsAdded.contains(fieldName)) { //the field must be unique
				if(selectedFieldsList[i].contains(SUM) || selectedFieldsList[i].contains(MAX) ||
						selectedFieldsList[i].contains(MIN) || selectedFieldsList[i].contains(AVG)) {
					fieldTypesList.add(Double.class);
				} else if(selectedFieldsList[i].contains(COUNT)) {
					fieldTypesList.add(Long.class);
				} else {
					fieldTypesList.add(String.class);
				}
				fieldsAdded.add(fieldName);
			}
		}
		
		Class<?>[] fieldTypes = fieldTypesList.toArray(new Class[0]);
		String[] fieldNames = fieldsAdded.toArray(new String[0]);
		
		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", "localhost:9092");
		properties.setProperty("zookeeper.connect", "localhost:2181");
		properties.setProperty("group.id", "flink_consumer" + idQuery);
		
		KafkaJsonTableSource kafkaTableSource = new Kafka010JsonTableSource(
		    topic,
		    properties,
		    fieldNames,
		    fieldTypes);
		
		LOGGER.info("Preparing query to Flink.. fieldNames: " + Arrays.toString(fieldNames) + ", fieldTypes: " + Arrays.toString(fieldTypes));
		
		kafkaTableSource.setFailOnMissingField(false);
		
		//StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(); //for local environment
		StreamTableEnvironment tableEnv = TableEnvironment.getTableEnvironment(env);
		tableEnv.config().setNullCheck(true);
		
		String existingFields = "";
		boolean firstField = true;
		
		for(int i=0; i<fieldNames.length; i++) {
			if(!fieldNames[i].isEmpty()) {
				if(firstField) {
					existingFields = existingFields + fieldNames[i] + ".isNotNull";
				} else {
					existingFields = existingFields + " && " + fieldNames[i] + ".isNotNull";
				}
				firstField = false;
			}
		}
		
		env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
		//env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime); //For CEP
		//env.getConfig().setAutoWatermarkInterval(time * 1000); //For CEP
		
		tableEnv.registerTableSource("kafka-source", kafkaTableSource);
		Table firstResult = tableEnv.ingest("kafka-source");
		Table result = firstResult.filter(existingFields).select("*");
		
		//Set windows query
		String groupByFieldsAux = selectFields + ", w";
		
		//Prepare query
		Table select = null;
		
		if(groupByFields.equals("")) {
			LOGGER.info("Launching Select query with repetitions each " + time + " seconds...");
			
			if(filterFields.equals("")) {
				select = result.window(Tumble.over(time + ".seconds").as("w")).groupBy(groupByFieldsAux).select(selectFields);
			} else {
				select = result.window(Tumble.over(time + ".seconds").as("w")).groupBy(groupByFieldsAux).select(selectFields).where(filterFields);
			}
		} else {	
			String aggregateFields = "";
			boolean first = true;
			String previous = "";
			String sumField = "";
			
			for(String field: selectFields.split(",")) {
				if(field.contains(COUNT) && !previous.isEmpty()) {
					if(first) {
						aggregateFields = aggregateFields + previous + ".count";
					} else {
						aggregateFields = aggregateFields + ", " + previous + ".count";
					}
				} else if(field.contains(SUM)) {
					sumField = field.replace("Sum(", "").replace(")", "");
					if(first) {
						aggregateFields = aggregateFields + sumField + ".sum";
					} else {
						aggregateFields = aggregateFields + ", " + sumField + ".sum";
					}
				} else if(field.contains(MAX)) {
					sumField = field.replace("Maximum(", "").replace(")", "");
					if(first) {
						aggregateFields = aggregateFields + sumField + ".max";
					} else {
						aggregateFields = aggregateFields + ", " + sumField + ".max";
					}
				} else if(field.contains(MIN)) {
					sumField = field.replace("Minimum(", "").replace(")", "");
					if(first) {
						aggregateFields = aggregateFields + sumField + ".min";
					} else {
						aggregateFields = aggregateFields + ", " + sumField + ".min";
					}
				} else if(field.contains(AVG)) {
					sumField = field.replace("Average(", "").replace(")", "");
					if(first) {
						aggregateFields = aggregateFields + sumField + ".avg";
					} else {
						aggregateFields = aggregateFields + ", " + sumField + ".avg";
					}
				} else {
					if(first) {
						aggregateFields = aggregateFields + field;
					} else {
						aggregateFields = aggregateFields + ", " + field;
					}
				}
				previous = field;
				first = false;
			}
			
			first = true;	
			
			groupByFieldsAux = groupByFields + ", w";
			
			LOGGER.info("Launching GroupBy query with repetitions each " + time + " seconds...");
			
			if(filterFields.equals("")) {
				select = result.window(Tumble.over(time + ".seconds").as("w")).groupBy(groupByFieldsAux).select(aggregateFields);
			} else {
				select = result.window(Tumble.over(time + ".seconds").as("w")).groupBy(groupByFieldsAux).select(aggregateFields).where(filterFields);
			}
		}
		
		//Generic types		
		TypeInformation<Tuple> type = TypeExtractor.getForObject(getTupleWithFieldsNumber(selectFields));
		DataStream<Tuple> resultDataStream = tableEnv.toDataStream(select, type);
		SingleOutputStreamOperator<String> resultList = resultDataStream.map(new TransformToText<Tuple>());
		
		//Write to Kafka
		topic = topic + "_" + idQuery;
		LOGGER.info("Writing reponse on Kafka with topic: "+topic);
				
		properties.setProperty("group.id", "flink_producer");
		resultList.addSink(new FlinkKafkaProducer010<>(topic, new SimpleStringSchema(), properties));		
		env.execute(topic);
		
		return "Done";
	}
	
	private class TransformToText<T> implements MapFunction<T, String> {
		private static final long serialVersionUID = 5606875044293566383L;

		@Override
		public String map(T value) throws Exception {
			return value.toString();
		}
	}
	
	private Tuple getTupleWithFieldsNumber(String fields) {
		Tuple tuple = null;
		String[] fieldsList = fields.split(",");
		try {
			tuple = Tuple.getTupleClass(fieldsList.length).newInstance();
			int i = 0;
			for(String field: fieldsList) {
				if(field.contains(SUM) || field.contains(MAX) || 
						field.contains(MIN) || field.contains(AVG)) {
					tuple.setField(0D, i);
				} else if(field.contains(COUNT)) {
					tuple.setField(0L, i);
				} else {
					tuple.setField("", i);
				}
				i++;
			}
		} catch(Exception e) {
			LOGGER.severe("Error on getTupleWithFieldsNumber method with message: " + e.getMessage());
		}
		return tuple;
	}
	
	/*
	 * 4)) REST operation to stop query from Flink
	 */
	
	protected String stopQuery(String idQuery) throws Exception {
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet("http://localhost:8081/joboverview/running");
	
		HttpResponse response = httpClient.execute(getRequest);
		StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
        
        String line = "";
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        String reponse = sb.toString();
        
        LOGGER.info("StopQuery method reponse: " +reponse);
        
        JSONObject json = new JSONObject(reponse);
        JSONArray jsonArray = json.getJSONArray("jobs");
        
        for(int i=0; i<jsonArray.length(); i++) {
        	if(jsonArray.getJSONObject(i).getString("name").equals(idQuery)) {
        		Configuration configuration = new Configuration();
        		configuration.setString("jobmanager.rpc.address", "localhost");
        		configuration.setString("jobmanager.rpc.port", "6123");
        		ClusterClient client = new StandaloneClusterClient(configuration); 
        		JobID jobID = JobID.fromHexString(jsonArray.getJSONObject(i).getString("jid"));
        		
        		try {
        			client.cancel(jobID);
        			httpClient.close();
        		} catch(Exception e) {
        			LOGGER.severe("StopQuery method error with message: " +e.getMessage());
        		}
        	}
        }
		return reponse;
	}
	
	/*
	 * 5)) REST operation to read content of Kafka queue
	 */
	
	protected String readFromKafkaQueue(String idQuery, String topic) throws Exception {
		
		String usedTopic = topic + "_" + idQuery;
		LOGGER.info("readFromKafkaQueue method, topic used: " +usedTopic);
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "android_consumer");
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", StringDeserializer.class.getName());
		//props.put("auto.offset.reset", "earliest");
		//props.put("partition.assignment.strategy", "roundrobin");
        //props.put("enable.auto.commit", "true");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		
		consumer.subscribe(Arrays.asList(usedTopic)); 
		
		String csv = "";
		
		try {
			ConsumerRecords<String, String> records = consumer.poll(1000);
			for (ConsumerRecord<String, String> record : records) {
				System.out.println(record.offset() + ": " + record.value());
				csv = csv + record.value().replaceAll("\\(", "").replaceAll("\\)", "") + "\n";
			}
		} catch(Exception e) {
			LOGGER.severe("readFromKafkaQueue method error with message: " +e.getMessage());
		} finally {
			consumer.close();
		}
		
		return csv;
	}
}