/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.serverauthor.services;

import org.odpi.openmetadata.adminservices.configuration.properties.CohortTopicStructure;
import org.odpi.openmetadata.adminservices.configuration.properties.EnterpriseAccessConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.rest.EngineServiceRequestBody;
import org.odpi.openmetadata.adminservices.rest.SuccessMessageResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.viewservices.serverauthor.api.ffdc.ServerAuthorExceptionHandler;
import org.odpi.openmetadata.viewservices.serverauthor.api.ffdc.ServerAuthorViewServiceException;
import org.odpi.openmetadata.viewservices.serverauthor.api.properties.ResourceEndpoint;
import org.odpi.openmetadata.viewservices.serverauthor.api.rest.ServerAuthorConfigurationResponse;
import org.odpi.openmetadata.viewservices.serverauthor.api.rest.ServerAuthorPlatformsResponse;
import org.odpi.openmetadata.viewservices.serverauthor.api.rest.ServerAuthorResourceEndpointListResponse;
import org.odpi.openmetadata.viewservices.serverauthor.api.rest.SupportedAuditLogSeveritiesResponse;
import org.odpi.openmetadata.viewservices.serverauthor.handlers.ServerAuthorViewHandler;
import org.odpi.openmetadata.viewservices.serverauthor.initialization.ServerAuthorViewInstanceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


/**
 * The ServerAuthorViewRESTServices provides the org.odpi.openmetadata.viewservices.serverauthor.services implementation of the Server Author Open Metadata
 * View Service (OMVS). This interface provides view interfaces for IT Infrastructure Administrators.
 */

public class ServerAuthorViewRESTServices {

