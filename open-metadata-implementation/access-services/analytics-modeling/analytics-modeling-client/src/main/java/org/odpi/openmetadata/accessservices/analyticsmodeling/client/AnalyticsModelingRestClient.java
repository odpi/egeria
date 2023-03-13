/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.client;


import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ModuleTableFilter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerAssets;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabase;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabaseSchema;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerModule;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerSchemaTables;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.AnalyticsModelingOMASAPIResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.AssetsResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.DatabasesResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.ModuleResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.SchemaTablesResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.SchemasResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * REST client is responsible for issuing calls to the AnalyticsModeling OMAS REST APIs.
 */
public class AnalyticsModelingRestClient extends FFDCRESTClient
{
    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

	
	private final String urlTemplateResource = "/servers/{0}/open-metadata/access-services/analytics-modeling/users/{1}/";
	private final String urlTemplateSynchronization = urlTemplateResource + "sync?serverCapability={2}";
	private final String urlTemplateSynchronizationWithGUID = urlTemplateSynchronization + "&serverCapabilityGUID={3}";

    /**
     * Constructor for no authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AnalyticsModelingRestClient(String serverName, String serverPlatformURLRoot, AuditLog auditLog)
    		throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AnalyticsModelingRestClient(String serverName, String serverPlatformURLRoot)
    		throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Constructor for simple userId and password authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AnalyticsModelingRestClient(String serverName, String serverPlatformURLRoot, String userId, String password, AuditLog auditLog)
    		throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Constructor for simple userId and password authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server manager where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AnalyticsModelingRestClient(String serverName, String serverPlatformURLRoot, String userId, String password)
    		throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }
    
    /**
     * Get list of available databases.  
     * @param userId requested the operation.
     * @param startFrom this index.
     * @param pageSize amount to retrieve.
     * @return list of databases.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 * @throws UserNotAuthorizedException in case user unauthorized to perform operation. 
	 * @throws InvalidParameterException in case any passed parameter is invalid.
     */
	public List<ResponseContainerDatabase> getDatabases(String userId, int startFrom, int pageSize)
			throws PropertyServerException, AnalyticsModelingCheckedException, InvalidParameterException, UserNotAuthorizedException 
	{
		String methodName = "getDatabases";
		String url = serverPlatformURLRoot + urlTemplateResource + "databases?startFrom={2}&pageSize={3}";
		AnalyticsModelingOMASAPIResponse response = 
				callGetRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class, url, serverName, userId, startFrom, pageSize);
		
		handleFailedResponse(response, methodName);
		
