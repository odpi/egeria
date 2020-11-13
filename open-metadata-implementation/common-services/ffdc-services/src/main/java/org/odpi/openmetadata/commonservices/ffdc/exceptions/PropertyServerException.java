/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.exceptions;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

import java.util.Map;

/**
 * The PropertyServerException is thrown by the OMAG Service when it is not able to communicate with the
 * property server.
 */
public class PropertyServerException extends org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException
{
    private static final long    serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition  content of the message
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
     * This is the typical constructor used for creating an exception.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
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
     * This is the constructor used for creating an exception when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     */
    public PropertyServerException(ExceptionMessageDefinition messageDefinition,
                                   String                     className,
                                   String                     actionDescription,
                                   Throwable                  caughtError)
    {
        super(messageDefinition, className, actionDescription, caughtError);
    }


    /**
     * This is the constructor used for creating an exception when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public PropertyServerException(ExceptionMessageDefinition messageDefinition,
                                   String                     className,
                                   String                     actionDescription,
                                   Throwable                  caughtError,
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
    }


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     */
    @Deprecated
    public PropertyServerException(int    httpCode,
                                   String className,
                                   String actionDescription,
                                   String errorMessage,
                                   String systemAction,
                                   String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param relatedProperties properties providing more information about the error
     */
    @Deprecated
    public PropertyServerException(int                 httpCode,
                                   String              className,
                                   String              actionDescription,
                                   String              errorMessage,
                                   String              systemAction,
                                   String              userAction,
                                   Map<String, Object> relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, relatedProperties);
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtError   the error that resulted in this exception.
     */
    @Deprecated
    public PropertyServerException(int       httpCode,
                                   String    className,
                                   String    actionDescription,
                                   String    errorMessage,
                                   String    systemAction,
                                   String    userAction,
                                   Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param httpCode   http response code to use if this exception flows over a rest call
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param errorMessage   description of error
     * @param systemAction   actions of the system as a result of the error
     * @param userAction   instructions for correcting the error
     * @param caughtError   the error that resulted in this exception.
     * @param relatedProperties properties providing more information about the error
     */
    @Deprecated
    public PropertyServerException(int                  httpCode,
                                   String               className,
                                   String               actionDescription,
                                   String               errorMessage,
                                   String               systemAction,
                                   String               userAction,
                                   Throwable            caughtError,
                                   Map<String, Object>  relatedProperties)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError, relatedProperties);
    }


    /**
     * This is the copy/clone constructor used for creating an exception.
     *
     * @param template   object to copy
     */
    public PropertyServerException(org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException template)
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
                ", errorMessage='" + getReportedErrorMessage() + '\'' +
                ", reportedSystemAction='" + getReportedSystemAction() + '\'' +
                ", reportedUserAction='" + getReportedUserAction() + '\'' +
                ", reportedCaughtException=" + getReportedCaughtException() +
                ", relatedProperties=" + getRelatedProperties() +
                '}';
    }
}
