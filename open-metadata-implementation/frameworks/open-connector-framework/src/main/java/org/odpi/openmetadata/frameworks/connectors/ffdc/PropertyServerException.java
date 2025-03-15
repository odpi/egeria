/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.util.Arrays;
import java.util.Map;


/**
 * PropertyServerException provides a checked exception for reporting errors when connecting to a
 * metadata repository to retrieve properties about the connection and/or connector.
 * It may be a configuration error or temporary outage.  The parameters captured by the constructors
 * pinpoint the type an cause of the error.
 */
public class    PropertyServerException extends OCFCheckedExceptionBase
{
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(PropertyServerException.class);

    /**
     * This is the typical constructor for creating a PropertyServerException.  It captures the essential details
     * about the error, where it occurred and how to fix it.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     */
    public PropertyServerException(ExceptionMessageDefinition messageDefinition,
                                   String                     className,
                                   String                     actionDescription)
    {
        super(messageDefinition, className, actionDescription);
    }


    /**
     * This is the typical constructor for creating a PropertyServerException.  It captures the essential details
     * about the error, where it occurred and how to fix it.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public PropertyServerException(ExceptionMessageDefinition messageDefinition,
                                   String                     className,
                                   String                     actionDescription,
                                   Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, relatedProperties);
    }


    /**
     * This constructor is used when an unexpected exception has been caught that needs to be wrapped in a
     * PropertyServerException in order to add the essential details about the error, where it occurred and
     * how to fix it.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   the exception/error that caused this exception to be raised
     */
    public PropertyServerException(ExceptionMessageDefinition messageDefinition,
                                   String                     className,
                                   String                     actionDescription,
                                   Exception                  caughtError)
    {
        super(messageDefinition, className, actionDescription, caughtError);
    }


    /**
     * This constructor is used when an unexpected exception has been caught that needs to be wrapped in a
     * PropertyServerException in order to add the essential details about the error, where it occurred and
     * how to fix it.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   the exception/error that caused this exception to be raised
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public PropertyServerException(ExceptionMessageDefinition messageDefinition,
                                   String                     className,
                                   String                     actionDescription,
                                   Exception                  caughtError,
                                   Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, caughtError, relatedProperties);
    }


    /**
     * This is the constructor used when receiving an exception from a remote server.  The values are
     * stored directly in the response object and are passed explicitly to the new exception.
     * Notice that the technical aspects of the exception - such as class name creating the exception
     * are local values so that the implementation of the server is not exposed.
     *
     * @param httpCode   http response code to use if this exception flows over a REST call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param errorMessageId unique identifier for the message
     * @param errorMessageParameters parameters that were inserted in the message
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtErrorClassName   previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public PropertyServerException(int                 httpCode,
                                   String              className,
                                   String              actionDescription,
                                   String              errorMessage,
                                   String              errorMessageId,
                                   String[]            errorMessageParameters,
                                   String              systemAction,
                                   String              userAction,
                                   String              caughtErrorClassName,
                                   Map<String, Object> relatedProperties)
    {
        super(httpCode,
              className,
              actionDescription,
              errorMessage,
              errorMessageId,
              errorMessageParameters,
              systemAction,
              userAction,
              caughtErrorClassName,
              relatedProperties);

        log.debug("{}, {}, {}, {}", httpCode, className, actionDescription, caughtErrorClassName);
    }


    /**
     * This is the copy/clone constructor used for creating an exception.
     *
     * @param template   object to copy
     */
    public PropertyServerException(OCFCheckedExceptionBase template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "PropertyServerException{" +
                "reportedHTTPCode=" + getReportedHTTPCode() +
                ", reportingClassName='" + getReportingClassName() + '\'' +
                ", reportingActionDescription='" + getReportingActionDescription() + '\'' +
                ", reportedErrorMessage='" + getReportedErrorMessage() + '\'' +
                ", reportedErrorMessageId='" + getReportedErrorMessageId() + '\'' +
                ", reportedErrorMessageParameters=" + Arrays.toString(getReportedErrorMessageParameters()) +
                ", reportedSystemAction='" + getReportedSystemAction() + '\'' +
                ", reportedUserAction='" + getReportedUserAction() + '\'' +
                ", reportedCaughtException=" + getReportedCaughtException() +
                ", reportedCaughtExceptionClassName='" + getReportedCaughtExceptionClassName() + '\'' +
                ", relatedProperties=" + getRelatedProperties() +
                '}';
    }
}