/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.client;

import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.api.AnalyticsModelingImport;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ModuleTableFilter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabase;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabaseSchema;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerModule;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerSchemaTables;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

public class ImportClient implements AnalyticsModelingImport {
	
    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private AnalyticsModelingRestClient   restClient;               /* Initialized in constructor */

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ImportClient(String serverName, String serverPlatformURLRoot, AuditLog auditLog)
    		throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.restClient = new AnalyticsModelingRestClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
	public ImportClient(String serverName, String serverPlatformURLRoot)
			throws InvalidParameterException 
	{
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.restClient = new AnalyticsModelingRestClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
	public ImportClient(String serverName,
						String serverPlatformURLRoot,
						String userId,
						String password,
						AuditLog auditLog)
			throws InvalidParameterException    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.restClient = new AnalyticsModelingRestClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
	public ImportClient(String serverName, String serverPlatformURLRoot, String userId, String password)
			throws InvalidParameterException {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.restClient = new AnalyticsModelingRestClient(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that is to be used within an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient pre-initialized REST client
     * @param maxPageSize pre-initialized parameter limit
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
	public ImportClient(String serverName,
						String serverPlatformURLRoot,
						AnalyticsModelingRestClient restClient,
						int maxPageSize)
			throws InvalidParameterException {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.restClient = restClient;
    }

    /**
	 * Get databases available on the server for the user.
	 * 
	 * @param userId		of the user.
     * @param startFrom		starting element (used in paging through large result sets)
     * @param pageSize		maximum number of results to return
	 * @return list of databases for the requested server/user.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws UserNotAuthorizedException in case user unauthorized to perform operation. 
	 * @throws InvalidParameterException in case any passed parameter is invalid.
	 */
	@Override
	public List<ResponseContainerDatabase> getDatabases(String userId, int startFrom, int pageSize)
			throws AnalyticsModelingCheckedException, PropertyServerException, InvalidParameterException, UserNotAuthorizedException
	{
		String method = "getDatabases";
		invalidParameterHandler.validateUserId(userId, method);
		return restClient.getDatabases(userId, startFrom, pageSize);
	}

	/**
	 * Get schema defined by database GUID.
	 * 
	 * @param userId         of the request.
	 * @param databaseGuid	 of the requested database.
     * @param startFrom		 starting element (used in paging through large result sets)
     * @param pageSize		 maximum number of results to return
	 * @return list of schemas for the requested database.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws UserNotAuthorizedException in case user unauthorized to perform operation. 
	 * @throws InvalidParameterException in case any passed parameter is invalid.
	 */
	@Override
	public List<ResponseContainerDatabaseSchema> getSchemas(String userId, String databaseGuid, int startFrom, int pageSize)
			throws AnalyticsModelingCheckedException, PropertyServerException, InvalidParameterException, UserNotAuthorizedException
	{
		String method = "getSchemas";
		invalidParameterHandler.validateUserId(userId, method);
		invalidParameterHandler.validateGUID(databaseGuid, "databaseGuid", method);
		return restClient.getSchemas(userId, databaseGuid, startFrom, pageSize);
	}

	/**
	 * Get tables for the schema.
	 * 
	 * @param userId       of the request.
	 * @param databaseGuid of the requested database.
	 * @param catalog      name of the database.
	 * @param schema       schema name on the database.
	 * @return list of tables for the requested schema.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws UserNotAuthorizedException in case user unauthorized to perform operation. 
	 * @throws InvalidParameterException in case any passed parameter is invalid.
	 */
	@Override
	public ResponseContainerSchemaTables getTables(String userId, String databaseGuid, String catalog, String schema)
			throws AnalyticsModelingCheckedException, PropertyServerException, InvalidParameterException, UserNotAuthorizedException
	{
		String method = "getTables";
		invalidParameterHandler.validateUserId(userId, method);
		invalidParameterHandler.validateGUID(databaseGuid, "databaseGuid", method);
		return restClient.getTables(userId, databaseGuid, catalog, schema);
	}

	/**
	 * Build module for the schema.
	 * 
	 * @param userId       of the request.
	 * @param databaseGuid of the requested database.
	 * @param catalog      catalog name of the database.
	 * @param schema       schema name of the database.
	 * @param request      table filter 
	 * @return module for the requested schema.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws UserNotAuthorizedException in case user unauthorized to perform operation. 
	 * @throws InvalidParameterException in case any passed parameter is invalid.
	 */
	@Override
	public ResponseContainerModule getModule(String userId, String databaseGuid, String catalog, String schema, ModuleTableFilter request)
			throws AnalyticsModelingCheckedException, PropertyServerException, InvalidParameterException, UserNotAuthorizedException
	{
		String method = "getModule";
		invalidParameterHandler.validateUserId(userId, method);
		invalidParameterHandler.validateGUID(databaseGuid, "databaseGuid", method);
		return restClient.getModule(userId, databaseGuid, catalog, schema, request);
	}

}
