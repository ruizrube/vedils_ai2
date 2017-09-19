package com.google.appinventor.components.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesAssets;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.la4ai.util.DeviceInfoFunctions;
import com.google.appinventor.components.runtime.util.ActivityQueryManager;
import com.google.appinventor.components.runtime.util.ActivityQueryManagerFusionTables;
import com.google.appinventor.components.runtime.util.ActivityQueryManagerMongoDB;
import com.google.appinventor.components.runtime.util.ActivityQueryManagerStream;
import com.google.appinventor.components.runtime.util.StreamQueryResultData;

/**
 * ActivityProcessor component
 * @author SPI-FM at UCA
 */
@UsesAssets(fileNames = "ActivityTrackerVEDILS-e804e05b5eb3.p12")
@UsesLibraries(libraries = "fusiontables.jar," + "google-api-client-beta.jar," + "google-api-client-android2-beta.jar,"
		+ "google-http-client-beta.jar," + "google-http-client-android2-beta.jar,"
		+ "google-http-client-android3-beta.jar," + "google-oauth-client-beta.jar," + "guava-14.0.1.jar,"
		+ "gson-2.1.jar," + "la4ai.jar")
@UsesPermissions(permissionNames = "android.permission.INTERNET, " + "android.permission.ACCESS_NETWORK_STATE,"
		+ "android.permission.WRITE_EXTERNAL_STORAGE, " + "android.permission.READ_EXTERNAL_STORAGE,"
		+ "android.permission.ACCESS_FINE_LOCATION," + "android.permission.ACCESS_COARSE_LOCATION,"
		+ "android.permission.ACCESS_MOCK_LOCATION," + "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS,"
		+ "android.permission.READ_PHONE_STATE")
@SimpleObject
public abstract class ActivityProcessor extends AndroidNonvisibleComponent implements Component {

	private String tableId;

	private String additionalFilter;
	private String filterByUserId;
	private String filterByIP;
	private String filterByMAC;
	private String filterByIMEI;
	private String filterByAppId;
	private String filterByScreenId;
	private String filterByComponentType;
	private String filterByComponentId;
	private String filterByActionType;
	private String filterByActionId;
	
	// Filters collected from query-tree
	protected List<String> filtersByScreenId;
	protected List<String> filtersByComponentType;
	protected List<String> filtersByComponentId;
	protected List<String> filtersByActionType;
	protected List<String> filtersByActionId;
	
	// List for parameters by ActionType (Change the name in Fusion Tables mode).
	protected List<String> propertySetterParameters;
	protected List<String> propertyGetterParameters;
	protected List<String> functionsParameters;
	protected List<String> eventsParameters;
	protected List<String> userParameters;
	
	//Special tree-keywords
	protected final String SCREEN_ID = "ScreenID";
	protected final String COMPONENT_TYPE = "ComponentType";
	protected final String COMPONENT_ID = "ComponentID";
	protected final String ACTION_TYPE = "ActionType";
	protected final String ACTION_ID = "ActionID";
	protected final String PARAM = "Param:";
	protected final String CATEGORY = "Category";
	
	private int storageMode;
	
	private ActivityQueryManager activityQueryManager;
	private ActivityQueryManagerStream activityStreamQueryManager;
	
	public ComponentContainer componentContainer;
	
	public int timeStreamQuery;
	private Timer timer;
	private boolean streamQueryRunning;
	
	private StreamQueryResultData timerStreamQueryResultData;

	public ActivityProcessor(ComponentContainer componentContainer) {
		super(componentContainer.$form());
		this.componentContainer = componentContainer;
		
		//Enable by default Google Fusion Tables mode.
		this.storageMode = Component.FUSIONTABLES;
		this.activityQueryManager = new ActivityQueryManagerFusionTables(this, componentContainer);
		this.activityStreamQueryManager = new ActivityQueryManagerStream(this, componentContainer);
		this.streamQueryRunning = false;
	}
	
	public ActivityQueryManager getQueryManager() {
		return this.activityQueryManager;
	}

	@Override
	public void ActivitiesToTrack(String activitiesNames) {}

	////////////////
	// PROPERTIES //
	////////////////
	
	/**
	 * Returns the current storage mode.
	 * @return  one of {@link Component#FUSIONTABLES} or
	 *          {@link Component#MONGODB}
	 */
	@SimpleProperty(
		      category = PropertyCategory.BEHAVIOR,
		      description = "Storage mode for process query.",
		      userVisible = false)
    public int StorageMode() {
		return this.storageMode;
		
    }
	
