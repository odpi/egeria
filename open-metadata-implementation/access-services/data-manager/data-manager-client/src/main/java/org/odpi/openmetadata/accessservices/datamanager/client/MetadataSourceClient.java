/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.client;

import org.odpi.openmetadata.accessservices.datamanager.api.MetadataSourceInterface;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * MetadataSourceClient is the client for setting up the SoftwareServerCapabilities that represent metadata sources.
 */
public class MetadataSourceClient extends DataManagerBaseClient implements MetadataSourceInterface
{
    private final String urlTemplatePrefix = "/servers/{0}/open-metadata/access-services/data-manager/users/{1}/metadata-sources";


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public MetadataSourceClient(String   serverName,
                                String   serverPlatformURLRoot,
                                AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public MetadataSourceClient(String serverName,
                                String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
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
    public MetadataSourceClient(String   serverName,
                                String   serverPlatformURLRoot,
                                String   userId,
                                String   password,
                                AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
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
    public MetadataSourceClient(String serverName,
                                String serverPlatformURLRoot,
                                String userId,
                                String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
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
    public MetadataSourceClient(String                serverName,
                                String                serverPlatformURLRoot,
                                DataManagerRESTClient restClient,
                                int                   maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /* ========================================================
     * The metadata source represents the third party technology this integration processing is connecting to
     */

    /**
     * Create information about the component that manages APIs.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param apiManagerProperties description of the API manager (specify qualified name at a minimum)
     *
     * @return unique identifier of the API manager's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public String createAPIManager(String               userId,
                                   String               externalSourceGUID,
                                   String               externalSourceName,
                                   APIManagerProperties apiManagerProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName                  = "createAPIManager";
        final String propertiesParameterName     = "apiManagerProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(apiManagerProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(apiManagerProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/api-managers";

        APIManagerRequestBody requestBody = new APIManagerRequestBody(apiManagerProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create information about the integration daemon that is managing the acquisition of metadata from the
     * data manager.  Typically, this is Egeria's data manager integrator OMIS.
     *
     * @param userId calling user
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param databaseManagerProperties description of the integration daemon (specify qualified name at a minimum)
     *
     * @return unique identifier of the integration daemon's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public String createDatabaseManager(String                     userId,
                                        String                     externalSourceGUID,
                                        String                     externalSourceName,
                                        DatabaseManagerProperties  databaseManagerProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName                  = "createDatabaseManager";
        final String propertiesParameterName     = "databaseManagerProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(databaseManagerProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(databaseManagerProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/database-managers";

        DatabaseManagerRequestBody requestBody = new DatabaseManagerRequestBody(databaseManagerProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create information about the integration daemon that is managing the acquisition of metadata from the
     * data manager.  Typically, this is Egeria's data manager proxy.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param eventBrokerProperties description of the event broker (specify qualified name at a minimum)
     *
     * @return unique identifier of the event broker's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public String createEventBroker(String                userId,
                                    String                externalSourceGUID,
                                    String                externalSourceName,
                                    EventBrokerProperties eventBrokerProperties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName                  = "createEventBroker";
        final String propertiesParameterName     = "eventBrokerProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(eventBrokerProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(eventBrokerProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/event-brokers";

        EventBrokerRequestBody requestBody = new EventBrokerRequestBody(eventBrokerProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create information about a File System that is being used to store data files.
     *
     * @param userId calling user
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param fileSystemProperties description of the file system
     *
     * @return unique identifier of the file system's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public String  createFileSystem(String               userId,
                                    String               externalSourceGUID,
                                    String               externalSourceName,
                                    FileSystemProperties fileSystemProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                  = "createFileSystem";
        final String propertiesParameterName     = "fileSystemProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(fileSystemProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(fileSystemProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        FileSystemRequestBody requestBody = new FileSystemRequestBody(fileSystemProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/filesystems";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create information about an application that manages a collection of data files.
     *
     * @param userId calling user
     * @param externalSourceGUID        guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software server capability entity that represented the external source
     * @param fileManagerProperties description of the
     *
     * @return unique identifier of the file manager's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public String  createFileManager(String                userId,
                                     String                externalSourceGUID,
                                     String                externalSourceName,
                                     FileManagerProperties fileManagerProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName                  = "createFileManager";
        final String propertiesParameterName     = "fileManagerProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(fileManagerProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(fileManagerProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        FileManagerRequestBody requestBody = new FileManagerRequestBody(fileManagerProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/file-managers";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create information about an application.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param applicationProperties description of the application (specify qualified name at a minimum)
     *
     * @return unique identifier of the application's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public String  createApplication(String                userId,
                                     String                externalSourceGUID,
                                     String                externalSourceName,
                                     ApplicationProperties applicationProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName                  = "createApplication";
        final String propertiesParameterName     = "applicationProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(applicationProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(applicationProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        ApplicationRequestBody requestBody = new ApplicationRequestBody(applicationProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/applications";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create information about a data processing engine - set up typeName in the properties to create subtypes such as
     * ReportingEngine, WorkflowEngine, AnalyticsEngine, DataMovementEngine or DataVirtualizationEngine.
     *
     * @param userId calling user
     * @param externalSourceGUID   guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName   name of the software server capability entity that represented the external source
     * @param engineProperties description of the engine (specify qualified name at a minimum)
     *
     * @return unique identifier of the engine's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public String  createDataProcessingEngine(String                         userId,
                                              String                         externalSourceGUID,
                                              String                         externalSourceName,
                                              DataProcessingEngineProperties engineProperties) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName                  = "createDataProcessingEngine";
        final String propertiesParameterName     = "engineProperties";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(engineProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(engineProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        DataProcessingEngineRequestBody requestBody = new DataProcessingEngineRequestBody(engineProperties);

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-processing-engines";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }



    /**
     * Retrieve the unique identifier of the data manager.
     *
     * @param userId calling user
     * @param qualifiedName unique name of the integration daemon
     *
     * @return unique identifier of the data manager's software server capability
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public String getMetadataSourceGUID(String  userId,
                                        String  qualifiedName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName                  = "getMetadataSourceGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/by-name";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(qualifiedName);
        requestBody.setNamePropertyName(qualifiedNameParameterName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }
}
