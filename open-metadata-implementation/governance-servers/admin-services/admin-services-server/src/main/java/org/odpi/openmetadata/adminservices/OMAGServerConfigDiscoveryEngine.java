/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.properties.DiscoveryEngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OMAGServerConfigDiscoveryEngine supports the configuration requests for a discovery engine.
 */
public class OMAGServerConfigDiscoveryEngine
{
    static final String serviceName    = "discovery engine services";
    static final String accessService  = "Discovery Engine OMAS";

    private OMAGServerAdminStoreServices   configStore = new OMAGServerAdminStoreServices();
    private OMAGServerErrorHandler         errorHandler = new OMAGServerErrorHandler();


    /**
     * Set up the root URL of the access service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param accessServiceRootURL  URL root for the access service.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    public VoidResponse setAccessServiceRootURL(String userId,
                                                String serverName,
                                                String accessServiceRootURL)
    {
        final String methodName = "setAccessServiceRootURL";

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateAccessServiceRootURL(accessServiceRootURL, accessService, serverName, serviceName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            List<String> configAuditLog = serverConfig.getAuditTrail();

            if (configAuditLog == null)
            {
                configAuditLog = new ArrayList<>();
            }

            if ("".equals(accessServiceRootURL))
            {
                accessServiceRootURL = null;
            }

            if (accessServiceRootURL == null)
            {
                configAuditLog.add(new Date().toString() + " " + userId + " removed configuration for " + serviceName + " access service root url.");
            }
            else
            {
                configAuditLog.add(new Date().toString() + " " + userId + " updated configuration for " + serviceName + " access service root url to " + accessServiceRootURL + ".");
            }

            serverConfig.setAuditTrail(configAuditLog);

            DiscoveryEngineConfig discoveryEngineConfig = serverConfig.getDiscoveryEngineConfig();

            if (discoveryEngineConfig == null)
            {
                discoveryEngineConfig = new DiscoveryEngineConfig();
            }

            discoveryEngineConfig.setAccessServiceRootURL(accessServiceRootURL);

            serverConfig.setDiscoveryEngineConfig(discoveryEngineConfig);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /**
     * Set up the server name of the access service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param accessServiceServerName  server name for the access service.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    public VoidResponse setAccessServiceServerName(String userId,
                                                   String serverName,
                                                   String accessServiceServerName)
    {
        final String methodName = "setAccessServiceServerName";

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateAccessServiceServerName(accessServiceServerName, accessService, serverName, serviceName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            List<String> configAuditLog = serverConfig.getAuditTrail();

            if (configAuditLog == null)
            {
                configAuditLog = new ArrayList<>();
            }

            if ("".equals(accessServiceServerName))
            {
                accessServiceServerName = null;
            }

            if (accessServiceServerName == null)
            {
                configAuditLog.add(new Date().toString() + " " + userId + " removed configuration for " + serviceName + " access service server name.");
            }
            else
            {
                configAuditLog.add(new Date().toString() + " " + userId + " updated configuration for " + serviceName + " access service server name " + accessServiceServerName + ".");
            }

            serverConfig.setAuditTrail(configAuditLog);

            DiscoveryEngineConfig discoveryEngineConfig = serverConfig.getDiscoveryEngineConfig();

            if (discoveryEngineConfig == null)
            {
                discoveryEngineConfig = new DiscoveryEngineConfig();
            }

            discoveryEngineConfig.setAccessServiceRootURL(accessServiceServerName);

            serverConfig.setDiscoveryEngineConfig(discoveryEngineConfig);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }

    /**
     * Set up the server name of the access service.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param connection  connection for topic.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    public VoidResponse setInboundRequestConnection(String     userId,
                                                    String     serverName,
                                                    Connection connection)
    {
        final String methodName = "setAccessServiceServerName";

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            List<String> configAuditLog = serverConfig.getAuditTrail();

            if (configAuditLog == null)
            {
                configAuditLog = new ArrayList<>();
            }

            if (connection == null)
            {
                configAuditLog.add(new Date().toString() + " " + userId + " removed configuration for " + serviceName + " inbound request connection.");
            }
            else
            {
                configAuditLog.add(new Date().toString() + " " + userId + " updated configuration for " + serviceName + " inbound request connection.");
            }

            serverConfig.setAuditTrail(configAuditLog);

            DiscoveryEngineConfig discoveryEngineConfig = serverConfig.getDiscoveryEngineConfig();

            if (discoveryEngineConfig == null)
            {
                discoveryEngineConfig = new DiscoveryEngineConfig();
            }

            discoveryEngineConfig.setInboundRequestConnection(connection);

            serverConfig.setDiscoveryEngineConfig(discoveryEngineConfig);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }


    /**
     * Remove this service from the server configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return
     */
    public VoidResponse deleteService(String userId, String serverName)
    {
        final String methodName = "deleteService";

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(serverName, methodName);

            List<String> configAuditLog = serverConfig.getAuditTrail();

            if (configAuditLog == null)
            {
                configAuditLog = new ArrayList<>();
            }

            configAuditLog.add(new Date().toString() + " " + userId + " removed configuration for " + serviceName + ".");

            serverConfig.setAuditTrail(configAuditLog);
            serverConfig.setDiscoveryEngineConfig(null);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (OMAGInvalidParameterException error)
        {
            errorHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            errorHandler.captureNotAuthorizedException(response, error);
        }

        return response;
    }
}
