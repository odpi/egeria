/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.client.exchange;

import org.odpi.openmetadata.accessservices.assetmanager.api.exchange.ExternalAssetManagerInterface;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.AssetManagerProperties;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;



/**
 * ExternalAssetManagerClient is the client for setting up the SoftwareServerCapabilities that represent asset managers.
 */
public class ExternalAssetManagerClient extends ExchangeClientBase implements ExternalAssetManagerInterface
{
    private final String urlTemplatePrefix = "/servers/{0}/open-metadata/access-services/asset-manager/users/{1}/asset-managers";


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalAssetManagerClient(String   serverName,
                                      String   serverPlatformURLRoot,
                                      AuditLog auditLog,
                                      int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog, maxPageSize);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalAssetManagerClient(String serverName,
                                      String serverPlatformURLRoot,
                                      int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalAssetManagerClient(String serverName,
                                      String serverPlatformURLRoot,
                                      String userId,
                                      String password,
                                      int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
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
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExternalAssetManagerClient(String   serverName,
                                      String   serverPlatformURLRoot,
                                      String   userId,
                                      String   password,
                                      AuditLog auditLog,
                                      int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog, maxPageSize);
    }

    

    /**
     * Create a new client that is to be used within an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient pre-initialized REST client
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public ExternalAssetManagerClient(String                 serverName,
                                      String                 serverPlatformURLRoot,
                                      AssetManagerRESTClient restClient,
                                      int                    maxPageSize,
                                      AuditLog               auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
    }


    /* ========================================================
     * The asset manager represents the third party technology this integration processing is connecting to
     */


    /**
     * Create information about the external asset manager.  This is represented as a software server capability
     * and all information that is specific to the external asset manager (such as the identifiers of the
     * metadata elements it stores) will be linked to it.
     *
     * @param userId calling user
     * @param assetManagerProperties description of the integration daemon (specify qualified name at a minimum)
     *
     * @return unique identifier of the asset management's software server capability
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public String createExternalAssetManager(String                 userId, 
                                             AssetManagerProperties assetManagerProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName                  = "createExternalAssetManager";
        final String propertiesParameterName     = "assetManagerProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(assetManagerProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(assetManagerProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix;

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  assetManagerProperties,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Retrieve the unique identifier of the external asset manager from its qualified name.
     * Typically, the qualified name comes from the integration connector configuration.
     *
     * @param userId calling user
     * @param qualifiedName unique name to use for the external asset
     *
     * @return unique identifier of the external asset manager's software server capability
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public String  getExternalAssetManagerGUID(String  userId,
                                               String  qualifiedName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName                  = "getExternalAssetManagerGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/by-name/{2}";

        GUIDResponse restResult = restClient.callGUIDGetRESTCall(methodName,
                                                                 urlTemplate,
                                                                 serverName,
                                                                 userId,
                                                                 qualifiedName);

        return restResult.getGUID();
    }
}
