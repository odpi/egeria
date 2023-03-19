/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;


import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.rest.IntegrationServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.IntegrationServiceRequestBody;
import org.odpi.openmetadata.adminservices.rest.IntegrationServicesResponse;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceRegistry;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * OMAGServerAdminForIntegrationServices provides the server-side support for the services that add integration services
 * configuration to an OMAG Server.
 */
public class OMAGServerAdminForIntegrationServices
{
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminForIntegrationServices.class),
                                                                            CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceName());
    

    private final OMAGServerAdminStoreServices   configStore = new OMAGServerAdminStoreServices();
    private final OMAGServerErrorHandler         errorHandler = new OMAGServerErrorHandler();
    private final OMAGServerExceptionHandler     exceptionHandler = new OMAGServerExceptionHandler();


    /**
     * Default constructor
     */
    public OMAGServerAdminForIntegrationServices()
    {
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
                        if (integrationServiceConfig.getIntegrationServiceOperationalStatus() == ServiceOperationalStatus.ENABLED)
                        {
                            RegisteredOMAGService service = new RegisteredOMAGService();

                            service.setServiceId(integrationServiceConfig.getIntegrationServiceId());
                            service.setServiceName(integrationServiceConfig.getIntegrationServiceFullName());
                            service.setServiceDevelopmentStatus(integrationServiceConfig.getIntegrationServiceDevelopmentStatus());
                            service.setServiceDescription(integrationServiceConfig.getIntegrationServiceDescription());
                            service.setServiceURLMarker(integrationServiceConfig.getIntegrationServiceURLMarker());
                            service.setServiceWiki(integrationServiceConfig.getIntegrationServiceWiki());
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
}
