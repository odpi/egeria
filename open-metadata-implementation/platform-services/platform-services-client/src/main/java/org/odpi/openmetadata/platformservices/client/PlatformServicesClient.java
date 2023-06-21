/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.platformservices.client;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.platformservices.properties.ServerStatus;
import org.odpi.openmetadata.platformservices.rest.ServerListResponse;
import org.odpi.openmetadata.platformservices.rest.ServerServicesListResponse;
import org.odpi.openmetadata.platformservices.rest.ServerStatusResponse;

import java.util.List;

/**
 * PlatformServicesClient is the client for issuing queries to the OMAG Server Platform platform-services interface
 */
public class PlatformServicesClient
{
    private final PlatformServicesRESTClient restClient;                 /* Initialized in constructor */

    private final String                     platformRootURL;            /* Initialized in constructor */
    protected     AuditLog                   auditLog;                   /* Initialized in constructor */

    private final String                retrieveURLTemplatePrefix   = "/open-metadata/platform-services/users/{1}/server-platform";

    private final InvalidParameterHandler   invalidParameterHandler     = new InvalidParameterHandler();

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param platformName name of the platform to connect to
     * @param platformRootURL the network address of the server running the OMAG Platform
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public PlatformServicesClient(String   platformName,
                                  String   platformRootURL,
                                  AuditLog auditLog) throws InvalidParameterException
    {
        this.platformRootURL = platformRootURL;
        this.restClient      = new PlatformServicesRESTClient(platformName, platformRootURL, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param platformName name of the platform to connect to
     * @param platformRootURL the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public PlatformServicesClient(String platformName,
                                  String platformRootURL) throws InvalidParameterException
    {
        this.platformRootURL = platformRootURL;
        this.restClient      = new PlatformServicesRESTClient(platformName, platformRootURL);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param platformName name of the platform to connect to
     * @param platformRootURL the network address of the platform
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public PlatformServicesClient(String   platformName,
                                  String   platformRootURL,
                                  String   userId,
                                  String   password,
                                  AuditLog auditLog) throws InvalidParameterException
    {
        this.platformRootURL = platformRootURL;
        this.restClient      = new PlatformServicesRESTClient(platformName, platformRootURL, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param platformName name of the server to connect to
     * @param platformRootURL the network address of the platform
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public PlatformServicesClient(String platformName,
                                  String platformRootURL,
                                  String userId,
                                  String password) throws InvalidParameterException
    {
        this.platformRootURL = platformRootURL;
        this.restClient      = new PlatformServicesRESTClient(platformName, platformRootURL, userId, password);
    }


    /**
     * Retrieve the platform origin
     *
     * @param userId calling user
     *
     * @return List of server names
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String getPlatformOrigin(String   userId) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String methodName = "getPlatformOrigin";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/origin";

        String restResult = restClient.callStringGetRESTCall(methodName, urlTemplate, userId);

        return restResult; // .getResultString();
    }


    /**
     * Return the connector type for the requested connector provider after validating that the
     * connector provider is available on the OMAGServerPlatform's class path.  This method is for tools that are configuring
     * connectors into an Egeria server.  It does not validate that the connector will load and initialize.
     *
     * @param userId calling user
     * @param connectorProviderClassName name of the connector provider class
     * @return ConnectorType bean or exceptions that occur when trying to create the connector
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */

    public ConnectorType getConnectorType(String userId,
                                          String connectorProviderClassName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "getConnectorType";
        final String connectorProviderParameterName = "connectorProviderClassName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(connectorProviderClassName, connectorProviderParameterName, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/connector-types/{1}" + connectorProviderClassName;

        ConnectorTypeResponse restResult = restClient.callConnectorTypeGetRESTCall(methodName, urlTemplate, userId, connectorProviderClassName);

        return restResult.getConnectorType();
    }


    /**
     * Retrieve the server status
     *
     * @param userId calling user
     * @param serverName the name of the server
     *
     * @return The server status
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ServerStatus getServerStatus(String userId,
                                        String serverName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "getServerStatus";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/"+serverName+"/status";

        ServerStatusResponse restResult = restClient.callServerStatusGetRESTCall(methodName, urlTemplate, userId);

        ServerStatus serverStatus = new ServerStatus();
        serverStatus.setServerName(restResult.getServerName());
        serverStatus.setIsActive(restResult.isActive());
        serverStatus.setServerStartTime(restResult.getServerStartTime());
        serverStatus.setServerEndTime(restResult.getServerEndTime());
        serverStatus.setServerHistory(restResult.getServerHistory());

        return serverStatus;
    }


    /**
     * Retrieve a list of the active services on a server
     *
     * @param userId calling user
     * @param serverName name of the server
     *
     * @return List of service names
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<String> getActiveServices(String   userId,
                                          String   serverName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "getActiveServices";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/"+serverName+"/services";

        ServerServicesListResponse restResult = restClient.callServiceListGetRESTCall(methodName, urlTemplate, userId);

        return restResult.getServerServicesList();
    }


    /**
     * Retrieve a list of the active servers on the platform
     *
     * @param userId calling user
     *
     * @return List of server names
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<String> getActiveServers(String   userId) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "getActiveServers";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/active";

        ServerListResponse restResult = restClient.callServerListGetRESTCall(methodName, urlTemplate, userId);

        return restResult.getServerList();
    }


    /**
     * Retrieve a list of the known servers on the platform
     *
     * @param userId calling user
     *
     * @return List of server names
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<String> getKnownServers(String   userId) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName = "getKnownServers";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers";

        ServerListResponse restResult = restClient.callServerListGetRESTCall(methodName, urlTemplate, userId);

        return restResult.getServerList();
    }


    /**
     * Retrieve a list of the access services registered on the platform
     *
     * @param userId calling user
     *
     * @return List of access services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getAccessServices(String   userId) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "getAccessServices";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/registered-services/access-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName, urlTemplate, userId);

        return restResult.getServices();
    }


    /**
     * Retrieve a list of the engine services registered on the platform
     *
     * @param userId calling user
     *
     * @return List of engine services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getEngineServices(String   userId) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "getEngineServices";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/registered-services/engine-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName, urlTemplate, userId);

        return restResult.getServices();
    }


    /**
     * Retrieve a list of the access services registered on the platform
     *
     * @param userId calling user
     *
     * @return List of view services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getViewServices(String   userId) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "getViewServices";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/registered-services/view-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName, urlTemplate, userId);

        return restResult.getServices();
    }


    /**
     * Retrieve a list of the governance services registered on the platform
     *
     * @param userId calling user
     *
     * @return List of governance services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getGovernanceServices(String   userId) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getGovernanceServices";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/registered-services/governance-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName, urlTemplate, userId);

        return restResult.getServices();
    }

    /**
     * Retrieve a list of the integration services registered on the platform
     *
     * @param userId calling user
     *
     * @return List of integration services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getIntegrationServices(String   userId) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getIntegrationServices";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/registered-services/integration-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName, urlTemplate, userId);

        return restResult.getServices();
    }


    /**
     * Retrieve a list of the common services registered on the platform
     *
     * @param userId calling user
     *
     * @return List of common services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getCommonServices(String   userId) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "getCommonServices";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/registered-services/common-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName, urlTemplate, userId);

        return restResult.getServices();
    }


    /**
     * Retrieve a list of the common services registered on the platform
     *
     * @param userId calling user
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void shutdownPlatform(String userId) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        final String methodName = "shutdownPlatform";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/shutdown";

        restClient.callVoidPostRESTCall(methodName, urlTemplate, userId);
    }
}