	/**
	 * Specifies the storage mode used.
	 * 
	 * @param storage
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STORAGEMODE,
		      defaultValue = Component.FUSIONTABLES + "")
		  @SimpleProperty(
		      userVisible = false)
	public void StorageMode(int storageMode) {
		this.storageMode = storageMode;
		
		//Configure ActivityQueryManager to query data (Fusion Tables or MongoDB storage mode).
		if(this.storageMode == Component.FUSIONTABLES) {
			this.activityQueryManager = new ActivityQueryManagerFusionTables(this, this.componentContainer);
		} else if(this.storageMode == Component.MONGODB) {
			this.activityQueryManager = new ActivityQueryManagerMongoDB(this);
		} /* else if(this.storageMode == Component.STREAM) {
			this.activityQueryManager = new ActivityQueryManagerStream(this);
		} */
	}

	/**
	 * Specifies the additionalFilter of the query.
	 * 
	 * @param additionalFilter
	 */
	@SimpleProperty
	public void AdditionalFilter(String additionalFilter) {
		this.additionalFilter = additionalFilter;

	}

	/**
	 * Return the additionalFilter of the query.
	 * 
	 * Return additionalFilter
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the additionalFilter of the query.", userVisible = true)
	public String AdditionalFilter() {
		return this.additionalFilter;
	}

	/**
	 * Specifies the filterByIP of the query.
	 * 
	 * @param filterByIP
	 */
	@SimpleProperty
	public void FilterByIP(String filterByIP) {
		this.filterByIP = filterByIP;

	}

	/**
	 * Return the filterByIP of the query.
	 * 
	 * Return filterByIP
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByIP of the query.", userVisible = true)
	public String FilterByIP() {
		return this.filterByIP;
	}


	/**
	 * Specifies the filterByMAC of the query.
	 * 
	 * @param filterByMAC
	 */
	@SimpleProperty
	public void FilterByMAC(String filterByMAC) {
		this.filterByMAC = filterByMAC;

	}

	/**
	 * Return the filterByMAC of the query.
	 * 
	 * Return filterByMAC
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByMAC of the query.", userVisible = true)
	public String FilterByMAC() {
		return this.filterByMAC;
	}

	/**
	 * Specifies the filterByIMEI of the query.
	 * 
	 * @param filterByIMEI
	 */
	@SimpleProperty
	public void FilterByIMEI(String filterByIMEI) {
		this.filterByIMEI = filterByIMEI;

	}

	/**
	 * Return the filterByIMEI of the query.
	 * 
	 * Return filterByIMEI
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByIMEI of the query.", userVisible = true)
	public String FilterByIMEI() {
		return this.filterByIMEI;
	}

	
	/**
	 * Specifies the filterByUserId of the query.
	 * 
	 * @param filterByUserId
	 */
	@SimpleProperty
	public void FilterByUserId(String filterByUserId) {
		this.filterByUserId = filterByUserId;

	}

	/**
	 * Return the filterByUserId of the query.
	 * 
	 * Return filterByUserId
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByUserId of the query.", userVisible = true)
	public String FilterByUserId() {
		return this.filterByUserId;
	}

	/**
	 * Specifies the filterByAppId of the query.
	 * 
	 * @param filterByAppId
	 */
	@SimpleProperty
	public void FilterByAppId(String filterByAppId) {
		this.filterByAppId = filterByAppId;

	}

	/**
	 * Return the filterByAppId of the query.
	 * 
	 * Return filterByAppId
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByAppId of the query.", userVisible = true)
	public String FilterByAppId() {
		return this.filterByAppId;
	}

	/**
	 * Specifies the filterByScreenId of the query.
	 * 
	 * @param filterByScreenId
	 */
	@SimpleProperty
	public void FilterByScreenId(String filterByScreenId) {
		this.filterByScreenId = filterByScreenId;
	}

	/**
	 * Return the filterByScreenId of the query.
	 * 
	 * Return filterByScreenId
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByScreenId of the query.", userVisible = true)
	public String FilterByScreenId() {
		return this.filterByScreenId;
	}

	/**
	 * Specifies the filterByComponentType of the query.
	 * 
	 * @param filterByComponentType
	 */
	@SimpleProperty
	public void FilterByComponentType(String filterByComponentType) {
		this.filterByComponentType = filterByComponentType;

	}

	/**
	 * Return the filterByComponentType of the query.
	 * 
	 * Return filterByComponentType
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByComponentType of the query.", userVisible = true)
	public String FilterByComponentType() {
		return this.filterByComponentType;
	}

	/**
	 * Specifies the filterByComponentId of the query.
	 * 
	 * @param filterByComponentId
	 */
	@SimpleProperty
	public void FilterByComponentId(String filterByComponentId) {
		this.filterByComponentId = filterByComponentId;

	}

	/**
	 * Return the filterByComponentId of the query.
	 * 
	 * Return filterByComponentId
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByComponentId of the query.", userVisible = true)
	public String FilterByComponentId() {
		return this.filterByComponentId;
	}

	/**
	 * Specifies the filterByActionType of the query.
	 * 
	 * @param filterByActionType
	 */
	@SimpleProperty
	public void FilterByActionType(String filterByActionType) {
		this.filterByActionType = filterByActionType;

	}

	/**
	 * Return the filterByActionType of the query.
	 * 
	 * Return filterByActionType
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByActionType of the query.", userVisible = true)
	public String FilterByActionType() {
		return this.filterByActionType;
	}

	/**
	 * Specifies the filterByActionId of the query.
	 * 
	 * @param filterByActionId
	 */
	@SimpleProperty
	public void FilterByActionId(String filterByActionId) {
		this.filterByActionId = filterByActionId;

	}

	/**
	 * Return the filterByActionId of the query.
	 * 
	 * Return filterByActionId
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the filterByActionId of the query.", userVisible = true)
	public String FilterByActionId() {
		return this.filterByActionId;
	}

	/**
	 * Specifies the tableId of Fusion Table (Google) to establish the
	 * connection.
	 * 
	 * @param tableId
	 */
	@DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_TEXTBOX_AND_HYPERLINK_FORACTIVITYTRACKER,
		      defaultValue = "")
	@SimpleProperty
	public void TableId(String tableId) {
		this.tableId = tableId;
	}

	/**
	 * Returns the id of the current Fusion Table.
	 * 
	 * @return tableId
	 */
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the id of the current Fusion Table", userVisible = true)
	public String TableId() {
		return tableId;
	}

	//////////////
	// FUNCTIONS //
	//////////////

	/**
	 * Function to send the query to analyze activities.
	 * 
	 * @param actionId
	 */
	@SimpleFunction(description = "Function to send the query to analyze activities.")
	public void SendQuery() {
		/*if(this.storageMode == Component.STREAM) {
			//And on this time, launch timer to receive Kafka data (30 sec)
			this.timerStreamQueryResultData = new StreamQueryResultData(this.activityQueryManager, componentContainer.$context().getApplicationInfo().packageName, this.tableId);
			new Timer().schedule(this.timerStreamQueryResultData, 0, 30000);
		}*/
		System.out.println("enviando datos");
		this.activityQueryManager.sendQuery();
	}
	
	/**
	 * Function to stream send the query to analyze activities from Flink.
	 * 
	 * @param actionId 
	 */
	@SimpleFunction(description = "Function to stream send the query to analyze activities from Flink.")
	public void SendStreamQuery(int seconds) {
		if(!streamQueryRunning) {
			System.out.println("enviando stream query...");
			timeStreamQuery = seconds;
			this.activityStreamQueryManager.sendQuery();
			
			//And on this time, launch timer to receive Kafka data.
			this.timerStreamQueryResultData = new StreamQueryResultData(this.activityStreamQueryManager, 
					componentContainer.$context().getApplicationInfo().packageName, this.tableId + "_" + getName()
					+ "_" + DeviceInfoFunctions.getMAC(this.componentContainer.$context()).replaceAll(":", ""));
			this.timer = new Timer();
			this.timer.schedule(this.timerStreamQueryResultData, 0, seconds * 1000);
			
			this.streamQueryRunning = true;
		}
	}
	
	
	/**
	 * Function to stream send the query to analyze activities from Flink.
	 * 
	 * @param actionId
	 */
	@SimpleFunction(description = "Function to stop the stream query from Flink.")
	public void StopStreamQuery() {
		if(this.streamQueryRunning) {
			System.out.println("parando stream query...");
			this.activityStreamQueryManager.stopQuery();
			
			if(timer != null) {
				this.timer.cancel(); this.timer.purge(); //Stopping the schedule requests
			}

			this.streamQueryRunning = false;
		}
	}

	////////////
	// EVENTS //
	////////////

	/**
	 * Event to be raised after query results are catched
	 */
	@SuppressWarnings("rawtypes")
	@SimpleEvent(description = "Event to be raised after query results are catched", userVisible = true)
	public void DataReceived(List data) {
		EventDispatcher.dispatchEvent(this, "DataReceived", data);
	}
	
	/**
	 * Event to be raised after stream query results are catched
	 */
	@SuppressWarnings("rawtypes")
	@SimpleEvent(description = "Event to be raised after stream query results are catched", userVisible = true)
	public void StreamDataReceived(List data) {
		EventDispatcher.dispatchEvent(this, "StreamDataReceived", data);
	}
	
	public List<String> getFiltersByScreenId() {
		return this.filtersByScreenId;
	}
	
	public List<String> getFiltersByActionId() {
		return this.filtersByActionId;
	}
	
	public List<String> getFiltersByActionType() {
		return this.filtersByActionType;
	}
	
	public List<String> getFiltersByComponentId() {
		return this.filtersByComponentId;
	}
	
	public List<String> getFiltersByComponentType() {
		return this.filtersByComponentType;
	}
	
	public List<String> getPropertySetterParameters() {
		return this.propertySetterParameters;
	}
	
	public List<String> getPropertyGetterParameters() {
		return this.propertyGetterParameters;
	}
	
	public List<String> getFunctionParameters() {
		return this.functionsParameters;
	}
	
	public List<String> getEventParameters() {
		return this.eventsParameters;
	}
	
	public List<String> getUserParameters() {
		return this.userParameters;
	}

	public abstract List<String> obtainAggregations();

	public abstract List<String> obtainFields();

	public abstract  List<String> obtainGroupingColumns();
	
	//
	// Process tree fields (collect filters and parameters from the tree)
	//
	
	protected List<String> processTreeFields(List<String> fields) {
		
		//First, remove old values
		filtersByScreenId = new ArrayList<String>();
		filtersByActionId = new ArrayList<String>();
		filtersByActionType = new ArrayList<String>();
		filtersByComponentId = new ArrayList<String>();
		filtersByComponentType = new ArrayList<String>();
		propertyGetterParameters = new ArrayList<String>();
		propertySetterParameters = new ArrayList<String>();
		functionsParameters = new ArrayList<String>();
		eventsParameters = new ArrayList<String>();
		userParameters = new ArrayList<String>();
		
		List<String> processedFields = new ArrayList<String>();
		
		for(String field: fields) {
			if(field.contains(":" + CATEGORY)) {
				String processedField = field.replace(":" + CATEGORY, "");
				if(!processedFields.contains(processedField)) {
					processedFields.add(processedField);
				}
			} else if(field.contains(SCREEN_ID + ":")) { //Filter by ScreenID
				
				String screenId = field.replace(SCREEN_ID + ":", "");
				
				if(!filtersByScreenId.contains(screenId)) { //Ignore repetitions
					filtersByScreenId.add(screenId);
				}
				
				if(!processedFields.contains(SCREEN_ID)) { //Select ScreenID
					processedFields.add(SCREEN_ID);
				}
			} else if(field.contains(ACTION_TYPE) && field.contains(ACTION_ID) 
					&& field.contains(COMPONENT_TYPE) && field.contains(COMPONENT_ID)) { //Filters: ActionType, ActionID,
																							//ComponentType and ComponentID
				String[] actionFields = field.split(":");
				
				String actionType = actionFields[1];
				String actionId = actionFields[3];
				String componentType = actionFields[5];
				String componentId = actionFields[7];
				
				if(!filtersByComponentType.contains(componentType)) {
					filtersByComponentType.add(componentType);
				}
				
				if(!filtersByComponentId.contains(componentId)) {
					filtersByComponentId.add(componentId);
				}
				
				if(!filtersByActionType.contains(actionType)) {
					filtersByActionType.add(actionType);
				}
				
				if(!filtersByActionId.contains(actionId)) {
					filtersByActionId.add(actionId);
				}
				
				if(!processedFields.contains(COMPONENT_TYPE)) {
					processedFields.add(COMPONENT_TYPE);
				}
				
				if(!processedFields.contains(COMPONENT_ID)) {
					processedFields.add(COMPONENT_ID);
				}
				
				if(!processedFields.contains(ACTION_TYPE)) {
					processedFields.add(ACTION_TYPE);
				}
				
				if(!processedFields.contains(ACTION_ID)) {
					processedFields.add(ACTION_ID);
				}
			} else if(field.contains(PARAM)) { //Collect parameters for select column
				
				System.out.println("Param is: " + field);
				
				String[] paramFields = field.split(":");
				
				if(paramFields[0].equals("Set")) {
					propertySetterParameters.add(paramFields[2]);
				} else if(paramFields[0].equals("Get")) {
					propertyGetterParameters.add(paramFields[2]);
				} else if(paramFields[0].equals("Function")) {
					functionsParameters.add(paramFields[2] + ":" + paramFields[4]); //Add index for FusionTables version
				} else if(paramFields[0].equals("Event")) {
					eventsParameters.add(paramFields[2] + ":" + paramFields[4]); //Add index for FusionTables version
				} else if(paramFields[0].equals("User")) {
					userParameters.add(paramFields[2] + ":" + paramFields[3]); //Add index for FusionTables version
				}
			}
		}
		return processedFields;
	}
}