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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OMAGServerExceptionHandler provides common error handling routines for the admin services
 */
public class OMAGServerExceptionHandler extends RESTExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(RESTExceptionHandler.class);

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
        log.error("Unexpected exception", runtimeException);

        OMAGConfigurationErrorException error =  new OMAGConfigurationErrorException(OMAGAdminErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(serverName,
                                                                                                                                                  methodName,
                                                                                                                                                  runtimeException.getClass().getName(),
                                                                                                                                                  runtimeException.getMessage()),
                                                                                     this.getClass().getName(),
                                                                                     methodName,
                                                                                     runtimeException);

        log.error("Returning sanitized exception", error);

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
        log.error("Unexpected platform exception", runtimeException);

        OMAGConfigurationErrorException error =  new OMAGConfigurationErrorException(OMAGAdminErrorCode.UNEXPECTED_PLATFORM_EXCEPTION.getMessageDefinition(methodName,
                                                                                                                                                           runtimeException.getClass().getName(),
                                                                                                                                                           runtimeException.getMessage()),
                                                                                     this.getClass().getName(),
                                                                                     methodName,
                                                                                     runtimeException);

        log.error("Returning sanitized platform exception", error);

        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    void captureConfigurationErrorException(FFDCResponseBase response, OMAGConfigurationErrorException error)
    {
        log.error("Configuration error returned", error);

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
        log.error("Invalid parameter error returned", error);

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
        log.error("(OMAG) User not authorized error returned", error);

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
        log.error("User not authorized error returned", error);

        captureCheckedException(response, error, error.getClass().getName());
    }
}
