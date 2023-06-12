/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server;

import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.adminservices.rest.PlatformSecurityRequestBody;
import org.odpi.openmetadata.adminservices.server.OMAGServerErrorHandler;
import org.odpi.openmetadata.adminservices.server.OMAGServerExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataPlatformSecurityVerifier;
import org.slf4j.LoggerFactory;

/**
 * OMAGServerPlatformSecurityServices provides the capability to set up open metadata security connectors.
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
public class OMAGServerPlatformSecurityServices
{
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerPlatformSecurityServices.class),
                                                                            CommonServicesDescription.PLATFORM_SERVICES.getServiceName());

    private final OMAGServerExceptionHandler   exceptionHandler = new OMAGServerExceptionHandler();
    private final OMAGServerErrorHandler       errorHandler     = new OMAGServerErrorHandler();


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
}
