/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;


import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.rest.EngineHostServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * OMAGServerAdminForEngineServices provides the server-side support for the services that add engine services
 * configuration to an OMAG Server.
 */
public class OMAGServerAdminForEngineHostServices extends TokenController
{
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminForEngineHostServices.class),
                                                                            CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName());

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private final OMAGServerAdminStoreServices   configStore = new OMAGServerAdminStoreServices();
    private final OMAGServerErrorHandler         errorHandler = new OMAGServerErrorHandler();


    /**
     * Default constructor
     */
    public OMAGServerAdminForEngineHostServices()
    {
    }


    /**
     * Set up the list of governance engines that will use the metadata from the same metadata access server as the
     * engine host uses for retrieving the engine configuration.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param engine  new engine
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * InvalidParameterException invalid serverName parameter.
     */
    public VoidResponse addEngine(String       serverName,
                                  String       delegatingUserId,
                                  EngineConfig engine)
    {
        final String methodName = "addEngine";
        final String engineParameterName = "engine";
        final String engineNameParameterName = "engine.qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validatePropertyNotNull(engine, engineParameterName, serverName, methodName);
            errorHandler.validatePropertyNotNull(engine.getEngineQualifiedName(), engineNameParameterName, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date() + " " + userId + " add new engine " + engine.getEngineQualifiedName() + " to list.");

            serverConfig.setAuditTrail(configAuditTrail);

            List<EngineConfig> engineHostServicesConfig = serverConfig.getGovernanceEnginesConfig();

            if (engineHostServicesConfig == null)
            {
                engineHostServicesConfig = new ArrayList<>();
                if (serverConfig.getRepositoryServicesConfig() == null)
                {
                    OMRSConfigurationFactory omrsConfigurationFactory = new OMRSConfigurationFactory();

                    serverConfig.setRepositoryServicesConfig(omrsConfigurationFactory.getDefaultRepositoryServicesConfig());
                }
            }

            Map<String, EngineConfig> engines = new HashMap<>();

            for (EngineConfig engineConfig : engineHostServicesConfig)
            {
                if ((engineConfig != null) && (engineConfig.getEngineQualifiedName() != null) && (!engineConfig.getEngineQualifiedName().isBlank()))
                {
                    engines.put(engineConfig.getEngineQualifiedName(), engineConfig);
                }
            }

            engines.put(engine.getEngineQualifiedName(), engine);

            serverConfig.setGovernanceEnginesConfig(new ArrayList<>(engines.values()));

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the engine host services configuration including the list of engine services that are configured for this server.
     *
     * @param serverName name of server
     * @param delegatingUserId external userId making request
     *
     * @return engine host services configuration
     */
    public EngineHostServicesResponse getEngineHostServicesConfiguration(String serverName,
                                                                         String delegatingUserId)
    {
        final String methodName = "getEngineHostServicesConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        EngineHostServicesResponse response = new EngineHostServicesResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, false, methodName);

            /*
             * Get the list of Engine Services configured in this server.
             */
            response.setGovernanceEngines(serverConfig.getGovernanceEnginesConfig());
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the configuration for an Engine Host OMAG Server in a single call.  This overrides the current values.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param governanceEngines full configuration for the engine host server.
     * @return void response
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * InvalidParameterException invalid serverName parameter.
     */
    public VoidResponse setEngineHostServicesConfig(String             serverName,
                                                    String             delegatingUserId,
                                                    List<EngineConfig> governanceEngines)
    {
        final String methodName                     = "setEngineHostServicesConfig";
        final String serviceConfigParameterName     = "governanceEngines";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate and set up the userName and server name.
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validatePropertyNotNull(governanceEngines, serviceConfigParameterName, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            if (serverConfig != null)
            {
                if (serverConfig.getRepositoryServicesConfig() == null)
                {
                    OMRSConfigurationFactory omrsConfigurationFactory = new OMRSConfigurationFactory();

                    serverConfig.setRepositoryServicesConfig(omrsConfigurationFactory.getDefaultRepositoryServicesConfig());
                }

                serverConfig.setGovernanceEnginesConfig(governanceEngines);

                this.configStore.saveServerConfig(serverName, methodName, serverConfig);
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
     * Remove the configuration for an Engine Host OMAG Server in a single call.  This overrides the current values.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @return void response
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * InvalidParameterException invalid serverName parameter.
     */
    public VoidResponse clearEngineHostServicesConfig(String serverName,
                                                      String delegatingUserId)
    {
        final String methodName = "clearEngineHostServicesConfig";

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

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            if (serverConfig != null)
            {
                serverConfig.setGovernanceEnginesConfig(null);

                this.configStore.saveServerConfig(serverName, methodName, serverConfig);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
