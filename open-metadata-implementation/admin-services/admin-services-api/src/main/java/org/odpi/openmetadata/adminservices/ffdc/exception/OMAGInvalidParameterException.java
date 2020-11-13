/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.ffdc.exception;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;

import java.util.Map;

/**
 * OMAGInvalidParameterException is used when invalid parameters are passed on an OMAG call.
 */
public class OMAGInvalidParameterException extends OMAGCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;


    /**
     * This is the typical constructor used for creating an OCFCheckedException.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     */
    public OMAGInvalidParameterException(ExceptionMessageDefinition messageDefinition,
                                           String                     className,
                                           String                     actionDescription)
    {
        super(messageDefinition, className, actionDescription);
    }


    /**
     * This is the typical constructor used for creating an OCFCheckedException.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public OMAGInvalidParameterException(ExceptionMessageDefinition messageDefinition,
                                           String                     className,
                                           String                     actionDescription,
                                           Map<String, Object> relatedProperties)
    {
        super(messageDefinition, className, actionDescription, relatedProperties);
    }


    /**
     * This is the constructor used for creating an OCFCheckedException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     */
    public OMAGInvalidParameterException(ExceptionMessageDefinition messageDefinition,
                                           String                     className,
                                           String                     actionDescription,
                                           Throwable                  caughtError)
    {
        super(messageDefinition, className, actionDescription, caughtError);
    }


    /**
     * This is the constructor used for creating an OCFCheckedException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public OMAGInvalidParameterException(ExceptionMessageDefinition messageDefinition,
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
    public OMAGInvalidParameterException(int                 httpCode,
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
    public OMAGInvalidParameterException(String                  errorMessage,
                                         OCFCheckedExceptionBase template)
    {
        super(errorMessage, template);
    }


    /**
     * This is the typical constructor used for creating a OMAGInvalidParameterException.
     *
     * @param httpCode http response code to use if this exception flows over a REST call
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage description of error
     * @param systemAction actions of the system as a result of the error
     * @param userAction instructions for correcting the error
     */
    @Deprecated
    public OMAGInvalidParameterException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the constructor used for creating a OMAGInvalidParameterException that resulted from a previous error.
     *
     * @param httpCode http response code to use if this exception flows over a REST call
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage description of error
     * @param systemAction actions of the system as a result of the error
     * @param userAction instructions for correcting the error
     * @param caughtError the error that resulted in this exception.
     */
    @Deprecated
    public OMAGInvalidParameterException(int  httpCode, String className, String  actionDescription, String errorMessage, String systemAction, String userAction, Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }
}
