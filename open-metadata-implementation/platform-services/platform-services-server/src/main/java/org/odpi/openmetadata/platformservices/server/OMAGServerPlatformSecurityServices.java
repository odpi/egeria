/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformservices.server;

import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.adminservices.rest.ConnectionResponse;
import org.odpi.openmetadata.adminservices.rest.PlatformSecurityRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.adminservices.server.OMAGServerErrorHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
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
     * @param delegatingUserId external userId making request
     * @param requestBody containing serverPlatformURL (URL Root of the server platform) and
     * connection used to create and configure the connector.
     * @return void response
     */
    public synchronized VoidResponse setPlatformSecurityConnection(String                      delegatingUserId,
                                                                   PlatformSecurityRequestBody requestBody)
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
                                                                                   delegatingUserId,
                                                                                   requestBody.getUrlRoot(),
                                                                                   requestBody.getPlatformSecurityConnection());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the connection object for the platform security connector.
     *
     * @param delegatingUserId external userId making request
     * @return connection response
     */
    public synchronized ConnectionResponse getPlatformSecurityConnection(String delegatingUserId)
    {
        final String methodName = "getPlatformSecurityConnection";

        RESTCallToken token = restCallLogger.logRESTCall(null, methodName);

        ConnectionResponse  response = new ConnectionResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setConnection(OpenMetadataPlatformSecurityVerifier.getPlatformSecurityConnection(userId, delegatingUserId));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Clear the connection object for the platform security connector.
     * This sets the platform security back to default.
     *
     * @param delegatingUserId external userId making request
     * @return connection response
     */
    public synchronized VoidResponse clearPlatformSecurityConnection(String delegatingUserId)
    {
        final String methodName = "clearPlatformSecurityConnection";

        RESTCallToken token = restCallLogger.logRESTCall(null, methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OpenMetadataPlatformSecurityVerifier.clearPlatformSecurityConnection(userId, delegatingUserId);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Set up details of a user account with the platform security connector.
     *
     * @param delegatingUserId external userId making request
     * @param requestBody containing the user account properties.
     * @return void response
     */
    public synchronized VoidResponse setUserAccount(String                 delegatingUserId,
                                                    UserAccountRequestBody requestBody)
    {
        final String methodName    = "setUserAccount";
        final String parameterName = "userAccount";
        final String userIdName    = "userAccount.userId";

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
                errorHandler.validatePropertyNotNull(requestBody.getUserAccount(), parameterName, "<null>", methodName);
                errorHandler.validatePropertyNotNull(requestBody.getUserAccount().getUserId(), userIdName, "<null>", methodName);

                OpenMetadataPlatformSecurityVerifier.updateUserAccount(userId, delegatingUserId, requestBody.getUserAccount());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the user account from the platform security connector.
     *
     * @param delegatingUserId external userId making request
     * @param accountUserId    user id of the account
     * @return connection response
     */
    public synchronized UserAccountResponse getUserAccount(String delegatingUserId,
                                                           String accountUserId)
    {
        final String methodName = "getUserAccount";

        RESTCallToken token = restCallLogger.logRESTCall(null, methodName);

        UserAccountResponse response = new UserAccountResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setUserAccount(OpenMetadataPlatformSecurityVerifier.getUserAccount(userId, delegatingUserId, accountUserId));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Clear a user's account from the platform security connector.
     *
     * @param delegatingUserId external userId making request
     * @param accountUserId    user id of the account
     * @return void response
     */
    public synchronized VoidResponse deleteUserAccount(String delegatingUserId,
                                                       String accountUserId)
    {
        final String methodName = "deleteUserAccount";

        RESTCallToken token = restCallLogger.logRESTCall(null, methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.SERVER_OPERATIONS.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OpenMetadataPlatformSecurityVerifier.deleteUserAccount(userId, delegatingUserId, accountUserId);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Set up details of a user account with the platform security connector.
     *
     * @param delegatingUserId external userId making request
     * @param requestBody containing the user account properties.
     * @return void response
     */
    public synchronized VoidResponse setSecurityAccessControl(String                           delegatingUserId,
                                                              SecurityAccessControlRequestBody requestBody)
    {
        final String methodName    = "setSecurityAccessControl";
        final String parameterName = "securityAccessControl";
        final String controlName   = "securityAccessControl.userId";

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
                errorHandler.validatePropertyNotNull(requestBody.getSecurityAccessControl(), parameterName, "<null>", methodName);
                errorHandler.validatePropertyNotNull(requestBody.getSecurityAccessControl().getControlName(), controlName, "<null>", methodName);

                OpenMetadataPlatformSecurityVerifier.setSecurityAccessControl(userId, delegatingUserId, requestBody.getSecurityAccessControl());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the user account from the platform security connector.
     *
     * @param delegatingUserId external userId making request
     * @param accountUserId    user id of the account
     * @return connection response
     */
    public synchronized SecurityAccessControlResponse getSecurityAccessControl(String delegatingUserId,
                                                                               String accountUserId)
    {
        final String methodName = "getSecurityAccessControl";

        RESTCallToken token = restCallLogger.logRESTCall(null, methodName);

        SecurityAccessControlResponse response = new SecurityAccessControlResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.PLATFORM_SERVICES.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            response.setSecurityAccessControl(OpenMetadataPlatformSecurityVerifier.getSecurityAccessControl(userId, delegatingUserId, accountUserId));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Clear a user's account from the platform security connector.
     *
     * @param delegatingUserId external userId making request
     * @param accountUserId    user id of the account
     * @return void response
     */
    public synchronized VoidResponse deleteSecurityAccessControl(String delegatingUserId,
                                                       String accountUserId)
    {
        final String methodName = "deleteSecurityAccessControl";

        RESTCallToken token = restCallLogger.logRESTCall(null, methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            String userId = super.getUser(CommonServicesDescription.SERVER_OPERATIONS.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            OpenMetadataPlatformSecurityVerifier.deleteSecurityAccessControl(userId, delegatingUserId, accountUserId);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }
}
