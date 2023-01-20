/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;


import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistrationEntry;
import org.odpi.openmetadata.adminservices.registration.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.AccessServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.AccessServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringMapResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * OMAGServerAdminForAccessServices provides the server-side support for the services that add access services
 * configuration to an OMAG Server.
 */
public class OMAGServerAdminForAccessServices
{
    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminForAccessServices.class),
                                                                      CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceName());

    private static final String      defaultInTopicName = "InTopic";
    private static final String      defaultOutTopicName = "OutTopic";

    private OMAGServerAdminStoreServices   configStore = new OMAGServerAdminStoreServices();
    private OMAGServerErrorHandler         errorHandler = new OMAGServerErrorHandler();
    private OMAGServerExceptionHandler     exceptionHandler = new OMAGServerExceptionHandler();


    /**
     * Default constructor
     */
    public OMAGServerAdminForAccessServices()
    {
    }


    /**
     * Return the list of access services that are configured for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     *
     * @return list of access service descriptions
     */
    public RegisteredOMAGServicesResponse getConfiguredAccessServices(String              userId,
                                                                      String              serverName)
    {
        final String methodName = "getConfiguredAccessServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            /*
             * Get the list of Access Services configured in this server.
             */
            List<AccessServiceConfig> accessServiceConfigList = serverConfig.getAccessServicesConfig();

            /*
             * Set up the available access services.
             */
            if ((accessServiceConfigList != null) && (! accessServiceConfigList.isEmpty()))
            {
                List<RegisteredOMAGService> services = new ArrayList<>();
                for (AccessServiceConfig accessServiceConfig : accessServiceConfigList)
                {
                    if (accessServiceConfig != null)
                    {
                        if (accessServiceConfig.getAccessServiceOperationalStatus() == ServiceOperationalStatus.ENABLED)
                        {
                            RegisteredOMAGService service = new RegisteredOMAGService();

                            service.setServiceId(accessServiceConfig.getAccessServiceId());
                            service.setServiceName(accessServiceConfig.getAccessServiceFullName());
                            service.setServiceDevelopmentStatus(accessServiceConfig.getAccessServiceDevelopmentStatus());
                            service.setServiceDescription(accessServiceConfig.getAccessServiceDescription());
                            service.setServiceURLMarker(accessServiceConfig.getAccessServiceURLMarker());
                            service.setServiceWiki(accessServiceConfig.getAccessServiceWiki());
                            services.add(service);
                        }
                    }
                }
                if (!services.isEmpty())
                {
                    response.setServices(services);
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
     * Return the list of access services that are configured for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     *
     * @return list of access service configurations
     */
    public AccessServicesResponse getAccessServicesConfiguration(String userId,
                                                                 String serverName)
    {
        final String methodName = "getAccessServicesConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AccessServicesResponse response = new AccessServicesResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            /*
             * Get the list of Access Services configured in this server.
             */
            response.setServices(serverConfig.getAccessServicesConfig());
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
     * Enable a single access service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker access service name used in URL
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureAccessService(String              userId,
                                               String              serverName,
                                               String              serviceURLMarker,
                                               Map<String, Object> accessServiceOptions)
    {
        final String methodName = "configureAccessService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            EventBusConfig            eventBusConfig          = serverConfig.getEventBusConfig();
            List<AccessServiceConfig> accessServiceConfigList = serverConfig.getAccessServicesConfig();
            EnterpriseAccessConfig    enterpriseAccessConfig  = this.getEnterpriseAccessConfig(serverConfig);

            /*
             * Get the registration information for this access service.
             */
            AccessServiceRegistrationEntry accessServiceRegistration = OMAGAccessServiceRegistration.getAccessServiceRegistration(serviceURLMarker);

            errorHandler.validateAccessServiceIsRegistered(accessServiceRegistration, serviceURLMarker, serverName, methodName);

            accessServiceConfigList = this.updateAccessServiceConfig(createAccessServiceConfig(accessServiceRegistration,
                                                                                               accessServiceOptions,
                                                                                               eventBusConfig,
                                                                                               serverName,
                                                                                               serverConfig.getLocalServerId()),
                                                                     accessServiceConfigList);


            if (enterpriseAccessConfig == null)
            {
                /*
                 * Set up the enterprise repository services if this is the first access service.
                 */
                OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();
                enterpriseAccessConfig = configurationFactory.getDefaultEnterpriseAccessConfig(serverConfig.getLocalServerName(),
                                                                                               serverConfig.getLocalServerId());
            }

            this.setAccessServicesConfig(userId, serverName, accessServiceConfigList);
            this.setEnterpriseAccessConfig(userId, serverName, enterpriseAccessConfig);

        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGConfigurationErrorException error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
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
     * Enable all access services that are registered with this server platform.   The configuration properties
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
    public VoidResponse configureAllAccessServices(String              userId,
                                                   String              serverName,
                                                   Map<String, Object> accessServiceOptions)
    {
        final String methodName = "configureAllAccessServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            EventBusConfig            eventBusConfig          = errorHandler.validateEventBusIsSet(serverName, serverConfig, methodName);
            List<AccessServiceConfig> accessServiceConfigList = new ArrayList<>();
            EnterpriseAccessConfig    enterpriseAccessConfig  = null;

            /*
             * Get the list of Access Services implemented in this server.
             */
            List<AccessServiceRegistrationEntry> accessServiceRegistrationList = OMAGAccessServiceRegistration.getAccessServiceRegistrationList();

            /*
             * Set up the available access services.
             */
            if ((accessServiceRegistrationList != null) && (! accessServiceRegistrationList.isEmpty()))
            {
                for (AccessServiceRegistrationEntry registration : accessServiceRegistrationList)
                {
                    if (registration != null)
                    {
                        if (registration.getAccessServiceOperationalStatus() == ServiceOperationalStatus.ENABLED)
                        {
                            accessServiceConfigList.add(createAccessServiceConfig(registration,
                                                                                  accessServiceOptions,
                                                                                  eventBusConfig,
                                                                                  serverName,
                                                                                  serverConfig.getLocalServerId()));
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
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGConfigurationErrorException error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
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
     * Enable a single access service.
     * This version of the call does not set up the InTopic nor the OutTopic.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker access service name used in URL
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureAccessServiceNoTopics(String              userId,
                                                       String              serverName,
                                                       String              serviceURLMarker,
                                                       Map<String, Object> accessServiceOptions)
    {
        final String methodName = "configureAccessServiceNoTopics";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<AccessServiceConfig> accessServiceConfigList = serverConfig.getAccessServicesConfig();
            EnterpriseAccessConfig    enterpriseAccessConfig  = this.getEnterpriseAccessConfig(serverConfig);

            /*
             * Get the registration information for this access service.
             */
            AccessServiceRegistrationEntry accessServiceRegistration = OMAGAccessServiceRegistration.getAccessServiceRegistration(serviceURLMarker);

            errorHandler.validateAccessServiceIsRegistered(accessServiceRegistration, serviceURLMarker, serverName, methodName);

            accessServiceConfigList = this.updateAccessServiceConfig(createAccessServiceConfig(accessServiceRegistration,
                                                                                               accessServiceOptions,
                                                                                               null,
                                                                                               serverName,
                                                                                               serverConfig.getLocalServerId()),
                                                                     accessServiceConfigList);


            if (enterpriseAccessConfig == null)
            {
                /*
                 * Set up the enterprise repository services if this is the first access service.
                 */
                OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();
                enterpriseAccessConfig = configurationFactory.getDefaultEnterpriseAccessConfig(serverConfig.getLocalServerName(),
                                                                                               serverConfig.getLocalServerId());
            }

            this.setAccessServicesConfig(userId, serverName, accessServiceConfigList);
            this.setEnterpriseAccessConfig(userId, serverName, enterpriseAccessConfig);

        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGConfigurationErrorException error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
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
     * Enable all access services that are registered with this server platform.   The configuration properties
     * for each access service can be changed from their default using setAccessServicesConfig operation.
     * This version of the call does not set up the InTopic nor the OutTopic.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureAllAccessServicesNoTopics(String              userId,
                                                           String              serverName,
                                                           Map<String, Object> accessServiceOptions)
    {
        final String methodName = "configureAllAccessServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<AccessServiceConfig> accessServiceConfigList = new ArrayList<>();
            EnterpriseAccessConfig    enterpriseAccessConfig  = null;

            /*
             * Get the list of Access Services implemented in this server.
             */
            List<AccessServiceRegistrationEntry> accessServiceRegistrationList = OMAGAccessServiceRegistration.getAccessServiceRegistrationList();

            /*
             * Set up the available access services.
             */
            if ((accessServiceRegistrationList != null) && (! accessServiceRegistrationList.isEmpty()))
            {
                for (AccessServiceRegistrationEntry registration : accessServiceRegistrationList)
                {
                    if (registration != null)
                    {
                        if (registration.getAccessServiceOperationalStatus() == ServiceOperationalStatus.ENABLED)
                        {
                            accessServiceConfigList.add(createAccessServiceConfig(registration,
                                                                                  accessServiceOptions,
                                                                                  null,
                                                                                  serverName,
                                                                                  serverConfig.getLocalServerId()));
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
     * Set up the configuration for a single access service.
     *
     * @param registration registration information about the service.
     * @param accessServiceOptions options for the service
     * @param eventBusConfig details of the event bus
     * @param serverName name of this server
     * @param localServerId unique Id for this server
     * @return newly created config object
     */
    private AccessServiceConfig  createAccessServiceConfig(AccessServiceRegistrationEntry registration,
                                                           Map<String, Object>         accessServiceOptions,
                                                           EventBusConfig              eventBusConfig,
                                                           String                      serverName,
                                                           String                      localServerId)
    {
        ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

        AccessServiceConfig accessServiceConfig = new AccessServiceConfig(registration);

        accessServiceConfig.setAccessServiceOptions(accessServiceOptions);

        if (eventBusConfig != null)
        {
            accessServiceConfig.setAccessServiceInTopic(
                    connectorConfigurationFactory.getDefaultEventBusConnection(eventBusConfig.getConnectorProvider(),
                                                                               eventBusConfig.getTopicURLRoot() + ".server." + serverName,
                                                                               registration.getAccessServiceInTopic(),
                                                                               localServerId,
                                                                               eventBusConfig.getConfigurationProperties()));
            accessServiceConfig.setAccessServiceOutTopic(
                    connectorConfigurationFactory.getDefaultEventBusConnection(eventBusConfig.getConnectorProvider(),
                                                                               eventBusConfig.getTopicURLRoot() + ".server." + serverName,
                                                                               registration.getAccessServiceOutTopic(),
                                                                               localServerId,
                                                                               eventBusConfig.getConfigurationProperties()));
        }

        return accessServiceConfig;
    }


    /**
     * Add/update the configuration for a single service in the configuration.
     *
     * @param accessServiceConfig configuration to add/change
     * @param currentList current config (may be null)
     * @return updated list
     */
    private List<AccessServiceConfig>  updateAccessServiceConfig(AccessServiceConfig         accessServiceConfig,
                                                                 List<AccessServiceConfig>   currentList)
    {
        if (accessServiceConfig == null)
        {
            return currentList;
        }
        else
        {
            List<AccessServiceConfig> newList = new ArrayList<>();

            if (currentList != null)
            {
                for (AccessServiceConfig existingConfig : currentList)
                {
                    if (existingConfig != null)
                    {
                        if (accessServiceConfig.getAccessServiceId() != existingConfig.getAccessServiceId())
                        {
                            newList.add(existingConfig);
                        }
                    }
                }
            }

            newList.add(accessServiceConfig);

            if (newList.isEmpty())
            {
                return null;
            }

            return newList;
        }
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
    public VoidResponse clearAllAccessServices(String          userId,
                                               String          serverName)
    {
        final String methodName = "clearAllAccessServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

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
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
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
     * Retrieve the config for an access service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker access service name used in URL
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    public AccessServiceConfigResponse getAccessServiceConfig(String userId,
                                                              String serverName,
                                                              String serviceURLMarker)
    {
        final String methodName = "getAccessServiceConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AccessServiceConfigResponse response = new AccessServiceConfigResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<AccessServiceConfig> currentList = serverConfig.getAccessServicesConfig();

            if (currentList != null)
            {
                for (AccessServiceConfig existingConfig : currentList)
                {
                    if (existingConfig != null)
                    {
                        if (serviceURLMarker.equals(existingConfig.getAccessServiceURLMarker()))
                        {
                            response.setConfig(existingConfig);
                        }
                    }
                }
            }
        }
        catch (OMAGInvalidParameterException  error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
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
     * Remove an access service.  This removes all configuration for the access service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker access service name used in URL
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    public VoidResponse clearAccessService(String userId,
                                           String serverName,
                                           String serviceURLMarker)
    {
        final String methodName = "clearAccessService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<AccessServiceConfig> currentList = serverConfig.getAccessServicesConfig();
            List<AccessServiceConfig> newList     = new ArrayList<>();

            if (currentList != null)
            {
                for (AccessServiceConfig existingConfig : currentList)
                {
                    if (existingConfig != null)
                    {
                        if (! serviceURLMarker.equals(existingConfig.getAccessServiceURLMarker()))
                        {
                            newList.add(existingConfig);
                        }
                    }
                }

                serverConfig.setAccessServicesConfig(newList);
                configStore.saveServerConfig(serverName, methodName, serverConfig);
            }
        }
        catch (OMAGInvalidParameterException  error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
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
     * Retrieve the topic names for this access service
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param serviceURLMarker string indicating which access service it requested
     *
     * @return map of topic names or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    public StringMapResponse  getAccessServiceTopicNames(String userId,
                                                         String serverName,
                                                         String serviceURLMarker)
    {
        final String methodName   = "getAccessServiceTopicNames";
        final String propertyName = "serviceURLMarker";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        StringMapResponse response = new StringMapResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validatePropertyNotNull(serviceURLMarker, propertyName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            List<AccessServiceConfig>  configuredAccessServices = serverConfig.getAccessServicesConfig();

            if (configuredAccessServices != null)
            {
                for (AccessServiceConfig accessServiceConfig : configuredAccessServices)
                {
                    if (accessServiceConfig != null)
                    {
                        if (serviceURLMarker.equals(accessServiceConfig.getAccessServiceURLMarker()))
                        {
                            /*
                             * Found it - just need to set up response
                             */
                            Map<String, String>  topicNames = new HashMap<>();

                            topicNames.put(accessServiceConfig.getAccessServiceFullName() + " " + defaultInTopicName,
                                           accessServiceConfig.getAccessServiceInTopic().getEndpoint().getAddress());

                            topicNames.put(accessServiceConfig.getAccessServiceFullName() + " " + defaultOutTopicName,
                                           accessServiceConfig.getAccessServiceOutTopic().getEndpoint().getAddress());

                            response.setStringMap(topicNames);
                        }
                    }
                }
            }
        }
        catch (OMAGInvalidParameterException  error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
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
     * Retrieve the topic names for all configured access services
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     *
     * @return map of topic names or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    public StringMapResponse  getAllAccessServiceTopicNames(String userId,
                                                            String serverName)
    {
        final String methodName   = "getAccessServiceTopicNames";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        StringMapResponse response = new StringMapResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            List<AccessServiceConfig>  configuredAccessServices = serverConfig.getAccessServicesConfig();

            if (configuredAccessServices != null)
            {
                Map<String, String>  topicNames = new HashMap<>();

                for (AccessServiceConfig accessServiceConfig : configuredAccessServices)
                {
                    if (accessServiceConfig != null)
                    {
                        topicNames.put(accessServiceConfig.getAccessServiceFullName() + " " + defaultInTopicName,
                                       accessServiceConfig.getAccessServiceInTopic().getEndpoint().getAddress());

                        topicNames.put(accessServiceConfig.getAccessServiceFullName() + " " + defaultOutTopicName,
                                       accessServiceConfig.getAccessServiceOutTopic().getEndpoint().getAddress());
                    }
                }

                response.setStringMap(topicNames);
            }
        }
        catch (OMAGInvalidParameterException  error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
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
     * Update the in topic name for a specific access service.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param serviceURLMarker string indicating which access service it requested
     * @param topicName string for new topic name
     *
     * @return map of topic names or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    public VoidResponse  overrideAccessServiceInTopicName(String userId,
                                                          String serverName,
                                                          String serviceURLMarker,
                                                          String topicName)
    {
        final String methodName   = "overrideAccessServiceInTopicName";
        final String serviceURLMarkerPropertyName = "serviceURLMarker";
        final String topicPropertyName = "topicName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validatePropertyNotNull(topicName, topicPropertyName, serverName, methodName);
            errorHandler.validatePropertyNotNull(serviceURLMarker, serviceURLMarkerPropertyName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            List<AccessServiceConfig>  configuredAccessServices = serverConfig.getAccessServicesConfig();

            if (configuredAccessServices != null)
            {
                for (AccessServiceConfig accessServiceConfig : configuredAccessServices)
                {
                    if (accessServiceConfig != null)
                    {
                        if (serviceURLMarker.equals(accessServiceConfig.getAccessServiceURLMarker()))
                        {
                            /*
                             * Found it - just need to set up topic Name
                             */
                            Connection connection = accessServiceConfig.getAccessServiceInTopic();

                            if (connection != null)
                            {
                                Endpoint endpoint = connection.getEndpoint();

                                if (endpoint != null)
                                {
                                    endpoint.setAddress(topicName);
                                    connection.setEndpoint(endpoint);
                                    accessServiceConfig.setAccessServiceInTopic(connection);
                                    setAccessServicesConfig(userId, serverName, configuredAccessServices);
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (OMAGInvalidParameterException  error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
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
     * Update the out topic name for a specific access service.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param serviceURLMarker string indicating which access service it requested
     * @param topicName string for new topic name
     *
     * @return map of topic names or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or accessServicesConfig parameter.
     */
    public VoidResponse  overrideAccessServiceOutTopicName(String userId,
                                                           String serverName,
                                                           String serviceURLMarker,
                                                           String topicName)
    {
        final String methodName   = "overrideAccessServiceOutTopicName";
        final String serviceURLMarkerPropertyName = "serviceURLMarker";
        final String topicPropertyName = "topicName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validatePropertyNotNull(topicName, topicPropertyName, serverName, methodName);
            errorHandler.validatePropertyNotNull(serviceURLMarker, serviceURLMarkerPropertyName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);
            List<AccessServiceConfig>  configuredAccessServices = serverConfig.getAccessServicesConfig();

            if (configuredAccessServices != null)
            {
                for (AccessServiceConfig accessServiceConfig : configuredAccessServices)
                {
                    if (accessServiceConfig != null)
                    {
                        if (serviceURLMarker.equals(accessServiceConfig.getAccessServiceURLMarker()))
                        {
                            /*
                             * Found it - just need to set up topic Name
                             */
                            Connection connection = accessServiceConfig.getAccessServiceOutTopic();

                            if (connection != null)
                            {
                                Endpoint endpoint = connection.getEndpoint();

                                if (endpoint != null)
                                {
                                    endpoint.setAddress(topicName);
                                    connection.setEndpoint(endpoint);
                                    accessServiceConfig.setAccessServiceOutTopic(connection);
                                    setAccessServicesConfig(userId, serverName, configuredAccessServices);
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (OMAGInvalidParameterException  error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
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
     * Set up the configuration for all the open metadata access services (OMASs).  This overrides
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

            if (accessServicesConfig == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for access services.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for access services.");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            serverConfig.setAccessServicesConfig(accessServicesConfig);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException  error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
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
     * Extract the current value of the enterprise access configuration.
     *
     * @param serverConfig current configuration
     * @return enterprise access configuration
     */
    private EnterpriseAccessConfig getEnterpriseAccessConfig(OMAGServerConfig   serverConfig)
    {
        RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

        if (repositoryServicesConfig != null)
        {
            return repositoryServicesConfig.getEnterpriseAccessConfig();
        }

        return null;
    }


    /**
     * Set up the default remote enterprise topic.  This allows a remote process to monitor enterprise topic events.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param configurationProperties additional properties for the cohort
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or null userId parameter.
     */
    public VoidResponse addRemoteEnterpriseTopic(String               userId,
                                                 String               serverName,
                                                 Map<String, Object>  configurationProperties)
    {
        final String methodName = "addRemoteEnterpriseTopic";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            EventBusConfig   eventBusConfig  = errorHandler.validateEventBusIsSet(serverName, serverConfig, methodName);

            ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

            EnterpriseAccessConfig enterpriseAccessConfig = this.getEnterpriseAccessConfig(serverConfig);

            if (enterpriseAccessConfig == null)
            {
                /*
                 * Set up the enterprise repository services if this is the first access service.
                 */
                OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();
                enterpriseAccessConfig = configurationFactory.getDefaultEnterpriseAccessConfig(serverConfig.getLocalServerName(),
                                                                                               serverConfig.getLocalServerId());
            }

            enterpriseAccessConfig.setRemoteEnterpriseOMRSTopicConnection(connectorConfigurationFactory.getDefaultRemoteEnterpriseOMRSTopicConnection(serverName,
                                                                                                                                                      configurationProperties,
                                                                                                                                                      eventBusConfig.getConnectorProvider(),
                                                                                                                                                      eventBusConfig.getTopicURLRoot(),
                                                                                                                                                      serverConfig.getLocalServerId(),
                                                                                                                                                      eventBusConfig.getConfigurationProperties()));
            this.setEnterpriseAccessConfig(userId, serverName, enterpriseAccessConfig);
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

            if (enterpriseAccessConfig == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for enterprise repository services (used by access services).");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for enterprise repository services (used by access services).");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            RepositoryServicesConfig repositoryServicesConfig = serverConfig.getRepositoryServicesConfig();

            if (repositoryServicesConfig != null)
            {
                repositoryServicesConfig.setEnterpriseAccessConfig(enterpriseAccessConfig);
            }
            else if (enterpriseAccessConfig != null)
            {
                OMRSConfigurationFactory configurationFactory     = new OMRSConfigurationFactory();

                repositoryServicesConfig = configurationFactory.getDefaultRepositoryServicesConfig();

                repositoryServicesConfig.setEnterpriseAccessConfig(enterpriseAccessConfig);
            }

            serverConfig.setRepositoryServicesConfig(repositoryServicesConfig);
            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException  error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException  error)
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
