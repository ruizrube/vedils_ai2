/**
 * 
 */
package com.google.appinventor.components.runtime.ld4ai.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.MonolingualTextValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonItemDocument;
import org.wikidata.wdtk.datamodel.json.jackson.JacksonTermedStatementDocument;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * @author ruizrube
 *
 */
public class WikiDataClient {

	static WikiDataClient instance;

	static WikibaseDataFetcher wbdf;

	
	private WikiDataClient() {

	}

	public static WikiDataClient getInstance() {
		if (instance == null) {
			instance = new WikiDataClient();
			wbdf = WikibaseDataFetcher.getWikidataDataFetcher();

		}
		return instance;
	}

	
	
	public ItemDocument obtainItemDocument(String entityId) {
		ItemDocument result = null;
		try {
			EntityDocument q = wbdf.getEntityDocument(entityId);

			if (q instanceof ItemDocument) {
				result = ((ItemDocument) q);
			}

		} catch (MediaWikiApiErrorException e) {

		}
		return result;
	}
	

	public Map<String, MonolingualTextValue> obtainLabelsDocument(String entityId) {
		Map<String, MonolingualTextValue> result=new HashMap<String, MonolingualTextValue>();
		try {
			EntityDocument q = wbdf.getEntityDocument(entityId);

			if (q instanceof PropertyDocument) {
				PropertyDocument aux = ((PropertyDocument) q);
				result=aux.getLabels();
			}
			if (q instanceof ItemDocument) {
				ItemDocument aux = ((ItemDocument) q);
				result=aux.getLabels();
			}
			

		} catch (MediaWikiApiErrorException e) {

		}
		return result;
	}

	


	public List<ItemDocument> classifyText(String title) {
		List<ItemDocument> result = new ArrayList<ItemDocument>();

		try {
			EntityDocument q = wbdf.getEntityDocumentByTitle("enwiki", title);
			if (q instanceof ItemDocument) {
				result.add((ItemDocument) q);
			}

		} catch (MediaWikiApiErrorException e) {

		}
		return result;
	}

	public ItemDocument LoadResource(String identifier) {
		ItemDocument obj=null;
		if (identifier != null && identifier.length() > 0) {
			HttpClient httpclient = new DefaultHttpClient();
			try {
				HttpGet httpget = new HttpGet(
						"https://www.wikidata.org/wiki/Special:EntityData/" + identifier + ".json");

				System.out.println("Executing request " + httpget.getURI());

				// Create a response handler
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				// Body contains your json stirng
				String responseBody = httpclient.execute(httpget, responseHandler);
				// System.out.println("----------------------------------------");
				// System.out.println(responseBody);

				ObjectMapper mapper = new ObjectMapper();

				String replace = responseBody.replace("{\"entities\":{\"" + identifier + "\":", "");

				replace = replace.substring(0, replace.length() - 2);
				// JSON from file to Object
				// System.out.println("----------------------------------------");
				// System.out.println(replace);
				obj = mapper.readValue(replace, JacksonItemDocument.class);

				System.out.println("Data fetched");

				((JacksonTermedStatementDocument) obj).setSiteIri(Datamodel.SITE_WIKIDATA);

			} catch (Exception e) {
				e.printStackTrace();
			}

			finally {

				// When HttpClient instance is no longer needed,
				// shut down the connection manager to ensure
				// immediate deallocation of all system resources
				httpclient.getConnectionManager().shutdown();
			}
		}
		return obj;
	}
	

	
	
	

}
