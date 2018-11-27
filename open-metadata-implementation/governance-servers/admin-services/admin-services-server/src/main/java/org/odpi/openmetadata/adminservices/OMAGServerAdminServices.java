/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.properties.OMAGAPIResponse;
import org.odpi.openmetadata.adminservices.properties.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.properties.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.adminservices.ffdc.OMAGErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGCheckedExceptionBase;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;
import org.odpi.openmetadata.repositoryservices.admin.OMRSOperationalServices;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OMAGServerAdminServices provides the server-side implementation of the administrative interface for
 * an Open Metadata and Governance (OMAG) Server.  It provides all of the
 * configuration properties for the Open Metadata Access Services (OMASs) and delegates administration requests
 * to the Open Metadata Repository Services (OMRS).
 * <p>
 * There are four types of operations defined by OMAGServerAdministration interface:
 * </p>
 * <ul>
 * <li>
 * Basic configuration - these methods use the minimum of configuration information to run the
 * server using default properties.
 * </li>
 * <li>
 * Advanced Configuration - provides access to all configuration properties to provide
 * fine-grained control of the server.
 * </li>
 * <li>
 * Initialization and shutdown - these methods control the initialization and shutdown of the
 * open metadata and governance service instance based on the supplied configuration.
 * </li>
 * <li>
 * Operational status and control - these methods query the status of the open metadata and governance
 * services as well as the audit log.
 * </li>
 * </ul>
 */
public class OMAGServerAdminServices
{
    private static final String      defaultInTopicName = "InTopic";
    private static final String      defaultOutTopicName = "OutTopic";

    private OMAGServerAdminStoreServices   configStore = new OMAGServerAdminStoreServices();
    private OMAGServerErrorHandler         errorHandler = new OMAGServerErrorHandler();

    /*
     * =============================================================
     * Configure server - basic options using defaults
     */

