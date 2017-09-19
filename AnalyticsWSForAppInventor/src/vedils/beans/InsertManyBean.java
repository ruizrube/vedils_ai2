package vedils.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InsertManyBean {
	
	@JsonProperty("database")
	public String database;
	
	@JsonProperty("collection")
	public String collection;
	
	@JsonProperty("data")
	public String data;
}
