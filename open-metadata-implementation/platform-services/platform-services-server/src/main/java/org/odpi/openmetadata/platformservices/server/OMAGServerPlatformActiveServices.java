/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerPlatformInstanceMap;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.platformservices.rest.ServerListResponse;
import org.odpi.openmetadata.platformservices.rest.ServerServicesListResponse;
import org.odpi.openmetadata.platformservices.rest.ServerStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * OMAGServerPlatformActiveServices allow an external caller to determine which servers are active on the
 * platform and the services that are active within them.
 */
public class OMAGServerPlatformActiveServices
{
    private static final Logger log = LoggerFactory.getLogger(OMAGServerPlatformActiveServices.class);

    private OMAGServerPlatformInstanceMap serverInstanceMap = new OMAGServerPlatformInstanceMap();

    private RESTExceptionHandler  exceptionHandler = new RESTExceptionHandler();


    /**
     * Return a flag to indicate if this server has ever run on this OMAG Server Platform.
     *
     * @param userId calling user
     * @param serverName server of interest
     * @return flag
     */
    public BooleanResponse  isServerKnown(String   userId,
                                          String   serverName)
    {
        final String   methodName = "isServerKnown";

        log.debug("Calling method: " + methodName);

        BooleanResponse response = new BooleanResponse();

        try
        {
            response.setFlag(serverInstanceMap.isServerKnown(userId, serverName));
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureThrowable(response, error, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the list of OMAG Servers that have run or are running in this OMAG Server Platform.
     *
     * @param userId calling user
     * @return list of OMAG server names
     */
    public ServerListResponse getKnownServerList(String userId)
    {
        final String   methodName = "getKnownServerList";

        log.debug("Calling method: " + methodName);

        ServerListResponse response = new ServerListResponse();

        try
        {
            response.setServerList(serverInstanceMap.getKnownServerList(userId));
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureThrowable(response, error, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the list of OMAG Servers that are active on this OMAG Server Platform.
     *
     * @param userId name of the user making the request
     * @return list of server names
     */
    public ServerListResponse getActiveServerList(String    userId)
    {
        final String   methodName = "getActiveServerList";

        log.debug("Calling method: " + methodName);

        ServerListResponse response = new ServerListResponse();

        try
        {
            response.setServerList(serverInstanceMap.getActiveServerList(userId));
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureThrowable(response, error, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return information about when the server has been active.
     *
     * @param userId name of the user making the request
     * @param serverName name of the server of interest
     * @return details of the server status
     */
    public ServerStatusResponse getServerStatus(String   userId,
                                                String   serverName)
    {
        final String   methodName = "getServerStatus";

        log.debug("Calling method: " + methodName);

        ServerStatusResponse response = new ServerStatusResponse();

        try
        {
            response.setServerName(serverName);
            response.setActive(serverInstanceMap.isServerActive(userId, serverName));
            response.setServerStartTime(serverInstanceMap.getServerStartTime(userId, serverName));
            response.setServerEndTime(serverInstanceMap.getServerEndTime(userId, serverName));
            response.setServerHistory(serverInstanceMap.getServerHistory(userId, serverName));
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureThrowable(response, error, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the list of services that are active on a specific OMAG Server that is active on this OMAG Server Platform.
     *
     * @param userId name of the user making the request
     * @param serverName name of the server of interest
     * @return List of service names
     */
    public ServerServicesListResponse getActiveServiceListForServer(String    userId,
                                                                    String    serverName)
    {
        final String   methodName = "getActiveServerList";

        log.debug("Calling method: " + methodName);

        ServerServicesListResponse response = new ServerServicesListResponse();

        try
        {
            response.setServerName(serverName);
            response.setServerServicesList(serverInstanceMap.getActiveServiceListForServer(userId, serverName));
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureThrowable(response, error, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }
}
