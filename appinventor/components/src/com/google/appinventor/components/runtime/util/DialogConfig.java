package com.google.appinventor.components.runtime.util;

import java.util.HashMap;
import java.util.Map;

public class DialogConfig {
	
	public static final Map<String, DialogLanguageConfig> languages = new HashMap<String, DialogLanguageConfig>();

	static {
		languages.put("en", new DialogLanguageConfig("en", "a11ea1d839e3446d84e402cb97cdadfb"));
		languages.put("ru", new DialogLanguageConfig("ru", "c8acebfbeeaa42ccb986e30573509055"));
		languages.put("de", new DialogLanguageConfig("de", "ae2afb2dfd3f4a02bb0da9dd32b78ff6"));
		languages.put("pt", new DialogLanguageConfig("pt", "b27372e24ee44db48df4dccbd57ea021"));
		languages.put("pt-BR", new DialogLanguageConfig("pt-BR", "a4e08b5bc87a41098237e3f23a5e1351"));
		languages.put("es", new DialogLanguageConfig("es", "49be4c10b6a543dfb41d49d88731bd49"));
		languages.put("fr", new DialogLanguageConfig("fr", "62161233bc094a75b3acfe16aeeed203"));
		languages.put("it", new DialogLanguageConfig("it", "57f80c9c9a2b4e0eae1739349a46e342"));
		languages.put("ja", new DialogLanguageConfig("ja", "b92617a3f82e4b52b3db44436d2d4b8b"));
		languages.put("ko", new DialogLanguageConfig("ko", "447a595234d74561a76b669a88ab3d99"));
		languages.put("zh-CN", new DialogLanguageConfig("zh-CN", "52d2b2bd992749409fc3a7d0605c3db4"));
		languages.put("zh-HK", new DialogLanguageConfig("zh-HK", "760c7a5efe5d43b9a90d62f73251de6a"));
		languages.put("zh-TW", new DialogLanguageConfig("zh-TW", "9cadea114425436cbaeaa504ea56555b"));
	}

	public static final String[] events = new String[] { "hello_event", "goodbye_event", "how_are_you_event" };
}