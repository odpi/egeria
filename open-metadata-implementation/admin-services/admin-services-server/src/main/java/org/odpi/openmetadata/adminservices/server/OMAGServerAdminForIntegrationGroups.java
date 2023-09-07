/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;


import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.IntegrationGroupsResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.repositoryservices.admin.OMRSConfigurationFactory;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OMAGServerAdminForIntegrationGroups provides the server-side support for the services that add integration group
 * configuration to an OMAG Server.
 */
public class OMAGServerAdminForIntegrationGroups
{
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminForIntegrationGroups.class),
                                                                            CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName());
    

    private final OMAGServerAdminStoreServices configStore      = new OMAGServerAdminStoreServices();
    private final OMAGServerErrorHandler       errorHandler     = new OMAGServerErrorHandler();
    private final OMAGServerExceptionHandler   exceptionHandler = new OMAGServerExceptionHandler();


    /**
     * Default constructor
     */
    public OMAGServerAdminForIntegrationGroups()
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
}
