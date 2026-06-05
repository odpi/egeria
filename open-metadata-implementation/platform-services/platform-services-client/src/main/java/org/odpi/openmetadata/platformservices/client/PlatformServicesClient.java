/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.platformservices.client;


import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataSecurityAccessControl;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataUserAccount;
import org.odpi.openmetadata.platformservices.properties.PublicProperties;
import org.odpi.openmetadata.platformservices.properties.BuildProperties;
import org.odpi.openmetadata.serveroperations.properties.ServerServicesStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerStatus;
import org.odpi.openmetadata.platformservices.rest.ServerListResponse;
import org.odpi.openmetadata.serveroperations.rest.OMAGServerStatusResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerServicesListResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerStatusResponse;
import org.odpi.openmetadata.serveroperations.rest.SuccessMessageResponse;


import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * PlatformServicesClient is the client for issuing queries to the OMAG Server Platform platform-services interface
 */
public class PlatformServicesClient
{
    private final PlatformServicesRESTClient restClient;                 /* Initialized in constructor */

    private final String                     platformRootURL;            /* Initialized in constructor */
    private final String                     delegatingUserId;           /* Initialized in the constructor */
    private final AuditLog                   auditLog;                   /* Initialized in constructor */

    private final String                     retrieveURLTemplatePrefix   = "/open-metadata/platform-services/server-platform";

    private final InvalidParameterHandler    invalidParameterHandler     = new InvalidParameterHandler();

    /**
     * Create a new client with bearer token from supplied secrets store.
     *
     * @param platformName name of the platform to connect to
     * @param platformRootURL the network address of the server running the OMAG Platform
     * @param secretStoreProvider class name of the secrets store
     * @param secretStoreLocation location (networkAddress) of the secrets store
     * @param secretStoreCollection name of the collection of secrets to use to connect to the remote server
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     * REST API calls.
     */
    public PlatformServicesClient(String   platformName,
                                  String   platformRootURL,
                                  String   secretStoreProvider,
                                  String   secretStoreLocation,
                                  String   secretStoreCollection,
                                  String   delegatingUserId,
                                  AuditLog auditLog) throws InvalidParameterException
    {
        this.platformRootURL  = platformRootURL;
        this.auditLog         = auditLog;
        this.delegatingUserId = delegatingUserId;
        this.restClient       = new PlatformServicesRESTClient(platformName, platformRootURL, secretStoreProvider, secretStoreLocation, secretStoreCollection, auditLog);
    }


    /**
     * Create a new client with bearer token from supplied secrets store.
     *
     * @param platformName name of the platform to connect to
     * @param platformRootURL the network address of the server running the OMAS REST services
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     * REST API calls.
     */
    public PlatformServicesClient(String                             platformName,
                                  String                             platformRootURL,
                                  Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                  String                             delegatingUserId,
                                  AuditLog                           auditLog) throws InvalidParameterException
    {
        this.platformRootURL  = platformRootURL;
        this.delegatingUserId = delegatingUserId;
        this.auditLog         = auditLog;
        this.restClient       = new PlatformServicesRESTClient(platformName, platformRootURL, secretsStoreConnectorMap, auditLog);
    }


