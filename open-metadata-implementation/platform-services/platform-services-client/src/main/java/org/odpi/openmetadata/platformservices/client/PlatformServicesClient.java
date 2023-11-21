/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.platformservices.client;


import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.rest.PlatformSecurityRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.serveroperations.properties.ServerServicesStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerStatus;
import org.odpi.openmetadata.platformservices.rest.ServerListResponse;
import org.odpi.openmetadata.serveroperations.rest.OMAGServerStatusResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerServicesListResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerStatusResponse;
import org.odpi.openmetadata.serveroperations.rest.SuccessMessageResponse;


import java.util.List;

/**
 * PlatformServicesClient is the client for issuing queries to the OMAG Server Platform platform-services interface
 */
public class PlatformServicesClient
{
    private final PlatformServicesRESTClient restClient;                 /* Initialized in constructor */

    private final String                     platformRootURL;            /* Initialized in constructor */
    protected     AuditLog                   auditLog;                   /* Initialized in constructor */

    private final String                     retrieveURLTemplatePrefix   = "/open-metadata/platform-services/users/{1}/server-platform";

    private final InvalidParameterHandler    invalidParameterHandler     = new InvalidParameterHandler();

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

        return restClient.callStringGetRESTCall(methodName, urlTemplate, userId);
    }

    
    /**
     * Set up a platform security connector.  This connector provides additional authorization
     * checks on API requests to the platform.
     *
     * @param userId calling user
     * @param connection connection object that defines the platform security connector
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void setPlatformSecurityConnection(String     userId,
                                              Connection connection) throws UserNotAuthorizedException,
                                                                            InvalidParameterException,
                                                                            PropertyServerException
    {
        final String methodName    = "setPlatformSecurityConnection";
        final String parameterName = "connection";
        final String urlTemplate   = platformRootURL + retrieveURLTemplatePrefix + "/security/connection";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateConnection(connection, parameterName, methodName);
        
        PlatformSecurityRequestBody requestBody = new PlatformSecurityRequestBody();

        requestBody.setPlatformSecurityConnection(connection);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, userId);
    }


    /**
     * Clear the connection object for platform security.  This means there is no platform security set up
     * and there will be no authorization checks within the platform.  All security will have to
     * come from the surrounding deployment environment.
     * This is the default state.
     *
     * @param userId calling user
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void clearPlatformSecurityConnection(String userId) throws UserNotAuthorizedException, 
                                                                      InvalidParameterException, 
                                                                      PropertyServerException
    {
        final String methodName  = "clearPlatformSecurityConnection";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/security/connection";

        invalidParameterHandler.validateUserId(userId, methodName);

        restClient.callVoidDeleteRESTCall(methodName, urlTemplate, userId);
    }


    /**
     * Return the connection object for platform security connector.  Null is returned if no platform security
     * has been set up.
     *
     * @param userId calling user
     * @return Platform security connection
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public Connection getPlatformSecurityConnection(String userId) throws UserNotAuthorizedException, 
                                                                          InvalidParameterException, 
                                                                          PropertyServerException
    {
        final String methodName  = "getPlatformSecurityConnection";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/security/connection";

        ConnectionResponse restResult = restClient.callConnectionGetRESTCall(methodName, urlTemplate, userId);

        return restResult.getConnection();
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

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/connector-types/{1}";

        ConnectorTypeResponse restResult = restClient.callConnectorTypeGetRESTCall(methodName, urlTemplate, userId, connectorProviderClassName);

        return restResult.getConnectorType();
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
     * Retrieve a list of the view services registered on the platform
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
     * Retrieve a list of the governance services supported on the platform
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
     * Retrieve a list of the common services supported on the platform
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
     * Retrieve a list of the services known on the platform
     *
     * @param userId calling user
     *
     * @return List of common services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getAllServices(String   userId) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "getAllServices";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/registered-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName, urlTemplate, userId);

        return restResult.getServices();
    }


    /*
     * ========================================================================================
     * Activate and deactivate the open metadata and governance capabilities in the OMAG Server
     */

    /**
     * Activate the Open Metadata and Governance (OMAG) server using the configuration document stored for this server.
     *
     * @param userId calling user
     * @param serverName server to start
     * @return success message
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the server.
     */
    public String activateWithStoredConfig(String userId,
                                           String serverName) throws UserNotAuthorizedException, 
                                                                     InvalidParameterException, 
                                                                     PropertyServerException
    {
        final String methodName  = "activateWithStoredConfig";
        final String serverNameParameter  = "serverName";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{1}/instance";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        SuccessMessageResponse restResult = restClient.callSuccessMessagePostRESTCall(methodName, urlTemplate, null, userId, serverName);

        return restResult.getSuccessMessage();
    }


    /**
     * Activate the open metadata and governance services using the supplied configuration
     * document.
     *
     * @param userId calling user
     * @param configuration  properties used to initialize the services
     * @return success message
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the server.
     */
    public String activateWithSuppliedConfig(String           userId,
                                             OMAGServerConfig configuration) throws UserNotAuthorizedException,
                                                                                    InvalidParameterException,
                                                                                    PropertyServerException
    {
        final String methodName  = "activateWithSuppliedConfig";
        final String parameterName = "configuration";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{1}/instance/configuration";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(configuration, parameterName, methodName);

        SuccessMessageResponse restResult = restClient.callSuccessMessagePostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      configuration,
                                                                                      userId,
                                                                                      configuration.getLocalServerName());

        return restResult.getSuccessMessage();
    }


    /**
     * Temporarily deactivate any open metadata and governance services.
     *
     * @param userId calling user
     * @param serverName server to start
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void shutdownServer(String userId,
                               String serverName) throws UserNotAuthorizedException,
                                                         InvalidParameterException,
                                                         PropertyServerException
    {
        final String methodName  = "shutdownServer";
        final String serverNameParameter  = "serverName";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{1}/instance";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        restClient.callVoidDeleteRESTCall(methodName, urlTemplate, userId, serverName);
    }


    /**
     * Temporarily shutdown all running servers.
     *
     * @param userId  user that is issuing the request
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void shutdownAllServers(String  userId) throws UserNotAuthorizedException,
                                                          InvalidParameterException,
                                                          PropertyServerException
    {
        final String methodName  = "shutdownAllServers";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/instance";

        invalidParameterHandler.validateUserId(userId, methodName);

        restClient.callVoidDeleteRESTCall(methodName, urlTemplate, userId);
    }


    /**
     * Permanently deactivate any open metadata and governance services and unregister from
     * any cohorts.
     *
     * @param userId calling user
     * @param serverName server to start
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */

    public void shutdownAndUnregisterServer(String userId,
                                            String serverName) throws UserNotAuthorizedException,
                                                                      InvalidParameterException,
                                                                      PropertyServerException
    {
        final String methodName  = "shutdownAndUnregisterServer";
        final String serverNameParameter  = "serverName";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{1}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        restClient.callVoidDeleteRESTCall(methodName, urlTemplate, userId, serverName);
    }


    /**
     * Shutdown any active servers and unregister them from
     * any cohorts.
     *
     * @param userId  user that is issuing the request
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void shutdownAndUnregisterAllServers(String  userId) throws UserNotAuthorizedException,
                                                                       InvalidParameterException,
                                                                       PropertyServerException
    {
        final String methodName  = "shutdownAndUnregisterAllServers";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers";

        invalidParameterHandler.validateUserId(userId, methodName);

        restClient.callVoidDeleteRESTCall(methodName, urlTemplate, userId);
    }


    /**
     * Shutdown the platform.
     *
     * @param userId  user that is issuing the request
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
     public void shutdownPlatform(String  userId) throws UserNotAuthorizedException,
                                                         InvalidParameterException,
                                                         PropertyServerException
     {
         final String methodName  = "shutdownPlatform";
         final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/instance";

         invalidParameterHandler.validateUserId(userId, methodName);

         restClient.callVoidDeleteRESTCall(methodName, urlTemplate, userId);
     }


    /*
     * =============================================================
     * Operational status and control
     */

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
     * Return a flag to indicate if this server has ever run on this OMAG Server Platform instance.
     *
     * @param userId calling user
     * @param serverName server of interest
     *
     * @return flag
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public boolean isServerKnown(String   userId,
                                 String   serverName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName = "isServerKnown";

        invalidParameterHandler.validateUserId(userId, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/is-known";

        BooleanResponse restResult = restClient.callBooleanGetRESTCall(methodName, urlTemplate, userId);

        return restResult.getFlag();
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
     * Return the configuration used for the current active instance of the server.  Null is returned if
     * the server instance is not running.
     *
     * @param userId calling user
     * @param serverName server to start
     * @return configuration properties used to initialize the server or null if not running
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public OMAGServerConfig getActiveConfiguration(String userId,
                                                   String serverName) throws UserNotAuthorizedException,
                                                                             InvalidParameterException,
                                                                             PropertyServerException
    {
        final String methodName  = "getActiveConfiguration";
        final String serverNameParameter  = "serverName";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{1}/instance/configuration";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        OMAGServerConfigResponse restResult = restClient.callOMAGServerConfigGetRESTCall(methodName, urlTemplate, userId, serverName);

        return restResult.getOMAGServerConfig();
    }



    /**
     * Return the status of a running server (use platform services to find out if the server is running).
     *
     * @param userId calling user
     * @param serverName server to start
     * @return status of the server
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public ServerServicesStatus getActiveServerStatus(String userId,
                                                      String serverName) throws UserNotAuthorizedException,
                                                                                InvalidParameterException,
                                                                                PropertyServerException
    {
        final String methodName  = "getActiveServerStatus";
        final String serverNameParameter  = "serverName";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{1}/instance/status";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        OMAGServerStatusResponse restResult = restClient.callOMAGServerStatusGetRESTCall(methodName, urlTemplate, userId, serverName);

        return restResult.getServerStatus();
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
        final String serverNameParameter = "serverName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/"+serverName+"/services";

        ServerServicesListResponse restResult = restClient.callServiceListGetRESTCall(methodName, urlTemplate, userId);

        return restResult.getServerServicesList();
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param userId calling user
     * @param serverName server to start
     * @param fileName name of the open metadata archive file.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void addOpenMetadataArchiveFile(String userId,
                                           String serverName,
                                           String fileName) throws UserNotAuthorizedException,
                                                                   InvalidParameterException,
                                                                   PropertyServerException
    {
        final String methodName    = "addOpenMetadataArchiveFile";
        final String parameterName = "fileName";
        final String serverNameParameter  = "serverName";
        final String urlTemplate   = platformRootURL + retrieveURLTemplatePrefix + "/servers/{1}/instance/open-metadata-archives/file";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);
        invalidParameterHandler.validateName(fileName, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, fileName, userId, serverName);
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param userId calling user
     * @param serverName server to start
     * @param connection connection for the open metadata archive.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void addOpenMetadataArchive(String     userId,
                                       String     serverName,
                                       Connection connection) throws UserNotAuthorizedException,
                                                                     InvalidParameterException,
                                                                     PropertyServerException
    {
        final String methodName    = "addOpenMetadataArchiveFile";
        final String parameterName = "connection";
        final String serverNameParameter  = "serverName";
        final String urlTemplate   = platformRootURL + retrieveURLTemplatePrefix + "/servers/{1}/instance/open-metadata-archives/connection";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);
        invalidParameterHandler.validateConnection(connection, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, connection, userId, serverName);
    }
}
