/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.ffdc.exception;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;

import java.io.Serial;
import java.util.Map;

/**
 * OMRSCheckedExceptionBase provides a checked exception for reporting errors found when using the OMRS.
 * Typically these errors are either configuration or operational errors that can be fixed by an administrator
 * or power user.  However, there may be the odd bug that surfaces here. The OMRSErrorCode can be used with
 * this exception to populate it with standard messages.  Otherwise messages defined uniquely for a
 * ConnectorProvider/Connector implementation can be used.  The aim is to be able to uniquely identify the cause
 * and remedy for the error.
 */
public abstract class OMRSCheckedExceptionBase extends OMFCheckedExceptionBase
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     */
    public OMRSCheckedExceptionBase(ExceptionMessageDefinition messageDefinition,
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
    public OMRSCheckedExceptionBase(ExceptionMessageDefinition messageDefinition,
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
    public OMRSCheckedExceptionBase(ExceptionMessageDefinition messageDefinition,
                                    String                     className,
                                    String                     actionDescription,
                                    Exception                  caughtError)
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
    public OMRSCheckedExceptionBase(ExceptionMessageDefinition messageDefinition,
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
    public OMRSCheckedExceptionBase(int                 httpCode,
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
     * This is the copy/clone constructor used for creating an exception.
     *
     * @param errorMessage associated message
     * @param template   object to copy
     */
    public OMRSCheckedExceptionBase(String                  errorMessage,
                                    OMFCheckedExceptionBase template)
    {
        super(errorMessage, template);
    }


    /**
     * This is the typical constructor used for creating a OMRSCheckedExceptionBase.
     *
     * @param httpCode  http response code to use if this exception flows over a REST call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     */
    @Deprecated
    public OMRSCheckedExceptionBase(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the constructor used for creating a OMRSCheckedExceptionBase when an unexpected error has been caught.
     *
     * @param httpCode  http response code to use if this exception flows over a rest call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     * @param caughtError  previous error causing this exception
     */
    @Deprecated
    public OMRSCheckedExceptionBase(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Exception caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }
}
