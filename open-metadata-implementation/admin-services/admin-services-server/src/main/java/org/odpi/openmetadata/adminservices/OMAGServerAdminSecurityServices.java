/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataPlatformSecurityVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OMAGServerAdminSecurityServices provides the capability to set up open metadata security connectors.
 *
 * The open metadata security connectors validate the authorization of a user to access specific
 * services and metadata in Egeria.  There are 2 connectors:
 *
 *  The Open Metadata Platform Security Connector verifies the authorization of calls to the OMAG
 *  Server Platform Services that are independent of a server.
 *
 *  The Open Metadata Server Security Connector verifies the authorization of calls to a specific server.
 *
 * The connectors are configured with a Connection.  The connection for the Open Metadata Server Security Connector
 * is stored in the server's configuration document.  The connection for the Open Metadata Platform Security Connector
 * is added dynamically when the platform starts.
 */
public class OMAGServerAdminSecurityServices
{
    private static final Logger log = LoggerFactory.getLogger(OMAGServerAdminSecurityServices.class);

    private OMAGServerExceptionHandler   exceptionHandler = new OMAGServerExceptionHandler();
    private OMAGServerErrorHandler       errorHandler = new OMAGServerErrorHandler();
    private OMAGServerAdminStoreServices configStore  = new OMAGServerAdminStoreServices();

    /**
     * Override the default platform security connector.
     *
     * @param userId calling user.
     * @param connection connection used to create and configure the connector.
     */
    public synchronized VoidResponse setPlatformSecurityConnection(String       userId,
                                                                   Connection   connection)
    {
        final String methodName = "setPlatformSecurityConnection";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateConnection(connection, methodName);

            OpenMetadataPlatformSecurityVerifier.setPlatformSecurityConnection(userId, connection);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable   error)
        {
            exceptionHandler.captureRuntimeException(methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the connection object for the platform security connector.
     *
     * @param userId calling user
     * @return connection response
     */
    public synchronized ConnectionResponse getPlatformSecurityConnection(String       userId)
    {
        final String methodName = "getPlatformSecurityConnection";

        log.debug("Calling method: " + methodName);

        ConnectionResponse  response = new ConnectionResponse();

        try
        {
            response.setConnection(OpenMetadataPlatformSecurityVerifier.getPlatformSecurityConnection(userId));
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable   error)
        {
            exceptionHandler.captureRuntimeException(methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Clear the connection object for the platform security connector.
     * This sets the platform security back to default.
     *
     * @param userId calling user
     * @return connection response
     */
    public synchronized VoidResponse clearPlatformSecurityConnection(String   userId)
    {
        final String methodName = "clearPlatformSecurityConnection";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.clearPlatformSecurityConnection(userId);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable   error)
        {
            exceptionHandler.captureRuntimeException(methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Override the default server security connector.
     *
     * @param userId calling user.
     * @param serverName server to configure
     * @param connection connection used to create and configure the connector.
     */
    public synchronized VoidResponse setServerSecurityConnection(String       userId,
                                                                 String       serverName,
                                                                 Connection   connection)
    {
        final String methodName = "setServerSecurityConnection";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateConnection(connection, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            serverConfig.setServerSecurityConnection(connection);

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date().toString() + " " + userId + " added configuration for an Open Metadata Server Security Connector");

            serverConfig.setAuditTrail(configAuditTrail);

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
        catch (Throwable   error)
        {
            exceptionHandler.captureRuntimeException(methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the connection object for the server security connector.
     *
     * @param userId calling user
     * @param serverName server to retrieve configuration from
     * @return connection response
     */
    public synchronized ConnectionResponse getServerSecurityConnection(String       userId,
                                                                       String       serverName)
    {
        final String methodName = "getServerSecurityConnection";

        log.debug("Calling method: " + methodName);

        ConnectionResponse  response = new ConnectionResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            response.setConnection(serverConfig.getServerSecurityConnection());
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable   error)
        {
            exceptionHandler.captureRuntimeException(methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Clear the connection object for the server security connector.
     * This sets the server security back to default.
     *
     * @param userId calling user
     * @param serverName server to configure
     * @return connection response
     */
    public synchronized VoidResponse clearServerSecurityConnection(String   userId,
                                                                   String   serverName)
    {
        final String methodName = "clearServerSecurityConnection";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGServerConfig serverConfig = configStore.getServerConfig(userId, serverName, methodName);

            serverConfig.setServerSecurityConnection(null);

            List<String> configAuditTrail = serverConfig.getAuditTrail();

            if (configAuditTrail == null)
            {
                configAuditTrail = new ArrayList<>();
            }

            configAuditTrail.add(new Date().toString() + " " + userId + " removed configuration for an Open Metadata Server Security Connector");

            serverConfig.setAuditTrail(configAuditTrail);

            configStore.saveServerConfig(serverName, methodName, serverConfig);

        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Throwable   error)
        {
            exceptionHandler.captureRuntimeException(methodName, response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }
}
