/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.rest.server;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.rest.properties.OMRSAPIResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * OMRSRESTExceptionHandler contains the methods to add exceptions into the response objects for REST API calls.
 */
public class OMRSRESTExceptionHandler
{
    /**
     * Default constructor
     */
    public OMRSRESTExceptionHandler()
    {
    }

    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    void captureUserNotAuthorizedException(OMRSAPIResponse response, UserNotAuthorizedException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    void captureRepositoryErrorException(OMRSAPIResponse response, RepositoryErrorException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    void captureInvalidParameterException(OMRSAPIResponse response, InvalidParameterException error)
    {
        final String propertyName = "parameterName";

        if (error.getParameterName() == null)
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
        else
        {
            Map<String, Object> exceptionProperties = new HashMap<>();

            exceptionProperties.put(propertyName, error.getParameterName());

            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response
     * @param methodName calling method
     * @param auditLog log location for recording an unexpected exception
     */
    void captureThrowable(OMRSAPIResponse response,
                          Throwable error,
                          String methodName,
                          OMRSAuditLog auditLog)
    {
        OMRSErrorCode errorCode = OMRSErrorCode.UNEXPECTED_EXCEPTION;

        String  message = error.getMessage();

        if (message == null)
        {
            message = "null";
        }
        response.setRelatedHTTPCode(errorCode.getHTTPErrorCode());
        response.setExceptionClassName(error.getClass().getName());
        response.setExceptionErrorMessage(errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                             methodName,
                                                                             message));
        response.setExceptionSystemAction(errorCode.getSystemAction());
        response.setExceptionUserAction(errorCode.getUserAction());
        response.setExceptionProperties(null);

        if (auditLog != null)
        {
            OMRSAuditCode auditCode = OMRSAuditCode.UNEXPECTED_EXCEPTION;
            auditLog.logException(methodName,
                                  auditCode.getLogMessageId(),
                                  auditCode.getSeverity(),
                                  auditCode.getFormattedLogMessage(error.getClass().getName(),
                                                                   methodName,
                                                                   message),
                                  null,
                                  auditCode.getSystemAction(),
                                  auditCode.getUserAction(),
                                  error);
        }
    }




    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     * @param exceptionClassName class name of the exception to recreate
     */
    private void captureCheckedException(OMRSAPIResponse          response,
                                         OMRSCheckedExceptionBase error,
                                         String                   exceptionClassName)
    {
        this.captureCheckedException(response, error, exceptionClassName, null);
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     * @param exceptionClassName class name of the exception to recreate
     */
    private void captureCheckedException(OMRSAPIResponse          response,
                                         OMRSCheckedExceptionBase error,
                                         String                   exceptionClassName,
                                         Map<String, Object>      exceptionProperties)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
        response.setExceptionProperties(exceptionProperties);
    }
}
