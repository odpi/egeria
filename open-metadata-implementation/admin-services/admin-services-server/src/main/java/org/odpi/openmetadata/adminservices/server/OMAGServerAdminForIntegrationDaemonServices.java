/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;


import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.odpi.openmetadata.adminservices.rest.*;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceRegistry;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * OMAGServerAdminForIntegrationDaemonServices provides the server-side support for the services that
 * configure the specialized part of the integration daemon.
 */
public class OMAGServerAdminForIntegrationDaemonServices
{
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminForIntegrationDaemonServices.class),
                                                                            CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName());
    

    private final OMAGServerAdminStoreServices   configStore = new OMAGServerAdminStoreServices();
    private final OMAGServerErrorHandler         errorHandler = new OMAGServerErrorHandler();
    private final OMAGServerExceptionHandler     exceptionHandler = new OMAGServerExceptionHandler();


    /**
     * Default constructor
     */
    public OMAGServerAdminForIntegrationDaemonServices()
    {
    }



    /**
     * Return the list of integration groups that are configured for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     *
     * @return list of access service configurations
     */
    public IntegrationGroupsResponse getIntegrationGroupsConfiguration(String userId,
                                                                       String serverName)
    {
        final String methodName = "getIntegrationGroupsConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationGroupsResponse response = new IntegrationGroupsResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            /*
             * Get the list of Integration Services configured in this server.
             */
            response.setGroups(serverConfig.getDynamicIntegrationGroupsConfig());
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
     * Enable a single registered integration group.  This builds the integration group configuration for the
     * server's config document.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param requestBody  minimum values to configure an integration group
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureIntegrationGroup(String                 userId,
                                                  String                 serverName,
                                                  IntegrationGroupConfig requestBody)
    {
        final String methodName = "configureIntegrationGroup";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate the incoming parameters
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateIntegrationGroupConfig(serverName, requestBody, methodName);

            /*
             * Get the configuration information for this integration group.
             */
            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<IntegrationGroupConfig> integrationGroupsConfig = serverConfig.getDynamicIntegrationGroupsConfig();

            response = this.storeIntegrationGroupsConfig(userId,
                                                         serverName,
                                                         requestBody.getIntegrationGroupQualifiedName(),
                                                         updateIntegrationGroupConfig(requestBody, integrationGroupsConfig),
                                                         methodName);
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
     * Add/update the configuration for a single service in the configuration.
     *
     * @param integrationGroupConfig configuration to add/change
     * @param currentList current config (may be null)
     * @return updated list
     */
    private List<IntegrationGroupConfig>  updateIntegrationGroupConfig(IntegrationGroupConfig         integrationGroupConfig,
                                                                       List<IntegrationGroupConfig>   currentList)
    {
        if (integrationGroupConfig == null)
        {
            return currentList;
        }
        else
        {
            List<IntegrationGroupConfig> newList = new ArrayList<>();

            if (currentList != null)
            {
                for (IntegrationGroupConfig existingConfig : currentList)
                {
                    if (existingConfig != null)
                    {
                        if (existingConfig.getIntegrationGroupQualifiedName().equals(integrationGroupConfig.getIntegrationGroupQualifiedName()))
                        {
                            newList.add(existingConfig);
                        }
                    }
                }
            }

            newList.add(integrationGroupConfig);

            return newList;
        }
    }


    /**
     * Set up the configuration for all the open metadata integration groups (OMISs).  This overrides
     * the current values.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param integrationGroupsConfig  list of configuration properties for each integration group.
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or integrationGroupsConfig parameter.
     */
    public VoidResponse setIntegrationGroupsConfig(String                       userId,
                                                   String                       serverName,
                                                   List<IntegrationGroupConfig> integrationGroupsConfig)
    {
        final String methodName = "setIntegrationGroupsConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = storeIntegrationGroupsConfig(userId, serverName, null, integrationGroupsConfig, methodName);

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Disable the integration groups.  This removes all configuration for the integration groups
     * and disables the enterprise repository services.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    public VoidResponse clearAllIntegrationGroups(String userId,
                                                  String serverName)
    {
        final String methodName = "clearAllIntegrationGroups";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = this.storeIntegrationGroupsConfig(userId, serverName, null, null, methodName);

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove an integration group.  This removes all configuration for the integration group.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param groupId integration group id
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    public VoidResponse clearIntegrationGroup(String userId,
                                              String serverName,
                                              String groupId)
    {
        final String methodName = "clearIntegrationGroup";

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

            List<IntegrationGroupConfig> currentList = serverConfig.getDynamicIntegrationGroupsConfig();
            List<IntegrationGroupConfig> newList     = new ArrayList<>();

            if (currentList != null)
            {
                for (IntegrationGroupConfig existingConfig : currentList)
                {
                    if (existingConfig != null)
                    {
                        if (! groupId.equals(existingConfig.getIntegrationGroupQualifiedName()))
                        {
                            newList.add(existingConfig);
                        }
                    }
                }

                response = this.storeIntegrationGroupsConfig(userId, serverName, groupId, newList, methodName);
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
     * Store the latest set of integration groups in the configuration document for the server.
     *
     * @param userId                     user that is issuing the request.
     * @param serverName                 local server name.
     * @param groupQualifiedName         identifier of specific integration group
     * @param integrationGroupsConfig    list of configuration properties for each integration group.
     * @param methodName                 calling method
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or integrationGroupsConfig parameter.
     */
    private VoidResponse storeIntegrationGroupsConfig(String                       userId,
                                                      String                       serverName,
                                                      String                       groupQualifiedName,
                                                      List<IntegrationGroupConfig> integrationGroupsConfig,
                                                      String                       methodName)
    {
        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            if (serverConfig.getRepositoryServicesConfig() == null)
            {
                OMRSConfigurationFactory omrsConfigurationFactory = new OMRSConfigurationFactory();

                serverConfig.setRepositoryServicesConfig(omrsConfigurationFactory.getDefaultRepositoryServicesConfig());
            }

            List<String>  configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if (integrationGroupsConfig == null)
            {
                configAuditTrail.add(new Date() + " " + userId + " removed configuration for integration groups.");
            }
            else if (groupQualifiedName == null)
            {
                configAuditTrail.add(new Date() + " " + userId + " updated configuration for integration groups.");
            }
            else
            {
                configAuditTrail.add(new Date() + " " + userId + " updated configuration for integration group " + groupQualifiedName + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            serverConfig.setDynamicIntegrationGroupsConfig(integrationGroupsConfig);

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

        return response;
    }
    
    
    


    /**
     * Return the list of integration services that are configured for this server.  If you want to see the configuration for these services,
     * use the getIntegrationServicesConfiguration.
     *
     * @param userId calling user
     * @param serverName name of server
     *
     * @return list of integration service descriptions
     */
    public RegisteredOMAGServicesResponse getRegisteredIntegrationServices(String userId,
                                                                           String serverName)
    {
        final String methodName = "getRegisteredIntegrationServices";

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
             * Get the list of View Services configured in this server.
             */
            List<IntegrationServiceConfig> integrationServiceConfigs = serverConfig.getIntegrationServicesConfig();

            /*
             * Set up the available view services.
             */
            if ((integrationServiceConfigs != null) && (!integrationServiceConfigs.isEmpty()))
            {
                List<RegisteredOMAGService> services = new ArrayList<>();
                for (IntegrationServiceConfig integrationServiceConfig : integrationServiceConfigs)
                {
                    if (integrationServiceConfig != null)
                    {
                        RegisteredOMAGService service = new RegisteredOMAGService();

                        service.setServiceId(integrationServiceConfig.getIntegrationServiceId());
                        service.setServiceName(integrationServiceConfig.getIntegrationServiceFullName());
                        service.setServiceDevelopmentStatus(integrationServiceConfig.getIntegrationServiceDevelopmentStatus());
                        service.setServiceDescription(integrationServiceConfig.getIntegrationServiceDescription());
                        service.setServiceURLMarker(integrationServiceConfig.getIntegrationServiceURLMarker());
                        service.setServiceWiki(integrationServiceConfig.getIntegrationServiceWiki());
                        service.setServerType(ServerTypeClassification.INTEGRATION_DAEMON.getServerTypeName());
                        service.setPartnerServiceName(integrationServiceConfig.getIntegrationServicePartnerOMAS());
                        service.setPartnerServerType(ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName());
                        services.add(service);
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
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of integration services that are configured for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     *
     * @return list of integration service configurations
     */
    public IntegrationServicesResponse getIntegrationServicesConfiguration(String userId,
                                                                           String serverName)
    {
        final String methodName = "getIntegrationServicesConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationServicesResponse response = new IntegrationServicesResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            /*
             * Get the list of Integration Services configured in this server.
             */
            response.setServices(serverConfig.getIntegrationServicesConfig());
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
     * Return the configuration for the requested integration service that is configured for this server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker integration service name used in URL
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serviceURLMarker parameter.
     */
    public IntegrationServiceConfigResponse getIntegrationServiceConfiguration(String userId,
                                                                               String serverName,
                                                                               String serviceURLMarker)
    {
        final String methodName = "getIntegrationServiceConfiguration";
        final String serviceURLMarkerParameterName = "serviceURLMarker";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationServiceConfigResponse response = new IntegrationServiceConfigResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validatePropertyNotNull(serviceURLMarker, serviceURLMarkerParameterName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<IntegrationServiceConfig> currentList = serverConfig.getIntegrationServicesConfig();

            if (currentList != null)
            {
                for (IntegrationServiceConfig existingConfig : currentList)
                {
                    if (existingConfig != null)
                    {
                        if (serviceURLMarker.equals(existingConfig.getIntegrationServiceURLMarker()))
                        {
                            response.setConfig(existingConfig);
                        }
                    }
                }
            }

            if (response.getConfig() == null)
            {
                response.setConfig(IntegrationServiceRegistry.getIntegrationServiceConfig(serviceURLMarker, serverName, methodName));
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
     * Enable a single registered integration service.  This builds the integration service configuration for the
     * server's config document.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker integration service name used in URL
     * @param requestBody  minimum values to configure an integration service
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureIntegrationService(String                        userId,
                                                    String                        serverName,
                                                    String                        serviceURLMarker,
                                                    IntegrationServiceRequestBody requestBody)
    {
        final String methodName = "configureIntegrationService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate the incoming parameters
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateIntegrationServiceConfig(serverName, requestBody, methodName);

            /*
             * Get the configuration information for this integration service.
             */
            IntegrationServiceConfig serviceConfig = IntegrationServiceRegistry.getIntegrationServiceConfig(serviceURLMarker, serverName, methodName);
            serviceConfig.setIntegrationServiceOperationalStatus(ServiceOperationalStatus.ENABLED);

            serviceConfig.setOMAGServerPlatformRootURL(requestBody.getOMAGServerPlatformRootURL());
            serviceConfig.setOMAGServerName(requestBody.getOMAGServerName());
            serviceConfig.setIntegrationConnectorConfigs(requestBody.getIntegrationConnectorConfigs());
            serviceConfig.setIntegrationServiceOptions(requestBody.getIntegrationServiceOptions());

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<IntegrationServiceConfig> integrationServiceConfigList = serverConfig.getIntegrationServicesConfig();

            response = this.storeIntegrationServicesConfig(userId,
                                                           serverName,
                                                           serviceURLMarker,
                                                           updateIntegrationServiceConfig(serviceConfig, integrationServiceConfigList),
                                                           methodName);
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
     * Enable all non-deprecated registered integration services.  This builds the integration service configuration for the
     * server's config document.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param requestBody  minimum values to configure an integration service
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureAllIntegrationServices(String                        userId,
                                                        String                        serverName,
                                                        IntegrationServiceRequestBody requestBody)
    {
        final String methodName = "configureAllIntegrationService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate the incoming parameters
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateIntegrationServiceConfig(serverName, requestBody, methodName);

            /*
             * Get the configuration information for this integration service.
             */
            List<String> registeredIntegrationServicesURLMarkers = IntegrationServiceRegistry.getRegisteredServiceURLMarkers();
            List<IntegrationServiceConfig> serviceConfigs = new ArrayList<>();

            if (registeredIntegrationServicesURLMarkers != null)
            {
                for (String serviceURLMarker : registeredIntegrationServicesURLMarkers)
                {
                    IntegrationServiceConfig serviceConfig = IntegrationServiceRegistry.getIntegrationServiceConfig(serviceURLMarker, serverName, methodName);

                    if (serviceConfig.getIntegrationServiceDevelopmentStatus() != ComponentDevelopmentStatus.DEPRECATED)
                    {
                        serviceConfig.setOMAGServerPlatformRootURL(requestBody.getOMAGServerPlatformRootURL());
                        serviceConfig.setOMAGServerName(requestBody.getOMAGServerName());
                        serviceConfig.setIntegrationConnectorConfigs(requestBody.getIntegrationConnectorConfigs());
                        serviceConfig.setIntegrationServiceOptions(requestBody.getIntegrationServiceOptions());

                        serviceConfigs.add(serviceConfig);
                    }
                }
            }

            response = this.storeIntegrationServicesConfig(userId,
                                                           serverName,
                                                           null,
                                                           serviceConfigs,
                                                           methodName);
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
     * Add configuration for a single integration service to the server's config document.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceConfig  all values to configure an integration service
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureIntegrationService(String                   userId,
                                                    String                   serverName,
                                                    IntegrationServiceConfig serviceConfig)
    {
        final String methodName = "configureIntegrationService";

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

            List<IntegrationServiceConfig> integrationServiceConfigList = serverConfig.getIntegrationServicesConfig();

            response = this.storeIntegrationServicesConfig(userId,
                                                           serverName,
                                                           serviceConfig.getIntegrationServiceURLMarker(),
                                                           updateIntegrationServiceConfig(serviceConfig, integrationServiceConfigList),
                                                           methodName);
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
     * Add/update the configuration for a single service in the configuration.
     *
     * @param integrationServiceConfig configuration to add/change
     * @param currentList current config (may be null)
     * @return updated list
     */
    private List<IntegrationServiceConfig>  updateIntegrationServiceConfig(IntegrationServiceConfig         integrationServiceConfig,
                                                                           List<IntegrationServiceConfig>   currentList)
    {
        if (integrationServiceConfig == null)
        {
            return currentList;
        }
        else
        {
            List<IntegrationServiceConfig> newList = new ArrayList<>();

            if (currentList != null)
            {
                for (IntegrationServiceConfig existingConfig : currentList)
                {
                    if (existingConfig != null)
                    {
                        if (integrationServiceConfig.getIntegrationServiceId() != existingConfig.getIntegrationServiceId())
                        {
                            newList.add(existingConfig);
                        }
                    }
                }
            }

            newList.add(integrationServiceConfig);

            return newList;
        }
    }


    /**
     * Set up the configuration for all the open metadata integration services (OMISs).  This overrides
     * the current values.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param integrationServicesConfig  list of configuration properties for each integration service.
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or integrationServicesConfig parameter.
     */
    public VoidResponse setIntegrationServicesConfig(String                         userId,
                                                     String                         serverName,
                                                     List<IntegrationServiceConfig> integrationServicesConfig)
    {
        final String methodName = "setIntegrationServicesConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = storeIntegrationServicesConfig(userId, serverName, null, integrationServicesConfig, methodName);

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Disable the integration services.  This removes all configuration for the integration services
     * and disables the enterprise repository services.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    public VoidResponse clearAllIntegrationServices(String userId,
                                                    String serverName)
    {
        final String methodName = "clearAllIntegrationServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = this.storeIntegrationServicesConfig(userId, serverName, null, null, methodName);

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove an integration service.  This removes all configuration for the integration service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker integration service name used in URL
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    public VoidResponse clearIntegrationService(String userId,
                                                String serverName,
                                                String serviceURLMarker)
    {
        final String methodName = "clearIntegrationService";

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

            List<IntegrationServiceConfig> currentList = serverConfig.getIntegrationServicesConfig();
            List<IntegrationServiceConfig> newList     = new ArrayList<>();

            if (currentList != null)
            {
                for (IntegrationServiceConfig existingConfig : currentList)
                {
                    if (existingConfig != null)
                    {
                        if (! serviceURLMarker.equals(existingConfig.getIntegrationServiceURLMarker()))
                        {
                            newList.add(existingConfig);
                        }
                    }
                }

                response = this.storeIntegrationServicesConfig(userId, serverName, serviceURLMarker, newList, methodName);
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
     * Store the latest set of integration services in the configuration document for the server.
     *
     * @param userId                     user that is issuing the request.
     * @param serverName                 local server name.
     * @param serviceURLMarker           identifier of specific integration service
     * @param integrationServicesConfig  list of configuration properties for each integration service.
     * @param methodName                 calling method
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or integrationServicesConfig parameter.
     */
    private VoidResponse storeIntegrationServicesConfig(String                         userId,
                                                        String                         serverName,
                                                        String                         serviceURLMarker,
                                                        List<IntegrationServiceConfig> integrationServicesConfig,
                                                        String                         methodName)
    {
        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            if (serverConfig.getRepositoryServicesConfig() == null)
            {
                OMRSConfigurationFactory omrsConfigurationFactory = new OMRSConfigurationFactory();

                serverConfig.setRepositoryServicesConfig(omrsConfigurationFactory.getDefaultRepositoryServicesConfig());
            }

            List<String>  configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if (integrationServicesConfig == null)
            {
                configAuditTrail.add(new Date() + " " + userId + " removed configuration for integration services.");
            }
            else if (serviceURLMarker == null)
            {
                configAuditTrail.add(new Date() + " " + userId + " updated configuration for integration services.");
            }
            else
            {
                configAuditTrail.add(new Date() + " " + userId + " updated configuration for integration service " + serviceURLMarker + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            serverConfig.setIntegrationServicesConfig(integrationServicesConfig);

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

        return response;
    }


    /**
     * Return the integration daemon services configuration including the list of integration services and
     * integration services that are configured for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     *
     * @return integration daemon services configuration
     */
    public IntegrationDaemonServicesResponse getIntegrationDaemonServicesConfiguration(String userId,
                                                                                       String serverName)
    {
        final String methodName = "getIntegrationDaemonServicesConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        IntegrationDaemonServicesResponse response = new IntegrationDaemonServicesResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            /*
             * Get the list of Integration Groups and Services configured in this server.
             */
            IntegrationDaemonServicesConfig integrationDaemonServicesConfig = new IntegrationDaemonServicesConfig();

            integrationDaemonServicesConfig.setDynamicIntegrationGroupsConfig(serverConfig.getDynamicIntegrationGroupsConfig());
            integrationDaemonServicesConfig.setIntegrationServicesConfig(serverConfig.getIntegrationServicesConfig());

            response.setServices(integrationDaemonServicesConfig);
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
     * Set up the configuration for an Integration Daemon OMAG Server in a single call.  This overrides the current values.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param servicesConfig full configuration for the integration daemon server.
     * @return void response
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse setIntegrationDaemonServicesConfig(String userId, String serverName, IntegrationDaemonServicesConfig servicesConfig)
    {
        final String methodName                       = "setIntegrationDaemonServicesConfig";
        final String serviceConfigParameterName       = "servicesConfig";

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
             * Just check the config is not null (use clear to remove config completely.)
             * However, it is not assumed that the supplied config is complete.  Therefore, there may be missing
             * client information - this is an advanced service, so we assume the caller knows what they are doing.
             */
            errorHandler.validatePropertyNotNull(servicesConfig, serviceConfigParameterName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            if (serverConfig != null)
            {
                serverConfig.setDynamicIntegrationGroupsConfig(servicesConfig.getDynamicIntegrationGroupsConfig());
                serverConfig.setIntegrationServicesConfig(servicesConfig.getIntegrationServicesConfig());

                this.configStore.saveServerConfig(serverName, methodName, serverConfig);
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
     * Remove the configuration for an Integration Daemon OMAG Server in a single call.  This overrides the current values.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse clearIntegrationDaemonServicesConfig(String userId, String serverName)
    {
        final String methodName = "clearIntegrationDaemonServicesConfig";

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

            if (serverConfig != null)
            {
                serverConfig.setDynamicIntegrationGroupsConfig(null);
                serverConfig.setIntegrationServicesConfig(null);

                this.configStore.saveServerConfig(serverName, methodName, serverConfig);
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
}
