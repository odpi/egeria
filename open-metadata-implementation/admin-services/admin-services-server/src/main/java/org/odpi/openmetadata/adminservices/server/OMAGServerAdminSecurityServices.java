/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OMAGServerAdminSecurityServices provides the capability to set up the open metadata security connector
 * for a server.
 * The Open Metadata Server Security Connector verifies the authorization of calls to a specific server.
 * The connector is configured with a Connection.  The connection for the Open Metadata Server Security Connector
 * is stored in the server's configuration document.
 */
public class OMAGServerAdminSecurityServices extends TokenController
{
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminSecurityServices.class),
                                                                            CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName());

    private static final RESTExceptionHandler   restExceptionHandler = new RESTExceptionHandler();
    private final        OMAGServerErrorHandler errorHandler         = new OMAGServerErrorHandler();
    private final OMAGServerAdminStoreServices configStore  = new OMAGServerAdminStoreServices();


    /**
     * Override the default server security connector.
     *
     * @param serverName server to configure
     * @param connection connection used to create and configure the connector.
     * @return void response
     */
    public synchronized VoidResponse setServerSecurityConnection(String       serverName,
                                                                 Connection   connection)
    {
        final String methodName = "setServerSecurityConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateServerConnection(connection, serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            serverConfig.setServerSecurityConnection(connection);

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date() + " " + userId + " added configuration for an Open Metadata Server Security Connector");

            serverConfig.setAuditTrail(configAuditTrail);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the connection object for the server security connector.
     *
     * @param serverName server to retrieve configuration from
     * @return connection response
     */
    public synchronized ConnectionResponse getServerSecurityConnection(String serverName)
    {
        final String methodName = "getServerSecurityConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ConnectionResponse  response = new ConnectionResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, false, methodName);

            response.setConnection(serverConfig.getServerSecurityConnection());
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Clear the connection object for the server security connector.
     * This sets the server security back to default.
     *
     * @param serverName server to configure
     * @return connection response
     */
    public synchronized VoidResponse clearServerSecurityConnection(String   serverName)
    {
        final String methodName = "clearServerSecurityConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);

            String userId = super.getUser(CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            serverConfig.setServerSecurityConnection(null);

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date() + " " + userId + " removed configuration for an Open Metadata Server Security Connector");

            serverConfig.setAuditTrail(configAuditTrail);

            configStore.saveServerConfig(serverName, methodName, serverConfig);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
