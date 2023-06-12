/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.adminservices.rest.PlatformSecurityRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataPlatformSecurityVerifier;
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
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerAdminSecurityServices.class),
                                                                            CommonServicesDescription.ADMINISTRATION_SERVICES.getServiceName());

    private final OMAGServerExceptionHandler   exceptionHandler = new OMAGServerExceptionHandler();
    private final OMAGServerErrorHandler       errorHandler = new OMAGServerErrorHandler();
    private final OMAGServerAdminStoreServices configStore  = new OMAGServerAdminStoreServices();

    /**
     * Override the default platform security connector.
     *
     * @param userId calling user.
     * @param requestBody containing serverPlatformURL (URL Root of the server platform) and
     * connection used to create and configure the connector.
     * @return void response
     */
    public synchronized VoidResponse setPlatformSecurityConnection(String                      userId,
                                                                   PlatformSecurityRequestBody requestBody)
    {
        final String methodName = "setPlatformSecurityConnection";

        RESTCallToken token = restCallLogger.logRESTCall(null, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            if (requestBody == null)
            {
                exceptionHandler.handleNoRequestBody(userId, methodName, "<null>");
            }
            else
            {
                errorHandler.validatePlatformConnection(requestBody.getPlatformSecurityConnection(), methodName);

                OpenMetadataPlatformSecurityVerifier.setPlatformSecurityConnection(userId,
                                                                                   requestBody.getUrlRoot(),
                                                                                   requestBody.getPlatformSecurityConnection());
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
        catch (Exception   error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

        RESTCallToken token = restCallLogger.logRESTCall(null, userId, methodName);

        ConnectionResponse  response = new ConnectionResponse();

        try
        {
            response.setConnection(OpenMetadataPlatformSecurityVerifier.getPlatformSecurityConnection(userId));
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception   error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

        RESTCallToken token = restCallLogger.logRESTCall(null, userId, methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            OpenMetadataPlatformSecurityVerifier.clearPlatformSecurityConnection(userId);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception   error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Override the default server security connector.
     *
     * @param userId calling user.
     * @param serverName server to configure
     * @param connection connection used to create and configure the connector.
     * @return void response
     */
    public synchronized VoidResponse setServerSecurityConnection(String       userId,
                                                                 String       serverName,
                                                                 Connection   connection)
    {
        final String methodName = "setServerSecurityConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateServerConnection(connection, serverName, methodName);

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
        catch (Exception   error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

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
        catch (Exception   error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

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
        catch (Exception   error)
        {
            exceptionHandler.capturePlatformRuntimeException(methodName, response, error);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
