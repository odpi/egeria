/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.properties.StewardshipServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OMAGServerConfigStewardshipServices supports the configuration requests for the stewardship services.
 */
public class OMAGServerConfigStewardshipServices
{
    static final String serviceName    = "stewardship services";
    static final String accessService  = "Stewardship Action OMAS";
    
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

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if ("".equals(accessServiceRootURL))
            {
                accessServiceRootURL = null;
            }

            if (accessServiceRootURL == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for " + serviceName + " access service root url.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for " + serviceName + " access service root url to " + accessServiceRootURL + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);
            
            StewardshipServicesConfig stewardshipServicesConfig = serverConfig.getStewardshipServicesConfig();
            
            if (stewardshipServicesConfig == null)
            {
                stewardshipServicesConfig = new StewardshipServicesConfig();
            }
            
            stewardshipServicesConfig.setAccessServiceRootURL(accessServiceRootURL);
            
            serverConfig.setStewardshipServicesConfig(stewardshipServicesConfig);

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

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if ("".equals(accessServiceServerName))
            {
                accessServiceServerName = null;
            }

            if (accessServiceServerName == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for " + serviceName + " access service server name.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for " + serviceName + " access service server name " + accessServiceServerName + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            StewardshipServicesConfig stewardshipServicesConfig = serverConfig.getStewardshipServicesConfig();

            if (stewardshipServicesConfig == null)
            {
                stewardshipServicesConfig = new StewardshipServicesConfig();
            }

            stewardshipServicesConfig.setAccessServiceRootURL(accessServiceServerName);

            serverConfig.setStewardshipServicesConfig(stewardshipServicesConfig);

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

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            if (connection == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for " + serviceName + " inbound request connection.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for " + serviceName + " inbound request connection.");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            StewardshipServicesConfig stewardshipServicesConfig = serverConfig.getStewardshipServicesConfig();

            if (stewardshipServicesConfig == null)
            {
                stewardshipServicesConfig = new StewardshipServicesConfig();
            }

            stewardshipServicesConfig.setInboundRequestConnection(connection);

            serverConfig.setStewardshipServicesConfig(stewardshipServicesConfig);

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

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for " + serviceName + ".");

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setStewardshipServicesConfig(null);

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
