/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceClientConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.DiscoveryEngineServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OMAGServerConfigDiscoveryEngineServices supports the configuration requests for a discovery server
 * and the discovery engines that run inside of it.
 */
public class OMAGServerConfigDiscoveryEngineServices
{
    private static final String serviceName    = GovernanceServicesDescription.DISCOVERY_ENGINE_SERVICES.getServiceName();
    private static final String accessService  = AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceName();

    private static final Logger log = LoggerFactory.getLogger(OMAGServerConfigDiscoveryEngineServices.class);

    private OMAGServerAdminStoreServices   configStore = new OMAGServerAdminStoreServices();
    private OMAGServerErrorHandler         errorHandler = new OMAGServerErrorHandler();
    private OMAGServerExceptionHandler     exceptionHandler = new OMAGServerExceptionHandler();


    /**
     * Set up the name and platform URL root for the metadata server supporting this discovery server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param clientConfig  URL root and server name for the metadata server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    public VoidResponse setAccessServiceLocation(String                    userId,
                                                 String                    serverName,
                                                 AccessServiceClientConfig clientConfig)
    {
        final String methodName = "setAccessServiceLocation";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            String accessServiceRootURL    = null;
            String accessServiceServerName = null;

            if (clientConfig != null)
            {
                accessServiceRootURL = clientConfig.getAccessServiceRootURL();
                accessServiceServerName = clientConfig.getAccessServiceServerName();
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
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for " + serviceName + " access service root url.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for " + serviceName + " access service root url to " + accessServiceRootURL + ".");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            DiscoveryEngineServicesConfig discoveryEngineServicesConfig = serverConfig.getDiscoveryEngineServicesConfig();

            if (discoveryEngineServicesConfig == null)
            {
                discoveryEngineServicesConfig = new DiscoveryEngineServicesConfig();
            }

            discoveryEngineServicesConfig.setAccessServiceRootURL(accessServiceRootURL);
            discoveryEngineServicesConfig.setAccessServiceServerName(accessServiceServerName);

            serverConfig.setDiscoveryEngineServicesConfig(discoveryEngineServicesConfig);

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
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Set up the list of discovery engines that will run in this discovery server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param discoveryEngineNames  list of discovery engine qualified names describing which discovery engines run in this server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverType parameter.
     */
    public VoidResponse setDiscoveryEngines(String       userId,
                                            String       serverName,
                                            List<String> discoveryEngineNames)
    {
        final String methodName = "setDiscoveryEngines";

        log.debug("Calling method: " + methodName);

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

            if (discoveryEngineNames == null)
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for " + serviceName + " inbound request discoveryEngineNames.");
            }
            else
            {
                configAuditTrail.add(new Date().toString() + " " + userId + " updated configuration for " + serviceName + " inbound request discoveryEngineNames.");
            }

            serverConfig.setAuditTrail(configAuditTrail);

            DiscoveryEngineServicesConfig discoveryEngineServicesConfig = serverConfig.getDiscoveryEngineServicesConfig();

            if (discoveryEngineServicesConfig == null)
            {
                discoveryEngineServicesConfig = new DiscoveryEngineServicesConfig();
            }

            discoveryEngineServicesConfig.setDiscoveryEngineNames(discoveryEngineNames);

            serverConfig.setDiscoveryEngineServicesConfig(discoveryEngineServicesConfig);

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
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Add this service to the server configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param servicesConfig full configuration for the service.
     * @return void response
     */
    public VoidResponse addService(String userId, String serverName, DiscoveryEngineServicesConfig servicesConfig)
    {
        final String methodName = "addService";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            this.setAccessServiceLocation(userId, serverName, servicesConfig);
            this.setDiscoveryEngines(userId, serverName, servicesConfig.getDiscoveryEngineNames());
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Remove this service from the server configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response
     */
    public VoidResponse deleteService(String userId, String serverName)
    {
        final String methodName = "deleteService";

        log.debug("Calling method: " + methodName);

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

            configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for " + serviceName + ".");

            serverConfig.setAuditTrail(configAuditTrail);
            serverConfig.setDiscoveryEngineServicesConfig(null);

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
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeException(serverName, methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }
}