    private static final String className = ServerAuthorViewRESTServices.class.getName();
    private static final Logger log = LoggerFactory.getLogger(ServerAuthorViewRESTServices.class);
    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    /**
     * instance handler
     */
    protected static ServerAuthorViewInstanceHandler instanceHandler = new ServerAuthorViewInstanceHandler();
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ServerAuthorViewRESTServices.class), instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public ServerAuthorViewRESTServices() {

    }

    /**
     * Retrieve the known platforms
     *
     * @param serverName name of the local view server.
     * @param userId     userId under which the request is performed
     * @return response     the list of resource endpoints configured for the view service
     */
    public ServerAuthorResourceEndpointListResponse getResourceEndpointList(String serverName, String userId) {

        final String methodName = "getResourceEndpointList";

        ServerAuthorResourceEndpointListResponse response = new ServerAuthorResourceEndpointListResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler handler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            Map<String, List<ResourceEndpoint>> lists = handler.getResourceEndpoints();
            List<ResourceEndpoint> platformList = null;
            if (lists != null) {
                platformList = lists.get("platformList");
            }
            response.setPlatformList(platformList);

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
    }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;

    }

    /**
     * Set up an in memory local repository.  This repository uses hashmaps to store content.  It is useful
     * for demos, testing and POCs.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public FFDCResponseBase setInMemLocalRepository(String userId, String serverName, String serverToBeConfiguredName) {
        final String methodName = "setInMemLocalRepository";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        FFDCResponseBase response = new ServerAuthorConfigurationResponse();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.setInMemLocalRepository(className, methodName, serverToBeConfiguredName);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Set up a graph store as the local repository.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param storageProperties        properties used to configure the back end storage for the graph
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public FFDCResponseBase setGraphLocalRepository(String userId, String serverName, String serverToBeConfiguredName, Map<String, Object> storageProperties) {
        final String methodName = "setGraphLocalRepository";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        FFDCResponseBase response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.setGraphLocalRepository(className, methodName, serverToBeConfiguredName, storageProperties);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Set up a read only local repository.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public FFDCResponseBase setReadOnlyLocalRepository(String userId, String serverName, String serverToBeConfiguredName) {
        final String methodName = "setGraphLocalRepository";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        FFDCResponseBase response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.setReadOnlyLocalRepository(className, methodName, serverToBeConfiguredName);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
    /**
     * Provide the connection to the local repository - used when the local repository mode is set to plugin repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param connection  connection to the OMRS repository connector.
     * @return FFDCResponseBase VoidResponse or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set
     */
    public FFDCResponseBase setPluginRepositoryConnection(String userId, String serverName, String serverToBeConfiguredName, Connection connection) {
        final String methodName = "setPluginRepositoryConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        FFDCResponseBase response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.setPluginRepositoryConnection(className, methodName, serverToBeConfiguredName, connection);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }




    /**
     * Return the stored configuration document for the server.
     *
     * @param userId                  user that is issuing the request
     * @param serverName              local server name
     * @param serverToBeRetrievedName name of the server to be retrieved for configuration.
     * @return OMAGServerConfig properties or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public ServerAuthorConfigurationResponse getStoredConfiguration(String userId, String serverName, String serverToBeRetrievedName) {
        final String methodName = "getStoredConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            OMAGServerConfig config = serverAuthorViewHandler.getStoredConfiguration(className, methodName, serverToBeRetrievedName);
            response = new ServerAuthorConfigurationResponse();
            response.setOmagServerConfig(config);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Push the configuration for the server to another OMAG Server Platform.
     *
     * @param userId                   user that is issuing the request
     * @param serverName               local server name
     * @param destinationPlatformName  Name of the platform where the config is to be deployed to
     * @param serverToBeConfiguredName name of the server to be configured.
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration or
     * OMAGInvalidParameterException invalid serverName or destinationPlatform parameter.
     */
    public ServerAuthorConfigurationResponse deployOMAGServerConfig(String userId, String serverName, String destinationPlatformName, String serverToBeConfiguredName) {
        final String methodName = "deployOMAGServerConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.deployOMAGServerConfig(className, methodName, destinationPlatformName, serverToBeConfiguredName);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Set up the configuration properties for an OMAG Server in a single command.
     *
     * @param userId                   user that is issuing the request
     * @param serverName               local server name
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param omagServerConfig         configuration for the server
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or OMAGServerConfig parameter.
     */
    public ServerAuthorConfigurationResponse setOMAGServerConfig(String userId, String serverName, String serverToBeConfiguredName, OMAGServerConfig omagServerConfig) {
        final String methodName = "setOMAGServerConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();
        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.setOMAGServerConfig(className, methodName, serverToBeConfiguredName, omagServerConfig);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Enable a single access service.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param accessServiceOptions     property name/value pairs used to configure the access services
     * @param serviceURLMarker         string indicating which access service it is configuring
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public ServerAuthorConfigurationResponse configureAccessService(String userId, String serverName, String serverToBeConfiguredName, String serviceURLMarker, Map<String, Object> accessServiceOptions) {
        final String methodName = "configureAccessService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.configureAccessService(className, methodName, serverToBeConfiguredName, serviceURLMarker, accessServiceOptions);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Disable a single access service.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param serviceURLMarker         string indicating which access service it is configuring
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public ServerAuthorConfigurationResponse disableAccessService(String userId, String serverName, String serverToBeConfiguredName, String serviceURLMarker) {
        final String methodName = "disableAccessService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.disableAccessService(className, methodName, serverToBeConfiguredName, serviceURLMarker);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Enable all access services that are registered with this server platform.
     * The access services are set up to use the default event bus.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param accessServiceOptions     property name/value pairs used to configure the access services
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public ServerAuthorConfigurationResponse configureAllAccessServices(String userId, String serverName, String serverToBeConfiguredName, Map<String, Object> accessServiceOptions) {
        final String methodName = "configureAllAccessServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.configureAllAccessServices(className, methodName, serverToBeConfiguredName, accessServiceOptions);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Enable a single view service.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param viewServiceOptions     property name/value pairs used to configure the view services
     * @param serviceURLMarker         string indicating which view service it is configuring
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public ServerAuthorConfigurationResponse configureViewService(String userId, String serverName, String serverToBeConfiguredName, String serviceURLMarker, Map<String, Object> viewServiceOptions) {
        final String methodName = "configureViewService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.configureViewService(className, methodName, serverToBeConfiguredName, serviceURLMarker, viewServiceOptions);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Disable a single view service.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param serviceURLMarker         string indicating which view service it is configuring
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public ServerAuthorConfigurationResponse disableViewService(String userId, String serverName, String serverToBeConfiguredName, String serviceURLMarker) {
        final String methodName = "disableViewService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.disableViewService(className, methodName, serverToBeConfiguredName, serviceURLMarker);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Enable a single engine service.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param serviceURLMarker         string indicating which engine service it is configuring
     * @param requestBody  request body
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public ServerAuthorConfigurationResponse configureEngineService(String userId, String serverName, String serverToBeConfiguredName, String serviceURLMarker, EngineServiceRequestBody requestBody) {
        final String methodName = "configureEngineService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.configureEngineService(className, methodName, serverToBeConfiguredName, serviceURLMarker, requestBody.getEngineServiceOptions(), requestBody.getEngines());
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Disable a single engine service.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param serviceURLMarker         string indicating which engine service it is configuring
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public ServerAuthorConfigurationResponse disableEngineService(String userId, String serverName, String serverToBeConfiguredName, String serviceURLMarker) {
        final String methodName = "disableEngineService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.disableEngineService(className, methodName, serverToBeConfiguredName, serviceURLMarker);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Set up the configuration that controls the enterprise repository services.  These services are part
     * of the Open Metadata Repository Services (OMRS).  They provide federated queries and federated event
     * notifications that cover metadata from the local repository plus any repositories connected via
     * open metadata repository cohorts.
     *
     * @param userId                 user that is issuing the request
     * @param serverName             local server name
     * @param serverToBeConfiguredName server to be configured name
     * @param enterpriseAccessConfig enterprise repository services configuration properties.
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or enterpriseAccessConfig parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    public ServerAuthorConfigurationResponse setEnterpriseAccessConfig(String userId, String serverName, String serverToBeConfiguredName, EnterpriseAccessConfig enterpriseAccessConfig) {
        final String methodName = "setEnterpriseAccessConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.setEnterpriseAccessConfig(className, methodName, serverToBeConfiguredName, enterpriseAccessConfig);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Set up the default event bus for embedding in event-driven connector.   The resulting connector will
     * be used for example, in the OMRS Topic Connector for each cohort, the in and out topics for each Access Service and
     * possibly the local repository's event mapper.
     * <p>
     * When the event bus is configured, it is used only on future configuration.  It does not effect
     * existing configuration.
     *
     * @param userId                  user that is issuing the request.
     * @param serverName              server name to be configured.
     * @param serverToBeConfiguredName server to be configured name
     * @param connectorProvider       connector provider for the event bus (if it is null then Kafka is assumed).
     * @param topicURLRoot            the common root of the topics used by the open metadata server.
     * @param configurationProperties property name/value pairs used to configure the connection to the event bus connector
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists or
     * OMAGInvalidParameterException invalid serverName or serviceMode parameter.
     */
    public ServerAuthorConfigurationResponse setEventBus(String userId, String serverName, String serverToBeConfiguredName, String connectorProvider, String topicURLRoot, Map<String, Object> configurationProperties) {
        final String methodName = "setEventBus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.setEventBus(className, methodName, serverToBeConfiguredName, connectorProvider, topicURLRoot, configurationProperties);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Set up the default audit log for the server.  This adds the console audit log destination.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public ServerAuthorConfigurationResponse setDefaultAuditLog(String userId, String serverName, String serverToBeConfiguredName) {
        final String methodName = "setDefaultAuditLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.setDefaultAuditLog(className, methodName, serverToBeConfiguredName);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Set up the console audit log for the server.  This writes selected parts of the audit log record to stdout.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param supportedSeverities      list of severities that should be logged to this destination (empty list means all)
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public ServerAuthorConfigurationResponse addConsoleAuditLogDestination(String userId, String serverName, String serverToBeConfiguredName, List<String> supportedSeverities) {
        final String methodName = "addConsoleAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.addConsoleAuditLogDestination(className, methodName, serverToBeConfiguredName, supportedSeverities);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Add an audit log destination that creates slf4j records.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param supportedSeverities      list of severities that should be logged to this destination (empty list means all)
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public ServerAuthorConfigurationResponse addSLF4JAuditLogDestination(String userId, String serverName, String serverToBeConfiguredName, List<String> supportedSeverities) {
        final String methodName = "addSLF4JAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.addSLF4JAuditLogDestination(className, methodName, serverToBeConfiguredName, supportedSeverities);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Add an audit log destination that creates log records as JSON files in a shared directory.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param supportedSeverities      list of severities that should be logged to this destination (empty list means all)
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public ServerAuthorConfigurationResponse addFileAuditLogDestination(String userId, String serverName, String serverToBeConfiguredName, List<String> supportedSeverities) {
        final String methodName = "addFileAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.addFileAuditLogDestination(className, methodName, serverToBeConfiguredName, supportedSeverities);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Add an audit log destination that sends each log record as an event on the supplied event topic.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param supportedSeverities      list of severities that should be logged to this destination (empty list means all)
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public ServerAuthorConfigurationResponse addEventTopicAuditLogDestination(String userId, String serverName, String serverToBeConfiguredName, List<String> supportedSeverities) {
        final String methodName = "addEventTopicAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.addEventTopicAuditLogDestination(className, methodName, serverToBeConfiguredName, supportedSeverities);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Add an audit log destination that is defined by the supplied connection object.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param connection               connection object that defines the audit log destination
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public ServerAuthorConfigurationResponse addAuditLogDestination(String userId, String serverName, String serverToBeConfiguredName, Connection connection) {
        final String methodName = "addAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.addAuditLogDestination(className, methodName, serverToBeConfiguredName, connection);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Update an audit log destination that is identified with the supplied destination name with
     * the supplied connection object.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param auditLogDestinationName name of the audit log destination to be updated
     * @param auditLogDestination connection object that defines the audit log destination
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public ServerAuthorConfigurationResponse updateAuditLogDestination(String userId, String serverName, String serverToBeConfiguredName, String auditLogDestinationName, Connection auditLogDestination) {
        final String methodName = "updateAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.updateAuditLogDestination(className, methodName, serverToBeConfiguredName, auditLogDestinationName, auditLogDestination);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
    /**
     * Delete an audit log destination that is identified with the supplied destination name
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param auditLogDestinationName name of the audit log destination to be deleted
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    public ServerAuthorConfigurationResponse deleteAuditLogDestination(String userId, String serverName, String serverToBeConfiguredName, String auditLogDestinationName) {
        final String methodName = "clearAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.deleteAuditLogDestination(className, methodName, serverToBeConfiguredName, auditLogDestinationName);
            response = getStoredConfiguration(userId, serverName, serverToBeConfiguredName);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;

    }

    /**
     * Activate the Open Metadata and Governance (OMAG) server using the configuration document stored for this server.
     *
     * @param userId                  user that is issuing the request
     * @param serverName              local server name
     * @param destinationPlatformName Name of the platform where the server lives
     * @param serverToBeActivatedName name of the server to be activated
     * @return the current active configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    public ServerAuthorConfigurationResponse activateWithStoredConfig(String userId, String serverName, String destinationPlatformName, String serverToBeActivatedName) {
        final String methodName = "activateWithStoredConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.activateWithStoredConfig(className, methodName, destinationPlatformName, serverToBeActivatedName);
            OMAGServerConfig config = serverAuthorViewHandler.getActiveConfiguration(className, methodName, serverToBeActivatedName);
            response.setOmagServerConfig(config);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Temporarily deactivate open metadata and governance server.
     *
     * @param userId                    user that is issuing the request
     * @param serverName                local server name
     * @param destinationPlatformName   Name of the platform where the server lives
     * @param serverToBeDeactivatedName name of the server to be deactivated
     * @return the current stored configuration that has been deactivated or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    public ServerAuthorConfigurationResponse deactivateTemporarily(String userId, String serverName, String destinationPlatformName, String serverToBeDeactivatedName) {
        final String methodName = "deactivateTemporarily";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.deactivateServerTemporarily(className, methodName, destinationPlatformName, serverToBeDeactivatedName);
            OMAGServerConfig config = serverAuthorViewHandler.getStoredConfiguration(className, methodName, serverToBeDeactivatedName);
            response.setOmagServerConfig(config);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Permanently deactivate any open metadata and governance services and unregister from
     * any cohorts.
     *
     * @param userId                    user that is issuing the request
     * @param serverName                local server name
     * @param destinationPlatformName   Name of the platform where the server lives
     * @param serverToBeDeactivatedName name of the server to be deactivated permanently
     * @return Success Message response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    public SuccessMessageResponse deactivatePermanently(String userId, String serverName, String destinationPlatformName, String serverToBeDeactivatedName) {
        final String methodName = "activateWithStoredConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        SuccessMessageResponse response = new SuccessMessageResponse();
        response.setSuccessMessage("Activate the server");

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            serverAuthorViewHandler.deactivateServerPermanently(className, methodName, destinationPlatformName, serverToBeDeactivatedName);
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;

    }

    /**
     * Return the configuration used for the current active instance of the server.  Null is returned if
     * the server instance is not running.
     *
     * @param userId                  user that is issuing the request
     * @param serverName              local server name
     * @param destinationPlatformName Name of the platform where the server lives
     * @param serverToBeConfiguredName server to be configured name
     * @return configuration properties used to initialize the server or null if not running or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    public ServerAuthorConfigurationResponse getActiveConfiguration(String userId, String serverName, String destinationPlatformName, String serverToBeConfiguredName) {
        final String methodName = "getStoredConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        ServerAuthorConfigurationResponse response = new ServerAuthorConfigurationResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ServerAuthorViewHandler serverAuthorViewHandler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            OMAGServerConfig config = serverAuthorViewHandler.getActiveConfiguration(className, methodName, serverToBeConfiguredName);
            response.setOmagServerConfig(config);
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        }
        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    /**
     * Get the server configurations associated with the platforms that this view service knows about.
     * @param userId                  user that is issuing the request
     * @param serverName              local server name
     * @return the known platforms, which if active will include he servers that are configured or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * ServerAuthorViewServiceException The Server Author has detected an error.
     */
    public ServerAuthorPlatformsResponse getKnownPlatforms(String userId, String serverName) {
        String methodName = "getKnownPlatforms";
        if (log.isDebugEnabled()) {
            log.debug("Entering method: " + methodName + " with serverName " + serverName);
        }
        ServerAuthorPlatformsResponse response = new ServerAuthorPlatformsResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            // get the defined platforms from the config
            ServerAuthorViewHandler handler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            response.setPlatforms(handler.getKnownPlatforms(userId, methodName, auditLog));
        } catch (ServerAuthorViewServiceException error) {
            ServerAuthorExceptionHandler.captureCheckedException(response, error, className);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }
        if (log.isDebugEnabled()) {
            log.debug("Returning from method: " + methodName + " with response: " + response.toString());
        }
        return response;
    }
    /**
     * Get the audit log supported severities for the server being configured
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @return a list of supported audit log severities
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public SupportedAuditLogSeveritiesResponse getAuditLogDestinationSupportedSeverities(String userId, String serverName, String serverToBeConfiguredName) {
        String methodName = " getAuditLogDestinationSupportedSeverities";
        if (log.isDebugEnabled()) {
            log.debug("Entering method: " + methodName + " with serverName " + serverName);
        }
        SupportedAuditLogSeveritiesResponse response = new SupportedAuditLogSeveritiesResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            // get the defined platforms from the config
            ServerAuthorViewHandler handler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            response.setSeverities(handler.getSupportedAuditLogSeverities());
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }
        if (log.isDebugEnabled()) {
            log.debug("Returning from method: " + methodName + " with response: " + response.toString());
        }
        return response;

    }
    /**
     * Clear the audit log destinations associated with the server being configured
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @return a list of supported audit log severities
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse clearAuditLogDestinations(String userId, String serverName, String serverToBeConfiguredName) {
        String methodName = "clearAuditLogDestinations";
        if (log.isDebugEnabled()) {
            log.debug("Entering method: " + methodName + " with serverName " + serverName);
        }
        VoidResponse response = new VoidResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            // get the defined platforms from the config
            ServerAuthorViewHandler handler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
           handler.clearAuditLogDestinations(serverToBeConfiguredName);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }
        if (log.isDebugEnabled()) {
            log.debug("Returning from method: " + methodName + " with response: " + response.toString());
        }
        return response;
    }
    /**
     * Enable registration of server to an open metadata repository cohort using the default topic structure (DEDICATED_TOPICS).
     *
     * A cohort is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to changes in their supported metadata types and instances.
     * They are also able to query each other's metadata directly through REST calls.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverToBeConfiguredName server to be configured
     * @param cohortName  name of the cohort.
     * @return VoidResponse or
     * ServerAuthorException if the supplied userId is not authorized to issue this command or
     * invalid serverName, cohortName or serviceMode parameter or
     * or the cohort registration failed
     */
    public VoidResponse addCohortRegistration(String userId, String serverName, String serverToBeConfiguredName, String cohortName) {
        String methodName = "addCohortRegistration";
        if (log.isDebugEnabled()) {
            log.debug("Entering method: " + methodName + " with serverName " + serverName + " server To Be Configured Name " + serverToBeConfiguredName);
        }
        VoidResponse response = new VoidResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            // register new cohort
            ServerAuthorViewHandler handler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            handler.addCohortRegistration(serverToBeConfiguredName, cohortName);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }
        if (log.isDebugEnabled()) {
            log.debug("Returning from method: " + methodName + " with response: " + response.toString());
        }
        return response;
    }
    /**
     * Unregister this server from an open metadata repository cohort.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverToBeConfiguredName name of server to pass the request to
     * @param cohortName  name of the cohort.
     * @return VoidResponse or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName, cohortName or serviceMode parameter.
     */
    public VoidResponse removeCohortRegistration(String userId, String serverName, String serverToBeConfiguredName, String cohortName) {
        String methodName = "removeCohortRegistration";
        if (log.isDebugEnabled()) {
            log.debug("Entering method: " + methodName + " with serverName " + serverName + " server To Be Configured Name " + serverToBeConfiguredName);
        }
        VoidResponse response = new VoidResponse();

        AuditLog auditLog = null;
        try {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            // unregister cohort
            ServerAuthorViewHandler handler = instanceHandler.getServerAuthorViewHandler(userId, serverName, methodName);
            handler.removeCohortRegistration(serverToBeConfiguredName, cohortName);
        } catch (Exception exception) {
            restExceptionHandler.captureExceptions(response, exception, methodName, auditLog);
        }
        if (log.isDebugEnabled()) {
            log.debug("Returning from method: " + methodName + " with response: " + response.toString());
        }
        return response;
    }

}
