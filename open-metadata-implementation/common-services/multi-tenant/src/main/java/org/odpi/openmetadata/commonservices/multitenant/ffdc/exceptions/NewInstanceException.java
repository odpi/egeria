/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions;


import org.odpi.openmetadata.commonservices.ffdc.exceptions.OMAGCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

import java.util.Map;

/**
 * NewInstanceException is thrown if the OMAG service is not able to register a new instance
 * of itself in its instance map.
 */
public class NewInstanceException extends OMAGCheckedExceptionBase
{
    private static final long    serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating an NewInstanceException.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     */
    public NewInstanceException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription)
    {
        super(messageDefinition, className, actionDescription);
    }


    /**
     * This is the typical constructor used for creating an NewInstanceException.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public NewInstanceException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription,
                                Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, relatedProperties);
    }


    /**
     * This is the constructor used for creating an NewInstanceException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     */
    public NewInstanceException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription,
                                Throwable                  caughtError)
    {
        super(messageDefinition, className, actionDescription, caughtError);
    }


    /**
     * This is the constructor used for creating an NewInstanceException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public NewInstanceException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription,
                                Throwable                  caughtError,
                                Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, caughtError, relatedProperties);
    }


    /**
     * This is the typical constructor used for creating a NewInstanceException.
     *
     * @param httpCode          http response code to use if this exception flows over a rest call
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage      description of error
     * @param systemAction      actions of the system as a result of the error
     * @param userAction        instructions for correcting the error
     */
    @Deprecated
    public NewInstanceException(int httpCode,
                                String className,
                                String actionDescription,
                                String errorMessage,
                                String systemAction,
                                String userAction)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction);
    }


    /**
     * This is the constructor used for creating a NewInstanceException that resulted from a previous error.
     *
     * @param httpCode          http response code to use if this exception flows over a rest call
     * @param className         name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param errorMessage      description of error
     * @param systemAction      actions of the system as a result of the error
     * @param userAction        instructions for correcting the error
     * @param caughtError       the error that resulted in this exception.
     */
    @Deprecated
    public NewInstanceException(int httpCode,
                                String className,
                                String actionDescription,
                                String errorMessage,
                                String systemAction,
                                String userAction,
                                Throwable caughtError)
    {
        super(httpCode, className, actionDescription, errorMessage, systemAction, userAction, caughtError);
    }
}
