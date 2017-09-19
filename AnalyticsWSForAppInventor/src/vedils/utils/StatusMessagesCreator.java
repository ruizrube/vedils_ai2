package vedils.utils;

import org.json.JSONObject;

public class StatusMessagesCreator {
	
	public static String create(String code, String message) {
		JSONObject jsonMessage = new JSONObject();
		jsonMessage.put("code", code);
		jsonMessage.put("message", message);
		return jsonMessage.toString();
	}
}
