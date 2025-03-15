/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server;


import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerPlatformInstanceMap;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.serveroperations.properties.ServerStatus;
import org.odpi.openmetadata.platformservices.rest.ServerListResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerServicesListResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerStatusResponse;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * OMAGServerPlatformActiveServices allow an external caller to determine which servers are active on the
 * platform and the services that are active within them.
 */
public class OMAGServerPlatformActiveServices
{
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerPlatformActiveServices.class),
                                                                            CommonServicesDescription.PLATFORM_SERVICES.getServiceName());

    private final OMAGServerPlatformInstanceMap serverInstanceMap = new OMAGServerPlatformInstanceMap();

    private final RESTExceptionHandler  exceptionHandler = new RESTExceptionHandler();

    private final String serverName        = "<null>";
    private final Date   platformStartTime = new Date();


    /**
     * Return the start time for this instance of the platform.
     *
     * @param userId calling user
     * @return start date/time
     */
    public Date getPlatformStartTime(String userId)
    {
        final String methodName = "getPlatformStartTime";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);
        restCallLogger.logRESTCallReturn(token, platformStartTime.toString());

        return platformStartTime;
    }


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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            response.setServices(serverInstanceMap.getRegisteredAccessServices(userId));
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            response.setServices(serverInstanceMap.getRegisteredEngineServices(userId));
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            response.setServices(serverInstanceMap.getRegisteredIntegrationServices(userId));
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            response.setServices(serverInstanceMap.getRegisteredViewServices(userId));
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            response.setServices(serverInstanceMap.getRegisteredGovernanceServices(userId));
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            response.setServices(serverInstanceMap.getRegisteredCommonServices(userId));
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            response.setServices(serverInstanceMap.getAllRegisteredServices(userId));
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the connector type for the requested connector provider after validating that the
     * connector provider is available on the OMAGServerPlatform's class path.  This method is for tools that are configuring
     * connectors into an Egeria server.  It does not validate that the connector will load and initialize.
     *
     * @param userId calling user
     * @param connectorProviderClassName name of the connector provider class
     * @return ConnectorType bean or exceptions that occur when trying to create the connector
     */
    public OCFConnectorTypeResponse getConnectorType(String userId,
                                                     String connectorProviderClassName)
    {
        final String methodName = "getConnectorType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        OCFConnectorTypeResponse response = new OCFConnectorTypeResponse();

        try
        {
            OMAGServerPlatformInstanceMap.validateUserAsInvestigatorForPlatform(userId);

            Class<?> connectorProviderClass     = Class.forName(connectorProviderClassName);
            Object   potentialConnectorProvider = connectorProviderClass.getDeclaredConstructor().newInstance();

            ConnectorProvider connectorProvider = (ConnectorProvider) potentialConnectorProvider;

            ConnectorType connectorType = connectorProvider.getConnectorType();

            if (connectorType == null)
            {
                connectorType = new ConnectorType();

                connectorType.setConnectorProviderClassName(connectorProviderClassName);
            }

            response.setConnectorType(connectorType);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        BooleanResponse response = new BooleanResponse();

        try
        {
            response.setFlag(serverInstanceMap.isServerKnown(userId, serverName));
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ServerListResponse response = new ServerListResponse();

        try
        {
            response.setServerList(serverInstanceMap.getKnownServerList(userId));
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ServerListResponse response = new ServerListResponse();

        try
        {
            response.setServerList(serverInstanceMap.getActiveServerList(userId));
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ServerStatusResponse response = new ServerStatusResponse();

        try
        {
            response.setServerName(serverName);

            ServerStatus serverStatus = serverInstanceMap.getServerStatus(userId, serverName, methodName);

            response.setServerType(serverStatus.getServerType());
            response.setActive(serverStatus.getIsActive());
            response.setServerStartTime(serverStatus.getServerStartTime());
            response.setServerEndTime(serverStatus.getServerEndTime());
            response.setServerHistory(serverStatus.getServerHistory());
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of services that are active on a specific OMAG Server that is active on this OMAG Server Platform.
     *
     * @param userId name of the user making the request
     * @param serverName name of the server of interest
     * @return List of service names
     */
    public ServerServicesListResponse getActiveServicesForServer(String    userId,
                                                                 String    serverName)
    {
        final String   methodName = "getActiveServerList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ServerServicesListResponse response = new ServerServicesListResponse();

        try
        {
            response.setServerName(serverName);
            response.setServerServicesList(serverInstanceMap.getActiveServicesForServer(userId, serverName));
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Shutdown this OMAG Server Platform.
     *
     * @param userId name of the user making the request
     * @return List of service names
     */
    public VoidResponse shutdownPlatform(String userId)
    {
        final String   methodName = "shutdownPlatform";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            OMAGServerPlatformInstanceMap.validateUserAsOperatorForPlatform(userId);

            System.exit(0);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
