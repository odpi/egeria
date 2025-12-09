/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server;


import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerPlatformInstanceMap;
import org.odpi.openmetadata.serveroperations.server.OMAGServerOperationalInstanceHandler;
import org.odpi.openmetadata.serveroperations.server.OMAGServerOperationalServices;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * OMAGServerPlatformOperationalServices will provide support to start, manage and stop services in the OMAG Server.
 */
public class OMAGServerPlatformOperationalServices extends TokenController
{
    /**
     * The instance handler is looking up the server operations instance and so it needs to be initialized with
     * the server operations service name.
     */
    private final OMAGServerOperationalInstanceHandler serverOperationalInstanceHandler =
            new OMAGServerOperationalInstanceHandler(CommonServicesDescription.SERVER_OPERATIONS.getServiceName());
    private final OMAGServerOperationalServices serverOperationalServices        = new OMAGServerOperationalServices();

    private final        OMAGServerPlatformInstanceMap platformInstanceMap  = new OMAGServerPlatformInstanceMap();
    private static final RESTExceptionHandler          restExceptionHandler = new RESTExceptionHandler();

    private final static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerPlatformOperationalServices.class),
                                                                            CommonServicesDescription.PLATFORM_SERVICES.getServiceName());

    /*
     * =============================================================
     * Platform unique shutdown services
     */


    /**
     * Temporarily deactivate any open metadata and governance services for all running servers.
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    public VoidResponse shutdownAllServers()
    {
        final String methodName = "shutdownAllServer";
        final String serverName = "<null>";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            List<String> activeServers = platformInstanceMap.getActiveServerList(userId);

            if (activeServers != null)
            {
                for (String activeServerName : activeServers)
                {
                    serverOperationalServices.deactivateRunningServiceInstances(userId,
                                                                                activeServerName,
                                                                                methodName,
                                                                                serverOperationalInstanceHandler.getServerServiceInstance(userId, activeServerName, methodName),
                                                                                false);
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
     * Terminate any running open metadata and governance services, remove the server from any open metadata cohorts
     * and delete the server's configuration.
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    public VoidResponse shutdownAndUnregisterAllServers()
    {
        final String methodName = "shutdownAndUnregisterAllServers";
        final String serverName = "<null>";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            List<String> activeServers = platformInstanceMap.getActiveServerList(userId);

            if (activeServers != null)
            {
                for (String activeServerName : activeServers)
                {
                    serverOperationalServices.deactivateRunningServiceInstances(userId,
                                                                                activeServerName,
                                                                                methodName,
                                                                                serverOperationalInstanceHandler.getServerServiceInstance(userId, activeServerName, methodName),
                                                                                true);
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
     * Terminate this platform.
     *
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    public VoidResponse shutdownPlatform()
    {
        final String methodName = "shutdownPlatform";
        final String serverName = "<null>";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            Runtime.getRuntime().exit(1);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
