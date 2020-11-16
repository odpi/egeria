/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.client;

import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.ffdc.AssetManagerErrorCode;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.util.Map;

/**
 * ExchangeClientBase provides the base class for the clients called XXXXExchangeClient
 */
public class ExchangeClientBase
{
    final String externalIdentifierParameterName = "externalIdentifier";
    final String urlTemplatePrefix             = "/servers/{0}/open-metadata/access-services/asset-manager/users/{1}";

    String   serverName;               /* Initialized in constructor */
    String   serverPlatformURLRoot;    /* Initialized in constructor */

    InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    AssetManagerRESTClient  restClient;               /* Initialized in constructor */

    AuditLog auditLog = null;



    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExchangeClientBase(String   serverName,
                              String   serverPlatformURLRoot,
                              AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new AssetManagerRESTClient(serverName, serverPlatformURLRoot, auditLog);

        this.auditLog = auditLog;
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExchangeClientBase(String serverName,
                              String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new AssetManagerRESTClient(serverName, serverPlatformURLRoot);
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
    public ExchangeClientBase(String   serverName,
                              String   serverPlatformURLRoot,
                              String   userId,
                              String   password,
                              AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new AssetManagerRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);

        this.auditLog = auditLog;
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ExchangeClientBase(String                 serverName,
                              String                 serverPlatformURLRoot,
                              AssetManagerRESTClient restClient,
                              int                    maxPageSize,
                              AuditLog               auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = restClient;

        this.auditLog = auditLog;
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
    public ExchangeClientBase(String serverName,
                              String serverPlatformURLRoot,
                              String userId,
                              String password) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;

        this.restClient = new AssetManagerRESTClient(serverName, serverPlatformURLRoot, userId, password);
    }


    /* ========================================================
     * Metadata correlation setup.
     */


    /**
     * Throw an exception because the asset manager's GUID has not been passed with an external identifier.
     *
     * @param externalIdentifier external identifier
     * @param methodName calling method
     * @throws InvalidParameterException resulting exception
     */
    void handleMissingScope(String externalIdentifier,
                            String methodName) throws InvalidParameterException
    {
        throw new InvalidParameterException(AssetManagerErrorCode.NO_SCOPE_FOR_EXTERNAL_ID.getMessageDefinition(externalIdentifier,
                                                                                                                methodName),
                                            this.getClass().getName(),
                                            methodName,
                                            externalIdentifierParameterName);
    }



    /**
     * Set up the correlation properties for request.
     *
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalIdentifier unique identifier of the glossary in the external asset manager
     * @param methodName calling method
     * @return filled out correlation properties
     * @throws InvalidParameterException missing external identifier
     */
    MetadataCorrelationProperties getCorrelationProperties(String assetManagerGUID,
                                                           String assetManagerName,
                                                           String externalIdentifier,
                                                           String methodName) throws InvalidParameterException
    {
        MetadataCorrelationProperties correlationProperties = new MetadataCorrelationProperties();

        if (assetManagerGUID != null)
        {
            invalidParameterHandler.validateName(externalIdentifier, externalIdentifierParameterName, methodName);

            correlationProperties.setAssetManagerGUID(assetManagerGUID);
            correlationProperties.setAssetManagerName(assetManagerName);
            correlationProperties.setExternalIdentifier(externalIdentifier);

            return correlationProperties;
        }
        else if (externalIdentifier != null)
        {
            handleMissingScope(externalIdentifier, methodName);
        }

        return correlationProperties;
    }


    /**
     * Set up the correlation properties for request.
     *
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalIdentifier unique identifier of the glossary in the external asset manager
     * @param externalIdentifierName name of property for the external identifier in the external asset manager
     * @param externalIdentifierUsage optional usage description for the external identifier when calling the external asset manager
     * @param externalIdentifierSource component that issuing this request.
     * @param externalIdentifierKeyPattern pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param methodName calling method
     * @return filled out correlation properties
     * @throws InvalidParameterException missing external identifier
     */
    MetadataCorrelationProperties getCorrelationProperties(String              assetManagerGUID,
                                                           String              assetManagerName,
                                                           String              externalIdentifier,
                                                           String              externalIdentifierName,
                                                           String              externalIdentifierUsage,
                                                           String              externalIdentifierSource,
                                                           KeyPattern          externalIdentifierKeyPattern,
                                                           Map<String, String> mappingProperties,
                                                           String              methodName) throws InvalidParameterException
    {
        MetadataCorrelationProperties correlationProperties = new MetadataCorrelationProperties();

        if (assetManagerGUID != null)
        {
            invalidParameterHandler.validateName(externalIdentifier, externalIdentifierParameterName, methodName);

            correlationProperties.setAssetManagerGUID(assetManagerGUID);
            correlationProperties.setAssetManagerName(assetManagerName);
            correlationProperties.setKeyPattern(KeyPattern.LOCAL_KEY);
            correlationProperties.setExternalIdentifier(externalIdentifier);
            correlationProperties.setExternalIdentifierName(externalIdentifierName);
            correlationProperties.setExternalIdentifierUsage(externalIdentifierUsage);
            correlationProperties.setExternalIdentifierSource(externalIdentifierSource);
            correlationProperties.setKeyPattern(externalIdentifierKeyPattern);
            correlationProperties.setMappingProperties(mappingProperties);
        }
        else if (externalIdentifier != null)
        {
            handleMissingScope(externalIdentifier, methodName);
        }

        return correlationProperties;
    }


    /**
     * Return the asset manager identifiers packaged in an appropriate request body (or null is assetManagerGUID is null).
     *
     * @param assetManagerGUID unique identifier for the asset manager
     * @param assetManagerName unique name for the asset manager
     * @return request body
     */
    AssetManagerIdentifiersRequestBody getAssetManagerIdentifiersRequestBody(String assetManagerGUID,
                                                                             String assetManagerName)
    {
        AssetManagerIdentifiersRequestBody requestBody = new AssetManagerIdentifiersRequestBody();

        if (assetManagerGUID != null)
        {
            requestBody.setAssetManagerGUID(assetManagerGUID);
            requestBody.setAssetManagerName(assetManagerName);
        }

        return requestBody;
    }
}
