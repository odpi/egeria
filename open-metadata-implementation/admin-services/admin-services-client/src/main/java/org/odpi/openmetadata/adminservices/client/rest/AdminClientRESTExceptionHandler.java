/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client.rest;

import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * AdminClientRESTExceptionHandler is managing the receipt of exceptions in the response from a REST call
 * and converting them into Admin Exceptions.
 */
public class AdminClientRESTExceptionHandler extends RESTExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(AdminClientRESTExceptionHandler.class);

    /**
     * Throw an exception if it is encoded in the REST response.
     *
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException configuration error
     */
    public void detectAndThrowAdminExceptions(FFDCResponseBase restResult) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  OMAGConfigurationErrorException
    {
        final String   invalidParameterExceptionClassName = InvalidParameterException.class.getName();
        final String   userNotAuthorizedExceptionClassName = UserNotAuthorizedException.class.getName();

        if (restResult != null)
        {
            String exceptionClassName = restResult.getExceptionClassName();

            if (exceptionClassName != null)
            {
                log.error("FFDC Response: {}", restResult.toString());

                if (exceptionClassName.equals(invalidParameterExceptionClassName))
                {
                    this.throwInvalidParameterException(restResult);
                }
                else if (exceptionClassName.equals(userNotAuthorizedExceptionClassName))
                {
                    this.throwUserNotAuthorizedException(restResult);
                }
                else
                {
                    this.throwOMAGConfigurationException(restResult);
                }
            }
            else
            {
                log.debug("FFDC Response: {}", restResult.toString());
            }
        }
    }


    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param restResult  response from UserNotAuthorizedException encoded exception from the server.
     *
     * @throws InvalidParameterException encoded exception from the server
     */
    private void throwInvalidParameterException(FFDCResponseBase restResult) throws InvalidParameterException
    {
        InvalidParameterException error = new InvalidParameterException(restResult.getRelatedHTTPCode(),
                                                                                this.getClass().getName(),
                                                                                restResult.getActionDescription(),
                                                                                restResult.getExceptionErrorMessage(),
                                                                                restResult.getExceptionErrorMessageId(),
                                                                                restResult.getExceptionErrorMessageParameters(),
                                                                                restResult.getExceptionSystemAction(),
                                                                                restResult.getExceptionUserAction(),
                                                                                restResult.getExceptionCausedBy(),
                                                                                null,
                                                                                restResult.getExceptionProperties());

        log.error("Invalid Parameter Exception", error);
        throw error;
    }


    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param restResult  response from UserNotAuthorizedException encoded exception from the server.
     *
     * @throws UserNotAuthorizedException encoded exception from the server
     */
    private void throwUserNotAuthorizedException(FFDCResponseBase restResult) throws UserNotAuthorizedException
    {
        UserNotAuthorizedException error = new UserNotAuthorizedException(restResult.getRelatedHTTPCode(),
                                                                          this.getClass().getName(),
                                                                          restResult.getActionDescription(),
                                                                          restResult.getExceptionErrorMessage(),
                                                                          restResult.getExceptionErrorMessageId(),
                                                                          restResult.getExceptionErrorMessageParameters(),
                                                                          restResult.getExceptionSystemAction(),
                                                                          restResult.getExceptionUserAction(),
                                                                          restResult.getExceptionCausedBy(),
                                                                          null,
                                                                          restResult.getExceptionProperties());

        log.error("User Not Authorized Exception", error);
        throw error;
    }


    /**
     * Throw an OMAGConfigurationErrorException if it is encoded in the REST response.
     *
     * @param restResult  response from UserNotAuthorizedException encoded exception from the server.
     *
     * @throws OMAGConfigurationErrorException encoded exception from the server
     */
    private void throwOMAGConfigurationException(FFDCResponseBase restResult) throws OMAGConfigurationErrorException
    {
        OMAGConfigurationErrorException error = new OMAGConfigurationErrorException(restResult.getRelatedHTTPCode(),
                                                                                    this.getClass().getName(),
                                                                                    restResult.getActionDescription(),
                                                                                    restResult.getExceptionErrorMessage(),
                                                                                    restResult.getExceptionErrorMessageId(),
                                                                                    restResult.getExceptionErrorMessageParameters(),
                                                                                    restResult.getExceptionSystemAction(),
                                                                                    restResult.getExceptionUserAction(),
                                                                                    restResult.getExceptionCausedBy(),
                                                                                    restResult.getExceptionProperties());

        log.error("Configuration Error Exception", error);
        throw error;
    }


    /**
     * Provide detailed logging for exceptions.
     *
     * @param serverPlatformURLRoot platform that was being called.
     * @param methodName calling method
     * @param error resulting exception
     * @throws OMAGConfigurationErrorException wrapping exception
     */
    public void logRESTCallException(String    serverPlatformURLRoot,
                                     String    methodName,
                                     Exception error) throws OMAGConfigurationErrorException
    {
        throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                                     serverPlatformURLRoot,
                                                                                                                     error.getMessage()),
                                                  this.getClass().getName(),
                                                  methodName,
                                                  error);
    }
}
