package vedils.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamQueryBean {
	
	@JsonProperty("idQuery")
	public String idQuery;
	
	@JsonProperty("timeQuery")
	public String timeQuery;
	
	@JsonProperty("topic")
	public String topic;
	
	@JsonProperty("selectFields")
	public String selectFields;
	
	@JsonProperty("filterFields")
	public String filterFields;
	
	@JsonProperty("groupByFields")
	public String groupByFields;
}