/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerPlatformInstanceMap;
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
     * Return the list of access services that are registered (supported) in this OMAG Server Platform
     * and can be configured in a metadata access point or metadata server.
     *
     * @param userId calling user
     * @return list of service descriptions
     */
    public RegisteredOMAGServicesResponse getRegisteredAccessServices(String userId)
    {
        final String methodName = "getRegisteredAccessServices";

        log.debug("Calling method: " + methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            response.setServices(serverInstanceMap.getRegisteredAccessServices(userId));
        }
        catch (Exception error)
        {
            exceptionHandler.captureExceptions(response, error, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the list of engine services that are registered (supported) in this OMAG Server Platform
     * and can be configured in an engine hosting OMAG server.
     *
     * @param userId calling user
     * @return list of service descriptions
     */
    public RegisteredOMAGServicesResponse getRegisteredEngineServices(String userId)
    {
        final String methodName = "getRegisteredEngineServices";

        log.debug("Calling method: " + methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            response.setServices(serverInstanceMap.getRegisteredEngineServices(userId));
        }
        catch (Exception error)
        {
            exceptionHandler.captureExceptions(response, error, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the list of integration services that are implemented in this OMAG Server Platform
     * and can be configured in an integration daemon.
     *
     * @param userId calling user
     * @return list of service descriptions
     */
    public RegisteredOMAGServicesResponse getRegisteredIntegrationServices(String userId)
    {
        final String methodName = "getRegisteredIntegrationServices";

        log.debug("Calling method: " + methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            response.setServices(serverInstanceMap.getRegisteredIntegrationServices(userId));
        }
        catch (Exception error)
        {
            exceptionHandler.captureExceptions(response, error, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the list of view services that are registered (supported) in this OMAG Server Platform
     * and can be configured in a view server.
     *
     * @param userId calling user
     * @return list of service descriptions
     */
    public RegisteredOMAGServicesResponse getRegisteredViewServices(String userId)
    {
        final String methodName = "getRegisteredViewServices";

        log.debug("Calling method: " + methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            response.setServices(serverInstanceMap.getRegisteredViewServices(userId));
        }
        catch (Exception error)
        {
            exceptionHandler.captureExceptions(response, error, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the list of governance services that are registered (supported) in this OMAG Server Platform
     * and can be configured as part of a governance server.
     *
     * @param userId calling user
     * @return list of service descriptions
     */
    public RegisteredOMAGServicesResponse getRegisteredGovernanceServices(String userId)
    {
        final String methodName = "getRegisteredGovernanceServices";

        log.debug("Calling method: " + methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            response.setServices(serverInstanceMap.getRegisteredGovernanceServices(userId));
        }
        catch (Exception error)
        {
            exceptionHandler.captureExceptions(response, error, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the list of common services that are registered (supported) in this OMAG Server Platform.
     * These services are use in all types of servers.
     *
     * @param userId calling user
     * @return list of service descriptions
     */
    public RegisteredOMAGServicesResponse getRegisteredCommonServices(String userId)
    {
        final String methodName = "getRegisteredCommonServices";

        log.debug("Calling method: " + methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            response.setServices(serverInstanceMap.getRegisteredCommonServices(userId));
        }
        catch (Exception error)
        {
            exceptionHandler.captureExceptions(response, error, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the list of all services that are registered (supported) in this OMAG Server Platform.
     *
     * @param userId calling user
     * @return list of service descriptions
     */
    public RegisteredOMAGServicesResponse getAllRegisteredServices(String userId)
    {
        final String methodName = "getAllRegisteredServices";

        log.debug("Calling method: " + methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            response.setServices(serverInstanceMap.getAllRegisteredServices(userId));
        }
        catch (Exception error)
        {
            exceptionHandler.captureExceptions(response, error, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


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
        catch (Exception error)
        {
            exceptionHandler.captureExceptions(response, error, methodName);
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
        catch (Exception error)
        {
            exceptionHandler.captureExceptions(response, error, methodName);
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
        catch (Exception error)
        {
            exceptionHandler.captureExceptions(response, error, methodName);
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
            response.setServerType(serverInstanceMap.getServerType(userId, serverName, methodName));
            response.setActive(serverInstanceMap.isServerActive(userId, serverName));
            response.setServerStartTime(serverInstanceMap.getServerStartTime(userId, serverName));
            response.setServerEndTime(serverInstanceMap.getServerEndTime(userId, serverName));
            response.setServerHistory(serverInstanceMap.getServerHistory(userId, serverName));
        }
        catch (Exception error)
        {
            exceptionHandler.captureExceptions(response, error, methodName);
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
        catch (Exception error)
        {
            exceptionHandler.captureExceptions(response, error, methodName, null);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }
}
