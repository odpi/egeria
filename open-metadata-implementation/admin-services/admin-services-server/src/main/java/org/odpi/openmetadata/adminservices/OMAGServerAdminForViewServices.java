/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.OMAGViewServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.*;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
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
public class OMAGServerAdminForViewServices {
    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminForViewServices.class),
                                                                      CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceName());

    private OMAGServerAdminStoreServices configStore = new OMAGServerAdminStoreServices();
    private OMAGServerErrorHandler errorHandler = new OMAGServerErrorHandler();
    private OMAGServerExceptionHandler exceptionHandler = new OMAGServerExceptionHandler();


    /**
     * Default constructor
     */
    public OMAGServerAdminForViewServices() {
    }


    /**
     * Return the list of view services that are configured for this server.
     *
     * @param userId     calling user
     * @param serverName name of server
     * @return list of view service descriptions
     */
    public RegisteredOMAGServicesResponse getConfiguredViewServices(String userId,
                                                                    String serverName) {
        final String methodName = "getConfiguredViewServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try {
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
            if ((viewServiceConfigList != null) && (!viewServiceConfigList.isEmpty())) {
                List<RegisteredOMAGService> services = new ArrayList<>();
                for (ViewServiceConfig viewServiceConfig : viewServiceConfigList) {
                    if (viewServiceConfig != null) {
                        if (viewServiceConfig.getViewServiceOperationalStatus() == ServiceOperationalStatus.ENABLED) {
                            RegisteredOMAGService service = new RegisteredOMAGService();
                            service.setServiceName(viewServiceConfig.getViewServiceFullName());
                            service.setServiceDescription(viewServiceConfig.getViewServiceDescription());
                            service.setServiceURLMarker(viewServiceConfig.getViewServiceURLMarker());
                            service.setServiceWiki(viewServiceConfig.getViewServiceWiki());
                            services.add(service);
                        }
                    }
                }
                if (!services.isEmpty()) {
                    response.setServices(services);
                }
            }
        } catch (OMAGInvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        } catch (OMAGNotAuthorizedException error) {
            exceptionHandler.captureNotAuthorizedException(response, error);
        } catch (Throwable error) {
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
     * @param clientConfig       URL root and server name that are used to access the downstream OMAG Server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureViewService(String userId,
                                             String serverName,
                                             String serviceURLMarker,
                                             OMAGServerClientConfig clientConfig) {
        final String methodName = "configureViewService";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateOMAGServerClientConfig(serverName, clientConfig,methodName);


            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<ViewServiceConfig> viewServiceConfigList = serverConfig.getViewServicesConfig();


            /*
             * Get the registration information for this view service.
             */
            ViewServiceRegistration viewServiceRegistration = OMAGViewServiceRegistration.getViewServiceRegistration(serviceURLMarker);

            errorHandler.validateViewServiceIsRegistered(viewServiceRegistration, serviceURLMarker, serverName, methodName);


            viewServiceConfigList = this.updateViewServiceConfig(createViewServiceConfig(viewServiceRegistration,
                                                                                         clientConfig),
                                                                 viewServiceConfigList);
            errorHandler.validateOMAGServerName(clientConfig.getOMAGServerName(),
                                                serverName,
                                                viewServiceRegistration.getViewServiceName());
            errorHandler.validateOMAGServerServiceRootURL(clientConfig.getOMAGServerPlatformRootURL(),
                                                          serverName,
                                                          viewServiceRegistration.getViewServiceName());
            this.setViewServicesConfig(userId, serverName, viewServiceConfigList);

        } catch (OMAGInvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        } catch (OMAGConfigurationErrorException error) {
            exceptionHandler.captureConfigurationErrorException(response, error);
        } catch (OMAGNotAuthorizedException error) {
            exceptionHandler.captureNotAuthorizedException(response, error);
        } catch (Throwable error) {
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
     * @param clientConfig       URL root and server name that are used to access the downstream OMAG Server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureAllViewServices(String userId,
                                                 String serverName,
                                                 OMAGServerClientConfig clientConfig) {
        final String methodName = "configureViewServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        VoidResponse response = new VoidResponse();

        try {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateOMAGServerClientConfig(serverName, clientConfig,methodName);


            List<ViewServiceConfig> viewServiceConfigList = new ArrayList<>();


            /*
             * Get the list of View Services implemented in this server.
             */
            List<ViewServiceRegistration> viewServiceRegistrationList = OMAGViewServiceRegistration.getViewServiceRegistrationList();

            /*
             * Set up the available view services.
             */
            if ((viewServiceRegistrationList != null) && (!viewServiceRegistrationList.isEmpty())) {
                for (ViewServiceRegistration registration : viewServiceRegistrationList) {
                    if (registration != null) {
                        if (registration.getViewServiceOperationalStatus() == ServiceOperationalStatus.ENABLED) {
                            errorHandler.validateOMAGServerName(clientConfig.getOMAGServerName(),
                                                         serverName,
                                                         registration.getViewServiceName());
                            errorHandler.validateOMAGServerServiceRootURL(clientConfig.getOMAGServerPlatformRootURL(),
                                                      serverName,
                                                     registration.getViewServiceName());

                            viewServiceConfigList.add(createViewServiceConfig(registration,
                                                                              clientConfig));
                        }
                    }
                }
            }

            if (viewServiceConfigList.isEmpty()) {
                viewServiceConfigList = null;
            }

            this.setViewServicesConfig(userId, serverName, viewServiceConfigList);
        } catch (OMAGInvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        } catch (OMAGNotAuthorizedException error) {
            exceptionHandler.captureNotAuthorizedException(response, error);
        } catch (Throwable error) {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the configuration for a single view service.
     *
     * @param registration registration information about the service.
     * @param clientConfig URL root and server name for the OMAG server that are used to access the downstream OMAG Server.
     * @return newly created config object
     */
    private ViewServiceConfig createViewServiceConfig(ViewServiceRegistration registration,
                                                      OMAGServerClientConfig clientConfig) {
        ViewServiceConfig viewServiceConfig = new ViewServiceConfig(registration);
        viewServiceConfig.setOMAGServerName(clientConfig.getOMAGServerName());
        viewServiceConfig.setOMAGServerPlatformRootURL(clientConfig.getOMAGServerPlatformRootURL());
        return viewServiceConfig;
    }


    /**
     * Add/update the configuration for a single service in the configuration.
     *
     * @param viewServiceConfig configuration to add/change
     * @param currentList       current config (may be null)
     * @return updated list
     */
    private List<ViewServiceConfig> updateViewServiceConfig(ViewServiceConfig viewServiceConfig,
                                                            List<ViewServiceConfig> currentList) {
        if (viewServiceConfig == null) {
            return currentList;
        } else {
            List<ViewServiceConfig> newList = new ArrayList<>();

            if (currentList != null) {
                for (ViewServiceConfig existingConfig : currentList) {
                    if (existingConfig != null) {
                        if (viewServiceConfig.getViewServiceId() != existingConfig.getViewServiceId()) {
                            newList.add(existingConfig);
                        }
                    }
                }
            }

            newList.add(viewServiceConfig);

            if (newList.isEmpty()) {
                return null;
            }

            return newList;
        }
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
                                             String serverName) {
        final String methodName = "disableViewServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            this.setViewServicesConfig(userId, serverName, null);
        } catch (OMAGInvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        } catch (OMAGNotAuthorizedException error) {
            exceptionHandler.captureNotAuthorizedException(response, error);
        } catch (Throwable error) {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the configuration for all of the open metadata view services (OMASs).  This overrides
     * the current values.
     *
     * @param userId             user that is issuing the request.
     * @param serverName         local server name.
     * @param viewServicesConfig list of configuration properties for each view service.
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or viewServicesConfig parameter.
     */
    public VoidResponse setViewServicesConfig(String userId,
                                              String serverName,
                                              List<ViewServiceConfig> viewServicesConfig) {
        final String methodName = "setViewServicesConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null) {
                configAuditTrail = new ArrayList<>();
            }

            if (viewServicesConfig == null) {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for view services.");
            } else {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for view services.");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            serverConfig.setViewServicesConfig(viewServicesConfig);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        } catch (OMAGInvalidParameterException error) {
            exceptionHandler.captureInvalidParameterException(response, error);
        } catch (OMAGNotAuthorizedException error) {
            exceptionHandler.captureNotAuthorizedException(response, error);
        } catch (Throwable error) {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