    /**
     * Return the start time for this instance of the platform.
     *
     * @return start date/time
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public Date getPlatformStartTime() throws InvalidParameterException,
                                              PropertyServerException,
                                              UserNotAuthorizedException
    {
        final String methodName = "getPlatformStartTime";

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/start-time?delegatingUserId={0}";

        DateResponse restResult = restClient.callDateGetRESTCall(methodName, urlTemplate, delegatingUserId);

        return restResult.getDateValue();
    }


    /**
     * Return details of when the platform was built.
     *
     * @return start date/time
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public BuildProperties getPlatformBuildProperties() throws PropertyServerException
    {
        final String methodName = "getPlatformBuildProperties";

        final String urlTemplate = platformRootURL + "/api/about";

        return restClient.callBuildPropertiesGetRESTCall(methodName, urlTemplate);
    }


    /**
     * Return public details about the platform.
     *
     * @return start date/time
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public PublicProperties getPublicProperties() throws PropertyServerException
    {
        final String methodName = "getPublicProperties";

        final String urlTemplate = platformRootURL + "/api/public/app/info";

        return restClient.callPublicPropertiesGetRESTCall(methodName, urlTemplate);
    }


    /**
     * Retrieve the platform origin
     *
     * @return origin and version
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String getPlatformOrigin() throws InvalidParameterException,
                                             UserNotAuthorizedException,
                                             PropertyServerException
    {
        final String methodName = "getPlatformOrigin";
        
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/origin";

        return restClient.callStringGetRESTCall(methodName, urlTemplate);
    }

    
    /**
     * Set up a platform security connector.  This connector provides additional authorization
     * checks on API requests to the platform.
     *
     * @param connection connection object that defines the platform security connector
     * @throws UserNotAuthorizedException the supplied user id (from bearer token) is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void setPlatformSecurityConnection(Connection connection) throws UserNotAuthorizedException,
                                                                            InvalidParameterException,
                                                                            PropertyServerException
    {
        final String methodName    = "setPlatformSecurityConnection";
        final String parameterName = "connection";
        final String urlTemplate   = platformRootURL + retrieveURLTemplatePrefix + "/security/connection?delegatingUserId={0}";

        invalidParameterHandler.validateConnection(connection, parameterName, methodName);
        
        PlatformSecurityRequestBody requestBody = new PlatformSecurityRequestBody();

        requestBody.setPlatformSecurityConnection(connection);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, delegatingUserId);
    }


    /**
     * Return the connection object for platform security connector.  Null is returned if no platform security
     * has been set up.
     *
     * @return Platform security connection
     * @throws UserNotAuthorizedException the supplied user id (from bearer token) is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public Connection getPlatformSecurityConnection() throws UserNotAuthorizedException,
                                                             InvalidParameterException,
                                                             PropertyServerException
    {
        final String methodName  = "getPlatformSecurityConnection";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/security/connection?delegatingUserId={0}";

        OCFConnectionResponse restResult = restClient.callOCFConnectionGetRESTCall(methodName, urlTemplate, delegatingUserId);

        return restResult.getConnection();
    }


    /**
     * Clear the connection object for platform security.  This means there is no platform security set up
     * and there will be no authorization checks within the platform.  All security will have to
     * come from the surrounding deployment environment.
     * This is the default state.
     *
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void clearPlatformSecurityConnection() throws UserNotAuthorizedException,
                                                         InvalidParameterException,
                                                         PropertyServerException
    {
        final String methodName  = "clearPlatformSecurityConnection";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/security/connection?delegatingUserId={0}";

        restClient.callVoidDeleteRESTCall(methodName, urlTemplate, delegatingUserId);
    }


    /**
     * Set up details of a user account with the platform security connector.
     *
     * @param userAccount details about the user to update
     * @throws UserNotAuthorizedException the supplied user id (from bearer token) is not authorized to issue this command.
     * @throws InvalidParameterException  invalid parameter.
     * @throws PropertyServerException    unusual state in the platform.
     */
    public void setUserAccount(OpenMetadataUserAccount userAccount) throws UserNotAuthorizedException,
                                                                           InvalidParameterException,
                                                                           PropertyServerException
    {
        final String methodName    = "setUserAccount";
        final String parameterName = "userAccount";
        final String urlTemplate   = platformRootURL + retrieveURLTemplatePrefix + "/security/user-accounts?delegatingUserId={1}";

        invalidParameterHandler.validateObject(userAccount, parameterName, methodName);

        UserAccountRequestBody requestBody = new UserAccountRequestBody();

        requestBody.setUserAccount(userAccount);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, delegatingUserId);
    }


    /**
     * Return details of a user account registered with the platform security connector.
     *
     * @param accountUserId identifier for the user to update
     * @throws UserNotAuthorizedException the supplied user id (from bearer token) is not authorized to issue this command.
     * @throws InvalidParameterException  invalid parameter.
     * @throws PropertyServerException    unusual state in the platform.
     */
    public OpenMetadataUserAccount getUserAccount(String accountUserId) throws UserNotAuthorizedException,
                                                                        InvalidParameterException,
                                                                        PropertyServerException
    {
        final String methodName    = "getUserAccount";
        final String urlTemplate   = platformRootURL + retrieveURLTemplatePrefix + "/security/user-accounts/{0}?delegatingUserId={1}";

        invalidParameterHandler.validateUserId(accountUserId, methodName);

        UserAccountResponse response = restClient.callUserAccountResponseGetRESTCall(methodName, urlTemplate, accountUserId, delegatingUserId);

        return response.getUserAccount();
    }


    /**
     * Return the list of users registered with the platform security connector.
     *
     * @param status status of the user - or null for any status
     * @param type   type of user - or null for any type
     * @return list of userIds in the user directory
     * @throws UserNotAuthorizedException the supplied user id (from bearer token) is not authorized to issue this command.
     * @throws InvalidParameterException  invalid parameter.
     * @throws PropertyServerException    unusual state in the platform.
     */
    public List<String> getUserList(UserAccountStatus status,
                                    UserAccountType   type) throws UserNotAuthorizedException,
                                                                   InvalidParameterException,
                                                                   PropertyServerException
    {
        final String methodName = "getUserList";

        String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/security/user-list?delegatingUserId={0}";

        if (status != null)
        {
            urlTemplate =  urlTemplate + "&userAccountStatus=" + status.name();
        }

        if (type != null)
        {
            urlTemplate = urlTemplate + "&userAccountType=" + type.name();
        }

        NameListResponse response = restClient.callNameListGetRESTCall(methodName, urlTemplate, delegatingUserId);

        return response.getNames();
    }


    /**
     * Clear the account for a user with the platform security connector.
     *
     * @param accountUserId identifier for the user to update
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void deleteUserAccount(String accountUserId) throws UserNotAuthorizedException,
                                                               InvalidParameterException,
                                                               PropertyServerException
    {
        final String methodName  = "deleteUserAccount";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/security/user-accounts/{0}?delegatingUserId={1}";

        invalidParameterHandler.validateUserId(accountUserId, methodName);

        restClient.callVoidDeleteRESTCall(methodName, urlTemplate, accountUserId, delegatingUserId);
    }


    /**
     * Set up a new security access control or update an existing one.
     * This is control is registered with the platform security connector.  The user
     * requires operator permission for the platform.
     *
     * @param securityAccessControl details about the control to update
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */

    public void setSecurityAccessControl(OpenMetadataSecurityAccessControl securityAccessControl) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName    = "setSecurityAccessControl";
        final String parameterName = "securityAccessControl";
        final String urlTemplate   = platformRootURL + retrieveURLTemplatePrefix + "/security/security-access-controls?delegatingUserId={1}";

        invalidParameterHandler.validateObject(securityAccessControl, parameterName, methodName);

        SecurityAccessControlRequestBody requestBody = new SecurityAccessControlRequestBody();

        requestBody.setSecurityAccessControl(securityAccessControl);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, requestBody, delegatingUserId);
    }


    /**
     * Return details of a security access control registered with the platform security connector.
     *
     * @param controlName identifier for the control to retrieve
     * @throws UserNotAuthorizedException the supplied user id (from bearer token) is not authorized to issue this command.
     * @throws InvalidParameterException  invalid parameter.
     * @throws PropertyServerException    unusual state in the platform.
     */
    public OpenMetadataSecurityAccessControl getSecurityAccessControl(String controlName) throws UserNotAuthorizedException,
                                                                                                 InvalidParameterException,
                                                                                                 PropertyServerException
    {
        final String methodName    = "getSecurityAccessControl";
        final String urlTemplate   = platformRootURL + retrieveURLTemplatePrefix + "/security/security-access-controls/{0}?delegatingUserId={1}";
        final String controlNameParameterName = "controlName";

        invalidParameterHandler.validateName(controlName, controlNameParameterName, methodName);

        SecurityAccessControlResponse response = restClient.callSecurityAccessControlResponseGetRESTCall(methodName, urlTemplate, controlName, delegatingUserId);

        return response.getSecurityAccessControl();
    }


    /**
     * Clear the requested security access control with the platform security connector.
     *
     * @param controlName identifier for the control to remove
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void deleteSecurityAccessControl(String controlName) throws UserNotAuthorizedException,
                                                                       InvalidParameterException,
                                                                       PropertyServerException
    {
        final String methodName  = "deleteSecurityAccessControl";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/security/security-access-controls/{0}?delegatingUserId={1}";
        final String controlNameParameterName = "controlName";

        invalidParameterHandler.validateName(controlName, controlNameParameterName, methodName);

        restClient.callVoidDeleteRESTCall(methodName, urlTemplate, controlName, delegatingUserId);
    }



    /**
     * Return the connector type for the requested connector provider after validating that the
     * connector provider is available on the OMAGServerPlatform's class path.  This method is for tools that are configuring
     * connectors into an Egeria server.  It does not validate that the connector will load and initialize.
     *
     * @param connectorProviderClassName name of the connector provider class
     * @return ConnectorType bean or exceptions that occur when trying to create the connector
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id (from bearer token) is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */

    public ConnectorType getConnectorType(String connectorProviderClassName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "getConnectorType";
        final String connectorProviderParameterName = "connectorProviderClassName";

        invalidParameterHandler.validateName(connectorProviderClassName, connectorProviderParameterName, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/connector-types/{0}?delegatingUserId={1}";

        OCFConnectorTypeResponse restResult = restClient.callOCFConnectorTypeGetRESTCall(methodName, urlTemplate, connectorProviderClassName, delegatingUserId);

        return restResult.getConnectorType();
    }


    /**
     * Retrieve a list of the access services registered on the platform
     *
     * @return List of access services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user user id (from bearer token) is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getAccessServices() throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "getAccessServices";

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/registered-services/access-services?delegatingUserId={0}";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName, urlTemplate, delegatingUserId);

        return restResult.getServices();
    }


    /**
     * Retrieve a list of the engine services registered on the platform
     *
     * @return List of engine services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getEngineServices() throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "getEngineServices";

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/registered-services/engine-services?delegatingUserId={0}";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName, urlTemplate, delegatingUserId);

        return restResult.getServices();
    }


    /**
     * Retrieve a list of the view services registered on the platform
     *
     * @return List of view services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getViewServices() throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName = "getViewServices";

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/registered-services/view-services?delegatingUserId={0}";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName, urlTemplate, delegatingUserId);

        return restResult.getServices();
    }


    /**
     * Retrieve a list of the governance services supported on the platform
     *
     * @return List of governance services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getGovernanceServices() throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "getGovernanceServices";

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/registered-services/governance-services?delegatingUserId={0}";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName, urlTemplate, delegatingUserId);

        return restResult.getServices();
    }


    /**
     * Retrieve a list of the common services supported on the platform
     *
     * @return List of common services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getCommonServices() throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "getCommonServices";


        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/registered-services/common-services?delegatingUserId={0}";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName, urlTemplate, delegatingUserId);

        return restResult.getServices();
    }


    /**
     * Retrieve a list of the services known on the platform
     *
     * @return List of common services
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<RegisteredOMAGService> getAllServices() throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName = "getAllServices";
        
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/registered-services?delegatingUserId={0}";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName, urlTemplate, delegatingUserId);

        return restResult.getServices();
    }


    /*
     * ========================================================================================
     * Activate and deactivate the open metadata and governance capabilities in the OMAG Server
     */

    /**
     * Activate the Open Metadata and Governance (OMAG) server using the configuration document stored for this server.
     *
     * @param serverName server to start
     * @return success message
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the server.
     */
    public String activateWithStoredConfig(String serverName) throws UserNotAuthorizedException,
                                                                     InvalidParameterException, 
                                                                     PropertyServerException
    {
        final String methodName  = "activateWithStoredConfig";
        final String serverNameParameter  = "serverName";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{0}/instance?delegatingUserId={1}";

        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        SuccessMessageResponse restResult = restClient.callSuccessMessagePostRESTCall(methodName, urlTemplate, null, serverName, delegatingUserId);

        return restResult.getSuccessMessage();
    }


    /**
     * Activate the open metadata and governance services using the supplied configuration
     * document.
     *
     * @param configuration  properties used to initialize the services
     * @return success message
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the server.
     */
    public String activateWithSuppliedConfig(OMAGServerConfig configuration) throws UserNotAuthorizedException,
                                                                                    InvalidParameterException,
                                                                                    PropertyServerException
    {
        final String methodName  = "activateWithSuppliedConfig";
        final String parameterName = "configuration";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{0}/instance/configuration?delegatingUserId={1}";

        invalidParameterHandler.validateObject(configuration, parameterName, methodName);

        SuccessMessageResponse restResult = restClient.callSuccessMessagePostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      configuration,
                                                                                      configuration.getLocalServerName(),
                                                                                      delegatingUserId);

        return restResult.getSuccessMessage();
    }


    /**
     * Temporarily deactivate any open metadata and governance services.
     *
     * @param serverName server to start
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void shutdownServer(String serverName) throws UserNotAuthorizedException,
                                                         InvalidParameterException,
                                                         PropertyServerException
    {
        final String methodName  = "shutdownServer";
        final String serverNameParameter  = "serverName";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{0}/instance?delegatingUserId={1}";

        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        restClient.callVoidDeleteRESTCall(methodName, urlTemplate, serverName, delegatingUserId);
    }


    /**
     * Temporarily shutdown all running servers.
     *
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void shutdownAllServers() throws UserNotAuthorizedException,
                                            InvalidParameterException,
                                            PropertyServerException
    {
        final String methodName  = "shutdownAllServers";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/instance?delegatingUserId={0}";

        restClient.callVoidDeleteRESTCall(methodName, urlTemplate, delegatingUserId);
    }


    /**
     * Permanently deactivate any open metadata and governance services and unregister from
     * any cohorts.
     *
     * @param serverName server to start
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */

    public void shutdownAndUnregisterServer(String serverName) throws UserNotAuthorizedException,
                                                                      InvalidParameterException,
                                                                      PropertyServerException
    {
        final String methodName  = "shutdownAndUnregisterServer";
        final String serverNameParameter  = "serverName";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{0}?delegatingUserId={1}";

        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        restClient.callVoidDeleteRESTCall(methodName, urlTemplate, serverName, delegatingUserId);
    }


    /**
     * Shutdown any active servers and unregister them from
     * any cohorts.
     *
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void shutdownAndUnregisterAllServers() throws UserNotAuthorizedException,
                                                         InvalidParameterException,
                                                         PropertyServerException
    {
        final String methodName  = "shutdownAndUnregisterAllServers";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers?delegatingUserId={0}";

        restClient.callVoidDeleteRESTCall(methodName, urlTemplate, delegatingUserId);
    }


    /**
     * Shutdown the platform.
     *
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
     public void shutdownPlatform() throws UserNotAuthorizedException,
                                           InvalidParameterException,
                                           PropertyServerException
     {
         final String methodName  = "shutdownPlatform";
         final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/instance?delegatingUserId={0}";

         restClient.callVoidDeleteRESTCall(methodName, urlTemplate, delegatingUserId);
     }


    /*
     * =============================================================
     * Operational status and control
     */

    /**
     * Retrieve a list of the known servers on the platform
     *
     * @return List of server names
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<String> getKnownServers() throws InvalidParameterException,
                                                 UserNotAuthorizedException,
                                                 PropertyServerException
    {
        final String methodName = "getKnownServers";

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers?delegatingUserId={0}";

        ServerListResponse restResult = restClient.callServerListGetRESTCall(methodName, urlTemplate, delegatingUserId);

        return restResult.getServerList();
    }


    /**
     * Return a flag to indicate if this server has ever run on this OMAG Server Platform instance.
     *
     * @param serverName server of interest
     *
     * @return flag
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public boolean isServerKnown(String   serverName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName = "isServerKnown";
        
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{0}/is-known?delegatingUserId={1}";

        BooleanResponse restResult = restClient.callBooleanGetRESTCall(methodName, urlTemplate, serverName, delegatingUserId);

        return restResult.getFlag();
    }


    /**
     * Retrieve a list of the active servers on the platform
     *
     * @return List of server names
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<String> getActiveServers() throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException
    {
        final String methodName = "getActiveServers";


        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/active?delegatingUserId={0}";

        ServerListResponse restResult = restClient.callServerListGetRESTCall(methodName, urlTemplate, delegatingUserId);

        return restResult.getServerList();
    }


    /**
     * Retrieve the server status
     *
     * @param serverName the name of the server
     *
     * @return The server status
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public ServerStatus getServerStatus(String serverName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "getServerStatus";
        final String serverNameParameter  = "serverName";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{0}/status?delegatingUserId={1}";

        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        ServerStatusResponse restResult = restClient.callServerStatusGetRESTCall(methodName, urlTemplate, serverName, delegatingUserId);

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
     * @param serverName server to start
     * @return configuration properties used to initialize the server or null if not running
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public OMAGServerConfig getActiveConfiguration(String serverName) throws UserNotAuthorizedException,
                                                                             InvalidParameterException,
                                                                             PropertyServerException
    {
        final String methodName  = "getActiveConfiguration";
        final String serverNameParameter  = "serverName";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{0}/instance/configuration?delegatingUserId={1}";

        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        OMAGServerConfigResponse restResult = restClient.callOMAGServerConfigGetRESTCall(methodName, urlTemplate, serverName, delegatingUserId);

        return restResult.getOMAGServerConfig();
    }


    /**
     * Return the status of a running server (use platform services to find out if the server is running).
     *
     * @param serverName server to start
     * @return status of the server
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public ServerServicesStatus getActiveServerStatus(String serverName) throws UserNotAuthorizedException,
                                                                                InvalidParameterException,
                                                                                PropertyServerException
    {
        final String methodName  = "getActiveServerStatus";
        final String serverNameParameter  = "serverName";
        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{0}/instance/status?delegatingUserId={1}";

        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        OMAGServerStatusResponse restResult = restClient.callOMAGServerStatusGetRESTCall(methodName, urlTemplate, serverName, delegatingUserId);

        return restResult.getServerStatus();
    }


    /**
     * Retrieve a list of the active services on a server
     *
     * @param serverName name of the server
     *
     * @return List of service names
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<String> getActiveServicesForServer(String   serverName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "getActiveServices";
        final String serverNameParameter = "serverName";

        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);

        final String urlTemplate = platformRootURL + retrieveURLTemplatePrefix + "/servers/{0}/services?delegatingUserId={1}";

        ServerServicesListResponse restResult = restClient.callServiceListGetRESTCall(methodName, urlTemplate, serverName, delegatingUserId);

        return restResult.getServerServicesList();
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param serverName server to start
     * @param fileName name of the open metadata archive file.
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void addOpenMetadataArchiveFile(String serverName,
                                           String fileName) throws UserNotAuthorizedException,
                                                                   InvalidParameterException,
                                                                   PropertyServerException
    {
        final String methodName    = "addOpenMetadataArchiveFile";
        final String parameterName = "fileName";
        final String serverNameParameter  = "serverName";
        final String urlTemplate   = platformRootURL + retrieveURLTemplatePrefix + "/servers/{0}/instance/open-metadata-archives/file?delegatingUserId={1}";

        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);
        invalidParameterHandler.validateName(fileName, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, fileName, serverName, delegatingUserId);
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param serverName server to start
     * @param connection connection for the open metadata archive.
     * @throws UserNotAuthorizedException the supplied user is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws PropertyServerException unusual state in the platform.
     */
    public void addOpenMetadataArchive(String     serverName,
                                       Connection connection) throws UserNotAuthorizedException,
                                                                     InvalidParameterException,
                                                                     PropertyServerException
    {
        final String methodName    = "addOpenMetadataArchiveFile";
        final String parameterName = "connection";
        final String serverNameParameter  = "serverName";
        final String urlTemplate   = platformRootURL + retrieveURLTemplatePrefix + "/servers/{0}/instance/open-metadata-archives/connection?delegatingUserId={1}";

        invalidParameterHandler.validateName(serverName, serverNameParameter, methodName);
        invalidParameterHandler.validateConnection(connection, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName, urlTemplate, connection, serverName, delegatingUserId);
    }
}