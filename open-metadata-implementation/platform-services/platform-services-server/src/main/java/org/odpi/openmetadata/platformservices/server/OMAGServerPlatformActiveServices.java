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
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * OMAGServerPlatformActiveServices allow an external caller to determine which servers are active on the
 * platform and the services that are active within them.
 */
public class OMAGServerPlatformActiveServices extends TokenController
{
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerPlatformActiveServices.class),
                                                                            CommonServicesDescription.PLATFORM_SERVICES.getServiceName());

    private final OMAGServerPlatformInstanceMap serverInstanceMap = new OMAGServerPlatformInstanceMap();

    private final RESTExceptionHandler  exceptionHandler = new RESTExceptionHandler();

    private final String serverName        = "<null>";


    /**
     * Return the start time for this instance of the platform.
     *
     * @param delegatingUserId external userId making request
     * @return start date/time
     */
    public DateResponse getPlatformStartTime(String delegatingUserId)
    {
        final String methodName = "getPlatformStartTime";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        DateResponse response = new DateResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setDateValue(serverInstanceMap.getPlatformStartTime(userId, delegatingUserId));
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the name of the organization running this platform.
     *
     * @param delegatingUserId external userId making request
     * @return String description
     * or UserNotAuthorizedException userId is not recognized
     */
    public StringResponse getServerPlatformOrganizationName(String delegatingUserId)
    {
        final String methodName = "getServerPlatformOrganizationName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        StringResponse response = new StringResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setResultString(serverInstanceMap.getServerPlatformOrganizationName(userId, delegatingUserId));
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of access services that are registered (supported) in this OMAG Server Platform
     * and can be configured in a metadata access point or metadata server.
     *
     * @param delegatingUserId external userId making request
     * @return list of service descriptions
     */
    public RegisteredOMAGServicesResponse getRegisteredAccessServices(String delegatingUserId)
    {
        final String methodName = "getRegisteredAccessServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setServices(serverInstanceMap.getRegisteredAccessServices(userId, delegatingUserId));
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
     * @param delegatingUserId external userId making request
     * @return list of service descriptions
     */
    public RegisteredOMAGServicesResponse getRegisteredEngineServices(String delegatingUserId)
    {
        final String methodName = "getRegisteredEngineServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setServices(serverInstanceMap.getRegisteredEngineServices(userId, delegatingUserId));
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
     * @param delegatingUserId external userId making request
     * @return list of service descriptions
     */
    public RegisteredOMAGServicesResponse getRegisteredViewServices(String delegatingUserId)
    {
        final String methodName = "getRegisteredViewServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setServices(serverInstanceMap.getRegisteredViewServices(userId, delegatingUserId));
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
     * @param delegatingUserId external userId making request
     * @return list of service descriptions
     */
    public RegisteredOMAGServicesResponse getRegisteredGovernanceServices(String delegatingUserId)
    {
        final String methodName = "getRegisteredGovernanceServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setServices(serverInstanceMap.getRegisteredGovernanceServices(userId, delegatingUserId));
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
     * @param delegatingUserId external userId making request
     * @return list of service descriptions
     */
    public RegisteredOMAGServicesResponse getRegisteredCommonServices(String delegatingUserId)
    {
        final String methodName = "getRegisteredCommonServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setServices(serverInstanceMap.getRegisteredCommonServices(userId, delegatingUserId));
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
     * @param delegatingUserId external userId making request
     * @return list of service descriptions
     */
    public RegisteredOMAGServicesResponse getAllRegisteredServices(String delegatingUserId)
    {
        final String methodName = "getAllRegisteredServices";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RegisteredOMAGServicesResponse response = new RegisteredOMAGServicesResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setServices(serverInstanceMap.getAllRegisteredServices(userId, delegatingUserId));
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
     * @param delegatingUserId external userId making request
     * @param connectorProviderClassName name of the connector provider class
     * @return ConnectorType bean or exceptions that occur when trying to create the connector
     */
    public OCFConnectorTypeResponse getConnectorType(String connectorProviderClassName,
                                                     String delegatingUserId)
    {
        final String methodName = "getConnectorType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OCFConnectorTypeResponse response = new OCFConnectorTypeResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerPlatformInstanceMap.validateUserAsInvestigatorForPlatform(userId);
            if (delegatingUserId != null)
            {
                OMAGServerPlatformInstanceMap.validateUserAsInvestigatorForPlatform(delegatingUserId);
            }

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
     * @param delegatingUserId external userId making request
     * @param serverName server of interest
     * @return flag
     */
    public BooleanResponse  isServerKnown(String serverName,
                                          String delegatingUserId)
    {
        final String   methodName = "isServerKnown";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setFlag(serverInstanceMap.isServerKnown(userId, delegatingUserId, serverName));
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
     * @param delegatingUserId external userId making request
     * @return list of OMAG server names
     */
    public ServerListResponse getKnownServerList(String delegatingUserId)
    {
        final String   methodName = "getKnownServerList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ServerListResponse response = new ServerListResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setServerList(serverInstanceMap.getKnownServerList(userId, delegatingUserId));
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
     * @param delegatingUserId external userId making request
     * @return list of server names
     */
    public ServerListResponse getActiveServerList(String delegatingUserId)
    {
        final String   methodName = "getActiveServerList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ServerListResponse response = new ServerListResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setServerList(serverInstanceMap.getActiveServerList(userId, delegatingUserId));
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
     * @param delegatingUserId external userId making request
     * @param serverName name of the server of interest
     * @return details of the server status
     */
    public ServerStatusResponse getServerStatus(String serverName,
                                                String delegatingUserId)
    {
        final String   methodName = "getServerStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ServerStatusResponse response = new ServerStatusResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setServerName(serverName);

            ServerStatus serverStatus = serverInstanceMap.getServerStatus(userId, delegatingUserId, serverName, methodName);

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
     * @param delegatingUserId external userId making request
     * @param serverName name of the server of interest
     * @return List of service names
     */
    public ServerServicesListResponse getActiveServicesForServer(String serverName,
                                                                 String delegatingUserId)
    {
        final String   methodName = "getActiveServerList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ServerServicesListResponse response = new ServerServicesListResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setServerName(serverName);
            response.setServerServicesList(serverInstanceMap.getActiveServicesForServer(userId, delegatingUserId, serverName));
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
