/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.classifier.ServerTypeClassifier;
import org.odpi.openmetadata.adminservices.client.ConfigurationManagementClient;
import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.properties.DedicatedTopicList;
import org.odpi.openmetadata.adminservices.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataPlatformSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OMAGServerAdminServices provides part of the server-side implementation of the administrative interface for
 * an Open Metadata and Governance (OMAG) Server.  In particular, this class supports the configuration
 * of the server name, server type and organization name.  It also supports the setting up of the
 * Open Metadata Repository Services' local repository and cohort.
 */
public class OMAGServerAdminServices
{
    private final static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminServices.class),
                                                                            CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceName());


    private final OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();
    private final OMAGServerErrorHandler errorHandler = new OMAGServerErrorHandler();
    private final OMAGServerExceptionHandler exceptionHandler = new OMAGServerExceptionHandler();


    /**
     * Return the derived server type that is created from the classification of the server configuration.
     *
     * @param userId     user that is issuing the request.
     * @param serverName local server name.
     * @return server type classification response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    public ServerTypeClassificationResponse getServerTypeClassification(String userId,
                                                                        String serverName)
    {
        final String methodName = "getServerTypeClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ServerTypeClassificationResponse response = new ServerTypeClassificationResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            ServerTypeClassifier classifier = new ServerTypeClassifier(serverName, configStore.getServerConfig(userId, serverName, methodName));
            ServerTypeClassificationSummary summary = new ServerTypeClassificationSummary(classifier.getServerType());

            response.setServerTypeClassification(summary);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /*
     * =============================================================
     * Configure server - basic options using defaults
     */


    /**
     * Set up the descriptive type of the server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is "Open Metadata and Governance Server".
     *
     * @param userId     user that is issuing the request.
     * @param serverName local server name.
     * @param typeName   short description for the type of server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    public VoidResponse setServerType(String userId,
                                      String serverName,
                                      String typeName)
    {
        final String methodName = "setServerType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if ((typeName != null) && (typeName.length() == 0))
            {
                typeName = null;
            }

            if (typeName == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for local server type name.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for local server type name to " + typeName + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setLocalServerType(typeName);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the name of the organization that is running this server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is null.
     *
     * @param userId     user that is issuing the request.
     * @param serverName local server name.
     * @param name       String name of the organization.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or organizationName parameter.
     */
    public VoidResponse setOrganizationName(String userId,
                                            String serverName,
                                            String name)
    {
        final String methodName = "setOrganizationName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if (name != null && (name.length() == 0))
            {
                name = null;
            }

            if (name == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for local server's owning organization's name.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for local server's owning organization's name to " + name + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setOrganizationName(name);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the description of this server. The default value is null.
     *
     * @param userId      user that is issuing the request.
     * @param serverName  local server description.
     * @param description String description of the server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or organizationName parameter.
     */
    public VoidResponse setServerDescription(String userId,
                                             String serverName,
                                             String description)
    {

        final String methodName = "setServerDescription";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if (description != null && (description.length() == 0))
            {
                description = null;
            }

            if (description == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for local server's description.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for local server's description to " + description + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setLocalServerDescription(description);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the user id to use when there is no external user driving the work (for example when processing events
     * from another server).
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverUserId  String user is for the server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    public VoidResponse setServerUserId(String userId,
                                        String serverName,
                                        String serverUserId)
    {
        final String methodName = "setServerUserId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateServerName(serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<String>  configAuditTrail          = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if ((serverUserId != null) && (serverUserId.length() == 0))
            {
                serverUserId = null;
            }

            if (serverUserId == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for local server's userId.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for local server's userId to " + serverUserId + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setLocalServerUserId(serverUserId);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the user id to use when there is no external user driving the work (for example when processing events
     * from another server).
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverPassword  String password for the server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    public VoidResponse setServerPassword(String userId,
                                          String serverName,
                                          String serverPassword)
    {
        final String methodName = "setServerPassword";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateServerName(serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<String>  configAuditTrail          = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if ((serverPassword != null) && (serverPassword.length() == 0))
            {
                serverPassword = null;
            }

            if (serverPassword == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for local server's password.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for local server's password to " + serverPassword + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setLocalServerPassword(serverPassword);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set an upper limit in the page size that can be requested on a REST call to the server.  The default
     * value is 1000.
     *
     * @param userId - user that is issuing the request.
     * @param serverName - local server name.
     * @param maxPageSize - max number of elements that can be returned on a request.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or maxPageSize parameter.
     */
    public VoidResponse setMaxPageSize(String  userId,
                                       String  serverName,
                                       int     maxPageSize)
    {
        final String methodName = "setMaxPageSize";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            if (maxPageSize >= 0)
            {
                OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

                List<String>  configAuditTrail          = serverConfig.getAuditTrail();

                if (configAuditTrail == null)
                {
                    configAuditTrail = new ArrayList<>();
                }

                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for maximum page size to " + maxPageSize + ".");

                serverConfig.setAuditTrail(configAuditTrail);
                serverConfig.setMaxPageSize(maxPageSize);

                configStore.saveServerConfig(serverName, methodName, serverConfig);
            }
            else
            {
                throw new OMAGInvalidParameterException(OMAGAdminErrorCode.BAD_MAX_PAGE_SIZE.getMessageDefinition(serverName, Integer.toString(maxPageSize)),
                                                        this.getClass().getName(),
                                                        methodName);
            }
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the default event bus for embedding in event-driven connector.   The resulting connector will
     * be used in the OMRS Topic Connector for each cohort, the in and out topics for each Access Service and
     * the local repositories event mapper.
     *
     * When the event bus is configured, it is used only on future configuration.  It does not effect
     * existing configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName local server name.
     * @param connectorProvider  connector provider for the event bus (if it is null then Kafka is assumed).
     * @param topicURLRoot the common root of the topics used by the open metadata server.
     * @param configurationProperties  property name/value pairs used to configure the connection to the event bus connector
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse setEventBus(String              userId,
                                    String              serverName,
                                    String              connectorProvider,
                                    String              topicURLRoot,
                                    Map<String, Object> configurationProperties)
    {
        final String methodName = "setEventBus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);


            /*
             * Retrieve the existing configuration.
             */
            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            EventBusConfig   eventBusConfig = new EventBusConfig();

            if ((connectorProvider != null) && (connectorProvider.length() == 0))
            {
                eventBusConfig.setConnectorProvider(null);
            }
            else
            {
                eventBusConfig.setConnectorProvider(connectorProvider);
            }

            if ((topicURLRoot != null) && (topicURLRoot.length() == 0))
            {
                eventBusConfig.setTopicURLRoot(null);
            }
            else
            {
                eventBusConfig.setTopicURLRoot(topicURLRoot);
            }

            if ((configurationProperties == null) || (configurationProperties.isEmpty()))
            {
                eventBusConfig.setConfigurationProperties(configurationProperties);
            }
            else
            {
                eventBusConfig.setConfigurationProperties(configurationProperties);
            }

            List<String>  configAuditTrail          = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for default event bus.");

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setEventBusConfig(eventBusConfig);

            /*
             * Save the config away
             */
            configStore.saveServerConfig(serverName, methodName, serverConfig);

        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the current configuration for the event bus.
     *
     * @param userId  user that is issuing the request.
     * @param serverName local server name.
     * @return event bus config response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public EventBusConfigResponse getEventBus(String userId, String serverName)
    {
        final String methodName = "getEventBus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EventBusConfigResponse response = new EventBusConfigResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            /*
             * Retrieve the existing configuration.
             */
            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            if (configStore != null)
            {
                response.setConfig(serverConfig.getEventBusConfig());
            }
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Delete the current configuration for the event bus.
     *
     * @param userId  user that is issuing the request.
     * @param serverName local server name.
     * @return void  or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse deleteEventBus(String userId, String serverName)
    {
        final String methodName = "deleteEventBus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            /*
             * Retrieve the existing configuration.
             */
            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            if (configStore != null)
            {
                List<String>  configAuditTrail          = serverConfig.getAuditTrail();

                if (configAuditTrail == null)
                {
                    configAuditTrail = new ArrayList<>();
                }

                configAuditTrail.add(new Date().toString() + " " + userId + " deleted configuration for default event bus.");

                serverConfig.setAuditTrail(configAuditTrail);
                serverConfig.setEventBusConfig(null);

                /*
                 * Save the config away
                 */
                configStore.saveServerConfig(serverName, methodName, serverConfig);
            }
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the root URL for this server that is used to construct full URL paths to calls for
     * this server's REST interfaces that is used by other members of the cohorts that this server
     * connects to.
     *
     * The default value is "localhost:9443".
     *
     * ServerURLRoot is used during the configuration of the local repository.  If called
     * after the local repository is configured, it has no effect.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param url  String url.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    @Deprecated
    public VoidResponse setServerURLRoot(String userId,
                                         String serverName,
                                         String url)
    {
        final String methodName = "setServerURLRoot";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateServerName(serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<String>  configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if ((url != null) && (url.length() == 0))
            {
                url = null;
            }

            if (url == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for local server's URL root.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for local server's URL root to " + url + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setLocalServerURL(url);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the root URL for this server that is used to construct full URL paths to calls for
     * this server's REST interfaces that is used by other members of the cohorts that this server
     * connects to.
     *
     * The default value is "localhost:9443".
     *
     * ServerURLRoot is used during the configuration of the local repository.  If called
     * after the local repository is configured, it has no effect.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param requestBody  String url.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    public VoidResponse setServerRootURL(String         userId,
                                         String         serverName,
                                         URLRequestBody requestBody)
    {
        final String methodName = "setServerURLRoot";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateServerName(serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<String>  configAuditTrail          = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            String urlRoot = null;

            if (requestBody != null)
            {
                urlRoot = requestBody.getUrlRoot();
            }

            if (urlRoot == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for local server's URL root.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for local server's URL root to " + urlRoot + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setLocalServerURL(urlRoot);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the default audit log for the server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param requestBody null request body
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or null userId parameter.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse setDefaultAuditLog(String          userId,
                                           String          serverName,
                                           NullRequestBody requestBody)
    {
        final String methodName = "setDefaultAuditLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig     = configStore.getServerConfig(userId, serverName, methodName);
            List<String>     configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date().toString() + " " + userId + " set up default audit log destinations.");


            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            if (repositoryServicesConfig == null)
            {
                OMRSConfigurationFactory omrsConfigurationFactory = new OMRSConfigurationFactory();

                repositoryServicesConfig = omrsConfigurationFactory.getDefaultRepositoryServicesConfig();
            }
            else
            {
                ConnectorConfigurationFactory configurationFactory = new ConnectorConfigurationFactory();
                Connection                    defaultAuditLogDestination = configurationFactory.getDefaultAuditLogConnection();

                List<Connection> auditLogDestinations = new ArrayList<>();

                auditLogDestinations.add(defaultAuditLogDestination);
                repositoryServicesConfig.setAuditLogConnections(auditLogDestinations);
            }

            serverConfig.setAuditTrail(configAuditTrail);

            /*
             * Save the open metadata repository services config in the server's config
             */
            serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);
            configStore.saveServerConfig(serverName, methodName, serverConfig);

        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the console (stdout) audit log destination for the server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or null userId parameter.
     */
    public VoidResponse addConsoleAuditLogDestination(String       userId,
                                                      String       serverName,
                                                      List<String> supportedSeverities)
    {
        final String methodName = "addConsoleAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            ConnectorConfigurationFactory configurationFactory = new ConnectorConfigurationFactory();

            String qualifier;

            if ((supportedSeverities == null) || (supportedSeverities.isEmpty()))
            {
                qualifier = "- no output";
            }
            else
            {
                qualifier = "- " + supportedSeverities.toString();
            }

            this.addAuditLogDestination(userId, serverName, configurationFactory.getConsoleAuditLogConnection(qualifier, supportedSeverities));
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the SLF4J audit log destination for the server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or null userId parameter.
     */
    public VoidResponse addSLF4JAuditLogDestination(String       userId,
                                                    String       serverName,
                                                    List<String> supportedSeverities)
    {
        final String methodName = "addSLF4JAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            ConnectorConfigurationFactory configurationFactory = new ConnectorConfigurationFactory();

            String qualifier;

            if ((supportedSeverities == null) || (supportedSeverities.isEmpty()))
            {
                qualifier = "- no output";
            }
            else
            {
                qualifier = "- " + supportedSeverities.toString();
            }

            this.addAuditLogDestination(userId, serverName, configurationFactory.getSLF4JAuditLogConnection(qualifier, supportedSeverities));
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the File based audit log destination for the server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or null userId parameter.
     */
    public VoidResponse addFileAuditLogDestination(String       userId,
                                                   String       serverName,
                                                   List<String> supportedSeverities)
    {
        final String methodName = "addFileAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            ConnectorConfigurationFactory configurationFactory = new ConnectorConfigurationFactory();

            this.addAuditLogDestination(userId, serverName, configurationFactory.getFileBasedAuditLogConnection(serverName, supportedSeverities));
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the File based audit log destination for the server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or null userId parameter.
     */
    public VoidResponse addEventTopicAuditLogDestination(String       userId,
                                                         String       serverName,
                                                         List<String> supportedSeverities)
    {
        final String methodName = "addEventTopicAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            EventBusConfig   eventBusConfig  = errorHandler.validateEventBusIsSet(serverName, serverConfig, methodName);

            ConnectorConfigurationFactory configurationFactory = new ConnectorConfigurationFactory();

            this.addAuditLogDestination(userId, serverName, configurationFactory.getEventTopicAuditLogConnection(serverName,
                                                                                                                 supportedSeverities,
                                                                                                                 eventBusConfig.getConnectorProvider(),
                                                                                                                 eventBusConfig.getTopicURLRoot(),
                                                                                                                 serverConfig.getLocalServerId(),
                                                                                                                 eventBusConfig.getConfigurationProperties()));
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Add a new open metadata archive to load at startup.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param fileName name of the open metadata archive file.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or fileName parameter.
     */
    public VoidResponse addStartUpOpenMetadataArchiveFile(String userId,
                                                          String serverName,
                                                          String fileName)
    {
        final String methodName = "addStartUpOpenMetadataArchiveFile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateFileName(fileName, serverName, methodName);

            ConnectorConfigurationFactory configurationFactory   = new ConnectorConfigurationFactory();
            Connection                    newOpenMetadataArchive = configurationFactory.getOpenMetadataArchiveFileConnection(fileName);

            List<Connection>              openMetadataArchiveConnections = null;

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            RepositoryServicesConfig  repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            if (repositoryServicesConfig != null)
            {
                openMetadataArchiveConnections = repositoryServicesConfig.getOpenMetadataArchiveConnections();
            }

            if (openMetadataArchiveConnections == null)
            {
                openMetadataArchiveConnections = new ArrayList<>();
            }

            openMetadataArchiveConnections.add(newOpenMetadataArchive);

            this.setOpenMetadataArchives(userId, serverName, openMetadataArchiveConnections);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up an in memory local repository.  This repository uses hashmaps to store content.  It is useful
     * for demos, testing and POCs.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param requestBody null request body
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse setInMemLocalRepository(String          userId,
                                                String          serverName,
                                                NullRequestBody requestBody)
    {
        final String methodName = "setInMemLocalRepository";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            OMRSConfigurationFactory configurationFactory     = new OMRSConfigurationFactory();


            this.setLocalRepositoryConfig(userId,
                                          serverName,
                                          configurationFactory.getInMemoryLocalRepositoryConfig(serverConfig.getLocalServerName(),
                                                                                                serverConfig.getLocalServerURL()));
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up a graph store as the local repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param storageProperties  properties used to configure Egeria Graph DB
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public VoidResponse setGraphLocalRepository(String              userId,
                                                String              serverName,
                                                Map<String, Object> storageProperties)
    {
        final String methodName = "setGraphLocalRepository";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            OMRSConfigurationFactory configurationFactory     = new OMRSConfigurationFactory();

            this.setLocalRepositoryConfig(userId,
                                          serverName,
                                          configurationFactory.getLocalGraphLocalRepositoryConfig(serverConfig.getLocalServerName(),
                                                                                                  serverConfig.getLocalServerURL(),
                                                                                                  storageProperties));
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up a read only store as the local repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public VoidResponse setReadOnlyLocalRepository(String              userId,
                                                   String              serverName)
    {
        final String methodName = "setReadOnlyLocalRepository";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            OMRSConfigurationFactory configurationFactory     = new OMRSConfigurationFactory();


            this.setLocalRepositoryConfig(userId,
                                          serverName,
                                          configurationFactory.getReadOnlyLocalRepositoryConfig(serverConfig.getLocalServerName(),
                                                                                                serverConfig.getLocalServerURL()));
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove all configuration for a local repository.  This is the default setting.  This call
     * can be used to remove subsequent local repository configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public VoidResponse setNoRepositoryMode(String userId,
                                            String serverName)
    {
        return clearLocalRepositoryConfig(userId, serverName);
    }


    /**
     * Provide the connection to the local repository - used when the local repository mode is set to plugin repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param connection  connection to the OMRS repository connector.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     */
    public VoidResponse setPluginRepositoryConnection(String     userId,
                                                      String     serverName,
                                                      Connection connection)
    {
        final String methodName = "setPluginRepositoryConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateServerConnection(connection, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            OMRSConfigurationFactory configurationFactory     = new OMRSConfigurationFactory();
            LocalRepositoryConfig localRepositoryConfig
                    = configurationFactory.getPluginRepositoryLocalRepositoryConfig(serverConfig.getLocalServerName(),
                                                                                    serverConfig.getLocalServerURL());

            /*
             * Set up the repository connection in the local repository config and clear any event mapper
             */
            localRepositoryConfig.setLocalRepositoryLocalConnection(connection);
            localRepositoryConfig.setEventMapperConnection(null);

            this.setLocalRepositoryConfig(userId, serverName, localRepositoryConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Provide the connection to the local repository.
     * This is used when the local repository mode is set to plugin repository.
     *
     * @param userId                    user that is issuing the request.
     * @param serverName                local server name.
     * @param connectorProvider         connector provider class name to the OMRS repository connector.
     * @param additionalProperties      additional parameters to pass to the repository connector
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set.
     */
    public VoidResponse setPluginRepositoryConnection(String               userId,
                                                      String               serverName,
                                                      String               connectorProvider,
                                                      Map<String, Object>  additionalProperties)
    {
        final String methodName  = "setPluginRepositoryConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            this.setPluginRepositoryConnection(userId,
                                               serverName,
                                               connectorConfigurationFactory.getRepositoryConnection(connectorProvider,
                                                                                                     serverConfig.getLocalServerURL(),
                                                                                                     additionalProperties));
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Provide the connection to the local repository - used when the local repository mode is set to repository proxy.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param connection  connection to the OMRS repository connector.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     */
    public VoidResponse setRepositoryProxyConnection(String     userId,
                                                     String     serverName,
                                                     Connection connection)
    {
        final String methodName = "setRepositoryProxyConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateServerConnection(connection, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            OMRSConfigurationFactory configurationFactory     = new OMRSConfigurationFactory();
            LocalRepositoryConfig localRepositoryConfig
                    = configurationFactory.getRepositoryProxyLocalRepositoryConfig(serverConfig.getLocalServerName(),
                                                                                   serverConfig.getLocalServerURL());

            /*
             * Set up the repository proxy connection in the local repository config
             */
            localRepositoryConfig.setLocalRepositoryLocalConnection(connection);

            this.setLocalRepositoryConfig(userId, serverName, localRepositoryConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Provide the connection to the local repository.
     * This is used when the local repository mode is set to repository proxy.
     *
     * @param userId                    user that is issuing the request.
     * @param serverName                local server name.
     * @param connectorProvider         connector provider class name to the OMRS repository connector.
     * @param additionalProperties      additional parameters to pass to the repository connector
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set.
     */
    public VoidResponse setRepositoryProxyConnection(String               userId,
                                                     String               serverName,
                                                     String               connectorProvider,
                                                     Map<String, Object>  additionalProperties)
    {
        final String methodName  = "setRepositoryProxyConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            this.setRepositoryProxyConnection(userId,
                                              serverName,
                                              connectorConfigurationFactory.getRepositoryConnection(connectorProvider,
                                                                                                    serverConfig.getLocalServerURL(),
                                                                                                    additionalProperties));
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Provide the connection to the local repository's event mapper if needed.  The default value is null which
     * means no event mapper.  An event mapper is needed if the local repository has additional APIs that can change
     * the metadata in the repository without going through the open metadata and governance services.
     *
     * @param userId user that is issuing the request.
     * @param serverName local server name.
     * @param connection connection to the OMRS repository event mapper.
     * @return void response
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryEventMapper parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set
     */
    public VoidResponse setRepositoryProxyEventMapper(String     userId,
                                                      String     serverName,
                                                      Connection connection)
    {
        final String methodName = "setRepositoryProxyEventMapper";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateServerConnection(connection, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();
            LocalRepositoryConfig    localRepositoryConfig    = null;

            /*
             * Extract any existing local repository configuration
             */
            if (repositoryServicesConfig != null)
            {
                localRepositoryConfig = repositoryServicesConfig.getLocalRepositoryConfig();
            }

            /*
             * The local repository should be partially configured already by setLocalRepositoryMode()
             */
            if (localRepositoryConfig == null)
            {
                throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.LOCAL_REPOSITORY_MODE_NOT_SET.getMessageDefinition(serverName),
                                                          this.getClass().getName(),
                                                          methodName);
            }
            else if (localRepositoryConfig.getLocalRepositoryMode() != LocalRepositoryMode.REPOSITORY_PROXY)
            {
                throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.LOCAL_REPOSITORY_MODE_NOT_PROXY.getMessageDefinition(serverName,
                                                                                                                                  localRepositoryConfig.getLocalRepositoryMode().getName()),
                                                          this.getClass().getName(),
                                                          methodName);
            }

            /*
             * Set up the event mapper connection in the local repository config
             */
            localRepositoryConfig.setEventMapperConnection(connection);

            this.setLocalRepositoryConfig(userId, serverName, localRepositoryConfig);
        }
        catch (OMAGConfigurationErrorException  error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Provide the connection to the local repository's event mapper if needed.  The default value is null which
     * means no event mapper.  An event mapper is needed if the local repository has additional APIs that can change
     * the metadata in the repository without going through the open metadata and governance services.
     *
     * @param userId                      user that is issuing the request.
     * @param serverName                  local server name.
     * @param connectorProvider           Java class name of the connector provider for the OMRS repository event mapper.
     * @param eventSource                 topic name or URL to the native event source.
     * @param additionalProperties        additional properties for the event mapper connection
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryEventMapper parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    public VoidResponse setRepositoryProxyEventMapper(String              userId,
                                                      String              serverName,
                                                      String              connectorProvider,
                                                      String              eventSource,
                                                      Map<String, Object> additionalProperties)
    {
        final String methodName = "setRepositoryProxyEventMapper";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            this.setRepositoryProxyEventMapper(userId,
                                               serverName,
                                               connectorConfigurationFactory.getRepositoryEventMapperConnection(connectorProvider,
                                                                                                                additionalProperties,
                                                                                                                eventSource));
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the local metadata collection id.  If the local repository is not configured
     * then the invalid parameter exception is returned.
     *
     * @param userId                      user that is issuing the request.
     * @param serverName                  local server name.
     * @return guid response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    public GUIDResponse getLocalMetadataCollectionId(String               userId,
                                                     String               serverName)
    {
        final String methodName = "getLocalMetadataCollectionId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();
            LocalRepositoryConfig    localRepositoryConfig    = null;

            /*
             * Extract any existing local repository configuration
             */
            if (repositoryServicesConfig != null)
            {
                localRepositoryConfig = repositoryServicesConfig.getLocalRepositoryConfig();
            }

            /*
             * The local repository should be partially configured already by setLocalRepositoryMode()
             */
            if (localRepositoryConfig == null)
            {
                throw new OMAGInvalidParameterException(OMAGAdminErrorCode.LOCAL_REPOSITORY_MODE_NOT_SET.getMessageDefinition(serverName),
                                                        this.getClass().getName(),
                                                        methodName);
            }

            /*
             * Set up the metadata collection name in the local repository config and save.
             */
            response.setGUID(localRepositoryConfig.getMetadataCollectionId());

            return response;

        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Return the local metadata collection id.  If the local repository is not configured
     * then the invalid parameter exception is returned.
     *
     * @param userId                      user that is issuing the request.
     * @param serverName                  local server name.
     * @param metadataCollectionId        new value for the metadata collection id
     * @return guid response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    public VoidResponse setLocalMetadataCollectionId(String userId,
                                                     String serverName,
                                                     String metadataCollectionId)
    {
        final String methodName = "setLocalMetadataCollectionId";
        final String parameterNameName = "setLocalMetadataCollectionId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validatePropertyNotNull(metadataCollectionId, parameterNameName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();
            LocalRepositoryConfig    localRepositoryConfig    = null;

            /*
             * Extract any existing local repository configuration
             */
            if (repositoryServicesConfig != null)
            {
                localRepositoryConfig = repositoryServicesConfig.getLocalRepositoryConfig();
            }

            /*
             * The local repository should be partially configured already by setLocalRepositoryMode()
             */
            if (localRepositoryConfig == null)
            {
                throw new OMAGInvalidParameterException(OMAGAdminErrorCode.LOCAL_REPOSITORY_MODE_NOT_SET.getMessageDefinition(serverName),
                                                        this.getClass().getName(),
                                                        methodName);
            }

            /*
             * Set up the metadata collection name in the local repository config and save.
             */
            localRepositoryConfig.setMetadataCollectionId(metadataCollectionId);

            List<String>  configAuditTrail          = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date() + " " + userId + " updated metadata collection id to " + metadataCollectionId +" for the local repository.");

            serverConfig.setAuditTrail(configAuditTrail);

            /*
             * Save the open metadata repository services config in the server's config
             */
            repositoryServicesConfig.setLocalRepositoryConfig(localRepositoryConfig);

            serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);
            configStore.saveServerConfig(serverName, methodName, serverConfig);

            return response;

        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the local metadata collection name.  If this is not set then the default value is the
     * local server name.
     *
     * @param userId                      user that is issuing the request.
     * @param serverName                  local server name.
     * @param localMetadataCollectionName metadata collection name
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    public VoidResponse setLocalMetadataCollectionName(String               userId,
                                                       String               serverName,
                                                       String               localMetadataCollectionName)
    {
        final String methodName = "setLocalMetadataCollectionName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateMetadataCollectionName(localMetadataCollectionName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();
            LocalRepositoryConfig    localRepositoryConfig    = null;

            /*
             * Extract any existing local repository configuration
             */
            if (repositoryServicesConfig != null)
            {
                localRepositoryConfig = repositoryServicesConfig.getLocalRepositoryConfig();
            }

            /*
             * The local repository should be partially configured already by setLocalRepositoryMode()
             */
            if (localRepositoryConfig == null)
            {
                throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.LOCAL_REPOSITORY_MODE_NOT_SET.getMessageDefinition(serverName),
                                                          this.getClass().getName(),
                                                          methodName);
            }

            /*
             * Set up the metadata collection name in the local repository config and save.
             */
            localRepositoryConfig.setMetadataCollectionName(localMetadataCollectionName);

            this.setLocalRepositoryConfig(userId, serverName, localRepositoryConfig);
        }
        catch (OMAGConfigurationErrorException  error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Enable registration of server to an open metadata repository cohort.  This is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to changes in their supported metadata types and instances.
     * They are also able to query each other's metadata directly through REST calls.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param cohortName  name of the cohort
     * @param cohortTopicStructure the style of cohort topic set up to use
     * @param configurationProperties additional properties for the cohort
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName, cohortName or serviceMode parameter.
     * OMAGConfigurationErrorException the event bus is not set.
     */
    public VoidResponse addCohortRegistration(String               userId,
                                              String               serverName,
                                              String               cohortName,
                                              CohortTopicStructure cohortTopicStructure,
                                              Map<String, Object>  configurationProperties)
    {
        final String methodName = "addCohortRegistration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            EventBusConfig   eventBusConfig  = errorHandler.validateEventBusIsSet(serverName, serverConfig, methodName);

            /*
             * Set up a new cohort
             */
            OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();

            CohortConfig newCohortConfig = configurationFactory.getDefaultCohortConfig(serverConfig.getLocalServerName(),
                                                                                       cohortName,
                                                                                       cohortTopicStructure,
                                                                                       configurationProperties,
                                                                                       eventBusConfig.getConnectorProvider(),
                                                                                       eventBusConfig.getTopicURLRoot(),
                                                                                       serverConfig.getLocalServerId(),
                                                                                       eventBusConfig.getConfigurationProperties());


            this.setCohortConfig(userId, serverName, cohortName, newCohortConfig);
        }
        catch (OMAGConfigurationErrorException  error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the topic name that is used by this server to contact the other members of the
     * open metadata repository cohort.  This call can only be made once the cohort
     * is set up with addCohortRegistration().
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @return string response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or cohortName parameter.
     * OMAGConfigurationErrorException the cohort is not setup, or set up in an non-standard way.
     */
    public StringResponse getCohortTopicName(String userId,
                                             String serverName,
                                             String cohortName)
    {
        final String methodName = "getCohortTopicName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        StringResponse response = new StringResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            if (serverConfig != null)
            {
                CohortConfig currentCohortDetails = errorHandler.validateCohortIsSet(serverName, serverConfig, cohortName, methodName);

                if (currentCohortDetails != null)
                {
                    response.setResultString(this.getCohortTopicName(currentCohortDetails.getCohortOMRSTopicConnection()));
                }
            }
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the current topic names for the three dedicated topics of the cohort.  This call can only be made once the cohort
     * is set up with addCohortRegistration().
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @return list of topics response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or cohortName parameter.
     * OMAGConfigurationErrorException the cohort is not setup, or set up in an non-standard way.
     */
    public DedicatedTopicListResponse getDedicatedCohortTopicNames(String userId,
                                                                   String serverName,
                                                                   String cohortName)
    {
        final String methodName = "getDedicatedCohortTopicNames";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DedicatedTopicListResponse response = new DedicatedTopicListResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            if (serverConfig != null)
            {
                CohortConfig currentCohortDetails = errorHandler.validateCohortIsSet(serverName, serverConfig, cohortName, methodName);

                if (currentCohortDetails != null)
                {
                    DedicatedTopicList topicNames = new DedicatedTopicList();

                    topicNames.setRegistrationTopicName(this.getCohortTopicName(currentCohortDetails.getCohortOMRSRegistrationTopicConnection()));
                    topicNames.setTypesTopicName(this.getCohortTopicName(currentCohortDetails.getCohortOMRSTypesTopicConnection()));
                    topicNames.setInstancesTopicName(this.getCohortTopicName(currentCohortDetails.getCohortOMRSInstancesTopicConnection()));

                    response.setDedicatedTopicList(topicNames);
                }
            }
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the topic name from an event topic connection.
     *
     * @param eventTopicConnection  connection object to interrogate
     * @return string  or null
     */
    private String getCohortTopicName(Connection eventTopicConnection)
    {
        if (eventTopicConnection instanceof VirtualConnection)
        {
            VirtualConnection virtualConnection = (VirtualConnection)eventTopicConnection;
            List<EmbeddedConnection> embeddedConnections = virtualConnection.getEmbeddedConnections();

            if ((embeddedConnections != null) && (embeddedConnections.size() == 1))
            {
                Connection connection = embeddedConnections.get(0).getEmbeddedConnection();

                if (connection != null)
                {
                    Endpoint endpoint = connection.getEndpoint();

                    if (endpoint != null)
                    {
                        return endpoint.getAddress();
                    }
                }
            }
        }

       return null;
    }


    /**
     * Change the topic name that is used by this server to contact the other members of the
     * open metadata repository cohort.  Note this name needs to be configured to same
     * in all members of a cohort.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param topicName new topic name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or cohortName parameter.
     * OMAGConfigurationErrorException the cohort is not setup, or set up in an non-standard way.
     */
    public VoidResponse overrideCohortTopicName(String userId,
                                                String serverName,
                                                String cohortName,
                                                String topicName)
    {
        final String methodName = "overrideCohortTopicName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            if (serverConfig != null)
            {
                CohortConfig currentCohortDetails = errorHandler.validateCohortIsSet(serverName, serverConfig, cohortName, methodName);

                if (currentCohortDetails != null)
                {
                    Connection eventTopicConnection = overrideCohortTopicName(currentCohortDetails.getCohortOMRSTopicConnection(), topicName);

                    if (eventTopicConnection != null)
                    {
                        currentCohortDetails.setCohortOMRSTopicConnection(eventTopicConnection);

                        this.setCohortConfig(userId, serverName, cohortName, currentCohortDetails);
                    }
                    else
                    {
                        errorHandler.logNoCohortTopicChange(cohortName, serverName, methodName);
                    }

                }
            }
        }
        catch (OMAGConfigurationErrorException  error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Change the topic name that is used by this server to register members in the
     * open metadata repository cohort.  Note this name needs to be configured to same
     * in all members of a cohort.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param topicName new topic name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or cohortName parameter.
     * OMAGConfigurationErrorException the cohort is not setup, or set up in an non-standard way.
     */
    public VoidResponse overrideRegistrationCohortTopicName(String userId,
                                                            String serverName,
                                                            String cohortName,
                                                            String topicName)
    {
        final String methodName = "overrideRegistrationCohortTopicName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            if (serverConfig != null)
            {
                CohortConfig currentCohortDetails = errorHandler.validateCohortIsSet(serverName, serverConfig, cohortName, methodName);

                if (currentCohortDetails != null)
                {
                    Connection eventTopicConnection = overrideCohortTopicName(currentCohortDetails.getCohortOMRSRegistrationTopicConnection(), topicName);

                    if (eventTopicConnection != null)
                    {
                        currentCohortDetails.setCohortOMRSRegistrationTopicConnection(eventTopicConnection);

                        this.setCohortConfig(userId, serverName, cohortName, currentCohortDetails);
                    }
                    else
                    {
                        errorHandler.logNoCohortTopicChange(cohortName, serverName, methodName);
                    }

                }
            }
        }
        catch (OMAGConfigurationErrorException  error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Change the topic name that is used by this server to verify type consistency with the other members of the
     * open metadata repository cohort.  Note this name needs to be configured to same
     * in all members of a cohort.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param topicName new topic name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or cohortName parameter.
     * OMAGConfigurationErrorException the cohort is not setup, or set up in an non-standard way.
     */
    public VoidResponse overrideTypesCohortTopicName(String userId,
                                                     String serverName,
                                                     String cohortName,
                                                     String topicName)
    {
        final String methodName = "overrideTypesCohortTopicName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            if (serverConfig != null)
            {
                CohortConfig currentCohortDetails = errorHandler.validateCohortIsSet(serverName, serverConfig, cohortName, methodName);

                if (currentCohortDetails != null)
                {
                    Connection eventTopicConnection = overrideCohortTopicName(currentCohortDetails.getCohortOMRSTypesTopicConnection(), topicName);

                    if (eventTopicConnection != null)
                    {
                        currentCohortDetails.setCohortOMRSTypesTopicConnection(eventTopicConnection);

                        this.setCohortConfig(userId, serverName, cohortName, currentCohortDetails);
                    }
                    else
                    {
                        errorHandler.logNoCohortTopicChange(cohortName, serverName, methodName);
                    }

                }
            }
        }
        catch (OMAGConfigurationErrorException  error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Change the topic name that is used by this server to exchange metadata instances with the other members of the
     * open metadata repository cohort.  Note this name needs to be configured to same
     * in all members of a cohort.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param topicName new topic name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or cohortName parameter.
     * OMAGConfigurationErrorException the cohort is not setup, or set up in an non-standard way.
     */
    public VoidResponse overrideInstancesCohortTopicName(String userId,
                                                         String serverName,
                                                         String cohortName,
                                                         String topicName)
    {
        final String methodName = "overrideInstancesCohortTopicName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            if (serverConfig != null)
            {
                CohortConfig currentCohortDetails = errorHandler.validateCohortIsSet(serverName, serverConfig, cohortName, methodName);

                if (currentCohortDetails != null)
                {
                    Connection eventTopicConnection = overrideCohortTopicName(currentCohortDetails.getCohortOMRSInstancesTopicConnection(), topicName);

                    if (eventTopicConnection != null)
                    {
                        currentCohortDetails.setCohortOMRSInstancesTopicConnection(eventTopicConnection);

                        this.setCohortConfig(userId, serverName, cohortName, currentCohortDetails);
                    }
                    else
                    {
                        errorHandler.logNoCohortTopicChange(cohortName, serverName, methodName);
                    }

                }
            }
        }
        catch (OMAGConfigurationErrorException  error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Change the topic name that is used by this server to contact the other members of the
     * open metadata repository cohort.  Note this name needs to be configured to same
     * in all members of a cohort.
     *
     * @param eventTopicConnection  connection object to update
     * @param topicName new topic name
     * @return
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or cohortName parameter.
     * OMAGConfigurationErrorException the cohort is not setup, or set up in an non-standard way.
     */
    private Connection overrideCohortTopicName(Connection eventTopicConnection,
                                               String     topicName)
    {
        if (eventTopicConnection instanceof VirtualConnection)
        {
            VirtualConnection virtualConnection = (VirtualConnection)eventTopicConnection;
            List<EmbeddedConnection> embeddedConnections = virtualConnection.getEmbeddedConnections();

            if ((embeddedConnections != null) && (embeddedConnections.size() == 1))
            {
                EmbeddedConnection embeddedConnection = embeddedConnections.get(0);
                Connection connection = embeddedConnection.getEmbeddedConnection();

                if (connection != null)
                {
                    Endpoint endpoint = connection.getEndpoint();

                    if (endpoint != null)
                    {
                        endpoint.setAddress(topicName);
                        connection.setEndpoint(endpoint);
                        embeddedConnection.setEmbeddedConnection(connection);
                        embeddedConnections = new ArrayList<>();
                        embeddedConnections.add(embeddedConnection);
                        virtualConnection.setEmbeddedConnections(embeddedConnections);

                        return virtualConnection;
                    }
                }
            }
        }

       return null;
    }


    /*
     * =============================================================
     * Configure server - advanced options overriding defaults
     */

    /**
     * Set up the list of audit log destinations.  These destinations are expressed as Connection objects
     * to the connectors that will handle the audit log records.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param auditLogDestination connection object for audit log destination
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    public VoidResponse addAuditLogDestination(String                userId,
                                               String                serverName,
                                               Connection            auditLogDestination)
    {
        final String methodName = "addAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateServerConnection(auditLogDestination, serverName, methodName);

            if (auditLogDestination != null)
            {
                OMAGServerConfig serverConfig     = configStore.getServerConfig(userId, serverName, methodName);
                List<String>     configAuditTrail = serverConfig.getAuditTrail();

                if (configAuditTrail == null)
                {
                    configAuditTrail = new ArrayList<>();
                }

                RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

                if (repositoryServicesConfig == null)
                {
                    OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();

                    repositoryServicesConfig = configurationFactory.getDefaultRepositoryServicesConfig();
                }

                List<Connection>  auditLogDestinations = repositoryServicesConfig.getAuditLogConnections();

                if (auditLogDestinations == null)
                {
                    auditLogDestinations = new ArrayList<>();
                    configAuditTrail.add(new Date().toString() + " " + userId + " created first audit log destination.");

                }
                else
                {
                    configAuditTrail.add(new Date().toString() + " " + userId + " added to list of audit log destinations.");
                }

                auditLogDestinations.add(auditLogDestination);
                repositoryServicesConfig.setAuditLogConnections(auditLogDestinations);

                serverConfig.setAuditTrail(configAuditTrail);

                /*
                 * Save the open metadata repository services config in the server's config
                 */
                serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);
                configStore.saveServerConfig(serverName, methodName, serverConfig);
            }
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update an audit log destination connection that is identified with the supplied destination connection name with
     * the supplied connection object.
     * It is possible to supply a suppliedConnectionName that matches an existing connection and the new connection specifies a different qualifiedName.
     * in this way it is possible to rename Connections.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param suppliedConnectionName name of the audit log destination to be updated
     * @param suppliedConnection connection object that defines the audit log destination
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or suppliedConnectionName parameter.
     */
    public VoidResponse updateAuditLogDestination(String     userId,
                                                  String     serverName,
                                                  String     suppliedConnectionName,
                                                  Connection suppliedConnection)
    {
        final String methodName = "updateAuditLogDestination";
        final String functionName = "update";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateServerConnection(suppliedConnection, serverName, methodName);

            if (suppliedConnection != null)
            {
                OMAGServerConfig serverConfig     = configStore.getServerConfig(userId, serverName, methodName);
                List<String>     configAuditTrail = serverConfig.getAuditTrail();

                if (configAuditTrail == null)
                {
                    configAuditTrail = new ArrayList<>();
                }

                RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

                if (repositoryServicesConfig == null)
                {
                    OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();

                    repositoryServicesConfig = configurationFactory.getDefaultRepositoryServicesConfig();
                }

                List<Connection>  auditLogDestinations = repositoryServicesConfig.getAuditLogConnections();

                if (auditLogDestinations == null)
                {
                    /*
                     * no audit logs so cannot delete a requested one.
                     */
                    throw new OMAGInvalidParameterException(OMAGAdminErrorCode.AUDIT_LOG_DESTINATION_NOT_FOUND.getMessageDefinition(suppliedConnectionName,
                                                                                                                                    functionName),
                                                            this.getClass().getName(),
                                                            methodName);

                }
                else
                {
                    List<Connection> newAuditLogConnections = new ArrayList<>();
                    boolean          found = false;

                    for (Connection existingConnection : auditLogDestinations)
                    {
                        if (existingConnection != null)
                        {
                            if (existingConnection.getQualifiedName().equals(suppliedConnectionName))
                            {
                                newAuditLogConnections.add(suppliedConnection);
                                found = true;
                                configAuditTrail.add(new Date().toString() + " " + userId + " updated " + existingConnection.getQualifiedName() + " in the list of audit log destinations.");
                            }
                            else
                            {
                                newAuditLogConnections.add(existingConnection);
                            }
                        }
                    }

                    if (! found)
                    {
                        /*
                         * Try the display name
                         */
                        for (Connection existingConnection : auditLogDestinations)
                        {
                            if (existingConnection != null)
                            {
                                if (existingConnection.getDisplayName().equals(suppliedConnectionName))
                                {
                                    newAuditLogConnections.add(suppliedConnection);
                                    found = true;
                                    configAuditTrail.add(new Date().toString() + " " + userId + " updated " + existingConnection.getQualifiedName() + " in the list of audit log destinations.");
                                }
                                else
                                {
                                    newAuditLogConnections.add(existingConnection);
                                }
                            }
                        }
                    }

                    if (! found)
                    {
                        /*
                         * error cannot find the audit log to update
                         */
                        throw new OMAGInvalidParameterException(OMAGAdminErrorCode.AUDIT_LOG_DESTINATION_NOT_FOUND.getMessageDefinition(suppliedConnectionName,
                                                                                                                                        functionName),
                                                                this.getClass().getName(),
                                                                methodName);
                    }
                    else
                    {
                        repositoryServicesConfig.setAuditLogConnections(newAuditLogConnections);

                        serverConfig.setAuditTrail(configAuditTrail);

                        /*
                         * Save the open metadata repository services config in the server's config
                         */
                        serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);
                        configStore.saveServerConfig(serverName, methodName, serverConfig);
                    }
                }
            }
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Delete an audit log destination connection, that is identified with the supplied destination connection name.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param suppliedConnectionName name of the audit log destination to be deleted
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or suppliedConnectionName parameter.
     */
    public VoidResponse clearAuditLogDestination(String userId,
                                                 String serverName,
                                                 String suppliedConnectionName)
    {
        final String methodName   = "clearAuditLogDestination";
        final String functionName = "delete";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig     = configStore.getServerConfig(userId, serverName, methodName);
            List<String>     configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            if (repositoryServicesConfig == null)
            {
                OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();

                repositoryServicesConfig = configurationFactory.getDefaultRepositoryServicesConfig();
            }

            List<Connection>  auditLogDestinations = repositoryServicesConfig.getAuditLogConnections();

            if (auditLogDestinations == null)
            {
                /*
                 * error nothing to delete
                 */
                throw new OMAGInvalidParameterException(OMAGAdminErrorCode.AUDIT_LOG_DESTINATION_NOT_FOUND.getMessageDefinition(suppliedConnectionName,
                                                                                                                                functionName),
                                                        this.getClass().getName(),
                                                        methodName);
            }
            else
            {
                List<Connection> newAuditLogConnections = new ArrayList<>();
                boolean          found = false;

                for (Connection existingConnection : auditLogDestinations)
                {
                    if (existingConnection != null)
                    {
                        if (existingConnection.getQualifiedName().equals(suppliedConnectionName))
                        {
                            found = true;
                            configAuditTrail.add(new Date().toString() + " " + userId + " removed " + existingConnection.getQualifiedName() + " from the list of audit log destinations.");
                        }
                        else
                        {
                            newAuditLogConnections.add(existingConnection);
                        }
                    }
                }

                if (! found)
                {
                    /*
                     * Try searching with the display name
                     */
                    for (Connection existingConnection : auditLogDestinations)
                    {
                        if (existingConnection != null)
                        {
                            if (existingConnection.getDisplayName().equals(suppliedConnectionName))
                            {
                                found = true;
                                configAuditTrail.add(new Date().toString() + " " + userId + " removed " + existingConnection.getQualifiedName() + " from the list of audit log destinations.");
                            }
                            else
                            {
                                newAuditLogConnections.add(existingConnection);
                            }
                        }
                    }
                }

                if (! found)
                {
                    /*
                     * Error did not find a audit log to remove
                     */
                    throw new OMAGInvalidParameterException(OMAGAdminErrorCode.AUDIT_LOG_DESTINATION_NOT_FOUND.getMessageDefinition(suppliedConnectionName,
                                                                                                                                    functionName),
                                                            this.getClass().getName(),
                                                            methodName);
                }
                else
                {
                    repositoryServicesConfig.setAuditLogConnections(newAuditLogConnections);

                    serverConfig.setAuditTrail(configAuditTrail);

                    /*
                     * Save the open metadata repository services config in the server's config
                     */
                    serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);
                    configStore.saveServerConfig(serverName, methodName, serverConfig);
                }
            }
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the list of audit log destinations.  These destinations are expressed as Connection objects
     * to the connectors that will handle the audit log records.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param auditLogDestinations list of connection objects
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    public VoidResponse setAuditLogDestinations(String                userId,
                                                String                serverName,
                                                List<Connection>      auditLogDestinations)
    {
        final String methodName = "setAuditLogDestinations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            if (auditLogDestinations != null)
            {
                for (Connection connection : auditLogDestinations)
                {
                    errorHandler.validateServerConnection(connection, serverName, methodName);
                }
            }

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if (auditLogDestinations == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " setting up no audit log destinations.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated list of audit log destinations.");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            /*
             * Set up the local repository config in the open metadata repository services config.
             */
            if (repositoryServicesConfig != null)
            {
                repositoryServicesConfig.setAuditLogConnections(auditLogDestinations);
            }
            else if (auditLogDestinations != null)
            {
                OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();

                repositoryServicesConfig = configurationFactory.getDefaultRepositoryServicesConfig();
                repositoryServicesConfig.setAuditLogConnections(auditLogDestinations);
            }

            /*
             * Save the open metadata repository services config in the server's config
             */
            serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);
            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of audit log destinations.  These destinations are expressed as Connection objects
     * to the connectors that will handle the audit log records.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return connection list response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    public ConnectionListResponse getAuditLogDestinations(String userId,
                                                          String serverName)
    {
        final String methodName = "getAuditLogDestinations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionListResponse response = new ConnectionListResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            if (repositoryServicesConfig != null)
            {
                response.setConnections(repositoryServicesConfig.getAuditLogConnections());
            }
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Clear the list of audit log destinations.  These destinations are expressed as Connection objects
     * to the connectors that will handle the audit log records.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    public VoidResponse clearAuditLogDestinations(String userId,
                                                  String serverName)
    {
        return this.setAuditLogDestinations(userId, serverName, null);
    }


    /**
     * Set up the list of open metadata archives.  These are open metadata types and instances that are loaded at
     * repository start up.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param openMetadataArchives list of connection objects
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    public VoidResponse setOpenMetadataArchives(String                userId,
                                                String                serverName,
                                                List<Connection>      openMetadataArchives)
    {
        final String methodName = "setOpenMetadataArchives";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            List<String>  configAuditTrail  = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if (openMetadataArchives == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " clearing open metadata archives.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated list of open metadata archives loaded at server start up.");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            /*
             * Set up the local repository config in the open metadata repository services config.
             */
            if (repositoryServicesConfig != null)
            {
                repositoryServicesConfig.setOpenMetadataArchiveConnections(openMetadataArchives);
            }
            else if (openMetadataArchives != null)
            {
                OMRSConfigurationFactory configurationFactory     = new OMRSConfigurationFactory();

                repositoryServicesConfig = configurationFactory.getDefaultRepositoryServicesConfig();
                repositoryServicesConfig.setOpenMetadataArchiveConnections(openMetadataArchives);
            }

            /*
             * Save the open metadata repository services config in the server's config
             */
            serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);
            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of open metadata archives.  These are open metadata types and instances that are loaded at
     * repository start up.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return connection list response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    public ConnectionListResponse getOpenMetadataArchives(String userId,
                                                          String serverName)
    {
        final String methodName = "getOpenMetadataArchives";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionListResponse response = new ConnectionListResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            if (repositoryServicesConfig != null)
            {
                response.setConnections(repositoryServicesConfig.getOpenMetadataArchiveConnections());
            }
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Clear the list of open metadata archives for loading at server startup.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    public VoidResponse clearOpenMetadataArchives(String userId,
                                                  String serverName)
    {
        return this.setOpenMetadataArchives(userId, serverName, null);
    }


    /**
     * Set up the configuration for the local repository.  This overrides the current values.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param localRepositoryConfig  configuration properties for the local repository.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryConfig parameter.
     */
    public VoidResponse setLocalRepositoryConfig(String                userId,
                                                 String                serverName,
                                                 LocalRepositoryConfig localRepositoryConfig)
    {
        final String methodName = "setLocalRepositoryConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            List<String>  configAuditTrail          = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if (localRepositoryConfig == null)
            {
                configAuditTrail.add(new Date() + " " + userId + " setting up a null local repository.");
            }
            else
            {
                configAuditTrail.add(new Date() + " " + userId + " updated configuration for the local repository.");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            /*
             * Set up the local repository config in the open metadata repository services config.
             */
            if (repositoryServicesConfig != null)
            {
                LocalRepositoryConfig    existingLocalRepositoryConfig = repositoryServicesConfig.getLocalRepositoryConfig();
                if ((localRepositoryConfig != null) && (existingLocalRepositoryConfig != null))
                {
                    String  existingMetadataCollectionId = existingLocalRepositoryConfig.getMetadataCollectionId();

                    if (existingMetadataCollectionId != null)
                    {
                        configAuditTrail.add(new Date() + " " + userId + " preserving local metadata collection id " + existingMetadataCollectionId + ".");
                        localRepositoryConfig.setMetadataCollectionId(existingMetadataCollectionId);
                    }
                }

                repositoryServicesConfig.setLocalRepositoryConfig(localRepositoryConfig);
            }
            else if (localRepositoryConfig != null)
            {
                OMRSConfigurationFactory configurationFactory     = new OMRSConfigurationFactory();

                repositoryServicesConfig = configurationFactory.getDefaultRepositoryServicesConfig();
                repositoryServicesConfig.setLocalRepositoryConfig(localRepositoryConfig);
            }

            /*
             * Save the open metadata repository services config in the server's config
             */
            serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);
            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the configuration for the local repository.  This overrides the current values.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return local repository config response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    public LocalRepositoryConfigResponse getLocalRepositoryConfig(String userId,
                                                                  String serverName)
    {
        final String methodName = "setLocalRepositoryConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LocalRepositoryConfigResponse response = new LocalRepositoryConfigResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            if (repositoryServicesConfig != null)
            {
                response.setConfig(repositoryServicesConfig.getLocalRepositoryConfig());
            }
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove all configuration for a local repository.  The default is no local repository.  This call
     * can be used to remove subsequent local repository configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public VoidResponse clearLocalRepositoryConfig(String userId,
                                                   String serverName)
    {
        final String methodName = "clearLocalRepositoryConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            this.setLocalRepositoryConfig(userId, serverName, null);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the configuration properties for a cohort.  This may reconfigure an existing cohort or create a
     * cohort.  Use setCohortMode to delete a cohort.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param cohortName  name of the cohort
     * @param cohortConfig  configuration for the cohort
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName, cohortName or cohortConfig parameter.
     */
    public VoidResponse setCohortConfig(String       userId,
                                        String       serverName,
                                        String       cohortName,
                                        CohortConfig cohortConfig)
    {
        final String methodName = "setCohortConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();
            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();
            List<CohortConfig>       existingCohortConfigs = null;
            List<CohortConfig>       newCohortConfigs = new ArrayList<>();

            List<String>  configAuditTrail          = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if (cohortConfig == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for cohort " + cohortName + ".");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for cohort " + cohortName + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            /*
             * Extract any existing local repository configuration
             */
            if (repositoryServicesConfig != null)
            {
                existingCohortConfigs = repositoryServicesConfig.getCohortConfigList();
            }

            /*
             * Transfer the cohort configurations of all other cohorts into the new cohort list
             */
            if (existingCohortConfigs != null)
            {
                /*
                 * If there is already a cohort of the same name then effectively remove it.
                 */
                for (CohortConfig existingCohort : existingCohortConfigs)
                {
                    if (existingCohort != null)
                    {
                        String existingCohortName = existingCohort.getCohortName();

                        if (! cohortName.equals(existingCohortName))
                        {
                            newCohortConfigs.add(existingCohort);
                        }
                    }
                }
            }

            /*
             * Add the new cohort to the list of cohorts
             */
            if (cohortConfig != null)
            {
                newCohortConfigs.add(cohortConfig);
            }

            /*
             * If there are no cohorts to save then remove the array list.
             */
            if (newCohortConfigs.isEmpty())
            {
                newCohortConfigs = null;
            }

            /*
             * Add the cohort list to the open metadata repository services config
             */
            if (repositoryServicesConfig != null)
            {
                repositoryServicesConfig.setCohortConfigList(newCohortConfigs);
            }
            else if (newCohortConfigs != null)
            {
                repositoryServicesConfig = configurationFactory.getDefaultRepositoryServicesConfig();

                repositoryServicesConfig.setCohortConfigList(newCohortConfigs);
            }

            serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);
            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the current configuration for a cohort.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @return cohort config response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName, cohortName or serviceMode parameter.
     * OMAGConfigurationErrorException the event bus is not set.
     */
    public CohortConfigResponse getCohortConfig(String userId,
                                                String serverName,
                                                String cohortName)
    {
        final String methodName = "getCohortConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CohortConfigResponse response = new CohortConfigResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            if (serverConfig != null)
            {
                response.setConfig(errorHandler.validateCohortIsSet(serverName, serverConfig, cohortName, methodName));
            }
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the mode for an open metadata repository cohort.  This is a group of open metadata repositories that
     * are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.  Each cohort needs
     * a unique name.  The members of the cohort use a shared topic to exchange registration information and
     * events related to changes in their supported metadata types and instances.  They are also able to
     * query each other's metadata directly through REST calls.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName, cohortName or serviceMode parameter.
     */
    public VoidResponse clearCohortConfig(String          userId,
                                          String          serverName,
                                          String          cohortName)
    {
        final String methodName = "clearCohortRegistration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            this.setCohortConfig(userId, serverName, cohortName, null);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the configuration properties for an OMAG Server in a single command.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param omagServerConfig  configuration for the server
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or OMAGServerConfig parameter.
     */
    public VoidResponse setOMAGServerConfig(String           userId,
                                            String           serverName,
                                            OMAGServerConfig omagServerConfig)
    {
        final String methodName = "setOMAGServerConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            if (omagServerConfig == null)
            {
               throw new OMAGInvalidParameterException(OMAGAdminErrorCode.NULL_SERVER_CONFIG.getMessageDefinition(serverName),
                                                        this.getClass().getName(),
                                                        methodName);
            }

            List<String>  configAuditTrail = omagServerConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date().toString() + " " + userId + " deployed configuration for server.");

            omagServerConfig.setAuditTrail(configAuditTrail);

            configStore.saveServerConfig(serverName, methodName, omagServerConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Push the configuration for the server to another OMAG Server Platform.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param destinationPlatform  location of the platform where the config is to be deployed to
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration or
     * OMAGInvalidParameterException invalid serverName or destinationPlatform parameter.
     */
    public VoidResponse deployOMAGServerConfig(String         userId,
                                               String         serverName,
                                               URLRequestBody destinationPlatform)
    {
        final String methodName = "deployOMAGServerConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            String  serverURLRoot = serverConfig.getLocalServerURL();

            if ((destinationPlatform != null) && (destinationPlatform.getUrlRoot() != null))
            {
                serverURLRoot = destinationPlatform.getUrlRoot();
            }

            ConfigurationManagementClient client = new ConfigurationManagementClient(userId,
                                                                                     serverURLRoot);

            client.setOMAGServerConfig(serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (OMAGConfigurationErrorException error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the complete set of configuration properties in use by the server.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return OMAGServerConfig properties or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public OMAGServerConfigResponse getStoredConfiguration(String userId,
                                                           String serverName)
    {
        final String methodName = "getStoredConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        OMAGServerConfigResponse response = new OMAGServerConfigResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            response.setOMAGServerConfig(configStore.getServerConfig(userId, serverName, methodName));
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
    /**
     * Return the stored configuration documents stored on the platform
     *
     * @param userId  user that is issuing the request
     * @return OMAGServerConfigs properties or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid parameter occurred while processing.
     */
    public OMAGServerConfigsResponse retrieveAllServerConfigs(String userId)
    {
        final String methodName = "retrieveAllServerConfigs";

        RESTCallToken token = restCallLogger.logRESTCall("", userId, methodName);

        OMAGServerConfigsResponse response = new OMAGServerConfigsResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);
            response.setOMAGServerConfigs(configStore.retrieveAllServerConfigs(userId));
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the configuration for the server.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse clearOMAGServerConfig(String userId,
                                              String serverName)
    {
        final String methodName = "clearOMAGServerConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            configStore.saveServerConfig(serverName, methodName, null);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

}