    /**
     * Set up the root URL for this server that is used to construct full URL paths to calls for
     * this server's REST interfaces.  The default value is "localhost:8080".
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param url  String url.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    public VoidResponse setServerURLRoot(String userId,
                                         String serverName,
                                         String url)
    {
        final String methodName = "setServerURLRoot";

        VoidResponse response = new VoidResponse();

        try
        {
            validateUserId(userId, serverName, methodName);
            validateServerName(serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            serverConfig.setLocalServerURL(url);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /**
     * Set up the descriptive type of the server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is "Open Metadata and Governance Server".
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param typeName  short description for the type of server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    public VoidResponse setServerType(String userId,
                                      String serverName,
                                      String typeName)
    {
        final String methodName = "setServerType";

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            serverConfig.setLocalServerType(typeName);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /**
     * Set up the name of the organization that is running this server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is null.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param name  String name of the organization.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or organizationName parameter.
     */
    public VoidResponse setOrganizationName(String userId,
                                            String serverName,
                                            String name)
    {
        final String methodName = "setOrganizationName";

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            serverConfig.setOrganizationName(name);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

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

        VoidResponse response = new VoidResponse();

        try
        {
            validateUserId(userId, serverName, methodName);
            validateServerName(serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            serverConfig.setLocalServerUserId(serverUserId);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

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

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            if (maxPageSize > 0)
            {
                OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

                serverConfig.setMaxPageSize(maxPageSize);

                configStore.saveServerConfig(serverName, methodName, serverConfig);
            }
            else
            {
                OMAGErrorCode errorCode    = OMAGErrorCode.BAD_MAX_PAGE_SIZE;
                String        errorMessage = errorCode.getErrorMessageId()
                                           + errorCode.getFormattedErrorMessage(serverName, Integer.toString(maxPageSize));

                throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        errorMessage,
                                                        errorCode.getSystemAction(),
                                                        errorCode.getUserAction());
            }
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /**
     * Set up the default event bus for embedding in event-driven connector.   The resulting connector will
     * be used in the OMRS Topic Connector for each cohort, the in and out topics for each Access Service and
     * the local repositories event mapper.
     *
     * @param userId  user that is issuing the request.
     * @param serverName local server name.
     * @param connectorProvider  connector provider for the event bus (if it is null then Kafka is assumed).
     * @param topicURLRoot the common root of the topics used by the open metadata server.
     * @param additionalProperties  property name/value pairs used to configure the connection to the event bus connector
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse setEventBus(String              userId,
                                    String              serverName,
                                    String              connectorProvider,
                                    String              topicURLRoot,
                                    Map<String, Object> additionalProperties)
    {
        final String methodName = "setEventBus";

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            /*
             * Retrieve the existing configuration and validate it is ok to set up event bus.
             */
            OMAGServerConfig serverConfig   = configStore.getServerConfig(serverName, methodName);
            validateNewEventBusAllowed(serverName, serverConfig, methodName);

            EventBusConfig   eventBusConfig = new EventBusConfig();

            eventBusConfig.setConnectorProvider(connectorProvider);
            eventBusConfig.setTopicURLRoot(topicURLRoot);
            eventBusConfig.setAdditionalProperties(additionalProperties);

            serverConfig.setEventBusConfig(eventBusConfig);

            /*
             * Save the config away
             */
            configStore.saveServerConfig(serverName, methodName, serverConfig);

        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGConfigurationErrorException  error)
        {
            errorHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /**
     * Enable all access services that are installed into this server.   The configuration properties
     * for each access service can be changed from their default using setAccessServicesConfig operation.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse enableAccessServices(String              userId,
                                             String              serverName,
                                             Map<String, Object> accessServiceOptions)
    {
        final String methodName = "enableAccessServices";

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            EventBusConfig            eventBusConfig           = validateEventBusIsSet(serverName, serverConfig, methodName);
            List<AccessServiceConfig> accessServiceConfigList  = new ArrayList<>();
            EnterpriseAccessConfig    enterpriseAccessConfig   = null;


            /*
             * Get the list of Access Services implemented in this server.
             */
            List<AccessServiceRegistration> accessServiceRegistrationList = OMAGAccessServiceRegistration.getAccessServiceRegistrationList();

            /*
             * Set up the available access services.
             */
            if ((accessServiceRegistrationList != null) && (! accessServiceRegistrationList.isEmpty()))
            {
                for (AccessServiceRegistration registration : accessServiceRegistrationList)
                {
                    if (registration != null)
                    {
                        if (registration.getAccessServiceOperationalStatus() == AccessServiceOperationalStatus.ENABLED)
                        {
                            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

                            AccessServiceConfig accessServiceConfig = new AccessServiceConfig(registration);

                            accessServiceConfig.setAccessServiceOptions(accessServiceOptions);
                            accessServiceConfig.setAccessServiceInTopic(
                                    connectorConfigurationFactory.getDefaultEventBusConnection(defaultInTopicName,
                                                                                               eventBusConfig.getConnectorProvider(),
                                                                                               eventBusConfig.getTopicURLRoot() + ".server." + serverName,
                                                                                               registration.getAccessServiceInTopic(),
                                                                                               serverConfig.getLocalServerId(),
                                                                                               eventBusConfig.getAdditionalProperties()));
                            accessServiceConfig.setAccessServiceOutTopic(
                                    connectorConfigurationFactory.getDefaultEventBusConnection(defaultOutTopicName,
                                                                                               eventBusConfig.getConnectorProvider(),
                                                                                               eventBusConfig.getTopicURLRoot(),
                                                                                               registration.getAccessServiceOutTopic(),
                                                                                               serverConfig.getLocalServerId(),
                                                                                               eventBusConfig.getAdditionalProperties()));

                            accessServiceConfigList.add(accessServiceConfig);
                        }
                    }
                }

                /*
                 * Now set up the enterprise repository services.
                 */
                OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();
                enterpriseAccessConfig = configurationFactory.getDefaultEnterpriseAccessConfig(serverConfig.getLocalServerName(),
                                                                                               serverConfig.getLocalServerId());
            }

            if (accessServiceConfigList.isEmpty())
            {
                accessServiceConfigList = null;
            }

            this.setAccessServicesConfig(userId, serverName, accessServiceConfigList);
            this.setEnterpriseAccessConfig(userId, serverName, enterpriseAccessConfig);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGConfigurationErrorException  error)
        {
            errorHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /**
     * Disable the access services.  This removes all configuration for the access services
     * and disables the enterprise repository services.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    public VoidResponse disableAccessServices(String          userId,
                                              String          serverName)
    {
        final String methodName = "disableAccessServices";

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            this.setAccessServicesConfig(userId, serverName, null);
            this.setEnterpriseAccessConfig(userId, serverName, null);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /**
     * Set up an in memory local repository.  This repository uses hashmaps to store content.  It is useful
     * for demos, testing and POCs.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public VoidResponse setInMemLocalRepository(String userId,
                                                String serverName)
    {
        final String methodName = "setInMemLocalRepository";

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            OMRSConfigurationFactory configurationFactory     = new OMRSConfigurationFactory();


            this.setLocalRepositoryConfig(userId,
                                          serverName,
                                          configurationFactory.getInMemoryLocalRepositoryConfig(serverConfig.getLocalServerName(),
                                                                                                serverConfig.getLocalServerURL()));
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /**
     * Set up a graph store as the local repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param additionalProperties additional properties for the event bus connection
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    public VoidResponse setGraphLocalRepository(String              userId,
                                                String              serverName,
                                                Map<String,Object>  additionalProperties)
    {
        final String methodName = "setGraphLocalRepository";

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);
            EventBusConfig eventBusConfig = validateEventBusIsSet(serverName, serverConfig, methodName);

            OMRSConfigurationFactory configurationFactory     = new OMRSConfigurationFactory();


            this.setLocalRepositoryConfig(userId,
                                          serverName,
                                          configurationFactory.getLocalGraphLocalRepositoryConfig(serverConfig.getLocalServerName(),
                                                                                                  serverConfig.getLocalServerURL(),
                                                                                                  additionalProperties,
                                                                                                  eventBusConfig.getConnectorProvider(),
                                                                                                  eventBusConfig.getTopicURLRoot(),
                                                                                                  serverConfig.getLocalServerId(),
                                                                                                  eventBusConfig.getAdditionalProperties()));
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGConfigurationErrorException  error)
        {
            errorHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

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
    public VoidResponse setNoRepositoryMode(String userId,
                                            String serverName)
    {
        final String methodName = "setNoRepositoryMode";

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            this.setLocalRepositoryConfig(userId, serverName, null);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

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

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

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
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

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

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            this.setRepositoryProxyConnection(userId,
                                              serverName,
                                              connectorConfigurationFactory.getRepositoryProxyConnection(serverName,
                                                                                                         connectorProvider,
                                                                                                         serverConfig.getLocalServerURL(),
                                                                                                         additionalProperties));
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

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
    public VoidResponse setLocalRepositoryEventMapper(String     userId,
                                                      String     serverName,
                                                      Connection connection)
    {
        final String methodName = "setLocalRepositoryEventMapper";

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

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
                OMAGErrorCode errorCode    = OMAGErrorCode.LOCAL_REPOSITORY_MODE_NOT_SET;
                String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

                throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          errorMessage,
                                                          errorCode.getSystemAction(),
                                                          errorCode.getUserAction());
            }

            /*
             * Set up the event mapper connection in the local repository config
             */
            localRepositoryConfig.setEventMapperConnection(connection);

            this.setLocalRepositoryConfig(userId, serverName, localRepositoryConfig);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGConfigurationErrorException  error)
        {
            errorHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

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
    public VoidResponse setLocalRepositoryEventMapper(String              userId,
                                                      String              serverName,
                                                      String              connectorProvider,
                                                      String              eventSource,
                                                      Map<String, Object> additionalProperties)
    {
        final String methodName = "setLocalRepositoryEventMapper";

        VoidResponse response = new VoidResponse();

        try
        {
            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            this.setLocalRepositoryEventMapper(userId,
                                               serverName,
                                               connectorConfigurationFactory.getRepositoryEventMapperConnection(serverName,
                                                                                                                connectorProvider,
                                                                                                                additionalProperties,
                                                                                                                eventSource));
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /**
     * Enable registration of server to an open metadata repository cohort.  This is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to changes in their supported metadata types and instances.
     * They are also able to query each other's metadata directly through REST calls.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName, cohortName or serviceMode parameter.
     * OMAGConfigurationErrorException the event bus is not set.
     */
    public VoidResponse enableCohortRegistration(String               userId,
                                                 String               serverName,
                                                 String               cohortName,
                                                 Map<String, Object>  additionalProperties)
    {
        final String methodName = "enableCohortRegistration";

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);
            validateCohortName(cohortName, serverName, methodName);

            OMAGServerConfig serverConfig    = configStore.getServerConfig(serverName, methodName);

            EventBusConfig   eventBusConfig  = validateEventBusIsSet(serverName, serverConfig, methodName);

            /*
             * Set up a new cohort
             */
            OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();

            CohortConfig newCohortConfig = configurationFactory.getDefaultCohortConfig(serverConfig.getLocalServerName(),
                                                                                       cohortName,
                                                                                       additionalProperties,
                                                                                       eventBusConfig.getConnectorProvider(),
                                                                                       eventBusConfig.getTopicURLRoot(),
                                                                                       serverConfig.getLocalServerId(),
                                                                                       eventBusConfig.getAdditionalProperties());


            this.setCohortConfig(userId, serverName, cohortName, newCohortConfig);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGConfigurationErrorException  error)
        {
            errorHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

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
    public VoidResponse disableCohortRegistration(String          userId,
                                                  String          serverName,
                                                  String          cohortName)
    {
        final String methodName = "setCohortMode";

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);
            validateCohortName(cohortName, serverName, methodName);

            this.setCohortConfig(userId, serverName, cohortName, null);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /*
     * =============================================================
     * Configure server - advanced options overriding defaults
     */


    /**
     * Set up the configuration for all of the open metadata access services (OMASs).  This overrides
     * the current values.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param accessServicesConfig  list of configuration properties for each access service.
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    public VoidResponse setAccessServicesConfig(String                    userId,
                                                String                    serverName,
                                                List<AccessServiceConfig> accessServicesConfig)
    {
        final String methodName = "setAccessServicesConfig";

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            serverConfig.setAccessServicesConfig(accessServicesConfig);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
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

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            /*
             * Set up the local repository config in the open metadata repository services config.
             */
            if (repositoryServicesConfig != null)
            {
                repositoryServicesConfig.setLocalRepositoryConfig(localRepositoryConfig);
            }
            else if (localRepositoryConfig != null)
            {
                OMRSConfigurationFactory configurationFactory     = new OMRSConfigurationFactory();

                repositoryServicesConfig = configurationFactory.getDefaultRepositoryServicesConfig(serverConfig.getLocalServerName());
                repositoryServicesConfig.setLocalRepositoryConfig(localRepositoryConfig);
            }

            /*
             * Save the open metadata repository services config in the server's config
             */
            serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);
            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /**
     * Set up the configuration that controls the enterprise repository services.  These services are part
     * of the Open Metadata Repository Services (OMRS).  They provide federated queries and federated event
     * notifications that cover metadata from the local repository plus any repositories connected via
     * open metadata repository cohorts.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param enterpriseAccessConfig  enterprise repository services configuration properties.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or enterpriseAccessConfig parameter.
     */
    public VoidResponse setEnterpriseAccessConfig(String                 userId,
                                                  String                 serverName,
                                                  EnterpriseAccessConfig enterpriseAccessConfig)
    {
        final String methodName = "setEnterpriseAccessConfig";

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            if (repositoryServicesConfig != null)
            {
                repositoryServicesConfig.setEnterpriseAccessConfig(enterpriseAccessConfig);
            }
            else if (enterpriseAccessConfig != null)
            {
                OMRSConfigurationFactory configurationFactory     = new OMRSConfigurationFactory();

                repositoryServicesConfig = configurationFactory.getDefaultRepositoryServicesConfig(serverConfig.getLocalServerName());

                repositoryServicesConfig.setEnterpriseAccessConfig(enterpriseAccessConfig);
            }

            serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);
            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

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

        VoidResponse response = new VoidResponse();

        try
        {
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);
            validateCohortName(cohortName, serverName, methodName);

            OMAGServerConfig         serverConfig = configStore.getServerConfig(serverName, methodName);
            OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();
            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();
            List<CohortConfig>       existingCohortConfigs = null;
            List<CohortConfig>       newCohortConfigs = new ArrayList<>();

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
                repositoryServicesConfig = configurationFactory.getDefaultRepositoryServicesConfig(serverConfig.getLocalServerName());

                repositoryServicesConfig.setCohortConfigList(newCohortConfigs);
            }

            serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);
            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /*
     * =============================================================
     * Query current configuration
     */


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

        OMAGServerConfigResponse response = new OMAGServerConfigResponse();

        try
        {
            validateServerName(serverName, methodName);
            validateUserId(userId, serverName, methodName);

            response.setOMAGServerConfig(configStore.getServerConfig(serverName, methodName));
        }
        catch (OMAGInvalidParameterException  error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /*
     * =============================================================
     * Private methods
     */

    /**
     * Validate that the user id is not null.
     *
     * @param userId  user name passed on the request
     * @param serverName  name of this server
     * @param methodName  method receiving the call
     * @throws OMAGNotAuthorizedException no userId provided
     */
    private void validateUserId(String userId,
                                String serverName,
                                String methodName) throws OMAGNotAuthorizedException
    {
        if (userId == null)
        {
            OMAGErrorCode errorCode    = OMAGErrorCode.NULL_USER_NAME;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

            throw new OMAGNotAuthorizedException(errorCode.getHTTPErrorCode(),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 errorMessage,
                                                 errorCode.getSystemAction(),
                                                 errorCode.getUserAction());
        }
    }


    /**
     * Validate that the server name is not null and save it in the config.
     *
     * @param serverName  serverName passed on a request
     * @param methodName  method being called
     * @throws OMAGInvalidParameterException null server name
     */
    private void validateServerName(String serverName,
                                    String methodName) throws OMAGInvalidParameterException
    {
        /*
         * If the local server name is still null then save the server name in the configuration.
         */
        if (serverName == null)
        {
            OMAGErrorCode errorCode    = OMAGErrorCode.NULL_LOCAL_SERVER_NAME;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
        }
    }


    /**
     * Test to see if the access services, cohorts or local repositories that use an event bus have been configured.
     * If they have then it is too late to set up the event bus properties.
     *
     * @param serverName  name of the server
     * @param serverConfig  existing configuration
     * @param methodName  calling method
     * @throws OMAGConfigurationErrorException it is too late to set up the event bus
     */
    private void validateNewEventBusAllowed(String           serverName,
                                            OMAGServerConfig serverConfig,
                                            String           methodName) throws OMAGConfigurationErrorException
    {
        List<AccessServiceConfig> accessServiceConfigList = null;
        LocalRepositoryConfig     localRepositoryConfig = null;
        List<CohortConfig>        cohortConfigList      = null;

        if (serverConfig != null)
        {
            accessServiceConfigList = serverConfig.getAccessServicesConfig();

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            if (repositoryServicesConfig != null)
            {
                localRepositoryConfig = repositoryServicesConfig.getLocalRepositoryConfig();
                cohortConfigList = repositoryServicesConfig.getCohortConfigList();

                if (localRepositoryConfig != null)
                {
                    if (localRepositoryConfig.getEventMapperConnection() == null)
                    {
                        localRepositoryConfig = null;
                    }
                }
            }
        }

        if ((accessServiceConfigList != null) ||
            (localRepositoryConfig != null) ||
            (cohortConfigList != null))
        {
            OMAGErrorCode errorCode    = OMAGErrorCode.TOO_LATE_TO_SET_EVENT_BUS;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());
        }
    }


    /**
     * Make sure the event bus properties are set before allowing configuration that is dependent on it to
     * be set.
     *
     * @param serverName  name of the server
     * @param serverConfig  existing configuration
     * @param methodName  calling method
     * @return the event bus config for this server
     * @throws OMAGConfigurationErrorException there is no event bus information
     */
    private EventBusConfig validateEventBusIsSet(String           serverName,
                                                 OMAGServerConfig serverConfig,
                                                 String           methodName) throws OMAGConfigurationErrorException
    {
        EventBusConfig   eventBusConfig = null;

        if (serverConfig != null)
        {
            eventBusConfig = serverConfig.getEventBusConfig();
        }

        if (eventBusConfig == null)
        {
            OMAGErrorCode errorCode    = OMAGErrorCode.NO_EVENT_BUS_SET;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

            throw new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      errorMessage,
                                                      errorCode.getSystemAction(),
                                                      errorCode.getUserAction());
        }

        return eventBusConfig;
    }


    /**
     * Validate that the cohort name is not null.
     *
     * @param cohortName  cohortName passed on the request
     * @param serverName  server name for this server
     * @param methodName  method called
     * @throws OMAGInvalidParameterException the cohort name is null
     */
    private void validateCohortName(String  cohortName,
                                    String  serverName,
                                    String  methodName) throws OMAGInvalidParameterException
    {
        if (cohortName == null)
        {
            OMAGErrorCode errorCode    = OMAGErrorCode.NULL_COHORT_NAME;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

            throw new OMAGInvalidParameterException(errorCode.getHTTPErrorCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    errorMessage,
                                                    errorCode.getSystemAction(),
                                                    errorCode.getUserAction());
        }
    }
}
