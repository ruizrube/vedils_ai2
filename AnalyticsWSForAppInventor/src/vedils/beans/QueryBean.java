package vedils.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryBean {
	
	@JsonProperty("database")
	public String database;
	
	@JsonProperty("collection")
	public String collection;
	
	@JsonProperty("selectFields")
	public String selectFields;
	
	@JsonProperty("filterFields")
	public String filterFields;
	
	@JsonProperty("groupByFields")
	public String groupByFields;
}