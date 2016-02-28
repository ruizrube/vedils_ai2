package com.google.appinventor.components.runtime.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.services.GoogleKeyInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.Fusiontables.Query.Sql;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.FusiontablesControl;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.OAuth2Helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

/*
 * FusionTablesConnection class for connect to Google FusionTables (by SPI-FM at UCA).
 */

public class FusionTablesConnection {
   private static final String LOG_TAG = "FUSIONTABLESCONNECTION";
   
   public static final String AUTH_TOKEN_TYPE_FUSIONTABLES = "oauth2:https://www.googleapis.com/auth/fusiontables";
   public static final String FUSIONTABLES_URL = "https://www.googleapis.com/fusiontables/v1/query";
   public static final String APP_NAME = "App Inventor";
   public static final String FUSIONTABLES_POST = "https://www.googleapis.com/fusiontables/v1/tables";
   private static final String DEFAULT_QUERY = "show tables";
   public static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";
   private String scope = "https://www.googleapis.com/auth/fusiontables";
	
   private String query;
   private String queryResultStr;
   private String apiKey;
   private String keyPath;
   private String authTokenType = AUTH_TOKEN_TYPE_FUSIONTABLES;
   private File cachedServiceCredentials = null; //For credentials
   private String serviceAccountEmail = "";
   private String columns;
   private Activity activity;
   private ComponentContainer container;
   private boolean isServiceAuth = false;
   
   public FusionTablesConnection(String columns, String apiKey, String keyPath, String email, ComponentContainer container, boolean isServiceAuth) {
	   this.apiKey = apiKey;
	   this.serviceAccountEmail = email;
	   this.query = DEFAULT_QUERY;
	   this.container = container;
	   this.activity = container.$context();
	   this.isServiceAuth = isServiceAuth;
	   this.keyPath = keyPath;
	   this.columns = columns;
   }
   
   public void insertRow(String values, String tableId) {
	   query = "INSERT INTO " + tableId + " (" + columns + ")" + " VALUES " + "(" + values + ")";
	   new QueryProcessorV1().execute(query);
   }
   
   /**
    * First uses OAuth2Helper to acquire an access token and then sends the
    * Fusiontables query asynchronously to the server and returns the result.
    *
    * This version uses the Fusion Tabes V1.0 API.
    */
   private class QueryProcessorV1 extends AsyncTask<String, Void, String> {
     private static final String TAG = "QueryProcessorV1";
     // alternative log tab used in service account processing
     private static final String STAG =  "FUSION_SERVICE_ACCOUNT";

     /**
      * @param activity, needed to create a progress dialog
      */
     QueryProcessorV1() {
       Log.i(TAG, "Creating AsyncFusiontablesQuery");
     }

     @Override
     protected void onPreExecute() {}

     /**
      * The Oauth handshake and the API request are both handled here.
      */
     @Override
     protected String doInBackground(String... params) {
       String query = params[0];
       Log.i(TAG, "Starting doInBackground " + query);
       if (isServiceAuth) {
         return serviceAuthRequest(query);
       } else {
         return userAuthRequest(query);
       }
     }
     
     /**
      * Method for handling 'create table' SQL queries. At this point that is
      * the only query that we support using a POST request.
      *
      * TODO: Generalize this for other queries that require POST.
      *
      * @param query -- a query of the form "create table <json encoded content>"
      * @param authToken -- Oauth 2.0 access token
      * @return
      */
     private String doPostRequest(String query, String authToken) {
       org.apache.http.HttpResponse response = null;
       String jsonContent = query.trim().substring("create table".length());
       Log.i(LOG_TAG, "Http Post content = " + jsonContent);

       // Set up the POST request

       StringEntity entity = null;
       HttpPost request = new HttpPost(FUSIONTABLES_POST + "?key=" + apiKey); // Fusiontables Uri
       try {
         entity = new StringEntity(jsonContent);
       } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
         return "Error: " + e.getMessage();
       }
       entity.setContentType("application/json");
       request.addHeader("Authorization", AUTHORIZATION_HEADER_PREFIX  + authToken);
       request.setEntity(entity);

       // Execute the request

       HttpClient client = new DefaultHttpClient();
       try {
         response = client.execute(request);
       } catch (ClientProtocolException e) {
         e.printStackTrace();
         return "Error: " + e.getMessage();
       } catch (IOException e) {
         e.printStackTrace();
         return "Error: " + e.getMessage();
       }

       // Process the response
       // A valid response will have code=200 and contain a tableId value plus other stuff.
       // We just return the table id.
       int statusCode = response.getStatusLine().getStatusCode();
       if (response != null && statusCode == 200) {
         try {
           String jsonResult =  FusiontablesControl.httpApacheResponseToString(response);
           JSONObject jsonObj = new JSONObject(jsonResult);
           if (jsonObj.has("tableId")) {
             queryResultStr = "tableId," + jsonObj.get("tableId");
           } else {
             queryResultStr = jsonResult;
           }

         } catch (IllegalStateException e) {
           e.printStackTrace();
           return "Error: " + e.getMessage();
         } catch (JSONException e) {
           e.printStackTrace();
           return "Error: " + e.getMessage();
         }
         Log.i(LOG_TAG, "Response code = " + response.getStatusLine());
         Log.i(LOG_TAG, "Query = " + query + "\nResultStr = " + queryResultStr);
         // queryResultStr = response.getStatusLine().toString();
       } else {
         Log.i(LOG_TAG, "Error: " + response.getStatusLine().toString());
         queryResultStr = response.getStatusLine().toString();
       }

