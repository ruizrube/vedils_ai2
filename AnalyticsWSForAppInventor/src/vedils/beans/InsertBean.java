package vedils.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertBean {
	
	@JsonProperty("database")
	public String database;
	
	@JsonProperty("collection")
	public String collection;
	
	@JsonProperty("data")
	public JsonNode data;
}
