/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server;


import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.server.OMAGServerErrorHandler;
import org.odpi.openmetadata.adminservices.server.OMAGServerExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerPlatformInstanceMap;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.serveroperations.server.OMAGServerOperationalInstanceHandler;
import org.odpi.openmetadata.serveroperations.server.OMAGServerOperationalServices;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * OMAGServerPlatformOperationalServices will provide support to start, manage and stop services in the OMAG Server.
 */
public class OMAGServerPlatformOperationalServices
{
    private final OMAGServerOperationalInstanceHandler instanceHandler           = new OMAGServerOperationalInstanceHandler(CommonServicesDescription.PLATFORM_SERVICES.getServiceName());
    private final OMAGServerOperationalServices        serverOperationalServices = new OMAGServerOperationalServices();

    private final OMAGServerPlatformInstanceMap        platformInstanceMap = new OMAGServerPlatformInstanceMap();

    private final OMAGServerErrorHandler     errorHandler     = new OMAGServerErrorHandler();
    private final OMAGServerExceptionHandler exceptionHandler = new OMAGServerExceptionHandler();

    private final static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerPlatformOperationalServices.class),
                                                                            CommonServicesDescription.PLATFORM_SERVICES.getServiceName());

    /*
     * =============================================================
     * Platform unique shutdown services
     */


    /**
     * Temporarily deactivate any open metadata and governance services for all running servers.
     *
     * @param userId  user that is issuing the request
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    public VoidResponse shutdownAllServers(String  userId)
    {
        final String methodName = "shutdownServer";
        final String serverName = "<null>";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateUserId(userId, serverName, methodName);

            List<String> activeServers = platformInstanceMap.getActiveServerList(userId);

            if (activeServers != null)
            {
                for (String activeServerName : activeServers)
                {
                    serverOperationalServices.deactivateRunningServiceInstances(userId,
                                                                                activeServerName,
                                                                                methodName,
                                                                                instanceHandler.getServerServiceInstance(userId, activeServerName, methodName),
                                                                                false);
                }
            }
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
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
     * Terminate any running open metadata and governance services, remove the server from any open metadata cohorts
     * and delete the server's configuration.
     *
     * @param userId  user that is issuing the request
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    public VoidResponse shutdownAndUnregisterAllServers(String  userId)
    {
        final String methodName = "shutdownAndUnregisterAllServers";
        final String serverName = "<null>";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateUserId(userId, serverName, methodName);

            List<String> activeServers = platformInstanceMap.getActiveServerList(userId);

            if (activeServers != null)
            {
                for (String activeServerName : activeServers)
                {
                    serverOperationalServices.deactivateRunningServiceInstances(userId,
                                                                                activeServerName,
                                                                                methodName,
                                                                                instanceHandler.getServerServiceInstance(userId, activeServerName, methodName),
                                                                                true);
                }
            }
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (PropertyServerException error)
        {
            exceptionHandler.capturePropertyServerException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Terminate this platform.
     *
     * @param userId  user that is issuing the request
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    public VoidResponse shutdownPlatform(String  userId)
    {
        final String methodName = "shutdownPlatform";
        final String serverName = "<null>";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateUserId(userId, serverName, methodName);

            Runtime.getRuntime().exit(1);
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


}
