/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;


import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.OMAGEngineServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.*;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.EngineHostServicesResponse;
import org.odpi.openmetadata.adminservices.rest.EngineServiceConfigResponse;
import org.odpi.openmetadata.adminservices.rest.EngineServiceRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OMAGServerAdminForEngineServices provides the server-side support for the services that add engine services
 * configuration to an OMAG Server.
 */
public class OMAGServerAdminForEngineHostServices
{
    private static final String serviceName    = GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName();
    private static final String accessService  = AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceName();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminForEngineHostServices.class),
                                                                            CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName());
    

    private final OMAGServerAdminStoreServices   configStore = new OMAGServerAdminStoreServices();
    private final OMAGServerErrorHandler         errorHandler = new OMAGServerErrorHandler();
    private final OMAGServerExceptionHandler     exceptionHandler = new OMAGServerExceptionHandler();


    /**
     * Default constructor
     */
    public OMAGServerAdminForEngineHostServices()
    {
    }


    /**
     * Set up the name and platform URL root for the metadata server running the Governance Engine OMAS that provides
     * the governance engine definitions used by the engine services.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param clientConfig  URL root and server name for the metadata server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse setEngineDefinitionsClientConfig(String                 userId,
                                                         String                 serverName,
                                                         OMAGServerClientConfig clientConfig)
    {
        final String methodName = "setEngineDefinitionsClientConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            String accessServiceRootURL    = null;
            String accessServiceServerName = null;

            if (clientConfig != null)
            {
                accessServiceRootURL = clientConfig.getOMAGServerPlatformRootURL();
                accessServiceServerName = clientConfig.getOMAGServerName();
            }

            errorHandler.validateAccessServiceRootURL(accessServiceRootURL, accessService, serverName, serviceName);
            errorHandler.validateAccessServiceServerName(accessServiceServerName, accessService, serverName, serviceName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if (accessServiceRootURL == null)
            {
                configAuditTrail.add(new Date() + " " + userId + " removed configuration for " + serviceName + " access service root url.");
            }
            else
            {
                configAuditTrail.add(new Date() + " " + userId + " updated configuration for " + serviceName + " access service root url to " + accessServiceRootURL + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            EngineHostServicesConfig engineHostServicesConfig = serverConfig.getEngineHostServicesConfig();

            if (engineHostServicesConfig == null)
            {
                engineHostServicesConfig = new EngineHostServicesConfig();
                if (serverConfig.getRepositoryServicesConfig() == null)
                {
                    OMRSConfigurationFactory omrsConfigurationFactory = new OMRSConfigurationFactory();

                    serverConfig.setRepositoryServicesConfig(omrsConfigurationFactory.getDefaultRepositoryServicesConfig());
                }
            }

            engineHostServicesConfig.setOMAGServerPlatformRootURL(accessServiceRootURL);
            engineHostServicesConfig.setOMAGServerName(accessServiceServerName);

            serverConfig.setEngineHostServicesConfig(engineHostServicesConfig);

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
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the configuration for the Governance Engine OMAS Engine client configuration in a single call.  This overrides the current values.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse clearEngineDefinitionsClientConfig(String userId, String serverName)
    {
        final String methodName = "clearEngineDefinitionsClientConfig";

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
                EngineHostServicesConfig engineHostServicesConfig = serverConfig.getEngineHostServicesConfig();

                if (engineHostServicesConfig != null)
                {
                    engineHostServicesConfig.setOMAGServerPlatformRootURL(null);
                    engineHostServicesConfig.setOMAGServerName(null);
                }

                serverConfig.setEngineHostServicesConfig(engineHostServicesConfig);

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
     * Set up the list of governance engines that will use the metadata from the same metadata access server as the
     * engine host uses for retrieving the engine configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param engines  list of engines
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse setEngineList(String             userId,
                                      String             serverName,
                                      List<EngineConfig> engines)
    {
        final String methodName = "setEngineList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

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

            if ((engines == null) || engines.isEmpty())
            {
                configAuditTrail.add(new Date() + " " + userId + " removed engine list.");
            }
            else
            {
                configAuditTrail.add(new Date() + " " + userId + " updated engine list.");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            EngineHostServicesConfig engineHostServicesConfig = serverConfig.getEngineHostServicesConfig();

            if (engineHostServicesConfig == null)
            {
                engineHostServicesConfig = new EngineHostServicesConfig();
                if (serverConfig.getRepositoryServicesConfig() == null)
                {
                    OMRSConfigurationFactory omrsConfigurationFactory = new OMRSConfigurationFactory();

                    serverConfig.setRepositoryServicesConfig(omrsConfigurationFactory.getDefaultRepositoryServicesConfig());
                }
            }

            engineHostServicesConfig.setEngineList(engines);

            serverConfig.setEngineHostServicesConfig(engineHostServicesConfig);

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
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the configuration for the governance engines in a single call.  This overrides the current values.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse clearEngineList(String userId, String serverName)
    {
        final String methodName = "clearEngineList";

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
                EngineHostServicesConfig engineHostServicesConfig = serverConfig.getEngineHostServicesConfig();

                if (engineHostServicesConfig != null)
                {
                    engineHostServicesConfig.setEngineList(null);
                }

                serverConfig.setEngineHostServicesConfig(engineHostServicesConfig);

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

            List<EngineServiceConfig> currentList = null;
            EngineHostServicesConfig  engineHostServicesConfig = serverConfig.getEngineHostServicesConfig();
            if (engineHostServicesConfig != null)
            {
                currentList = engineHostServicesConfig.getEngineServiceConfigs();
            }

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
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of engine services that are configured for this server.  If you want to see the configuration for these services,
     * use the getEngineHostServicesConfiguration.
     *
     * @param userId calling user
     * @param serverName name of server
     *
     * @return list of engine service descriptions
     */
    public RegisteredOMAGServicesResponse getConfiguredEngineServices(String userId,
                                                                      String serverName)
    {
        final String methodName = "getConfiguredEngineServices";

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
            List<EngineServiceConfig> engineServiceConfigs = null;
            EngineHostServicesConfig  engineHostServicesConfig = serverConfig.getEngineHostServicesConfig();
            if (engineHostServicesConfig != null)
            {
                engineServiceConfigs = engineHostServicesConfig.getEngineServiceConfigs();
            }

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
                        RegisteredOMAGService service = new RegisteredOMAGService();

                        service.setServiceId(engineServiceConfig.getEngineServiceId());
                        service.setServiceName(engineServiceConfig.getEngineServiceFullName());
                        service.setServiceDevelopmentStatus(engineServiceConfig.getEngineServiceDevelopmentStatus());
                        service.setServiceDescription(engineServiceConfig.getEngineServiceDescription());
                        service.setServiceURLMarker(engineServiceConfig.getEngineServiceURLMarker());
                        service.setServiceWiki(engineServiceConfig.getEngineServiceWiki());
                        service.setServerType(ServerTypeClassification.ENGINE_HOST.getServerTypeName());
                        service.setPartnerServiceName(engineServiceConfig.getEngineServicePartnerOMAS());
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
    @SuppressWarnings(value = "deprecation")
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
            EngineServiceRegistrationEntry registration = OMAGEngineServiceRegistration.getEngineServiceRegistration(serviceURLMarker);

            errorHandler.validateEngineServiceIsRegistered(registration, serviceURLMarker, serverName, methodName);

            EngineServiceConfig newEngineServiceConfig = new EngineServiceConfig(registration);

            newEngineServiceConfig.setOMAGServerPlatformRootURL(requestBody.getOMAGServerPlatformRootURL());
            newEngineServiceConfig.setOMAGServerName(requestBody.getOMAGServerName());
            newEngineServiceConfig.setEngines(requestBody.getEngines());
            newEngineServiceConfig.setEngineServiceOptions(requestBody.getEngineServiceOptions());

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            EngineHostServicesConfig engineHostServicesConfig = serverConfig.getEngineHostServicesConfig();
            List<EngineServiceConfig> existingEngineServices = null;

            if (engineHostServicesConfig != null)
            {
                existingEngineServices = engineHostServicesConfig.getEngineServiceConfigs();
            }

            response = this.storeEngineServicesConfig(userId,
                                                      serverName,
                                                      serviceURLMarker,
                                                      updateEngineServiceConfig(newEngineServiceConfig, existingEngineServices),
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
     * Enable all non-deprecated engine services.  This builds the engine service configuration for the
     * server's config document.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param requestBody  minimum values to configure an engine service
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @SuppressWarnings(value = "deprecation")
    public VoidResponse configureAllEngineServices(String                   userId,
                                                   String                   serverName,
                                                   EngineServiceRequestBody requestBody)
    {
        final String methodName = "configureAllEngineService";

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
             * Get the list of engine services implemented in this server.
             */
            List<EngineServiceConfig>            engineServiceConfigs          = new ArrayList<>();
            List<EngineServiceRegistrationEntry> engineServiceRegistrationList = OMAGEngineServiceRegistration.getEngineServiceRegistrationList();

            /*
             * Set up the available engine services.
             */
            if ((engineServiceRegistrationList != null) && (! engineServiceRegistrationList.isEmpty()))
            {
                for (EngineServiceRegistrationEntry registration : engineServiceRegistrationList)
                {
                    if (registration != null)
                    {
                        if (registration.getEngineServiceDevelopmentStatus() != ComponentDevelopmentStatus.DEPRECATED)
                        {
                            EngineServiceConfig newEngineServiceConfig = new EngineServiceConfig(registration);

                            newEngineServiceConfig.setOMAGServerPlatformRootURL(requestBody.getOMAGServerPlatformRootURL());
                            newEngineServiceConfig.setOMAGServerName(requestBody.getOMAGServerName());
                            newEngineServiceConfig.setEngines(requestBody.getEngines());
                            newEngineServiceConfig.setEngineServiceOptions(requestBody.getEngineServiceOptions());

                            engineServiceConfigs.add(newEngineServiceConfig);
                        }
                    }
                }
            }

            if (engineServiceConfigs.isEmpty())
            {
                engineServiceConfigs = null;
            }

            return this.storeEngineServicesConfig(userId,
                                                  serverName,
                                                  null,
                                                  engineServiceConfigs,
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
    @Deprecated
    public VoidResponse configureEngineService(String              userId,
                                               String              serverName,
                                               EngineServiceConfig serviceConfig)
    {
        final String methodName                    = "configureEngineService";
        final String serviceConfigParameterName    = "serviceConfig";
        final String serviceURLMarkerParameterName = "serviceConfig.serviceURLMarker";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateOMAGServerClientConfig(serverName, serviceConfig, methodName);
            errorHandler.validatePropertyNotNull(serviceConfig, serviceConfigParameterName, serverName, methodName);
            errorHandler.validatePropertyNotNull(serviceConfig.getEngineServiceURLMarker(), serviceURLMarkerParameterName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            EngineServiceRegistrationEntry registration = OMAGEngineServiceRegistration.getEngineServiceRegistration(serviceConfig.getEngineServiceURLMarker());

            errorHandler.validateEngineServiceIsRegistered(registration, serviceConfig.getEngineServiceURLMarker(), serverName, methodName);

            List<EngineServiceConfig> existingEngineServices = null;
            EngineHostServicesConfig  engineHostServicesConfig = serverConfig.getEngineHostServicesConfig();

            if (engineHostServicesConfig != null)
            {
                existingEngineServices = engineHostServicesConfig.getEngineServiceConfigs();
            }

            response = this.storeEngineServicesConfig(userId,
                                                      serverName,
                                                      serviceConfig.getEngineServiceURLMarker(),
                                                      updateEngineServiceConfig(serviceConfig, existingEngineServices),
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
     * @param newEngineServiceConfig configuration to add/change
     * @param currentList current config (may be null)
     * @return updated list
     */
    private List<EngineServiceConfig>  updateEngineServiceConfig(EngineServiceConfig         newEngineServiceConfig,
                                                                 List<EngineServiceConfig>   currentList)
    {
        if (newEngineServiceConfig == null)
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
                        if (newEngineServiceConfig.getEngineServiceId() != existingConfig.getEngineServiceId())
                        {
                            newList.add(existingConfig);
                        }
                    }
                }
            }

            newList.add(newEngineServiceConfig);

            return newList;
        }
    }


    /**
     * Set up the configuration for all the open metadata engine services (OMESs).  This overrides
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
        final String methodName = "setEngineHostServicesConfig";

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

            List<EngineServiceConfig> currentList = null;
            EngineHostServicesConfig engineHostServicesConfig = serverConfig.getEngineHostServicesConfig();

            if (engineHostServicesConfig != null)
            {
                currentList = engineHostServicesConfig.getEngineServiceConfigs();
            }

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
        catch (Exception  error)
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

            if ((engineServicesConfig == null) || (engineServicesConfig.isEmpty()))
            {
                configAuditTrail.add(new Date() + " " + userId + " removed configuration for engine services.");
            }
            else if (serviceURLMarker == null)
            {
                configAuditTrail.add(new Date() + " " + userId + " updated configuration for engine services.");
            }
            else
            {
                configAuditTrail.add(new Date() + " " + userId + " updated configuration for engine service " + serviceURLMarker + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            EngineHostServicesConfig engineHostServicesConfig = serverConfig.getEngineHostServicesConfig();

            if ((engineServicesConfig == null) || (engineServicesConfig.isEmpty()))
            {
                if (engineHostServicesConfig != null)
                {
                    if ((engineHostServicesConfig.getOMAGServerPlatformRootURL() == null) && (engineHostServicesConfig.getOMAGServerName() == null))
                    {
                        serverConfig.setEngineHostServicesConfig(null);
                    }
                    else
                    {
                        engineHostServicesConfig.setEngineServiceConfigs(null);
                        serverConfig.setEngineHostServicesConfig(engineHostServicesConfig);
                    }
                }
            }
            else /* services to save */
            {
                if (engineHostServicesConfig == null)
                {
                    engineHostServicesConfig = new EngineHostServicesConfig();
                }

                engineHostServicesConfig.setEngineServiceConfigs(engineServicesConfig);
                serverConfig.setEngineHostServicesConfig(engineHostServicesConfig);
            }

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
     * Return the engine host services configuration including the list of engine services that are configured for this server.
     *
     * @param userId calling user
     * @param serverName name of server
     *
     * @return engine host services configuration
     */
    public EngineHostServicesResponse getEngineHostServicesConfiguration(String userId,
                                                                         String serverName)
    {
        final String methodName = "getEngineHostServicesConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EngineHostServicesResponse response = new EngineHostServicesResponse();

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
            response.setServices(serverConfig.getEngineHostServicesConfig());
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
     * Set up the configuration for an Engine Host OMAG Server in a single call.  This overrides the current values.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param servicesConfig full configuration for the engine host server.
     * @return void response
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse setEngineHostServicesConfig(String userId, String serverName, EngineHostServicesConfig servicesConfig)
    {
        final String methodName                     = "setEngineHostServicesConfig";
        final String serviceConfigParameterName     = "servicesConfig";
        final String engineServicesParameterName    = "servicesConfig.engineServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validatePropertyNotNull(servicesConfig, serviceConfigParameterName, serverName, methodName);
            errorHandler.validateOMAGServerClientConfig(serverName, servicesConfig, methodName);
            errorHandler.validatePropertyNotNull(servicesConfig.getEngineServiceConfigs(), engineServicesParameterName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            if (serverConfig != null)
            {
                serverConfig.setEngineHostServicesConfig(servicesConfig);

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
     * Remove the configuration for an Engine Host OMAG Server in a single call.  This overrides the current values.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    public VoidResponse clearEngineHostServicesConfig(String userId, String serverName)
    {
        final String methodName = "clearEngineHostServicesConfig";

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
                serverConfig.setEngineHostServicesConfig(null);

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
