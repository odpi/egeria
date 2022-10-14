/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.rest.server;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.rest.properties.OMRSAPIResponse;
import org.odpi.openmetadata.repositoryservices.rest.services.OMRSRepositoryServicesInstanceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * OMRSRESTExceptionHandler contains the methods to add exceptions into the response objects for REST API calls.
 */
class OMRSRESTExceptionHandler
{
    private static final MessageFormatter messageFormatter = new MessageFormatter();
    private final OMRSRepositoryServicesInstanceHandler instanceHandler;

    private static final Logger log = LoggerFactory.getLogger(OMRSRESTExceptionHandler.class);

    /**
     * Default constructor
     *
     * @param instanceHandler handler for retrieving a server's audit log.
     */
    OMRSRESTExceptionHandler(OMRSRepositoryServicesInstanceHandler instanceHandler)
    {
        this.instanceHandler = instanceHandler;
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
     * @param userId calling user
     * @param serverName targeted server instance
     * @param methodName calling method
     */
    void captureGenericException(OMRSAPIResponse              response,
                                 Exception                    error,
                                 String                       userId,
                                 String                       serverName,
                                 String                       methodName)
    {
        String  message = error.getMessage();

        if (message == null)
        {
            message = "null";
        }

        ExceptionMessageDefinition messageDefinition = OMRSErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               message);
        response.setRelatedHTTPCode(messageDefinition.getHttpErrorCode());
        response.setExceptionClassName(error.getClass().getName());
        response.setActionDescription(message);
        response.setExceptionErrorMessage(messageFormatter.getFormattedMessage(messageDefinition));
        response.setExceptionErrorMessageId(messageDefinition.getMessageId());
        response.setExceptionErrorMessageParameters(messageDefinition.getMessageParams());
        response.setExceptionSystemAction(messageDefinition.getSystemAction());
        response.setExceptionUserAction(messageDefinition.getUserAction());

        if (error.getCause() != null)
        {
            response.setExceptionCausedBy(error.getCause().getClass().getName());
        }

        response.setExceptionProperties(null);

        try
        {
            AuditLog auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OMRSAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                              methodName,
                                                                                              message),
                                      error);
            }
        }
        catch (Exception  secondError)
        {
            log.error("Unexpected exception processing error {}", error.toString(), secondError);
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
        response.setActionDescription(error.getReportingActionDescription());
        response.setExceptionErrorMessage(error.getReportedErrorMessage());
        response.setExceptionErrorMessageId(error.getReportedErrorMessageId());
        response.setExceptionErrorMessageParameters(error.getReportedErrorMessageParameters());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
        response.setExceptionProperties(exceptionProperties);
        response.setExceptionCausedBy(error.getReportedCaughtExceptionClassName());
    }
}