       return queryResultStr;
     }
     
     /**
      * Executes a Fusiontable query with an OAuth 2.0 authenticated
      * request.  Requests are authenticated by attaching an
      * Authentication header to the Http request.  The header
      * takes the form 'Authentication Oauth <access_token>'.
      *
      * Requests take the form of SQL strings, using an Sql
      * object from the Google API Client library.  Apparently
      * the Sql object handles the decision of whether the request
      * should be a GET or a POST.  Queries such as 'show tables'
      * and 'select' are supposed to be GETs and queries such as
      * 'insert' are supposed to be POSTS.
      *
      * See <a href="https://developers.google.com/fusiontables/docs/v1/using">https://developers.google.com/fusiontables/docs/v1/using</a>
      *
      * @param query the raw SQL string used by App Inventor
      * @param authToken the OAuth 2.0 access token
      * @return the HttpResponse if the request succeeded, or null
      */
     public com.google.api.client.http.HttpResponse sendQuery(String query, String authToken) {
       Log.i(LOG_TAG, "executing " + query);
       com.google.api.client.http.HttpResponse response = null;

       // Create a Fusiontables service object (from Google API client lib)
       Fusiontables service = new Fusiontables.Builder(
             AndroidHttp.newCompatibleTransport(),
             new GsonFactory(),
             new GoogleCredential())
       .setApplicationName("App Inventor FusiontablesConnection/v1.0")
       .setJsonHttpRequestInitializer(new GoogleKeyInitializer(apiKey))
       .build();

       try {

         // Construct the SQL query and get a CSV result
         Sql sql =
           ((Fusiontables) service).query().sql(query);
         sql.put("alt", "csv");

         // Add the authToken to authentication header
         sql.setOauthToken(authToken);

         response = sql.executeUnparsed();

       } catch (GoogleJsonResponseException e) {
         Log.e(LOG_TAG, "JsonResponseException");
         Log.e(LOG_TAG, "e.getMessage() is " + e.getMessage());
         Log.e(LOG_TAG, "response is " + response);
       } catch (IOException e) {
         Log.e(LOG_TAG, "IOException");
         Log.e(LOG_TAG, "e.getMessage() is " + e.getMessage());
         Log.e(LOG_TAG, "response is " + response);
       }
       return response;
     }
     
     /**
      * Parses SQL API Create query into v1.0 a JSon string which is then submitted as a POST request
      * E.g., parses "
      *   CREATE TABLE Notes (NoteField: STRING,  NoteLength: NUMBER, Date:DATETIME, Location:LOCATION)"
      * into :
      *  "CREATE TABLE " +
         "{\"columns\": [{\"name\": \"NoteField\",\"type\": \"STRING\"},{\"name\": \"NoteLength\",\"type\": \"NUMBER\"}," +
         "{\"name\": \"Location\",\"type\": \"LOCATION\"},{\"name\": \"Date\",\"type\": \"DATETIME\"}], " +
         "\"isExportable\": \"true\", \"name\": \"Notes\"}"

      * @param query
      * @return
      */
     private String parseSqlCreateQueryToJson (String query) {
       Log.i(LOG_TAG, "parsetoJSonSqlCreate :" + query);
       StringBuilder jsonContent = new StringBuilder();
       query = query.trim();
       String tableName = query.substring("create table".length(), query.indexOf('(')).trim();
       String columnsList = query.substring(query.indexOf('(') + 1, query.indexOf(')'));
       String [] columnSpecs = columnsList.split(",");
       jsonContent.append("{'columns':[");
       for (int k = 0; k < columnSpecs.length; k++) {
         String [] nameTypePair = columnSpecs[k].split(":");
         jsonContent.append("{'name': '" + nameTypePair[0].trim() + "', 'type': '" + nameTypePair[1].trim() + "'}" );
         if (k < columnSpecs.length -1) {
           jsonContent.append(",");
         }
       }
       jsonContent.append("],");
       jsonContent.append("'isExportable':'true',");
       jsonContent.append("'name': '" + tableName + "'");
       jsonContent.append("}");

       jsonContent.insert(0, "CREATE TABLE ");

       Log.i(LOG_TAG, "result = " + jsonContent.toString());
       return jsonContent.toString();
     }

