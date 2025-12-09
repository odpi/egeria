/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server;

import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.adminservices.rest.PlatformSecurityRequestBody;
import org.odpi.openmetadata.adminservices.server.OMAGServerErrorHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataPlatformSecurityVerifier;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

/**
 * OMAGServerPlatformSecurityServices provides the capability to set up open metadata security connectors.
 * The open metadata security connectors validate the authorization of a user to access specific
 * services and metadata in Egeria.  There are 2 connectors:
 *  (1) The Open Metadata Platform Security Connector verifies the authorization of calls to the OMAG
 *  Server Platform Services that are independent of a server.
 *  (2) The Open Metadata Server Security Connector verifies the authorization of calls to a specific server.
 * The connectors are configured with a Connection.  The connection for the Open Metadata Server Security Connector
 * is stored in the server's configuration document.  The connection for the Open Metadata Platform Security Connector
 * is added dynamically when the platform starts.
 */
public class OMAGServerPlatformSecurityServices extends TokenController
{
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerPlatformSecurityServices.class),
                                                                            CommonServicesDescription.PLATFORM_SERVICES.getServiceName());

    private static final RESTExceptionHandler   restExceptionHandler = new RESTExceptionHandler();
    private final        OMAGServerErrorHandler errorHandler         = new OMAGServerErrorHandler();


    /**
     * Override the default platform security connector.
     *
     * @param requestBody containing serverPlatformURL (URL Root of the server platform) and
     * connection used to create and configure the connector.
     * @return void response
     */
    public synchronized VoidResponse setPlatformSecurityConnection(PlatformSecurityRequestBody requestBody)
    {
        final String methodName = "setPlatformSecurityConnection";

        RESTCallToken token = restCallLogger.logRESTCall(null, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, "<null>");
            }
            else
            {
                errorHandler.validatePlatformConnection(requestBody.getPlatformSecurityConnection(), methodName);

                OpenMetadataPlatformSecurityVerifier.setPlatformSecurityConnection(userId,
                                                                                   requestBody.getUrlRoot(),
                                                                                   requestBody.getPlatformSecurityConnection());
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
     * Return the connection object for the platform security connector.
     *
     * @return connection response
     */
    public synchronized ConnectionResponse getPlatformSecurityConnection()
    {
        final String methodName = "getPlatformSecurityConnection";

        RESTCallToken token = restCallLogger.logRESTCall(null, methodName);

        ConnectionResponse  response = new ConnectionResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.SERVER_OPERATIONS.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setConnection(OpenMetadataPlatformSecurityVerifier.getPlatformSecurityConnection(userId));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Clear the connection object for the platform security connector.
     * This sets the platform security back to default.
     *
     * @return connection response
     */
    public synchronized VoidResponse clearPlatformSecurityConnection()
    {
        final String methodName = "clearPlatformSecurityConnection";

        RESTCallToken token = restCallLogger.logRESTCall(null, methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.SERVER_OPERATIONS.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OpenMetadataPlatformSecurityVerifier.clearPlatformSecurityConnection(userId);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
