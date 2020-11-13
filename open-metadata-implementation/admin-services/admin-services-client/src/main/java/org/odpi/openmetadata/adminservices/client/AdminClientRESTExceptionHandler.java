/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * AdminClientRESTExceptionHandler is managing the receipt of exceptions in the response from a REST call
 * and converting them into Admin Exceptions.
 */
class AdminClientRESTExceptionHandler extends RESTExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(AdminClientRESTExceptionHandler.class);

    /**
     * Throw an exception if it is encoded in the REST response.
     *
     * @param restResult  response from the rest call.  This generated in the remote server.
     *
     * @throws OMAGInvalidParameterException one of the parameters is invalid.
     * @throws OMAGNotAuthorizedException the user is not authorized to make this request.
     * @throws OMAGConfigurationErrorException configuration error
     */
    void detectAndThrowAdminExceptions(FFDCResponseBase restResult) throws OMAGInvalidParameterException,
                                                                           OMAGNotAuthorizedException,
                                                                           OMAGConfigurationErrorException
    {
        final String   invalidParameterExceptionClassName = OMAGInvalidParameterException.class.getName();
        final String   userNotAuthorizedExceptionClassName = OMAGNotAuthorizedException.class.getName();

        if (restResult != null)
        {
            String exceptionClassName = restResult.getExceptionClassName();

            if (exceptionClassName != null)
            {
                log.error("FFDC Response: {}", restResult.toString());

                if (exceptionClassName.equals(invalidParameterExceptionClassName))
                {
                    this.throwOMAGInvalidParameterException(restResult);
                }
                else if (exceptionClassName.equals(userNotAuthorizedExceptionClassName))
                {
                    this.throwOMAGNotAuthorizedException(restResult);
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
     * Throw an OMAGInvalidParameterException if it is encoded in the REST response.
     *
     * @param restResult  response from UserNotAuthorizedException encoded exception from the server.
     *
     * @throws OMAGInvalidParameterException encoded exception from the server
     */
    private void throwOMAGInvalidParameterException(FFDCResponseBase restResult) throws OMAGInvalidParameterException
    {
        OMAGInvalidParameterException error = new OMAGInvalidParameterException(restResult.getRelatedHTTPCode(),
                                                                                this.getClass().getName(),
                                                                                restResult.getActionDescription(),
                                                                                restResult.getExceptionErrorMessage(),
                                                                                restResult.getExceptionErrorMessageId(),
                                                                                restResult.getExceptionErrorMessageParameters(),
                                                                                restResult.getExceptionSystemAction(),
                                                                                restResult.getExceptionUserAction(),
                                                                                restResult.getExceptionCausedBy(),
                                                                                restResult.getExceptionProperties());

        log.error("Invalid Parameter Exception", error);
        throw error;
    }


    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param restResult  response from UserNotAuthorizedException encoded exception from the server.
     *
     * @throws OMAGNotAuthorizedException encoded exception from the server
     */
    private void throwOMAGNotAuthorizedException(FFDCResponseBase restResult) throws OMAGNotAuthorizedException
    {
        OMAGNotAuthorizedException error = new OMAGNotAuthorizedException(restResult.getRelatedHTTPCode(),
                                                                          this.getClass().getName(),
                                                                          restResult.getActionDescription(),
                                                                          restResult.getExceptionErrorMessage(),
                                                                          restResult.getExceptionErrorMessageId(),
                                                                          restResult.getExceptionErrorMessageParameters(),
                                                                          restResult.getExceptionSystemAction(),
                                                                          restResult.getExceptionUserAction(),
                                                                          restResult.getExceptionCausedBy(),
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
    void logRESTCallException(String    serverPlatformURLRoot,
                              String    methodName,
                              Throwable error) throws OMAGConfigurationErrorException
    {
        throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                                     serverPlatformURLRoot,
                                                                                                                     error.getMessage()),
                                                  this.getClass().getName(),
                                                  methodName,
                                                  error);
    }
}
