/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;


import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OMAGServerAdminForAccessServices provides the server-side support for the services that add access services
 * configuration to an OMAG Server.
 */
public class OMAGServerAdminForAccessServices
{
    private static final String      defaultInTopicName = "InTopic";
    private static final String      defaultOutTopicName = "OutTopic";

    private OMAGServerAdminStoreServices   configStore = new OMAGServerAdminStoreServices();
    private OMAGServerErrorHandler         errorHandler = new OMAGServerErrorHandler();


    /**
     * Default constructor
     */
    public OMAGServerAdminForAccessServices()
    {
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
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            EventBusConfig            eventBusConfig          = errorHandler.validateEventBusIsSet(serverName, serverConfig, methodName);
            List<AccessServiceConfig> accessServiceConfigList = new ArrayList<>();
            EnterpriseAccessConfig    enterpriseAccessConfig  = null;

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
                                                                                               eventBusConfig.getTopicURLRoot() + ".server." + serverName,
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
        catch (OMAGInvalidParameterException error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGConfigurationErrorException error)
        {
            errorHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
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
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

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
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            List<String>  configAuditLog          = serverConfig.getAuditTrail();

            if (configAuditLog == null)
            {
                configAuditLog = new ArrayList<>();
            }

            if (accessServicesConfig == null)
            {
                configAuditLog.add(new Date().toString() + " " + userId + " removed configuration for access services.");
            }
            else
            {
                configAuditLog.add(new Date().toString() + " " + userId + " updated configuration for access services.");
            }

            serverConfig.setAuditTrail(configAuditLog);

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
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            List<String>  configAuditLog          = serverConfig.getAuditTrail();

            if (configAuditLog == null)
            {
                configAuditLog = new ArrayList<>();
            }

            if (enterpriseAccessConfig == null)
            {
                configAuditLog.add(new Date().toString() + " " + userId + " removed configuration for enterprise repository services (used by access services).");
            }
            else
            {
                configAuditLog.add(new Date().toString() + " " + userId + " updated configuration for enterprise repository services (used by access services).");
            }

            serverConfig.setAuditTrail(configAuditLog);

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
}
