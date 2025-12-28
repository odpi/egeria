/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;


import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.rest.*;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * OMAGServerAdminForIntegrationDaemonServices provides the server-side support for the services that
 * configure the specialized part of the integration daemon.
 */
public class OMAGServerAdminForIntegrationDaemonServices extends TokenController
{
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminForIntegrationDaemonServices.class),
                                                                            CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName());
    

    private final OMAGServerAdminStoreServices   configStore = new OMAGServerAdminStoreServices();
    private final        OMAGServerErrorHandler errorHandler         = new OMAGServerErrorHandler();
    private static final RESTExceptionHandler   restExceptionHandler = new RESTExceptionHandler();


    /**
     * Default constructor
     */
    public OMAGServerAdminForIntegrationDaemonServices()
    {
    }



    /**
     * Return the list of integration groups that are configured for this server.
     *
     * @param serverName name of server
     * @param delegatingUserId external userId making request
     *
     * @return list of access service configurations
     */
    public IntegrationGroupsResponse getIntegrationGroupsConfiguration(String serverName,
                                                                       String delegatingUserId)
    {
        final String methodName = "getIntegrationGroupsConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        IntegrationGroupsResponse response = new IntegrationGroupsResponse();

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
             * Get the list of Integration Services configured in this server.
             */
            response.setGroups(serverConfig.getDynamicIntegrationGroupsConfig());
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Enable a single registered integration group.  This builds the integration group configuration for the
     * server's config document.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param requestBody  minimum values to configure an integration group
     *
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * InvalidParameterException invalid serverName parameter.
     */
    public VoidResponse configureIntegrationGroup(String                 serverName,
                                                  String                 delegatingUserId,
                                                  IntegrationGroupConfig requestBody)
    {
        final String methodName = "configureIntegrationGroup";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            /*
             * Validate the incoming parameters
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateIntegrationGroupConfig(serverName, requestBody, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            /*
             * Get the configuration information for this integration group.
             */
            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            List<IntegrationGroupConfig> integrationGroupsConfig = serverConfig.getDynamicIntegrationGroupsConfig();

            response = this.storeIntegrationGroupsConfig(userId,
                                                         delegatingUserId,
                                                         serverName,
                                                         requestBody.getIntegrationGroupQualifiedName(),
                                                         updateIntegrationGroupConfig(requestBody, integrationGroupsConfig),
                                                         methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
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
                        if (! existingConfig.getIntegrationGroupQualifiedName().equals(integrationGroupConfig.getIntegrationGroupQualifiedName()))
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
     * Set up the configuration for all the open metadata integration groups.  This overrides
     * the current values.
     *
     * @param serverName            local server name.
     * @param delegatingUserId external userId making request
     * @param integrationGroupsConfig  list of configuration properties for each integration group.
     * @return void response or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or integrationGroupsConfig parameter.
     */
    public VoidResponse setIntegrationGroupsConfig(String                       serverName,
                                                   String                       delegatingUserId,
                                                   List<IntegrationGroupConfig> integrationGroupsConfig)
    {
        final String methodName = "setIntegrationGroupsConfig";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response = storeIntegrationGroupsConfig(userId, delegatingUserId, serverName, null, integrationGroupsConfig, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Disable the integration groups.  This removes all configuration for the integration groups
     * and disables the enterprise repository services.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName  parameter.
     */
    public VoidResponse clearAllIntegrationGroups(String serverName,
                                                  String delegatingUserId)
    {
        final String methodName = "clearAllIntegrationGroups";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response = this.storeIntegrationGroupsConfig(userId, delegatingUserId, serverName, null, null, methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove an integration group.  This removes all configuration for the integration group.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param groupId integration group id
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName  parameter.
     */
    public VoidResponse clearIntegrationGroup(String serverName,
                                              String delegatingUserId,
                                              String groupId)
    {
        final String methodName = "clearIntegrationGroup";

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

                response = this.storeIntegrationGroupsConfig(userId, delegatingUserId, serverName, groupId, newList, methodName);
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
     * Store the latest set of integration groups in the configuration document for the server.
     *
     * @param userId                     user that is issuing the request.
     * @param delegatingUserId external userId making request
     * @param serverName                 local server name.
     * @param groupQualifiedName         identifier of specific integration group
     * @param integrationGroupsConfig    list of configuration properties for each integration group.
     * @param methodName                 calling method
     * @return void response or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or integrationGroupsConfig parameter.
     */
    private VoidResponse storeIntegrationGroupsConfig(String                       userId,
                                                      String                       delegatingUserId,
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

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        return response;
    }
    

    /**
     * Return the integration daemon services configuration including the list of integration services and
     * integration services that are configured for this server.
     *
     * @param serverName name of server
     * @param delegatingUserId external userId making request
     *
     * @return integration daemon services configuration
     */
    public IntegrationDaemonServicesResponse getIntegrationDaemonServicesConfiguration(String serverName,
                                                                                       String delegatingUserId)
    {
        final String methodName = "getIntegrationDaemonServicesConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        IntegrationDaemonServicesResponse response = new IntegrationDaemonServicesResponse();

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
             * Get the list of Integration Groups and Services configured in this server.
             */
            IntegrationDaemonServicesConfig integrationDaemonServicesConfig = new IntegrationDaemonServicesConfig();

            integrationDaemonServicesConfig.setDynamicIntegrationGroupsConfig(serverConfig.getDynamicIntegrationGroupsConfig());

            response.setServices(integrationDaemonServicesConfig);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the configuration for an Integration Daemon OMAG Server in a single call.  This overrides the current values.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @param servicesConfig full configuration for the integration daemon server.
     * @return void response
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * InvalidParameterException invalid serverName parameter.
     */
    public VoidResponse setIntegrationDaemonServicesConfig(String                          serverName,
                                                           String                          delegatingUserId,
                                                           IntegrationDaemonServicesConfig servicesConfig)
    {
        final String methodName                       = "setIntegrationDaemonServicesConfig";
        final String serviceConfigParameterName       = "servicesConfig";

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

            /*
             * Just check the config is not null (use clear to remove config completely.)
             * However, it is not assumed that the supplied config is complete.  Therefore, there may be missing
             * client information - this is an advanced service, so we assume the caller knows what they are doing.
             */
            errorHandler.validatePropertyNotNull(servicesConfig, serviceConfigParameterName, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, delegatingUserId, serverName, true, methodName);

            if (serverConfig != null)
            {
                serverConfig.setDynamicIntegrationGroupsConfig(servicesConfig.getDynamicIntegrationGroupsConfig());

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
     * Remove the configuration for an Integration Daemon OMAG Server in a single call.  This overrides the current values.
     *
     * @param serverName  local server name.
     * @param delegatingUserId external userId making request
     * @return void response
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException unexpected exception or
     * InvalidParameterException invalid serverName parameter.
     */
    public VoidResponse clearIntegrationDaemonServicesConfig(String serverName,
                                                             String delegatingUserId)
    {
        final String methodName = "clearIntegrationDaemonServicesConfig";

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
                serverConfig.setDynamicIntegrationGroupsConfig(null);

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
