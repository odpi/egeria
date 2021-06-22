/* SPDX-License-Identifier: Apache 2.0 */
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
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;


/**
 * REST client is responsible for issuing calls to the AnalyticsModeling OMAS REST APIs.
 */
public class AnalyticsModelingRestClient extends FFDCRESTClient
{
	private final String urlTemplateResource = "/servers/{0}/open-metadata/access-services/analytics-modeling/users/{1}/";
	private final String urlTemplateSynchronization= urlTemplateResource + "sync?serverCapability={2}";

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
     */
	public List<ResponseContainerDatabase> getDatabases(String userId, Integer startFrom, Integer pageSize)
			throws PropertyServerException, AnalyticsModelingCheckedException 
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
	 */
	public List<ResponseContainerDatabaseSchema> getSchemas(String userId, String databaseGuid, Integer startFrom, Integer pageSize) 
			throws PropertyServerException, AnalyticsModelingCheckedException
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
	 */
	public ResponseContainerSchemaTables getTables(String userId, String databaseGuid, String catalog, String schema)
			throws PropertyServerException, AnalyticsModelingCheckedException
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
	 */
	public ResponseContainerModule getModule(String userId, String databaseGuid, String catalog, String schema, ModuleTableFilter request)
			throws PropertyServerException, AnalyticsModelingCheckedException
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
	 * @param artifact definition.
	 * @return list of GUIDs for created assets.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 */
	public ResponseContainerAssets createAssets(String user, String serverCapability, AnalyticsAsset artifact)
			throws PropertyServerException, AnalyticsModelingCheckedException
	{
		String methodName = "createAssets";
		String url = serverPlatformURLRoot + urlTemplateSynchronization;
		AnalyticsModelingOMASAPIResponse response = 
				callPostRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class, url, artifact, serverName, user, serverCapability);

		handleFailedResponse(response, methodName);
		
		return ((AssetsResponse) response).getAssetList();
	}
    
	/**
	 * Update assets defined by artifact.
	 * @param user requested the operation.
	 * @param serverCapability the source of artifact.
	 * @param artifact definition.
	 * @return list of GUIDs for updated assets.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 */
	public ResponseContainerAssets updateAssets(String user, String serverCapability, AnalyticsAsset artifact)
			throws PropertyServerException, AnalyticsModelingCheckedException
	{
		String methodName = "updateAssets";
		String url = serverPlatformURLRoot + urlTemplateSynchronization;
		AnalyticsModelingOMASAPIResponse response = 
				callPutRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class, url, artifact, serverName, user, serverCapability);

		handleFailedResponse(response, methodName);
		
		return ((AssetsResponse) response).getAssetList();
	}
    
	/**
	 * Delete assets defined by identifier.
	 * @param user requested the operation.
	 * @param serverCapability the source of artifact.
	 * @param identifier defining the assets.
	 * @return list of GUIDs for deleted assets.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 */
	public ResponseContainerAssets deleteAssets(String user, String serverCapability, String identifier)
			throws PropertyServerException, AnalyticsModelingCheckedException
	{
		String methodName = "deleteAssets";
		String url = serverPlatformURLRoot + urlTemplateSynchronization;
		AnalyticsModelingOMASAPIResponse response =
				callDeleteRESTCall(methodName, AnalyticsModelingOMASAPIResponse.class, url, serverName, user, serverCapability, identifier);
		
		handleFailedResponse(response, methodName);
		
		return ((AssetsResponse) response).getAssetList();
	}
	
	private void handleFailedResponse(AnalyticsModelingOMASAPIResponse response, String methodName) 
			throws AnalyticsModelingCheckedException
	{
		if (response.getRelatedHTTPCode() != 200) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.FAIL_REST_CALL.getMessageDefinition(methodName, response.getExceptionErrorMessage()),
					this.getClass().getSimpleName(),
					methodName);
		}
	}

}
