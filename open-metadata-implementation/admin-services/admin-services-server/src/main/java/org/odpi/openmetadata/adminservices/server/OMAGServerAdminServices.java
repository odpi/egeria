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
import org.odpi.openmetadata.adminservices.properties.DedicatedTopicList;
import org.odpi.openmetadata.adminservices.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataPlatformSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;
import org.odpi.openmetadata.tokencontroller.TokenController;
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
public class OMAGServerAdminServices extends TokenController
{
    private final static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminServices.class),
                                                                            CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName());


    private final OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();
    private final        OMAGServerErrorHandler errorHandler         = new OMAGServerErrorHandler();
    private static final RESTExceptionHandler   restExceptionHandler = new RESTExceptionHandler();


    /**
     * Return the derived server type that is created from the classification of the server configuration.
     *
     * @param serverName local server name.
     * @param delegatingUserId external userId making request
     * @return server type classification response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or serverType parameter.
     */
    public ServerTypeClassificationResponse getServerTypeClassification(String serverName,
                                                                        String delegatingUserId)
    {
        final String methodName = "getServerTypeClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ServerTypeClassificationResponse response = new ServerTypeClassificationResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            ServerTypeClassifier classifier = new ServerTypeClassifier(serverName, configStore.getServerConfig(userId, delegatingUserId, serverName, false, methodName));
            ServerTypeClassificationSummary summary = new ServerTypeClassificationSummary(classifier.getServerType());

            response.setServerTypeClassification(summary);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
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
     * @param serverName local server name.
     * @param delegatingUserId external userId making request
     * @param typeName   short description for the type of server.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or serverType parameter.
     */
    public VoidResponse setServerType(String serverName,
                                      String delegatingUserId,
                                      String typeName)
    {
        final String methodName = "setServerType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if ((typeName != null) && (typeName.isBlank()))
            {
                typeName = null;
            }

            if (typeName == null)
            {
                configAuditTrail.add(new Date() + " " + userId + " removed configuration for local server type name.");
            }
            else
            {
                configAuditTrail.add(new Date() + " " + userId + " updated configuration for local server type name to " + typeName + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setLocalServerType(typeName);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the name of the organization that is running this server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is null.
     *
     * @param serverName local server name.
     * @param delegatingUserId external userId making request
     * @param name       String name of the organization.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or organizationName parameter.
     */
    public VoidResponse setOrganizationName(String serverName,
                                            String delegatingUserId,
                                            String name)
    {
        final String methodName = "setOrganizationName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            this.setOrganizationName(serverConfig, userId, serverName, name, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the name of the organization that is running this server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is null.
     *
     * @param serverConfig server configuration document to update
     * @param userId     user that is issuing the request.
     * @param serverName local server name.
     * @param name       String name of the organization.
     * @param methodName calling method
     * @throws InvalidParameterException invalid serverName or organizationName parameter.
     */
    private void setOrganizationName(OMAGServerConfig serverConfig,
                                     String           userId,
                                     String           serverName,
                                     String           name,
                                     String           methodName) throws InvalidParameterException
    {
        List<String> configAuditTrail = serverConfig.getAuditTrail();

        if (configAuditTrail == null)
        {
            configAuditTrail = new ArrayList<>();
        }

        if (name != null && (name.isBlank()))
        {
            name = null;
        }

        if (name == null)
        {
            configAuditTrail.add(new Date() + " " + userId + " removed configuration for local server's owning organization's name.");
        }
        else
        {
            configAuditTrail.add(new Date() + " " + userId + " updated configuration for local server's owning organization's name to " + name + ".");
        }

        serverConfig.setAuditTrail(configAuditTrail);
        serverConfig.setOrganizationName(name);

        configStore.saveServerConfig(serverName, methodName, serverConfig);
    }


    /**
     * Set up the description of this server. The default value is null.
     *
     * @param serverName  local server description.
     * @param delegatingUserId external userId making request
     * @param description String description of the server.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or organizationName parameter.
     */
    public VoidResponse setServerDescription(String serverName,
                                             String delegatingUserId,
                                             String description)
    {

        final String methodName = "setServerDescription";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            this.setServerDescription(serverConfig, userId, serverName, description, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the description of this server. The default value is null.
     *
     * @param serverConfig server configuration document to update
     * @param userId      user that is issuing the request.
     * @param serverName  local server description.
     * @param description String description of the server.
     * @param methodName calling method
     * @throws InvalidParameterException invalid serverName or organizationName parameter.
     */
    private void setServerDescription(OMAGServerConfig serverConfig,
                                      String           userId,
                                      String           serverName,
                                      String           description,
                                      String           methodName) throws InvalidParameterException
    {
        List<String> configAuditTrail = serverConfig.getAuditTrail();

        if (configAuditTrail == null)
        {
            configAuditTrail = new ArrayList<>();
        }

        if ((description != null) && (description.isEmpty()))
        {
            description = null;
        }

        if (description == null)
        {
            configAuditTrail.add(new Date() + " " + userId + " removed configuration for local server's description.");
        }
        else
        {
            configAuditTrail.add(new Date() + " " + userId + " updated configuration for local server's description to " + description + ".");
        }

        serverConfig.setAuditTrail(configAuditTrail);
        serverConfig.setLocalServerDescription(description);

        configStore.saveServerConfig(serverName, methodName, serverConfig);
    }


    /**
     * Set up the user id to use when there is no external user driving the work (for example when processing events
     * from another server).
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param serverUserId  String user is for the server.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    public VoidResponse setServerUserId(String serverName,
                                        String delegatingUserId,
                                        String serverUserId)
    {
        final String methodName = "setServerUserId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            this.setServerUserId(serverConfig, userId, serverName, serverUserId, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the user id to use when there is no external user driving the work (for example when processing events
     * from another server).
     *
     * @param serverConfig server configuration document to update
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverUserId  String user is for the server.
     * @param methodName calling method
     * @throws InvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    private void setServerUserId(OMAGServerConfig serverConfig,
                                 String           userId,
                                 String           serverName,
                                 String           serverUserId,
                                 String           methodName) throws InvalidParameterException
    {
        List<String>  configAuditTrail          = serverConfig.getAuditTrail();

        if (configAuditTrail == null)
        {
            configAuditTrail = new ArrayList<>();
        }

        if ((serverUserId != null) && (serverUserId.isBlank()))
        {
            serverUserId = null;
        }

        if (serverUserId == null)
        {
            configAuditTrail.add(new Date() + " " + userId + " removed configuration for local server's userId.");
        }
        else
        {
            configAuditTrail.add(new Date() + " " + userId + " updated configuration for local server's userId to " + serverUserId + ".");
        }

        serverConfig.setAuditTrail(configAuditTrail);
        serverConfig.setLocalServerUserId(serverUserId);

        configStore.saveServerConfig(serverName, methodName, serverConfig);
    }


    /**
     * Set an upper limit in the page size that can be requested on a REST call to the server.  The default
     * value is 1000.
     *
     * @param serverName - local server name.
     * @param delegatingUserId external userId making request
     * @param maxPageSize - max number of elements that can be returned on a request.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or maxPageSize parameter.
     */
    public VoidResponse setMaxPageSize(String  serverName,
                                       String  delegatingUserId,
                                       int     maxPageSize)
    {
        final String methodName = "setMaxPageSize";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            this.setMaxPageSize(serverConfig, userId, serverName, maxPageSize, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set an upper limit in the page size that can be requested on a REST call to the server.  The default
     * value is 1000.
     *
     * @param serverConfig server configuration document to update
     * @param userId - user that is issuing the request.
     * @param serverName - local server name.
     * @param maxPageSize - max number of elements that can be returned on a request.
     * @param methodName calling method
     * @throws InvalidParameterException invalid serverName or maxPageSize parameter.
     */
    private void setMaxPageSize(OMAGServerConfig serverConfig,
                                String           userId,
                                String           serverName,
                                int              maxPageSize,
                                String           methodName) throws InvalidParameterException
    {
        if (maxPageSize >= 0)
        {
            List<String>  configAuditTrail          = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date() + " " + userId + " updated configuration for maximum page size to " + maxPageSize + ".");

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setMaxPageSize(maxPageSize);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        else
        {
            throw new InvalidParameterException(OMAGAdminErrorCode.BAD_MAX_PAGE_SIZE.getMessageDefinition(serverName, Integer.toString(maxPageSize)),
                                                this.getClass().getName(),
                                                methodName,
                                                "maxPageSize");
        }
    }


    /**
     * Set an upper limit in the page size that can be requested on a REST call to the server.  The default
     * value is 1000.
     *
     * @param serverConfig server configuration document to update
     * @param userId - user that is issuing the request.
     * @param serverName - local server name.
     * @param secretsStoreProvider secrets store connector for bearer token
     * @param secretsStoreLocation secrets store location for bearer token
     * @param secretsStoreCollection secrets store collection for bearer token
     * @param methodName calling method
     * @throws InvalidParameterException invalid serverName or maxPageSize parameter.
     */
    private void setSecretStore(OMAGServerConfig serverConfig,
                                String           userId,
                                String           serverName,
                                String           secretsStoreProvider,
                                String           secretsStoreLocation,
                                String           secretsStoreCollection,
                                String           methodName) throws InvalidParameterException
    {
        if ((secretsStoreProvider != null) && (secretsStoreLocation != null) && (secretsStoreCollection != null))
        {
            List<String>  configAuditTrail          = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date() + " " + userId + " updated configuration for secrets store.");

            serverConfig.setAuditTrail(configAuditTrail);

            serverConfig.setSecretsStoreProvider(secretsStoreProvider);
            serverConfig.setSecretsStoreLocation(secretsStoreLocation);
            serverConfig.setSecretsStoreCollection(secretsStoreCollection);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        else
        {
            throw new InvalidParameterException(OMAGAdminErrorCode.BAD_CONNECTION.getMessageDefinition(serverName, secretsStoreProvider),
                                                this.getClass().getName(),
                                                methodName,
                                                "secrets store");
        }
    }


    /**
     * Set up the basic server properties in a single request.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param requestBody property details
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or maxPageSize parameter.
     */

    public VoidResponse setBasicServerProperties(String                      serverName,
                                                 String                      delegatingUserId,
                                                 ServerPropertiesRequestBody requestBody)
    {
        final String methodName = "setBasicServerProperties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validatePropertyNotNull(response, "serverProperties", serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            this.setOrganizationName(serverConfig, userId, serverName, requestBody.getOrganizationName(), methodName);

            serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            this.setServerDescription(serverConfig, userId, serverName, requestBody.getLocalServerDescription(), methodName);

            serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            this.setServerUserId(serverConfig, userId, serverName, requestBody.getLocalServerUserId(), methodName);

            serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            this.setServerRootURL(serverConfig, userId, serverName, requestBody.getLocalServerURL(), methodName);

            serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            this.setMaxPageSize(serverConfig, userId, serverName, requestBody.getMaxPageSize(), methodName);

            serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            this.setSecretStore(serverConfig, userId, serverName, requestBody.getSecretsStoreProvider(), requestBody.getSecretsStoreLocation(), requestBody.getSecretsStoreCollection(), methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the basic server properties in a single request.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @return properties response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or maxPageSize parameter.
     */

    public BasicServerPropertiesResponse getBasicServerProperties(String serverName,
                                                                  String delegatingUserId)
    {
        final String methodName = "setBasicServerProperties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BasicServerPropertiesResponse response = new BasicServerPropertiesResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, false, methodName);

            response.setBasicServerProperties(serverConfig);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the default event bus for embedding in event-driven connector.   The resulting connector will
     * be used in the OMRS Topic Connector for each cohort, the open metadata out topic and
     * the local repositories' event mapper.
     * When the event bus is configured, it is used only on future configuration.  It does not affect
     * existing configuration.
     *
     * @param serverName local server name.
     * @param delegatingUserId external userId making request
     * @param connectorProvider  connector provider for the event bus (if it is null then Kafka is assumed).
     * @param topicURLRoot the common root of the topics used by the open metadata server.
     * @param configurationProperties  property name/value pairs used to configure the connection to the event bus connector
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists or
     * InvalidParameterException invalid serverName parameter.
     */
    public VoidResponse setEventBus(String              serverName,
                                    String              delegatingUserId,
                                    String              connectorProvider,
                                    String              topicURLRoot,
                                    Map<String, Object> configurationProperties)
    {
        final String methodName = "setEventBus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            /*
             * Retrieve the existing configuration.
             */
            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            EventBusConfig   eventBusConfig = new EventBusConfig();

            if ((connectorProvider != null) && (connectorProvider.isEmpty()))
            {
                eventBusConfig.setConnectorProvider(null);
            }
            else
            {
                eventBusConfig.setConnectorProvider(connectorProvider);
            }

            if ((topicURLRoot != null) && (topicURLRoot.isEmpty()))
            {
                eventBusConfig.setTopicURLRoot(null);
            }
            else
            {
                eventBusConfig.setTopicURLRoot(topicURLRoot);
            }

            if ((configurationProperties == null) || (configurationProperties.isEmpty()))
            {
                eventBusConfig.setConfigurationProperties(null);
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

            configAuditTrail.add(new Date() + " " + userId + " updated configuration for default event bus.");

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setEventBusConfig(eventBusConfig);

            /*
             * Save the config away
             */
            configStore.saveServerConfig(serverName, methodName, serverConfig);

        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the current configuration for the event bus.
     *
     * @param serverName local server name.
     * @param delegatingUserId external userId making request
     * @return event bus config response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists or
     * InvalidParameterException invalid serverName parameter.
     */
    public EventBusConfigResponse getEventBus(String serverName,
                                              String delegatingUserId)
    {
        final String methodName = "getEventBus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        EventBusConfigResponse response = new EventBusConfigResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            /*
             * Retrieve the existing configuration.
             */
            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, false, methodName);

            if (configStore != null)
            {
                response.setConfig(serverConfig.getEventBusConfig());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Delete the current configuration for the event bus.
     *
     * @param serverName local server name.
     * @param delegatingUserId external userId making request
     * @return void  or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists or
     * InvalidParameterException invalid serverName parameter.
     */
    public VoidResponse deleteEventBus(String serverName,
                                       String delegatingUserId)
    {
        final String methodName = "deleteEventBus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            /*
             * Retrieve the existing configuration.
             */
            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            if (configStore != null)
            {
                List<String>  configAuditTrail          = serverConfig.getAuditTrail();

                if (configAuditTrail == null)
                {
                    configAuditTrail = new ArrayList<>();
                }

                configAuditTrail.add(new Date() + " " + userId + " deleted configuration for default event bus.");

                serverConfig.setAuditTrail(configAuditTrail);
                serverConfig.setEventBusConfig(null);

                /*
                 * Save the config away
                 */
                configStore.saveServerConfig(serverName, methodName, serverConfig);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the root URL for this server that is used to construct full URL paths to calls for
     * this server's REST interfaces that is used by other members of the cohorts that this server
     * connects to.
     * The default value is "localhost:9443".
     * ServerURLRoot is used during the configuration of the local repository.  If called
     * after the local repository is configured, it has no effect.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param requestBody  String url.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or serverURLRoot parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    public VoidResponse setServerRootURL(String         serverName,
                                         String         delegatingUserId,
                                         URLRequestBody requestBody)
    {
        final String methodName = "setServerURLRoot";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            if (requestBody != null)
            {
                this.setServerRootURL(serverConfig, userId, serverName, requestBody.getUrlRoot(), methodName);
            }
            else
            {
                this.setServerRootURL(serverConfig, userId, serverName, null, methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the root URL for this server that is used to construct full URL paths to calls for
     * this server's REST interfaces that is used by other members of the cohorts that this server
     * connects to.
     * The default value is "localhost:9443".
     * ServerURLRoot is used during the configuration of the local repository.  If called
     * after the local repository is configured, it has no effect.
     *
     * @param serverConfig server configuration document to update
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param urlRoot  String url.
     * @param methodName calling method
     * @throws InvalidParameterException invalid serverName or serverURLRoot parameter or
     */
    private void setServerRootURL(OMAGServerConfig serverConfig,
                                  String           userId,
                                  String           serverName,
                                  String           urlRoot,
                                  String           methodName) throws  InvalidParameterException
    {
        List<String>  configAuditTrail          = serverConfig.getAuditTrail();

        if (configAuditTrail == null)
        {
            configAuditTrail = new ArrayList<>();
        }

        if (urlRoot == null)
        {
            configAuditTrail.add(new Date() + " " + userId + " removed configuration for local server's URL root.");
        }
        else
        {
            configAuditTrail.add(new Date() + " " + userId + " updated configuration for local server's URL root to " + urlRoot + ".");
        }

        serverConfig.setAuditTrail(configAuditTrail);
        serverConfig.setLocalServerURL(urlRoot);

        configStore.saveServerConfig(serverName, methodName, serverConfig);
    }


    /**
     * Set up the default audit log for the server.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param requestBody null request body
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or null userId parameter.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse setDefaultAuditLog(String          serverName,
                                           String          delegatingUserId,
                                           NullRequestBody requestBody)
    {
        final String methodName = "setDefaultAuditLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig     = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);
            List<String>     configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date() + " " + userId + " set up default audit log destinations.");


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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the console (stdout) audit log destination for the server.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or null userId parameter.
     */
    public VoidResponse addConsoleAuditLogDestination(String       serverName,
                                                      String       delegatingUserId,
                                                      List<String> supportedSeverities)
    {
        final String methodName = "addConsoleAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            ConnectorConfigurationFactory configurationFactory = new ConnectorConfigurationFactory();

            String qualifier;

            if ((supportedSeverities == null) || (supportedSeverities.isEmpty()))
            {
                qualifier = "- no output";
            }
            else
            {
                qualifier = "- " + supportedSeverities;
            }

            this.addAuditLogDestination(serverName, delegatingUserId, configurationFactory.getConsoleAuditLogConnection(qualifier, supportedSeverities));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the SLF4J audit log destination for the server.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or null userId parameter.
     */
    public VoidResponse addSLF4JAuditLogDestination(String       serverName,
                                                    String       delegatingUserId,
                                                    List<String> supportedSeverities)
    {
        final String methodName = "addSLF4JAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            ConnectorConfigurationFactory configurationFactory = new ConnectorConfigurationFactory();

            String qualifier;

            if ((supportedSeverities == null) || (supportedSeverities.isEmpty()))
            {
                qualifier = "- no output";
            }
            else
            {
                qualifier = "- " + supportedSeverities;
            }

            this.addAuditLogDestination(serverName, delegatingUserId, configurationFactory.getSLF4JAuditLogConnection(qualifier, supportedSeverities));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the File based audit log destination for the server.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param directoryName name of directory
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or null userId parameter.
     */
    public VoidResponse addFileAuditLogDestination(String       serverName,
                                                   String       delegatingUserId,
                                                   String       directoryName,
                                                   List<String> supportedSeverities)
    {
        final String methodName = "addFileAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            ConnectorConfigurationFactory configurationFactory = new ConnectorConfigurationFactory();

            this.addAuditLogDestination(serverName, delegatingUserId, configurationFactory.getFileBasedAuditLogConnection(serverName, directoryName, supportedSeverities));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the JDBC based audit log destination for the server.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param storageProperties  properties used to configure access to the database
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or null userId parameter.
     */
    public VoidResponse addPostgreSQLAuditLogDestination(String              serverName,
                                                         String              delegatingUserId,
                                                         Map<String, Object> storageProperties)
    {
        final String methodName = "addPostgreSQLAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            ConnectorConfigurationFactory configurationFactory = new ConnectorConfigurationFactory();

            this.addAuditLogDestination(serverName, delegatingUserId, configurationFactory.getPostgreSQLBasedAuditLogConnection(storageProperties));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the File based audit log destination for the server.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param topicName name of topic
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or null userId parameter.
     */
    public VoidResponse addEventTopicAuditLogDestination(String       serverName,
                                                         String       delegatingUserId,
                                                         String       topicName,
                                                         List<String> supportedSeverities)
    {
        final String methodName = "addEventTopicAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            EventBusConfig   eventBusConfig  = errorHandler.validateEventBusIsSet(serverName, serverConfig, methodName);

            ConnectorConfigurationFactory configurationFactory = new ConnectorConfigurationFactory();

            this.addAuditLogDestination(serverName,
                                        delegatingUserId,
                                        configurationFactory.getEventTopicAuditLogConnection(serverName,
                                                                                             supportedSeverities,
                                                                                             eventBusConfig.getConnectorProvider(),
                                                                                             eventBusConfig.getTopicURLRoot(),
                                                                                             topicName,
                                                                                             serverConfig.getLocalServerId(),
                                                                                             eventBusConfig.getConfigurationProperties()));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Add a new open metadata archive to load at startup.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param fileName name of the open metadata archive file.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or fileName parameter.
     */
    public VoidResponse addStartUpOpenMetadataArchiveFile(String serverName,
                                                          String delegatingUserId,
                                                          String fileName)
    {
        final String methodName = "addStartUpOpenMetadataArchiveFile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateFileName(fileName, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            ConnectorConfigurationFactory configurationFactory   = new ConnectorConfigurationFactory();
            Connection                    newOpenMetadataArchive = configurationFactory.getOpenMetadataArchiveFileConnection(fileName);

            List<Connection>              openMetadataArchiveConnections = null;

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);
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

            this.setOpenMetadataArchives(serverName, delegatingUserId, openMetadataArchiveConnections);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up an in memory local repository.  This repository uses hashmaps to store content.  It is useful
     * for demos, testing and POCs.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param requestBody null request body
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse setInMemLocalRepository(String          serverName,
                                                String          delegatingUserId,
                                                NullRequestBody requestBody)
    {
        final String methodName = "setInMemLocalRepository";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            OMRSConfigurationFactory configurationFactory     = new OMRSConfigurationFactory();

            this.setLocalRepositoryConfig(serverName,
                                          delegatingUserId,
                                          configurationFactory.getInMemoryLocalRepositoryConfig(serverConfig.getLocalServerName(),
                                                                                                serverConfig.getLocalServerURL()));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up a PostgreSQL Database Schema as the local repository.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param storageProperties  properties used to configure Egeria Graph DB
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public VoidResponse setPostgresLocalRepository(String              serverName,
                                                   String              delegatingUserId,
                                                   Map<String, Object> storageProperties)
    {
        final String methodName = "setPostgresLocalRepository";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();

            this.setLocalRepositoryConfig(serverName,
                                          delegatingUserId,
                                          configurationFactory.getPostgresLocalRepositoryConfig(serverConfig.getLocalServerName(),
                                                                                                serverConfig.getLocalServerURL(),
                                                                                                storageProperties));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up a read only store as the local repository.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public VoidResponse setReadOnlyLocalRepository(String serverName,
                                                   String delegatingUserId)
    {
        final String methodName = "setReadOnlyLocalRepository";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            OMRSConfigurationFactory configurationFactory     = new OMRSConfigurationFactory();


            this.setLocalRepositoryConfig(serverName,
                                          delegatingUserId,
                                          configurationFactory.getReadOnlyLocalRepositoryConfig(serverConfig.getLocalServerName(),
                                                                                                serverConfig.getLocalServerURL()));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove all configuration for a local repository.  This is the default setting.  This call
     * can be used to remove subsequent local repository configuration.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public VoidResponse setNoRepositoryMode(String serverName,
                                            String delegatingUserId)
    {
        return clearLocalRepositoryConfig(serverName, delegatingUserId);
    }


    /**
     * Provide the connection to the local repository - used when the local repository mode is set to plugin repository.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param connection  connection to the OMRS repository connector.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     */
    public VoidResponse setPluginRepositoryConnection(String     serverName,
                                                      String     delegatingUserId,
                                                      Connection connection)
    {
        final String methodName = "setPluginRepositoryConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateServerConnection(connection, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();
            LocalRepositoryConfig localRepositoryConfig
                    = configurationFactory.getPluginRepositoryLocalRepositoryConfig(serverConfig.getLocalServerName(),
                                                                                    serverConfig.getLocalServerURL());

            /*
             * Set up the repository connection in the local repository config and clear any event mapper
             */
            localRepositoryConfig.setLocalRepositoryLocalConnection(connection);
            localRepositoryConfig.setEventMapperConnection(null);

            this.setLocalRepositoryConfig(serverName, delegatingUserId, localRepositoryConfig);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Provide the connection to the local repository.
     * This is used when the local repository mode is set to plugin repository.
     *
     * @param serverName                local server name.
     * @param delegatingUserId external userId making request
     * @param connectorProvider         connector provider class name to the OMRS repository connector.
     * @param additionalProperties      additional parameters to pass to the repository connector
     * @return void response or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set.
     */
    public VoidResponse setPluginRepositoryConnection(String               serverName,
                                                      String               delegatingUserId,
                                                      String               connectorProvider,
                                                      Map<String, Object>  additionalProperties)
    {
        final String methodName  = "setPluginRepositoryConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            this.setPluginRepositoryConnection(serverName,
                                               delegatingUserId,
                                               connectorConfigurationFactory.getRepositoryConnection(connectorProvider,
                                                                                                     serverConfig.getLocalServerURL(),
                                                                                                     additionalProperties));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Provide the connection to the local repository - used when the local repository mode is set to repository proxy.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param connection  connection to the OMRS repository connector.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     */
    public VoidResponse setRepositoryProxyConnection(String     serverName,
                                                     String     delegatingUserId,
                                                     Connection connection)
    {
        final String methodName = "setRepositoryProxyConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateServerConnection(connection, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();
            LocalRepositoryConfig localRepositoryConfig
                    = configurationFactory.getRepositoryProxyLocalRepositoryConfig(serverConfig.getLocalServerName(),
                                                                                   serverConfig.getLocalServerURL());

            /*
             * Set up the repository proxy connection in the local repository config
             */
            localRepositoryConfig.setLocalRepositoryLocalConnection(connection);

            this.setLocalRepositoryConfig(serverName, delegatingUserId, localRepositoryConfig);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Provide the connection to the local repository.
     * This is used when the local repository mode is set to repository proxy.
     *
     * @param serverName                local server name.
     * @param delegatingUserId external userId making request
     * @param connectorProvider         connector provider class name to the OMRS repository connector.
     * @param additionalProperties      additional parameters to pass to the repository connector
     * @return void response or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set.
     */
    public VoidResponse setRepositoryProxyConnection(String               serverName,
                                                     String               delegatingUserId,
                                                     String               connectorProvider,
                                                     Map<String, Object>  additionalProperties)
    {
        final String methodName  = "setRepositoryProxyConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            this.setRepositoryProxyConnection(serverName,
                                              delegatingUserId,
                                              connectorConfigurationFactory.getRepositoryConnection(connectorProvider,
                                                                                                    serverConfig.getLocalServerURL(),
                                                                                                    additionalProperties));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Provide the connection to the local repository's event mapper if needed.  The default value is null which
     * means no event mapper.  An event mapper is needed if the local repository has additional APIs that can change
     * the metadata in the repository without going through the open metadata and governance services.
     *
     * @param serverName local server name.
     * @param delegatingUserId external userId making request
     * @param connection connection to the OMRS repository event mapper.
     * @return void response
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryEventMapper parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set
     */
    public VoidResponse setRepositoryProxyEventMapper(String     serverName,
                                                      String     delegatingUserId,
                                                      Connection connection)
    {
        final String methodName = "setRepositoryProxyEventMapper";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateServerConnection(connection, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

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

            this.setLocalRepositoryConfig(serverName, delegatingUserId, localRepositoryConfig);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Provide the connection to the local repository's event mapper if needed.  The default value is null which
     * means no event mapper.  An event mapper is needed if the local repository has additional APIs that can change
     * the metadata in the repository without going through the open metadata and governance services.
     *
     * @param serverName                  local server name.
     * @param delegatingUserId external userId making request
     * @param connectorProvider           Java class name of the connector provider for the OMRS repository event mapper.
     * @param eventSource                 topic name or URL to the native event source.
     * @param additionalProperties        additional properties for the event mapper connection
     * @return void response or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryEventMapper parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    public VoidResponse setRepositoryProxyEventMapper(String              serverName,
                                                      String              delegatingUserId,
                                                      String              connectorProvider,
                                                      String              eventSource,
                                                      Map<String, Object> additionalProperties)
    {
        final String methodName = "setRepositoryProxyEventMapper";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            this.setRepositoryProxyEventMapper(serverName,
                                               delegatingUserId,
                                               connectorConfigurationFactory.getRepositoryEventMapperConnection(connectorProvider,
                                                                                                                additionalProperties,
                                                                                                                eventSource));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the local metadata collection id.  If the local repository is not configured
     * then the invalid parameter exception is returned.
     *
     * @param serverName                  local server name.
     * @param delegatingUserId external userId making request
     * @return guid response or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    public GUIDResponse getLocalMetadataCollectionId(String serverName,
                                                     String delegatingUserId)
    {
        final String methodName = "getLocalMetadataCollectionId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, false, methodName);

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
                throw new InvalidParameterException(OMAGAdminErrorCode.LOCAL_REPOSITORY_MODE_NOT_SET.getMessageDefinition(serverName),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    "localRepositoryConfig");
            }

            /*
             * Set up the metadata collection name in the local repository config and save.
             */
            response.setGUID(localRepositoryConfig.getMetadataCollectionId());

            return response;

        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Return the local metadata collection id.  If the local repository is not configured
     * then the invalid parameter exception is returned.
     *
     * @param serverName                  local server name.
     * @param delegatingUserId external userId making request
     * @param metadataCollectionId        new value for the metadata collection id
     * @return guid response or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    public VoidResponse setLocalMetadataCollectionId(String serverName,
                                                     String delegatingUserId,
                                                     String metadataCollectionId)
    {
        final String methodName = "setLocalMetadataCollectionId";
        final String parameterNameName = "setLocalMetadataCollectionId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validatePropertyNotNull(metadataCollectionId, parameterNameName, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

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
                throw new InvalidParameterException(OMAGAdminErrorCode.LOCAL_REPOSITORY_MODE_NOT_SET.getMessageDefinition(serverName),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    "localRepositoryConfig");
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }




    /**
     * Return the local metadata collection id.  If the local repository is not configured
     * then the invalid parameter exception is returned.
     *
     * @param serverName                  local server name.
     * @param delegatingUserId external userId making request
     * @return guid response or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    public StringResponse getLocalMetadataCollectionName(String serverName,
                                                         String delegatingUserId)
    {
        final String methodName = "getLocalMetadataCollectionName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        StringResponse response = new StringResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, false, methodName);

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
                throw new InvalidParameterException(OMAGAdminErrorCode.LOCAL_REPOSITORY_MODE_NOT_SET.getMessageDefinition(serverName),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    "localRepositoryConfig");
            }

            /*
             * Set up the metadata collection name in the local repository config and save.
             */
            response.setResultString(localRepositoryConfig.getMetadataCollectionName());

            return response;

        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Set up the local metadata collection name.  If this is not set then the default value is the
     * local server name.
     *
     * @param serverName                  local server name.
     * @param delegatingUserId external userId making request
     * @param localMetadataCollectionName metadata collection name
     * @return void response or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    public VoidResponse setLocalMetadataCollectionName(String serverName,
                                                       String delegatingUserId,
                                                       String localMetadataCollectionName)
    {
        final String methodName = "setLocalMetadataCollectionName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateMetadataCollectionName(localMetadataCollectionName, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

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

            this.setLocalRepositoryConfig(serverName, delegatingUserId, localRepositoryConfig);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
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
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @param cohortName  name of the cohort
     * @param configurationProperties additional properties for the cohort
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName, cohortName or serviceMode parameter.
     * OMAGConfigurationErrorException the event bus is not set.
     */
    public VoidResponse addCohortRegistration(String               serverName,
                                              String               delegatingUserId,
                                              String               cohortName,
                                              Map<String, Object>  configurationProperties)
    {
        final String methodName = "addCohortRegistration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            EventBusConfig   eventBusConfig  = errorHandler.validateEventBusIsSet(serverName, serverConfig, methodName);

            /*
             * Set up a new cohort
             */
            OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();

            CohortConfig newCohortConfig = configurationFactory.getDefaultCohortConfig(serverConfig.getLocalServerName(),
                                                                                       cohortName,
                                                                                       configurationProperties,
                                                                                       eventBusConfig.getConnectorProvider(),
                                                                                       eventBusConfig.getTopicURLRoot(),
                                                                                       serverConfig.getLocalServerId(),
                                                                                       eventBusConfig.getConfigurationProperties());


            this.setCohortConfig(serverName, delegatingUserId, cohortName, newCohortConfig);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the current topic names for the three dedicated topics of the cohort.  This call can only be made once the cohort
     * is set up with addCohortRegistration().
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param cohortName  name of the cohort.
     * @return list of topics response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or cohortName parameter.
     * OMAGConfigurationErrorException the cohort is not setup, or set up in a non-standard way.
     */
    public DedicatedTopicListResponse getDedicatedCohortTopicNames(String serverName,
                                                                   String delegatingUserId,
                                                                   String cohortName)
    {
        final String methodName = "getDedicatedCohortTopicNames";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        DedicatedTopicListResponse response = new DedicatedTopicListResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, false, methodName);

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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
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
        if (eventTopicConnection instanceof VirtualConnection virtualConnection)
        {
            List<EmbeddedConnection> embeddedConnections = virtualConnection.getEmbeddedConnections();

            if ((embeddedConnections != null) && (embeddedConnections.size() == 1))
            {
                Connection connection = embeddedConnections.get(0).getEmbeddedConnection();

                if (connection != null)
                {
                    Endpoint endpoint = connection.getEndpoint();

                    if (endpoint != null)
                    {
                        return endpoint.getNetworkAddress();
                    }
                }
            }
        }

       return null;
    }



    /**
     * Change the topic name that is used by this server to register members in the
     * open metadata repository cohort.  Note this name needs to be configured to same
     * in all members of a cohort.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param cohortName  name of the cohort.
     * @param topicName new topic name
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or cohortName parameter.
     * OMAGConfigurationErrorException the cohort is not setup, or set up in a non-standard way.
     */
    public VoidResponse overrideRegistrationCohortTopicName(String serverName,
                                                            String delegatingUserId,
                                                            String cohortName,
                                                            String topicName)
    {
        final String methodName = "overrideRegistrationCohortTopicName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            if (serverConfig != null)
            {
                CohortConfig currentCohortDetails = errorHandler.validateCohortIsSet(serverName, serverConfig, cohortName, methodName);

                if (currentCohortDetails != null)
                {
                    Connection eventTopicConnection = overrideCohortTopicName(currentCohortDetails.getCohortOMRSRegistrationTopicConnection(), topicName);

                    if (eventTopicConnection != null)
                    {
                        currentCohortDetails.setCohortOMRSRegistrationTopicConnection(eventTopicConnection);

                        this.setCohortConfig(serverName, delegatingUserId, cohortName, currentCohortDetails);
                    }
                    else
                    {
                        errorHandler.logNoCohortTopicChange(cohortName, serverName, methodName);
                    }

                }
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Change the topic name that is used by this server to verify type consistency with the other members of the
     * open metadata repository cohort.  Note this name needs to be configured to same
     * in all members of a cohort.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param cohortName  name of the cohort.
     * @param topicName new topic name
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or cohortName parameter.
     * OMAGConfigurationErrorException the cohort is not setup, or set up in a non-standard way.
     */
    public VoidResponse overrideTypesCohortTopicName(String serverName,
                                                     String delegatingUserId,
                                                     String cohortName,
                                                     String topicName)
    {
        final String methodName = "overrideTypesCohortTopicName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            if (serverConfig != null)
            {
                CohortConfig currentCohortDetails = errorHandler.validateCohortIsSet(serverName, serverConfig, cohortName, methodName);

                if (currentCohortDetails != null)
                {
                    Connection eventTopicConnection = overrideCohortTopicName(currentCohortDetails.getCohortOMRSTypesTopicConnection(), topicName);

                    if (eventTopicConnection != null)
                    {
                        currentCohortDetails.setCohortOMRSTypesTopicConnection(eventTopicConnection);

                        this.setCohortConfig(serverName, delegatingUserId, cohortName, currentCohortDetails);
                    }
                    else
                    {
                        errorHandler.logNoCohortTopicChange(cohortName, serverName, methodName);
                    }

                }
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Change the topic name that is used by this server to exchange metadata instances with the other members of the
     * open metadata repository cohort.  Note this name needs to be configured to same
     * in all members of a cohort.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param cohortName  name of the cohort.
     * @param topicName new topic name
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or cohortName parameter.
     * OMAGConfigurationErrorException the cohort is not setup, or set up in a non-standard way.
     */
    public VoidResponse overrideInstancesCohortTopicName(String serverName,
                                                         String delegatingUserId,
                                                         String cohortName,
                                                         String topicName)
    {
        final String methodName = "overrideInstancesCohortTopicName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            if (serverConfig != null)
            {
                CohortConfig currentCohortDetails = errorHandler.validateCohortIsSet(serverName, serverConfig, cohortName, methodName);

                if (currentCohortDetails != null)
                {
                    Connection eventTopicConnection = overrideCohortTopicName(currentCohortDetails.getCohortOMRSInstancesTopicConnection(), topicName);

                    if (eventTopicConnection != null)
                    {
                        currentCohortDetails.setCohortOMRSInstancesTopicConnection(eventTopicConnection);

                        this.setCohortConfig(serverName, delegatingUserId, cohortName, currentCohortDetails);
                    }
                    else
                    {
                        errorHandler.logNoCohortTopicChange(cohortName, serverName, methodName);
                    }
                }
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
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
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or cohortName parameter.
     * OMAGConfigurationErrorException the cohort is not setup, or set up in a non-standard way.
     */
    private Connection overrideCohortTopicName(Connection eventTopicConnection,
                                               String     topicName)
    {
        if (eventTopicConnection instanceof VirtualConnection virtualConnection)
        {
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
                        endpoint.setNetworkAddress(topicName);
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
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param auditLogDestination connection object for audit log destination
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName.
     */
    public VoidResponse addAuditLogDestination(String     serverName,
                                               String     delegatingUserId,
                                               Connection auditLogDestination)
    {
        final String methodName = "addAuditLogDestination";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateServerConnection(auditLogDestination, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (auditLogDestination != null)
            {
                OMAGServerConfig serverConfig     = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);
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
                    configAuditTrail.add(new Date() + " " + userId + " created first audit log destination.");

                }
                else
                {
                    configAuditTrail.add(new Date() + " " + userId + " added to list of audit log destinations.");
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update an audit log destination connection that is identified with the supplied destination connection name with
     * the supplied connection object.
     * It is possible to supply a suppliedConnectionName that matches an existing connection and the new connection specifies a different qualifiedName.
     * In this way it is possible to rename Connections.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param suppliedConnectionName name of the audit log destination to be updated
     * @param suppliedConnection connection object that defines the audit log destination
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or suppliedConnectionName parameter.
     */
    public VoidResponse updateAuditLogDestination(String     serverName,
                                                  String     delegatingUserId,
                                                  String     suppliedConnectionName,
                                                  Connection suppliedConnection)
    {
        final String methodName = "updateAuditLogDestination";
        final String functionName = "update";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateServerConnection(suppliedConnection, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (suppliedConnection != null)
            {
                OMAGServerConfig serverConfig     = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);
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
                    throw new InvalidParameterException(OMAGAdminErrorCode.AUDIT_LOG_DESTINATION_NOT_FOUND.getMessageDefinition(suppliedConnectionName,
                                                                                                                                functionName),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        "auditLogDestinations");

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
                                configAuditTrail.add(new Date() + " " + userId + " updated " + existingConnection.getQualifiedName() + " in the list of audit log destinations.");
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
                                    configAuditTrail.add(new Date() + " " + userId + " updated " + existingConnection.getQualifiedName() + " in the list of audit log destinations.");
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
                        throw new InvalidParameterException(OMAGAdminErrorCode.AUDIT_LOG_DESTINATION_NOT_FOUND.getMessageDefinition(suppliedConnectionName,
                                                                                                                                    functionName),
                                                            this.getClass().getName(),
                                                            methodName,
                                                            "auditLogDestination");
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Delete an audit log destination connection, that is identified with the supplied destination connection name.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param suppliedConnectionName qualified name of the audit log destination to be deleted
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or suppliedConnectionName parameter.
     */
    public VoidResponse clearAuditLogDestination(String serverName,
                                                 String delegatingUserId,
                                                 String suppliedConnectionName)
    {
        final String methodName   = "clearAuditLogDestination";
        final String functionName = "delete";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig     = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);
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
                throw new InvalidParameterException(OMAGAdminErrorCode.AUDIT_LOG_DESTINATION_NOT_FOUND.getMessageDefinition(suppliedConnectionName,
                                                                                                                            functionName),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    "auditLogDestinations");
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
                            configAuditTrail.add(new Date() + " " + userId + " removed " + existingConnection.getQualifiedName() + " from the list of audit log destinations.");
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
                                configAuditTrail.add(new Date() + " " + userId + " removed " + existingConnection.getQualifiedName() + " from the list of audit log destinations.");
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
                     * Error did not find an audit log to remove
                     */
                    throw new InvalidParameterException(OMAGAdminErrorCode.AUDIT_LOG_DESTINATION_NOT_FOUND.getMessageDefinition(suppliedConnectionName,
                                                                                                                                functionName),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        "auditLogDestination");
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the list of audit log destinations.  These destinations are expressed as Connection objects
     * to the connectors that will handle the audit log records.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param auditLogDestinations list of connection objects
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName.
     */
    public VoidResponse setAuditLogDestinations(String           serverName,
                                                String           delegatingUserId,
                                                List<Connection> auditLogDestinations)
    {
        final String methodName = "setAuditLogDestinations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (auditLogDestinations != null)
            {
                for (Connection connection : auditLogDestinations)
                {
                    errorHandler.validateServerConnection(connection, serverName, methodName);
                }
            }

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);
            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if (auditLogDestinations == null)
            {
                configAuditTrail.add(new Date() + " " + userId + " setting up no audit log destinations.");
            }
            else
            {
                configAuditTrail.add(new Date() + " " + userId + " updated list of audit log destinations.");
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of audit log destinations.  These destinations are expressed as Connection objects
     * to the connectors that will handle the audit log records.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @return connection list response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName.
     */
    public ConnectionListResponse getAuditLogDestinations(String serverName,
                                                          String delegatingUserId)
    {
        final String methodName = "getAuditLogDestinations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ConnectionListResponse response = new ConnectionListResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, false, methodName);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            if (repositoryServicesConfig != null)
            {
                response.setConnections(repositoryServicesConfig.getAuditLogConnections());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Clear the list of audit log destinations.  These destinations are expressed as Connection objects
     * to the connectors that will handle the audit log records.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName.
     */
    public VoidResponse clearAuditLogDestinations(String serverName,
                                                  String delegatingUserId)
    {
        return this.setAuditLogDestinations(serverName, delegatingUserId, null);
    }


    /**
     * Set up the list of open metadata archives.  These are open metadata types and instances that are loaded at
     * repository start up.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param openMetadataArchives list of connection objects
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName.
     */
    public VoidResponse setOpenMetadataArchives(String           serverName,
                                                String           delegatingUserId,
                                                List<Connection> openMetadataArchives)
    {
        final String methodName = "setOpenMetadataArchives";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);
            List<String>  configAuditTrail  = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if (openMetadataArchives == null)
            {
                configAuditTrail.add(new Date() + " " + userId + " clearing open metadata archives.");
            }
            else
            {
                configAuditTrail.add(new Date() + " " + userId + " updated list of open metadata archives loaded at server start up.");
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of open metadata archives.  These are open metadata types and instances that are loaded at
     * repository start up.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @return connection list response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName.
     */
    public ConnectionListResponse getOpenMetadataArchives(String serverName,
                                                          String delegatingUserId)
    {
        final String methodName = "getOpenMetadataArchives";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ConnectionListResponse response = new ConnectionListResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, false, methodName);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            if (repositoryServicesConfig != null)
            {
                response.setConnections(repositoryServicesConfig.getOpenMetadataArchiveConnections());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Clear the list of open metadata archives for loading at server startup.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName.
     */
    public VoidResponse clearOpenMetadataArchives(String serverName,
                                                  String delegatingUserId)
    {
        return this.setOpenMetadataArchives(serverName, delegatingUserId, null);
    }


    /**
     * Set up the configuration for the local repository.  This overrides the current values.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param localRepositoryConfig  configuration properties for the local repository.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryConfig parameter.
     */
    public VoidResponse setLocalRepositoryConfig(String                serverName,
                                                 String                delegatingUserId,
                                                 LocalRepositoryConfig localRepositoryConfig)
    {
        final String methodName = "setLocalRepositoryConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig     = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);
            List<String>     configAuditTrail = serverConfig.getAuditTrail();

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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the configuration for the local repository.  This overrides the current values.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @return local repository config response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName.
     */
    public LocalRepositoryConfigResponse getLocalRepositoryConfig(String serverName,
                                                                  String delegatingUserId)
    {
        final String methodName = "setLocalRepositoryConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        LocalRepositoryConfigResponse response = new LocalRepositoryConfigResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, false, methodName);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            if (repositoryServicesConfig != null)
            {
                response.setConfig(repositoryServicesConfig.getLocalRepositoryConfig());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove all configuration for a local repository.  The default is no local repository.  This call
     * can be used to remove subsequent local repository configuration.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public VoidResponse clearLocalRepositoryConfig(String serverName,
                                                   String delegatingUserId)
    {
        final String methodName = "clearLocalRepositoryConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            this.setLocalRepositoryConfig(serverName, delegatingUserId, null);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Update the URL broadcast across the cohort to allow other members to issue queries to this repository.
     * This method is needed to reconfigure a server that has moved from one platform to another.  Once the
     * URL is updated, and the server restarted, it will broadcast its new URL to the rest of the cohort.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param requestBody  String url.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or serverURLRoot parameter or
     * OMAGConfigurationErrorException unusual state in the admin server.
     */
    public VoidResponse resetRemoteCohortURL(String         serverName,
                                             String         delegatingUserId,
                                             URLRequestBody requestBody)
    {
        final String methodName = "resetRemoteCohortURL";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig      = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);
            List<String>     configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            String urlRoot = null;

            if (requestBody != null)
            {
                urlRoot = requestBody.getUrlRoot();
            }

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            if (repositoryServicesConfig != null)
            {
                LocalRepositoryConfig localRepositoryConfig = repositoryServicesConfig.getLocalRepositoryConfig();

                if (localRepositoryConfig != null)
                {
                    Connection localRepositoryRemoteConnection = localRepositoryConfig.getLocalRepositoryRemoteConnection();

                    if (localRepositoryRemoteConnection != null)
                    {
                        if (urlRoot == null)
                        {
                            configAuditTrail.add(
                                    new Date() + " " + userId + " removed configuration for local server's remote repository connector's endpoint address.");
                        }
                        else
                        {
                            configAuditTrail.add(
                                    new Date() + " " + userId + " updated configuration for local server's remote repository connector's endpoint address to " + urlRoot + ".");
                        }

                        Endpoint endpoint = localRepositoryRemoteConnection.getEndpoint();

                        if (endpoint != null)
                        {
                            endpoint.setNetworkAddress(urlRoot);
                            localRepositoryRemoteConnection.setEndpoint(endpoint);
                            localRepositoryConfig.setLocalRepositoryLocalConnection(localRepositoryRemoteConnection);
                            repositoryServicesConfig.setLocalRepositoryConfig(localRepositoryConfig);
                            serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);
                            serverConfig.setAuditTrail(configAuditTrail);

                            configStore.saveServerConfig(serverName, methodName, serverConfig);
                        }
                    }
                }
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the configuration properties for a cohort.  This may reconfigure an existing cohort or create a
     * cohort.  Use setCohortMode to delete a cohort.
     *
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @param cohortName  name of the cohort
     * @param cohortConfig  configuration for the cohort
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName, cohortName or cohortConfig parameter.
     */
    public VoidResponse setCohortConfig(String       serverName,
                                        String       delegatingUserId,
                                        String       cohortName,
                                        CohortConfig cohortConfig)
    {
        final String methodName = "setCohortConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);
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
                configAuditTrail.add(new Date() + " " + userId + " removed configuration for cohort " + cohortName + ".");
            }
            else
            {
                configAuditTrail.add(new Date() + " " + userId + " updated configuration for cohort " + cohortName + ".");
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the current configuration for a cohort.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param cohortName  name of the cohort.
     * @return cohort config response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName, cohortName or serviceMode parameter.
     * OMAGConfigurationErrorException the event bus is not set.
     */
    public CohortConfigResponse getCohortConfig(String serverName,
                                                String delegatingUserId,
                                                String cohortName)
    {
        final String methodName = "getCohortConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CohortConfigResponse response = new CohortConfigResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, false, methodName);

            if (serverConfig != null)
            {
                response.setConfig(errorHandler.validateCohortIsSet(serverName, serverConfig, cohortName, methodName));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
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
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param cohortName  name of the cohort.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName, cohortName or serviceMode parameter.
     */
    public VoidResponse clearCohortConfig(String serverName,
                                          String delegatingUserId,
                                          String cohortName)
    {
        final String methodName = "clearCohortRegistration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateCohortName(cohortName, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            this.setCohortConfig(serverName, delegatingUserId, cohortName, null);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the configuration properties for an OMAG Server in a single command.
     *
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @param omagServerConfig  configuration for the server
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or OMAGServerConfig parameter.
     */
    public VoidResponse setOMAGServerConfig(String           serverName,
                                            String           delegatingUserId,
                                            OMAGServerConfig omagServerConfig)
    {
        final String methodName = "setOMAGServerConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (omagServerConfig == null)
            {
               throw new InvalidParameterException(OMAGAdminErrorCode.NULL_SERVER_CONFIG.getMessageDefinition(serverName),
                                                   this.getClass().getName(),
                                                   methodName,
                                                   OMAGServerConfig.class.getName());
            }

            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);
            if (delegatingUserId != null)
            {
                OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(delegatingUserId);
            }

            List<String>  configAuditTrail = omagServerConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date() + " " + userId + " deployed configuration for server.");

            omagServerConfig.setAuditTrail(configAuditTrail);

            configStore.saveServerConfig(serverName, methodName, omagServerConfig);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Push the configuration for the server to another OMAG Server Platform.
     *
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @param destinationPlatform  location of the platform where the config is to be deployed to
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration or
     * InvalidParameterException invalid serverName or destinationPlatform parameter.
     */
    public VoidResponse deployOMAGServerConfig(String         serverName,
                                               String         delegatingUserId,
                                               URLRequestBody destinationPlatform)
    {
        final String methodName = "deployOMAGServerConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, false, methodName);

            String  serverURLRoot = serverConfig.getLocalServerURL();
            String secretsStoreProvider = serverConfig.getSecretsStoreProvider();
            String secretsStoreLocation = serverConfig.getSecretsStoreLocation();
            String secretsStoreCollection = serverConfig.getSecretsStoreCollection();

            if (destinationPlatform != null)
            {
                if (destinationPlatform.getUrlRoot() != null)
                {
                    serverURLRoot = destinationPlatform.getUrlRoot();
                }

                if (destinationPlatform.getSecretsStoreProvider() != null)
                {
                    secretsStoreProvider = destinationPlatform.getSecretsStoreProvider();
                }

                if (destinationPlatform.getSecretsStoreLocation() != null)
                {
                    secretsStoreLocation = destinationPlatform.getSecretsStoreLocation();
                }

                if (destinationPlatform.getSecretsStoreCollection() != null)
                {
                    secretsStoreCollection = destinationPlatform.getSecretsStoreCollection();
                }
            }

            ConfigurationManagementClient client = new ConfigurationManagementClient(serverURLRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, delegatingUserId, null);

            client.setOMAGServerConfig(serverConfig);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the complete set of configuration properties in use by the server from the configuration store.
     *
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @return OMAGServerConfig properties or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName parameter.
     */
    public OMAGServerConfigResponse getStoredConfiguration(String serverName,
                                                           String delegatingUserId)
    {
        final String methodName = "getStoredConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OMAGServerConfigResponse response = new OMAGServerConfigResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OpenMetadataPlatformSecurityVerifier.validateUserAsInvestigatorForPlatform(userId);
            if (delegatingUserId != null)
            {
                OpenMetadataPlatformSecurityVerifier.validateUserAsInvestigatorForPlatform(delegatingUserId);
            }

            response.setOMAGServerConfig(configStore.getServerConfig(userId, delegatingUserId, serverName, false, methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the complete set of configuration properties in use by the server that have the placeholders resolved.
     *
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @return OMAGServerConfig properties or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName parameter.
     */
    public OMAGServerConfigResponse getResolvedConfiguration(String serverName,
                                                             String delegatingUserId)
    {
        final String methodName = "getResolvedConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OMAGServerConfigResponse response = new OMAGServerConfigResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OpenMetadataPlatformSecurityVerifier.validateUserAsInvestigatorForPlatform(userId);
            if (delegatingUserId != null)
            {
                OpenMetadataPlatformSecurityVerifier.validateUserAsInvestigatorForPlatform(delegatingUserId);
            }

            response.setOMAGServerConfig(configStore.getServerConfigForStartUp(userId, delegatingUserId, serverName, methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the stored configuration documents stored on the platform
     *
     * @param delegatingUserId external userId making request
     * @return OMAGServerConfigs properties or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid parameter occurred while processing.
     */
    public OMAGServerConfigsResponse retrieveAllServerConfigs(String delegatingUserId)
    {
        final String methodName = "retrieveAllServerConfigs";

        RESTCallToken token = restCallLogger.logRESTCall("", methodName);

        OMAGServerConfigsResponse response = new OMAGServerConfigsResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OpenMetadataPlatformSecurityVerifier.validateUserAsInvestigatorForPlatform(userId);
            if (delegatingUserId != null)
            {
                OpenMetadataPlatformSecurityVerifier.validateUserAsInvestigatorForPlatform(delegatingUserId);
            }

            response.setOMAGServerConfigs(configStore.retrieveAllServerConfigs(userId, delegatingUserId));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the configuration for the server.
     *
     * @param serverName  local server name
     * @param delegatingUserId external userId making request
     * @return void or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName parameter.
     */
    public VoidResponse clearOMAGServerConfig(String serverName,
                                              String delegatingUserId)
    {
        final String methodName = "clearOMAGServerConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(userId);
            if (delegatingUserId != null)
            {
                OpenMetadataPlatformSecurityVerifier.validateUserAsOperatorForPlatform(delegatingUserId);
            }

            configStore.saveServerConfig(serverName, methodName, null);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

}
