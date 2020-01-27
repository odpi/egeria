/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * OMAGServerExceptionHandler provides common error handling routines for the admin services
 */
public class OMAGServerExceptionHandler extends RESTExceptionHandler
{
    /**
     * Default constructor
     */
    public OMAGServerExceptionHandler()
    {
    }


    /**
     * Set the exception information into the response.
     *
     * @param serverName  this server instance
     * @param methodName  method called
     * @param response  REST Response
     * @param runtimeException returned error.
     */
    public void capturePlatformRuntimeException(String           serverName,
                                                String           methodName,
                                                FFDCResponseBase response,
                                                Throwable        runtimeException)
    {
        OMAGAdminErrorCode errorCode = OMAGAdminErrorCode.UNEXPECTED_EXCEPTION;
        String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName,
                                                                                                        methodName,
                                                                                                        runtimeException.getClass().getName(),
                                                                                                        runtimeException.getMessage());

        OMAGConfigurationErrorException error =  new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                                                     this.getClass().getName(),
                                                                                     methodName,
                                                                                     errorMessage,
                                                                                     errorCode.getSystemAction(),
                                                                                     errorCode.getUserAction(),
                                                                                     runtimeException);
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param methodName  method called
     * @param response  REST Response
     * @param runtimeException returned error.
     */
    public void capturePlatformRuntimeException(String           methodName,
                                                FFDCResponseBase response,
                                                Throwable        runtimeException)
    {
        OMAGAdminErrorCode errorCode = OMAGAdminErrorCode.UNEXPECTED_PLATFORM_EXCEPTION;
        String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                        runtimeException.getClass().getName(),
                                                                                                        runtimeException.getMessage());

        OMAGConfigurationErrorException error =  new OMAGConfigurationErrorException(errorCode.getHTTPErrorCode(),
                                                                                     this.getClass().getName(),
                                                                                     methodName,
                                                                                     errorMessage,
                                                                                     errorCode.getSystemAction(),
                                                                                     errorCode.getUserAction(),
                                                                                     runtimeException);
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    public void captureConfigurationErrorException(FFDCResponseBase response, OMAGConfigurationErrorException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    void captureInvalidParameterException(FFDCResponseBase response, OMAGInvalidParameterException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    public void captureNotAuthorizedException(FFDCResponseBase response, OMAGNotAuthorizedException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    void captureNotAuthorizedException(FFDCResponseBase response, UserNotAuthorizedException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }
}
