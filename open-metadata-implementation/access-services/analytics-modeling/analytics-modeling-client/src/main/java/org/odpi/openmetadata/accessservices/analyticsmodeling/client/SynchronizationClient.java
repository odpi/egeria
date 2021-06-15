/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.client;

import org.odpi.openmetadata.accessservices.analyticsmodeling.api.AnalyticsModelingSynchronization;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.AnalyticsModelingOMASAPIResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

public class SynchronizationClient implements AnalyticsModelingSynchronization {
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
    public SynchronizationClient(String serverName, String serverPlatformURLRoot, AuditLog auditLog)
    		throws InvalidParameterException
    {
        final String methodName = "Synchronization Client Constructor";

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
    public SynchronizationClient(String serverName, String serverPlatformURLRoot)
    		throws InvalidParameterException
    {
        final String methodName = "Synchronization Client Constructor";

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
    public SynchronizationClient(String   serverName,
                                 String   serverPlatformURLRoot,
                                 String   userId,
                                 String   password,
                                 AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Synchronization Client Constructor";

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
    public SynchronizationClient(String serverName,
                                 String serverPlatformURLRoot,
                                 String userId,
                                 String password) throws InvalidParameterException
    {
        final String methodName = "Synchronization Client Constructor";

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
    public SynchronizationClient(String                serverName,
                                 String                serverPlatformURLRoot,
                                 AnalyticsModelingRestClient restClient,
                                 int                   maxPageSize,
                                 AuditLog              auditLog) throws InvalidParameterException
    {
        final String methodName = "Synchronization Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.restClient = restClient;
    }

    
	/**
	 * Create analytics artifact defined as json input.
	 * @param userId requested the operation.
	 * @param serverCapability source where artifact persist.
	 * @param artifact definition.
	 * @return response with artifact or error description.
	 */
    @Override
	public AnalyticsModelingOMASAPIResponse createArtifact(String userId, String serverCapability, AnalyticsAsset artifact)
	{
		return restClient.createAssets(userId, serverCapability, artifact);
	}

	/**
	 * Update analytics artifact defined as json input.
	 * @param userId requested the operation.
	 * @param serverCapability source where artifact persist.
	 * @param artifact definition.
	 * @return response with artifact or error description.
	 */
    @Override
	public AnalyticsModelingOMASAPIResponse updateArtifact(String userId, String serverCapability, AnalyticsAsset artifact)
	{
		return restClient.updateAssets(userId, serverCapability, artifact);
	}
	
    /**
	 * Delete assets in repository defined by artifact unique identifier.
     * @param userId      request user
	 * @param serverCapability where the artifact is stored.
	 * @param identifier of the artifact in 3rd party system.
	 * @return errors or list of created assets.
	 */
    @Override
	public AnalyticsModelingOMASAPIResponse deleteArtifact(String userId, String serverCapability, String identifier)
    {
		return restClient.deleteAssets(userId, serverCapability, identifier);
	}
	
}