		return ((DatabasesResponse)response).getDatabasesList();
	}


	/**
	 * Get list of available schemas for a databases.  
	 * @param userId requested the operation.
	 * @param databaseGuid to defined database.
     * @param startFrom this index.
     * @param pageSize amount to retrieve.
	 * @return list of schemas.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 * @throws UserNotAuthorizedException in case user unauthorized to perform operation. 
	 * @throws InvalidParameterException in case any passed parameter is invalid.
	 */
	public List<ResponseContainerDatabaseSchema> getSchemas(String userId, String databaseGuid, int startFrom, int pageSize) 
			throws PropertyServerException, AnalyticsModelingCheckedException, InvalidParameterException, UserNotAuthorizedException
	{
		String methodName = "getSchemas";
		String url = serverPlatformURLRoot + urlTemplateResource + "{2}/schemas?startFrom={3}&pageSize={4}";
		AnalyticsModelingOMASAPIResponse response =
				callGetRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class, url, serverName, userId, databaseGuid, startFrom, pageSize);

		handleFailedResponse(response, methodName);
		
		return ((SchemasResponse) response).getSchemaList();
	}


	/**
	 * Get list of tables for database schema.
	 * @param userId requested the operation.
	 * @param databaseGuid to defined database.
	 * @param catalog name.
	 * @param schema name.
	 * @return list of tables.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 * @throws UserNotAuthorizedException in case user unauthorized to perform operation. 
	 * @throws InvalidParameterException in case any passed parameter is invalid.
	 */
	public ResponseContainerSchemaTables getTables(String userId, String databaseGuid, String catalog, String schema)
			throws PropertyServerException, AnalyticsModelingCheckedException, InvalidParameterException, UserNotAuthorizedException
	{
		String methodName = "getTables";

		String url = serverPlatformURLRoot + urlTemplateResource + "{2}/tables?catalog={3}&schema={4}";
		AnalyticsModelingOMASAPIResponse response =
				callPostRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class, url, null, serverName, userId, databaseGuid, catalog, schema);

		handleFailedResponse(response, methodName);
		
		return ((SchemaTablesResponse)response).getTableList();
	}


	/**
	 * Get module.
	 * @param userId requested the operation.
	 * @param databaseGuid to create module for.
	 * @param catalog name of the module.
	 * @param schema name of the module.
	 * @param request set of tables to include.
	 * @return created module.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 * @throws UserNotAuthorizedException in case user unauthorized to perform operation. 
	 * @throws InvalidParameterException in case any passed parameter is invalid.
	 */
	public ResponseContainerModule getModule(String userId, String databaseGuid, String catalog, String schema, ModuleTableFilter request)
			throws PropertyServerException, AnalyticsModelingCheckedException, InvalidParameterException, UserNotAuthorizedException
	{
		String methodName = "getModule";
		String url = serverPlatformURLRoot + urlTemplateResource + "{2}/physicalModule?catalog={3}&schema={4}";
		AnalyticsModelingOMASAPIResponse response = 
				callPostRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class, url, request, serverName, userId, databaseGuid, catalog, schema);
		
		handleFailedResponse(response, methodName);
		
		return ((ModuleResponse)response).getModule();
	}
    
	/**
	 * Create assets defined by artifact.
	 * @param user requested the operation.
	 * @param serverCapability the source of artifact.
	 * @param serverCapabilityGUID the source of artifact.
	 * @param artifact definition.
	 * @return list of GUIDs for created assets.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 * @throws UserNotAuthorizedException in case user unauthorized to perform operation. 
	 * @throws InvalidParameterException in case any passed parameter is invalid.
	 */
	public ResponseContainerAssets createAssets(String user, String serverCapability, String serverCapabilityGUID, AnalyticsAsset artifact)
			throws PropertyServerException, AnalyticsModelingCheckedException, InvalidParameterException, UserNotAuthorizedException
	{
		String methodName = "createAssets";

		AnalyticsModelingOMASAPIResponse response = serverCapabilityGUID == null
				? callPostRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class,
						serverPlatformURLRoot + urlTemplateSynchronization,
						artifact, serverName, user,	serverCapability)
				: callPostRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class,
						serverPlatformURLRoot + urlTemplateSynchronizationWithGUID,
						artifact, serverName, user, serverCapability, serverCapabilityGUID);

		handleFailedResponse(response, methodName);
		
		return ((AssetsResponse) response).getAssetList();
	}
    
	/**
	 * Update assets defined by artifact.
	 * @param user requested the operation.
	 * @param serverCapability the source of artifact.
	 * @param serverCapabilityGUID the source of artifact.
	 * @param artifact definition.
	 * @return list of GUIDs for updated assets.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 * @throws UserNotAuthorizedException in case user unauthorized to perform operation. 
	 * @throws InvalidParameterException in case any passed parameter is invalid.
	 */
	public ResponseContainerAssets updateAssets(String user, String serverCapability, String serverCapabilityGUID, AnalyticsAsset artifact)
			throws PropertyServerException, AnalyticsModelingCheckedException, InvalidParameterException, UserNotAuthorizedException
	{
		String methodName = "updateAssets";
		AnalyticsModelingOMASAPIResponse response = serverCapabilityGUID == null
				? callPutRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class,
						serverPlatformURLRoot + urlTemplateSynchronization,
						artifact, serverName, user, serverCapability)
				: callPutRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class,
						serverPlatformURLRoot + urlTemplateSynchronizationWithGUID,
						artifact, serverName, user, serverCapability, serverCapabilityGUID);

		handleFailedResponse(response, methodName);
		
		return ((AssetsResponse) response).getAssetList();
	}
    
	/**
	 * Delete assets defined by identifier.
	 * @param user requested the operation.
	 * @param serverCapability the source of artifact.
	 * @param serverCapabilityGUID the source of artifact.
	 * @param identifier defining the assets.
	 * @return list of GUIDs for deleted assets.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 * @throws UserNotAuthorizedException in case user unauthorized to perform operation. 
	 * @throws InvalidParameterException in case any passed parameter is invalid.
	 */
	public ResponseContainerAssets deleteAssets(String user, String serverCapability, String serverCapabilityGUID, String identifier)
			throws PropertyServerException, AnalyticsModelingCheckedException, InvalidParameterException, UserNotAuthorizedException
	{
		String methodName = "deleteAssets";

		AnalyticsModelingOMASAPIResponse response = serverCapabilityGUID == null
				? callDeleteRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class, 
						serverPlatformURLRoot + urlTemplateSynchronization + "&identifier={3}",
						null,
						serverName, user, serverCapability, identifier)
				: callDeleteRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class, 
						serverPlatformURLRoot + urlTemplateSynchronizationWithGUID + "&identifier={4}",
						null,
						serverName, user, serverCapability, serverCapabilityGUID, identifier);
		
		handleFailedResponse(response, methodName);
		
		return ((AssetsResponse) response).getAssetList();
	}
	
	private void handleFailedResponse(AnalyticsModelingOMASAPIResponse response, String methodName) 
			throws AnalyticsModelingCheckedException, InvalidParameterException, UserNotAuthorizedException, PropertyServerException
	{
		restExceptionHandler.detectAndThrowStandardExceptions(methodName, response);
		
		if (response.getRelatedHTTPCode() != 200) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.FAIL_REST_CALL.getMessageDefinition(methodName, response.getExceptionErrorMessage()),
					this.getClass().getSimpleName(),
					methodName);
		}
	}

}
