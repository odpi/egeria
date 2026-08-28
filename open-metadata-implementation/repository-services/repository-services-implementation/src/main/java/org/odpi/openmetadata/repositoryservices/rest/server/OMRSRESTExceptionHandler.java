/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.rest.server;

import org.odpi.openmetadata.commonservices.ffdc.OMAGCommonAuditCode;
import org.odpi.openmetadata.commonservices.ffdc.OMAGCommonErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
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
            log.error("Unexpected exception processing error {}", error, secondError);
        }
    }






    /**
     * A runtime error occurred.
     *
     * @param response  REST Response
     * @param error returned response
     * @param methodName calling method
     * @param auditLog log location for recording an unexpected exception
     */
    public  void captureRuntimeExceptions(OMRSAPIResponse response,
                                          Throwable    error,
                                          String       methodName,
                                          AuditLog     auditLog)
    {
        if (error instanceof Exception exception)
        {
            /*
             * An exception is handleable ...
             */
            captureExceptions(response, exception, methodName, auditLog);
        }
        else
        {
            /*
             * An Error rather than an Exception.  This used to end the JVM with System.exit; it no longer
             * does, because the OMAG Server Platform is a shared resource and one request should not be able
             * to stop every server running on it.  Most Errors leave the JVM usable anyway - a
             * StackOverflowError unwinds one thread's stack, a LinkageError means one optional component is
             * not deployable - and even an OutOfMemoryError usually frees the memory it failed to allocate.
             * The failure is reported to the caller and recorded here instead.  See the equivalent handling
             * in RESTExceptionHandler.captureRuntimeExceptions.
             */
            log.error("Error from " + methodName + " returned to the caller; the platform continues", error);

            String message = error.getMessage();

            if (message == null)
            {
                message = error.getClass().getName();
            }

            ExceptionMessageDefinition messageDefinition = OMAGCommonErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                         methodName,
                                                                                                                         message);

            response.setRelatedHTTPCode(messageDefinition.getHttpErrorCode());
            response.setExceptionClassName(PropertyServerException.class.getName());
            response.setExceptionCausedBy(error.getClass().getName());
            response.setActionDescription(methodName);
            response.setExceptionErrorMessage(messageFormatter.getFormattedMessage(messageDefinition));
            response.setExceptionErrorMessageId(messageDefinition.getMessageId());
            response.setExceptionErrorMessageParameters(messageDefinition.getMessageParams());
            response.setExceptionSystemAction(messageDefinition.getSystemAction());
            response.setExceptionUserAction(messageDefinition.getUserAction());
            response.setExceptionProperties(null);

            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OMAGCommonAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(), methodName, message),
                                      error);
            }
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
    private void captureExceptions(OMRSAPIResponse response,
                                   Exception       error,
                                   String          methodName,
                                   AuditLog      auditLog)
    {
        log.error("Exception from " + methodName + " being packaged for return on REST call", error);

        if (error instanceof RepositoryErrorException)
        {
            captureRepositoryErrorException(response, (RepositoryErrorException)error);
        }
        else if (error instanceof UserNotAuthorizedException)
        {
            captureUserNotAuthorizedException(response, (UserNotAuthorizedException)error);
        }
        else if (error instanceof InvalidParameterException)
        {
            captureInvalidParameterException(response, (InvalidParameterException)error);
        }
        else
        {
            String message = error.getMessage();

            if (message == null)
            {
                message = "null";
            }

            ExceptionMessageDefinition messageDefinition = OMAGCommonErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                         methodName,
                                                                                                                         message);

            response.setRelatedHTTPCode(messageDefinition.getHttpErrorCode());
            response.setExceptionClassName(PropertyServerException.class.getName());
            response.setExceptionCausedBy(error.getClass().getName());
            response.setActionDescription(methodName);
            response.setExceptionErrorMessage(messageFormatter.getFormattedMessage(messageDefinition));
            response.setExceptionErrorMessageId(messageDefinition.getMessageId());
            response.setExceptionErrorMessageParameters(messageDefinition.getMessageParams());
            response.setExceptionSystemAction(messageDefinition.getSystemAction());
            response.setExceptionUserAction(messageDefinition.getUserAction());
            response.setExceptionProperties(null);


            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      OMAGCommonAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(), methodName, message),
                                      error);
            }
        }
    }



    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     * @param exceptionClassName class name of the exception to recreate
     */
    private void captureCheckedException(OMRSAPIResponse         response,
                                         OMFCheckedExceptionBase error,
                                         String                  exceptionClassName)
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
    private void captureCheckedException(OMRSAPIResponse         response,
                                         OMFCheckedExceptionBase error,
                                         String                  exceptionClassName,
                                         Map<String, Object>     exceptionProperties)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setActionDescription(error.getReportingActionDescription());
        response.setExceptionErrorMessage(error.getReportedErrorMessage());
        response.setExceptionErrorMessageId(error.getReportedErrorMessageId());
        response.setExceptionErrorMessageParameters(error.getReportedErrorMessageParameters());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
        response.setExceptionURL(error.getReportedURL());
        response.setExceptionProperties(exceptionProperties);
        response.setExceptionCausedBy(error.getReportedCaughtExceptionClassName());
    }
}
