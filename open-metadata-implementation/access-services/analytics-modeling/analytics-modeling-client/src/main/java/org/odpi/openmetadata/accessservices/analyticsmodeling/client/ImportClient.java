/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.client;

import org.odpi.openmetadata.accessservices.analyticsmodeling.api.AnalyticsModelingImport;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ModuleTableFilter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.AnalyticsModelingOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

public class ImportClient implements AnalyticsModelingImport {
	
    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private AnalyticsModelingRestClient   restClient;               /* Initialized in constructor */

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ImportClient(String   serverName,
                                 String   serverPlatformURLRoot,
                                 String   userId,
                                 String   password,
                                 AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.restClient = new AnalyticsModelingRestClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ImportClient(String serverName,
                                 String serverPlatformURLRoot,
                                 String userId,
                                 String password) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.restClient = new AnalyticsModelingRestClient(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that is to be used within an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient pre-initialized REST client
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public ImportClient(String                serverName,
                                 String                serverPlatformURLRoot,
                                 AnalyticsModelingRestClient restClient,
                                 int                   maxPageSize,
                                 AuditLog              auditLog) throws InvalidParameterException
    {
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
	 */
	public AnalyticsModelingOMASAPIResponse getDatabases(String userId, Integer startFrom, Integer pageSize)
	{
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
	 */
	public AnalyticsModelingOMASAPIResponse getSchemas(String userId, String databaseGuid, Integer startFrom, Integer pageSize)
	{
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
	 */
	public AnalyticsModelingOMASAPIResponse getTables(String userId, String databaseGuid, String catalog, String schema)
	{
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
	 */
	public AnalyticsModelingOMASAPIResponse getModule(String userId, String databaseGuid, String catalog, String schema, ModuleTableFilter request)
	{
		return restClient.getModule(userId, databaseGuid, catalog, schema, request);
	}

}
