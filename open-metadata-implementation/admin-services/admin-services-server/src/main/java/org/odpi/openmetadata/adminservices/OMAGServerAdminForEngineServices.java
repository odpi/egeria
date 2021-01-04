/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;


import org.odpi.openmetadata.adminservices.configuration.OMAGEngineServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.EngineServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.EngineServiceRequestBody;
import org.odpi.openmetadata.adminservices.rest.EngineServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OMAGServerAdminForEngineServices provides the server-side support for the services that add engine services
 * configuration to an OMAG Server.
 */
public class OMAGServerAdminForEngineServices
{
    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminForEngineServices.class),
                                                                      CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceName());
    

    private OMAGServerAdminStoreServices   configStore = new OMAGServerAdminStoreServices();
    private OMAGServerErrorHandler         errorHandler = new OMAGServerErrorHandler();
    private OMAGServerExceptionHandler     exceptionHandler = new OMAGServerExceptionHandler();


    /**
     * Default constructor
     */
    public OMAGServerAdminForEngineServices()
    {
    }


    /**
     * Return the list of engine services that are configured for this server.  If you want to see the configuration for these services,
     * use the getEngineServicesConfiguration.
     *
     * @param userId calling user
     * @param serverName name of server
     *
     * @return list of engine service descriptions
     */
    public RegisteredOMAGServicesResponse getRegisteredEngineServices(String userId,
                                                                      String serverName)
    {
        final String methodName = "getRegisteredEngineServices";

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
             * Get the list of Engine Services configured in this server.
             */
            List<EngineServiceConfig> engineServiceConfigs = serverConfig.getEngineServicesConfig();

            /*
             * Set up the available view services.
             */
            if ((engineServiceConfigs != null) && (!engineServiceConfigs.isEmpty()))
            {
                List<RegisteredOMAGService> services = new ArrayList<>();
                for (EngineServiceConfig engineServiceConfig : engineServiceConfigs)
                {
                    if (engineServiceConfig != null)
                    {
                        if (engineServiceConfig.getEngineServiceOperationalStatus() == ServiceOperationalStatus.ENABLED)
                        {
                            RegisteredOMAGService service = new RegisteredOMAGService();
                            service.setServiceName(engineServiceConfig.getEngineServiceFullName());
                            service.setServiceDescription(engineServiceConfig.getEngineServiceDescription());
                            service.setServiceURLMarker(engineServiceConfig.getEngineServiceURLMarker());
                            service.setServiceWiki(engineServiceConfig.getEngineServiceWiki());
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
        catch (Throwable error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of engine services that are configured for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     *
     * @return list of access service configurations
     */
    public EngineServicesResponse getEngineServicesConfiguration(String userId,
                                                                 String serverName)
    {
        final String methodName = "getEngineServicesConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EngineServicesResponse response = new EngineServicesResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            /*
             * Get the list of Engine Services configured in this server.
             */
            response.setServices(serverConfig.getEngineServicesConfig());
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the configuration for the requested engine service that is configured for this server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker engine service name used in URL
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serviceURLMarker parameter.
     */
    public EngineServiceConfigResponse getEngineServiceConfiguration(String userId,
                                                                     String serverName,
                                                                     String serviceURLMarker)
    {
        final String methodName = "getEngineServiceConfiguration";
        final String serviceURLMarkerParameterName = "serviceURLMarker";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EngineServiceConfigResponse response = new EngineServiceConfigResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validatePropertyNotNull(serviceURLMarker, serviceURLMarkerParameterName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<EngineServiceConfig> currentList = serverConfig.getEngineServicesConfig();

            if (currentList != null)
            {
                for (EngineServiceConfig existingConfig : currentList)
                {
                    if (existingConfig != null)
                    {
                        if (serviceURLMarker.equals(existingConfig.getEngineServiceURLMarker()))
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
        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Enable a single registered engine service.  This builds the engine service configuration for the
     * server's config document.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker engine service name used in URL
     * @param requestBody  minimum values to configure an engine service
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureEngineService(String                   userId,
                                               String                   serverName,
                                               String                   serviceURLMarker,
                                               EngineServiceRequestBody requestBody)
    {
        final String methodName = "configureEngineService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate the incoming parameters
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateOMAGServerClientConfig(serverName, requestBody, methodName);

            /*
             * Get the configuration information for this engine service.
             */
            EngineServiceRegistration registration = OMAGEngineServiceRegistration.getEngineServiceRegistration(serviceURLMarker);

            errorHandler.validateEngineServiceIsRegistered(registration, serviceURLMarker, serverName, methodName);

            EngineServiceConfig serviceConfig = new EngineServiceConfig(registration);

            serviceConfig.setOMAGServerPlatformRootURL(requestBody.getOMAGServerPlatformRootURL());
            serviceConfig.setOMAGServerName(requestBody.getOMAGServerName());
            serviceConfig.setEngines(requestBody.getEngines());
            serviceConfig.setEngineServiceOptions(requestBody.getEngineServiceOptions());

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<EngineServiceConfig> engineServiceConfigList = serverConfig.getEngineServicesConfig();

            response = this.storeEngineServicesConfig(userId,
                                                      serverName,
                                                      serviceURLMarker,
                                                      updateEngineServiceConfig(serviceConfig, engineServiceConfigList),
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
        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Add configuration for a single engine service to the server's config document.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceConfig  all values to configure an engine service
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureEngineService(String              userId,
                                               String              serverName,
                                               EngineServiceConfig serviceConfig)
    {
        final String methodName                    = "configureEngineService";
        final String serviceConfigParameterName    = "serviceConfig";
        final String serviceURLMarkerParameterName = "serviceConfig.serviceURLMarker";
        final String engineNamesParameterName      = "serviceConfig.engineNames";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validatePropertyNotNull(serviceConfig, serviceConfigParameterName, serverName, methodName);
            errorHandler.validatePropertyNotNull(serviceConfig.getEngineServiceURLMarker(), serviceURLMarkerParameterName, serverName, methodName);
            errorHandler.validatePropertyNotNull(serviceConfig.getEngines(), engineNamesParameterName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            EngineServiceRegistration registration = OMAGEngineServiceRegistration.getEngineServiceRegistration(serviceConfig.getEngineServiceURLMarker());

            errorHandler.validateEngineServiceIsRegistered(registration, serviceConfig.getEngineServiceURLMarker(), serverName, methodName);

            List<EngineServiceConfig> engineServiceConfigList = serverConfig.getEngineServicesConfig();

            response = this.storeEngineServicesConfig(userId,
                                                      serverName,
                                                      serviceConfig.getEngineServiceURLMarker(),
                                                      updateEngineServiceConfig(serviceConfig, engineServiceConfigList),
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
        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Add/update the configuration for a single service in the configuration.
     *
     * @param engineServiceConfig configuration to add/change
     * @param currentList current config (may be null)
     * @return updated list
     */
    private List<EngineServiceConfig>  updateEngineServiceConfig(EngineServiceConfig         engineServiceConfig,
                                                                 List<EngineServiceConfig>   currentList)
    {
        if (engineServiceConfig == null)
        {
            return currentList;
        }
        else
        {
            List<EngineServiceConfig> newList = new ArrayList<>();

            if (currentList != null)
            {
                for (EngineServiceConfig existingConfig : currentList)
                {
                    if (existingConfig != null)
                    {
                        if (engineServiceConfig.getEngineServiceId() != existingConfig.getEngineServiceId())
                        {
                            newList.add(existingConfig);
                        }
                    }
                }
            }

            newList.add(engineServiceConfig);

            if (newList.isEmpty())
            {
                return null;
            }

            return newList;
        }
    }


    /**
     * Set up the configuration for all of the open metadata engine services (OMESs).  This overrides
     * the current values.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param engineServicesConfig  list of configuration properties for each engine service.
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or engineServicesConfig parameter.
     */
    public VoidResponse setEngineServicesConfig(String                    userId,
                                                String                    serverName,
                                                List<EngineServiceConfig> engineServicesConfig)
    {
        final String methodName = "setEngineServicesConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = storeEngineServicesConfig(userId, serverName, null, engineServicesConfig, methodName);

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Disable the engine services.  This removes all configuration for the engine services
     * and disables the enterprise repository services.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    public VoidResponse clearAllEngineServices(String userId,
                                               String serverName)
    {
        final String methodName = "clearAllEngineServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = this.storeEngineServicesConfig(userId, serverName, null, null, methodName);

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove an engine service.  This removes all configuration for the engine service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker engine service name used in URL
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    public VoidResponse clearEngineService(String userId,
                                           String serverName,
                                           String serviceURLMarker)
    {
        final String methodName = "clearEngineService";

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

            List<EngineServiceConfig> currentList = serverConfig.getEngineServicesConfig();
            List<EngineServiceConfig> newList     = new ArrayList<>();

            if (currentList != null)
            {
                for (EngineServiceConfig existingConfig : currentList)
                {
                    if (existingConfig != null)
                    {
                        if (! serviceURLMarker.equals(existingConfig.getEngineServiceURLMarker()))
                        {
                            newList.add(existingConfig);
                        }
                    }
                }

                response = this.storeEngineServicesConfig(userId, serverName, serviceURLMarker, newList, methodName);
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
        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Store the latest set of engine services in the configuration document for the server.
     *
     * @param userId                     user that is issuing the request.
     * @param serverName                 local server name.
     * @param serviceURLMarker           identifier of specific engine service
     * @param engineServicesConfig  list of configuration properties for each engine service.
     * @param methodName                 calling method
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or engineServicesConfig parameter.
     */
    private VoidResponse storeEngineServicesConfig(String                    userId,
                                                   String                    serverName,
                                                   String                    serviceURLMarker,
                                                   List<EngineServiceConfig> engineServicesConfig,
                                                   String                    methodName)
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

            if (engineServicesConfig == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for engine services.");
            }
            else if (serviceURLMarker == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for engine services.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId +
                                             " updated configuration for engine service " + serviceURLMarker + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            serverConfig.setEngineServicesConfig(engineServicesConfig);

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
        catch (Throwable  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        return response;
    }
}
