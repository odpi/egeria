/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.OMAGViewServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.SolutionViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceRegistration;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.ViewServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.ViewServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OMAGServerAdminForViewServices provides the server-side support for the services that add view services
 * configuration to an OMAG Server.
 */
public class OMAGServerAdminForViewServices
{
    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminForViewServices.class),
                                                                      CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceName());

    private OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();
    private OMAGServerErrorHandler errorHandler = new OMAGServerErrorHandler();
    private OMAGServerExceptionHandler exceptionHandler = new OMAGServerExceptionHandler();


    /**
     * Default constructor
     */
    public OMAGServerAdminForViewServices()
    {
    }


    /**
     * Return the list of view services that are configured for this server.
     *
     * @param userId     calling user
     * @param serverName name of server
     * @return list of view service descriptions
     */
    public RegisteredOMAGServicesResponse getConfiguredViewServices(String userId,
                                                                    String serverName)
    {
        final String methodName = "getConfiguredViewServices";

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
            List<ViewServiceConfig> viewServiceConfigList = serverConfig.getViewServicesConfig();

            /*
             * Set up the available view services.
             */
            if ((viewServiceConfigList != null) && (!viewServiceConfigList.isEmpty()))
            {
                List<RegisteredOMAGService> services = new ArrayList<>();
                for (ViewServiceConfig viewServiceConfig : viewServiceConfigList)
                {
                    if (viewServiceConfig != null)
                    {
                        if (viewServiceConfig.getViewServiceOperationalStatus() == ServiceOperationalStatus.ENABLED)
                        {
                            RegisteredOMAGService service = new RegisteredOMAGService();

                            service.setServiceId(viewServiceConfig.getViewServiceId());
                            service.setServiceName(viewServiceConfig.getViewServiceFullName());
                            service.setServiceDevelopmentStatus(viewServiceConfig.getViewServiceDevelopmentStatus());
                            service.setServiceDescription(viewServiceConfig.getViewServiceDescription());
                            service.setServiceURLMarker(viewServiceConfig.getViewServiceURLMarker());
                            service.setServiceWiki(viewServiceConfig.getViewServiceWiki());
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
     * Return the view services configuration for this server.
     *
     * @param userId     calling user
     * @param serverName name of server
     * @return view services response
     */
    public ViewServicesResponse getViewServicesConfiguration(String userId,
                                                             String serverName)
    {
        final String methodName = "getViewServicesConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ViewServicesResponse response = new ViewServicesResponse();

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
            List<ViewServiceConfig> viewServiceConfigList = serverConfig.getViewServicesConfig();

            /*
             * Set up the available view services.
             */
            if ((viewServiceConfigList != null) && (!viewServiceConfigList.isEmpty()))
            {
                List<ViewServiceConfig> services = new ArrayList<>();

                for (ViewServiceConfig viewServiceConfig : viewServiceConfigList)
                {
                    if (viewServiceConfig != null)
                    {
                        if (viewServiceConfig.getViewServiceOperationalStatus() == ServiceOperationalStatus.ENABLED)
                        {
                            services.add(viewServiceConfig);
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
     * Return the configuration of a single view service
     *
     * @param userId     calling user
     * @param serverName name of server
     * @param serviceURLMarker server URL marker identifying the view service
     * @return view services response
     */
    public ViewServiceConfigResponse getViewServiceConfig(String userId,
                                                          String serverName,
                                                          String serviceURLMarker)
    {

        final String methodName = "getViewServiceConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ViewServiceConfigResponse response = new ViewServiceConfigResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<ViewServiceConfig> currentList = serverConfig.getViewServicesConfig();

            if (currentList != null)
            {
                for (ViewServiceConfig existingConfig : currentList)
                {
                    if (existingConfig != null)
                    {
                        if (serviceURLMarker.equals(existingConfig.getViewServiceURLMarker()))
                        {
                            response.setConfig(existingConfig);
                        }
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
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }




    /**
     * Configure a single view service.
     *
     * @param userId             user that is issuing the request.
     * @param serverName         local server name.
     * @param serviceURLMarker   view service name used in URL
     * @param requestedViewServiceConfig  view service config
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureViewService(String            userId,
                                             String            serverName,
                                             String            serviceURLMarker,
                                             ViewServiceConfig requestedViewServiceConfig)
    {
        final String methodName = "configureViewService";

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
             * If the view service is NOT an integration view service then validate the client confguration.
             * An integration view service does not connect to a specific OMAG server, it uses resource endpoints instead.
             */
            if ( !(requestedViewServiceConfig instanceof IntegrationViewServiceConfig) ) {
                errorHandler.validateOMAGServerClientConfig(serverName, requestedViewServiceConfig, methodName);
            }

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<ViewServiceConfig> viewServiceConfigList = serverConfig.getViewServicesConfig();


            /*
             * Get the registration information for this view service.
             */
            ViewServiceRegistration viewServiceRegistration = OMAGViewServiceRegistration.getViewServiceRegistration(serviceURLMarker);

            errorHandler.validateViewServiceIsRegistered(viewServiceRegistration, serviceURLMarker, serverName, methodName);

            viewServiceConfigList = this.updateViewServiceConfig(createViewServiceConfig(viewServiceRegistration,
                                                                                         requestedViewServiceConfig),
                                                                 viewServiceConfigList);

            this.storeViewServicesConfig(userId, serverName, serviceURLMarker, viewServiceConfigList, methodName);


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
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Enable all view services that are registered with this server platform.   The configuration properties
     * for each view service can be changed from their default using setViewServicesConfig operation.
     *
     * @param userId       user that is issuing the request.
     * @param serverName   local server name.
     * @param requestedViewServiceConfig  requested View Service Config containing the OMAGServerName and OMAGServerRootPlatformURL
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureAllViewServices(String            userId,
                                                 String            serverName,
                                                 ViewServiceConfig requestedViewServiceConfig)
    {
        final String methodName = "configureViewServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateOMAGServerClientConfig(serverName, requestedViewServiceConfig, methodName);

            List<ViewServiceConfig> viewServiceConfigList = new ArrayList<>();

            /*
             * Get the list of View Services implemented in this server.
             */
            List<ViewServiceRegistration> viewServiceRegistrationList = OMAGViewServiceRegistration.getViewServiceRegistrationList();

            /*
             * Set up the available view services.
             */
            if ((viewServiceRegistrationList != null) && (! viewServiceRegistrationList.isEmpty()))
            {
                for (ViewServiceRegistration registration : viewServiceRegistrationList)
                {
                    if (registration != null)
                    {
                        if (registration.getViewServiceOperationalStatus() == ServiceOperationalStatus.ENABLED)
                        {
                            viewServiceConfigList.add(createViewServiceConfig(registration, requestedViewServiceConfig));
                        }
                    }
                }
            }

            if (viewServiceConfigList.isEmpty())
            {
                viewServiceConfigList = null;
            }

            this.storeViewServicesConfig(userId, serverName, null, viewServiceConfigList, methodName);
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
     * Set up the configuration for a single view service.
     *
     * @param registration registration information about the service.
     * @param requestedViewServiceConfig requested view service config
     * @return newly created config object
     */
    private ViewServiceConfig createViewServiceConfig(ViewServiceRegistration    registration,
                                                      ViewServiceConfig          requestedViewServiceConfig)
    {
        ViewServiceConfig viewServiceConfig;

        if (requestedViewServiceConfig instanceof IntegrationViewServiceConfig)
        {
            /*
             * The requested configuration is for an Integration View Service
             */
            IntegrationViewServiceConfig requestedIntegrationViewServiceConfig = (IntegrationViewServiceConfig)requestedViewServiceConfig;
            IntegrationViewServiceConfig createdViewServiceConfig = new IntegrationViewServiceConfig(registration);
            createdViewServiceConfig.setResourceEndpoints(requestedIntegrationViewServiceConfig.getResourceEndpoints());
            viewServiceConfig = createdViewServiceConfig;
            // some integration services require the OMAGServerPlatformRootURL
            createdViewServiceConfig.setOMAGServerPlatformRootURL(requestedViewServiceConfig.getOMAGServerPlatformRootURL());
        }
        else if (requestedViewServiceConfig instanceof SolutionViewServiceConfig)
        {
            /*
             * The requested configuration is for a Solution View Service
             */
            SolutionViewServiceConfig requestedSolutionViewServiceConfig = (SolutionViewServiceConfig)requestedViewServiceConfig;
            SolutionViewServiceConfig createdViewServiceConfig = new SolutionViewServiceConfig(registration);
            createdViewServiceConfig.setOMAGServerPlatformRootURL(requestedSolutionViewServiceConfig.getOMAGServerPlatformRootURL());
            createdViewServiceConfig.setOMAGServerName(requestedSolutionViewServiceConfig.getOMAGServerName());
            viewServiceConfig =  createdViewServiceConfig;
        }
        else
        {
            /*
             * Assume that the requested configuration is a vanilla view service configuration
             */
            ViewServiceConfig createdViewServiceConfig = new ViewServiceConfig(registration);
            createdViewServiceConfig.setOMAGServerPlatformRootURL(requestedViewServiceConfig.getOMAGServerPlatformRootURL());
            createdViewServiceConfig.setOMAGServerName(requestedViewServiceConfig.getOMAGServerName());
            viewServiceConfig = createdViewServiceConfig;
        }

        /*
         * Always copy the view service options if any are present
         */
        Map<String, Object> viewOptions = requestedViewServiceConfig.getViewServiceOptions();
        viewServiceConfig.setViewServiceOptions(viewOptions);

        return viewServiceConfig;
    }


    /**
     * Add/update the configuration for a single service in the configuration.
     *
     * @param viewServiceConfig configuration to add/change
     * @param currentList       current config (may be null)
     * @return updated list
     */
    private List<ViewServiceConfig> updateViewServiceConfig(ViewServiceConfig       viewServiceConfig,
                                                            List<ViewServiceConfig> currentList)
    {
        if (viewServiceConfig == null)
        {
            return currentList;
        }
        else
        {
            List<ViewServiceConfig> newList = new ArrayList<>();

            if (currentList != null)
            {
                for (ViewServiceConfig existingConfig : currentList)
                {
                    if (existingConfig != null)
                    {
                        if (viewServiceConfig.getViewServiceId() != existingConfig.getViewServiceId())
                        {
                            newList.add(existingConfig);
                        }
                    }
                }
            }

            newList.add(viewServiceConfig);

            if (newList.isEmpty())
            {
                return null;
            }

            return newList;
        }
    }


    /**
     * Remove a view service.  This removes all configuration for the view service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker view service name used in URL
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    public VoidResponse clearViewService(String userId,
                                         String serverName,
                                         String serviceURLMarker)
    {
        final String methodName = "clearViewService";

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

            List<ViewServiceConfig> currentList = serverConfig.getViewServicesConfig();
            List<ViewServiceConfig> newList     = new ArrayList<>();

            if (currentList != null)
            {
                for (ViewServiceConfig existingConfig : currentList)
                {
                    if (existingConfig != null)
                    {
                        if (! serviceURLMarker.equals(existingConfig.getViewServiceURLMarker()))
                        {
                            newList.add(existingConfig);
                        }
                    }
                }

                serverConfig.setViewServicesConfig(newList);
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
     * Disable the view services.  This removes all configuration for the view services
     * and disables the enterprise repository services.
     *
     * @param userId     user that is issuing the request.
     * @param serverName local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    public VoidResponse clearAllViewServices(String userId,
                                             String serverName)
    {
        final String methodName = "clearAllViewServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            this.storeViewServicesConfig(userId, serverName, null, null, methodName);
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
     * Set up the configuration for all the open metadata view services (OMASs).  This overrides
     * the current values.
     *
     * @param userId             user that is issuing the request.
     * @param serverName         local server name.
     * @param viewServicesConfig list of configuration properties for each view service.
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or viewServicesConfig parameter.
     */
    public VoidResponse setViewServicesConfig(String                  userId,
                                              String                  serverName,
                                              List<ViewServiceConfig> viewServicesConfig)
    {
        final String methodName = "setViewServicesConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = storeViewServicesConfig(userId, serverName, null, viewServicesConfig, methodName);

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;

    }


    /**
     * Set up the configuration for all the open metadata view services (OMASs).  This overrides
     * the current values.
     *
     * @param userId             user that is issuing the request.
     * @param serverName         local server name.
     * @param serviceURLMarker   identifier of specific view service
     * @param viewServicesConfig list of configuration properties for each view service.
     * @param methodName         calling method
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or viewServicesConfig parameter.
     */
    private VoidResponse storeViewServicesConfig(String                  userId,
                                                 String                  serverName,
                                                 String                  serviceURLMarker,
                                                 List<ViewServiceConfig> viewServicesConfig,
                                                 String                  methodName)
    {

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

            if (viewServicesConfig == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for view services.");
            }
            else if (serviceURLMarker == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for view services.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for view service " + serviceURLMarker + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            serverConfig.setViewServicesConfig(viewServicesConfig);

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

        return response;
    }
}
