/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;

import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.odpi.openmetadata.adminservices.registration.OMAGViewServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceRegistrationEntry;
import org.odpi.openmetadata.adminservices.rest.ViewServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.ViewServiceRequestBody;
import org.odpi.openmetadata.adminservices.rest.ViewServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OMAGServerAdminForViewServices provides the server-side support for the services that add view services
 * configuration to an OMAG Server.
 */
public class OMAGServerAdminForViewServices extends TokenController
{
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminForViewServices.class),
                                                                            CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName());

    private final OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();
    private final        OMAGServerErrorHandler errorHandler         = new OMAGServerErrorHandler();
    private static final RESTExceptionHandler   restExceptionHandler = new RESTExceptionHandler();


    /**
     * Default constructor
     */
    public OMAGServerAdminForViewServices()
    {
    }


    /**
     * Return the list of view services that are configured for this server.
     *
     * @param serverName name of server
     * @return list of view service descriptions
     */
    public RegisteredOMAGServicesResponse getConfiguredViewServices(String serverName)
    {
        final String methodName = "getConfiguredViewServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, false, methodName);

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
                            service.setServerType(ServerTypeClassification.VIEW_SERVER.getServerTypeName());
                            service.setPartnerServiceName(viewServiceConfig.getViewServicePartnerService());
                            service.setPartnerServerType(ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName());
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the view services configuration for this server.
     *
     * @param serverName name of server
     * @return view services response
     */
    public ViewServicesResponse getViewServicesConfiguration(String serverName)
    {
        final String methodName = "getViewServicesConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ViewServicesResponse response = new ViewServicesResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, false, methodName);

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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Add the view services configuration for this server as a single call.
     * This operation is used for editing existing view service configuration.
     *
     * @param serverName name of server
     * @param viewServiceConfigs list of configured view services
     * @return void
     */
    public VoidResponse setViewServicesConfiguration(String                  serverName,
                                                     List<ViewServiceConfig> viewServiceConfigs)
    {
        final String methodName = "setViewServicesConfiguration";

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

            return this.storeViewServicesConfig(userId, serverName, null, viewServiceConfigs, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the configuration of a single view service
     *
     * @param serverName name of server
     * @param serviceURLMarker server URL marker identifying the view service
     * @return view services response
     */
    public ViewServiceConfigResponse getViewServiceConfig(String serverName,
                                                          String serviceURLMarker)
    {

        final String methodName = "getViewServiceConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ViewServiceConfigResponse response = new ViewServiceConfigResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, false, methodName);

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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Configure a single view service.
     *
     * @param serverName         local server name.
     * @param serviceURLMarker   view service name used in URL
     * @param requestBody  view service config
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * InvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureViewService(String                 serverName,
                                             String                 serviceURLMarker,
                                             ViewServiceRequestBody requestBody)
    {
        final String methodName = "configureViewService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName, server name and the name of the metadata access server to call.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateOMAGServerClientConfig(serverName, requestBody, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<ViewServiceConfig> viewServiceConfigList = serverConfig.getViewServicesConfig();

            /*
             * Get the registration information for this view service.
             */
            ViewServiceRegistrationEntry viewServiceRegistration = OMAGViewServiceRegistration.getViewServiceRegistration(serviceURLMarker);

            errorHandler.validateViewServiceIsRegistered(viewServiceRegistration, serviceURLMarker, serverName, methodName);

            viewServiceConfigList = this.updateViewServiceConfig(createViewServiceConfig(viewServiceRegistration,
                                                                                         requestBody),
                                                                 viewServiceConfigList);

            return this.storeViewServicesConfig(userId, serverName, serviceURLMarker, viewServiceConfigList, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Enable all view services that are registered with this server platform.   The configuration properties
     * for each view service can be changed from their default using setViewServicesConfig operation.
     *
     * @param serverName   local server name.
     * @param requestBody  requested View Service Config containing the OMAGServerName and OMAGServerRootPlatformURL,
     *                     view service options and resource endpoints
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * InvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureAllViewServices(String                 serverName,
                                                 ViewServiceRequestBody requestBody)
    {
        final String methodName = "configureAllViewServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);
        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateOMAGServerClientConfig(serverName, requestBody, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            List<ViewServiceConfig> viewServiceConfigList = new ArrayList<>();

            /*
             * Get the list of View Services implemented in this server.
             */
            List<ViewServiceRegistrationEntry> viewServiceRegistrationList = OMAGViewServiceRegistration.getViewServiceRegistrationList();

            /*
             * Set up the available view services.
             */
            if ((viewServiceRegistrationList != null) && (! viewServiceRegistrationList.isEmpty()))
            {
                for (ViewServiceRegistrationEntry registration : viewServiceRegistrationList)
                {
                    if (registration != null)
                    {
                        if (registration.getViewServiceDevelopmentStatus() != ComponentDevelopmentStatus.DEPRECATED)
                        {
                            viewServiceConfigList.add(createViewServiceConfig(registration, requestBody));
                        }
                    }
                }
            }

            if (viewServiceConfigList.isEmpty())
            {
                viewServiceConfigList = null;
            }

            return this.storeViewServicesConfig(userId, serverName, null, viewServiceConfigList, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the configuration for a single view service.
     *
     * @param registration registration information about the service.
     * @param requestBody requested view service config
     * @return newly created config object
     */
    private ViewServiceConfig createViewServiceConfig(ViewServiceRegistrationEntry registration,
                                                      ViewServiceRequestBody       requestBody)
    {
        ViewServiceConfig viewServiceConfig = new ViewServiceConfig(registration);

        viewServiceConfig.setOMAGServerPlatformRootURL(requestBody.getOMAGServerPlatformRootURL());
        viewServiceConfig.setOMAGServerName(requestBody.getOMAGServerName());
        viewServiceConfig.setSecretsStoreProvider(requestBody.getSecretsStoreProvider());
        viewServiceConfig.setSecretsStoreLocation(requestBody.getSecretsStoreLocation());
        viewServiceConfig.setSecretsStoreCollection(requestBody.getSecretsStoreCollection());
        viewServiceConfig.setViewServiceOptions(requestBody.getViewServiceOptions());

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
     * @param serverName  local server name.
     * @param serviceURLMarker view service name used in URL
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName  parameter.
     */
    public VoidResponse clearViewService(String serverName,
                                         String serviceURLMarker)
    {
        final String methodName = "clearViewService";

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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /**
     * Disable the view services.  This removes all configuration for the view services
     * and disables the enterprise repository services.
     *
     * @param serverName local server name.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName  parameter.
     */
    public VoidResponse clearAllViewServices(String serverName)
    {
        final String methodName = "clearAllViewServices";

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

            return this.storeViewServicesConfig(userId, serverName, null, null, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
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
     * @param serviceURLMarker   identifier of specific view service
     * @param viewServicesConfig list of configuration properties for each view service.
     * @param methodName         calling method
     * @return void response or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or viewServicesConfig parameter.
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

            if (serverConfig.getRepositoryServicesConfig() == null)
            {
                OMRSConfigurationFactory omrsConfigurationFactory = new OMRSConfigurationFactory();

                serverConfig.setRepositoryServicesConfig(omrsConfigurationFactory.getDefaultRepositoryServicesConfig());
            }

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if (viewServicesConfig == null)
            {
                configAuditTrail.add(new Date() + " " + userId + " removed configuration for view services.");
            }
            else if (serviceURLMarker == null)
            {
                configAuditTrail.add(new Date() + " " + userId + " updated configuration for view services.");
            }
            else
            {
                configAuditTrail.add(new Date() + " " + userId + " updated configuration for view service " + serviceURLMarker + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            serverConfig.setViewServicesConfig(viewServicesConfig);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        return response;
    }
}