     private String userAuthRequest(String query) {
       queryResultStr = "";

       // Get a fresh access token
       OAuth2Helper oauthHelper = new OAuth2Helper();
       String authToken = oauthHelper.getRefreshedAuthToken(activity, authTokenType);

       // Make the fusiontables query

       if (authToken != null) {

         // We handle CREATE TABLE as a special case
         if (query.toLowerCase().contains("create table")) {
           queryResultStr = doPostRequest(parseSqlCreateQueryToJson(query), authToken);
           return queryResultStr;
         } else {

           // Execute all other queries
           com.google.api.client.http.HttpResponse response = sendQuery(query, authToken);

           // Process the response
           if (response != null) {
             queryResultStr = httpResponseToString(response);
             Log.i(TAG, "Query = " + query + "\nResultStr = " + queryResultStr);
           } 
           return queryResultStr;
         }
       } else {
         return OAuth2Helper.getErrorMessage();
       }
     }
     
     /**
      * Static utility method to prettify the HttpResponse. This version uses Google API
      * HttpResponse object, which is different than Apache's
      * @param response
      * @return resultString
      */
     public String httpResponseToString(com.google.api.client.http.HttpResponse response) {
       String resultStr = "";
       if (response != null) {
         if (response.getStatusCode() != 200) {
           resultStr = response.getStatusCode() + " " + response.getStatusMessage();
         } else {
           try {
             resultStr = parseResponse(response.getContent());
           } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
           }
         }
       }
       return resultStr;
     }
     
     /**
      * Parses the input stream returned from Http query
      * @param input
      * @return The Result String
      */
     public String parseResponse(InputStream input) {
       String resultStr = "";
       try {
         BufferedReader br = new BufferedReader(new InputStreamReader(input));

         StringBuilder sb = new StringBuilder();

         String line;
         while ((line = br.readLine()) != null) {
           sb.append(line + "\n");
         }
         resultStr = sb.toString();
         Log.i(LOG_TAG, "resultStr = " + resultStr);
         br.close();
       } catch (IOException e) {
         e.printStackTrace();
       }
       return resultStr;
     }

     private String serviceAuthRequest(String query) {

       queryResultStr = "";

       final HttpTransport TRANSPORT = AndroidHttp.newCompatibleTransport();
       final JsonFactory JSON_FACTORY = new GsonFactory();

       Log.i(STAG, "keyPath " + keyPath);

       try {
         if (cachedServiceCredentials == null) { // Need to cache the credentials in a temp file
           // copyMediaToTempFile will copy the credentials either from the /sdcard if
           // we are running in the Companion, or from the packaged assets if we are a
           // packaged application.
           cachedServiceCredentials = MediaUtil.copyMediaToTempFile(container.$form(), keyPath);
         }
         GoogleCredential credential = new  GoogleCredential.Builder()
             .setTransport(TRANSPORT)
             .setJsonFactory(JSON_FACTORY)
             .setServiceAccountId(serviceAccountEmail)
             .setServiceAccountScopes(scope)
             .setServiceAccountPrivateKeyFromP12File(cachedServiceCredentials)
             .build();

         Fusiontables fusiontables = new Fusiontables.Builder(TRANSPORT, JSON_FACTORY, credential)
           .setJsonHttpRequestInitializer(new GoogleKeyInitializer(apiKey))
           .build();

         // See the try/catch below for the exception thrown if the query is bad SQL
         Sql sql = fusiontables.query().sql(query);
         sql.put("alt", "csv");

         com.google.api.client.http.HttpResponse response = null;

         try {
           // if an error is thrown here, the catch clauses take care of signaling a form error
           // to the end user, and the response will be null.   The null response will cause
           // the FusionTables.query command to return a standard error message as it result.
         response = sql.executeUnparsed();

         } catch (GoogleJsonResponseException e) {
           // This is the exception that was thrown as a result of a bad query to fusion tables.
           // I determined this experimentally since I could not find documentation, so I don't know
           // if throwing this particular exception is officially supported.
           Log.i(STAG, "Got a JsonResponse exception on sql.executeUnparsed");

         } catch (Exception e) {
           // Maybe there could be some other kind of exception thrown?
           Log.i(STAG, "Got an unanticipated exception on sql.executeUnparsed");
           Log.i(STAG, "Exception class is " + e.getClass());
           Log.i(STAG, "Exception message is " + e.getMessage());
           Log.i(STAG, "Exception is " + e);
           Log.i(STAG, "Point e");
           Log.i(STAG, "end of printing exception"); // e might have been multiline

         }

         // Process the response
         if (response != null) {
           // in the non-error case, get the response as a string to so we can return it
           queryResultStr = httpResponseToString(response);
           Log.i(STAG, "Query = " + query + "\nResultStr = " + queryResultStr);
         } 

         Log.i(STAG, "executed sql query");

       } catch (Throwable e) {
         Log.i(STAG, "in Catch Throwable e");
         e.printStackTrace();
         queryResultStr = e.getMessage();
       }

       Log.i(STAG, "returning queryResultStr = " + queryResultStr);
       return queryResultStr;
     }  //end of ServiceAuthRequest

     /**
      * Fires the AppInventor GotResult() method
      */
     @Override
     protected void onPostExecute(String result) {
       Log.i(LOG_TAG, "Query result " + result);
    }
     
   }

   void signalJsonResponseError(String query, String parsedException) {}
   
}
