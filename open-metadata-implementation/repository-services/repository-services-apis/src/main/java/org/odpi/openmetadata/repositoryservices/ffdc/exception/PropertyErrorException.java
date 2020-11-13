/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.ffdc.exception;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

import java.util.Map;

/**
 * The PropertyErrorException is thrown by an OMRS Connector when the properties defined for a specific entity
 * or relationship instance do not match the TypeDefs for the metadata collection.
 */
public class PropertyErrorException extends OMRSCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     */
    public PropertyErrorException(ExceptionMessageDefinition messageDefinition,
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
    public PropertyErrorException(ExceptionMessageDefinition messageDefinition,
                                  String                     className,
                                  String                     actionDescription,
                                  Map<String, Object> relatedProperties)
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
    public PropertyErrorException(ExceptionMessageDefinition messageDefinition,
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
    public PropertyErrorException(ExceptionMessageDefinition messageDefinition,
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
    public PropertyErrorException(int                 httpCode,
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
     * This is the typical constructor used for creating a PropertyErrorException.
     *
     * @param httpCode  http response code to use if this exception flows over a REST call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     */
    @Deprecated
    public PropertyErrorException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the constructor used for creating a PropertyErrorException that resulted from a previous error.
     *
     * @param httpCode  http response code to use if this exception flows over a REST call
     * @param className  name of class reporting error
     * @param actionDescription  description of function it was performing when error detected
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param userAction  instructions for correcting the error
     * @param caughtError  the error that resulted in this exception.
     */
    @Deprecated
    public PropertyErrorException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }
}
