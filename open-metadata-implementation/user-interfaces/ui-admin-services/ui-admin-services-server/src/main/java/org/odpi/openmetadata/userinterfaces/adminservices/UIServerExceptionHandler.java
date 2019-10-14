/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.UIAdminErrorCode;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.exception.UIConfigurationErrorException;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.exception.UIInvalidParameterException;
import org.odpi.openmetadata.userinterface.adminservices.ffdc.exception.UINotAuthorizedException;

/**
 * OMAGServerExceptionHandler provides common error handling routines for the ui admin services
 */
class UIServerExceptionHandler extends RESTExceptionHandler
{
    /**
     * Default constructor
     */
    public UIServerExceptionHandler()
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
    void captureRuntimeException(String            serverName,
                                 String            methodName,
                                 FFDCResponseBase  response,
                                 Throwable         runtimeException)
    {
        UIAdminErrorCode errorCode = UIAdminErrorCode.UNEXPECTED_EXCEPTION;
        String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName,
                                                                                                        methodName,
                                                                                                        runtimeException.getClass().getName(),
                                                                                                        runtimeException.getMessage());

        UIConfigurationErrorException error =  new UIConfigurationErrorException(errorCode.getHTTPErrorCode(),
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
    void captureRuntimeException(String            methodName,
                                 FFDCResponseBase  response,
                                 Throwable         runtimeException)
    {
        UIAdminErrorCode errorCode = UIAdminErrorCode.UNEXPECTED_PLATFORM_EXCEPTION;
        String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                        runtimeException.getClass().getName(),
                                                                                                        runtimeException.getMessage());

        UIConfigurationErrorException error =  new UIConfigurationErrorException(errorCode.getHTTPErrorCode(),
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
    void captureConfigurationErrorException(FFDCResponseBase response, UIConfigurationErrorException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    void captureInvalidParameterException(FFDCResponseBase response, UIInvalidParameterException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    void captureNotAuthorizedException(FFDCResponseBase response, UINotAuthorizedException error)
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
