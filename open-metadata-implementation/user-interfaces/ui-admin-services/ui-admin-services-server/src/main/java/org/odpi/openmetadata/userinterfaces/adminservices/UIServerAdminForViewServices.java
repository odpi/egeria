/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices;


import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.OMAGServerExceptionHandler;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceOperationalStatus;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceRegistration;
import org.odpi.userinterface.adminservices.configuration.UIViewServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OMAGServerAdminForViewServices provides the server-side support for the services that add access services
 * configuration to an OMAG Server.
 */
public class UIServerAdminForViewServices
{
    private static final Logger log = LoggerFactory.getLogger(UIServerAdminForViewServices.class);
    

    private UIServerAdminStoreServices   configStore = new UIServerAdminStoreServices();
    private UIServerErrorHandler         errorHandler = new UIServerErrorHandler();
    private OMAGServerExceptionHandler   exceptionHandler = new OMAGServerExceptionHandler();


    /**
     * Default constructor
     */
    public UIServerAdminForViewServices()
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
    public RegisteredOMAGServicesResponse getConfiguredViewServices(String              userId,
                                                                      String              serverName)
    {
        final String methodName = "getConfiguredViewServices";

        log.debug("Calling method: " + methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            UIServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            /*
             * Get the list of View Services configured in this server.
             */
            List<ViewServiceConfig> viewServiceConfigList = serverConfig.getViewServicesConfig();

            /*
             * Set up the available access services.
             */
            if ((viewServiceConfigList != null) && (! viewServiceConfigList.isEmpty()))
            {
                for (ViewServiceConfig viewServiceConfig : viewServiceConfigList)
                {
                    if (viewServiceConfig != null)
                    {
                        if (viewServiceConfig.getViewServiceOperationalStatus() == ViewServiceOperationalStatus.ENABLED)
                        {
                            RegisteredOMAGService service = new RegisteredOMAGService();

                            service.setServiceName(viewServiceConfig.getViewServiceName());
                            service.setServiceDescription(viewServiceConfig.getViewServiceDescription());
                            service.setServiceURLMarker(viewServiceConfig.getViewServiceURLMarker());
                            service.setServiceWiki(viewServiceConfig.getViewServiceWiki());
                        }
                    }
                }

            }
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Enable a single view service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serviceURLMarker view service name used in URL
     * @param viewServiceOptions  property name/value pairs used to configure the view services
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureViewService(String              userId,
                                               String              serverName,
                                               String              serviceURLMarker,
                                               Map<String, Object> viewServiceOptions)
    {
        final String methodName = "configureViewService";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            UIServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);


            List<ViewServiceConfig> viewServiceConfigList = serverConfig.getViewServicesConfig();

            /*
             * Get the registration information for this view service.
             */
            ViewServiceRegistration viewServiceRegistration = UIViewServiceRegistration.getViewServiceRegistration(serviceURLMarker);

            errorHandler.validateViewServiceIsRegistered(viewServiceRegistration, serviceURLMarker, serverName, methodName);

            viewServiceConfigList = this.updateViewServiceConfig(createViewServiceConfig(viewServiceRegistration,
                                                                                               viewServiceOptions,
                                                                                               serverName,
                                                                                               serverConfig.getLocalServerId()),
                                                                     viewServiceConfigList);


            this.setViewServicesConfig(userId, serverName, viewServiceConfigList);

        }
        catch (OMAGConfigurationErrorException error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Enable all view services that are installed into this server.   The configuration properties
     * for each view service can be changed from their default using setViewServicesConfig operation.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param viewServiceOptions  property name/value pairs used to configure the view services
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse enableViewServices(String              userId,
                                             String              serverName,
                                             Map<String, Object> viewServiceOptions)
    {
        final String methodName = "enableViewServices";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            UIServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<ViewServiceConfig> viewServiceConfigList = new ArrayList<>();

            /*
             * Get the list of View Services implemented in this server.
             */
            List<ViewServiceRegistration> viewServiceRegistrationList = UIViewServiceRegistration.getViewServiceRegistrationList();

            /*
             * Set up the available view services.
             */
            if ((viewServiceRegistrationList != null) && (! viewServiceRegistrationList.isEmpty()))
            {
                for (ViewServiceRegistration registration : viewServiceRegistrationList)
                {
                    if (registration != null)
                    {
                        if (registration.getViewServiceOperationalStatus() == ViewServiceOperationalStatus.ENABLED)
                        {
                            viewServiceConfigList.add(createViewServiceConfig(registration,
                                                                                  viewServiceOptions,
                                                                                  serverName,
                                                                                  serverConfig.getLocalServerId()));
                        }
                    }
                }

                /*
                 * Now set up the enterprise repository services.
                 */
                OMRSConfigurationFactory configurationFactory = new OMRSConfigurationFactory();
            }

            if (viewServiceConfigList.isEmpty())
            {
                viewServiceConfigList = null;
            }

            this.setViewServicesConfig(userId, serverName, viewServiceConfigList);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Set up the configuration for a single view service.
     *
     * @param registration registration information about the service.
     * @param viewServiceOptions options for the service
     * @param serverName name of this server
     * @param localServerId unique Id for this server
     * @return newly created config object
     */
    private ViewServiceConfig  createViewServiceConfig(ViewServiceRegistration registration,
                                                       Map<String, Object>         viewServiceOptions,
                                                       String                      serverName,
                                                       String                      localServerId)
    {
        ConnectorConfigurationFactory connectorConfigurationFactory = new ConnectorConfigurationFactory();

        ViewServiceConfig viewServiceConfig = new ViewServiceConfig(registration);

        viewServiceConfig.setViewServiceOptions(viewServiceOptions);

        return viewServiceConfig;
    }


    /**
     * Add/update the configuration for a single service in the configuration.
     *
     * @param viewServiceConfig configuration to add/change
     * @param currentList current config (may be null)
     * @return updated list
     */
    private List<ViewServiceConfig>  updateViewServiceConfig(ViewServiceConfig         viewServiceConfig,
                                                                 List<ViewServiceConfig>   currentList)
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
     * Disable the view services.  This removes all configuration for the view services
     * and disables the enterprise repository services.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName  parameter.
     */
    public VoidResponse disableViewServices(String          userId,
                                              String          serverName)
    {
        final String methodName = "disableViewServices";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            this.setViewServicesConfig(userId, serverName, null);
        }
        catch (OMAGNotAuthorizedException  error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Set up the configuration for all of the open metadata view services (OMVSs).  This overrides
     * the current values.
     *
     * @param userId                user that is issuing the request.
     * @param serverName            local server name.
     * @param viewServicesConfig    list of configuration properties for each view service.
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or viewServicesConfig parameter.
     */
    public VoidResponse setViewServicesConfig(String                    userId,
                                                String                    serverName,
                                                List<ViewServiceConfig> viewServicesConfig)
    {
        final String methodName = "setViewServicesConfig";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            UIServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<String>  configAuditTrail          = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if (viewServicesConfig == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for view services.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for view services.");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            serverConfig.setViewServicesConfig(viewServicesConfig);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }

        catch (OMAGNotAuthorizedException  error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }

}
